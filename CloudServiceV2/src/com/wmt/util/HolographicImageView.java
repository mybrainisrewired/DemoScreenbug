package com.wmt.util;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

public class HolographicImageView extends ImageView {
    private final HolographicViewHelper mHolographicHelper;

    public HolographicImageView(Context context) {
        this(context, null);
    }

    public HolographicImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HolographicImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mHolographicHelper = new HolographicViewHelper(context);
    }

    public void invalidatePressedFocusedStates() {
        this.mHolographicHelper.invalidatePressedFocusedStates(this);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mHolographicHelper.generatePressedFocusedStates(this);
    }

    public void setHoloOutline(boolean only) {
        this.mHolographicHelper.onlyHoloOutline(only);
    }
}