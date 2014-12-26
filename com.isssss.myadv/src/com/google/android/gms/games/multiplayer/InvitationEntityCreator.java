package com.google.android.gms.games.multiplayer;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.games.GameEntity;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import java.util.ArrayList;

public class InvitationEntityCreator implements Creator<InvitationEntity> {
    static void a(InvitationEntity invitationEntity, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.a(parcel, 1, invitationEntity.getGame(), i, false);
        b.c(parcel, GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE, invitationEntity.getVersionCode());
        b.a(parcel, (int)MMAdView.TRANSITION_UP, invitationEntity.getInvitationId(), false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, invitationEntity.getCreationTimestamp());
        b.c(parcel, MMAdView.TRANSITION_RANDOM, invitationEntity.getInvitationType());
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, invitationEntity.getInviter(), i, false);
        b.b(parcel, ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, invitationEntity.getParticipants(), false);
        b.c(parcel, ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, invitationEntity.getVariant());
        b.c(parcel, ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, invitationEntity.getAvailableAutoMatchSlots());
        b.F(parcel, p);
    }

    public InvitationEntity au(Parcel parcel) {
        ArrayList arrayList = null;
        int i = 0;
        int o = a.o(parcel);
        long j = 0;
        int i2 = 0;
        ParticipantEntity participantEntity = null;
        int i3 = 0;
        String str = null;
        GameEntity gameEntity = null;
        int i4 = 0;
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
                    j = a.i(parcel, n);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    i3 = a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    participantEntity = (ParticipantEntity) a.a(parcel, n, ParticipantEntity.CREATOR);
                    break;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    arrayList = a.c(parcel, n, ParticipantEntity.CREATOR);
                    break;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    i2 = a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
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
            return new InvitationEntity(i4, gameEntity, str, j, i3, participantEntity, arrayList, i2, i);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public InvitationEntity[] bn(int i) {
        return new InvitationEntity[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return au(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return bn(x0);
    }
}