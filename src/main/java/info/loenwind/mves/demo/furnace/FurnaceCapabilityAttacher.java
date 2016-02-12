package info.loenwind.mves.demo.furnace;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FurnaceCapabilityAttacher {

  public FurnaceCapabilityAttacher() {
  }

  @SubscribeEvent
  public void onTELoad(AttachCapabilitiesEvent.TileEntity event) {
    TileEntity tileEntity = event.getTileEntity();
    if (tileEntity instanceof TileEntityFurnace) {
      event.addCapability(new ResourceLocation("mves:furnace"), new FurnaceCapabilityProvider((TileEntityFurnace) tileEntity));
    }
  }

}
