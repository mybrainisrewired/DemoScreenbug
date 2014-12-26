package com.wmt.frameworkbridge.video;

public interface IVideoSurfaceView {
    int getBottom();

    int getHeight();

    int getLeft();

    int getOffsetX();

    int getOffsetY();

    int getRight();

    int getTop();

    int getWidth();

    void resetParamters();

    void setOffSetXY(int i, int i2);

    void setVideoSize(int i, int i2, int i3);

    void updateLayout();
}