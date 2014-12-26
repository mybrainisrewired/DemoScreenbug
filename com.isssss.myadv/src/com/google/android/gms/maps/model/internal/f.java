package com.google.android.gms.maps.model.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.d;
import com.google.android.gms.maps.model.LatLng;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public interface f extends IInterface {

    public static abstract class a extends Binder implements f {

        private static class a implements f {
            private IBinder kn;

            a(IBinder iBinder) {
                this.kn = iBinder;
            }

            public IBinder asBinder() {
                return this.kn;
            }

            public float getAlpha() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                this.kn.transact(ApiEventType.API_MRAID_VIBRATE, obtain, obtain2, 0);
                obtain2.readException();
                float readFloat = obtain2.readFloat();
                obtain2.recycle();
                obtain.recycle();
                return readFloat;
            }

            public String getId() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                this.kn.transact(MMAdView.TRANSITION_UP, obtain, obtain2, 0);
                obtain2.readException();
                String readString = obtain2.readString();
                obtain2.recycle();
                obtain.recycle();
                return readString;
            }

            public LatLng getPosition() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                this.kn.transact(MMAdView.TRANSITION_RANDOM, obtain, obtain2, 0);
                obtain2.readException();
                LatLng createFromParcel = obtain2.readInt() != 0 ? LatLng.CREATOR.createFromParcel(obtain2) : null;
                obtain2.recycle();
                obtain.recycle();
                return createFromParcel;
            }

            public float getRotation() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                this.kn.transact(ApiEventType.API_MRAID_STORE_PICTURE, obtain, obtain2, 0);
                obtain2.readException();
                float readFloat = obtain2.readFloat();
                obtain2.recycle();
                obtain.recycle();
                return readFloat;
            }

            public String getSnippet() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                this.kn.transact(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                String readString = obtain2.readString();
                obtain2.recycle();
                obtain.recycle();
                return readString;
            }

            public String getTitle() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                this.kn.transact(ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                String readString = obtain2.readString();
                obtain2.recycle();
                obtain.recycle();
                return readString;
            }

            public boolean h(f fVar) throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                obtain.writeStrongBinder(fVar != null ? fVar.asBinder() : null);
                this.kn.transact(ApiEventType.API_MRAID_GET_ORIENTATION, obtain, obtain2, 0);
                obtain2.readException();
                if (obtain2.readInt() != 0) {
                    z = true;
                }
                obtain2.recycle();
                obtain.recycle();
                return z;
            }

            public int hashCodeRemote() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                this.kn.transact(ApiEventType.API_MRAID_GET_SCREEN_SIZE, obtain, obtain2, 0);
                obtain2.readException();
                int readInt = obtain2.readInt();
                obtain2.recycle();
                obtain.recycle();
                return readInt;
            }

            public void hideInfoWindow() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                this.kn.transact(ApiEventType.API_MRAID_RESIZE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public boolean isDraggable() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                this.kn.transact(ApiEventType.API_MRAID_USE_CUSTOM_CLOSE, obtain, obtain2, 0);
                obtain2.readException();
                if (obtain2.readInt() != 0) {
                    z = true;
                }
                obtain2.recycle();
                obtain.recycle();
                return z;
            }

            public boolean isFlat() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                this.kn.transact(ApiEventType.API_MRAID_POST_TO_SOCIAL, obtain, obtain2, 0);
                obtain2.readException();
                if (obtain2.readInt() != 0) {
                    z = true;
                }
                obtain2.recycle();
                obtain.recycle();
                return z;
            }

            public boolean isInfoWindowShown() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                this.kn.transact(ApiEventType.API_MRAID_CLOSE, obtain, obtain2, 0);
                obtain2.readException();
                if (obtain2.readInt() != 0) {
                    z = true;
                }
                obtain2.recycle();
                obtain.recycle();
                return z;
            }

            public boolean isVisible() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                this.kn.transact(ApiEventType.API_MRAID_GET_PLACEMENT_TYPE, obtain, obtain2, 0);
                obtain2.readException();
                if (obtain2.readInt() != 0) {
                    z = true;
                }
                obtain2.recycle();
                obtain.recycle();
                return z;
            }

            public void l(d dVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                obtain.writeStrongBinder(dVar != null ? dVar.asBinder() : null);
                this.kn.transact(ApiEventType.API_MRAID_GET_CURRENT_POSITION, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void remove() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                this.kn.transact(1, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void setAlpha(float alpha) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                obtain.writeFloat(alpha);
                this.kn.transact(ApiEventType.API_MRAID_GET_GALLERY_IMAGE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void setAnchor(float anchorU, float anchorV) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                obtain.writeFloat(anchorU);
                obtain.writeFloat(anchorV);
                this.kn.transact(Encoder.LINE_GROUPS, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void setDraggable(boolean draggable) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                if (draggable) {
                    i = 1;
                }
                obtain.writeInt(i);
                this.kn.transact(ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void setFlat(boolean flat) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                if (flat) {
                    i = 1;
                }
                obtain.writeInt(i);
                this.kn.transact(ApiEventType.API_MRAID_GET_MAX_SIZE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void setInfoWindowAnchor(float anchorU, float anchorV) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                obtain.writeFloat(anchorU);
                obtain.writeFloat(anchorV);
                this.kn.transact(ApiEventType.API_MRAID_TAKE_CAMERA_PICTURE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void setPosition(LatLng latlng) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                if (latlng != null) {
                    obtain.writeInt(1);
                    latlng.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(MMAdView.TRANSITION_DOWN, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void setRotation(float rotation) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                obtain.writeFloat(rotation);
                this.kn.transact(ApiEventType.API_MRAID_SUPPORTS, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void setSnippet(String snippet) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                obtain.writeString(snippet);
                this.kn.transact(ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void setTitle(String title) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                obtain.writeString(title);
                this.kn.transact(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void setVisible(boolean visible) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                if (visible) {
                    i = 1;
                }
                obtain.writeInt(i);
                this.kn.transact(ApiEventType.API_MRAID_IS_VIEWABLE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void showInfoWindow() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                this.kn.transact(ApiEventType.API_MRAID_EXPAND, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }
        }

        public static f aG(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
            return (queryLocalInterface == null || !queryLocalInterface instanceof f) ? new a(iBinder) : (f) queryLocalInterface;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = 0;
            String id;
            boolean z;
            boolean isDraggable;
            float rotation;
            switch (code) {
                case MMAdView.TRANSITION_FADE:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    remove();
                    reply.writeNoException();
                    return true;
                case MMAdView.TRANSITION_UP:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    id = getId();
                    reply.writeNoException();
                    reply.writeString(id);
                    return true;
                case MMAdView.TRANSITION_DOWN:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    setPosition(data.readInt() != 0 ? LatLng.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case MMAdView.TRANSITION_RANDOM:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    LatLng position = getPosition();
                    reply.writeNoException();
                    if (position != null) {
                        reply.writeInt(1);
                        position.writeToParcel(reply, 1);
                        return true;
                    } else {
                        reply.writeInt(0);
                        return true;
                    }
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    setTitle(data.readString());
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    id = getTitle();
                    reply.writeNoException();
                    reply.writeString(id);
                    return true;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    setSnippet(data.readString());
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    id = getSnippet();
                    reply.writeNoException();
                    reply.writeString(id);
                    return true;
                case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    setDraggable(z);
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    isDraggable = isDraggable();
                    reply.writeNoException();
                    if (isDraggable) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case ApiEventType.API_MRAID_EXPAND:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    showInfoWindow();
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_RESIZE:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    hideInfoWindow();
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_CLOSE:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    isDraggable = isInfoWindowShown();
                    reply.writeNoException();
                    if (isDraggable) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case ApiEventType.API_MRAID_IS_VIEWABLE:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    setVisible(z);
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_GET_PLACEMENT_TYPE:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    isDraggable = isVisible();
                    reply.writeNoException();
                    if (isDraggable) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case ApiEventType.API_MRAID_GET_ORIENTATION:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    isDraggable = h(aG(data.readStrongBinder()));
                    reply.writeNoException();
                    if (isDraggable) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case ApiEventType.API_MRAID_GET_SCREEN_SIZE:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    i = hashCodeRemote();
                    reply.writeNoException();
                    reply.writeInt(i);
                    return true;
                case ApiEventType.API_MRAID_GET_CURRENT_POSITION:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    l(com.google.android.gms.dynamic.d.a.K(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case Encoder.LINE_GROUPS:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    setAnchor(data.readFloat(), data.readFloat());
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_GET_MAX_SIZE:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    setFlat(z);
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_POST_TO_SOCIAL:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    isDraggable = isFlat();
                    reply.writeNoException();
                    if (isDraggable) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case ApiEventType.API_MRAID_SUPPORTS:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    setRotation(data.readFloat());
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_STORE_PICTURE:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    rotation = getRotation();
                    reply.writeNoException();
                    reply.writeFloat(rotation);
                    return true;
                case ApiEventType.API_MRAID_TAKE_CAMERA_PICTURE:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    setInfoWindowAnchor(data.readFloat(), data.readFloat());
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_GET_GALLERY_IMAGE:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    setAlpha(data.readFloat());
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_VIBRATE:
                    data.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    rotation = getAlpha();
                    reply.writeNoException();
                    reply.writeFloat(rotation);
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    float getAlpha() throws RemoteException;

    String getId() throws RemoteException;

    LatLng getPosition() throws RemoteException;

    float getRotation() throws RemoteException;

    String getSnippet() throws RemoteException;

    String getTitle() throws RemoteException;

    boolean h(f fVar) throws RemoteException;

    int hashCodeRemote() throws RemoteException;

    void hideInfoWindow() throws RemoteException;

    boolean isDraggable() throws RemoteException;

    boolean isFlat() throws RemoteException;

    boolean isInfoWindowShown() throws RemoteException;

    boolean isVisible() throws RemoteException;

    void l(d dVar) throws RemoteException;

    void remove() throws RemoteException;

    void setAlpha(float f) throws RemoteException;

    void setAnchor(float f, float f2) throws RemoteException;

    void setDraggable(boolean z) throws RemoteException;

    void setFlat(boolean z) throws RemoteException;

    void setInfoWindowAnchor(float f, float f2) throws RemoteException;

    void setPosition(LatLng latLng) throws RemoteException;

    void setRotation(float f) throws RemoteException;

    void setSnippet(String str) throws RemoteException;

    void setTitle(String str) throws RemoteException;

    void setVisible(boolean z) throws RemoteException;

    void showInfoWindow() throws RemoteException;
}