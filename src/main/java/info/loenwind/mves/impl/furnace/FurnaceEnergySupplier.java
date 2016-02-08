package info.loenwind.mves.impl.furnace;

import info.loenwind.mves.IEnergyStack;
import info.loenwind.mves.IEnergySupplier;
import net.minecraft.tileentity.TileEntityFurnace;

public class FurnaceEnergySupplier implements IEnergySupplier {

  private final TileEntityFurnace furnace;

  public FurnaceEnergySupplier(TileEntityFurnace furnace) {
    this.furnace = furnace;
  }

  @Override
  public IEnergyStack get() {
    if (!furnace.isBurning() || furnace.getStackInSlot(0) != null) {
      return null;
    }
    return new FurnaceEnergyStack(furnace);
  }

}
