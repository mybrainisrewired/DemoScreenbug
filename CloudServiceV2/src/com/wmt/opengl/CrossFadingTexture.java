package com.wmt.opengl;

import javax.microedition.khronos.opengles.GL11;

public class CrossFadingTexture {
    private static final String TAG = "CrossFadingTexture";
    private float mAnimatedMixRatio;
    private boolean mBind;
    private boolean mBindUsingMixed;
    private boolean mFadeNecessary;
    private Texture mFadingTexture;
    private float mMixRatio;
    private Texture mTexture;

    public CrossFadingTexture() {
        this.mMixRatio = 0.0f;
        this.mAnimatedMixRatio = 0.0f;
        this.mBindUsingMixed = false;
        this.mBind = false;
        this.mFadeNecessary = false;
    }

    public CrossFadingTexture(Texture initialTexture) {
        this.mMixRatio = 0.0f;
        this.mAnimatedMixRatio = 0.0f;
        this.mBindUsingMixed = false;
        this.mBind = false;
        this.mFadeNecessary = false;
        this.mMixRatio = 1.0f;
        this.mAnimatedMixRatio = 1.0f;
        this.mFadeNecessary = false;
        this.mTexture = initialTexture;
        this.mFadingTexture = initialTexture;
    }

    public CrossFadingTexture(Texture source, Texture destination) {
        this.mMixRatio = 0.0f;
        this.mAnimatedMixRatio = 0.0f;
        this.mBindUsingMixed = false;
        this.mBind = false;
        this.mFadeNecessary = false;
        this.mFadingTexture = source;
        this.mTexture = destination;
        this.mMixRatio = 1.0f;
        this.mAnimatedMixRatio = 0.0f;
    }

    public boolean bind(GLContext glContext) {
        if (this.mBind) {
            return true;
        }
        GLCanvas glCanvas = glContext.glCanvas();
        TextureManager textureManager = glCanvas.getTextureManager();
        if (this.mFadingTexture != null && this.mFadingTexture.isError()) {
            this.mFadingTexture = null;
        }
        if (this.mTexture != null && this.mTexture.isError()) {
            this.mTexture = null;
        }
        this.mBindUsingMixed = false;
        boolean fadingTextureLoaded = false;
        boolean textureLoaded = false;
        if (this.mFadingTexture != null) {
            fadingTextureLoaded = textureManager.bind(this.mFadingTexture);
        }
        if (this.mTexture != null) {
            textureManager.bind(this.mTexture);
            textureLoaded = this.mTexture.isUploaded();
        }
        if (this.mFadeNecessary) {
            if (glCanvas.getAlpha() != this.mAnimatedMixRatio) {
                glCanvas.setAlpha(this.mAnimatedMixRatio);
            }
            if (this.mAnimatedMixRatio == 1.0f) {
                this.mFadeNecessary = false;
            }
        }
        if (!textureLoaded && !fadingTextureLoaded) {
            return false;
        }
        this.mBind = true;
        if (this.mAnimatedMixRatio <= 0.0f && fadingTextureLoaded) {
            textureManager.bind(this.mFadingTexture);
            return true;
        } else if (this.mAnimatedMixRatio >= 1.0f || !fadingTextureLoaded || glCanvas.getAlpha() < this.mAnimatedMixRatio || this.mFadingTexture == this.mTexture) {
            textureManager.bind(this.mTexture);
            return true;
        } else {
            this.mBindUsingMixed = true;
            glCanvas.bindMixed(this.mFadingTexture, this.mTexture, this.mAnimatedMixRatio);
            return true;
        }
    }

    public void clear() {
        this.mTexture = null;
        this.mFadingTexture = null;
        this.mMixRatio = 1.0f;
        this.mAnimatedMixRatio = 1.0f;
    }

    public Texture getTexture() {
        return this.mTexture;
    }

    public boolean setTexture(Texture texture) {
        if (this.mTexture == texture || texture == null || this.mAnimatedMixRatio < 1.0f) {
            return false;
        }
        this.mFadeNecessary = false;
        if (this.mFadingTexture == null) {
            this.mFadeNecessary = true;
        }
        if (this.mTexture != null) {
            this.mFadingTexture = this.mTexture;
        } else {
            this.mFadingTexture = texture;
        }
        this.mTexture = texture;
        this.mAnimatedMixRatio = 0.0f;
        this.mMixRatio = 1.0f;
        return true;
    }

    public void setTextureImmediate(Texture texture) {
        if (texture != null && texture.isUploaded() && this.mTexture != texture) {
            if (this.mTexture != null) {
                this.mFadingTexture = this.mTexture;
            }
            this.mTexture = texture;
            this.mMixRatio = 1.0f;
        }
    }

    public void unbind(GLContext glContext, GL11 gl) {
        if (this.mBindUsingMixed && this.mBind) {
            glContext.glCanvas().unbindMixed();
            this.mBindUsingMixed = false;
        }
        this.mBind = false;
    }

    public boolean update(float timeElapsed) {
        if (this.mTexture != null && this.mFadingTexture != null && this.mTexture.isUploaded() && this.mFadingTexture.isUploaded()) {
            this.mAnimatedMixRatio = FloatUtils.animate(this.mAnimatedMixRatio, this.mMixRatio, 0.2f * timeElapsed);
            return this.mMixRatio != this.mAnimatedMixRatio;
        } else if (this.mTexture == null || this.mFadingTexture == null || this.mTexture.isError() || this.mFadingTexture.isError()) {
            this.mAnimatedMixRatio = 1.0f;
            return false;
        } else {
            this.mAnimatedMixRatio = 0.0f;
            return false;
        }
    }
}