package com.google.android.gms.dynamic;

import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public interface c extends IInterface {

    public static abstract class a extends Binder implements c {

        private static class a implements c {
            private IBinder kn;

            a(IBinder iBinder) {
                this.kn = iBinder;
            }

            public IBinder asBinder() {
                return this.kn;
            }

            public void b(d dVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                obtain.writeStrongBinder(dVar != null ? dVar.asBinder() : null);
                this.kn.transact(ApiEventType.API_MRAID_GET_MAX_SIZE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void c(d dVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                obtain.writeStrongBinder(dVar != null ? dVar.asBinder() : null);
                this.kn.transact(ApiEventType.API_MRAID_SEND_SMS, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public d fX() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                this.kn.transact(MMAdView.TRANSITION_UP, obtain, obtain2, 0);
                obtain2.readException();
                d K = com.google.android.gms.dynamic.d.a.K(obtain2.readStrongBinder());
                obtain2.recycle();
                obtain.recycle();
                return K;
            }

            public c fY() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                this.kn.transact(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                c J = com.google.android.gms.dynamic.c.a.J(obtain2.readStrongBinder());
                obtain2.recycle();
                obtain.recycle();
                return J;
            }

            public d fZ() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                this.kn.transact(ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                d K = com.google.android.gms.dynamic.d.a.K(obtain2.readStrongBinder());
                obtain2.recycle();
                obtain.recycle();
                return K;
            }

            public c ga() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                this.kn.transact(ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                c J = com.google.android.gms.dynamic.c.a.J(obtain2.readStrongBinder());
                obtain2.recycle();
                obtain.recycle();
                return J;
            }

            public Bundle getArguments() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                this.kn.transact(MMAdView.TRANSITION_DOWN, obtain, obtain2, 0);
                obtain2.readException();
                Bundle bundle = obtain2.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(obtain2) : null;
                obtain2.recycle();
                obtain.recycle();
                return bundle;
            }

            public int getId() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                this.kn.transact(MMAdView.TRANSITION_RANDOM, obtain, obtain2, 0);
                obtain2.readException();
                int readInt = obtain2.readInt();
                obtain2.recycle();
                obtain.recycle();
                return readInt;
            }

            public boolean getRetainInstance() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                this.kn.transact(ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                if (obtain2.readInt() != 0) {
                    z = true;
                }
                obtain2.recycle();
                obtain.recycle();
                return z;
            }

            public String getTag() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                this.kn.transact(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                String readString = obtain2.readString();
                obtain2.recycle();
                obtain.recycle();
                return readString;
            }

            public int getTargetRequestCode() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                this.kn.transact(ApiEventType.API_MRAID_USE_CUSTOM_CLOSE, obtain, obtain2, 0);
                obtain2.readException();
                int readInt = obtain2.readInt();
                obtain2.recycle();
                obtain.recycle();
                return readInt;
            }

            public boolean getUserVisibleHint() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                this.kn.transact(ApiEventType.API_MRAID_EXPAND, obtain, obtain2, 0);
                obtain2.readException();
                if (obtain2.readInt() != 0) {
                    z = true;
                }
                obtain2.recycle();
                obtain.recycle();
                return z;
            }

            public d getView() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                this.kn.transact(ApiEventType.API_MRAID_RESIZE, obtain, obtain2, 0);
                obtain2.readException();
                d K = com.google.android.gms.dynamic.d.a.K(obtain2.readStrongBinder());
                obtain2.recycle();
                obtain.recycle();
                return K;
            }

            public boolean isAdded() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                this.kn.transact(ApiEventType.API_MRAID_CLOSE, obtain, obtain2, 0);
                obtain2.readException();
                if (obtain2.readInt() != 0) {
                    z = true;
                }
                obtain2.recycle();
                obtain.recycle();
                return z;
            }

            public boolean isDetached() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                this.kn.transact(ApiEventType.API_MRAID_IS_VIEWABLE, obtain, obtain2, 0);
                obtain2.readException();
                if (obtain2.readInt() != 0) {
                    z = true;
                }
                obtain2.recycle();
                obtain.recycle();
                return z;
            }

            public boolean isHidden() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                this.kn.transact(ApiEventType.API_MRAID_GET_PLACEMENT_TYPE, obtain, obtain2, 0);
                obtain2.readException();
                if (obtain2.readInt() != 0) {
                    z = true;
                }
                obtain2.recycle();
                obtain.recycle();
                return z;
            }

            public boolean isInLayout() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                this.kn.transact(ApiEventType.API_MRAID_GET_ORIENTATION, obtain, obtain2, 0);
                obtain2.readException();
                if (obtain2.readInt() != 0) {
                    z = true;
                }
                obtain2.recycle();
                obtain.recycle();
                return z;
            }

            public boolean isRemoving() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                this.kn.transact(ApiEventType.API_MRAID_GET_SCREEN_SIZE, obtain, obtain2, 0);
                obtain2.readException();
                if (obtain2.readInt() != 0) {
                    z = true;
                }
                obtain2.recycle();
                obtain.recycle();
                return z;
            }

            public boolean isResumed() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                this.kn.transact(ApiEventType.API_MRAID_GET_CURRENT_POSITION, obtain, obtain2, 0);
                obtain2.readException();
                if (obtain2.readInt() != 0) {
                    z = true;
                }
                obtain2.recycle();
                obtain.recycle();
                return z;
            }

            public boolean isVisible() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                this.kn.transact(Encoder.LINE_GROUPS, obtain, obtain2, 0);
                obtain2.readException();
                if (obtain2.readInt() != 0) {
                    z = true;
                }
                obtain2.recycle();
                obtain.recycle();
                return z;
            }

            public void setHasOptionsMenu(boolean hasMenu) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                if (hasMenu) {
                    i = 1;
                }
                obtain.writeInt(i);
                this.kn.transact(ApiEventType.API_MRAID_POST_TO_SOCIAL, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void setMenuVisibility(boolean menuVisible) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                if (menuVisible) {
                    i = 1;
                }
                obtain.writeInt(i);
                this.kn.transact(ApiEventType.API_MRAID_SUPPORTS, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void setRetainInstance(boolean retain) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                if (retain) {
                    i = 1;
                }
                obtain.writeInt(i);
                this.kn.transact(ApiEventType.API_MRAID_STORE_PICTURE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void setUserVisibleHint(boolean isVisibleToUser) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                if (isVisibleToUser) {
                    i = 1;
                }
                obtain.writeInt(i);
                this.kn.transact(ApiEventType.API_MRAID_TAKE_CAMERA_PICTURE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void startActivity(Intent intent) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                if (intent != null) {
                    obtain.writeInt(1);
                    intent.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(ApiEventType.API_MRAID_GET_GALLERY_IMAGE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void startActivityForResult(Intent intent, int requestCode) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                if (intent != null) {
                    obtain.writeInt(1);
                    intent.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                obtain.writeInt(requestCode);
                this.kn.transact(ApiEventType.API_MRAID_VIBRATE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }
        }

        public a() {
            attachInterface(this, "com.google.android.gms.dynamic.IFragmentWrapper");
        }

        public static c J(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.dynamic.IFragmentWrapper");
            return (queryLocalInterface == null || !queryLocalInterface instanceof c) ? new a(iBinder) : (c) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            Intent intent = null;
            boolean z = false;
            d fX;
            IBinder asBinder;
            int id;
            c fY;
            boolean retainInstance;
            int i;
            switch (code) {
                case MMAdView.TRANSITION_UP:
                    data.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    fX = fX();
                    reply.writeNoException();
                    if (fX != null) {
                        asBinder = fX.asBinder();
                    }
                    reply.writeStrongBinder(asBinder);
                    return true;
                case MMAdView.TRANSITION_DOWN:
                    data.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    Bundle arguments = getArguments();
                    reply.writeNoException();
                    if (arguments != null) {
                        reply.writeInt(1);
                        arguments.writeToParcel(reply, 1);
                        return true;
                    } else {
                        reply.writeInt(0);
                        return true;
                    }
                case MMAdView.TRANSITION_RANDOM:
                    data.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    id = getId();
                    reply.writeNoException();
                    reply.writeInt(id);
                    return true;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    fY = fY();
                    reply.writeNoException();
                    if (fY != null) {
                        asBinder = fY.asBinder();
                    }
                    reply.writeStrongBinder(asBinder);
                    return true;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    fX = fZ();
                    reply.writeNoException();
                    if (fX != null) {
                        asBinder = fX.asBinder();
                    }
                    reply.writeStrongBinder(asBinder);
                    return true;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    retainInstance = getRetainInstance();
                    reply.writeNoException();
                    reply.writeInt(retainInstance ? 1 : 0);
                    return true;
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    String tag = getTag();
                    reply.writeNoException();
                    reply.writeString(tag);
                    return true;
                case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    fY = ga();
                    reply.writeNoException();
                    if (fY != null) {
                        asBinder = fY.asBinder();
                    }
                    reply.writeStrongBinder(asBinder);
                    return true;
                case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                    data.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    id = getTargetRequestCode();
                    reply.writeNoException();
                    reply.writeInt(id);
                    return true;
                case ApiEventType.API_MRAID_EXPAND:
                    data.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    retainInstance = getUserVisibleHint();
                    reply.writeNoException();
                    if (retainInstance) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case ApiEventType.API_MRAID_RESIZE:
                    data.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    fX = getView();
                    reply.writeNoException();
                    if (fX != null) {
                        asBinder = fX.asBinder();
                    }
                    reply.writeStrongBinder(asBinder);
                    return true;
                case ApiEventType.API_MRAID_CLOSE:
                    data.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    retainInstance = isAdded();
                    reply.writeNoException();
                    if (retainInstance) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case ApiEventType.API_MRAID_IS_VIEWABLE:
                    data.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    retainInstance = isDetached();
                    reply.writeNoException();
                    if (retainInstance) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case ApiEventType.API_MRAID_GET_PLACEMENT_TYPE:
                    data.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    retainInstance = isHidden();
                    reply.writeNoException();
                    if (retainInstance) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case ApiEventType.API_MRAID_GET_ORIENTATION:
                    data.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    retainInstance = isInLayout();
                    reply.writeNoException();
                    if (retainInstance) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case ApiEventType.API_MRAID_GET_SCREEN_SIZE:
                    data.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    retainInstance = isRemoving();
                    reply.writeNoException();
                    if (retainInstance) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case ApiEventType.API_MRAID_GET_CURRENT_POSITION:
                    data.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    retainInstance = isResumed();
                    reply.writeNoException();
                    if (retainInstance) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case Encoder.LINE_GROUPS:
                    data.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    retainInstance = isVisible();
                    reply.writeNoException();
                    if (retainInstance) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case ApiEventType.API_MRAID_GET_MAX_SIZE:
                    data.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    b(com.google.android.gms.dynamic.d.a.K(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_POST_TO_SOCIAL:
                    data.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    setHasOptionsMenu(z);
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_SUPPORTS:
                    data.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    setMenuVisibility(z);
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_STORE_PICTURE:
                    data.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    setRetainInstance(z);
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_TAKE_CAMERA_PICTURE:
                    data.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    setUserVisibleHint(z);
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_GET_GALLERY_IMAGE:
                    data.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    if (data.readInt() != 0) {
                        intent = (Intent) Intent.CREATOR.createFromParcel(data);
                    }
                    startActivity(intent);
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_VIBRATE:
                    data.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    if (data.readInt() != 0) {
                        intent = (Intent) Intent.CREATOR.createFromParcel(data);
                    }
                    startActivityForResult(intent, data.readInt());
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_SEND_SMS:
                    data.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    c(com.google.android.gms.dynamic.d.a.K(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.dynamic.IFragmentWrapper");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void b(d dVar) throws RemoteException;

    void c(d dVar) throws RemoteException;

    d fX() throws RemoteException;

    c fY() throws RemoteException;

    d fZ() throws RemoteException;

    c ga() throws RemoteException;

    Bundle getArguments() throws RemoteException;

    int getId() throws RemoteException;

    boolean getRetainInstance() throws RemoteException;

    String getTag() throws RemoteException;

    int getTargetRequestCode() throws RemoteException;

    boolean getUserVisibleHint() throws RemoteException;

    d getView() throws RemoteException;

    boolean isAdded() throws RemoteException;

    boolean isDetached() throws RemoteException;

    boolean isHidden() throws RemoteException;

    boolean isInLayout() throws RemoteException;

    boolean isRemoving() throws RemoteException;

    boolean isResumed() throws RemoteException;

    boolean isVisible() throws RemoteException;

    void setHasOptionsMenu(boolean z) throws RemoteException;

    void setMenuVisibility(boolean z) throws RemoteException;

    void setRetainInstance(boolean z) throws RemoteException;

    void setUserVisibleHint(boolean z) throws RemoteException;

    void startActivity(Intent intent) throws RemoteException;

    void startActivityForResult(Intent intent, int i) throws RemoteException;
}