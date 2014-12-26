package com.google.android.gms.games.internal.game;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.millennialmedia.android.MMAdView;

public class GameBadgeEntityCreator implements Creator<GameBadgeEntity> {
    static void a(GameBadgeEntity gameBadgeEntity, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, gameBadgeEntity.getType());
        b.c(parcel, GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE, gameBadgeEntity.getVersionCode());
        b.a(parcel, (int)MMAdView.TRANSITION_UP, gameBadgeEntity.getTitle(), false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, gameBadgeEntity.getDescription(), false);
        b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, gameBadgeEntity.getIconImageUri(), i, false);
        b.F(parcel, p);
    }

    public GameBadgeEntity ar(Parcel parcel) {
        int i = 0;
        Uri uri = null;
        int o = a.o(parcel);
        String str = null;
        String str2 = null;
        int i2 = 0;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    str2 = a.n(parcel, n);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    str = a.n(parcel, n);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    uri = (Uri) a.a(parcel, n, Uri.CREATOR);
                    break;
                case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                    i2 = a.g(parcel, n);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new GameBadgeEntity(i2, i, str2, str, uri);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public GameBadgeEntity[] bg(int i) {
        return new GameBadgeEntity[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return ar(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return bg(x0);
    }
}