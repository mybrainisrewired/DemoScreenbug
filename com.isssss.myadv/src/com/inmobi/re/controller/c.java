package com.inmobi.re.controller;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.inmobi.re.controller.JSController.Properties;

// compiled from: JSController.java
static class c implements Creator<Properties> {
    c() {
    }

    public Properties a(Parcel parcel) {
        return new Properties(parcel);
    }

    public Properties[] a(int i) {
        return new Properties[i];
    }

    public /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* bridge */ /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }
}