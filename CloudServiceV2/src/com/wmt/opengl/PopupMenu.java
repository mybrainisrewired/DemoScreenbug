package com.wmt.opengl;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.util.TimeUtils;
import android.support.v4.view.MotionEventCompat;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import com.wmt.data.DataManager;
import com.wmt.data.LocalAudioAll;
import com.wmt.opengl.IconTitleDrawable.Config;
import com.wmt.remotectrl.KeyTouchInputEvent;
import com.wmt.util.Log;
import com.wmt.util.Utils;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.smile.SmileConstants;

public final class PopupMenu extends GLView {
    private static Config ICON_TITLE_CONFIG = null;
    private static Config ICON_TITLE_GRAY_CONFIG = null;
    private static final int ICON_TITLE_MIN_WIDTH = 100;
    private static final int PADDING_BOTTOM = 22;
    private static final int PADDING_LEFT = 15;
    private static final int PADDING_RIGHT = 15;
    private static final int PADDING_TOP = 0;
    public static float PIXEL_DENSITY = 0.0f;
    private static final int POPUP_TRIANGLE_EXTRA_HEIGHT = 14;
    private static final Paint SRC_PAINT;
    private static final String TAG = "PopupMenu";
    private Config mIconTilteConfig;
    private Listener mListener;
    private boolean mNeedsLayout;
    private Option[] mOptions;
    private PopupTexture mPopupTexture;
    private int mRowHeight;
    private int mSelectedItem;
    private boolean mShow;
    private final FloatAnim mShowAnim;
    private float mShowHeight;
    private float mShowWidth;
    private float mShowX;
    private float mShowY;

    public static interface Listener {
        void onSelectionChanged(PopupMenu popupMenu, int i);

        void onSelectionClicked(PopupMenu popupMenu, int i);
    }

    public static final class Option {
        private final Runnable mAction;
        private int mBottom;
        private IconTitleDrawable mDrawable;
        private final Drawable mIcon;
        private boolean mIsGray;
        private final String mTitle;

        public Option(String title, Drawable icon, Runnable action) {
            this.mDrawable = null;
            this.mIsGray = false;
            this.mTitle = title;
            this.mIcon = icon;
            this.mAction = action;
        }

        public Option(String title, Drawable icon, Runnable action, boolean isGray) {
            this.mDrawable = null;
            this.mIsGray = false;
            this.mTitle = title;
            this.mIcon = icon;
            this.mAction = action;
            this.mIsGray = isGray;
        }
    }

    private final class PopupTexture extends Texture {
        private NinePatch mBackground;
        private final Rect mBackgroundRect;
        private Context mContext;
        public int mHeight;
        public boolean mNeedsDraw;
        private int mPopupRes;
        public int mWidth;

        public PopupTexture(Context context, int popup_res) {
            this.mBackgroundRect = new Rect();
            this.mNeedsDraw = true;
            this.mPopupRes = popup_res;
            this.mContext = context;
        }

        public Bitmap generateBitmap() {
            Bitmap tmp = Bitmap.createBitmap(this.mWidth, this.mHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(tmp);
            if (this.mBackground == null) {
                Bitmap background = BitmapFactory.decodeResource(this.mContext.getResources(), this.mPopupRes);
                this.mBackground = new NinePatch(background, background.getNinePatchChunk(), null);
            }
            this.mBackground.draw(canvas, this.mBackgroundRect, SRC_PAINT);
            com.wmt.opengl.PopupMenu.Option[] options = PopupMenu.this.mOptions;
            int selectedItem = PopupMenu.this.mSelectedItem;
            if (selectedItem != -1) {
                com.wmt.opengl.PopupMenu.Option option = options[selectedItem];
                Rect rect = new Rect();
                rect.left = option.mDrawable.getBounds().left;
                rect.right = option.mDrawable.getBounds().right;
                option.mDrawable.getBounds().top += 2;
                if (selectedItem == 0) {
                    rect.top += 2;
                }
                option.mDrawable.getBounds().bottom -= 2;
                Paint paint = new Paint();
                paint.setColor(Color.rgb(MotionEventCompat.ACTION_MASK, SmileConstants.TOKEN_PREFIX_TINY_UNICODE, Opcodes.ALOAD));
                canvas.drawRect(rect, paint);
            }
            int numOptions = options.length;
            int i = PADDING_TOP;
            while (i != numOptions) {
                options[i].mDrawable.draw(canvas);
                i++;
            }
            canvas.setBitmap(Utils.s_nullBitmap);
            return tmp;
        }

        protected Bitmap load(Context context) {
            return generateBitmap();
        }

        public void setNeedsDraw() {
            this.mNeedsDraw = true;
        }

        public void setSize(int width, int height) {
            this.mWidth = width;
            this.mHeight = height;
            this.mBackgroundRect.set(PADDING_TOP, PADDING_TOP, this.mWidth, this.mHeight - ((int) (14.0f * PIXEL_DENSITY)));
        }
    }

