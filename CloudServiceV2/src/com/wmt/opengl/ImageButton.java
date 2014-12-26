package com.wmt.opengl;

import android.view.KeyEvent;
import android.view.MotionEvent;

public class ImageButton extends GLView {
    private static int FOCUS_FRAME_HEIGHT = 0;
    private static int FOCUS_FRAME_WIDTH = 0;
    public static final int IMAGE_FOCUSED = 3;
    public static final int IMAGE_INVALID = 4;
    public static final int IMAGE_NORMAL = 0;
    public static final int IMAGE_PRESSED = 1;
    public static final int IMAGE_SELECTED = 2;
    private static final int sAnimitionTime = 300;
    private Runnable mAction;
    private boolean mActionRunInUIThread;
    private boolean mAnimation;
    private float mAnimationPosY;
    private final FloatAnim mFade;
    private final VertexDrawer mFocusDrawer;
    private boolean mIsHiddening;
    private boolean mIsInvalid;
    private boolean mIsSelected;
    private final VertexDrawer mNormalDrawer;
    private boolean mPressed;
    private Texture[] mTextures;

    static {
        FOCUS_FRAME_WIDTH = 25;
        FOCUS_FRAME_HEIGHT = 25;
    }

    public ImageButton(GLContext glContext) {
        super(glContext, 7);
        this.mTextures = new Texture[5];
        this.mAction = null;
        this.mFade = new FloatAnim();
        this.mAnimationPosY = 0.0f;
        this.mAnimation = false;
        this.mIsHiddening = false;
        this.mIsInvalid = false;
        this.mIsSelected = false;
        this.mPressed = false;
        this.mNormalDrawer = new VertexDrawer(-0.3f, null);
        this.mFocusDrawer = new VertexDrawer(-0.3f, null);
    }

    public ImageButton(GLContext glContext, float z) {
        super(glContext, 7);
        this.mTextures = new Texture[5];
        this.mAction = null;
        this.mFade = new FloatAnim();
        this.mAnimationPosY = 0.0f;
        this.mAnimation = false;
        this.mIsHiddening = false;
        this.mIsInvalid = false;
        this.mIsSelected = false;
        this.mPressed = false;
        this.mNormalDrawer = new VertexDrawer(z, null);
        this.mFocusDrawer = new VertexDrawer(z, null);
    }

    private final boolean containsPoint(float x, float y, boolean addTrackingMargin) {
        return x >= 0.0f && y >= 0.0f && x <= ((float) getWidth()) && y <= ((float) getHeight());
    }

    private void executeAction() {
        if (this.mAction == null) {
            return;
        }
        if (this.mActionRunInUIThread) {
            glContext().postUIThreadRunnable(this.mAction, 0);
        } else {
            this.mAction.run();
        }
    }

    public boolean getIsHiddening() {
        return this.mIsHiddening;
    }

    public boolean onKeyEvent(KeyEvent event) {
        if (!this.mIsInvalid) {
            int keyCode = event.getKeyCode();
            int action = event.getAction();
            if (keyCode == 23 || keyCode == 66) {
                if (action == 0) {
                    this.mPressed = true;
                    invalidate();
                    return true;
                } else if (action != 1) {
                    return true;
                } else {
                    executeAction();
                    this.mPressed = false;
                    invalidate();
                    return true;
                }
            }
        }
        return false;
    }

    protected void onLayout(boolean changeSize) {
        if (changeSize) {
            int width = getWidth();
            int height = getHeight();
            this.mNormalDrawer.setDrawSize((float) width, (float) height);
            this.mFocusDrawer.setDrawSize((float) (FOCUS_FRAME_WIDTH * 2 + width), (float) (FOCUS_FRAME_HEIGHT * 2 + height));
        }
    }

