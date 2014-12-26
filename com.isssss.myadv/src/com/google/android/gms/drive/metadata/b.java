package com.google.android.gms.drive.metadata;

import com.google.android.gms.common.data.DataHolder;
import java.util.Collection;

public abstract class b<T> extends a<Collection<T>> {
    protected b(String str, int i) {
        super(str, i);
    }

    protected /* synthetic */ Object b(DataHolder dataHolder, int i, int i2) {
        return c(dataHolder, i, i2);
    }

    protected Collection<T> c(DataHolder dataHolder, int i, int i2) {
        throw new UnsupportedOperationException("Cannot read collections from a dataHolder.");
    }
}