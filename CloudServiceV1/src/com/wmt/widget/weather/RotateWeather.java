package com.wmt.widget.weather;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.RemoteViews.RemoteView;
import com.wmt.remotectrl.ConnectionInstance;
import com.wmt.res.CommRes;
import com.wmt.util.ColorBaseSlot;
import com.wmt.widget.weather.WeatherUtils.Callbacks;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@RemoteView
public class RotateWeather extends View implements OnClickListener, Callbacks {
    private static final String ACTION_CHECK = "com.wmt.weather.Service.intent.action.CHECK_WEATHER";
    private static final String CLOCK_FONT = "/system/fonts/Roboto-Regular.ttf";
    private static final boolean DEBUG = false;
    private static final String EXTRA_FORCE_UPDATE = "force_update";
    private static final String EXTRA_UPDATE_CITY = "update_city";
    private static final String FONT_DIR = "/system/fonts/";
    private static final int MSG_WEATHER_ANIMATION = 1;
    private static final float SHADOW_LARGE_RADIUS = 4.0f;
    private static final int SHADOW_SMALL_COLOUR = -872415232;
    private static final float SHADOW_Y_OFFSET = 2.0f;
    private static final String TAG = "RotateWeather";
    private static final String midChar = "~";
    private static Typeface sSolidType;
    private static Bitmap s_nullBitmap;
    private Bitmap mAppIconBmp;
    private boolean mBDegreeUnit;
    private Bitmap mBackWeatherBmp;
    private String mButtonStrBack;
    private String mButtonStrNext;
    private int mChinese;
    private String mCurFailedString;
    private int mCurValidDay;
    private float mDegrees;
    private float mDrawScaleX;
    private float mDrawScaleY;
    private Bitmap mFirstForcastBmp;
    private float mFromDegrees;
    private Bitmap mFrontWeatherBmp;
    private MyHandler mHandler;
    private String mHumidityString;
    private boolean mIsInBack;
    private boolean mIsNoCity;
    private boolean mIsRefresh;
    private String mNewCity;
    private String mNoCityString;
    private Paint mPaint;
    private Paint mPaintTextBlack;
    private Paint mPaintTextWhite;
    private String mRealUnit;
    private float mRefreshAngle;
    private Bitmap mRefreshWeatherBmp;
    private Bitmap mSecondForcastBmp;
    private float mTargetDegrees;
    private Bitmap mThirdForcastBmp;
    private Bitmap mToForcastBmp;
    private Bitmap mTodayForcastBmp;
    private WeatherItem mWeatherItem;
    private WeatherUtils mWeatherUtils;
    private String mWindConditionString;
    private float mXpos;
    private float mYpos;
    private boolean mbShadow;

