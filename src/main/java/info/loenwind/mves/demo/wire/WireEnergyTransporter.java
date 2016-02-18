package info.loenwind.mves.demo.wire;

import info.loenwind.mves.api.IEnergyOffer;
import info.loenwind.mves.api.IEnergyStack;
import info.loenwind.mves.api.simple.SimpleEnergyOffer;
import info.loenwind.mves.api.simple.SimpleEnergyTransporter;
import info.loenwind.mves.config.Config;

import java.util.EnumSet;
import java.util.List;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * An example for an energy transporter that only sends energy to pre-determined
 * connected partners. Useful for a network.
 * <p>
 * Also an example of how to wrap energy offers for limiting and loss. Hint: If
 * you only want to limit, check the limit of the offer you get first. Maybe it
 * already is low enough so you don't need to wrap it.
 *
 */
public class WireEnergyTransporter extends SimpleEnergyTransporter {

  private final WireConnections connections;

  public WireEnergyTransporter(World world, BlockPos blockPos, WireConnections connections) {
    super(world, blockPos, EnumSet.allOf(EnumFacing.class));
    this.connections = connections;
  }

  /**
   * Offer we create ourself when pulling energy from suppliers.
   */
  protected IEnergyOffer createOffer(List<IEnergyStack> stacks) {
    return new LossyEnergyOffer(new SimpleEnergyOffer(stacks, Config.rainbowWireCapacity.getInt()), Config.rainbowWireLossPerBlock.getFloat());
  }

  @Override
  public int relayEnergy(IEnergyOffer offer) {
    if (offer.hasSeen(this)) {
      return 0;
    }
    offer.see(this);
    LossyEnergyOffer offer2 = new LossyEnergyOffer(offer, Config.rainbowWireLossPerBlock.getFloat());
    // Don't waste time on processing an offer that has been exhausted. Our LossyEnergyOffer reports the sum of its stacks,
    // so we can just check its total limit. Other offers might just report a fixed offer-limit, so you must check their stacks.
    if (offer2.getLimit() > 0) {
      return push(world, blockPos, directionsOut, offer2);
    } else {
      return 0;
    }
  }

  private int lastTick = -1;

  protected int push(World world, BlockPos blockPos, EnumSet<EnumFacing> directions, IEnergyOffer offer) {
    int pushed = push2(world, blockPos, directions, offer);
    if (pushed > 0 && lastTick < MinecraftServer.getServer().getTickCounter()) {
      lastTick = MinecraftServer.getServer().getTickCounter() + 5;
      world.addBlockEvent(blockPos, BlockMvesWire.block, BlockMvesWire.PARTICLES, 0);
    }
    return pushed;
  }

  protected int push2(World world, BlockPos blockPos, EnumSet<EnumFacing> directions, IEnergyOffer offer) {
    int result = 0;
    // acceptors first, then relays!
    for (EnumFacing direction : directions) {
      if (connections.is(direction, WireConnections.EnumConnection.ACC)) {
        result += offerToAcceptor(world, blockPos.offset(direction), direction.getOpposite(), offer);
        if (result >= offer.getLimit()) {
          return result;
        }
      }
    }
    for (EnumFacing direction : directions) {
      if (connections.is(direction, WireConnections.EnumConnection.TRP)) {
        result += offerToRelay(world, blockPos.offset(direction), direction.getOpposite(), offer);
        if (result >= offer.getLimit()) {
          return result;
        }
      }
      // ABOVE and BELOW are always mvesWires which are relays
      if (connections.is(direction, WireConnections.EnumConnection.ABOVE)) {
        result += offerToRelay(world, blockPos.offset(direction).up(), direction.getOpposite(), offer);
        if (result >= offer.getLimit()) {
          return result;
        }
      }
      if (connections.is(direction, WireConnections.EnumConnection.BELOW)) {
        result += offerToRelay(world, blockPos.offset(direction).down(), direction.getOpposite(), offer);
        if (result >= offer.getLimit()) {
          return result;
        }
      }
    }
    return result;
  }

}
