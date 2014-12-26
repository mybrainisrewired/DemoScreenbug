package com.wmt.frameworkbridge.video;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

public class MovieSubtitleTextView extends TextView {
    private SubtitleTouchListener mListener;

    public static interface SubtitleChangeListener {
        void notifyTextChanged(String str);

        void notifyTextColorChanged(int i);

        void notifyTextSizeChanged(int i);
    }

    public static interface SubtitleTouchListener {
        void clearLongClick();

        boolean isLongClick();

        void showMovieController();
    }

    public MovieSubtitleTextView(Context context) {
        this(context, null);
    }

    public MovieSubtitleTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MovieSubtitleTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == 1 && this.mListener != null) {
            if (this.mListener.isLongClick()) {
                this.mListener.clearLongClick();
            } else {
                this.mListener.showMovieController();
            }
        }
        return super.onTouchEvent(event);
    }

    public void setSubtitleTouchListener(SubtitleTouchListener l) {
        this.mListener = l;
    }
}