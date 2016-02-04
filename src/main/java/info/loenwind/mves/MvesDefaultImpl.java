package info.loenwind.mves;

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
