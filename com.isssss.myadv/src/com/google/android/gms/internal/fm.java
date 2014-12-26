package com.google.android.gms.internal;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public interface fm extends IInterface {

    public static abstract class a extends Binder implements fm {

        private static class a implements fm {
            private IBinder kn;

            a(IBinder iBinder) {
                this.kn = iBinder;
            }

            public void a(fl flVar, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                obtain.writeStrongBinder(flVar != null ? flVar.asBinder() : null);
                obtain.writeInt(i);
                this.kn.transact(MMAdView.TRANSITION_RANDOM, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(fl flVar, int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                obtain.writeStrongBinder(flVar != null ? flVar.asBinder() : null);
                obtain.writeInt(i);
                obtain.writeString(str);
                this.kn.transact(MMAdView.TRANSITION_DOWN, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(fl flVar, int i, String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                obtain.writeStrongBinder(flVar != null ? flVar.asBinder() : null);
                obtain.writeInt(i);
                obtain.writeString(str);
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

            public void a(fl flVar, int i, String str, IBinder iBinder, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                obtain.writeStrongBinder(flVar != null ? flVar.asBinder() : null);
                obtain.writeInt(i);
                obtain.writeString(str);
                obtain.writeStrongBinder(iBinder);
                if (bundle != null) {
                    obtain.writeInt(1);
                    bundle.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(Encoder.LINE_GROUPS, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(fl flVar, int i, String str, String str2, String[] strArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                obtain.writeStrongBinder(flVar != null ? flVar.asBinder() : null);
                obtain.writeInt(i);
                obtain.writeString(str);
                obtain.writeString(str2);
                obtain.writeStringArray(strArr);
                this.kn.transact(ApiEventType.API_MRAID_USE_CUSTOM_CLOSE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(fl flVar, int i, String str, String str2, String[] strArr, String str3, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                obtain.writeStrongBinder(flVar != null ? flVar.asBinder() : null);
                obtain.writeInt(i);
                obtain.writeString(str);
                obtain.writeString(str2);
                obtain.writeStringArray(strArr);
                obtain.writeString(str3);
                if (bundle != null) {
                    obtain.writeInt(1);
                    bundle.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(1, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(fl flVar, int i, String str, String str2, String[] strArr, String str3, IBinder iBinder, String str4, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                obtain.writeStrongBinder(flVar != null ? flVar.asBinder() : null);
                obtain.writeInt(i);
                obtain.writeString(str);
                obtain.writeString(str2);
                obtain.writeStringArray(strArr);
                obtain.writeString(str3);
                obtain.writeStrongBinder(iBinder);
                obtain.writeString(str4);
                if (bundle != null) {
                    obtain.writeInt(1);
                    bundle.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(fl flVar, int i, String str, String[] strArr, String str2, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                obtain.writeStrongBinder(flVar != null ? flVar.asBinder() : null);
                obtain.writeInt(i);
                obtain.writeString(str);
                obtain.writeStringArray(strArr);
                obtain.writeString(str2);
                if (bundle != null) {
                    obtain.writeInt(1);
                    bundle.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(ApiEventType.API_MRAID_GET_MAX_SIZE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public IBinder asBinder() {
                return this.kn;
            }

            public void b(fl flVar, int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                obtain.writeStrongBinder(flVar != null ? flVar.asBinder() : null);
                obtain.writeInt(i);
                obtain.writeString(str);
                this.kn.transact(ApiEventType.API_MRAID_POST_TO_SOCIAL, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void b(fl flVar, int i, String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                obtain.writeStrongBinder(flVar != null ? flVar.asBinder() : null);
                obtain.writeInt(i);
                obtain.writeString(str);
                if (bundle != null) {
                    obtain.writeInt(1);
                    bundle.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void b(fl flVar, int i, String str, String str2, String[] strArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                obtain.writeStrongBinder(flVar != null ? flVar.asBinder() : null);
                obtain.writeInt(i);
                obtain.writeString(str);
                obtain.writeString(str2);
                obtain.writeStringArray(strArr);
                this.kn.transact(ApiEventType.API_MRAID_SEND_MAIL, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void c(fl flVar, int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                obtain.writeStrongBinder(flVar != null ? flVar.asBinder() : null);
                obtain.writeInt(i);
                obtain.writeString(str);
                this.kn.transact(ApiEventType.API_MRAID_SUPPORTS, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void c(fl flVar, int i, String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                obtain.writeStrongBinder(flVar != null ? flVar.asBinder() : null);
                obtain.writeInt(i);
                obtain.writeString(str);
                if (bundle != null) {
                    obtain.writeInt(1);
                    bundle.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void d(fl flVar, int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                obtain.writeStrongBinder(flVar != null ? flVar.asBinder() : null);
                obtain.writeInt(i);
                obtain.writeString(str);
                this.kn.transact(ApiEventType.API_MRAID_TAKE_CAMERA_PICTURE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void d(fl flVar, int i, String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                obtain.writeStrongBinder(flVar != null ? flVar.asBinder() : null);
                obtain.writeInt(i);
                obtain.writeString(str);
                if (bundle != null) {
                    obtain.writeInt(1);
                    bundle.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void e(fl flVar, int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                obtain.writeStrongBinder(flVar != null ? flVar.asBinder() : null);
                obtain.writeInt(i);
                obtain.writeString(str);
                this.kn.transact(ApiEventType.API_MRAID_VIBRATE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void e(fl flVar, int i, String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                obtain.writeStrongBinder(flVar != null ? flVar.asBinder() : null);
                obtain.writeInt(i);
                obtain.writeString(str);
                if (bundle != null) {
                    obtain.writeInt(1);
                    bundle.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void f(fl flVar, int i, String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                obtain.writeStrongBinder(flVar != null ? flVar.asBinder() : null);
                obtain.writeInt(i);
                obtain.writeString(str);
                if (bundle != null) {
                    obtain.writeInt(1);
                    bundle.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(ApiEventType.API_MRAID_EXPAND, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void g(fl flVar, int i, String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                obtain.writeStrongBinder(flVar != null ? flVar.asBinder() : null);
                obtain.writeInt(i);
                obtain.writeString(str);
                if (bundle != null) {
                    obtain.writeInt(1);
                    bundle.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(ApiEventType.API_MRAID_RESIZE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void h(fl flVar, int i, String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                obtain.writeStrongBinder(flVar != null ? flVar.asBinder() : null);
                obtain.writeInt(i);
                obtain.writeString(str);
                if (bundle != null) {
                    obtain.writeInt(1);
                    bundle.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(ApiEventType.API_MRAID_CLOSE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void i(fl flVar, int i, String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                obtain.writeStrongBinder(flVar != null ? flVar.asBinder() : null);
                obtain.writeInt(i);
                obtain.writeString(str);
                if (bundle != null) {
                    obtain.writeInt(1);
                    bundle.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(ApiEventType.API_MRAID_IS_VIEWABLE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void j(fl flVar, int i, String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                obtain.writeStrongBinder(flVar != null ? flVar.asBinder() : null);
                obtain.writeInt(i);
                obtain.writeString(str);
                if (bundle != null) {
                    obtain.writeInt(1);
                    bundle.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(ApiEventType.API_MRAID_GET_PLACEMENT_TYPE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void k(fl flVar, int i, String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                obtain.writeStrongBinder(flVar != null ? flVar.asBinder() : null);
                obtain.writeInt(i);
                obtain.writeString(str);
                if (bundle != null) {
                    obtain.writeInt(1);
                    bundle.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(ApiEventType.API_MRAID_GET_ORIENTATION, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void l(fl flVar, int i, String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                obtain.writeStrongBinder(flVar != null ? flVar.asBinder() : null);
                obtain.writeInt(i);
                obtain.writeString(str);
                if (bundle != null) {
                    obtain.writeInt(1);
                    bundle.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(ApiEventType.API_MRAID_GET_SCREEN_SIZE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void m(fl flVar, int i, String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                obtain.writeStrongBinder(flVar != null ? flVar.asBinder() : null);
                obtain.writeInt(i);
                obtain.writeString(str);
                if (bundle != null) {
                    obtain.writeInt(1);
                    bundle.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(ApiEventType.API_MRAID_GET_CURRENT_POSITION, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void n(fl flVar, int i, String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                obtain.writeStrongBinder(flVar != null ? flVar.asBinder() : null);
                obtain.writeInt(i);
                obtain.writeString(str);
                if (bundle != null) {
                    obtain.writeInt(1);
                    bundle.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(ApiEventType.API_MRAID_STORE_PICTURE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void o(fl flVar, int i, String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                obtain.writeStrongBinder(flVar != null ? flVar.asBinder() : null);
                obtain.writeInt(i);
                obtain.writeString(str);
                if (bundle != null) {
                    obtain.writeInt(1);
                    bundle.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(ApiEventType.API_MRAID_GET_GALLERY_IMAGE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void p(fl flVar, int i, String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                obtain.writeStrongBinder(flVar != null ? flVar.asBinder() : null);
                obtain.writeInt(i);
                obtain.writeString(str);
                if (bundle != null) {
                    obtain.writeInt(1);
                    bundle.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(ApiEventType.API_MRAID_SEND_SMS, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }
        }

        public static fm C(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
            return (queryLocalInterface == null || !queryLocalInterface instanceof fm) ? new a(iBinder) : (fm) queryLocalInterface;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            Bundle bundle = null;
            fl B;
            int readInt;
            String readString;
            switch (code) {
                case MMAdView.TRANSITION_FADE:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    a(com.google.android.gms.internal.fl.a.B(data.readStrongBinder()), data.readInt(), data.readString(), data.readString(), data.createStringArray(), data.readString(), data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case MMAdView.TRANSITION_UP:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    B = com.google.android.gms.internal.fl.a.B(data.readStrongBinder());
                    readInt = data.readInt();
                    readString = data.readString();
                    if (data.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    }
                    a(B, readInt, readString, bundle);
                    reply.writeNoException();
                    return true;
                case MMAdView.TRANSITION_DOWN:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    a(com.google.android.gms.internal.fl.a.B(data.readStrongBinder()), data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case MMAdView.TRANSITION_RANDOM:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    a(com.google.android.gms.internal.fl.a.B(data.readStrongBinder()), data.readInt());
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    B = com.google.android.gms.internal.fl.a.B(data.readStrongBinder());
                    readInt = data.readInt();
                    readString = data.readString();
                    if (data.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    }
                    b(B, readInt, readString, bundle);
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    B = com.google.android.gms.internal.fl.a.B(data.readStrongBinder());
                    readInt = data.readInt();
                    readString = data.readString();
                    if (data.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    }
                    c(B, readInt, readString, bundle);
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    B = com.google.android.gms.internal.fl.a.B(data.readStrongBinder());
                    readInt = data.readInt();
                    readString = data.readString();
                    if (data.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    }
                    d(B, readInt, readString, bundle);
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    B = com.google.android.gms.internal.fl.a.B(data.readStrongBinder());
                    readInt = data.readInt();
                    readString = data.readString();
                    if (data.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    }
                    e(B, readInt, readString, bundle);
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    a(com.google.android.gms.internal.fl.a.B(data.readStrongBinder()), data.readInt(), data.readString(), data.readString(), data.createStringArray(), data.readString(), data.readStrongBinder(), data.readString(), data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    a(com.google.android.gms.internal.fl.a.B(data.readStrongBinder()), data.readInt(), data.readString(), data.readString(), data.createStringArray());
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_EXPAND:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    B = com.google.android.gms.internal.fl.a.B(data.readStrongBinder());
                    readInt = data.readInt();
                    readString = data.readString();
                    if (data.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    }
                    f(B, readInt, readString, bundle);
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_RESIZE:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    B = com.google.android.gms.internal.fl.a.B(data.readStrongBinder());
                    readInt = data.readInt();
                    readString = data.readString();
                    if (data.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    }
                    g(B, readInt, readString, bundle);
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_CLOSE:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    B = com.google.android.gms.internal.fl.a.B(data.readStrongBinder());
                    readInt = data.readInt();
                    readString = data.readString();
                    if (data.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    }
                    h(B, readInt, readString, bundle);
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_IS_VIEWABLE:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    B = com.google.android.gms.internal.fl.a.B(data.readStrongBinder());
                    readInt = data.readInt();
                    readString = data.readString();
                    if (data.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    }
                    i(B, readInt, readString, bundle);
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_GET_PLACEMENT_TYPE:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    B = com.google.android.gms.internal.fl.a.B(data.readStrongBinder());
                    readInt = data.readInt();
                    readString = data.readString();
                    if (data.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    }
                    j(B, readInt, readString, bundle);
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_GET_ORIENTATION:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    B = com.google.android.gms.internal.fl.a.B(data.readStrongBinder());
                    readInt = data.readInt();
                    readString = data.readString();
                    if (data.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    }
                    k(B, readInt, readString, bundle);
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_GET_SCREEN_SIZE:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    B = com.google.android.gms.internal.fl.a.B(data.readStrongBinder());
                    readInt = data.readInt();
                    readString = data.readString();
                    if (data.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    }
                    l(B, readInt, readString, bundle);
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_GET_CURRENT_POSITION:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    B = com.google.android.gms.internal.fl.a.B(data.readStrongBinder());
                    readInt = data.readInt();
                    readString = data.readString();
                    if (data.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    }
                    m(B, readInt, readString, bundle);
                    reply.writeNoException();
                    return true;
                case Encoder.LINE_GROUPS:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    a(com.google.android.gms.internal.fl.a.B(data.readStrongBinder()), data.readInt(), data.readString(), data.readStrongBinder(), data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_GET_MAX_SIZE:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    a(com.google.android.gms.internal.fl.a.B(data.readStrongBinder()), data.readInt(), data.readString(), data.createStringArray(), data.readString(), data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_POST_TO_SOCIAL:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    b(com.google.android.gms.internal.fl.a.B(data.readStrongBinder()), data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_SUPPORTS:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    c(com.google.android.gms.internal.fl.a.B(data.readStrongBinder()), data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_STORE_PICTURE:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    B = com.google.android.gms.internal.fl.a.B(data.readStrongBinder());
                    readInt = data.readInt();
                    readString = data.readString();
                    if (data.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    }
                    n(B, readInt, readString, bundle);
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_TAKE_CAMERA_PICTURE:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    d(com.google.android.gms.internal.fl.a.B(data.readStrongBinder()), data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_GET_GALLERY_IMAGE:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    B = com.google.android.gms.internal.fl.a.B(data.readStrongBinder());
                    readInt = data.readInt();
                    readString = data.readString();
                    if (data.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    }
                    o(B, readInt, readString, bundle);
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_VIBRATE:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    e(com.google.android.gms.internal.fl.a.B(data.readStrongBinder()), data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_SEND_SMS:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    B = com.google.android.gms.internal.fl.a.B(data.readStrongBinder());
                    readInt = data.readInt();
                    readString = data.readString();
                    if (data.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    }
                    p(B, readInt, readString, bundle);
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_SEND_MAIL:
                    data.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                    b(com.google.android.gms.internal.fl.a.B(data.readStrongBinder()), data.readInt(), data.readString(), data.readString(), data.createStringArray());
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.common.internal.IGmsServiceBroker");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void a(fl flVar, int i) throws RemoteException;

    void a(fl flVar, int i, String str) throws RemoteException;

    void a(fl flVar, int i, String str, Bundle bundle) throws RemoteException;

    void a(fl flVar, int i, String str, IBinder iBinder, Bundle bundle) throws RemoteException;

    void a(fl flVar, int i, String str, String str2, String[] strArr) throws RemoteException;

    void a(fl flVar, int i, String str, String str2, String[] strArr, String str3, Bundle bundle) throws RemoteException;

    void a(fl flVar, int i, String str, String str2, String[] strArr, String str3, IBinder iBinder, String str4, Bundle bundle) throws RemoteException;

    void a(fl flVar, int i, String str, String[] strArr, String str2, Bundle bundle) throws RemoteException;

    void b(fl flVar, int i, String str) throws RemoteException;

    void b(fl flVar, int i, String str, Bundle bundle) throws RemoteException;

    void b(fl flVar, int i, String str, String str2, String[] strArr) throws RemoteException;

    void c(fl flVar, int i, String str) throws RemoteException;

    void c(fl flVar, int i, String str, Bundle bundle) throws RemoteException;

    void d(fl flVar, int i, String str) throws RemoteException;

    void d(fl flVar, int i, String str, Bundle bundle) throws RemoteException;

    void e(fl flVar, int i, String str) throws RemoteException;

    void e(fl flVar, int i, String str, Bundle bundle) throws RemoteException;

    void f(fl flVar, int i, String str, Bundle bundle) throws RemoteException;

    void g(fl flVar, int i, String str, Bundle bundle) throws RemoteException;

    void h(fl flVar, int i, String str, Bundle bundle) throws RemoteException;

    void i(fl flVar, int i, String str, Bundle bundle) throws RemoteException;

    void j(fl flVar, int i, String str, Bundle bundle) throws RemoteException;

    void k(fl flVar, int i, String str, Bundle bundle) throws RemoteException;

    void l(fl flVar, int i, String str, Bundle bundle) throws RemoteException;

    void m(fl flVar, int i, String str, Bundle bundle) throws RemoteException;

    void n(fl flVar, int i, String str, Bundle bundle) throws RemoteException;

    void o(fl flVar, int i, String str, Bundle bundle) throws RemoteException;

    void p(fl flVar, int i, String str, Bundle bundle) throws RemoteException;
}