package info.loenwind.mves.impl.wire;

import info.loenwind.mves.api.IEnergyStack;
import info.loenwind.mves.config.Config;

/**
 * This is an example of wrapping energy stacks.
 * <p>
 * It simulated an energy loss that starts after a certain amount of blocks,
 * which is determined by repeated wrapping.
 *
 */
public class LossyEnergyStack implements IEnergyStack {

  private final IEnergyStack parent;
  private final float loss;
  private final int gen;

  public LossyEnergyStack(IEnergyStack parent, float loss) {
    this.parent = parent;
    this.loss = loss;
    if (parent instanceof LossyEnergyStack) {
      this.gen = ((LossyEnergyStack) parent).gen + 1;
    } else {
      this.gen = 1;
    }
  }

  @Override
  public int getStackSize() {
    return gen > Config.rainbowWireLosslessDistance.getInt() ? (int) (parent.getStackSize() * (1f - loss)) : parent.getStackSize();
  }

  /**
   * We try to be nice here and adjust for rounding losses to deliver as close
   * to the requested amount as possible.
   */
  @Override
  public int extractEnergy(int amount) {
    int ourEnergy = getStackSize();
    int parentEnergy = parent.getStackSize();
    int toDeliver = Math.min(amount, ourEnergy);
    if (toDeliver == ourEnergy) {
      // shortcut for "everything"
      int extracted = parent.extractEnergy(parentEnergy);
      if (extracted == parentEnergy) {
        return toDeliver;
      } else {
        return (int) (extracted * (1f - loss));
      }
    } else {
      int toExtract = (int) (toDeliver / (1f - loss));
      int toReturn = (int) (toExtract * (1f - loss));
      while (toReturn < toDeliver && toExtract < parentEnergy) {
        toExtract++;
        toReturn = (int) (toExtract * (1f - loss));
      }
      int extracted = parent.extractEnergy(toExtract);
      if (extracted == toExtract) {
        return toReturn;
      } else {
        return (int) (extracted * (1f - loss));
      }
    }
  }

  @Override
  public Object getSource() {
    return parent.getSource();
  }

  @Override
  public boolean isStoredEnergy() {
    return parent.isStoredEnergy();
  }

}
