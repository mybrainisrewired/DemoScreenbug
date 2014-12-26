package com.wmt.frameworkbridge.video;

import android.graphics.Rect;

public interface MovieDropTarget {
    void getHitRect(Rect rect);

    int getLeft();

    void getLocationOnScreen(int[] iArr);

    int getTop();

    void onDrop(int i, int i2, int i3, int i4);
}