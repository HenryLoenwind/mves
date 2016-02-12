package info.loenwind.mves.api.simple;

import info.loenwind.mves.api.IEnergyAcceptor;
import info.loenwind.mves.api.IEnergyOffer;
import info.loenwind.mves.api.IEnergyStack;

import java.util.Iterator;

/**
 * A simple energy acceptor that takes as much as it can put into the
 * SimpleEnergyBuffer it is connected to.
 * <p>
 * For most machines this is exactly what they need, so just use this class.
 * <p>
 * Machines that can switch between being a battery and being a generator can
 * also use this class. They should use two instances and decide in
 * getCapability() which one to hand out.
 *
 */
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
