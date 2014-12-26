package com.google.android.gms.drive.internal;

import android.content.IntentSender;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public interface u extends IInterface {

    public static abstract class a extends Binder implements u {

        private static class a implements u {
            private IBinder kn;

            a(IBinder iBinder) {
                this.kn = iBinder;
            }

            public IntentSender a(CreateFileIntentSenderRequest createFileIntentSenderRequest) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.drive.internal.IDriveService");
                if (createFileIntentSenderRequest != null) {
                    obtain.writeInt(1);
                    createFileIntentSenderRequest.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(ApiEventType.API_MRAID_EXPAND, obtain, obtain2, 0);
                obtain2.readException();
                IntentSender intentSender = obtain2.readInt() != 0 ? (IntentSender) IntentSender.CREATOR.createFromParcel(obtain2) : null;
                obtain2.recycle();
                obtain.recycle();
                return intentSender;
            }

            public IntentSender a(OpenFileIntentSenderRequest openFileIntentSenderRequest) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.drive.internal.IDriveService");
                if (openFileIntentSenderRequest != null) {
                    obtain.writeInt(1);
                    openFileIntentSenderRequest.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(ApiEventType.API_MRAID_USE_CUSTOM_CLOSE, obtain, obtain2, 0);
                obtain2.readException();
                IntentSender intentSender = obtain2.readInt() != 0 ? (IntentSender) IntentSender.CREATOR.createFromParcel(obtain2) : null;
                obtain2.recycle();
                obtain.recycle();
                return intentSender;
            }

            public void a(AddEventListenerRequest addEventListenerRequest, w wVar, String str, v vVar) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.drive.internal.IDriveService");
                if (addEventListenerRequest != null) {
                    obtain.writeInt(1);
                    addEventListenerRequest.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                obtain.writeStrongBinder(wVar != null ? wVar.asBinder() : null);
                obtain.writeString(str);
                if (vVar != null) {
                    iBinder = vVar.asBinder();
                }
                obtain.writeStrongBinder(iBinder);
                this.kn.transact(ApiEventType.API_MRAID_IS_VIEWABLE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(AuthorizeAccessRequest authorizeAccessRequest, v vVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.drive.internal.IDriveService");
                if (authorizeAccessRequest != null) {
                    obtain.writeInt(1);
                    authorizeAccessRequest.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                obtain.writeStrongBinder(vVar != null ? vVar.asBinder() : null);
                this.kn.transact(ApiEventType.API_MRAID_RESIZE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(CloseContentsAndUpdateMetadataRequest closeContentsAndUpdateMetadataRequest, v vVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.drive.internal.IDriveService");
                if (closeContentsAndUpdateMetadataRequest != null) {
                    obtain.writeInt(1);
                    closeContentsAndUpdateMetadataRequest.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                obtain.writeStrongBinder(vVar != null ? vVar.asBinder() : null);
                this.kn.transact(ApiEventType.API_MRAID_GET_CURRENT_POSITION, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(CloseContentsRequest closeContentsRequest, v vVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.drive.internal.IDriveService");
                if (closeContentsRequest != null) {
                    obtain.writeInt(1);
                    closeContentsRequest.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                obtain.writeStrongBinder(vVar != null ? vVar.asBinder() : null);
                this.kn.transact(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(CreateContentsRequest createContentsRequest, v vVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.drive.internal.IDriveService");
                if (createContentsRequest != null) {
                    obtain.writeInt(1);
                    createContentsRequest.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                obtain.writeStrongBinder(vVar != null ? vVar.asBinder() : null);
                this.kn.transact(MMAdView.TRANSITION_RANDOM, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(CreateFileRequest createFileRequest, v vVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.drive.internal.IDriveService");
                if (createFileRequest != null) {
                    obtain.writeInt(1);
                    createFileRequest.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                obtain.writeStrongBinder(vVar != null ? vVar.asBinder() : null);
                this.kn.transact(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(CreateFolderRequest createFolderRequest, v vVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.drive.internal.IDriveService");
                if (createFolderRequest != null) {
                    obtain.writeInt(1);
                    createFolderRequest.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                obtain.writeStrongBinder(vVar != null ? vVar.asBinder() : null);
                this.kn.transact(ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(DisconnectRequest disconnectRequest) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.drive.internal.IDriveService");
                if (disconnectRequest != null) {
                    obtain.writeInt(1);
                    disconnectRequest.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(ApiEventType.API_MRAID_GET_ORIENTATION, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(GetMetadataRequest getMetadataRequest, v vVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.drive.internal.IDriveService");
                if (getMetadataRequest != null) {
                    obtain.writeInt(1);
                    getMetadataRequest.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                obtain.writeStrongBinder(vVar != null ? vVar.asBinder() : null);
                this.kn.transact(1, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(ListParentsRequest listParentsRequest, v vVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.drive.internal.IDriveService");
                if (listParentsRequest != null) {
                    obtain.writeInt(1);
                    listParentsRequest.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                obtain.writeStrongBinder(vVar != null ? vVar.asBinder() : null);
                this.kn.transact(ApiEventType.API_MRAID_CLOSE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(OpenContentsRequest openContentsRequest, v vVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.drive.internal.IDriveService");
                if (openContentsRequest != null) {
                    obtain.writeInt(1);
                    openContentsRequest.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                obtain.writeStrongBinder(vVar != null ? vVar.asBinder() : null);
                this.kn.transact(ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(QueryRequest queryRequest, v vVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.drive.internal.IDriveService");
                if (queryRequest != null) {
                    obtain.writeInt(1);
                    queryRequest.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                obtain.writeStrongBinder(vVar != null ? vVar.asBinder() : null);
                this.kn.transact(MMAdView.TRANSITION_UP, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(RemoveEventListenerRequest removeEventListenerRequest, w wVar, String str, v vVar) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.drive.internal.IDriveService");
                if (removeEventListenerRequest != null) {
                    obtain.writeInt(1);
                    removeEventListenerRequest.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                obtain.writeStrongBinder(wVar != null ? wVar.asBinder() : null);
                obtain.writeString(str);
                if (vVar != null) {
                    iBinder = vVar.asBinder();
                }
                obtain.writeStrongBinder(iBinder);
                this.kn.transact(ApiEventType.API_MRAID_GET_PLACEMENT_TYPE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(TrashResourceRequest trashResourceRequest, v vVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.drive.internal.IDriveService");
                if (trashResourceRequest != null) {
                    obtain.writeInt(1);
                    trashResourceRequest.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                obtain.writeStrongBinder(vVar != null ? vVar.asBinder() : null);
                this.kn.transact(ApiEventType.API_MRAID_GET_SCREEN_SIZE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(UpdateMetadataRequest updateMetadataRequest, v vVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.drive.internal.IDriveService");
                if (updateMetadataRequest != null) {
                    obtain.writeInt(1);
                    updateMetadataRequest.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                obtain.writeStrongBinder(vVar != null ? vVar.asBinder() : null);
                this.kn.transact(MMAdView.TRANSITION_DOWN, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(v vVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.drive.internal.IDriveService");
                obtain.writeStrongBinder(vVar != null ? vVar.asBinder() : null);
                this.kn.transact(ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public IBinder asBinder() {
                return this.kn;
            }

            public void b(QueryRequest queryRequest, v vVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.drive.internal.IDriveService");
                if (queryRequest != null) {
                    obtain.writeInt(1);
                    queryRequest.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                obtain.writeStrongBinder(vVar != null ? vVar.asBinder() : null);
                this.kn.transact(Encoder.LINE_GROUPS, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }
        }

        public static u G(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.drive.internal.IDriveService");
            return (queryLocalInterface == null || !queryLocalInterface instanceof u) ? new a(iBinder) : (u) queryLocalInterface;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            QueryRequest queryRequest = null;
            IntentSender a;
            switch (code) {
                case MMAdView.TRANSITION_FADE:
                    GetMetadataRequest getMetadataRequest;
                    data.enforceInterface("com.google.android.gms.drive.internal.IDriveService");
                    if (data.readInt() != 0) {
                        getMetadataRequest = (GetMetadataRequest) GetMetadataRequest.CREATOR.createFromParcel(data);
                    }
                    a(getMetadataRequest, com.google.android.gms.drive.internal.v.a.H(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case MMAdView.TRANSITION_UP:
                    data.enforceInterface("com.google.android.gms.drive.internal.IDriveService");
                    if (data.readInt() != 0) {
                        queryRequest = (QueryRequest) QueryRequest.CREATOR.createFromParcel(data);
                    }
                    a(queryRequest, com.google.android.gms.drive.internal.v.a.H(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case MMAdView.TRANSITION_DOWN:
                    UpdateMetadataRequest updateMetadataRequest;
                    data.enforceInterface("com.google.android.gms.drive.internal.IDriveService");
                    if (data.readInt() != 0) {
                        updateMetadataRequest = (UpdateMetadataRequest) UpdateMetadataRequest.CREATOR.createFromParcel(data);
                    }
                    a(updateMetadataRequest, com.google.android.gms.drive.internal.v.a.H(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case MMAdView.TRANSITION_RANDOM:
                    CreateContentsRequest createContentsRequest;
                    data.enforceInterface("com.google.android.gms.drive.internal.IDriveService");
                    if (data.readInt() != 0) {
                        createContentsRequest = (CreateContentsRequest) CreateContentsRequest.CREATOR.createFromParcel(data);
                    }
                    a(createContentsRequest, com.google.android.gms.drive.internal.v.a.H(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    CreateFileRequest createFileRequest;
                    data.enforceInterface("com.google.android.gms.drive.internal.IDriveService");
                    if (data.readInt() != 0) {
                        createFileRequest = (CreateFileRequest) CreateFileRequest.CREATOR.createFromParcel(data);
                    }
                    a(createFileRequest, com.google.android.gms.drive.internal.v.a.H(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    CreateFolderRequest createFolderRequest;
                    data.enforceInterface("com.google.android.gms.drive.internal.IDriveService");
                    if (data.readInt() != 0) {
                        createFolderRequest = (CreateFolderRequest) CreateFolderRequest.CREATOR.createFromParcel(data);
                    }
                    a(createFolderRequest, com.google.android.gms.drive.internal.v.a.H(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    OpenContentsRequest openContentsRequest;
                    data.enforceInterface("com.google.android.gms.drive.internal.IDriveService");
                    if (data.readInt() != 0) {
                        openContentsRequest = (OpenContentsRequest) OpenContentsRequest.CREATOR.createFromParcel(data);
                    }
                    a(openContentsRequest, com.google.android.gms.drive.internal.v.a.H(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    CloseContentsRequest closeContentsRequest;
                    data.enforceInterface("com.google.android.gms.drive.internal.IDriveService");
                    if (data.readInt() != 0) {
                        closeContentsRequest = (CloseContentsRequest) CloseContentsRequest.CREATOR.createFromParcel(data);
                    }
                    a(closeContentsRequest, com.google.android.gms.drive.internal.v.a.H(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                    data.enforceInterface("com.google.android.gms.drive.internal.IDriveService");
                    a(com.google.android.gms.drive.internal.v.a.H(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                    OpenFileIntentSenderRequest openFileIntentSenderRequest;
                    data.enforceInterface("com.google.android.gms.drive.internal.IDriveService");
                    if (data.readInt() != 0) {
                        openFileIntentSenderRequest = (OpenFileIntentSenderRequest) OpenFileIntentSenderRequest.CREATOR.createFromParcel(data);
                    }
                    a = a(openFileIntentSenderRequest);
                    reply.writeNoException();
                    if (a != null) {
                        reply.writeInt(1);
                        a.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case ApiEventType.API_MRAID_EXPAND:
                    CreateFileIntentSenderRequest createFileIntentSenderRequest;
                    data.enforceInterface("com.google.android.gms.drive.internal.IDriveService");
                    if (data.readInt() != 0) {
                        createFileIntentSenderRequest = (CreateFileIntentSenderRequest) CreateFileIntentSenderRequest.CREATOR.createFromParcel(data);
                    }
                    a = a(createFileIntentSenderRequest);
                    reply.writeNoException();
                    if (a != null) {
                        reply.writeInt(1);
                        a.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case ApiEventType.API_MRAID_RESIZE:
                    AuthorizeAccessRequest authorizeAccessRequest;
                    data.enforceInterface("com.google.android.gms.drive.internal.IDriveService");
                    if (data.readInt() != 0) {
                        authorizeAccessRequest = (AuthorizeAccessRequest) AuthorizeAccessRequest.CREATOR.createFromParcel(data);
                    }
                    a(authorizeAccessRequest, com.google.android.gms.drive.internal.v.a.H(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_CLOSE:
                    ListParentsRequest listParentsRequest;
                    data.enforceInterface("com.google.android.gms.drive.internal.IDriveService");
                    if (data.readInt() != 0) {
                        listParentsRequest = (ListParentsRequest) ListParentsRequest.CREATOR.createFromParcel(data);
                    }
                    a(listParentsRequest, com.google.android.gms.drive.internal.v.a.H(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_IS_VIEWABLE:
                    AddEventListenerRequest addEventListenerRequest;
                    data.enforceInterface("com.google.android.gms.drive.internal.IDriveService");
                    if (data.readInt() != 0) {
                        addEventListenerRequest = (AddEventListenerRequest) AddEventListenerRequest.CREATOR.createFromParcel(data);
                    }
                    a(addEventListenerRequest, com.google.android.gms.drive.internal.w.a.I(data.readStrongBinder()), data.readString(), com.google.android.gms.drive.internal.v.a.H(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_GET_PLACEMENT_TYPE:
                    RemoveEventListenerRequest removeEventListenerRequest;
                    data.enforceInterface("com.google.android.gms.drive.internal.IDriveService");
                    if (data.readInt() != 0) {
                        removeEventListenerRequest = (RemoveEventListenerRequest) RemoveEventListenerRequest.CREATOR.createFromParcel(data);
                    }
                    a(removeEventListenerRequest, com.google.android.gms.drive.internal.w.a.I(data.readStrongBinder()), data.readString(), com.google.android.gms.drive.internal.v.a.H(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_GET_ORIENTATION:
                    DisconnectRequest disconnectRequest;
                    data.enforceInterface("com.google.android.gms.drive.internal.IDriveService");
                    if (data.readInt() != 0) {
                        disconnectRequest = (DisconnectRequest) DisconnectRequest.CREATOR.createFromParcel(data);
                    }
                    a(disconnectRequest);
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_GET_SCREEN_SIZE:
                    TrashResourceRequest trashResourceRequest;
                    data.enforceInterface("com.google.android.gms.drive.internal.IDriveService");
                    if (data.readInt() != 0) {
                        trashResourceRequest = (TrashResourceRequest) TrashResourceRequest.CREATOR.createFromParcel(data);
                    }
                    a(trashResourceRequest, com.google.android.gms.drive.internal.v.a.H(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case ApiEventType.API_MRAID_GET_CURRENT_POSITION:
                    CloseContentsAndUpdateMetadataRequest closeContentsAndUpdateMetadataRequest;
                    data.enforceInterface("com.google.android.gms.drive.internal.IDriveService");
                    if (data.readInt() != 0) {
                        closeContentsAndUpdateMetadataRequest = (CloseContentsAndUpdateMetadataRequest) CloseContentsAndUpdateMetadataRequest.CREATOR.createFromParcel(data);
                    }
                    a(closeContentsAndUpdateMetadataRequest, com.google.android.gms.drive.internal.v.a.H(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case Encoder.LINE_GROUPS:
                    data.enforceInterface("com.google.android.gms.drive.internal.IDriveService");
                    if (data.readInt() != 0) {
                        queryRequest = (QueryRequest) QueryRequest.CREATOR.createFromParcel(data);
                    }
                    b(queryRequest, com.google.android.gms.drive.internal.v.a.H(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.drive.internal.IDriveService");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    IntentSender a(CreateFileIntentSenderRequest createFileIntentSenderRequest) throws RemoteException;

    IntentSender a(OpenFileIntentSenderRequest openFileIntentSenderRequest) throws RemoteException;

    void a(AddEventListenerRequest addEventListenerRequest, w wVar, String str, v vVar) throws RemoteException;

    void a(AuthorizeAccessRequest authorizeAccessRequest, v vVar) throws RemoteException;

    void a(CloseContentsAndUpdateMetadataRequest closeContentsAndUpdateMetadataRequest, v vVar) throws RemoteException;

    void a(CloseContentsRequest closeContentsRequest, v vVar) throws RemoteException;

    void a(CreateContentsRequest createContentsRequest, v vVar) throws RemoteException;

    void a(CreateFileRequest createFileRequest, v vVar) throws RemoteException;

    void a(CreateFolderRequest createFolderRequest, v vVar) throws RemoteException;

    void a(DisconnectRequest disconnectRequest) throws RemoteException;

    void a(GetMetadataRequest getMetadataRequest, v vVar) throws RemoteException;

    void a(ListParentsRequest listParentsRequest, v vVar) throws RemoteException;

    void a(OpenContentsRequest openContentsRequest, v vVar) throws RemoteException;

    void a(QueryRequest queryRequest, v vVar) throws RemoteException;

    void a(RemoveEventListenerRequest removeEventListenerRequest, w wVar, String str, v vVar) throws RemoteException;

    void a(TrashResourceRequest trashResourceRequest, v vVar) throws RemoteException;

    void a(UpdateMetadataRequest updateMetadataRequest, v vVar) throws RemoteException;

    void a(v vVar) throws RemoteException;

    void b(QueryRequest queryRequest, v vVar) throws RemoteException;
}