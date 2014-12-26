package com.facebook.ads.internal.action;

import android.content.Context;
import android.net.Uri;

public class AdActionFactory {
    private static final String AD_ACTION_APP_AD = "store";
    private static final String AD_ACTION_LINK_AD = "open_link";

    public static AdAction getAdAction(Context context, Uri uri) {
        String action = uri.getAuthority();
        return AD_ACTION_APP_AD.equals(action) ? uri.getQueryParameter("video_url") != null ? new VideoAppAdAction(context, uri) : new AppAdAction(context, uri) : AD_ACTION_LINK_AD.equals(action) ? new LinkAdAction(context, uri) : null;
    }
}