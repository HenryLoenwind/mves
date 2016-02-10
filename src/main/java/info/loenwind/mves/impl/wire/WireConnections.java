package info.loenwind.mves.impl.wire;

import info.loenwind.mves.IEnergyAcceptor;
import info.loenwind.mves.IEnergySupplier;
import info.loenwind.mves.IEnergyTransporter;
import info.loenwind.mves.IEnergyTransporterRelay;
import info.loenwind.mves.MvesMod;

import java.util.Arrays;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

public class WireConnections {

  protected static enum EnumPosition {
    ADJ,
    BELOW,
    ABOVE
  }

  protected static enum EnumType {
    ACC,
    SUP,
    TRN,
    TRP
  }

  private static final int BITMASK = (0b1 << EnumType.values().length) - 1;

  private final long[] data = new long[EnumFacing.VALUES.length];

  private void set(EnumFacing direction, EnumPosition position, EnumType typ) {
    int pos = position.ordinal() * EnumType.values().length + typ.ordinal();
    data[direction.ordinal()] |= 1 << pos;
  }

  public boolean is(EnumFacing direction, EnumPosition position, EnumType typ) {
    int pos = position.ordinal() * EnumType.values().length + typ.ordinal();
    return (data[direction.ordinal()] & (1 << pos)) != 0;
  }

  public boolean is(EnumFacing direction, EnumPosition position) {
    int pos = position.ordinal() * EnumType.values().length;
    return (data[direction.ordinal()] & (BITMASK << pos)) != 0;
  }

  public WireConnections(IBlockAccess worldIn, BlockPos pos) {
    for (EnumFacing direction : EnumFacing.Plane.HORIZONTAL) {
      findConnection(worldIn, pos, direction);
    }
    findConnection(worldIn, pos.offset(EnumFacing.DOWN), EnumFacing.DOWN, EnumPosition.ADJ);
  }

  private void findConnection(IBlockAccess worldIn, BlockPos pos, EnumFacing direction) {
    BlockPos blockPosTarget = pos.offset(direction);
    BlockPos blockPosAboveUs = pos.offset(EnumFacing.UP);
    BlockPos blockPosBelowUs = pos.offset(EnumFacing.DOWN);
    BlockPos blockPosAboveTarget = blockPosTarget.offset(EnumFacing.UP);
    BlockPos blockPosBelowTarget = blockPosTarget.offset(EnumFacing.DOWN);

    if (worldIn.getBlockState(blockPosTarget).getBlock() == BlockMvesWire.block) {
      set(direction, EnumPosition.ADJ, EnumType.TRP);
    } else {
      findConnection(worldIn, blockPosTarget, direction, EnumPosition.ADJ);
    }
    if (worldIn.getBlockState(blockPosAboveTarget).getBlock() == BlockMvesWire.block && worldIn.isAirBlock(blockPosAboveUs)
        && worldIn.getBlockState(blockPosTarget).getBlock().isSideSolid(worldIn, blockPosTarget, direction.getOpposite())) {
      //        && worldIn.isSideSolid(blockPosTarget, direction.getOpposite(), false)) {
      set(direction, EnumPosition.ABOVE, EnumType.TRP);
    }
    if (worldIn.getBlockState(blockPosBelowTarget).getBlock() == BlockMvesWire.block && worldIn.isAirBlock(blockPosTarget)
        && worldIn.getBlockState(blockPosBelowUs).getBlock().isSideSolid(worldIn, blockPosBelowUs, direction)) {
      set(direction, EnumPosition.BELOW, EnumType.TRP);
    }
  }

  private void findConnection(IBlockAccess world, BlockPos blockPosTarget, EnumFacing direction, EnumPosition position) {
    TileEntity tileEntity = world.getTileEntity(blockPosTarget);
    if (tileEntity != null) {
      IEnergySupplier energySupplier = tileEntity.getCapability(MvesMod.CAP_EnergySupplier, direction.getOpposite());
      if (energySupplier != null) {
        set(direction, position, EnumType.SUP);
      }
      IEnergyAcceptor energyAcceptor = tileEntity.getCapability(MvesMod.CAP_EnergyAcceptor, direction.getOpposite());
      if (energyAcceptor != null) {
        set(direction, position, EnumType.ACC);
      }
      IEnergyTransporter energyTransporter = tileEntity.getCapability(MvesMod.CAP_EnergyTransporter, direction.getOpposite());
      if (energyTransporter instanceof IEnergyTransporterRelay) {
        set(direction, position, EnumType.TRP);
      } else if (energyTransporter != null) {
        set(direction, position, EnumType.TRN);
      }
    }
  }

  @Override
  public String toString() {
    return "WireConnections [data=" + Arrays.toString(data) + "]";
  }

}
