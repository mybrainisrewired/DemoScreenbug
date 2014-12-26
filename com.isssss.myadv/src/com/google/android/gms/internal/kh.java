package com.google.android.gms.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.common.data.DataHolder;
import com.millennialmedia.android.MMAdView;

public interface kh extends IInterface {

    public static abstract class a extends Binder implements kh {
        public a() {
            attachInterface(this, "com.google.android.gms.wearable.internal.IWearableListener");
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            kk kkVar = null;
            switch (code) {
                case MMAdView.TRANSITION_FADE:
                    DataHolder createFromParcel;
                    data.enforceInterface("com.google.android.gms.wearable.internal.IWearableListener");
                    if (data.readInt() != 0) {
                        createFromParcel = DataHolder.CREATOR.createFromParcel(data);
                    }
                    M(createFromParcel);
                    return true;
                case MMAdView.TRANSITION_UP:
                    ki kiVar;
                    data.enforceInterface("com.google.android.gms.wearable.internal.IWearableListener");
                    if (data.readInt() != 0) {
                        kiVar = (ki) ki.CREATOR.createFromParcel(data);
                    }
                    a(kiVar);
                    return true;
                case MMAdView.TRANSITION_DOWN:
                    data.enforceInterface("com.google.android.gms.wearable.internal.IWearableListener");
                    if (data.readInt() != 0) {
                        kkVar = (kk) kk.CREATOR.createFromParcel(data);
                    }
                    a(kkVar);
                    return true;
                case MMAdView.TRANSITION_RANDOM:
                    data.enforceInterface("com.google.android.gms.wearable.internal.IWearableListener");
                    if (data.readInt() != 0) {
                        kkVar = (kk) kk.CREATOR.createFromParcel(data);
                    }
                    b(kkVar);
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.wearable.internal.IWearableListener");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void M(DataHolder dataHolder) throws RemoteException;

    void a(ki kiVar) throws RemoteException;

    void a(kk kkVar) throws RemoteException;

    void b(kk kkVar) throws RemoteException;
}