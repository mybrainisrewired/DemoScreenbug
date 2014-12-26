package com.mopub.common.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.MoPubBrowser;
import com.mopub.common.logging.MoPubLog;
import com.mopub.mobileads.MoPubActivity;
import com.mopub.mobileads.MraidActivity;
import com.mopub.mobileads.MraidVideoPlayerActivity;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ManifestUtils {
    private static final List<Class<? extends Activity>> REQUIRED_NATIVE_SDK_ACTIVITIES;
    private static final List<Class<? extends Activity>> REQUIRED_WEB_VIEW_SDK_ACTIVITIES;

    static {
        REQUIRED_WEB_VIEW_SDK_ACTIVITIES = new ArrayList(4);
        REQUIRED_WEB_VIEW_SDK_ACTIVITIES.add(MoPubActivity.class);
        REQUIRED_WEB_VIEW_SDK_ACTIVITIES.add(MraidActivity.class);
        REQUIRED_WEB_VIEW_SDK_ACTIVITIES.add(MraidVideoPlayerActivity.class);
        REQUIRED_WEB_VIEW_SDK_ACTIVITIES.add(MoPubBrowser.class);
        REQUIRED_NATIVE_SDK_ACTIVITIES = new ArrayList(1);
        REQUIRED_NATIVE_SDK_ACTIVITIES.add(MoPubBrowser.class);
    }

    private ManifestUtils() {
    }

    public static void checkNativeActivitiesDeclared(Context context) {
        displayWarningForMissingActivities(context, REQUIRED_NATIVE_SDK_ACTIVITIES);
    }

    public static void checkWebViewActivitiesDeclared(Context context) {
        displayWarningForMissingActivities(context, REQUIRED_WEB_VIEW_SDK_ACTIVITIES);
    }

    static void displayWarningForMissingActivities(Context context, List<Class<? extends Activity>> requiredActivities) {
        if (context != null) {
            List<String> undeclaredActivities = getUndeclaredActivities(context, requiredActivities);
            if (!undeclaredActivities.isEmpty()) {
                if (isDebuggable(context)) {
                    String message = "ERROR: YOUR MOPUB INTEGRATION IS INCOMPLETE.\nCheck logcat and update your AndroidManifest.xml with the correct activities.";
                    Toast toast = Toast.makeText(context, "ERROR: YOUR MOPUB INTEGRATION IS INCOMPLETE.\nCheck logcat and update your AndroidManifest.xml with the correct activities.", 1);
                    toast.setGravity(ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, 0, 0);
                    toast.show();
                }
                logMissingActivities(undeclaredActivities);
            }
        }
    }

    @Deprecated
    static List<Class<? extends Activity>> getRequiredNativeSdkActivities() {
        return REQUIRED_NATIVE_SDK_ACTIVITIES;
    }

    @Deprecated
    static List<Class<? extends Activity>> getRequiredWebViewSdkActivities() {
        return REQUIRED_WEB_VIEW_SDK_ACTIVITIES;
    }

    private static List<String> getUndeclaredActivities(Context context, List<Class<? extends Activity>> requiredActivities) {
        List<String> undeclaredActivities = new ArrayList();
        Iterator it = requiredActivities.iterator();
        while (it.hasNext()) {
            Class<? extends Activity> activityClass = (Class) it.next();
            if (!IntentUtils.deviceCanHandleIntent(context, new Intent(context, activityClass))) {
                undeclaredActivities.add(activityClass.getName());
            }
        }
        return undeclaredActivities;
    }

    static boolean isDebuggable(Context context) {
        return (context == null || context.getApplicationInfo() == null) ? false : Utils.bitMaskContainsFlag(context.getApplicationInfo().flags, MMAdView.TRANSITION_UP);
    }

    private static void logMissingActivities(List<String> undeclaredActivities) {
        StringBuilder stringBuilder = new StringBuilder("AndroidManifest permissions for the following required MoPub activities are missing:\n");
        Iterator it = undeclaredActivities.iterator();
        while (it.hasNext()) {
            stringBuilder.append("\n\t").append((String) it.next());
        }
        stringBuilder.append("\n\nPlease update your manifest to include them.");
        MoPubLog.w(stringBuilder.toString());
    }
}