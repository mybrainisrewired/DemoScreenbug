package com.android.systemui.recent;

import android.view.View;

public interface RecentsCallback {
    public static final int SWIPE_DOWN = 3;
    public static final int SWIPE_LEFT = 0;
    public static final int SWIPE_RIGHT = 1;
    public static final int SWIPE_UP = 2;

    void dismiss();

    void handleLongPress(View view, View view2, View view3);

    void handleOnClick(View view);

    void handleSwipe(View view);
}