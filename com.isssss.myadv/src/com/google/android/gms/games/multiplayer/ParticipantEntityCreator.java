package com.google.android.gms.games.multiplayer;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.games.PlayerEntity;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public class ParticipantEntityCreator implements Creator<ParticipantEntity> {
    static void a(ParticipantEntity participantEntity, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.a(parcel, 1, participantEntity.getParticipantId(), false);
        b.a(parcel, (int)MMAdView.TRANSITION_UP, participantEntity.getDisplayName(), false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, participantEntity.getIconImageUri(), i, false);
        b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, participantEntity.getHiResImageUri(), i, false);
        b.c(parcel, ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, participantEntity.getStatus());
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, participantEntity.gi(), false);
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, participantEntity.isConnectedToRoom());
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, participantEntity.getPlayer(), i, false);
        b.c(parcel, ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES, participantEntity.getCapabilities());
        b.a(parcel, (int)ApiEventType.API_MRAID_USE_CUSTOM_CLOSE, participantEntity.getResult(), i, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_EXPAND, participantEntity.getIconImageUrl(), false);
        b.a(parcel, (int)ApiEventType.API_MRAID_RESIZE, participantEntity.getHiResImageUrl(), false);
        b.c(parcel, GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE, participantEntity.getVersionCode());
        b.F(parcel, p);
    }

    public ParticipantEntity av(Parcel parcel) {
        int o = a.o(parcel);
        int i = 0;
        String str = null;
        String str2 = null;
        Uri uri = null;
        Uri uri2 = null;
        int i2 = 0;
        String str3 = null;
        boolean z = false;
        PlayerEntity playerEntity = null;
        int i3 = 0;
        ParticipantResult participantResult = null;
        String str4 = null;
        String str5 = null;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    str = a.n(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    str2 = a.n(parcel, n);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    uri = (Uri) a.a(parcel, n, Uri.CREATOR);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    uri2 = (Uri) a.a(parcel, n, Uri.CREATOR);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    i2 = a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    str3 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    z = a.c(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    playerEntity = (PlayerEntity) a.a(parcel, n, PlayerEntity.CREATOR);
                    break;
                case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                    i3 = a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                    participantResult = (ParticipantResult) a.a(parcel, n, ParticipantResult.CREATOR);
                    break;
                case ApiEventType.API_MRAID_EXPAND:
                    str4 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_RESIZE:
                    str5 = a.n(parcel, n);
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
            return new ParticipantEntity(i, str, str2, uri, uri2, i2, str3, z, playerEntity, i3, participantResult, str4, str5);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public ParticipantEntity[] bo(int i) {
        return new ParticipantEntity[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return av(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return bo(x0);
    }
}