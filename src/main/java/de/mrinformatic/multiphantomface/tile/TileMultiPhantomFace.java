package de.mrinformatic.multiphantomface.tile;

import de.mrinformatic.multiphantomface.MultiPhantomFace;
import de.mrinformatic.multiphantomface.container.ContainerMultiPhantomFace;
import de.mrinformatic.multiphantomface.energyhandler.MultiPhantomFaceEnergyHandler;
import de.mrinformatic.multiphantomface.fluidhandler.MultiPhantomFaceFluidHandler;
import de.mrinformatic.multiphantomface.gui.GuiMultiPhantomFace;
import de.mrinformatic.multiphantomface.inventory.MultiPhantomFaceInventory;
import de.mrinformatic.multiphantomface.item.ItemPhantomFaceConnector;
import de.mrinformatic.multiphantomface.itemhandler.MultiPhantomFaceItemHandler;
import de.mrinformatic.multiphantomface.util.FacingMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TileMultiPhantomFace extends TileEntityBase implements ITickable {
    public static final int INVENTORY_SIZE = 27;

    private final MultiPhantomFaceInventory inventory;
    private final FacingMap<MultiPhantomFaceFluidHandler> fluidHandlers;
    private final FacingMap<MultiPhantomFaceItemHandler> itemHandlers;
    private final FacingMap<MultiPhantomFaceEnergyHandler> energyHandlers;

    public TileMultiPhantomFace() {
        inventory = new MultiPhantomFaceInventory(INVENTORY_SIZE, this);
        fluidHandlers = new FacingMap<>(MultiPhantomFaceFluidHandler.class, facing -> new MultiPhantomFaceFluidHandler(this, facing));
        itemHandlers = new FacingMap<>(MultiPhantomFaceItemHandler.class, facing -> new MultiPhantomFaceItemHandler(this, facing));
        energyHandlers = new FacingMap<>(MultiPhantomFaceEnergyHandler.class, facing -> new MultiPhantomFaceEnergyHandler(this, facing));
    }

    public void makeRemoteCapabilitiesDirty() {
        for(MultiPhantomFaceFluidHandler fluidHandler: fluidHandlers.values()) {
            fluidHandler.makeDirty();
        }
        for(MultiPhantomFaceItemHandler itemHandler: itemHandlers.values()) {
            itemHandler.makeDirty();
        }
        for(MultiPhantomFaceEnergyHandler energyHandler: energyHandlers.values()) {
            energyHandler.makeDirty();
        }
    }

    public IItemHandler[] getRemoteItemHandler(EnumFacing facing) {
        return IntStream.range(0, inventory.getSlots())
                .mapToObj(inventory::getStackInSlot)
                .filter(stack -> isPhantomFaceConnectorItem(stack.getItem()))
                .map(ItemPhantomFaceConnector::getConnection)
                .map(connection -> this.getItemHandler(connection, facing))
                .filter(Objects::nonNull)
                .toArray(IItemHandler[]::new);
    }

    public boolean isPhantomFaceConnectorItem(Item item) {
        return item instanceof ItemPhantomFaceConnector &&
                ((ItemPhantomFaceConnector) item).getType() == ItemPhantomFaceConnector.Type.ITEM;
    }

    public IItemHandler getItemHandler(ItemPhantomFaceConnector.Connection connection, EnumFacing facing) {
        TileEntity tileEntity = world.getTileEntity(connection.getBlockPos());
        if(tileEntity != null) {
            return tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
        }
        return null;
    }

    public IFluidHandler[] getRemoteFluidHandler(EnumFacing facing) {
        return IntStream.range(0, inventory.getSlots())
                .mapToObj(inventory::getStackInSlot)
                .filter(stack -> isPhantomFaceConnectorFluid(stack.getItem()))
                .map(ItemPhantomFaceConnector::getConnection)
                .map(connection -> this.getFluidHandler(connection, facing))
                .filter(Objects::nonNull)
                .toArray(IFluidHandler[]::new);
    }

    public boolean isPhantomFaceConnectorFluid(Item item) {
        return item instanceof ItemPhantomFaceConnector &&
                ((ItemPhantomFaceConnector) item).getType() == ItemPhantomFaceConnector.Type.FLUID;
    }

    public IFluidHandler getFluidHandler(ItemPhantomFaceConnector.Connection connection, EnumFacing facing) {
        TileEntity tileEntity = world.getTileEntity(connection.getBlockPos());
        if(tileEntity != null) {
            return tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing);
        }
        return null;
    }

    public IEnergyStorage[] getRemoteEnergyStorage(EnumFacing facing) {
        return IntStream.range(0, inventory.getSlots())
                .mapToObj(inventory::getStackInSlot)
                .filter(stack -> isPhantomFaceConnectorEnergy(stack.getItem()))
                .map(ItemPhantomFaceConnector::getConnection)
                .map(connection -> this.getEnergyStorage(connection, facing))
                .filter(Objects::nonNull)
                .toArray(IEnergyStorage[]::new);
    }

    public boolean isPhantomFaceConnectorEnergy(Item item) {
        return item instanceof ItemPhantomFaceConnector &&
                ((ItemPhantomFaceConnector) item).getType() == ItemPhantomFaceConnector.Type.ENERGY;
    }

    public IEnergyStorage getEnergyStorage(ItemPhantomFaceConnector.Connection connection, EnumFacing facing) {
        TileEntity tileEntity = world.getTileEntity(connection.getBlockPos());
        if(tileEntity != null) {
            return tileEntity.getCapability(CapabilityEnergy.ENERGY, facing);
        }
        return null;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        inventory.deserializeNBT(compound.getCompoundTag("Items"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);

        compound.setTag("Items", inventory.serializeNBT());

        return compound;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ||
                capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ||
                capability == CapabilityEnergy.ENERGY;
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) itemHandlers.get(facing);
        }
        if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return (T) fluidHandlers.get(facing);
        }
        if(capability == CapabilityEnergy.ENERGY) {
            return (T) energyHandlers.get(facing);
        }
        return super.getCapability(capability, facing);
    }

    public boolean isConnectionValid(ItemPhantomFaceConnector.Connection connection) {
        return connection.getDimension() == world.provider.getDimension() &&
                connection.getBlockPos().distanceSq(pos.getX(), pos.getY(), pos.getZ()) <= 576;
    }

    public boolean isItemValid(ItemStack item) {
        if (item.getItem() instanceof ItemPhantomFaceConnector) {
            ItemPhantomFaceConnector.Connection connection = ItemPhantomFaceConnector.getConnection(item);
            return connection != null && isConnectionValid(connection);
        }
        return false;
    }

    @Override
    public Object getServerGuiElement(EntityPlayer player) {
        return new ContainerMultiPhantomFace(player.inventory, this);
    }

    @Override
    public Object getClientGuiElement(EntityPlayer player) {
        return new GuiMultiPhantomFace(player.inventory, this);
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public boolean isUsableByPlayer(EntityPlayer player) {
        return player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
    }

    @Nullable
    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentTranslation("container." + MultiPhantomFace.MOD_ID + ".multi_phantom_face.name");
    }

    @Override
    public void update() {
        makeRemoteCapabilitiesDirty();
    }

    public void dropInventory() {
        Random random = new Random();

        IntStream.range(0, inventory.getSlots())
                .mapToObj(inventory::getStackInSlot)
                .forEach(stack -> {
                    float f = random.nextFloat() * 0.8F + 0.1F;
                    float f1 = random.nextFloat() * 0.8F + 0.1F;
                    float f2 = random.nextFloat() * 0.8F + 0.1F;

                    while (!stack.isEmpty())
                    {
                        EntityItem entityitem = new EntityItem(world, pos.getX() + (double)f, pos.getY() + (double)f1, pos.getZ() + (double)f2, stack.splitStack(random.nextInt(21) + 10));
                        float f3 = 0.05F;
                        entityitem.motionX = random.nextGaussian() * 0.05000000074505806D;
                        entityitem.motionY = random.nextGaussian() * 0.05000000074505806D + 0.20000000298023224D;
                        entityitem.motionZ = random.nextGaussian() * 0.05000000074505806D;
                        world.spawnEntity(entityitem);
                    }
                });

    }
}
