package com.wmt.opengl.grid;

public interface IGLGridAdapter {
    boolean dragItem(int i, int i2);

    boolean drawItem(GridDrawArgs gridDrawArgs);

    int getCount();

    int getItemHeight();

    int getItemWidth();

    int hitTest(HitTestArgs hitTestArgs);
}