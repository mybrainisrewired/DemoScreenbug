package com.facebook.ads.internal;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import org.json.JSONArray;

public class AdInvalidationUtils {
    public static boolean isNativePackageInstalled(Context context, String packageName) {
        if (StringUtils.isNullOrEmpty(packageName)) {
            return false;
        }
        try {
            context.getPackageManager().getPackageInfo(packageName, 1);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public static Collection<String> parseDetectionStrings(JSONArray detectionStrings) {
        if (detectionStrings == null || detectionStrings.length() == 0) {
            return null;
        }
        Collection<String> results = new HashSet();
        int i = 0;
        while (i < detectionStrings.length()) {
            results.add(detectionStrings.optString(i));
            i++;
        }
        return results;
    }

    public static boolean shouldInvalidate(Context context, AdDataModel dataModel) {
        AdInvalidationBehavior invalidationBehavior = dataModel.getInvalidationBehavior();
        if (invalidationBehavior == null || invalidationBehavior == AdInvalidationBehavior.NONE) {
            return false;
        }
        boolean packageInstalled = false;
        Collection<String> detectionStrings = dataModel.getDetectionStrings();
        if (detectionStrings == null || detectionStrings.isEmpty()) {
            return false;
        }
        Iterator i$ = detectionStrings.iterator();
        while (i$.hasNext()) {
            if (isNativePackageInstalled(context, (String) i$.next())) {
                packageInstalled = true;
                break;
            }
        }
        if (invalidationBehavior == AdInvalidationBehavior.INSTALLED) {
            return packageInstalled;
        }
        return invalidationBehavior == AdInvalidationBehavior.NOT_INSTALLED && !packageInstalled;
    }
}