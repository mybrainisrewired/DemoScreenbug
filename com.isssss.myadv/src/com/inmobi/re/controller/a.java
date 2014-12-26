package com.inmobi.re.controller;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.inmobi.re.controller.JSController.OrientationProperties;

// compiled from: JSController.java
static class a implements Creator<OrientationProperties> {
    a() {
    }

    public OrientationProperties a(Parcel parcel) {
        return new OrientationProperties(parcel);
    }

    public OrientationProperties[] a(int i) {
        return new OrientationProperties[i];
    }

    public /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* bridge */ /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}