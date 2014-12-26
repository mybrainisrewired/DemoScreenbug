package com.facebook.ads.internal.action;

import android.content.Context;
import android.net.Uri;
import com.facebook.ads.internal.AdUtilities;
import com.facebook.ads.internal.OpenUrlTask;
import com.facebook.ads.internal.StringUtils;
import java.util.Map;

public abstract class AdAction {
    public abstract void execute(Map<String, String> map);

    protected void logAdClick(Context context, Uri uri) {
        if (!StringUtils.isNullOrEmpty(uri.getQueryParameter("native_click_report_url"))) {
            new OpenUrlTask().execute(new String[]{clickReportUrl});
            AdUtilities.displayDebugMessage(context, "Click logged");
        }
    }
}