package com.google.android.gms.games.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;

public interface IRoomServiceCallbacks extends IInterface {

    public static abstract class Stub extends Binder implements IRoomServiceCallbacks {

        private static class Proxy implements IRoomServiceCallbacks {
            private IBinder kn;

            Proxy(IBinder remote) {
                this.kn = remote;
            }

            public void P(IBinder iBinder) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                obtain.writeStrongBinder(iBinder);
                this.kn.transact(1021, obtain, null, 1);
                obtain.recycle();
            }

            public void a(ParcelFileDescriptor parcelFileDescriptor, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                if (parcelFileDescriptor != null) {
                    obtain.writeInt(1);
                    parcelFileDescriptor.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                obtain.writeInt(i);
                this.kn.transact(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT, obtain, null, 1);
                obtain.recycle();
            }

            public void a(ConnectionInfo connectionInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                if (connectionInfo != null) {
                    obtain.writeInt(1);
                    connectionInfo.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(1022, obtain, null, 1);
                obtain.recycle();
            }

            public void a(String str, byte[] bArr, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                obtain.writeString(str);
                obtain.writeByteArray(bArr);
                obtain.writeInt(i);
                this.kn.transact(ApiEventType.API_IMAI_OPEN_EXTERNAL, obtain, null, 1);
                obtain.recycle();
            }

            public void a(String str, String[] strArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                obtain.writeString(str);
                obtain.writeStringArray(strArr);
                this.kn.transact(1008, obtain, null, 1);
                obtain.recycle();
            }

            public void aO(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                obtain.writeString(str);
                this.kn.transact(ApiEventType.API_IMAI_PING, obtain, null, 1);
                obtain.recycle();
            }

            public void aP(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                obtain.writeString(str);
                this.kn.transact(ApiEventType.API_IMAI_PING_IN_WEB_VIEW, obtain, null, 1);
                obtain.recycle();
            }

            public void aQ(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                obtain.writeString(str);
                this.kn.transact(1005, obtain, null, 1);
                obtain.recycle();
            }

            public void aR(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                obtain.writeString(str);
                this.kn.transact(1006, obtain, null, 1);
                obtain.recycle();
            }

            public void aS(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                obtain.writeString(str);
                this.kn.transact(1007, obtain, null, 1);
                obtain.recycle();
            }

            public void aT(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                obtain.writeString(str);
                this.kn.transact(1018, obtain, null, 1);
                obtain.recycle();
            }

            public void aU(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                obtain.writeString(str);
                this.kn.transact(1019, obtain, null, 1);
                obtain.recycle();
            }

            public IBinder asBinder() {
                return this.kn;
            }

            public void b(String str, String[] strArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                obtain.writeString(str);
                obtain.writeStringArray(strArr);
                this.kn.transact(1009, obtain, null, 1);
                obtain.recycle();
            }

            public void bb(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                obtain.writeInt(i);
                this.kn.transact(1020, obtain, null, 1);
                obtain.recycle();
            }

            public void c(int i, int i2, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                obtain.writeInt(i);
                obtain.writeInt(i2);
                obtain.writeString(str);
                this.kn.transact(ApiEventType.API_IMAI_OPEN_EMBEDDED, obtain, null, 1);
                obtain.recycle();
            }

            public void c(String str, String[] strArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                obtain.writeString(str);
                obtain.writeStringArray(strArr);
                this.kn.transact(1010, obtain, null, 1);
                obtain.recycle();
            }

            public void d(String str, String[] strArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                obtain.writeString(str);
                obtain.writeStringArray(strArr);
                this.kn.transact(1011, obtain, null, 1);
                obtain.recycle();
            }

            public void e(String str, String[] strArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                obtain.writeString(str);
                obtain.writeStringArray(strArr);
                this.kn.transact(1012, obtain, null, 1);
                obtain.recycle();
            }

            public void f(String str, String[] strArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                obtain.writeString(str);
                obtain.writeStringArray(strArr);
                this.kn.transact(1013, obtain, null, 1);
                obtain.recycle();
            }

            public void g(String str, String[] strArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                obtain.writeString(str);
                obtain.writeStringArray(strArr);
                this.kn.transact(1017, obtain, null, 1);
                obtain.recycle();
            }

            public void gQ() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                this.kn.transact(1016, obtain, null, 1);
                obtain.recycle();
            }

            public void gR() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                this.kn.transact(1023, obtain, null, 1);
                obtain.recycle();
            }

            public void onP2PConnected(String participantId) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                obtain.writeString(participantId);
                this.kn.transact(1014, obtain, null, 1);
                obtain.recycle();
            }

