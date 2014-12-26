package com.wmt.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Rect;
import android.support.v4.util.TimeUtils;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewConfiguration;
import com.wmt.data.DataManager;
import com.wmt.opengl.grid.ItemAnimation;
import com.wmt.remotectrl.KeyTouchInputEvent;

public class WmtSurfaceView extends SurfaceView implements Callback, OnClickListener, OnLongClickListener, WmtScrollerListener {
    public static final int BACKGROUND_FLOW = 1;
    public static final int BACKGROUND_STILL = 0;
    private static final int SCROLLER_STROKE_WIDTH = 8;
    public static final int SURFACEVIEW_HORIZONTAL = 0;
    public static final int SURFACEVIEW_VERTICAL = 1;
    private static final String TAG = "MySurfaceView";
    private static final int TOLERANCE_CLICK = 10;
    protected static final int UPDATE_FLING = 0;
    private int mAdapterHeight;
    private int mAdapterWidth;
    private int mBackgroundMode;
    private Bitmap mBgBmp;
    private int mBgColor;
    private Bitmap mCacheBitmap;
    private int mCacheHeight;
    private Object mCacheLock;
    private int mCacheWidth;
    private int mCacheX;
    private int mCacheY;
    private int mChildCount;
    private int mCols;
    private Context mContext;
    private Bitmap mHeadBmp;
    private ISurfaceAdapter mISurfaceAdapter;
    private int mInitSelectedItem;
    private int mItemPaddingX;
    private int mItemPaddingY;
    private int mLastMotionX;
    private int mLastMotionY;
    private int mMaxX;
    private int mMaxY;
    private int mMaximumVelocity;
    private int mMinX;
    private int mMinY;
    private int mMinimumVelocity;
    OnItemClickListener mOnItemClickListener;
    OnItemLongClickListener mOnItemLongClickListener;
    OnItemSelectedListener mOnItemSelectedListener;
    private int mPaddingBottom;
    private int mPaddingLeft;
    private int mPaddingRight;
    private int mPaddingTop;
    private int mPageCols;
    private int mPageRows;
    private Object mRenderLock;
    public RenderThread mRenderThread;
    private int mRows;
    private int mScrollX;
    private int mScrollY;
    private WmtScroller mScroller;
    private int mScrollerSize;
    private SurfaceHolder mSurfaceHolder;
    private int mSurfaceViewMode;
    private Bitmap mTailBmp;
    private int mTouchDownX;
    private int mTouchDownY;
    private Object mUpdateLock;
    private VelocityTracker mVelocityTracker;

    class CacheThread extends Thread {
        private Bitmap mBitmapFree;
        private Boolean mCacheThreadRunning;
        private Canvas mCanvas;
        private int mLastx;
        private int mLasty;
        private Paint mPaint;

        public CacheThread() {
            super("SV_Cache");
            this.mCacheThreadRunning = Boolean.valueOf(true);
            this.mBitmapFree = Bitmap.createBitmap(SURFACEVIEW_VERTICAL, SURFACEVIEW_VERTICAL, Config.RGB_565);
            this.mCanvas = new Canvas();
            this.mPaint = new Paint();
            if (WmtSurfaceView.this.mSurfaceViewMode == 0) {
                WmtSurfaceView.this.mCacheWidth = Math.min(WmtSurfaceView.this.getWidth() * 3, WmtSurfaceView.this.mAdapterWidth);
                WmtSurfaceView.this.mCacheHeight = WmtSurfaceView.this.mAdapterHeight;
            } else {
                WmtSurfaceView.this.mCacheWidth = WmtSurfaceView.this.mAdapterWidth;
                WmtSurfaceView.this.mCacheHeight = Math.min(WmtSurfaceView.this.getHeight() * 3, WmtSurfaceView.this.mAdapterHeight);
            }
            WmtSurfaceView.this.mCacheBitmap = Bitmap.createBitmap(WmtSurfaceView.this.mCacheWidth, WmtSurfaceView.this.mCacheHeight, Config.RGB_565);
            this.mCanvas.setBitmap(WmtSurfaceView.this.mCacheBitmap);
        }

        public void UpdateCacheSize() {
            if (WmtSurfaceView.this.mCacheWidth != WmtSurfaceView.this.getWidth() || WmtSurfaceView.this.mCacheHeight != WmtSurfaceView.this.getHeight()) {
                if (WmtSurfaceView.this.mSurfaceViewMode == 0) {
                    WmtSurfaceView.this.mCacheWidth = Math.min(WmtSurfaceView.this.getWidth() * 3, WmtSurfaceView.this.mAdapterWidth);
                    WmtSurfaceView.this.mCacheHeight = WmtSurfaceView.this.mAdapterHeight;
                } else {
                    WmtSurfaceView.this.mCacheWidth = WmtSurfaceView.this.mAdapterWidth;
                    WmtSurfaceView.this.mCacheHeight = Math.min(WmtSurfaceView.this.getHeight() * 3, WmtSurfaceView.this.mAdapterHeight);
                }
                if (WmtSurfaceView.this.mCacheBitmap != null) {
                    this.mCanvas.setBitmap(this.mBitmapFree);
                    WmtSurfaceView.this.mCacheBitmap.recycle();
                    WmtSurfaceView.this.mCacheBitmap = null;
                }
                WmtSurfaceView.this.mCacheBitmap = Bitmap.createBitmap(WmtSurfaceView.this.mCacheWidth, WmtSurfaceView.this.mCacheHeight, Config.RGB_565);
                this.mCanvas.setBitmap(WmtSurfaceView.this.mCacheBitmap);
            }
        }

        void doDrawCache(Canvas canvas) {
            drawBackground(canvas);
            drawItems(canvas);
        }

