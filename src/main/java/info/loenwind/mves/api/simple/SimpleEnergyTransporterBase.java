package info.loenwind.mves.api.simple;

import info.loenwind.mves.MvesMod;
import info.loenwind.mves.api.IEnergyAcceptor;
import info.loenwind.mves.api.IEnergyOffer;
import info.loenwind.mves.api.IEnergyStack;
import info.loenwind.mves.api.IEnergySupplier;
import info.loenwind.mves.api.IEnergyTransporter;
import info.loenwind.mves.api.IEnergyTransporterRelay;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * An abstract base class for energy transporters.
 * <p>
 * All kinds of goodies in here. You should try to use this as base class if you
 * roll your own transporter. Or at least have a good look at this code.
 *
 */
public abstract class SimpleEnergyTransporterBase implements IEnergyTransporter {

  /**
   * Entry point to transport from all sides to all sides.
   * <p>
   * Call one of the transport()s once per tick in your update().
   */
  public int transport(World world, BlockPos blockPos) {
    return transport(world, blockPos, EnumSet.allOf(EnumFacing.class), EnumSet.allOf(EnumFacing.class));
  }

  /**
   * Entry point to transport from the given input sides to the given output
   * sides.
   * <p>
   * Call one of the transport()s once per tick in your update().
   */
  public int transport(World world, BlockPos blockPos, EnumSet<EnumFacing> directionsIn, EnumSet<EnumFacing> directionsOut) {
    List<IEnergyStack> stacks = collect(world, blockPos, directionsIn);
    if (stacks.isEmpty()) {
      return 0;
    } else {
      return push(world, blockPos, directionsOut, createOffer(stacks));
    }
  }

  /**
   * Convert a list of energy stacks into an energy offer. Overwrite this to use
   * another offer class.
   */
  protected IEnergyOffer createOffer(List<IEnergyStack> stacks) {
    return new SimpleEnergyOffer(stacks);
  }

  /**
   * Collects energy stacks from the neighbors in the given directions.
   */
  protected List<IEnergyStack> collect(World world, BlockPos blockPos, EnumSet<EnumFacing> directions) {
    List<IEnergyStack> result = new ArrayList<>();
    for (EnumFacing direction : directions) {
      IEnergyStack energyStack = collectFromSupplier(world, blockPos.offset(direction), direction.getOpposite());
      if (energyStack != null) {
        result.add(energyStack);
      }
    }
    return result;
  }

  /**
   * Try to collect an energy stack from the given block. The BlockPos is that
   * of the block to collect from, the EnumFacing is relative to that block
   * (which means opposite to your side). Return null if no energy stack could
   * be collected, or if it would have been empty.
   * <p>
   * This checks:
   * <ul>
   * <li>That the block's chunk is loaded
   * <li>That the chunk is not empty (short-circuit)
   * <li>That there is a tile entity
   * <li>That the tile entity has a world object
   * <li>That it provides an energy supplier on the given side
   * <li>That an energy offer is returned
   * <li>That is contains energy
   * </ul>
   */
  protected IEnergyStack collectFromSupplier(World world, BlockPos blockPos, EnumFacing direction) {
    if (world.isBlockLoaded(blockPos, false)) {
      TileEntity tileEntity = world.getTileEntity(blockPos);
      if (tileEntity != null && tileEntity.hasWorldObj()) {
        IEnergySupplier energySupplier = tileEntity.getCapability(MvesMod.CAP_EnergySupplier, direction);
        if (energySupplier != null) {
          IEnergyStack energyStack = energySupplier.get();
          if (energyStack != null && energyStack.getStackSize() > 0) {
            return energyStack;
          }
        }
      }
    }
    return null;
  }

  /**
   * Pushes energy offers to energy acceptors and relays in the given
   * directions.
   */
  protected int push(World world, BlockPos blockPos, EnumSet<EnumFacing> directions, IEnergyOffer offer) {
    int result = 0;
    for (EnumFacing direction : directions) {
      result += offerToAcceptor(world, blockPos.offset(direction), direction.getOpposite(), offer);
      if (result >= offer.getLimit()) {
        return result;
      }
    }
    for (EnumFacing direction : directions) {
      result += offerToRelay(world, blockPos.offset(direction), direction.getOpposite(), offer);
      if (result >= offer.getLimit()) {
        return result;
      }
    }
    return result;
  }

  /**
   * Try to push an energy offer to the energy acceptor of the given block. The
   * BlockPos is that of the block to push to, the EnumFacing is relative to
   * that block (which means opposite to your side). Returns the amount of
   * energy that was pushed.
   * <p>
   * This checks:
   * <ul>
   * <li>That the block's chunk is loaded
   * <li>That the chunk is not empty (short-circuit)
   * <li>That there is a tile entity
   * <li>That the tile entity has a world object
   * <li>That it provides an energy acceptor on the given side
   * <li>That the acceptor doesn't take more energy than as offered
   * </ul>
   */
  protected int offerToAcceptor(World world, BlockPos blockPos, EnumFacing direction, IEnergyOffer offer) {
    if (world.isBlockLoaded(blockPos, false)) {
      TileEntity tileEntity = world.getTileEntity(blockPos);
      if (tileEntity != null && tileEntity.hasWorldObj()) {
        IEnergyAcceptor energyAcceptor = tileEntity.getCapability(MvesMod.CAP_EnergyAcceptor, direction);
        if (energyAcceptor != null) {
          int taken = energyAcceptor.offerEnergy(offer);
          if (taken > offer.getLimit()) {
            MvesMod.LOG.warn("Block at " + blockPos + " was offered " + offer.getLimit() + " but it took " + taken);
            explode(blockPos, direction);
          }
          return taken;
        }
      }
    }
    return 0;
  }

  /**
   * Try to push an energy offer to the energy relay of the given block. The
   * BlockPos is that of the block to push to, the EnumFacing is relative to
   * that block (which means opposite to your side). Returns the amount of
   * energy that was pushed.
   * <p>
   * This checks:
   * <ul>
   * <li>That the block's chunk is loaded
   * <li>That the chunk is not empty (short-circuit)
   * <li>That there is a tile entity
   * <li>That the tile entity has a world object
   * <li>That it provides an energy relay on the given side
   * <li>That the relay doesn't take more energy than as offered
   * </ul>
   */
  protected int offerToRelay(World world, BlockPos blockPos, EnumFacing direction, IEnergyOffer offer) {
    if (world.isBlockLoaded(blockPos, false)) {
      TileEntity tileEntity = world.getTileEntity(blockPos);
      if (tileEntity != null && tileEntity.hasWorldObj()) {
        IEnergyTransporter energyTransporter = tileEntity.getCapability(MvesMod.CAP_EnergyTransporter, direction);
        if (energyTransporter instanceof IEnergyTransporterRelay) {
          int taken = ((IEnergyTransporterRelay) energyTransporter).relayEnergy(offer);
          if (taken > offer.getLimit()) {
            explode(blockPos, direction);
          }
          return taken;
        }
      }
    }
    return 0;
  }

  /**
   * This is called when an acceptor or relay takes more energy than it was
   * offered.
   */
  protected abstract void explode(BlockPos offendingBlock, EnumFacing offendingDirection);

}
