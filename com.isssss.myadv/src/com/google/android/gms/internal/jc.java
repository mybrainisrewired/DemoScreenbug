package com.google.android.gms.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.c;
import com.google.android.gms.dynamic.d;
import com.google.android.gms.wallet.fragment.WalletFragmentOptions;
import com.millennialmedia.android.MMAdView;

public interface jc extends IInterface {

    public static abstract class a extends Binder implements jc {

        private static class a implements jc {
            private IBinder kn;

            a(IBinder iBinder) {
                this.kn = iBinder;
            }

            public iz a(d dVar, c cVar, WalletFragmentOptions walletFragmentOptions, ja jaVar) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.wallet.internal.IWalletDynamiteCreator");
                obtain.writeStrongBinder(dVar != null ? dVar.asBinder() : null);
                obtain.writeStrongBinder(cVar != null ? cVar.asBinder() : null);
                if (walletFragmentOptions != null) {
                    obtain.writeInt(1);
                    walletFragmentOptions.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                if (jaVar != null) {
                    iBinder = jaVar.asBinder();
                }
                obtain.writeStrongBinder(iBinder);
                this.kn.transact(1, obtain, obtain2, 0);
                obtain2.readException();
                iz aS = com.google.android.gms.internal.iz.a.aS(obtain2.readStrongBinder());
                obtain2.recycle();
                obtain.recycle();
                return aS;
            }

            public IBinder asBinder() {
                return this.kn;
            }
        }

        public static jc aV(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.wallet.internal.IWalletDynamiteCreator");
            return (queryLocalInterface == null || !queryLocalInterface instanceof jc) ? new a(iBinder) : (jc) queryLocalInterface;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            IBinder iBinder = null;
            switch (code) {
                case MMAdView.TRANSITION_FADE:
                    data.enforceInterface("com.google.android.gms.wallet.internal.IWalletDynamiteCreator");
                    iz a = a(com.google.android.gms.dynamic.d.a.K(data.readStrongBinder()), com.google.android.gms.dynamic.c.a.J(data.readStrongBinder()), data.readInt() != 0 ? (WalletFragmentOptions) WalletFragmentOptions.CREATOR.createFromParcel(data) : null, com.google.android.gms.internal.ja.a.aT(data.readStrongBinder()));
                    reply.writeNoException();
                    if (a != null) {
                        iBinder = a.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.wallet.internal.IWalletDynamiteCreator");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    iz a(d dVar, c cVar, WalletFragmentOptions walletFragmentOptions, ja jaVar) throws RemoteException;
}