package com.inmobi.re.controller;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.inmobi.re.controller.JSController.ResizeProperties;

// compiled from: JSController.java
static class e implements Creator<ResizeProperties> {
    e() {
    }

    public ResizeProperties a(Parcel parcel) {
        return new ResizeProperties(parcel);
    }

    public ResizeProperties[] a(int i) {
        return new ResizeProperties[i];
    }

    public /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* bridge */ /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}