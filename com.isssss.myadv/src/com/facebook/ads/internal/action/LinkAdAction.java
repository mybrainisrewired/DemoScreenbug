package com.facebook.ads.internal.action;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import java.util.Map;

public class LinkAdAction extends AdAction {
    private static final String TAG;
    private final Context context;
    private final Uri uri;

    static {
        TAG = LinkAdAction.class.getSimpleName();
    }

    public LinkAdAction(Context context, Uri uri) {
        this.context = context;
        this.uri = uri;
    }

    public void execute(Map<String, String> intentExtras) {
        logAdClick(this.context, this.uri);
        try {
            this.context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(this.uri.getQueryParameter("link"))));
        } catch (Exception e) {
            Log.d(TAG, "Failed to open market url: " + this.uri.toString(), e);
        }
    }
}