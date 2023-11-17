package de.mrinformatic.multiphantomface.gui;

import de.mrinformatic.multiphantomface.MultiPhantomFace;
import de.mrinformatic.multiphantomface.container.ContainerMultiPhantomFace;
import de.mrinformatic.multiphantomface.tile.TileEntityBase;
import de.mrinformatic.multiphantomface.tile.TileMultiPhantomFace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class GuiHandler implements IGuiHandler {
    @Nullable
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntityBase tileEntity = (TileEntityBase) world.getTileEntity(new BlockPos(x,y,z));
        return tileEntity.getServerGuiElement(player);
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntityBase tileEntity = (TileEntityBase) world.getTileEntity(new BlockPos(x,y,z));
        return tileEntity.getClientGuiElement(player);
    }
}
