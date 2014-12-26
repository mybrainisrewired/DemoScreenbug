package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class id implements Creator<ic> {
    static void a(ic icVar, Parcel parcel, int i) {
        int p = b.p(parcel);
        Set ja = icVar.ja();
        if (ja.contains(Integer.valueOf(1))) {
            b.c(parcel, 1, icVar.getVersionCode());
        }
        if (ja.contains(Integer.valueOf(MMAdView.TRANSITION_UP))) {
            b.a(parcel, (int)MMAdView.TRANSITION_UP, icVar.jb(), i, true);
        }
        if (ja.contains(Integer.valueOf(MMAdView.TRANSITION_DOWN))) {
            b.a(parcel, (int)MMAdView.TRANSITION_DOWN, icVar.getAdditionalName(), true);
        }
        if (ja.contains(Integer.valueOf(MMAdView.TRANSITION_RANDOM))) {
            b.a(parcel, (int)MMAdView.TRANSITION_RANDOM, icVar.jc(), i, true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, icVar.getAddressCountry(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, icVar.getAddressLocality(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, icVar.getAddressRegion(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES))) {
            b.b(parcel, ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, icVar.jd(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES))) {
            b.c(parcel, ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES, icVar.getAttendeeCount());
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_USE_CUSTOM_CLOSE))) {
            b.b(parcel, ApiEventType.API_MRAID_USE_CUSTOM_CLOSE, icVar.je(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_EXPAND))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_EXPAND, icVar.jf(), i, true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_RESIZE))) {
            b.b(parcel, ApiEventType.API_MRAID_RESIZE, icVar.jg(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_CLOSE))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_CLOSE, icVar.getBestRating(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_IS_VIEWABLE))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_IS_VIEWABLE, icVar.getBirthDate(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_PLACEMENT_TYPE))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_GET_PLACEMENT_TYPE, icVar.jh(), i, true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_SCREEN_SIZE))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_GET_SCREEN_SIZE, icVar.getContentSize(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_ORIENTATION))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_GET_ORIENTATION, icVar.getCaption(), true);
        }
        if (ja.contains(Integer.valueOf(Encoder.LINE_GROUPS))) {
            b.b(parcel, Encoder.LINE_GROUPS, icVar.ji(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_CURRENT_POSITION))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_GET_CURRENT_POSITION, icVar.getContentUrl(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_POST_TO_SOCIAL))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_POST_TO_SOCIAL, icVar.getDateModified(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_MAX_SIZE))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_GET_MAX_SIZE, icVar.getDateCreated(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_STORE_PICTURE))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_STORE_PICTURE, icVar.getDescription(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_SUPPORTS))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_SUPPORTS, icVar.getDatePublished(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_GALLERY_IMAGE))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_GET_GALLERY_IMAGE, icVar.getEmbedUrl(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_TAKE_CAMERA_PICTURE))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_TAKE_CAMERA_PICTURE, icVar.getDuration(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_SEND_SMS))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_SEND_SMS, icVar.getFamilyName(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_VIBRATE))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_VIBRATE, icVar.getEndDate(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_MAKE_CALL))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_MAKE_CALL, icVar.jj(), i, true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_SEND_MAIL))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_SEND_MAIL, icVar.getGender(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_ASYNC_PING))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_ASYNC_PING, icVar.getHeight(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_CREATE_CALENDAR_EVENT))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_CREATE_CALENDAR_EVENT, icVar.getGivenName(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_UNMUTE_AUDIO))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_UNMUTE_AUDIO, icVar.jk(), i, true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_PLAY_AUDIO))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_PLAY_AUDIO, icVar.getId(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_MUTE_AUDIO))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_MUTE_AUDIO, icVar.getImage(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_SEEK_AUDIO))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_SEEK_AUDIO, icVar.getLongitude());
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_PAUSE_AUDIO))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_PAUSE_AUDIO, icVar.getName(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_SET_AUDIO_VOLUME))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_SET_AUDIO_VOLUME, icVar.getLatitude());
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_AUDIO_VOLUME))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_GET_AUDIO_VOLUME, icVar.jl(), i, true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_UNMUTE_VIDEO))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_UNMUTE_VIDEO, icVar.getPlayerType(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_IS_VIDEO_MUTED))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_IS_VIDEO_MUTED, icVar.getPostOfficeBoxNumber(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_PLAY_VIDEO))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_PLAY_VIDEO, icVar.jm(), i, true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_MUTE_VIDEO))) {
            b.b(parcel, ApiEventType.API_MRAID_MUTE_VIDEO, icVar.jn(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_SEEK_VIDEO))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_SEEK_VIDEO, icVar.jo(), i, true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_PAUSE_VIDEO))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_PAUSE_VIDEO, icVar.getStartDate(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_SET_VIDEO_VOLUME))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_SET_VIDEO_VOLUME, icVar.getPostalCode(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_VIDEO_VOLUME))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_GET_VIDEO_VOLUME, icVar.getRatingValue(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_MIC_INTENSITY))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_GET_MIC_INTENSITY, icVar.getThumbnailUrl(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_CLOSE_VIDEO))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_CLOSE_VIDEO, icVar.jp(), i, true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_SHOW_VIDEO))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_SHOW_VIDEO, icVar.getText(), true);
        }
        if (ja.contains(Integer.valueOf(ApiEventType.API_MRAID_HIDE_VIDEO))) {
            b.a(parcel, (int)ApiEventType.API_MRAID_HIDE_VIDEO, icVar.getStreetAddress(), true);
        }
        if (ja.contains(Integer.valueOf(55))) {
            b.a(parcel, 55, icVar.getWidth(), true);
        }
        if (ja.contains(Integer.valueOf(54))) {
            b.a(parcel, 54, icVar.getUrl(), true);
        }
        if (ja.contains(Integer.valueOf(53))) {
            b.a(parcel, 53, icVar.getType(), true);
        }
        if (ja.contains(Integer.valueOf(52))) {
            b.a(parcel, 52, icVar.getTickerSymbol(), true);
        }
        if (ja.contains(Integer.valueOf(56))) {
            b.a(parcel, 56, icVar.getWorstRating(), true);
        }
        b.F(parcel, p);
    }

    public ic aL(Parcel parcel) {
        int o = a.o(parcel);
        Set hashSet = new HashSet();
        int i = 0;
        ic icVar = null;
        List list = null;
        ic icVar2 = null;
        String str = null;
        String str2 = null;
        String str3 = null;
        List list2 = null;
        int i2 = 0;
        List list3 = null;
        ic icVar3 = null;
        List list4 = null;
        String str4 = null;
        String str5 = null;
        ic icVar4 = null;
        String str6 = null;
        String str7 = null;
        String str8 = null;
        List list5 = null;
        String str9 = null;
        String str10 = null;
        String str11 = null;
        String str12 = null;
        String str13 = null;
        String str14 = null;
        String str15 = null;
        String str16 = null;
        String str17 = null;
        ic icVar5 = null;
        String str18 = null;
        String str19 = null;
        String str20 = null;
        String str21 = null;
        ic icVar6 = null;
        double d = 0.0d;
        ic icVar7 = null;
        double d2 = 0.0d;
        String str22 = null;
        ic icVar8 = null;
        List list6 = null;
        String str23 = null;
        String str24 = null;
        String str25 = null;
        String str26 = null;
        ic icVar9 = null;
        String str27 = null;
        String str28 = null;
        String str29 = null;
        ic icVar10 = null;
        String str30 = null;
        String str31 = null;
        String str32 = null;
        String str33 = null;
        String str34 = null;
        String str35 = null;
        while (parcel.dataPosition() < o) {
            int n = a.n(parcel);
            ic icVar11;
            switch (a.R(n)) {
                case MMAdView.TRANSITION_FADE:
                    i = a.g(parcel, n);
                    hashSet.add(Integer.valueOf(1));
                    break;
                case MMAdView.TRANSITION_UP:
                    icVar11 = (ic) a.a(parcel, n, ic.CREATOR);
                    hashSet.add(Integer.valueOf(MMAdView.TRANSITION_UP));
                    icVar = icVar11;
                    break;
                case MMAdView.TRANSITION_DOWN:
                    list = a.A(parcel, n);
                    hashSet.add(Integer.valueOf(MMAdView.TRANSITION_DOWN));
                    break;
                case MMAdView.TRANSITION_RANDOM:
                    icVar11 = (ic) a.a(parcel, n, ic.CREATOR);
                    hashSet.add(Integer.valueOf(MMAdView.TRANSITION_RANDOM));
                    icVar2 = icVar11;
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    str = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES));
                    break;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    str2 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES));
                    break;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    str3 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES));
                    break;
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    list2 = a.c(parcel, n, ic.CREATOR);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES));
                    break;
                case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                    i2 = a.g(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES));
                    break;
                case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                    list3 = a.c(parcel, n, ic.CREATOR);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_USE_CUSTOM_CLOSE));
                    break;
                case ApiEventType.API_MRAID_EXPAND:
                    icVar11 = (ic) a.a(parcel, n, ic.CREATOR);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_EXPAND));
                    icVar3 = icVar11;
                    break;
                case ApiEventType.API_MRAID_RESIZE:
                    list4 = a.c(parcel, n, ic.CREATOR);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_RESIZE));
                    break;
                case ApiEventType.API_MRAID_CLOSE:
                    str4 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_CLOSE));
                    break;
                case ApiEventType.API_MRAID_IS_VIEWABLE:
                    str5 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_IS_VIEWABLE));
                    break;
                case ApiEventType.API_MRAID_GET_PLACEMENT_TYPE:
                    icVar11 = (ic) a.a(parcel, n, (Creator)ic.CREATOR);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_GET_PLACEMENT_TYPE));
                    icVar4 = icVar11;
                    break;
                case ApiEventType.API_MRAID_GET_ORIENTATION:
                    str6 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_GET_ORIENTATION));
                    break;
                case ApiEventType.API_MRAID_GET_SCREEN_SIZE:
                    str7 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_GET_SCREEN_SIZE));
                    break;
                case ApiEventType.API_MRAID_GET_CURRENT_POSITION:
                    str8 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_GET_CURRENT_POSITION));
                    break;
                case Encoder.LINE_GROUPS:
                    list5 = a.c(parcel, n, ic.CREATOR);
                    hashSet.add(Integer.valueOf(Encoder.LINE_GROUPS));
                    break;
                case ApiEventType.API_MRAID_GET_MAX_SIZE:
                    str9 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_GET_MAX_SIZE));
                    break;
                case ApiEventType.API_MRAID_POST_TO_SOCIAL:
                    str10 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_POST_TO_SOCIAL));
                    break;
                case ApiEventType.API_MRAID_SUPPORTS:
                    str11 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_SUPPORTS));
                    break;
                case ApiEventType.API_MRAID_STORE_PICTURE:
                    str12 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_STORE_PICTURE));
                    break;
                case ApiEventType.API_MRAID_TAKE_CAMERA_PICTURE:
                    str13 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_TAKE_CAMERA_PICTURE));
                    break;
                case ApiEventType.API_MRAID_GET_GALLERY_IMAGE:
                    str14 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_GET_GALLERY_IMAGE));
                    break;
                case ApiEventType.API_MRAID_VIBRATE:
                    str15 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_VIBRATE));
                    break;
                case ApiEventType.API_MRAID_SEND_SMS:
                    str16 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_SEND_SMS));
                    break;
                case ApiEventType.API_MRAID_SEND_MAIL:
                    str17 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_SEND_MAIL));
                    break;
                case ApiEventType.API_MRAID_MAKE_CALL:
                    icVar11 = (ic) a.a(parcel, n, (Creator)ic.CREATOR);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_MAKE_CALL));
                    icVar5 = icVar11;
                    break;
                case ApiEventType.API_MRAID_CREATE_CALENDAR_EVENT:
                    str18 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_CREATE_CALENDAR_EVENT));
                    break;
                case ApiEventType.API_MRAID_ASYNC_PING:
                    str19 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_ASYNC_PING));
                    break;
                case ApiEventType.API_MRAID_PLAY_AUDIO:
                    str20 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_PLAY_AUDIO));
                    break;
                case ApiEventType.API_MRAID_MUTE_AUDIO:
                    str21 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_MUTE_AUDIO));
                    break;
                case ApiEventType.API_MRAID_UNMUTE_AUDIO:
                    icVar11 = (ic) a.a(parcel, n, (Creator)ic.CREATOR);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_UNMUTE_AUDIO));
                    icVar6 = icVar11;
                    break;
                case ApiEventType.API_MRAID_SET_AUDIO_VOLUME:
                    d = a.l(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_SET_AUDIO_VOLUME));
                    break;
                case ApiEventType.API_MRAID_GET_AUDIO_VOLUME:
                    icVar11 = (ic) a.a(parcel, n, (Creator)ic.CREATOR);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_GET_AUDIO_VOLUME));
                    icVar7 = icVar11;
                    break;
                case ApiEventType.API_MRAID_SEEK_AUDIO:
                    d2 = a.l(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_SEEK_AUDIO));
                    break;
                case ApiEventType.API_MRAID_PAUSE_AUDIO:
                    str22 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_PAUSE_AUDIO));
                    break;
                case ApiEventType.API_MRAID_PLAY_VIDEO:
                    icVar11 = (ic) a.a(parcel, n, (Creator)ic.CREATOR);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_PLAY_VIDEO));
                    icVar8 = icVar11;
                    break;
                case ApiEventType.API_MRAID_MUTE_VIDEO:
                    list6 = a.c(parcel, n, ic.CREATOR);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_MUTE_VIDEO));
                    break;
                case ApiEventType.API_MRAID_UNMUTE_VIDEO:
                    str23 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_UNMUTE_VIDEO));
                    break;
                case ApiEventType.API_MRAID_IS_VIDEO_MUTED:
                    str24 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_IS_VIDEO_MUTED));
                    break;
                case ApiEventType.API_MRAID_SET_VIDEO_VOLUME:
                    str25 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_SET_VIDEO_VOLUME));
                    break;
                case ApiEventType.API_MRAID_GET_VIDEO_VOLUME:
                    str26 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_GET_VIDEO_VOLUME));
                    break;
                case ApiEventType.API_MRAID_SEEK_VIDEO:
                    icVar11 = (ic) a.a(parcel, n, (Creator)ic.CREATOR);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_SEEK_VIDEO));
                    icVar9 = icVar11;
                    break;
                case ApiEventType.API_MRAID_PAUSE_VIDEO:
                    str27 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_PAUSE_VIDEO));
                    break;
                case ApiEventType.API_MRAID_HIDE_VIDEO:
                    str28 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_HIDE_VIDEO));
                    break;
                case ApiEventType.API_MRAID_SHOW_VIDEO:
                    str29 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_SHOW_VIDEO));
                    break;
                case ApiEventType.API_MRAID_CLOSE_VIDEO:
                    icVar11 = (ic) a.a(parcel, n, (Creator)ic.CREATOR);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_CLOSE_VIDEO));
                    icVar10 = icVar11;
                    break;
                case ApiEventType.API_MRAID_GET_MIC_INTENSITY:
                    str30 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(ApiEventType.API_MRAID_GET_MIC_INTENSITY));
                    break;
                case 52:
                    str31 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(52));
                    break;
                case 53:
                    str32 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(53));
                    break;
                case 54:
                    str33 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(54));
                    break;
                case 55:
                    str34 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(55));
                    break;
                case 56:
                    str35 = a.n(parcel, n);
                    hashSet.add(Integer.valueOf(56));
                    break;
                default:
                    a.b(parcel, n);
                    break;
            }
        }
        if (parcel.dataPosition() == o) {
            return new ic(hashSet, i, icVar, list, icVar2, str, str2, str3, list2, i2, list3, icVar3, list4, str4, str5, icVar4, str6, str7, str8, list5, str9, str10, str11, str12, str13, str14, str15, str16, str17, icVar5, str18, str19, str20, str21, icVar6, d, icVar7, d2, str22, icVar8, list6, str23, str24, str25, str26, icVar9, str27, str28, str29, icVar10, str30, str31, str32, str33, str34, str35);
        }
        throw new a.a("Overread allowed size end=" + o, parcel);
    }

    public ic[] bO(int i) {
        return new ic[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return aL(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return bO(x0);
    }
}