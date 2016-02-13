package info.loenwind.mves.api;

import info.loenwind.mves.capability.IMvesCapability;

import java.util.EnumSet;

/**
 * Energy handler is the capability of a block, item or entity to part of the
 * MVES energy system.
 * <p>
 * It should be implemented everywhere where there also is one of the other
 * capabilities. It is ok to not check for the other capabilities if there is no
 * energy handler.
 * <p>
 * Do not rely on the information you can get from this capability. It may be
 * static, an approximation, not implemented or simply meaningless.
 *
 */
public interface IEnergyHandler extends IMvesCapability {

  /**
   * Gives a set of all roles this energy handler can have.
   * <p>
   * It is recommended to always return the same set, no matter what the current
   * state is.
   */
  EnumSet<EnergyRole> getRoles();

  /**
   * The maximum size of the internal buffer at the moment.
   * <p>
   * "0" if there is no buffer. "-1" if the size cannot be determined or is
   * meaningless.
   * <p>
   * Do not use this to determine whether to pull/push energy from/to this
   * energy handler or not.
   */
  long getBufferSize();

  /**
   * The amount of energy currently stored in the internal buffer.
   * <p>
   * "0" if the buffer is empty or there is no buffer. "-1" if the amount cannot
   * be determined or is meaningless, but there actually <i>is</i> or <i>may
   * be</i> some energy.
   * <p>
   * Example: A DIRECT_ACTION ON_DEMAND_GENERATOR that generates a random amount
   * of energy for each mob it kills may return 0 if it sees no mobs and -1 if
   * there are mobs to kill around. It may also opt to not bother counting mobs
   * and always return -1.
   * <p>
   * Do not use this to determine whether to pull/push energy from/to this
   * energy handler or not.
   */
  long getBufferContent();

  /**
   * The amount of energy that can be put into the internal buffer.
   * <p>
   * "0" if there is no buffer or no energy can be put into it. "-1" if the
   * amount cannot be determined or is meaningless.
   * <p>
   * This is usually 0 for generators. For DIRECT_ACTION MACHINEs it may be 0 or
   * it may be the minimum amount it can accept.
   * <p>
   * Do not use this to determine whether to pull/push energy from/to this
   * energy handler or not.
   */
  long getBufferFree();

  /**
   * <i>For mod-internal use only.</i>
   * <p>
   * You'll know when you need this.
   */
  Object getNetwork();
}