            public void onP2PDisconnected(String participantId) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                obtain.writeString(participantId);
                this.kn.transact(1015, obtain, null, 1);
                obtain.recycle();
            }

            public void r(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                obtain.writeString(str);
                obtain.writeInt(i);
                this.kn.transact(1025, obtain, null, 1);
                obtain.recycle();
            }
        }

        public Stub() {
            attachInterface(this, "com.google.android.gms.games.internal.IRoomServiceCallbacks");
        }

        public static IRoomServiceCallbacks Q(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.games.internal.IRoomServiceCallbacks");
            return (queryLocalInterface == null || !queryLocalInterface instanceof IRoomServiceCallbacks) ? new Proxy(iBinder) : (IRoomServiceCallbacks) queryLocalInterface;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            ParcelFileDescriptor parcelFileDescriptor = null;
            switch (code) {
                case ApiEventType.API_IMAI_OPEN_EMBEDDED:
                    data.enforceInterface("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                    c(data.readInt(), data.readInt(), data.readString());
                    return true;
                case ApiEventType.API_IMAI_OPEN_EXTERNAL:
                    data.enforceInterface("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                    a(data.readString(), data.createByteArray(), data.readInt());
                    return true;
                case ApiEventType.API_IMAI_PING:
                    data.enforceInterface("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                    aO(data.readString());
                    return true;
                case ApiEventType.API_IMAI_PING_IN_WEB_VIEW:
                    data.enforceInterface("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                    aP(data.readString());
                    return true;
                case 1005:
                    data.enforceInterface("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                    aQ(data.readString());
                    return true;
                case 1006:
                    data.enforceInterface("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                    aR(data.readString());
                    return true;
                case 1007:
                    data.enforceInterface("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                    aS(data.readString());
                    return true;
                case 1008:
                    data.enforceInterface("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                    a(data.readString(), data.createStringArray());
                    return true;
                case 1009:
                    data.enforceInterface("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                    b(data.readString(), data.createStringArray());
                    return true;
                case 1010:
                    data.enforceInterface("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                    c(data.readString(), data.createStringArray());
                    return true;
                case 1011:
                    data.enforceInterface("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                    d(data.readString(), data.createStringArray());
                    return true;
                case 1012:
                    data.enforceInterface("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                    e(data.readString(), data.createStringArray());
                    return true;
                case 1013:
                    data.enforceInterface("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                    f(data.readString(), data.createStringArray());
                    return true;
                case 1014:
                    data.enforceInterface("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                    onP2PConnected(data.readString());
                    return true;
                case 1015:
                    data.enforceInterface("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                    onP2PDisconnected(data.readString());
                    return true;
                case 1016:
                    data.enforceInterface("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                    gQ();
                    return true;
                case 1017:
                    data.enforceInterface("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                    g(data.readString(), data.createStringArray());
                    return true;
                case 1018:
                    data.enforceInterface("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                    aT(data.readString());
                    return true;
                case 1019:
                    data.enforceInterface("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                    aU(data.readString());
                    return true;
                case 1020:
                    data.enforceInterface("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                    bb(data.readInt());
                    return true;
                case 1021:
                    data.enforceInterface("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                    P(data.readStrongBinder());
                    return true;
                case 1022:
                    ConnectionInfo ap;
                    data.enforceInterface("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                    if (data.readInt() != 0) {
                        ap = ConnectionInfo.CREATOR.ap(data);
                    }
                    a(ap);
                    return true;
                case 1023:
                    data.enforceInterface("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                    gR();
                    return true;
                case AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT:
                    data.enforceInterface("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                    if (data.readInt() != 0) {
                        parcelFileDescriptor = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(data);
                    }
                    a(parcelFileDescriptor, data.readInt());
                    return true;
                case 1025:
                    data.enforceInterface("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                    r(data.readString(), data.readInt());
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.games.internal.IRoomServiceCallbacks");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void P(IBinder iBinder) throws RemoteException;

    void a(ParcelFileDescriptor parcelFileDescriptor, int i) throws RemoteException;

    void a(ConnectionInfo connectionInfo) throws RemoteException;

    void a(String str, byte[] bArr, int i) throws RemoteException;

    void a(String str, String[] strArr) throws RemoteException;

    void aO(String str) throws RemoteException;

    void aP(String str) throws RemoteException;

    void aQ(String str) throws RemoteException;

    void aR(String str) throws RemoteException;

    void aS(String str) throws RemoteException;

    void aT(String str) throws RemoteException;

    void aU(String str) throws RemoteException;

    void b(String str, String[] strArr) throws RemoteException;

    void bb(int i) throws RemoteException;

    void c(int i, int i2, String str) throws RemoteException;

    void c(String str, String[] strArr) throws RemoteException;

    void d(String str, String[] strArr) throws RemoteException;

    void e(String str, String[] strArr) throws RemoteException;

    void f(String str, String[] strArr) throws RemoteException;

    void g(String str, String[] strArr) throws RemoteException;

    void gQ() throws RemoteException;

    void gR() throws RemoteException;

    void onP2PConnected(String str) throws RemoteException;

    void onP2PDisconnected(String str) throws RemoteException;

    void r(String str, int i) throws RemoteException;
}