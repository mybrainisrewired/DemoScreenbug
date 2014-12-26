package com.wmt.opengl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;
import com.wmt.res.CommRes;
import com.wmt.util.StringBitmap;
import com.wmt.util.StringBitmap.Config;
import com.wmt.util.Utils;

public final class GLToast extends GLView {
    private static final int DEFAULT_FONTSIZE = 16;
    private static final Paint SRC_PAINT;
    private static Drawable sBackground;
    private boolean mShow;
    private final FloatAnim mShowAnim;
    private TimeoutRunnable mTimeoutHandler;
    private ToastTexture mToastTexture;

    private final class TimeoutRunnable implements Runnable {
        private TimeoutRunnable() {
        }

        public void run() {
            GLToast.this.close(true);
        }
    }

    private final class ToastTexture extends Texture {
        private Config mConfig;
        private final int mFontSize;
        private boolean mIsNewString;
        private String mOldText;
        private Bitmap mStringBitmap;

        ToastTexture() {
            this.mOldText = "";
            this.mStringBitmap = null;
            this.mFontSize = 16;
            this.mConfig = new Config(16, 0);
        }

        public Bitmap generateBitmap() {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            Bitmap bmp = Bitmap.createBitmap(this.mWidth, this.mHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bmp);
            if (sBackground != null) {
                sBackground.setBounds(0, 0, this.mWidth, this.mHeight);
                sBackground.draw(canvas);
            }
            canvas.drawBitmap(this.mStringBitmap, new Rect(0, 0, this.mStringBitmap.getWidth(), this.mStringBitmap.getHeight()), new RectF((float) ((this.mWidth - this.mStringBitmap.getWidth()) / 2), (float) ((this.mHeight - this.mStringBitmap.getHeight()) / 2 - 3), (float) ((this.mWidth - this.mStringBitmap.getWidth()) / 2 + this.mStringBitmap.getWidth()), (float) ((this.mHeight - this.mStringBitmap.getHeight()) / 2 - 3 + this.mStringBitmap.getHeight())), paint);
            canvas.setBitmap(Utils.s_nullBitmap);
            return bmp;
        }

        public boolean getIsNewString() {
            return this.mIsNewString;
        }

        protected Bitmap load(Context context) {
            return GLToast.this.mToastTexture.generateBitmap();
        }

        public void setIsNewString(boolean newString) {
            this.mIsNewString = newString;
        }

        public void setResourceId(String text) {
            Context context = GLToast.this.glContext().uiContext();
            if (!this.mOldText.equals(text)) {
                this.mOldText = text;
                if (this.mStringBitmap != null) {
                    this.mStringBitmap.recycle();
                }
                this.mStringBitmap = StringBitmap.buildBitmap(context, text, this.mConfig);
                this.mWidth = this.mStringBitmap.getWidth() + 30;
                this.mHeight = 42;
                if (sBackground == null) {
                    try {
                        sBackground = CommRes.getResourceDrawable(context, "R.drawable.toast");
                    } catch (RuntimeException e) {
                        Log.w("GLToast", "Can not load R.drawable.toast. Do you installed wmt-commres.apk?");
                    }
                }
                this.mIsNewString = true;
            }
        }
    }

    static {
        SRC_PAINT = new Paint();
        SRC_PAINT.setXfermode(new PorterDuffXfermode(Mode.SRC));
        sBackground = null;
    }

    GLToast(GLContext glContext) {
        super(glContext, 0);
        this.mShow = false;
        this.mShowAnim = new FloatAnim();
        this.mToastTexture = null;
        this.mTimeoutHandler = new TimeoutRunnable(null);
        this.mToastTexture = new ToastTexture();
    }

    public void close(boolean fadeOut) {
        if (fadeOut) {
            this.mShowAnim.animateValue(this.mShowAnim.getCurValue(), 0.0f, 400);
            invalidate();
        } else {
            this.mShowAnim.stop();
            setVisibility(false);
        }
        this.mShow = false;
    }

    protected void onRender(GLCanvas glCanvas, int left, int top) {
        float showRatio = this.mShowAnim.getCurValue();
        if (!this.mShow && !this.mShowAnim.isAnimating()) {
            super.setVisibility(false);
        } else if (this.mToastTexture.getHeight() != 0 && this.mToastTexture.getWidth() != 0) {
            boolean bindOK;
            glCanvas.getGL().glBlendFunc(1, 771);
            glCanvas.setAlpha(showRatio);
            TextureManager tm = glCanvas.getTextureManager();
            float x = (float) (glCanvas.getSurfaceWidth() / 2 - this.mToastTexture.getWidth() / 2);
            float y = ((float) glCanvas.getSurfaceHeight()) * 0.75f;
            if (this.mToastTexture.getIsNewString()) {
                bindOK = tm.bindReplace(this.mToastTexture, this.mToastTexture.generateBitmap());
                this.mToastTexture.setIsNewString(false);
            } else {
                bindOK = tm.bindForce(this.mToastTexture);
            }
            if (bindOK) {
                glCanvas.draw3D(this.mToastTexture, x, y, -0.3f, (float) this.mToastTexture.getWidth(), (float) this.mToastTexture.getHeight());
            }
            glCanvas.setAlpha(1.0f);
            if (this.mShowAnim.isAnimating()) {
                invalidate();
            }
        }
    }

    public void stringShow(int stringResId, long duration) {
        stringShow(glContext().uiContext().getString(stringResId), duration);
    }

    public void stringShow(String string, long duration) {
        this.mToastTexture.setResourceId(string);
        this.mShow = true;
        setVisibility(true);
        this.mShowAnim.animateValue(0.0f, 0.9f, 400);
        glContext().removeRunnable(this.mTimeoutHandler);
        glContext().postRunnable(this.mTimeoutHandler, duration);
        invalidate();
    }
}