package com.google.android.gms.games.multiplayer.turnbased;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.games.GameEntity;
import com.google.android.gms.games.multiplayer.ParticipantEntity;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import java.util.ArrayList;

public class TurnBasedMatchEntityCreator implements Creator<TurnBasedMatchEntity> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void a(TurnBasedMatchEntity turnBasedMatchEntity, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.a(parcel, 1, turnBasedMatchEntity.getGame(), i, false);
        b.a(parcel, (int)MMAdView.TRANSITION_UP, turnBasedMatchEntity.getMatchId(), false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, turnBasedMatchEntity.getCreatorId(), false);
        b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, turnBasedMatchEntity.getCreationTimestamp());
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, turnBasedMatchEntity.getLastUpdaterId(), false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, turnBasedMatchEntity.getLastUpdatedTimestamp());
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, turnBasedMatchEntity.getPendingParticipantId(), false);
        b.c(parcel, ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, turnBasedMatchEntity.getStatus());
        b.c(parcel, ApiEventType.API_MRAID_USE_CUSTOM_CLOSE, turnBasedMatchEntity.getVariant());
        b.c(parcel, ApiEventType.API_MRAID_EXPAND, turnBasedMatchEntity.getVersion());
        b.a(parcel, (int)ApiEventType.API_MRAID_RESIZE, turnBasedMatchEntity.getData(), false);
        b.b(parcel, ApiEventType.API_MRAID_CLOSE, turnBasedMatchEntity.getParticipants(), false);
        b.a(parcel, (int)ApiEventType.API_MRAID_IS_VIEWABLE, turnBasedMatchEntity.getRematchId(), false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_PLACEMENT_TYPE, turnBasedMatchEntity.getPreviousMatchData(), false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_SCREEN_SIZE, turnBasedMatchEntity.getAutoMatchCriteria(), false);
        b.c(parcel, ApiEventType.API_MRAID_GET_ORIENTATION, turnBasedMatchEntity.getMatchNumber());
        b.c(parcel, GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE, turnBasedMatchEntity.getVersionCode());
        b.a(parcel, (int)Encoder.LINE_GROUPS, turnBasedMatchEntity.isLocallyModified());
        b.c(parcel, ApiEventType.API_MRAID_GET_CURRENT_POSITION, turnBasedMatchEntity.getTurnStatus());
        b.a(parcel, (int)ApiEventType.API_MRAID_POST_TO_SOCIAL, turnBasedMatchEntity.getDescriptionParticipantId(), false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_MAX_SIZE, turnBasedMatchEntity.getDescription(), false);
        b.F(parcel, p);
    }

    public TurnBasedMatchEntity createFromParcel(Parcel parcel) {
        int o = a.o(parcel);
        int i = 0;
        GameEntity gameEntity = null;
        String str = null;
        String str2 = null;
        long j = 0;
        String str3 = null;
        long j2 = 0;
        String str4 = null;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        byte[] bArr = null;
        ArrayList arrayList = null;
        String str5 = null;
        byte[] bArr2 = null;
        int i5 = 0;
        Bundle bundle = null;
        int i6 = 0;
        boolean z = false;
        String str6 = null;
        String str7 = null;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    gameEntity = (GameEntity) a.a(parcel, n, GameEntity.CREATOR);
                    break;
                case MMAdView.TRANSITION_UP:
                    str = a.n(parcel, n);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    str2 = a.n(parcel, n);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    j = a.i(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    str3 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    j2 = a.i(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    str4 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    i2 = a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                    i3 = a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_EXPAND:
                    i4 = a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_RESIZE:
                    bArr = a.q(parcel, n);
                    break;
                case ApiEventType.API_MRAID_CLOSE:
                    arrayList = a.c(parcel, n, ParticipantEntity.CREATOR);
                    break;
                case ApiEventType.API_MRAID_IS_VIEWABLE:
                    str5 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_PLACEMENT_TYPE:
                    bArr2 = a.q(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION:
                    i5 = a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_SCREEN_SIZE:
                    bundle = a.p(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_CURRENT_POSITION:
                    i6 = a.g(parcel, n);
                    break;
                case Encoder.LINE_GROUPS:
                    z = a.c(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_MAX_SIZE:
                    str6 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_POST_TO_SOCIAL:
                    str7 = a.n(parcel, n);
                    break;
                case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                    i = a.g(parcel, n);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new TurnBasedMatchEntity(i, gameEntity, str, str2, j, str3, j2, str4, i2, i3, i4, bArr, arrayList, str5, bArr2, i5, bundle, i6, z, str6, str7);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public TurnBasedMatchEntity[] newArray(int size) {
        return new TurnBasedMatchEntity[size];
    }
}