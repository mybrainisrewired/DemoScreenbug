package com.google.android.gms.games;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public class GameEntityCreator implements Creator<GameEntity> {
    static void a(GameEntity gameEntity, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.a(parcel, 1, gameEntity.getApplicationId(), false);
        b.a(parcel, (int)MMAdView.TRANSITION_UP, gameEntity.getDisplayName(), false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, gameEntity.getPrimaryCategory(), false);
        b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, gameEntity.getSecondaryCategory(), false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, gameEntity.getDescription(), false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, gameEntity.getDeveloperName(), false);
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, gameEntity.getIconImageUri(), i, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, gameEntity.getHiResImageUri(), i, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES, gameEntity.getFeaturedImageUri(), i, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_USE_CUSTOM_CLOSE, gameEntity.gb());
        b.a(parcel, (int)ApiEventType.API_MRAID_EXPAND, gameEntity.gd());
        b.a(parcel, (int)ApiEventType.API_MRAID_RESIZE, gameEntity.ge(), false);
        b.c(parcel, ApiEventType.API_MRAID_CLOSE, gameEntity.gf());
        b.c(parcel, ApiEventType.API_MRAID_IS_VIEWABLE, gameEntity.getAchievementTotalCount());
        b.c(parcel, ApiEventType.API_MRAID_GET_PLACEMENT_TYPE, gameEntity.getLeaderboardCount());
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_SCREEN_SIZE, gameEntity.isTurnBasedMultiplayerEnabled());
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION, gameEntity.isRealTimeMultiplayerEnabled());
        b.c(parcel, GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE, gameEntity.getVersionCode());
        b.a(parcel, (int)Encoder.LINE_GROUPS, gameEntity.getHiResImageUrl(), false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_CURRENT_POSITION, gameEntity.getIconImageUrl(), false);
        b.a(parcel, (int)ApiEventType.API_MRAID_POST_TO_SOCIAL, gameEntity.isMuted());
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_MAX_SIZE, gameEntity.getFeaturedImageUrl(), false);
        b.a(parcel, (int)ApiEventType.API_MRAID_SUPPORTS, gameEntity.gc());
        b.F(parcel, p);
    }

    public GameEntity[] aS(int i) {
        return new GameEntity[i];
    }

    public GameEntity an(Parcel parcel) {
        int o = a.o(parcel);
        int i = 0;
        String str = null;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        String str6 = null;
        Uri uri = null;
        Uri uri2 = null;
        Uri uri3 = null;
        boolean z = false;
        boolean z2 = false;
        String str7 = null;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        boolean z3 = false;
        boolean z4 = false;
        String str8 = null;
        String str9 = null;
        String str10 = null;
        boolean z5 = false;
        boolean z6 = false;
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
                    str3 = a.n(parcel, n);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    str4 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    str5 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    str6 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    uri = (Uri) a.a(parcel, n, Uri.CREATOR);
                    break;
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    uri2 = (Uri) a.a(parcel, n, Uri.CREATOR);
                    break;
                case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                    uri3 = (Uri) a.a(parcel, n, Uri.CREATOR);
                    break;
                case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                    z = a.c(parcel, n);
                    break;
                case ApiEventType.API_MRAID_EXPAND:
                    z2 = a.c(parcel, n);
                    break;
                case ApiEventType.API_MRAID_RESIZE:
                    str7 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_CLOSE:
                    i2 = a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_IS_VIEWABLE:
                    i3 = a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_PLACEMENT_TYPE:
                    i4 = a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION:
                    z3 = a.c(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_SCREEN_SIZE:
                    z4 = a.c(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_CURRENT_POSITION:
                    str8 = a.n(parcel, n);
                    break;
                case Encoder.LINE_GROUPS:
                    str9 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_MAX_SIZE:
                    str10 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_POST_TO_SOCIAL:
                    z5 = a.c(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SUPPORTS:
                    z6 = a.c(parcel, n);
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
            return new GameEntity(i, str, str2, str3, str4, str5, str6, uri, uri2, uri3, z, z2, str7, i2, i3, i4, z3, z4, str8, str9, str10, z5, z6);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return an(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return aS(x0);
    }
}