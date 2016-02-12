package info.loenwind.mves.demo.wire;

import info.loenwind.mves.MvesMod;
import info.loenwind.mves.api.IEnergyTransporter;
import info.loenwind.mves.api.IEnergyTransporterRelay;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

/**
 * This is an example of an energy network. A very simple one with just one
 * block, so it only does half of what a network need to do: It creates a map of
 * all outside connections. A network that checks all its neighbors on every
 * tick would be a nice source of lag. So it needs to map them when there's a
 * change.
 * <p>
 * I bit-bang that information into a single int because Minecraft has a nice
 * built-in way to send single ints from the server to the client. And because I
 * can.
 *
 */
public class WireConnections {

  protected static enum EnumConnection {
    ACC(0b00000001), // adjacent block is an acceptor
    SUP(0b00000010), // adjacent block is a supplier
    TRP(0b00000100), // adjacent block is a transporter relay
    BELOW(0b00001000), // adjacent block is air, block below that is wire
    ABOVE(0b00010000), // block above us is air, block above the adjacent one is wire
    _ANY(0b00011111); // there's any kind of connection (only used to query)

    private final int bitmask;

    private EnumConnection(int bitmask) {
      this.bitmask = bitmask;
    }

    protected int getBitmask(EnumFacing direction) {
      // EnumFacing is D-U-N-S-W-E, bitmask is E-W-S-N-D with 2 unused bits in the front
      if (direction == EnumFacing.DOWN) {
        return bitmask;
      } else if (direction == EnumFacing.UP) {
        return 0;
      } else {
        return bitmask << ((direction.ordinal() - 1) * 5);
      }
    }
  }

  public static final WireConnections NONE = new WireConnections(0);

  private int data = 0;

  private void set(EnumFacing direction, EnumConnection conn) {
    data |= conn.getBitmask(direction);
  }

  public boolean is(EnumFacing direction, EnumConnection conn) {
    return (data & conn.getBitmask(direction)) != 0;
  }

  public WireConnections(int data) {
    this.data = data;
  }

  public int getData() {
    return data;
  }

  public WireConnections(IBlockAccess worldIn, BlockPos pos) {
    for (EnumFacing direction : EnumFacing.Plane.HORIZONTAL) {
      findConnections(worldIn, pos, direction);
    }
    findConnection(worldIn, pos.offset(EnumFacing.DOWN), EnumFacing.DOWN);
  }

  private void findConnections(IBlockAccess worldIn, BlockPos pos, EnumFacing direction) {
    BlockPos blockPosTarget = pos.offset(direction);
    BlockPos blockPosAboveUs = pos.offset(EnumFacing.UP);
    BlockPos blockPosBelowUs = pos.offset(EnumFacing.DOWN);
    BlockPos blockPosAboveTarget = blockPosTarget.offset(EnumFacing.UP);
    BlockPos blockPosBelowTarget = blockPosTarget.offset(EnumFacing.DOWN);

    if (worldIn.getBlockState(blockPosTarget).getBlock() == BlockMvesWire.block) {
      set(direction, EnumConnection.TRP);
    } else {
      findConnection(worldIn, blockPosTarget, direction);
    }
    if (worldIn.getBlockState(blockPosAboveTarget).getBlock() == BlockMvesWire.block && worldIn.isAirBlock(blockPosAboveUs)
        && worldIn.getBlockState(blockPosTarget).getBlock().isSideSolid(worldIn, blockPosTarget, direction.getOpposite())) {
      // doesn't work in some cases, see https://github.com/MinecraftForge/MinecraftForge/issues/2451
      //        && worldIn.isSideSolid(blockPosTarget, direction.getOpposite(), false)) {
      set(direction, EnumConnection.ABOVE);
    }
    if (worldIn.getBlockState(blockPosBelowTarget).getBlock() == BlockMvesWire.block && worldIn.isAirBlock(blockPosTarget)
        && worldIn.getBlockState(blockPosBelowUs).getBlock().isSideSolid(worldIn, blockPosBelowUs, direction)) {
      set(direction, EnumConnection.BELOW);
    }
  }

  private void findConnection(IBlockAccess world, BlockPos blockPosTarget, EnumFacing direction) {
    TileEntity tileEntity = world.getTileEntity(blockPosTarget);
    if (tileEntity != null && tileEntity.hasWorldObj()) {
      if (tileEntity.hasCapability(MvesMod.CAP_EnergySupplier, direction.getOpposite())) {
        set(direction, EnumConnection.SUP);
      }
      if (tileEntity.hasCapability(MvesMod.CAP_EnergyAcceptor, direction.getOpposite())) {
        set(direction, EnumConnection.ACC);
      }
      IEnergyTransporter energyTransporter = tileEntity.getCapability(MvesMod.CAP_EnergyTransporter, direction.getOpposite());
      if (energyTransporter instanceof IEnergyTransporterRelay) {
        set(direction, EnumConnection.TRP);
      }
      // non-relays are useless to us. We cannot send them energy, so we neither try to nor render a connection to them. 
    }
  }

  @Override
  public String toString() {
    String s = Integer.toBinaryString(data);
    for (int i = 0; i <= 5; i++) {
      s = s.replaceAll("(\\d)(\\d\\d\\d\\d\\d(\\.|$))", "$1.$2");
    }
    return "[" + s + "]";
  }

}
