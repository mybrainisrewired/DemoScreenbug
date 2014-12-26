package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.internal.ih.c;
import com.google.android.gms.internal.ih.d;
import com.google.android.gms.internal.ih.f;
import com.google.android.gms.internal.ih.g;
import com.google.android.gms.internal.ih.h;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ii implements Creator<ih> {
    static void a(ih ihVar, Parcel parcel, int i) {
        int p = b.p(parcel);
        Set ja = ihVar.ja();
        if (ja.contains(Integer.valueOf(1))) {
            b.c(parcel, 1, ihVar.getVersionCode());
        }
        if (ja.contains(Integer.valueOf(MMAdView.TRANSITION_UP))) {
            b.a(parcel, (int)MMAdView.TRANSITION_UP, ihVar.getAboutMe(), true);
        }
        if (ja.contains(Integer.valueOf(MMAdView.TRANSITION_DOWN))) {
            b.a(parcel, (int)MMAdView.TRANSITION_DOWN, ihVar.jv(), i, true);
        }
        if (ja.contains(Integer.valueOf(MMAdView.TRANSITION_RANDOM))) {
            b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, ihVar.getBirthday(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, ihVar.getBraggingRights(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES))) {
            b.c(parcel, ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, ihVar.getCircledByCount());
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, ihVar.jw(), i, true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, ihVar.getCurrentLocation(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES, ihVar.getDisplayName(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_RESIZE))) {
            b.c(parcel, ApiEventType.API_MRAID_RESIZE, ihVar.getGender());
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_IS_VIEWABLE))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_IS_VIEWABLE, ihVar.getId(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_PLACEMENT_TYPE))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_GET_PLACEMENT_TYPE, ihVar.jx(), i, true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_ORIENTATION))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION, ihVar.isPlusUser());
        }
        if (ja.contains(Integer.valueOf(Encoder.LINE_GROUPS))) {
            b.a(parcel, (int)Encoder.LINE_GROUPS, ihVar.jy(), i, true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_CURRENT_POSITION))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_GET_CURRENT_POSITION, ihVar.getLanguage(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_POST_TO_SOCIAL))) {
            b.c(parcel, ApiEventType.API_MRAID_POST_TO_SOCIAL, ihVar.getObjectType());
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_MAX_SIZE))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_GET_MAX_SIZE, ihVar.getNickname(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_STORE_PICTURE))) {
            b.b(parcel, ApiEventType.API_MRAID_STORE_PICTURE, ihVar.jA(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_SUPPORTS))) {
            b.b(parcel, ApiEventType.API_MRAID_SUPPORTS, ihVar.jz(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_GALLERY_IMAGE))) {
            b.c(parcel, ApiEventType.API_MRAID_GET_GALLERY_IMAGE, ihVar.getRelationshipStatus());
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_TAKE_CAMERA_PICTURE))) {
            b.c(parcel, ApiEventType.API_MRAID_TAKE_CAMERA_PICTURE, ihVar.getPlusOneCount());
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_SEND_SMS))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_SEND_SMS, ihVar.getUrl(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_VIBRATE))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_VIBRATE, ihVar.getTagline(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_MAKE_CALL))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_MAKE_CALL, ihVar.isVerified());
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_SEND_MAIL))) {
            b.b(parcel, ApiEventType.API_MRAID_SEND_MAIL, ihVar.jB(), true);
        }
        b.F(parcel, p);
    }

    public ih aN(Parcel parcel) {
        int o = a.o(parcel);
        Set hashSet = new HashSet();
        int i = 0;
        String str = null;
        ih.a aVar = null;
        String str2 = null;
        String str3 = null;
        int i2 = 0;
        ih.b bVar = null;
        String str4 = null;
        String str5 = null;
        int i3 = 0;
        String str6 = null;
        c cVar = null;
        boolean z = false;
        String str7 = null;
        d dVar = null;
        String str8 = null;
        int i4 = 0;
        List list = null;
        List list2 = null;
        int i5 = 0;
        int i6 = 0;
        String str9 = null;
        String str10 = null;
        List list3 = null;
        boolean z2 = false;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i = a.g(parcel, n);
                    hashSet.add(Integer.valueOf(1));
                    break;
                case MMAdView.TRANSITION_UP:
                    str = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(MMAdView.TRANSITION_UP));
                    break;
                case MMAdView.TRANSITION_DOWN:
                    ih.a aVar2 = (ih.a) a.a(parcel, n, ih.a.CREATOR);
                    hashSet.add(Integer.valueOf(MMAdView.TRANSITION_DOWN));
                    aVar = aVar2;
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    str2 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(MMAdView.TRANSITION_RANDOM));
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    str3 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES));
                    break;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    i2 = a.g(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES));
                    break;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    ih.b bVar2 = (ih.b) a.a(parcel, n, ih.b.CREATOR);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES));
                    bVar = bVar2;
                    break;
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    str4 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES));
                    break;
                case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                    str5 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES));
                    break;
                case ApiEventType.API_MRAID_RESIZE:
                    i3 = a.g(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_RESIZE));
                    break;
                case ApiEventType.API_MRAID_IS_VIEWABLE:
                    str6 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_IS_VIEWABLE));
                    break;
                case ApiEventType.API_MRAID_GET_PLACEMENT_TYPE:
                    c cVar2 = (c) a.a(parcel, n, c.CREATOR);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_GET_PLACEMENT_TYPE));
                    cVar = cVar2;
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION:
                    z = a.c(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_GET_ORIENTATION));
                    break;
                case ApiEventType.API_MRAID_GET_CURRENT_POSITION:
                    str7 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_GET_CURRENT_POSITION));
                    break;
                case Encoder.LINE_GROUPS:
                    d dVar2 = (d) a.a(parcel, n, (Creator)d.CREATOR);
                    hashSet.add(Integer.valueOf(Encoder.LINE_GROUPS));
                    dVar = dVar2;
                    break;
                case ApiEventType.API_MRAID_GET_MAX_SIZE:
                    str8 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_GET_MAX_SIZE));
                    break;
                case ApiEventType.API_MRAID_POST_TO_SOCIAL:
                    i4 = a.g(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_POST_TO_SOCIAL));
                    break;
                case ApiEventType.API_MRAID_SUPPORTS:
                    list = a.c(parcel, n, f.CREATOR);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_SUPPORTS));
                    break;
                case ApiEventType.API_MRAID_STORE_PICTURE:
                    list2 = a.c(parcel, n, g.CREATOR);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_STORE_PICTURE));
                    break;
                case ApiEventType.API_MRAID_TAKE_CAMERA_PICTURE:
                    i5 = a.g(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_TAKE_CAMERA_PICTURE));
                    break;
                case ApiEventType.API_MRAID_GET_GALLERY_IMAGE:
                    i6 = a.g(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_GET_GALLERY_IMAGE));
                    break;
                case ApiEventType.API_MRAID_VIBRATE:
                    str9 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_VIBRATE));
                    break;
                case ApiEventType.API_MRAID_SEND_SMS:
                    str10 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_SEND_SMS));
                    break;
                case ApiEventType.API_MRAID_SEND_MAIL:
                    list3 = a.c(parcel, n, h.CREATOR);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_SEND_MAIL));
                    break;
                case ApiEventType.API_MRAID_MAKE_CALL:
                    z2 = a.c(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_MAKE_CALL));
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new ih(hashSet, i, str, aVar, str2, str3, i2, bVar, str4, str5, i3, str6, cVar, z, str7, dVar, str8, i4, list, list2, i5, i6, str9, str10, list3, z2);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public ih[] bQ(int i) {
        return new ih[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return aN(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return bQ(x0);
    }
}