package info.loenwind.mves.impl;

import info.loenwind.mves.IEnergyOffer;
import info.loenwind.mves.IEnergyStack;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SimpleEnergyOffer implements IEnergyOffer {

  private final List<IEnergyStack> stacks;
  private Set<Object> seen = null;
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
