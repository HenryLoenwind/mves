package info.loenwind.mves.proxies;

import info.loenwind.mves.impl.wire.BlockMvesWire;
import info.loenwind.mves.impl.wire.ItemMvesWire;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

  public void init(FMLPreInitializationEvent e) {
    BlockMvesWire.create();
    ItemMvesWire.create();
  }

  public void init(FMLInitializationEvent e) {

  }

  public void init(FMLPostInitializationEvent e) {

  }
}
