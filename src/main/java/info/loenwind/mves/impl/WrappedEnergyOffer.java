package info.loenwind.mves.impl;

import info.loenwind.mves.IEnergyOffer;
import info.loenwind.mves.IEnergyStack;

import java.util.List;

public class WrappedEnergyOffer implements IEnergyOffer {

  private final IEnergyOffer offer;
  private int limit;

  public WrappedEnergyOffer(IEnergyOffer offer) {
    this(offer, offer.getLimit());
  }

  public WrappedEnergyOffer(IEnergyOffer offer, int limit) {
    this.offer = offer;
    if (offer.getLimit() < limit) {
      this.limit = offer.getLimit();
    } else {
      this.limit = limit;
    }
  }

  @Override
  public List<IEnergyStack> getStacks() {
    return offer.getStacks();
  }

  @Override
  public boolean hasSeen(Object o) {
    return offer.hasSeen(o);
  }

  @Override
  public void see(Object o) {
    offer.see(o);
  }

  @Override
  public int getLimit() {
    return limit;
  }

  public void setLimit(int limit) {
    if (limit > offer.getLimit()) {
      this.limit = offer.getLimit();
    } else {
      this.limit = limit;
    }
  }

  public void reduceLimit(int amount) {
    if (amount < limit) {
      limit = limit - amount;
    } else {
      limit = 0;
    }
  }

}
