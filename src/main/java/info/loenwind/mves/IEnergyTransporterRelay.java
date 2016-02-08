package info.loenwind.mves;

/**
 * Energy Transporter Relay is an interface that can be implemented by energy
 * transporters (the capability) to accept and distribute energy from other
 * transporters. That energy should then be distributed to all connected
 * acceptors. An energy transporter that only wants to accept energy into its
 * own buffer should implement energy acceptor instead---it is an end point.
 *
 */
public interface IEnergyTransporterRelay extends IEnergyTransporter {

  /**
   * Processes the offered energy and returns the amount that was taken. See
   * IEnergyOffer for details on how to process offers.
   * <p>
   * For tile entities: This method may be called multiple times per tick, but
   * should only be called once per tick of the caller. It may only be called by
   * the adjacent block (or its network).
   * <p>
   * An energy transporter relay that is located on the "inside"/"null" side of
   * a block may not be called by anyone but code from the same mod.
   * 
   * @param offer
   *          The Energy offer to be processed.
   * @return The amount of energy that was taken.
   */
  int relayEnergy(IEnergyOffer offer);

}
