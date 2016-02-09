package info.loenwind.mves.impl.wire;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemMvesWire extends Item {

  public static ItemMvesWire item;

  public static void create() {
    GameRegistry.registerItem(item = new ItemMvesWire(), "mves:mvesWireItem");
  }

  public ItemMvesWire() {
    this.setCreativeTab(CreativeTabs.tabRedstone);
    //        setUnlocalizedName("redstone");
    setPotionEffect(PotionHelper.redstoneEffect);
  }

  public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
    boolean flag = worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos);
    BlockPos blockpos = flag ? pos : pos.offset(side);

    if (!playerIn.canPlayerEdit(blockpos, side, stack)) {
      return false;
    } else {
      Block block = worldIn.getBlockState(blockpos).getBlock();

      if (!worldIn.canBlockBePlaced(block, blockpos, false, side, (Entity) null, stack)) {
        return false;
      } else if (BlockMvesWire.block.canPlaceBlockAt(worldIn, blockpos)) {
        --stack.stackSize;
        worldIn.setBlockState(blockpos, BlockMvesWire.block.getDefaultState());
        return true;
      } else {
        return false;
      }
    }
  }
}