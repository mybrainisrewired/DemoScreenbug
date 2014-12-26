package com.google.android.gms.games.multiplayer.realtime;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.games.multiplayer.ParticipantEntity;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import java.util.ArrayList;

public class RoomEntityCreator implements Creator<RoomEntity> {
    static void a(RoomEntity roomEntity, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.a(parcel, 1, roomEntity.getRoomId(), false);
        b.c(parcel, GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE, roomEntity.getVersionCode());
        b.a(parcel, (int)MMAdView.TRANSITION_UP, roomEntity.getCreatorId(), false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, roomEntity.getCreationTimestamp());
        b.c(parcel, MMAdView.TRANSITION_RANDOM, roomEntity.getStatus());
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, roomEntity.getDescription(), false);
        b.c(parcel, ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, roomEntity.getVariant());
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, roomEntity.getAutoMatchCriteria(), false);
        b.b(parcel, ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, roomEntity.getParticipants(), false);
        b.c(parcel, ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES, roomEntity.getAutoMatchWaitEstimateSeconds());
        b.F(parcel, p);
    }

    public RoomEntity ax(Parcel parcel) {
        int i = 0;
        ArrayList arrayList = null;
        int o = a.o(parcel);
        long j = 0;
        Bundle bundle = null;
        int i2 = 0;
        String str = null;
        int i3 = 0;
        String str2 = null;
        String str3 = null;
        int i4 = 0;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    str3 = a.n(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    str2 = a.n(parcel, n);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    j = a.i(parcel, n);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    i3 = a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    str = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    i2 = a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    bundle = a.p(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    arrayList = a.c(parcel, n, ParticipantEntity.CREATOR);
                    break;
                case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                    i = a.g(parcel, n);
                    break;
                case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                    i4 = a.g(parcel, n);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new RoomEntity(i4, str3, str2, j, i3, str, i2, bundle, arrayList, i);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public RoomEntity[] bq(int i) {
        return new RoomEntity[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return ax(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return bq(x0);
    }
}