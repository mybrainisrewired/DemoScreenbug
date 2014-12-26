package com.nostra13.universalimageloader.core.assist;

import android.graphics.Bitmap;
import com.nostra13.universalimageloader.cache.memory.MemoryCacheAware;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public final class MemoryCacheUtil {
    private static final String URI_AND_SIZE_SEPARATOR = "_";
    private static final String WIDTH_AND_HEIGHT_SEPARATOR = "x";

    private MemoryCacheUtil() {
    }

    public static Comparator<String> createFuzzyKeyComparator() {
        return new Comparator<String>() {
            public int compare(String key1, String key2) {
                return key1.substring(0, key1.lastIndexOf(URI_AND_SIZE_SEPARATOR)).compareTo(key2.substring(0, key2.lastIndexOf(URI_AND_SIZE_SEPARATOR)));
            }
        };
    }

    public static List<String> findCacheKeysForImageUri(String imageUri, MemoryCacheAware<String, Bitmap> memoryCache) {
        List<String> values = new ArrayList();
        Iterator it = memoryCache.keys().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            if (key.startsWith(imageUri)) {
                values.add(key);
            }
        }
        return values;
    }

    public static List<Bitmap> findCachedBitmapsForImageUri(String imageUri, MemoryCacheAware<String, Bitmap> memoryCache) {
        List<Bitmap> values = new ArrayList();
        Iterator it = memoryCache.keys().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            if (key.startsWith(imageUri)) {
                values.add((Bitmap) memoryCache.get(key));
            }
        }
        return values;
    }

    public static String generateKey(String imageUri, ImageSize targetSize) {
        return new StringBuilder(imageUri).append(URI_AND_SIZE_SEPARATOR).append(targetSize.getWidth()).append(WIDTH_AND_HEIGHT_SEPARATOR).append(targetSize.getHeight()).toString();
    }

    public static void removeFromCache(String imageUri, MemoryCacheAware<String, Bitmap> memoryCache) {
        List<String> keysToRemove = new ArrayList();
        Iterator it = memoryCache.keys().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            if (key.startsWith(imageUri)) {
                keysToRemove.add(key);
            }
        }
        it = keysToRemove.iterator();
        while (it.hasNext()) {
            memoryCache.remove((String) it.next());
        }
    }
}