package com.google.android.gms.maps;

import android.graphics.Bitmap;
import android.location.Location;
import android.os.RemoteException;
import android.view.View;
import com.google.android.gms.dynamic.d;
import com.google.android.gms.dynamic.e;
import com.google.android.gms.internal.fq;
import com.google.android.gms.maps.LocationSource.OnLocationChangedListener;
import com.google.android.gms.maps.internal.IGoogleMapDelegate;
import com.google.android.gms.maps.internal.h;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.IndoorBuilding;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.internal.c;
import com.google.android.gms.maps.model.internal.f;

public final class GoogleMap {
    public static final int MAP_TYPE_HYBRID = 4;
    public static final int MAP_TYPE_NONE = 0;
    public static final int MAP_TYPE_NORMAL = 1;
    public static final int MAP_TYPE_SATELLITE = 2;
    public static final int MAP_TYPE_TERRAIN = 3;
    private final IGoogleMapDelegate Rp;
    private UiSettings Rq;

    public static interface CancelableCallback {
        void onCancel();

        void onFinish();
    }

    public static interface InfoWindowAdapter {
        View getInfoContents(Marker marker);

        View getInfoWindow(Marker marker);
    }

    public static interface OnCameraChangeListener {
        void onCameraChange(CameraPosition cameraPosition);
    }

    public static interface OnIndoorStateChangeListener {
        void onIndoorBuildingFocused();

        void onIndoorLevelActivated(IndoorBuilding indoorBuilding);
    }

    public static interface OnInfoWindowClickListener {
        void onInfoWindowClick(Marker marker);
    }

    public static interface OnMapClickListener {
        void onMapClick(LatLng latLng);
    }

    public static interface OnMapLoadedCallback {
        void onMapLoaded();
    }

    public static interface OnMapLongClickListener {
        void onMapLongClick(LatLng latLng);
    }

    public static interface OnMarkerClickListener {
        boolean onMarkerClick(Marker marker);
    }

    public static interface OnMarkerDragListener {
        void onMarkerDrag(Marker marker);

        void onMarkerDragEnd(Marker marker);

        void onMarkerDragStart(Marker marker);
    }

    public static interface OnMyLocationButtonClickListener {
        boolean onMyLocationButtonClick();
    }

    @Deprecated
    public static interface OnMyLocationChangeListener {
        void onMyLocationChange(Location location);
    }

    public static interface SnapshotReadyCallback {
        void onSnapshotReady(Bitmap bitmap);
    }

    class AnonymousClass_10 extends com.google.android.gms.maps.internal.l.a {
        final /* synthetic */ com.google.android.gms.maps.GoogleMap.OnMarkerClickListener RD;

        AnonymousClass_10(com.google.android.gms.maps.GoogleMap.OnMarkerClickListener onMarkerClickListener) {
            this.RD = onMarkerClickListener;
        }

        public boolean a(f fVar) {
            return this.RD.onMarkerClick(new Marker(fVar));
        }
    }

    class AnonymousClass_11 extends com.google.android.gms.maps.internal.m.a {
        final /* synthetic */ com.google.android.gms.maps.GoogleMap.OnMarkerDragListener RE;

        AnonymousClass_11(com.google.android.gms.maps.GoogleMap.OnMarkerDragListener onMarkerDragListener) {
            this.RE = onMarkerDragListener;
        }

        public void b(f fVar) {
            this.RE.onMarkerDragStart(new Marker(fVar));
        }

        public void c(f fVar) {
            this.RE.onMarkerDragEnd(new Marker(fVar));
        }

        public void d(f fVar) {
            this.RE.onMarkerDrag(new Marker(fVar));
        }
    }

    class AnonymousClass_12 extends com.google.android.gms.maps.internal.g.a {
        final /* synthetic */ com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener RF;

        AnonymousClass_12(com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener onInfoWindowClickListener) {
            this.RF = onInfoWindowClickListener;
        }

        public void e(f fVar) {
            this.RF.onInfoWindowClick(new Marker(fVar));
        }
    }

    class AnonymousClass_13 extends com.google.android.gms.maps.internal.d.a {
        final /* synthetic */ com.google.android.gms.maps.GoogleMap.InfoWindowAdapter RG;

        AnonymousClass_13(com.google.android.gms.maps.GoogleMap.InfoWindowAdapter infoWindowAdapter) {
            this.RG = infoWindowAdapter;
        }

        public d f(f fVar) {
            return e.h(this.RG.getInfoWindow(new Marker(fVar)));
        }

