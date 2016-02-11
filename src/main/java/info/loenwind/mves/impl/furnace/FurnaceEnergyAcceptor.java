package info.loenwind.mves.impl.furnace;

import info.loenwind.mves.IEnergyAcceptor;
import info.loenwind.mves.IEnergyOffer;
import info.loenwind.mves.IEnergyStack;
import info.loenwind.mves.config.Config;

import java.util.Iterator;

import net.minecraft.tileentity.TileEntityFurnace;

public class FurnaceEnergyAcceptor implements IEnergyAcceptor {

  private final TileEntityFurnace furnace;

  public FurnaceEnergyAcceptor(TileEntityFurnace furnace) {
    this.furnace = furnace;
  }

  @Override
  public int offerEnergy(IEnergyOffer offer) {
    if (furnace.getStackInSlot(0) == null) {
      return 0;
    }
    int energyPerTick = Config.furnacePowerPerTickConsumed.getInt();
    int remainingFuel = furnace.getField(0);
    int cookTime = furnace.getField(2);
    int totalCookTime = furnace.getField(3);
    int neededCookTime = totalCookTime - cookTime;
    int maxTicks = neededCookTime - remainingFuel;
    System.out.println("Offer in for " + furnace + ", maxTicks=" + maxTicks + " neededCookTime=" + neededCookTime);
    if (maxTicks <= 0) {
      return 0;
    }
    int maxEnergy = maxTicks * energyPerTick;

    int used = 0;
    Iterator<IEnergyStack> iterator = offer.getStacks().iterator();
    while (used < maxEnergy && iterator.hasNext()) {
      IEnergyStack next = iterator.next();
      System.out.println("Offer in for " + furnace + ", next.getStackSize()=" + next.getStackSize());
      while (used < maxEnergy && next != null && next.getStackSize() >= energyPerTick) {
        int extractEnergy = next.extractEnergy(energyPerTick);
        System.out.println("Offer in for " + furnace + ", extractEnergy=" + extractEnergy);
        used += extractEnergy;
      }
    }

    int addtlTicks = used / energyPerTick;
    if (addtlTicks > 0) {
      if (remainingFuel + addtlTicks == 1) {
        addtlTicks++;
      }
      furnace.setField(0, remainingFuel + addtlTicks);
      System.out.println("Offer in for " + furnace + ", remainingFuel=" + remainingFuel + " furnace.getField(0)=" + furnace.getField(0));
    }

    return used;
  }

}
