package info.loenwind.mves.demo.wire;

import info.loenwind.mves.api.EnergyRole;
import info.loenwind.mves.api.IEnergyHandler;

import java.util.EnumSet;

/**
 * An example for a very basic energy handler. It will report only static data.
 * <p>
 * Most pure transporters will have a handler like this. Their roles don't
 * change and they have no buffer to report dynamic data on.
 *
 */
public class WireEnergyHandler implements IEnergyHandler {

  private static final EnumSet<EnergyRole> ROLE = EnergyRole.build(EnergyRole.PIPE, EnergyRole.RELAY);

  public WireEnergyHandler() {
  }

  @Override
  public EnumSet<EnergyRole> getRoles() {
    return ROLE;
  }

  @Override
  public long getBufferSize() {
    return 0;
  }

  @Override
  public long getBufferContent() {
    return 0;
  }

  @Override
  public long getBufferFree() {
    return 0;
  }

  @Override
  public Object getNetwork() {
    return null;
  }

}
