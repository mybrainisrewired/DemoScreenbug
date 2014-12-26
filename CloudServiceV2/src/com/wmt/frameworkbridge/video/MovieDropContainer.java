package com.wmt.frameworkbridge.video;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.wmt.frameworkbridge.video.MovieSubtitleTextView.SubtitleChangeListener;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;

public class MovieDropContainer extends LinearLayout implements MovieDropTarget, SubtitleChangeListener {
    private static final int TEXT_SIZE_MAX = 40;
    private static final int TEXT_SIZE_MIN = 12;
    private int REAL_WINDOW_HEIGHT;
    private int REAL_WINDOW_WIDTH;
    private LayoutParams layoutParams;
    private Context mContext;
    private int mCurrentOrientation;
    private float mCurrentTextSize;
    private MovieDragController mDragController;
    private MovieDragLayer mDragLayer;
    private Handler mHandler;
    private boolean mIsDragging;
    private float mMaxFontHeight;
    private float mMinFontHeight;
    private boolean mResizeSize;
    private Runnable mSaveParamsRunnable;
    private TextView mSubtitle;

    public static class LayoutParams {
        private static final String KEY_HEIGHT_Y_LAND = "height_land_y";
        private static final String KEY_HEIGHT_Y_PORT = "height_port_y";
        private static final String KEY_POSITION_Y_LAND = "position_land_y";
        private static final String KEY_POSITION_Y_PORT = "position_port_y";
        private static final String KEY_TEXTSIZE_LAND = "text_size_land";
        private static final String KEY_TEXTSIZE_PORT = "text_size_port";
        private static final String KEY_TEXT_COLOR = "text_color";
        private static final String LAYOUTPARAMS_FILE = "layout_params_file";
        public int height;
        public int width;
        public int x;
        public int y;

        public LayoutParams() {
            this.x = 0;
            this.y = -1;
            this.width = -1;
            this.height = -1;
        }

        public LayoutParams(int posX, int posY, int viewWidth, int viewHeight) {
            this.x = 0;
            this.y = -1;
            this.width = -1;
            this.height = -1;
            this.x = posX;
            this.y = posY;
            this.width = viewWidth;
            this.height = viewHeight;
        }

        public LayoutParams(com.wmt.frameworkbridge.video.MovieDropContainer.LayoutParams lp) {
            this.x = 0;
            this.y = -1;
            this.width = -1;
            this.height = -1;
            this.x = lp.x;
            this.y = lp.y;
            this.width = lp.width;
            this.height = lp.height;
        }

        public static int getSubtitleLayoutParamsH(Context context, int orientation) {
            SharedPreferences sp = context.getSharedPreferences(LAYOUTPARAMS_FILE, ClassWriter.COMPUTE_FRAMES);
            return orientation == 2 ? sp.getInt(KEY_HEIGHT_Y_LAND, -1) : sp.getInt(KEY_HEIGHT_Y_PORT, -1);
        }

        public static int getSubtitleLayoutParamsY(Context context, int orientation) {
            SharedPreferences sp = context.getSharedPreferences(LAYOUTPARAMS_FILE, ClassWriter.COMPUTE_FRAMES);
            return orientation == 2 ? sp.getInt(KEY_POSITION_Y_LAND, -1) : sp.getInt(KEY_POSITION_Y_PORT, -1);
        }

        public static int getSubtitleTextColor(Context context) {
            return context.getSharedPreferences(LAYOUTPARAMS_FILE, ClassWriter.COMPUTE_FRAMES).getInt(KEY_TEXT_COLOR, -1);
        }

        public static float getSubtitleTextSize(Context context, int orientation) {
            SharedPreferences sp = context.getSharedPreferences(LAYOUTPARAMS_FILE, ClassWriter.COMPUTE_FRAMES);
            return orientation == 2 ? sp.getFloat(KEY_TEXTSIZE_LAND, -1.0f) : sp.getFloat(KEY_TEXTSIZE_PORT, -1.0f);
        }

        public static void saveSubtitleLayoutParamsH(Context context, int height, int orientation) {
            Editor edit = context.getSharedPreferences(LAYOUTPARAMS_FILE, ClassWriter.COMPUTE_FRAMES).edit();
            if (orientation == 2) {
                edit.putInt(KEY_HEIGHT_Y_LAND, height);
            } else {
                edit.putInt(KEY_HEIGHT_Y_PORT, height);
            }
            edit.commit();
        }

        public static void saveSubtitleLayoutParamsY(Context context, int posY, int orientation) {
            Editor edit = context.getSharedPreferences(LAYOUTPARAMS_FILE, ClassWriter.COMPUTE_FRAMES).edit();
            if (orientation == 2) {
                edit.putInt(KEY_POSITION_Y_LAND, posY);
            } else {
                edit.putInt(KEY_POSITION_Y_PORT, posY);
            }
            edit.commit();
        }

