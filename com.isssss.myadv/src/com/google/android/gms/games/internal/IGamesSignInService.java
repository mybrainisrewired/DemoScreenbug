package com.google.android.gms.games.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.inmobi.commons.analytics.iat.impl.AdTrackerConstants;

public interface IGamesSignInService extends IInterface {

    public static abstract class Stub extends Binder implements IGamesSignInService {

        private static class Proxy implements IGamesSignInService {
            private IBinder kn;

            public void a(IGamesSignInCallbacks iGamesSignInCallbacks, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesSignInService");
                obtain.writeStrongBinder(iGamesSignInCallbacks != null ? iGamesSignInCallbacks.asBinder() : null);
                obtain.writeString(str);
                obtain.writeString(str2);
                this.kn.transact(5006, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(IGamesSignInCallbacks iGamesSignInCallbacks, String str, String str2, String str3) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesSignInService");
                obtain.writeStrongBinder(iGamesSignInCallbacks != null ? iGamesSignInCallbacks.asBinder() : null);
                obtain.writeString(str);
                obtain.writeString(str2);
                obtain.writeString(str3);
                this.kn.transact(AdTrackerConstants.WEBVIEW_INVALIDJSON, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(IGamesSignInCallbacks iGamesSignInCallbacks, String str, String str2, String[] strArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesSignInService");
                obtain.writeStrongBinder(iGamesSignInCallbacks != null ? iGamesSignInCallbacks.asBinder() : null);
                obtain.writeString(str);
                obtain.writeString(str2);
                obtain.writeStringArray(strArr);
                this.kn.transact(AdTrackerConstants.WEBVIEW_SERVERERROR, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(IGamesSignInCallbacks iGamesSignInCallbacks, String str, String str2, String[] strArr, String str3) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesSignInService");
                obtain.writeStrongBinder(iGamesSignInCallbacks != null ? iGamesSignInCallbacks.asBinder() : null);
                obtain.writeString(str);
                obtain.writeString(str2);
                obtain.writeStringArray(strArr);
                obtain.writeString(str3);
                this.kn.transact(AdTrackerConstants.WEBVIEW_TIMEOUT, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public String aK(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesSignInService");
                obtain.writeString(str);
                this.kn.transact(AdTrackerConstants.WEBVIEW_INVALIDPARAM, obtain, obtain2, 0);
                obtain2.readException();
                String readString = obtain2.readString();
                obtain2.recycle();
                obtain.recycle();
                return readString;
            }

            public String aL(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesSignInService");
                obtain.writeString(str);
                this.kn.transact(AdTrackerConstants.WEBVIEW_XMLHTTPSUPPORT, obtain, obtain2, 0);
                obtain2.readException();
                String readString = obtain2.readString();
                obtain2.recycle();
                obtain.recycle();
                return readString;
            }

            public IBinder asBinder() {
                return this.kn;
            }

            public void b(IGamesSignInCallbacks iGamesSignInCallbacks, String str, String str2, String str3) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesSignInService");
                obtain.writeStrongBinder(iGamesSignInCallbacks != null ? iGamesSignInCallbacks.asBinder() : null);
                obtain.writeString(str);
                obtain.writeString(str2);
                obtain.writeString(str3);
                this.kn.transact(5007, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void b(IGamesSignInCallbacks iGamesSignInCallbacks, String str, String str2, String[] strArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesSignInService");
                obtain.writeStrongBinder(iGamesSignInCallbacks != null ? iGamesSignInCallbacks.asBinder() : null);
                obtain.writeString(str);
                obtain.writeString(str2);
                obtain.writeStringArray(strArr);
                this.kn.transact(5008, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public String f(String str, boolean z) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesSignInService");
                obtain.writeString(str);
                if (z) {
                    i = 1;
                }
                obtain.writeInt(i);
                this.kn.transact(5009, obtain, obtain2, 0);
                obtain2.readException();
                String readString = obtain2.readString();
                obtain2.recycle();
                obtain.recycle();
                return readString;
            }

            public void l(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesSignInService");
                obtain.writeString(str);
                obtain.writeString(str2);
                this.kn.transact(9001, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }
        }

        public Stub() {
            attachInterface(this, "com.google.android.gms.games.internal.IGamesSignInService");
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String aK;
            switch (code) {
                case AdTrackerConstants.WEBVIEW_INVALIDPARAM:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesSignInService");
                    aK = aK(data.readString());
                    reply.writeNoException();
                    reply.writeString(aK);
                    return true;
                case AdTrackerConstants.WEBVIEW_XMLHTTPSUPPORT:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesSignInService");
                    aK = aL(data.readString());
                    reply.writeNoException();
                    reply.writeString(aK);
                    return true;
                case AdTrackerConstants.WEBVIEW_TIMEOUT:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesSignInService");
                    a(com.google.android.gms.games.internal.IGamesSignInCallbacks.Stub.O(data.readStrongBinder()), data.readString(), data.readString(), data.createStringArray(), data.readString());
                    reply.writeNoException();
                    return true;
                case AdTrackerConstants.WEBVIEW_SERVERERROR:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesSignInService");
                    a(com.google.android.gms.games.internal.IGamesSignInCallbacks.Stub.O(data.readStrongBinder()), data.readString(), data.readString(), data.createStringArray());
                    reply.writeNoException();
                    return true;
                case AdTrackerConstants.WEBVIEW_INVALIDJSON:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesSignInService");
                    a(com.google.android.gms.games.internal.IGamesSignInCallbacks.Stub.O(data.readStrongBinder()), data.readString(), data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 5006:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesSignInService");
                    a(com.google.android.gms.games.internal.IGamesSignInCallbacks.Stub.O(data.readStrongBinder()), data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 5007:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesSignInService");
                    b(com.google.android.gms.games.internal.IGamesSignInCallbacks.Stub.O(data.readStrongBinder()), data.readString(), data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 5008:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesSignInService");
                    b(com.google.android.gms.games.internal.IGamesSignInCallbacks.Stub.O(data.readStrongBinder()), data.readString(), data.readString(), data.createStringArray());
                    reply.writeNoException();
                    return true;
                case 5009:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesSignInService");
                    aK = f(data.readString(), data.readInt() != 0);
                    reply.writeNoException();
                    reply.writeString(aK);
                    return true;
                case 9001:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesSignInService");
                    l(data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.games.internal.IGamesSignInService");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void a(IGamesSignInCallbacks iGamesSignInCallbacks, String str, String str2) throws RemoteException;

    void a(IGamesSignInCallbacks iGamesSignInCallbacks, String str, String str2, String str3) throws RemoteException;

    void a(IGamesSignInCallbacks iGamesSignInCallbacks, String str, String str2, String[] strArr) throws RemoteException;

    void a(IGamesSignInCallbacks iGamesSignInCallbacks, String str, String str2, String[] strArr, String str3) throws RemoteException;

    String aK(String str) throws RemoteException;

    String aL(String str) throws RemoteException;

    void b(IGamesSignInCallbacks iGamesSignInCallbacks, String str, String str2, String str3) throws RemoteException;

    void b(IGamesSignInCallbacks iGamesSignInCallbacks, String str, String str2, String[] strArr) throws RemoteException;

    String f(String str, boolean z) throws RemoteException;

    void l(String str, String str2) throws RemoteException;
}