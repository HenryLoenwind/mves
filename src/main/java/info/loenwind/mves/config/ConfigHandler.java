package info.loenwind.mves.config;

import io.netty.buffer.ByteBuf;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import org.apache.logging.log4j.Logger;

public class ConfigHandler {

  private static final int ID = 0;

  private final SimpleNetworkWrapper network;
  private final String modid;
  private final Logger log;

  static {
    loadAll();
  }

  //	****************************************************************************************

  public Configuration configuration;
  public File configDirectory;
  public boolean configLockedByServer = false;

  public ConfigHandler(String modid, Logger log, SimpleNetworkWrapper network) {
    this.modid = modid;
    this.log = log;
    this.network = network;
  }

  public void init(FMLPreInitializationEvent event) {
    network.registerMessage(new HandleConfigSync(this), PacketConfigSync.class, ID, Side.CLIENT);
    MinecraftForge.EVENT_BUS.register(this);
    configDirectory = new File(event.getModConfigurationDirectory(), modid.toLowerCase());
    if (!configDirectory.exists()) {
      configDirectory.mkdir();
    }

    File configFile = new File(configDirectory, modid + ".cfg");
    configuration = new Configuration(configFile);
    syncConfig(false);
  }

  private void syncConfig(boolean load) {
    try {
      if (load) {
        configuration.load();
      }
      processConfig();
    } catch (Exception e) {
      log.error(modid + " has a problem loading its configuration");
      e.printStackTrace();
    } finally {
      if (configuration.hasChanged()) {
        configuration.save();
      }
    }
  }

  @SuppressWarnings("static-method")
  @SubscribeEvent
  public void onConfigChanged(OnConfigChangedEvent event) {
    if (event.getModID().equals(modid)) {
      log.info("Updating config...");
      syncConfig(false);
    }
  }

  private void processConfig() {
    loadAll(configuration);
    if (configuration.hasChanged()) {
      configuration.save();
    }
  }

  public static void toBytes(ByteBuf buf) {
    for (Config value : Config.values()) {
      if (value.getSection().sync) {
        value.store(buf);
      }
    }
  }

  public void fromBytes(ByteBuf buf) {
    if (Minecraft.getMinecraft().isIntegratedServerRunning()) {
      return;
    }
    for (Config value : Config.values()) {
      if (value.getSection().sync) {
        value.read(buf);
      }
    }
    configLockedByServer = true;
  }

  @SuppressWarnings("static-method")
  @SubscribeEvent
  public void onPlayerLoggon(PlayerLoggedInEvent evt) {
    network.sendTo(new PacketConfigSync(), (EntityPlayerMP) evt.player);
  }

  @SuppressWarnings("static-method")
  @SubscribeEvent
  public void onPlayerLogout(ClientDisconnectionFromServerEvent event) {
    syncConfig(false);
    configLockedByServer = false;
  }

  public static void loadAll(Configuration config) {
    for (Config value : Config.values()) {
      value.load(config);
    }
  }

  public static void loadAll() {
    for (Config value : Config.values()) {
      value.resetToDefault();
    }
  }

}
