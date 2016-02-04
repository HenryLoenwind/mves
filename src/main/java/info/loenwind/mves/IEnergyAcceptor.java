package info.loenwind.mves;

/**
 * Energy acceptor is the capability of a block’s side or an item to receive
 * energy. It is a passive capability that will only be triggered by Energy
 * Transporters.
 * 
 * <p>
 * It also is an interface that may be implemented by energy transporters that
 * want to be able to accept energy from other energy transporters.
 * 
 *
 */
public interface IEnergyAcceptor extends IMvesCapability {

  /**
   * Processes the offered energy and returns the amount that was taken. See
   * IEnergyOffer for details on how to process offers.
   * <p>
   * For tile entities: This method may be called multiple times per tick, but
   * should only be called once per tick of the caller. It may only be called by
   * the adjacent block (or its network).
   * <p>
   * An energy acceptor that is located on the "inside"/"null" side of a block
   * may only be called by that block (and its tile entity and other
   * capabilities) itself.
   * 
   * @param offer
   *          The Energy offer to be processed.
   * @return The amount of energy that was taken.
   */
  int offerEnergy(IEnergyOffer offer);
}
