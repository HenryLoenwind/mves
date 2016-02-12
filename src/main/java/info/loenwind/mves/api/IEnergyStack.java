package info.loenwind.mves.api;

  /**
 * An energy stack represents a potential source of energy. It must be backed by
 * some kind of energy store and cannot contain the energy it is offering by
 * itself. They are created by Energy Suppliers only.
 * 
 * <p>
 * Energy stacks may not be stored, they are only valid until the end of the
 * processing of the object that asked for them. For tile entity based energy
 * stacks that is the end of the tick of the currently ticking tile entity. Tile
 * entity based energy stacks are to be considered invalid outside of tile
 * entity ticks, regardless of when they were created.
 */
public interface IEnergyStack {

  /**
   * How much energy is available in this stack? The return value is valid until
   * the next time extractEnergy() is called on any energy stack (or energy is
   * extracted from anything by any other means).
   * 
   * @return The amount of available energy. “0” is a valid return value.
   */
  int getStackSize();

  /**
   * Tries to extracts the given amount of energy.
   * 
   * @param amount
   * @return The amount that was actually extracted.
   */
  int extractEnergy(int amount);

  /**
   * Get the object that represents the source of the energy. This objects is
   * intended as an identification, not to manipulate the the source. It also is
   * implementation specific and optional. Do not do anything with this object
   * unless it belongs to your mod!
   * 
   * @return The object that represents the source of the energy. May be null.
   */
  Object getSource();

  /**
   * Determine if this energy was taken from an energy storage device. Energy
   * storage devices should not accept energy from other storage devices.
   * 
   * @return "true" if this energy comes from some kind of energy storage
   *         device. "false" if it comes from some kind of generator or from a
   *         storage device that was configured by the user to drain.
   */
  boolean isStoredEnergy();
  
}
