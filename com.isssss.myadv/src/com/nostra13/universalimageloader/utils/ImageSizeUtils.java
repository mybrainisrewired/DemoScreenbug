package com.nostra13.universalimageloader.utils;

import android.util.DisplayMetrics;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import com.android.volley.DefaultRetryPolicy;
import com.millennialmedia.android.MMAdView;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.ViewScaleType;
import java.lang.reflect.Field;

public final class ImageSizeUtils {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$nostra13$universalimageloader$core$assist$ViewScaleType;

    static /* synthetic */ int[] $SWITCH_TABLE$com$nostra13$universalimageloader$core$assist$ViewScaleType() {
        int[] iArr = $SWITCH_TABLE$com$nostra13$universalimageloader$core$assist$ViewScaleType;
        if (iArr == null) {
            iArr = new int[ViewScaleType.values().length];
            try {
                iArr[ViewScaleType.CROP.ordinal()] = 2;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[ViewScaleType.FIT_INSIDE.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
            $SWITCH_TABLE$com$nostra13$universalimageloader$core$assist$ViewScaleType = iArr;
        }
        return iArr;
    }

    private ImageSizeUtils() {
    }

    public static int computeImageSampleSize(ImageSize srcSize, ImageSize targetSize, ViewScaleType viewScaleType, boolean powerOf2Scale) {
        int srcWidth = srcSize.getWidth();
        int srcHeight = srcSize.getHeight();
        int targetWidth = targetSize.getWidth();
        int targetHeight = targetSize.getHeight();
        int scale = 1;
        int widthScale = srcWidth / targetWidth;
        int heightScale = srcHeight / targetHeight;
        switch ($SWITCH_TABLE$com$nostra13$universalimageloader$core$assist$ViewScaleType()[viewScaleType.ordinal()]) {
            case MMAdView.TRANSITION_FADE:
                if (!powerOf2Scale) {
                    scale = Math.max(widthScale, heightScale);
                }
                while (true) {
                    if (srcWidth / 2 >= targetWidth || srcHeight / 2 >= targetHeight) {
                        srcWidth /= 2;
                        srcHeight /= 2;
                        scale *= 2;
                    }
                }
            case MMAdView.TRANSITION_UP:
                if (!powerOf2Scale) {
                    scale = Math.min(widthScale, heightScale);
                }
                while (srcWidth / 2 >= targetWidth && srcHeight / 2 >= targetHeight) {
                    srcWidth /= 2;
                    srcHeight /= 2;
                    scale *= 2;
                }
                break;
        }
        return scale < 1 ? 1 : scale;
    }

    public static float computeImageScale(ImageSize srcSize, ImageSize targetSize, ViewScaleType viewScaleType, boolean stretch) {
        int destWidth;
        int srcWidth = srcSize.getWidth();
        int srcHeight = srcSize.getHeight();
        int targetWidth = targetSize.getWidth();
        int targetHeight = targetSize.getHeight();
        float widthScale = ((float) srcWidth) / ((float) targetWidth);
        float heightScale = ((float) srcHeight) / ((float) targetHeight);
        int destHeight;
        if ((viewScaleType != ViewScaleType.FIT_INSIDE || widthScale < heightScale) && (viewScaleType != ViewScaleType.CROP || widthScale >= heightScale)) {
            destWidth = (int) (((float) srcWidth) / heightScale);
            destHeight = targetHeight;
        } else {
            destWidth = targetWidth;
            destHeight = (int) (((float) srcHeight) / widthScale);
        }
        return ((stretch || destWidth >= srcWidth || destHeight >= srcHeight) && (!stretch || destWidth == srcWidth || destHeight == srcHeight)) ? DefaultRetryPolicy.DEFAULT_BACKOFF_MULT : ((float) destWidth) / ((float) srcWidth);
    }

    public static ImageSize defineTargetSizeForView(ImageView imageView, int maxImageWidth, int maxImageHeight) {
        int height = 0;
        DisplayMetrics displayMetrics = imageView.getContext().getResources().getDisplayMetrics();
        LayoutParams params = imageView.getLayoutParams();
        int width = (params == null || params.width != -2) ? imageView.getWidth() : 0;
        if (width <= 0 && params != null) {
            width = params.width;
        }
        if (width <= 0) {
            width = getImageViewFieldValue(imageView, "mMaxWidth");
        }
        if (width <= 0) {
            width = maxImageWidth;
        }
        if (width <= 0) {
            width = displayMetrics.widthPixels;
        }
        if (params == null || params.height != -2) {
            height = imageView.getHeight();
        }
        if (height <= 0 && params != null) {
            height = params.height;
        }
        if (height <= 0) {
            height = getImageViewFieldValue(imageView, "mMaxHeight");
        }
        if (height <= 0) {
            height = maxImageHeight;
        }
        if (height <= 0) {
            height = displayMetrics.heightPixels;
        }
        return new ImageSize(width, height);
    }

    private static int getImageViewFieldValue(Object object, String fieldName) {
        int value = 0;
        try {
            Field field = ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldValue = ((Integer) field.get(object)).intValue();
            return (fieldValue <= 0 || fieldValue >= Integer.MAX_VALUE) ? value : fieldValue;
        } catch (Exception e) {
            L.e(e);
            return value;
        }
    }
}