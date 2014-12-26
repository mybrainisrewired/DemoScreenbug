package com.google.android.gms.maps.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.d;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.StreetViewPanoramaOptions;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public interface c extends IInterface {

    public static abstract class a extends Binder implements c {

        private static class a implements c {
            private IBinder kn;

            a(IBinder iBinder) {
                this.kn = iBinder;
            }

            public IMapViewDelegate a(d dVar, GoogleMapOptions googleMapOptions) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICreator");
                obtain.writeStrongBinder(dVar != null ? dVar.asBinder() : null);
                if (googleMapOptions != null) {
                    obtain.writeInt(1);
                    googleMapOptions.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(MMAdView.TRANSITION_DOWN, obtain, obtain2, 0);
                obtain2.readException();
                IMapViewDelegate ag = com.google.android.gms.maps.internal.IMapViewDelegate.a.ag(obtain2.readStrongBinder());
                obtain2.recycle();
                obtain.recycle();
                return ag;
            }

            public IStreetViewPanoramaViewDelegate a(d dVar, StreetViewPanoramaOptions streetViewPanoramaOptions) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICreator");
                obtain.writeStrongBinder(dVar != null ? dVar.asBinder() : null);
                if (streetViewPanoramaOptions != null) {
                    obtain.writeInt(1);
                    streetViewPanoramaOptions.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                IStreetViewPanoramaViewDelegate az = com.google.android.gms.maps.internal.IStreetViewPanoramaViewDelegate.a.az(obtain2.readStrongBinder());
                obtain2.recycle();
                obtain.recycle();
                return az;
            }

            public void a(d dVar, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICreator");
                obtain.writeStrongBinder(dVar != null ? dVar.asBinder() : null);
                obtain.writeInt(i);
                this.kn.transact(ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public IBinder asBinder() {
                return this.kn;
            }

            public void g(d dVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICreator");
                obtain.writeStrongBinder(dVar != null ? dVar.asBinder() : null);
                this.kn.transact(1, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public IMapFragmentDelegate h(d dVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICreator");
                obtain.writeStrongBinder(dVar != null ? dVar.asBinder() : null);
                this.kn.transact(MMAdView.TRANSITION_UP, obtain, obtain2, 0);
                obtain2.readException();
                IMapFragmentDelegate af = com.google.android.gms.maps.internal.IMapFragmentDelegate.a.af(obtain2.readStrongBinder());
                obtain2.recycle();
                obtain.recycle();
                return af;
            }

            public IStreetViewPanoramaFragmentDelegate i(d dVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICreator");
                obtain.writeStrongBinder(dVar != null ? dVar.asBinder() : null);
                this.kn.transact(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                IStreetViewPanoramaFragmentDelegate ay = com.google.android.gms.maps.internal.IStreetViewPanoramaFragmentDelegate.a.ay(obtain2.readStrongBinder());
                obtain2.recycle();
                obtain.recycle();
                return ay;
            }

            public ICameraUpdateFactoryDelegate ix() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICreator");
                this.kn.transact(MMAdView.TRANSITION_RANDOM, obtain, obtain2, 0);
                obtain2.readException();
                ICameraUpdateFactoryDelegate Z = com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate.a.Z(obtain2.readStrongBinder());
                obtain2.recycle();
                obtain.recycle();
                return Z;
            }

            public com.google.android.gms.maps.model.internal.a iy() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICreator");
                this.kn.transact(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                com.google.android.gms.maps.model.internal.a aB = com.google.android.gms.maps.model.internal.a.a.aB(obtain2.readStrongBinder());
                obtain2.recycle();
                obtain.recycle();
                return aB;
            }
        }

        public static c ab(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.maps.internal.ICreator");
            return (queryLocalInterface == null || !queryLocalInterface instanceof c) ? new a(iBinder) : (c) queryLocalInterface;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            IBinder iBinder = null;
            switch (code) {
                case MMAdView.TRANSITION_FADE:
                    data.enforceInterface("com.google.android.gms.maps.internal.ICreator");
                    g(com.google.android.gms.dynamic.d.a.K(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case MMAdView.TRANSITION_UP:
                    data.enforceInterface("com.google.android.gms.maps.internal.ICreator");
                    IMapFragmentDelegate h = h(com.google.android.gms.dynamic.d.a.K(data.readStrongBinder()));
                    reply.writeNoException();
                    if (h != null) {
                        iBinder = h.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case MMAdView.TRANSITION_DOWN:
                    data.enforceInterface("com.google.android.gms.maps.internal.ICreator");
                    IMapViewDelegate a = a(com.google.android.gms.dynamic.d.a.K(data.readStrongBinder()), data.readInt() != 0 ? GoogleMapOptions.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    if (a != null) {
                        iBinder = a.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case MMAdView.TRANSITION_RANDOM:
                    data.enforceInterface("com.google.android.gms.maps.internal.ICreator");
                    ICameraUpdateFactoryDelegate ix = ix();
                    reply.writeNoException();
                    if (ix != null) {
                        iBinder = ix.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.maps.internal.ICreator");
                    com.google.android.gms.maps.model.internal.a iy = iy();
                    reply.writeNoException();
                    if (iy != null) {
                        iBinder = iy.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.maps.internal.ICreator");
                    a(com.google.android.gms.dynamic.d.a.K(data.readStrongBinder()), data.readInt());
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.maps.internal.ICreator");
                    IStreetViewPanoramaViewDelegate a2 = a(com.google.android.gms.dynamic.d.a.K(data.readStrongBinder()), data.readInt() != 0 ? StreetViewPanoramaOptions.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    if (a2 != null) {
                        iBinder = a2.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.maps.internal.ICreator");
                    IStreetViewPanoramaFragmentDelegate i = i(com.google.android.gms.dynamic.d.a.K(data.readStrongBinder()));
                    reply.writeNoException();
                    if (i != null) {
                        iBinder = i.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.maps.internal.ICreator");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    IMapViewDelegate a(d dVar, GoogleMapOptions googleMapOptions) throws RemoteException;

    IStreetViewPanoramaViewDelegate a(d dVar, StreetViewPanoramaOptions streetViewPanoramaOptions) throws RemoteException;

    void a(d dVar, int i) throws RemoteException;

    void g(d dVar) throws RemoteException;

    IMapFragmentDelegate h(d dVar) throws RemoteException;

    IStreetViewPanoramaFragmentDelegate i(d dVar) throws RemoteException;

    ICameraUpdateFactoryDelegate ix() throws RemoteException;

    com.google.android.gms.maps.model.internal.a iy() throws RemoteException;
}