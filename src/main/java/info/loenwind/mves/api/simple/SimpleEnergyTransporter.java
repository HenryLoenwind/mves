package info.loenwind.mves.api.simple;

import java.util.EnumSet;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * A simple energy transporter.
 * <p>
 * Not much to see here, all the juicy parts are in the base class.
 * <p>
 * BTW: Do you really want to implement a transporter without relay?
 *
 */
public class SimpleEnergyTransporter extends SimpleEnergyTransporterBase {

  protected final World world;
  protected final BlockPos blockPos;
  protected final EnumSet<EnumFacing> directionsIn;
  protected final EnumSet<EnumFacing> directionsOut;

  public SimpleEnergyTransporter(World world, BlockPos blockPos) {
    this(world, blockPos, EnumSet.allOf(EnumFacing.class));
  }

  public SimpleEnergyTransporter(World world, BlockPos blockPos, EnumSet<EnumFacing> directions) {
    this(world, blockPos, directions, directions);
  }

  public SimpleEnergyTransporter(World world, BlockPos blockPos, EnumSet<EnumFacing> directionsIn, EnumSet<EnumFacing> directionsOut) {
    this.world = world;
    this.blockPos = blockPos;
    this.directionsIn = directionsIn;
    this.directionsOut = directionsOut;
  }

  public int transport() {
    return transport(world, blockPos, directionsIn, directionsOut);
  }

}
