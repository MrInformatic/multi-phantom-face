package de.mrinformatic.multiphantomface.fluidhandler;

import de.mrinformatic.multiphantomface.tile.TileMultiPhantomFace;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;
import java.util.Arrays;

public class MultiPhantomFaceFluidHandler implements IFluidHandler {
    private final TileMultiPhantomFace tileEntity;
    private final EnumFacing facing;
    private IFluidHandler[] remoteFluidHandlers;
    private boolean dirty = false;

    public MultiPhantomFaceFluidHandler(TileMultiPhantomFace tileEntity, EnumFacing facing) {
        this.tileEntity = tileEntity;
        this.facing = facing;
        this.remoteFluidHandlers = new IFluidHandler[0];
    }

    public IFluidHandler[] getRemoteFluidHandlers() {
        if(dirty) {
            this.remoteFluidHandlers = tileEntity.getRemoteFluidHandler(facing);
            dirty = false;
        }
        return this.remoteFluidHandlers;
    }

    @Override
    public IFluidTankProperties[] getTankProperties() {
        return Arrays.stream(this.getRemoteFluidHandlers())
                .flatMap(fluidHandler -> Arrays.stream(fluidHandler.getTankProperties()))
                .toArray(IFluidTankProperties[]::new);
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        int amount = resource.amount;
        int filledAmount = 0;
        for(IFluidHandler fluidHandler : this.getRemoteFluidHandlers()) {
            int currentFilledAmount = fluidHandler.fill(new FluidStack(resource.getFluid(), amount), doFill);
            filledAmount += currentFilledAmount;
            amount -= currentFilledAmount;
        }
        return filledAmount;
    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        int amount = resource.amount;
        int drainedAmount = 0;
        for(IFluidHandler fluidHandler: this.getRemoteFluidHandlers()) {
            FluidStack currentDrained = fluidHandler.drain(new FluidStack(resource.getFluid(), amount), doDrain);
            if(currentDrained != null && currentDrained.getFluid().equals(resource.getFluid())) {
                drainedAmount += currentDrained.amount;
                amount -= currentDrained.amount;
            }
        }
        return new FluidStack(resource.getFluid(), drainedAmount);
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        Fluid fluid = null;
        int amount = maxDrain;
        int drainedAmount = 0;

        for(IFluidHandler fluidHandler: this.getRemoteFluidHandlers()) {
            if(fluid == null) {
                FluidStack currentDrained = fluidHandler.drain(amount, doDrain);
                if(currentDrained != null && currentDrained.amount < 0) {
                    fluid = currentDrained.getFluid();
                    amount -= currentDrained.amount;
                    drainedAmount += currentDrained.amount;
                }
            } else {
                FluidStack currentDrained = fluidHandler.drain(new FluidStack(fluid, amount), doDrain);
                if(currentDrained != null && fluid.equals(currentDrained.getFluid())) {
                    amount -= currentDrained.amount;
                    drainedAmount += currentDrained.amount;
                }
            }
        }

        if(fluid != null) {
            return new FluidStack(fluid, drainedAmount);
        } else {
            return null;
        }
    }

    public void makeDirty() {
        dirty = true;
    }
}
