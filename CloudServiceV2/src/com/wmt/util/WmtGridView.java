package com.wmt.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Rect;
import android.support.v4.util.TimeUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewConfiguration;
import com.wmt.data.DataManager;
import com.wmt.remotectrl.KeyTouchInputEvent;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;

public class WmtGridView extends View implements OnClickListener, OnLongClickListener, WmtScrollerListener {
    public static final int BACKGROUND_FLOW = 1;
    public static final int BACKGROUND_STILL = 0;
    private static final int FLING_BORDER = 8;
    private static final int SCROLLER_STROKE_WIDTH = 6;
    public static final int SURFACEVIEW_HORIZONTAL = 0;
    public static final int SURFACEVIEW_VERTICAL = 1;
    private static final String TAG = "WmtGridView";
    private static final int TOLERANCE_CLICK = 10;
    protected static final int UPDATE_FLING = 0;
    int alpha;
    int fade;
    int fps;
    int i;
    long lasttime;
    private int mAdapterHeight;
    private int mAdapterWidth;
    private int mBackgroundMode;
    private Bitmap mBgBmp;
    private int mBgColor;
    private int mChildCount;
    private int mCols;
    private Context mContext;
    private Bitmap mHeadBmp;
    private ISurfaceAdapter mISurfaceAdapter;
    private int mInitSelectedItem;
    private float mItemPaddingX;
    private float mItemPaddingY;
    private int mLastMotionX;
    private int mLastMotionY;
    private int mMaxX;
    private int mMaxY;
    private int mMaximumVelocity;
    private int mMinX;
    private int mMinY;
    private int mMinimumVelocity;
    private boolean mMouseDown;
    OnItemClickListener mOnItemClickListener;
    OnItemLongClickListener mOnItemLongClickListener;
    OnItemSelectedListener mOnItemSelectedListener;
    private int mPaddingBottom;
    private int mPaddingLeft;
    private int mPaddingRight;
    private int mPaddingTop;
    private int mPageCols;
    private int mPageRows;
    Paint mPaint;
    private int mRows;
    private int mScrollX;
    private int mScrollY;
    private WmtScroller mScroller;
    Paint mScrollerPaint;
    private int mScrollerSize;
    private boolean mShowScroller;
    private int mSurfaceViewMode;
    private Bitmap mTailBmp;
    private int mTouchDownX;
    private int mTouchDownY;
    private VelocityTracker mVelocityTracker;
    int num;
    long time;

    public static interface OnItemClickListener {
        void onItemClick(WmtGridView wmtGridView, int i, long j);
    }

    public static interface OnItemLongClickListener {
        boolean onItemLongClick(WmtGridView wmtGridView, int i, long j);
    }

    public static interface OnItemSelectedListener {
        void onItemSelected(WmtGridView wmtGridView, int i, long j);

        void onNothingSelected(WmtSurfaceView wmtSurfaceView);
    }

    public WmtGridView(Context context) {
        super(context);
        this.mBgColor = Color.argb(SURFACEVIEW_HORIZONTAL, SURFACEVIEW_HORIZONTAL, SURFACEVIEW_HORIZONTAL, SURFACEVIEW_HORIZONTAL);
        this.mChildCount = 0;
        this.mMouseDown = false;
        this.mShowScroller = false;
        this.mSurfaceViewMode = 0;
        this.mBackgroundMode = 1;
        this.alpha = 255;
        this.fade = 10;
        this.i = 0;
        this.lasttime = 0;
        this.fps = 0;
        this.num = 0;
        init(context);
    }

