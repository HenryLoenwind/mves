package info.loenwind.mves.impl.wire;

import info.loenwind.mves.IEnergyAcceptor;
import info.loenwind.mves.IEnergySupplier;
import info.loenwind.mves.IEnergyTransporter;
import info.loenwind.mves.IEnergyTransporterRelay;
import info.loenwind.mves.MvesMod;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

public class WireConnections {

  protected static enum EnumConnection {
    ACC(0b00000001),
    SUP(0b00000010),
    TRP(0b00000100),
    BELOW(0b00001000),
    ABOVE(0b00010000),
    _ANY(0b00011111),
    _ANY_LEVEL(0b00001111);

    private final int bitmask;

    private EnumConnection(int bitmask) {
      this.bitmask = bitmask;
    }

    protected int getBitmask(EnumFacing direction) {
      // D-U-N-S-W-E
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
      IEnergySupplier energySupplier = tileEntity.getCapability(MvesMod.CAP_EnergySupplier, direction.getOpposite());
      if (energySupplier != null) {
        set(direction, EnumConnection.SUP);
      }
      IEnergyAcceptor energyAcceptor = tileEntity.getCapability(MvesMod.CAP_EnergyAcceptor, direction.getOpposite());
      if (energyAcceptor != null) {
        set(direction, EnumConnection.ACC);
      }
      IEnergyTransporter energyTransporter = tileEntity.getCapability(MvesMod.CAP_EnergyTransporter, direction.getOpposite());
      if (energyTransporter instanceof IEnergyTransporterRelay) {
        set(direction, EnumConnection.TRP);
      }
    }
  }

  @Override
  public String toString() {
    return "WireConnections [data=" + Integer.toBinaryString(data) + "]";
  }

}
