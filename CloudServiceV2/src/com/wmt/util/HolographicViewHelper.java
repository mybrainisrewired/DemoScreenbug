package com.wmt.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.StateListDrawable;
import android.widget.ImageView;

public class HolographicViewHelper {
    private int mHighlightColor;
    private boolean mOnlyOutline;
    private boolean mStatesUpdated;
    private final Canvas mTempCanvas;

    public HolographicViewHelper(Context context) {
        this.mTempCanvas = new Canvas();
        this.mOnlyOutline = false;
        this.mHighlightColor = context.getResources().getColor(17170450);
    }

    private Bitmap createPressImage(ImageView v, Canvas canvas) {
        int padding = HolographicOutlineHelper.MAX_OUTER_BLUR_RADIUS;
        Bitmap b = Bitmap.createBitmap(v.getWidth() + padding, v.getHeight() + padding, Config.ARGB_8888);
        canvas.setBitmap(b);
        canvas.save();
        v.getDrawable().draw(canvas);
        canvas.restore();
        if (this.mOnlyOutline) {
            new HolographicOutlineHelper().applyOuterBlur(b, canvas, this.mHighlightColor);
        } else {
            canvas.drawColor(this.mHighlightColor, Mode.SRC_IN);
        }
        canvas.setBitmap(null);
        return b;
    }

    public void generatePressedFocusedStates(ImageView v) {
        if (!this.mStatesUpdated && v != null) {
            this.mStatesUpdated = true;
            FastBitmapDrawable d = new FastBitmapDrawable(createPressImage(v, this.mTempCanvas));
            StateListDrawable states = new StateListDrawable();
            states.addState(new int[]{16842919}, d);
            states.addState(new int[]{16842908}, d);
            states.addState(new int[0], v.getDrawable());
            v.setImageDrawable(states);
        }
    }

    public void invalidatePressedFocusedStates(ImageView v) {
        this.mStatesUpdated = false;
        if (v != null) {
            v.invalidate();
        }
    }

    public void onlyHoloOutline(boolean only) {
        this.mOnlyOutline = only;
    }
}