package info.loenwind.mves.capability;

import info.loenwind.mves.api.IEnergyAcceptor;
import info.loenwind.mves.api.IEnergyOffer;
import info.loenwind.mves.api.IEnergyStack;
import info.loenwind.mves.api.IEnergySupplier;
import info.loenwind.mves.api.IEnergyTransporter;

public final class MvesDefaultImpl implements IEnergyAcceptor, IEnergySupplier, IEnergyTransporter {

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

}
