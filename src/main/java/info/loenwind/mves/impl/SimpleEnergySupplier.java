package info.loenwind.mves.impl;

import info.loenwind.mves.IEnergyStack;
import info.loenwind.mves.IEnergySupplier;

public class SimpleEnergySupplier implements IEnergySupplier {

  private final SimpleEnergyBuffer buffer;
  private final boolean isBattery;

  public SimpleEnergySupplier(SimpleEnergyBuffer buffer, boolean isBattery) {
    this.buffer = buffer;
    this.isBattery = isBattery;
  }

  @Override
  public IEnergyStack get() {
    return new SimpleEnergyStack(buffer, isBattery);
  }

}
