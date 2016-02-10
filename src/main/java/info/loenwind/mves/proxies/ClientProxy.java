package info.loenwind.mves.proxies;

import info.loenwind.mves.MvesMod;
import info.loenwind.mves.impl.wire.BlockMvesWire;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

  @Override
  public void init(FMLPreInitializationEvent e) {
    super.init(e);
  }

  @Override
  public void init(FMLInitializationEvent e) {
    super.init(e);
    Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
        .register(Item.getItemFromBlock(BlockMvesWire.block), 0, new ModelResourceLocation(MvesMod.MODID + ":" + BlockMvesWire.name(), "inventory"));
  }

  @Override
  public void init(FMLPostInitializationEvent e) {
    super.init(e);
  }


}
