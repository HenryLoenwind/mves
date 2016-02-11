package info.loenwind.mves.impl.furnace;

import info.loenwind.mves.MvesMod;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class FurnaceCapabilityProvider implements ICapabilityProvider {

  private final TileEntityFurnace furnace;
  private FurnaceEnergySupplier supplier;
  private FurnaceEnergyAcceptor acceptor;

  public FurnaceCapabilityProvider(TileEntityFurnace furnace) {
    this.furnace = furnace;
    supplier = new FurnaceEnergySupplier(furnace);
    acceptor = new FurnaceEnergyAcceptor(furnace);
  }

  @Override
  public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
    return furnace.hasWorldObj() && !furnace.getWorld().isRemote && facing != null
        && (capability == MvesMod.CAP_EnergySupplier || capability == MvesMod.CAP_EnergyAcceptor);
  }

  @Override
  public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
    if (furnace.hasWorldObj() && !furnace.getWorld().isRemote && facing != null) {
      if (capability == MvesMod.CAP_EnergySupplier) {
        return (T) supplier;
      } else if (capability == MvesMod.CAP_EnergyAcceptor) {
        return (T) acceptor;
      }
    }
    return null;
  }

}
