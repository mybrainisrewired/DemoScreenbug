package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.fo;
import com.google.android.gms.internal.fq;
import com.google.android.gms.maps.internal.v;

public final class LatLngBounds implements SafeParcelable {
    public static final LatLngBoundsCreator CREATOR;
    public final LatLng northeast;
    public final LatLng southwest;
    private final int xH;

    public static final class Builder {
        private double Ta;
        private double Tb;
        private double Tc;
        private double Td;

        public Builder() {
            this.Ta = Double.POSITIVE_INFINITY;
            this.Tb = Double.NEGATIVE_INFINITY;
            this.Tc = Double.NaN;
            this.Td = Double.NaN;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private boolean d(double r7) {
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.maps.model.LatLngBounds.Builder.d(double):boolean");
            /*
            r6 = this;
            r0 = 1;
            r1 = 0;
            r2 = r6.Tc;
            r4 = r6.Td;
            r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
            if (r2 > 0) goto L_0x0019;
        L_0x000a:
            r2 = r6.Tc;
            r2 = (r2 > r7 ? 1 : (r2 == r7 ? 0 : -1));
            if (r2 > 0) goto L_0x0017;
        L_0x0010:
            r2 = r6.Td;
            r2 = (r7 > r2 ? 1 : (r7 == r2 ? 0 : -1));
            if (r2 > 0) goto L_0x0017;
        L_0x0016:
            return r0;
        L_0x0017:
            r0 = r1;
            goto L_0x0016;
        L_0x0019:
            r2 = r6.Tc;
            r2 = (r2 > r7 ? 1 : (r2 == r7 ? 0 : -1));
            if (r2 <= 0) goto L_0x0025;
        L_0x001f:
            r2 = r6.Td;
            r2 = (r7 > r2 ? 1 : (r7 == r2 ? 0 : -1));
            if (r2 > 0) goto L_0x0026;
        L_0x0025:
            r1 = r0;
        L_0x0026:
            r0 = r1;
            goto L_0x0016;
            */
        }

        public LatLngBounds build() {
            fq.a(!Double.isNaN(this.Tc), "no included points");
            return new LatLngBounds(new LatLng(this.Ta, this.Tc), new LatLng(this.Tb, this.Td));
        }

        public com.google.android.gms.maps.model.LatLngBounds.Builder include(LatLng point) {
            this.Ta = Math.min(this.Ta, point.latitude);
            this.Tb = Math.max(this.Tb, point.latitude);
            double d = point.longitude;
            if (Double.isNaN(this.Tc)) {
                this.Tc = d;
                this.Td = d;
            } else if (!d(d)) {
                if (LatLngBounds.b(this.Tc, d) < LatLngBounds.c(this.Td, d)) {
                    this.Tc = d;
                } else {
                    this.Td = d;
                }
            }
            return this;
        }
    }

    static {
        CREATOR = new LatLngBoundsCreator();
    }

    LatLngBounds(int versionCode, Object southwest, Object northeast) {
        fq.b(southwest, (Object)"null southwest");
        fq.b(northeast, (Object)"null northeast");
        fq.a(northeast.latitude >= southwest.latitude, "southern latitude exceeds northern latitude (%s > %s)", new Object[]{Double.valueOf(southwest.latitude), Double.valueOf(northeast.latitude)});
        this.xH = versionCode;
        this.southwest = southwest;
        this.northeast = northeast;
    }

    public LatLngBounds(LatLng southwest, LatLng northeast) {
        this(1, southwest, northeast);
    }

    private static double b(double d, double d2) {
        return ((d - d2) + 360.0d) % 360.0d;
    }

    public static Builder builder() {
        return new Builder();
    }

    private static double c(double d, double d2) {
        return ((d2 - d) + 360.0d) % 360.0d;
    }

    private boolean c(double d) {
        return this.southwest.latitude <= d && d <= this.northeast.latitude;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean d(double r7) {
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.maps.model.LatLngBounds.d(double):boolean");
        /*
        r6 = this;
        r0 = 1;
        r1 = 0;
        r2 = r6.southwest;
        r2 = r2.longitude;
        r4 = r6.northeast;
        r4 = r4.longitude;
        r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r2 > 0) goto L_0x0021;
    L_0x000e:
        r2 = r6.southwest;
        r2 = r2.longitude;
        r2 = (r2 > r7 ? 1 : (r2 == r7 ? 0 : -1));
        if (r2 > 0) goto L_0x001f;
    L_0x0016:
        r2 = r6.northeast;
        r2 = r2.longitude;
        r2 = (r7 > r2 ? 1 : (r7 == r2 ? 0 : -1));
        if (r2 > 0) goto L_0x001f;
    L_0x001e:
        return r0;
    L_0x001f:
        r0 = r1;
        goto L_0x001e;
    L_0x0021:
        r2 = r6.southwest;
        r2 = r2.longitude;
        r2 = (r2 > r7 ? 1 : (r2 == r7 ? 0 : -1));
        if (r2 <= 0) goto L_0x0031;
    L_0x0029:
        r2 = r6.northeast;
        r2 = r2.longitude;
        r2 = (r7 > r2 ? 1 : (r7 == r2 ? 0 : -1));
        if (r2 > 0) goto L_0x0032;
    L_0x0031:
        r1 = r0;
    L_0x0032:
        r0 = r1;
        goto L_0x001e;
        */
    }

    public boolean contains(LatLng point) {
        return c(point.latitude) && d(point.longitude);
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(LatLngBounds o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LatLngBounds)) {
            return false;
        }
        o = o;
        return this.southwest.equals(o.southwest) && this.northeast.equals(o.northeast);
    }

    public LatLng getCenter() {
        double d = (this.southwest.latitude + this.northeast.latitude) / 2.0d;
        double d2 = this.northeast.longitude;
        double d3 = this.southwest.longitude;
        return new LatLng(d, d3 <= d2 ? (d2 + d3) / 2.0d : ((d2 + 360.0d) + d3) / 2.0d);
    }

    int getVersionCode() {
        return this.xH;
    }

    public int hashCode() {
        return fo.hashCode(new Object[]{this.southwest, this.northeast});
    }

    public LatLngBounds including(LatLng point) {
        double min = Math.min(this.southwest.latitude, point.latitude);
        double max = Math.max(this.northeast.latitude, point.latitude);
        double d = this.northeast.longitude;
        double d2 = this.southwest.longitude;
        double d3 = point.longitude;
        if (d(d3)) {
            d3 = d2;
            d2 = d;
        } else if (b(d2, d3) < c(d, d3)) {
            d2 = d;
        } else {
            double d4 = d2;
            d2 = d3;
            d3 = d4;
        }
        return new LatLngBounds(new LatLng(min, d3), new LatLng(max, d2));
    }

    public String toString() {
        return fo.e(this).a("southwest", this.southwest).a("northeast", this.northeast).toString();
    }

    public void writeToParcel(Parcel out, int flags) {
        if (v.iB()) {
            d.a(this, out, flags);
        } else {
            LatLngBoundsCreator.a(this, out, flags);
        }
    }
}