        void drawBackground(Canvas canvas) {
            canvas.drawColor(WmtSurfaceView.this.mBgColor);
            if (WmtSurfaceView.this.mBgBmp != null) {
                int thisW = WmtSurfaceView.this.mCacheWidth;
                int thisH = WmtSurfaceView.this.mCacheHeight;
                int bgW = WmtSurfaceView.this.mBgBmp.getWidth();
                int bgH = WmtSurfaceView.this.mBgBmp.getHeight();
                int num;
                int i;
                if (WmtSurfaceView.this.mBackgroundMode == 0) {
                    if (WmtSurfaceView.this.mSurfaceViewMode == 0) {
                        num = bgW >= thisW ? ItemAnimation.CUR_Z : thisW / bgW + 1;
                        i = SURFACEVIEW_HORIZONTAL;
                        while (i < num) {
                            canvas.drawBitmap(WmtSurfaceView.this.mBgBmp, (float) (i * bgW), 0.0f, this.mPaint);
                            i++;
                        }
                    } else {
                        num = bgH >= thisH ? ItemAnimation.CUR_Z : thisH / bgH + 1;
                        i = SURFACEVIEW_HORIZONTAL;
                        while (i < num) {
                            canvas.drawBitmap(WmtSurfaceView.this.mBgBmp, 0.0f, (float) (i * bgH), this.mPaint);
                            i++;
                        }
                    }
                } else if (WmtSurfaceView.this.mSurfaceViewMode == 0) {
                    num = bgW >= thisW ? ItemAnimation.CUR_Z : thisW / bgW + 2;
                    start = this.mLastx - (this.mLastx / bgW) * bgW;
                    i = SURFACEVIEW_HORIZONTAL;
                    while (i < num) {
                        canvas.drawBitmap(WmtSurfaceView.this.mBgBmp, (float) ((-start) + i * bgW), 0.0f, this.mPaint);
                        i++;
                    }
                } else {
                    num = bgH >= thisH ? ItemAnimation.CUR_Z : thisH / bgH + 2;
                    start = this.mLasty - (this.mLasty / bgH) * bgH;
                    i = SURFACEVIEW_HORIZONTAL;
                    while (i < num) {
                        canvas.drawBitmap(WmtSurfaceView.this.mBgBmp, 0.0f, (float) ((-start) + i * bgH), this.mPaint);
                        i++;
                    }
                }
            }
        }

        void drawItems(Canvas canvas) {
            int itemw = WmtSurfaceView.this.mISurfaceAdapter.getItemWidth();
            int itemh = WmtSurfaceView.this.mISurfaceAdapter.getItemHeight();
            int cols = WmtSurfaceView.this.mCacheWidth / itemw;
            int rows = WmtSurfaceView.this.mCacheHeight / itemh;
            int offset;
            int start;
            int i;
            int j;
            if (WmtSurfaceView.this.mSurfaceViewMode == 0) {
                if (this.mLastx > 0) {
                    offset = this.mLastx - (this.mLastx / itemw) * itemw;
                    start = this.mLastx / itemw;
                } else {
                    offset = this.mLastx;
                    start = SURFACEVIEW_HORIZONTAL;
                }
                i = start;
                while (i < start + cols + 1) {
                    j = SURFACEVIEW_HORIZONTAL;
                    while (j < rows) {
                        WmtSurfaceView.this.mISurfaceAdapter.drawItem(i * rows + j, canvas, (float) ((-offset) + (i - start) * itemw), (float) (j * itemh), 0.0f, 1.0f);
                        j++;
                    }
                    i++;
                }
            } else {
                if (this.mLasty > 0) {
                    offset = this.mLasty - (this.mLasty / itemh) * itemh;
                    start = this.mLasty / itemh;
                } else {
                    offset = this.mLasty;
                    start = SURFACEVIEW_HORIZONTAL;
                }
                i = start;
                while (i < start + rows + 1) {
                    j = SURFACEVIEW_HORIZONTAL;
                    while (j < cols) {
                        WmtSurfaceView.this.mISurfaceAdapter.drawItem(i * cols + j, canvas, (float) (j * itemw), (float) ((-offset) + (i - start) * itemh), 0.0f, 1.0f);
                        j++;
                    }
                    i++;
                }
            }
        }

