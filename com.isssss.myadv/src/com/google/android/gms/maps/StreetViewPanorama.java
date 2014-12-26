package com.google.android.gms.maps;

import android.graphics.Point;
import android.os.RemoteException;
import com.google.android.gms.dynamic.e;
import com.google.android.gms.internal.fq;
import com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate;
import com.google.android.gms.maps.internal.p;
import com.google.android.gms.maps.internal.q.a;
import com.google.android.gms.maps.internal.r;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;
import com.google.android.gms.maps.model.StreetViewPanoramaLocation;
import com.google.android.gms.maps.model.StreetViewPanoramaOrientation;

public class StreetViewPanorama {
    private final IStreetViewPanoramaDelegate Sd;

    public static interface OnStreetViewPanoramaCameraChangeListener {
        void onStreetViewPanoramaCameraChange(StreetViewPanoramaCamera streetViewPanoramaCamera);
    }

    public static interface OnStreetViewPanoramaChangeListener {
        void onStreetViewPanoramaChange(StreetViewPanoramaLocation streetViewPanoramaLocation);
    }

    public static interface OnStreetViewPanoramaClickListener {
        void onStreetViewPanoramaClick(StreetViewPanoramaOrientation streetViewPanoramaOrientation);
    }

    class AnonymousClass_1 extends a {
        final /* synthetic */ com.google.android.gms.maps.StreetViewPanorama.OnStreetViewPanoramaChangeListener Se;

        AnonymousClass_1(com.google.android.gms.maps.StreetViewPanorama.OnStreetViewPanoramaChangeListener onStreetViewPanoramaChangeListener) {
            this.Se = onStreetViewPanoramaChangeListener;
        }

        public void onStreetViewPanoramaChange(StreetViewPanoramaLocation location) {
            this.Se.onStreetViewPanoramaChange(location);
        }
    }

    class AnonymousClass_2 extends p.a {
        final /* synthetic */ com.google.android.gms.maps.StreetViewPanorama.OnStreetViewPanoramaCameraChangeListener Sg;

        AnonymousClass_2(com.google.android.gms.maps.StreetViewPanorama.OnStreetViewPanoramaCameraChangeListener onStreetViewPanoramaCameraChangeListener) {
            this.Sg = onStreetViewPanoramaCameraChangeListener;
        }

        public void onStreetViewPanoramaCameraChange(StreetViewPanoramaCamera camera) {
            this.Sg.onStreetViewPanoramaCameraChange(camera);
        }
    }

    class AnonymousClass_3 extends r.a {
        final /* synthetic */ com.google.android.gms.maps.StreetViewPanorama.OnStreetViewPanoramaClickListener Sh;

        AnonymousClass_3(com.google.android.gms.maps.StreetViewPanorama.OnStreetViewPanoramaClickListener onStreetViewPanoramaClickListener) {
            this.Sh = onStreetViewPanoramaClickListener;
        }

        public void onStreetViewPanoramaClick(StreetViewPanoramaOrientation orientation) {
            this.Sh.onStreetViewPanoramaClick(orientation);
        }
    }

    protected StreetViewPanorama(IStreetViewPanoramaDelegate sv) {
        this.Sd = (IStreetViewPanoramaDelegate) fq.f(sv);
    }

