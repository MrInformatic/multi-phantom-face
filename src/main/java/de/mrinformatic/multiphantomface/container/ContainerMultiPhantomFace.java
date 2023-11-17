package de.mrinformatic.multiphantomface.container;

import de.mrinformatic.multiphantomface.tile.TileMultiPhantomFace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerMultiPhantomFace extends Container {
    private static final int NUM_ROWS = 3;
    private static final int NUM_COLS = 9;

    private final TileMultiPhantomFace tileMultiPhantomFace;

    public ContainerMultiPhantomFace(InventoryPlayer playerInventory, TileMultiPhantomFace tileMultiPhantomFace) {
        this.tileMultiPhantomFace = tileMultiPhantomFace;
        for (int j = 0; j < NUM_ROWS; ++j) {
            for (int k = 0; k < NUM_COLS; ++k) {
                this.addSlotToContainer(new SlotItemHandler(tileMultiPhantomFace.getInventory(), k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 85 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 143));
        }
    }



    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return this.tileMultiPhantomFace.isUsableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            itemstack = slot.getStack().copy();

            if (index < NUM_ROWS * NUM_COLS) {
                if (!this.mergeItemStack(slot.getStack(), NUM_ROWS * NUM_COLS, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (tileMultiPhantomFace.isItemValid(slot.getStack())) {
                if (!this.mergeItemStack(slot.getStack(), 0, NUM_ROWS * NUM_COLS, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index < NUM_ROWS * NUM_COLS + 3 * 9) {
                if (!this.mergeItemStack(slot.getStack(), NUM_ROWS * NUM_COLS + 3 * 9, NUM_ROWS * NUM_COLS + 4 * 9, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index < NUM_ROWS * NUM_COLS + 4 * 9) {
                if (!this.mergeItemStack(slot.getStack(), NUM_ROWS * NUM_COLS, NUM_ROWS * NUM_COLS + 3 * 9, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (slot.getStack().isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (slot.getStack().getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, slot.getStack());
        }

        return itemstack;
    }
}
