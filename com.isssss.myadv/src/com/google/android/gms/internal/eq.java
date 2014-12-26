package com.google.android.gms.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.cast.ApplicationMetadata;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public interface eq extends IInterface {

    public static abstract class a extends Binder implements eq {
        public a() {
            attachInterface(this, "com.google.android.gms.cast.internal.ICastDeviceControllerListener");
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean z = false;
            switch (code) {
                case MMAdView.TRANSITION_FADE:
                    data.enforceInterface("com.google.android.gms.cast.internal.ICastDeviceControllerListener");
                    z(data.readInt());
                    return true;
                case MMAdView.TRANSITION_UP:
                    data.enforceInterface("com.google.android.gms.cast.internal.ICastDeviceControllerListener");
                    ApplicationMetadata applicationMetadata = data.readInt() != 0 ? (ApplicationMetadata) ApplicationMetadata.CREATOR.createFromParcel(data) : null;
                    String readString = data.readString();
                    String readString2 = data.readString();
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    a(applicationMetadata, readString, readString2, z);
                    return true;
                case MMAdView.TRANSITION_DOWN:
                    data.enforceInterface("com.google.android.gms.cast.internal.ICastDeviceControllerListener");
                    A(data.readInt());
                    return true;
                case MMAdView.TRANSITION_RANDOM:
                    data.enforceInterface("com.google.android.gms.cast.internal.ICastDeviceControllerListener");
                    String readString3 = data.readString();
                    double readDouble = data.readDouble();
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    b(readString3, readDouble, z);
                    return true;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.cast.internal.ICastDeviceControllerListener");
                    d(data.readString(), data.readString());
                    return true;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.cast.internal.ICastDeviceControllerListener");
                    b(data.readString(), data.createByteArray());
                    return true;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.cast.internal.ICastDeviceControllerListener");
                    C(data.readInt());
                    return true;
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.cast.internal.ICastDeviceControllerListener");
                    B(data.readInt());
                    return true;
                case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.cast.internal.ICastDeviceControllerListener");
                    onApplicationDisconnected(data.readInt());
                    return true;
                case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                    data.enforceInterface("com.google.android.gms.cast.internal.ICastDeviceControllerListener");
                    a(data.readString(), data.readLong(), data.readInt());
                    return true;
                case ApiEventType.API_MRAID_EXPAND:
                    data.enforceInterface("com.google.android.gms.cast.internal.ICastDeviceControllerListener");
                    a(data.readString(), data.readLong());
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.cast.internal.ICastDeviceControllerListener");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void A(int i) throws RemoteException;

    void B(int i) throws RemoteException;

    void C(int i) throws RemoteException;

    void a(ApplicationMetadata applicationMetadata, String str, String str2, boolean z) throws RemoteException;

    void a(String str, long j) throws RemoteException;

    void a(String str, long j, int i) throws RemoteException;

    void b(String str, double d, boolean z) throws RemoteException;

    void b(String str, byte[] bArr) throws RemoteException;

    void d(String str, String str2) throws RemoteException;

    void onApplicationDisconnected(int i) throws RemoteException;

    void z(int i) throws RemoteException;
}