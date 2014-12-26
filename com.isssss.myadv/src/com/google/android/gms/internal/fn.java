package com.google.android.gms.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.d;
import com.millennialmedia.android.MMAdView;

public interface fn extends IInterface {

    public static abstract class a extends Binder implements fn {

        private static class a implements fn {
            private IBinder kn;

            a(IBinder iBinder) {
                this.kn = iBinder;
            }

            public d a(d dVar, int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.common.internal.ISignInButtonCreator");
                obtain.writeStrongBinder(dVar != null ? dVar.asBinder() : null);
                obtain.writeInt(i);
                obtain.writeInt(i2);
                this.kn.transact(1, obtain, obtain2, 0);
                obtain2.readException();
                d K = com.google.android.gms.dynamic.d.a.K(obtain2.readStrongBinder());
                obtain2.recycle();
                obtain.recycle();
                return K;
            }

            public IBinder asBinder() {
                return this.kn;
            }
        }

        public static fn D(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.common.internal.ISignInButtonCreator");
            return (queryLocalInterface == null || !queryLocalInterface instanceof fn) ? new a(iBinder) : (fn) queryLocalInterface;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case MMAdView.TRANSITION_FADE:
                    data.enforceInterface("com.google.android.gms.common.internal.ISignInButtonCreator");
                    d a = a(com.google.android.gms.dynamic.d.a.K(data.readStrongBinder()), data.readInt(), data.readInt());
                    reply.writeNoException();
                    reply.writeStrongBinder(a != null ? a.asBinder() : null);
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.common.internal.ISignInButtonCreator");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    d a(d dVar, int i, int i2) throws RemoteException;
}