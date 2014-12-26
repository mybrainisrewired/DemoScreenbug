package com.inmobi.re.controller;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.inmobi.re.controller.JSController.ExpandProperties;

// compiled from: JSController.java
static class d implements Creator<ExpandProperties> {
    d() {
    }

    public ExpandProperties a(Parcel parcel) {
        return new ExpandProperties(parcel);
    }

    public ExpandProperties[] a(int i) {
        return new ExpandProperties[i];
    }

    public /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* bridge */ /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}