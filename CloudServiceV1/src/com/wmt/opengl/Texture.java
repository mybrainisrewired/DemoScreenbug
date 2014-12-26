package com.wmt.opengl;

import android.content.Context;
import android.graphics.Bitmap;

public abstract class Texture {
    private static final int BIND_FORCE = 4;
    private static final int POT_PADDED = 8;
    private static final int SHOW_ANIMATION_MARK = 2;
    public static final int STATE_LOADED = 2;
    public static final int STATE_LOAD_ERROR = 4;
    public static final int STATE_QUEUED = 1;
    public static final int STATE_UNLOADED = 0;
    public static final int STATE_UPLOADED = 3;
    public static final int STATE_UPLOAD_ERROR = 5;
    private static final String TAG = "Texture";
    public int mHeight;
    public int mLoadCounter;
    public Bitmap mLoadedBitmap;
    public float mNormalizedHeight;
    public float mNormalizedWidth;
    public float mScale;
    int mState;
    private int mTextureFlags;
    public int mTextureId;
    private int mWeight;
    public int mWidth;

    public Texture() {
        this.mState = 0;
        this.mLoadCounter = 0;
        this.mLoadedBitmap = null;
        clear();
    }

    public final void clear() {
        this.mTextureId = -1;
        this.mState = 0;
        this.mLoadCounter = 0;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mNormalizedWidth = 1.0f;
        this.mNormalizedHeight = 1.0f;
        this.mScale = 1.0f;
        this.mWeight = 1;
        recycleAfterLoaded();
    }

    public final Bitmap externalLoad(Context context) {
        if (this.mLoadedBitmap == null) {
            this.mLoadedBitmap = load(context);
        }
        if (!(this.mLoadedBitmap == null || this.mState == 3)) {
            this.mState = 2;
        }
        return this.mLoadedBitmap;
    }

    public final int getHeight() {
        return this.mHeight;
    }

    public final float getNormalizedHeight() {
        return this.mNormalizedHeight;
    }

    public final float getNormalizedWidth() {
        return this.mNormalizedWidth;
    }

    public final float getScale() {
        return this.mScale;
    }

    public final int getState() {
        return this.mState;
    }

    public final int getWeight() {
        return this.mWeight;
    }

    public final int getWidth() {
        return this.mWidth;
    }

    public final boolean isBindForce() {
        return (this.mTextureFlags & 4) == 4;
    }

    public final boolean isError() {
        return this.mState == 5 || this.mState == 4;
    }

    public final boolean isPOTPadded() {
        return (this.mTextureFlags & 8) == 8;
    }

    public final boolean isShowAnimationMark() {
        return (this.mTextureFlags & 2) == 2;
    }

    public final boolean isUnited() {
        return this.mState == 0;
    }

    public final boolean isUploaded() {
        return this.mState == 3;
    }

    protected abstract Bitmap load(Context context);

    public final boolean needUpload() {
        return (isUploaded() || isError()) ? false : true;
    }

    public void recycleAfterLoaded() {
        if (this.mLoadedBitmap != null) {
            this.mLoadedBitmap.recycle();
            this.mLoadedBitmap = null;
        }
    }

    public final void setBindForce(boolean bindForce) {
        if (bindForce) {
            this.mTextureFlags |= 4;
        } else {
            this.mTextureFlags &= -5;
        }
    }

    final void setPOTPadded(boolean padded) {
        if (padded) {
            this.mTextureFlags |= 8;
        } else {
            this.mTextureFlags &= -9;
        }
    }

    public final void setScale(float scale) {
        this.mScale = scale;
    }

    public final void setShowAnimationMark(boolean isShowAnimationMark) {
        if (isShowAnimationMark) {
            this.mTextureFlags |= 2;
        } else {
            this.mTextureFlags &= -3;
        }
    }

    public final void setWeight(int weight) {
        if (weight < 1) {
            weight = 1;
        }
        this.mWeight = weight;
    }

    public String toString() {
        return super.toString() + ",State:" + this.mState + ",TextureId:" + this.mTextureId + " mWidth : " + this.mWidth + " mHeight : " + this.mHeight;
    }
}