package com.mopub.mobileads.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import com.mopub.common.util.IntentUtils;
import com.mopub.common.util.VersionCode;
import com.mopub.mobileads.MraidVideoPlayerActivity;

public class Mraids {
    public static final String ANDROID_CALENDAR_CONTENT_TYPE = "vnd.android.cursor.item/event";

    public static boolean isCalendarAvailable(Context context) {
        Intent calendarIntent = new Intent("android.intent.action.INSERT").setType(ANDROID_CALENDAR_CONTENT_TYPE);
        return VersionCode.currentApiLevel().isAtLeast(VersionCode.ICE_CREAM_SANDWICH) && IntentUtils.deviceCanHandleIntent(context, calendarIntent);
    }

    public static boolean isInlineVideoAvailable(Context context) {
        return IntentUtils.deviceCanHandleIntent(context, new Intent(context, MraidVideoPlayerActivity.class));
    }

    public static boolean isSmsAvailable(Context context) {
        Intent smsIntent = new Intent("android.intent.action.VIEW");
        smsIntent.setData(Uri.parse("sms:"));
        return IntentUtils.deviceCanHandleIntent(context, smsIntent);
    }

    public static boolean isStorePictureSupported(Context context) {
        return "mounted".equals(Environment.getExternalStorageState()) && context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }

    public static boolean isTelAvailable(Context context) {
        Intent telIntent = new Intent("android.intent.action.DIAL");
        telIntent.setData(Uri.parse("tel:"));
        return IntentUtils.deviceCanHandleIntent(context, telIntent);
    }
}