package com.google.android.gms.plus.internal;

import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.internal.fk;
import com.google.android.gms.internal.gg;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import java.util.List;

public interface d extends IInterface {

    public static abstract class a extends Binder implements d {

        private static class a implements d {
            private IBinder kn;

            a(IBinder iBinder) {
                this.kn = iBinder;
            }

            public fk a(b bVar, int i, int i2, int i3, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.plus.internal.IPlusService");
                obtain.writeStrongBinder(bVar != null ? bVar.asBinder() : null);
                obtain.writeInt(i);
                obtain.writeInt(i2);
                obtain.writeInt(i3);
                obtain.writeString(str);
                this.kn.transact(ApiEventType.API_MRAID_GET_ORIENTATION, obtain, obtain2, 0);
                obtain2.readException();
                fk A = com.google.android.gms.internal.fk.a.A(obtain2.readStrongBinder());
                obtain2.recycle();
                obtain.recycle();
                return A;
            }

            public void a(gg ggVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.plus.internal.IPlusService");
                if (ggVar != null) {
                    obtain.writeInt(1);
                    ggVar.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(MMAdView.TRANSITION_RANDOM, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(b bVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.plus.internal.IPlusService");
                obtain.writeStrongBinder(bVar != null ? bVar.asBinder() : null);
                this.kn.transact(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(b bVar, int i, String str, Uri uri, String str2, String str3) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.plus.internal.IPlusService");
                obtain.writeStrongBinder(bVar != null ? bVar.asBinder() : null);
                obtain.writeInt(i);
                obtain.writeString(str);
                if (uri != null) {
                    obtain.writeInt(1);
                    uri.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                obtain.writeString(str2);
                obtain.writeString(str3);
                this.kn.transact(ApiEventType.API_MRAID_IS_VIEWABLE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(b bVar, Uri uri, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.plus.internal.IPlusService");
                obtain.writeStrongBinder(bVar != null ? bVar.asBinder() : null);
                if (uri != null) {
                    obtain.writeInt(1);
                    uri.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                if (bundle != null) {
                    obtain.writeInt(1);
                    bundle.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(b bVar, gg ggVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.plus.internal.IPlusService");
                obtain.writeStrongBinder(bVar != null ? bVar.asBinder() : null);
                if (ggVar != null) {
                    obtain.writeInt(1);
                    ggVar.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(ApiEventType.API_MRAID_GET_VIDEO_VOLUME, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(b bVar, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.plus.internal.IPlusService");
                obtain.writeStrongBinder(bVar != null ? bVar.asBinder() : null);
                obtain.writeString(str);
                this.kn.transact(1, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(b bVar, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.plus.internal.IPlusService");
                obtain.writeStrongBinder(bVar != null ? bVar.asBinder() : null);
                obtain.writeString(str);
                obtain.writeString(str2);
                this.kn.transact(MMAdView.TRANSITION_UP, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(b bVar, List<String> list) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.plus.internal.IPlusService");
                obtain.writeStrongBinder(bVar != null ? bVar.asBinder() : null);
                obtain.writeStringList(list);
                this.kn.transact(ApiEventType.API_MRAID_UNMUTE_AUDIO, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public IBinder asBinder() {
                return this.kn;
            }

            public void b(b bVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.plus.internal.IPlusService");
                obtain.writeStrongBinder(bVar != null ? bVar.asBinder() : null);
                this.kn.transact(Encoder.LINE_GROUPS, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void b(b bVar, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.plus.internal.IPlusService");
                obtain.writeStrongBinder(bVar != null ? bVar.asBinder() : null);
                obtain.writeString(str);
                this.kn.transact(MMAdView.TRANSITION_DOWN, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void c(b bVar, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.plus.internal.IPlusService");
                obtain.writeStrongBinder(bVar != null ? bVar.asBinder() : null);
                obtain.writeString(str);
                this.kn.transact(ApiEventType.API_MRAID_GET_CURRENT_POSITION, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void clearDefaultAccount() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.plus.internal.IPlusService");
                this.kn.transact(ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void d(b bVar, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.plus.internal.IPlusService");
                obtain.writeStrongBinder(bVar != null ? bVar.asBinder() : null);
                obtain.writeString(str);
                this.kn.transact(ApiEventType.API_MRAID_PLAY_VIDEO, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void e(b bVar, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.plus.internal.IPlusService");
                obtain.writeStrongBinder(bVar != null ? bVar.asBinder() : null);
                obtain.writeString(str);
                this.kn.transact(ApiEventType.API_MRAID_SET_VIDEO_VOLUME, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public String getAccountName() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.plus.internal.IPlusService");
                this.kn.transact(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                String readString = obtain2.readString();
                obtain2.recycle();
                obtain.recycle();
                return readString;
            }

            public String iK() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.plus.internal.IPlusService");
                this.kn.transact(ApiEventType.API_MRAID_MUTE_VIDEO, obtain, obtain2, 0);
                obtain2.readException();
                String readString = obtain2.readString();
                obtain2.recycle();
                obtain.recycle();
                return readString;
            }

            public boolean iL() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.plus.internal.IPlusService");
                this.kn.transact(ApiEventType.API_MRAID_UNMUTE_VIDEO, obtain, obtain2, 0);
                obtain2.readException();
                if (obtain2.readInt() != 0) {
                    z = true;
                }
                obtain2.recycle();
                obtain.recycle();
                return z;
            }

            public String iM() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.plus.internal.IPlusService");
                this.kn.transact(ApiEventType.API_MRAID_IS_VIDEO_MUTED, obtain, obtain2, 0);
                obtain2.readException();
                String readString = obtain2.readString();
                obtain2.recycle();
                obtain.recycle();
                return readString;
            }

            public void removeMoment(String momentId) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.plus.internal.IPlusService");
                obtain.writeString(momentId);
                this.kn.transact(ApiEventType.API_MRAID_GET_SCREEN_SIZE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }
        }

        public static d aQ(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.plus.internal.IPlusService");
            return (queryLocalInterface == null || !queryLocalInterface instanceof d) ? new a(iBinder) : (d) queryLocalInterface;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            gg ggVar = null;
            String accountName;
            switch (code) {
                case MMAdView.TRANSITION_FADE:
                    data.enforceInterface("com.google.android.gms.plus.internal.IPlusService");
                    a(com.google.android.gms.plus.internal.b.a.aO(data.readStrongBinder()), data.readString());
                    reply.writeNoException();
                    return true;
                case MMAdView.TRANSITION_UP:
                    data.enforceInterface("com.google.android.gms.plus.internal.IPlusService");
                    a(com.google.android.gms.plus.internal.b.a.aO(data.readStrongBinder()), data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case MMAdView.TRANSITION_DOWN:
                    data.enforceInterface("com.google.android.gms.plus.internal.IPlusService");
                    b(com.google.android.gms.plus.internal.b.a.aO(data.readStrongBinder()), data.readString());
                    reply.writeNoException();
                    return true;
                case MMAdView.TRANSITION_RANDOM:
                    data.enforceInterface("com.google.android.gms.plus.internal.IPlusService");
                    a(data.readInt() != 0 ? gg.CREATOR.x(data) : null);
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.plus.internal.IPlusService");
                    accountName = getAccountName();
                    reply.writeNoException();
                    reply.writeString(accountName);
                    return true;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.plus.internal.IPlusService");
                    clearDefaultAccount();
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.plus.internal.IPlusService");
                    a(com.google.android.gms.plus.internal.b.a.aO(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.plus.internal.IPlusService");
                    a(com.google.android.gms.plus.internal.b.a.aO(data.readStrongBinder()), data.readInt() != 0 ? (Uri) Uri.CREATOR.createFromParcel(data) : null, data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_IS_VIEWABLE:
                    data.enforceInterface("com.google.android.gms.plus.internal.IPlusService");
                    a(com.google.android.gms.plus.internal.b.a.aO(data.readStrongBinder()), data.readInt(), data.readString(), data.readInt() != 0 ? (Uri) Uri.CREATOR.createFromParcel(data) : null, data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_GET_ORIENTATION:
                    IBinder asBinder;
                    data.enforceInterface("com.google.android.gms.plus.internal.IPlusService");
                    fk a = a(com.google.android.gms.plus.internal.b.a.aO(data.readStrongBinder()), data.readInt(), data.readInt(), data.readInt(), data.readString());
                    reply.writeNoException();
                    if (a != null) {
                        asBinder = a.asBinder();
                    }
                    reply.writeStrongBinder(asBinder);
                    return true;
                case ApiEventType.API_MRAID_GET_SCREEN_SIZE:
                    data.enforceInterface("com.google.android.gms.plus.internal.IPlusService");
                    removeMoment(data.readString());
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_GET_CURRENT_POSITION:
                    data.enforceInterface("com.google.android.gms.plus.internal.IPlusService");
                    c(com.google.android.gms.plus.internal.b.a.aO(data.readStrongBinder()), data.readString());
                    reply.writeNoException();
                    return true;
                case Encoder.LINE_GROUPS:
                    data.enforceInterface("com.google.android.gms.plus.internal.IPlusService");
                    b(com.google.android.gms.plus.internal.b.a.aO(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_UNMUTE_AUDIO:
                    data.enforceInterface("com.google.android.gms.plus.internal.IPlusService");
                    a(com.google.android.gms.plus.internal.b.a.aO(data.readStrongBinder()), data.createStringArrayList());
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_PLAY_VIDEO:
                    data.enforceInterface("com.google.android.gms.plus.internal.IPlusService");
                    d(com.google.android.gms.plus.internal.b.a.aO(data.readStrongBinder()), data.readString());
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_MUTE_VIDEO:
                    data.enforceInterface("com.google.android.gms.plus.internal.IPlusService");
                    accountName = iK();
                    reply.writeNoException();
                    reply.writeString(accountName);
                    return true;
                case ApiEventType.API_MRAID_UNMUTE_VIDEO:
                    data.enforceInterface("com.google.android.gms.plus.internal.IPlusService");
                    boolean iL = iL();
                    reply.writeNoException();
                    reply.writeInt(iL ? 1 : 0);
                    return true;
                case ApiEventType.API_MRAID_IS_VIDEO_MUTED:
                    data.enforceInterface("com.google.android.gms.plus.internal.IPlusService");
                    accountName = iM();
                    reply.writeNoException();
                    reply.writeString(accountName);
                    return true;
                case ApiEventType.API_MRAID_SET_VIDEO_VOLUME:
                    data.enforceInterface("com.google.android.gms.plus.internal.IPlusService");
                    e(com.google.android.gms.plus.internal.b.a.aO(data.readStrongBinder()), data.readString());
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_GET_VIDEO_VOLUME:
                    data.enforceInterface("com.google.android.gms.plus.internal.IPlusService");
                    b aO = com.google.android.gms.plus.internal.b.a.aO(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        ggVar = gg.CREATOR.x(data);
                    }
                    a(aO, ggVar);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.plus.internal.IPlusService");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    fk a(b bVar, int i, int i2, int i3, String str) throws RemoteException;

    void a(gg ggVar) throws RemoteException;

    void a(b bVar) throws RemoteException;

    void a(b bVar, int i, String str, Uri uri, String str2, String str3) throws RemoteException;

    void a(b bVar, Uri uri, Bundle bundle) throws RemoteException;

    void a(b bVar, gg ggVar) throws RemoteException;

    void a(b bVar, String str) throws RemoteException;

    void a(b bVar, String str, String str2) throws RemoteException;

    void a(b bVar, List<String> list) throws RemoteException;

    void b(b bVar) throws RemoteException;

    void b(b bVar, String str) throws RemoteException;

    void c(b bVar, String str) throws RemoteException;

    void clearDefaultAccount() throws RemoteException;

    void d(b bVar, String str) throws RemoteException;

    void e(b bVar, String str) throws RemoteException;

    String getAccountName() throws RemoteException;

    String iK() throws RemoteException;

    boolean iL() throws RemoteException;

    String iM() throws RemoteException;

    void removeMoment(String str) throws RemoteException;
}