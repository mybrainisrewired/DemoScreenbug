package com.nostra13.universalimageloader.utils;

import android.content.Context;
import android.os.Environment;
import com.inmobi.androidsdk.IMBrowserActivity;
import java.io.File;
import java.io.IOException;

public final class StorageUtils {
    private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";
    private static final String INDIVIDUAL_DIR_NAME = "uil-images";

    private StorageUtils() {
    }

    public static File getCacheDirectory(Context context) {
        File appCacheDir = null;
        if ("mounted".equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
            appCacheDir = getExternalCacheDir(context);
        }
        if (appCacheDir == null) {
            appCacheDir = context.getCacheDir();
        }
        if (appCacheDir == null) {
            L.w("Can't define system cache directory! The app should be re-installed.", new Object[0]);
        }
        return appCacheDir;
    }

    private static File getExternalCacheDir(Context context) {
        File appCacheDir = new File(new File(new File(new File(Environment.getExternalStorageDirectory(), "Android"), IMBrowserActivity.EXPANDDATA), context.getPackageName()), "cache");
        if (appCacheDir.exists()) {
            return appCacheDir;
        }
        if (appCacheDir.mkdirs()) {
            try {
                new File(appCacheDir, ".nomedia").createNewFile();
                return appCacheDir;
            } catch (IOException e) {
                L.i("Can't create \".nomedia\" file in application external cache directory", new Object[0]);
                return appCacheDir;
            }
        } else {
            L.w("Unable to create external cache directory", new Object[0]);
            return null;
        }
    }

    public static File getIndividualCacheDirectory(Context context) {
        File cacheDir = getCacheDirectory(context);
        File individualCacheDir = new File(cacheDir, INDIVIDUAL_DIR_NAME);
        return (individualCacheDir.exists() || individualCacheDir.mkdir()) ? individualCacheDir : cacheDir;
    }

    public static File getOwnCacheDirectory(Context context, String cacheDir) {
        File appCacheDir = null;
        if ("mounted".equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
            appCacheDir = new File(Environment.getExternalStorageDirectory(), cacheDir);
        }
        return (appCacheDir == null || !(appCacheDir.exists() || appCacheDir.mkdirs())) ? context.getCacheDir() : appCacheDir;
    }

    private static boolean hasExternalStoragePermission(Context context) {
        return context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION) == 0;
    }
}