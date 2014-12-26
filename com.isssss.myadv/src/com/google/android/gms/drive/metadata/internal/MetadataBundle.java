package com.google.android.gms.drive.metadata.internal;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.Log;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.drive.metadata.MetadataField;
import com.google.android.gms.internal.fo;
import com.google.android.gms.internal.fq;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public final class MetadataBundle implements SafeParcelable {
    public static final Creator<MetadataBundle> CREATOR;
    final Bundle FQ;
    final int xH;

    static {
        CREATOR = new f();
    }

    MetadataBundle(int versionCode, Bundle valueBundle) {
        this.xH = versionCode;
        this.FQ = (Bundle) fq.f(valueBundle);
        this.FQ.setClassLoader(getClass().getClassLoader());
        List arrayList = new ArrayList();
        Iterator it = this.FQ.keySet().iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            if (c.ax(str) == null) {
                arrayList.add(str);
                Log.w("MetadataBundle", "Ignored unknown metadata field in bundle: " + str);
            }
        }
        Iterator it2 = arrayList.iterator();
        while (it2.hasNext()) {
            this.FQ.remove((String) it2.next());
        }
    }

    private MetadataBundle(Bundle valueBundle) {
        this(1, valueBundle);
    }

    public static <T> MetadataBundle a(MetadataField<T> metadataField, T t) {
        MetadataBundle fT = fT();
        fT.b(metadataField, t);
        return fT;
    }

    public static MetadataBundle a(MetadataBundle metadataBundle) {
        return new MetadataBundle(new Bundle(metadataBundle.FQ));
    }

    public static MetadataBundle fT() {
        return new MetadataBundle(new Bundle());
    }

    public <T> T a(MetadataField<T> metadataField) {
        return metadataField.d(this.FQ);
    }

    public <T> void b(MetadataField<T> metadataField, T t) {
        if (c.ax(metadataField.getName()) == null) {
            throw new IllegalArgumentException("Unregistered field: " + metadataField.getName());
        }
        metadataField.a(t, this.FQ);
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(MetadataBundle obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MetadataBundle)) {
            return false;
        }
        obj = obj;
        Set keySet = this.FQ.keySet();
        if (!keySet.equals(obj.FQ.keySet())) {
            return false;
        }
        Iterator it = keySet.iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            if (!fo.equal(this.FQ.get(str), obj.FQ.get(str))) {
                return false;
            }
        }
        return true;
    }

    public Set<MetadataField<?>> fU() {
        Set<MetadataField<?>> hashSet = new HashSet();
        Iterator it = this.FQ.keySet().iterator();
        while (it.hasNext()) {
            hashSet.add(c.ax((String) it.next()));
        }
        return hashSet;
    }

    public int hashCode() {
        Iterator it = this.FQ.keySet().iterator();
        int i = 1;
        while (it.hasNext()) {
            i *= 31;
            i = this.FQ.get((String) it.next()).hashCode() + i;
        }
        return i;
    }

    public String toString() {
        return "MetadataBundle [values=" + this.FQ + "]";
    }

    public void writeToParcel(Parcel dest, int flags) {
        f.a(this, dest, flags);
    }
}