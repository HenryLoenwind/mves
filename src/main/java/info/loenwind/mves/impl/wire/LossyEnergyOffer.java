package info.loenwind.mves.impl.wire;

import info.loenwind.mves.IEnergyOffer;
import info.loenwind.mves.IEnergyStack;

import java.util.ArrayList;
import java.util.List;

public class LossyEnergyOffer implements IEnergyOffer {

  private final IEnergyOffer parent;
  private final List<IEnergyStack> stacks = new ArrayList<>();
  private int limit = 0;

  public LossyEnergyOffer(IEnergyOffer parent, float loss) {
    this.parent = parent;
    for (IEnergyStack stack : parent.getStacks()) {
      LossyEnergyStack stack2 = new LossyEnergyStack(stack, loss);
      int stackSize = stack2.getStackSize();
      if (stackSize > 0) {
        stacks.add(stack2);
      }
    }
    updateLimit();
  }

  @Override
  public List<IEnergyStack> getStacks() {
    return stacks;
  }

  @Override
  public boolean hasSeen(Object o) {
    return parent.hasSeen(o);
  }

  @Override
  public void see(Object o) {
    parent.see(o);
  }

  @Override
  public int getLimit() {
    return limit;
  }

  public void updateLimit() {
    limit = 0;
    for (IEnergyStack stack : stacks) {
      limit += stack.getStackSize();
    }
    limit = Math.max(0, Math.min(limit, parent.getLimit()));
  }
}
