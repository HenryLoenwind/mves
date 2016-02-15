package info.loenwind.mves.api.simple;

import info.loenwind.mves.api.IEnergyOffer;
import info.loenwind.mves.api.IEnergyStack;

import java.util.List;

/**
 * A simple energy offer wrapper.
 * <p>
 * This wrapper allows you to reduce an offer's limit. It is meant to be used by
 * transporters that relay energy offers.
 * <p>
 * Please refrain from manipulation the limit of offers that you don't own.
 *
 */
public class SimpleEnergyOfferWrapper implements IEnergyOffer {

  private final IEnergyOffer offer;
  private int limit;

  public SimpleEnergyOfferWrapper(IEnergyOffer offer) {
    this(offer, offer.getLimit());
  }

  public SimpleEnergyOfferWrapper(IEnergyOffer offer, int limit) {
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
