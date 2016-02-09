package info.loenwind.mves.impl.wire;

import info.loenwind.mves.impl.wire.WireConnections.EnumPosition;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
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

public class BlockMvesWire extends Block {

  public static BlockMvesWire block;

  public static void create() {
    GameRegistry.registerBlock(block = new BlockMvesWire(), null, "mvesWire");
    GameRegistry.registerTileEntity(TileMvesWire.class, "mvesWire_te");
  }

  public static final PropertyEnum<EnumAttachPosition> NORTH = PropertyEnum.<EnumAttachPosition> create("north", EnumAttachPosition.class);
  public static final PropertyEnum<EnumAttachPosition> EAST = PropertyEnum.<EnumAttachPosition> create("east", EnumAttachPosition.class);
  public static final PropertyEnum<EnumAttachPosition> SOUTH = PropertyEnum.<EnumAttachPosition> create("south", EnumAttachPosition.class);
  public static final PropertyEnum<EnumAttachPosition> WEST = PropertyEnum.<EnumAttachPosition> create("west", EnumAttachPosition.class);

  // D-U-N-S-W-E
  private static final PropertyEnum[] mapping = { null, null, NORTH, SOUTH, WEST, EAST };

  public BlockMvesWire() {
    super(Material.circuits);
    this.setDefaultState(this.blockState.getBaseState().withProperty(NORTH, EnumAttachPosition.NONE).withProperty(EAST, EnumAttachPosition.NONE)
        .withProperty(SOUTH, EnumAttachPosition.NONE).withProperty(WEST, EnumAttachPosition.NONE));
    this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
    setHardness(0.0F);
    setStepSound(soundTypeStone);
    setUnlocalizedName("mvesWire");
    disableStats();
    //    setCreativeTab(CreativeTabs.tabRedstone);
  }

  public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
    WireConnections connections = new WireConnections(worldIn, pos);
    for (EnumFacing direction : EnumFacing.Plane.HORIZONTAL) {
      if (connections.is(direction, EnumPosition.ABOVE)) {
        state = state.withProperty(mapping[direction.ordinal()], EnumAttachPosition.UP);
      } else if (connections.is(direction, EnumPosition.ADJ) || connections.is(direction, EnumPosition.BELOW)) {
        state = state.withProperty(mapping[direction.ordinal()], EnumAttachPosition.SIDE);
      }
    }
    //    state = state.withProperty(WEST, this.getAttachPosition(worldIn, pos, EnumFacing.WEST));
    //    state = state.withProperty(EAST, this.getAttachPosition(worldIn, pos, EnumFacing.EAST));
    //    state = state.withProperty(NORTH, this.getAttachPosition(worldIn, pos, EnumFacing.NORTH));
    //    state = state.withProperty(SOUTH, this.getAttachPosition(worldIn, pos, EnumFacing.SOUTH));
    return state;
  }

  //  private EnumAttachPosition getAttachPosition(IBlockAccess worldIn, BlockPos pos, EnumFacing direction) {
  //    BlockPos blockPosTarget = pos.offset(direction);
  //    BlockPos blockPosAboveUs = pos.offset(EnumFacing.UP);
  //    BlockPos blockPosAboveTarget = blockPosTarget.offset(EnumFacing.UP);
  //    BlockPos blockPosBelowTarget = blockPosTarget.offset(EnumFacing.DOWN);
  //
  //    if (worldIn.isAirBlock(blockPosAboveUs) && worldIn.getBlockState(blockPosTarget).getBlock().isBlockNormalCube()
  //        && isConnectable(worldIn, blockPosAboveTarget, direction)) {
  //      return EnumAttachPosition.UP;
  //    }
  //    if (isConnectable(worldIn, blockPosTarget, direction)) {
  //      return EnumAttachPosition.SIDE;
  //    }
  //    if (worldIn.getBlockState(blockPosBelowTarget).getBlock() == this && worldIn.isAirBlock(blockPosTarget)) {
  //      return EnumAttachPosition.DOWN;
  //    }
  //    return EnumAttachPosition.NONE;
  //  }
  //
  //  private boolean isConnectable(IBlockAccess world, BlockPos blockPosTarget, EnumFacing direction) {
  //    TileEntity tileEntity = world.getTileEntity(blockPosTarget);
  //    if (tileEntity != null) {
  //      IEnergySupplier energySupplier = tileEntity.getCapability(MvesMod.CAP_EnergySupplier, direction.getOpposite());
  //      if (energySupplier != null) {
  //        return true;
  //      }
  //      IEnergyAcceptor energyAcceptor = tileEntity.getCapability(MvesMod.CAP_EnergyAcceptor, direction.getOpposite());
  //      if (energyAcceptor != null) {
  //        return true;
  //      }
  //      IEnergyTransporter energyTransporter = tileEntity.getCapability(MvesMod.CAP_EnergyTransporter, direction.getOpposite());
  //      if (energyTransporter instanceof IEnergyTransporterRelay) {
  //        return true;
  //      }
  //    }
  //    return false;
  //  }

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
    return iblockstate.getBlock() != this ? super.colorMultiplier(worldIn, pos, renderPass) : this.colorMultiplier(15);
  }

  @SideOnly(Side.CLIENT)
  private int colorMultiplier(int powerLevel) {
    float f = (float) powerLevel / 15.0F;
    float r = f * 0.6F + 0.4F;

    if (powerLevel == 0) {
      r = 0.3F;
    }

    float b = f * f * 0.7F - 0.5F;
    float g = f * f * 0.6F - 0.7F;

    int r255 = MathHelper.clamp_int((int) (g * 255.0F), 0, 255);
    int b255 = MathHelper.clamp_int((int) (b * 255.0F), 0, 255);
    int g255 = MathHelper.clamp_int((int) (r * 255.0F), 0, 255);
    return -16777216 | r255 << 16 | b255 << 8 | g255;
  }

  public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
    if (!worldIn.isRemote) {
      if (!canPlaceBlockAt(worldIn, pos)) {
        dropBlockAsItem(worldIn, pos, state, 0);
        worldIn.setBlockToAir(pos);
      }
    }
  }

  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return ItemMvesWire.item;
  }

  @SideOnly(Side.CLIENT)
  public Item getItem(World worldIn, BlockPos pos) {
    return ItemMvesWire.item;
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
    return new BlockState(this, new IProperty[] { NORTH, EAST, SOUTH, WEST });
  }

  @Override
  public boolean hasTileEntity(IBlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(World world, IBlockState state) {
    return new TileMvesWire();
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

}
