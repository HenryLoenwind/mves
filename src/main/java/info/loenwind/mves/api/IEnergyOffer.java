package info.loenwind.mves.api;

import java.util.List;

/**
 * An energy offer represents the combined energy sources of some kind of energy
 * distribution network. They are created by Energy Transporters and given to
 * Energy Acceptors and other Energy Transporters.
 * 
 * <p>
 * Energy offers may not be stored.
 *
 */
public interface IEnergyOffer {

  /**
   * The list of energy stacks that are offered. It is legal for some of the
   * stacks to be empty. The list may be sorted, so the acceptor should loop
   * over it in order and take energy until it is full. The returned list must
   * not be modified.
   * 
   * @return List of energy stacks. May be empty. May be null.
   */
  List<IEnergyStack> getStacks();

  /**
   * Checks if this offer has seen the given object before.
   * 
   * @param o
   *          The object to check.
   * @return True if the object has been seen before, false otherwise.
   */
  boolean hasSeen(Object o);

  /**
   * Sees the given object.
   * 
   * @param o
   *          The object to see.
   */
  void see(Object o);

  /**
   * Determines how much energy may be taken from this offer. Taking more than
   * this amount will result in a fiery explosion or worse.
   * 
   * @return The limit. Integer.MAX_VALUE if unlimited.
   */
  int getLimit();
}
