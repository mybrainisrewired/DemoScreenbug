package com.google.android.gms.games.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.common.data.DataHolder;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;

public interface IRoomService extends IInterface {

    public static abstract class Stub extends Binder implements IRoomService {

        private static class Proxy implements IRoomService {
            private IBinder kn;

            public void B(boolean z) throws RemoteException {
                int i = 1;
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IRoomService");
                if (!z) {
                    i = 0;
                }
                obtain.writeInt(i);
                this.kn.transact(1008, obtain, null, 1);
                obtain.recycle();
            }

            public void a(IBinder iBinder, IRoomServiceCallbacks iRoomServiceCallbacks) throws RemoteException {
                IBinder iBinder2 = null;
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IRoomService");
                obtain.writeStrongBinder(iBinder);
                if (iRoomServiceCallbacks != null) {
                    iBinder2 = iRoomServiceCallbacks.asBinder();
                }
                obtain.writeStrongBinder(iBinder2);
                this.kn.transact(ApiEventType.API_IMAI_OPEN_EMBEDDED, obtain, null, 1);
                obtain.recycle();
            }

            public void a(DataHolder dataHolder, boolean z) throws RemoteException {
                int i = 1;
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IRoomService");
                if (dataHolder != null) {
                    obtain.writeInt(1);
                    dataHolder.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                if (!z) {
                    i = 0;
                }
                obtain.writeInt(i);
                this.kn.transact(1006, obtain, null, 1);
                obtain.recycle();
            }

            public void a(String str, String str2, String str3) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IRoomService");
                obtain.writeString(str);
                obtain.writeString(str2);
                obtain.writeString(str3);
                this.kn.transact(ApiEventType.API_IMAI_PING_IN_WEB_VIEW, obtain, null, 1);
                obtain.recycle();
            }

            public void a(byte[] bArr, String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IRoomService");
                obtain.writeByteArray(bArr);
                obtain.writeString(str);
                obtain.writeInt(i);
                this.kn.transact(1009, obtain, null, 1);
                obtain.recycle();
            }

            public void a(byte[] bArr, String[] strArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IRoomService");
                obtain.writeByteArray(bArr);
                obtain.writeStringArray(strArr);
                this.kn.transact(1010, obtain, null, 1);
                obtain.recycle();
            }

            public void aM(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IRoomService");
                obtain.writeString(str);
                this.kn.transact(1013, obtain, null, 1);
                obtain.recycle();
            }

            public void aN(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IRoomService");
                obtain.writeString(str);
                this.kn.transact(1014, obtain, null, 1);
                obtain.recycle();
            }

            public IBinder asBinder() {
                return this.kn;
            }

            public void gM() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IRoomService");
                this.kn.transact(ApiEventType.API_IMAI_OPEN_EXTERNAL, obtain, null, 1);
                obtain.recycle();
            }

            public void gN() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IRoomService");
                this.kn.transact(ApiEventType.API_IMAI_PING, obtain, null, 1);
                obtain.recycle();
            }

            public void gO() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IRoomService");
                this.kn.transact(1005, obtain, null, 1);
                obtain.recycle();
            }

            public void gP() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IRoomService");
                this.kn.transact(1007, obtain, null, 1);
                obtain.recycle();
            }

            public void p(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IRoomService");
                obtain.writeString(str);
                obtain.writeInt(i);
                this.kn.transact(1011, obtain, null, 1);
                obtain.recycle();
            }

            public void q(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IRoomService");
                obtain.writeString(str);
                obtain.writeInt(i);
                this.kn.transact(1012, obtain, null, 1);
                obtain.recycle();
            }
        }

        public Stub() {
            attachInterface(this, "com.google.android.gms.games.internal.IRoomService");
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean z = false;
            switch (code) {
                case ApiEventType.API_IMAI_OPEN_EMBEDDED:
                    data.enforceInterface("com.google.android.gms.games.internal.IRoomService");
                    a(data.readStrongBinder(), com.google.android.gms.games.internal.IRoomServiceCallbacks.Stub.Q(data.readStrongBinder()));
                    return true;
                case ApiEventType.API_IMAI_OPEN_EXTERNAL:
                    data.enforceInterface("com.google.android.gms.games.internal.IRoomService");
                    gM();
                    return true;
                case ApiEventType.API_IMAI_PING:
                    data.enforceInterface("com.google.android.gms.games.internal.IRoomService");
                    gN();
                    return true;
                case ApiEventType.API_IMAI_PING_IN_WEB_VIEW:
                    data.enforceInterface("com.google.android.gms.games.internal.IRoomService");
                    a(data.readString(), data.readString(), data.readString());
                    return true;
                case 1005:
                    data.enforceInterface("com.google.android.gms.games.internal.IRoomService");
                    gO();
                    return true;
                case 1006:
                    data.enforceInterface("com.google.android.gms.games.internal.IRoomService");
                    DataHolder createFromParcel = data.readInt() != 0 ? DataHolder.CREATOR.createFromParcel(data) : null;
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    a(createFromParcel, z);
                    return true;
                case 1007:
                    data.enforceInterface("com.google.android.gms.games.internal.IRoomService");
                    gP();
                    return true;
                case 1008:
                    data.enforceInterface("com.google.android.gms.games.internal.IRoomService");
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    B(z);
                    return true;
                case 1009:
                    data.enforceInterface("com.google.android.gms.games.internal.IRoomService");
                    a(data.createByteArray(), data.readString(), data.readInt());
                    return true;
                case 1010:
                    data.enforceInterface("com.google.android.gms.games.internal.IRoomService");
                    a(data.createByteArray(), data.createStringArray());
                    return true;
                case 1011:
                    data.enforceInterface("com.google.android.gms.games.internal.IRoomService");
                    p(data.readString(), data.readInt());
                    return true;
                case 1012:
                    data.enforceInterface("com.google.android.gms.games.internal.IRoomService");
                    q(data.readString(), data.readInt());
                    return true;
                case 1013:
                    data.enforceInterface("com.google.android.gms.games.internal.IRoomService");
                    aM(data.readString());
                    return true;
                case 1014:
                    data.enforceInterface("com.google.android.gms.games.internal.IRoomService");
                    aN(data.readString());
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.games.internal.IRoomService");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void B(boolean z) throws RemoteException;

    void a(IBinder iBinder, IRoomServiceCallbacks iRoomServiceCallbacks) throws RemoteException;

    void a(DataHolder dataHolder, boolean z) throws RemoteException;

    void a(String str, String str2, String str3) throws RemoteException;

    void a(byte[] bArr, String str, int i) throws RemoteException;

    void a(byte[] bArr, String[] strArr) throws RemoteException;

    void aM(String str) throws RemoteException;

    void aN(String str) throws RemoteException;

    void gM() throws RemoteException;

    void gN() throws RemoteException;

    void gO() throws RemoteException;

    void gP() throws RemoteException;

    void p(String str, int i) throws RemoteException;

    void q(String str, int i) throws RemoteException;
}