package com.google.android.gms.internal;

import android.app.PendingIntent;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.b;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import java.util.List;

public interface ha extends IInterface {

    public static abstract class a extends Binder implements ha {

        private static class a implements ha {
            private IBinder kn;

            a(IBinder iBinder) {
                this.kn = iBinder;
            }

            public void a(long j, boolean z, PendingIntent pendingIntent) throws RemoteException {
                int i = 1;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                obtain.writeLong(j);
                if (!z) {
                    i = 0;
                }
                obtain.writeInt(i);
                if (pendingIntent != null) {
                    obtain.writeInt(1);
                    pendingIntent.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(PendingIntent pendingIntent) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                if (pendingIntent != null) {
                    obtain.writeInt(1);
                    pendingIntent.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(ApiEventType.API_MRAID_EXPAND, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(PendingIntent pendingIntent, gz gzVar, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                if (pendingIntent != null) {
                    obtain.writeInt(1);
                    pendingIntent.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                obtain.writeStrongBinder(gzVar != null ? gzVar.asBinder() : null);
                obtain.writeString(str);
                this.kn.transact(MMAdView.TRANSITION_UP, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(Location location, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                if (location != null) {
                    obtain.writeInt(1);
                    location.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                obtain.writeInt(i);
                this.kn.transact(ApiEventType.API_MRAID_VIBRATE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(gz gzVar, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                obtain.writeStrongBinder(gzVar != null ? gzVar.asBinder() : null);
                obtain.writeString(str);
                this.kn.transact(MMAdView.TRANSITION_RANDOM, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(hg hgVar, hs hsVar, hq hqVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                if (hgVar != null) {
                    obtain.writeInt(1);
                    hgVar.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                if (hsVar != null) {
                    obtain.writeInt(1);
                    hsVar.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                obtain.writeStrongBinder(hqVar != null ? hqVar.asBinder() : null);
                this.kn.transact(ApiEventType.API_MRAID_GET_SCREEN_SIZE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(hi hiVar, hs hsVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                if (hiVar != null) {
                    obtain.writeInt(1);
                    hiVar.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                if (hsVar != null) {
                    obtain.writeInt(1);
                    hsVar.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(ApiEventType.API_MRAID_GET_GALLERY_IMAGE, obtain, null, 1);
                obtain.recycle();
            }

            public void a(hk hkVar, hs hsVar, PendingIntent pendingIntent) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                if (hkVar != null) {
                    obtain.writeInt(1);
                    hkVar.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                if (hsVar != null) {
                    obtain.writeInt(1);
                    hsVar.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                if (pendingIntent != null) {
                    obtain.writeInt(1);
                    pendingIntent.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(ApiEventType.API_MRAID_GET_CURRENT_POSITION, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(ho hoVar, hs hsVar, hq hqVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                if (hoVar != null) {
                    obtain.writeInt(1);
                    hoVar.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                if (hsVar != null) {
                    obtain.writeInt(1);
                    hsVar.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                obtain.writeStrongBinder(hqVar != null ? hqVar.asBinder() : null);
                this.kn.transact(ApiEventType.API_MRAID_SEEK_VIDEO, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(hs hsVar, PendingIntent pendingIntent) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                if (hsVar != null) {
                    obtain.writeInt(1);
                    hsVar.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                if (pendingIntent != null) {
                    obtain.writeInt(1);
                    pendingIntent.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(Encoder.LINE_GROUPS, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(LocationRequest locationRequest, PendingIntent pendingIntent) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                if (locationRequest != null) {
                    obtain.writeInt(1);
                    locationRequest.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                if (pendingIntent != null) {
                    obtain.writeInt(1);
                    pendingIntent.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(LocationRequest locationRequest, com.google.android.gms.location.a aVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                if (locationRequest != null) {
                    obtain.writeInt(1);
                    locationRequest.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                obtain.writeStrongBinder(aVar != null ? aVar.asBinder() : null);
                this.kn.transact(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(LocationRequest locationRequest, com.google.android.gms.location.a aVar, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                if (locationRequest != null) {
                    obtain.writeInt(1);
                    locationRequest.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                obtain.writeStrongBinder(aVar != null ? aVar.asBinder() : null);
                obtain.writeString(str);
                this.kn.transact(ApiEventType.API_MRAID_GET_MAX_SIZE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(com.google.android.gms.location.a aVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                obtain.writeStrongBinder(aVar != null ? aVar.asBinder() : null);
                this.kn.transact(ApiEventType.API_MRAID_USE_CUSTOM_CLOSE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(LatLng latLng, hg hgVar, hs hsVar, hq hqVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                if (latLng != null) {
                    obtain.writeInt(1);
                    latLng.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                if (hgVar != null) {
                    obtain.writeInt(1);
                    hgVar.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                if (hsVar != null) {
                    obtain.writeInt(1);
                    hsVar.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                obtain.writeStrongBinder(hqVar != null ? hqVar.asBinder() : null);
                this.kn.transact(ApiEventType.API_MRAID_GET_ORIENTATION, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(LatLngBounds latLngBounds, int i, hg hgVar, hs hsVar, hq hqVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                if (latLngBounds != null) {
                    obtain.writeInt(1);
                    latLngBounds.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                obtain.writeInt(i);
                if (hgVar != null) {
                    obtain.writeInt(1);
                    hgVar.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                if (hsVar != null) {
                    obtain.writeInt(1);
                    hsVar.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                obtain.writeStrongBinder(hqVar != null ? hqVar.asBinder() : null);
                this.kn.transact(ApiEventType.API_MRAID_IS_VIEWABLE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(LatLngBounds latLngBounds, int i, String str, hg hgVar, hs hsVar, hq hqVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                if (latLngBounds != null) {
                    obtain.writeInt(1);
                    latLngBounds.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                obtain.writeInt(i);
                obtain.writeString(str);
                if (hgVar != null) {
                    obtain.writeInt(1);
                    hgVar.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                if (hsVar != null) {
                    obtain.writeInt(1);
                    hsVar.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                obtain.writeStrongBinder(hqVar != null ? hqVar.asBinder() : null);
                this.kn.transact(ApiEventType.API_MRAID_PAUSE_VIDEO, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(String str, hs hsVar, hq hqVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                obtain.writeString(str);
                if (hsVar != null) {
                    obtain.writeInt(1);
                    hsVar.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                obtain.writeStrongBinder(hqVar != null ? hqVar.asBinder() : null);
                this.kn.transact(ApiEventType.API_MRAID_GET_PLACEMENT_TYPE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(String str, LatLngBounds latLngBounds, hg hgVar, hs hsVar, hq hqVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                obtain.writeString(str);
                if (latLngBounds != null) {
                    obtain.writeInt(1);
                    latLngBounds.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                if (hgVar != null) {
                    obtain.writeInt(1);
                    hgVar.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                if (hsVar != null) {
                    obtain.writeInt(1);
                    hsVar.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                obtain.writeStrongBinder(hqVar != null ? hqVar.asBinder() : null);
                this.kn.transact(ApiEventType.API_MRAID_GET_VIDEO_VOLUME, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(List<hd> list, PendingIntent pendingIntent, gz gzVar, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                obtain.writeTypedList(list);
                if (pendingIntent != null) {
                    obtain.writeInt(1);
                    pendingIntent.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                obtain.writeStrongBinder(gzVar != null ? gzVar.asBinder() : null);
                obtain.writeString(str);
                this.kn.transact(1, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(String[] strArr, gz gzVar, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                obtain.writeStringArray(strArr);
                obtain.writeStrongBinder(gzVar != null ? gzVar.asBinder() : null);
                obtain.writeString(str);
                this.kn.transact(MMAdView.TRANSITION_DOWN, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public Location aW(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                obtain.writeString(str);
                this.kn.transact(ApiEventType.API_MRAID_POST_TO_SOCIAL, obtain, obtain2, 0);
                obtain2.readException();
                Location location = obtain2.readInt() != 0 ? (Location) Location.CREATOR.createFromParcel(obtain2) : null;
                obtain2.recycle();
                obtain.recycle();
                return location;
            }

            public b aX(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                obtain.writeString(str);
                this.kn.transact(ApiEventType.API_MRAID_UNMUTE_AUDIO, obtain, obtain2, 0);
                obtain2.readException();
                b aB = obtain2.readInt() != 0 ? b.CREATOR.aB(obtain2) : null;
                obtain2.recycle();
                obtain.recycle();
                return aB;
            }

            public IBinder asBinder() {
                return this.kn;
            }

            public void b(String str, hs hsVar, hq hqVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                obtain.writeString(str);
                if (hsVar != null) {
                    obtain.writeInt(1);
                    hsVar.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                obtain.writeStrongBinder(hqVar != null ? hqVar.asBinder() : null);
                this.kn.transact(ApiEventType.API_MRAID_UNMUTE_VIDEO, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public Location hP() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                this.kn.transact(ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                Location location = obtain2.readInt() != 0 ? (Location) Location.CREATOR.createFromParcel(obtain2) : null;
                obtain2.recycle();
                obtain.recycle();
                return location;
            }

            public void removeActivityUpdates(PendingIntent callbackIntent) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                if (callbackIntent != null) {
                    obtain.writeInt(1);
                    callbackIntent.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void setMockLocation(Location location) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                if (location != null) {
                    obtain.writeInt(1);
                    location.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(ApiEventType.API_MRAID_CLOSE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void setMockMode(boolean isMockMode) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                if (isMockMode) {
                    i = 1;
                }
                obtain.writeInt(i);
                this.kn.transact(ApiEventType.API_MRAID_RESIZE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }
        }

        public static ha W(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
            return (queryLocalInterface == null || !queryLocalInterface instanceof ha) ? new a(iBinder) : (ha) queryLocalInterface;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean z = false;
            hs hsVar = null;
            Location hP;
            LocationRequest createFromParcel;
            String readString;
            switch (code) {
                case MMAdView.TRANSITION_FADE:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    a(data.createTypedArrayList(hd.CREATOR), data.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(data) : null, com.google.android.gms.internal.gz.a.V(data.readStrongBinder()), data.readString());
                    reply.writeNoException();
                    return true;
                case MMAdView.TRANSITION_UP:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    a(data.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(data) : null, com.google.android.gms.internal.gz.a.V(data.readStrongBinder()), data.readString());
                    reply.writeNoException();
                    return true;
                case MMAdView.TRANSITION_DOWN:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    a(data.createStringArray(), com.google.android.gms.internal.gz.a.V(data.readStrongBinder()), data.readString());
                    reply.writeNoException();
                    return true;
                case MMAdView.TRANSITION_RANDOM:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    a(com.google.android.gms.internal.gz.a.V(data.readStrongBinder()), data.readString());
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    a(data.readLong(), data.readInt() != 0, data.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    removeActivityUpdates(data.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    hP = hP();
                    reply.writeNoException();
                    if (hP != null) {
                        reply.writeInt(1);
                        hP.writeToParcel(reply, 1);
                        return true;
                    } else {
                        reply.writeInt(0);
                        return true;
                    }
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    if (data.readInt() != 0) {
                        createFromParcel = LocationRequest.CREATOR.createFromParcel(data);
                    }
                    a(createFromParcel, com.google.android.gms.location.a.a.U(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    a(data.readInt() != 0 ? LocationRequest.CREATOR.createFromParcel(data) : null, data.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    a(com.google.android.gms.location.a.a.U(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_EXPAND:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    a(data.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_RESIZE:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    setMockMode(z);
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_CLOSE:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    setMockLocation(data.readInt() != 0 ? (Location) Location.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_IS_VIEWABLE:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    a(data.readInt() != 0 ? LatLngBounds.CREATOR.createFromParcel(data) : null, data.readInt(), data.readInt() != 0 ? hg.CREATOR.aD(data) : null, data.readInt() != 0 ? hs.CREATOR.aI(data) : null, com.google.android.gms.internal.hq.a.Y(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_GET_PLACEMENT_TYPE:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    readString = data.readString();
                    if (data.readInt() != 0) {
                        hsVar = hs.CREATOR.aI(data);
                    }
                    a(readString, hsVar, com.google.android.gms.internal.hq.a.Y(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_GET_ORIENTATION:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    LatLng createFromParcel2 = data.readInt() != 0 ? LatLng.CREATOR.createFromParcel(data) : null;
                    hg aD = data.readInt() != 0 ? hg.CREATOR.aD(data) : null;
                    if (data.readInt() != 0) {
                        hsVar = hs.CREATOR.aI(data);
                    }
                    a(createFromParcel2, aD, hsVar, com.google.android.gms.internal.hq.a.Y(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_GET_SCREEN_SIZE:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    hg aD2 = data.readInt() != 0 ? hg.CREATOR.aD(data) : null;
                    if (data.readInt() != 0) {
                        hsVar = hs.CREATOR.aI(data);
                    }
                    a(aD2, hsVar, com.google.android.gms.internal.hq.a.Y(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_GET_CURRENT_POSITION:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    a(data.readInt() != 0 ? hk.CREATOR.aF(data) : null, data.readInt() != 0 ? hs.CREATOR.aI(data) : null, data.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case Encoder.LINE_GROUPS:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    a(data.readInt() != 0 ? hs.CREATOR.aI(data) : null, data.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_GET_MAX_SIZE:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    if (data.readInt() != 0) {
                        createFromParcel = LocationRequest.CREATOR.createFromParcel(data);
                    }
                    a(createFromParcel, com.google.android.gms.location.a.a.U(data.readStrongBinder()), data.readString());
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_POST_TO_SOCIAL:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    hP = aW(data.readString());
                    reply.writeNoException();
                    if (hP != null) {
                        reply.writeInt(1);
                        hP.writeToParcel(reply, 1);
                        return true;
                    } else {
                        reply.writeInt(0);
                        return true;
                    }
                case ApiEventType.API_MRAID_GET_GALLERY_IMAGE:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    hi aE = data.readInt() != 0 ? hi.CREATOR.aE(data) : null;
                    if (data.readInt() != 0) {
                        hsVar = hs.CREATOR.aI(data);
                    }
                    a(aE, hsVar);
                    return true;
                case ApiEventType.API_MRAID_VIBRATE:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    a(data.readInt() != 0 ? (Location) Location.CREATOR.createFromParcel(data) : null, data.readInt());
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_UNMUTE_AUDIO:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    b aX = aX(data.readString());
                    reply.writeNoException();
                    if (aX != null) {
                        reply.writeInt(1);
                        aX.writeToParcel(reply, 1);
                        return true;
                    } else {
                        reply.writeInt(0);
                        return true;
                    }
                case ApiEventType.API_MRAID_UNMUTE_VIDEO:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    readString = data.readString();
                    if (data.readInt() != 0) {
                        hsVar = hs.CREATOR.aI(data);
                    }
                    b(readString, hsVar, com.google.android.gms.internal.hq.a.Y(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_GET_VIDEO_VOLUME:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    a(data.readString(), data.readInt() != 0 ? LatLngBounds.CREATOR.createFromParcel(data) : null, data.readInt() != 0 ? hg.CREATOR.aD(data) : null, data.readInt() != 0 ? hs.CREATOR.aI(data) : null, com.google.android.gms.internal.hq.a.Y(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_SEEK_VIDEO:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    ho hoVar = data.readInt() != 0 ? (ho) ho.CREATOR.createFromParcel(data) : null;
                    if (data.readInt() != 0) {
                        hsVar = hs.CREATOR.aI(data);
                    }
                    a(hoVar, hsVar, com.google.android.gms.internal.hq.a.Y(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_PAUSE_VIDEO:
                    data.enforceInterface("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    LatLngBounds createFromParcel3 = data.readInt() != 0 ? LatLngBounds.CREATOR.createFromParcel(data) : null;
                    int readInt = data.readInt();
                    String readString2 = data.readString();
                    hg aD3 = data.readInt() != 0 ? hg.CREATOR.aD(data) : null;
                    if (data.readInt() != 0) {
                        hsVar = hs.CREATOR.aI(data);
                    }
                    a(createFromParcel3, readInt, readString2, aD3, hsVar, com.google.android.gms.internal.hq.a.Y(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.location.internal.IGoogleLocationManagerService");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void a(long j, boolean z, PendingIntent pendingIntent) throws RemoteException;

    void a(PendingIntent pendingIntent) throws RemoteException;

    void a(PendingIntent pendingIntent, gz gzVar, String str) throws RemoteException;

    void a(Location location, int i) throws RemoteException;

    void a(gz gzVar, String str) throws RemoteException;

    void a(hg hgVar, hs hsVar, hq hqVar) throws RemoteException;

    void a(hi hiVar, hs hsVar) throws RemoteException;

    void a(hk hkVar, hs hsVar, PendingIntent pendingIntent) throws RemoteException;

    void a(ho hoVar, hs hsVar, hq hqVar) throws RemoteException;

    void a(hs hsVar, PendingIntent pendingIntent) throws RemoteException;

    void a(LocationRequest locationRequest, PendingIntent pendingIntent) throws RemoteException;

    void a(LocationRequest locationRequest, com.google.android.gms.location.a aVar) throws RemoteException;

    void a(LocationRequest locationRequest, com.google.android.gms.location.a aVar, String str) throws RemoteException;

    void a(com.google.android.gms.location.a aVar) throws RemoteException;

    void a(LatLng latLng, hg hgVar, hs hsVar, hq hqVar) throws RemoteException;

    void a(LatLngBounds latLngBounds, int i, hg hgVar, hs hsVar, hq hqVar) throws RemoteException;

    void a(LatLngBounds latLngBounds, int i, String str, hg hgVar, hs hsVar, hq hqVar) throws RemoteException;

    void a(String str, hs hsVar, hq hqVar) throws RemoteException;

    void a(String str, LatLngBounds latLngBounds, hg hgVar, hs hsVar, hq hqVar) throws RemoteException;

    void a(List<hd> list, PendingIntent pendingIntent, gz gzVar, String str) throws RemoteException;

    void a(String[] strArr, gz gzVar, String str) throws RemoteException;

    Location aW(String str) throws RemoteException;

    b aX(String str) throws RemoteException;

    void b(String str, hs hsVar, hq hqVar) throws RemoteException;

    Location hP() throws RemoteException;

    void removeActivityUpdates(PendingIntent pendingIntent) throws RemoteException;

    void setMockLocation(Location location) throws RemoteException;

    void setMockMode(boolean z) throws RemoteException;
}