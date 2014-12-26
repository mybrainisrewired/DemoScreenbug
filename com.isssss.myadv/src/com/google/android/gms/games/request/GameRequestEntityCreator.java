package com.google.android.gms.games.request;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.games.GameEntity;
import com.google.android.gms.games.PlayerEntity;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import java.util.ArrayList;

public class GameRequestEntityCreator implements Creator<GameRequestEntity> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void a(GameRequestEntity gameRequestEntity, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.a(parcel, 1, gameRequestEntity.getGame(), i, false);
        b.c(parcel, GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE, gameRequestEntity.getVersionCode());
        b.a(parcel, (int)MMAdView.TRANSITION_UP, gameRequestEntity.getSender(), i, false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, gameRequestEntity.getData(), false);
        b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, gameRequestEntity.getRequestId(), false);
        b.b(parcel, ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, gameRequestEntity.getRecipients(), false);
        b.c(parcel, ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, gameRequestEntity.getType());
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES, gameRequestEntity.getCreationTimestamp());
        b.a(parcel, (int)ApiEventType.API_MRAID_USE_CUSTOM_CLOSE, gameRequestEntity.getExpirationTimestamp());
        b.a(parcel, (int)ApiEventType.API_MRAID_EXPAND, gameRequestEntity.hK(), false);
        b.c(parcel, ApiEventType.API_MRAID_RESIZE, gameRequestEntity.getStatus());
        b.F(parcel, p);
    }

    public GameRequestEntity createFromParcel(Parcel parcel) {
        int o = a.o(parcel);
        int i = 0;
        GameEntity gameEntity = null;
        PlayerEntity playerEntity = null;
        byte[] bArr = null;
        String str = null;
        ArrayList arrayList = null;
        int i2 = 0;
        long j = 0;
        long j2 = 0;
        Bundle bundle = null;
        int i3 = 0;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    gameEntity = (GameEntity) a.a(parcel, n, GameEntity.CREATOR);
                    break;
                case MMAdView.TRANSITION_UP:
                    playerEntity = (PlayerEntity) a.a(parcel, n, PlayerEntity.CREATOR);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    bArr = a.q(parcel, n);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    str = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    arrayList = a.c(parcel, n, PlayerEntity.CREATOR);
                    break;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    i2 = a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                    j = a.i(parcel, n);
                    break;
                case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                    j2 = a.i(parcel, n);
                    break;
                case ApiEventType.API_MRAID_EXPAND:
                    bundle = a.p(parcel, n);
                    break;
                case ApiEventType.API_MRAID_RESIZE:
                    i3 = a.g(parcel, n);
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
            return new GameRequestEntity(i, gameEntity, playerEntity, bArr, str, arrayList, i2, j, j2, bundle, i3);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public GameRequestEntity[] newArray(int size) {
        return new GameRequestEntity[size];
    }
}