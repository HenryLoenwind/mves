package info.loenwind.mves.impl.furnace;

import info.loenwind.mves.api.IEnergyStack;
import info.loenwind.mves.api.IEnergySupplier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityFurnace;

public class FurnaceEnergySupplier implements IEnergySupplier {

  private final TileEntityFurnace furnace;
  private int lastTick = -1;
  private FurnaceEnergyStack lastStack = null;

  public FurnaceEnergySupplier(TileEntityFurnace furnace) {
    this.furnace = furnace;
  }

  @Override
  public IEnergyStack get() {
    int tick = MinecraftServer.getServer().getTickCounter();
    if (lastStack != null && lastTick == tick) {
      return lastStack;
    }
    if (furnace.getStackInSlot(0) != null) {
      return null;
    }
    if (!furnace.isBurning() && furnace.getItemBurnTime(furnace.getStackInSlot(1)) <= 0) {
      return null;
    }
    return lastStack = new FurnaceEnergyStack(furnace);
  }

}