        public d g(f fVar) {
            return e.h(this.RG.getInfoContents(new Marker(fVar)));
        }
    }

    class AnonymousClass_1 extends com.google.android.gms.maps.internal.f.a {
        final /* synthetic */ com.google.android.gms.maps.GoogleMap.OnIndoorStateChangeListener Rr;

        AnonymousClass_1(com.google.android.gms.maps.GoogleMap.OnIndoorStateChangeListener onIndoorStateChangeListener) {
            this.Rr = onIndoorStateChangeListener;
        }

        public void a(com.google.android.gms.maps.model.internal.d dVar) {
            this.Rr.onIndoorLevelActivated(new IndoorBuilding(dVar));
        }

        public void onIndoorBuildingFocused() {
            this.Rr.onIndoorBuildingFocused();
        }
    }

    class AnonymousClass_2 extends com.google.android.gms.maps.internal.o.a {
        final /* synthetic */ com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener Rt;

        AnonymousClass_2(com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener onMyLocationChangeListener) {
            this.Rt = onMyLocationChangeListener;
        }

        public void e(d dVar) {
            this.Rt.onMyLocationChange((Location) e.d(dVar));
        }
    }

    class AnonymousClass_3 extends com.google.android.gms.maps.internal.n.a {
        final /* synthetic */ com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener Ru;

        AnonymousClass_3(com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener onMyLocationButtonClickListener) {
            this.Ru = onMyLocationButtonClickListener;
        }

        public boolean onMyLocationButtonClick() throws RemoteException {
            return this.Ru.onMyLocationButtonClick();
        }
    }

    class AnonymousClass_4 extends com.google.android.gms.maps.internal.j.a {
        final /* synthetic */ com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback Rv;

        AnonymousClass_4(com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback onMapLoadedCallback) {
            this.Rv = onMapLoadedCallback;
        }

        public void onMapLoaded() throws RemoteException {
            this.Rv.onMapLoaded();
        }
    }

    class AnonymousClass_5 extends com.google.android.gms.maps.internal.s.a {
        final /* synthetic */ com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback Rw;

        AnonymousClass_5(com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback snapshotReadyCallback) {
            this.Rw = snapshotReadyCallback;
        }

        public void f(d dVar) throws RemoteException {
            this.Rw.onSnapshotReady((Bitmap) e.d(dVar));
        }

        public void onSnapshotReady(Bitmap snapshot) throws RemoteException {
            this.Rw.onSnapshotReady(snapshot);
        }
    }

    class AnonymousClass_6 extends com.google.android.gms.maps.internal.ILocationSourceDelegate.a {
        final /* synthetic */ LocationSource Rx;

        class AnonymousClass_1 implements OnLocationChangedListener {
            final /* synthetic */ h Ry;

            AnonymousClass_1(h hVar) {
                this.Ry = hVar;
            }

            public void onLocationChanged(Location location) {
                try {
                    this.Ry.j(e.h(location));
                } catch (RemoteException e) {
                    throw new RuntimeRemoteException(e);
                }
            }
        }

        AnonymousClass_6(LocationSource locationSource) {
            this.Rx = locationSource;
        }

        public void activate(h listener) {
            this.Rx.activate(new AnonymousClass_1(listener));
        }

        public void deactivate() {
            this.Rx.deactivate();
        }
    }

    class AnonymousClass_7 extends com.google.android.gms.maps.internal.e.a {
        final /* synthetic */ com.google.android.gms.maps.GoogleMap.OnCameraChangeListener RA;

        AnonymousClass_7(com.google.android.gms.maps.GoogleMap.OnCameraChangeListener onCameraChangeListener) {
            this.RA = onCameraChangeListener;
        }

        public void onCameraChange(CameraPosition position) {
            this.RA.onCameraChange(position);
        }
    }

    class AnonymousClass_8 extends com.google.android.gms.maps.internal.i.a {
        final /* synthetic */ com.google.android.gms.maps.GoogleMap.OnMapClickListener RB;

        AnonymousClass_8(com.google.android.gms.maps.GoogleMap.OnMapClickListener onMapClickListener) {
            this.RB = onMapClickListener;
        }

        public void onMapClick(LatLng point) {
            this.RB.onMapClick(point);
        }
    }

    class AnonymousClass_9 extends com.google.android.gms.maps.internal.k.a {
        final /* synthetic */ com.google.android.gms.maps.GoogleMap.OnMapLongClickListener RC;

