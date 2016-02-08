package info.loenwind.mves.impl;

import info.loenwind.mves.IEnergyOffer;
import info.loenwind.mves.IEnergyTransporterRelay;

import java.util.EnumSet;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SimpleEnergyTransporterRelay extends SimpleEnergyTransporter implements IEnergyTransporterRelay {

  private final World world;
  private final BlockPos blockPos;
  private final EnumSet<EnumFacing> directions;

  public SimpleEnergyTransporterRelay(World world, BlockPos blockPos, EnumSet<EnumFacing> directions) {
    this.world = world;
    this.blockPos = blockPos;
    this.directions = directions;
  }

  @Override
  public int relayEnergy(IEnergyOffer offer) {
    if (offer.hasSeen(this)) {
      return 0;
    }
    offer.see(this);
    return push(world, blockPos, directions, offer);
  }

}