    public WmtGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mBgColor = Color.argb(SURFACEVIEW_HORIZONTAL, SURFACEVIEW_HORIZONTAL, SURFACEVIEW_HORIZONTAL, SURFACEVIEW_HORIZONTAL);
        this.mChildCount = 0;
        this.mMouseDown = false;
        this.mShowScroller = false;
        this.mSurfaceViewMode = 0;
        this.mBackgroundMode = 1;
        this.alpha = 255;
        this.fade = 10;
        this.i = 0;
        this.lasttime = 0;
        this.fps = 0;
        this.num = 0;
        init(context);
    }

    public WmtGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mBgColor = Color.argb(SURFACEVIEW_HORIZONTAL, SURFACEVIEW_HORIZONTAL, SURFACEVIEW_HORIZONTAL, SURFACEVIEW_HORIZONTAL);
        this.mChildCount = 0;
        this.mMouseDown = false;
        this.mShowScroller = false;
        this.mSurfaceViewMode = 0;
        this.mBackgroundMode = 1;
        this.alpha = 255;
        this.fade = 10;
        this.i = 0;
        this.lasttime = 0;
        this.fps = 0;
        this.num = 0;
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
            if (itemw == -1) {
                itemw = w;
            }
            if (itemh == -1) {
                itemh = h;
            }
            if (this.mSurfaceViewMode == 0) {
                this.mPageRows = h / itemh;
                if (this.mPageRows == 0) {
                    this.mPageRows = 1;
                }
                this.mPageCols = ((w + itemw) - 1) / itemw;
                this.mRows = this.mISurfaceAdapter.getItemCount() < this.mPageRows ? this.mISurfaceAdapter.getItemCount() : this.mPageRows;
                this.mCols = this.mISurfaceAdapter.getItemCount() < this.mPageRows ? 1 : ((this.mISurfaceAdapter.getItemCount() + this.mRows) - 1) / this.mRows;
                this.mMinX = (-w) / 8;
                this.mMaxX = this.mCols * itemw > w ? this.mCols * itemw - (w * 7) / 8 : w / 8;
                this.mMinY = 0;
                this.mMaxY = 0;
                this.mAdapterWidth = this.mCols * itemw;
                this.mAdapterHeight = h;
                if (this.mAdapterWidth <= w) {
                    this.mScrollerSize = 0;
                } else {
                    this.mScrollerSize = (w * w) / this.mAdapterWidth;
                }
                this.mItemPaddingX = 0.0f;
                this.mItemPaddingY = ((float) (h - (this.mPageRows * itemh))) / (((float) this.mPageRows) * 2.0f);
            } else {
                this.mPageCols = w / itemw;
                if (this.mPageCols == 0) {
                    this.mPageCols = 1;
                }
                this.mPageRows = ((h + itemh) - 1) / itemh;
                this.mCols = this.mISurfaceAdapter.getItemCount() < this.mPageCols ? this.mISurfaceAdapter.getItemCount() : this.mPageCols;
                if (this.mISurfaceAdapter.getItemCount() >= this.mPageCols) {
                    i = ((this.mISurfaceAdapter.getItemCount() + this.mCols) - 1) / this.mCols;
                }
                this.mRows = i;
                this.mMinX = 0;
                this.mMaxX = 0;
                this.mMinY = (-h) / 8;
                this.mMaxY = this.mRows * itemh > h ? this.mRows * itemh - (h * 7) / 8 : h / 8;
                this.mAdapterWidth = w;
                this.mAdapterHeight = this.mRows * itemh;
                if (this.mAdapterHeight <= h) {
                    this.mScrollerSize = 0;
                } else {
                    this.mScrollerSize = (h * h) / this.mAdapterHeight;
                }
                this.mItemPaddingX = ((float) (w - (this.mPageCols * itemw))) / (((float) this.mPageCols) * 2.0f);
                this.mItemPaddingY = 0.0f;
            }
        }
    }

    private void doDraw(Canvas canvas) {
        drawBackground(canvas);
        drawItems(canvas);
        drawMasks(canvas);
        drawScroller(canvas);
    }

    private int findClickItem(int lastx, int lasty) {
        if (this.mISurfaceAdapter == null) {
            return -1;
        }
        int itemw = this.mISurfaceAdapter.getItemWidth();
        int itemh = this.mISurfaceAdapter.getItemHeight();
        int count = this.mISurfaceAdapter.getItemCount();
        if (itemw == -1) {
            itemw = getWidth() - this.mPaddingLeft - this.mPaddingRight;
        }
        if (itemh == -1) {
            itemh = getHeight() - this.mPaddingTop - this.mPaddingBottom;
        }
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
                    rc.offsetTo((-offset) + (i - start) * itemw, (int) (((float) (j * itemh)) + ((float) ((j * 2) + 1)) * this.mItemPaddingY + ((float) this.mPaddingTop)));
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
                    rc.offsetTo((int) (((float) (j * itemw)) + ((float) ((j * 2) + 1)) * this.mItemPaddingX + ((float) this.mPaddingLeft)), (-offset) + (i - start) * itemh);
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
        this.mPaint = new Paint();
        this.mScrollerPaint = new Paint();
        this.mScrollerPaint.setColor(-3355444);
        this.mScrollerPaint.setAlpha(this.alpha);
        this.mScrollerPaint.setStrokeCap(Cap.ROUND);
        this.mScrollerPaint.setStrokeWidth(6.0f);
        this.mMinX = (-getWidth()) / 8;
        this.mMaxX = getWidth() / 8;
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
        if (this.mISurfaceAdapter == null) {
            return true;
        }
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
                case Opcodes.ILOAD:
                    if (index >= this.mRows) {
                        index -= this.mRows;
                    }
                    break;
                case Opcodes.LLOAD:
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
                case Opcodes.ILOAD:
                    if (index > 0) {
                        index--;
                    }
                    break;
                case Opcodes.LLOAD:
                    if (index < count - 1) {
                        index++;
                    }
                    break;
                default:
                    break;
            }
        }
        if (index == old) {
            return true;
        }
        this.mShowScroller = true;
        setSelectedItem(index);
        invalidate();
        return false;
    }

    private void touch_down(int x, int y) {
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
        invalidate();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void touch_move(int r6_x, int r7_y) {
        throw new UnsupportedOperationException("Method not decompiled: com.wmt.util.WmtGridView.touch_move(int, int):void");
        /*
        r5 = this;
        r2 = r5.mLastMotionX;
        r0 = r6 - r2;
        r2 = r5.mLastMotionY;
        r1 = r7 - r2;
        r2 = r5.mSurfaceViewMode;
        if (r2 != 0) goto L_0x0032;
    L_0x000c:
        r2 = r5.mScrollX;
        if (r2 <= 0) goto L_0x001d;
    L_0x0010:
        r2 = r5.mScrollX;
        r3 = r5.mMaxX;
        r4 = r5.getWidth();
        r4 = r4 / 8;
        r3 = r3 - r4;
        if (r2 < r3) goto L_0x002c;
    L_0x001d:
        r2 = r5.mScrollX;
        r3 = r0 / 2;
        r2 = r2 - r3;
        r5.mScrollX = r2;
    L_0x0024:
        r5.mLastMotionX = r6;
        r5.mLastMotionY = r7;
        r5.invalidate();
        return;
    L_0x002c:
        r2 = r5.mScrollX;
        r2 = r2 - r0;
        r5.mScrollX = r2;
        goto L_0x0024;
    L_0x0032:
        r2 = r5.mScrollY;
        if (r2 <= 0) goto L_0x0043;
    L_0x0036:
        r2 = r5.mScrollY;
        r3 = r5.mMaxY;
        r4 = r5.getHeight();
        r4 = r4 / 8;
        r3 = r3 - r4;
        if (r2 < r3) goto L_0x004b;
    L_0x0043:
        r2 = r5.mScrollY;
        r3 = r1 / 2;
        r2 = r2 - r3;
        r5.mScrollY = r2;
        goto L_0x0024;
    L_0x004b:
        r2 = r5.mScrollY;
        r2 = r2 - r1;
        r5.mScrollY = r2;
        goto L_0x0024;
        */
    }

    private void touch_up(int x, int y) {
        bounce();
        this.mLastMotionX = x;
        this.mLastMotionY = y;
        invalidate();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void bounce() {
        throw new UnsupportedOperationException("Method not decompiled: com.wmt.util.WmtGridView.bounce():void");
        /*
        r6 = this;
        r5 = 0;
        r4 = 0;
        r1 = r6.mSurfaceViewMode;
        if (r1 != 0) goto L_0x0035;
    L_0x0006:
        r1 = r6.mScrollX;
        if (r1 < 0) goto L_0x0017;
    L_0x000a:
        r1 = r6.mScrollX;
        r2 = r6.mMaxX;
        r3 = r6.getWidth();
        r3 = r3 / 8;
        r2 = r2 - r3;
        if (r1 <= r2) goto L_0x0026;
    L_0x0017:
        r1 = r6.mScrollX;
        if (r1 >= 0) goto L_0x0027;
    L_0x001b:
        r1 = r6.mScrollX;
        r1 = -r1;
        r0 = (float) r1;
    L_0x001f:
        r1 = r6.mScroller;
        r2 = r6.mScrollX;
        r1.bounce(r0, r2, r4, r5);
    L_0x0026:
        return;
    L_0x0027:
        r1 = r6.mMaxX;
        r2 = r6.getWidth();
        r2 = r2 / 8;
        r1 = r1 - r2;
        r2 = r6.mScrollX;
        r1 = r1 - r2;
        r0 = (float) r1;
        goto L_0x001f;
    L_0x0035:
        r1 = r6.mScrollY;
        if (r1 < 0) goto L_0x0046;
    L_0x0039:
        r1 = r6.mScrollY;
        r2 = r6.mMaxY;
        r3 = r6.getHeight();
        r3 = r3 / 8;
        r2 = r2 - r3;
        if (r1 <= r2) goto L_0x0026;
    L_0x0046:
        r1 = r6.mScrollY;
        if (r1 >= 0) goto L_0x0056;
    L_0x004a:
        r1 = r6.mScrollY;
        r1 = -r1;
        r0 = (float) r1;
    L_0x004e:
        r1 = r6.mScroller;
        r2 = r6.mScrollY;
        r1.bounce(r4, r5, r0, r2);
        goto L_0x0026;
    L_0x0056:
        r1 = r6.mMaxY;
        r2 = r6.getHeight();
        r2 = r2 / 8;
        r1 = r1 - r2;
        r2 = r6.mScrollY;
        r1 = r1 - r2;
        r0 = (float) r1;
        goto L_0x004e;
        */
    }

    public boolean compute() {
        if (!this.mScroller.computeScrollOffset()) {
            return false;
        }
        int x = this.mScroller.getCurrX();
        int y = this.mScroller.getCurrY();
        if (getChildCount() > 0) {
            this.mScrollX = clamp(x, getWidth() - this.mPaddingRight - this.mPaddingLeft, SURFACEVIEW_HORIZONTAL);
            this.mScrollY = clamp(y, getHeight() - this.mPaddingBottom - this.mPaddingTop, SURFACEVIEW_HORIZONTAL);
        } else {
            this.mScrollX = x;
            this.mScrollY = y;
        }
        int w = getWidth() - this.mPaddingLeft - this.mPaddingRight;
        int h = getHeight() - this.mPaddingBottom - this.mPaddingTop;
        if (this.mSurfaceViewMode == 0) {
            this.mScroller.crossBorder();
        } else {
            this.mScroller.crossBorder();
        }
        postInvalidate();
        return true;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    void drawBackground(android.graphics.Canvas r17_canvas) {
        throw new UnsupportedOperationException("Method not decompiled: com.wmt.util.WmtGridView.drawBackground(android.graphics.Canvas):void");
        /*
        r16 = this;
        r11 = r16.getWidth();
        r10 = r16.getHeight();
        r0 = r16;
        r12 = r0.mBgBmp;
        if (r12 != 0) goto L_0x008b;
    L_0x000e:
        r0 = r16;
        r12 = r0.mBgColor;
        r0 = r17;
        r0.drawColor(r12);
    L_0x0017:
        r0 = r16;
        r12 = r0.mBackgroundMode;
        r13 = 1;
        if (r12 != r13) goto L_0x008a;
    L_0x001e:
        r0 = r16;
        r12 = r0.mSurfaceViewMode;
        if (r12 != 0) goto L_0x019a;
    L_0x0024:
        r0 = r16;
        r12 = r0.mHeadBmp;
        if (r12 == 0) goto L_0x004e;
    L_0x002a:
        r0 = r16;
        r12 = r0.mScrollX;
        if (r12 >= 0) goto L_0x004e;
    L_0x0030:
        r0 = r16;
        r12 = r0.mHeadBmp;
        r12 = r12.getWidth();
        r12 = -r12;
        r0 = r16;
        r13 = r0.mScrollX;
        r8 = r12 - r13;
        r0 = r16;
        r12 = r0.mHeadBmp;
        r13 = (float) r8;
        r14 = 0;
        r0 = r16;
        r15 = r0.mPaint;
        r0 = r17;
        r0.drawBitmap(r12, r13, r14, r15);
    L_0x004e:
        r0 = r16;
        r12 = r0.mTailBmp;
        if (r12 == 0) goto L_0x008a;
    L_0x0054:
        r0 = r16;
        r12 = r0.mScrollX;
        r0 = r16;
        r13 = r0.mMaxX;
        r14 = r16.getWidth();
        r14 = r14 / 8;
        r13 = r13 - r14;
        if (r12 <= r13) goto L_0x008a;
    L_0x0065:
        r12 = r16.getWidth();
        r0 = r16;
        r13 = r0.mScrollX;
        r12 = r12 - r13;
        r0 = r16;
        r13 = r0.mMaxX;
        r12 = r12 + r13;
        r13 = r16.getWidth();
        r13 = r13 / 8;
        r8 = r12 - r13;
        r0 = r16;
        r12 = r0.mTailBmp;
        r13 = (float) r8;
        r14 = 0;
        r0 = r16;
        r15 = r0.mPaint;
        r0 = r17;
        r0.drawBitmap(r12, r13, r14, r15);
    L_0x008a:
        return;
    L_0x008b:
        r0 = r16;
        r12 = r0.mBgBmp;
        r2 = r12.getWidth();
        r0 = r16;
        r12 = r0.mBgBmp;
        r1 = r12.getHeight();
        if (r2 <= 0) goto L_0x009f;
    L_0x009d:
        if (r1 > 0) goto L_0x00a9;
    L_0x009f:
        r0 = r16;
        r12 = r0.mBgColor;
        r0 = r17;
        r0.drawColor(r12);
        goto L_0x008a;
    L_0x00a9:
        r0 = r16;
        r12 = r0.mBackgroundMode;
        if (r12 != 0) goto L_0x010d;
    L_0x00af:
        r6 = 1;
        r5 = 1;
        r0 = r16;
        r12 = r0.mSurfaceViewMode;
        if (r12 != 0) goto L_0x00e2;
    L_0x00b7:
        if (r2 >= r11) goto L_0x00bd;
    L_0x00b9:
        r12 = r11 / r2;
        r6 = r12 + 1;
    L_0x00bd:
        if (r1 >= r10) goto L_0x00c3;
    L_0x00bf:
        r12 = r10 / r1;
        r5 = r12 + 1;
    L_0x00c3:
        r3 = 0;
    L_0x00c4:
        if (r3 >= r6) goto L_0x0017;
    L_0x00c6:
        r4 = 0;
    L_0x00c7:
        if (r4 >= r5) goto L_0x00df;
    L_0x00c9:
        r0 = r16;
        r12 = r0.mBgBmp;
        r13 = r3 * r2;
        r13 = (float) r13;
        r14 = r4 * r1;
        r14 = (float) r14;
        r0 = r16;
        r15 = r0.mPaint;
        r0 = r17;
        r0.drawBitmap(r12, r13, r14, r15);
        r4 = r4 + 1;
        goto L_0x00c7;
    L_0x00df:
        r3 = r3 + 1;
        goto L_0x00c4;
    L_0x00e2:
        if (r1 >= r10) goto L_0x00e8;
    L_0x00e4:
        r12 = r10 / r1;
        r6 = r12 + 1;
    L_0x00e8:
        if (r2 >= r11) goto L_0x00ee;
    L_0x00ea:
        r12 = r11 / r2;
        r5 = r12 + 1;
    L_0x00ee:
        r3 = 0;
    L_0x00ef:
        if (r3 >= r6) goto L_0x0017;
    L_0x00f1:
        r4 = 0;
    L_0x00f2:
        if (r4 >= r5) goto L_0x010a;
    L_0x00f4:
        r0 = r16;
        r12 = r0.mBgBmp;
        r13 = r4 * r2;
        r13 = (float) r13;
        r14 = r3 * r1;
        r14 = (float) r14;
        r0 = r16;
        r15 = r0.mPaint;
        r0 = r17;
        r0.drawBitmap(r12, r13, r14, r15);
        r4 = r4 + 1;
        goto L_0x00f2;
    L_0x010a:
        r3 = r3 + 1;
        goto L_0x00ef;
    L_0x010d:
        r6 = 2;
        r5 = 1;
        r0 = r16;
        r12 = r0.mSurfaceViewMode;
        if (r12 != 0) goto L_0x0161;
    L_0x0115:
        if (r2 >= r11) goto L_0x011b;
    L_0x0117:
        r12 = r11 / r2;
        r6 = r12 + 2;
    L_0x011b:
        if (r1 >= r10) goto L_0x0121;
    L_0x011d:
        r12 = r10 / r1;
        r5 = r12 + 1;
    L_0x0121:
        r0 = r16;
        r12 = r0.mScrollX;
        r0 = r16;
        r13 = r0.mScrollX;
        r13 = r13 / r2;
        r13 = r13 * r2;
        r7 = r12 - r13;
        r3 = 0;
    L_0x012e:
        if (r3 >= r6) goto L_0x0017;
    L_0x0130:
        r0 = r16;
        r12 = r0.mBgBmp;
        r13 = -r7;
        r14 = r3 * r2;
        r13 = r13 + r14;
        r13 = (float) r13;
        r14 = 0;
        r0 = r16;
        r15 = r0.mPaint;
        r0 = r17;
        r0.drawBitmap(r12, r13, r14, r15);
        r4 = 0;
    L_0x0144:
        if (r4 >= r5) goto L_0x015e;
    L_0x0146:
        r0 = r16;
        r12 = r0.mBgBmp;
        r13 = -r7;
        r14 = r3 * r2;
        r13 = r13 + r14;
        r13 = (float) r13;
        r14 = r4 * r1;
        r14 = (float) r14;
        r0 = r16;
        r15 = r0.mPaint;
        r0 = r17;
        r0.drawBitmap(r12, r13, r14, r15);
        r4 = r4 + 1;
        goto L_0x0144;
    L_0x015e:
        r3 = r3 + 1;
        goto L_0x012e;
    L_0x0161:
        if (r1 >= r10) goto L_0x0167;
    L_0x0163:
        r12 = r10 / r1;
        r6 = r12 + 2;
    L_0x0167:
        if (r2 >= r11) goto L_0x016d;
    L_0x0169:
        r12 = r11 / r2;
        r5 = r12 + 1;
    L_0x016d:
        r0 = r16;
        r12 = r0.mScrollY;
        r0 = r16;
        r13 = r0.mScrollY;
        r13 = r13 / r1;
        r13 = r13 * r1;
        r7 = r12 - r13;
        r3 = 0;
    L_0x017a:
        if (r3 >= r6) goto L_0x0017;
    L_0x017c:
        r4 = 0;
    L_0x017d:
        if (r4 >= r5) goto L_0x0197;
    L_0x017f:
        r0 = r16;
        r12 = r0.mBgBmp;
        r13 = r4 * r2;
        r13 = (float) r13;
        r14 = -r7;
        r15 = r3 * r1;
        r14 = r14 + r15;
        r14 = (float) r14;
        r0 = r16;
        r15 = r0.mPaint;
        r0 = r17;
        r0.drawBitmap(r12, r13, r14, r15);
        r4 = r4 + 1;
        goto L_0x017d;
    L_0x0197:
        r3 = r3 + 1;
        goto L_0x017a;
    L_0x019a:
        r0 = r16;
        r12 = r0.mHeadBmp;
        if (r12 == 0) goto L_0x01c4;
    L_0x01a0:
        r0 = r16;
        r12 = r0.mScrollY;
        if (r12 >= 0) goto L_0x01c4;
    L_0x01a6:
        r0 = r16;
        r12 = r0.mHeadBmp;
        r12 = r12.getHeight();
        r12 = -r12;
        r0 = r16;
        r13 = r0.mScrollY;
        r9 = r12 - r13;
        r0 = r16;
        r12 = r0.mHeadBmp;
        r13 = 0;
        r14 = (float) r9;
        r0 = r16;
        r15 = r0.mPaint;
        r0 = r17;
        r0.drawBitmap(r12, r13, r14, r15);
    L_0x01c4:
        r0 = r16;
        r12 = r0.mTailBmp;
        if (r12 == 0) goto L_0x008a;
    L_0x01ca:
        r0 = r16;
        r12 = r0.mScrollY;
        r0 = r16;
        r13 = r0.mMaxY;
        r14 = r16.getHeight();
        r14 = r14 / 8;
        r13 = r13 - r14;
        if (r12 <= r13) goto L_0x008a;
    L_0x01db:
        r12 = r16.getHeight();
        r0 = r16;
        r13 = r0.mScrollY;
        r12 = r12 - r13;
        r0 = r16;
        r13 = r0.mMaxY;
        r12 = r12 + r13;
        r13 = r16.getHeight();
        r13 = r13 / 8;
        r9 = r12 - r13;
        r0 = r16;
        r12 = r0.mTailBmp;
        r13 = 0;
        r14 = (float) r9;
        r0 = r16;
        r15 = r0.mPaint;
        r0 = r17;
        r0.drawBitmap(r12, r13, r14, r15);
        goto L_0x008a;
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
        if (this.mISurfaceAdapter != null && this.mISurfaceAdapter.getItemCount() > 0) {
            int itemw = this.mISurfaceAdapter.getItemWidth();
            int itemh = this.mISurfaceAdapter.getItemHeight();
            if (itemw == -1) {
                itemw = getWidth() - this.mPaddingLeft - this.mPaddingRight;
            }
            if (itemh == -1) {
                itemh = getHeight() - this.mPaddingTop - this.mPaddingBottom;
            }
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
                        if (this.mPageRows * i + j < this.mISurfaceAdapter.getItemCount()) {
                            this.mISurfaceAdapter.drawItem(this.mPageRows * i + j, canvas, (float) ((-offset) + (i - start) * itemw), ((float) this.mPaddingTop) + ((float) (j * itemh)) + ((float) ((j * 2) + 1)) * this.mItemPaddingY, 0.0f, 1.0f);
                            j++;
                        } else {
                            return;
                        }
                    }
                    i++;
                }
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
                        if (this.mPageCols * i + j < this.mISurfaceAdapter.getItemCount()) {
                            this.mISurfaceAdapter.drawItem(this.mPageCols * i + j, canvas, ((float) this.mPaddingLeft) + ((float) (j * itemw)) + ((float) ((j * 2) + 1)) * this.mItemPaddingX, (float) ((-offset) + (i - start) * itemh), 0.0f, 1.0f);
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

    void drawMasks(Canvas canvas) {
        if (this.mISurfaceAdapter != null && this.mISurfaceAdapter.getItemCount() > 0) {
            int itemw = this.mISurfaceAdapter.getItemWidth();
            int itemh = this.mISurfaceAdapter.getItemHeight();
            if (itemw == -1) {
                itemw = getWidth() - this.mPaddingLeft - this.mPaddingRight;
            }
            if (itemh == -1) {
                itemh = getHeight() - this.mPaddingTop - this.mPaddingBottom;
            }
            int start;
            int i;
            int j;
            if (this.mSurfaceViewMode == 0) {
                start = this.mScrollX > 0 ? this.mScrollX / itemw : SURFACEVIEW_HORIZONTAL;
                i = start;
                while (i < this.mPageCols + start + 1) {
                    j = SURFACEVIEW_HORIZONTAL;
                    while (j < this.mPageRows) {
                        if (this.mPageRows * i + j < this.mISurfaceAdapter.getItemCount()) {
                            j++;
                        } else {
                            return;
                        }
                    }
                    i++;
                }
            } else {
                start = this.mScrollY > 0 ? this.mScrollY / itemh : SURFACEVIEW_HORIZONTAL;
                i = start;
                while (i < this.mPageRows + start + 1) {
                    j = SURFACEVIEW_HORIZONTAL;
                    while (j < this.mPageCols) {
                        if (this.mPageCols * i + j < this.mISurfaceAdapter.getItemCount()) {
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
        if (this.mScrollerSize != 0) {
            int offset;
            if (this.mSurfaceViewMode == 0) {
                if (this.mAdapterWidth > getWidth()) {
                    offset = ((getWidth() - this.mScrollerSize) * this.mScrollX) / (this.mAdapterWidth - getWidth());
                    canvas.drawLine((float) (offset + 3), (float) (getHeight() - 3), (float) (this.mScrollerSize + offset - 3), (float) (getHeight() - 3), this.mScrollerPaint);
                }
            } else if (this.mAdapterHeight > getHeight()) {
                offset = ((getHeight() - this.mScrollerSize) * this.mScrollY) / (this.mAdapterHeight - getHeight());
                canvas.drawLine((float) (getWidth() - 3), (float) (offset + 3), (float) (getWidth() - 3), (float) (this.mScrollerSize + offset - 3), this.mScrollerPaint);
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

    public boolean getItemRect(int item, Rect rect) {
        if (item < 0 || item >= this.mISurfaceAdapter.getItemCount()) {
            return false;
        }
        int itemw = this.mISurfaceAdapter.getItemWidth();
        int itemh = this.mISurfaceAdapter.getItemHeight();
        if (itemw == -1) {
            itemw = getWidth() - this.mPaddingLeft - this.mPaddingRight;
        }
        if (itemh == -1) {
            itemh = getHeight() - this.mPaddingTop - this.mPaddingBottom;
        }
        rect.set(-this.mScrollX, -this.mScrollY, (-this.mScrollX) + itemw, (-this.mScrollY) + itemh);
        if (this.mSurfaceViewMode == 0) {
            int row = item % this.mPageRows;
            rect.offset((item / this.mPageRows) * itemw, (int) (((float) (row * itemh)) + ((float) ((row * 2) + 1)) * this.mItemPaddingY + ((float) this.mPaddingTop)));
        } else {
            int col = item % this.mPageCols;
            rect.offset((int) (((float) (col * itemw)) + ((float) ((col * 2) + 1)) * this.mItemPaddingX + ((float) this.mPaddingLeft)), (item / this.mPageCols) * itemh);
        }
        return true;
    }

    public int[] getItemsOnScreen() {
        if (this.mISurfaceAdapter == null || this.mISurfaceAdapter.getItemCount() <= 0) {
            return null;
        }
        int startIndex;
        int endIndex;
        int itemw = this.mISurfaceAdapter.getItemWidth();
        int itemh = this.mISurfaceAdapter.getItemHeight();
        if (itemw == -1) {
            itemw = getWidth() - this.mPaddingLeft - this.mPaddingRight;
        }
        if (itemh == -1) {
            itemh = getHeight() - this.mPaddingTop - this.mPaddingBottom;
        }
        int count = this.mISurfaceAdapter.getItemCount();
        if (this.mSurfaceViewMode == 0) {
            startIndex = this.mScrollX > 0 ? this.mScrollX / itemw : SURFACEVIEW_HORIZONTAL;
            endIndex = (this.mPageCols + startIndex) * this.mPageRows + this.mPageRows - 1;
            if (endIndex > count) {
                endIndex = count;
            }
            startIndex *= this.mPageRows;
        } else {
            startIndex = this.mScrollY > 0 ? this.mScrollY / itemh : SURFACEVIEW_HORIZONTAL;
            endIndex = (this.mPageRows + startIndex) * this.mPageCols + this.mPageCols - 1;
            if (endIndex > count) {
                endIndex = count;
            }
            startIndex *= this.mPageCols;
        }
        return new int[]{startIndex, endIndex};
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
        invalidate();
    }

    public void invalidateItem(int index) {
        invalidate();
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

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (compute() || this.mMouseDown || this.mShowScroller) {
            resetFade();
        } else {
            stepFade();
        }
        doDraw(canvas);
    }

    public void onFlingBegin() {
    }

    public void onFlingEnd() {
        this.mScrollX = this.mScroller.getCurrX();
        this.mScrollY = this.mScroller.getCurrY();
        bounce();
        invalidate();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case TimeUtils.HUNDRED_DAY_FIELD_LEN:
            case KeyTouchInputEvent.EV_REP:
            case Opcodes.ILOAD:
            case Opcodes.LLOAD:
                return onNavigation(keyCode);
            case Opcodes.FLOAD:
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

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        computeRowsCols();
        if (this.mInitSelectedItem != 0) {
            setSelectedItem(this.mInitSelectedItem);
            this.mInitSelectedItem = 0;
        } else if (changed) {
            setSelectedItem(SURFACEVIEW_HORIZONTAL);
        }
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
                this.mMouseDown = true;
                if (!this.mScroller.isFinished()) {
                    this.mScroller.abortAnimation();
                }
                touch_down(x, y);
                break;
            case SURFACEVIEW_VERTICAL:
                this.mMouseDown = false;
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
            case ClassWriter.COMPUTE_FRAMES:
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

    void resetFade() {
        if (this.mShowScroller) {
            this.mShowScroller = false;
            postInvalidate();
        }
        this.fade = 10;
        this.alpha = 255;
        this.mScrollerPaint.setAlpha(this.alpha);
    }

    public void setAdapter(ISurfaceAdapter adapter) {
        this.mISurfaceAdapter = adapter;
        computeRowsCols();
        this.mScrollX = 0;
        this.mScrollY = 0;
        postInvalidate();
    }

    public void setBackgroundMode(int mode) {
        this.mBackgroundMode = mode;
    }

    public void setBgBmp(Bitmap bmp) {
        this.mBgBmp = bmp;
    }

    public void setBgColor(int color) {
        this.mBgColor = color;
    }

    public void setHeadBackground(Bitmap bmp) {
        this.mHeadBmp = bmp;
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
            if (itemw == -1) {
                itemw = w;
            }
            if (itemh == -1) {
                itemh = h;
            }
            if (this.mPageRows == 0 && this.mPageCols == 0) {
                computeRowsCols();
            }
            if (index >= 0 && index < this.mISurfaceAdapter.getItemCount()) {
                this.mISurfaceAdapter.setSelectedItem(index);
                if (this.mOnItemSelectedListener != null) {
                    this.mOnItemSelectedListener.onItemSelected(this, index, (long) index);
                }
                if (this.mSurfaceViewMode == 0) {
                    int x = (index / this.mPageRows) * itemw;
                    if (this.mScrollX >= x || this.mScrollX <= x - w - itemw) {
                        if (x > this.mMaxX - w / 8) {
                            x = this.mMaxX - w / 8;
                        }
                        this.mScrollX = x;
                        this.mScrollY = 0;
                    }
                } else {
                    int y = (index / this.mPageCols) * itemh;
                    if (this.mScrollY >= y || this.mScrollY <= y - h - itemh) {
                        if (y > this.mMaxY - h / 8) {
                            y = this.mMaxY - h / 8;
                        }
                        this.mScrollX = 0;
                        this.mScrollY = y;
                    }
                }
                invalidate();
            }
        }
    }

    public void setSurfaceViewMode(int mode) {
        this.mSurfaceViewMode = mode;
    }

    public void setTailBackground(Bitmap bmp) {
        this.mTailBmp = bmp;
    }

    public void setViewPadding(int left, int top, int right, int bottom) {
        this.mPaddingLeft = left;
        this.mPaddingTop = top;
        this.mPaddingRight = right;
        this.mPaddingBottom = bottom;
    }

    void stepFade() {
        if (this.fade <= 5 && this.alpha > 0) {
            this.alpha -= 51;
            this.mScrollerPaint.setAlpha(this.alpha);
        }
        this.fade--;
        if (this.fade > 0) {
            postInvalidate();
        }
    }

    public void updateAdapter(boolean reset) {
        computeRowsCols();
        if (reset) {
            this.mScrollX = 0;
            this.mScrollY = 0;
        }
        postInvalidate();
    }
}