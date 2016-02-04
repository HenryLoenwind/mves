package info.loenwind.mves;

public final class MvesDefaultImpl implements IEnergyAcceptor, IEnergySupplier, IEnergyTransporter {

  private MvesDefaultImpl() {
  }

  @Override
  public IEnergyStack supplyEnergy() {
    return null;
  }

  @Override
  public int offerEnergy(IEnergyOffer offer) {
    return 0;
  }

}
