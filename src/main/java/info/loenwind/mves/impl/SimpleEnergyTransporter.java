package info.loenwind.mves.impl;

import info.loenwind.mves.IEnergyAcceptor;
import info.loenwind.mves.IEnergyOffer;
import info.loenwind.mves.IEnergyTransporter;
import info.loenwind.mves.IEnergyTransporterRelay;
import info.loenwind.mves.MvesMod;

import java.util.EnumSet;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SimpleEnergyTransporter implements IEnergyTransporter {

  public SimpleEnergyTransporter() {
  }

  protected int push(World world, BlockPos blockPos, EnumSet<EnumFacing> directions, IEnergyOffer offer) {
    int result = 0;
    for (EnumFacing direction : directions) {
      BlockPos blockPos2 = blockPos.offset(direction);
      if (world.isBlockLoaded(blockPos2)) {
        TileEntity tileEntity = world.getTileEntity(blockPos2);
        if (tileEntity != null) {
          IEnergyAcceptor energyAcceptor = tileEntity.getCapability(MvesMod.CAP_EnergyAcceptor, direction.getOpposite());
          if (energyAcceptor != null) {
            result += energyAcceptor.offerEnergy(offer);
            if (result >= offer.getLimit()) {
              return result;
            }
          }
        }
      }
    }
    for (EnumFacing direction : directions) {
      BlockPos blockPos2 = blockPos.offset(direction);
      if (world.isBlockLoaded(blockPos2)) {
        TileEntity tileEntity = world.getTileEntity(blockPos2);
        if (tileEntity != null) {
          IEnergyTransporter energyTransporter = tileEntity.getCapability(MvesMod.CAP_EnergyTransporter, direction.getOpposite());
          if (energyTransporter instanceof IEnergyTransporterRelay) {
            result += ((IEnergyTransporterRelay) energyTransporter).relayEnergy(offer);
            if (result >= offer.getLimit()) {
              return result;
            }
          }
        }
      }
    }
    return result;
  }

}
