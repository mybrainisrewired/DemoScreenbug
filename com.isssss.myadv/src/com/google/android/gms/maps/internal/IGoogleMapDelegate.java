package com.google.android.gms.maps.internal;

import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.d;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.internal.IPolylineDelegate;
import com.google.android.gms.maps.model.internal.b;
import com.google.android.gms.maps.model.internal.c;
import com.google.android.gms.maps.model.internal.f;
import com.google.android.gms.maps.model.internal.g;
import com.google.android.gms.maps.model.internal.h;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public interface IGoogleMapDelegate extends IInterface {

    public static abstract class a extends Binder implements IGoogleMapDelegate {

        private static class a implements IGoogleMapDelegate {
            private IBinder kn;

            a(IBinder iBinder) {
                this.kn = iBinder;
            }

            public b addCircle(CircleOptions options) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                if (options != null) {
                    obtain.writeInt(1);
                    options.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(ApiEventType.API_MRAID_IS_AUDIO_MUTED, obtain, obtain2, 0);
                obtain2.readException();
                b aC = com.google.android.gms.maps.model.internal.b.a.aC(obtain2.readStrongBinder());
                obtain2.recycle();
                obtain.recycle();
                return aC;
            }

            public c addGroundOverlay(GroundOverlayOptions options) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                if (options != null) {
                    obtain.writeInt(1);
                    options.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(ApiEventType.API_MRAID_RESIZE, obtain, obtain2, 0);
                obtain2.readException();
                c aD = com.google.android.gms.maps.model.internal.c.a.aD(obtain2.readStrongBinder());
                obtain2.recycle();
                obtain.recycle();
                return aD;
            }

            public f addMarker(MarkerOptions options) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                if (options != null) {
                    obtain.writeInt(1);
                    options.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(ApiEventType.API_MRAID_EXPAND, obtain, obtain2, 0);
                obtain2.readException();
                f aG = com.google.android.gms.maps.model.internal.f.a.aG(obtain2.readStrongBinder());
                obtain2.recycle();
                obtain.recycle();
                return aG;
            }

            public g addPolygon(PolygonOptions options) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                if (options != null) {
                    obtain.writeInt(1);
                    options.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(ApiEventType.API_MRAID_USE_CUSTOM_CLOSE, obtain, obtain2, 0);
                obtain2.readException();
                g aH = com.google.android.gms.maps.model.internal.g.a.aH(obtain2.readStrongBinder());
                obtain2.recycle();
                obtain.recycle();
                return aH;
            }

            public IPolylineDelegate addPolyline(PolylineOptions options) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                if (options != null) {
                    obtain.writeInt(1);
                    options.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                IPolylineDelegate aI = com.google.android.gms.maps.model.internal.IPolylineDelegate.a.aI(obtain2.readStrongBinder());
                obtain2.recycle();
                obtain.recycle();
                return aI;
            }

            public h addTileOverlay(TileOverlayOptions options) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                if (options != null) {
                    obtain.writeInt(1);
                    options.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(ApiEventType.API_MRAID_CLOSE, obtain, obtain2, 0);
                obtain2.readException();
                h aJ = com.google.android.gms.maps.model.internal.h.a.aJ(obtain2.readStrongBinder());
                obtain2.recycle();
                obtain.recycle();
                return aJ;
            }

            public void animateCamera(d update) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                obtain.writeStrongBinder(update != null ? update.asBinder() : null);
                this.kn.transact(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void animateCameraWithCallback(d update, b callback) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                obtain.writeStrongBinder(update != null ? update.asBinder() : null);
                if (callback != null) {
                    iBinder = callback.asBinder();
                }
                obtain.writeStrongBinder(iBinder);
                this.kn.transact(ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void animateCameraWithDurationAndCallback(d update, int durationMs, b callback) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                obtain.writeStrongBinder(update != null ? update.asBinder() : null);
                obtain.writeInt(durationMs);
                if (callback != null) {
                    iBinder = callback.asBinder();
                }
                obtain.writeStrongBinder(iBinder);
                this.kn.transact(ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public IBinder asBinder() {
                return this.kn;
            }

            public void clear() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                this.kn.transact(ApiEventType.API_MRAID_IS_VIEWABLE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public CameraPosition getCameraPosition() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                this.kn.transact(1, obtain, obtain2, 0);
                obtain2.readException();
                CameraPosition createFromParcel = obtain2.readInt() != 0 ? CameraPosition.CREATOR.createFromParcel(obtain2) : null;
                obtain2.recycle();
                obtain.recycle();
                return createFromParcel;
            }

            public com.google.android.gms.maps.model.internal.d getFocusedBuilding() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                this.kn.transact(ApiEventType.API_MRAID_SET_VIDEO_VOLUME, obtain, obtain2, 0);
                obtain2.readException();
                com.google.android.gms.maps.model.internal.d aE = com.google.android.gms.maps.model.internal.d.a.aE(obtain2.readStrongBinder());
                obtain2.recycle();
                obtain.recycle();
                return aE;
            }

            public int getMapType() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                this.kn.transact(ApiEventType.API_MRAID_GET_PLACEMENT_TYPE, obtain, obtain2, 0);
                obtain2.readException();
                int readInt = obtain2.readInt();
                obtain2.recycle();
                obtain.recycle();
                return readInt;
            }

            public float getMaxZoomLevel() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                this.kn.transact(MMAdView.TRANSITION_UP, obtain, obtain2, 0);
                obtain2.readException();
                float readFloat = obtain2.readFloat();
                obtain2.recycle();
                obtain.recycle();
                return readFloat;
            }

            public float getMinZoomLevel() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                this.kn.transact(MMAdView.TRANSITION_DOWN, obtain, obtain2, 0);
                obtain2.readException();
                float readFloat = obtain2.readFloat();
                obtain2.recycle();
                obtain.recycle();
                return readFloat;
            }

            public Location getMyLocation() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                this.kn.transact(ApiEventType.API_MRAID_STORE_PICTURE, obtain, obtain2, 0);
                obtain2.readException();
                Location location = obtain2.readInt() != 0 ? (Location) Location.CREATOR.createFromParcel(obtain2) : null;
                obtain2.recycle();
                obtain.recycle();
                return location;
            }

            public IProjectionDelegate getProjection() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                this.kn.transact(ApiEventType.API_MRAID_VIBRATE, obtain, obtain2, 0);
                obtain2.readException();
                IProjectionDelegate av = com.google.android.gms.maps.internal.IProjectionDelegate.a.av(obtain2.readStrongBinder());
                obtain2.recycle();
                obtain.recycle();
                return av;
            }

            public d getTestingHelper() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                this.kn.transact(ApiEventType.API_MRAID_UNMUTE_AUDIO, obtain, obtain2, 0);
                obtain2.readException();
                d K = com.google.android.gms.dynamic.d.a.K(obtain2.readStrongBinder());
                obtain2.recycle();
                obtain.recycle();
                return K;
            }

            public IUiSettingsDelegate getUiSettings() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                this.kn.transact(ApiEventType.API_MRAID_GET_GALLERY_IMAGE, obtain, obtain2, 0);
                obtain2.readException();
                IUiSettingsDelegate aA = com.google.android.gms.maps.internal.IUiSettingsDelegate.a.aA(obtain2.readStrongBinder());
                obtain2.recycle();
                obtain.recycle();
                return aA;
            }

            public boolean isBuildingsEnabled() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                this.kn.transact(ApiEventType.API_MRAID_PLAY_VIDEO, obtain, obtain2, 0);
                obtain2.readException();
                if (obtain2.readInt() != 0) {
                    z = true;
                }
                obtain2.recycle();
                obtain.recycle();
                return z;
            }

            public boolean isIndoorEnabled() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                this.kn.transact(Encoder.LINE_GROUPS, obtain, obtain2, 0);
                obtain2.readException();
                if (obtain2.readInt() != 0) {
                    z = true;
                }
                obtain2.recycle();
                obtain.recycle();
                return z;
            }

            public boolean isMyLocationEnabled() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                this.kn.transact(ApiEventType.API_MRAID_POST_TO_SOCIAL, obtain, obtain2, 0);
                obtain2.readException();
                if (obtain2.readInt() != 0) {
                    z = true;
                }
                obtain2.recycle();
                obtain.recycle();
                return z;
            }

            public boolean isTrafficEnabled() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                this.kn.transact(ApiEventType.API_MRAID_GET_SCREEN_SIZE, obtain, obtain2, 0);
                obtain2.readException();
                if (obtain2.readInt() != 0) {
                    z = true;
                }
                obtain2.recycle();
                obtain.recycle();
                return z;
            }

            public void moveCamera(d update) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                obtain.writeStrongBinder(update != null ? update.asBinder() : null);
                this.kn.transact(MMAdView.TRANSITION_RANDOM, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void setBuildingsEnabled(boolean enabled) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                if (enabled) {
                    i = 1;
                }
                obtain.writeInt(i);
                this.kn.transact(ApiEventType.API_MRAID_MUTE_VIDEO, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public boolean setIndoorEnabled(boolean enabled) throws RemoteException {
                boolean z = true;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                obtain.writeInt(enabled ? 1 : 0);
                this.kn.transact(ApiEventType.API_MRAID_GET_MAX_SIZE, obtain, obtain2, 0);
                obtain2.readException();
                if (obtain2.readInt() == 0) {
                    z = false;
                }
                obtain2.recycle();
                obtain.recycle();
                return z;
            }

            public void setInfoWindowAdapter(d adapter) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                obtain.writeStrongBinder(adapter != null ? adapter.asBinder() : null);
                this.kn.transact(ApiEventType.API_MRAID_MUTE_AUDIO, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void setLocationSource(ILocationSourceDelegate source) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                obtain.writeStrongBinder(source != null ? source.asBinder() : null);
                this.kn.transact(ApiEventType.API_MRAID_TAKE_CAMERA_PICTURE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void setMapType(int type) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                obtain.writeInt(type);
                this.kn.transact(ApiEventType.API_MRAID_GET_ORIENTATION, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void setMyLocationEnabled(boolean enabled) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                if (enabled) {
                    i = 1;
                }
                obtain.writeInt(i);
                this.kn.transact(ApiEventType.API_MRAID_SUPPORTS, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void setOnCameraChangeListener(e listener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                obtain.writeStrongBinder(listener != null ? listener.asBinder() : null);
                this.kn.transact(ApiEventType.API_MRAID_SEND_SMS, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void setOnIndoorStateChangeListener(f listener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                obtain.writeStrongBinder(listener != null ? listener.asBinder() : null);
                this.kn.transact(ApiEventType.API_MRAID_GET_VIDEO_VOLUME, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void setOnInfoWindowClickListener(g listener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                obtain.writeStrongBinder(listener != null ? listener.asBinder() : null);
                this.kn.transact(ApiEventType.API_MRAID_PLAY_AUDIO, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void setOnMapClickListener(i listener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                obtain.writeStrongBinder(listener != null ? listener.asBinder() : null);
                this.kn.transact(ApiEventType.API_MRAID_SEND_MAIL, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void setOnMapLoadedCallback(j callback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                obtain.writeStrongBinder(callback != null ? callback.asBinder() : null);
                this.kn.transact(ApiEventType.API_MRAID_UNMUTE_VIDEO, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void setOnMapLongClickListener(k listener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                obtain.writeStrongBinder(listener != null ? listener.asBinder() : null);
                this.kn.transact(ApiEventType.API_MRAID_MAKE_CALL, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void setOnMarkerClickListener(l listener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                obtain.writeStrongBinder(listener != null ? listener.asBinder() : null);
                this.kn.transact(ApiEventType.API_MRAID_CREATE_CALENDAR_EVENT, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void setOnMarkerDragListener(m listener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                obtain.writeStrongBinder(listener != null ? listener.asBinder() : null);
                this.kn.transact(ApiEventType.API_MRAID_ASYNC_PING, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void setOnMyLocationButtonClickListener(n listener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                obtain.writeStrongBinder(listener != null ? listener.asBinder() : null);
                this.kn.transact(ApiEventType.API_MRAID_GET_AUDIO_VOLUME, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void setOnMyLocationChangeListener(o listener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                obtain.writeStrongBinder(listener != null ? listener.asBinder() : null);
                this.kn.transact(ApiEventType.API_MRAID_SET_AUDIO_VOLUME, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void setPadding(int left, int top, int right, int bottom) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                obtain.writeInt(left);
                obtain.writeInt(top);
                obtain.writeInt(right);
                obtain.writeInt(bottom);
                this.kn.transact(ApiEventType.API_MRAID_PAUSE_AUDIO, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void setTrafficEnabled(boolean enabled) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                if (enabled) {
                    i = 1;
                }
                obtain.writeInt(i);
                this.kn.transact(ApiEventType.API_MRAID_GET_CURRENT_POSITION, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void setWatermarkEnabled(boolean enabled) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                if (enabled) {
                    i = 1;
                }
                obtain.writeInt(i);
                this.kn.transact(ApiEventType.API_MRAID_GET_MIC_INTENSITY, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void snapshot(s callback, d bitmap) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                obtain.writeStrongBinder(callback != null ? callback.asBinder() : null);
                if (bitmap != null) {
                    iBinder = bitmap.asBinder();
                }
                obtain.writeStrongBinder(iBinder);
                this.kn.transact(ApiEventType.API_MRAID_SEEK_AUDIO, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void stopAnimation() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                this.kn.transact(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }
        }

        public static IGoogleMapDelegate ac(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
            return (queryLocalInterface == null || !queryLocalInterface instanceof IGoogleMapDelegate) ? new a(iBinder) : (IGoogleMapDelegate) queryLocalInterface;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean z = false;
            IBinder iBinder = null;
            float maxZoomLevel;
            int mapType;
            boolean isTrafficEnabled;
            switch (code) {
                case MMAdView.TRANSITION_FADE:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    CameraPosition cameraPosition = getCameraPosition();
                    reply.writeNoException();
                    if (cameraPosition != null) {
                        reply.writeInt(1);
                        cameraPosition.writeToParcel(reply, 1);
                        return true;
                    } else {
                        reply.writeInt(0);
                        return true;
                    }
                case MMAdView.TRANSITION_UP:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    maxZoomLevel = getMaxZoomLevel();
                    reply.writeNoException();
                    reply.writeFloat(maxZoomLevel);
                    return true;
                case MMAdView.TRANSITION_DOWN:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    maxZoomLevel = getMinZoomLevel();
                    reply.writeNoException();
                    reply.writeFloat(maxZoomLevel);
                    return true;
                case MMAdView.TRANSITION_RANDOM:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    moveCamera(com.google.android.gms.dynamic.d.a.K(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    animateCamera(com.google.android.gms.dynamic.d.a.K(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    animateCameraWithCallback(com.google.android.gms.dynamic.d.a.K(data.readStrongBinder()), com.google.android.gms.maps.internal.b.a.aa(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    animateCameraWithDurationAndCallback(com.google.android.gms.dynamic.d.a.K(data.readStrongBinder()), data.readInt(), com.google.android.gms.maps.internal.b.a.aa(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    stopAnimation();
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    IPolylineDelegate addPolyline = addPolyline(data.readInt() != 0 ? PolylineOptions.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    if (addPolyline != null) {
                        iBinder = addPolyline.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    g addPolygon = addPolygon(data.readInt() != 0 ? PolygonOptions.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    if (addPolygon != null) {
                        iBinder = addPolygon.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case ApiEventType.API_MRAID_EXPAND:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    f addMarker = addMarker(data.readInt() != 0 ? MarkerOptions.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    if (addMarker != null) {
                        iBinder = addMarker.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case ApiEventType.API_MRAID_RESIZE:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    c addGroundOverlay = addGroundOverlay(data.readInt() != 0 ? GroundOverlayOptions.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    if (addGroundOverlay != null) {
                        iBinder = addGroundOverlay.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case ApiEventType.API_MRAID_CLOSE:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    h addTileOverlay = addTileOverlay(data.readInt() != 0 ? TileOverlayOptions.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    if (addTileOverlay != null) {
                        iBinder = addTileOverlay.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case ApiEventType.API_MRAID_IS_VIEWABLE:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    clear();
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_GET_PLACEMENT_TYPE:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    mapType = getMapType();
                    reply.writeNoException();
                    reply.writeInt(mapType);
                    return true;
                case ApiEventType.API_MRAID_GET_ORIENTATION:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    setMapType(data.readInt());
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_GET_SCREEN_SIZE:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    isTrafficEnabled = isTrafficEnabled();
                    reply.writeNoException();
                    if (isTrafficEnabled) {
                        mapType = 1;
                    }
                    reply.writeInt(mapType);
                    return true;
                case ApiEventType.API_MRAID_GET_CURRENT_POSITION:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    setTrafficEnabled(z);
                    reply.writeNoException();
                    return true;
                case Encoder.LINE_GROUPS:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    isTrafficEnabled = isIndoorEnabled();
                    reply.writeNoException();
                    if (isTrafficEnabled) {
                        mapType = 1;
                    }
                    reply.writeInt(mapType);
                    return true;
                case ApiEventType.API_MRAID_GET_MAX_SIZE:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    isTrafficEnabled = setIndoorEnabled(data.readInt() != 0);
                    reply.writeNoException();
                    if (isTrafficEnabled) {
                        mapType = 1;
                    }
                    reply.writeInt(mapType);
                    return true;
                case ApiEventType.API_MRAID_POST_TO_SOCIAL:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    isTrafficEnabled = isMyLocationEnabled();
                    reply.writeNoException();
                    if (isTrafficEnabled) {
                        mapType = 1;
                    }
                    reply.writeInt(mapType);
                    return true;
                case ApiEventType.API_MRAID_SUPPORTS:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    setMyLocationEnabled(z);
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_STORE_PICTURE:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    Location myLocation = getMyLocation();
                    reply.writeNoException();
                    if (myLocation != null) {
                        reply.writeInt(1);
                        myLocation.writeToParcel(reply, 1);
                        return true;
                    } else {
                        reply.writeInt(0);
                        return true;
                    }
                case ApiEventType.API_MRAID_TAKE_CAMERA_PICTURE:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    setLocationSource(com.google.android.gms.maps.internal.ILocationSourceDelegate.a.ae(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_GET_GALLERY_IMAGE:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    IUiSettingsDelegate uiSettings = getUiSettings();
                    reply.writeNoException();
                    if (uiSettings != null) {
                        iBinder = uiSettings.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case ApiEventType.API_MRAID_VIBRATE:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    IProjectionDelegate projection = getProjection();
                    reply.writeNoException();
                    if (projection != null) {
                        iBinder = projection.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case ApiEventType.API_MRAID_SEND_SMS:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    setOnCameraChangeListener(com.google.android.gms.maps.internal.e.a.ah(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_SEND_MAIL:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    setOnMapClickListener(com.google.android.gms.maps.internal.i.a.al(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_MAKE_CALL:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    setOnMapLongClickListener(com.google.android.gms.maps.internal.k.a.an(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_CREATE_CALENDAR_EVENT:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    setOnMarkerClickListener(com.google.android.gms.maps.internal.l.a.ao(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_ASYNC_PING:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    setOnMarkerDragListener(com.google.android.gms.maps.internal.m.a.ap(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_PLAY_AUDIO:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    setOnInfoWindowClickListener(com.google.android.gms.maps.internal.g.a.aj(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_MUTE_AUDIO:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    setInfoWindowAdapter(com.google.android.gms.maps.internal.d.a.ad(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_UNMUTE_AUDIO:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    d testingHelper = getTestingHelper();
                    reply.writeNoException();
                    if (testingHelper != null) {
                        iBinder = testingHelper.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case ApiEventType.API_MRAID_IS_AUDIO_MUTED:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    b addCircle = addCircle(data.readInt() != 0 ? CircleOptions.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    if (addCircle != null) {
                        iBinder = addCircle.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case ApiEventType.API_MRAID_SET_AUDIO_VOLUME:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    setOnMyLocationChangeListener(com.google.android.gms.maps.internal.o.a.ar(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_GET_AUDIO_VOLUME:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    setOnMyLocationButtonClickListener(com.google.android.gms.maps.internal.n.a.aq(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_SEEK_AUDIO:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    snapshot(com.google.android.gms.maps.internal.s.a.aw(data.readStrongBinder()), com.google.android.gms.dynamic.d.a.K(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_PAUSE_AUDIO:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    setPadding(data.readInt(), data.readInt(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_PLAY_VIDEO:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    isTrafficEnabled = isBuildingsEnabled();
                    reply.writeNoException();
                    if (isTrafficEnabled) {
                        mapType = 1;
                    }
                    reply.writeInt(mapType);
                    return true;
                case ApiEventType.API_MRAID_MUTE_VIDEO:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    setBuildingsEnabled(z);
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_UNMUTE_VIDEO:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    setOnMapLoadedCallback(com.google.android.gms.maps.internal.j.a.am(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_SET_VIDEO_VOLUME:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    com.google.android.gms.maps.model.internal.d focusedBuilding = getFocusedBuilding();
                    reply.writeNoException();
                    if (focusedBuilding != null) {
                        iBinder = focusedBuilding.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case ApiEventType.API_MRAID_GET_VIDEO_VOLUME:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    setOnIndoorStateChangeListener(com.google.android.gms.maps.internal.f.a.ai(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_GET_MIC_INTENSITY:
                    data.enforceInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    setWatermarkEnabled(z);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.maps.internal.IGoogleMapDelegate");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    b addCircle(CircleOptions circleOptions) throws RemoteException;

    c addGroundOverlay(GroundOverlayOptions groundOverlayOptions) throws RemoteException;

    f addMarker(MarkerOptions markerOptions) throws RemoteException;

    g addPolygon(PolygonOptions polygonOptions) throws RemoteException;

    IPolylineDelegate addPolyline(PolylineOptions polylineOptions) throws RemoteException;

    h addTileOverlay(TileOverlayOptions tileOverlayOptions) throws RemoteException;

    void animateCamera(d dVar) throws RemoteException;

    void animateCameraWithCallback(d dVar, b bVar) throws RemoteException;

    void animateCameraWithDurationAndCallback(d dVar, int i, b bVar) throws RemoteException;

    void clear() throws RemoteException;

    CameraPosition getCameraPosition() throws RemoteException;

    com.google.android.gms.maps.model.internal.d getFocusedBuilding() throws RemoteException;

    int getMapType() throws RemoteException;

    float getMaxZoomLevel() throws RemoteException;

    float getMinZoomLevel() throws RemoteException;

    Location getMyLocation() throws RemoteException;

    IProjectionDelegate getProjection() throws RemoteException;

    d getTestingHelper() throws RemoteException;

    IUiSettingsDelegate getUiSettings() throws RemoteException;

    boolean isBuildingsEnabled() throws RemoteException;

    boolean isIndoorEnabled() throws RemoteException;

    boolean isMyLocationEnabled() throws RemoteException;

    boolean isTrafficEnabled() throws RemoteException;

    void moveCamera(d dVar) throws RemoteException;

    void setBuildingsEnabled(boolean z) throws RemoteException;

    boolean setIndoorEnabled(boolean z) throws RemoteException;

    void setInfoWindowAdapter(d dVar) throws RemoteException;

    void setLocationSource(ILocationSourceDelegate iLocationSourceDelegate) throws RemoteException;

    void setMapType(int i) throws RemoteException;

    void setMyLocationEnabled(boolean z) throws RemoteException;

    void setOnCameraChangeListener(e eVar) throws RemoteException;

    void setOnIndoorStateChangeListener(f fVar) throws RemoteException;

    void setOnInfoWindowClickListener(g gVar) throws RemoteException;

    void setOnMapClickListener(i iVar) throws RemoteException;

    void setOnMapLoadedCallback(j jVar) throws RemoteException;

    void setOnMapLongClickListener(k kVar) throws RemoteException;

    void setOnMarkerClickListener(l lVar) throws RemoteException;

    void setOnMarkerDragListener(m mVar) throws RemoteException;

    void setOnMyLocationButtonClickListener(n nVar) throws RemoteException;

    void setOnMyLocationChangeListener(o oVar) throws RemoteException;

    void setPadding(int i, int i2, int i3, int i4) throws RemoteException;

    void setTrafficEnabled(boolean z) throws RemoteException;

    void setWatermarkEnabled(boolean z) throws RemoteException;

    void snapshot(s sVar, d dVar) throws RemoteException;

    void stopAnimation() throws RemoteException;
}