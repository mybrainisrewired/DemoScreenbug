package com.google.android.gms.maps.internal;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.d;
import com.google.android.gms.maps.StreetViewPanoramaOptions;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public interface IStreetViewPanoramaFragmentDelegate extends IInterface {

    public static abstract class a extends Binder implements IStreetViewPanoramaFragmentDelegate {

        private static class a implements IStreetViewPanoramaFragmentDelegate {
            private IBinder kn;

            a(IBinder iBinder) {
                this.kn = iBinder;
            }

            public IBinder asBinder() {
                return this.kn;
            }

            public IStreetViewPanoramaDelegate getStreetViewPanorama() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IStreetViewPanoramaFragmentDelegate");
                this.kn.transact(1, obtain, obtain2, 0);
                obtain2.readException();
                IStreetViewPanoramaDelegate ax = com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate.a.ax(obtain2.readStrongBinder());
                obtain2.recycle();
                obtain.recycle();
                return ax;
            }

            public boolean isReady() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IStreetViewPanoramaFragmentDelegate");
                this.kn.transact(ApiEventType.API_MRAID_EXPAND, obtain, obtain2, 0);
                obtain2.readException();
                if (obtain2.readInt() != 0) {
                    z = true;
                }
                obtain2.recycle();
                obtain.recycle();
                return z;
            }

            public void onCreate(Bundle savedInstanceState) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IStreetViewPanoramaFragmentDelegate");
                if (savedInstanceState != null) {
                    obtain.writeInt(1);
                    savedInstanceState.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(MMAdView.TRANSITION_DOWN, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public d onCreateView(d inflaterWrapper, d containerWrapper, Bundle savedInstanceState) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IStreetViewPanoramaFragmentDelegate");
                obtain.writeStrongBinder(inflaterWrapper != null ? inflaterWrapper.asBinder() : null);
                if (containerWrapper != null) {
                    iBinder = containerWrapper.asBinder();
                }
                obtain.writeStrongBinder(iBinder);
                if (savedInstanceState != null) {
                    obtain.writeInt(1);
                    savedInstanceState.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(MMAdView.TRANSITION_RANDOM, obtain, obtain2, 0);
                obtain2.readException();
                d K = com.google.android.gms.dynamic.d.a.K(obtain2.readStrongBinder());
                obtain2.recycle();
                obtain.recycle();
                return K;
            }

            public void onDestroy() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IStreetViewPanoramaFragmentDelegate");
                this.kn.transact(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void onDestroyView() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IStreetViewPanoramaFragmentDelegate");
                this.kn.transact(ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void onInflate(d activityWrapper, StreetViewPanoramaOptions options, Bundle savedInstanceState) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IStreetViewPanoramaFragmentDelegate");
                obtain.writeStrongBinder(activityWrapper != null ? activityWrapper.asBinder() : null);
                if (options != null) {
                    obtain.writeInt(1);
                    options.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                if (savedInstanceState != null) {
                    obtain.writeInt(1);
                    savedInstanceState.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(MMAdView.TRANSITION_UP, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void onLowMemory() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IStreetViewPanoramaFragmentDelegate");
                this.kn.transact(ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void onPause() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IStreetViewPanoramaFragmentDelegate");
                this.kn.transact(ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void onResume() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IStreetViewPanoramaFragmentDelegate");
                this.kn.transact(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void onSaveInstanceState(Bundle outState) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IStreetViewPanoramaFragmentDelegate");
                if (outState != null) {
                    obtain.writeInt(1);
                    outState.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(ApiEventType.API_MRAID_USE_CUSTOM_CLOSE, obtain, obtain2, 0);
                obtain2.readException();
                if (obtain2.readInt() != 0) {
                    outState.readFromParcel(obtain2);
                }
                obtain2.recycle();
                obtain.recycle();
            }
        }

        public static IStreetViewPanoramaFragmentDelegate ay(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.maps.internal.IStreetViewPanoramaFragmentDelegate");
            return (queryLocalInterface == null || !queryLocalInterface instanceof IStreetViewPanoramaFragmentDelegate) ? new a(iBinder) : (IStreetViewPanoramaFragmentDelegate) queryLocalInterface;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            IBinder iBinder = null;
            switch (code) {
                case MMAdView.TRANSITION_FADE:
                    data.enforceInterface("com.google.android.gms.maps.internal.IStreetViewPanoramaFragmentDelegate");
                    IStreetViewPanoramaDelegate streetViewPanorama = getStreetViewPanorama();
                    reply.writeNoException();
                    reply.writeStrongBinder(streetViewPanorama != null ? streetViewPanorama.asBinder() : null);
                    return true;
                case MMAdView.TRANSITION_UP:
                    data.enforceInterface("com.google.android.gms.maps.internal.IStreetViewPanoramaFragmentDelegate");
                    onInflate(com.google.android.gms.dynamic.d.a.K(data.readStrongBinder()), data.readInt() != 0 ? StreetViewPanoramaOptions.CREATOR.createFromParcel(data) : null, data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case MMAdView.TRANSITION_DOWN:
                    data.enforceInterface("com.google.android.gms.maps.internal.IStreetViewPanoramaFragmentDelegate");
                    onCreate(data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case MMAdView.TRANSITION_RANDOM:
                    data.enforceInterface("com.google.android.gms.maps.internal.IStreetViewPanoramaFragmentDelegate");
                    d onCreateView = onCreateView(com.google.android.gms.dynamic.d.a.K(data.readStrongBinder()), com.google.android.gms.dynamic.d.a.K(data.readStrongBinder()), data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    if (onCreateView != null) {
                        iBinder = onCreateView.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.maps.internal.IStreetViewPanoramaFragmentDelegate");
                    onResume();
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.maps.internal.IStreetViewPanoramaFragmentDelegate");
                    onPause();
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.maps.internal.IStreetViewPanoramaFragmentDelegate");
                    onDestroyView();
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.maps.internal.IStreetViewPanoramaFragmentDelegate");
                    onDestroy();
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.maps.internal.IStreetViewPanoramaFragmentDelegate");
                    onLowMemory();
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                    data.enforceInterface("com.google.android.gms.maps.internal.IStreetViewPanoramaFragmentDelegate");
                    Bundle bundle = data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null;
                    onSaveInstanceState(bundle);
                    reply.writeNoException();
                    if (bundle != null) {
                        reply.writeInt(1);
                        bundle.writeToParcel(reply, 1);
                        return true;
                    } else {
                        reply.writeInt(0);
                        return true;
                    }
                case ApiEventType.API_MRAID_EXPAND:
                    data.enforceInterface("com.google.android.gms.maps.internal.IStreetViewPanoramaFragmentDelegate");
                    boolean isReady = isReady();
                    reply.writeNoException();
                    reply.writeInt(isReady ? 1 : 0);
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.maps.internal.IStreetViewPanoramaFragmentDelegate");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    IStreetViewPanoramaDelegate getStreetViewPanorama() throws RemoteException;

    boolean isReady() throws RemoteException;

    void onCreate(Bundle bundle) throws RemoteException;

    d onCreateView(d dVar, d dVar2, Bundle bundle) throws RemoteException;

    void onDestroy() throws RemoteException;

    void onDestroyView() throws RemoteException;

    void onInflate(d dVar, StreetViewPanoramaOptions streetViewPanoramaOptions, Bundle bundle) throws RemoteException;

    void onLowMemory() throws RemoteException;

    void onPause() throws RemoteException;

    void onResume() throws RemoteException;

    void onSaveInstanceState(Bundle bundle) throws RemoteException;
}