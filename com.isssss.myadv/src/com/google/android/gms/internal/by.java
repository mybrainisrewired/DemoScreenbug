package com.google.android.gms.internal;

import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.AdRequest.Gender;
import com.google.ads.AdSize;
import com.google.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.a;
import com.millennialmedia.android.MMAdView;
import java.util.Date;
import java.util.HashSet;

public final class by {

    static /* synthetic */ class AnonymousClass_1 {
        static final /* synthetic */ int[] nL;
        static final /* synthetic */ int[] nM;

        static {
            nM = new int[ErrorCode.values().length];
            try {
                nM[ErrorCode.INTERNAL_ERROR.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                nM[ErrorCode.INVALID_REQUEST.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                nM[ErrorCode.NETWORK_ERROR.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                nM[ErrorCode.NO_FILL.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            nL = new int[Gender.values().length];
            try {
                nL[Gender.FEMALE.ordinal()] = 1;
            } catch (NoSuchFieldError e5) {
            }
            try {
                nL[Gender.MALE.ordinal()] = 2;
            } catch (NoSuchFieldError e6) {
            }
            nL[Gender.UNKNOWN.ordinal()] = 3;
        }
    }

    public static int a(ErrorCode errorCode) {
        switch (AnonymousClass_1.nM[errorCode.ordinal()]) {
            case MMAdView.TRANSITION_UP:
                return 1;
            case MMAdView.TRANSITION_DOWN:
                return MMAdView.TRANSITION_UP;
            case MMAdView.TRANSITION_RANDOM:
                return MMAdView.TRANSITION_DOWN;
            default:
                return 0;
        }
    }

    public static AdSize b(ak akVar) {
        int i = 0;
        AdSize[] adSizeArr = new AdSize[]{AdSize.SMART_BANNER, AdSize.BANNER, AdSize.IAB_MRECT, AdSize.IAB_BANNER, AdSize.IAB_LEADERBOARD, AdSize.IAB_WIDE_SKYSCRAPER};
        while (i < adSizeArr.length) {
            if (adSizeArr[i].getWidth() == akVar.width && adSizeArr[i].getHeight() == akVar.height) {
                return adSizeArr[i];
            }
            i++;
        }
        return new AdSize(a.a(akVar.width, akVar.height, akVar.lS));
    }

    public static MediationAdRequest e(ah ahVar) {
        return new MediationAdRequest(new Date(ahVar.lH), g(ahVar.lI), ahVar.lJ != null ? new HashSet(ahVar.lJ) : null, ahVar.lK, ahVar.lP);
    }

    public static Gender g(int i) {
        switch (i) {
            case MMAdView.TRANSITION_FADE:
                return Gender.MALE;
            case MMAdView.TRANSITION_UP:
                return Gender.FEMALE;
            default:
                return Gender.UNKNOWN;
        }
    }
}