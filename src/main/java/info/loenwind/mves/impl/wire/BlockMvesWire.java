package info.loenwind.mves.impl.wire;

import info.loenwind.mves.MvesMod;
import info.loenwind.mves.impl.wire.WireConnections.EnumConnection;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockMvesWire extends Block implements ITileEntityProvider {

  public static BlockMvesWire block;

  public static String name() {
    return "mvesWire";
  }

  public static void create() {
    GameRegistry.registerBlock(block = new BlockMvesWire(), name());
    GameRegistry.registerTileEntity(TileMvesWire.class, MvesMod.MODID + "_" + name() + "_te");
  }

  public static final PropertyEnum<EnumAttachPosition> NORTH = PropertyEnum.<EnumAttachPosition> create("north", EnumAttachPosition.class);
  public static final PropertyEnum<EnumAttachPosition> EAST = PropertyEnum.<EnumAttachPosition> create("east", EnumAttachPosition.class);
  public static final PropertyEnum<EnumAttachPosition> SOUTH = PropertyEnum.<EnumAttachPosition> create("south", EnumAttachPosition.class);
  public static final PropertyEnum<EnumAttachPosition> WEST = PropertyEnum.<EnumAttachPosition> create("west", EnumAttachPosition.class);
  public static final PropertyBool DOWN = PropertyBool.create("down");

  // D-U-N-S-W-E
  private static final IProperty[] mapping = { DOWN, null, NORTH, SOUTH, WEST, EAST };

  public BlockMvesWire() {
    super(Material.circuits);
    this.setDefaultState(this.blockState.getBaseState().withProperty(NORTH, EnumAttachPosition.NONE).withProperty(EAST, EnumAttachPosition.NONE)
        .withProperty(SOUTH, EnumAttachPosition.NONE).withProperty(WEST, EnumAttachPosition.NONE).withProperty(DOWN, true));
    this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
    setHardness(0.0F);
    setStepSound(soundTypeStone);
    setUnlocalizedName("mvesWire");
    setCreativeTab(CreativeTabs.tabRedstone);
    disableStats();
    isBlockContainer = true;
  }

  public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
    TileEntity tileEntity = worldIn.getTileEntity(pos);
    WireConnections connections = tileEntity instanceof TileMvesWire ? ((TileMvesWire) tileEntity).getConnections() : new WireConnections(0);
    int count = 0;
    for (EnumFacing direction : EnumFacing.Plane.HORIZONTAL) {
      if (connections.is(direction, EnumConnection.ABOVE)) {
        state = state.withProperty(mapping[direction.ordinal()], EnumAttachPosition.UP);
        count++;
      } else if (connections.is(direction, EnumConnection._ANY_LEVEL)) {
        state = state.withProperty(mapping[direction.ordinal()], EnumAttachPosition.SIDE);
        count++;
      }
    }
    state = state.withProperty(DOWN, count == 0 || connections.is(EnumFacing.DOWN, EnumConnection._ANY));
    return state;
  }

  public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
    return null;
  }

  public boolean isOpaqueCube() {
    return false;
  }

  public boolean isFullCube() {
    return false;
  }

  public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
    return World.doesBlockHaveSolidTopSurface(worldIn, pos.down()) || worldIn.getBlockState(pos.down()).getBlock() == Blocks.glowstone;
  }

  @SideOnly(Side.CLIENT)
  public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
    IBlockState iblockstate = worldIn.getBlockState(pos);
    return iblockstate.getBlock() != this ? super.colorMultiplier(worldIn, pos, renderPass) : this.calcColorMultiplier(worldIn, pos);
  }

  @SideOnly(Side.CLIENT)
  private int calcColorMultiplier(IBlockAccess worldIn, BlockPos pos) {
    float c0 = Math.abs((Math.abs(pos.getX()) % 160) - 80) / 80f;
    float c1 = Math.abs((pos.getY() % 16) - 8) / 8f;
    float c2 = Math.abs((Math.abs(pos.getZ()) % 80) - 40) / 40f;

    float r = c0 * 0.25F + c1 * 0.10f + (1f - c2) * 0.25f + 0.4F;
    float g = c0 * 0.10F + (1f - c1) * 0.30f + c2 * 0.20f + 0.4F;
    float b = (1f - c0) * 0.10F + c1 * 0.10f + c2 * 0.40f + 0.4F;

    int r255 = MathHelper.clamp_int((int) (r * 255.0F), 0, 255);
    int g255 = MathHelper.clamp_int((int) (g * 255.0F), 0, 255);
    int b255 = MathHelper.clamp_int((int) (b * 255.0F), 0, 255);

    return -16777216 | r255 << 16 | g255 << 8 | b255;
  }

  @SideOnly(Side.CLIENT)
  public EnumWorldBlockLayer getBlockLayer() {
    return EnumWorldBlockLayer.CUTOUT;
  }

  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState();
  }

  public int getMetaFromState(IBlockState state) {
    return 0;
  }

  protected BlockState createBlockState() {
    return new BlockState(this, new IProperty[] { NORTH, EAST, SOUTH, WEST, DOWN });
  }

  @Override
  public boolean hasTileEntity(IBlockState state) {
    return true;
  }

  protected static enum EnumAttachPosition implements IStringSerializable {
    UP("up"),
    SIDE("side"),
    NONE("none");

    private final String name;

    private EnumAttachPosition(String name) {
      this.name = name;
    }

    public String toString() {
      return this.getName();
    }

    public String getName() {
      return this.name;
    }
  }

  // TODO
  @Override
  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
    IBlockState actualState = worldIn.getBlockState(pos).getBlock().getActualState(state, worldIn, pos);
    TileEntity tileEntity = worldIn.getTileEntity(pos);
    WireConnections connections = tileEntity instanceof TileMvesWire ? ((TileMvesWire) tileEntity).getConnections() : null;
    System.out.println(pos + ": " + actualState + " " + connections);
    return true;
  }

  @Override
  public TileEntity createNewTileEntity(World worldIn, int meta) {
    return new TileMvesWire();
  }

  @Override
  public boolean onBlockEventReceived(World worldIn, BlockPos pos, IBlockState state, int eventID, int eventParam) {
    TileEntity tileEntity = worldIn.getTileEntity(pos);
    if (tileEntity instanceof TileMvesWire) {
      return tileEntity.receiveClientEvent(eventID, eventParam);
    }
    return super.onBlockEventReceived(worldIn, pos, state, eventID, eventParam);
  }

  @Override
  public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
    if (!worldIn.isRemote) {
      if (!canPlaceBlockAt(worldIn, pos)) {
        dropBlockAsItem(worldIn, pos, state, 0);
        worldIn.setBlockToAir(pos);
      } else {
        onNeighborChange(worldIn, pos, null);
      }
    }
  }

  @Override
  public void onNeighborChange(IBlockAccess worldIn, BlockPos pos, BlockPos neighbor) {
    TileEntity tileEntity = worldIn.getTileEntity(pos);
    if (tileEntity instanceof TileMvesWire && !tileEntity.getWorld().isRemote) {
      ((TileMvesWire) tileEntity).setConnections(new WireConnections(worldIn, pos));
    }
  }

  public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    TileEntity tileEntity = worldIn.getTileEntity(pos);
    if (tileEntity instanceof TileMvesWire && !worldIn.isRemote) {
      WireConnections connections = new WireConnections(worldIn, pos);
      ((TileMvesWire) tileEntity).setConnections(connections);
      notifyIndirectNeighbors(worldIn, pos, connections);
    }
  }

  private void notifyIndirectNeighbors(World worldIn, BlockPos pos, WireConnections connections) {
    for (EnumFacing direction : EnumFacing.Plane.HORIZONTAL) {
      if (connections.is(direction, EnumConnection.ABOVE)) {
        BlockPos posTarget = pos.offset(direction).up();
        Block blockTarget = worldIn.getBlockState(posTarget).getBlock();
        blockTarget.onNeighborBlockChange(worldIn, posTarget, worldIn.getBlockState(posTarget), this);
      } else if (connections.is(direction, EnumConnection.BELOW)) {
        BlockPos posTarget = pos.offset(direction).down();
        Block blockTarget = worldIn.getBlockState(posTarget).getBlock();
        blockTarget.onNeighborBlockChange(worldIn, posTarget, worldIn.getBlockState(posTarget), this);
      }

    }
  }

  @Override
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    TileEntity tileEntity = worldIn.getTileEntity(pos);
    if (tileEntity instanceof TileMvesWire && !worldIn.isRemote) {
      notifyIndirectNeighbors(worldIn, pos, ((TileMvesWire) tileEntity).getConnections());
    }
    super.breakBlock(worldIn, pos, state);
  }

}
