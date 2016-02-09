package info.loenwind.mves.impl.wire;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class TileMvesWire extends TileEntity {

  public TileMvesWire() {
    // TODO Auto-generated constructor stub
  }

  @Override
  public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
    // TODO Auto-generated method stub
    return super.hasCapability(capability, facing);
  }

  @Override
  public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
    // TODO Auto-generated method stub
    return super.getCapability(capability, facing);
  }

}
