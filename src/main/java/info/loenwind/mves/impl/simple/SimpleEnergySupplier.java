package info.loenwind.mves.impl.simple;

/**
 * A simple energy supplier that is backed by a simple energy buffer.
 * <p>
 * No funny business here. Use this if you have a simple energy buffer and don't
 * need to limit your energy output.
 * <p>
 * TODO: add limit
 *
 */
import info.loenwind.mves.api.IEnergyStack;
import info.loenwind.mves.api.IEnergySupplier;

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
