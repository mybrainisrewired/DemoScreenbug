package com.google.android.gms.internal;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.dynamic.d.a;
import com.google.android.gms.dynamic.e;

public final class ce implements SafeParcelable {
    public static final cd CREATOR;
    public final dx kK;
    public final String nO;
    public final cb og;
    public final u oh;
    public final cf oi;
    public final dz oj;
    public final az ok;
    public final String ol;
    public final boolean om;
    public final String on;
    public final ci oo;
    public final int op;
    public final bc oq;
    public final String or;
    public final int orientation;
    public final int versionCode;

    static {
        CREATOR = new cd();
    }

    ce(int i, cb cbVar, IBinder iBinder, IBinder iBinder2, IBinder iBinder3, IBinder iBinder4, String str, boolean z, String str2, IBinder iBinder5, int i2, int i3, String str3, dx dxVar, IBinder iBinder6, String str4) {
        this.versionCode = i;
        this.og = cbVar;
        this.oh = (u) e.d(a.K(iBinder));
        this.oi = (cf) e.d(a.K(iBinder2));
        this.oj = (dz) e.d(a.K(iBinder3));
        this.ok = (az) e.d(a.K(iBinder4));
        this.ol = str;
        this.om = z;
        this.on = str2;
        this.oo = (ci) e.d(a.K(iBinder5));
        this.orientation = i2;
        this.op = i3;
        this.nO = str3;
        this.kK = dxVar;
        this.oq = (bc) e.d(a.K(iBinder6));
        this.or = str4;
    }

    public ce(cb cbVar, u uVar, cf cfVar, ci ciVar, dx dxVar) {
        this.versionCode = 3;
        this.og = cbVar;
        this.oh = uVar;
        this.oi = cfVar;
        this.oj = null;
        this.ok = null;
        this.ol = null;
        this.om = false;
        this.on = null;
        this.oo = ciVar;
        this.orientation = -1;
        this.op = 4;
        this.nO = null;
        this.kK = dxVar;
        this.oq = null;
        this.or = null;
    }

    public ce(u uVar, cf cfVar, az azVar, ci ciVar, dz dzVar, boolean z, int i, String str, dx dxVar, bc bcVar) {
        this.versionCode = 3;
        this.og = null;
        this.oh = uVar;
        this.oi = cfVar;
        this.oj = dzVar;
        this.ok = azVar;
        this.ol = null;
        this.om = z;
        this.on = null;
        this.oo = ciVar;
        this.orientation = i;
        this.op = 3;
        this.nO = str;
        this.kK = dxVar;
        this.oq = bcVar;
        this.or = null;
    }

    public ce(u uVar, cf cfVar, az azVar, ci ciVar, dz dzVar, boolean z, int i, String str, String str2, dx dxVar, bc bcVar) {
        this.versionCode = 3;
        this.og = null;
        this.oh = uVar;
        this.oi = cfVar;
        this.oj = dzVar;
        this.ok = azVar;
        this.ol = str2;
        this.om = z;
        this.on = str;
        this.oo = ciVar;
        this.orientation = i;
        this.op = 3;
        this.nO = null;
        this.kK = dxVar;
        this.oq = bcVar;
        this.or = null;
    }

    public ce(u uVar, cf cfVar, ci ciVar, dz dzVar, int i, dx dxVar, String str) {
        this.versionCode = 3;
        this.og = null;
        this.oh = uVar;
        this.oi = cfVar;
        this.oj = dzVar;
        this.ok = null;
        this.ol = null;
        this.om = false;
        this.on = null;
        this.oo = ciVar;
        this.orientation = i;
        this.op = 1;
        this.nO = null;
        this.kK = dxVar;
        this.oq = null;
        this.or = str;
    }

    public ce(u uVar, cf cfVar, ci ciVar, dz dzVar, boolean z, int i, dx dxVar) {
        this.versionCode = 3;
        this.og = null;
        this.oh = uVar;
        this.oi = cfVar;
        this.oj = dzVar;
        this.ok = null;
        this.ol = null;
        this.om = z;
        this.on = null;
        this.oo = ciVar;
        this.orientation = i;
        this.op = 2;
        this.nO = null;
        this.kK = dxVar;
        this.oq = null;
        this.or = null;
    }

    public static ce a(Intent intent) {
        try {
            Bundle bundleExtra = intent.getBundleExtra("com.google.android.gms.ads.inernal.overlay.AdOverlayInfo");
            bundleExtra.setClassLoader(ce.class.getClassLoader());
            return (ce) bundleExtra.getParcelable("com.google.android.gms.ads.inernal.overlay.AdOverlayInfo");
        } catch (Exception e) {
            return null;
        }
    }

    public static void a(Intent intent, ce ceVar) {
        Bundle bundle = new Bundle(1);
        bundle.putParcelable("com.google.android.gms.ads.inernal.overlay.AdOverlayInfo", ceVar);
        intent.putExtra("com.google.android.gms.ads.inernal.overlay.AdOverlayInfo", bundle);
    }

    IBinder aO() {
        return e.h(this.oh).asBinder();
    }

    IBinder aP() {
        return e.h(this.oi).asBinder();
    }

    IBinder aQ() {
        return e.h(this.oj).asBinder();
    }

    IBinder aR() {
        return e.h(this.ok).asBinder();
    }

    IBinder aS() {
        return e.h(this.oq).asBinder();
    }

    IBinder aT() {
        return e.h(this.oo).asBinder();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        cd.a(this, out, flags);
    }
}