package de.mrinformatic.multiphantomface.util;

import net.minecraft.util.EnumFacing;

import java.util.function.Function;

public class FacingMap<T> {
    private final T[] values;

    public FacingMap(Class<T> type, Function<EnumFacing, T> mapper) {
        values = (T[]) java.lang.reflect.Array.newInstance(type, EnumFacing.values().length + 1);;
        for(int i = 0; i < values.length; i++) {
            if(i > 0) {
                values[i] = mapper.apply(EnumFacing.values()[i-1]);
            } else {
                values[i] = mapper.apply(null);
            }
        }
    }

    public T get(EnumFacing enumFacing) {
        if (enumFacing == null) {
            return values[0];
        } else {
            return values[enumFacing.ordinal() + 1];
        }
    }

    public T[] values() {
        return values;
    }
}
