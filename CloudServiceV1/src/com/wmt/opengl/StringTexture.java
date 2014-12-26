package com.wmt.opengl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import com.wmt.util.StringBitmap;
import com.wmt.util.StringBitmap.Config;

public final class StringTexture extends Texture {
    private int mBackgroundRes;
    private Config mConfig;
    private String mString;

    public StringTexture(String string) {
        this.mBackgroundRes = -1;
        this.mString = string;
        this.mConfig = Config.DEFAULT_CONFIG_SCALED;
        this.mBackgroundRes = -1;
    }

    public StringTexture(String string, int res) {
        this.mBackgroundRes = -1;
        this.mString = string;
        this.mConfig = Config.DEFAULT_CONFIG_SCALED;
        this.mBackgroundRes = res;
    }

    public StringTexture(String string, Config config) {
        this(string, config, config.width, config.height);
    }

    public StringTexture(String string, Config config, int res) {
        this(string, config, config.width, config.height, res);
    }

    public StringTexture(String string, Config config, int width, int height) {
        this.mBackgroundRes = -1;
        this.mString = string;
        this.mConfig = config;
        this.mWidth = width;
        this.mHeight = height;
        this.mBackgroundRes = -1;
    }

    public StringTexture(String string, Config config, int width, int height, int res) {
        this.mBackgroundRes = -1;
        this.mString = string;
        this.mConfig = config;
        this.mWidth = width;
        this.mHeight = height;
        this.mBackgroundRes = res;
    }

    public float computeTextWidth() {
        Paint paint = StringBitmap.computePaint(this.mString, this.mConfig, this.mWidth, this.mHeight);
        return (paint == null || this.mString == null) ? 0.0f : paint.measureText(this.mString);
    }

    protected Bitmap load(Context context) {
        return StringBitmap.buildBitmap(context, this.mString, this.mConfig, this.mWidth, this.mHeight, this.mBackgroundRes);
    }
}