package info.loenwind.mves.impl.simple;

import info.loenwind.mves.api.IEnergyStack;

/**
 * A simple energy stack that is backed by a simple energy buffer.
 * <p>
 * No funny business here. Use this if you have a simple energy buffer and don't
 * need to limit your energy output.
 * <p>
 * TODO: add limit
 *
 */
public class SimpleEnergyStack implements IEnergyStack {

  private final SimpleEnergyBuffer buffer;
  private final Object source;
  private final boolean storedEnergy;

  public SimpleEnergyStack(SimpleEnergyBuffer buffer, boolean storedEnergy) {
    this(buffer, null, storedEnergy);
  }

  public SimpleEnergyStack(SimpleEnergyBuffer buffer, Object source, boolean storedEnergy) {
    this.buffer = buffer;
    this.source = source;
    this.storedEnergy = storedEnergy;
  }

  @Override
  public int getStackSize() {
    long amount = buffer.getAmount();
    return (int) (amount > Integer.MAX_VALUE ? Integer.MAX_VALUE : amount);
  }

  @Override
  public int extractEnergy(int amount) {
    long bamount = buffer.getAmount();
    if (bamount <= 0 || amount <= 0) {
      return 0;
    } else if (amount > bamount) {
      buffer.setAmount(0);
      return (int) bamount;
    } else {
      buffer.setAmount(bamount - amount);
      return amount;
    }
  }

  @Override
  public Object getSource() {
    return source;
  }

  @Override
  public boolean isStoredEnergy() {
    return storedEnergy;
  }

}