        public static void saveSubtitleTextColor(Context context, int color) {
            Editor edit = context.getSharedPreferences(LAYOUTPARAMS_FILE, ClassWriter.COMPUTE_FRAMES).edit();
            edit.putInt(KEY_TEXT_COLOR, color);
            edit.commit();
        }

        public static void saveSubtitleTextSize(Context context, float textSize, int orientation) {
            Editor edit = context.getSharedPreferences(LAYOUTPARAMS_FILE, ClassWriter.COMPUTE_FRAMES).edit();
            if (orientation == 2) {
                edit.putFloat(KEY_TEXTSIZE_LAND, textSize);
            } else {
                edit.putFloat(KEY_TEXTSIZE_PORT, textSize);
            }
            edit.commit();
        }

        public void reset() {
            this.x = 0;
            this.y = -1;
            this.width = -1;
            this.height = -1;
        }
    }

    public MovieDropContainer(Context context) {
        this(context, null);
    }

    public MovieDropContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MovieDropContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mCurrentOrientation = -1;
        this.mResizeSize = false;
        this.mIsDragging = false;
        this.mSaveParamsRunnable = new Runnable() {
            public void run() {
                int[] result = MovieDropContainer.this.getMinMaxHeight();
                if (MovieDropContainer.this.layoutParams.height < result[0]) {
                    MovieDropContainer.this.layoutParams.height = result[0];
                }
                if (MovieDropContainer.this.layoutParams.height > result[1]) {
                    MovieDropContainer.this.layoutParams.height = result[1];
                }
                com.wmt.frameworkbridge.video.MovieDropContainer.LayoutParams.saveSubtitleLayoutParamsY(MovieDropContainer.this.mContext, MovieDropContainer.this.layoutParams.y, MovieDropContainer.this.mCurrentOrientation);
                com.wmt.frameworkbridge.video.MovieDropContainer.LayoutParams.saveSubtitleTextSize(MovieDropContainer.this.mContext, MovieDropContainer.this.mCurrentTextSize, MovieDropContainer.this.mCurrentOrientation);
            }
        };
        init(context);
    }

    private void adjustSubtitleTextSize() {
        int paddingTop = this.mSubtitle.getPaddingTop();
        int paddingBottom = this.mSubtitle.getPaddingBottom();
        int linecount = this.mSubtitle.getLineCount();
        int lineSpace = this.mSubtitle.getLineHeight();
        if (linecount <= 0) {
            linecount = 1;
        }
        float textSize = (((1.0f * ((float) (((this.layoutParams.height - paddingTop) - paddingBottom) + ((linecount - 1) * lineSpace)))) / (this.mMaxFontHeight - this.mMinFontHeight)) * 28.0f) / ((float) linecount);
        if (textSize > 40.0f) {
            textSize = 40.0f;
        } else if (textSize < 12.0f) {
            textSize = 12.0f;
        }
        this.mCurrentTextSize = textSize;
        this.mSubtitle.setTextSize(textSize);
    }

    private void init(Context context) {
        this.mContext = context;
        this.mHandler = new Handler();
        this.layoutParams = new LayoutParams();
        Paint paint = new Paint();
        paint.setTextSize(12.0f);
        FontMetrics fm = paint.getFontMetrics();
        this.mMinFontHeight = fm.descent - fm.ascent;
        paint.setTextSize(40.0f);
        fm = paint.getFontMetrics();
        this.mMaxFontHeight = fm.descent - fm.ascent;
        setCurrentOrientation(context.getResources().getConfiguration().orientation);
    }

    private void setCurrentOrientation(int curOrientation) {
        this.mCurrentOrientation = curOrientation;
    }

    private void setSubtitleTextSize(float textsize) {
        if (textsize > 11.0f && textsize < 41.0f) {
            this.mCurrentTextSize = textsize;
        } else if (textsize < 0.0f) {
            this.mCurrentTextSize = 24.0f;
        } else {
            this.mCurrentTextSize = this.mSubtitle.getTextSize();
        }
        this.mSubtitle.setTextSize(this.mCurrentTextSize);
    }

    public int[] getMinMaxHeight() {
        int[] result = new int[2];
        int paddingTop = this.mSubtitle.getPaddingTop();
        int paddingBottom = this.mSubtitle.getPaddingBottom();
        int linecount = this.mSubtitle.getLineCount();
        int lineSpace = this.mSubtitle.getLineHeight();
        if (linecount <= 0) {
            linecount = 1;
        }
        if (lineSpace <= 0) {
            lineSpace = 0;
        }
        float maxHeight = ((float) (paddingTop + paddingBottom + (linecount - 1) * lineSpace)) + this.mMaxFontHeight * ((float) linecount);
        result[0] = (int) (((float) (paddingTop + paddingBottom + (linecount - 1) * lineSpace)) + this.mMinFontHeight * ((float) linecount));
        result[1] = (int) maxHeight;
        return result;
    }

    public LayoutParams getSubtitleLayoutParams() {
        return this.layoutParams;
    }

    public TextView getSubtitleTextView() {
        return this.mSubtitle;
    }

    public void notifySubtitleLayoutParamsChanged(int y, int height) {
        this.layoutParams.y = y;
        this.layoutParams.height = height;
        adjustSubtitleTextSize();
        this.mHandler.removeCallbacks(this.mSaveParamsRunnable);
        this.mHandler.postDelayed(this.mSaveParamsRunnable, 500);
        requestLayout();
    }

    public void notifyTextChanged(String text) {
        if (!(this.mIsDragging || this.mSubtitle.getVisibility() == 0)) {
            this.mSubtitle.setVisibility(0);
        }
        this.mSubtitle.setText(text);
        this.mDragController.notifySubtitleContentChanged(text);
    }

    public void notifyTextColorChanged(int color) {
        this.mSubtitle.setTextColor(color);
        LayoutParams.saveSubtitleTextColor(this.mContext, color);
    }

    public void notifyTextSizeChanged(int size) {
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mDragController.setWindowToken(getWindowToken());
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.layoutParams.reset();
        setCurrentOrientation(newConfig.orientation);
        this.mCurrentTextSize = LayoutParams.getSubtitleTextSize(this.mContext, this.mCurrentOrientation);
        setSubtitleTextSize(this.mCurrentTextSize);
    }

    public void onDrop(int x, int y, int xOffset, int yOffset) {
        this.mIsDragging = false;
        int childCount = getChildCount();
        int i = 0;
        while (i < childCount) {
            if (getChildAt(i) instanceof TextView) {
                this.layoutParams.x = 0;
                this.layoutParams.y = y - yOffset;
                if (this.layoutParams.y > this.REAL_WINDOW_HEIGHT - 30) {
                    this.layoutParams.y = this.REAL_WINDOW_HEIGHT - 30;
                }
                LayoutParams.saveSubtitleLayoutParamsY(this.mContext, this.layoutParams.y, this.mCurrentOrientation);
            }
            i++;
        }
        this.mResizeSize = true;
        requestLayout();
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int curY;
        super.onLayout(changed, l, t, r, b);
        int height = -1;
        int width = -1;
        if (this.mSubtitle != null) {
            height = this.mSubtitle.getHeight();
            width = this.mSubtitle.getWidth();
        }
        if (this.layoutParams.y >= 0) {
            curY = this.layoutParams.y;
        } else {
            curY = LayoutParams.getSubtitleLayoutParamsY(this.mContext, this.mCurrentOrientation);
        }
        if (height > 0 && width > 0 && curY >= 0) {
            this.layoutParams.height = height;
            this.layoutParams.width = width;
            this.layoutParams.y = curY;
            int childCount = getChildCount();
            int i = 0;
            while (i < childCount) {
                View child = getChildAt(i);
                if (child.getVisibility() != 8) {
                    child.layout(this.layoutParams.x, this.layoutParams.y, this.layoutParams.width + this.layoutParams.x, this.layoutParams.y + this.layoutParams.height);
                }
                i++;
            }
        }
        if (this.mResizeSize) {
            this.mResizeSize = false;
            this.mDragLayer.addSubtitleResizeLayout(this.mSubtitle);
            requestLayout();
        }
    }

    public void setDragController(MovieDragController dc) {
        this.mDragController = dc;
    }

    public void setDragLayer(MovieDragLayer dl) {
        this.mDragLayer = dl;
    }

    public void setOnLongClickListener(OnLongClickListener l) {
        int childCount = getChildCount();
        int i = 0;
        while (i < childCount) {
            getChildAt(i).setOnLongClickListener(l);
            i++;
        }
    }

    public void setSubtitleTextView(TextView tv) {
        this.mSubtitle = tv;
        this.mCurrentTextSize = LayoutParams.getSubtitleTextSize(this.mContext, this.mCurrentOrientation);
        setSubtitleTextSize(this.mCurrentTextSize);
        this.mSubtitle.setTextColor(LayoutParams.getSubtitleTextColor(this.mContext));
    }

    public void startDrag() {
        this.mIsDragging = true;
        this.mDragController.startDrag(this.mSubtitle, MovieDragController.DRAG_ACTION_MOVE);
    }

    public void updateWindowSize(int width, int height) {
        this.REAL_WINDOW_WIDTH = width;
        this.REAL_WINDOW_HEIGHT = height;
    }
}