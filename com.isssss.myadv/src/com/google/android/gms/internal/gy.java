package com.google.android.gms.internal;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.identity.intents.UserAddressRequest;
import com.millennialmedia.android.MMAdView;

public interface gy extends IInterface {

    public static abstract class a extends Binder implements gy {

        private static class a implements gy {
            private IBinder kn;

            a(IBinder iBinder) {
                this.kn = iBinder;
            }

            public void a(gx gxVar, UserAddressRequest userAddressRequest, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.identity.intents.internal.IAddressService");
                obtain.writeStrongBinder(gxVar != null ? gxVar.asBinder() : null);
                if (userAddressRequest != null) {
                    obtain.writeInt(1);
                    userAddressRequest.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                if (bundle != null) {
                    obtain.writeInt(1);
                    bundle.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(MMAdView.TRANSITION_UP, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public IBinder asBinder() {
                return this.kn;
            }
        }

        public static gy T(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.identity.intents.internal.IAddressService");
            return (queryLocalInterface == null || !queryLocalInterface instanceof gy) ? new a(iBinder) : (gy) queryLocalInterface;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case MMAdView.TRANSITION_UP:
                    data.enforceInterface("com.google.android.gms.identity.intents.internal.IAddressService");
                    a(com.google.android.gms.internal.gx.a.S(data.readStrongBinder()), data.readInt() != 0 ? (UserAddressRequest) UserAddressRequest.CREATOR.createFromParcel(data) : null, data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.identity.intents.internal.IAddressService");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void a(gx gxVar, UserAddressRequest userAddressRequest, Bundle bundle) throws RemoteException;
}