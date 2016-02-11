package info.loenwind.mves.impl;

import info.loenwind.mves.IEnergyAcceptor;
import info.loenwind.mves.IEnergyOffer;
import info.loenwind.mves.IEnergyStack;
import info.loenwind.mves.IEnergySupplier;
import info.loenwind.mves.IEnergyTransporter;
import info.loenwind.mves.IEnergyTransporterRelay;
import info.loenwind.mves.MvesMod;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public abstract class SimpleEnergyTransporterBase implements IEnergyTransporter {

  public int transport(World world, BlockPos blockPos) {
    return transport(world, blockPos, EnumSet.allOf(EnumFacing.class), EnumSet.allOf(EnumFacing.class));
  }

  public int transport(World world, BlockPos blockPos, EnumSet<EnumFacing> directionsIn, EnumSet<EnumFacing> directionsOut) {
    List<IEnergyStack> stacks = collect(world, blockPos, directionsIn);
    if (stacks.isEmpty()) {
      return 0;
    } else {
      return push(world, blockPos, directionsOut, createOffer(stacks));
    }
  }

  protected IEnergyOffer createOffer(List<IEnergyStack> stacks) {
    return new SimpleEnergyOffer(stacks);
  }

  protected List<IEnergyStack> collect(World world, BlockPos blockPos, EnumSet<EnumFacing> directions) {
    List<IEnergyStack> result = new ArrayList<>();
    for (EnumFacing direction : directions) {
      BlockPos blockPos2 = blockPos.offset(direction);
      if (world.isBlockLoaded(blockPos2)) {
        TileEntity tileEntity = world.getTileEntity(blockPos2);
        if (tileEntity != null) {
          IEnergySupplier energySupplier = tileEntity.getCapability(MvesMod.CAP_EnergySupplier, direction.getOpposite());
          if (energySupplier != null) {
            IEnergyStack energyStack = energySupplier.get();
            if (energyStack != null) {
              result.add(energyStack);
            }
          }
        }
      }
    }
    return result;
  }

  protected int push(World world, BlockPos blockPos, EnumSet<EnumFacing> directions, IEnergyOffer offer) {
    int result = 0;
    for (EnumFacing direction : directions) {
      result += offerToAcceptor(world, blockPos.offset(direction), direction, offer);
      if (result >= offer.getLimit()) {
        return result;
      }
    }
    for (EnumFacing direction : directions) {
      result += offerToRelay(world, blockPos.offset(direction), direction, offer);
      if (result >= offer.getLimit()) {
        return result;
      }
    }
    return result;
  }

  protected int offerToAcceptor(World world, BlockPos blockPos, EnumFacing direction, IEnergyOffer offer) {
    if (world.isBlockLoaded(blockPos)) {
      TileEntity tileEntity = world.getTileEntity(blockPos);
      if (tileEntity != null) {
        IEnergyAcceptor energyAcceptor = tileEntity.getCapability(MvesMod.CAP_EnergyAcceptor, direction.getOpposite());
        if (energyAcceptor != null) {
          return energyAcceptor.offerEnergy(offer);
        }
      }
    }
    return 0;
  }

  protected int offerToRelay(World world, BlockPos blockPos, EnumFacing direction, IEnergyOffer offer) {
    if (world.isBlockLoaded(blockPos)) {
      TileEntity tileEntity = world.getTileEntity(blockPos);
      if (tileEntity != null) {
        IEnergyTransporter energyTransporter = tileEntity.getCapability(MvesMod.CAP_EnergyTransporter, direction.getOpposite());
        if (energyTransporter instanceof IEnergyTransporterRelay) {
          return ((IEnergyTransporterRelay) energyTransporter).relayEnergy(offer);
        }
      }
    }
    return 0;
  }

}
