package com.android.volley.toolbox;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import com.android.volley.RequestQueue;
import java.io.File;

public class Volley {
    private static final String DEFAULT_CACHE_DIR = "audios";

    public static RequestQueue newRequestQueue(Context context) {
        return newRequestQueue(context, null);
    }

    public static RequestQueue newRequestQueue(Context context, HttpStack stack) {
        File cDir = context.getCacheDir();
        if (!cDir.exists()) {
            cDir.mkdirs();
        }
        File cacheDir = new File(cDir, DEFAULT_CACHE_DIR);
        String str = "volley/0";
        try {
            String packageName = context.getPackageName();
            str = new StringBuilder(String.valueOf(packageName)).append("/").append(context.getPackageManager().getPackageInfo(packageName, 0).versionCode).toString();
        } catch (NameNotFoundException e) {
        }
        if (stack == null) {
            stack = new HurlStack();
        }
        RequestQueue queue = new RequestQueue(new DiskBasedCache(cacheDir), new BasicNetwork(stack));
        queue.start();
        return queue;
    }
}