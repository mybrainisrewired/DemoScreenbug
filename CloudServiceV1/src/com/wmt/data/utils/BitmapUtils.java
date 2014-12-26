package com.wmt.data.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Build.VERSION;
import android.util.Log;
import com.wmt.util.Utils;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;

public class BitmapUtils {
    private static final int COMPRESS_JPEG_QUALITY = 90;
    private static final String TAG = "BitmapUtils";
    public static final int UNCONSTRAINED = -1;

    private BitmapUtils() {
    }

    public static byte[] compressBitmap(Bitmap bitmap) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, COMPRESS_JPEG_QUALITY, os);
        return os.toByteArray();
    }

    public static byte[] compressToBytes(Bitmap bitmap, int quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(65536);
        bitmap.compress(CompressFormat.JPEG, quality, baos);
        return baos.toByteArray();
    }

    private static int computeInitialSampleSize(int w, int h, int minSideLength, int maxNumOfPixels) {
        int lowerBound = 1;
        if (maxNumOfPixels == -1 && minSideLength == -1) {
            return 1;
        }
        if (maxNumOfPixels != -1) {
            lowerBound = (int) Math.ceil(Math.sqrt(((double) (w * h)) / ((double) maxNumOfPixels)));
        }
        return minSideLength != -1 ? Math.max(Math.min(w / minSideLength, h / minSideLength), lowerBound) : lowerBound;
    }

    public static int computeSampleSize(float scale) {
        Utils.assertTrue(scale > 0.0f);
        int initialSize = Math.max(1, (int) Math.ceil((double) (1.0f / scale)));
        return initialSize <= 8 ? Utils.nextPowerOf2(initialSize) : ((initialSize + 7) / 8) * 8;
    }

    public static int computeSampleSize(int width, int height, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(width, height, minSideLength, maxNumOfPixels);
        return initialSize <= 8 ? Utils.nextPowerOf2(initialSize) : ((initialSize + 7) / 8) * 8;
    }

    public static int computeSampleSizeLarger(float scale) {
        int initialSize = (int) Math.floor((double) (1.0f / scale));
        if (initialSize <= 1) {
            return 1;
        }
        return initialSize <= 8 ? Utils.prevPowerOf2(initialSize) : (initialSize / 8) * 8;
    }

    public static int computeSampleSizeLarger(int w, int h, int minSideLength) {
        int initialSize = Math.max(w / minSideLength, h / minSideLength);
        if (initialSize <= 1) {
            return 1;
        }
        return initialSize <= 8 ? Utils.prevPowerOf2(initialSize) : (initialSize / 8) * 8;
    }

    public static Bitmap createVideoThumbnail(String filePath) {
        Class<?> clazz = null;
        Object instance = null;
        try {
            clazz = Class.forName("android.media.MediaMetadataRetriever");
            instance = clazz.newInstance();
            clazz.getMethod("setDataSource", new Class[]{String.class}).invoke(instance, new Object[]{filePath});
            Bitmap bitmap;
            if (VERSION.SDK_INT <= 9) {
                bitmap = (Bitmap) clazz.getMethod("captureFrame", new Class[0]).invoke(instance, new Object[0]);
                if (instance == null) {
                    return bitmap;
                }
                try {
                    clazz.getMethod("release", new Class[0]).invoke(instance, new Object[0]);
                    return bitmap;
                } catch (Exception e) {
                    return bitmap;
                }
            } else {
                byte[] data = (byte[]) clazz.getMethod("getEmbeddedPicture", new Class[0]).invoke(instance, new Object[0]);
                if (data != null) {
                    Bitmap bitmap2 = BitmapFactory.decodeByteArray(data, 0, data.length);
                    if (bitmap2 != null) {
                        if (instance != null) {
                            try {
                                clazz.getMethod("release", new Class[0]).invoke(instance, new Object[0]);
                            } catch (Exception e2) {
                            }
                        }
                        return bitmap2;
                    }
                }
                bitmap = (Bitmap) clazz.getMethod("getFrameAtTime", new Class[0]).invoke(instance, new Object[0]);
                if (instance == null) {
                    return bitmap;
                }
                try {
                    clazz.getMethod("release", new Class[0]).invoke(instance, new Object[0]);
                    return bitmap;
                } catch (Exception e3) {
                    return bitmap;
                }
            }
        } catch (IllegalArgumentException e4) {
            if (instance != null) {
                try {
                    clazz.getMethod("release", new Class[0]).invoke(instance, new Object[0]);
                } catch (Exception e5) {
                }
            }
            return null;
        } catch (RuntimeException e6) {
            if (instance != null) {
                try {
                    clazz.getMethod("release", new Class[0]).invoke(instance, new Object[0]);
                } catch (Exception e7) {
                }
            }
            return null;
        } catch (InstantiationException e8) {
            Log.e(TAG, "createVideoThumbnail", e8);
            if (instance != null) {
                try {
                    clazz.getMethod("release", new Class[0]).invoke(instance, new Object[0]);
                } catch (Exception e9) {
                }
            }
            return null;
        } catch (InvocationTargetException e10) {
            Log.e(TAG, "createVideoThumbnail", e10);
            if (instance != null) {
                try {
                    clazz.getMethod("release", new Class[0]).invoke(instance, new Object[0]);
                } catch (Exception e11) {
                }
            }
            return null;
        } catch (ClassNotFoundException e12) {
            Log.e(TAG, "createVideoThumbnail", e12);
            if (instance != null) {
                try {
                    clazz.getMethod("release", new Class[0]).invoke(instance, new Object[0]);
                } catch (Exception e13) {
                }
            }
            return null;
        } catch (NoSuchMethodException e14) {
            Log.e(TAG, "createVideoThumbnail", e14);
            if (instance != null) {
                try {
                    clazz.getMethod("release", new Class[0]).invoke(instance, new Object[0]);
                } catch (Exception e15) {
                }
            }
            return null;
        } catch (IllegalAccessException e16) {
            Log.e(TAG, "createVideoThumbnail", e16);
            if (instance != null) {
                try {
                    clazz.getMethod("release", new Class[0]).invoke(instance, new Object[0]);
                } catch (Exception e17) {
                }
            }
            return null;
        }
    }

    public static Bitmap cropCenter(Bitmap bitmap, boolean recycle) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width == height) {
            return bitmap;
        }
        int size = Math.min(width, height);
        Bitmap target = Bitmap.createBitmap(size, size, getConfig(bitmap));
        Canvas canvas = new Canvas(target);
        canvas.translate((float) ((size - width) / 2), (float) ((size - height) / 2));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, new Paint(2));
        if (recycle) {
            bitmap.recycle();
        }
        canvas.setBitmap(Utils.s_nullBitmap);
        return target;
    }

    private static Config getConfig(Bitmap bitmap) {
        Config config = bitmap.getConfig();
        return config == null ? Config.ARGB_8888 : config;
    }

    public static boolean isRotationSupported(String mimeType) {
        return mimeType == null ? false : mimeType.toLowerCase().equals("image/jpeg");
    }

    public static boolean isSupportedByRegionDecoder(String mimeType) {
        if (mimeType == null) {
            return false;
        }
        mimeType = mimeType.toLowerCase();
        return (!mimeType.startsWith("image/") || mimeType.equals("image/gif") || mimeType.endsWith("bmp")) ? false : true;
    }

    public static void recycleSilently(Bitmap bitmap) {
        if (bitmap != null) {
            try {
                bitmap.recycle();
            } catch (Throwable th) {
                Log.w(TAG, "unable recycle bitmap", th);
            }
        }
    }

    public static Bitmap resizeBitmapByScale(Bitmap bitmap, float scale, boolean recycle) {
        int width = Math.round(((float) bitmap.getWidth()) * scale);
        int height = Math.round(((float) bitmap.getHeight()) * scale);
        if (width == bitmap.getWidth() && height == bitmap.getHeight()) {
            return bitmap;
        }
        Bitmap target = Bitmap.createBitmap(width, height, getConfig(bitmap));
        Canvas canvas = new Canvas(target);
        canvas.scale(scale, scale);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, new Paint(6));
        if (recycle) {
            bitmap.recycle();
        }
        canvas.setBitmap(Utils.s_nullBitmap);
        return target;
    }

    public static Bitmap resizeDownAndCropCenter(Bitmap bitmap, int size, boolean recycle) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int minSide = Math.min(w, h);
        if (w == h && minSide <= size) {
            return bitmap;
        }
        size = Math.min(size, minSide);
        float scale = Math.max(((float) size) / ((float) bitmap.getWidth()), ((float) size) / ((float) bitmap.getHeight()));
        Bitmap target = Bitmap.createBitmap(size, size, getConfig(bitmap));
        int width = Math.round(((float) bitmap.getWidth()) * scale);
        int height = Math.round(((float) bitmap.getHeight()) * scale);
        Canvas canvas = new Canvas(target);
        canvas.translate(((float) (size - width)) / 2.0f, ((float) (size - height)) / 2.0f);
        canvas.scale(scale, scale);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, new Paint(6));
        if (recycle) {
            bitmap.recycle();
        }
        canvas.setBitmap(Utils.s_nullBitmap);
        return target;
    }

    public static Bitmap resizeDownBySideLength(Bitmap bitmap, int maxLength, boolean recycle) {
        float scale = Math.min(((float) maxLength) / ((float) bitmap.getWidth()), ((float) maxLength) / ((float) bitmap.getHeight()));
        return scale >= 1.0f ? bitmap : resizeBitmapByScale(bitmap, scale, recycle);
    }

    public static Bitmap resizeDownIfTooBig(Bitmap bitmap, int targetSize, boolean recycle) {
        float scale = Math.max(((float) targetSize) / ((float) bitmap.getWidth()), ((float) targetSize) / ((float) bitmap.getHeight()));
        return scale > 0.5f ? bitmap : resizeBitmapByScale(bitmap, scale, recycle);
    }

    public static Bitmap resizeDownToPixels(Bitmap bitmap, int targetPixels, boolean recycle) {
        float scale = (float) Math.sqrt(((double) targetPixels) / ((double) (bitmap.getWidth() * bitmap.getHeight())));
        return scale >= 1.0f ? bitmap : resizeBitmapByScale(bitmap, scale, recycle);
    }

    public static Bitmap rotateBitmap(Bitmap source, int rotation, boolean recycle) {
        if (rotation == 0) {
            return source;
        }
        int w = source.getWidth();
        int h = source.getHeight();
        Matrix m = new Matrix();
        m.postRotate((float) rotation);
        Bitmap bitmap = Bitmap.createBitmap(source, 0, 0, w, h, m, true);
        if (recycle) {
            source.recycle();
        }
        return bitmap;
    }
}