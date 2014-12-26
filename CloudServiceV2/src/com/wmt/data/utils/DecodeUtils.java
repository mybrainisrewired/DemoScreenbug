package com.wmt.data.utils;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import com.wmt.data.utils.ThreadPool.CancelListener;
import com.wmt.data.utils.ThreadPool.JobContext;
import com.wmt.util.Utils;
import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.InputStream;

public class DecodeUtils {
    private static final String TAG = "DecodeService";

    private static class DecodeCanceller implements CancelListener {
        Options mOptions;

        public DecodeCanceller(Options options) {
            this.mOptions = options;
        }

        public void onCancel() {
            this.mOptions.requestCancelDecode();
        }
    }

    public static Bitmap ensureGLCompatibleBitmap(Bitmap bitmap) {
        if (bitmap == null || bitmap.getConfig() != null) {
            return bitmap;
        }
        Bitmap newBitmap = bitmap.copy(Config.ARGB_8888, false);
        bitmap.recycle();
        return newBitmap;
    }

    public static BitmapRegionDecoder requestCreateBitmapRegionDecoder(JobContext jc, Uri uri, ContentResolver resolver, boolean shareable) {
        ParcelFileDescriptor pfd = null;
        try {
            pfd = resolver.openFileDescriptor(uri, "r");
            BitmapRegionDecoder newInstance = BitmapRegionDecoder.newInstance(pfd.getFileDescriptor(), shareable);
            Utils.closeSilently(pfd);
            return newInstance;
        } catch (Throwable th) {
            Log.w(TAG, th);
            Utils.closeSilently(pfd);
            return null;
        }
    }

    public static BitmapRegionDecoder requestCreateBitmapRegionDecoder(JobContext jc, FileDescriptor fd, boolean shareable) {
        try {
            return BitmapRegionDecoder.newInstance(fd, shareable);
        } catch (Throwable th) {
            Log.w(TAG, th);
            return null;
        }
    }

    public static BitmapRegionDecoder requestCreateBitmapRegionDecoder(JobContext jc, InputStream is, boolean shareable) {
        try {
            return BitmapRegionDecoder.newInstance(is, shareable);
        } catch (Throwable th) {
            Log.w(TAG, "requestCreateBitmapRegionDecoder: " + th);
            return null;
        }
    }

    public static BitmapRegionDecoder requestCreateBitmapRegionDecoder(JobContext jc, String filePath, boolean shareable) {
        try {
            return BitmapRegionDecoder.newInstance(filePath, shareable);
        } catch (Throwable th) {
            Log.w(TAG, th);
            return null;
        }
    }

    public static BitmapRegionDecoder requestCreateBitmapRegionDecoder(JobContext jc, byte[] bytes, int offset, int length, boolean shareable) {
        if (offset < 0 || length <= 0 || offset + length > bytes.length) {
            throw new IllegalArgumentException(String.format("offset = %s, length = %s, bytes = %s", new Object[]{Integer.valueOf(offset), Integer.valueOf(length), Integer.valueOf(bytes.length)}));
        } else {
            try {
                return BitmapRegionDecoder.newInstance(bytes, offset, length, shareable);
            } catch (Throwable th) {
                Log.w(TAG, th);
                return null;
            }
        }
    }

    public static Bitmap requestDecode(JobContext jc, FileDescriptor fd, Options options) {
        if (options == null) {
            options = new Options();
        }
        jc.setCancelListener(new DecodeCanceller(options));
        return ensureGLCompatibleBitmap(BitmapFactory.decodeFileDescriptor(fd, null, options));
    }

    public static Bitmap requestDecode(JobContext jc, FileDescriptor fd, Options options, int targetSize) {
        if (options == null) {
            options = new Options();
        }
        jc.setCancelListener(new DecodeCanceller(options));
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd, null, options);
        if (jc.isCancelled()) {
            return null;
        }
        options.inSampleSize = BitmapUtils.computeSampleSizeLarger(options.outWidth, options.outHeight, targetSize);
        options.inJustDecodeBounds = false;
        return ensureGLCompatibleBitmap(BitmapUtils.resizeDownIfTooBig(BitmapFactory.decodeFileDescriptor(fd, null, options), targetSize, true));
    }

    public static Bitmap requestDecode(JobContext jc, FileDescriptor fileDescriptor, Rect paddings, Options options) {
        if (options == null) {
            options = new Options();
        }
        jc.setCancelListener(new DecodeCanceller(options));
        return ensureGLCompatibleBitmap(BitmapFactory.decodeFileDescriptor(fileDescriptor, paddings, options));
    }

    public static Bitmap requestDecode(JobContext jc, String filePath, Options options) {
        if (options == null) {
            options = new Options();
        }
        jc.setCancelListener(new DecodeCanceller(options));
        return ensureGLCompatibleBitmap(BitmapFactory.decodeFile(filePath, options));
    }

    public static Bitmap requestDecode(JobContext jc, String filePath, Options options, int targetSize) {
        Closeable fis;
        FileInputStream fis2 = null;
        try {
            Closeable fis3 = new FileInputStream(filePath);
            try {
                Bitmap requestDecode = requestDecode(jc, fis3.getFD(), options, targetSize);
                Utils.closeSilently(fis3);
                return requestDecode;
            } catch (Exception e) {
                ex = e;
                fis = fis3;
                Log.w(TAG, ex);
                Utils.closeSilently(fis);
                return null;
            } catch (Throwable th) {
                th = th;
                fis = fis3;
                Utils.closeSilently(fis);
                throw th;
            }
        } catch (Exception e2) {
            ex = e2;
            try {
                Exception ex2;
                Log.w(TAG, ex2);
                Utils.closeSilently(fis);
                return null;
            } catch (Throwable th2) {
                Throwable th3 = th2;
                Utils.closeSilently(fis);
                throw th3;
            }
        }
    }

    public static Bitmap requestDecode(JobContext jc, byte[] bytes, int offset, int length, Options options) {
        if (options == null) {
            options = new Options();
        }
        jc.setCancelListener(new DecodeCanceller(options));
        return ensureGLCompatibleBitmap(BitmapFactory.decodeByteArray(bytes, offset, length, options));
    }

    public static Bitmap requestDecode(JobContext jc, byte[] bytes, Options options) {
        return requestDecode(jc, bytes, 0, bytes.length, options);
    }

    public static Bitmap requestDecodeIfBigEnough(JobContext jc, byte[] data, Options options, int targetSize) {
        if (options == null) {
            options = new Options();
        }
        jc.setCancelListener(new DecodeCanceller(options));
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        if (jc.isCancelled() || options.outWidth < targetSize || options.outHeight < targetSize) {
            return null;
        }
        options.inSampleSize = BitmapUtils.computeSampleSizeLarger(options.outWidth, options.outHeight, targetSize);
        options.inJustDecodeBounds = false;
        return ensureGLCompatibleBitmap(BitmapFactory.decodeByteArray(data, 0, data.length, options));
    }
}