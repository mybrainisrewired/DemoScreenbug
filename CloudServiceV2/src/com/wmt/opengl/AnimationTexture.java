package com.wmt.opengl;

public abstract class AnimationTexture extends Texture {
    public Animation mAnimation;
    public boolean mAnimationFlag;

    public AnimationTexture() {
        this.mAnimationFlag = false;
        this.mAnimation = null;
    }
}