    protected void onRender(GLCanvas glCanvas, int left, int top) {
        if (this.mPressed && !this.mIsInvalid) {
            left += 2;
            top += 2;
        }
        Texture texture = this.mTextures[0];
        if (this.mIsSelected && this.mTextures[2] != null) {
            texture = this.mTextures[2];
        }
        if (this.mPressed && this.mTextures[1] != null) {
            texture = this.mTextures[1];
        }
        if (this.mIsInvalid && this.mTextures[4] != null) {
            texture = this.mTextures[4];
        }
        if (texture != null) {
            if (isFocused() && !this.mPressed && !this.mIsInvalid && this.mTextures[3] == null) {
                glCanvas.lineRect(-13647105, (float) left, this.mAnimationPosY + ((float) top), this.mNormalDrawer.getZ(), (float) getWidth(), (float) getHeight());
                glCanvas.getGL();
            }
            if (this.mAnimation) {
                float ratio = this.mFade.getCurValue();
                if (!this.mFade.isAnimating() && !this.mIsHiddening) {
                    setAnimation(false);
                } else if (!this.mFade.isAnimating() && this.mIsHiddening) {
                    setAnimation(false);
                    this.mIsHiddening = false;
                    setVisibility(false);
                }
                glCanvas.setColor(1.0f, 1.0f, 1.0f, ratio);
            }
            this.mNormalDrawer.setTexture(texture);
            this.mNormalDrawer.draw(glCanvas, left, top, (int)IMAGE_INVALID);
            if (!(!isFocused() || this.mIsSelected || this.mPressed || this.mIsInvalid || this.mTextures[3] == null)) {
                this.mFocusDrawer.setTexture(this.mTextures[3]);
                this.mFocusDrawer.draw(glCanvas, (float) (left - FOCUS_FRAME_WIDTH), ((float) top) + this.mAnimationPosY - ((float) FOCUS_FRAME_HEIGHT), (int)IMAGE_NORMAL);
            }
            if (this.mAnimation) {
                invalidate();
            }
        }
    }

    protected boolean onTouchEvent(MotionEvent e) {
        int action = e.getAction();
        if (!this.mIsInvalid) {
            boolean pressed = this.mPressed;
            switch (action) {
                case IMAGE_NORMAL:
                case IMAGE_SELECTED:
                    this.mPressed = containsPoint(e.getX(), e.getY(), true);
                    break;
                case IMAGE_PRESSED:
                    if (this.mPressed) {
                        executeAction();
                    }
                    this.mPressed = false;
                    break;
                case IMAGE_FOCUSED:
                    this.mPressed = false;
                    break;
            }
            if (pressed != this.mPressed) {
                invalidate();
            }
        }
        return true;
    }

    protected boolean onVisibilityChanged(boolean visibility) {
        if (!this.mAnimation) {
            return true;
        }
        if (visibility) {
            this.mFade.animateValue(0.0f, 1.0f, sAnimitionTime);
            this.mIsHiddening = false;
        } else {
            this.mFade.animateValue(1.0f, 0.0f, sAnimitionTime);
            this.mIsHiddening = true;
        }
        invalidate();
        return false;
    }

    public final void setAction(Runnable action) {
        this.mAction = action;
        this.mActionRunInUIThread = false;
    }

    public void setAnimation(boolean animation) {
        this.mAnimation = animation;
    }

    public void setAnimationPosY(float LTy) {
        this.mAnimationPosY = LTy;
    }

    public void setImage(int imageType, int resourceId) {
        setImage(imageType, new BitmapTexture(resourceId));
    }

    public void setImage(int imageType, Texture texture) {
        this.mTextures[imageType] = texture;
        if (glContext().runInGLThread()) {
            glContext().getTextureManager().bindForce(texture);
        }
    }

    public void setInvalid(boolean isInvalid) {
        if (isInvalid != this.mIsInvalid) {
            this.mIsInvalid = isInvalid;
            if (this.mIsInvalid) {
                setFocusable(false);
            } else {
                setFocusable(true);
            }
            invalidate();
        }
    }

    public void setSelected(boolean isSelected) {
        if (isSelected != this.mIsSelected) {
            this.mIsSelected = isSelected;
            invalidate();
        }
    }

    public final void setUIThreadAction(Runnable action) {
        this.mAction = action;
        this.mActionRunInUIThread = true;
    }
}