package com.inmobi.re.controller;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.inmobi.re.controller.JSController.Dimensions;

// compiled from: JSController.java
static class b implements Creator<Dimensions> {
    b() {
    }

    public Dimensions a(Parcel parcel) {
        return new Dimensions(parcel);
    }

    public Dimensions[] a(int i) {
        return new Dimensions[i];
    }

    public /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* bridge */ /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}