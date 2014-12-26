package com.mopub.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import com.mopub.common.DiskLruCache.Editor;
import com.mopub.common.DiskLruCache.Snapshot;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.DeviceUtils;
import com.mopub.common.util.Streams;
import com.mopub.common.util.Utils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CacheService {
    private static final int APP_VERSION = 1;
    private static final int DISK_CACHE_INDEX = 0;
    static final String UNIQUE_CACHE_NAME = "mopub-cache";
    private static final int VALUE_COUNT = 1;
    private static BitmapLruCache sBitmapLruCache;
    private static DiskLruCache sDiskLruCache;

    public static interface DiskLruCacheGetListener {
        void onComplete(String str, byte[] bArr);
    }

    private static class DiskLruCacheGetTask extends AsyncTask<Void, Void, byte[]> {
        private final com.mopub.common.CacheService.DiskLruCacheGetListener mDiskLruCacheGetListener;
        private final String mKey;

        DiskLruCacheGetTask(String key, com.mopub.common.CacheService.DiskLruCacheGetListener diskLruCacheGetListener) {
            this.mDiskLruCacheGetListener = diskLruCacheGetListener;
            this.mKey = key;
        }

        protected byte[] doInBackground(Void... voids) {
            return CacheService.getFromDiskCache(this.mKey);
        }

        protected void onCancelled() {
            if (this.mDiskLruCacheGetListener != null) {
                this.mDiskLruCacheGetListener.onComplete(this.mKey, null);
            }
        }

        protected void onPostExecute(byte[] bytes) {
            if (isCancelled()) {
                onCancelled();
            } else if (this.mDiskLruCacheGetListener != null) {
                this.mDiskLruCacheGetListener.onComplete(this.mKey, bytes);
            }
        }
    }

    private static class DiskLruCachePutTask extends AsyncTask<Void, Void, Void> {
        private final byte[] mContent;
        private final String mKey;

        DiskLruCachePutTask(String key, byte[] content) {
            this.mKey = key;
            this.mContent = content;
        }

        protected Void doInBackground(Void... voids) {
            CacheService.putToDiskCache(this.mKey, this.mContent);
            return null;
        }
    }

    private static class BitmapLruCache extends LruCache<String, Bitmap> {
        public BitmapLruCache(int maxSize) {
            super(maxSize);
        }

        protected int sizeOf(String key, Bitmap bitmap) {
            return bitmap != null ? bitmap.getRowBytes() * bitmap.getHeight() : super.sizeOf(key, bitmap);
        }
    }

    @Deprecated
    @VisibleForTesting
    public static void clearAndNullCaches() {
        if (sDiskLruCache != null) {
            try {
                sDiskLruCache.delete();
                sDiskLruCache = null;
            } catch (IOException e) {
                sDiskLruCache = null;
            }
        }
        if (sBitmapLruCache != null) {
            sBitmapLruCache.evictAll();
            sBitmapLruCache = null;
        }
    }

    public static boolean containsKeyDiskCache(String key) {
        if (sDiskLruCache == null) {
            return false;
        }
        try {
            return sDiskLruCache.get(createValidDiskCacheKey(key)) != null;
        } catch (Exception e) {
            return false;
        }
    }

    public static String createValidDiskCacheKey(String key) {
        return Utils.sha1(key);
    }

    @Deprecated
    @VisibleForTesting
    public static LruCache<String, Bitmap> getBitmapLruCache() {
        return sBitmapLruCache;
    }

    public static File getDiskCacheDirectory(Context context) {
        return new File(new StringBuilder(String.valueOf(context.getCacheDir().getPath())).append(File.separator).append(UNIQUE_CACHE_NAME).toString());
    }

    @Deprecated
    @VisibleForTesting
    public static DiskLruCache getDiskLruCache() {
        return sDiskLruCache;
    }

    public static String getFilePathDiskCache(String key) {
        return sDiskLruCache == null ? null : sDiskLruCache.getDirectory() + File.separator + createValidDiskCacheKey(key) + "." + DISK_CACHE_INDEX;
    }

    public static Bitmap getFromBitmapCache(String key) {
        return sBitmapLruCache == null ? null : (Bitmap) sBitmapLruCache.get(key);
    }

    public static byte[] getFromDiskCache(String key) {
        if (sDiskLruCache == null) {
            return null;
        }
        byte[] bArr = null;
        Snapshot snapshot = null;
        try {
            snapshot = sDiskLruCache.get(createValidDiskCacheKey(key));
            if (snapshot == null) {
                if (snapshot != null) {
                    snapshot.close();
                }
                return null;
            } else {
                InputStream in = snapshot.getInputStream(DISK_CACHE_INDEX);
                if (in != null) {
                    bArr = new byte[((int) snapshot.getLength(DISK_CACHE_INDEX))];
                    BufferedInputStream buffIn = new BufferedInputStream(in);
                    Streams.readStream(buffIn, bArr);
                    Streams.closeStream(buffIn);
                }
                if (snapshot == null) {
                    return bArr;
                }
                snapshot.close();
                return bArr;
            }
        } catch (Exception e) {
            try {
                MoPubLog.d("Unable to get from DiskLruCache", e);
                if (snapshot == null) {
                    return bArr;
                }
                snapshot.close();
                return bArr;
            } catch (Throwable th) {
                if (snapshot != null) {
                    snapshot.close();
                }
            }
        }
    }

    public static void getFromDiskCacheAsync(String key, DiskLruCacheGetListener diskLruCacheGetListener) {
        new DiskLruCacheGetTask(key, diskLruCacheGetListener).execute(new Void[0]);
    }

    public static void initialize(Context context) {
        initializeBitmapCache(context);
        initializeDiskCache(context);
    }

    public static boolean initializeBitmapCache(Context context) {
        if (context == null) {
            return false;
        }
        if (sBitmapLruCache == null) {
            sBitmapLruCache = new BitmapLruCache(DeviceUtils.memoryCacheSizeBytes(context));
        }
        return true;
    }

    public static boolean initializeDiskCache(Context context) {
        if (context == null) {
            return false;
        }
        if (sDiskLruCache != null) {
            return true;
        }
        File cacheDirectory = getDiskCacheDirectory(context);
        try {
            sDiskLruCache = DiskLruCache.open(cacheDirectory, VALUE_COUNT, VALUE_COUNT, DeviceUtils.diskCacheSizeBytes(cacheDirectory));
            return true;
        } catch (IOException e) {
            MoPubLog.d("Unable to create DiskLruCache", e);
            return true;
        }
    }

    public static void putToBitmapCache(String key, Bitmap bitmap) {
        if (sBitmapLruCache != null) {
            sBitmapLruCache.put(key, bitmap);
        }
    }

    public static boolean putToDiskCache(String key, InputStream content) {
        if (sDiskLruCache == null) {
            return false;
        }
        Editor editor = null;
        try {
            editor = sDiskLruCache.edit(createValidDiskCacheKey(key));
            if (editor == null) {
                return false;
            }
            OutputStream outputStream = new BufferedOutputStream(editor.newOutputStream(DISK_CACHE_INDEX));
            Streams.copyContent(content, outputStream);
            outputStream.flush();
            outputStream.close();
            sDiskLruCache.flush();
            editor.commit();
            return true;
        } catch (Exception e) {
            MoPubLog.d("Unable to put to DiskLruCache", e);
            if (editor == null) {
                return false;
            }
            try {
                editor.abort();
                return false;
            } catch (IOException e2) {
                return false;
            }
        }
    }

    public static boolean putToDiskCache(String key, byte[] content) {
        return putToDiskCache(key, new ByteArrayInputStream(content));
    }

    public static void putToDiskCacheAsync(String key, byte[] content) {
        new DiskLruCachePutTask(key, content).execute(new Void[0]);
    }
}