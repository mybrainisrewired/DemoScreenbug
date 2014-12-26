package com.google.android.gms.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.common.data.DataHolder;
import com.inmobi.commons.analytics.iat.impl.AdTrackerConstants;

public interface ej extends IInterface {

    public static abstract class a extends Binder implements ej {

        private static class a implements ej {
            private IBinder kn;

            a(IBinder iBinder) {
                this.kn = iBinder;
            }

            public void a(int i, DataHolder dataHolder) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.appstate.internal.IAppStateCallbacks");
                obtain.writeInt(i);
                if (dataHolder != null) {
                    obtain.writeInt(1);
                    dataHolder.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(AdTrackerConstants.WEBVIEW_INVALIDPARAM, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(DataHolder dataHolder) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.appstate.internal.IAppStateCallbacks");
                if (dataHolder != null) {
                    obtain.writeInt(1);
                    dataHolder.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(AdTrackerConstants.WEBVIEW_XMLHTTPSUPPORT, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public IBinder asBinder() {
                return this.kn;
            }

            public void b(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.appstate.internal.IAppStateCallbacks");
                obtain.writeInt(i);
                obtain.writeInt(i2);
                this.kn.transact(AdTrackerConstants.WEBVIEW_TIMEOUT, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void du() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.appstate.internal.IAppStateCallbacks");
                this.kn.transact(AdTrackerConstants.WEBVIEW_SERVERERROR, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void v(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.appstate.internal.IAppStateCallbacks");
                obtain.writeInt(i);
                this.kn.transact(AdTrackerConstants.WEBVIEW_INVALIDJSON, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }
        }

        public a() {
            attachInterface(this, "com.google.android.gms.appstate.internal.IAppStateCallbacks");
        }

        public static ej v(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.appstate.internal.IAppStateCallbacks");
            return (queryLocalInterface == null || !queryLocalInterface instanceof ej) ? new a(iBinder) : (ej) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            DataHolder dataHolder = null;
            switch (code) {
                case AdTrackerConstants.WEBVIEW_INVALIDPARAM:
                    data.enforceInterface("com.google.android.gms.appstate.internal.IAppStateCallbacks");
                    int readInt = data.readInt();
                    if (data.readInt() != 0) {
                        dataHolder = DataHolder.CREATOR.createFromParcel(data);
                    }
                    a(readInt, dataHolder);
                    reply.writeNoException();
                    return true;
                case AdTrackerConstants.WEBVIEW_XMLHTTPSUPPORT:
                    data.enforceInterface("com.google.android.gms.appstate.internal.IAppStateCallbacks");
                    if (data.readInt() != 0) {
                        dataHolder = DataHolder.CREATOR.createFromParcel(data);
                    }
                    a(dataHolder);
                    reply.writeNoException();
                    return true;
                case AdTrackerConstants.WEBVIEW_TIMEOUT:
                    data.enforceInterface("com.google.android.gms.appstate.internal.IAppStateCallbacks");
                    b(data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case AdTrackerConstants.WEBVIEW_SERVERERROR:
                    data.enforceInterface("com.google.android.gms.appstate.internal.IAppStateCallbacks");
                    du();
                    reply.writeNoException();
                    return true;
                case AdTrackerConstants.WEBVIEW_INVALIDJSON:
                    data.enforceInterface("com.google.android.gms.appstate.internal.IAppStateCallbacks");
                    v(data.readInt());
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.appstate.internal.IAppStateCallbacks");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void a(int i, DataHolder dataHolder) throws RemoteException;

    void a(DataHolder dataHolder) throws RemoteException;

    void b(int i, int i2) throws RemoteException;

    void du() throws RemoteException;

    void v(int i) throws RemoteException;
}