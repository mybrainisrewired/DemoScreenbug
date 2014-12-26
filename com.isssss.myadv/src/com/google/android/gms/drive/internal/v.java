package com.google.android.gms.drive.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.common.api.Status;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public interface v extends IInterface {

    public static abstract class a extends Binder implements v {

        private static class a implements v {
            private IBinder kn;

            a(IBinder iBinder) {
                this.kn = iBinder;
            }

            public void a(OnContentsResponse onContentsResponse) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.drive.internal.IDriveServiceCallbacks");
                if (onContentsResponse != null) {
                    obtain.writeInt(1);
                    onContentsResponse.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(OnDownloadProgressResponse onDownloadProgressResponse) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.drive.internal.IDriveServiceCallbacks");
                if (onDownloadProgressResponse != null) {
                    obtain.writeInt(1);
                    onDownloadProgressResponse.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(1, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(OnDriveIdResponse onDriveIdResponse) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.drive.internal.IDriveServiceCallbacks");
                if (onDriveIdResponse != null) {
                    obtain.writeInt(1);
                    onDriveIdResponse.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(MMAdView.TRANSITION_DOWN, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(OnListEntriesResponse onListEntriesResponse) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.drive.internal.IDriveServiceCallbacks");
                if (onListEntriesResponse != null) {
                    obtain.writeInt(1);
                    onListEntriesResponse.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(MMAdView.TRANSITION_UP, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(OnListParentsResponse onListParentsResponse) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.drive.internal.IDriveServiceCallbacks");
                if (onListParentsResponse != null) {
                    obtain.writeInt(1);
                    onListParentsResponse.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(OnMetadataResponse onMetadataResponse) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.drive.internal.IDriveServiceCallbacks");
                if (onMetadataResponse != null) {
                    obtain.writeInt(1);
                    onMetadataResponse.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(MMAdView.TRANSITION_RANDOM, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(OnSyncMoreResponse onSyncMoreResponse) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.drive.internal.IDriveServiceCallbacks");
                if (onSyncMoreResponse != null) {
                    obtain.writeInt(1);
                    onSyncMoreResponse.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public IBinder asBinder() {
                return this.kn;
            }

            public void m(Status status) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.drive.internal.IDriveServiceCallbacks");
                if (status != null) {
                    obtain.writeInt(1);
                    status.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void onSuccess() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.drive.internal.IDriveServiceCallbacks");
                this.kn.transact(ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }
        }

        public a() {
            attachInterface(this, "com.google.android.gms.drive.internal.IDriveServiceCallbacks");
        }

        public static v H(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.drive.internal.IDriveServiceCallbacks");
            return (queryLocalInterface == null || !queryLocalInterface instanceof v) ? new a(iBinder) : (v) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            OnSyncMoreResponse onSyncMoreResponse = null;
            switch (code) {
                case MMAdView.TRANSITION_FADE:
                    OnDownloadProgressResponse onDownloadProgressResponse;
                    data.enforceInterface("com.google.android.gms.drive.internal.IDriveServiceCallbacks");
                    if (data.readInt() != 0) {
                        onDownloadProgressResponse = (OnDownloadProgressResponse) OnDownloadProgressResponse.CREATOR.createFromParcel(data);
                    }
                    a(onDownloadProgressResponse);
                    reply.writeNoException();
                    return true;
                case MMAdView.TRANSITION_UP:
                    OnListEntriesResponse onListEntriesResponse;
                    data.enforceInterface("com.google.android.gms.drive.internal.IDriveServiceCallbacks");
                    if (data.readInt() != 0) {
                        onListEntriesResponse = (OnListEntriesResponse) OnListEntriesResponse.CREATOR.createFromParcel(data);
                    }
                    a(onListEntriesResponse);
                    reply.writeNoException();
                    return true;
                case MMAdView.TRANSITION_DOWN:
                    OnDriveIdResponse onDriveIdResponse;
                    data.enforceInterface("com.google.android.gms.drive.internal.IDriveServiceCallbacks");
                    if (data.readInt() != 0) {
                        onDriveIdResponse = (OnDriveIdResponse) OnDriveIdResponse.CREATOR.createFromParcel(data);
                    }
                    a(onDriveIdResponse);
                    reply.writeNoException();
                    return true;
                case MMAdView.TRANSITION_RANDOM:
                    OnMetadataResponse onMetadataResponse;
                    data.enforceInterface("com.google.android.gms.drive.internal.IDriveServiceCallbacks");
                    if (data.readInt() != 0) {
                        onMetadataResponse = (OnMetadataResponse) OnMetadataResponse.CREATOR.createFromParcel(data);
                    }
                    a(onMetadataResponse);
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    OnContentsResponse onContentsResponse;
                    data.enforceInterface("com.google.android.gms.drive.internal.IDriveServiceCallbacks");
                    if (data.readInt() != 0) {
                        onContentsResponse = (OnContentsResponse) OnContentsResponse.CREATOR.createFromParcel(data);
                    }
                    a(onContentsResponse);
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    Status createFromParcel;
                    data.enforceInterface("com.google.android.gms.drive.internal.IDriveServiceCallbacks");
                    if (data.readInt() != 0) {
                        createFromParcel = Status.CREATOR.createFromParcel(data);
                    }
                    m(createFromParcel);
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.drive.internal.IDriveServiceCallbacks");
                    onSuccess();
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    OnListParentsResponse onListParentsResponse;
                    data.enforceInterface("com.google.android.gms.drive.internal.IDriveServiceCallbacks");
                    if (data.readInt() != 0) {
                        onListParentsResponse = (OnListParentsResponse) OnListParentsResponse.CREATOR.createFromParcel(data);
                    }
                    a(onListParentsResponse);
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.drive.internal.IDriveServiceCallbacks");
                    if (data.readInt() != 0) {
                        onSyncMoreResponse = (OnSyncMoreResponse) OnSyncMoreResponse.CREATOR.createFromParcel(data);
                    }
                    a(onSyncMoreResponse);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.drive.internal.IDriveServiceCallbacks");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void a(OnContentsResponse onContentsResponse) throws RemoteException;

    void a(OnDownloadProgressResponse onDownloadProgressResponse) throws RemoteException;

    void a(OnDriveIdResponse onDriveIdResponse) throws RemoteException;

    void a(OnListEntriesResponse onListEntriesResponse) throws RemoteException;

    void a(OnListParentsResponse onListParentsResponse) throws RemoteException;

    void a(OnMetadataResponse onMetadataResponse) throws RemoteException;

    void a(OnSyncMoreResponse onSyncMoreResponse) throws RemoteException;

    void m(Status status) throws RemoteException;

    void onSuccess() throws RemoteException;
}