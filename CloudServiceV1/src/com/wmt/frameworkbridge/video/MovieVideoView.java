package com.wmt.frameworkbridge.video;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

public class MovieVideoView extends VideoView implements IVideoSurfaceView {
    private int mOffsetX;
    private int mOffsetY;
    private int mOrigBottom;
    private int mOrigLeft;
    private int mOrigRight;
    private int mOrigTop;

    public MovieVideoView(Context context) {
        this(context, null);
    }

    public MovieVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MovieVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public int getOffsetX() {
        return this.mOffsetX;
    }

    public int getOffsetY() {
        return this.mOffsetY;
    }

    public void layout(int l, int t, int r, int b) {
        this.mOrigLeft = l;
        this.mOrigTop = t;
        this.mOrigRight = r;
        this.mOrigBottom = b;
        super.layout(this.mOffsetX + l, this.mOffsetY + t, this.mOffsetX + r, this.mOffsetY + b);
    }

    public void resetParamters() {
        this.mOffsetX = 0;
        this.mOffsetY = 0;
    }

    public void setOffSetXY(int x, int y) {
        this.mOffsetX = x;
        this.mOffsetY = y;
    }

    public void updateLayout() {
        layout(this.mOrigLeft, this.mOrigTop, this.mOrigRight, this.mOrigBottom);
    }
}