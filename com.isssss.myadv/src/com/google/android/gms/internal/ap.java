package com.google.android.gms.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.d;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public interface ap extends IInterface {

    public static abstract class a extends Binder implements ap {

        private static class a implements ap {
            private IBinder kn;

            a(IBinder iBinder) {
                this.kn = iBinder;
            }

            public d Q() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.ads.internal.client.IAdManager");
                this.kn.transact(1, obtain, obtain2, 0);
                obtain2.readException();
                d K = com.google.android.gms.dynamic.d.a.K(obtain2.readStrongBinder());
                obtain2.recycle();
                obtain.recycle();
                return K;
            }

            public ak R() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.ads.internal.client.IAdManager");
                this.kn.transact(ApiEventType.API_MRAID_RESIZE, obtain, obtain2, 0);
                obtain2.readException();
                ak b = obtain2.readInt() != 0 ? ak.CREATOR.b(obtain2) : null;
                obtain2.recycle();
                obtain.recycle();
                return b;
            }

            public void a(ak akVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.ads.internal.client.IAdManager");
                if (akVar != null) {
                    obtain.writeInt(1);
                    akVar.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(ApiEventType.API_MRAID_CLOSE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(ao aoVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.ads.internal.client.IAdManager");
                obtain.writeStrongBinder(aoVar != null ? aoVar.asBinder() : null);
                this.kn.transact(ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(ar arVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.ads.internal.client.IAdManager");
                obtain.writeStrongBinder(arVar != null ? arVar.asBinder() : null);
                this.kn.transact(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(co coVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.ads.internal.client.IAdManager");
                obtain.writeStrongBinder(coVar != null ? coVar.asBinder() : null);
                this.kn.transact(ApiEventType.API_MRAID_IS_VIEWABLE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public boolean a(ah ahVar) throws RemoteException {
                boolean z = true;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.ads.internal.client.IAdManager");
                if (ahVar != null) {
                    obtain.writeInt(1);
                    ahVar.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(MMAdView.TRANSITION_RANDOM, obtain, obtain2, 0);
                obtain2.readException();
                if (obtain2.readInt() == 0) {
                    z = false;
                }
                obtain2.recycle();
                obtain.recycle();
                return z;
            }

            public void ac() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.ads.internal.client.IAdManager");
                this.kn.transact(ApiEventType.API_MRAID_EXPAND, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public IBinder asBinder() {
                return this.kn;
            }

            public void destroy() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.ads.internal.client.IAdManager");
                this.kn.transact(MMAdView.TRANSITION_UP, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public boolean isReady() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.ads.internal.client.IAdManager");
                this.kn.transact(MMAdView.TRANSITION_DOWN, obtain, obtain2, 0);
                obtain2.readException();
                if (obtain2.readInt() != 0) {
                    z = true;
                }
                obtain2.recycle();
                obtain.recycle();
                return z;
            }

            public void pause() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.ads.internal.client.IAdManager");
                this.kn.transact(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void resume() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.ads.internal.client.IAdManager");
                this.kn.transact(ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void showInterstitial() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.ads.internal.client.IAdManager");
                this.kn.transact(ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void stopLoading() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.ads.internal.client.IAdManager");
                this.kn.transact(ApiEventType.API_MRAID_USE_CUSTOM_CLOSE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }
        }

        public a() {
            attachInterface(this, "com.google.android.gms.ads.internal.client.IAdManager");
        }

        public static ap f(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.ads.internal.client.IAdManager");
            return (queryLocalInterface == null || !queryLocalInterface instanceof ap) ? new a(iBinder) : (ap) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            ak akVar = null;
            int i = 0;
            boolean isReady;
            switch (code) {
                case MMAdView.TRANSITION_FADE:
                    IBinder asBinder;
                    data.enforceInterface("com.google.android.gms.ads.internal.client.IAdManager");
                    d Q = Q();
                    reply.writeNoException();
                    if (Q != null) {
                        asBinder = Q.asBinder();
                    }
                    reply.writeStrongBinder(asBinder);
                    return true;
                case MMAdView.TRANSITION_UP:
                    data.enforceInterface("com.google.android.gms.ads.internal.client.IAdManager");
                    destroy();
                    reply.writeNoException();
                    return true;
                case MMAdView.TRANSITION_DOWN:
                    data.enforceInterface("com.google.android.gms.ads.internal.client.IAdManager");
                    isReady = isReady();
                    reply.writeNoException();
                    reply.writeInt(isReady ? 1 : 0);
                    return true;
                case MMAdView.TRANSITION_RANDOM:
                    ah a;
                    data.enforceInterface("com.google.android.gms.ads.internal.client.IAdManager");
                    if (data.readInt() != 0) {
                        a = ah.CREATOR.a(data);
                    }
                    isReady = a(a);
                    reply.writeNoException();
                    if (isReady) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.ads.internal.client.IAdManager");
                    pause();
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.ads.internal.client.IAdManager");
                    resume();
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.ads.internal.client.IAdManager");
                    a(com.google.android.gms.internal.ao.a.e(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.ads.internal.client.IAdManager");
                    a(com.google.android.gms.internal.ar.a.h(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.ads.internal.client.IAdManager");
                    showInterstitial();
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                    data.enforceInterface("com.google.android.gms.ads.internal.client.IAdManager");
                    stopLoading();
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_EXPAND:
                    data.enforceInterface("com.google.android.gms.ads.internal.client.IAdManager");
                    ac();
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_RESIZE:
                    data.enforceInterface("com.google.android.gms.ads.internal.client.IAdManager");
                    akVar = R();
                    reply.writeNoException();
                    if (akVar != null) {
                        reply.writeInt(1);
                        akVar.writeToParcel(reply, 1);
                        return true;
                    } else {
                        reply.writeInt(0);
                        return true;
                    }
                case ApiEventType.API_MRAID_CLOSE:
                    data.enforceInterface("com.google.android.gms.ads.internal.client.IAdManager");
                    if (data.readInt() != 0) {
                        akVar = ak.CREATOR.b(data);
                    }
                    a(akVar);
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_IS_VIEWABLE:
                    data.enforceInterface("com.google.android.gms.ads.internal.client.IAdManager");
                    a(com.google.android.gms.internal.co.a.p(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.ads.internal.client.IAdManager");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    d Q() throws RemoteException;

    ak R() throws RemoteException;

    void a(ak akVar) throws RemoteException;

    void a(ao aoVar) throws RemoteException;

    void a(ar arVar) throws RemoteException;

    void a(co coVar) throws RemoteException;

    boolean a(ah ahVar) throws RemoteException;

    void ac() throws RemoteException;

    void destroy() throws RemoteException;

    boolean isReady() throws RemoteException;

    void pause() throws RemoteException;

    void resume() throws RemoteException;

    void showInterstitial() throws RemoteException;

    void stopLoading() throws RemoteException;
}