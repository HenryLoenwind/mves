package info.loenwind.mves.capability;

import info.loenwind.mves.api.EnergyRole;
import info.loenwind.mves.api.IEnergyAcceptor;
import info.loenwind.mves.api.IEnergyHandler;
import info.loenwind.mves.api.IEnergyOffer;
import info.loenwind.mves.api.IEnergyStack;
import info.loenwind.mves.api.IEnergySupplier;
import info.loenwind.mves.api.IEnergyTransporter;

import java.util.EnumSet;

public final class MvesDefaultImpl implements IEnergyAcceptor, IEnergySupplier, IEnergyTransporter, IEnergyHandler {

  private MvesDefaultImpl() {
  }

  @Override
  public IEnergyStack get() {
    return null;
  }

  @Override
  public int offerEnergy(IEnergyOffer offer) {
    return 0;
  }

  @Override
  public EnumSet<EnergyRole> getRoles() {
    return EnergyRole.build();
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

  @Override
  public int relayEnergy(IEnergyOffer offer) {
    return 0;
  }

}
