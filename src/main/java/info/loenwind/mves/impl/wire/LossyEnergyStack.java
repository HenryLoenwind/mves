package info.loenwind.mves.impl.wire;

import info.loenwind.mves.IEnergyStack;
import info.loenwind.mves.config.Config;

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

  @Override
  public int extractEnergy(int amount) {
    return parent.extractEnergy(Math.min(amount, getStackSize()));
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
