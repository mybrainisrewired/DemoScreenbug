package com.google.android.gms.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.inmobi.commons.analytics.iat.impl.AdTrackerConstants;

public interface ek extends IInterface {

    public static abstract class a extends Binder implements ek {

        private static class a implements ek {
            private IBinder kn;

            a(IBinder iBinder) {
                this.kn = iBinder;
            }

            public void a(ej ejVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.appstate.internal.IAppStateService");
                obtain.writeStrongBinder(ejVar != null ? ejVar.asBinder() : null);
                this.kn.transact(AdTrackerConstants.WEBVIEW_INVALIDJSON, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(ej ejVar, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.appstate.internal.IAppStateService");
                obtain.writeStrongBinder(ejVar != null ? ejVar.asBinder() : null);
                obtain.writeInt(i);
                this.kn.transact(AdTrackerConstants.WEBVIEW_SERVERERROR, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(ej ejVar, int i, String str, byte[] bArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.appstate.internal.IAppStateService");
                obtain.writeStrongBinder(ejVar != null ? ejVar.asBinder() : null);
                obtain.writeInt(i);
                obtain.writeString(str);
                obtain.writeByteArray(bArr);
                this.kn.transact(5006, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(ej ejVar, int i, byte[] bArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.appstate.internal.IAppStateService");
                obtain.writeStrongBinder(ejVar != null ? ejVar.asBinder() : null);
                obtain.writeInt(i);
                obtain.writeByteArray(bArr);
                this.kn.transact(AdTrackerConstants.WEBVIEW_TIMEOUT, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public IBinder asBinder() {
                return this.kn;
            }

            public void b(ej ejVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.appstate.internal.IAppStateService");
                obtain.writeStrongBinder(ejVar != null ? ejVar.asBinder() : null);
                this.kn.transact(5008, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void b(ej ejVar, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.appstate.internal.IAppStateService");
                obtain.writeStrongBinder(ejVar != null ? ejVar.asBinder() : null);
                obtain.writeInt(i);
                this.kn.transact(5007, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void c(ej ejVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.appstate.internal.IAppStateService");
                obtain.writeStrongBinder(ejVar != null ? ejVar.asBinder() : null);
                this.kn.transact(5009, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public int dv() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.appstate.internal.IAppStateService");
                this.kn.transact(AdTrackerConstants.WEBVIEW_INVALIDPARAM, obtain, obtain2, 0);
                obtain2.readException();
                int readInt = obtain2.readInt();
                obtain2.recycle();
                obtain.recycle();
                return readInt;
            }

            public int dw() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.appstate.internal.IAppStateService");
                this.kn.transact(AdTrackerConstants.WEBVIEW_XMLHTTPSUPPORT, obtain, obtain2, 0);
                obtain2.readException();
                int readInt = obtain2.readInt();
                obtain2.recycle();
                obtain.recycle();
                return readInt;
            }
        }

        public static ek w(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.appstate.internal.IAppStateService");
            return (queryLocalInterface == null || !queryLocalInterface instanceof ek) ? new a(iBinder) : (ek) queryLocalInterface;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int dv;
            switch (code) {
                case AdTrackerConstants.WEBVIEW_INVALIDPARAM:
                    data.enforceInterface("com.google.android.gms.appstate.internal.IAppStateService");
                    dv = dv();
                    reply.writeNoException();
                    reply.writeInt(dv);
                    return true;
                case AdTrackerConstants.WEBVIEW_XMLHTTPSUPPORT:
                    data.enforceInterface("com.google.android.gms.appstate.internal.IAppStateService");
                    dv = dw();
                    reply.writeNoException();
                    reply.writeInt(dv);
                    return true;
                case AdTrackerConstants.WEBVIEW_TIMEOUT:
                    data.enforceInterface("com.google.android.gms.appstate.internal.IAppStateService");
                    a(com.google.android.gms.internal.ej.a.v(data.readStrongBinder()), data.readInt(), data.createByteArray());
                    reply.writeNoException();
                    return true;
                case AdTrackerConstants.WEBVIEW_SERVERERROR:
                    data.enforceInterface("com.google.android.gms.appstate.internal.IAppStateService");
                    a(com.google.android.gms.internal.ej.a.v(data.readStrongBinder()), data.readInt());
                    reply.writeNoException();
                    return true;
                case AdTrackerConstants.WEBVIEW_INVALIDJSON:
                    data.enforceInterface("com.google.android.gms.appstate.internal.IAppStateService");
                    a(com.google.android.gms.internal.ej.a.v(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 5006:
                    data.enforceInterface("com.google.android.gms.appstate.internal.IAppStateService");
                    a(com.google.android.gms.internal.ej.a.v(data.readStrongBinder()), data.readInt(), data.readString(), data.createByteArray());
                    reply.writeNoException();
                    return true;
                case 5007:
                    data.enforceInterface("com.google.android.gms.appstate.internal.IAppStateService");
                    b(com.google.android.gms.internal.ej.a.v(data.readStrongBinder()), data.readInt());
                    reply.writeNoException();
                    return true;
                case 5008:
                    data.enforceInterface("com.google.android.gms.appstate.internal.IAppStateService");
                    b(com.google.android.gms.internal.ej.a.v(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 5009:
                    data.enforceInterface("com.google.android.gms.appstate.internal.IAppStateService");
                    c(com.google.android.gms.internal.ej.a.v(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.appstate.internal.IAppStateService");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void a(ej ejVar) throws RemoteException;

    void a(ej ejVar, int i) throws RemoteException;

    void a(ej ejVar, int i, String str, byte[] bArr) throws RemoteException;

    void a(ej ejVar, int i, byte[] bArr) throws RemoteException;

    void b(ej ejVar) throws RemoteException;

    void b(ej ejVar, int i) throws RemoteException;

    void c(ej ejVar) throws RemoteException;

    int dv() throws RemoteException;

    int dw() throws RemoteException;
}