package info.loenwind.mves.impl.wire;

import info.loenwind.mves.IEnergyOffer;
import info.loenwind.mves.IEnergyTransporterRelay;
import info.loenwind.mves.impl.SimpleEnergyTransporter;

import java.util.EnumSet;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class WireEnergyTransporter extends SimpleEnergyTransporter implements IEnergyTransporterRelay {

  private static final float LOSS = 1f / 3f;

  private final WireConnections connections;

  public WireEnergyTransporter(World world, BlockPos blockPos, WireConnections connections) {
    super(world, blockPos, EnumSet.allOf(EnumFacing.class));
    this.connections = connections;
  }

  @Override
  public int relayEnergy(IEnergyOffer offer) {
    if (offer.hasSeen(this)) {
      return 0;
    }
    offer.see(this);
    LossyEnergyOffer offer2 = new LossyEnergyOffer(offer, LOSS);
    if (offer2.getLimit() > 0) {
      return push(world, blockPos, directionsOut, offer2);
    } else {
      return 0;
    }
  }

  protected int push(World world, BlockPos blockPos, EnumSet<EnumFacing> directions, IEnergyOffer offer) {
    int result = 0;
    for (EnumFacing direction : directions) {
      if (connections.is(direction, WireConnections.EnumConnection.ACC)) {
        result += offerToAcceptor(world, blockPos.offset(direction), direction, offer);
        if (result >= offer.getLimit()) {
          return result;
        }
      }
    }
    for (EnumFacing direction : directions) {
      if (connections.is(direction, WireConnections.EnumConnection.TRP)) {
        result += offerToRelay(world, blockPos.offset(direction), direction, offer);
        if (result >= offer.getLimit()) {
          return result;
        }
      }
      if (connections.is(direction, WireConnections.EnumConnection.ABOVE)) {
        result += offerToRelay(world, blockPos.offset(direction).up(), direction, offer);
        if (result >= offer.getLimit()) {
          return result;
        }
      }
      if (connections.is(direction, WireConnections.EnumConnection.BELOW)) {
        result += offerToRelay(world, blockPos.offset(direction).down(), direction, offer);
        if (result >= offer.getLimit()) {
          return result;
        }
      }
    }
    return result;
  }

}