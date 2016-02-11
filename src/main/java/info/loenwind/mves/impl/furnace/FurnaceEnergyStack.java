package info.loenwind.mves.impl.furnace;

import info.loenwind.mves.IEnergyStack;
import info.loenwind.mves.config.Config;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

public class FurnaceEnergyStack implements IEnergyStack {

  private final TileEntityFurnace furnace;
  private boolean used = false;

  public FurnaceEnergyStack(TileEntityFurnace furnace) {
    this.furnace = furnace;
  }

  @Override
  public int getStackSize() {
    return used ? 0 : Config.furnacePowerPerTickProduced.getInt();
  }

  @Override
  public int extractEnergy(int amount) {
    if (used) {
      return 0;
    } else {
      used = true;
      if (!furnace.isBurning() && !ignite()) {
        return 0;
      }
      return Config.furnacePowerPerTickProduced.getInt();
    }
  }

  @Override
  public Object getSource() {
    return furnace;
  }

  @Override
  public boolean isStoredEnergy() {
    return false;
  }

  private static final int furnaceBurnTime = 0;
  private static final int currentItemBurnTime = 1;

  private boolean ignite() {
    ItemStack stackInSlot = furnace.getStackInSlot(1);
    int burnTime = furnace.getItemBurnTime(stackInSlot);
    if (burnTime > 0) {
      furnace.setField(currentItemBurnTime, burnTime);
      furnace.setField(furnaceBurnTime, burnTime - 1);
      stackInSlot.stackSize--;
      if (stackInSlot.stackSize <= 0) {
        stackInSlot = stackInSlot.getItem().getContainerItem(stackInSlot);
      }
      furnace.setInventorySlotContents(1, stackInSlot);
      return true;
    }
    return false;
  }

}
