package com.google.android.gms.cast;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.images.WebImage;
import com.google.android.gms.common.internal.safeparcel.b;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import java.util.List;

public class a implements Creator<ApplicationMetadata> {
    static void a(ApplicationMetadata applicationMetadata, Parcel parcel, int i) {
        int p = b.p(parcel);
        b.c(parcel, 1, applicationMetadata.getVersionCode());
        b.a(parcel, (int)MMAdView.TRANSITION_UP, applicationMetadata.getApplicationId(), false);
        b.a(parcel, (int)MMAdView.TRANSITION_DOWN, applicationMetadata.getName(), false);
        b.b(parcel, MMAdView.TRANSITION_RANDOM, applicationMetadata.getImages(), false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, applicationMetadata.xK, false);
        b.a(parcel, (int)ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, applicationMetadata.getSenderAppIdentifier(), false);
        b.a(parcel, (int)ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, applicationMetadata.dz(), i, false);
        b.F(parcel, p);
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return j(x0);
    }

    public ApplicationMetadata j(Parcel parcel) {
        Uri uri = null;
        int o = com.google.android.gms.common.internal.safeparcel.a.o(parcel);
        int i = 0;
        String str = null;
        List list = null;
        List list2 = null;
        String str2 = null;
        String str3 = null;
        while (parcel.dataPosition() < o) {
            int n = com.google.android.gms.common.internal.safeparcel.a.n(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i = com.google.android.gms.common.internal.safeparcel.a.g(parcel, n);
                    break;
                case MMAdView.TRANSITION_UP:
                    str3 = com.google.android.gms.common.internal.safeparcel.a.n(parcel, n);
                    break;
                case MMAdView.TRANSITION_DOWN:
                    str2 = com.google.android.gms.common.internal.safeparcel.a.n(parcel, n);
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    list2 = com.google.android.gms.common.internal.safeparcel.a.c(parcel, n, WebImage.CREATOR);
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    list = com.google.android.gms.common.internal.safeparcel.a.A(parcel, n);
                    break;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    str = com.google.android.gms.common.internal.safeparcel.a.n(parcel, n);
                    break;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    uri = (Uri) com.google.android.gms.common.internal.safeparcel.a.a(parcel, n, Uri.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new ApplicationMetadata(i, str3, str2, list2, list, str, uri);
        }
        throw new com.google.android.gms.common.internal.safeparcel.a.a("Overread allowed size end=" + o, parcel);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return w(x0);
    }

    public ApplicationMetadata[] w(int i) {
        return new ApplicationMetadata[i];
    }
}