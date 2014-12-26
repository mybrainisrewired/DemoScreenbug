package com.google.android.gms.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.millennialmedia.android.MMAdView;
import java.util.List;
import java.util.Map;

public interface eg extends IInterface {

    public static abstract class a extends Binder implements eg {

        private static class a implements eg {
            private IBinder kn;

            a(IBinder iBinder) {
                this.kn = iBinder;
            }

            public void a(Map map, long j, String str, List<ef> list) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.analytics.internal.IAnalyticsService");
                obtain.writeMap(map);
                obtain.writeLong(j);
                obtain.writeString(str);
                obtain.writeTypedList(list);
                this.kn.transact(1, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public IBinder asBinder() {
                return this.kn;
            }

            public void bR() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.analytics.internal.IAnalyticsService");
                this.kn.transact(MMAdView.TRANSITION_UP, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public String getVersion() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.analytics.internal.IAnalyticsService");
                this.kn.transact(MMAdView.TRANSITION_DOWN, obtain, obtain2, 0);
                obtain2.readException();
                String readString = obtain2.readString();
                obtain2.recycle();
                obtain.recycle();
                return readString;
            }
        }

        public static eg t(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.analytics.internal.IAnalyticsService");
            return (queryLocalInterface == null || !queryLocalInterface instanceof eg) ? new a(iBinder) : (eg) queryLocalInterface;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case MMAdView.TRANSITION_FADE:
                    data.enforceInterface("com.google.android.gms.analytics.internal.IAnalyticsService");
                    a(data.readHashMap(getClass().getClassLoader()), data.readLong(), data.readString(), data.createTypedArrayList(ef.CREATOR));
                    reply.writeNoException();
                    return true;
                case MMAdView.TRANSITION_UP:
                    data.enforceInterface("com.google.android.gms.analytics.internal.IAnalyticsService");
                    bR();
                    reply.writeNoException();
                    return true;
                case MMAdView.TRANSITION_DOWN:
                    data.enforceInterface("com.google.android.gms.analytics.internal.IAnalyticsService");
                    String version = getVersion();
                    reply.writeNoException();
                    reply.writeString(version);
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.analytics.internal.IAnalyticsService");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void a(Map map, long j, String str, List<ef> list) throws RemoteException;

    void bR() throws RemoteException;

    String getVersion() throws RemoteException;
}