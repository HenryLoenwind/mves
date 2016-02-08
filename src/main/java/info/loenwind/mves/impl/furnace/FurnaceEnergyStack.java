package info.loenwind.mves.impl.furnace;

import info.loenwind.mves.IEnergyStack;
import net.minecraft.tileentity.TileEntityFurnace;

public class FurnaceEnergyStack implements IEnergyStack {

  public static final int ENERGY_PER_TICK = 40;

  private final TileEntityFurnace furnace;
  private boolean used = false;

  public FurnaceEnergyStack(TileEntityFurnace furnace) {
    this.furnace = furnace;
  }

  @Override
  public int getStackSize() {
    return used ? 0 : ENERGY_PER_TICK;
  }

  @Override
  public int extractEnergy(int amount) {
    if (used) {
      return 0;
    } else {
      used = true;
      return ENERGY_PER_TICK;
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

}
