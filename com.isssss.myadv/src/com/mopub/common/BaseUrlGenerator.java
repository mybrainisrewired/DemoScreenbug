package com.mopub.common;

import android.net.Uri;
import com.inmobi.commons.analytics.iat.impl.AdTrackerConstants;
import com.mopub.common.util.Strings;

public abstract class BaseUrlGenerator {
    private static final String IFA_PREFIX = "ifa:";
    private static final String SHA_PREFIX = "sha:";
    private boolean mFirstParam;
    private StringBuilder mStringBuilder;

    private String getParamDelimiter() {
        if (!this.mFirstParam) {
            return "&";
        }
        this.mFirstParam = false;
        return "?";
    }

    protected void addParam(String key, String value) {
        if (value != null && !Strings.isEmpty(value)) {
            this.mStringBuilder.append(getParamDelimiter());
            this.mStringBuilder.append(key);
            this.mStringBuilder.append("=");
            this.mStringBuilder.append(Uri.encode(value));
        }
    }

    public abstract String generateUrlString(String str);

    protected String getFinalUrlString() {
        return this.mStringBuilder.toString();
    }

    protected void initUrlString(String serverHostname, String handlerType) {
        this.mStringBuilder = new StringBuilder(new StringBuilder("http://").append(serverHostname).append(handlerType).toString());
        this.mFirstParam = true;
    }

    protected void setApiVersion(String apiVersion) {
        addParam("v", apiVersion);
    }

    protected void setAppVersion(String appVersion) {
        addParam("av", appVersion);
    }

    protected void setDeviceInfo(String... info) {
        StringBuilder result = new StringBuilder();
        if (info != null && info.length >= 1) {
            int i = 0;
            while (i < info.length - 1) {
                result.append(info[i]).append(",");
                i++;
            }
            result.append(info[info.length - 1]);
            addParam("dn", result.toString());
        }
    }

    protected void setDoNotTrack(boolean dnt) {
        if (dnt) {
            addParam("dnt", "1");
        }
    }

    protected void setExternalStoragePermission(boolean isExternalStoragePermissionGranted) {
        addParam("android_perms_ext_storage", isExternalStoragePermissionGranted ? "1" : "0");
    }

    protected void setUdid(String udid) {
        addParam(AdTrackerConstants.UDID, udid);
    }
}