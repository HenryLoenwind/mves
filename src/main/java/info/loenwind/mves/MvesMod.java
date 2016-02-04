package info.loenwind.mves;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = MvesMod.MODID, version = MvesMod.VERSION, name = MvesMod.MODID)
public class MvesMod {
  public static final String MODID = "mves";
  public static final String VERSION = "1.0";

  @CapabilityInject(IEnergySupplier.class)
  private static final Capability<IEnergySupplier> CAP_EnergySupplier = null;
  @CapabilityInject(IEnergyTransporter.class)
  private static final Capability<IEnergyTransporter> CAP_EnergyTransporter = null;
  @CapabilityInject(IEnergyAcceptor.class)
  private static final Capability<IEnergyAcceptor> CAP_EnergyAcceptor = null;

  @EventHandler
  public void preinit(FMLPreInitializationEvent evt) {
    CapabilityManager.INSTANCE.register(IEnergySupplier.class, new MvesStorage<IEnergySupplier>(), MvesDefaultImpl.class);
    CapabilityManager.INSTANCE.register(IEnergyTransporter.class, new MvesStorage<IEnergyTransporter>(), MvesDefaultImpl.class);
    CapabilityManager.INSTANCE.register(IEnergyAcceptor.class, new MvesStorage<IEnergyAcceptor>(), MvesDefaultImpl.class);
  }

  @EventHandler
  public void init(FMLInitializationEvent event) {
    // some example code
    System.out.println("CAP_EnergyAcceptor >> " + CAP_EnergyAcceptor);
  }
}
