package com.wmt.opengl;

public class AnimationTextureWrapper {
    public float mCurrentRotatef;
    public long mCurrentTime;
    public float mCurrentX;
    public float mCurrentY;
    public float mCurrentZ;
    public float mFinalRotatef;
    public long mFinalTime;
    public float mFinalX;
    public float mFinalY;
    public float mFinalZ;
    public float mStartRotatef;
    public long mStartTime;
    public float mStartX;
    public float mStartY;
    public float mStartZ;
    private Texture mTexture;

    public AnimationTextureWrapper(Texture animationTexture) {
        this.mStartTime = 0;
        this.mStartX = 0.0f;
        this.mStartY = 0.0f;
        this.mStartZ = 0.2f;
        this.mStartRotatef = 0.0f;
        this.mCurrentTime = 0;
        this.mCurrentX = 0.0f;
        this.mCurrentY = 0.0f;
        this.mCurrentZ = 0.2f;
        this.mCurrentRotatef = 0.0f;
        this.mFinalTime = 0;
        this.mFinalX = 0.0f;
        this.mFinalY = 0.0f;
        this.mFinalZ = 0.0f;
        this.mFinalRotatef = 0.0f;
        this.mTexture = animationTexture;
    }

    public Texture getAimationTexture() {
        return this.mTexture;
    }

    public void setAimationTexture(Texture animationTexture) {
        this.mTexture = animationTexture;
    }
}