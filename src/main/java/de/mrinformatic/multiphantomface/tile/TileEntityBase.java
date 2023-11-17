package de.mrinformatic.multiphantomface.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public abstract class TileEntityBase extends TileEntity {
    public abstract Object getServerGuiElement(EntityPlayer player);

    public abstract Object getClientGuiElement(EntityPlayer player);
}
