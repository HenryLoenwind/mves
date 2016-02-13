package info.loenwind.mves.api.simple;

import info.loenwind.mves.api.EnergyRole;
import info.loenwind.mves.api.IEnergyHandler;

import java.util.EnumSet;

/**
 * A simple energy handler that is backed by a simple energy buffer.
 * <p>
 * No funny business here. Use this if you have a simple energy buffer and don't
 * need to report different data.
 *
 */
public class SimpleEnergyHandler implements IEnergyHandler {

  private final SimpleEnergyBuffer buffer;
  private final EnumSet<EnergyRole> roles;

  public SimpleEnergyHandler(SimpleEnergyBuffer buffer, EnergyRole... roles) {
    this.buffer = buffer;
    this.roles = EnergyRole.build(roles);
  }

  @Override
  public EnumSet<EnergyRole> getRoles() {
    return roles;
  }

  @Override
  public long getBufferSize() {
    return buffer.getMaxAmount();
  }

  @Override
  public long getBufferContent() {
    return buffer.getAmount();
  }

  @Override
  public long getBufferFree() {
    return buffer.getMaxAmount() - buffer.getAmount();
  }

  @Override
  public Object getNetwork() {
    return null;
  }

}
