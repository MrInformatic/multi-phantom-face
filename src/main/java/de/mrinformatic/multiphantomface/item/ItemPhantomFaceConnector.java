package de.mrinformatic.multiphantomface.item;

import de.mrinformatic.multiphantomface.MultiPhantomFace;
import de.mrinformatic.multiphantomface.tile.TileMultiPhantomFace;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemPhantomFaceConnector extends Item {
    public static final String X_COORD_OF_TILE_STORED = "XCoordOfTileStored";
    public static final String Y_COORD_OF_TILE_STORED = "YCoordOfTileStored";
    public static final String Z_COORD_OF_TILE_STORED = "ZCoordOfTileStored";
    public static final String WORLD_OF_TILE_STORED = "WorldOfTileStored";

    private final Type type;

    public ItemPhantomFaceConnector(Type type) {
        this.type = type;

        switch (type) {
            case ITEM:
                this.setRegistryName(MultiPhantomFace.MOD_ID, "item_phantom_face_connector_items");
                this.setUnlocalizedName(MultiPhantomFace.MOD_ID + ".phantom_face_connector_items");
                break;
            case FLUID:
                this.setRegistryName(MultiPhantomFace.MOD_ID, "item_phantom_face_connector_fluids");
                this.setUnlocalizedName(MultiPhantomFace.MOD_ID + ".phantom_face_connector_fluids");
                break;
            case ENERGY:
                this.setRegistryName(MultiPhantomFace.MOD_ID, "item_phantom_face_connector_energy");
                this.setUnlocalizedName(MultiPhantomFace.MOD_ID + ".phantom_face_connector_energy");
                break;
        }

        this.setCreativeTab(CreativeTabs.REDSTONE);
        this.setMaxStackSize(1);
    }

    @Override
    public boolean getShareTag() {
        return true;
    }

    public static Connection getConnection(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null &&
                tag.hasKey(X_COORD_OF_TILE_STORED) &&
                tag.hasKey(Y_COORD_OF_TILE_STORED) &&
                tag.hasKey(Z_COORD_OF_TILE_STORED) &&
                tag.hasKey(WORLD_OF_TILE_STORED)) {
            int x = tag.getInteger(X_COORD_OF_TILE_STORED);
            int y = tag.getInteger(Y_COORD_OF_TILE_STORED);
            int z = tag.getInteger(Z_COORD_OF_TILE_STORED);
            int dimension = tag.getInteger(WORLD_OF_TILE_STORED);

            return new Connection(new BlockPos(x, y, z), dimension);
        }
        return null;
    }

    public static void storeConnection(ItemStack stack, int x, int y, int z, World world) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) {
            tag = new NBTTagCompound();
        }

        tag.setInteger(X_COORD_OF_TILE_STORED, x);
        tag.setInteger(Y_COORD_OF_TILE_STORED, y);
        tag.setInteger(Z_COORD_OF_TILE_STORED, z);
        tag.setInteger(WORLD_OF_TILE_STORED, world.provider.getDimension());

        stack.setTagCompound(tag);
    }

    public static void clearConnection(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null) {
            tag.removeTag(X_COORD_OF_TILE_STORED);
            tag.removeTag(Y_COORD_OF_TILE_STORED);
            tag.removeTag(Z_COORD_OF_TILE_STORED);
            tag.removeTag(WORLD_OF_TILE_STORED);
        }
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemStack = player.getHeldItem(hand);
        if(!world.isRemote) {
            TileEntity tile = world.getTileEntity(pos);

            if(tile instanceof TileMultiPhantomFace) {
                clearConnection(itemStack);

                return EnumActionResult.FAIL;
            }

            storeConnection(itemStack, pos.getX(), pos.getY(), pos.getZ(), world);
        }

        return EnumActionResult.SUCCESS;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        Connection coords = getConnection(stack);
        if (coords != null) {
            tooltip.add(I18n.format("tooltip." + MultiPhantomFace.MOD_ID + ".boundTo.desc") + ":");
            tooltip.add("X: " + coords.getBlockPos().getX());
            tooltip.add("Y: " + coords.getBlockPos().getY());
            tooltip.add("Z: " + coords.getBlockPos().getZ());
            tooltip.add("Dimension: " + coords.getDimension());
            tooltip.add(TextFormatting.ITALIC + I18n.format("tooltip." + MultiPhantomFace.MOD_ID + ".clearStorage.desc"));
        }
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        ITEM,
        FLUID,
        ENERGY
    }

    public static final class Connection {
        private final BlockPos blockPos;
        private final int dimension;

        private Connection(BlockPos blockPos, int dimension) {
            this.blockPos = blockPos;
            this.dimension = dimension;
        }

        public BlockPos getBlockPos() {
            return blockPos;
        }

        public int getDimension() {
            return dimension;
        }
    }
}
