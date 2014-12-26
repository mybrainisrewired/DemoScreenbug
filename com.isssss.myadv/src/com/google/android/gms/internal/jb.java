package com.google.android.gms.internal;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.wallet.FullWalletRequest;
import com.google.android.gms.wallet.MaskedWalletRequest;
import com.google.android.gms.wallet.NotifyTransactionStatusRequest;
import com.google.android.gms.wallet.d;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public interface jb extends IInterface {

    public static abstract class a extends Binder implements jb {

        private static class a implements jb {
            private IBinder kn;

            a(IBinder iBinder) {
                this.kn = iBinder;
            }

            public void a(Bundle bundle, je jeVar) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.wallet.internal.IOwService");
                if (bundle != null) {
                    obtain.writeInt(1);
                    bundle.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                if (jeVar != null) {
                    iBinder = jeVar.asBinder();
                }
                obtain.writeStrongBinder(iBinder);
                this.kn.transact(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, obtain, null, 1);
                obtain.recycle();
            }

            public void a(iv ivVar, Bundle bundle, je jeVar) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.wallet.internal.IOwService");
                if (ivVar != null) {
                    obtain.writeInt(1);
                    ivVar.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                if (bundle != null) {
                    obtain.writeInt(1);
                    bundle.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                if (jeVar != null) {
                    iBinder = jeVar.asBinder();
                }
                obtain.writeStrongBinder(iBinder);
                this.kn.transact(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, obtain, null, 1);
                obtain.recycle();
            }

            public void a(FullWalletRequest fullWalletRequest, Bundle bundle, je jeVar) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.wallet.internal.IOwService");
                if (fullWalletRequest != null) {
                    obtain.writeInt(1);
                    fullWalletRequest.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                if (bundle != null) {
                    obtain.writeInt(1);
                    bundle.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                if (jeVar != null) {
                    iBinder = jeVar.asBinder();
                }
                obtain.writeStrongBinder(iBinder);
                this.kn.transact(MMAdView.TRANSITION_UP, obtain, null, 1);
                obtain.recycle();
            }

            public void a(MaskedWalletRequest maskedWalletRequest, Bundle bundle, jd jdVar) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.wallet.internal.IOwService");
                if (maskedWalletRequest != null) {
                    obtain.writeInt(1);
                    maskedWalletRequest.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                if (bundle != null) {
                    obtain.writeInt(1);
                    bundle.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                if (jdVar != null) {
                    iBinder = jdVar.asBinder();
                }
                obtain.writeStrongBinder(iBinder);
                this.kn.transact(ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, obtain, null, 1);
                obtain.recycle();
            }

            public void a(MaskedWalletRequest maskedWalletRequest, Bundle bundle, je jeVar) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.wallet.internal.IOwService");
                if (maskedWalletRequest != null) {
                    obtain.writeInt(1);
                    maskedWalletRequest.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                if (bundle != null) {
                    obtain.writeInt(1);
                    bundle.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                if (jeVar != null) {
                    iBinder = jeVar.asBinder();
                }
                obtain.writeStrongBinder(iBinder);
                this.kn.transact(1, obtain, null, 1);
                obtain.recycle();
            }

            public void a(NotifyTransactionStatusRequest notifyTransactionStatusRequest, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.wallet.internal.IOwService");
                if (notifyTransactionStatusRequest != null) {
                    obtain.writeInt(1);
                    notifyTransactionStatusRequest.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                if (bundle != null) {
                    obtain.writeInt(1);
                    bundle.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(MMAdView.TRANSITION_RANDOM, obtain, null, 1);
                obtain.recycle();
            }

            public void a(d dVar, Bundle bundle, je jeVar) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.wallet.internal.IOwService");
                if (dVar != null) {
                    obtain.writeInt(1);
                    dVar.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                if (bundle != null) {
                    obtain.writeInt(1);
                    bundle.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                if (jeVar != null) {
                    iBinder = jeVar.asBinder();
                }
                obtain.writeStrongBinder(iBinder);
                this.kn.transact(ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, obtain, null, 1);
                obtain.recycle();
            }

            public void a(String str, String str2, Bundle bundle, je jeVar) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.wallet.internal.IOwService");
                obtain.writeString(str);
                obtain.writeString(str2);
                if (bundle != null) {
                    obtain.writeInt(1);
                    bundle.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                if (jeVar != null) {
                    iBinder = jeVar.asBinder();
                }
                obtain.writeStrongBinder(iBinder);
                this.kn.transact(MMAdView.TRANSITION_DOWN, obtain, null, 1);
                obtain.recycle();
            }

            public IBinder asBinder() {
                return this.kn;
            }
        }

        public static jb aU(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.wallet.internal.IOwService");
            return (queryLocalInterface == null || !queryLocalInterface instanceof jb) ? new a(iBinder) : (jb) queryLocalInterface;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case MMAdView.TRANSITION_FADE:
                    data.enforceInterface("com.google.android.gms.wallet.internal.IOwService");
                    a(data.readInt() != 0 ? (MaskedWalletRequest) MaskedWalletRequest.CREATOR.createFromParcel(data) : null, data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null, com.google.android.gms.internal.je.a.aX(data.readStrongBinder()));
                    return true;
                case MMAdView.TRANSITION_UP:
                    data.enforceInterface("com.google.android.gms.wallet.internal.IOwService");
                    a(data.readInt() != 0 ? (FullWalletRequest) FullWalletRequest.CREATOR.createFromParcel(data) : null, data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null, com.google.android.gms.internal.je.a.aX(data.readStrongBinder()));
                    return true;
                case MMAdView.TRANSITION_DOWN:
                    data.enforceInterface("com.google.android.gms.wallet.internal.IOwService");
                    a(data.readString(), data.readString(), data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null, com.google.android.gms.internal.je.a.aX(data.readStrongBinder()));
                    return true;
                case MMAdView.TRANSITION_RANDOM:
                    data.enforceInterface("com.google.android.gms.wallet.internal.IOwService");
                    a(data.readInt() != 0 ? (NotifyTransactionStatusRequest) NotifyTransactionStatusRequest.CREATOR.createFromParcel(data) : null, data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null);
                    return true;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.wallet.internal.IOwService");
                    a(data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null, com.google.android.gms.internal.je.a.aX(data.readStrongBinder()));
                    return true;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.wallet.internal.IOwService");
                    a(data.readInt() != 0 ? (d) d.CREATOR.createFromParcel(data) : null, data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null, com.google.android.gms.internal.je.a.aX(data.readStrongBinder()));
                    return true;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.wallet.internal.IOwService");
                    a(data.readInt() != 0 ? (MaskedWalletRequest) MaskedWalletRequest.CREATOR.createFromParcel(data) : null, data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null, com.google.android.gms.internal.jd.a.aW(data.readStrongBinder()));
                    return true;
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.wallet.internal.IOwService");
                    a(data.readInt() != 0 ? (iv) iv.CREATOR.createFromParcel(data) : null, data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null, com.google.android.gms.internal.je.a.aX(data.readStrongBinder()));
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.wallet.internal.IOwService");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void a(Bundle bundle, je jeVar) throws RemoteException;

    void a(iv ivVar, Bundle bundle, je jeVar) throws RemoteException;

    void a(FullWalletRequest fullWalletRequest, Bundle bundle, je jeVar) throws RemoteException;

    void a(MaskedWalletRequest maskedWalletRequest, Bundle bundle, jd jdVar) throws RemoteException;

    void a(MaskedWalletRequest maskedWalletRequest, Bundle bundle, je jeVar) throws RemoteException;

    void a(NotifyTransactionStatusRequest notifyTransactionStatusRequest, Bundle bundle) throws RemoteException;

    void a(d dVar, Bundle bundle, je jeVar) throws RemoteException;

    void a(String str, String str2, Bundle bundle, je jeVar) throws RemoteException;
}