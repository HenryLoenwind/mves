package info.loenwind.mves.proxies;

import info.loenwind.mves.api.IEnergyAcceptor;
import info.loenwind.mves.api.IEnergySupplier;
import info.loenwind.mves.api.IEnergyTransporter;
import info.loenwind.mves.capability.MvesDefaultImpl;
import info.loenwind.mves.capability.MvesStorage;
import info.loenwind.mves.config.Config;
import info.loenwind.mves.demo.furnace.FurnaceCapabilityAttacher;
import info.loenwind.mves.demo.wire.BlockMvesWire;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {

  public void init(FMLPreInitializationEvent e) {
    CapabilityManager.INSTANCE.register(IEnergySupplier.class, new MvesStorage<IEnergySupplier>(), MvesDefaultImpl.class);
    CapabilityManager.INSTANCE.register(IEnergyTransporter.class, new MvesStorage<IEnergyTransporter>(), MvesDefaultImpl.class);
    CapabilityManager.INSTANCE.register(IEnergyAcceptor.class, new MvesStorage<IEnergyAcceptor>(), MvesDefaultImpl.class);

    if (Config.enableFurnaceHooking.getBoolean()) {
      MinecraftForge.EVENT_BUS.register(new FurnaceCapabilityAttacher());
    }
    if (Config.enableRainbowWire.getBoolean()) {
      BlockMvesWire.create();
    }
  }

  public void init(FMLInitializationEvent e) {
    if (Config.enableRainbowWire.getBoolean() && Config.enableRainbowWireRecipe.getBoolean()) {
      GameRegistry.addRecipe(new ItemStack(Item.getItemFromBlock(BlockMvesWire.block)), new Object[] { "345", "2x6", "1x7", 'x', Items.redstone, '1',
          new ItemStack(Items.dye, 1, EnumDyeColor.RED.getDyeDamage()), '2', new ItemStack(Items.dye, 1, EnumDyeColor.ORANGE.getDyeDamage()), '3',
          new ItemStack(Items.dye, 1, EnumDyeColor.YELLOW.getDyeDamage()), '4', new ItemStack(Items.dye, 1, EnumDyeColor.GREEN.getDyeDamage()), '5',
          new ItemStack(Items.dye, 1, EnumDyeColor.CYAN.getDyeDamage()), '6', new ItemStack(Items.dye, 1, EnumDyeColor.BLUE.getDyeDamage()), '7',
          new ItemStack(Items.dye, 1, EnumDyeColor.PURPLE.getDyeDamage()) });
    }
  }

  public void init(FMLPostInitializationEvent e) {

  }
}
