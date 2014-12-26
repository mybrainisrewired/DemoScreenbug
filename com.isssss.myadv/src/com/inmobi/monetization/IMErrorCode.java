package com.inmobi.monetization;

import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.inmobi.monetization.internal.AdErrorCode;
import com.millennialmedia.android.MMAdView;

public enum IMErrorCode {
    INVALID_REQUEST("Invalid ad request"),
    INTERNAL_ERROR("An error occurred while fetching the ad"),
    NO_FILL("The ad request was successful, but no ad was returned"),
    DO_MONETIZE("Please load a mediation network"),
    DO_NOTHING("No Ads"),
    NETWORK_ERROR("Ad network failed to retrieve ad");
    private String a;

    static /* synthetic */ class a {
        static final /* synthetic */ int[] a;

        static {
            a = new int[AdErrorCode.values().length];
            try {
                a[AdErrorCode.INVALID_REQUEST.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                a[AdErrorCode.AD_CLICK_IN_PROGRESS.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                a[AdErrorCode.AD_DOWNLOAD_IN_PROGRESS.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                a[AdErrorCode.INVALID_APP_ID.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                a[AdErrorCode.AD_RENDERING_TIMEOUT.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                a[AdErrorCode.INTERNAL_ERROR.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                a[AdErrorCode.NO_FILL.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                a[AdErrorCode.NETWORK_ERROR.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                a[AdErrorCode.DO_MONETIZE.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            a[AdErrorCode.DO_NOTHING.ordinal()] = 10;
        }
    }

    static {
        String str = "Invalid ad request";
        INVALID_REQUEST = new IMErrorCode("INVALID_REQUEST", 0, "Invalid ad request");
        str = "An error occurred while fetching the ad";
        INTERNAL_ERROR = new IMErrorCode("INTERNAL_ERROR", 1, "An error occurred while fetching the ad");
        str = "The ad request was successful, but no ad was returned";
        NO_FILL = new IMErrorCode("NO_FILL", 2, "The ad request was successful, but no ad was returned");
        str = "Please load a mediation network";
        DO_MONETIZE = new IMErrorCode("DO_MONETIZE", 3, "Please load a mediation network");
        str = "No Ads";
        DO_NOTHING = new IMErrorCode("DO_NOTHING", 4, "No Ads");
        String str2 = "Ad network failed to retrieve ad";
        NETWORK_ERROR = new IMErrorCode("NETWORK_ERROR", 5, "Ad network failed to retrieve ad");
        b = new IMErrorCode[]{INVALID_REQUEST, INTERNAL_ERROR, NO_FILL, DO_MONETIZE, DO_NOTHING, NETWORK_ERROR};
    }

    private IMErrorCode(String str) {
        this.a = str;
    }

    static IMErrorCode a(AdErrorCode adErrorCode) {
        IMErrorCode iMErrorCode = INTERNAL_ERROR;
        switch (a.a[adErrorCode.ordinal()]) {
            case MMAdView.TRANSITION_FADE:
            case MMAdView.TRANSITION_UP:
            case MMAdView.TRANSITION_DOWN:
            case MMAdView.TRANSITION_RANDOM:
                iMErrorCode = INVALID_REQUEST;
                break;
            case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
            case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                iMErrorCode = INTERNAL_ERROR;
                break;
            case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                iMErrorCode = NO_FILL;
                break;
            case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                iMErrorCode = NETWORK_ERROR;
                break;
            case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                iMErrorCode = DO_MONETIZE;
                break;
            case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                iMErrorCode = DO_NOTHING;
                break;
            default:
                iMErrorCode = INTERNAL_ERROR;
                break;
        }
        iMErrorCode.setMessage(adErrorCode.toString());
        return iMErrorCode;
    }

    public void setMessage(String str) {
        this.a = str;
    }

    public String toString() {
        return this.a;
    }
}