    static {
        SRC_PAINT = new Paint();
        PIXEL_DENSITY = 0.0f;
    }

    public PopupMenu(Context context, GLContext glContext, int popup_res, int popup_sel_res, boolean hasIcon) {
        super(glContext, 0);
        this.mListener = null;
        this.mOptions = new Option[0];
        this.mNeedsLayout = false;
        this.mShow = false;
        this.mShowAnim = new FloatAnim();
        this.mRowHeight = 36;
        this.mSelectedItem = -1;
        this.mPopupTexture = new PopupTexture(context, popup_res);
        setVisibility(true);
        if (PIXEL_DENSITY == 0.0f) {
            DisplayMetrics metrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
            PIXEL_DENSITY = metrics.density;
        }
        TextPaint paint = new TextPaint();
        paint.setTextSize(17.0f * PIXEL_DENSITY);
        paint.setStrokeWidth(0.3f);
        paint.setColor(-1);
        paint.setAntiAlias(true);
        TextPaint graypaint = new TextPaint();
        graypaint.setTextSize(17.0f * PIXEL_DENSITY);
        graypaint.setStrokeWidth(0.3f);
        graypaint.setColor(-2139062144);
        graypaint.setAntiAlias(true);
        if (hasIcon) {
            ICON_TITLE_CONFIG = new Config((int) (PIXEL_DENSITY * 50.0f), (int) (PIXEL_DENSITY * 34.0f), paint);
            ICON_TITLE_GRAY_CONFIG = new Config((int) (PIXEL_DENSITY * 50.0f), (int) (PIXEL_DENSITY * 34.0f), graypaint);
        } else {
            ICON_TITLE_CONFIG = new Config((int) (16.0f * PIXEL_DENSITY), (int) (PIXEL_DENSITY * 0.0f), paint);
            ICON_TITLE_GRAY_CONFIG = new Config((int) (16.0f * PIXEL_DENSITY), (int) (PIXEL_DENSITY * 0.0f), graypaint);
        }
        Config config = new Config((int) (PIXEL_DENSITY * 50.0f), (int) (PIXEL_DENSITY * 34.0f), paint);
        ICON_TITLE_CONFIG = config;
        this.mIconTilteConfig = config;
        SRC_PAINT.setXfermode(new PorterDuffXfermode(Mode.SRC));
    }

    private boolean bindPopupTexture(GLCanvas glCanvas) {
        TextureManager tm = glCanvas.getTextureManager();
        if (!this.mPopupTexture.mNeedsDraw) {
            return tm.bindForce(this.mPopupTexture);
        }
        boolean bindOK = tm.bindReplace(this.mPopupTexture, this.mPopupTexture.generateBitmap());
        this.mPopupTexture.mNeedsDraw = false;
        return bindOK;
    }

    public static int clamp(int value, int min, int max) {
        if (value < min) {
            return min;
        }
        return value > max ? max : value;
    }

