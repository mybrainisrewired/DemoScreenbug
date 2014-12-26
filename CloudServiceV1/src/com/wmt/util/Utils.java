package com.wmt.util;

import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;
import java.io.Closeable;
import java.io.InterruptedIOException;
import java.util.Random;

public final class Utils {
    private static final String DEBUG_TAG = "Utils";
    private static final double EARTH_RADIUS_METERS = 6367000.0d;
    private static final long INITIALCRC = -1;
    public static final double INVALID_LATLNG = 0.0d;
    private static final boolean IS_DEBUG_BUILD;
    private static final String MASK_STRING = "********************************";
    private static final long POLY64REV = -7661587058870466123L;
    private static final double RAD_PER_DEG = 0.017453292519943295d;
    private static final String TAG = "Utils";
    private static long[] sCrcTable;
    public static final Bitmap s_nullBitmap;

    static {
        sCrcTable = new long[256];
        boolean z = (Build.TYPE.equals("eng") || Build.TYPE.equals("userdebug")) ? true : IS_DEBUG_BUILD;
        IS_DEBUG_BUILD = z;
        s_nullBitmap = Bitmap.createBitmap(1, 1, Config.RGB_565);
        int i = 0;
        while (i < 256) {
            long part = (long) i;
            int j = 0;
            while (j < 8) {
                part = (part >> 1) ^ ((((int) part) & 1) != 0 ? POLY64REV : 0);
                j++;
            }
            sCrcTable[i] = part;
            i++;
        }
    }

    public static double accurateDistanceMeters(double lat1, double lng1, double lat2, double lng2) {
        double dlat = Math.sin(0.5d * (lat2 - lat1));
        double dlng = Math.sin(0.5d * (lng2 - lng1));
        double x = dlat * dlat + ((dlng * dlng) * Math.cos(lat1)) * Math.cos(lat2);
        return (2.0d * Math.atan2(Math.sqrt(x), Math.sqrt(Math.max(INVALID_LATLNG, 1.0d - x)))) * 6367000.0d;
    }

    public static void assertTrue(boolean cond) {
        if (!cond) {
            throw new AssertionError();
        }
    }

    public static void assertTrue(boolean cond, String message, Object... args) {
        if (!cond) {
            if (args.length != 0) {
                message = String.format(message, args);
            }
            throw new AssertionError(message);
        }
    }

    public static int ceilLog2(float value) {
        int i = 0;
        while (i < 31 && ((float) (1 << i)) < value) {
            i++;
        }
        return i;
    }

    public static <T> T checkNotNull(T object) {
        if (object != null) {
            return object;
        }
        throw new NullPointerException();
    }

    public static float clamp(float x, float min, float max) {
        if (x > max) {
            return max;
        }
        return x < min ? min : x;
    }

    public static int clamp(int x, int min, int max) {
        if (x > max) {
            return max;
        }
        return x < min ? min : x;
    }

    public static long clamp(long x, long min, long max) {
        if (x > max) {
            return max;
        }
        return x < min ? min : x;
    }

    public static void closeSilently(Cursor cursor) {
        if (cursor != null) {
            try {
                cursor.close();
            } catch (Throwable th) {
                Log.w(TAG, "fail to close", th);
            }
        }
    }

    public static void closeSilently(ParcelFileDescriptor fd) {
        if (fd != null) {
            try {
                fd.close();
            } catch (Throwable th) {
                Log.w(TAG, "fail to close", th);
            }
        }
    }

