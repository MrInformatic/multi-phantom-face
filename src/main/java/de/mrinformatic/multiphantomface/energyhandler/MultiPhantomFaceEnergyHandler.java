package de.mrinformatic.multiphantomface.energyhandler;

import de.mrinformatic.multiphantomface.tile.TileMultiPhantomFace;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.Arrays;

public class MultiPhantomFaceEnergyHandler implements IEnergyStorage {
    private final TileMultiPhantomFace tileEntity;
    private final EnumFacing facing;
    private IEnergyStorage[] remoteEnergyStorage;
    private boolean dirty = false;

    public MultiPhantomFaceEnergyHandler(TileMultiPhantomFace tileEntity, EnumFacing facing) {
        this.tileEntity = tileEntity;
        this.facing = facing;
        remoteEnergyStorage = new IEnergyStorage[0];
    }

    public void makeDirty() {
        this.dirty = true;
    }

    public IEnergyStorage[] getRemoteEnergyStorage() {
        if(dirty) {
            this.remoteEnergyStorage = tileEntity.getRemoteEnergyStorage(facing);
        }
        return this.remoteEnergyStorage;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int energy = maxReceive;
        int energyFilled = 0;
        for(IEnergyStorage energyStorage: getRemoteEnergyStorage()) {
            int currentEnergyFilled = energyStorage.receiveEnergy(energy, simulate);
            energy -= currentEnergyFilled;
            energyFilled += currentEnergyFilled;
        }
        return energyFilled;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int energy = maxExtract;
        int energyDrained = 0;
        for(IEnergyStorage energyStorage: getRemoteEnergyStorage()) {
            int currentEnergyDrained = energyStorage.extractEnergy(energy, simulate);
            energy -= currentEnergyDrained;
            energyDrained += currentEnergyDrained;
        }
        return energyDrained;
    }

    @Override
    public int getEnergyStored() {
        return Arrays.stream(getRemoteEnergyStorage())
                .mapToInt(IEnergyStorage::getEnergyStored)
                .sum();
    }

    @Override
    public int getMaxEnergyStored() {
        return Arrays.stream(getRemoteEnergyStorage())
                .mapToInt(IEnergyStorage::getMaxEnergyStored)
                .sum();
    }

    @Override
    public boolean canExtract() {
        for(IEnergyStorage energyStorage: getRemoteEnergyStorage()) {
            if(energyStorage.canExtract()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canReceive() {
        for(IEnergyStorage energyStorage: getRemoteEnergyStorage()) {
            if(energyStorage.canReceive()) {
                return true;
            }
        }
        return false;
    }
}
