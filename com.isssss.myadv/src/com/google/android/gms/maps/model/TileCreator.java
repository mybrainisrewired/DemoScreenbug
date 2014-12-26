package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.millennialmedia.android.MMAdView;

public class TileCreator implements Creator<Tile> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void a(Tile tile, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, tile.getVersionCode());
        b.c(parcel, MMAdView.TRANSITION_UP, tile.width);
        b.c(parcel, MMAdView.TRANSITION_DOWN, tile.height);
        b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, tile.data, false);
        b.F(parcel, p);
    }

    public Tile createFromParcel(Parcel parcel) {
        int i = 0;
        int o = a.o(parcel);
        byte[] bArr = null;
        int i2 = 0;
        int i3 = 0;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i3 = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    i2 = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    i = a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    bArr = a.q(parcel, n);
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new Tile(i3, i2, i, bArr);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public Tile[] newArray(int size) {
        return new Tile[size];
    }
}