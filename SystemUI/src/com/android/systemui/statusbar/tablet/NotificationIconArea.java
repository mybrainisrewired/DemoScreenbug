package com.android.systemui.statusbar.tablet;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.android.systemui.R;

public class NotificationIconArea extends RelativeLayout {
    private static final String TAG = "NotificationIconArea";
    IconLayout mIconLayout;

    static class IconLayout extends LinearLayout {
        public IconLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public boolean onInterceptTouchEvent(MotionEvent e) {
            return true;
        }
    }

    public NotificationIconArea(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mIconLayout = (IconLayout) findViewById(R.id.icons);
    }
}