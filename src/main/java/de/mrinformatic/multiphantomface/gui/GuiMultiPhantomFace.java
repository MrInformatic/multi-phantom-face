package de.mrinformatic.multiphantomface.gui;

import de.mrinformatic.multiphantomface.MultiPhantomFace;
import de.mrinformatic.multiphantomface.container.ContainerMultiPhantomFace;
import de.mrinformatic.multiphantomface.tile.TileMultiPhantomFace;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiMultiPhantomFace extends GuiContainer {
    private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
    private static final int INVENTORY_ROWS = 3;

    private final InventoryPlayer playerInventory;
    private final TileMultiPhantomFace multiPhantomFaceInventory;

    public GuiMultiPhantomFace(InventoryPlayer playerInventory, TileMultiPhantomFace multiPhantomFaceInventory)
    {
        super(new ContainerMultiPhantomFace(playerInventory, multiPhantomFaceInventory));
        this.playerInventory = playerInventory;
        this.multiPhantomFaceInventory = multiPhantomFaceInventory;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRenderer.drawString(this.multiPhantomFaceInventory.getDisplayName().getUnformattedText(), 8, 6, 4210752);
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 3, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(CHEST_GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, INVENTORY_ROWS * 18 + 17);
        this.drawTexturedModalRect(i, j + INVENTORY_ROWS * 18 + 17, 0, 126, this.xSize, 96);
    }
}
