package com.google.android.gms.games.internal;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.common.data.DataHolder;
import com.inmobi.commons.analytics.iat.impl.AdTrackerConstants;

public interface IGamesSignInCallbacks extends IInterface {

    public static abstract class Stub extends Binder implements IGamesSignInCallbacks {

        private static class Proxy implements IGamesSignInCallbacks {
            private IBinder kn;

            Proxy(IBinder remote) {
                this.kn = remote;
            }

            public void H(DataHolder dataHolder) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesSignInCallbacks");
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

            public void I(DataHolder dataHolder) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesSignInCallbacks");
                if (dataHolder != null) {
                    obtain.writeInt(1);
                    dataHolder.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(AdTrackerConstants.WEBVIEW_TIMEOUT, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void aZ(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesSignInCallbacks");
                obtain.writeInt(i);
                this.kn.transact(AdTrackerConstants.WEBVIEW_SERVERERROR, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public IBinder asBinder() {
                return this.kn;
            }

            public void b(int i, Intent intent) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesSignInCallbacks");
                obtain.writeInt(i);
                if (intent != null) {
                    obtain.writeInt(1);
                    intent.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(AdTrackerConstants.WEBVIEW_INVALIDPARAM, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void ba(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesSignInCallbacks");
                obtain.writeInt(i);
                this.kn.transact(5006, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void e(DataHolder dataHolder) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesSignInCallbacks");
                if (dataHolder != null) {
                    obtain.writeInt(1);
                    dataHolder.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(AdTrackerConstants.WEBVIEW_INVALIDJSON, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }
        }

        public Stub() {
            attachInterface(this, "com.google.android.gms.games.internal.IGamesSignInCallbacks");
        }

        public static IGamesSignInCallbacks O(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.games.internal.IGamesSignInCallbacks");
            return (queryLocalInterface == null || !queryLocalInterface instanceof IGamesSignInCallbacks) ? new Proxy(iBinder) : (IGamesSignInCallbacks) queryLocalInterface;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            DataHolder dataHolder = null;
            switch (code) {
                case AdTrackerConstants.WEBVIEW_INVALIDPARAM:
                    Intent intent;
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesSignInCallbacks");
                    int readInt = data.readInt();
                    if (data.readInt() != 0) {
                        intent = (Intent) Intent.CREATOR.createFromParcel(data);
                    }
                    b(readInt, intent);
                    reply.writeNoException();
                    return true;
                case AdTrackerConstants.WEBVIEW_XMLHTTPSUPPORT:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesSignInCallbacks");
                    if (data.readInt() != 0) {
                        dataHolder = DataHolder.CREATOR.createFromParcel(data);
                    }
                    H(dataHolder);
                    reply.writeNoException();
                    return true;
                case AdTrackerConstants.WEBVIEW_TIMEOUT:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesSignInCallbacks");
                    if (data.readInt() != 0) {
                        dataHolder = DataHolder.CREATOR.createFromParcel(data);
                    }
                    I(dataHolder);
                    reply.writeNoException();
                    return true;
                case AdTrackerConstants.WEBVIEW_SERVERERROR:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesSignInCallbacks");
                    aZ(data.readInt());
                    reply.writeNoException();
                    return true;
                case AdTrackerConstants.WEBVIEW_INVALIDJSON:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesSignInCallbacks");
                    if (data.readInt() != 0) {
                        dataHolder = DataHolder.CREATOR.createFromParcel(data);
                    }
                    e(dataHolder);
                    reply.writeNoException();
                    return true;
                case 5006:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesSignInCallbacks");
                    ba(data.readInt());
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.games.internal.IGamesSignInCallbacks");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void H(DataHolder dataHolder) throws RemoteException;

    void I(DataHolder dataHolder) throws RemoteException;

    void aZ(int i) throws RemoteException;

    void b(int i, Intent intent) throws RemoteException;

    void ba(int i) throws RemoteException;

    void e(DataHolder dataHolder) throws RemoteException;
}