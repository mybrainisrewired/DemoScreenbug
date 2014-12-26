package com.android.systemui.statusbar.policy;

import android.app.ActivityManager;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.android.systemui.statusbar.CommandQueue;

public class CompatModeButton extends ImageView {
    private static final boolean DEBUG = false;
    private static final String TAG = "StatusBar.CompatModeButton";
    private ActivityManager mAM;

    public CompatModeButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CompatModeButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        setClickable(true);
        this.mAM = (ActivityManager) context.getSystemService("activity");
        refresh();
    }

    public void refresh() {
        int i = 0;
        int mode = this.mAM.getFrontActivityScreenCompatMode();
        if (mode != -3) {
            boolean vis;
            if (mode == -2 || mode == -1) {
                vis = false;
            } else {
                vis = true;
            }
            if (!vis) {
                i = CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL;
            }
            setVisibility(i);
        }
    }
}