package com.google.android.gms.drive.metadata;

import android.os.Bundle;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.internal.fq;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public abstract class a<T> implements MetadataField<T> {
    private final String FM;
    private final Set<String> FN;
    private final int FO;

    protected a(Object obj, int i) {
        this.FM = (String) fq.b(obj, (Object)"fieldName");
        this.FN = Collections.singleton(obj);
        this.FO = i;
    }

    protected a(Object obj, Collection<String> collection, int i) {
        this.FM = (String) fq.b(obj, (Object)"fieldName");
        this.FN = Collections.unmodifiableSet(new HashSet(collection));
        this.FO = i;
    }

    public final T a(DataHolder dataHolder, int i, int i2) {
        Iterator it = this.FN.iterator();
        while (it.hasNext()) {
            if (dataHolder.hasNull((String) it.next(), i, i2)) {
                return null;
            }
        }
        return b(dataHolder, i, i2);
    }

    protected abstract void a(Bundle bundle, T t);

    public final void a(Object obj, Object obj2, int i, int i2) {
        fq.b(obj, (Object)"dataHolder");
        fq.b(obj2, (Object)"bundle");
        obj2.b(this, a(obj, i, i2));
    }

    public final void a(Object obj, Bundle bundle) {
        fq.b((Object)bundle, (Object)"bundle");
        if (obj == null) {
            bundle.putString(getName(), null);
        } else {
            a(bundle, obj);
        }
    }

    protected abstract T b(DataHolder dataHolder, int i, int i2);

    public final T d(Object obj) {
        fq.b(obj, (Object)"bundle");
        return obj.get(getName()) != null ? e(obj) : null;
    }

    protected abstract T e(Bundle bundle);

    public final Collection<String> fR() {
        return this.FN;
    }

    public final String getName() {
        return this.FM;
    }

    public String toString() {
        return this.FM;
    }
}