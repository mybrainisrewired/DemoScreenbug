package com.wmt.data;

import android.content.Context;
import com.wmt.data.utils.BlobCache;
import com.wmt.data.utils.BlobCacheManager;
import com.wmt.util.Utils;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ImageCacheService {
    private static final String IMAGE_CACHE_FILE = "imgcache";
    private static final int IMAGE_CACHE_MAX_BYTES = 209715200;
    private static final int IMAGE_CACHE_MAX_ENTRIES = 5000;
    private static final int IMAGE_CACHE_VERSION = 3;
    private static final String TAG = "ImageCacheService";
    private BlobCache mCache;

    public static class ImageData {
        public byte[] mData;
        public int mOffset;

        public ImageData(byte[] data, int offset) {
            this.mData = data;
            this.mOffset = offset;
        }
    }

    public ImageCacheService(Context context) {
        this.mCache = BlobCacheManager.getCache(context, IMAGE_CACHE_FILE, IMAGE_CACHE_MAX_ENTRIES, IMAGE_CACHE_MAX_BYTES, IMAGE_CACHE_VERSION);
    }

    private static boolean isSameKey(byte[] key, byte[] buffer) {
        int n = key.length;
        if (buffer.length < n) {
            return false;
        }
        int i = 0;
        while (i < n) {
            if (key[i] != buffer[i]) {
                return false;
            }
            i++;
        }
        return true;
    }

    private static byte[] makeKey(Path path, int type) {
        return Utils.getBytes(path.toString() + "+" + type);
    }

    public ImageData getImageData(Path path, int type) {
        ImageData imageData = null;
        byte[] key = makeKey(path, type);
        long cacheKey = Utils.crc64Long(key);
        try {
            byte[] value;
            synchronized (this.mCache) {
                value = this.mCache.lookup(cacheKey);
            }
            return (value != null && isSameKey(key, value)) ? new ImageData(value, key.length) : imageData;
        } catch (IOException e) {
            return imageData;
        }
    }

    public void putImageData(Path path, int type, byte[] value) {
        byte[] key = makeKey(path, type);
        long cacheKey = Utils.crc64Long(key);
        ByteBuffer buffer = ByteBuffer.allocate(key.length + value.length);
        buffer.put(key);
        buffer.put(value);
        synchronized (this.mCache) {
            try {
                this.mCache.insert(cacheKey, buffer.array());
            } catch (IOException e) {
            }
        }
    }
}