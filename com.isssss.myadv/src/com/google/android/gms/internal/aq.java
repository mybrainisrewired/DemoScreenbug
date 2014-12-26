package com.google.android.gms.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.d;
import com.millennialmedia.android.MMAdView;

public interface aq extends IInterface {

    public static abstract class a extends Binder implements aq {

        private static class a implements aq {
            private IBinder kn;

            a(IBinder iBinder) {
                this.kn = iBinder;
            }

            public IBinder a(d dVar, ak akVar, String str, bq bqVar, int i) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.ads.internal.client.IAdManagerCreator");
                obtain.writeStrongBinder(dVar != null ? dVar.asBinder() : null);
                if (akVar != null) {
                    obtain.writeInt(1);
                    akVar.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                obtain.writeString(str);
                if (bqVar != null) {
                    iBinder = bqVar.asBinder();
                }
                obtain.writeStrongBinder(iBinder);
                obtain.writeInt(i);
                this.kn.transact(1, obtain, obtain2, 0);
                obtain2.readException();
                iBinder = obtain2.readStrongBinder();
                obtain2.recycle();
                obtain.recycle();
                return iBinder;
            }

            public IBinder asBinder() {
                return this.kn;
            }
        }

        public static aq g(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.ads.internal.client.IAdManagerCreator");
            return (queryLocalInterface == null || !queryLocalInterface instanceof aq) ? new a(iBinder) : (aq) queryLocalInterface;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case MMAdView.TRANSITION_FADE:
                    data.enforceInterface("com.google.android.gms.ads.internal.client.IAdManagerCreator");
                    IBinder a = a(com.google.android.gms.dynamic.d.a.K(data.readStrongBinder()), data.readInt() != 0 ? ak.CREATOR.b(data) : null, data.readString(), com.google.android.gms.internal.bq.a.i(data.readStrongBinder()), data.readInt());
                    reply.writeNoException();
                    reply.writeStrongBinder(a);
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.ads.internal.client.IAdManagerCreator");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    IBinder a(d dVar, ak akVar, String str, bq bqVar, int i) throws RemoteException;
}