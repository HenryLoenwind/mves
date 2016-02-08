package info.loenwind.mves.impl.furnace;

import info.loenwind.mves.IEnergyAcceptor;
import info.loenwind.mves.IEnergyOffer;
import info.loenwind.mves.IEnergyStack;

import java.util.Iterator;

import net.minecraft.tileentity.TileEntityFurnace;

public class FurnaceEnergyAcceptor implements IEnergyAcceptor {

  public static final int ENERGY_PER_TICK = 40;

  private final TileEntityFurnace furnace;

  public FurnaceEnergyAcceptor(TileEntityFurnace furnace) {
    this.furnace = furnace;
  }

  @Override
  public int offerEnergy(IEnergyOffer offer) {
    if (furnace.getStackInSlot(0) == null) {
      return 0;
    }
    int remainingFuel = furnace.getField(0);
    int neededCookTime = furnace.getField(2);
    int maxTicks = neededCookTime - remainingFuel;
    if (maxTicks <= 0) {
      return 0;
    }
    int maxEnergy = maxTicks * ENERGY_PER_TICK;

    int used = 0;
    Iterator<IEnergyStack> iterator = offer.getStacks().iterator();
    while (used < maxEnergy && iterator.hasNext()) {
      IEnergyStack next = iterator.next();
      while (used < maxEnergy && next != null && next.getStackSize() >= ENERGY_PER_TICK) {
        int extractEnergy = next.extractEnergy(ENERGY_PER_TICK);
        used += extractEnergy;
      }
    }

    int addtlTicks = used / ENERGY_PER_TICK;
    if (addtlTicks > 0) {
      furnace.setField(0, remainingFuel + addtlTicks);
    }

    return used;
  }

}
