package info.loenwind.mves;

import info.loenwind.mves.impl.furnace.FurnaceCapabilityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = MvesMod.MODID, version = MvesMod.VERSION, name = MvesMod.MODID)
public class MvesMod {
  public static final String MODID = "mves";
  public static final String VERSION = "1.0";

  @CapabilityInject(IEnergySupplier.class)
  public static final Capability<IEnergySupplier> CAP_EnergySupplier = null;
  @CapabilityInject(IEnergyTransporter.class)
  public static final Capability<IEnergyTransporter> CAP_EnergyTransporter = null;
  @CapabilityInject(IEnergyAcceptor.class)
  public static final Capability<IEnergyAcceptor> CAP_EnergyAcceptor = null;

  @EventHandler
  public void preinit(FMLPreInitializationEvent evt) {
    CapabilityManager.INSTANCE.register(IEnergySupplier.class, new MvesStorage<IEnergySupplier>(), MvesDefaultImpl.class);
    CapabilityManager.INSTANCE.register(IEnergyTransporter.class, new MvesStorage<IEnergyTransporter>(), MvesDefaultImpl.class);
    CapabilityManager.INSTANCE.register(IEnergyAcceptor.class, new MvesStorage<IEnergyAcceptor>(), MvesDefaultImpl.class);
    if (evt.getSide() == Side.SERVER) {
      MinecraftForge.EVENT_BUS.register(this);
    }
  }

  @EventHandler
  public void init(FMLInitializationEvent event) {
    // some example code
    System.out.println("CAP_EnergyAcceptor >> " + CAP_EnergyAcceptor);
  }

  @SubscribeEvent
  public void onTELoad(AttachCapabilitiesEvent.TileEntity event) {
    TileEntity tileEntity = event.getTileEntity();
    if (tileEntity instanceof TileEntityFurnace) {
      event.addCapability(new ResourceLocation("mves:furnace"), new FurnaceCapabilityProvider((TileEntityFurnace) tileEntity));
    }
  }
}
