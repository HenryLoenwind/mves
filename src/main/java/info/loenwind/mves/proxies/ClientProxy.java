package info.loenwind.mves.proxies;

import info.loenwind.mves.impl.wire.BlockMvesWire;
import info.loenwind.mves.impl.wire.ItemMvesWire;

import java.util.Map;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import com.google.common.collect.Maps;

public class ClientProxy extends CommonProxy {

  @Override
  public void init(FMLPreInitializationEvent e) {
    super.init(e);
    ModelBakery.registerItemVariants(ItemMvesWire.item, new ModelResourceLocation("redstone", "inventory"));
    //    MinecraftForge.EVENT_BUS.register(this);
  }

  //  private boolean mapped = false;
  //
  //  @SubscribeEvent
  //  public void bake(ModelBakeEvent event) {
  //    if (!mapped) {
  //      System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> HERE");
  //      mapped = true;
  //      event.modelManager.getBlockModelShapes().getBlockStateMapper().registerBlockStateMapper(BlockMvesWire.block, (new StateMapperBase() {
  //        protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
  //          System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> SNIP!");
  //          Map<IProperty, Comparable> map = Maps.<IProperty, Comparable> newLinkedHashMap(state.getProperties());
  //          return new ModelResourceLocation("redstone_wire", this.getPropertyString(map));
  //        }
  //      }));
  //    }
  //  }

  @Override
  public void init(FMLInitializationEvent e) {
    super.init(e);
    Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getBlockStateMapper()
        .registerBlockStateMapper(BlockMvesWire.block, (new StateMapperBase() {
          protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
            Map<IProperty, Comparable> map = Maps.<IProperty, Comparable> newLinkedHashMap(state.getProperties());
            return new ModelResourceLocation("redstone_wire", this.getPropertyString(map));
          }
        }));
    Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(ItemMvesWire.item, 0, new ModelResourceLocation("redstone", "inventory"));
  }

  @Override
  public void init(FMLPostInitializationEvent e) {
    super.init(e);
  }


}
