package info.loenwind.mves.api.simple;

import info.loenwind.mves.api.IEnergyOffer;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * A simple energy transporter that will transport energy from an internal
 * buffer to adjacent acceptors.
 * <p>
 * For many generators/batteries (energy suppliers) this is just what they need.
 * Call one of the two push() methods once per tick and you're done.
 * <p>
 * Note: Please expose this capability on your sides so adjacent machines can
 * know there's an energy source there. Some of them may want to render this in
 * their interface. On the other hand, no machine should rely on it.
 * <p>
 * Note 2: <b>Never</b> implement the opposite of this. Pullers are not valid.
 * Also, never push into relays as supplier. Both would create double-action
 * problems.
 *
 */
public class SimpleEnergyPusher extends SimpleEnergyTransporterBase {

  protected final SimpleEnergyBuffer buffer;
  protected final boolean isBattery;
  protected final int limit;

  public SimpleEnergyPusher(SimpleEnergyBuffer buffer, boolean isBattery) {
    this(buffer, isBattery, Integer.MAX_VALUE);
  }

  public SimpleEnergyPusher(SimpleEnergyBuffer buffer, boolean isBattery, int limit) {
    this.buffer = buffer;
    this.isBattery = isBattery;
    this.limit = limit;
  }

  public void push(World world, BlockPos blockPos) {
    push(world, blockPos, EnumSet.allOf(EnumFacing.class));
  }

  public int push(World world, BlockPos blockPos, EnumSet<EnumFacing> directions) {
    if (buffer.getAmount() <= 0) {
      return 0;
    }
    long max = buffer.getAmount();
    int maxi = Math.min(limit, (int) (max > Integer.MAX_VALUE ? Integer.MAX_VALUE : max));
    IEnergyOffer offer = new SimpleEnergyOffer((List) Collections.singletonList(new SimpleEnergyStack(buffer, isBattery)), maxi);
    return push(world, blockPos, directions, offer);
  }

  @Override
  protected final int offerToRelay(World world, BlockPos blockPos, EnumFacing direction, IEnergyOffer offer) {
    return 0;
  }

  @Override
  protected void explode(BlockPos offendingBlock, EnumFacing offendingDirection) {
  }

}
