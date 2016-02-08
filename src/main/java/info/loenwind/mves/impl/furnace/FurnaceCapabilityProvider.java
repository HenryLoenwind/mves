package info.loenwind.mves.impl.furnace;

import info.loenwind.mves.MvesMod;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class FurnaceCapabilityProvider implements ICapabilityProvider {

  private final TileEntityFurnace furnace;

  public FurnaceCapabilityProvider(TileEntityFurnace furnace) {
    this.furnace = furnace;
  }

  @Override
  public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
    return facing != null && (capability == MvesMod.CAP_EnergySupplier || capability == MvesMod.CAP_EnergyAcceptor);
  }

  @Override
  public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
    if (facing != null) {
      if (capability == MvesMod.CAP_EnergySupplier) {
        return (T) new FurnaceEnergySupplier(furnace);
      } else if (capability == MvesMod.CAP_EnergyAcceptor) {
        return (T) new FurnaceEnergyAcceptor(furnace);
      }
    }
    return null;
  }

}
