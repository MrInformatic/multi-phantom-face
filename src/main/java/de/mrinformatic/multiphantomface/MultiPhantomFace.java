package de.mrinformatic.multiphantomface;

import de.mrinformatic.multiphantomface.block.BlockMultiPhantomFace;
import de.mrinformatic.multiphantomface.gui.GuiHandler;
import de.mrinformatic.multiphantomface.item.ItemPhantomFaceConnector;
import de.mrinformatic.multiphantomface.tile.TileMultiPhantomFace;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Logger;

@Mod(modid = MultiPhantomFace.MOD_ID, version = "1.0.4", name = "Multi Phantom Face", useMetadata=true)
public class MultiPhantomFace {
    public static final String MOD_ID = "multiphantomface";

    public static final Block MULTI_PHANTOM_FACE_BLOCK = new BlockMultiPhantomFace();

    public static final Item PHANTOM_FACE_CONNECTOR_ITEMS = new ItemPhantomFaceConnector(ItemPhantomFaceConnector.Type.ITEM);
    public static final Item PHANTOM_FACE_CONNECTOR_FLUIDS = new ItemPhantomFaceConnector(ItemPhantomFaceConnector.Type.FLUID);
    public static final Item PHANTOM_FACE_CONNECTOR_ENERGY = new ItemPhantomFaceConnector(ItemPhantomFaceConnector.Type.ENERGY);


    public static MultiPhantomFace INSTANCE;
    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        INSTANCE = this;

        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
        GameRegistry.registerTileEntity(TileMultiPhantomFace.class, new ResourceLocation(MOD_ID, "tile_multi_phantom_face"));

        registerBlock(MULTI_PHANTOM_FACE_BLOCK);

        registerItem(PHANTOM_FACE_CONNECTOR_ITEMS);
        registerItem(PHANTOM_FACE_CONNECTOR_FLUIDS);
        registerItem(PHANTOM_FACE_CONNECTOR_ENERGY);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
    }

    public static void registerBlock(Block block) {
        ForgeRegistries.BLOCKS.register(block);

        registerItem(new ItemBlock(block).setRegistryName(block.getRegistryName()));
    }

    public static void registerItem(Item item) {
        ForgeRegistries.ITEMS.register(item);

        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }
}