        AnonymousClass_9(com.google.android.gms.maps.GoogleMap.OnMapLongClickListener onMapLongClickListener) {
            this.RC = onMapLongClickListener;
        }

        public void onMapLongClick(LatLng point) {
            this.RC.onMapLongClick(point);
        }
    }

    private static final class a extends com.google.android.gms.maps.internal.b.a {
        private final com.google.android.gms.maps.GoogleMap.CancelableCallback RH;

        a(com.google.android.gms.maps.GoogleMap.CancelableCallback cancelableCallback) {
            this.RH = cancelableCallback;
        }

        public void onCancel() {
            this.RH.onCancel();
        }

        public void onFinish() {
            this.RH.onFinish();
        }
    }

    protected GoogleMap(IGoogleMapDelegate map) {
        this.Rp = (IGoogleMapDelegate) fq.f(map);
    }

    public final Circle addCircle(CircleOptions options) {
        try {
            return new Circle(this.Rp.addCircle(options));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final GroundOverlay addGroundOverlay(GroundOverlayOptions options) {
        try {
            c addGroundOverlay = this.Rp.addGroundOverlay(options);
            return addGroundOverlay != null ? new GroundOverlay(addGroundOverlay) : null;
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final Marker addMarker(MarkerOptions options) {
        try {
            f addMarker = this.Rp.addMarker(options);
            return addMarker != null ? new Marker(addMarker) : null;
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final Polygon addPolygon(PolygonOptions options) {
        try {
            return new Polygon(this.Rp.addPolygon(options));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final Polyline addPolyline(PolylineOptions options) {
        try {
            return new Polyline(this.Rp.addPolyline(options));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final TileOverlay addTileOverlay(TileOverlayOptions options) {
        try {
            com.google.android.gms.maps.model.internal.h addTileOverlay = this.Rp.addTileOverlay(options);
            return addTileOverlay != null ? new TileOverlay(addTileOverlay) : null;
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void animateCamera(CameraUpdate update) {
        try {
            this.Rp.animateCamera(update.id());
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void animateCamera(CameraUpdate update, int durationMs, CancelableCallback callback) {
        try {
            this.Rp.animateCameraWithDurationAndCallback(update.id(), durationMs, callback == null ? null : new a(callback));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void animateCamera(CameraUpdate update, CancelableCallback callback) {
        try {
            this.Rp.animateCameraWithCallback(update.id(), callback == null ? null : new a(callback));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void clear() {
        try {
            this.Rp.clear();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final CameraPosition getCameraPosition() {
        try {
            return this.Rp.getCameraPosition();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public IndoorBuilding getFocusedBuilding() {
        try {
            com.google.android.gms.maps.model.internal.d focusedBuilding = this.Rp.getFocusedBuilding();
            return focusedBuilding != null ? new IndoorBuilding(focusedBuilding) : null;
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final int getMapType() {
        try {
            return this.Rp.getMapType();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final float getMaxZoomLevel() {
        try {
            return this.Rp.getMaxZoomLevel();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final float getMinZoomLevel() {
        try {
            return this.Rp.getMinZoomLevel();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    @Deprecated
    public final Location getMyLocation() {
        try {
            return this.Rp.getMyLocation();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final Projection getProjection() {
        try {
            return new Projection(this.Rp.getProjection());
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final UiSettings getUiSettings() {
        try {
            if (this.Rq == null) {
                this.Rq = new UiSettings(this.Rp.getUiSettings());
            }
            return this.Rq;
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    IGoogleMapDelegate if() {
        return this.Rp;
    }

    public final boolean isBuildingsEnabled() {
        try {
            return this.Rp.isBuildingsEnabled();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean isIndoorEnabled() {
        try {
            return this.Rp.isIndoorEnabled();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean isMyLocationEnabled() {
        try {
            return this.Rp.isMyLocationEnabled();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean isTrafficEnabled() {
        try {
            return this.Rp.isTrafficEnabled();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void moveCamera(CameraUpdate update) {
        try {
            this.Rp.moveCamera(update.id());
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setBuildingsEnabled(boolean enabled) {
        try {
            this.Rp.setBuildingsEnabled(enabled);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean setIndoorEnabled(boolean enabled) {
        try {
            return this.Rp.setIndoorEnabled(enabled);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void setInfoWindowAdapter(com.google.android.gms.maps.GoogleMap.InfoWindowAdapter r3_adapter) {
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.maps.GoogleMap.setInfoWindowAdapter(com.google.android.gms.maps.GoogleMap$InfoWindowAdapter):void");
        /*
        r2 = this;
        if (r3 != 0) goto L_0x0009;
    L_0x0002:
        r0 = r2.Rp;	 Catch:{ RemoteException -> 0x0014 }
        r1 = 0;
        r0.setInfoWindowAdapter(r1);	 Catch:{ RemoteException -> 0x0014 }
    L_0x0008:
        return;
    L_0x0009:
        r0 = r2.Rp;	 Catch:{ RemoteException -> 0x0014 }
        r1 = new com.google.android.gms.maps.GoogleMap$13;	 Catch:{ RemoteException -> 0x0014 }
        r1.<init>(r3);	 Catch:{ RemoteException -> 0x0014 }
        r0.setInfoWindowAdapter(r1);	 Catch:{ RemoteException -> 0x0014 }
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
    public final void setLocationSource(com.google.android.gms.maps.LocationSource r3_source) {
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.maps.GoogleMap.setLocationSource(com.google.android.gms.maps.LocationSource):void");
        /*
        r2 = this;
        if (r3 != 0) goto L_0x0009;
    L_0x0002:
        r0 = r2.Rp;	 Catch:{ RemoteException -> 0x0014 }
        r1 = 0;
        r0.setLocationSource(r1);	 Catch:{ RemoteException -> 0x0014 }
    L_0x0008:
        return;
    L_0x0009:
        r0 = r2.Rp;	 Catch:{ RemoteException -> 0x0014 }
        r1 = new com.google.android.gms.maps.GoogleMap$6;	 Catch:{ RemoteException -> 0x0014 }
        r1.<init>(r3);	 Catch:{ RemoteException -> 0x0014 }
        r0.setLocationSource(r1);	 Catch:{ RemoteException -> 0x0014 }
        goto L_0x0008;
    L_0x0014:
        r0 = move-exception;
        r1 = new com.google.android.gms.maps.model.RuntimeRemoteException;
        r1.<init>(r0);
        throw r1;
        */
    }

    public final void setMapType(int type) {
        try {
            this.Rp.setMapType(type);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setMyLocationEnabled(boolean enabled) {
        try {
            this.Rp.setMyLocationEnabled(enabled);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void setOnCameraChangeListener(com.google.android.gms.maps.GoogleMap.OnCameraChangeListener r3_listener) {
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.maps.GoogleMap.setOnCameraChangeListener(com.google.android.gms.maps.GoogleMap$OnCameraChangeListener):void");
        /*
        r2 = this;
        if (r3 != 0) goto L_0x0009;
    L_0x0002:
        r0 = r2.Rp;	 Catch:{ RemoteException -> 0x0014 }
        r1 = 0;
        r0.setOnCameraChangeListener(r1);	 Catch:{ RemoteException -> 0x0014 }
    L_0x0008:
        return;
    L_0x0009:
        r0 = r2.Rp;	 Catch:{ RemoteException -> 0x0014 }
        r1 = new com.google.android.gms.maps.GoogleMap$7;	 Catch:{ RemoteException -> 0x0014 }
        r1.<init>(r3);	 Catch:{ RemoteException -> 0x0014 }
        r0.setOnCameraChangeListener(r1);	 Catch:{ RemoteException -> 0x0014 }
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
    public final void setOnIndoorStateChangeListener(com.google.android.gms.maps.GoogleMap.OnIndoorStateChangeListener r3_listener) {
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.maps.GoogleMap.setOnIndoorStateChangeListener(com.google.android.gms.maps.GoogleMap$OnIndoorStateChangeListener):void");
        /*
        r2 = this;
        if (r3 != 0) goto L_0x0009;
    L_0x0002:
        r0 = r2.Rp;	 Catch:{ RemoteException -> 0x0014 }
        r1 = 0;
        r0.setOnIndoorStateChangeListener(r1);	 Catch:{ RemoteException -> 0x0014 }
    L_0x0008:
        return;
    L_0x0009:
        r0 = r2.Rp;	 Catch:{ RemoteException -> 0x0014 }
        r1 = new com.google.android.gms.maps.GoogleMap$1;	 Catch:{ RemoteException -> 0x0014 }
        r1.<init>(r3);	 Catch:{ RemoteException -> 0x0014 }
        r0.setOnIndoorStateChangeListener(r1);	 Catch:{ RemoteException -> 0x0014 }
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
    public final void setOnInfoWindowClickListener(com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener r3_listener) {
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.maps.GoogleMap.setOnInfoWindowClickListener(com.google.android.gms.maps.GoogleMap$OnInfoWindowClickListener):void");
        /*
        r2 = this;
        if (r3 != 0) goto L_0x0009;
    L_0x0002:
        r0 = r2.Rp;	 Catch:{ RemoteException -> 0x0014 }
        r1 = 0;
        r0.setOnInfoWindowClickListener(r1);	 Catch:{ RemoteException -> 0x0014 }
    L_0x0008:
        return;
    L_0x0009:
        r0 = r2.Rp;	 Catch:{ RemoteException -> 0x0014 }
        r1 = new com.google.android.gms.maps.GoogleMap$12;	 Catch:{ RemoteException -> 0x0014 }
        r1.<init>(r3);	 Catch:{ RemoteException -> 0x0014 }
        r0.setOnInfoWindowClickListener(r1);	 Catch:{ RemoteException -> 0x0014 }
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
    public final void setOnMapClickListener(com.google.android.gms.maps.GoogleMap.OnMapClickListener r3_listener) {
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.maps.GoogleMap.setOnMapClickListener(com.google.android.gms.maps.GoogleMap$OnMapClickListener):void");
        /*
        r2 = this;
        if (r3 != 0) goto L_0x0009;
    L_0x0002:
        r0 = r2.Rp;	 Catch:{ RemoteException -> 0x0014 }
        r1 = 0;
        r0.setOnMapClickListener(r1);	 Catch:{ RemoteException -> 0x0014 }
    L_0x0008:
        return;
    L_0x0009:
        r0 = r2.Rp;	 Catch:{ RemoteException -> 0x0014 }
        r1 = new com.google.android.gms.maps.GoogleMap$8;	 Catch:{ RemoteException -> 0x0014 }
        r1.<init>(r3);	 Catch:{ RemoteException -> 0x0014 }
        r0.setOnMapClickListener(r1);	 Catch:{ RemoteException -> 0x0014 }
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
    public void setOnMapLoadedCallback(com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback r3_callback) {
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.maps.GoogleMap.setOnMapLoadedCallback(com.google.android.gms.maps.GoogleMap$OnMapLoadedCallback):void");
        /*
        r2 = this;
        if (r3 != 0) goto L_0x0009;
    L_0x0002:
        r0 = r2.Rp;	 Catch:{ RemoteException -> 0x0014 }
        r1 = 0;
        r0.setOnMapLoadedCallback(r1);	 Catch:{ RemoteException -> 0x0014 }
    L_0x0008:
        return;
    L_0x0009:
        r0 = r2.Rp;	 Catch:{ RemoteException -> 0x0014 }
        r1 = new com.google.android.gms.maps.GoogleMap$4;	 Catch:{ RemoteException -> 0x0014 }
        r1.<init>(r3);	 Catch:{ RemoteException -> 0x0014 }
        r0.setOnMapLoadedCallback(r1);	 Catch:{ RemoteException -> 0x0014 }
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
    public final void setOnMapLongClickListener(com.google.android.gms.maps.GoogleMap.OnMapLongClickListener r3_listener) {
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.maps.GoogleMap.setOnMapLongClickListener(com.google.android.gms.maps.GoogleMap$OnMapLongClickListener):void");
        /*
        r2 = this;
        if (r3 != 0) goto L_0x0009;
    L_0x0002:
        r0 = r2.Rp;	 Catch:{ RemoteException -> 0x0014 }
        r1 = 0;
        r0.setOnMapLongClickListener(r1);	 Catch:{ RemoteException -> 0x0014 }
    L_0x0008:
        return;
    L_0x0009:
        r0 = r2.Rp;	 Catch:{ RemoteException -> 0x0014 }
        r1 = new com.google.android.gms.maps.GoogleMap$9;	 Catch:{ RemoteException -> 0x0014 }
        r1.<init>(r3);	 Catch:{ RemoteException -> 0x0014 }
        r0.setOnMapLongClickListener(r1);	 Catch:{ RemoteException -> 0x0014 }
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
    public final void setOnMarkerClickListener(com.google.android.gms.maps.GoogleMap.OnMarkerClickListener r3_listener) {
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.maps.GoogleMap.setOnMarkerClickListener(com.google.android.gms.maps.GoogleMap$OnMarkerClickListener):void");
        /*
        r2 = this;
        if (r3 != 0) goto L_0x0009;
    L_0x0002:
        r0 = r2.Rp;	 Catch:{ RemoteException -> 0x0014 }
        r1 = 0;
        r0.setOnMarkerClickListener(r1);	 Catch:{ RemoteException -> 0x0014 }
    L_0x0008:
        return;
    L_0x0009:
        r0 = r2.Rp;	 Catch:{ RemoteException -> 0x0014 }
        r1 = new com.google.android.gms.maps.GoogleMap$10;	 Catch:{ RemoteException -> 0x0014 }
        r1.<init>(r3);	 Catch:{ RemoteException -> 0x0014 }
        r0.setOnMarkerClickListener(r1);	 Catch:{ RemoteException -> 0x0014 }
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
    public final void setOnMarkerDragListener(com.google.android.gms.maps.GoogleMap.OnMarkerDragListener r3_listener) {
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.maps.GoogleMap.setOnMarkerDragListener(com.google.android.gms.maps.GoogleMap$OnMarkerDragListener):void");
        /*
        r2 = this;
        if (r3 != 0) goto L_0x0009;
    L_0x0002:
        r0 = r2.Rp;	 Catch:{ RemoteException -> 0x0014 }
        r1 = 0;
        r0.setOnMarkerDragListener(r1);	 Catch:{ RemoteException -> 0x0014 }
    L_0x0008:
        return;
    L_0x0009:
        r0 = r2.Rp;	 Catch:{ RemoteException -> 0x0014 }
        r1 = new com.google.android.gms.maps.GoogleMap$11;	 Catch:{ RemoteException -> 0x0014 }
        r1.<init>(r3);	 Catch:{ RemoteException -> 0x0014 }
        r0.setOnMarkerDragListener(r1);	 Catch:{ RemoteException -> 0x0014 }
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
    public final void setOnMyLocationButtonClickListener(com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener r3_listener) {
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.maps.GoogleMap.setOnMyLocationButtonClickListener(com.google.android.gms.maps.GoogleMap$OnMyLocationButtonClickListener):void");
        /*
        r2 = this;
        if (r3 != 0) goto L_0x0009;
    L_0x0002:
        r0 = r2.Rp;	 Catch:{ RemoteException -> 0x0014 }
        r1 = 0;
        r0.setOnMyLocationButtonClickListener(r1);	 Catch:{ RemoteException -> 0x0014 }
    L_0x0008:
        return;
    L_0x0009:
        r0 = r2.Rp;	 Catch:{ RemoteException -> 0x0014 }
        r1 = new com.google.android.gms.maps.GoogleMap$3;	 Catch:{ RemoteException -> 0x0014 }
        r1.<init>(r3);	 Catch:{ RemoteException -> 0x0014 }
        r0.setOnMyLocationButtonClickListener(r1);	 Catch:{ RemoteException -> 0x0014 }
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
    @java.lang.Deprecated
    public final void setOnMyLocationChangeListener(com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener r3_listener) {
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.maps.GoogleMap.setOnMyLocationChangeListener(com.google.android.gms.maps.GoogleMap$OnMyLocationChangeListener):void");
        /*
        r2 = this;
        if (r3 != 0) goto L_0x0009;
    L_0x0002:
        r0 = r2.Rp;	 Catch:{ RemoteException -> 0x0014 }
        r1 = 0;
        r0.setOnMyLocationChangeListener(r1);	 Catch:{ RemoteException -> 0x0014 }
    L_0x0008:
        return;
    L_0x0009:
        r0 = r2.Rp;	 Catch:{ RemoteException -> 0x0014 }
        r1 = new com.google.android.gms.maps.GoogleMap$2;	 Catch:{ RemoteException -> 0x0014 }
        r1.<init>(r3);	 Catch:{ RemoteException -> 0x0014 }
        r0.setOnMyLocationChangeListener(r1);	 Catch:{ RemoteException -> 0x0014 }
        goto L_0x0008;
    L_0x0014:
        r0 = move-exception;
        r1 = new com.google.android.gms.maps.model.RuntimeRemoteException;
        r1.<init>(r0);
        throw r1;
        */
    }

    public final void setPadding(int left, int top, int right, int bottom) {
        try {
            this.Rp.setPadding(left, top, right, bottom);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setTrafficEnabled(boolean enabled) {
        try {
            this.Rp.setTrafficEnabled(enabled);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void snapshot(SnapshotReadyCallback callback) {
        snapshot(callback, null);
    }

    public final void snapshot(SnapshotReadyCallback callback, Bitmap bitmap) {
        try {
            this.Rp.snapshot(new AnonymousClass_5(callback), (e) (bitmap != null ? e.h(bitmap) : null));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void stopAnimation() {
        try {
            this.Rp.stopAnimation();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }
}