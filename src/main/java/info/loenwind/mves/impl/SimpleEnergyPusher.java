package info.loenwind.mves.impl;

import info.loenwind.mves.IEnergyOffer;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SimpleEnergyPusher extends SimpleEnergyTransporterBase {

  protected final SimpleEnergyBuffer buffer;
  protected final boolean isBattery;

  public SimpleEnergyPusher(SimpleEnergyBuffer buffer, boolean isBattery) {
    this.buffer = buffer;
    this.isBattery = isBattery;
  }

  public void push(World world, BlockPos blockPos) {
    push(world, blockPos, EnumSet.allOf(EnumFacing.class));
  }

  public int push(World world, BlockPos blockPos, EnumSet<EnumFacing> directions) {
    if (buffer.getAmount() <= 0) {
      return 0;
    }
    long max = buffer.getAmount();
    int maxi = (int) (max > Integer.MAX_VALUE ? Integer.MAX_VALUE : max);
    IEnergyOffer offer = new SimpleEnergyOffer((List) Collections.singletonList(new SimpleEnergyStack(buffer, isBattery)), maxi);
    return push(world, blockPos, directions, offer);
  }

}
