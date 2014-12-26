package com.loopme.utilites;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.inmobi.commons.cache.ProductCacheConfig;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;

public final class ScreenMetrics {
    private static ScreenMetrics mInstance;
    private DisplayMetrics mDisplayMetrics;
    private WindowManager mWindowManager;

    private ScreenMetrics() {
    }

    private int convertDpToPixel(float dp, Context context) {
        return (int) (dp * (((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f));
    }

    public static ScreenMetrics getInstance() {
        if (mInstance == null) {
            mInstance = new ScreenMetrics();
        }
        return mInstance;
    }

    public int getBtnCloseHeight(Context context) {
        switch (getDisplayMetrics(context).densityDpi) {
            case 120:
                return convertDpToPixel(BitmapDescriptorFactory.HUE_ORANGE, context);
            case 160:
                return convertDpToPixel(40.0f, context);
            case 240:
                return convertDpToPixel(35.0f, context);
            case 320:
                return convertDpToPixel(35.0f, context);
            default:
                return convertDpToPixel(35.0f, context);
        }
    }

    public int getCloseTouchAreaHeight(Context context) {
        switch (getDisplayMetrics(context).densityDpi) {
            case 120:
                return convertDpToPixel(BitmapDescriptorFactory.HUE_ORANGE, context);
            case 160:
                return convertDpToPixel(40.0f, context);
            case 240:
                return convertDpToPixel(40.0f, context);
            case 320:
                return convertDpToPixel(45.0f, context);
            default:
                return convertDpToPixel(45.0f, context);
        }
    }

    public DisplayMetrics getDisplayMetrics(Context context) {
        if (this.mDisplayMetrics == null) {
            this.mDisplayMetrics = new DisplayMetrics();
        }
        if (this.mWindowManager == null) {
            this.mWindowManager = (WindowManager) context.getSystemService("window");
        }
        this.mWindowManager.getDefaultDisplay().getMetrics(this.mDisplayMetrics);
        return this.mDisplayMetrics;
    }

    public int getFooterButtonsHeight(Context context) {
        switch (getDisplayMetrics(context).densityDpi) {
            case 120:
            case 160:
                return ApiEventType.API_MRAID_GET_GALLERY_IMAGE;
            case 240:
                return ApiEventType.API_MRAID_PLAY_VIDEO;
            default:
                return ProductCacheConfig.DEFAULT_INTERVAL;
        }
    }

    public int getHeaderHeight(Context context) {
        switch (getDisplayMetrics(context).densityDpi) {
            case 120:
                return ApiEventType.API_MRAID_UNMUTE_AUDIO;
            case 160:
                return ApiEventType.API_MRAID_SET_VIDEO_VOLUME;
            case 240:
                return ProductCacheConfig.DEFAULT_INTERVAL;
            default:
                return 88;
        }
    }
}