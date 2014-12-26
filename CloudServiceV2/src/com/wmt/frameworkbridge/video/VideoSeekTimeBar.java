package com.wmt.frameworkbridge.video;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

public class VideoSeekTimeBar extends SeekBar {
    private static final int DEFAULT_DELAY = 200;
    private boolean mEnableShowSeekbar;
    private long mTouchDownTime;

    public VideoSeekTimeBar(Context context) {
        this(context, null);
    }

    public VideoSeekTimeBar(Context context, AttributeSet attrs) {
        this(context, attrs, 16842875);
    }

    public VideoSeekTimeBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void disableShowSeekbar() {
        this.mEnableShowSeekbar = false;
    }

    public boolean isEnableShow() {
        return this.mEnableShowSeekbar;
    }

    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == 0) {
            this.mTouchDownTime = SystemClock.uptimeMillis();
        }
        if (action == 2 && SystemClock.uptimeMillis() - this.mTouchDownTime >= 200) {
            this.mEnableShowSeekbar = true;
        }
        if (action == 1 && SystemClock.uptimeMillis() - this.mTouchDownTime >= 200) {
            this.mEnableShowSeekbar = true;
        }
        return super.onTouchEvent(event);
    }
}