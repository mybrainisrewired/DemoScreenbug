package com.google.android.gms.games.internal.game;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.games.GameEntity;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import java.util.ArrayList;

public class ExtendedGameEntityCreator implements Creator<ExtendedGameEntity> {
    static void a(ExtendedGameEntity extendedGameEntity, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.a(parcel, 1, extendedGameEntity.hf(), i, false);
        b.c(parcel, GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE, extendedGameEntity.getVersionCode());
        b.c(parcel, MMAdView.TRANSITION_UP, extendedGameEntity.gX());
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, extendedGameEntity.gY());
        b.c(parcel, MMAdView.TRANSITION_RANDOM, extendedGameEntity.gZ());
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, extendedGameEntity.ha());
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, extendedGameEntity.hb());
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, extendedGameEntity.hc(), false);
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, extendedGameEntity.hd());
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES, extendedGameEntity.he(), false);
        b.b(parcel, ApiEventType.API_MRAID_USE_CUSTOM_CLOSE, extendedGameEntity.gW(), false);
        b.F(parcel, p);
    }

    public ExtendedGameEntity aq(Parcel parcel) {
        int o = a.o(parcel);
        int i = 0;
        GameEntity gameEntity = null;
        int i2 = 0;
        boolean z = false;
        int i3 = 0;
        long j = 0;
        long j2 = 0;
        String str = null;
        long j3 = 0;
        String str2 = null;
        ArrayList arrayList = null;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    gameEntity = (GameEntity) a.a(parcel, n, GameEntity.CREATOR);
                    break;
                case MMAdView.TRANSITION_UP:
                    i2 = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    z = a.c(parcel, n);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    i3 = a.g(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    j = a.i(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    j2 = a.i(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    str = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    j3 = a.i(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                    str2 = a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                    arrayList = a.c(parcel, n, GameBadgeEntity.CREATOR);
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
            return new ExtendedGameEntity(i, gameEntity, i2, z, i3, j, j2, str, j3, str2, arrayList);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public ExtendedGameEntity[] be(int i) {
        return new ExtendedGameEntity[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return aq(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return be(x0);
    }
}