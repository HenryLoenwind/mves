package info.loenwind.mves.demo.furnace;

import info.loenwind.mves.MvesMod;
import info.loenwind.mves.api.EnergyRole;
import info.loenwind.mves.api.IEnergyAcceptor;
import info.loenwind.mves.api.IEnergyHandler;
import info.loenwind.mves.api.IEnergyOffer;
import info.loenwind.mves.api.IEnergyStack;
import info.loenwind.mves.api.IEnergySupplier;
import info.loenwind.mves.config.Config;

import java.util.EnumSet;
import java.util.Iterator;

import net.minecraft.block.BlockFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

/**
 * This is an example of an all-in-one implementation.
 *
 */
public class FurnaceCapabilityProvider implements ICapabilityProvider, IEnergyHandler, IEnergyAcceptor, IEnergySupplier, IEnergyStack {

  private static final EnumSet<EnergyRole> ROLE = EnergyRole.build(EnergyRole.GENERATOR, EnergyRole.DIRECT_ACTION, EnergyRole.MACHINE);

  private static final int furnaceBurnTime_field = 0;
  private static final int currentItemBurnTime_field = 1;
  private static final int cookTime_field = 2;
  private static final int totalCookTime_field = 3;
  private static final int input_slot = 0;
  private static final int fuel_slot = 1;

  private final TileEntityFurnace furnace;

  public FurnaceCapabilityProvider(TileEntityFurnace furnace) {
    this.furnace = furnace;
  }

  /////////////////////////////////////////////////////////////////////////
  // CAPABILITY PROVIDER
  /////////////////////////////////////////////////////////////////////////

  @Override
  public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
    return furnace.hasWorldObj() && !furnace.getWorld().isRemote && facing != null
        && (capability == MvesMod.CAP_EnergySupplier || capability == MvesMod.CAP_EnergyAcceptor || capability == MvesMod.CAP_EnergyHandler);
  }

  @Override
  public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
    return hasCapability(capability, facing) ? (T) this : null;
  }

  /////////////////////////////////////////////////////////////////////////
  // HANDLER
  /////////////////////////////////////////////////////////////////////////

  @Override
  public EnumSet<EnergyRole> getRoles() {
    return ROLE;
  }

  @Override
  public long getBufferSize() {
    return 0;
  }

  @Override
  public long getBufferContent() {
    IEnergyStack stack = get();
    if (stack != null) {
      return stack.getStackSize();
    }
    return 0;
  }

  @Override
  public long getBufferFree() {
    return 0;
  }

  @Override
  public Object getNetwork() {
    return null;
  }

  /////////////////////////////////////////////////////////////////////////
  // ACCEPTOR
  /////////////////////////////////////////////////////////////////////////

  @Override
  public int offerEnergy(IEnergyOffer offer) {
    if (!isSmelting()) {
      return 0;
    }
    int energyPerTick = Config.furnacePowerPerTickConsumed.getInt();
    int offerMax = offer.getLimit();
    if (energyPerTick > offerMax) {
      return 0;
    }
    boolean wasBurning = furnace.isBurning();
    int remainingFuel = furnace.getField(furnaceBurnTime_field);
    int cookTime = furnace.getField(cookTime_field);
    int totalCookTime = furnace.getField(totalCookTime_field);
    int neededCookTime = totalCookTime - cookTime;
    int maxTicks = neededCookTime - remainingFuel + 1;
    if (maxTicks <= 0) {
      return 0;
    }
    int maxEnergy = maxTicks * energyPerTick;
    while (maxEnergy > offerMax) {
      maxEnergy -= energyPerTick;
      if (maxEnergy <= 0) {
        return 0;
      }
    }

    int used = 0;
    Iterator<IEnergyStack> iterator = offer.getStacks().iterator();
    while (used < maxEnergy && iterator.hasNext()) {
      IEnergyStack next = iterator.next();
      while (used < maxEnergy && next != null && next.getStackSize() >= energyPerTick) {
        int extractEnergy = next.extractEnergy(energyPerTick);
        used += extractEnergy;
      }
    }

    int addtlTicks = used / energyPerTick;
    if (addtlTicks > 0) {
      if (remainingFuel + addtlTicks <= 3) {
        addtlTicks = 4;
      }
      furnace.setField(furnaceBurnTime_field, remainingFuel + addtlTicks);
      if (!wasBurning) {
        BlockFurnace.setState(furnace.isBurning(), furnace.getWorld(), furnace.getPos());
      }
      furnace.markDirty();
    }

    return used;
  }

  /////////////////////////////////////////////////////////////////////////
  // SUPPLIER
  /////////////////////////////////////////////////////////////////////////

  private int lastTick = -1;

  @Override
  public IEnergyStack get() {
    return lastTick == MinecraftServer.getServer().getTickCounter() || isSmelting() || (!furnace.isBurning() && !isIgnitable()) ? null : this;
  }

  /////////////////////////////////////////////////////////////////////////
  // STACK
  /////////////////////////////////////////////////////////////////////////

  @Override
  public int getStackSize() {
    return lastTick == MinecraftServer.getServer().getTickCounter() ? 0 : Config.furnacePowerPerTickProduced.getInt();
  }

  @Override
  public int extractEnergy(int amount) {
    if (lastTick == MinecraftServer.getServer().getTickCounter() || (!furnace.isBurning() && !ignite())) {
      return 0;
    } else {
      lastTick = MinecraftServer.getServer().getTickCounter();
      return Config.furnacePowerPerTickProduced.getInt();
    }
  }

  @Override
  public Object getSource() {
    return furnace;
  }

  @Override
  public boolean isStoredEnergy() {
    return false;
  }

  /////////////////////////////////////////////////////////////////////////
  // TOOLS
  /////////////////////////////////////////////////////////////////////////

  private boolean isSmelting() {
    return furnace.getStackInSlot(input_slot) != null;
  }

  private boolean isIgnitable() {
    return furnace.getItemBurnTime(furnace.getStackInSlot(fuel_slot)) > 0;
  }

  private boolean ignite() {
    ItemStack stackInSlot = furnace.getStackInSlot(fuel_slot);
    int burnTime = furnace.getItemBurnTime(stackInSlot);
    if (burnTime > 0) {
      furnace.setField(currentItemBurnTime_field, burnTime);
      furnace.setField(furnaceBurnTime_field, burnTime - 1);
      stackInSlot.stackSize--;
      if (stackInSlot.stackSize <= 0) {
        stackInSlot = stackInSlot.getItem().getContainerItem(stackInSlot);
      }
      furnace.setInventorySlotContents(fuel_slot, stackInSlot);
      BlockFurnace.setState(furnace.isBurning(), furnace.getWorld(), furnace.getPos());
      furnace.markDirty();
      return true;
    }
    return false;
  }

}
