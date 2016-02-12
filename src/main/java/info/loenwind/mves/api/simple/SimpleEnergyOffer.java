package info.loenwind.mves.api.simple;

import info.loenwind.mves.api.IEnergyOffer;
import info.loenwind.mves.api.IEnergyStack;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A simple energy offer.
 * <p>
 * It also supports manipulation of the limit. However, please refrain from
 * manipulation the limit of offers that you don't own.
 * <p>
 * If you don't need to do funny things, use this. If you need to do funny
 * things, have a look at the LossyEnergyOffer.
 * <p>
 * Note: This offer does not calculate the limit from its stacks. That's on
 * purpose, so people don't even start to misuse the getLimit(). See
 * WireEnergyTransporter for more info on this topic.
 *
 */
public class SimpleEnergyOffer implements IEnergyOffer {

  private final List<IEnergyStack> stacks;
  private Set<Object> seen = null; // lazy field, it's only needed when relaying
  private int limit;

  public SimpleEnergyOffer(List<IEnergyStack> stacks) {
    this(stacks, Integer.MAX_VALUE);
  }

  public SimpleEnergyOffer(List<IEnergyStack> stacks, int limit) {
    this.stacks = stacks;
    this.limit = limit;
  }

  @Override
  public List<IEnergyStack> getStacks() {
    return stacks;
  }

  @Override
  public boolean hasSeen(Object o) {
    return seen == null ? false : seen.contains(o);
  }

  @Override
  public void see(Object o) {
    if (seen == null) {
      seen = new HashSet<Object>();
    }
    seen.add(o);
  }

  @Override
  public int getLimit() {
    return limit;
  }

  public void setLimit(int limit) {
    this.limit = limit;
  }

  public void reduceLimit(int amount) {
    if (amount < limit) {
      limit = limit - amount;
    } else {
      limit = 0;
    }
  }

}
