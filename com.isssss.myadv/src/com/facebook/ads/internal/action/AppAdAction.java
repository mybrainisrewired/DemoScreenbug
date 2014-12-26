package com.facebook.ads.internal.action;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;
import com.facebook.ads.internal.AdInvalidationUtils;
import com.facebook.ads.internal.AppSiteData;
import com.facebook.ads.internal.StringUtils;
import com.google.android.gms.cast.Cast;
import com.google.android.gms.drive.DriveFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AppAdAction extends AdAction {
    private static final String MARKET_DETAIL_URI_FORMAT = "market://details?id=%s";
    private static final String TAG;
    private final Context context;
    private final Uri uri;

    static {
        TAG = AppAdAction.class.getSimpleName();
    }

    public AppAdAction(Context context, Uri uri) {
        this.context = context;
        this.uri = uri;
    }

    private Intent getAppLaunchIntent(AppSiteData appSiteData) {
        if (StringUtils.isNullOrEmpty(appSiteData.getPackageName())) {
            return null;
        }
        if (!AdInvalidationUtils.isNativePackageInstalled(this.context, appSiteData.getPackageName())) {
            return null;
        }
        String appLinkUri = appSiteData.getAppLinkUri();
        if (!StringUtils.isNullOrEmpty(appLinkUri) && (appLinkUri.startsWith("tel:") || appLinkUri.startsWith("telprompt:"))) {
            return new Intent("android.intent.action.CALL", Uri.parse(appLinkUri));
        }
        PackageManager pm = this.context.getPackageManager();
        if (StringUtils.isNullOrEmpty(appSiteData.getClassName()) && StringUtils.isNullOrEmpty(appLinkUri)) {
            return pm.getLaunchIntentForPackage(appSiteData.getPackageName());
        }
        Intent unresolvedAppIntent = getAppLinkIntentUnresolved(appSiteData);
        List<ResolveInfo> resolved = pm.queryIntentActivities(unresolvedAppIntent, Cast.MAX_MESSAGE_LENGTH);
        if (unresolvedAppIntent.getComponent() == null) {
            Iterator i$ = resolved.iterator();
            while (i$.hasNext()) {
                ResolveInfo ri = (ResolveInfo) i$.next();
                if (ri.activityInfo.packageName.equals(appSiteData.getPackageName())) {
                    unresolvedAppIntent.setComponent(new ComponentName(ri.activityInfo.packageName, ri.activityInfo.name));
                    break;
                }
            }
        }
        return (resolved.isEmpty() || unresolvedAppIntent.getComponent() == null) ? null : unresolvedAppIntent;
    }

    private Intent getAppLinkIntentUnresolved(AppSiteData appSiteData) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(DriveFile.MODE_READ_ONLY);
        if (!(StringUtils.isNullOrEmpty(appSiteData.getPackageName()) || StringUtils.isNullOrEmpty(appSiteData.getClassName()))) {
            intent.setComponent(new ComponentName(appSiteData.getPackageName(), appSiteData.getClassName()));
        }
        if (!StringUtils.isNullOrEmpty(appSiteData.getAppLinkUri())) {
            intent.setData(Uri.parse(appSiteData.getAppLinkUri()));
        }
        return intent;
    }

    private List<AppSiteData> getAppsiteDatas() {
        String appsiteDataString = this.uri.getQueryParameter("appsite_data");
        if (StringUtils.isNullOrEmpty(appsiteDataString) || "[]".equals(appsiteDataString)) {
            return null;
        }
        List<AppSiteData> appSiteDatas = new ArrayList();
        try {
            JSONArray appsiteDataArray = new JSONObject(appsiteDataString).optJSONArray("android");
            if (appsiteDataArray == null) {
                return appSiteDatas;
            }
            int i = 0;
            while (i < appsiteDataArray.length()) {
                AppSiteData appSiteData = AppSiteData.fromJSONObject(appsiteDataArray.optJSONObject(i));
                if (appSiteData != null) {
                    appSiteDatas.add(appSiteData);
                }
                i++;
            }
            return appSiteDatas;
        } catch (JSONException e) {
            Log.w(TAG, "Error parsing appsite_data", e);
            return appSiteDatas;
        }
    }

    public void execute(Map<String, String> intentExtras) {
        logAdClick(this.context, this.uri);
        List<Intent> appLaunchIntents = getAppLaunchIntents();
        if (appLaunchIntents != null) {
            Iterator i$ = appLaunchIntents.iterator();
            while (i$.hasNext()) {
                try {
                    this.context.startActivity((Intent) i$.next());
                    return;
                } catch (Exception e) {
                    Log.d(TAG, "Failed to open app intent, falling back", e);
                }
            }
        }
        goToMarketURL();
    }

    protected List<Intent> getAppLaunchIntents() {
        List<AppSiteData> appSiteDatas = getAppsiteDatas();
        List<Intent> intents = new ArrayList();
        if (appSiteDatas != null) {
            Iterator i$ = appSiteDatas.iterator();
            while (i$.hasNext()) {
                Intent intent = getAppLaunchIntent((AppSiteData) i$.next());
                if (intent != null) {
                    intents.add(intent);
                }
            }
        }
        return intents;
    }

    protected Uri getMarketUri() {
        String storeId = this.uri.getQueryParameter("store_id");
        return Uri.parse(String.format(MARKET_DETAIL_URI_FORMAT, new Object[]{storeId}));
    }

    public void goToMarketURL() {
        try {
            this.context.startActivity(new Intent("android.intent.action.VIEW", getMarketUri()));
        } catch (Exception e) {
            Log.d(TAG, "Failed to open market url: " + this.uri.toString(), e);
            fallbackUrl = this.uri.getQueryParameter("store_url_web_fallback");
            String fallbackUrl2;
            if (fallbackUrl2 != null && fallbackUrl2.length() > 0) {
                try {
                    this.context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(fallbackUrl2)));
                } catch (Exception e2) {
                    Log.d(TAG, "Failed to open fallback url: " + fallbackUrl2, e2);
                }
            }
        }
    }
}