package com.inmobi.re.controller;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.inmobi.re.controller.JSController.PlayerProperties;

// compiled from: JSController.java
static class f implements Creator<PlayerProperties> {
    f() {
    }

    public PlayerProperties a(Parcel parcel) {
        return new PlayerProperties(parcel);
    }

    public PlayerProperties[] a(int i) {
        return new PlayerProperties[i];
    }

    public /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* bridge */ /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}