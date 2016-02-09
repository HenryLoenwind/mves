package info.loenwind.mves.proxies;

import info.loenwind.mves.impl.wire.ItemMvesWire;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

  @Override
  public void init(FMLPreInitializationEvent e) {
    super.init(e);
    ModelBakery.registerItemVariants(ItemMvesWire.item, new ModelResourceLocation("redstone", "inventory"));
  }

  @Override
  public void init(FMLInitializationEvent e) {
    super.init(e);
    Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(ItemMvesWire.item, 0, new ModelResourceLocation("redstone", "inventory"));
  }

  @Override
  public void init(FMLPostInitializationEvent e) {
    super.init(e);
  }


}
