package de.mrinformatic.multiphantomface.block;

import de.mrinformatic.multiphantomface.MultiPhantomFace;
import de.mrinformatic.multiphantomface.gui.GuiHandler;
import de.mrinformatic.multiphantomface.tile.TileMultiPhantomFace;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class BlockMultiPhantomFace extends BlockContainer {
    public BlockMultiPhantomFace() {
        super(Material.ROCK);
        this.setRegistryName(MultiPhantomFace.MOD_ID, "block_multi_phantom_face");
        this.setUnlocalizedName(MultiPhantomFace.MOD_ID + ".multi_phantom_face");
        this.setCreativeTab(CreativeTabs.REDSTONE);
        this.setHarvestLevel("pickaxe", 0);
        this.setHardness(4.5F);
        this.setResistance(10.0F);
        this.setSoundType(SoundType.STONE);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileMultiPhantomFace();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!world.isRemote) {
            TileEntity tileEntity = world.getTileEntity(pos);

            if(tileEntity instanceof TileMultiPhantomFace) {
                System.out.println(player.getClass().getName());
                player.openGui(MultiPhantomFace.INSTANCE, 0, world, pos.getX(), pos.getY(), pos.getZ());
            }
        }

        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        if(!world.isRemote) {
            TileEntity tileEntity = world.getTileEntity(pos);

            if (tileEntity instanceof TileMultiPhantomFace) {
                ((TileMultiPhantomFace) tileEntity).dropInventory();
            }
        }

        super.breakBlock(world, pos, state);
    }
}
