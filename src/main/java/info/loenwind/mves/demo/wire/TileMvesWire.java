package info.loenwind.mves.demo.wire;

import info.loenwind.mves.MvesMod;
import info.loenwind.mves.api.IEnergyHandler;
import info.loenwind.mves.api.IEnergyOffer;
import info.loenwind.mves.api.IEnergyTransporterRelay;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

public class TileMvesWire extends TileEntity implements ITickable {

  private static final int _CONN = 0xc011;
  private WireConnections connections = WireConnections.NONE;
  private WireEnergyTransporter transporter = null;

  private static final IEnergyHandler HANDLER = new WireEnergyHandler();
  private static final IEnergyTransporterRelay NULL_RELAY = new IEnergyTransporterRelay() {
    @Override
    public int relayEnergy(IEnergyOffer offer) {
      return 0;
    }
  };

  public TileMvesWire() {
    super();
  }

  @Override
  public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
    return (!worldObj.isRemote && (capability == MvesMod.CAP_EnergyTransporter || capability == MvesMod.CAP_EnergyHandler)) || super.hasCapability(capability, facing);
  }

  @Override
  public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
    if (!worldObj.isRemote) {
      if (capability == MvesMod.CAP_EnergyTransporter) {
        return (T) (transporter != null ? transporter : NULL_RELAY);
      } else if (capability == MvesMod.CAP_EnergyHandler) {
        return (T) HANDLER;
      }
    }
    return super.getCapability(capability, facing);
  }

  public WireConnections getConnections() {
    return connections;
  }

  int i = 0;
  public void setConnections(WireConnections connections) {
    if (this.connections.getData() != connections.getData()) {
      this.connections = connections;
      transporter = null;
      if (this.worldObj != null) {
        if (worldObj.isRemote) {
          worldObj.markBlockRangeForRenderUpdate(getPos(), getPos());
        } else {
          markDirty();
          transporter = connections.getData() == 0 ? null : new WireEnergyTransporter(worldObj, getPos(), connections);
          worldObj.addBlockEvent(getPos(), getBlockType(), _CONN, connections.getData());
        }
      } else {
        // caller is readFromNBT(), so cont'd in setWorldObj()
      }
    }
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    setConnections(new WireConnections(compound.getInteger("conn")));
  }

  @Override
  public void setWorldObj(World worldIn) {
    super.setWorldObj(worldIn);
    if (!worldObj.isRemote && transporter == null && connections.getData() != 0) {
      transporter = new WireEnergyTransporter(worldObj, getPos(), connections);
    }
  }

  @Override
  public void writeToNBT(NBTTagCompound compound) {
    super.writeToNBT(compound);
    compound.setInteger("conn", connections.getData());
  }

  @Override
  public Packet getDescriptionPacket() {
    NBTTagCompound compound = new NBTTagCompound();
    compound.setInteger("conn", connections.getData());
    return new S35PacketUpdateTileEntity(getPos(), getBlockMetadata(), compound);
  }

  @Override
  public boolean receiveClientEvent(int id, int type) {
    if (id == _CONN) {
      if (this.worldObj != null && worldObj.isRemote) {
        setConnections(new WireConnections(type));
      }
      return true;
    }
    return false;
  }

  @Override
  public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
    setConnections(new WireConnections(pkt.getNbtCompound().getInteger("conn")));
  }

  @Override
  public void update() {
    if (transporter != null) {
      transporter.transport();
    }
  }

}
