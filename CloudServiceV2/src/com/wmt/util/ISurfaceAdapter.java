package com.wmt.util;

import android.graphics.Canvas;

public interface ISurfaceAdapter {
    void drawItem(int i, Canvas canvas, float f, float f2, float f3, float f4);

    int getColumns();

    int getItemCount();

    int getItemHeight();

    int getItemWidth();

    int getRows();

    int getSelectedItem();

    void setSelectedItem(int i);
}