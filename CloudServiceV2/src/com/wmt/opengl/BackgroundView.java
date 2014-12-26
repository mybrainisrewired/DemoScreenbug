package com.wmt.opengl;

import com.wmt.opengl.GLContext.GLIdleListener;
import com.wmt.util.LruCache;
import javax.microedition.khronos.opengles.GL11;

public class BackgroundView extends GLView implements GLIdleListener {
    private static final int ADAPTIVE_BACKGROUND_HEIGHT = 128;
    private static final int ADAPTIVE_BACKGROUND_WIDTH = 256;
    private static final float PARALLAX = 0.5f;
    private static final String TAG = "AdaptiveBackground";
    private static final float Z_FAR_PLANE = 0.9999f;
    private CrossFadingTexture mBackground;
    private int mBackgroundBlitWidth;
    private int mBackgroundOverlap;
    private final LruCache<Texture, AdaptiveBackgroundTexture> mCacheAdaptiveTexture;
    private Texture mFallbackBackground;
    private boolean mIdleListenerAdded;
    private Texture mNextTexture;
    private float mPositionX;

    public BackgroundView(GLContext glContext, Texture defaultTexture) {
        super(glContext, 1);
        this.mCacheAdaptiveTexture = new LruCache(2);
        this.mFallbackBackground = null;
        this.mNextTexture = null;
        this.mPositionX = 0.0f;
        this.mIdleListenerAdded = false;
        this.mFallbackBackground = new AdaptiveBackgroundTexture(defaultTexture, 256, 128);
        this.mIdleListenerAdded = true;
        glContext().addIdleListener(this);
    }

    private Texture getAdaptive(Texture nextTexture) {
        Texture itemThumbnail = nextTexture;
        if (itemThumbnail == null) {
            return this.mFallbackBackground;
        }
        AdaptiveBackgroundTexture retVal = (AdaptiveBackgroundTexture) this.mCacheAdaptiveTexture.get(itemThumbnail);
        if (retVal != null) {
            return retVal;
        }
        Texture retVal2 = new AdaptiveBackgroundTexture(itemThumbnail, 256, 128);
        this.mCacheAdaptiveTexture.put(itemThumbnail, retVal2);
        return retVal2;
    }

    private boolean update(float frameInterval) {
        Texture fallback = this.mFallbackBackground;
        if (fallback == null || !fallback.isUploaded()) {
            return false;
        }
        CrossFadingTexture background = this.mBackground;
        return background != null ? background.update(frameInterval) : false;
    }

    public void clearCache() {
        this.mCacheAdaptiveTexture.clear();
        this.mBackground = null;
    }

    public boolean onGLIdle(GLContext glContext, GLCanvas glCanvas) {
        if (this.mBackground == null) {
            this.mBackground = new CrossFadingTexture(this.mFallbackBackground);
        }
        this.mBackground.setTexture(getAdaptive(this.mNextTexture));
        this.mIdleListenerAdded = false;
        invalidate();
        return false;
    }

    protected void onGetLayoutParam(LayoutParam lp) {
        lp.setFullSize();
    }

    protected void onLayout(boolean changeSize) {
        if (changeSize) {
            this.mBackgroundBlitWidth = (int) (((float) getWidth()) * 1.5f);
            this.mBackgroundOverlap = this.mBackgroundBlitWidth * 0;
        }
    }

    protected void onRender(GLCanvas glCanvas, int left, int top) {
        if (update(0.04f)) {
            invalidate();
        }
        GL11 gl = glCanvas.getGL();
        if (!(this.mFallbackBackground == null || this.mFallbackBackground.isUploaded())) {
            glCanvas.getTextureManager().prime(this.mFallbackBackground, true);
        }
        TextureManager textureManager = glCanvas.getTextureManager();
        CrossFadingTexture anchorTexture = this.mBackground;
        if (this.mBackground != null && this.mFallbackBackground != null) {
            gl.glEnable(3042);
            gl.glBlendFunc(770, 771);
            gl.glTexEnvf(8960, 8704, 8448.0f);
            glCanvas.setAlpha(1.0f);
            boolean bind = anchorTexture.bind(glContext());
            if (bind || textureManager.bind(this.mFallbackBackground)) {
                int backgroundSpacing = this.mBackgroundBlitWidth - this.mBackgroundOverlap;
                int anchorEdge = (-((int) (this.mPositionX * 0.5f))) % backgroundSpacing;
                float height = (float) getHeight();
                glCanvas.draw2D((float) (anchorEdge + backgroundSpacing + left), (float) top, (float)Z_FAR_PLANE, (float) this.mBackgroundBlitWidth, height);
                glCanvas.draw2D((float) (anchorEdge + left), (float) top, (float)Z_FAR_PLANE, (float) this.mBackgroundBlitWidth, height);
                glCanvas.draw2D((float) (anchorEdge - backgroundSpacing), 0.0f, (float)Z_FAR_PLANE, (float) this.mBackgroundBlitWidth, height);
                if (bind) {
                    anchorTexture.unbind(glContext(), gl);
                }
                gl.glBlendFunc(1, 771);
                gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            }
        }
    }

    public void setBackgroundTexture(Texture texture) {
        this.mNextTexture = texture;
        if (this.mNextTexture != null && !this.mIdleListenerAdded) {
            this.mIdleListenerAdded = true;
            glContext().addIdleListener(this);
        }
    }

    public void setPositionX(float positionX) {
        this.mPositionX = 0.0f;
    }
}