package info.loenwind.mves.impl.wire;

import info.loenwind.mves.MvesMod;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class TileMvesWire extends TileEntity {

  private static final int _CONN = 0xc011;
  private WireConnections connections = WireConnections.NONE;

  public TileMvesWire() {
    super();
  }

  @Override
  public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
    return (!worldObj.isRemote && capability == MvesMod.CAP_EnergyTransporter) || super.hasCapability(capability, facing);
  }

  @Override
  public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
    // TODO Auto-generated method stub
    return super.getCapability(capability, facing);
  }

  public WireConnections getConnections() {
    return connections;
  }

  public void setConnections(WireConnections connections) {
    if (this.connections.getData() != connections.getData()) {
      this.connections = connections;
      if (this.worldObj != null) {
        markDirty();
        if (worldObj.isRemote) {
          worldObj.markBlockRangeForRenderUpdate(getPos(), getPos());
        } else {
          worldObj.addBlockEvent(getPos(), getBlockType(), _CONN, connections.getData());
        }
      }
    }
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    setConnections(new WireConnections(compound.getInteger("conn")));
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
      setConnections(new WireConnections(type));
      return true;
    }
    return super.receiveClientEvent(id, type);
  }

  @Override
  public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
    setConnections(new WireConnections(pkt.getNbtCompound().getInteger("conn")));
  }

}
