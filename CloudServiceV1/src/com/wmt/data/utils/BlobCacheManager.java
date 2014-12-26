package com.wmt.data.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class BlobCacheManager {
    private static final String KEY_CACHE_UP_TO_DATE = "cache-up-to-date";
    private static final String TAG = "BlobCacheManager";
    private static HashMap<String, BlobCache> sCacheMap;
    private static boolean sOldCheckDone;

    static {
        sCacheMap = new HashMap();
        sOldCheckDone = false;
    }

    public static BlobCache getCache(Context context, String filename, int maxEntries, int maxBytes, int version) {
        IOException e;
        synchronized (sCacheMap) {
            BlobCache cache;
            if (!sOldCheckDone) {
                removeOldFilesIfNecessary(context);
                sOldCheckDone = true;
            }
            BlobCache cache2 = (BlobCache) sCacheMap.get(filename);
            if (cache2 == null) {
                File cacheDir = context.getExternalCacheDir();
                if (cacheDir == null) {
                    return null;
                } else {
                    try {
                        cache = new BlobCache(cacheDir.getAbsolutePath() + "/" + filename, maxEntries, maxBytes, false, version);
                        try {
                            sCacheMap.put(filename, cache);
                        } catch (IOException e2) {
                            e = e2;
                            Log.e(TAG, "Cannot instantiate cache!", e);
                            return cache;
                        }
                    } catch (IOException e3) {
                        e = e3;
                        cache = cache2;
                        Log.e(TAG, "Cannot instantiate cache!", e);
                        return cache;
                    }
                }
            } else {
                cache = cache2;
            }
            return cache;
        }
    }

    private static void removeOldFilesIfNecessary(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        int n = 0;
        try {
            n = pref.getInt(KEY_CACHE_UP_TO_DATE, 0);
        } catch (Throwable th) {
        }
        if (n == 0) {
            pref.edit().putInt(KEY_CACHE_UP_TO_DATE, 1).commit();
            File cacheDir = context.getExternalCacheDir();
            if (cacheDir != null) {
                String prefix = cacheDir.getAbsolutePath() + "/";
                BlobCache.deleteFiles(prefix + "imgcache");
                BlobCache.deleteFiles(prefix + "rev_geocoding");
                BlobCache.deleteFiles(prefix + "bookmark");
            }
        }
    }
}