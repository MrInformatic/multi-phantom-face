package de.mrinformatic.multiphantomface.itemhandler;

import de.mrinformatic.multiphantomface.tile.TileMultiPhantomFace;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class MultiPhantomFaceItemHandler implements IItemHandler {
    private final TileMultiPhantomFace tileEntity;
    private final EnumFacing enumFacing;
    private IItemHandler[] remoteItemHandlers;
    private boolean dirty = false;

    public MultiPhantomFaceItemHandler(TileMultiPhantomFace tileEntity, EnumFacing enumFacing) {
        this.tileEntity = tileEntity;
        this.enumFacing = enumFacing;
        this.remoteItemHandlers = new IItemHandler[0];
    }

    public IItemHandler[] getRemoteItemHandlers() {
        if(dirty) {
            this.remoteItemHandlers = tileEntity.getRemoteItemHandler(enumFacing);
            dirty = false;
        }
        return this.remoteItemHandlers;
    }

    public void makeDirty() {
        dirty = true;
    }

    @Override
    public int getSlots() {
        return Arrays.stream(getRemoteItemHandlers())
                .mapToInt(IItemHandler::getSlots)
                .sum();
    }

    public SlotHandlerBundle getSlotHandlerBundle(int slot) {
        for(IItemHandler itemHandler: getRemoteItemHandlers()) {
            int currentSlots = itemHandler.getSlots();
            if(slot < currentSlots) {
                return new SlotHandlerBundle(itemHandler, slot);
            } else {
                slot -= currentSlots;
            }
        }
        throw new IllegalArgumentException("Requested slot out of range!");
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return getSlotHandlerBundle(slot).getStackInSlot();
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return getSlotHandlerBundle(slot).insertItem(stack, simulate);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return getSlotHandlerBundle(slot).extractItem(amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return getSlotHandlerBundle(slot).getSlotLimit();
    }

    public static final class SlotHandlerBundle {
        private final IItemHandler itemHandler;
        private final int slot;

        private SlotHandlerBundle(IItemHandler itemHandler, int slot) {
            this.itemHandler = itemHandler;
            this.slot = slot;
        }

        public IItemHandler getItemHandler() {
            return itemHandler;
        }

        public int getSlot() {
            return slot;
        }

        @Nonnull
        public ItemStack getStackInSlot() {
            return itemHandler.getStackInSlot(slot);
        }

        @Nonnull
        public ItemStack insertItem(@Nonnull ItemStack stack, boolean simulate) {
            return itemHandler.insertItem(slot, stack, simulate);
        }

        @Nonnull
        public ItemStack extractItem(int amount, boolean simulate) {
            return itemHandler.extractItem(slot, amount, simulate);
        }

        public int getSlotLimit() {
            return itemHandler.getSlotLimit(slot);
        }
    }
}