    public static void closeSilently(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (Throwable th) {
                Log.w(TAG, "close fail", th);
            }
        }
    }

    public static int compare(long a, long b) {
        if (a < b) {
            return -1;
        }
        return a == b ? 0 : 1;
    }

    public static String[] copyOf(String[] source, int newSize) {
        String[] result = new String[newSize];
        System.arraycopy(source, 0, result, 0, Math.min(source.length, newSize));
        return result;
    }

    public static final long crc64Long(String in) {
        return (in == null || in.length() == 0) ? 0 : crc64Long(getBytes(in));
    }

    public static final long crc64Long(byte[] buffer) {
        long crc = INITIALCRC;
        int k = 0;
        while (k < buffer.length) {
            crc = sCrcTable[(((int) crc) ^ buffer[k]) & 255] ^ (crc >> 8);
            k++;
        }
        return crc;
    }

    public static void debug(String format, Object... args) {
        if (args.length == 0) {
            Log.d(TAG, format);
        } else {
            Log.d(TAG, String.format(format, args));
        }
    }

    public static PendingIntent deserializePendingIntent(byte[] rawPendingIntent) {
        PendingIntent readPendingIntentOrNullFromParcel;
        if (rawPendingIntent != null) {
            try {
                Parcel parcel = Parcel.obtain();
                parcel.unmarshall(rawPendingIntent, 0, rawPendingIntent.length);
                readPendingIntentOrNullFromParcel = PendingIntent.readPendingIntentOrNullFromParcel(parcel);
                if (parcel != null) {
                    parcel.recycle();
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("error parsing PendingIntent");
            } catch (Throwable th) {
                if (0 != 0) {
                    null.recycle();
                }
            }
        } else {
            readPendingIntentOrNullFromParcel = null;
            if (0 != 0) {
                null.recycle();
            }
        }
        return readPendingIntentOrNullFromParcel;
    }

    public static float distance(float x, float y, float sx, float sy) {
        return (float) Math.hypot((double) (x - sx), (double) (y - sy));
    }

    public static String ensureNotNull(String value) {
        return value == null ? "" : value;
    }

    public static boolean equals(Object a, Object b) {
        return (a == b || (a != null && a.equals(b))) ? true : IS_DEBUG_BUILD;
    }

    public static String escapeXml(String s) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        int len = s.length();
        while (i < len) {
            char c = s.charAt(i);
            switch (c) {
                case '\"':
                    sb.append("&quot;");
                    break;
                case '&':
                    sb.append("&amp;");
                    break;
                case '\'':
                    sb.append("&#039;");
                    break;
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                default:
                    sb.append(c);
                    break;
            }
            i++;
        }
        return sb.toString();
    }

    public static double fastDistanceMeters(double latRad1, double lngRad1, double latRad2, double lngRad2) {
        if (Math.abs(latRad1 - latRad2) > 0.017453292519943295d || Math.abs(lngRad1 - lngRad2) > 0.017453292519943295d) {
            return accurateDistanceMeters(latRad1, lngRad1, latRad2, lngRad2);
        }
        double sineLat = latRad1 - latRad2;
        double sineLng = lngRad1 - lngRad2;
        double cosTerms = Math.cos((latRad1 + latRad2) / 2.0d);
        return 6367000.0d * Math.sqrt((sineLat * sineLat) + (((cosTerms * cosTerms) * sineLng) * sineLng));
    }

    public static int floorLog2(float value) {
        int i = 0;
        while (i < 31 && ((float) (1 << i)) <= value) {
            i++;
        }
        return i - 1;
    }

    public static Drawable getApkIcon(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(packageName, 1);
            if (info != null) {
                return pm.getApplicationIcon(info.applicationInfo);
            }
        } catch (NameNotFoundException e) {
            Log.i(TAG, "NameNotFoundException");
        }
        return null;
    }

    public static int getBucketId(String path) {
        return path.toLowerCase().hashCode();
    }

    public static byte[] getBytes(String in) {
        byte[] result = new byte[(in.length() * 2)];
        char[] arr$ = in.toCharArray();
        int len$ = arr$.length;
        int i$ = 0;
        int output = 0;
        while (i$ < len$) {
            char ch = arr$[i$];
            int output2 = output + 1;
            result[output] = (byte) (ch & 255);
            output = output2 + 1;
            result[output2] = (byte) (ch >> 8);
            i$++;
        }
        return result;
    }

    public static String getUserAgent(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return String.format("%s/%s; %s/%s/%s/%s; %s/%s/%s", new Object[]{packageInfo.packageName, packageInfo.versionName, Build.BRAND, Build.DEVICE, Build.MODEL, Build.ID, VERSION.SDK, VERSION.RELEASE, VERSION.INCREMENTAL});
        } catch (NameNotFoundException e) {
            throw new IllegalStateException("getPackageInfo failed");
        }
    }

    public static boolean handleInterrruptedException(Throwable e) {
        if (!(e instanceof InterruptedIOException) && !(e instanceof InterruptedException)) {
            return IS_DEBUG_BUILD;
        }
        Thread.currentThread().interrupt();
        return true;
    }

    public static boolean hasSpaceForSize(long size) {
        if (!"mounted".equals(Environment.getExternalStorageState())) {
            return IS_DEBUG_BUILD;
        }
        try {
            StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
            return ((long) stat.getAvailableBlocks()) * ((long) stat.getBlockSize()) > size ? true : IS_DEBUG_BUILD;
        } catch (Exception e) {
            Log.i(TAG, "Fail to access external storage", e);
            return IS_DEBUG_BUILD;
        }
    }

    public static float interpolateAngle(float source, float target, float progress) {
        float diff = target - source;
        if (diff < 0.0f) {
            diff += 360.0f;
        }
        if (diff > 180.0f) {
            diff -= 360.0f;
        }
        float result = source + diff * progress;
        return result < 0.0f ? result + 360.0f : result;
    }

    public static float interpolateScale(float source, float target, float progress) {
        return (target - source) * progress + source;
    }

    public static boolean isEmulator() {
        String value = Build.HARDWARE;
        return (value == null || !value.equals("goldfish")) ? IS_DEBUG_BUILD : true;
    }

    public static boolean isNullOrEmpty(String exifMake) {
        return TextUtils.isEmpty(exifMake);
    }

    public static boolean isOpaque(int color) {
        return (color >>> 24) == 255 ? true : IS_DEBUG_BUILD;
    }

    public static boolean isPowerOf2(int n) {
        if (n > 0) {
            return ((-n) & n) == n ? true : IS_DEBUG_BUILD;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static boolean isValidLocation(double latitude, double longitude) {
        return (latitude == 0.0d && longitude == 0.0d) ? IS_DEBUG_BUILD : true;
    }

    public static String maskDebugInfo(Object info) {
        if (info == null) {
            return null;
        }
        String s = info.toString();
        return !IS_DEBUG_BUILD ? MASK_STRING.substring(0, Math.min(s.length(), MASK_STRING.length())) : s;
    }

    public static int nextPowerOf2(int n) {
        if (n <= 0 || n > 1073741824) {
            throw new IllegalArgumentException();
        }
        n--;
        n |= n >> 16;
        n |= n >> 8;
        n |= n >> 4;
        n |= n >> 2;
        return n | (n >> 1) + 1;
    }

    public static float parseFloatSafely(String content, float defaultValue) {
        if (content == null) {
            return defaultValue;
        }
        try {
            return Float.parseFloat(content);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static int parseIntSafely(String content, int defaultValue) {
        if (content == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(content);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static int prevPowerOf2(int n) {
        if (n > 0) {
            return Integer.highestOneBit(n);
        }
        throw new IllegalArgumentException();
    }

    public static byte[] serializePendingIntent(PendingIntent pendingIntent) {
        Parcel parcel = null;
        try {
            parcel = Parcel.obtain();
            PendingIntent.writePendingIntentOrNullToParcel(pendingIntent, parcel);
            byte[] marshall = parcel.marshall();
            if (parcel != null) {
                parcel.recycle();
            }
            return marshall;
        } catch (Throwable th) {
            if (parcel != null) {
                parcel.recycle();
            }
        }
    }

    public static void shuffle(int[] array, Random random) {
        int i = array.length;
        while (i > 0) {
            int t = random.nextInt(i);
            if (t != i - 1) {
                int tmp = array[i - 1];
                array[i - 1] = array[t];
                array[t] = tmp;
            }
            i--;
        }
    }

    public static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public static <T> void swap(T[] array, int i, int j) {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public static final double toMile(double meter) {
        return meter / 1609.0d;
    }

    public static void waitWithoutInterrupt(Object object) {
        try {
            object.wait();
        } catch (InterruptedException e) {
            Log.w(TAG, "unexpected interrupt: " + object);
        }
    }
}