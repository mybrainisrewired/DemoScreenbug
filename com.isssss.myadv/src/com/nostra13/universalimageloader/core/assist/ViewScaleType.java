package com.nostra13.universalimageloader.core.assist;

import android.widget.ImageView;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public enum ViewScaleType {
    FIT_INSIDE,
    CROP;

    static {
        FIT_INSIDE = new ViewScaleType("FIT_INSIDE", 0);
        CROP = new ViewScaleType("CROP", 1);
        ENUM$VALUES = new ViewScaleType[]{FIT_INSIDE, CROP};
    }

    public static ViewScaleType fromImageView(ImageView imageView) {
        switch ($SWITCH_TABLE$android$widget$ImageView$ScaleType()[imageView.getScaleType().ordinal()]) {
            case MMAdView.TRANSITION_DOWN:
            case MMAdView.TRANSITION_RANDOM:
            case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
            case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
            case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                return FIT_INSIDE;
            default:
                return CROP;
        }
    }
}