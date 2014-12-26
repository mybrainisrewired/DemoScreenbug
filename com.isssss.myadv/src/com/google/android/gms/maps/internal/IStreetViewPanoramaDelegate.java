package com.google.android.gms.maps.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.d;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;
import com.google.android.gms.maps.model.StreetViewPanoramaLocation;
import com.google.android.gms.maps.model.StreetViewPanoramaOrientation;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public interface IStreetViewPanoramaDelegate extends IInterface {

    public static abstract class a extends Binder implements IStreetViewPanoramaDelegate {

        private static class a implements IStreetViewPanoramaDelegate {
            private IBinder kn;

            a(IBinder iBinder) {
                this.kn = iBinder;
            }

            public void animateTo(StreetViewPanoramaCamera camera, long duration) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate");
                if (camera != null) {
                    obtain.writeInt(1);
                    camera.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                obtain.writeLong(duration);
                this.kn.transact(ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public IBinder asBinder() {
                return this.kn;
            }

            public void enablePanning(boolean enablePanning) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate");
                if (enablePanning) {
                    i = 1;
                }
                obtain.writeInt(i);
                this.kn.transact(MMAdView.TRANSITION_UP, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void enableStreetNames(boolean enableStreetNames) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate");
                if (enableStreetNames) {
                    i = 1;
                }
                obtain.writeInt(i);
                this.kn.transact(MMAdView.TRANSITION_RANDOM, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void enableUserNavigation(boolean enableUserNavigation) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate");
                if (enableUserNavigation) {
                    i = 1;
                }
                obtain.writeInt(i);
                this.kn.transact(MMAdView.TRANSITION_DOWN, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void enableZoom(boolean enableZoom) throws RemoteException {
                int i = 1;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate");
                if (!enableZoom) {
                    i = 0;
                }
                obtain.writeInt(i);
                this.kn.transact(1, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public StreetViewPanoramaCamera getPanoramaCamera() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate");
                this.kn.transact(ApiEventType.API_MRAID_USE_CUSTOM_CLOSE, obtain, obtain2, 0);
                obtain2.readException();
                StreetViewPanoramaCamera createFromParcel = obtain2.readInt() != 0 ? StreetViewPanoramaCamera.CREATOR.createFromParcel(obtain2) : null;
                obtain2.recycle();
                obtain.recycle();
                return createFromParcel;
            }

            public StreetViewPanoramaLocation getStreetViewPanoramaLocation() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate");
                this.kn.transact(ApiEventType.API_MRAID_IS_VIEWABLE, obtain, obtain2, 0);
                obtain2.readException();
                StreetViewPanoramaLocation createFromParcel = obtain2.readInt() != 0 ? StreetViewPanoramaLocation.CREATOR.createFromParcel(obtain2) : null;
                obtain2.recycle();
                obtain.recycle();
                return createFromParcel;
            }

            public boolean isPanningGesturesEnabled() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate");
                this.kn.transact(ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                if (obtain2.readInt() != 0) {
                    z = true;
                }
                obtain2.recycle();
                obtain.recycle();
                return z;
            }

            public boolean isStreetNamesEnabled() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate");
                this.kn.transact(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                if (obtain2.readInt() != 0) {
                    z = true;
                }
                obtain2.recycle();
                obtain.recycle();
                return z;
            }

            public boolean isUserNavigationEnabled() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate");
                this.kn.transact(ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                if (obtain2.readInt() != 0) {
                    z = true;
                }
                obtain2.recycle();
                obtain.recycle();
                return z;
            }

            public boolean isZoomGesturesEnabled() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate");
                this.kn.transact(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                if (obtain2.readInt() != 0) {
                    z = true;
                }
                obtain2.recycle();
                obtain.recycle();
                return z;
            }

            public d orientationToPoint(StreetViewPanoramaOrientation orientation) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate");
                if (orientation != null) {
                    obtain.writeInt(1);
                    orientation.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(Encoder.LINE_GROUPS, obtain, obtain2, 0);
                obtain2.readException();
                d K = com.google.android.gms.dynamic.d.a.K(obtain2.readStrongBinder());
                obtain2.recycle();
                obtain.recycle();
                return K;
            }

            public StreetViewPanoramaOrientation pointToOrientation(d point) throws RemoteException {
                StreetViewPanoramaOrientation streetViewPanoramaOrientation = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate");
                obtain.writeStrongBinder(point != null ? point.asBinder() : null);
                this.kn.transact(ApiEventType.API_MRAID_GET_CURRENT_POSITION, obtain, obtain2, 0);
                obtain2.readException();
                if (obtain2.readInt() != 0) {
                    streetViewPanoramaOrientation = StreetViewPanoramaOrientation.CREATOR.createFromParcel(obtain2);
                }
                obtain2.recycle();
                obtain.recycle();
                return streetViewPanoramaOrientation;
            }

            public void setOnStreetViewPanoramaCameraChangeListener(p listener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate");
                obtain.writeStrongBinder(listener != null ? listener.asBinder() : null);
                this.kn.transact(ApiEventType.API_MRAID_GET_ORIENTATION, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void setOnStreetViewPanoramaChangeListener(q listener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate");
                obtain.writeStrongBinder(listener != null ? listener.asBinder() : null);
                this.kn.transact(ApiEventType.API_MRAID_GET_PLACEMENT_TYPE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void setOnStreetViewPanoramaClickListener(r listener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate");
                obtain.writeStrongBinder(listener != null ? listener.asBinder() : null);
                this.kn.transact(ApiEventType.API_MRAID_GET_SCREEN_SIZE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void setPosition(LatLng position) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate");
                if (position != null) {
                    obtain.writeInt(1);
                    position.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(ApiEventType.API_MRAID_RESIZE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void setPositionWithID(String panoId) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate");
                obtain.writeString(panoId);
                this.kn.transact(ApiEventType.API_MRAID_EXPAND, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void setPositionWithRadius(LatLng position, int radius) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate");
                if (position != null) {
                    obtain.writeInt(1);
                    position.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                obtain.writeInt(radius);
                this.kn.transact(ApiEventType.API_MRAID_CLOSE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }
        }

        public static IStreetViewPanoramaDelegate ax(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate");
            return (queryLocalInterface == null || !queryLocalInterface instanceof IStreetViewPanoramaDelegate) ? new a(iBinder) : (IStreetViewPanoramaDelegate) queryLocalInterface;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            IBinder iBinder = null;
            int i = 0;
            boolean z;
            boolean isZoomGesturesEnabled;
            LatLng createFromParcel;
            switch (code) {
                case MMAdView.TRANSITION_FADE:
                    data.enforceInterface("com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate");
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    enableZoom(z);
                    reply.writeNoException();
                    return true;
                case MMAdView.TRANSITION_UP:
                    data.enforceInterface("com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate");
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    enablePanning(z);
                    reply.writeNoException();
                    return true;
                case MMAdView.TRANSITION_DOWN:
                    data.enforceInterface("com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate");
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    enableUserNavigation(z);
                    reply.writeNoException();
                    return true;
                case MMAdView.TRANSITION_RANDOM:
                    data.enforceInterface("com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate");
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    enableStreetNames(z);
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate");
                    isZoomGesturesEnabled = isZoomGesturesEnabled();
                    reply.writeNoException();
                    if (isZoomGesturesEnabled) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate");
                    isZoomGesturesEnabled = isPanningGesturesEnabled();
                    reply.writeNoException();
                    if (isZoomGesturesEnabled) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate");
                    isZoomGesturesEnabled = isUserNavigationEnabled();
                    reply.writeNoException();
                    if (isZoomGesturesEnabled) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate");
                    isZoomGesturesEnabled = isStreetNamesEnabled();
                    reply.writeNoException();
                    if (isZoomGesturesEnabled) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate");
                    animateTo(data.readInt() != 0 ? StreetViewPanoramaCamera.CREATOR.createFromParcel(data) : null, data.readLong());
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                    data.enforceInterface("com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate");
                    StreetViewPanoramaCamera panoramaCamera = getPanoramaCamera();
                    reply.writeNoException();
                    if (panoramaCamera != null) {
                        reply.writeInt(1);
                        panoramaCamera.writeToParcel(reply, 1);
                        return true;
                    } else {
                        reply.writeInt(0);
                        return true;
                    }
                case ApiEventType.API_MRAID_EXPAND:
                    data.enforceInterface("com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate");
                    setPositionWithID(data.readString());
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_RESIZE:
                    data.enforceInterface("com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate");
                    if (data.readInt() != 0) {
                        createFromParcel = LatLng.CREATOR.createFromParcel(data);
                    }
                    setPosition(createFromParcel);
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_CLOSE:
                    data.enforceInterface("com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate");
                    if (data.readInt() != 0) {
                        createFromParcel = LatLng.CREATOR.createFromParcel(data);
                    }
                    setPositionWithRadius(createFromParcel, data.readInt());
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_IS_VIEWABLE:
                    data.enforceInterface("com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate");
                    StreetViewPanoramaLocation streetViewPanoramaLocation = getStreetViewPanoramaLocation();
                    reply.writeNoException();
                    if (streetViewPanoramaLocation != null) {
                        reply.writeInt(1);
                        streetViewPanoramaLocation.writeToParcel(reply, 1);
                        return true;
                    } else {
                        reply.writeInt(0);
                        return true;
                    }
                case ApiEventType.API_MRAID_GET_PLACEMENT_TYPE:
                    data.enforceInterface("com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate");
                    setOnStreetViewPanoramaChangeListener(com.google.android.gms.maps.internal.q.a.at(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_GET_ORIENTATION:
                    data.enforceInterface("com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate");
                    setOnStreetViewPanoramaCameraChangeListener(com.google.android.gms.maps.internal.p.a.as(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_GET_SCREEN_SIZE:
                    data.enforceInterface("com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate");
                    setOnStreetViewPanoramaClickListener(com.google.android.gms.maps.internal.r.a.au(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_GET_CURRENT_POSITION:
                    data.enforceInterface("com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate");
                    StreetViewPanoramaOrientation pointToOrientation = pointToOrientation(com.google.android.gms.dynamic.d.a.K(data.readStrongBinder()));
                    reply.writeNoException();
                    if (pointToOrientation != null) {
                        reply.writeInt(1);
                        pointToOrientation.writeToParcel(reply, 1);
                        return true;
                    } else {
                        reply.writeInt(0);
                        return true;
                    }
                case Encoder.LINE_GROUPS:
                    data.enforceInterface("com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate");
                    d orientationToPoint = orientationToPoint(data.readInt() != 0 ? StreetViewPanoramaOrientation.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    if (orientationToPoint != null) {
                        iBinder = orientationToPoint.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void animateTo(StreetViewPanoramaCamera streetViewPanoramaCamera, long j) throws RemoteException;

    void enablePanning(boolean z) throws RemoteException;

    void enableStreetNames(boolean z) throws RemoteException;

    void enableUserNavigation(boolean z) throws RemoteException;

    void enableZoom(boolean z) throws RemoteException;

    StreetViewPanoramaCamera getPanoramaCamera() throws RemoteException;

    StreetViewPanoramaLocation getStreetViewPanoramaLocation() throws RemoteException;

    boolean isPanningGesturesEnabled() throws RemoteException;

    boolean isStreetNamesEnabled() throws RemoteException;

    boolean isUserNavigationEnabled() throws RemoteException;

    boolean isZoomGesturesEnabled() throws RemoteException;

    d orientationToPoint(StreetViewPanoramaOrientation streetViewPanoramaOrientation) throws RemoteException;

    StreetViewPanoramaOrientation pointToOrientation(d dVar) throws RemoteException;

    void setOnStreetViewPanoramaCameraChangeListener(p pVar) throws RemoteException;

    void setOnStreetViewPanoramaChangeListener(q qVar) throws RemoteException;

    void setOnStreetViewPanoramaClickListener(r rVar) throws RemoteException;

    void setPosition(LatLng latLng) throws RemoteException;

    void setPositionWithID(String str) throws RemoteException;

    void setPositionWithRadius(LatLng latLng, int i) throws RemoteException;
}