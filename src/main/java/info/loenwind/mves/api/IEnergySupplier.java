package info.loenwind.mves.api;

import info.loenwind.mves.capability.IMvesCapability;

import com.google.common.base.Supplier;

/**
 * Energy supplier is the capability of a block’s side or an item to provide
 * energy on request. It is a passive capability that will only be triggered by
 * Energy Transporters.
 *
 */
public interface IEnergySupplier extends IMvesCapability, Supplier<IEnergyStack> {

  /**
   * Get an representation of this supplier's available energy.
   * <p>
   * For tile entities: This method may only be called once per block side and
   * tick, and only by the adjacent block (or its network) during its tick.
   * <p>
   * An energy supplier that is located on the "inside"/"null" side of a block
   * may only be called by that block (and its tile entity and other
   * capabilities) itself. In this case, the once-per-tick rule may be broken.
   * <p>
   * Both "null" and energy stacks with "0" size are valid return values.
   * 
   * @return An Energy Stack that can be used to retrieve energy. May be null.
   */
  IEnergyStack get();
}
