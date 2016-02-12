package info.loenwind.mves.impl.furnace;

import info.loenwind.mves.api.IEnergyAcceptor;
import info.loenwind.mves.api.IEnergyOffer;
import info.loenwind.mves.api.IEnergyStack;
import info.loenwind.mves.config.Config;

import java.util.Iterator;

import net.minecraft.block.BlockFurnace;
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
    boolean wasBurning = furnace.isBurning();
    int remainingFuel = furnace.getField(0);
    int cookTime = furnace.getField(2);
    int totalCookTime = furnace.getField(3);
    int neededCookTime = totalCookTime - cookTime;
    int maxTicks = neededCookTime - remainingFuel + 1;
    if (maxTicks <= 0) {
      return 0;
    }
    int maxEnergy = maxTicks * energyPerTick;

    int used = 0;
    Iterator<IEnergyStack> iterator = offer.getStacks().iterator();
    while (used < maxEnergy && iterator.hasNext()) {
      IEnergyStack next = iterator.next();
      while (used < maxEnergy && next != null && next.getStackSize() >= energyPerTick) {
        int extractEnergy = next.extractEnergy(energyPerTick);
        used += extractEnergy;
      }
    }

    int addtlTicks = used / energyPerTick;
    if (addtlTicks > 0) {
      while (remainingFuel + addtlTicks <= 3) {
        addtlTicks++;
      }
      furnace.setField(0, remainingFuel + addtlTicks);
      if (!wasBurning) {
        BlockFurnace.setState(furnace.isBurning(), furnace.getWorld(), furnace.getPos());
      }
      furnace.markDirty();
    }

    return used;
  }

}