    public void animateTo(StreetViewPanoramaCamera camera, long duration) {
        try {
            this.Sd.animateTo(camera, duration);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public StreetViewPanoramaLocation getLocation() {
        try {
            return this.Sd.getStreetViewPanoramaLocation();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public StreetViewPanoramaCamera getPanoramaCamera() {
        try {
            return this.Sd.getPanoramaCamera();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    IStreetViewPanoramaDelegate ir() {
        return this.Sd;
    }

    public boolean isPanningGesturesEnabled() {
        try {
            return this.Sd.isPanningGesturesEnabled();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public boolean isStreetNamesEnabled() {
        try {
            return this.Sd.isStreetNamesEnabled();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public boolean isUserNavigationEnabled() {
        try {
            return this.Sd.isUserNavigationEnabled();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public boolean isZoomGesturesEnabled() {
        try {
            return this.Sd.isZoomGesturesEnabled();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public Point orientationToPoint(StreetViewPanoramaOrientation orientation) {
        try {
            return (Point) e.d(this.Sd.orientationToPoint(orientation));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public StreetViewPanoramaOrientation pointToOrientation(Point point) {
        try {
            return this.Sd.pointToOrientation(e.h(point));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void setOnStreetViewPanoramaCameraChangeListener(com.google.android.gms.maps.StreetViewPanorama.OnStreetViewPanoramaCameraChangeListener r3_listener) {
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.maps.StreetViewPanorama.setOnStreetViewPanoramaCameraChangeListener(com.google.android.gms.maps.StreetViewPanorama$OnStreetViewPanoramaCameraChangeListener):void");
        /*
        r2 = this;
        if (r3 != 0) goto L_0x0009;
    L_0x0002:
        r0 = r2.Sd;	 Catch:{ RemoteException -> 0x0014 }
        r1 = 0;
        r0.setOnStreetViewPanoramaCameraChangeListener(r1);	 Catch:{ RemoteException -> 0x0014 }
    L_0x0008:
        return;
    L_0x0009:
        r0 = r2.Sd;	 Catch:{ RemoteException -> 0x0014 }
        r1 = new com.google.android.gms.maps.StreetViewPanorama$2;	 Catch:{ RemoteException -> 0x0014 }
        r1.<init>(r3);	 Catch:{ RemoteException -> 0x0014 }
        r0.setOnStreetViewPanoramaCameraChangeListener(r1);	 Catch:{ RemoteException -> 0x0014 }
        goto L_0x0008;
    L_0x0014:
        r0 = move-exception;
        r1 = new com.google.android.gms.maps.model.RuntimeRemoteException;
        r1.<init>(r0);
        throw r1;
        */
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void setOnStreetViewPanoramaChangeListener(com.google.android.gms.maps.StreetViewPanorama.OnStreetViewPanoramaChangeListener r3_listener) {
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.maps.StreetViewPanorama.setOnStreetViewPanoramaChangeListener(com.google.android.gms.maps.StreetViewPanorama$OnStreetViewPanoramaChangeListener):void");
        /*
        r2 = this;
        if (r3 != 0) goto L_0x0009;
    L_0x0002:
        r0 = r2.Sd;	 Catch:{ RemoteException -> 0x0014 }
        r1 = 0;
        r0.setOnStreetViewPanoramaChangeListener(r1);	 Catch:{ RemoteException -> 0x0014 }
    L_0x0008:
        return;
    L_0x0009:
        r0 = r2.Sd;	 Catch:{ RemoteException -> 0x0014 }
        r1 = new com.google.android.gms.maps.StreetViewPanorama$1;	 Catch:{ RemoteException -> 0x0014 }
        r1.<init>(r3);	 Catch:{ RemoteException -> 0x0014 }
        r0.setOnStreetViewPanoramaChangeListener(r1);	 Catch:{ RemoteException -> 0x0014 }
        goto L_0x0008;
    L_0x0014:
        r0 = move-exception;
        r1 = new com.google.android.gms.maps.model.RuntimeRemoteException;
        r1.<init>(r0);
        throw r1;
        */
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void setOnStreetViewPanoramaClickListener(com.google.android.gms.maps.StreetViewPanorama.OnStreetViewPanoramaClickListener r3_listener) {
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.maps.StreetViewPanorama.setOnStreetViewPanoramaClickListener(com.google.android.gms.maps.StreetViewPanorama$OnStreetViewPanoramaClickListener):void");
        /*
        r2 = this;
        if (r3 != 0) goto L_0x0009;
    L_0x0002:
        r0 = r2.Sd;	 Catch:{ RemoteException -> 0x0014 }
        r1 = 0;
        r0.setOnStreetViewPanoramaClickListener(r1);	 Catch:{ RemoteException -> 0x0014 }
    L_0x0008:
        return;
    L_0x0009:
        r0 = r2.Sd;	 Catch:{ RemoteException -> 0x0014 }
        r1 = new com.google.android.gms.maps.StreetViewPanorama$3;	 Catch:{ RemoteException -> 0x0014 }
        r1.<init>(r3);	 Catch:{ RemoteException -> 0x0014 }
        r0.setOnStreetViewPanoramaClickListener(r1);	 Catch:{ RemoteException -> 0x0014 }
        goto L_0x0008;
    L_0x0014:
        r0 = move-exception;
        r1 = new com.google.android.gms.maps.model.RuntimeRemoteException;
        r1.<init>(r0);
        throw r1;
        */
    }

    public void setPanningGesturesEnabled(boolean enablePanning) {
        try {
            this.Sd.enablePanning(enablePanning);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public void setPosition(LatLng position) {
        try {
            this.Sd.setPosition(position);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public void setPosition(LatLng position, int radius) {
        try {
            this.Sd.setPositionWithRadius(position, radius);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public void setPosition(String panoId) {
        try {
            this.Sd.setPositionWithID(panoId);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public void setStreetNamesEnabled(boolean enableStreetNames) {
        try {
            this.Sd.enableStreetNames(enableStreetNames);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public void setUserNavigationEnabled(boolean enableUserNavigation) {
        try {
            this.Sd.enableUserNavigation(enableUserNavigation);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public void setZoomGesturesEnabled(boolean enableZoom) {
        try {
            this.Sd.enableZoom(enableZoom);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }
}