    private class MyHandler extends Handler {
        private MyHandler() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_WEATHER_ANIMATION:
                    if (RotateWeather.this.mDegrees == RotateWeather.this.mTargetDegrees) {
                        RotateWeather.this.mIsInBack = !RotateWeather.this.mIsInBack ? true : DEBUG;
                    } else {
                        if (RotateWeather.this.mFromDegrees < RotateWeather.this.mTargetDegrees) {
                            RotateWeather.access$116(RotateWeather.this, 10.0f);
                        } else if (RotateWeather.this.mFromDegrees > RotateWeather.this.mTargetDegrees) {
                            RotateWeather.access$124(RotateWeather.this, 10.0f);
                        }
                        RotateWeather.this.invalidate();
                        RotateWeather.this.startWeatherAnimation();
                    }
                default:
                    break;
            }
        }
    }

    static {
        s_nullBitmap = Bitmap.createBitmap(MSG_WEATHER_ANIMATION, MSG_WEATHER_ANIMATION, Config.RGB_565);
    }

    public RotateWeather(Context context) {
        super(context);
        this.mDrawScaleX = 0.9f;
        this.mDrawScaleY = 0.8f;
        this.mHandler = new MyHandler(null);
        init(context);
    }

    public RotateWeather(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mDrawScaleX = 0.9f;
        this.mDrawScaleY = 0.8f;
        this.mHandler = new MyHandler(null);
        init(context);
    }

    public RotateWeather(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mDrawScaleX = 0.9f;
        this.mDrawScaleY = 0.8f;
        this.mHandler = new MyHandler(null);
        init(context);
    }

    static /* synthetic */ float access$116(RotateWeather x0, float x1) {
        float f = x0.mDegrees + x1;
        x0.mDegrees = f;
        return f;
    }

    static /* synthetic */ float access$124(RotateWeather x0, float x1) {
        float f = x0.mDegrees - x1;
        x0.mDegrees = f;
        return f;
    }

    private Bitmap bitmapFactory(String filePath) {
        try {
            Options options = new Options();
            options.inSampleSize = 1;
            return BitmapFactory.decodeFile(filePath, options);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String constrcutAverageDayTemp(int index, String unit) {
        boolean z = true;
        if (index < 0 || index >= 4) {
            Log.e(TAG, "Index of Day out of boundary!!");
            return "";
        } else {
            int th = 0;
            int tl = 0;
            if (this.mWeatherItem == null || this.mWeatherItem.mForecastConditions[index] == null) {
                return "";
            }
            String high = this.mWeatherItem.mForecastConditions[index].mHigh;
            String low = this.mWeatherItem.mForecastConditions[index].mLow;
            if (high == null || high.equals("") || low == null || low.equals("")) {
                return "";
            }
            try {
                if (this.mBDegreeUnit) {
                    th = Integer.valueOf(high).intValue();
                    tl = Integer.valueOf(low).intValue();
                } else {
                    th = getTempValue(!this.mBDegreeUnit, Integer.valueOf(high).intValue());
                    if (this.mBDegreeUnit) {
                        z = false;
                    }
                    tl = getTempValue(z, Integer.valueOf(low).intValue());
                }
            } catch (Exception e) {
                Log.e(TAG, "Average Day: nvalid high " + high + " and low " + low);
            }
            return ((tl + th) / 2) + " " + unit;
        }
    }

    private String contructDayTempStr(int index, String unit) {
        boolean z = true;
        if (index < 0 || index >= 4) {
            Log.e(TAG, "Index of Day out of boundary!!");
            return "";
        } else {
            int th = 0;
            int tl = 0;
            if (this.mWeatherItem == null || this.mWeatherItem.mForecastConditions[index] == null) {
                return "";
            }
            String high = this.mWeatherItem.mForecastConditions[index].mHigh;
            String low = this.mWeatherItem.mForecastConditions[index].mLow;
            if (high == null || high.equals("") || low == null || low.equals("")) {
                return "";
            }
            try {
                if (this.mBDegreeUnit) {
                    th = Integer.valueOf(high).intValue();
                    tl = Integer.valueOf(low).intValue();
                } else {
                    th = getTempValue(!this.mBDegreeUnit, Integer.valueOf(high).intValue());
                    if (this.mBDegreeUnit) {
                        z = false;
                    }
                    tl = getTempValue(z, Integer.valueOf(low).intValue());
                }
            } catch (Exception e) {
                Log.e(TAG, "Day Temp: Invalid high " + high + " and low " + low);
            }
            return tl + midChar + th + " " + unit;
        }
    }

    private Bitmap createWidgetBitmap(Bitmap src) {
        if (src == null) {
            return null;
        }
        Bitmap target = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Config.RGB_565);
        Canvas canvas = new Canvas(target);
        canvas.drawBitmap(src, 0.0f, 0.0f, null);
        canvas.setBitmap(s_nullBitmap);
        return target;
    }

    private void drawImage(Canvas canvas, Bitmap blt, int x, int y, int w, int h, int bx, int by, float scalex, float scaley) {
        Rect src = new Rect();
        Rect dst = new Rect();
        src.left = bx;
        src.top = by;
        src.right = bx + w;
        src.bottom = by + h;
        dst.left = x;
        dst.top = y;
        dst.right = ((int) (((float) w) * scalex)) + x;
        dst.bottom = ((int) (((float) h) * scaley)) + y;
        canvas.drawBitmap(blt, src, dst, this.mPaint);
    }

    private String getForcastDate(String date) {
        if (date == null || date.equals("")) {
            return "";
        }
        try {
            return new SimpleDateFormat("  E  yyyy-MM-dd", Locale.US).format(new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private float getProperSize(Paint paint, String str, float w, float h) {
        if (str == null || str.equals("")) {
            return 1.0f;
        }
        paint.setStyle(Style.FILL);
        float fSize = 0.75f;
        while (true) {
            paint.setTextSize(h * fSize);
            FontMetrics fm = paint.getFontMetrics();
            float fFontHeight = (float) Math.ceil((double) (fm.descent - fm.ascent));
            float fTextWidth = paint.measureText(str);
            if (fFontHeight <= h - 5.0f && fTextWidth <= w - 3.0f) {
                return h * fSize;
            }
            fSize -= 0.1f;
        }
    }

    private String getWidthestStr(Paint paint, String first, String second, String third) {
        paint.setTextSize(20.0f);
        int a = (int) paint.measureText(first);
        int b = (int) paint.measureText(second);
        int c = (int) paint.measureText(third);
        if (a < b || a < c) {
            return (a > b || c > b) ? third : second;
        } else {
            return first;
        }
    }

    private void init(Context context) {
        this.mIsInBack = false;
        this.mbShadow = true;
        if (sSolidType == null) {
            sSolidType = Typeface.createFromFile(CLOCK_FONT);
        }
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setDither(true);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setFilterBitmap(true);
        this.mPaint.setTextAlign(Align.CENTER);
        this.mPaintTextBlack = new Paint();
        this.mPaintTextBlack.setColor(ColorBaseSlot.INVALID_COLOR);
        this.mPaintTextBlack.setAntiAlias(true);
        this.mPaintTextBlack.setSubpixelText(true);
        this.mPaintTextBlack.setTextAlign(Align.LEFT);
        this.mPaintTextBlack.setTypeface(sSolidType);
        this.mPaintTextBlack.setShadowLayer(SHADOW_LARGE_RADIUS, 0.0f, SHADOW_Y_OFFSET, SHADOW_SMALL_COLOUR);
        this.mPaintTextWhite = new Paint();
        this.mPaintTextWhite.setColor(-1);
        this.mPaintTextWhite.setSubpixelText(true);
        this.mPaintTextWhite.setAntiAlias(true);
        this.mPaintTextWhite.setDither(true);
        this.mPaintTextWhite.setTextAlign(Align.LEFT);
        this.mPaintTextWhite.setTypeface(sSolidType);
        setOnClickListener(this);
        this.mWeatherUtils = new WeatherUtils(context);
        this.mWeatherUtils.setmCallbacks(this);
    }

    private Bitmap loadCurrentWeatherBitmap() {
        float w = ((float) getWidth()) * this.mDrawScaleX;
        float h = ((float) getHeight()) * this.mDrawScaleY;
        if (w == 0.0f || h == 0.0f) {
            return null;
        }
        Bitmap target = Bitmap.createBitmap((int) w, (int) h, Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        if (this.mWeatherItem == null) {
            Log.d(TAG, "No current weather item.");
            loadFailedBitmap(canvas, DEBUG);
            canvas.setBitmap(s_nullBitmap);
            return target;
        } else {
            String unit = this.mBDegreeUnit ? this.mRealUnit : "F";
            String cityString = removeProvince(this.mWeatherItem.mForecastInformation.mCity);
            float cityHight = (7.0f * h) / 32.0f;
            float proper = getProperSize(this.mPaintTextWhite, cityString, (3.0f * w) / 5.0f - 10.0f, cityHight);
            this.mPaintTextWhite.setTextSize(proper);
            FontMetrics fm = this.mPaintTextWhite.getFontMetrics();
            float y = cityHight / 2.0f - (fm.ascent + fm.descent) / 2.0f;
            if (this.mbShadow) {
                this.mPaintTextBlack.setTextSize(proper);
                canvas.drawText(cityString, 10.0f, y, this.mPaintTextBlack);
            }
            canvas.drawText(cityString, 10.0f - 1.0f, y - 1.0f, this.mPaintTextWhite);
            String dateString = this.mWeatherItem.mForecastConditions[4 - this.mCurValidDay].mDate;
            float dateHight = h / 8.0f;
            proper = getProperSize(this.mPaintTextWhite, dateString, (3.0f * w) / 5.0f - 20.0f, dateHight);
            this.mPaintTextWhite.setTextSize(proper);
            fm = this.mPaintTextWhite.getFontMetrics();
            y = dateHight / 2.0f + cityHight - (fm.ascent + fm.descent) / 2.0f;
            if (this.mbShadow) {
                this.mPaintTextBlack.setTextSize(proper);
                canvas.drawText(dateString, 10.0f, y, this.mPaintTextBlack);
            }
            canvas.drawText(dateString, 10.0f - 1.0f, y - 1.0f, this.mPaintTextWhite);
            String tempString = "";
            if (this.mCurValidDay <= 0) {
                canvas.setBitmap(s_nullBitmap);
                return target;
            } else {
                float scaleH;
                tempString = this.mWeatherItem.mForecastConditions[4 - this.mCurValidDay].mTempC;
                if (tempString.equals("")) {
                    tempString = constrcutAverageDayTemp(4 - this.mCurValidDay, unit);
                } else if (this.mBDegreeUnit) {
                    tempString = tempString + this.mRealUnit;
                } else {
                    int value = 0;
                    try {
                        value = Integer.valueOf(tempString).intValue();
                    } catch (Exception e) {
                    }
                    tempString = getTempValue(true, value) + "F";
                }
                float tempHight = (3.0f * h) / 8.0f;
                proper = getProperSize(this.mPaintTextWhite, tempString, (2.0f * w) / 5.0f - 10.0f, tempHight);
                this.mPaintTextWhite.setTextSize(proper);
                fm = this.mPaintTextWhite.getFontMetrics();
                float x = (3.0f * w) / 5.0f + 10.0f;
                y = cityHight / 2.0f + tempHight / 2.0f - (fm.ascent + fm.descent) / 2.0f;
                if (this.mbShadow) {
                    this.mPaintTextBlack.setTextSize(proper);
                    canvas.drawText(tempString, x, y, this.mPaintTextBlack);
                }
                canvas.drawText(tempString, x - 1.0f, y - 1.0f, this.mPaintTextWhite);
                String conditionString = this.mWeatherItem.mForecastConditions[4 - this.mCurValidDay].mCondition;
                float conditionHeight = (9.0f * h) / 32.0f;
                proper = getProperSize(this.mPaintTextWhite, conditionString, (3.0f * w) / 5.0f - 10.0f, conditionHeight);
                this.mPaintTextWhite.setTextSize(proper);
                fm = this.mPaintTextWhite.getFontMetrics();
                y = cityHight + dateHight + conditionHeight / 2.0f - (fm.ascent + fm.descent) / 2.0f;
                if (this.mbShadow) {
                    this.mPaintTextBlack.setTextSize(proper);
                    canvas.drawText(conditionString, 10.0f, y, this.mPaintTextBlack);
                }
                canvas.drawText(conditionString, 10.0f - 1.0f, y - 1.0f, this.mPaintTextWhite);
                String highLowString = contructDayTempStr(4 - this.mCurValidDay, unit);
                float highLowHeight = h / 8.0f;
                proper = getProperSize(this.mPaintTextWhite, highLowString, (3.0f * w) / 5.0f - 20.0f, highLowHeight);
                this.mPaintTextWhite.setTextSize(proper);
                fm = this.mPaintTextWhite.getFontMetrics();
                y = cityHight + dateHight + conditionHeight + highLowHeight / 2.0f - (fm.ascent + fm.descent) / 2.0f;
                if (this.mbShadow) {
                    this.mPaintTextBlack.setTextSize(proper);
                    canvas.drawText(highLowString, 10.0f, y, this.mPaintTextBlack);
                }
                canvas.drawText(highLowString, 10.0f - 1.0f, y - 1.0f, this.mPaintTextWhite);
                String huminityString = this.mWeatherItem.mForecastConditions[4 - this.mCurValidDay].mHumidity;
                if (huminityString.equals("")) {
                    huminityString = this.mHumidityString + " " + "N/A";
                } else {
                    huminityString = this.mHumidityString + " " + huminityString + "%";
                }
                float huminityHeight = h / 8.0f;
                proper = getProperSize(this.mPaintTextWhite, huminityString, (3.0f * w) / 5.0f - 20.0f, huminityHeight);
                this.mPaintTextWhite.setTextSize(proper);
                fm = this.mPaintTextWhite.getFontMetrics();
                y = cityHight + dateHight + conditionHeight + highLowHeight + huminityHeight / 2.0f - (fm.ascent + fm.descent) / 2.0f;
                if (this.mbShadow) {
                    this.mPaintTextBlack.setTextSize(proper);
                    canvas.drawText(huminityString, 10.0f, y, this.mPaintTextBlack);
                }
                canvas.drawText(huminityString, 10.0f - 1.0f, y - 1.0f, this.mPaintTextWhite);
                String windyString = this.mWeatherItem.mForecastConditions[4 - this.mCurValidDay].mWindCondition;
                if (windyString.equals("")) {
                    windyString = "N/A";
                }
                windyString = this.mWindConditionString + " " + windyString;
                float windyHeight = h / 8.0f;
                proper = getProperSize(this.mPaintTextWhite, windyString, (3.0f * w) / 4.0f, windyHeight);
                this.mPaintTextWhite.setTextSize(proper);
                fm = this.mPaintTextWhite.getFontMetrics();
                y = cityHight + dateHight + conditionHeight + highLowHeight + huminityHeight + windyHeight / 2.0f - (fm.ascent + fm.descent) / 2.0f;
                if (this.mbShadow) {
                    this.mPaintTextBlack.setTextSize(proper);
                    canvas.drawText(windyString, 10.0f, y, this.mPaintTextBlack);
                }
                canvas.drawText(windyString, 10.0f - 1.0f, y - 1.0f, this.mPaintTextWhite);
                float buttonStrWidth = (float) this.mToForcastBmp.getHeight();
                this.mPaintTextWhite.setTextSize(getProperSize(this.mPaintTextWhite, this.mButtonStrNext, (float) (this.mToForcastBmp.getWidth() - 17), buttonStrWidth));
                fm = this.mPaintTextWhite.getFontMetrics();
                x = w - ((float) this.mToForcastBmp.getWidth());
                y = buttonStrWidth / 2.0f - (fm.ascent + fm.descent) / 2.0f;
                canvas.drawBitmap(this.mToForcastBmp, x, 0.0f, null);
                canvas.drawText(this.mButtonStrNext, 17.0f + x, y, this.mPaintTextWhite);
                int layHInterval = (int) ((((float) getHeight()) * (1.0f - this.mDrawScaleY)) / 2.0f);
                int layWInterval = (int) ((((float) getWidth()) * (1.0f - this.mDrawScaleX)) / 2.0f);
                if (this.mWeatherItem.mForecastConditions[4 - this.mCurValidDay].mIconPath != null) {
                    this.mTodayForcastBmp = bitmapFactory(this.mWeatherItem.mForecastConditions[4 - this.mCurValidDay].mIconPath);
                } else {
                    this.mTodayForcastBmp = createWidgetBitmap(this.mWeatherItem.mForecastConditions[4 - this.mCurValidDay].mBitmap);
                }
                y = cityHight + tempHight;
                if (h - y - ((float) layHInterval) < w / 5.0f - ((float) layWInterval)) {
                    scaleH = h - y - ((float) layHInterval);
                } else {
                    scaleH = w / 5.0f - ((float) layWInterval);
                }
                x = (4.0f * w) / 5.0f - scaleH / 2.0f;
                if (this.mTodayForcastBmp != null) {
                    drawImage(canvas, this.mTodayForcastBmp, (int) x, (int) y, this.mTodayForcastBmp.getWidth(), this.mTodayForcastBmp.getHeight(), 0, 0, scaleH / ((float) this.mTodayForcastBmp.getWidth()), scaleH / ((float) this.mTodayForcastBmp.getHeight()));
                }
                if (!this.mIsRefresh) {
                    canvas.drawBitmap(this.mRefreshWeatherBmp, w - ((float) this.mRefreshWeatherBmp.getWidth()), h - ((float) this.mRefreshWeatherBmp.getHeight()), this.mPaint);
                }
                canvas.setBitmap(s_nullBitmap);
                return target;
            }
        }
    }

    private void loadFailedBitmap(Canvas canvas, boolean isForcast) {
        if (this.mCurFailedString == null) {
            Log.e(TAG, "No weather failure information to show");
        } else {
            float w = ((float) getWidth()) * this.mDrawScaleX;
            float h = ((float) getHeight()) * this.mDrawScaleY;
            float proper;
            FontMetrics fm;
            float x;
            float y;
            Bitmap bitmap;
            if (this.mCurFailedString.equals(this.mNoCityString)) {
                this.mIsNoCity = true;
                float failedHight = h / 5.0f;
                proper = getProperSize(this.mPaintTextWhite, this.mCurFailedString, w, failedHight);
                this.mPaintTextWhite.setTextSize(proper);
                this.mPaintTextBlack.setTextAlign(Align.CENTER);
                this.mPaintTextWhite.setTextAlign(Align.CENTER);
                fm = this.mPaintTextWhite.getFontMetrics();
                x = w / 2.0f;
                y = (3.0f * h) / 7.0f + failedHight / 2.0f - (fm.ascent + fm.descent) / 2.0f;
                if (this.mbShadow) {
                    this.mPaintTextBlack.setTextSize(proper);
                    canvas.drawText(this.mCurFailedString, x, y, this.mPaintTextBlack);
                }
                canvas.drawText(this.mCurFailedString, x - 1.0f, y - 1.0f, this.mPaintTextWhite);
                this.mPaintTextBlack.setTextAlign(Align.LEFT);
                this.mPaintTextWhite.setTextAlign(Align.LEFT);
                if (this.mAppIconBmp != null) {
                    bitmap = this.mAppIconBmp;
                    canvas.drawBitmap(bitmap, w - ((float) this.mAppIconBmp.getWidth()), h - ((float) this.mAppIconBmp.getHeight()), this.mPaint);
                }
            } else {
                if (this.mNewCity == null) {
                    this.mNewCity = "N/A";
                }
                String cityString = this.mNewCity;
                float cityHight = (6.0f * h) / 23.0f;
                proper = getProperSize(this.mPaintTextWhite, cityString, (3.0f * w) / 5.0f - 20.0f, cityHight);
                this.mPaintTextWhite.setTextSize(proper);
                fm = this.mPaintTextWhite.getFontMetrics();
                y = cityHight / 2.0f - (fm.ascent + fm.descent) / 2.0f;
                if (this.mbShadow) {
                    this.mPaintTextBlack.setTextSize(proper);
                    canvas.drawText(cityString, 10.0f, y, this.mPaintTextBlack);
                }
                canvas.drawText(cityString, 10.0f - 1.0f, y - 1.0f, this.mPaintTextWhite);
                String dateString = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                float dateHight = (5.0f * h) / 23.0f;
                proper = getProperSize(this.mPaintTextWhite, dateString, (3.0f * w) / 5.0f - 20.0f, dateHight - (0.6f * h) / 23.0f);
                this.mPaintTextWhite.setTextSize(proper);
                fm = this.mPaintTextWhite.getFontMetrics();
                y = dateHight / 2.0f + cityHight - (fm.ascent + fm.descent) / 2.0f;
                if (this.mbShadow) {
                    this.mPaintTextBlack.setTextSize(proper);
                    canvas.drawText(dateString, 10.0f, y, this.mPaintTextBlack);
                }
                canvas.drawText(dateString, 10.0f - 1.0f, y - 1.0f, this.mPaintTextWhite);
                String tempString = "N/A";
                Paint paint = this.mPaintTextWhite;
                proper = getProperSize(paint, tempString, (2.0f * w) / 5.0f - 10.0f, (1.0f * h) / 4.0f);
                this.mPaintTextWhite.setTextSize(proper);
                fm = this.mPaintTextWhite.getFontMetrics();
                x = (3.0f * w) / 5.0f + 20.0f;
                y = cityHight - (fm.ascent + fm.descent) / 2.0f;
                if (this.mbShadow) {
                    this.mPaintTextBlack.setTextSize(proper);
                    canvas.drawText(tempString, x, y, this.mPaintTextBlack);
                }
                canvas.drawText(tempString, x - 1.0f, y - 1.0f, this.mPaintTextWhite);
                String FailedString = this.mCurFailedString;
                float failedHeight = (7.0f * h) / 23.0f;
                proper = getProperSize(this.mPaintTextWhite, FailedString, (17.0f * w) / 20.0f, failedHeight - (1.5f * h) / 23.0f);
                this.mPaintTextWhite.setTextSize(proper);
                fm = this.mPaintTextWhite.getFontMetrics();
                y = cityHight + dateHight + failedHeight / 2.0f - (fm.ascent + fm.descent) / 2.0f;
                if (this.mbShadow) {
                    this.mPaintTextBlack.setTextSize(proper);
                    canvas.drawText(FailedString, 10.0f, y, this.mPaintTextBlack);
                }
                canvas.drawText(FailedString, 10.0f - 1.0f, y - 1.0f, this.mPaintTextWhite);
                String huminityString = this.mHumidityString + " " + "N/A";
                float huminityHeight = (5.0f * h) / 23.0f;
                proper = getProperSize(this.mPaintTextWhite, huminityString, (4.0f * w) / 5.0f - 20.0f, huminityHeight - (0.6f * h) / 23.0f);
                this.mPaintTextWhite.setTextSize(proper);
                fm = this.mPaintTextWhite.getFontMetrics();
                y = cityHight + dateHight + failedHeight + huminityHeight / 2.0f - (fm.ascent + fm.descent) / 2.0f;
                if (this.mbShadow) {
                    this.mPaintTextBlack.setTextSize(proper);
                    canvas.drawText(huminityString, 10.0f, y, this.mPaintTextBlack);
                }
                canvas.drawText(huminityString, 10.0f - 1.0f, y - 1.0f, this.mPaintTextWhite);
                if (!this.mIsRefresh) {
                    bitmap = this.mRefreshWeatherBmp;
                    canvas.drawBitmap(bitmap, w - ((float) this.mRefreshWeatherBmp.getWidth()), h - ((float) this.mRefreshWeatherBmp.getHeight()), this.mPaint);
                }
                String str = "";
                if (isForcast) {
                    str = this.mButtonStrBack;
                } else {
                    str = this.mButtonStrNext;
                }
                float buttonStrWidth = (float) this.mToForcastBmp.getHeight();
                this.mPaintTextWhite.setTextSize(getProperSize(this.mPaintTextWhite, str, (float) (this.mToForcastBmp.getWidth() - 17), buttonStrWidth));
                fm = this.mPaintTextWhite.getFontMetrics();
                x = w - ((float) this.mToForcastBmp.getWidth());
                y = buttonStrWidth / 2.0f - (fm.ascent + fm.descent) / 2.0f;
                canvas.drawBitmap(this.mToForcastBmp, x, 0.0f, null);
                canvas.drawText(str, 17.0f + x, y, this.mPaintTextWhite);
            }
        }
    }

    private Bitmap loadForcastWeatherBitmap() {
        float w = ((float) getWidth()) * this.mDrawScaleX;
        float h = ((float) getHeight()) * this.mDrawScaleY;
        if (w == 0.0f || h == 0.0f) {
            return null;
        }
        Bitmap target = Bitmap.createBitmap((int) w, (int) h, Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        String unit = this.mBDegreeUnit ? this.mRealUnit : "F";
        if (this.mWeatherItem == null) {
            Log.d(TAG, "No current weather item.");
            loadFailedBitmap(canvas, true);
            canvas.setBitmap(s_nullBitmap);
            return target;
        } else {
            Bitmap bitmap;
            String firstDateString = "";
            if (this.mCurValidDay > 1) {
                firstDateString = getForcastDate(this.mWeatherItem.mForecastConditions[4 - this.mCurValidDay + 1].mDate);
            }
            float firstDateHight = (1.0f * h) / 9.0f;
            float dayWidth = w;
            if (this.mToForcastBmp != null) {
                dayWidth = w - ((float) this.mToForcastBmp.getWidth());
            }
            float proper = getProperSize(this.mPaintTextWhite, firstDateString, dayWidth, firstDateHight);
            this.mPaintTextWhite.setTextSize(proper);
            FontMetrics fm = this.mPaintTextWhite.getFontMetrics();
            float y = firstDateHight / 2.0f - (fm.ascent + fm.descent) / 2.0f;
            if (this.mbShadow) {
                this.mPaintTextBlack.setTextSize(proper);
                canvas.drawText(firstDateString, 10.0f, y, this.mPaintTextBlack);
            }
            canvas.drawText(firstDateString, 10.0f - 1.0f, y - 1.0f, this.mPaintTextWhite);
            String firstWeatherString = "";
            String secondWeatherString = "";
            String thirdWeatherString = "";
            if (this.mCurValidDay > 1) {
                firstWeatherString = contructDayTempStr(4 - this.mCurValidDay + 1, unit) + "  " + this.mWeatherItem.mForecastConditions[4 - this.mCurValidDay + 1].mCondition;
            }
            if (this.mCurValidDay > 2) {
                secondWeatherString = contructDayTempStr(4 - this.mCurValidDay + 2, unit) + "  " + this.mWeatherItem.mForecastConditions[4 - this.mCurValidDay + 2].mCondition;
            }
            if (this.mCurValidDay > 3) {
                thirdWeatherString = contructDayTempStr(4 - this.mCurValidDay + 3, unit) + "  " + this.mWeatherItem.mForecastConditions[4 - this.mCurValidDay + 3].mCondition;
            }
            String largeStr = getWidthestStr(this.mPaintTextWhite, firstWeatherString, secondWeatherString, thirdWeatherString);
            float firstWeatherHight = (2.0f * h) / 9.0f;
            proper = getProperSize(this.mPaintTextWhite, largeStr, w - 10.0f - (2.0f * h) / 9.0f, (3.0f * firstWeatherHight) / 4.0f);
            this.mPaintTextWhite.setTextSize(proper);
            float x = 10.0f;
            y = firstDateHight;
            fm = this.mPaintTextWhite.getFontMetrics();
            if (this.mCurValidDay > 1) {
                if (this.mWeatherItem.mForecastConditions[4 - this.mCurValidDay + 1].mIconPath != null) {
                    this.mFirstForcastBmp = bitmapFactory(this.mWeatherItem.mForecastConditions[4 - this.mCurValidDay + 1].mIconPath);
                } else {
                    this.mFirstForcastBmp = createWidgetBitmap(this.mWeatherItem.mForecastConditions[4 - this.mCurValidDay + 1].mBitmap);
                }
            }
            if (this.mFirstForcastBmp != null) {
                bitmap = Bitmap.createScaledBitmap(this.mFirstForcastBmp, (int) ((2.0f * h) / 9.0f), (int) ((2.0f * h) / 9.0f), true);
                if (!(bitmap == null || bitmap == this.mFirstForcastBmp)) {
                    this.mFirstForcastBmp.recycle();
                    this.mFirstForcastBmp = bitmap;
                }
                if (this.mCurValidDay >= 2) {
                    canvas.drawBitmap(this.mFirstForcastBmp, 10.0f, y, this.mPaint);
                }
                x = 10.0f + ((float) this.mFirstForcastBmp.getWidth());
            }
            y = firstWeatherHight / 2.0f + y - (fm.ascent + fm.descent) / 2.0f;
            if (this.mbShadow) {
                this.mPaintTextBlack.setTextSize(proper);
                canvas.drawText(firstWeatherString, x, y, this.mPaintTextBlack);
            }
            canvas.drawText(firstWeatherString, x - 1.0f, y - 1.0f, this.mPaintTextWhite);
            String secondDateString = "";
            if (this.mCurValidDay > 2) {
                secondDateString = getForcastDate(this.mWeatherItem.mForecastConditions[4 - this.mCurValidDay + 2].mDate);
            }
            float secondDateHight = (1.0f * h) / 9.0f;
            proper = getProperSize(this.mPaintTextWhite, secondDateString, dayWidth, secondDateHight);
            this.mPaintTextWhite.setTextSize(proper);
            fm = this.mPaintTextWhite.getFontMetrics();
            y = firstDateHight + firstWeatherHight + secondDateHight / 2.0f - (fm.ascent + fm.descent) / 2.0f;
            if (this.mbShadow) {
                this.mPaintTextBlack.setTextSize(proper);
                canvas.drawText(secondDateString, 10.0f, y, this.mPaintTextBlack);
            }
            canvas.drawText(secondDateString, 10.0f - 1.0f, y - 1.0f, this.mPaintTextWhite);
            float secondWeatherHight = (2.0f * h) / 9.0f;
            proper = getProperSize(this.mPaintTextWhite, largeStr, w - 10.0f - (2.0f * h) / 9.0f, (3.0f * secondWeatherHight) / 4.0f);
            this.mPaintTextWhite.setTextSize(proper);
            x = 10.0f;
            y = firstDateHight + firstWeatherHight + secondDateHight;
            fm = this.mPaintTextWhite.getFontMetrics();
            if (this.mCurValidDay > 2) {
                if (this.mWeatherItem.mForecastConditions[4 - this.mCurValidDay + 2].mIconPath != null) {
                    this.mSecondForcastBmp = bitmapFactory(this.mWeatherItem.mForecastConditions[4 - this.mCurValidDay + 2].mIconPath);
                } else {
                    this.mSecondForcastBmp = createWidgetBitmap(this.mWeatherItem.mForecastConditions[4 - this.mCurValidDay + 2].mBitmap);
                }
            }
            if (this.mSecondForcastBmp != null) {
                bitmap = Bitmap.createScaledBitmap(this.mSecondForcastBmp, (int) ((2.0f * h) / 9.0f), (int) ((2.0f * h) / 9.0f), true);
                if (!(bitmap == null || bitmap == this.mSecondForcastBmp)) {
                    this.mSecondForcastBmp.recycle();
                    this.mSecondForcastBmp = bitmap;
                }
                if (this.mCurValidDay >= 3) {
                    canvas.drawBitmap(this.mSecondForcastBmp, 10.0f, y, this.mPaint);
                }
                x = 10.0f + ((float) this.mSecondForcastBmp.getWidth());
            }
            y = secondWeatherHight / 2.0f + y - (fm.ascent + fm.descent) / 2.0f;
            if (this.mbShadow) {
                this.mPaintTextBlack.setTextSize(proper);
                canvas.drawText(secondWeatherString, x, y, this.mPaintTextBlack);
            }
            canvas.drawText(secondWeatherString, x - 1.0f, y - 1.0f, this.mPaintTextWhite);
            String thirdDateString = "";
            if (this.mCurValidDay > 3) {
                thirdDateString = getForcastDate(this.mWeatherItem.mForecastConditions[4 - this.mCurValidDay + 3].mDate);
            }
            float thirdDateHight = (1.0f * h) / 9.0f;
            proper = getProperSize(this.mPaintTextWhite, thirdDateString, dayWidth, thirdDateHight);
            this.mPaintTextWhite.setTextSize(proper);
            fm = this.mPaintTextWhite.getFontMetrics();
            y = firstDateHight + firstWeatherHight + secondDateHight + secondWeatherHight + thirdDateHight / 2.0f - (fm.ascent + fm.descent) / 2.0f;
            if (this.mbShadow) {
                this.mPaintTextBlack.setTextSize(proper);
                canvas.drawText(thirdDateString, 10.0f, y, this.mPaintTextBlack);
            }
            canvas.drawText(thirdDateString, 10.0f - 1.0f, y - 1.0f, this.mPaintTextWhite);
            float thirdWeatherHight = (2.0f * h) / 9.0f;
            proper = getProperSize(this.mPaintTextWhite, largeStr, w - 10.0f - (2.0f * h) / 9.0f, (3.0f * thirdWeatherHight) / 4.0f);
            this.mPaintTextWhite.setTextSize(proper);
            x = 10.0f;
            y = firstDateHight + firstWeatherHight + secondDateHight + secondWeatherHight + thirdDateHight;
            fm = this.mPaintTextWhite.getFontMetrics();
            if (this.mCurValidDay > 3) {
                if (this.mWeatherItem.mForecastConditions[4 - this.mCurValidDay + 3].mIconPath != null) {
                    this.mThirdForcastBmp = bitmapFactory(this.mWeatherItem.mForecastConditions[4 - this.mCurValidDay + 3].mIconPath);
                } else {
                    this.mThirdForcastBmp = createWidgetBitmap(this.mWeatherItem.mForecastConditions[4 - this.mCurValidDay + 3].mBitmap);
                }
            }
            if (this.mThirdForcastBmp != null) {
                bitmap = Bitmap.createScaledBitmap(this.mThirdForcastBmp, (int) ((2.0f * h) / 9.0f), (int) ((2.0f * h) / 9.0f), true);
                if (!(bitmap == null || bitmap == this.mThirdForcastBmp)) {
                    this.mThirdForcastBmp.recycle();
                    this.mThirdForcastBmp = bitmap;
                }
                if (this.mCurValidDay >= 4) {
                    canvas.drawBitmap(this.mThirdForcastBmp, 10.0f, y, this.mPaint);
                }
                x = 10.0f + ((float) this.mThirdForcastBmp.getWidth());
            }
            y = thirdWeatherHight / 2.0f + y - (fm.ascent + fm.descent) / 2.0f;
            if (this.mbShadow) {
                this.mPaintTextBlack.setTextSize(proper);
                canvas.drawText(thirdWeatherString, x, y, this.mPaintTextBlack);
            }
            canvas.drawText(thirdWeatherString, x - 1.0f, y - 1.0f, this.mPaintTextWhite);
            float buttonStrWidth = (float) this.mToForcastBmp.getHeight();
            this.mPaintTextWhite.setTextSize(getProperSize(this.mPaintTextWhite, this.mButtonStrBack, (float) (this.mToForcastBmp.getWidth() - 17), buttonStrWidth));
            fm = this.mPaintTextWhite.getFontMetrics();
            x = w - ((float) this.mToForcastBmp.getWidth());
            y = buttonStrWidth / 2.0f - (fm.ascent + fm.descent) / 2.0f;
            canvas.drawBitmap(this.mToForcastBmp, x, 0.0f, null);
            canvas.drawText(this.mButtonStrBack, 17.0f + x, y, this.mPaintTextWhite);
            if (!this.mIsRefresh) {
                canvas.drawBitmap(this.mRefreshWeatherBmp, w - ((float) this.mRefreshWeatherBmp.getWidth()), h - ((float) this.mRefreshWeatherBmp.getHeight()), this.mPaint);
            }
            canvas.setBitmap(s_nullBitmap);
            return target;
        }
    }

    private void refreshLocale() {
        Locale locale = getContext().getResources().getConfiguration().locale;
        if (locale.toString() == null) {
            this.mChinese = 0;
        } else if (locale.toString().equals("zh_CN")) {
            this.mChinese = 1;
        } else if (locale.toString().equals("zh_TW")) {
            this.mChinese = 2;
        } else {
            this.mChinese = 0;
        }
    }

    private String removeProvince(String city) {
        if (city != null) {
            return city.indexOf(ConnectionInstance.ALL_CONNECT_SUCCEED) != -1 ? city.substring(0, city.indexOf(ConnectionInstance.ALL_CONNECT_SUCCEED)) : city;
        } else {
            return "";
        }
    }

    private void setAnimation(float start, float end) {
        this.mFromDegrees = start;
        this.mDegrees = start;
        this.mTargetDegrees = end;
        startWeatherAnimation();
    }

    public int getTempValue(boolean beCentGrade, int value) {
        return !beCentGrade ? ((value - 32) * 5) / 9 : (value * 9) / 5 + 32;
    }

    public void loadWeatherBitmap(WeatherItem weatherItem) {
        if (this.mAppIconBmp == null || this.mToForcastBmp == null || this.mRefreshWeatherBmp == null) {
            Log.d(TAG, "widget bitmap is null");
        } else {
            this.mNewCity = this.mWeatherUtils.mNewCity;
            this.mBDegreeUnit = this.mWeatherUtils.mBDegreeUnit;
            this.mCurFailedString = this.mWeatherUtils.mCurFailedString;
            this.mCurValidDay = this.mWeatherUtils.mCurValidDay;
            this.mIsRefresh = this.mWeatherUtils.mIsRefresh;
            this.mIsNoCity = this.mWeatherUtils.mIsNoCity;
            this.mNoCityString = this.mWeatherUtils.mNoCityString;
            refreshLocale();
            this.mWeatherItem = weatherItem;
            this.mFrontWeatherBmp = loadCurrentWeatherBitmap();
            this.mBackWeatherBmp = loadForcastWeatherBitmap();
            invalidate();
        }
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        try {
            this.mHumidityString = CommRes.getResourceString(getContext(), "R.string.humidity");
            this.mWindConditionString = CommRes.getResourceString(getContext(), "R.string.windcond");
            this.mRealUnit = CommRes.getResourceString(getContext(), "R.string.unit");
            this.mButtonStrNext = CommRes.getResourceString(getContext(), "R.string.weather_forcast_btn_text");
            this.mButtonStrBack = CommRes.getResourceString(getContext(), "R.string.weather_back_btn_text");
            if (this.mToForcastBmp == null) {
                this.mToForcastBmp = ((BitmapDrawable) CommRes.getResourceDrawable(getContext(), "R.drawable.weather_rotate")).getBitmap();
            }
            if (this.mRefreshWeatherBmp == null) {
                this.mRefreshWeatherBmp = ((BitmapDrawable) CommRes.getResourceDrawable(getContext(), "R.drawable.weather_refresh")).getBitmap();
            }
            if (this.mAppIconBmp == null) {
                this.mAppIconBmp = ((BitmapDrawable) CommRes.getResourceDrawable(getContext(), "R.drawable.weather_icon")).getBitmap();
            }
        } catch (Exception e) {
            Log.e(TAG, "No com.wmt.weather resources found!!!");
        }
        this.mWeatherUtils.start();
    }

    public void onClick(View v) {
        int x = (int) this.mXpos;
        int y = (int) this.mYpos;
        int w = getWidth();
        int h = getHeight();
        int layHInterval = (int) ((((float) getHeight()) * (1.0f - this.mDrawScaleY)) / 2.0f);
        int layWInterval = (int) ((((float) getWidth()) * (1.0f - this.mDrawScaleX)) / 2.0f);
        int forcasTop = layHInterval;
        int forcasBottom = layHInterval + h / 3;
        int refreshW = 0;
        int refreshH = 0;
        if (!(this.mIsRefresh || this.mIsNoCity || this.mRefreshWeatherBmp == null)) {
            refreshW = this.mRefreshWeatherBmp.getWidth();
            refreshH = this.mRefreshWeatherBmp.getHeight();
        }
        Rect rect = settingRect;
        int i = w - layWInterval;
        int i2 = h - layHInterval;
        Rect rotateRect = new Rect(w / 2, forcasTop, w - layWInterval, forcasBottom);
        Rect refreshRect = new Rect(w - layWInterval - refreshW * 2, h - layHInterval - refreshH * 2, w - layWInterval, h - layHInterval);
        if (rotateRect.contains(x, y)) {
            if (!this.mIsRefresh) {
                if (this.mIsInBack) {
                    setAnimation(180.0f, 0.0f);
                } else {
                    setAnimation(0.0f, 180.0f);
                }
            }
        } else if (refreshRect.contains(x, y)) {
            try {
                Intent service = new Intent();
                service.setAction(ACTION_CHECK);
                service.putExtra(EXTRA_FORCE_UPDATE, true);
                service.putExtra(EXTRA_UPDATE_CITY, this.mNewCity);
                service.setClassName("com.wmt.weather", "com.wmt.weather.Service.WmtWeatherService");
                this.mContext.startService(service);
            } catch (Exception e) {
                Log.e(TAG, "can not start service!");
            }
            this.mIsRefresh = true;
            invalidate();
        } else if (settingRect.contains(x, y)) {
            try {
                Intent launchIntent = new Intent();
                launchIntent.setComponent(new ComponentName("com.wmt.weather", "com.wmt.weather.Setting.WmtWeatherSetting"));
                launchIntent.setAction("android.intent.action.MAIN");
                launchIntent.addCategory("android.intent.category.LAUNCHER");
                launchIntent.setFlags(270532608);
                getContext().startActivity(launchIntent);
            } catch (ActivityNotFoundException e2) {
                Log.e(TAG, "No this activity found!");
            }
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mWeatherUtils.stop();
        stopWeatherAnimation();
        srcBmpRecycle();
        if (!(this.mToForcastBmp == null || this.mToForcastBmp.isRecycled())) {
            this.mToForcastBmp.recycle();
        }
        this.mToForcastBmp = null;
        if (!(this.mAppIconBmp == null || this.mAppIconBmp.isRecycled())) {
            this.mAppIconBmp.recycle();
        }
        this.mAppIconBmp = null;
        if (!(this.mRefreshWeatherBmp == null || this.mRefreshWeatherBmp.isRecycled())) {
            this.mRefreshWeatherBmp.recycle();
        }
        this.mRefreshWeatherBmp = null;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float centerX = ((float) getWidth()) / 2.0f;
        float centerY = ((float) getHeight()) / 2.0f;
        float layWInterval = (((float) getWidth()) * (1.0f - this.mDrawScaleX)) / 2.0f;
        float layHInterval = (((float) getHeight()) * (1.0f - this.mDrawScaleY)) / 2.0f;
        Camera c = new Camera();
        Bitmap drawBitmap = this.mFrontWeatherBmp;
        float depthX = (((float) getWidth()) * (1.0f - this.mDrawScaleX)) / 2.0f;
        float depthY = (((float) (-getHeight())) * (1.0f - this.mDrawScaleY)) / 2.0f;
        if (this.mDegrees > 90.0f) {
            c.rotateY(180.0f - this.mDegrees);
            c.translate(depthX, depthY, 0.0f);
            drawBitmap = this.mBackWeatherBmp;
        } else {
            c.rotateY(-this.mDegrees);
            c.translate(depthX, depthY, 0.0f);
        }
        canvas.save();
        Matrix base = new Matrix();
        Matrix matrix = new Matrix();
        c.getMatrix(matrix);
        matrix.preTranslate(-centerX, -centerY);
        matrix.preConcat(base);
        matrix.postTranslate(centerX, centerY);
        if (drawBitmap != null) {
            canvas.drawBitmap(drawBitmap, matrix, this.mPaint);
        }
        if (this.mIsRefresh) {
            this.mRefreshAngle += 12.0f;
            matrix.reset();
            matrix.setTranslate(((float) getWidth()) - layWInterval - ((float) this.mRefreshWeatherBmp.getWidth()), ((float) getHeight()) - layHInterval - ((float) this.mRefreshWeatherBmp.getHeight()));
            matrix.preRotate(this.mRefreshAngle, (float) (this.mRefreshWeatherBmp.getWidth() / 2), (float) (this.mRefreshWeatherBmp.getHeight() / 2));
            canvas.drawBitmap(this.mRefreshWeatherBmp, matrix, null);
            invalidate();
        }
        canvas.restore();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > h) {
            this.mDrawScaleX = 0.9f;
            this.mDrawScaleY = 0.8f;
        } else {
            this.mDrawScaleX = 1.0f;
            this.mDrawScaleY = 0.7f;
        }
        this.mWeatherUtils.loadNewWeatherBitmap();
    }

    public boolean onTouchEvent(MotionEvent event) {
        this.mXpos = event.getX();
        this.mYpos = event.getY();
        return super.onTouchEvent(event);
    }

    public void srcBmpRecycle() {
        if (!(this.mFirstForcastBmp == null || this.mFirstForcastBmp.isRecycled())) {
            this.mFirstForcastBmp.recycle();
        }
        this.mFirstForcastBmp = null;
        if (!(this.mSecondForcastBmp == null || this.mSecondForcastBmp.isRecycled())) {
            this.mSecondForcastBmp.recycle();
        }
        this.mSecondForcastBmp = null;
        if (!(this.mThirdForcastBmp == null || this.mThirdForcastBmp.isRecycled())) {
            this.mThirdForcastBmp.recycle();
        }
        this.mThirdForcastBmp = null;
        if (!(this.mTodayForcastBmp == null || this.mTodayForcastBmp.isRecycled())) {
            this.mTodayForcastBmp.recycle();
        }
        this.mTodayForcastBmp = null;
        if (!(this.mFrontWeatherBmp == null || this.mFrontWeatherBmp.isRecycled())) {
            this.mFrontWeatherBmp.recycle();
        }
        this.mFrontWeatherBmp = null;
        if (!(this.mBackWeatherBmp == null || this.mBackWeatherBmp.isRecycled())) {
            this.mBackWeatherBmp.recycle();
        }
        this.mBackWeatherBmp = null;
        System.gc();
    }

    public void startWeatherAnimation() {
        this.mHandler.removeMessages(MSG_WEATHER_ANIMATION);
        this.mHandler.sendEmptyMessageDelayed(MSG_WEATHER_ANIMATION, 10);
    }

    public void stopWeatherAnimation() {
        this.mHandler.removeMessages(MSG_WEATHER_ANIMATION);
    }
}