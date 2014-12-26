package com.mopub.common.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.gms.drive.DriveFile;
import com.mopub.common.logging.MoPubLog;

public class IntentUtils {
    private static final String HTTP = "http";
    private static final String HTTPS = "https";
    private static final String MARKET = "market";
    private static final String MARKET_ANDROID_COM = "market.android.com";
    private static final String PLAY_GOOGLE_COM = "play.google.com";
    private static final String TWITTER_APPLICATION_DEEPLINK_URL = "twitter://timeline";

    private IntentUtils() {
    }

    public static boolean canHandleApplicationUrl(Context context, String url) {
        return canHandleApplicationUrl(context, url, true);
    }

    public static boolean canHandleApplicationUrl(Context context, String url, boolean logError) {
        if (deviceCanHandleIntent(context, new Intent("android.intent.action.VIEW", Uri.parse(url)))) {
            return true;
        }
        if (logError) {
            MoPubLog.w(new StringBuilder("Could not handle application specific action: ").append(url).append(". ").append("You may be running in the emulator or another device which does not ").append("have the required application.").toString());
        }
        return false;
    }

    public static boolean canHandleTwitterUrl(Context context) {
        return canHandleApplicationUrl(context, TWITTER_APPLICATION_DEEPLINK_URL, false);
    }

    public static boolean deviceCanHandleIntent(Context context, Intent intent) {
        boolean z = false;
        try {
            return context.getPackageManager().queryIntentActivities(intent, 0).isEmpty() ? z : true;
        } catch (NullPointerException e) {
            return z;
        }
    }

    public static Intent getStartActivityIntent(Context context, Class clazz, Bundle extras) {
        Intent intent = new Intent(context, clazz);
        if (!context instanceof Activity) {
            intent.addFlags(DriveFile.MODE_READ_ONLY);
        }
        if (extras != null) {
            intent.putExtras(extras);
        }
        return intent;
    }

    private static boolean isAppStoreUrl(String url) {
        if (url == null) {
            return false;
        }
        Uri uri = Uri.parse(url);
        String scheme = uri.getScheme();
        String host = uri.getHost();
        if (PLAY_GOOGLE_COM.equals(host) || MARKET_ANDROID_COM.equals(host)) {
            return true;
        }
        return MARKET.equals(scheme);
    }

    public static boolean isDeepLink(String url) {
        return isAppStoreUrl(url) || !isHttpUrl(url);
    }

    public static boolean isHttpUrl(String url) {
        if (url == null) {
            return false;
        }
        String scheme = Uri.parse(url).getScheme();
        return HTTP.equals(scheme) || HTTPS.equals(scheme);
    }
}