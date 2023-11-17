package de.mrinformatic.multiphantomface.inventory;

import de.mrinformatic.multiphantomface.tile.TileMultiPhantomFace;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class MultiPhantomFaceInventory extends ItemStackHandler {
    private final TileMultiPhantomFace tileEntity;

    public MultiPhantomFaceInventory(TileMultiPhantomFace tileEntity) {
        this.tileEntity = tileEntity;
    }

    public MultiPhantomFaceInventory(int size, TileMultiPhantomFace tileEntity) {
        super(size);
        this.tileEntity = tileEntity;
    }

    public MultiPhantomFaceInventory(NonNullList<ItemStack> stacks, TileMultiPhantomFace tileEntity) {
        super(stacks);
        this.tileEntity = tileEntity;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return tileEntity.isItemValid(stack);
    }
}