    private int hitTestOptions(int x, int y) {
        Option[] options = this.mOptions;
        int numOptions = options.length;
        x = (int) (((float) x) - this.mShowX);
        y = (int) (((float) y) - this.mShowY);
        if (numOptions != 0 && x >= 0 && ((float) x) < this.mShowWidth && y >= 0) {
            int i = PADDING_TOP;
            while (i != numOptions) {
                if (y < options[i].mBottom) {
                    return i;
                }
                i++;
            }
        }
        return -1;
    }

    private void layout() {
        this.mNeedsLayout = false;
        Option[] options = this.mOptions;
        int numOptions = options.length;
        int maxWidth = (int) (100.0f * PIXEL_DENSITY);
        int i = PADDING_TOP;
        while (i != numOptions) {
            Option option = options[i];
            IconTitleDrawable drawable = option.mDrawable;
            if (drawable == null) {
                if (option.mIsGray) {
                    drawable = new IconTitleDrawable(option.mTitle, option.mIcon, ICON_TITLE_GRAY_CONFIG);
                } else if (option.mIcon != null) {
                    drawable = new IconTitleDrawable(option.mTitle, option.mIcon, this.mIconTilteConfig);
                } else {
                    drawable = new IconTitleDrawable(option.mTitle, option.mIcon, ICON_TITLE_CONFIG);
                }
                if (i == numOptions - 1) {
                    drawable.setDrawLine(false);
                }
                option.mDrawable = drawable;
            }
            int width = drawable.getIntrinsicWidth();
            if (width > maxWidth) {
                maxWidth = width;
            }
            i++;
        }
        int rowHeight = (int) (((float) this.mRowHeight) * PIXEL_DENSITY);
        int left = (int) (15.0f * PIXEL_DENSITY);
        int top = (int) (0.0f * PIXEL_DENSITY);
        int right = left + maxWidth;
        i = PADDING_TOP;
        while (i != numOptions) {
            option = options[i];
            drawable = option.mDrawable;
            option.mBottom = top + rowHeight;
            drawable.setBounds(left, top, right, option.mBottom);
            top += rowHeight;
            i++;
        }
        setSize(((float) right) + 15.0f * PIXEL_DENSITY, ((float) top) + 22.0f * PIXEL_DENSITY);
    }

    private void setSelectedItem(int hit) {
        if (this.mSelectedItem != hit) {
            this.mSelectedItem = hit;
            this.mPopupTexture.setNeedsDraw();
            if (this.mListener != null) {
                this.mListener.onSelectionChanged(this, hit);
            }
        }
    }

    public void addToRenderList(GLContext glContext) {
        Log.i(TAG, "----------------------> not need addToRenderList");
    }

    public void close(boolean fadeOut) {
        if (this.mShow) {
            if (fadeOut) {
                this.mShowAnim.animateValue(this.mShowAnim.getCurValue(), 0.0f, 300);
            } else {
                this.mShowAnim.stop();
            }
            this.mShow = false;
            this.mSelectedItem = -1;
        }
    }

    public boolean onKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (event.getAction() == 1) {
            switch (keyCode) {
                case JsonWriteContext.STATUS_EXPECT_VALUE:
                    close(true);
                    break;
                case TimeUtils.HUNDRED_DAY_FIELD_LEN:
                    setSelectedItem(clamp(this.mSelectedItem - 1, PADDING_TOP, this.mOptions.length - 1));
                    break;
                case KeyTouchInputEvent.EV_REP:
                    setSelectedItem(clamp(this.mSelectedItem + 1, PADDING_TOP, this.mOptions.length - 1));
                    break;
                case Opcodes.FLOAD:
                case DataManager.INCLUDE_LOCAL_VIDEO_ONLY:
                    int selectedItem = this.mSelectedItem;
                    if (this.mSelectedItem >= 0 && this.mSelectedItem < this.mOptions.length) {
                        this.mOptions[this.mSelectedItem].mAction.run();
                        if (this.mListener != null) {
                            this.mListener.onSelectionClicked(this, selectedItem);
                        } else {
                            android.util.Log.i(TAG, " mListener is null");
                        }
                    }
                    break;
            }
        }
        return true;
    }

    protected void onLayout(boolean changeSize) {
        super.onLayout(changeSize);
        this.mPopupTexture.setSize((int) this.mShowWidth, (int) this.mShowHeight);
    }

