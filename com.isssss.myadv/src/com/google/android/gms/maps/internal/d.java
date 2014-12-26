package com.google.android.gms.maps.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.maps.model.internal.f;
import com.millennialmedia.android.MMAdView;

public interface d extends IInterface {

    public static abstract class a extends Binder implements d {

        private static class a implements d {
            private IBinder kn;

            a(IBinder iBinder) {
                this.kn = iBinder;
            }

            public IBinder asBinder() {
                return this.kn;
            }

            public com.google.android.gms.dynamic.d f(f fVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IInfoWindowAdapter");
                obtain.writeStrongBinder(fVar != null ? fVar.asBinder() : null);
                this.kn.transact(1, obtain, obtain2, 0);
                obtain2.readException();
                com.google.android.gms.dynamic.d K = com.google.android.gms.dynamic.d.a.K(obtain2.readStrongBinder());
                obtain2.recycle();
                obtain.recycle();
                return K;
            }

            public com.google.android.gms.dynamic.d g(f fVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IInfoWindowAdapter");
                obtain.writeStrongBinder(fVar != null ? fVar.asBinder() : null);
                this.kn.transact(MMAdView.TRANSITION_UP, obtain, obtain2, 0);
                obtain2.readException();
                com.google.android.gms.dynamic.d K = com.google.android.gms.dynamic.d.a.K(obtain2.readStrongBinder());
                obtain2.recycle();
                obtain.recycle();
                return K;
            }
        }

        public a() {
            attachInterface(this, "com.google.android.gms.maps.internal.IInfoWindowAdapter");
        }

        public static d ad(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.maps.internal.IInfoWindowAdapter");
            return (queryLocalInterface == null || !queryLocalInterface instanceof d) ? new a(iBinder) : (d) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            IBinder iBinder = null;
            com.google.android.gms.dynamic.d f;
            switch (code) {
                case MMAdView.TRANSITION_FADE:
                    data.enforceInterface("com.google.android.gms.maps.internal.IInfoWindowAdapter");
                    f = f(com.google.android.gms.maps.model.internal.f.a.aG(data.readStrongBinder()));
                    reply.writeNoException();
                    if (f != null) {
                        iBinder = f.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case MMAdView.TRANSITION_UP:
                    data.enforceInterface("com.google.android.gms.maps.internal.IInfoWindowAdapter");
                    f = g(com.google.android.gms.maps.model.internal.f.a.aG(data.readStrongBinder()));
                    reply.writeNoException();
                    if (f != null) {
                        iBinder = f.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.maps.internal.IInfoWindowAdapter");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    com.google.android.gms.dynamic.d f(f fVar) throws RemoteException;

    com.google.android.gms.dynamic.d g(f fVar) throws RemoteException;
}