package com.google.android.gms.internal;

import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.millennialmedia.android.MMAdView;

public interface hv extends IInterface {

    public static abstract class a extends Binder implements hv {

        private static class a implements hv {
            private IBinder kn;

            a(IBinder iBinder) {
                this.kn = iBinder;
            }

            public void a(hu huVar, Uri uri, Bundle bundle, boolean z) throws RemoteException {
                IBinder iBinder = null;
                int i = 1;
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.panorama.internal.IPanoramaService");
                if (huVar != null) {
                    iBinder = huVar.asBinder();
                }
                obtain.writeStrongBinder(iBinder);
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
                if (!z) {
                    i = 0;
                }
                obtain.writeInt(i);
                this.kn.transact(1, obtain, null, 1);
                obtain.recycle();
            }

            public IBinder asBinder() {
                return this.kn;
            }
        }

        public static hv aM(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.panorama.internal.IPanoramaService");
            return (queryLocalInterface == null || !queryLocalInterface instanceof hv) ? new a(iBinder) : (hv) queryLocalInterface;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case MMAdView.TRANSITION_FADE:
                    data.enforceInterface("com.google.android.gms.panorama.internal.IPanoramaService");
                    a(com.google.android.gms.internal.hu.a.aL(data.readStrongBinder()), data.readInt() != 0 ? (Uri) Uri.CREATOR.createFromParcel(data) : null, data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null, data.readInt() != 0);
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.panorama.internal.IPanoramaService");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void a(hu huVar, Uri uri, Bundle bundle, boolean z) throws RemoteException;
}