    protected void onRender(GLCanvas glCanvas, int left, int top) {
        glCanvas.getGL().glBlendFunc(1, 771);
        float showRatio = this.mShowAnim.getCurValue();
        boolean show = this.mShow;
        if (showRatio < 0.003f && !show) {
            setVisibility(false);
        }
        if (bindPopupTexture(glCanvas)) {
            int x = (int) this.mShowX;
            int y = (int) this.mShowY;
            if (!show || showRatio >= 1.0f) {
                glCanvas.setAlpha(showRatio);
                glCanvas.draw2D((float) x, (float) y, 0.0f, this.mShowWidth, this.mShowHeight);
            } else {
                float scale;
                if (showRatio < 0.7f) {
                    scale = 0.8f + (0.3f * showRatio) / 0.7f;
                } else {
                    scale = 1.0f + ((1.0f - showRatio) / (1.0f - 0.7f)) * 0.1f;
                }
                glCanvas.drawWithEffect((float) x, (float) y, this.mShowWidth, this.mShowHeight, 0.5f, 0.65f, showRatio, scale);
            }
            glCanvas.setAlpha(1.0f);
            if (this.mShowAnim.isAnimating()) {
                invalidate();
            }
        } else {
            android.util.Log.e(TAG, "bindPopupTexture() return false");
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        int hit = hitTestOptions((int) event.getX(), (int) event.getY());
        switch (event.getAction()) {
            case PADDING_TOP:
            case ClassWriter.COMPUTE_FRAMES:
                setSelectedItem(hit);
                break;
            case LocalAudioAll.SORT_BY_DATE:
                if (hit == -1 || this.mSelectedItem != hit) {
                    close(true);
                    setSelectedItem(-1);
                } else {
                    this.mOptions[hit].mAction.run();
                    if (this.mListener != null) {
                        this.mListener.onSelectionClicked(this, hit);
                    } else {
                        android.util.Log.i(TAG, " mListener is null");
                    }
                    setSelectedItem(-1);
                }
                break;
            case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                setSelectedItem(-1);
                break;
        }
        return true;
    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    public void setNeedsDraw() {
        this.mNeedsLayout = true;
        this.mPopupTexture.setNeedsDraw();
    }

    public void setOption(Option option, int index) {
        close(false);
        this.mOptions[index] = option;
        this.mNeedsLayout = true;
    }

    public void setOptions(Option[] options) {
        close(false);
        this.mOptions = options;
        this.mNeedsLayout = true;
    }

    public void setPosition(float x, float y) {
        this.mShowX = x;
        this.mShowY = y;
    }

    public void setShowIcon(int index, boolean bShow) {
        Option option;
        int numOptions = this.mOptions.length;
        if (numOptions <= index || index >= 0) {
            option = this.mOptions[index];
        } else {
            option = this.mOptions[index];
        }
        if (option != null && option.mIcon != null) {
            if (option.mDrawable == null) {
                IconTitleDrawable drawable = new IconTitleDrawable(option.mTitle, option.mIcon, this.mIconTilteConfig);
                if (index == numOptions - 1) {
                    drawable.setDrawLine(false);
                }
                option.mDrawable = drawable;
            }
            option.mDrawable.setDrawIcon(bShow);
        }
    }

    public void setSize(float width, float height) {
        if (this.mShowWidth != width || this.mShowHeight != height) {
            this.mShowWidth = width;
            this.mShowHeight = height;
        }
    }

    public void showAtPoint(int pointX, int pointY, int outerWidth, int outerHeight) {
        if (this.mNeedsLayout) {
            layout();
        }
        setPosition((float) clamp(pointX, PADDING_TOP, outerWidth - ((int) this.mShowWidth)), (float) pointY);
        this.mShow = true;
        setVisibility(false);
        requestFocus();
        this.mShowAnim.animateValue(1.0f, 0.0f, 400);
        if (this.mOptions != null && this.mOptions.length >= 1) {
            setSelectedItem(PADDING_TOP);
        }
    }
}