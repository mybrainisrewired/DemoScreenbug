package com.wmt.frameworkbridge.video;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.SweepGradient;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.wmt.data.LocalAudioAll;
import com.wmt.data.MediaObject;
import com.wmt.data.utils.ReverseGeocoder;
import com.wmt.opengl.grid.ItemAnimation;
import com.wmt.util.ColorBaseSlot;

public class MovieColorPickerView extends View {
    private static final int ALPHA_WIDTH = 30;
    private static final int CENTER_RADIUS = 20;
    private static final int CENTER_X = 75;
    private static final int CENTER_Y = 75;
    private static final float PI = 3.1415925f;
    private Paint mAlphaLinePaint;
    private Paint mAlphaPaint;
    private Paint mCenterPaint;
    private int[] mColors;
    private Paint mGrayPaint;
    private int[] mGrays;
    private boolean mHighlightCenter;
    private OnColorChangedListener mListener;
    private Paint mPaint;
    private boolean mTrackingCenter;

    public static interface OnColorChangedListener {
        void colorChanged(int i);
    }

    MovieColorPickerView(Context c) {
        super(c);
        init(c, null, -1);
    }

    public MovieColorPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, null, -1);
    }

    public MovieColorPickerView(Context context, AttributeSet attrs, int def) {
        super(context, attrs, def);
        init(context, null, -1);
    }

    private int ave(int s, int d, float p) {
        return Math.round(((float) (d - s)) * p) + s;
    }

    private int floatToByte(float x) {
        return Math.round(x);
    }

    private int interpColor(int[] colors, float unit) {
        if (unit <= 0.0f) {
            return colors[0];
        }
        if (unit >= 1.0f) {
            return colors[colors.length - 1];
        }
        float p = unit * ((float) (colors.length - 1));
        int i = (int) p;
        p -= (float) i;
        int c0 = colors[i];
        int c1 = colors[i + 1];
        return Color.argb(ave(Color.alpha(c0), Color.alpha(c1), p), ave(Color.red(c0), Color.red(c1), p), ave(Color.green(c0), Color.green(c1), p), ave(Color.blue(c0), Color.blue(c1), p));
    }

    private int offsetColorOfBlack(int color) {
        return Color.argb(Color.alpha(color), Color.red(color), Color.blue(color), Color.blue(color));
    }

    private int pinToByte(int n) {
        if (n < 0) {
            return 0;
        }
        return n > 255 ? MotionEventCompat.ACTION_MASK : n;
    }

    public int getCenterColor() {
        return this.mCenterPaint.getColor();
    }

    public void init(Context c, OnColorChangedListener l, int color) {
        this.mListener = l;
        this.mColors = new int[]{-65536, -65281, -16776961, -16711681, -16711936, -256, -65536};
        this.mGrays = new int[]{-15724528, -13421773, -10066330, -6710887, -3355444, -1, -3355444, -6710887, -10066330, -13421773, -15724528};
        Shader s = new SweepGradient(0.0f, 0.0f, this.mColors, null);
        Shader g = new SweepGradient(0.0f, 0.0f, this.mGrays, null);
        this.mPaint = new Paint(1);
        this.mPaint.setShader(s);
        this.mPaint.setStyle(Style.STROKE);
        this.mPaint.setStrokeWidth(32.0f);
        this.mGrayPaint = new Paint(1);
        this.mGrayPaint.setShader(g);
        this.mGrayPaint.setStyle(Style.STROKE);
        this.mGrayPaint.setStrokeWidth(27.0f);
        this.mAlphaPaint = new Paint(1);
        this.mAlphaPaint.setStyle(Style.FILL);
        this.mAlphaLinePaint = new Paint(1);
        this.mAlphaLinePaint.setColor(ColorBaseSlot.INVALID_COLOR);
        this.mAlphaLinePaint.setStrokeWidth(2.0f);
        this.mCenterPaint = new Paint(1);
        this.mCenterPaint.setColor(color);
        this.mCenterPaint.setStrokeWidth(5.0f);
    }

    protected void onDraw(Canvas canvas) {
        float r = 75.0f - this.mPaint.getStrokeWidth() * 0.5f;
        float gr = r - this.mPaint.getStrokeWidth() * 0.5f - this.mGrayPaint.getStrokeWidth() * 0.5f;
        canvas.translate(75.0f, 75.0f);
        canvas.drawOval(new RectF(-r, -r, r, r), this.mPaint);
        canvas.drawOval(new RectF(-gr, -gr, gr, gr), this.mGrayPaint);
        canvas.drawCircle(0.0f, 0.0f, 20.0f, this.mCenterPaint);
        if (this.mTrackingCenter) {
            int c = this.mCenterPaint.getColor();
            this.mCenterPaint.setStyle(Style.STROKE);
            if (this.mHighlightCenter) {
                this.mCenterPaint.setAlpha(MotionEventCompat.ACTION_MASK);
            } else {
                this.mCenterPaint.setAlpha(MediaObject.SUPPORT_PLAY);
            }
            canvas.drawCircle(0.0f, 0.0f, this.mCenterPaint.getStrokeWidth() + 20.0f, this.mCenterPaint);
            this.mCenterPaint.setStyle(Style.FILL);
            this.mCenterPaint.setColor(c);
        }
        int ca = this.mCenterPaint.getColor();
        this.mAlphaPaint.setShader(new LinearGradient(-75.0f, 75.0f, 75.0f, 75.0f, Color.argb(MotionEventCompat.ACTION_MASK, Color.red(ca), Color.green(ca), Color.blue(ca)), Color.argb(0, Color.red(ca), Color.green(ca), Color.blue(ca)), TileMode.CLAMP));
        canvas.drawRect(new RectF(-75.0f, 75.0f, 75.0f, 105.0f), this.mAlphaPaint);
        float xx = (float) (75 - (Color.alpha(ca) * 150) / 255);
        canvas.drawLine(xx, 75.0f, xx, 105.0f, this.mAlphaLinePaint);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(150, ReverseGeocoder.LON_MAX);
    }

    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX() - 75.0f;
        float y = event.getY() - 75.0f;
        boolean inCenter = Math.sqrt((double) ((x * x) + (y * y))) <= 20.0d;
        boolean inGray;
        boolean inAlpha;
        float angle;
        float unit;
        int c;
        int cc;
        float xx;
        switch (event.getAction()) {
            case LocalAudioAll.SORT_BY_TITLE:
                this.mTrackingCenter = inCenter;
                if (inCenter) {
                    this.mHighlightCenter = true;
                    invalidate();
                }
                if (this.mTrackingCenter) {
                    inGray = Math.sqrt((double) ((x * x) + (y * y))) > ((double) (75.0f - this.mPaint.getStrokeWidth()));
                    inAlpha = y < 75.0f;
                    angle = (float) Math.atan2((double) y, (double) x);
                    unit = angle / 6.283185f;
                    if (unit < 0.0f) {
                        unit += 1.0f;
                    }
                    if (inGray) {
                        if (angle < 0.0f) {
                            angle *= -1.0f;
                        }
                        c = floatToByte((255.0f * angle) / 3.1415925f);
                        this.mCenterPaint.setColor(Color.argb(MotionEventCompat.ACTION_MASK, pinToByte(c), pinToByte(c), pinToByte(c)));
                    } else if (inAlpha) {
                        this.mCenterPaint.setColor(interpColor(this.mColors, unit));
                    } else {
                        cc = this.mCenterPaint.getColor();
                        xx = event.getX();
                        if (xx < 0.0f) {
                            xx = 0.0f;
                        } else if (xx > 150.0f) {
                            xx = 150.0f;
                        }
                        this.mCenterPaint.setColor(Color.argb((int) ((255.0f * (150.0f - xx)) / 150.0f), Color.red(cc), Color.green(cc), Color.blue(cc)));
                    }
                    invalidate();
                } else if (this.mHighlightCenter != inCenter) {
                    this.mHighlightCenter = inCenter;
                    invalidate();
                }
                break;
            case LocalAudioAll.SORT_BY_DATE:
                if (this.mListener != null) {
                    this.mListener.colorChanged(this.mCenterPaint.getColor());
                }
                this.mTrackingCenter = false;
                invalidate();
                break;
            case ItemAnimation.CUR_Z:
                if (this.mTrackingCenter) {
                    if (Math.sqrt((double) ((x * x) + (y * y))) > ((double) (75.0f - this.mPaint.getStrokeWidth()))) {
                    }
                    if (y < 75.0f) {
                    }
                    angle = (float) Math.atan2((double) y, (double) x);
                    unit = angle / 6.283185f;
                    if (unit < 0.0f) {
                        unit += 1.0f;
                    }
                    if (inGray) {
                        if (angle < 0.0f) {
                            angle *= -1.0f;
                        }
                        c = floatToByte((255.0f * angle) / 3.1415925f);
                        this.mCenterPaint.setColor(Color.argb(MotionEventCompat.ACTION_MASK, pinToByte(c), pinToByte(c), pinToByte(c)));
                    } else if (inAlpha) {
                        this.mCenterPaint.setColor(interpColor(this.mColors, unit));
                    } else {
                        cc = this.mCenterPaint.getColor();
                        xx = event.getX();
                        if (xx < 0.0f) {
                            xx = 0.0f;
                        } else if (xx > 150.0f) {
                            xx = 150.0f;
                        }
                        this.mCenterPaint.setColor(Color.argb((int) ((255.0f * (150.0f - xx)) / 150.0f), Color.red(cc), Color.green(cc), Color.blue(cc)));
                    }
                    invalidate();
                } else if (this.mHighlightCenter != inCenter) {
                    this.mHighlightCenter = inCenter;
                    invalidate();
                }
                break;
        }
        return true;
    }

    public void setOnColorChangedListener(OnColorChangedListener l) {
        this.mListener = l;
    }
}