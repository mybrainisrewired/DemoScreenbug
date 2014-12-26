package com.clouds.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import com.wmt.data.LocalAudioAll;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;

public class AdvertisementLayout extends LinearLayout {
    private static final String TAG;
    public static int TOOL_BAR_HIGH;
    public static LayoutParams params;
    private float startX;
    private float startY;
    WindowManager wm;
    private float x;
    private float y;

    static {
        TAG = AdvertisementLayout.class.getSimpleName();
        TOOL_BAR_HIGH = 0;
        params = new LayoutParams();
    }

    public AdvertisementLayout(Context context) {
        super(context);
        Context applicationContext = getContext().getApplicationContext();
        getContext();
        this.wm = (WindowManager) applicationContext.getSystemService("window");
    }

    public AdvertisementLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        Context applicationContext = getContext().getApplicationContext();
        getContext();
        this.wm = (WindowManager) applicationContext.getSystemService("window");
    }

    @SuppressLint({"NewApi"})
    public AdvertisementLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Context applicationContext = getContext().getApplicationContext();
        getContext();
        this.wm = (WindowManager) applicationContext.getSystemService("window");
    }

    private void updatePosition() {
        params.x = (int) (this.x - this.startX);
        params.y = (int) (this.y - this.startY);
        Log.v(TAG, new StringBuilder("------X: ").append(this.x).append("------Y:").append(this.y).append(" startX: ").append(this.startX).append(" startY: ").append(this.startY).append(" params.x: ").append(params.x).append(" params.y: ").append(params.y).toString());
        this.wm.updateViewLayout(this, params);
    }

    public boolean onTouchEvent(MotionEvent event) {
        this.x = event.getRawX();
        this.y = event.getRawY() - ((float) TOOL_BAR_HIGH);
        Log.v(TAG, new StringBuilder("------X: ").append(this.x).append("------Y:").append(this.y).toString());
        switch (event.getAction()) {
            case LocalAudioAll.SORT_BY_TITLE:
                this.startX = event.getX();
                this.startY = event.getY();
                return true;
            case LocalAudioAll.SORT_BY_DATE:
                updatePosition();
                this.startY = 0.0f;
                this.startX = 0.0f;
                return true;
            case ClassWriter.COMPUTE_FRAMES:
                updatePosition();
                return false;
            default:
                return true;
        }
    }
}