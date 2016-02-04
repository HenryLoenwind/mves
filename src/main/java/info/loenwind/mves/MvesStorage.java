package info.loenwind.mves;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class MvesStorage<T extends IMvesCapability> implements IStorage<T> {

  @Override
  public NBTBase writeNBT(Capability<T> capability, T instance, EnumFacing side) {
    if (instance instanceof net.minecraftforge.common.util.INBTSerializable<?>) {
      return ((net.minecraftforge.common.util.INBTSerializable) instance).serializeNBT();
    }
    return null;
  }

  @Override
  public void readNBT(Capability<T> capability, T instance, EnumFacing side, NBTBase nbt) {
    if (instance instanceof net.minecraftforge.common.util.INBTSerializable<?>) {
      ((net.minecraftforge.common.util.INBTSerializable) instance).deserializeNBT(nbt);
    }
  }

}
