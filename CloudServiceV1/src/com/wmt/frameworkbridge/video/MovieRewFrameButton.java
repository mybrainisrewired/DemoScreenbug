package com.wmt.frameworkbridge.video;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.wmt.util.HolographicImageView;

public class MovieRewFrameButton extends HolographicImageView {
    private CancelFastOperationListener mListener;

    public static interface CancelFastOperationListener {
        void cacelFastOperation();
    }

    public MovieRewFrameButton(Context context) {
        this(context, null);
    }

    public MovieRewFrameButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MovieRewFrameButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if ((event.getAction() == 1 || event.getAction() == 3) && this.mListener != null) {
            this.mListener.cacelFastOperation();
        }
        return super.onTouchEvent(event);
    }

    public void setCancelFastOperationListener(CancelFastOperationListener l) {
        this.mListener = l;
    }
}