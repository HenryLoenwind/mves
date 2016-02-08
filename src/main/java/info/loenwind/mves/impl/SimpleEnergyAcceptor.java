package info.loenwind.mves.impl;

import info.loenwind.mves.IEnergyAcceptor;
import info.loenwind.mves.IEnergyOffer;
import info.loenwind.mves.IEnergyStack;

import java.util.Iterator;

public class SimpleEnergyAcceptor implements IEnergyAcceptor {

  private final SimpleEnergyBuffer buffer;
  private final boolean isBattery;

  public SimpleEnergyAcceptor(SimpleEnergyBuffer buffer, boolean isBattery) {
    this.buffer = buffer;
    this.isBattery = isBattery;
  }

  @Override
  public int offerEnergy(IEnergyOffer offer) {
    long max = buffer.getMaxAmount() - buffer.getAmount();
    if (max <= 0) {
      return 0;
    }
    int maxi = (int) (max > Integer.MAX_VALUE ? Integer.MAX_VALUE : max);
    if (offer.getLimit() < maxi) {
      maxi = offer.getLimit();
    }
    int used = 0;
    Iterator<IEnergyStack> iterator = offer.getStacks().iterator();
    while (used < max && iterator.hasNext()) {
      IEnergyStack next = iterator.next();
      if (next != null && (!isBattery || !next.isStoredEnergy()) && next.getStackSize() > 0) {
        int extractEnergy = next.extractEnergy(maxi - used);
        if (extractEnergy > 0) {
          used += extractEnergy;
          buffer.setAmount(buffer.getAmount() + extractEnergy);
        }
      }
    }
    return used;
  }

}
