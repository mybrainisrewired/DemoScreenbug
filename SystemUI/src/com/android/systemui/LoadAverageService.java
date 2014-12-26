package com.android.systemui;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import com.android.ex.carousel.CarouselView.DetailAlignment;
import com.android.internal.os.ProcessStats;
import com.android.systemui.statusbar.CommandQueue;

public class LoadAverageService extends Service {
    private View mView;

    private class LoadView extends View {
        private Paint mAddedPaint;
        private float mAscent;
        private int mFH;
        private Handler mHandler;
        private Paint mIrqPaint;
        private Paint mLoadPaint;
        private int mNeededHeight;
        private int mNeededWidth;
        private Paint mRemovedPaint;
        private Paint mShadow2Paint;
        private Paint mShadowPaint;
        private final Stats mStats;
        private Paint mSystemPaint;
        private Paint mUserPaint;

        LoadView(Context c) {
            int textSize;
            super(c);
            this.mHandler = new Handler() {
                public void handleMessage(Message msg) {
                    if (msg.what == 1) {
                        LoadView.this.mStats.update();
                        LoadView.this.updateDisplay();
                        sendMessageDelayed(obtainMessage(1), 2000);
                    }
                }
            };
            setPadding(CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL, CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL, CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL, CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL);
            float density = c.getResources().getDisplayMetrics().density;
            if (density < 1.0f) {
                textSize = 9;
            } else {
                textSize = (int) (10.0f * density);
                if (textSize < 10) {
                    textSize = 10;
                }
            }
            this.mLoadPaint = new Paint();
            this.mLoadPaint.setAntiAlias(true);
            this.mLoadPaint.setTextSize((float) textSize);
            this.mLoadPaint.setARGB(DetailAlignment.VERTICAL_ALIGNMENT_MASK, DetailAlignment.VERTICAL_ALIGNMENT_MASK, DetailAlignment.VERTICAL_ALIGNMENT_MASK, DetailAlignment.VERTICAL_ALIGNMENT_MASK);
            this.mAddedPaint = new Paint();
            this.mAddedPaint.setAntiAlias(true);
            this.mAddedPaint.setTextSize((float) textSize);
            this.mAddedPaint.setARGB(DetailAlignment.VERTICAL_ALIGNMENT_MASK, 128, DetailAlignment.VERTICAL_ALIGNMENT_MASK, 128);
            this.mRemovedPaint = new Paint();
            this.mRemovedPaint.setAntiAlias(true);
            this.mRemovedPaint.setStrikeThruText(true);
            this.mRemovedPaint.setTextSize((float) textSize);
            this.mRemovedPaint.setARGB(DetailAlignment.VERTICAL_ALIGNMENT_MASK, DetailAlignment.VERTICAL_ALIGNMENT_MASK, 128, 128);
            this.mShadowPaint = new Paint();
            this.mShadowPaint.setAntiAlias(true);
            this.mShadowPaint.setTextSize((float) textSize);
            this.mShadowPaint.setARGB(192, 0, 0, 0);
            this.mLoadPaint.setShadowLayer(4.0f, 0.0f, 0.0f, -16777216);
            this.mShadow2Paint = new Paint();
            this.mShadow2Paint.setAntiAlias(true);
            this.mShadow2Paint.setTextSize((float) textSize);
            this.mShadow2Paint.setARGB(192, 0, 0, 0);
            this.mLoadPaint.setShadowLayer(2.0f, 0.0f, 0.0f, -16777216);
            this.mIrqPaint = new Paint();
            this.mIrqPaint.setARGB(128, 0, 0, DetailAlignment.VERTICAL_ALIGNMENT_MASK);
            this.mIrqPaint.setShadowLayer(2.0f, 0.0f, 0.0f, -16777216);
            this.mSystemPaint = new Paint();
            this.mSystemPaint.setARGB(128, DetailAlignment.VERTICAL_ALIGNMENT_MASK, 0, 0);
            this.mSystemPaint.setShadowLayer(2.0f, 0.0f, 0.0f, -16777216);
            this.mUserPaint = new Paint();
            this.mUserPaint.setARGB(128, 0, DetailAlignment.VERTICAL_ALIGNMENT_MASK, 0);
            this.mSystemPaint.setShadowLayer(2.0f, 0.0f, 0.0f, -16777216);
            this.mAscent = this.mLoadPaint.ascent();
            this.mFH = (int) (this.mLoadPaint.descent() - this.mAscent + 0.5f);
            this.mStats = new Stats(this.mLoadPaint);
            this.mStats.init();
            updateDisplay();
        }

        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            this.mHandler.sendEmptyMessage(1);
        }

        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            this.mHandler.removeMessages(1);
        }

        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            int W = this.mNeededWidth;
            int RIGHT = getWidth() - 1;
            Stats stats = this.mStats;
            int userTime = stats.getLastUserTime();
            int systemTime = stats.getLastSystemTime();
            int iowaitTime = stats.getLastIoWaitTime();
            int irqTime = stats.getLastIrqTime();
            int softIrqTime = stats.getLastSoftIrqTime();
            int totalTime = userTime + systemTime + iowaitTime + irqTime + softIrqTime + stats.getLastIdleTime();
            if (totalTime != 0) {
                int userW = (userTime * W) / totalTime;
                int systemW = (systemTime * W) / totalTime;
                int irqW = (((iowaitTime + irqTime) + softIrqTime) * W) / totalTime;
                int x = RIGHT - this.mPaddingRight;
                int top = this.mPaddingTop + 2;
                int bottom = this.mPaddingTop + this.mFH - 2;
                if (irqW > 0) {
                    canvas.drawRect((float) (x - irqW), (float) top, (float) x, (float) bottom, this.mIrqPaint);
                    x -= irqW;
                }
                if (systemW > 0) {
                    canvas.drawRect((float) (x - systemW), (float) top, (float) x, (float) bottom, this.mSystemPaint);
                    x -= systemW;
                }
                if (userW > 0) {
                    canvas.drawRect((float) (x - userW), (float) top, (float) x, (float) bottom, this.mUserPaint);
                    x -= userW;
                }
                int y = this.mPaddingTop - ((int) this.mAscent);
                canvas.drawText(stats.mLoadText, (float) (RIGHT - this.mPaddingRight - stats.mLoadWidth - 1), (float) (y - 1), this.mShadowPaint);
                canvas.drawText(stats.mLoadText, (float) (RIGHT - this.mPaddingRight - stats.mLoadWidth - 1), (float) (y + 1), this.mShadowPaint);
                canvas.drawText(stats.mLoadText, (float) (RIGHT - this.mPaddingRight - stats.mLoadWidth + 1), (float) (y - 1), this.mShadow2Paint);
                canvas.drawText(stats.mLoadText, (float) (RIGHT - this.mPaddingRight - stats.mLoadWidth + 1), (float) (y + 1), this.mShadow2Paint);
                canvas.drawText(stats.mLoadText, (float) (RIGHT - this.mPaddingRight - stats.mLoadWidth), (float) y, this.mLoadPaint);
                int N = stats.countWorkingStats();
                int i = 0;
                while (i < N) {
                    com.android.internal.os.ProcessStats.Stats st = stats.getWorkingStats(i);
                    y += this.mFH;
                    top += this.mFH;
                    bottom += this.mFH;
                    userW = (st.rel_utime * W) / totalTime;
                    systemW = (st.rel_stime * W) / totalTime;
                    x = RIGHT - this.mPaddingRight;
                    if (systemW > 0) {
                        canvas.drawRect((float) (x - systemW), (float) top, (float) x, (float) bottom, this.mSystemPaint);
                        x -= systemW;
                    }
                    if (userW > 0) {
                        canvas.drawRect((float) (x - userW), (float) top, (float) x, (float) bottom, this.mUserPaint);
                        x -= userW;
                    }
                    canvas.drawText(st.name, (float) (RIGHT - this.mPaddingRight - st.nameWidth - 1), (float) (y - 1), this.mShadowPaint);
                    canvas.drawText(st.name, (float) (RIGHT - this.mPaddingRight - st.nameWidth - 1), (float) (y + 1), this.mShadowPaint);
                    canvas.drawText(st.name, (float) (RIGHT - this.mPaddingRight - st.nameWidth + 1), (float) (y - 1), this.mShadow2Paint);
                    canvas.drawText(st.name, (float) (RIGHT - this.mPaddingRight - st.nameWidth + 1), (float) (y + 1), this.mShadow2Paint);
                    Paint paint = this.mLoadPaint;
                    if (st.added) {
                        paint = this.mAddedPaint;
                    }
                    if (st.removed) {
                        paint = this.mRemovedPaint;
                    }
                    Canvas canvas2 = canvas;
                    canvas2.drawText(st.name, (float) (RIGHT - this.mPaddingRight - st.nameWidth), (float) y, paint);
                    i++;
                }
            }
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            setMeasuredDimension(resolveSize(this.mNeededWidth, widthMeasureSpec), resolveSize(this.mNeededHeight, heightMeasureSpec));
        }

        void updateDisplay() {
            Stats stats = this.mStats;
            int NW = stats.countWorkingStats();
            int maxWidth = stats.mLoadWidth;
            int i = 0;
            while (i < NW) {
                com.android.internal.os.ProcessStats.Stats st = stats.getWorkingStats(i);
                if (st.nameWidth > maxWidth) {
                    maxWidth = st.nameWidth;
                }
                i++;
            }
            int neededWidth = this.mPaddingLeft + this.mPaddingRight + maxWidth;
            int neededHeight = this.mPaddingTop + this.mPaddingBottom + this.mFH * (NW + 1);
            if (neededWidth == this.mNeededWidth && neededHeight == this.mNeededHeight) {
                invalidate();
            } else {
                this.mNeededWidth = neededWidth;
                this.mNeededHeight = neededHeight;
                requestLayout();
            }
        }
    }

    private static final class Stats extends ProcessStats {
        String mLoadText;
        int mLoadWidth;
        private final Paint mPaint;

        Stats(Paint paint) {
            super(false);
            this.mPaint = paint;
        }

        public void onLoadChanged(float load1, float load5, float load15) {
            this.mLoadText = load1 + " / " + load5 + " / " + load15;
            this.mLoadWidth = (int) this.mPaint.measureText(this.mLoadText);
        }

        public int onMeasureProcessName(String name) {
            return (int) this.mPaint.measureText(name);
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        this.mView = new LoadView(this);
        LayoutParams params = new LayoutParams(-1, -2, 2015, 24, -3);
        params.gravity = 53;
        params.setTitle("Load Average");
        ((WindowManager) getSystemService("window")).addView(this.mView, params);
    }

    public void onDestroy() {
        super.onDestroy();
        ((WindowManager) getSystemService("window")).removeView(this.mView);
        this.mView = null;
    }
}