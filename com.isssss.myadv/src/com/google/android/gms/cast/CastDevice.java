package com.google.android.gms.cast;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.images.WebImage;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.eo;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CastDevice implements SafeParcelable {
    public static final Creator<CastDevice> CREATOR;
    private final int xH;
    private String ya;
    String yb;
    private Inet4Address yc;
    private String yd;
    private String ye;
    private String yf;
    private int yg;
    private List<WebImage> yh;

    static {
        CREATOR = new b();
    }

    private CastDevice() {
        this(1, null, null, null, null, null, -1, new ArrayList());
    }

    CastDevice(int versionCode, String deviceId, String hostAddress, String friendlyName, String modelName, String deviceVersion, int servicePort, List<WebImage> icons) {
        this.xH = versionCode;
        this.ya = deviceId;
        this.yb = hostAddress;
        if (this.yb != null) {
            try {
                InetAddress byName = InetAddress.getByName(this.yb);
                if (byName instanceof Inet4Address) {
                    this.yc = (Inet4Address) byName;
                }
            } catch (UnknownHostException e) {
                this.yc = null;
            }
        }
        this.yd = friendlyName;
        this.ye = modelName;
        this.yf = deviceVersion;
        this.yg = servicePort;
        this.yh = icons;
    }

    public static CastDevice getFromBundle(Bundle extras) {
        if (extras == null) {
            return null;
        }
        extras.setClassLoader(CastDevice.class.getClassLoader());
        return (CastDevice) extras.getParcelable("com.google.android.gms.cast.EXTRA_CAST_DEVICE");
    }

    public int describeContents() {
        return 0;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(com.google.android.gms.cast.CastDevice r5_obj) {
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.cast.CastDevice.equals(java.lang.Object):boolean");
        /*
        r4 = this;
        r0 = 1;
        r1 = 0;
        if (r5 != r4) goto L_0x0005;
    L_0x0004:
        return r0;
    L_0x0005:
        r2 = r5 instanceof com.google.android.gms.cast.CastDevice;
        if (r2 != 0) goto L_0x000b;
    L_0x0009:
        r0 = r1;
        goto L_0x0004;
    L_0x000b:
        r5 = (com.google.android.gms.cast.CastDevice) r5;
        r2 = r4.getDeviceId();
        if (r2 != 0) goto L_0x001b;
    L_0x0013:
        r2 = r5.getDeviceId();
        if (r2 == 0) goto L_0x0004;
    L_0x0019:
        r0 = r1;
        goto L_0x0004;
    L_0x001b:
        r2 = r4.ya;
        r3 = r5.ya;
        r2 = com.google.android.gms.internal.eo.a(r2, r3);
        if (r2 == 0) goto L_0x005d;
    L_0x0025:
        r2 = r4.yc;
        r3 = r5.yc;
        r2 = com.google.android.gms.internal.eo.a(r2, r3);
        if (r2 == 0) goto L_0x005d;
    L_0x002f:
        r2 = r4.ye;
        r3 = r5.ye;
        r2 = com.google.android.gms.internal.eo.a(r2, r3);
        if (r2 == 0) goto L_0x005d;
    L_0x0039:
        r2 = r4.yd;
        r3 = r5.yd;
        r2 = com.google.android.gms.internal.eo.a(r2, r3);
        if (r2 == 0) goto L_0x005d;
    L_0x0043:
        r2 = r4.yf;
        r3 = r5.yf;
        r2 = com.google.android.gms.internal.eo.a(r2, r3);
        if (r2 == 0) goto L_0x005d;
    L_0x004d:
        r2 = r4.yg;
        r3 = r5.yg;
        if (r2 != r3) goto L_0x005d;
    L_0x0053:
        r2 = r4.yh;
        r3 = r5.yh;
        r2 = com.google.android.gms.internal.eo.a(r2, r3);
        if (r2 != 0) goto L_0x0004;
    L_0x005d:
        r0 = r1;
        goto L_0x0004;
        */
    }

    public String getDeviceId() {
        return this.ya;
    }

    public String getDeviceVersion() {
        return this.yf;
    }

    public String getFriendlyName() {
        return this.yd;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.google.android.gms.common.images.WebImage getIcon(int r10_preferredWidth, int r11_preferredHeight) {
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.cast.CastDevice.getIcon(int, int):com.google.android.gms.common.images.WebImage");
        /*
        r9 = this;
        r7 = 0;
        r1 = 0;
        r0 = r9.yh;
        r0 = r0.isEmpty();
        if (r0 == 0) goto L_0x000b;
    L_0x000a:
        return r1;
    L_0x000b:
        if (r10 <= 0) goto L_0x000f;
    L_0x000d:
        if (r11 > 0) goto L_0x0019;
    L_0x000f:
        r0 = r9.yh;
        r0 = r0.get(r7);
        r0 = (com.google.android.gms.common.images.WebImage) r0;
        r1 = r0;
        goto L_0x000a;
    L_0x0019:
        r0 = r9.yh;
        r3 = r0.iterator();
        r2 = r1;
    L_0x0020:
        r0 = r3.hasNext();
        if (r0 == 0) goto L_0x0060;
    L_0x0026:
        r0 = r3.next();
        r0 = (com.google.android.gms.common.images.WebImage) r0;
        r4 = r0.getWidth();
        r5 = r0.getHeight();
        if (r4 < r10) goto L_0x004c;
    L_0x0036:
        if (r5 < r11) goto L_0x004c;
    L_0x0038:
        if (r2 == 0) goto L_0x0046;
    L_0x003a:
        r6 = r2.getWidth();
        if (r6 <= r4) goto L_0x0072;
    L_0x0040:
        r4 = r2.getHeight();
        if (r4 <= r5) goto L_0x0072;
    L_0x0046:
        r8 = r1;
        r1 = r0;
        r0 = r8;
    L_0x0049:
        r2 = r1;
        r1 = r0;
        goto L_0x0020;
    L_0x004c:
        if (r4 >= r10) goto L_0x0072;
    L_0x004e:
        if (r5 >= r11) goto L_0x0072;
    L_0x0050:
        if (r1 == 0) goto L_0x005e;
    L_0x0052:
        r6 = r1.getWidth();
        if (r6 >= r4) goto L_0x0072;
    L_0x0058:
        r4 = r1.getHeight();
        if (r4 >= r5) goto L_0x0072;
    L_0x005e:
        r1 = r2;
        goto L_0x0049;
    L_0x0060:
        if (r2 == 0) goto L_0x0064;
    L_0x0062:
        r1 = r2;
        goto L_0x000a;
    L_0x0064:
        if (r1 == 0) goto L_0x0068;
    L_0x0066:
        r2 = r1;
        goto L_0x0062;
    L_0x0068:
        r0 = r9.yh;
        r0 = r0.get(r7);
        r0 = (com.google.android.gms.common.images.WebImage) r0;
        r2 = r0;
        goto L_0x0062;
    L_0x0072:
        r0 = r1;
        r1 = r2;
        goto L_0x0049;
        */
    }

    public List<WebImage> getIcons() {
        return Collections.unmodifiableList(this.yh);
    }

    public Inet4Address getIpAddress() {
        return this.yc;
    }

    public String getModelName() {
        return this.ye;
    }

    public int getServicePort() {
        return this.yg;
    }

    int getVersionCode() {
        return this.xH;
    }

    public boolean hasIcons() {
        return !this.yh.isEmpty();
    }

    public int hashCode() {
        return this.ya == null ? 0 : this.ya.hashCode();
    }

    public boolean isSameDevice(CastDevice castDevice) {
        if (castDevice == null) {
            return false;
        }
        if (getDeviceId() == null) {
            return castDevice.getDeviceId() == null;
        } else {
            return eo.a(getDeviceId(), castDevice.getDeviceId());
        }
    }

    public void putInBundle(Bundle bundle) {
        if (bundle != null) {
            bundle.putParcelable("com.google.android.gms.cast.EXTRA_CAST_DEVICE", this);
        }
    }

    public String toString() {
        return String.format("\"%s\" (%s)", new Object[]{this.yd, this.ya});
    }

    public void writeToParcel(Parcel out, int flags) {
        b.a(this, out, flags);
    }
}