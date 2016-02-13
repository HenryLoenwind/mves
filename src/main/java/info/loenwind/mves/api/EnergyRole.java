package info.loenwind.mves.api;

import java.util.EnumSet;

/**
 * Any energy handler's role can be described with a few general attributes.
 * While this will not give a complete description of it, it will give a general
 * picture.
 * <p>
 * Usually it doesn't matter what a blocks actually does, as long as it exposes
 * some capabilities. But in some cases a tiny bit of "more information" is
 * needed. Instead of filling your config file with ID lists of e.g. known
 * batteries, you can just ask the item if it is one.
 *
 */
public enum EnergyRole {

  /**
   * A generator creates energy.
   */
  GENERATOR,
  /**
   * An on-demand generator stops generating when its buffer is full.
   */
  ON_DEMAND_GENERATOR(GENERATOR),
  /**
   * A machine uses energy.
   */
  MACHINE,
  /**
   * A battery stores energy.
   */
  BATTERY,
  /**
   * A transport transports energy.
   */
  TRANSPORT,
  /**
   * A pipe transports energy through connected blocks.
   */
  PIPE(TRANSPORT),
  /**
   * A portal transports energy between 2 or more non-adjacent portal blocks by
   * other means.
   */
  PORTAL(TRANSPORT),
  /**
   * A wireless transports energy into non-adjacent blocks that are not
   * transports.
   */
  WIRELESS(TRANSPORT),
  /**
   * A relay is a transport that can relay energy to other transports.
   */
  RELAY(TRANSPORT),
  /**
   * A charger (dis-)charges batteries.
   */
  CHARGER,
  /**
   * An item charger (dis-)charges items.
   */
  ITEM_CHARGER(CHARGER),
  /**
   * An entity charger (dis-)charges entities.
   */
  ENTITY_CHARGER(CHARGER),
  /**
   * An inventory charger (dis-)charges items that are in an entity's inventory.
   */
  INVENTORY_CHARGER(ITEM_CHARGER),
  /**
   * A void destroys energy.
   */
  VOID,
  /**
   * Marker for a creative-only block/item.
   */
  CREATIVE,
  /**
   * A meter collects information about energy.
   */
  METER,
  /**
   * A direct action has no buffer of its own.
   */
  DIRECT_ACTION,
  ;

  private final EnergyRole[] parents;

  private EnergyRole() {
    this((EnergyRole[]) null);
  }

  private EnergyRole(EnergyRole... parents) {
    this.parents = parents;
  }

  /**
   * Builds a set of the given role(s) and all of its parents.
   */
  public static EnumSet<EnergyRole> build(EnergyRole... roles) {
    EnumSet<EnergyRole> result = EnumSet.noneOf(EnergyRole.class);
    for (EnergyRole energyRole : roles) {
      add(result, energyRole);
    }
    return result;
  }

  private static void add(EnumSet<EnergyRole> set, EnergyRole energyRole) {
    set.add(energyRole);
    if (energyRole.parents != null) {
      for (EnergyRole parent : energyRole.parents) {
        add(set, parent);
      }
    }
  }

}
