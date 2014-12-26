package com.google.android.gms.games.internal;

import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.internal.multiplayer.ZInvitationCluster;
import com.google.android.gms.games.internal.request.GameRequestCluster;
import com.google.android.gms.games.multiplayer.ParticipantEntity;
import com.google.android.gms.games.multiplayer.ParticipantResult;
import com.google.android.gms.games.multiplayer.realtime.RoomEntity;
import com.inmobi.commons.analytics.iat.impl.AdTrackerConstants;

public interface IGamesService extends IInterface {

    public static abstract class Stub extends Binder implements IGamesService {

        private static class Proxy implements IGamesService {
            private IBinder kn;

            Proxy(IBinder remote) {
                this.kn = remote;
            }

            public void A(boolean z) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                if (z) {
                    i = 1;
                }
                obtain.writeInt(i);
                this.kn.transact(5068, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public int a(IGamesCallbacks iGamesCallbacks, byte[] bArr, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeByteArray(bArr);
                obtain.writeString(str);
                obtain.writeString(str2);
                this.kn.transact(5033, obtain, obtain2, 0);
                obtain2.readException();
                int readInt = obtain2.readInt();
                obtain2.recycle();
                obtain.recycle();
                return readInt;
            }

            public Intent a(int i, int i2, boolean z) throws RemoteException {
                int i3 = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeInt(i);
                obtain.writeInt(i2);
                if (z) {
                    i3 = 1;
                }
                obtain.writeInt(i3);
                this.kn.transact(9008, obtain, obtain2, 0);
                obtain2.readException();
                Intent intent = obtain2.readInt() != 0 ? (Intent) Intent.CREATOR.createFromParcel(obtain2) : null;
                obtain2.recycle();
                obtain.recycle();
                return intent;
            }

            public Intent a(int i, byte[] bArr, int i2, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeInt(i);
                obtain.writeByteArray(bArr);
                obtain.writeInt(i2);
                obtain.writeString(str);
                this.kn.transact(10012, obtain, obtain2, 0);
                obtain2.readException();
                Intent intent = obtain2.readInt() != 0 ? (Intent) Intent.CREATOR.createFromParcel(obtain2) : null;
                obtain2.recycle();
                obtain.recycle();
                return intent;
            }

            public Intent a(ZInvitationCluster zInvitationCluster, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                if (zInvitationCluster != null) {
                    obtain.writeInt(1);
                    zInvitationCluster.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                obtain.writeString(str);
                obtain.writeString(str2);
                this.kn.transact(10021, obtain, obtain2, 0);
                obtain2.readException();
                Intent intent = obtain2.readInt() != 0 ? (Intent) Intent.CREATOR.createFromParcel(obtain2) : null;
                obtain2.recycle();
                obtain.recycle();
                return intent;
            }

            public Intent a(GameRequestCluster gameRequestCluster, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                if (gameRequestCluster != null) {
                    obtain.writeInt(1);
                    gameRequestCluster.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                obtain.writeString(str);
                this.kn.transact(10022, obtain, obtain2, 0);
                obtain2.readException();
                Intent intent = obtain2.readInt() != 0 ? (Intent) Intent.CREATOR.createFromParcel(obtain2) : null;
                obtain2.recycle();
                obtain.recycle();
                return intent;
            }

            public Intent a(RoomEntity roomEntity, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                if (roomEntity != null) {
                    obtain.writeInt(1);
                    roomEntity.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                obtain.writeInt(i);
                this.kn.transact(9011, obtain, obtain2, 0);
                obtain2.readException();
                Intent intent = obtain2.readInt() != 0 ? (Intent) Intent.CREATOR.createFromParcel(obtain2) : null;
                obtain2.recycle();
                obtain.recycle();
                return intent;
            }

            public Intent a(ParticipantEntity[] participantEntityArr, String str, String str2, Uri uri, Uri uri2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeTypedArray(participantEntityArr, 0);
                obtain.writeString(str);
                obtain.writeString(str2);
                if (uri != null) {
                    obtain.writeInt(1);
                    uri.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                if (uri2 != null) {
                    obtain.writeInt(1);
                    uri2.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(9031, obtain, obtain2, 0);
                obtain2.readException();
                Intent intent = obtain2.readInt() != 0 ? (Intent) Intent.CREATOR.createFromParcel(obtain2) : null;
                obtain2.recycle();
                obtain.recycle();
                return intent;
            }

            public void a(long j, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeLong(j);
                obtain.writeString(str);
                this.kn.transact(8019, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(IBinder iBinder, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iBinder);
                if (bundle != null) {
                    obtain.writeInt(1);
                    bundle.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(AdTrackerConstants.WEBVIEW_INVALIDJSON, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(IGamesCallbacks iGamesCallbacks) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                this.kn.transact(AdTrackerConstants.WEBVIEW_XMLHTTPSUPPORT, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(IGamesCallbacks iGamesCallbacks, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeInt(i);
                this.kn.transact(10016, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(IGamesCallbacks iGamesCallbacks, int i, int i2, int i3) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeInt(i);
                obtain.writeInt(i2);
                obtain.writeInt(i3);
                this.kn.transact(10009, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(IGamesCallbacks iGamesCallbacks, int i, int i2, boolean z, boolean z2) throws RemoteException {
                int i3 = 1;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeInt(i);
                obtain.writeInt(i2);
                obtain.writeInt(z ? 1 : 0);
                if (!z2) {
                    i3 = 0;
                }
                obtain.writeInt(i3);
                this.kn.transact(5044, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(IGamesCallbacks iGamesCallbacks, int i, int i2, String[] strArr, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeInt(i);
                obtain.writeInt(i2);
                obtain.writeStringArray(strArr);
                if (bundle != null) {
                    obtain.writeInt(1);
                    bundle.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(8004, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(IGamesCallbacks iGamesCallbacks, int i, boolean z, boolean z2) throws RemoteException {
                int i2 = 1;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeInt(i);
                obtain.writeInt(z ? 1 : 0);
                if (!z2) {
                    i2 = 0;
                }
                obtain.writeInt(i2);
                this.kn.transact(5015, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(IGamesCallbacks iGamesCallbacks, int i, int[] iArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeInt(i);
                obtain.writeIntArray(iArr);
                this.kn.transact(10018, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(IGamesCallbacks iGamesCallbacks, long j) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeLong(j);
                this.kn.transact(5058, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(IGamesCallbacks iGamesCallbacks, long j, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeLong(j);
                obtain.writeString(str);
                this.kn.transact(8018, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(IGamesCallbacks iGamesCallbacks, Bundle bundle, int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                if (bundle != null) {
                    obtain.writeInt(1);
                    bundle.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                obtain.writeInt(i);
                obtain.writeInt(i2);
                this.kn.transact(5021, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(IGamesCallbacks iGamesCallbacks, IBinder iBinder, int i, String[] strArr, Bundle bundle, boolean z, long j) throws RemoteException {
                int i2 = 1;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeStrongBinder(iBinder);
                obtain.writeInt(i);
                obtain.writeStringArray(strArr);
                if (bundle != null) {
                    obtain.writeInt(1);
                    bundle.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                if (!z) {
                    i2 = 0;
                }
                obtain.writeInt(i2);
                obtain.writeLong(j);
                this.kn.transact(5030, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(IGamesCallbacks iGamesCallbacks, IBinder iBinder, String str, boolean z, long j) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeStrongBinder(iBinder);
                obtain.writeString(str);
                if (z) {
                    i = 1;
                }
                obtain.writeInt(i);
                obtain.writeLong(j);
                this.kn.transact(5031, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(IGamesCallbacks iGamesCallbacks, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                this.kn.transact(5014, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(IGamesCallbacks iGamesCallbacks, String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                obtain.writeInt(i);
                this.kn.transact(10011, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(IGamesCallbacks iGamesCallbacks, String str, int i, int i2, int i3, boolean z) throws RemoteException {
                int i4 = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                obtain.writeInt(i);
                obtain.writeInt(i2);
                obtain.writeInt(i3);
                if (z) {
                    i4 = 1;
                }
                obtain.writeInt(i4);
                this.kn.transact(5019, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(IGamesCallbacks iGamesCallbacks, String str, int i, IBinder iBinder, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                obtain.writeInt(i);
                obtain.writeStrongBinder(iBinder);
                if (bundle != null) {
                    obtain.writeInt(1);
                    bundle.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(5025, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(IGamesCallbacks iGamesCallbacks, String str, int i, boolean z) throws RemoteException {
                int i2 = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                obtain.writeInt(i);
                if (z) {
                    i2 = 1;
                }
                obtain.writeInt(i2);
                this.kn.transact(8023, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(IGamesCallbacks iGamesCallbacks, String str, int i, boolean z, boolean z2) throws RemoteException {
                int i2 = 1;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                obtain.writeInt(i);
                obtain.writeInt(z ? 1 : 0);
                if (!z2) {
                    i2 = 0;
                }
                obtain.writeInt(i2);
                this.kn.transact(5045, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(IGamesCallbacks iGamesCallbacks, String str, int i, boolean z, boolean z2, boolean z3, boolean z4) throws RemoteException {
                int i2 = 1;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                obtain.writeInt(i);
                obtain.writeInt(z ? 1 : 0);
                obtain.writeInt(z2 ? 1 : 0);
                obtain.writeInt(z3 ? 1 : 0);
                if (!z4) {
                    i2 = 0;
                }
                obtain.writeInt(i2);
                this.kn.transact(GamesStatusCodes.STATUS_MATCH_ERROR_INACTIVE_MATCH, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(IGamesCallbacks iGamesCallbacks, String str, int i, int[] iArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                obtain.writeInt(i);
                obtain.writeIntArray(iArr);
                this.kn.transact(10019, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(IGamesCallbacks iGamesCallbacks, String str, long j) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                obtain.writeLong(j);
                this.kn.transact(5016, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(IGamesCallbacks iGamesCallbacks, String str, long j, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                obtain.writeLong(j);
                obtain.writeString(str2);
                this.kn.transact(GamesStatusCodes.STATUS_INVALID_REAL_TIME_ROOM_ID, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(IGamesCallbacks iGamesCallbacks, String str, IBinder iBinder, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                obtain.writeStrongBinder(iBinder);
                if (bundle != null) {
                    obtain.writeInt(1);
                    bundle.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(5023, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(IGamesCallbacks iGamesCallbacks, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                obtain.writeString(str2);
                this.kn.transact(5038, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(IGamesCallbacks iGamesCallbacks, String str, String str2, int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                obtain.writeString(str2);
                obtain.writeInt(i);
                obtain.writeInt(i2);
                this.kn.transact(8001, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(IGamesCallbacks iGamesCallbacks, String str, String str2, int i, int i2, int i3) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                obtain.writeString(str2);
                obtain.writeInt(i);
                obtain.writeInt(i2);
                obtain.writeInt(i3);
                this.kn.transact(10010, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(IGamesCallbacks iGamesCallbacks, String str, String str2, int i, int i2, int i3, boolean z) throws RemoteException {
                int i4 = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                obtain.writeString(str2);
                obtain.writeInt(i);
                obtain.writeInt(i2);
                obtain.writeInt(i3);
                if (z) {
                    i4 = 1;
                }
                obtain.writeInt(i4);
                this.kn.transact(5039, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(IGamesCallbacks iGamesCallbacks, String str, String str2, int i, boolean z, boolean z2) throws RemoteException {
                int i2 = 1;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                obtain.writeString(str2);
                obtain.writeInt(i);
                obtain.writeInt(z ? 1 : 0);
                if (!z2) {
                    i2 = 0;
                }
                obtain.writeInt(i2);
                this.kn.transact(9028, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(IGamesCallbacks iGamesCallbacks, String str, String str2, boolean z) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                obtain.writeString(str2);
                if (z) {
                    i = 1;
                }
                obtain.writeInt(i);
                this.kn.transact(AdTrackerConstants.SERVER_BADREQUEST, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(IGamesCallbacks iGamesCallbacks, String str, String str2, String[] strArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                obtain.writeString(str2);
                obtain.writeStringArray(strArr);
                this.kn.transact(GamesActivityResultCodes.RESULT_INVALID_ROOM, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(IGamesCallbacks iGamesCallbacks, String str, boolean z) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                if (z) {
                    i = 1;
                }
                obtain.writeInt(i);
                this.kn.transact(5054, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(IGamesCallbacks iGamesCallbacks, String str, byte[] bArr, String str2, ParticipantResult[] participantResultArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                obtain.writeByteArray(bArr);
                obtain.writeString(str2);
                obtain.writeTypedArray(participantResultArr, 0);
                this.kn.transact(8007, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(IGamesCallbacks iGamesCallbacks, String str, byte[] bArr, ParticipantResult[] participantResultArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                obtain.writeByteArray(bArr);
                obtain.writeTypedArray(participantResultArr, 0);
                this.kn.transact(8008, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(IGamesCallbacks iGamesCallbacks, String str, int[] iArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                obtain.writeIntArray(iArr);
                this.kn.transact(8017, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(IGamesCallbacks iGamesCallbacks, String str, String[] strArr, int i, byte[] bArr, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                obtain.writeStringArray(strArr);
                obtain.writeInt(i);
                obtain.writeByteArray(bArr);
                obtain.writeInt(i2);
                this.kn.transact(GamesActivityResultCodes.RESULT_LEFT_ROOM, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(IGamesCallbacks iGamesCallbacks, boolean z) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                if (z) {
                    i = 1;
                }
                obtain.writeInt(i);
                this.kn.transact(AdTrackerConstants.SERVER_RELOAD_WEBVIEW, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(IGamesCallbacks iGamesCallbacks, boolean z, Bundle bundle) throws RemoteException {
                int i = 1;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                if (!z) {
                    i = 0;
                }
                obtain.writeInt(i);
                if (bundle != null) {
                    obtain.writeInt(1);
                    bundle.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(5063, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(IGamesCallbacks iGamesCallbacks, int[] iArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeIntArray(iArr);
                this.kn.transact(8003, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void a(IGamesCallbacks iGamesCallbacks, String[] strArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeStringArray(strArr);
                this.kn.transact(GamesActivityResultCodes.RESULT_NETWORK_FAILURE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public Intent aA(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeString(str);
                this.kn.transact(9004, obtain, obtain2, 0);
                obtain2.readException();
                Intent intent = obtain2.readInt() != 0 ? (Intent) Intent.CREATOR.createFromParcel(obtain2) : null;
                obtain2.recycle();
                obtain.recycle();
                return intent;
            }

            public String aD(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeString(str);
                this.kn.transact(5064, obtain, obtain2, 0);
                obtain2.readException();
                String readString = obtain2.readString();
                obtain2.recycle();
                obtain.recycle();
                return readString;
            }

            public String aE(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeString(str);
                this.kn.transact(5035, obtain, obtain2, 0);
                obtain2.readException();
                String readString = obtain2.readString();
                obtain2.recycle();
                obtain.recycle();
                return readString;
            }

            public void aF(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeString(str);
                this.kn.transact(5050, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public int aG(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeString(str);
                this.kn.transact(5060, obtain, obtain2, 0);
                obtain2.readException();
                int readInt = obtain2.readInt();
                obtain2.recycle();
                obtain.recycle();
                return readInt;
            }

            public Uri aH(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeString(str);
                this.kn.transact(5066, obtain, obtain2, 0);
                obtain2.readException();
                Uri uri = obtain2.readInt() != 0 ? (Uri) Uri.CREATOR.createFromParcel(obtain2) : null;
                obtain2.recycle();
                obtain.recycle();
                return uri;
            }

            public void aI(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeString(str);
                this.kn.transact(8002, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public ParcelFileDescriptor aJ(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeString(str);
                this.kn.transact(9030, obtain, obtain2, 0);
                obtain2.readException();
                ParcelFileDescriptor parcelFileDescriptor = obtain2.readInt() != 0 ? (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(obtain2) : null;
                obtain2.recycle();
                obtain.recycle();
                return parcelFileDescriptor;
            }

            public void aY(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeInt(i);
                this.kn.transact(5036, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public IBinder asBinder() {
                return this.kn;
            }

            public int b(byte[] bArr, String str, String[] strArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeByteArray(bArr);
                obtain.writeString(str);
                obtain.writeStringArray(strArr);
                this.kn.transact(5034, obtain, obtain2, 0);
                obtain2.readException();
                int readInt = obtain2.readInt();
                obtain2.recycle();
                obtain.recycle();
                return readInt;
            }

            public Intent b(int i, int i2, boolean z) throws RemoteException {
                int i3 = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeInt(i);
                obtain.writeInt(i2);
                if (z) {
                    i3 = 1;
                }
                obtain.writeInt(i3);
                this.kn.transact(9009, obtain, obtain2, 0);
                obtain2.readException();
                Intent intent = obtain2.readInt() != 0 ? (Intent) Intent.CREATOR.createFromParcel(obtain2) : null;
                obtain2.recycle();
                obtain.recycle();
                return intent;
            }

            public void b(long j, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeLong(j);
                obtain.writeString(str);
                this.kn.transact(8021, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void b(IGamesCallbacks iGamesCallbacks) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                this.kn.transact(5017, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void b(IGamesCallbacks iGamesCallbacks, int i, boolean z, boolean z2) throws RemoteException {
                int i2 = 1;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeInt(i);
                obtain.writeInt(z ? 1 : 0);
                if (!z2) {
                    i2 = 0;
                }
                obtain.writeInt(i2);
                this.kn.transact(5046, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void b(IGamesCallbacks iGamesCallbacks, long j) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeLong(j);
                this.kn.transact(8012, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void b(IGamesCallbacks iGamesCallbacks, long j, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeLong(j);
                obtain.writeString(str);
                this.kn.transact(8020, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void b(IGamesCallbacks iGamesCallbacks, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                this.kn.transact(5018, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void b(IGamesCallbacks iGamesCallbacks, String str, int i, int i2, int i3, boolean z) throws RemoteException {
                int i4 = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                obtain.writeInt(i);
                obtain.writeInt(i2);
                obtain.writeInt(i3);
                if (z) {
                    i4 = 1;
                }
                obtain.writeInt(i4);
                this.kn.transact(5020, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void b(IGamesCallbacks iGamesCallbacks, String str, int i, IBinder iBinder, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                obtain.writeInt(i);
                obtain.writeStrongBinder(iBinder);
                if (bundle != null) {
                    obtain.writeInt(1);
                    bundle.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(GamesStatusCodes.STATUS_PARTICIPANT_NOT_CONNECTED, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void b(IGamesCallbacks iGamesCallbacks, String str, int i, boolean z) throws RemoteException {
                int i2 = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                obtain.writeInt(i);
                if (z) {
                    i2 = 1;
                }
                obtain.writeInt(i2);
                this.kn.transact(10017, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void b(IGamesCallbacks iGamesCallbacks, String str, int i, boolean z, boolean z2) throws RemoteException {
                int i2 = 1;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                obtain.writeInt(i);
                obtain.writeInt(z ? 1 : 0);
                if (!z2) {
                    i2 = 0;
                }
                obtain.writeInt(i2);
                this.kn.transact(5501, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void b(IGamesCallbacks iGamesCallbacks, String str, IBinder iBinder, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                obtain.writeStrongBinder(iBinder);
                if (bundle != null) {
                    obtain.writeInt(1);
                    bundle.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(5024, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void b(IGamesCallbacks iGamesCallbacks, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                obtain.writeString(str2);
                this.kn.transact(5041, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void b(IGamesCallbacks iGamesCallbacks, String str, String str2, int i, int i2, int i3, boolean z) throws RemoteException {
                int i4 = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                obtain.writeString(str2);
                obtain.writeInt(i);
                obtain.writeInt(i2);
                obtain.writeInt(i3);
                if (z) {
                    i4 = 1;
                }
                obtain.writeInt(i4);
                this.kn.transact(5040, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void b(IGamesCallbacks iGamesCallbacks, String str, String str2, boolean z) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                obtain.writeString(str2);
                if (z) {
                    i = 1;
                }
                obtain.writeInt(i);
                this.kn.transact(GamesStatusCodes.STATUS_MATCH_NOT_FOUND, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void b(IGamesCallbacks iGamesCallbacks, String str, boolean z) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                if (z) {
                    i = 1;
                }
                obtain.writeInt(i);
                this.kn.transact(GamesStatusCodes.STATUS_MATCH_ERROR_INVALID_MATCH_STATE, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void b(IGamesCallbacks iGamesCallbacks, boolean z) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                if (z) {
                    i = 1;
                }
                obtain.writeInt(i);
                this.kn.transact(GamesStatusCodes.STATUS_MATCH_ERROR_OUT_OF_DATE_VERSION, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void b(IGamesCallbacks iGamesCallbacks, String[] strArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeStringArray(strArr);
                this.kn.transact(GamesActivityResultCodes.RESULT_SEND_REQUEST_FAILED, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void b(String str, String str2, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeString(str);
                obtain.writeString(str2);
                obtain.writeInt(i);
                this.kn.transact(5051, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void c(long j, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeLong(j);
                obtain.writeString(str);
                this.kn.transact(GamesActivityResultCodes.RESULT_APP_MISCONFIGURED, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void c(IGamesCallbacks iGamesCallbacks) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                this.kn.transact(5022, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void c(IGamesCallbacks iGamesCallbacks, int i, boolean z, boolean z2) throws RemoteException {
                int i2 = 1;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeInt(i);
                obtain.writeInt(z ? 1 : 0);
                if (!z2) {
                    i2 = 0;
                }
                obtain.writeInt(i2);
                this.kn.transact(5048, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void c(IGamesCallbacks iGamesCallbacks, long j) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeLong(j);
                this.kn.transact(GamesActivityResultCodes.RESULT_RECONNECT_REQUIRED, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void c(IGamesCallbacks iGamesCallbacks, long j, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeLong(j);
                obtain.writeString(str);
                this.kn.transact(GamesActivityResultCodes.RESULT_LICENSE_FAILED, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void c(IGamesCallbacks iGamesCallbacks, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                this.kn.transact(5032, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void c(IGamesCallbacks iGamesCallbacks, String str, int i, boolean z, boolean z2) throws RemoteException {
                int i2 = 1;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                obtain.writeInt(i);
                obtain.writeInt(z ? 1 : 0);
                if (!z2) {
                    i2 = 0;
                }
                obtain.writeInt(i2);
                this.kn.transact(9001, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void c(IGamesCallbacks iGamesCallbacks, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                obtain.writeString(str2);
                this.kn.transact(8011, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void c(IGamesCallbacks iGamesCallbacks, String str, boolean z) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                if (z) {
                    i = 1;
                }
                obtain.writeInt(i);
                this.kn.transact(GamesStatusCodes.STATUS_MATCH_ERROR_INVALID_MATCH_RESULTS, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void c(IGamesCallbacks iGamesCallbacks, boolean z) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                if (z) {
                    i = 1;
                }
                obtain.writeInt(i);
                this.kn.transact(8027, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void c(IGamesCallbacks iGamesCallbacks, String[] strArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeStringArray(strArr);
                this.kn.transact(10020, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void c(String str, String str2, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeString(str);
                obtain.writeString(str2);
                obtain.writeInt(i);
                this.kn.transact(8026, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void d(IGamesCallbacks iGamesCallbacks) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                this.kn.transact(5026, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void d(IGamesCallbacks iGamesCallbacks, int i, boolean z, boolean z2) throws RemoteException {
                int i2 = 1;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeInt(i);
                obtain.writeInt(z ? 1 : 0);
                if (!z2) {
                    i2 = 0;
                }
                obtain.writeInt(i2);
                this.kn.transact(AdTrackerConstants.SERVER_INVALIDAPPID, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void d(IGamesCallbacks iGamesCallbacks, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                this.kn.transact(5037, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void d(IGamesCallbacks iGamesCallbacks, String str, int i, boolean z, boolean z2) throws RemoteException {
                int i2 = 1;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                obtain.writeInt(i);
                obtain.writeInt(z ? 1 : 0);
                if (!z2) {
                    i2 = 0;
                }
                obtain.writeInt(i2);
                this.kn.transact(9020, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void d(IGamesCallbacks iGamesCallbacks, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                obtain.writeString(str2);
                this.kn.transact(8015, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void d(IGamesCallbacks iGamesCallbacks, String str, boolean z) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                if (z) {
                    i = 1;
                }
                obtain.writeInt(i);
                this.kn.transact(GamesStatusCodes.STATUS_MATCH_ERROR_ALREADY_REMATCHED, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public Bundle dG() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                this.kn.transact(AdTrackerConstants.WEBVIEW_SERVERERROR, obtain, obtain2, 0);
                obtain2.readException();
                Bundle bundle = obtain2.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(obtain2) : null;
                obtain2.recycle();
                obtain.recycle();
                return bundle;
            }

            public void e(IGamesCallbacks iGamesCallbacks) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                this.kn.transact(5027, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void e(IGamesCallbacks iGamesCallbacks, int i, boolean z, boolean z2) throws RemoteException {
                int i2 = 1;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeInt(i);
                obtain.writeInt(z ? 1 : 0);
                if (!z2) {
                    i2 = 0;
                }
                obtain.writeInt(i2);
                this.kn.transact(GamesStatusCodes.STATUS_MULTIPLAYER_ERROR_INVALID_OPERATION, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void e(IGamesCallbacks iGamesCallbacks, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                this.kn.transact(5042, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void e(IGamesCallbacks iGamesCallbacks, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                obtain.writeString(str2);
                this.kn.transact(8016, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public ParcelFileDescriptor f(Uri uri) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                if (uri != null) {
                    obtain.writeInt(1);
                    uri.writeToParcel(obtain, 0);
                } else {
                    obtain.writeInt(0);
                }
                this.kn.transact(GamesStatusCodes.STATUS_MATCH_ERROR_LOCALLY_MODIFIED, obtain, obtain2, 0);
                obtain2.readException();
                ParcelFileDescriptor parcelFileDescriptor = obtain2.readInt() != 0 ? (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(obtain2) : null;
                obtain2.recycle();
                obtain.recycle();
                return parcelFileDescriptor;
            }

            public void f(IGamesCallbacks iGamesCallbacks) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                this.kn.transact(5047, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void f(IGamesCallbacks iGamesCallbacks, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                this.kn.transact(5043, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void g(IGamesCallbacks iGamesCallbacks) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                this.kn.transact(5049, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void g(IGamesCallbacks iGamesCallbacks, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                this.kn.transact(5052, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public int gA() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                this.kn.transact(8024, obtain, obtain2, 0);
                obtain2.readException();
                int readInt = obtain2.readInt();
                obtain2.recycle();
                obtain.recycle();
                return readInt;
            }

            public Intent gB() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                this.kn.transact(10015, obtain, obtain2, 0);
                obtain2.readException();
                Intent intent = obtain2.readInt() != 0 ? (Intent) Intent.CREATOR.createFromParcel(obtain2) : null;
                obtain2.recycle();
                obtain.recycle();
                return intent;
            }

            public int gC() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                this.kn.transact(10013, obtain, obtain2, 0);
                obtain2.readException();
                int readInt = obtain2.readInt();
                obtain2.recycle();
                obtain.recycle();
                return readInt;
            }

            public int gD() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                this.kn.transact(10023, obtain, obtain2, 0);
                obtain2.readException();
                int readInt = obtain2.readInt();
                obtain2.recycle();
                obtain.recycle();
                return readInt;
            }

            public void gF() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                this.kn.transact(5006, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public DataHolder gG() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                this.kn.transact(5013, obtain, obtain2, 0);
                obtain2.readException();
                DataHolder createFromParcel = obtain2.readInt() != 0 ? DataHolder.CREATOR.createFromParcel(obtain2) : null;
                obtain2.recycle();
                obtain.recycle();
                return createFromParcel;
            }

            public boolean gH() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                this.kn.transact(5067, obtain, obtain2, 0);
                obtain2.readException();
                if (obtain2.readInt() != 0) {
                    z = true;
                }
                obtain2.recycle();
                obtain.recycle();
                return z;
            }

            public DataHolder gI() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                this.kn.transact(5502, obtain, obtain2, 0);
                obtain2.readException();
                DataHolder createFromParcel = obtain2.readInt() != 0 ? DataHolder.CREATOR.createFromParcel(obtain2) : null;
                obtain2.recycle();
                obtain.recycle();
                return createFromParcel;
            }

            public void gJ() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                this.kn.transact(8022, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public Intent gK() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                this.kn.transact(9013, obtain, obtain2, 0);
                obtain2.readException();
                Intent intent = obtain2.readInt() != 0 ? (Intent) Intent.CREATOR.createFromParcel(obtain2) : null;
                obtain2.recycle();
                obtain.recycle();
                return intent;
            }

            public void gL() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                this.kn.transact(11002, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public String gl() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                this.kn.transact(5007, obtain, obtain2, 0);
                obtain2.readException();
                String readString = obtain2.readString();
                obtain2.recycle();
                obtain.recycle();
                return readString;
            }

            public String gm() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                this.kn.transact(5012, obtain, obtain2, 0);
                obtain2.readException();
                String readString = obtain2.readString();
                obtain2.recycle();
                obtain.recycle();
                return readString;
            }

            public Intent gp() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                this.kn.transact(9003, obtain, obtain2, 0);
                obtain2.readException();
                Intent intent = obtain2.readInt() != 0 ? (Intent) Intent.CREATOR.createFromParcel(obtain2) : null;
                obtain2.recycle();
                obtain.recycle();
                return intent;
            }

            public Intent gq() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                this.kn.transact(9005, obtain, obtain2, 0);
                obtain2.readException();
                Intent intent = obtain2.readInt() != 0 ? (Intent) Intent.CREATOR.createFromParcel(obtain2) : null;
                obtain2.recycle();
                obtain.recycle();
                return intent;
            }

            public Intent gr() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                this.kn.transact(9006, obtain, obtain2, 0);
                obtain2.readException();
                Intent intent = obtain2.readInt() != 0 ? (Intent) Intent.CREATOR.createFromParcel(obtain2) : null;
                obtain2.recycle();
                obtain.recycle();
                return intent;
            }

            public Intent gs() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                this.kn.transact(9007, obtain, obtain2, 0);
                obtain2.readException();
                Intent intent = obtain2.readInt() != 0 ? (Intent) Intent.CREATOR.createFromParcel(obtain2) : null;
                obtain2.recycle();
                obtain.recycle();
                return intent;
            }

            public Intent gw() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                this.kn.transact(9010, obtain, obtain2, 0);
                obtain2.readException();
                Intent intent = obtain2.readInt() != 0 ? (Intent) Intent.CREATOR.createFromParcel(obtain2) : null;
                obtain2.recycle();
                obtain.recycle();
                return intent;
            }

            public Intent gx() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                this.kn.transact(9012, obtain, obtain2, 0);
                obtain2.readException();
                Intent intent = obtain2.readInt() != 0 ? (Intent) Intent.CREATOR.createFromParcel(obtain2) : null;
                obtain2.recycle();
                obtain.recycle();
                return intent;
            }

            public int gy() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                this.kn.transact(9019, obtain, obtain2, 0);
                obtain2.readException();
                int readInt = obtain2.readInt();
                obtain2.recycle();
                obtain.recycle();
                return readInt;
            }

            public String gz() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                this.kn.transact(AdTrackerConstants.WEBVIEW_TIMEOUT, obtain, obtain2, 0);
                obtain2.readException();
                String readString = obtain2.readString();
                obtain2.recycle();
                obtain.recycle();
                return readString;
            }

            public RoomEntity h(IGamesCallbacks iGamesCallbacks, String str) throws RemoteException {
                RoomEntity roomEntity = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                this.kn.transact(5053, obtain, obtain2, 0);
                obtain2.readException();
                if (obtain2.readInt() != 0) {
                    roomEntity = (RoomEntity) RoomEntity.CREATOR.createFromParcel(obtain2);
                }
                obtain2.recycle();
                obtain.recycle();
                return roomEntity;
            }

            public void h(IGamesCallbacks iGamesCallbacks) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                this.kn.transact(5056, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void i(IGamesCallbacks iGamesCallbacks) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                this.kn.transact(5062, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void i(IGamesCallbacks iGamesCallbacks, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                this.kn.transact(5061, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void j(IGamesCallbacks iGamesCallbacks) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                this.kn.transact(11001, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void j(IGamesCallbacks iGamesCallbacks, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                this.kn.transact(5057, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void j(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeString(str);
                obtain.writeString(str2);
                this.kn.transact(5065, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void k(IGamesCallbacks iGamesCallbacks, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                this.kn.transact(GamesStatusCodes.STATUS_REAL_TIME_MESSAGE_SEND_FAILED, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void k(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeString(str);
                obtain.writeString(str2);
                this.kn.transact(8025, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void l(IGamesCallbacks iGamesCallbacks, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                this.kn.transact(8005, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void l(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeString(str);
                obtain.writeInt(i);
                this.kn.transact(5029, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void m(IGamesCallbacks iGamesCallbacks, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                this.kn.transact(8006, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void m(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeString(str);
                obtain.writeInt(i);
                this.kn.transact(5028, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void n(IGamesCallbacks iGamesCallbacks, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                this.kn.transact(8009, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void n(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeString(str);
                obtain.writeInt(i);
                this.kn.transact(5055, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void o(long j) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeLong(j);
                this.kn.transact(AdTrackerConstants.WEBVIEW_INVALIDPARAM, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void o(IGamesCallbacks iGamesCallbacks, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                this.kn.transact(8010, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void o(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeString(str);
                obtain.writeInt(i);
                this.kn.transact(10014, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void p(long j) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeLong(j);
                this.kn.transact(5059, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void p(IGamesCallbacks iGamesCallbacks, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                this.kn.transact(8014, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void q(long j) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeLong(j);
                this.kn.transact(8013, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void q(IGamesCallbacks iGamesCallbacks, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeStrongBinder(iGamesCallbacks != null ? iGamesCallbacks.asBinder() : null);
                obtain.writeString(str);
                this.kn.transact(9002, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }

            public void r(long j) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                obtain.writeLong(j);
                this.kn.transact(GamesActivityResultCodes.RESULT_SIGN_IN_FAILED, obtain, obtain2, 0);
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
            }
        }

        public Stub() {
            attachInterface(this, "com.google.android.gms.games.internal.IGamesService");
        }

        public static IGamesService N(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.games.internal.IGamesService");
            return (queryLocalInterface == null || !queryLocalInterface instanceof IGamesService) ? new Proxy(iBinder) : (IGamesService) queryLocalInterface;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            ZInvitationCluster zInvitationCluster = null;
            boolean z = false;
            String gz;
            DataHolder gG;
            IGamesCallbacks M;
            int readInt;
            boolean z2;
            String readString;
            int readInt2;
            IBinder readStrongBinder;
            Bundle bundle;
            String[] createStringArray;
            int a;
            String readString2;
            int readInt3;
            int readInt4;
            int readInt5;
            IGamesCallbacks M2;
            String readString3;
            ParcelFileDescriptor f;
            Intent gp;
            switch (code) {
                case AdTrackerConstants.WEBVIEW_INVALIDPARAM:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    o(data.readLong());
                    reply.writeNoException();
                    return true;
                case AdTrackerConstants.WEBVIEW_XMLHTTPSUPPORT:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case AdTrackerConstants.WEBVIEW_TIMEOUT:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    gz = gz();
                    reply.writeNoException();
                    reply.writeString(gz);
                    return true;
                case AdTrackerConstants.WEBVIEW_SERVERERROR:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    Bundle dG = dG();
                    reply.writeNoException();
                    if (dG != null) {
                        reply.writeInt(1);
                        dG.writeToParcel(reply, 1);
                        return true;
                    } else {
                        reply.writeInt(0);
                        return true;
                    }
                case AdTrackerConstants.WEBVIEW_INVALIDJSON:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(data.readStrongBinder(), data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case 5006:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    gF();
                    reply.writeNoException();
                    return true;
                case 5007:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    gz = gl();
                    reply.writeNoException();
                    reply.writeString(gz);
                    return true;
                case 5012:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    gz = gm();
                    reply.writeNoException();
                    reply.writeString(gz);
                    return true;
                case 5013:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    gG = gG();
                    reply.writeNoException();
                    if (gG != null) {
                        reply.writeInt(1);
                        gG.writeToParcel(reply, 1);
                        return true;
                    } else {
                        reply.writeInt(0);
                        return true;
                    }
                case 5014:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readString());
                    reply.writeNoException();
                    return true;
                case 5015:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    M = com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder());
                    readInt = data.readInt();
                    z2 = data.readInt() != 0;
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    a(M, readInt, z2, z);
                    reply.writeNoException();
                    return true;
                case 5016:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readString(), data.readLong());
                    reply.writeNoException();
                    return true;
                case 5017:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    b(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 5018:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    b(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readString());
                    reply.writeNoException();
                    return true;
                case 5019:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readString(), data.readInt(), data.readInt(), data.readInt(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 5020:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    b(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readString(), data.readInt(), data.readInt(), data.readInt(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 5021:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null, data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 5022:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    c(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 5023:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readString(), data.readStrongBinder(), data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case 5024:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    b(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readString(), data.readStrongBinder(), data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case 5025:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    M = com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder());
                    readString = data.readString();
                    readInt2 = data.readInt();
                    readStrongBinder = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    }
                    a(M, readString, readInt2, readStrongBinder, bundle);
                    reply.writeNoException();
                    return true;
                case 5026:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    d(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 5027:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    e(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 5028:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    m(data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 5029:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    l(data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 5030:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    M = com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder());
                    IBinder readStrongBinder2 = data.readStrongBinder();
                    readInt2 = data.readInt();
                    createStringArray = data.createStringArray();
                    if (data.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    }
                    a(M, readStrongBinder2, readInt2, createStringArray, bundle, data.readInt() != 0, data.readLong());
                    reply.writeNoException();
                    return true;
                case 5031:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readStrongBinder(), data.readString(), data.readInt() != 0, data.readLong());
                    reply.writeNoException();
                    return true;
                case 5032:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    c(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readString());
                    reply.writeNoException();
                    return true;
                case 5033:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a = a(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.createByteArray(), data.readString(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(a);
                    return true;
                case 5034:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a = b(data.createByteArray(), data.readString(), data.createStringArray());
                    reply.writeNoException();
                    reply.writeInt(a);
                    return true;
                case 5035:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    gz = aE(data.readString());
                    reply.writeNoException();
                    reply.writeString(gz);
                    return true;
                case 5036:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    aY(data.readInt());
                    reply.writeNoException();
                    return true;
                case 5037:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    d(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readString());
                    reply.writeNoException();
                    return true;
                case 5038:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 5039:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    M = com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder());
                    readString = data.readString();
                    readString2 = data.readString();
                    readInt3 = data.readInt();
                    readInt4 = data.readInt();
                    readInt5 = data.readInt();
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    a(M, readString, readString2, readInt3, readInt4, readInt5, z);
                    reply.writeNoException();
                    return true;
                case 5040:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    M = com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder());
                    readString = data.readString();
                    readString2 = data.readString();
                    readInt3 = data.readInt();
                    readInt4 = data.readInt();
                    readInt5 = data.readInt();
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    b(M, readString, readString2, readInt3, readInt4, readInt5, z);
                    reply.writeNoException();
                    return true;
                case 5041:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    b(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 5042:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    e(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readString());
                    reply.writeNoException();
                    return true;
                case 5043:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    f(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readString());
                    reply.writeNoException();
                    return true;
                case 5044:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readInt(), data.readInt(), data.readInt() != 0, data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 5045:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readString(), data.readInt(), data.readInt() != 0, data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 5046:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    M = com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder());
                    readInt = data.readInt();
                    z2 = data.readInt() != 0;
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    b(M, readInt, z2, z);
                    reply.writeNoException();
                    return true;
                case 5047:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    f(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 5048:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    M = com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder());
                    readInt = data.readInt();
                    z2 = data.readInt() != 0;
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    c(M, readInt, z2, z);
                    reply.writeNoException();
                    return true;
                case 5049:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    g(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 5050:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    aF(data.readString());
                    reply.writeNoException();
                    return true;
                case 5051:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    b(data.readString(), data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 5052:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    g(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readString());
                    reply.writeNoException();
                    return true;
                case 5053:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    RoomEntity h = h(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readString());
                    reply.writeNoException();
                    if (h != null) {
                        reply.writeInt(1);
                        h.writeToParcel(reply, 1);
                        return true;
                    } else {
                        reply.writeInt(0);
                        return true;
                    }
                case 5054:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    M2 = com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder());
                    readString3 = data.readString();
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    a(M2, readString3, z);
                    reply.writeNoException();
                    return true;
                case 5055:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    n(data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 5056:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    h(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 5057:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    j(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readString());
                    reply.writeNoException();
                    return true;
                case 5058:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readLong());
                    reply.writeNoException();
                    return true;
                case 5059:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    p(data.readLong());
                    reply.writeNoException();
                    return true;
                case 5060:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a = aG(data.readString());
                    reply.writeNoException();
                    reply.writeInt(a);
                    return true;
                case 5061:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    i(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readString());
                    reply.writeNoException();
                    return true;
                case 5062:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    i(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 5063:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    M = com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    a(M, z, data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case 5064:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    gz = aD(data.readString());
                    reply.writeNoException();
                    reply.writeString(gz);
                    return true;
                case 5065:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    j(data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 5066:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    Uri aH = aH(data.readString());
                    reply.writeNoException();
                    if (aH != null) {
                        reply.writeInt(1);
                        aH.writeToParcel(reply, 1);
                        return true;
                    } else {
                        reply.writeInt(0);
                        return true;
                    }
                case 5067:
                    int i;
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    z2 = gH();
                    reply.writeNoException();
                    if (z2) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 5068:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    A(z);
                    reply.writeNoException();
                    return true;
                case 5501:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    b(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readString(), data.readInt(), data.readInt() != 0, data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 5502:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    gG = gI();
                    reply.writeNoException();
                    if (gG != null) {
                        reply.writeInt(1);
                        gG.writeToParcel(reply, 1);
                        return true;
                    } else {
                        reply.writeInt(0);
                        return true;
                    }
                case AdTrackerConstants.SERVER_RELOAD_WEBVIEW:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    M2 = com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    a(M2, z);
                    reply.writeNoException();
                    return true;
                case AdTrackerConstants.SERVER_BADREQUEST:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    M2 = com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder());
                    readString3 = data.readString();
                    readString = data.readString();
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    a(M2, readString3, readString, z);
                    reply.writeNoException();
                    return true;
                case AdTrackerConstants.SERVER_INVALIDAPPID:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    M = com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder());
                    readInt = data.readInt();
                    z2 = data.readInt() != 0;
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    d(M, readInt, z2, z);
                    reply.writeNoException();
                    return true;
                case GamesStatusCodes.STATUS_MULTIPLAYER_ERROR_INVALID_OPERATION:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    M = com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder());
                    readInt = data.readInt();
                    z2 = data.readInt() != 0;
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    e(M, readInt, z2, z);
                    reply.writeNoException();
                    return true;
                case GamesStatusCodes.STATUS_MATCH_ERROR_INACTIVE_MATCH:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    M = com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder());
                    readString = data.readString();
                    readInt2 = data.readInt();
                    boolean z3 = data.readInt() != 0;
                    boolean z4 = data.readInt() != 0;
                    boolean z5 = data.readInt() != 0;
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    a(M, readString, readInt2, z3, z4, z5, z);
                    reply.writeNoException();
                    return true;
                case GamesStatusCodes.STATUS_MATCH_ERROR_INVALID_MATCH_STATE:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    M2 = com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder());
                    readString3 = data.readString();
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    b(M2, readString3, z);
                    reply.writeNoException();
                    return true;
                case GamesStatusCodes.STATUS_MATCH_ERROR_OUT_OF_DATE_VERSION:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    M2 = com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    b(M2, z);
                    reply.writeNoException();
                    return true;
                case GamesStatusCodes.STATUS_MATCH_ERROR_INVALID_MATCH_RESULTS:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    M2 = com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder());
                    readString3 = data.readString();
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    c(M2, readString3, z);
                    reply.writeNoException();
                    return true;
                case GamesStatusCodes.STATUS_MATCH_ERROR_ALREADY_REMATCHED:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    M2 = com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder());
                    readString3 = data.readString();
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    d(M2, readString3, z);
                    reply.writeNoException();
                    return true;
                case GamesStatusCodes.STATUS_MATCH_NOT_FOUND:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    M2 = com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder());
                    readString3 = data.readString();
                    readString = data.readString();
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    b(M2, readString3, readString, z);
                    reply.writeNoException();
                    return true;
                case GamesStatusCodes.STATUS_MATCH_ERROR_LOCALLY_MODIFIED:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    f = f(data.readInt() != 0 ? (Uri) Uri.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    if (f != null) {
                        reply.writeInt(1);
                        f.writeToParcel(reply, 1);
                        return true;
                    } else {
                        reply.writeInt(0);
                        return true;
                    }
                case GamesStatusCodes.STATUS_REAL_TIME_MESSAGE_SEND_FAILED:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    k(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readString());
                    reply.writeNoException();
                    return true;
                case GamesStatusCodes.STATUS_INVALID_REAL_TIME_ROOM_ID:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readString(), data.readLong(), data.readString());
                    reply.writeNoException();
                    return true;
                case GamesStatusCodes.STATUS_PARTICIPANT_NOT_CONNECTED:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    M = com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder());
                    readString = data.readString();
                    readInt2 = data.readInt();
                    readStrongBinder = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    }
                    b(M, readString, readInt2, readStrongBinder, bundle);
                    reply.writeNoException();
                    return true;
                case 8001:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readString(), data.readString(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 8002:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    aI(data.readString());
                    reply.writeNoException();
                    return true;
                case 8003:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.createIntArray());
                    reply.writeNoException();
                    return true;
                case 8004:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    M = com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder());
                    readInt = data.readInt();
                    readInt2 = data.readInt();
                    createStringArray = data.createStringArray();
                    if (data.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    }
                    a(M, readInt, readInt2, createStringArray, bundle);
                    reply.writeNoException();
                    return true;
                case 8005:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    l(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readString());
                    reply.writeNoException();
                    return true;
                case 8006:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    m(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readString());
                    reply.writeNoException();
                    return true;
                case 8007:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readString(), data.createByteArray(), data.readString(), (ParticipantResult[]) data.createTypedArray(ParticipantResult.CREATOR));
                    reply.writeNoException();
                    return true;
                case 8008:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readString(), data.createByteArray(), (ParticipantResult[]) data.createTypedArray(ParticipantResult.CREATOR));
                    reply.writeNoException();
                    return true;
                case 8009:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    n(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readString());
                    reply.writeNoException();
                    return true;
                case 8010:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    o(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readString());
                    reply.writeNoException();
                    return true;
                case 8011:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    c(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 8012:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    b(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readLong());
                    reply.writeNoException();
                    return true;
                case 8013:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    q(data.readLong());
                    reply.writeNoException();
                    return true;
                case 8014:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    p(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readString());
                    reply.writeNoException();
                    return true;
                case 8015:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    d(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 8016:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    e(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 8017:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readString(), data.createIntArray());
                    reply.writeNoException();
                    return true;
                case 8018:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readLong(), data.readString());
                    reply.writeNoException();
                    return true;
                case 8019:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(data.readLong(), data.readString());
                    reply.writeNoException();
                    return true;
                case 8020:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    b(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readLong(), data.readString());
                    reply.writeNoException();
                    return true;
                case 8021:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    b(data.readLong(), data.readString());
                    reply.writeNoException();
                    return true;
                case 8022:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    gJ();
                    reply.writeNoException();
                    return true;
                case 8023:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    M2 = com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder());
                    readString3 = data.readString();
                    readInt = data.readInt();
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    a(M2, readString3, readInt, z);
                    reply.writeNoException();
                    return true;
                case 8024:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a = gA();
                    reply.writeNoException();
                    reply.writeInt(a);
                    return true;
                case 8025:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    k(data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 8026:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    c(data.readString(), data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 8027:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    M2 = com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    c(M2, z);
                    reply.writeNoException();
                    return true;
                case 9001:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    c(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readString(), data.readInt(), data.readInt() != 0, data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 9002:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    q(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readString());
                    reply.writeNoException();
                    return true;
                case 9003:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    gp = gp();
                    reply.writeNoException();
                    if (gp != null) {
                        reply.writeInt(1);
                        gp.writeToParcel(reply, 1);
                        return true;
                    } else {
                        reply.writeInt(0);
                        return true;
                    }
                case 9004:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    gp = aA(data.readString());
                    reply.writeNoException();
                    if (gp != null) {
                        reply.writeInt(1);
                        gp.writeToParcel(reply, 1);
                        return true;
                    } else {
                        reply.writeInt(0);
                        return true;
                    }
                case 9005:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    gp = gq();
                    reply.writeNoException();
                    if (gp != null) {
                        reply.writeInt(1);
                        gp.writeToParcel(reply, 1);
                        return true;
                    } else {
                        reply.writeInt(0);
                        return true;
                    }
                case 9006:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    gp = gr();
                    reply.writeNoException();
                    if (gp != null) {
                        reply.writeInt(1);
                        gp.writeToParcel(reply, 1);
                        return true;
                    } else {
                        reply.writeInt(0);
                        return true;
                    }
                case 9007:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    gp = gs();
                    reply.writeNoException();
                    if (gp != null) {
                        reply.writeInt(1);
                        gp.writeToParcel(reply, 1);
                        return true;
                    } else {
                        reply.writeInt(0);
                        return true;
                    }
                case 9008:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    gp = a(data.readInt(), data.readInt(), data.readInt() != 0);
                    reply.writeNoException();
                    if (gp != null) {
                        reply.writeInt(1);
                        gp.writeToParcel(reply, 1);
                        return true;
                    } else {
                        reply.writeInt(0);
                        return true;
                    }
                case 9009:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    gp = b(data.readInt(), data.readInt(), data.readInt() != 0);
                    reply.writeNoException();
                    if (gp != null) {
                        reply.writeInt(1);
                        gp.writeToParcel(reply, 1);
                        return true;
                    } else {
                        reply.writeInt(0);
                        return true;
                    }
                case 9010:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    gp = gw();
                    reply.writeNoException();
                    if (gp != null) {
                        reply.writeInt(1);
                        gp.writeToParcel(reply, 1);
                        return true;
                    } else {
                        reply.writeInt(0);
                        return true;
                    }
                case 9011:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    gp = a(data.readInt() != 0 ? (RoomEntity) RoomEntity.CREATOR.createFromParcel(data) : null, data.readInt());
                    reply.writeNoException();
                    if (gp != null) {
                        reply.writeInt(1);
                        gp.writeToParcel(reply, 1);
                        return true;
                    } else {
                        reply.writeInt(0);
                        return true;
                    }
                case 9012:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    gp = gx();
                    reply.writeNoException();
                    if (gp != null) {
                        reply.writeInt(1);
                        gp.writeToParcel(reply, 1);
                        return true;
                    } else {
                        reply.writeInt(0);
                        return true;
                    }
                case 9013:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    gp = gK();
                    reply.writeNoException();
                    if (gp != null) {
                        reply.writeInt(1);
                        gp.writeToParcel(reply, 1);
                        return true;
                    } else {
                        reply.writeInt(0);
                        return true;
                    }
                case 9019:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a = gy();
                    reply.writeNoException();
                    reply.writeInt(a);
                    return true;
                case 9020:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    d(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readString(), data.readInt(), data.readInt() != 0, data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 9028:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readString(), data.readString(), data.readInt(), data.readInt() != 0, data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 9030:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    f = aJ(data.readString());
                    reply.writeNoException();
                    if (f != null) {
                        reply.writeInt(1);
                        f.writeToParcel(reply, 1);
                        return true;
                    } else {
                        reply.writeInt(0);
                        return true;
                    }
                case 9031:
                    Uri uri;
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    ParticipantEntity[] participantEntityArr = (ParticipantEntity[]) data.createTypedArray(ParticipantEntity.CREATOR);
                    readString = data.readString();
                    readString2 = data.readString();
                    Uri uri2 = data.readInt() != 0 ? (Uri) Uri.CREATOR.createFromParcel(data) : null;
                    if (data.readInt() != 0) {
                        uri = (Uri) Uri.CREATOR.createFromParcel(data);
                    }
                    gp = a(participantEntityArr, readString, readString2, uri2, uri);
                    reply.writeNoException();
                    if (gp != null) {
                        reply.writeInt(1);
                        gp.writeToParcel(reply, 1);
                        return true;
                    } else {
                        reply.writeInt(0);
                        return true;
                    }
                case GamesActivityResultCodes.RESULT_RECONNECT_REQUIRED:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    c(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readLong());
                    reply.writeNoException();
                    return true;
                case GamesActivityResultCodes.RESULT_SIGN_IN_FAILED:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    r(data.readLong());
                    reply.writeNoException();
                    return true;
                case GamesActivityResultCodes.RESULT_LICENSE_FAILED:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    c(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readLong(), data.readString());
                    reply.writeNoException();
                    return true;
                case GamesActivityResultCodes.RESULT_APP_MISCONFIGURED:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    c(data.readLong(), data.readString());
                    reply.writeNoException();
                    return true;
                case GamesActivityResultCodes.RESULT_LEFT_ROOM:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readString(), data.createStringArray(), data.readInt(), data.createByteArray(), data.readInt());
                    reply.writeNoException();
                    return true;
                case GamesActivityResultCodes.RESULT_NETWORK_FAILURE:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.createStringArray());
                    reply.writeNoException();
                    return true;
                case GamesActivityResultCodes.RESULT_SEND_REQUEST_FAILED:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    b(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.createStringArray());
                    reply.writeNoException();
                    return true;
                case GamesActivityResultCodes.RESULT_INVALID_ROOM:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readString(), data.readString(), data.createStringArray());
                    reply.writeNoException();
                    return true;
                case 10009:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readInt(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 10010:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readString(), data.readString(), data.readInt(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 10011:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 10012:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    gp = a(data.readInt(), data.createByteArray(), data.readInt(), data.readString());
                    reply.writeNoException();
                    if (gp != null) {
                        reply.writeInt(1);
                        gp.writeToParcel(reply, 1);
                        return true;
                    } else {
                        reply.writeInt(0);
                        return true;
                    }
                case 10013:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a = gC();
                    reply.writeNoException();
                    reply.writeInt(a);
                    return true;
                case 10014:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    o(data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 10015:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    gp = gB();
                    reply.writeNoException();
                    if (gp != null) {
                        reply.writeInt(1);
                        gp.writeToParcel(reply, 1);
                        return true;
                    } else {
                        reply.writeInt(0);
                        return true;
                    }
                case 10016:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readInt());
                    reply.writeNoException();
                    return true;
                case 10017:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    M2 = com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder());
                    readString3 = data.readString();
                    readInt = data.readInt();
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    b(M2, readString3, readInt, z);
                    reply.writeNoException();
                    return true;
                case 10018:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readInt(), data.createIntArray());
                    reply.writeNoException();
                    return true;
                case 10019:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.readString(), data.readInt(), data.createIntArray());
                    reply.writeNoException();
                    return true;
                case 10020:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    c(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()), data.createStringArray());
                    reply.writeNoException();
                    return true;
                case 10021:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    if (data.readInt() != 0) {
                        zInvitationCluster = ZInvitationCluster.CREATOR.as(data);
                    }
                    gp = a(zInvitationCluster, data.readString(), data.readString());
                    reply.writeNoException();
                    if (gp != null) {
                        reply.writeInt(1);
                        gp.writeToParcel(reply, 1);
                        return true;
                    } else {
                        reply.writeInt(0);
                        return true;
                    }
                case 10022:
                    GameRequestCluster at;
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    if (data.readInt() != 0) {
                        at = GameRequestCluster.CREATOR.at(data);
                    }
                    gp = a(at, data.readString());
                    reply.writeNoException();
                    if (gp != null) {
                        reply.writeInt(1);
                        gp.writeToParcel(reply, 1);
                        return true;
                    } else {
                        reply.writeInt(0);
                        return true;
                    }
                case 10023:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a = gD();
                    reply.writeNoException();
                    reply.writeInt(a);
                    return true;
                case 11001:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    j(com.google.android.gms.games.internal.IGamesCallbacks.Stub.M(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 11002:
                    data.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    gL();
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.games.internal.IGamesService");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void A(boolean z) throws RemoteException;

    int a(IGamesCallbacks iGamesCallbacks, byte[] bArr, String str, String str2) throws RemoteException;

    Intent a(int i, int i2, boolean z) throws RemoteException;

    Intent a(int i, byte[] bArr, int i2, String str) throws RemoteException;

    Intent a(ZInvitationCluster zInvitationCluster, String str, String str2) throws RemoteException;

    Intent a(GameRequestCluster gameRequestCluster, String str) throws RemoteException;

    Intent a(RoomEntity roomEntity, int i) throws RemoteException;

    Intent a(ParticipantEntity[] participantEntityArr, String str, String str2, Uri uri, Uri uri2) throws RemoteException;

    void a(long j, String str) throws RemoteException;

    void a(IBinder iBinder, Bundle bundle) throws RemoteException;

    void a(IGamesCallbacks iGamesCallbacks) throws RemoteException;

    void a(IGamesCallbacks iGamesCallbacks, int i) throws RemoteException;

    void a(IGamesCallbacks iGamesCallbacks, int i, int i2, int i3) throws RemoteException;

    void a(IGamesCallbacks iGamesCallbacks, int i, int i2, boolean z, boolean z2) throws RemoteException;

    void a(IGamesCallbacks iGamesCallbacks, int i, int i2, String[] strArr, Bundle bundle) throws RemoteException;

    void a(IGamesCallbacks iGamesCallbacks, int i, boolean z, boolean z2) throws RemoteException;

    void a(IGamesCallbacks iGamesCallbacks, int i, int[] iArr) throws RemoteException;

    void a(IGamesCallbacks iGamesCallbacks, long j) throws RemoteException;

    void a(IGamesCallbacks iGamesCallbacks, long j, String str) throws RemoteException;

    void a(IGamesCallbacks iGamesCallbacks, Bundle bundle, int i, int i2) throws RemoteException;

    void a(IGamesCallbacks iGamesCallbacks, IBinder iBinder, int i, String[] strArr, Bundle bundle, boolean z, long j) throws RemoteException;

    void a(IGamesCallbacks iGamesCallbacks, IBinder iBinder, String str, boolean z, long j) throws RemoteException;

    void a(IGamesCallbacks iGamesCallbacks, String str) throws RemoteException;

    void a(IGamesCallbacks iGamesCallbacks, String str, int i) throws RemoteException;

    void a(IGamesCallbacks iGamesCallbacks, String str, int i, int i2, int i3, boolean z) throws RemoteException;

    void a(IGamesCallbacks iGamesCallbacks, String str, int i, IBinder iBinder, Bundle bundle) throws RemoteException;

    void a(IGamesCallbacks iGamesCallbacks, String str, int i, boolean z) throws RemoteException;

    void a(IGamesCallbacks iGamesCallbacks, String str, int i, boolean z, boolean z2) throws RemoteException;

    void a(IGamesCallbacks iGamesCallbacks, String str, int i, boolean z, boolean z2, boolean z3, boolean z4) throws RemoteException;

    void a(IGamesCallbacks iGamesCallbacks, String str, int i, int[] iArr) throws RemoteException;

    void a(IGamesCallbacks iGamesCallbacks, String str, long j) throws RemoteException;

    void a(IGamesCallbacks iGamesCallbacks, String str, long j, String str2) throws RemoteException;

    void a(IGamesCallbacks iGamesCallbacks, String str, IBinder iBinder, Bundle bundle) throws RemoteException;

    void a(IGamesCallbacks iGamesCallbacks, String str, String str2) throws RemoteException;

    void a(IGamesCallbacks iGamesCallbacks, String str, String str2, int i, int i2) throws RemoteException;

    void a(IGamesCallbacks iGamesCallbacks, String str, String str2, int i, int i2, int i3) throws RemoteException;

    void a(IGamesCallbacks iGamesCallbacks, String str, String str2, int i, int i2, int i3, boolean z) throws RemoteException;

    void a(IGamesCallbacks iGamesCallbacks, String str, String str2, int i, boolean z, boolean z2) throws RemoteException;

    void a(IGamesCallbacks iGamesCallbacks, String str, String str2, boolean z) throws RemoteException;

    void a(IGamesCallbacks iGamesCallbacks, String str, String str2, String[] strArr) throws RemoteException;

    void a(IGamesCallbacks iGamesCallbacks, String str, boolean z) throws RemoteException;

    void a(IGamesCallbacks iGamesCallbacks, String str, byte[] bArr, String str2, ParticipantResult[] participantResultArr) throws RemoteException;

    void a(IGamesCallbacks iGamesCallbacks, String str, byte[] bArr, ParticipantResult[] participantResultArr) throws RemoteException;

    void a(IGamesCallbacks iGamesCallbacks, String str, int[] iArr) throws RemoteException;

    void a(IGamesCallbacks iGamesCallbacks, String str, String[] strArr, int i, byte[] bArr, int i2) throws RemoteException;

    void a(IGamesCallbacks iGamesCallbacks, boolean z) throws RemoteException;

    void a(IGamesCallbacks iGamesCallbacks, boolean z, Bundle bundle) throws RemoteException;

    void a(IGamesCallbacks iGamesCallbacks, int[] iArr) throws RemoteException;

    void a(IGamesCallbacks iGamesCallbacks, String[] strArr) throws RemoteException;

    Intent aA(String str) throws RemoteException;

    String aD(String str) throws RemoteException;

    String aE(String str) throws RemoteException;

    void aF(String str) throws RemoteException;

    int aG(String str) throws RemoteException;

    Uri aH(String str) throws RemoteException;

    void aI(String str) throws RemoteException;

    ParcelFileDescriptor aJ(String str) throws RemoteException;

    void aY(int i) throws RemoteException;

    int b(byte[] bArr, String str, String[] strArr) throws RemoteException;

    Intent b(int i, int i2, boolean z) throws RemoteException;

    void b(long j, String str) throws RemoteException;

    void b(IGamesCallbacks iGamesCallbacks) throws RemoteException;

    void b(IGamesCallbacks iGamesCallbacks, int i, boolean z, boolean z2) throws RemoteException;

    void b(IGamesCallbacks iGamesCallbacks, long j) throws RemoteException;

    void b(IGamesCallbacks iGamesCallbacks, long j, String str) throws RemoteException;

    void b(IGamesCallbacks iGamesCallbacks, String str) throws RemoteException;

    void b(IGamesCallbacks iGamesCallbacks, String str, int i, int i2, int i3, boolean z) throws RemoteException;

    void b(IGamesCallbacks iGamesCallbacks, String str, int i, IBinder iBinder, Bundle bundle) throws RemoteException;

    void b(IGamesCallbacks iGamesCallbacks, String str, int i, boolean z) throws RemoteException;

    void b(IGamesCallbacks iGamesCallbacks, String str, int i, boolean z, boolean z2) throws RemoteException;

    void b(IGamesCallbacks iGamesCallbacks, String str, IBinder iBinder, Bundle bundle) throws RemoteException;

    void b(IGamesCallbacks iGamesCallbacks, String str, String str2) throws RemoteException;

    void b(IGamesCallbacks iGamesCallbacks, String str, String str2, int i, int i2, int i3, boolean z) throws RemoteException;

    void b(IGamesCallbacks iGamesCallbacks, String str, String str2, boolean z) throws RemoteException;

    void b(IGamesCallbacks iGamesCallbacks, String str, boolean z) throws RemoteException;

    void b(IGamesCallbacks iGamesCallbacks, boolean z) throws RemoteException;

    void b(IGamesCallbacks iGamesCallbacks, String[] strArr) throws RemoteException;

    void b(String str, String str2, int i) throws RemoteException;

    void c(long j, String str) throws RemoteException;

    void c(IGamesCallbacks iGamesCallbacks) throws RemoteException;

    void c(IGamesCallbacks iGamesCallbacks, int i, boolean z, boolean z2) throws RemoteException;

    void c(IGamesCallbacks iGamesCallbacks, long j) throws RemoteException;

    void c(IGamesCallbacks iGamesCallbacks, long j, String str) throws RemoteException;

    void c(IGamesCallbacks iGamesCallbacks, String str) throws RemoteException;

    void c(IGamesCallbacks iGamesCallbacks, String str, int i, boolean z, boolean z2) throws RemoteException;

    void c(IGamesCallbacks iGamesCallbacks, String str, String str2) throws RemoteException;

    void c(IGamesCallbacks iGamesCallbacks, String str, boolean z) throws RemoteException;

    void c(IGamesCallbacks iGamesCallbacks, boolean z) throws RemoteException;

    void c(IGamesCallbacks iGamesCallbacks, String[] strArr) throws RemoteException;

    void c(String str, String str2, int i) throws RemoteException;

    void d(IGamesCallbacks iGamesCallbacks) throws RemoteException;

    void d(IGamesCallbacks iGamesCallbacks, int i, boolean z, boolean z2) throws RemoteException;

    void d(IGamesCallbacks iGamesCallbacks, String str) throws RemoteException;

    void d(IGamesCallbacks iGamesCallbacks, String str, int i, boolean z, boolean z2) throws RemoteException;

    void d(IGamesCallbacks iGamesCallbacks, String str, String str2) throws RemoteException;

    void d(IGamesCallbacks iGamesCallbacks, String str, boolean z) throws RemoteException;

    Bundle dG() throws RemoteException;

    void e(IGamesCallbacks iGamesCallbacks) throws RemoteException;

    void e(IGamesCallbacks iGamesCallbacks, int i, boolean z, boolean z2) throws RemoteException;

    void e(IGamesCallbacks iGamesCallbacks, String str) throws RemoteException;

    void e(IGamesCallbacks iGamesCallbacks, String str, String str2) throws RemoteException;

    ParcelFileDescriptor f(Uri uri) throws RemoteException;

    void f(IGamesCallbacks iGamesCallbacks) throws RemoteException;

    void f(IGamesCallbacks iGamesCallbacks, String str) throws RemoteException;

    void g(IGamesCallbacks iGamesCallbacks) throws RemoteException;

    void g(IGamesCallbacks iGamesCallbacks, String str) throws RemoteException;

    int gA() throws RemoteException;

    Intent gB() throws RemoteException;

    int gC() throws RemoteException;

    int gD() throws RemoteException;

    void gF() throws RemoteException;

    DataHolder gG() throws RemoteException;

    boolean gH() throws RemoteException;

    DataHolder gI() throws RemoteException;

    void gJ() throws RemoteException;

    Intent gK() throws RemoteException;

    void gL() throws RemoteException;

    String gl() throws RemoteException;

    String gm() throws RemoteException;

    Intent gp() throws RemoteException;

    Intent gq() throws RemoteException;

    Intent gr() throws RemoteException;

    Intent gs() throws RemoteException;

    Intent gw() throws RemoteException;

    Intent gx() throws RemoteException;

    int gy() throws RemoteException;

    String gz() throws RemoteException;

    RoomEntity h(IGamesCallbacks iGamesCallbacks, String str) throws RemoteException;

    void h(IGamesCallbacks iGamesCallbacks) throws RemoteException;

    void i(IGamesCallbacks iGamesCallbacks) throws RemoteException;

    void i(IGamesCallbacks iGamesCallbacks, String str) throws RemoteException;

    void j(IGamesCallbacks iGamesCallbacks) throws RemoteException;

    void j(IGamesCallbacks iGamesCallbacks, String str) throws RemoteException;

    void j(String str, String str2) throws RemoteException;

    void k(IGamesCallbacks iGamesCallbacks, String str) throws RemoteException;

    void k(String str, String str2) throws RemoteException;

    void l(IGamesCallbacks iGamesCallbacks, String str) throws RemoteException;

    void l(String str, int i) throws RemoteException;

    void m(IGamesCallbacks iGamesCallbacks, String str) throws RemoteException;

    void m(String str, int i) throws RemoteException;

    void n(IGamesCallbacks iGamesCallbacks, String str) throws RemoteException;

    void n(String str, int i) throws RemoteException;

    void o(long j) throws RemoteException;

    void o(IGamesCallbacks iGamesCallbacks, String str) throws RemoteException;

    void o(String str, int i) throws RemoteException;

    void p(long j) throws RemoteException;

    void p(IGamesCallbacks iGamesCallbacks, String str) throws RemoteException;

    void q(long j) throws RemoteException;

    void q(IGamesCallbacks iGamesCallbacks, String str) throws RemoteException;

    void r(long j) throws RemoteException;
}