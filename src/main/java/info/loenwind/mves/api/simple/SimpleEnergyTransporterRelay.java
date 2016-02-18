package info.loenwind.mves.api.simple;

import info.loenwind.mves.api.IEnergyOffer;

import java.util.EnumSet;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * A simple energy transporter relay.
 * <p>
 * You'd probably want to subclass this, not use it as is.
 *
 */
public class SimpleEnergyTransporterRelay extends SimpleEnergyTransporter {

  public SimpleEnergyTransporterRelay(World world, BlockPos blockPos) {
    super(world, blockPos, EnumSet.allOf(EnumFacing.class));
  }

  public SimpleEnergyTransporterRelay(World world, BlockPos blockPos, EnumSet<EnumFacing> directions) {
    super(world, blockPos, directions, directions);
  }

  public SimpleEnergyTransporterRelay(World world, BlockPos blockPos, EnumSet<EnumFacing> directionsIn, EnumSet<EnumFacing> directionsOut) {
    super(world, blockPos, directionsIn, directionsOut);
  }

  @Override
  public int relayEnergy(IEnergyOffer offer) {
    if (offer.hasSeen(this)) {
      return 0;
    }
    offer.see(this);
    return push(world, blockPos, directionsOut, offer);
  }

}
