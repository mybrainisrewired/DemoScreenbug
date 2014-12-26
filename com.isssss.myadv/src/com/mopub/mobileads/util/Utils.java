package com.mopub.mobileads.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.google.android.gms.drive.DriveFile;
import com.mopub.common.logging.MoPubLog;

public class Utils {
    private Utils() {
    }

    public static boolean executeIntent(Context context, Intent intent, String errorMessage) {
        try {
            if (!context instanceof Activity) {
                intent.addFlags(DriveFile.MODE_READ_ONLY);
            }
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            if (errorMessage == null) {
                errorMessage = "Unable to start intent.";
            }
            MoPubLog.d(errorMessage);
            return false;
        }
    }
}