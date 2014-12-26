package com.loopme;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.Uri.Builder;
import com.mopub.common.Preconditions;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

class AdRequest {
    private static final String LOG_TAG;
    private static final String PARAM_APPKEY = "ak";
    private static final String PARAM_APP_VERSION = "av";
    private static final String PARAM_CONNECTION_TYPE = "ct";
    private static final String PARAM_DNT = "dnt";
    private static final String PARAM_FAIL = "fail";
    private static final String PARAM_LANGUAGE = "lng";
    private static final String PARAM_MRAID = "mr";
    private static final String PARAM_ORIENTATION = "or";
    private static final String PARAM_SDK_VERSION = "sv";
    private static final String PARAM_TESTMODE = "tm";
    private static final String PARAM_VIEWER_TOKEN = "vt";
    private final Context mContext;
    private boolean mDntPresent;

    static {
        LOG_TAG = AdRequest.class.getSimpleName();
    }

    public AdRequest(Context context) {
        this.mContext = context;
        if (context == null) {
            Utilities.log(LOG_TAG, "Context should not be null", LogLevel.ERROR);
        }
    }

    private String getAppVersion() {
        try {
            return this.mContext.getPackageManager().getPackageInfo(this.mContext.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            Utilities.log(LOG_TAG, new StringBuilder("Can't get app version. Exception: ").append(e.getMessage()).toString(), LogLevel.ERROR);
            return "0.0";
        }
    }

    private String getConnectionType() {
        return ((ConnectivityManager) this.mContext.getSystemService("connectivity")).getActiveNetworkInfo().getTypeName();
    }

    private String getGoogleAdvertisingId() {
        String advId = LoopMe.getInstance(this.mContext).getAdvertisingId();
        if (advId != Preconditions.EMPTY_ARGUMENTS) {
            return advId;
        }
        this.mDntPresent = true;
        if (LoopMe.getInstance(this.mContext).getLoopMeId() == null) {
            LoopMe.getInstance(this.mContext).storeLoopMeId(Long.toHexString(Double.doubleToLongBits(Math.random())));
        }
        return LoopMe.getInstance(this.mContext).getLoopMeId();
    }

    private String getLanguage() {
        return Locale.getDefault().getLanguage();
    }

    private String getMraidSupport() {
        String mraid = "1";
        try {
            Class.forName("com.loopme.MraidView");
            return mraid;
        } catch (ClassNotFoundException e) {
            return "0";
        }
    }

    private String getOrientation() {
        return 2 == this.mContext.getResources().getConfiguration().orientation ? "l" : "p";
    }

    private String getSdkVersion() {
        return LoopMe.SDK_VERSION;
    }

    private boolean isTestModeOn() {
        return BaseLoopMeHolder.get().getTestMode();
    }

    public String getRequestUrl(String appKey) {
        List<String> list = Arrays.asList(LoopMe.URL_MOBILE.split("/"));
        Builder builder = new Builder();
        builder.scheme("http");
        Iterator it = list.iterator();
        while (it.hasNext()) {
            String s = (String) it.next();
            if (list.indexOf(s) == 0) {
                builder.authority(s);
            } else {
                builder.appendPath(s);
            }
        }
        builder.appendQueryParameter(PARAM_APPKEY, appKey).appendQueryParameter(PARAM_CONNECTION_TYPE, getConnectionType()).appendQueryParameter(PARAM_LANGUAGE, getLanguage()).appendQueryParameter(PARAM_SDK_VERSION, getSdkVersion()).appendQueryParameter(PARAM_APP_VERSION, getAppVersion()).appendQueryParameter(PARAM_MRAID, getMraidSupport()).appendQueryParameter(PARAM_ORIENTATION, getOrientation()).appendQueryParameter(PARAM_FAIL, "loopme").appendQueryParameter(PARAM_VIEWER_TOKEN, getGoogleAdvertisingId());
        if (this.mDntPresent) {
            builder.appendQueryParameter(PARAM_DNT, "1");
        }
        if (isTestModeOn()) {
            builder.appendQueryParameter(PARAM_TESTMODE, "1");
        }
        return builder.build().toString();
    }
}