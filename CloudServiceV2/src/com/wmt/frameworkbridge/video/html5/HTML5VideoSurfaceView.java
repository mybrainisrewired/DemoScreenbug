package com.wmt.frameworkbridge.video.html5;

import android.content.Context;
import android.frameworkbridge.WmtHTML5Bridge.ISurfaceViewBridge;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.SurfaceView;
import com.wmt.frameworkbridge.video.IVideoSurfaceView;

class HTML5VideoSurfaceView extends SurfaceView implements IVideoSurfaceView {
    static final int COMMAND_TO_PLAYENGINE_SCALEMODE = -65532;
    private boolean canResize;
    private MediaPlayer mMediaPlayer;
    private int mOffsetX;
    private int mOffsetY;
    private int mOrigBottom;
    private int mOrigLeft;
    private int mOrigRight;
    private int mOrigTop;
    private ISurfaceViewBridge mSurfaceBridge;
    private int mVideoHeight;
    private int mVideoWidth;

    public HTML5VideoSurfaceView(Context context) {
        this(context, null);
    }

    public HTML5VideoSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HTML5VideoSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mSurfaceBridge = new ISurfaceViewBridge() {
            public void setMediaPlayer(MediaPlayer mp) {
                HTML5VideoSurfaceView.this.setMediaPlayer(mp);
            }

            public void setVideoSize(int width, int height, int scaleMode) {
                HTML5VideoSurfaceView.this.setVideoSize(width, height, scaleMode);
            }
        };
    }

    public ISurfaceViewBridge getBridgeImpl() {
        return this.mSurfaceBridge;
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

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(this.mVideoWidth, widthMeasureSpec);
        int height = getDefaultSize(this.mVideoHeight, heightMeasureSpec);
        if (this.mVideoWidth > 0 && this.mVideoHeight > 0) {
            if (this.mVideoWidth * height > this.mVideoHeight * width) {
                height = (this.mVideoHeight * width) / this.mVideoWidth;
            } else if (this.mVideoWidth * height < this.mVideoHeight * width) {
                width = (this.mVideoWidth * height) / this.mVideoHeight;
            }
        }
        if (!this.canResize || this.mVideoWidth <= 0 || this.mVideoHeight <= 0) {
            setMeasuredDimension(width, height);
        } else {
            setMeasuredDimension(this.mVideoWidth, this.mVideoHeight);
        }
    }

    public void resetParamters() {
        this.mOffsetX = 0;
        this.mOffsetY = 0;
    }

    public void setMediaPlayer(MediaPlayer mp) {
        this.mMediaPlayer = mp;
    }

    public void setOffSetXY(int x, int y) {
        this.mOffsetX = x;
        this.mOffsetY = y;
    }

    public void setVideoSize(int width, int height, int scaleMode) {
        this.canResize = true;
        if (width != 0 && height != 0) {
            if (width == -1 && height == -1) {
                this.mVideoWidth = this.mMediaPlayer.getVideoWidth();
                this.mVideoHeight = this.mMediaPlayer.getVideoHeight();
            } else {
                this.mVideoWidth = width;
                this.mVideoHeight = height;
            }
            if (this.mVideoWidth != 0 && this.mVideoHeight != 0) {
                getHolder().setFixedSize(this.mVideoWidth, this.mVideoHeight);
                if (this.mMediaPlayer != null) {
                    this.mMediaPlayer.setParameter(COMMAND_TO_PLAYENGINE_SCALEMODE, String.valueOf(scaleMode));
                }
            }
        }
    }

    public void updateLayout() {
        layout(this.mOrigLeft, this.mOrigTop, this.mOrigRight, this.mOrigBottom);
    }
}