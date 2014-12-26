package com.google.android.gms.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public interface ep extends IInterface {

    public static abstract class a extends Binder implements ep {

        private static class a implements ep {
            private IBinder kn;

            a(IBinder iBinder) {
                this.kn = iBinder;
            }

            public void Y(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.cast.internal.ICastDeviceController");
                obtain.writeString(str);
                this.kn.transact(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, obtain, null, 1);
                obtain.recycle();
            }

            public void Z(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.cast.internal.ICastDeviceController");
                obtain.writeString(str);
                this.kn.transact(ApiEventType.API_MRAID_EXPAND, obtain, null, 1);
                obtain.recycle();
            }

            public void a(double d, double d2, boolean z) throws RemoteException {
                int i = 1;
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.cast.internal.ICastDeviceController");
                obtain.writeDouble(d);
                obtain.writeDouble(d2);
                if (!z) {
                    i = 0;
                }
                obtain.writeInt(i);
                this.kn.transact(ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, obtain, null, 1);
                obtain.recycle();
            }

            public void a(String str, String str2, long j) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.cast.internal.ICastDeviceController");
                obtain.writeString(str);
                obtain.writeString(str2);
                obtain.writeLong(j);
                this.kn.transact(ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES, obtain, null, 1);
                obtain.recycle();
            }

            public void a(String str, byte[] bArr, long j) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.cast.internal.ICastDeviceController");
                obtain.writeString(str);
                obtain.writeByteArray(bArr);
                obtain.writeLong(j);
                this.kn.transact(ApiEventType.API_MRAID_USE_CUSTOM_CLOSE, obtain, null, 1);
                obtain.recycle();
            }

            public void a(boolean z, double d, boolean z2) throws RemoteException {
                int i = 1;
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.cast.internal.ICastDeviceController");
                obtain.writeInt(z ? 1 : 0);
                obtain.writeDouble(d);
                if (!z2) {
                    i = 0;
                }
                obtain.writeInt(i);
                this.kn.transact(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, obtain, null, 1);
                obtain.recycle();
            }

            public void aa(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.cast.internal.ICastDeviceController");
                obtain.writeString(str);
                this.kn.transact(ApiEventType.API_MRAID_RESIZE, obtain, null, 1);
                obtain.recycle();
            }

            public IBinder asBinder() {
                return this.kn;
            }

            public void dH() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.cast.internal.ICastDeviceController");
                this.kn.transact(ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, obtain, null, 1);
                obtain.recycle();
            }

            public void dO() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.cast.internal.ICastDeviceController");
                this.kn.transact(MMAdView.TRANSITION_RANDOM, obtain, null, 1);
                obtain.recycle();
            }

            public void disconnect() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.cast.internal.ICastDeviceController");
                this.kn.transact(1, obtain, null, 1);
                obtain.recycle();
            }

            public void e(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.cast.internal.ICastDeviceController");
                obtain.writeString(str);
                obtain.writeString(str2);
                this.kn.transact(MMAdView.TRANSITION_DOWN, obtain, null, 1);
                obtain.recycle();
            }

            public void e(String str, boolean z) throws RemoteException {
                int i = 1;
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.cast.internal.ICastDeviceController");
                obtain.writeString(str);
                if (!z) {
                    i = 0;
                }
                obtain.writeInt(i);
                this.kn.transact(MMAdView.TRANSITION_UP, obtain, null, 1);
                obtain.recycle();
            }
        }

        public static ep y(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.cast.internal.ICastDeviceController");
            return (queryLocalInterface == null || !queryLocalInterface instanceof ep) ? new a(iBinder) : (ep) queryLocalInterface;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean z = false;
            switch (code) {
                case MMAdView.TRANSITION_FADE:
                    data.enforceInterface("com.google.android.gms.cast.internal.ICastDeviceController");
                    disconnect();
                    return true;
                case MMAdView.TRANSITION_UP:
                    data.enforceInterface("com.google.android.gms.cast.internal.ICastDeviceController");
                    String readString = data.readString();
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    e(readString, z);
                    return true;
                case MMAdView.TRANSITION_DOWN:
                    data.enforceInterface("com.google.android.gms.cast.internal.ICastDeviceController");
                    e(data.readString(), data.readString());
                    return true;
                case MMAdView.TRANSITION_RANDOM:
                    data.enforceInterface("com.google.android.gms.cast.internal.ICastDeviceController");
                    dO();
                    return true;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.cast.internal.ICastDeviceController");
                    Y(data.readString());
                    return true;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.cast.internal.ICastDeviceController");
                    dH();
                    return true;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.cast.internal.ICastDeviceController");
                    a(data.readDouble(), data.readDouble(), data.readInt() != 0);
                    return true;
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.cast.internal.ICastDeviceController");
                    boolean z2 = data.readInt() != 0;
                    double readDouble = data.readDouble();
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    a(z2, readDouble, z);
                    return true;
                case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.cast.internal.ICastDeviceController");
                    a(data.readString(), data.readString(), data.readLong());
                    return true;
                case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                    data.enforceInterface("com.google.android.gms.cast.internal.ICastDeviceController");
                    a(data.readString(), data.createByteArray(), data.readLong());
                    return true;
                case ApiEventType.API_MRAID_EXPAND:
                    data.enforceInterface("com.google.android.gms.cast.internal.ICastDeviceController");
                    Z(data.readString());
                    return true;
                case ApiEventType.API_MRAID_RESIZE:
                    data.enforceInterface("com.google.android.gms.cast.internal.ICastDeviceController");
                    aa(data.readString());
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.cast.internal.ICastDeviceController");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void Y(String str) throws RemoteException;

    void Z(String str) throws RemoteException;

    void a(double d, double d2, boolean z) throws RemoteException;

    void a(String str, String str2, long j) throws RemoteException;

    void a(String str, byte[] bArr, long j) throws RemoteException;

    void a(boolean z, double d, boolean z2) throws RemoteException;

    void aa(String str) throws RemoteException;

    void dH() throws RemoteException;

    void dO() throws RemoteException;

    void disconnect() throws RemoteException;

    void e(String str, String str2) throws RemoteException;

    void e(String str, boolean z) throws RemoteException;
}