        public void run() {
            while (this.mCacheThreadRunning.booleanValue()) {
                WmtSurfaceView.this.computeScroll();
                WmtSurfaceView.this.mCacheX = WmtSurfaceView.this.mScrollX;
                WmtSurfaceView.this.mCacheY = WmtSurfaceView.this.mScrollY;
                this.mLastx = WmtSurfaceView.this.mCacheX;
                this.mLasty = WmtSurfaceView.this.mCacheY;
                doDrawCache(this.mCanvas);
                synchronized (WmtSurfaceView.this.mRenderLock) {
                    WmtSurfaceView.this.mRenderLock.notify();
                }
                synchronized (WmtSurfaceView.this.mCacheLock) {
                    try {
                        WmtSurfaceView.this.mCacheLock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static interface OnItemClickListener {
        void onItemClick(WmtSurfaceView wmtSurfaceView, int i, long j);
    }

    public static interface OnItemLongClickListener {
        boolean onItemLongClick(WmtSurfaceView wmtSurfaceView, int i, long j);
    }

    public static interface OnItemSelectedListener {
        void onItemSelected(WmtSurfaceView wmtSurfaceView, int i, long j);

        void onNothingSelected(WmtSurfaceView wmtSurfaceView);
    }

    class RenderThread extends Thread {
        int alpha;
        int fade;
        int fps;
        int i;
        long lasttime;
        private boolean mForceRefresh;
        private int mLastx;
        private int mLasty;
        Paint mPaint;
        private boolean mRunning;
        Paint mScrollerPaint;
        int num;
        long time;

        public RenderThread() {
            super("SV_Render");
            this.mForceRefresh = false;
            this.mLastx = -1;
            this.mLasty = -1;
            this.i = 0;
            this.lasttime = 0;
            this.fps = 0;
            this.num = 0;
            this.alpha = 170;
            this.fade = 15;
            this.mRunning = true;
            this.mPaint = new Paint();
            this.mScrollerPaint = new Paint();
            this.mScrollerPaint.setColor(-12303292);
            this.mScrollerPaint.setAlpha(this.alpha);
            this.mScrollerPaint.setStrokeCap(Cap.ROUND);
            this.mScrollerPaint.setStrokeWidth(8.0f);
        }

        private void doDraw(Canvas canvas) {
            synchronized (WmtSurfaceView.this.mUpdateLock) {
                this.mLastx = WmtSurfaceView.this.mScrollX;
                this.mLasty = WmtSurfaceView.this.mScrollY;
                drawBackground(canvas);
                drawItems(canvas);
                drawScroller(canvas);
            }
        }

        public void cancel() {
            this.mRunning = false;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        void drawBackground(android.graphics.Canvas r14_canvas) {
            throw new UnsupportedOperationException("Method not decompiled: com.wmt.util.WmtSurfaceView.RenderThread.drawBackground(android.graphics.Canvas):void");
            /*
            r13 = this;
            r12 = 0;
            r9 = com.wmt.util.WmtSurfaceView.this;
            r8 = r9.getWidth();
            r9 = com.wmt.util.WmtSurfaceView.this;
            r7 = r9.getHeight();
            r9 = com.wmt.util.WmtSurfaceView.this;
            r9 = r9.mBgBmp;
            if (r9 != 0) goto L_0x008f;
        L_0x0015:
            r9 = com.wmt.util.WmtSurfaceView.this;
            r9 = r9.mBgColor;
            r14.drawColor(r9);
        L_0x001e:
            r9 = com.wmt.util.WmtSurfaceView.this;
            r9 = r9.mSurfaceViewMode;
            if (r9 != 0) goto L_0x013f;
        L_0x0026:
            r9 = com.wmt.util.WmtSurfaceView.this;
            r9 = r9.mHeadBmp;
            if (r9 == 0) goto L_0x004d;
        L_0x002e:
            r9 = r13.mLastx;
            if (r9 >= 0) goto L_0x004d;
        L_0x0032:
            r9 = com.wmt.util.WmtSurfaceView.this;
            r9 = r9.mHeadBmp;
            r9 = r9.getWidth();
            r9 = -r9;
            r10 = r13.mLastx;
            r5 = r9 - r10;
            r9 = com.wmt.util.WmtSurfaceView.this;
            r9 = r9.mHeadBmp;
            r10 = (float) r5;
            r11 = r13.mPaint;
            r14.drawBitmap(r9, r10, r12, r11);
        L_0x004d:
            r9 = com.wmt.util.WmtSurfaceView.this;
            r9 = r9.mTailBmp;
            if (r9 == 0) goto L_0x008e;
        L_0x0055:
            r9 = r13.mLastx;
            r10 = com.wmt.util.WmtSurfaceView.this;
            r10 = r10.mMaxX;
            r11 = com.wmt.util.WmtSurfaceView.this;
            r11 = r11.getWidth();
            r11 = r11 / 2;
            r10 = r10 - r11;
            if (r9 <= r10) goto L_0x008e;
        L_0x0068:
            r9 = com.wmt.util.WmtSurfaceView.this;
            r9 = r9.getWidth();
            r10 = r13.mLastx;
            r9 = r9 - r10;
            r10 = com.wmt.util.WmtSurfaceView.this;
            r10 = r10.mMaxX;
            r9 = r9 + r10;
            r10 = com.wmt.util.WmtSurfaceView.this;
            r10 = r10.getWidth();
            r10 = r10 / 2;
            r5 = r9 - r10;
            r9 = com.wmt.util.WmtSurfaceView.this;
            r9 = r9.mTailBmp;
            r10 = (float) r5;
            r11 = r13.mPaint;
            r14.drawBitmap(r9, r10, r12, r11);
        L_0x008e:
            return;
        L_0x008f:
            r9 = com.wmt.util.WmtSurfaceView.this;
            r9 = r9.mBgBmp;
            r1 = r9.getWidth();
            r9 = com.wmt.util.WmtSurfaceView.this;
            r9 = r9.mBgBmp;
            r0 = r9.getHeight();
            r9 = com.wmt.util.WmtSurfaceView.this;
            r9 = r9.mBackgroundMode;
            if (r9 != 0) goto L_0x00eb;
        L_0x00ab:
            r9 = com.wmt.util.WmtSurfaceView.this;
            r9 = r9.mSurfaceViewMode;
            if (r9 != 0) goto L_0x00cf;
        L_0x00b3:
            if (r1 < r8) goto L_0x00ca;
        L_0x00b5:
            r3 = 2;
        L_0x00b6:
            r2 = 0;
        L_0x00b7:
            if (r2 >= r3) goto L_0x001e;
        L_0x00b9:
            r9 = com.wmt.util.WmtSurfaceView.this;
            r9 = r9.mBgBmp;
            r10 = r2 * r1;
            r10 = (float) r10;
            r11 = r13.mPaint;
            r14.drawBitmap(r9, r10, r12, r11);
            r2 = r2 + 1;
            goto L_0x00b7;
        L_0x00ca:
            r9 = r8 / r1;
            r3 = r9 + 1;
            goto L_0x00b6;
        L_0x00cf:
            if (r0 < r7) goto L_0x00e6;
        L_0x00d1:
            r3 = 2;
        L_0x00d2:
            r2 = 0;
        L_0x00d3:
            if (r2 >= r3) goto L_0x001e;
        L_0x00d5:
            r9 = com.wmt.util.WmtSurfaceView.this;
            r9 = r9.mBgBmp;
            r10 = r2 * r0;
            r10 = (float) r10;
            r11 = r13.mPaint;
            r14.drawBitmap(r9, r12, r10, r11);
            r2 = r2 + 1;
            goto L_0x00d3;
        L_0x00e6:
            r9 = r7 / r0;
            r3 = r9 + 1;
            goto L_0x00d2;
        L_0x00eb:
            r9 = com.wmt.util.WmtSurfaceView.this;
            r9 = r9.mSurfaceViewMode;
            if (r9 != 0) goto L_0x0119;
        L_0x00f3:
            if (r1 < r8) goto L_0x0114;
        L_0x00f5:
            r3 = 2;
        L_0x00f6:
            r9 = r13.mLastx;
            r10 = r13.mLastx;
            r10 = r10 / r1;
            r10 = r10 * r1;
            r4 = r9 - r10;
            r2 = 0;
        L_0x00ff:
            if (r2 >= r3) goto L_0x001e;
        L_0x0101:
            r9 = com.wmt.util.WmtSurfaceView.this;
            r9 = r9.mBgBmp;
            r10 = -r4;
            r11 = r2 * r1;
            r10 = r10 + r11;
            r10 = (float) r10;
            r11 = r13.mPaint;
            r14.drawBitmap(r9, r10, r12, r11);
            r2 = r2 + 1;
            goto L_0x00ff;
        L_0x0114:
            r9 = r8 / r1;
            r3 = r9 + 2;
            goto L_0x00f6;
        L_0x0119:
            if (r0 < r7) goto L_0x013a;
        L_0x011b:
            r3 = 2;
        L_0x011c:
            r9 = r13.mLasty;
            r10 = r13.mLasty;
            r10 = r10 / r0;
            r10 = r10 * r0;
            r4 = r9 - r10;
            r2 = 0;
        L_0x0125:
            if (r2 >= r3) goto L_0x001e;
        L_0x0127:
            r9 = com.wmt.util.WmtSurfaceView.this;
            r9 = r9.mBgBmp;
            r10 = -r4;
            r11 = r2 * r0;
            r10 = r10 + r11;
            r10 = (float) r10;
            r11 = r13.mPaint;
            r14.drawBitmap(r9, r12, r10, r11);
            r2 = r2 + 1;
            goto L_0x0125;
        L_0x013a:
            r9 = r7 / r0;
            r3 = r9 + 2;
            goto L_0x011c;
        L_0x013f:
            r9 = com.wmt.util.WmtSurfaceView.this;
            r9 = r9.mHeadBmp;
            if (r9 == 0) goto L_0x0166;
        L_0x0147:
            r9 = r13.mLasty;
            if (r9 >= 0) goto L_0x0166;
        L_0x014b:
            r9 = com.wmt.util.WmtSurfaceView.this;
            r9 = r9.mHeadBmp;
            r9 = r9.getHeight();
            r9 = -r9;
            r10 = r13.mLasty;
            r6 = r9 - r10;
            r9 = com.wmt.util.WmtSurfaceView.this;
            r9 = r9.mHeadBmp;
            r10 = (float) r6;
            r11 = r13.mPaint;
            r14.drawBitmap(r9, r12, r10, r11);
        L_0x0166:
            r9 = com.wmt.util.WmtSurfaceView.this;
            r9 = r9.mTailBmp;
            if (r9 == 0) goto L_0x008e;
        L_0x016e:
            r9 = r13.mLasty;
            r10 = com.wmt.util.WmtSurfaceView.this;
            r10 = r10.mMaxY;
            r11 = com.wmt.util.WmtSurfaceView.this;
            r11 = r11.getHeight();
            r11 = r11 / 2;
            r10 = r10 - r11;
            if (r9 <= r10) goto L_0x008e;
        L_0x0181:
            r9 = com.wmt.util.WmtSurfaceView.this;
            r9 = r9.getHeight();
            r10 = r13.mLasty;
            r9 = r9 - r10;
            r10 = com.wmt.util.WmtSurfaceView.this;
            r10 = r10.mMaxY;
            r9 = r9 + r10;
            r10 = com.wmt.util.WmtSurfaceView.this;
            r10 = r10.getHeight();
            r10 = r10 / 2;
            r6 = r9 - r10;
            r9 = com.wmt.util.WmtSurfaceView.this;
            r9 = r9.mTailBmp;
            r10 = (float) r6;
            r11 = r13.mPaint;
            r14.drawBitmap(r9, r12, r10, r11);
            goto L_0x008e;
            */
        }

        void drawFPS(Canvas canvas) {
            this.time = System.currentTimeMillis();
            if (this.lasttime == 0) {
                this.lasttime = this.time;
            }
            if (this.time - this.lasttime > 1000) {
                this.fps = this.num;
                this.num = 0;
                this.lasttime = this.time;
            }
            this.num++;
            this.mPaint.setColor(-65536);
            canvas.drawText("FPS:" + this.fps, 10.0f, 10.0f, this.mPaint);
            this.i++;
        }

        void drawItems(Canvas canvas) {
            if (WmtSurfaceView.this.mISurfaceAdapter != null && WmtSurfaceView.this.mISurfaceAdapter.getItemCount() > 0) {
                int itemw = WmtSurfaceView.this.mISurfaceAdapter.getItemWidth();
                int itemh = WmtSurfaceView.this.mISurfaceAdapter.getItemHeight();
                int offset;
                int start;
                int i;
                int j;
                if (WmtSurfaceView.this.mSurfaceViewMode == 0) {
                    if (this.mLastx > 0) {
                        offset = this.mLastx - (this.mLastx / itemw) * itemw;
                        start = this.mLastx / itemw;
                    } else {
                        offset = this.mLastx;
                        start = SURFACEVIEW_HORIZONTAL;
                    }
                    i = start;
                    while (i < WmtSurfaceView.this.mPageCols + start + 1) {
                        j = SURFACEVIEW_HORIZONTAL;
                        while (j < WmtSurfaceView.this.mPageRows) {
                            if (WmtSurfaceView.this.mPageRows * i + j < WmtSurfaceView.this.mISurfaceAdapter.getItemCount()) {
                                WmtSurfaceView.this.mISurfaceAdapter.drawItem(WmtSurfaceView.this.mPageRows * i + j, canvas, (float) ((-offset) + (i - start) * itemw), (float) (j * itemh + ((j * 2) + 1) * WmtSurfaceView.this.mItemPaddingY + WmtSurfaceView.this.mPaddingTop), 0.0f, 1.0f);
                                j++;
                            } else {
                                return;
                            }
                        }
                        i++;
                    }
                } else {
                    if (this.mLasty > 0) {
                        offset = this.mLasty - (this.mLasty / itemh) * itemh;
                        start = this.mLasty / itemh;
                    } else {
                        offset = this.mLasty;
                        start = SURFACEVIEW_HORIZONTAL;
                    }
                    i = start;
                    while (i < WmtSurfaceView.this.mPageRows + start + 1) {
                        j = SURFACEVIEW_HORIZONTAL;
                        while (j < WmtSurfaceView.this.mPageCols) {
                            if (WmtSurfaceView.this.mPageCols * i + j < WmtSurfaceView.this.mISurfaceAdapter.getItemCount()) {
                                WmtSurfaceView.this.mISurfaceAdapter.drawItem(WmtSurfaceView.this.mPageCols * i + j, canvas, (float) (j * itemw + ((j * 2) + 1) * WmtSurfaceView.this.mItemPaddingX + WmtSurfaceView.this.mPaddingLeft), (float) ((-offset) + (i - start) * itemh), 0.0f, 1.0f);
                                j++;
                            } else {
                                return;
                            }
                        }
                        i++;
                    }
                }
            }
        }

        void drawScroller(Canvas canvas) {
            if (WmtSurfaceView.this.mScrollerSize != 0) {
                int offset;
                if (WmtSurfaceView.this.mSurfaceViewMode == 0) {
                    offset = ((WmtSurfaceView.this.getWidth() - WmtSurfaceView.this.mScrollerSize) * this.mLastx) / (WmtSurfaceView.this.mAdapterWidth - WmtSurfaceView.this.getWidth());
                    canvas.drawLine((float) (offset + 4), (float) (WmtSurfaceView.this.getHeight() - 4), (float) (WmtSurfaceView.this.mScrollerSize + offset - 4), (float) (WmtSurfaceView.this.getHeight() - 4), this.mScrollerPaint);
                } else {
                    offset = ((WmtSurfaceView.this.getHeight() - WmtSurfaceView.this.mScrollerSize) * this.mLasty) / (WmtSurfaceView.this.mAdapterHeight - WmtSurfaceView.this.getHeight());
                    canvas.drawLine((float) (WmtSurfaceView.this.getWidth() - 4), (float) (offset + 4), (float) (WmtSurfaceView.this.getWidth() - 4), (float) (WmtSurfaceView.this.mScrollerSize + offset - 4), this.mScrollerPaint);
                }
            }
        }

        public void forceRefresh(boolean refresh) {
            this.mForceRefresh = refresh;
        }

        void resetFade() {
            this.fade = 15;
            this.alpha = 170;
            this.mScrollerPaint.setAlpha(this.alpha);
        }

        public void run() {
            while (this.mRunning) {
                synchronized (WmtSurfaceView.this.mRenderLock) {
                    WmtSurfaceView.this.computeScroll();
                    if (this.mForceRefresh || this.mLastx != WmtSurfaceView.this.mScrollX || this.mLasty != WmtSurfaceView.this.mScrollY) {
                        if (this.mForceRefresh) {
                            this.mForceRefresh = false;
                        }
                        if (this.fade < 15) {
                            resetFade();
                        }
                    } else if (this.fade > 0) {
                        stepFade();
                    } else {
                        try {
                            WmtSurfaceView.this.mRenderLock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                Canvas canvas = null;
                try {
                    canvas = WmtSurfaceView.this.mSurfaceHolder.lockCanvas();
                    if (canvas != null) {
                        doDraw(canvas);
                    }
                    if (canvas != null) {
                        WmtSurfaceView.this.mSurfaceHolder.unlockCanvasAndPost(canvas);
                    }
                } catch (Throwable th) {
                    if (canvas != null) {
                        WmtSurfaceView.this.mSurfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }

        void stepFade() {
            if (this.fade <= 10) {
                this.alpha -= 17;
                this.mScrollerPaint.setAlpha(this.alpha);
            }
            this.fade--;
        }
    }

    public WmtSurfaceView(Context context) {
        super(context);
        this.mBgColor = Color.argb(MotionEventCompat.ACTION_MASK, 24, 24, 24);
        this.mRenderLock = new Object();
        this.mUpdateLock = new Object();
        this.mChildCount = 0;
        this.mSurfaceViewMode = 0;
        this.mBackgroundMode = 1;
        init(context);
    }

    public WmtSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mBgColor = Color.argb(MotionEventCompat.ACTION_MASK, 24, 24, 24);
        this.mRenderLock = new Object();
        this.mUpdateLock = new Object();
        this.mChildCount = 0;
        this.mSurfaceViewMode = 0;
        this.mBackgroundMode = 1;
        init(context);
    }

    public WmtSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mBgColor = Color.argb(MotionEventCompat.ACTION_MASK, 24, 24, 24);
        this.mRenderLock = new Object();
        this.mUpdateLock = new Object();
        this.mChildCount = 0;
        this.mSurfaceViewMode = 0;
        this.mBackgroundMode = 1;
        init(context);
    }

    private int clamp(int n, int my, int child) {
        if (my >= child || n < 0) {
            return SURFACEVIEW_HORIZONTAL;
        }
        return my + n > child ? child - my : n;
    }

    private void computeRowsCols() {
        int i = SURFACEVIEW_VERTICAL;
        int w = getWidth() - this.mPaddingLeft - this.mPaddingRight;
        int h = getHeight() - this.mPaddingBottom - this.mPaddingTop;
        if (w != 0 && h != 0 && this.mISurfaceAdapter != null) {
            int itemw = this.mISurfaceAdapter.getItemWidth();
            int itemh = this.mISurfaceAdapter.getItemHeight();
            if (this.mSurfaceViewMode == 0) {
                this.mPageRows = h / itemh;
                this.mPageCols = ((w + itemw) - 1) / itemw;
                this.mRows = this.mISurfaceAdapter.getItemCount() < this.mPageRows ? this.mISurfaceAdapter.getItemCount() : this.mPageRows;
                this.mCols = this.mISurfaceAdapter.getItemCount() < this.mPageRows ? 1 : ((this.mISurfaceAdapter.getItemCount() + this.mRows) - 1) / this.mRows;
                this.mMinX = (int) (((double) w) * -0.5d);
                this.mMaxX = (int) (this.mCols * itemw > w ? ((double) (this.mCols * itemw)) - ((double) w) * 0.5d : ((double) w) * 0.5d);
                this.mMinY = 0;
                this.mMaxY = 0;
                this.mAdapterWidth = this.mCols * itemw;
                this.mAdapterHeight = h;
                if (this.mAdapterWidth <= w) {
                    this.mScrollerSize = 0;
                } else {
                    this.mScrollerSize = (w * w) / this.mAdapterWidth;
                }
                this.mItemPaddingX = 0;
                this.mItemPaddingY = (h - (this.mPageRows * itemh)) / (this.mPageRows * 2);
            } else {
                this.mPageCols = w / itemw;
                this.mPageRows = ((h + itemh) - 1) / itemh;
                this.mCols = this.mISurfaceAdapter.getItemCount() < this.mPageCols ? this.mISurfaceAdapter.getItemCount() : this.mPageCols;
                if (this.mISurfaceAdapter.getItemCount() >= this.mPageCols) {
                    i = ((this.mISurfaceAdapter.getItemCount() + this.mCols) - 1) / this.mCols;
                }
                this.mRows = i;
                this.mMinX = 0;
                this.mMaxX = 0;
                this.mMinY = (int) (((double) h) * -0.5d);
                this.mMaxY = (int) (this.mRows * itemh > h ? ((double) (this.mRows * itemh)) - ((double) h) * 0.5d : ((double) h) * 0.5d);
                this.mAdapterWidth = w;
                this.mAdapterHeight = this.mRows * itemh;
                if (this.mAdapterHeight <= h) {
                    this.mScrollerSize = 0;
                } else {
                    this.mScrollerSize = (h * h) / this.mAdapterHeight;
                }
                this.mItemPaddingX = (w - (this.mPageCols * itemw)) / (this.mPageCols * 2);
                this.mItemPaddingY = 0;
            }
        }
    }

    private int findClickItem(int lastx, int lasty) {
        int itemw = this.mISurfaceAdapter.getItemWidth();
        int itemh = this.mISurfaceAdapter.getItemHeight();
        int count = this.mISurfaceAdapter.getItemCount();
        Rect rc = new Rect(0, 0, itemw, itemh);
        int offset;
        int start;
        int i;
        int j;
        if (this.mSurfaceViewMode == 0) {
            if (this.mScrollX > 0) {
                offset = this.mScrollX - (this.mScrollX / itemw) * itemw;
                start = this.mScrollX / itemw;
            } else {
                offset = this.mScrollX;
                start = SURFACEVIEW_HORIZONTAL;
            }
            i = start;
            while (i < this.mPageCols + start + 1) {
                j = SURFACEVIEW_HORIZONTAL;
                while (j < this.mPageRows) {
                    rc.offsetTo((-offset) + (i - start) * itemw, j * itemh + ((j * 2) + 1) * this.mItemPaddingY + this.mPaddingTop);
                    if (rc.contains(lastx, lasty)) {
                        return (this.mPageRows * i) + j < count ? this.mPageRows * i + j : -1;
                    } else {
                        j++;
                    }
                }
                i++;
            }
            return -1;
        } else {
            if (this.mScrollY > 0) {
                offset = this.mScrollY - (this.mScrollY / itemh) * itemh;
                start = this.mScrollY / itemh;
            } else {
                offset = this.mScrollY;
                start = SURFACEVIEW_HORIZONTAL;
            }
            i = start;
            while (i < this.mPageRows + start + 1) {
                j = SURFACEVIEW_HORIZONTAL;
                while (j < this.mPageCols) {
                    rc.offsetTo(j * itemw + ((j * 2) + 1) * this.mItemPaddingX + this.mPaddingLeft, (-offset) + (i - start) * itemh);
                    if (rc.contains(lastx, lasty)) {
                        return (this.mPageCols * i) + j < count ? this.mPageCols * i + j : -1;
                    } else {
                        j++;
                    }
                }
                i++;
            }
            return -1;
        }
    }

    private void init(Context context) {
        this.mContext = context;
        this.mSurfaceHolder = getHolder();
        this.mSurfaceHolder.addCallback(this);
        this.mCacheLock = new Object();
        this.mMinX = (int) (-0.5d * ((double) getWidth()));
        this.mMaxX = (int) (2.5d * ((double) getWidth()));
        initScrollView();
        setFocusable(true);
        setClickable(true);
        setLongClickable(true);
        setOnClickListener(this);
        setOnLongClickListener(this);
    }

    private void initScrollView() {
        this.mScroller = new WmtScroller(getContext());
        ViewConfiguration configuration = ViewConfiguration.get(this.mContext);
        configuration.getScaledTouchSlop();
        this.mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        this.mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        this.mScroller.setScrollerListener(this);
    }

    private boolean onNavigation(int keyCode) {
        if (this.mISurfaceAdapter != null) {
            int index = this.mISurfaceAdapter.getSelectedItem();
            int old = index;
            int count = this.mISurfaceAdapter.getItemCount();
            if (this.mSurfaceViewMode == 0) {
                switch (keyCode) {
                    case TimeUtils.HUNDRED_DAY_FIELD_LEN:
                        if (index > 0) {
                            index--;
                        }
                        break;
                    case KeyTouchInputEvent.EV_REP:
                        if (index < count - 1) {
                            index++;
                        }
                        break;
                    case KeyTouchInputEvent.EV_FF:
                        if (index >= this.mRows) {
                            index -= this.mRows;
                        }
                        break;
                    case KeyTouchInputEvent.EV_PWR:
                        if (index < count - this.mRows) {
                            index += this.mRows;
                        }
                        break;
                }
            } else {
                switch (keyCode) {
                    case TimeUtils.HUNDRED_DAY_FIELD_LEN:
                        if (index >= this.mCols) {
                            index -= this.mCols;
                        }
                        break;
                    case KeyTouchInputEvent.EV_REP:
                        if (index < count - this.mCols) {
                            index += this.mCols;
                        }
                        break;
                    case KeyTouchInputEvent.EV_FF:
                        if (index > 0) {
                            index--;
                        }
                        break;
                    case KeyTouchInputEvent.EV_PWR:
                        if (index < count - 1) {
                            index++;
                        }
                        break;
                    default:
                        break;
                }
            }
            if (index != old) {
                synchronized (this.mRenderLock) {
                    setSelectedItem(index);
                    this.mRenderLock.notify();
                }
                return false;
            }
        }
        return true;
    }

    private void touch_down(int x, int y) {
        Log.d(TAG, "synchronized : touch_down");
        synchronized (this.mRenderLock) {
            this.mLastMotionX = x;
            this.mLastMotionY = y;
            this.mTouchDownX = x;
            this.mTouchDownY = y;
            int index = findClickItem(x, y);
            if (index >= 0 && this.mISurfaceAdapter.getSelectedItem() != index) {
                this.mISurfaceAdapter.setSelectedItem(index);
                if (this.mOnItemSelectedListener != null) {
                    this.mOnItemSelectedListener.onItemSelected(this, index, (long) index);
                }
            }
            Log.d(TAG, "mRenderLock.notify()");
            this.mRenderLock.notify();
        }
        Log.d(TAG, "touch_down synchronized!");
    }

    private void touch_move(int x, int y) {
        synchronized (this.mRenderLock) {
            int dx = x - this.mLastMotionX;
            int dy = y - this.mLastMotionY;
            if (this.mSurfaceViewMode == 0) {
                if (this.mScrollX < 0 || this.mScrollX > this.mMaxX - getWidth() / 2) {
                    this.mScrollX -= dx / 2;
                } else {
                    this.mScrollX -= dx;
                }
            } else if (this.mScrollY < 0 || this.mScrollY > this.mMaxY - getHeight() / 2) {
                this.mScrollY -= dy / 2;
            } else {
                this.mScrollY -= dy;
            }
            this.mLastMotionX = x;
            this.mLastMotionY = y;
            this.mRenderLock.notify();
        }
    }

    private void touch_up(int x, int y) {
        Log.d(TAG, "synchronized : touch_up");
        synchronized (this.mRenderLock) {
            bounce();
            this.mLastMotionX = x;
            this.mLastMotionY = y;
            Log.d(TAG, "mRenderLock.notify()");
            this.mRenderLock.notify();
        }
        Log.d(TAG, "touch_up synchronized!");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void bounce() {
        throw new UnsupportedOperationException("Method not decompiled: com.wmt.util.WmtSurfaceView.bounce():void");
        /*
        r6 = this;
        r5 = 0;
        r4 = 0;
        r1 = "MySurfaceView";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "bounce: mScrollX=";
        r2 = r2.append(r3);
        r3 = r6.mScrollX;
        r2 = r2.append(r3);
        r3 = ", mScrollY=";
        r2 = r2.append(r3);
        r3 = r6.mScrollY;
        r2 = r2.append(r3);
        r2 = r2.toString();
        com.wmt.util.Log.d(r1, r2);
        r1 = r6.mSurfaceViewMode;
        if (r1 != 0) goto L_0x005b;
    L_0x002c:
        r1 = r6.mScrollX;
        if (r1 < 0) goto L_0x003d;
    L_0x0030:
        r1 = r6.mScrollX;
        r2 = r6.mMaxX;
        r3 = r6.getWidth();
        r3 = r3 / 2;
        r2 = r2 - r3;
        if (r1 <= r2) goto L_0x004c;
    L_0x003d:
        r1 = r6.mScrollX;
        if (r1 >= 0) goto L_0x004d;
    L_0x0041:
        r1 = r6.mScrollX;
        r1 = -r1;
        r0 = (float) r1;
    L_0x0045:
        r1 = r6.mScroller;
        r2 = r6.mScrollX;
        r1.bounce(r0, r2, r4, r5);
    L_0x004c:
        return;
    L_0x004d:
        r1 = r6.mMaxX;
        r2 = r6.getWidth();
        r2 = r2 / 2;
        r1 = r1 - r2;
        r2 = r6.mScrollX;
        r1 = r1 - r2;
        r0 = (float) r1;
        goto L_0x0045;
    L_0x005b:
        r1 = r6.mScrollY;
        if (r1 < 0) goto L_0x006c;
    L_0x005f:
        r1 = r6.mScrollY;
        r2 = r6.mMaxY;
        r3 = r6.getHeight();
        r3 = r3 / 2;
        r2 = r2 - r3;
        if (r1 <= r2) goto L_0x004c;
    L_0x006c:
        r1 = r6.mScrollY;
        if (r1 >= 0) goto L_0x007c;
    L_0x0070:
        r1 = r6.mScrollY;
        r1 = -r1;
        r0 = (float) r1;
    L_0x0074:
        r1 = r6.mScroller;
        r2 = r6.mScrollY;
        r1.bounce(r4, r5, r0, r2);
        goto L_0x004c;
    L_0x007c:
        r1 = r6.mMaxY;
        r2 = r6.getHeight();
        r2 = r2 / 2;
        r1 = r1 - r2;
        r2 = r6.mScrollY;
        r1 = r1 - r2;
        r0 = (float) r1;
        goto L_0x0074;
        */
    }

    public void computeScroll() {
        if (this.mScroller.computeScrollOffset()) {
            this.mLastMotionX = this.mScrollX;
            this.mLastMotionY = this.mScrollY;
            int x = this.mScroller.getCurrX();
            int y = this.mScroller.getCurrY();
            if (getChildCount() > 0) {
                this.mScrollX = clamp(x, getWidth() - this.mPaddingRight - this.mPaddingLeft, SURFACEVIEW_HORIZONTAL);
                this.mScrollY = clamp(y, getHeight() - this.mPaddingBottom - this.mPaddingTop, SURFACEVIEW_HORIZONTAL);
            } else {
                this.mScrollX = x;
                this.mScrollY = y;
            }
        }
    }

    public void fling(int velocityx, int velocityy) {
        this.mScroller.fling(this.mScrollX, this.mScrollY, velocityx, velocityy, this.mMinX, this.mMaxX, this.mMinY, this.mMaxY);
    }

    public ISurfaceAdapter getAdapter() {
        return this.mISurfaceAdapter;
    }

    public int getBackgroundMode() {
        return this.mBackgroundMode;
    }

    public Bitmap getBgBmp() {
        return this.mBgBmp;
    }

    public int getChildCount() {
        return this.mChildCount;
    }

    public Bitmap getHeadBackground() {
        return this.mHeadBmp;
    }

    public final OnItemClickListener getOnItemClickListener() {
        return this.mOnItemClickListener;
    }

    public final OnItemLongClickListener getOnItemLongClickListener() {
        return this.mOnItemLongClickListener;
    }

    public final OnItemSelectedListener getOnItemSelectedListener() {
        return this.mOnItemSelectedListener;
    }

    public int getSurfaceViewMode() {
        return this.mSurfaceViewMode;
    }

    public Bitmap getTailBackground() {
        return this.mTailBmp;
    }

    public void invalidateAll() {
        synchronized (this.mRenderLock) {
            if (this.mRenderThread != null) {
                this.mRenderThread.forceRefresh(true);
            }
            this.mRenderLock.notify();
        }
    }

    public void invalidateItem(int index) {
        synchronized (this.mRenderLock) {
            if (this.mRenderThread != null) {
                this.mRenderThread.forceRefresh(true);
            }
            this.mRenderLock.notify();
        }
    }

    public void onBounceBegin() {
    }

    public void onBounceEnd() {
    }

    public void onClick(View v) {
        Log.d(TAG, "onClick");
        if (this.mOnItemClickListener != null && Math.abs(this.mLastMotionX - this.mTouchDownX) <= 10 && Math.abs(this.mLastMotionY - this.mTouchDownY) <= 10) {
            int index = findClickItem(this.mLastMotionX, this.mLastMotionY);
            if (index >= 0 && index < this.mISurfaceAdapter.getItemCount()) {
                this.mOnItemClickListener.onItemClick(this, index, (long) index);
            }
        }
    }

    public void onFlingBegin() {
    }

    public void onFlingEnd() {
        Log.d(TAG, "synchronized : onFlingEnd");
        synchronized (this.mRenderLock) {
            this.mScrollX = this.mScroller.getCurrX();
            this.mScrollY = this.mScroller.getCurrY();
            bounce();
            Log.d(TAG, "mRenderLock.notify()");
            this.mRenderLock.notify();
        }
        Log.d(TAG, "onFlingEnd synchronized!");
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case TimeUtils.HUNDRED_DAY_FIELD_LEN:
            case KeyTouchInputEvent.EV_REP:
            case KeyTouchInputEvent.EV_FF:
            case KeyTouchInputEvent.EV_PWR:
                return onNavigation(keyCode);
            case KeyTouchInputEvent.EV_FF_STATUS:
            case DataManager.INCLUDE_LOCAL_VIDEO_ONLY:
                if (!(this.mISurfaceAdapter == null || this.mISurfaceAdapter.getSelectedItem() == -1)) {
                    int index = this.mISurfaceAdapter.getSelectedItem();
                    if (index < this.mISurfaceAdapter.getItemCount() && this.mOnItemClickListener != null) {
                        this.mOnItemClickListener.onItemClick(this, index, (long) index);
                    }
                }
                return super.onKeyDown(keyCode, event);
            default:
                return super.onKeyDown(keyCode, event);
        }
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);
    }

    public boolean onLongClick(View v) {
        Log.d(TAG, "onLongClick");
        if (this.mOnItemLongClickListener == null || Math.abs(this.mLastMotionX - this.mTouchDownX) > 10 || Math.abs(this.mLastMotionY - this.mTouchDownY) > 10) {
            return false;
        }
        int index = findClickItem(this.mLastMotionX, this.mLastMotionY);
        return (index < 0 || index >= this.mISurfaceAdapter.getItemCount()) ? false : this.mOnItemLongClickListener.onItemLongClick(this, index, (long) index);
    }

    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(event);
        switch (event.getAction()) {
            case SURFACEVIEW_HORIZONTAL:
                if (!this.mScroller.isFinished()) {
                    this.mScroller.abortAnimation();
                }
                touch_down(x, y);
                break;
            case SURFACEVIEW_VERTICAL:
                VelocityTracker velocityTracker = this.mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
                int initialVelocityX = (int) velocityTracker.getXVelocity();
                int initialVelocityY = (int) velocityTracker.getYVelocity();
                if (Math.abs(initialVelocityX) > this.mMinimumVelocity || Math.abs(initialVelocityY) > this.mMinimumVelocity) {
                    fling(-initialVelocityX, -initialVelocityY);
                }
                if (this.mVelocityTracker != null) {
                    this.mVelocityTracker.recycle();
                    this.mVelocityTracker = null;
                }
                touch_up(x, y);
                break;
            case ItemAnimation.CUR_Z:
                touch_move(x, y);
                break;
        }
        return super.onTouchEvent(event);
    }

    public boolean performItemClick(int position, long id) {
        if (this.mOnItemClickListener == null) {
            return false;
        }
        playSoundEffect(SURFACEVIEW_HORIZONTAL);
        this.mOnItemClickListener.onItemClick(this, position, id);
        return true;
    }

    public void setAdapter(ISurfaceAdapter adapter) {
        synchronized (this.mUpdateLock) {
            this.mISurfaceAdapter = adapter;
            computeRowsCols();
        }
        synchronized (this.mRenderLock) {
            this.mScrollX = 0;
            this.mScrollY = 0;
            this.mRenderLock.notify();
            if (this.mRenderThread != null) {
                this.mRenderThread.forceRefresh(true);
            }
        }
    }

    public void setBackgroundMode(int mode) {
        this.mBackgroundMode = mode;
    }

    public void setBgBmp(Bitmap bmp) {
        synchronized (this.mUpdateLock) {
            this.mBgBmp = bmp;
        }
    }

    public void setBgColor(int color) {
        this.mBgColor = color;
    }

    public void setHeadBackground(Bitmap bmp) {
        synchronized (this.mUpdateLock) {
            this.mHeadBmp = bmp;
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        if (!isLongClickable()) {
            setLongClickable(true);
        }
        this.mOnItemLongClickListener = listener;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.mOnItemSelectedListener = listener;
    }

    public void setSelectedItem(int index) {
        int w = getWidth() - this.mPaddingLeft - this.mPaddingRight;
        int h = getHeight() - this.mPaddingBottom - this.mPaddingTop;
        if (w == 0 || h == 0 || this.mISurfaceAdapter == null) {
            this.mInitSelectedItem = index;
        } else {
            int itemw = this.mISurfaceAdapter.getItemWidth();
            int itemh = this.mISurfaceAdapter.getItemHeight();
            if (this.mPageRows == 0 && this.mPageCols == 0) {
                computeRowsCols();
            }
            Log.d(TAG, "synchronized : setSelectedItem");
            synchronized (this.mRenderLock) {
                if (index >= 0) {
                    if (index < this.mISurfaceAdapter.getItemCount()) {
                        this.mISurfaceAdapter.setSelectedItem(index);
                        if (this.mOnItemSelectedListener != null) {
                            this.mOnItemSelectedListener.onItemSelected(this, index, (long) index);
                        }
                        if (this.mSurfaceViewMode == 0) {
                            int x = (index / this.mPageRows) * itemw;
                            if (this.mScrollX >= x || this.mScrollX <= x - w - itemw) {
                                if (x > this.mMaxX - w / 2) {
                                    x = this.mMaxX - w / 2;
                                }
                                this.mScrollX = x;
                                this.mScrollY = 0;
                            }
                        } else {
                            int y = (index / this.mPageCols) * itemh;
                            if (this.mScrollY >= y || this.mScrollY <= y - h - itemh) {
                                if (y > this.mMaxY - h / 2) {
                                    y = this.mMaxY - h / 2;
                                }
                                this.mScrollX = 0;
                                this.mScrollY = y;
                            }
                        }
                        Log.d(TAG, "mRenderLock.notify()");
                        this.mRenderLock.notify();
                        if (this.mRenderThread != null) {
                            this.mRenderThread.forceRefresh(true);
                        }
                    }
                }
            }
            Log.d(TAG, "setSelectedItem synchronized!");
        }
    }

    public void setSurfaceViewMode(int mode) {
        this.mSurfaceViewMode = mode;
    }

    public void setTailBackground(Bitmap bmp) {
        synchronized (this.mUpdateLock) {
            this.mTailBmp = bmp;
        }
    }

    public void setViewPadding(int left, int top, int right, int bottom) {
        this.mPaddingLeft = left;
        this.mPaddingTop = top;
        this.mPaddingRight = right;
        this.mPaddingBottom = bottom;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged");
        computeRowsCols();
    }

    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated");
        computeRowsCols();
        if (this.mInitSelectedItem != 0) {
            setSelectedItem(this.mInitSelectedItem);
            this.mInitSelectedItem = 0;
        }
        this.mRenderThread = new RenderThread();
        this.mRenderThread.start();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed");
        this.mRenderThread.cancel();
    }

    public void updateAdapter(boolean reset) {
        synchronized (this.mUpdateLock) {
            computeRowsCols();
        }
        synchronized (this.mRenderLock) {
            if (reset) {
                this.mScrollX = 0;
                this.mScrollY = 0;
            }
            this.mRenderLock.notify();
            if (this.mRenderThread != null) {
                this.mRenderThread.forceRefresh(true);
            }
        }
    }
}