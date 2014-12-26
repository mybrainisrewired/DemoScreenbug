package com.wmt.opengl.grid;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.util.TimeUtils;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import com.wmt.data.DataManager;
import com.wmt.opengl.BitmapTexture;
import com.wmt.opengl.FloatUtils;
import com.wmt.opengl.GLCanvas;
import com.wmt.opengl.GLContext;
import com.wmt.opengl.GLView;
import com.wmt.opengl.NinePatchTexture;
import com.wmt.opengl.PopupMenu;
import com.wmt.opengl.PopupMenu.Listener;
import com.wmt.opengl.PopupMenu.Option;
import com.wmt.opengl.Texture;
import com.wmt.opengl.Utils;
import com.wmt.opengl.VertexDrawer;
import com.wmt.opengl.grid.BounceScroller.ScrollerListener;
import com.wmt.remotectrl.ConnectionInstance;
import com.wmt.remotectrl.KeyTouchInputEvent;
import com.wmt.util.Log;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import javax.microedition.khronos.opengles.GL11;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.smile.SmileConstants;

public class GLGridView extends GLView {
    public static final int CHOICE_MODE_MULTIPLE = 2;
    public static final int CHOICE_MODE_NONE = 0;
    public static final int CHOICE_MODE_SINGLE = 1;
    private static final int CROSSFADE_BOTTOM = 3;
    private static final int CROSSFADE_LEFT = 0;
    private static final int CROSSFADE_NONE = -1;
    private static final int CROSSFADE_RIGHT = 2;
    private static final int CROSSFADE_TOP = 1;
    private static final boolean DEBUG = false;
    private static final int DEFAULT_SPACE = 5;
    public static final int INVALID_POSITION = -1;
    private static final int MAX_BOUNCE_LEN = 100;
    private static final int MIN_THUMB_SIZE = 15;
    public static final int SCROLL_MODE_HORIZONTAL = 0;
    public static final int SCROLL_MODE_MAX = 1;
    public static final int SCROLL_MODE_VERTICAL = 1;
    public static final int SLIDE_MODE_ARC = 1;
    public static final int SLIDE_MODE_PLANE = 0;
    public static final int SLIDE_MODE_TILT = 2;
    private static final int SNAP_VELOCITY = 200;
    public static final int VELOCITYX_MAX = 5000;
    private final float MAX_TILT_ARC;
    private float MINR;
    private final float PLANER;
    private String TAG;
    private IGLGridAdapter mAdapter;
    float[] mArcAXZ;
    private BitmapTexture mBackGround;
    private int mBackGroundID;
    private boolean mCanShowItemMenu;
    private SparseBooleanArray mCheckStates;
    private SparseArray<Boolean> mCheckedIdStates;
    private int mChoiceMode;
    private float mCircleZ;
    private FloatBuffer mColorsBuffer;
    private int mCount;
    private float mCrossfadeRatio;
    private float mCurrentR;
    private int mCurrentScreen;
    private boolean mDiffAnimationMark;
    private ArrayList<ItemAnimation> mDiffList;
    private boolean mDiscardFoucsHorizontal;
    private boolean mDiscardFoucsVertical;
    int mDragAnimationEnd;
    boolean mDragAnimationMart;
    int mDragAnimationStart;
    boolean mDragAnimationUp;
    ArrayList<DragAnimationXY> mDragAnimationXYs;
    private boolean mFocusChangedByKeyBoard;
    private Rect mFocusRect;
    VertexDrawer mGLGridBackgroundDrawer;
    GestureDetector mGestureDetector;
    GridDrawArgs mGridDrawArgs;
    private boolean mGroupAnimationMark;
    private ArrayList<ItemAnimation> mGroupList;
    private HitTestArgs mHitTestArgs;
    private int mHorizontalSpace;
    private IOnItemClickListener mIOnItemClickListener;
    private IOnItemLongClickListener mIOnItemLongClickListener;
    private IOnItemFocusChangeListener mItemChangeListener;
    private int mItemFocused;
    private int mItemPartCount;
    private int mItemTouchDowm;
    private int mLastDownItem;
    private float mLastDownX;
    private float mLastDownY;
    private long mLastDragTime;
    private long mLastFrameTime;
    private LayoutDetail mLayoutDetail;
    private int mOldItemFocus;
    private boolean mOnShowScrollBar;
    private Rect mPaddingRect;
    private PopupMenu mPopupMenu;
    private Rect mRealRect;
    private FloatBuffer mReflectionBuffer;
    private float[] mReflectionColors;
    private float mReflectionHeight;
    private boolean mReqScrollBar;
    private boolean mRequestRender;
    private float mScrollAdjust;
    private float mScrollBarAlpha;
    private VertexDrawer mScrollBarThumbDrawer;
    private Rect mScrollBarThumbDrawerRect;
    private VertexDrawer mScrollBarTrackDrawer;
    private Rect mScrollBarTrackDrawerRect;
    private int mScrollMode;
    private float mScrollX;
    private float mScrollY;
    private BounceScroller mScroller;
    private ScrollerListener mScrollerListener;
    private boolean mSingleLine;
    private boolean mSizeOrPosChanged;
    private int mSlideMode;
    private int mThumbHorizontal;
    private int mThumbVertical;
    private float mTiltArc;
    private boolean mTouchMoved;
    private int mTrackBarSize;
    private int mTrackHorizontal;
    private int mTrackVertical;
    ArrayList<Integer> mUnableShowMenuItems;
    private VelocityTracker mVelocityTracker;
    private int mVerticalSpace;
    private float mZValue;

    class AnonymousClass_1 implements Runnable {
        final /* synthetic */ Context val$context;

        AnonymousClass_1(Context context) {
            this.val$context = context;
        }

        public void run() {
            GLGridView.this.mGestureDetector = new GestureDetector(this.val$context, new MyGestureListener(null), null, true);
            GLGridView.this.mGestureDetector.setIsLongpressEnabled(true);
        }
    }

    static /* synthetic */ class AnonymousClass_4 {
        static final /* synthetic */ int[] $SwitchMap$com$wmt$opengl$grid$GLGridView$FocusDirection;

        static {
            $SwitchMap$com$wmt$opengl$grid$GLGridView$FocusDirection = new int[com.wmt.opengl.grid.GLGridView.FocusDirection.values().length];
            try {
                $SwitchMap$com$wmt$opengl$grid$GLGridView$FocusDirection[com.wmt.opengl.grid.GLGridView.FocusDirection.LEFT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$wmt$opengl$grid$GLGridView$FocusDirection[com.wmt.opengl.grid.GLGridView.FocusDirection.RIGHT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$wmt$opengl$grid$GLGridView$FocusDirection[com.wmt.opengl.grid.GLGridView.FocusDirection.UP.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            $SwitchMap$com$wmt$opengl$grid$GLGridView$FocusDirection[com.wmt.opengl.grid.GLGridView.FocusDirection.DOWN.ordinal()] = 4;
        }
    }

    private static class DragAnimationXY {
        float mDeviantX;
        float mDeviantY;

        private DragAnimationXY() {
        }
    }

    public enum FocusDirection {
        LEFT,
        RIGHT,
        UP,
        DOWN
    }

    public static interface IOnItemClickListener {
        void onItemClick(int i, float f, float f2);
    }

    public static interface IOnItemFocusChangeListener {
        void onItemFocusChange(int i, int i2);
    }

    public static interface IOnItemLongClickListener {
        void onLongItemClick(int i, float f, float f2);
    }

    private static class LayoutDetail {
        int startIndex;
        int startOffset;

        private LayoutDetail() {
        }
    }

    private class MyGestureListener extends SimpleOnGestureListener {
        private MyGestureListener() {
        }

        public boolean onDown(MotionEvent e) {
            GLGridView.this.glContext().lockRenderThread();
            GLGridView.this.mScroller.forceFinished(true);
            GLGridView.this.fillGap();
            if (GLGridView.this.mSlideMode == 1) {
                GLGridView.this.calculationRadiusBySpeed(0.0f);
            }
            GLGridView.this.mItemTouchDowm = GLGridView.this.findTouchItem(e.getX(), e.getY());
            GLGridView.this.mItemFocused = INVALID_POSITION;
            GLGridView.this.glContext().unlockRenderThread();
            return true;
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            GLGridView.this.glContext().lockRenderThread();
            GLGridView.this.snapScreen((int) ((float) GLGridView.this.transformSpeed((int) velocityX)), (int) ((float) GLGridView.this.transformSpeed((int) velocityY)));
            GLGridView.this.glContext().unlockRenderThread();
            GLGridView.this.invalidate();
            return true;
        }

        public void onLongPress(MotionEvent e) {
            GLGridView.this.glContext().lockRenderThread();
            GLGridView.this.mLastDownX = e.getX();
            GLGridView.this.mLastDownY = e.getY();
            GLGridView.this.performLongClick();
            GLGridView.this.glContext().unlockRenderThread();
            GLGridView.this.invalidate();
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            GLGridView.this.glContext().lockRenderThread();
            GLGridView.this.mTouchMoved = true;
            if (GLGridView.this.mSlideMode == 1) {
                VelocityTracker velocityTracker = GLGridView.this.mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000);
                GLGridView.this.calculationRadiusBySpeed((float) GLGridView.this.transformSpeed((int) velocityTracker.getXVelocity()));
            }
            float deltaX = distanceX;
            float deltaY = distanceY;
            int lastPageLen;
            int available;
            if (GLGridView.this.mScrollMode == 0) {
                lastPageLen = GLGridView.this.getContentLen() - GLGridView.this.mRealRect.width();
                if (lastPageLen == 0) {
                    lastPageLen = GLGridView.this.mRealRect.width();
                }
                if ((GLGridView.this.mScrollX < 0.0f && deltaX < 0.0f) || (GLGridView.this.mScrollX > ((float) lastPageLen) && deltaX > 0.0f)) {
                    deltaX /= 2.0f;
                }
                GLGridView.access$516(GLGridView.this, deltaX);
                if (GLGridView.this.mSlideMode == 2) {
                    if (GLGridView.this.mScrollX < 0.0f) {
                        GLGridView.this.mTiltArc = (GLGridView.this.mScrollX * 30.0f) / ((float) GLGridView.this.mRealRect.width());
                    } else {
                        available = GLGridView.this.getContentLen() - GLGridView.this.mRealRect.width();
                        if (available < 0) {
                            available = SLIDE_MODE_PLANE;
                        }
                        if (GLGridView.this.mScrollX > ((float) available)) {
                            GLGridView.this.mTiltArc = ((GLGridView.this.mScrollX - ((float) available)) * 30.0f) / ((float) GLGridView.this.mRealRect.width());
                        } else {
                            GLGridView.this.mTiltArc = 0.0f;
                        }
                    }
                }
            } else {
                lastPageLen = GLGridView.this.getContentLen() - GLGridView.this.mRealRect.height();
                if (lastPageLen == 0) {
                    lastPageLen = GLGridView.this.mRealRect.height();
                }
                if ((GLGridView.this.mScrollY < 0.0f && deltaY < 0.0f) || (GLGridView.this.mScrollY > ((float) lastPageLen) && deltaY > 0.0f)) {
                    deltaY /= 2.0f;
                }
                GLGridView.access$616(GLGridView.this, deltaY);
                if (GLGridView.this.mSlideMode == 2) {
                    if (GLGridView.this.mScrollY < 0.0f) {
                        GLGridView.this.mTiltArc = (GLGridView.this.mScrollY * 30.0f) / ((float) GLGridView.this.mRealRect.height());
                    } else {
                        available = GLGridView.this.getContentLen() - GLGridView.this.mRealRect.height();
                        if (available < 0) {
                            available = SLIDE_MODE_PLANE;
                        }
                        if (GLGridView.this.mScrollY > ((float) available)) {
                            GLGridView.this.mTiltArc = ((GLGridView.this.mScrollY - ((float) available)) * 30.0f) / ((float) GLGridView.this.mRealRect.height());
                        } else {
                            GLGridView.this.mTiltArc = 0.0f;
                        }
                    }
                }
            }
            if (GLGridView.this.mTiltArc > 30.0f) {
                GLGridView.this.mTiltArc = 30.0f;
            }
            GLGridView.this.glContext().unlockRenderThread();
            GLGridView.this.invalidate();
            return true;
        }

        public boolean onSingleTapConfirmed(MotionEvent e) {
            GLGridView.this.glContext().lockRenderThread();
            int item = GLGridView.this.findTouchItem(e.getX(), e.getY());
            GLGridView.this.setFocusedItem(item, DEBUG);
            if (GLGridView.this.mIOnItemClickListener != null && GLGridView.this.mAdapter != null && item >= 0 && item < GLGridView.this.mAdapter.getCount()) {
                float[] coord = new float[2];
                GLGridView.this.findItemXY(item, coord);
                GLGridView.this.performItemClick(item);
                GLGridView.this.mIOnItemClickListener.onItemClick(item, coord[0], coord[1]);
            }
            GLGridView.this.glContext().unlockRenderThread();
            GLGridView.this.invalidate();
            return true;
        }
    }

    public GLGridView(GLContext glContext) {
        super(glContext, 23);
        this.TAG = "GLGridView";
        this.mScrollAdjust = 0.0f;
        this.mLastDragTime = 0;
        this.mHorizontalSpace = 5;
        this.mVerticalSpace = 5;
        this.mScrollMode = 0;
        this.mSlideMode = 0;
        this.MAX_TILT_ARC = 30.0f;
        this.mTiltArc = 0.0f;
        this.mArcAXZ = new float[3];
        this.mCircleZ = 0.0f;
        this.PLANER = 100000.0f;
        this.MINR = 600.0f;
        this.mCurrentR = 100000.0f;
        this.mReflectionHeight = 0.0f;
        this.mReflectionColors = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.8f, 0.8f, 0.8f, 0.8f, 0.8f, 0.8f, 0.8f, 0.8f, 0.0f, 0.0f, 0.0f, 0.0f};
        this.mCrossfadeRatio = -1.0f;
        this.mZValue = -2.0f;
        this.mTouchMoved = false;
        this.mOldItemFocus = 0;
        this.mItemFocused = 0;
        this.mItemTouchDowm = -1;
        this.mFocusChangedByKeyBoard = false;
        this.mDiscardFoucsHorizontal = true;
        this.mDiscardFoucsVertical = true;
        this.mLayoutDetail = new LayoutDetail();
        this.mBackGroundID = -1;
        this.mFocusRect = null;
        this.mRequestRender = false;
        this.mCanShowItemMenu = false;
        this.mTrackBarSize = 5;
        this.mReqScrollBar = true;
        this.mOnShowScrollBar = false;
        this.mScrollBarAlpha = 0.0f;
        this.mLastFrameTime = 0;
        this.mHitTestArgs = new HitTestArgs();
        this.mRealRect = new Rect();
        this.mPaddingRect = new Rect(5, 5, 5, 5);
        this.mDiffList = new ArrayList();
        this.mDiffAnimationMark = false;
        this.mGroupAnimationMark = false;
        this.mDragAnimationMart = false;
        this.mDragAnimationUp = false;
        this.mDragAnimationStart = -1;
        this.mDragAnimationEnd = -1;
        this.mDragAnimationXYs = new ArrayList();
        this.mItemPartCount = 1;
        this.mScrollerListener = new ScrollerListener() {
            public void onFlingBegin() {
            }

            public void onFlingEnd() {
                GLGridView.this.mScroller.computeScrollOffset();
                GLGridView.this.mScrollX = (float) GLGridView.this.mScroller.getCurrX();
                GLGridView.this.mScrollY = (float) GLGridView.this.mScroller.getCurrY();
                int len = GLGridView.this.getContentLen();
                if (GLGridView.this.mScrollMode == 0) {
                    if (GLGridView.this.mScrollX < 0.0f) {
                        GLGridView.this.mScroller.bounce(-GLGridView.this.mScrollX, GLGridView.this.mScrollX, 0.0f, 0.0f);
                    } else if (GLGridView.this.mScrollX + ((float) GLGridView.this.mRealRect.width()) > ((float) len)) {
                        GLGridView.this.mScroller.bounce(((float) len) - GLGridView.this.mScrollX + ((float) GLGridView.this.mRealRect.width()), GLGridView.this.mScrollX, 0.0f, 0.0f);
                    }
                } else if (GLGridView.this.mScrollY < 0.0f) {
                    GLGridView.this.mScroller.bounce(0.0f, 0.0f, -GLGridView.this.mScrollY, GLGridView.this.mScrollY);
                } else if (GLGridView.this.mScrollY + ((float) GLGridView.this.mRealRect.height()) > ((float) len)) {
                    GLGridView.this.mScroller.bounce(0.0f, 0.0f, ((float) len) - GLGridView.this.mScrollY + ((float) GLGridView.this.mRealRect.height()), GLGridView.this.mScrollY);
                }
                GLGridView.this.invalidate();
            }

            public void onScrollEnd() {
            }
        };
        this.mGridDrawArgs = new GridDrawArgs();
        this.mGridDrawArgs.z = this.mZValue;
        initScroller();
        initColors();
        glContext.postUIThreadRunnable(new AnonymousClass_1(glContext.uiContext()), 0);
    }

    static /* synthetic */ float access$516(GLGridView x0, float x1) {
        float f = x0.mScrollX + x1;
        x0.mScrollX = f;
        return f;
    }

    static /* synthetic */ float access$616(GLGridView x0, float x1) {
        float f = x0.mScrollY + x1;
        x0.mScrollY = f;
        return f;
    }

    private void calculateArcVer(float startOffsetX, int xLoopIndex, float itemW, int left, int top) {
        float[] vertex = this.mGridDrawArgs.arcVertex;
        if (this.mCurrentR < 100000.0f && vertex != null && vertex.length >= ((this.mItemPartCount * 2) + 2) * 3) {
            int centerX = this.mRealRect.width() / 2;
            calculateCircleZ((float) centerX);
            float zeroArc = (float) (1.5707963267948966d - Math.asin((double) (((float) centerX) / this.mCurrentR)));
            float angle = getAngleByLen(((float) this.mHorizontalSpace) + itemW, this.mCurrentR);
            float partAngle = getAngleByLen(itemW / ((float) this.mItemPartCount), this.mCurrentR);
            float startAngle = getAngleByLen(startOffsetX, this.mCurrentR) + zeroArc + ((float) xLoopIndex) * angle;
            GLCanvas glCanvas = glContext().glCanvas();
            float glTopY = glCanvas.getYPos2GL(this.mGridDrawArgs.itemTop, this.mZValue);
            float glBottomY = glCanvas.getYPos2GL(this.mGridDrawArgs.itemTop + this.mGridDrawArgs.itemHeight, this.mZValue);
            int i = SLIDE_MODE_PLANE;
            while (i <= this.mItemPartCount) {
                float angle1 = startAngle + ((float) i) * partAngle;
                float z = (float) (((double) this.mCircleZ) - ((double) this.mCurrentR) * Math.sin((double) angle1));
                int startIndex = i * 6;
                vertex[startIndex] = glCanvas.getXPos2GL(((float) centerX) - ((float) (((double) this.mCurrentR) * Math.cos((double) angle1))) + ((float) left), this.mZValue);
                vertex[startIndex + 1] = glTopY;
                vertex[startIndex + 2] = glCanvas.getPixel2GL(this.mZValue) * z + this.mZValue;
                vertex[startIndex + 3] = vertex[startIndex];
                vertex[startIndex + 4] = glBottomY;
                vertex[startIndex + 5] = vertex[startIndex + 2];
                i++;
            }
        }
    }

    private float calculateCenterX(int startOffset, int xLoopIndeX, float itemW) {
        float itemCenterX = itemW / 2.0f + ((float) xLoopIndeX) * (((float) this.mHorizontalSpace) + itemW);
        return this.mScrollMode == 0 ? itemCenterX + ((float) startOffset) : itemCenterX;
    }

    private float calculateCenterY(int startOffset, int yLoopIndex, float itemH) {
        float itemCenterY = itemH / 2.0f + ((float) yLoopIndex) * (((float) this.mVerticalSpace) + itemH);
        return this.mScrollMode == 1 ? itemCenterY + ((float) startOffset) : itemCenterY;
    }

    private FloatBuffer calculateColorsBuffer(float color, FloatBuffer buffer, int site) {
        float point1 = 1.0f;
        float point2 = 1.0f;
        float point3 = 1.0f;
        float point4 = 1.0f;
        if (buffer == null) {
            ByteBuffer bb = ByteBuffer.allocateDirect(SmileConstants.TOKEN_PREFIX_TINY_ASCII);
            bb.order(ByteOrder.nativeOrder());
            buffer = bb.asFloatBuffer();
        }
        switch (site) {
            case SLIDE_MODE_PLANE:
                point1 = 1.0f - color;
                point2 = 1.0f - color;
                point3 = 1.0f - color * color;
                point4 = point3;
                break;
            case SLIDE_MODE_ARC:
                point1 = 1.0f - color * color;
                point2 = 1.0f - color;
                point3 = point1;
                point4 = 1.0f - color;
                break;
            case SLIDE_MODE_TILT:
                point1 = 1.0f - color * color;
                point2 = point1;
                point3 = 1.0f - color;
                point4 = 1.0f - color;
                break;
            case CROSSFADE_BOTTOM:
                point1 = 1.0f - color;
                point2 = 1.0f - color * color;
                point3 = 1.0f - color;
                point4 = point2;
                break;
        }
        buffer.clear();
        buffer.position(SLIDE_MODE_PLANE);
        putFloatBufferFour(buffer, point1);
        putFloatBufferFour(buffer, point2);
        putFloatBufferFour(buffer, point3);
        putFloatBufferFour(buffer, point4);
        buffer.position(SLIDE_MODE_PLANE);
        return buffer;
    }

    private void calculationRadiusBySpeed(float speed) {
        if (this.mSlideMode == 1) {
            this.mCurrentR = this.MINR;
        } else {
            speed = Math.abs(speed);
            if (speed > 5000.0f) {
                this.mCurrentR = this.MINR;
            } else {
                this.mCurrentR = (float) (100000.0d * Math.sqrt((double) ((5000.0f - speed) / 5000.0f)));
            }
            if (this.mCurrentR < this.MINR) {
                this.mCurrentR = this.MINR;
            }
        }
    }

    private void computerItemsLayoutDetail() {
        if (this.mAdapter != null) {
            float thisSize;
            float itemSize;
            float scrollLen;
            int startIndex;
            int startOffset;
            LayoutDetail detail = this.mLayoutDetail;
            int itemWidthWithSpace = this.mAdapter.getItemWidth() + this.mHorizontalSpace;
            int itemHeightWithSpace = this.mAdapter.getItemHeight() + this.mVerticalSpace;
            int totalCount = this.mAdapter.getCount();
            float thisW = (float) getWidth();
            float thisH = (float) getHeight();
            float contentLen = (float) getContentLen();
            if (this.mScrollMode == 0) {
                thisSize = thisW;
                itemSize = (float) itemWidthWithSpace;
                scrollLen = this.mScrollX;
                if (contentLen >= ((float) this.mRealRect.width())) {
                    this.mScrollAdjust = 0.0f;
                } else if (this.mSlideMode == 0 || this.mSlideMode == 2) {
                    this.mScrollAdjust = (-(((float) this.mRealRect.width()) - contentLen)) / 2.0f;
                } else if (this.mSlideMode == 1) {
                    this.mScrollAdjust = getScrollAdjustByArc();
                }
            } else {
                thisSize = thisH;
                itemSize = (float) itemHeightWithSpace;
                scrollLen = this.mScrollY;
            }
            if (this.mSlideMode == 2) {
                if (scrollLen < 0.0f) {
                    scrollLen = 0.0f;
                } else if (scrollLen > contentLen - thisSize) {
                    scrollLen = contentLen - thisSize;
                    if (scrollLen < 0.0f) {
                        scrollLen = 0.0f;
                    }
                }
            }
            if (this.mScrollMode == 0) {
                scrollLen += this.mScrollAdjust;
            }
            int visibleItems = ((int) Math.ceil((double) (thisSize / itemSize))) + 1;
            if (scrollLen > 0.0f) {
                if (scrollLen % itemSize == 0.0f) {
                    startIndex = (int) (scrollLen / itemSize);
                    startOffset = SLIDE_MODE_PLANE;
                } else {
                    startIndex = ((int) Math.ceil((double) (scrollLen / itemSize))) - 1;
                    if (startIndex > totalCount) {
                        startIndex = totalCount;
                    }
                    startOffset = (int) ((-scrollLen) % itemSize);
                }
            } else if (scrollLen < 0.0f) {
                startIndex = SLIDE_MODE_PLANE;
                float distance = -scrollLen;
                visibleItems = Math.max(0, (int) Math.ceil((double) ((thisSize - distance) / itemSize)));
                startOffset = (int) distance;
            } else {
                startIndex = SLIDE_MODE_PLANE;
                startOffset = SLIDE_MODE_PLANE;
            }
            detail.startIndex = startIndex;
            detail.startOffset = startOffset;
        }
    }

    private int computerStartIndex() {
        computerItemsLayoutDetail();
        int startIndex = this.mLayoutDetail.startIndex;
        int xNum = getNumOfColPerPage();
        int yNum = getNumOfRowPerPage();
        if (this.mScrollMode == 0) {
            return startIndex * yNum;
        }
        return this.mScrollMode == 1 ? startIndex * xNum : startIndex;
    }

    private void fillGap() {
        int contentLen = getContentLen();
        int avilable;
        if (this.mScrollMode != 0) {
            if (this.mScrollY < 0.0f) {
                this.mScrollY = 0.0f;
            }
            avilable = contentLen - this.mRealRect.height();
            if (avilable < 0) {
                this.mScrollY = 0.0f;
            } else if (this.mScrollY > ((float) avilable)) {
                this.mScrollY = (float) avilable;
            }
        } else if (this.mScrollX <= 0.0f) {
            this.mScrollX = 0.0f;
        } else {
            avilable = contentLen - this.mRealRect.width();
            if (avilable < 0) {
                this.mScrollX = 0.0f;
            } else if (this.mScrollX > ((float) avilable)) {
                this.mScrollX = (float) avilable;
            }
        }
    }

    private DragAnimationXY getAnimationXY(int from, int to) {
        DragAnimationXY dragAnimationXY = new DragAnimationXY();
        float itemW = (float) this.mAdapter.getItemWidth();
        float itemH = (float) this.mAdapter.getItemHeight();
        int yNum = getNumOfRowPerPage();
        dragAnimationXY.mDeviantX = calculateCenterX(SLIDE_MODE_PLANE, from / yNum, itemW) - calculateCenterX(SLIDE_MODE_PLANE, to / yNum, itemW);
        dragAnimationXY.mDeviantY = calculateCenterY(SLIDE_MODE_PLANE, from % yNum, itemH) - calculateCenterY(SLIDE_MODE_PLANE, to % yNum, itemH);
        return dragAnimationXY;
    }

    private int getMinAbs(int a, int b) {
        return a == 0 ? SLIDE_MODE_PLANE : Math.min(Math.abs(a), Math.abs(b)) * (Math.abs(a) / a);
    }

    private float getScrollAdjustByArc() {
        float len = (float) getContentLen();
        int rows = getNumOfRowPerPage();
        int cols = ((this.mAdapter.getCount() + rows) - 1) / rows;
        if (len >= ((float) this.mRealRect.width()) || this.mSlideMode != 1) {
            return 0.0f;
        }
        float angle = (getAngleByLen((float) this.mRealRect.width(), this.mCurrentR) - (((float) cols) * getAngleByLen((float) (this.mAdapter.getItemWidth() + this.mHorizontalSpace), this.mCurrentR))) / 2.0f;
        if (angle <= 0.0f) {
            return 0.0f;
        }
        float adjust = -((float) (((double) (this.mCurrentR * 2.0f)) * Math.sin((double) (angle / 2.0f))));
        return adjust > 0.0f ? 0.0f : adjust;
    }

    private void initColors() {
        ByteBuffer bb = ByteBuffer.allocateDirect(this.mReflectionColors.length * 4);
        bb.order(ByteOrder.nativeOrder());
        this.mReflectionBuffer = bb.asFloatBuffer();
        this.mReflectionBuffer.put(this.mReflectionColors);
        this.mReflectionBuffer.position(SLIDE_MODE_PLANE);
    }

    private void initScrollBarDrawer(int left, int top) {
        int totalLen = getContentLen();
        boolean showScrollBar = true;
        if (this.mScrollMode == 0) {
            if (totalLen < this.mRealRect.width()) {
                showScrollBar = DEBUG;
            }
        } else if (totalLen < this.mRealRect.height()) {
            showScrollBar = DEBUG;
        }
        if (!this.mReqScrollBar || !showScrollBar) {
            this.mScrollBarTrackDrawer = null;
            this.mScrollBarThumbDrawer = null;
        } else if (this.mSizeOrPosChanged) {
            this.mSizeOrPosChanged = false;
            this.mScrollBarTrackDrawer = null;
            this.mScrollBarThumbDrawer = null;
            initScrollBarTrackDrawer(left, top);
            initScrollBarThumbDrawer(left, top);
        }
    }

    private void initScrollBarThumbDrawer(int left, int top) {
        int trackSize = this.mTrackBarSize;
        if (this.mScrollBarThumbDrawer == null) {
            Rect rect;
            Texture scrollBarTrack;
            int y;
            int h;
            int x;
            if (this.mScrollMode == 0) {
                if (this.mThumbHorizontal != 0) {
                    int trackHeight = trackSize;
                    y = getHeight() + top - trackHeight;
                    h = trackHeight;
                    x = left + this.mPaddingRect.left;
                    rect = new Rect(x, y, x + Math.max((getWidth() * getWidth()) / getContentLen(), MIN_THUMB_SIZE), y + h);
                    scrollBarTrack = new NinePatchTexture(this.mThumbHorizontal, rect);
                } else {
                    return;
                }
            } else if (this.mThumbVertical != 0) {
                int w = trackSize;
                x = getWidth() + left - w;
                y = top + this.mPaddingRect.top;
                h = getHeight();
                rect = new Rect(x, y, x + w, y + Math.max((getHeight() * getHeight()) / getContentLen(), MIN_THUMB_SIZE));
                scrollBarTrack = new NinePatchTexture(this.mThumbVertical, rect);
            } else {
                return;
            }
            this.mScrollBarThumbDrawerRect = rect;
            this.mScrollBarThumbDrawer = new VertexDrawer(this.mZValue + 0.001f, scrollBarTrack);
            this.mScrollBarThumbDrawer.setDrawSize((float) rect.width(), (float) rect.height());
        }
    }

    private void initScrollBarTrackDrawer(int left, int top) {
        int trackHeight = this.mTrackBarSize;
        if (this.mScrollBarTrackDrawer == null) {
            Rect rect;
            Texture scrollBarTrack;
            if (this.mScrollMode == 0) {
                if (this.mTrackHorizontal != 0) {
                    rect = new Rect(this.mPaddingRect.left + left, getHeight() + top - trackHeight, getWidth() + left - this.mPaddingRect.right, getHeight() + top);
                    scrollBarTrack = new NinePatchTexture(this.mTrackHorizontal, rect);
                } else {
                    return;
                }
            } else if (this.mTrackVertical != 0) {
                rect = new Rect(getWidth() + left - trackHeight, this.mPaddingRect.top + top, getWidth() + left, getHeight() + top - this.mPaddingRect.bottom);
                scrollBarTrack = new NinePatchTexture(this.mTrackVertical, rect);
            } else {
                return;
            }
            this.mScrollBarTrackDrawer = new VertexDrawer(this.mZValue, scrollBarTrack);
            this.mScrollBarTrackDrawer.setDrawSize((float) rect.width(), (float) rect.height());
            this.mScrollBarTrackDrawerRect = rect;
        }
    }

    private void initScroller() {
        this.mScroller = new BounceScroller(glContext().uiContext(), 9.80665f);
        this.mScroller.setScrollerListener(this.mScrollerListener);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void makeFocusCenterVisible(int r15_focusStep) {
        throw new UnsupportedOperationException("Method not decompiled: com.wmt.opengl.grid.GLGridView.makeFocusCenterVisible(int):void");
        /*
        r14 = this;
        r13 = 0;
        r7 = r14.mItemFocused;
        r10 = r14.computerStartIndex();
        if (r15 >= 0) goto L_0x001f;
    L_0x0009:
        r11 = r14.mScrollMode;
        if (r11 != 0) goto L_0x008a;
    L_0x000d:
        r11 = r14.mLayoutDetail;
        r11 = r11.startOffset;
        r12 = r14.mAdapter;
        r12 = r12.getItemWidth();
        r11 = r11 % r12;
        if (r11 <= 0) goto L_0x008a;
    L_0x001a:
        r11 = r14.getNumOfRowPerPage();
        r10 = r10 + r11;
    L_0x001f:
        r11 = r14.getFloorNumberPrePage();
        r0 = r10 + r11;
        r11 = r14.mAdapter;
        r11 = r11.getCount();
        r11 = r11 + -1;
        r0 = java.lang.Math.min(r0, r11);
        r5 = r7 + r15;
        r8 = 0;
        r9 = 0;
        r11 = r14.mScrollMode;
        if (r11 != 0) goto L_0x00a3;
    L_0x0039:
        r9 = r14.getNumOfColPerPage();
        r8 = r14.getNumOfRowPerPage();
    L_0x0041:
        r11 = r14.mAdapter;
        r3 = r11.getItemWidth();
        r11 = r14.mAdapter;
        r1 = r11.getItemHeight();
        r11 = r14.mHorizontalSpace;
        r11 = r11 + r3;
        r4 = (float) r11;
        r11 = r14.mVerticalSpace;
        r11 = r11 + r1;
        r2 = (float) r11;
        r11 = 0;
        r14.setFocusedItem(r5, r11);
        if (r5 < r10) goto L_0x00ac;
    L_0x005b:
        if (r5 >= r0) goto L_0x00ac;
    L_0x005d:
        r11 = r14.getContentLen();
        r12 = r14.mRealRect;
        r12 = r12.width();
        if (r11 <= r12) goto L_0x0089;
    L_0x0069:
        r11 = r14.getContentLen();
        r12 = r14.mRealRect;
        r12 = r12.width();
        r11 = r11 - r12;
        r11 = (float) r11;
        r12 = r14.mScrollX;
        r11 = (r11 > r12 ? 1 : (r11 == r12 ? 0 : -1));
        if (r11 >= 0) goto L_0x0089;
    L_0x007b:
        r11 = r14.getContentLen();
        r12 = r14.mRealRect;
        r12 = r12.width();
        r11 = r11 - r12;
        r11 = (float) r11;
        r14.mScrollX = r11;
    L_0x0089:
        return;
    L_0x008a:
        r11 = r14.mScrollMode;
        r12 = 1;
        if (r11 != r12) goto L_0x001f;
    L_0x008f:
        r11 = r14.mLayoutDetail;
        r11 = r11.startOffset;
        r12 = r14.mAdapter;
        r12 = r12.getItemHeight();
        r11 = r11 % r12;
        if (r11 <= 0) goto L_0x001f;
    L_0x009c:
        r11 = r14.getNumOfColPerPage();
        r10 = r10 + r11;
        goto L_0x001f;
    L_0x00a3:
        r8 = r14.getNumOfColPerPage();
        r9 = r14.getNumOfRowPerPage();
        goto L_0x0041;
    L_0x00ac:
        if (r5 < r10) goto L_0x00b4;
    L_0x00ae:
        r11 = r14.getFloorNumberPrePage();
        if (r5 < r11) goto L_0x0089;
    L_0x00b4:
        r11 = r9 / 2;
        r11 = r11 * r8;
        r6 = r5 - r11;
        if (r6 <= 0) goto L_0x00ed;
    L_0x00bb:
        r11 = r14.mScrollMode;
        if (r11 != 0) goto L_0x00d6;
    L_0x00bf:
        r11 = r14.getContentLen();
        r12 = r14.mRealRect;
        r12 = r12.width();
        r11 = r11 - r12;
        r11 = (float) r11;
        r12 = r6 / r8;
        r12 = (float) r12;
        r12 = r12 * r4;
        r11 = java.lang.Math.min(r11, r12);
        r14.mScrollX = r11;
        goto L_0x0089;
    L_0x00d6:
        r11 = r14.getContentLen();
        r12 = r14.mRealRect;
        r12 = r12.height();
        r11 = r11 - r12;
        r11 = (float) r11;
        r12 = r6 / r8;
        r12 = (float) r12;
        r12 = r12 * r2;
        r11 = java.lang.Math.min(r11, r12);
        r14.mScrollY = r11;
        goto L_0x0089;
    L_0x00ed:
        r14.mScrollX = r13;
        r14.mScrollY = r13;
        goto L_0x0089;
        */
    }

    private void makeFocusVisible(int focusStep) {
        makeFocusVisible(focusStep, true);
    }

    private void makeFocusVisible(int focusStep, boolean showAnimation) {
        int oldFocus = this.mItemFocused;
        int startIndex = computerStartIndex();
        int endIndex = startIndex + getFloorNumberPrePage();
        if (focusStep < 0) {
            if (this.mScrollMode == 0 && this.mLayoutDetail.startOffset % this.mAdapter.getItemWidth() < 0) {
                startIndex += getNumOfRowPerPage();
            } else if (this.mScrollMode == 1 && this.mLayoutDetail.startOffset % this.mAdapter.getItemHeight() < 0) {
                startIndex += getNumOfColPerPage();
            }
        }
        endIndex = Math.min(endIndex, this.mAdapter.getCount() - 1);
        int newFocus = oldFocus + focusStep;
        int itemWidth = this.mAdapter.getItemWidth();
        float itemWidthWithSpace = (float) (this.mHorizontalSpace + itemWidth);
        float itemHeightWithSpace = (float) (this.mVerticalSpace + this.mAdapter.getItemHeight());
        setFocusedItem(newFocus, true);
        if (newFocus >= startIndex && newFocus < endIndex) {
            return;
        }
        int delta;
        float avilable;
        if (this.mScrollMode == 0) {
            int fullVisibleItemsWidth = (int) (((float) ((int) (((float) this.mRealRect.width()) / itemWidthWithSpace))) * itemWidthWithSpace);
            if (newFocus <= startIndex) {
                delta = -Math.abs((int) Math.min((float) fullVisibleItemsWidth, this.mScrollX));
            } else {
                avilable = ((float) (getContentLen() - this.mRealRect.width())) - this.mScrollX;
                if (avilable < 0.0f) {
                    avilable = 0.0f;
                }
                delta = Math.abs((int) Math.min((float) fullVisibleItemsWidth, avilable));
            }
            if (showAnimation) {
                this.mScroller.startScroll(this.mScrollX, 0.0f, (float) delta, 0.0f);
            } else {
                this.mScrollX += (float) delta;
            }
        } else {
            int fullVisibleItemsHeight = (int) (((float) ((int) (((float) this.mRealRect.height()) / itemHeightWithSpace))) * itemHeightWithSpace);
            if (newFocus <= startIndex) {
                delta = -Math.abs((int) Math.min((float) fullVisibleItemsHeight, this.mScrollY));
            } else {
                avilable = ((float) (getContentLen() - this.mRealRect.height())) - this.mScrollY;
                if (avilable < 0.0f) {
                    avilable = 0.0f;
                }
                delta = Math.abs((int) Math.min((float) fullVisibleItemsHeight, avilable));
            }
            if (showAnimation) {
                this.mScroller.startScroll(0.0f, this.mScrollY, 0.0f, (float) delta);
            } else {
                this.mScrollY += (float) delta;
            }
        }
    }

    private boolean moveDragAnimation(int index, GridDrawArgs gridDrawArgs) {
        boolean needRender = DEBUG;
        int dragItemIndex = index - this.mDragAnimationStart;
        if (index >= this.mDragAnimationStart && index <= this.mDragAnimationEnd && dragItemIndex >= 0 && dragItemIndex < this.mDragAnimationXYs.size()) {
            long ct = System.currentTimeMillis();
            DragAnimationXY dragAnimationXY = (DragAnimationXY) this.mDragAnimationXYs.get(dragItemIndex);
            dragAnimationXY.mDeviantX = FloatUtils.animate(dragAnimationXY.mDeviantX, 0.0f, (float) (ct - this.mLastDragTime));
            dragAnimationXY.mDeviantY = FloatUtils.animate(dragAnimationXY.mDeviantY, 0.0f, (float) (ct - this.mLastDragTime));
            if (!(dragAnimationXY.mDeviantX == 0.0f && dragAnimationXY.mDeviantY == 0.0f)) {
                needRender = true;
            }
            this.mLastDragTime = ct;
            gridDrawArgs.itemCenterX += dragAnimationXY.mDeviantX;
            gridDrawArgs.itemCenterY += dragAnimationXY.mDeviantY;
        }
        return needRender;
    }

    private void notifyCountChange() {
        fillGap();
    }

    private boolean onDownKeyPress() {
        boolean ret = true;
        int col = getNumOfColPerPage();
        int row = getNumOfRowPerPage();
        int count = this.mAdapter.getCount();
        int focusMovedSetp = SLIDE_MODE_PLANE;
        if (this.mScrollMode == 0) {
            if ((this.mItemFocused + 1) % row != 0) {
                focusMovedSetp = SLIDE_MODE_ARC;
                ret = true;
            } else if (checkDiscardFoucsVertical()) {
                ret = false;
            }
        } else if (this.mItemFocused + col < count) {
            focusMovedSetp = col;
            ret = true;
        } else if (this.mItemFocused < count - 1) {
            this.mItemFocused = count - 1;
            ret = true;
        } else if (checkDiscardFoucsVertical()) {
            ret = false;
        }
        if (focusMovedSetp != 0) {
            makeFocusVisible(focusMovedSetp);
        }
        return ret;
    }

    private void onDrawBackground(GLCanvas glCanvas, int left, int top, boolean forceDrawSth) {
        if (this.mBackGroundID != -1) {
            if (this.mGLGridBackgroundDrawer == null) {
                this.mBackGround = new BitmapTexture(this.mBackGroundID, this.mRealRect.width(), this.mRealRect.height());
                this.mGLGridBackgroundDrawer = new VertexDrawer(this.mZValue - 0.001f, this.mBackGround);
            }
            this.mGLGridBackgroundDrawer.draw(glCanvas, this.mRealRect.left + left, this.mRealRect.top + top, (int)JsonWriteContext.STATUS_EXPECT_VALUE);
        } else if (forceDrawSth) {
            float f = 0.001f;
            if (this.mSlideMode == 1) {
                calculateCircleZ((float) (this.mRealRect.width() / 2));
                f = (this.mCurrentR - this.mCircleZ) * glCanvas.getPixel2GL(this.mZValue) + 0.001f;
            }
            glCanvas.fillRect(SLIDE_MODE_PLANE, (float) (this.mRealRect.left + left), (float) (this.mRealRect.top + top), this.mZValue - f, (float) this.mRealRect.width(), (float) this.mRealRect.height());
        }
    }

    private void onDrawFlatMode(GLCanvas glCanvas, int left, int top) {
        GL11 gl = glCanvas.getGL();
        float itemW = (float) this.mAdapter.getItemWidth();
        float itemH = (float) this.mAdapter.getItemHeight();
        boolean z = INVALID_POSITION;
        boolean z2 = SLIDE_MODE_PLANE;
        boolean dragContinue = DEBUG;
        boolean diffContinue = DEBUG;
        boolean z3 = SLIDE_MODE_PLANE;
        boolean more = !this.mScroller.isFinished() ? true : DEBUG;
        this.mScroller.computeScrollOffset();
        if (more) {
            int available;
            if (this.mScrollMode == 0) {
                this.mScrollX = (float) this.mScroller.getCurrX();
                if (this.mSlideMode == 1) {
                    if (this.mScroller.isFinished()) {
                        calculationRadiusBySpeed(0.0f);
                    } else {
                        calculationRadiusBySpeed((float) ((int) this.mScroller.getCurrVelocity()));
                    }
                }
                if (this.mSlideMode == 2) {
                    if (this.mScrollX < 0.0f) {
                        this.mTiltArc = (this.mScrollX * 30.0f) / ((float) this.mRealRect.width());
                    } else {
                        available = getContentLen() - this.mRealRect.width();
                        if (available < 0) {
                            available = SLIDE_MODE_PLANE;
                        }
                        if (this.mScrollX > ((float) available)) {
                            this.mTiltArc = ((this.mScrollX - ((float) available)) * 30.0f) / ((float) this.mRealRect.width());
                        } else {
                            this.mTiltArc = 0.0f;
                        }
                    }
                }
            } else {
                this.mScrollY = (float) this.mScroller.getCurrY();
                if (this.mSlideMode == 2) {
                    if (this.mScrollY < 0.0f) {
                        this.mTiltArc = (this.mScrollY * 30.0f) / ((float) this.mRealRect.height());
                    } else {
                        available = getContentLen() - this.mRealRect.height();
                        if (available < 0) {
                            available = SLIDE_MODE_PLANE;
                        }
                        if (this.mScrollY > ((float) available)) {
                            this.mTiltArc = ((this.mScrollY - ((float) available)) * 30.0f) / ((float) this.mRealRect.height());
                        } else {
                            this.mTiltArc = 0.0f;
                        }
                    }
                }
            }
            this.mOnShowScrollBar = true;
        } else {
            if (this.mSlideMode == 1) {
                calculationRadiusBySpeed(0.0f);
            }
            this.mOnShowScrollBar = false;
        }
        if (this.mSlideMode == 2) {
            gl.glTranslatef(0.0f, 0.0f, this.mZValue);
            if (this.mScrollMode == 0) {
                gl.glRotatef(-this.mTiltArc, 0.0f, 1.0f, 0.0f);
            } else {
                gl.glRotatef(-this.mTiltArc, 1.0f, 0.0f, 0.0f);
            }
            gl.glTranslatef(0.0f, 0.0f, -this.mZValue);
        }
        onDrawBackground(glCanvas, left, top, DEBUG);
        computerItemsLayoutDetail();
        int startIndex = this.mLayoutDetail.startIndex;
        float offset = (float) this.mLayoutDetail.startOffset;
        float itemCenterX = 0.0f;
        float glZ = this.mZValue;
        int count = this.mAdapter.getCount();
        int xNum = getNumOfColPerPage();
        int yNum = getNumOfRowPerPage();
        int xLoopCount = xNum;
        int yLoopCount = yNum;
        if (this.mScrollMode == 0) {
            startIndex *= yNum;
            if (((float) xLoopCount) * (((float) this.mHorizontalSpace) + itemW) + offset < ((float) this.mRealRect.width())) {
                xLoopCount++;
            }
        } else if (this.mScrollMode == 1) {
            startIndex *= xNum;
            if (((float) yLoopCount) * (((float) this.mVerticalSpace) + itemH) + offset < ((float) this.mRealRect.height())) {
                yLoopCount++;
            }
        }
        if (this.mDiffAnimationMark) {
            Iterator i$ = this.mDiffList.iterator();
            while (i$.hasNext()) {
                ItemAnimation iAnimation = (ItemAnimation) i$.next();
                iAnimation.computeOffset();
                diffContinue |= iAnimation.isValid();
            }
        }
        this.mGridDrawArgs.canvas = glCanvas;
        this.mGridDrawArgs.gl = glCanvas.getGL();
        this.mGridDrawArgs.tm = glCanvas.getTextureManager();
        this.mGridDrawArgs.itemWidth = (float) this.mAdapter.getItemWidth();
        this.mGridDrawArgs.itemHeight = (float) this.mAdapter.getItemHeight();
        float halfItemWidth = itemW / 2.0f;
        float halfItemHeight = itemH / 2.0f;
        if (this.mReflectionHeight > 0.0f) {
            gl.glBlendFunc(770, 771);
        }
        int i = SLIDE_MODE_PLANE;
        while (i < xLoopCount) {
            if (!(this.mSlideMode == 1 && this.mScrollMode == 0 && this.mCurrentR < 100000.0f)) {
                itemCenterX = halfItemWidth + ((float) i) * (((float) this.mHorizontalSpace) + itemW);
            }
            if (this.mScrollMode == 0) {
                if (this.mSlideMode != 1 || this.mCurrentR >= 100000.0f) {
                    itemCenterX += offset;
                }
            }
            int j = SLIDE_MODE_PLANE;
            while (j < yLoopCount) {
                float itemCenterY = halfItemHeight + ((float) j) * (((float) this.mVerticalSpace) + itemH);
                if (this.mScrollMode == 1) {
                    itemCenterY += offset;
                }
                int index = yNum * i + startIndex + j;
                if (this.mScrollMode == 1) {
                    index = xNum * j + startIndex + i;
                }
                if (index < count) {
                    ItemAnimation itemAnimation;
                    this.mGridDrawArgs.itemIndex = index;
                    this.mGridDrawArgs.itemPartIndex = 0;
                    GridDrawArgs gridDrawArgs = this.mGridDrawArgs;
                    boolean z4 = (index != this.mItemTouchDowm || this.mItemTouchDowm < 0) ? DEBUG : true;
                    gridDrawArgs.isTouchDowm = z4;
                    gridDrawArgs = this.mGridDrawArgs;
                    z4 = (index == this.mItemFocused && isFocused()) ? true : DEBUG;
                    gridDrawArgs.isFocused = z4;
                    gridDrawArgs = this.mGridDrawArgs;
                    z4 = (this.mChoiceMode == 0 || !this.mCheckStates.get(index)) ? DEBUG : true;
                    gridDrawArgs.isChecked = z4;
                    this.mGridDrawArgs.itemCenterX = ((float) (this.mPaddingRect.left + left)) + itemCenterX;
                    this.mGridDrawArgs.itemCenterY = ((float) (this.mPaddingRect.top + top)) + itemCenterY;
                    if (this.mDragAnimationMart) {
                        dragContinue |= moveDragAnimation(index, this.mGridDrawArgs);
                    }
                    this.mGridDrawArgs.itemLeft = this.mGridDrawArgs.itemCenterX - halfItemWidth;
                    this.mGridDrawArgs.itemTop = this.mGridDrawArgs.itemCenterY - halfItemHeight;
                    this.mGridDrawArgs.isFastScroll = !this.mScroller.isFinished() ? true : DEBUG;
                    if (index == this.mItemFocused) {
                        if (this.mOldItemFocus >= 0 && this.mFocusChangedByKeyBoard) {
                            this.mFocusChangedByKeyBoard = false;
                            this.mGridDrawArgs.moveFocus2CurPos(this.mGridDrawArgs.itemCenterX - ((float) (this.mAdapter.getItemWidth() / 2)), this.mGridDrawArgs.itemCenterY - ((float) (this.mAdapter.getItemHeight() / 2)));
                        }
                        this.mGridDrawArgs.recodeOldFocusGLPos(this.mGridDrawArgs.itemCenterX - ((float) (this.mAdapter.getItemWidth() / 2)), this.mGridDrawArgs.itemCenterY - ((float) (this.mAdapter.getItemHeight() / 2)));
                    }
                    if (this.mSlideMode == 1 && this.mScrollMode == 0 && this.mCurrentR < 100000.0f) {
                        calculateArcVer(offset, i, itemW, this.mPaddingRect.left + left, this.mPaddingRect.top + top);
                    }
                    if (this.mDiffAnimationMark) {
                        if (index < this.mDiffList.size()) {
                            itemAnimation = this.mDiffList.get(index);
                            this.mGridDrawArgs.gl.glTranslatef(0.0f, 0.0f, this.mZValue);
                            itemAnimation.updateApply(glCanvas, 0);
                            this.mGridDrawArgs.gl.glTranslatef(-0.0f, -0.0f, -this.mZValue);
                        } else {
                            itemAnimation = null;
                        }
                    }
                    if (this.mCrossfadeRatio >= 0.0f && this.mSlideMode != 1) {
                        float colors;
                        int crossfadeDirection = INVALID_POSITION;
                        if (this.mScrollMode == 0) {
                            if (itemCenterX < halfItemWidth - this.mCrossfadeRatio * itemW) {
                                crossfadeDirection = SLIDE_MODE_PLANE;
                                colors = Math.abs((itemCenterX - halfItemWidth) + (this.mCrossfadeRatio * itemW)) / ((1.0f - this.mCrossfadeRatio) * itemW);
                            } else if (itemCenterX > ((float) this.mRealRect.width()) + this.mCrossfadeRatio * itemW - halfItemWidth) {
                                crossfadeDirection = SLIDE_MODE_TILT;
                                colors = Math.abs(((itemCenterX - ((float) this.mRealRect.width())) - (this.mCrossfadeRatio * itemW)) + halfItemWidth) / ((1.0f - this.mCrossfadeRatio) * itemW);
                            }
                        } else if (this.mScrollMode == 1) {
                            if (itemCenterY < halfItemHeight - this.mCrossfadeRatio * itemH) {
                                crossfadeDirection = SLIDE_MODE_ARC;
                                colors = Math.abs((itemCenterY - halfItemHeight) + (this.mCrossfadeRatio * itemH)) / ((1.0f - this.mCrossfadeRatio) * itemH);
                            } else if (itemCenterY > ((float) this.mRealRect.height()) + this.mCrossfadeRatio * itemH - halfItemHeight) {
                                crossfadeDirection = CROSSFADE_BOTTOM;
                                colors = Math.abs(((itemCenterY - ((float) this.mRealRect.height())) - (this.mCrossfadeRatio * itemH)) + halfItemHeight) / ((1.0f - this.mCrossfadeRatio) * itemH);
                            }
                        }
                        if (-1 != crossfadeDirection) {
                            if (colors > 1.0f) {
                                colors = 1.0f;
                            }
                            this.mColorsBuffer = calculateColorsBuffer(colors, this.mColorsBuffer, crossfadeDirection);
                            gl.glEnableClientState(32886);
                            gl.glColorPointer(JsonWriteContext.STATUS_EXPECT_VALUE, 5126, SLIDE_MODE_PLANE, this.mColorsBuffer);
                        }
                    }
                    this.mRequestRender |= this.mAdapter.drawItem(this.mGridDrawArgs);
                    if (this.mReflectionHeight > 0.0f && yLoopCount - 1 == j) {
                        this.mGridDrawArgs.itemCenterY = this.mGridDrawArgs.itemCenterY + itemH + ((float) (this.mVerticalSpace * 2));
                        this.mGridDrawArgs.itemTop = this.mGridDrawArgs.itemCenterY - halfItemHeight;
                        Utils.startGLReflection(glCanvas, this.mGridDrawArgs.itemCenterX, this.mGridDrawArgs.itemCenterY, this.mZValue, null);
                        this.mRequestRender |= this.mAdapter.drawItem(this.mGridDrawArgs);
                        Utils.endGLReflection(gl);
                    }
                    if (this.mCrossfadeRatio >= 0.0f && -1 != crossfadeDirection) {
                        gl.glDisableClientState(32886);
                    }
                    if (this.mDiffAnimationMark && itemAnimation != null) {
                        this.mGridDrawArgs.gl.glTranslatef(0.0f, 0.0f, this.mZValue);
                        itemAnimation.restoreGL(glCanvas);
                        this.mGridDrawArgs.gl.glTranslatef(-0.0f, -0.0f, -this.mZValue);
                    }
                }
                j++;
            }
            i++;
        }
        this.mDragAnimationMart = dragContinue;
        this.mDiffAnimationMark = diffContinue;
        if (this.mRequestRender || dragContinue || diffContinue || !this.mScroller.isFinished()) {
            this.mRequestRender = false;
            invalidate();
        }
        if (this.mSlideMode == 2) {
            gl.glTranslatef(0.0f, 0.0f, this.mZValue);
            if (this.mScrollMode == 0) {
                gl.glRotatef(this.mTiltArc, 0.0f, 1.0f, 0.0f);
            } else {
                gl.glRotatef(this.mTiltArc, 1.0f, 0.0f, 0.0f);
            }
            gl.glTranslatef(0.0f, 0.0f, -this.mZValue);
        }
    }

    private void onDrawScrollBar(GLCanvas glCanvas, int left, int top) {
        int tmumbPosX = SLIDE_MODE_PLANE;
        int tmumbPosY = SLIDE_MODE_PLANE;
        int totalLen = getContentLen();
        if (this.mScrollMode == 0) {
            tmumbPosX = (int) ((this.mScrollX / ((float) totalLen)) * ((float) this.mRealRect.width()));
        } else {
            tmumbPosY = (int) ((this.mScrollY / ((float) totalLen)) * ((float) this.mRealRect.height()));
        }
        initScrollBarDrawer(left, top);
        if (this.mScrollBarTrackDrawer != null) {
            glCanvas.draw2D(this.mScrollBarTrackDrawer.getTexture(), (float) this.mScrollBarTrackDrawerRect.left, (float) this.mScrollBarTrackDrawerRect.top);
            glCanvas.draw2D(this.mScrollBarThumbDrawer.getTexture(), (float) (this.mScrollBarThumbDrawerRect.left + tmumbPosX), (float) (this.mScrollBarThumbDrawerRect.top + tmumbPosY));
        }
    }

    private boolean onLeftKeyPress() {
        boolean ret = true;
        int col = getNumOfColPerPage();
        int row = getNumOfRowPerPage();
        int focusMovedSetp = SLIDE_MODE_PLANE;
        if (this.mScrollMode == 0) {
            if (this.mItemFocused - row >= 0) {
                focusMovedSetp = -row;
                ret = true;
            } else if (checkDiscardFoucsHorizontal()) {
                ret = false;
            }
        } else if (this.mItemFocused % col != 0) {
            focusMovedSetp = INVALID_POSITION;
            ret = true;
        } else if (checkDiscardFoucsHorizontal()) {
            ret = false;
        }
        if (focusMovedSetp == 0) {
            this.mScroller.startScroll(this.mScrollX, this.mScrollY, -this.mScrollX, 0.0f);
        } else {
            makeFocusVisible(focusMovedSetp);
        }
        return ret;
    }

    private boolean onMenuKeyPress() {
        if (this.mItemFocused < 0 || !this.mCanShowItemMenu) {
            return DEBUG;
        }
        showPopupMenu();
        return true;
    }

    private void onPageKeyPress(boolean down) {
        int totalLen = getContentLen();
        if ((totalLen >= this.mRealRect.width() || this.mScrollMode != 0) && totalLen >= this.mRealRect.height()) {
            int focusMovedSetp;
            if (!this.mScroller.isFinished()) {
                this.mScroller.abortAnimation();
                this.mScrollX = (float) this.mScroller.getCurrX();
                this.mScrollY = (float) this.mScroller.getCurrY();
            }
            int startIndex = computerStartIndex();
            int endIndex = startIndex + getCeilNumberPrePage();
            int curFocus = Math.max(this.mItemFocused, startIndex);
            if (down) {
                focusMovedSetp = endIndex - curFocus;
            } else {
                focusMovedSetp = startIndex - this.mItemFocused - 1;
            }
            makeFocusVisible(focusMovedSetp);
        }
    }

    private boolean onRightKeyPress() {
        boolean ret = true;
        int col = getNumOfColPerPage();
        int row = getNumOfRowPerPage();
        int count = this.mAdapter.getCount();
        int focusMovedSetp = SLIDE_MODE_PLANE;
        if (this.mScrollMode == 0) {
            if (this.mItemFocused + row < count) {
                focusMovedSetp = row;
                ret = true;
            } else if (this.mItemFocused < count - 1) {
                focusMovedSetp = INVALID_POSITION;
                ret = true;
            } else if (checkDiscardFoucsHorizontal()) {
                ret = false;
            }
        } else if ((this.mItemFocused + 1) % col != 0) {
            focusMovedSetp = SLIDE_MODE_ARC;
            ret = DEBUG;
        } else if (checkDiscardFoucsHorizontal()) {
            ret = false;
        }
        if (focusMovedSetp != 0) {
            makeFocusVisible(focusMovedSetp);
        }
        return ret;
    }

    private boolean onUpKeyPress() {
        boolean ret = true;
        int col = getNumOfColPerPage();
        int row = getNumOfRowPerPage();
        int focusMovedSetp = SLIDE_MODE_PLANE;
        if (this.mScrollMode == 0) {
            if (this.mItemFocused % row != 0) {
                focusMovedSetp = INVALID_POSITION;
                ret = true;
            } else if (checkDiscardFoucsVertical()) {
                ret = false;
            }
        } else if (this.mItemFocused - col >= 0) {
            focusMovedSetp = -col;
            ret = true;
        } else if (checkDiscardFoucsVertical()) {
            ret = false;
        }
        if (focusMovedSetp == 0) {
            this.mScroller.startScroll(this.mScrollX, this.mScrollY, 0.0f, -this.mScrollY);
        } else {
            makeFocusVisible(focusMovedSetp);
        }
        return ret;
    }

    private boolean performItemClick(int position) {
        boolean newValue = DEBUG;
        boolean handled = DEBUG;
        if (this.mChoiceMode != 0) {
            handled = true;
            if (this.mChoiceMode == 2) {
                if (!this.mCheckStates.get(position, DEBUG)) {
                    newValue = true;
                }
                this.mCheckStates.put(position, newValue);
                if (this.mCheckedIdStates != null) {
                    if (newValue) {
                        this.mCheckedIdStates.put(position, Boolean.TRUE);
                    } else {
                        this.mCheckedIdStates.delete(position);
                    }
                }
            } else {
                if (!this.mCheckStates.get(position, DEBUG)) {
                    newValue = true;
                }
                if (newValue) {
                    this.mCheckStates.clear();
                    this.mCheckStates.put(position, true);
                    if (this.mCheckedIdStates != null) {
                        this.mCheckedIdStates.clear();
                        this.mCheckedIdStates.put(position, Boolean.TRUE);
                    }
                }
            }
        }
        return handled;
    }

    private void putFloatBufferFour(FloatBuffer buffer, float src) {
        buffer.put(src);
        buffer.put(src);
        buffer.put(src);
        buffer.put(src);
    }

    private void setFocusedItem(int itemFocused, boolean isKeyEvent) {
        if (itemFocused >= 0 && isKeyEvent) {
            this.mFocusChangedByKeyBoard = true;
        }
        if (itemFocused < this.mAdapter.getCount() && itemFocused >= 0) {
            this.mOldItemFocus = this.mItemFocused;
            this.mItemTouchDowm = -1;
            this.mItemFocused = itemFocused;
            if (this.mAdapter != null && this.mItemChangeListener != null) {
                this.mItemChangeListener.onItemFocusChange(this.mOldItemFocus, this.mItemFocused);
            }
        }
    }

    private void setSelectedItem(int index, boolean isShowAnimation) {
        if (this.mAdapter != null && this.mRealRect.width() != 0 && this.mRealRect.height() != 0 && index < this.mAdapter.getCount() && index >= 0) {
            int startIndex = computerStartIndex();
            if (this.mItemFocused == -1) {
                this.mItemFocused = startIndex;
            }
            makeFocusCenterVisible(index - this.mItemFocused);
        }
    }

    private void showPopupMenu() {
        if (this.mUnableShowMenuItems != null) {
            int n = this.mUnableShowMenuItems.size();
            int i = SLIDE_MODE_PLANE;
            while (i < n) {
                if (getSelectedItem() != ((Integer) this.mUnableShowMenuItems.get(i)).intValue()) {
                    i++;
                } else {
                    return;
                }
            }
        }
        float[] coord = new float[2];
        findItemXY(this.mItemFocused, coord);
        float menuX = coord[0] + ((float) (this.mRealRect.width() / 2)) + ((float) getPositionX());
        float menuY = (-coord[1]) + ((float) (this.mRealRect.height() / 2)) + ((float) getPositionX());
        if (this.mPopupMenu != null) {
            this.mPopupMenu.setPosition(menuX, menuY);
            this.mPopupMenu.showAtPoint((int) menuX, (int) menuY, this.mRealRect.width(), this.mRealRect.height());
        } else {
            throw new IllegalArgumentException("call setPopupMenu before showPopupMenu !!!");
        }
    }

    private void snapScreen(int velocityX, int velocityY) {
        if (this.mAdapter != null && this.mAdapter.getCount() > 0) {
            int offset = Math.min(MAX_BOUNCE_LEN, this.mRealRect.width() / 2);
            int totalLen;
            int available;
            if (this.mScrollMode == 1) {
                offset = Math.min(MAX_BOUNCE_LEN, this.mRealRect.height() / 2);
                if (velocityY > 200) {
                    if (this.mScrollY < 0.0f) {
                        this.mScroller.startScroll((int)SLIDE_MODE_PLANE, (int) this.mScrollY, (int)SLIDE_MODE_PLANE, -((int) this.mScrollY));
                    } else {
                        this.mScroller.fling(SLIDE_MODE_PLANE, (int) this.mScrollY, SLIDE_MODE_PLANE, -velocityY, SLIDE_MODE_PLANE, SLIDE_MODE_PLANE, -offset, (int) this.mScrollY);
                    }
                } else if (velocityY < -200) {
                    totalLen = getContentLen();
                    if (totalLen < this.mRealRect.height()) {
                        this.mScroller.startScroll(0.0f, this.mScrollY, 0.0f, -this.mScrollY);
                    } else {
                        this.mScroller.fling(SLIDE_MODE_PLANE, (int) this.mScrollY, SLIDE_MODE_PLANE, -velocityY, SLIDE_MODE_PLANE, SLIDE_MODE_PLANE, SLIDE_MODE_PLANE, totalLen - this.mRealRect.height() + offset);
                    }
                } else if (this.mScrollY < 0.0f) {
                    this.mScroller.startScroll(0.0f, this.mScrollY, 0.0f, ((float) (this.mCurrentScreen * this.mRealRect.height())) - this.mScrollY);
                } else {
                    available = getContentLen() - this.mRealRect.height();
                    if (available < 0) {
                        available = SLIDE_MODE_PLANE;
                    }
                    if (this.mScrollY > ((float) available)) {
                        this.mScroller.startScroll(0.0f, this.mScrollY, 0.0f, ((float) available) - this.mScrollY);
                    }
                }
            } else if (velocityX > 200) {
                if (this.mScrollX < 0.0f) {
                    this.mScroller.startScroll((int) this.mScrollX, (int)SLIDE_MODE_PLANE, -((int) this.mScrollX), (int)SLIDE_MODE_PLANE);
                } else {
                    this.mScroller.fling((int) this.mScrollX, SLIDE_MODE_PLANE, -velocityX, SLIDE_MODE_PLANE, -offset, (int) this.mScrollX, SLIDE_MODE_PLANE, SLIDE_MODE_PLANE);
                }
            } else if (velocityX < -200) {
                totalLen = getContentLen();
                if (totalLen < this.mRealRect.width()) {
                    this.mScroller.startScroll(this.mScrollX, 0.0f, -this.mScrollX, 0.0f);
                } else {
                    this.mScroller.fling((int) this.mScrollX, SLIDE_MODE_PLANE, -velocityX, SLIDE_MODE_PLANE, SLIDE_MODE_PLANE, totalLen - this.mRealRect.width() + offset, SLIDE_MODE_PLANE, SLIDE_MODE_PLANE);
                }
            } else if (this.mScrollX < 0.0f) {
                this.mScroller.startScroll(this.mScrollX, 0.0f, ((float) (this.mCurrentScreen * this.mRealRect.width())) - this.mScrollX, 0.0f);
            } else {
                available = getContentLen() - this.mRealRect.width();
                if (available < 0) {
                    available = SLIDE_MODE_PLANE;
                }
                if (this.mScrollX > ((float) available)) {
                    this.mScroller.startScroll(this.mScrollX, 0.0f, ((float) available) - this.mScrollX, 0.0f);
                }
            }
        }
    }

    private int transformSpeed(int speed) {
        if (speed > 1500 && speed < 3000) {
            return (speed - 1500) / 2 + 1500;
        }
        if (speed > 3000) {
            return (speed - 3000) / 3 + 2250;
        }
        if (speed >= -1500 || speed <= -3000) {
            return speed < -3000 ? (speed + 3000) / 3 - 2250 : speed;
        } else {
            return (speed + 1500) / 2 - 1500;
        }
    }

    public void calculateCircleZ(float r) {
        this.mCircleZ = (float) Math.sqrt((double) (this.mCurrentR * this.mCurrentR - r * r));
    }

    public void changeAdapterSort(IGLGridAdapter adapter) {
        this.mAdapter = adapter;
    }

    boolean checkDiscardFoucsHorizontal() {
        return this.mDiscardFoucsHorizontal ? true : DEBUG;
    }

    boolean checkDiscardFoucsVertical() {
        return this.mDiscardFoucsVertical ? true : DEBUG;
    }

    public void clearChoices() {
        if (this.mCheckStates != null) {
            this.mCheckStates.clear();
        }
    }

    public void disableDiscardFoucsHorizontal(boolean disable) {
        this.mDiscardFoucsHorizontal = disable;
    }

    public void disableDiscardFoucsVertical(boolean disable) {
        this.mDiscardFoucsVertical = disable;
    }

    public void disableItemMenu() {
        this.mCanShowItemMenu = false;
    }

    public void enableItemMenu(String[] items, Listener listener, ArrayList<Integer> unableItems) {
        this.mCanShowItemMenu = true;
        this.mUnableShowMenuItems = unableItems;
        int n = items.length;
        Option[] mOptions = new Option[n];
        int i = SLIDE_MODE_PLANE;
        while (i < n) {
            mOptions[i] = new Option(items[i], null, new Runnable() {
                public void run() {
                    if (GLGridView.this.mPopupMenu != null) {
                        GLGridView.this.mPopupMenu.close(true);
                    }
                }
            }, false);
            i++;
        }
        if (this.mPopupMenu != null) {
            this.mPopupMenu.setOptions(mOptions);
            this.mPopupMenu.setListener(listener);
        } else {
            throw new IllegalArgumentException("call setPopupMenu before call enableItemMenu");
        }
    }

    public void findItemXY(int index, float[] coord) {
        if (coord.length >= 2 && this.mAdapter != null) {
            computerItemsLayoutDetail();
            int startIndex = this.mLayoutDetail.startIndex;
            int offset = this.mLayoutDetail.startOffset;
            int xNum = getNumOfColPerPage();
            int yNum = getNumOfRowPerPage();
            float itemW = (float) this.mAdapter.getItemWidth();
            float itemH = (float) this.mAdapter.getItemHeight();
            int xLoopCount = xNum;
            int yLoopCount = yNum;
            if (this.mScrollMode == 0) {
                startIndex *= yNum;
                xLoopCount++;
            } else if (this.mScrollMode == 1) {
                startIndex *= xNum;
                yLoopCount++;
            }
            float halfItemWidth = itemW / 2.0f;
            float halfItemHeight = itemH / 2.0f;
            int i = SLIDE_MODE_PLANE;
            while (i < xLoopCount) {
                float itemCenterX = ((float) this.mRealRect.left) + halfItemWidth + ((float) i) * (((float) this.mHorizontalSpace) + itemW);
                if (this.mScrollMode == 0) {
                    itemCenterX += (float) offset;
                }
                int j = SLIDE_MODE_PLANE;
                while (j < yLoopCount) {
                    float itemCenterY = ((float) this.mRealRect.top) + ((float) j) * (((float) this.mVerticalSpace) + itemH) + halfItemHeight;
                    if (this.mScrollMode == 1) {
                        itemCenterY += (float) offset;
                    }
                    int proIndex = yNum * i + startIndex + j;
                    if (this.mScrollMode == 1) {
                        proIndex = xNum * j + startIndex + i;
                    }
                    if (index == proIndex) {
                        coord[0] = itemCenterX;
                        coord[1] = itemCenterY;
                        return;
                    } else {
                        j++;
                    }
                }
                i++;
            }
        }
    }

    protected int findTouchItem(float x, float y) {
        if (!this.mRealRect.contains((int) x, (int) y) || this.mAdapter == null) {
            return INVALID_POSITION;
        }
        computerItemsLayoutDetail();
        int startIndex = this.mLayoutDetail.startIndex;
        int offset = this.mLayoutDetail.startOffset;
        float myCenterX = (float) (this.mRealRect.width() / 2);
        float myCenterY = (float) (this.mRealRect.height() / 2);
        int count = this.mAdapter.getCount();
        int xNum = getNumOfColPerPage();
        int yNum = getNumOfRowPerPage();
        float itemW = (float) this.mAdapter.getItemWidth();
        float itemH = (float) this.mAdapter.getItemHeight();
        RectF rc = new RectF();
        int xLoopCount = xNum;
        int yLoopCount = yNum;
        if (this.mScrollMode == 0) {
            startIndex *= yNum;
            xLoopCount++;
        } else if (this.mScrollMode == 1) {
            startIndex *= xNum;
            yLoopCount++;
        }
        float halfItemWidth = itemW / 2.0f;
        float halfItemHeight = itemH / 2.0f;
        int i = SLIDE_MODE_PLANE;
        while (i < xLoopCount) {
            float itemCenterX = halfItemWidth + ((float) i) * (((float) this.mHorizontalSpace) + itemW);
            if (this.mScrollMode == 0) {
                itemCenterX += (float) offset;
            }
            float xLen2MyCenter = itemCenterX - myCenterX;
            int j = SLIDE_MODE_PLANE;
            while (j < yLoopCount) {
                float itemCenterY = halfItemHeight + ((float) j) * (((float) this.mVerticalSpace) + itemH);
                if (this.mScrollMode == 1) {
                    itemCenterY += (float) offset;
                }
                float yLen2MyCenter = myCenterY - itemCenterY;
                int index = yNum * i + startIndex + j;
                if (this.mScrollMode == 1) {
                    index = xNum * j + startIndex + i;
                }
                rc.set(itemCenterX - halfItemWidth, itemCenterY - halfItemHeight, itemCenterX + halfItemWidth, itemCenterY + halfItemHeight);
                rc.offset((float) this.mRealRect.left, (float) this.mRealRect.top);
                this.mGridDrawArgs.itemIndex = index;
                if (index < count) {
                    boolean hit;
                    this.mHitTestArgs.itemRect = rc;
                    this.mHitTestArgs.mTouchX = x;
                    this.mHitTestArgs.mTouchY = y;
                    this.mHitTestArgs.itemIndex = index;
                    int ret = this.mAdapter.hitTest(this.mHitTestArgs);
                    if (ret > 0) {
                        hit = true;
                    } else {
                        hit = ret < 0 ? DEBUG : pointInItem(index, xLen2MyCenter, yLen2MyCenter, rc, x, y);
                    }
                    if (hit) {
                        return index;
                    }
                }
                j++;
            }
            i++;
        }
        return INVALID_POSITION;
    }

    float getAngleByLen(float len, float r) {
        return (float) (2.0d * Math.asin((double) (len / (2.0f * r))));
    }

    public int getCeilNumberPrePage() {
        return getNumOfRowPerPage() * getNumOfColPerPage();
    }

    public long[] getCheckedItemIndexs() {
        if (this.mChoiceMode == 0 || this.mCheckedIdStates == null || this.mAdapter == null) {
            return new long[0];
        }
        SparseArray<Boolean> idStates = this.mCheckedIdStates;
        int count = idStates.size();
        long[] ids = new long[count];
        int i = SLIDE_MODE_PLANE;
        while (i < count) {
            ids[i] = (long) idStates.keyAt(i);
            i++;
        }
        return ids;
    }

    public int getCheckedItemPosition() {
        return (this.mChoiceMode == 1 && this.mCheckStates != null && this.mCheckStates.size() == 1) ? this.mCheckStates.keyAt(SLIDE_MODE_PLANE) : INVALID_POSITION;
    }

    public SparseBooleanArray getCheckedItemPositions() {
        return this.mChoiceMode != 0 ? this.mCheckStates : null;
    }

    public int getChoiceMode() {
        return this.mChoiceMode;
    }

    public int getContentLen() {
        if (this.mAdapter == null) {
            return SLIDE_MODE_PLANE;
        }
        if (this.mScrollMode == 0) {
            int itemWidth = this.mAdapter.getItemWidth();
            int rows = getNumOfRowPerPage();
            return (this.mHorizontalSpace + itemWidth) * (((this.mAdapter.getCount() + rows) - 1) / rows);
        } else {
            int itemHeight = this.mAdapter.getItemHeight();
            int clos = getNumOfColPerPage();
            return (this.mVerticalSpace + itemHeight) * (((this.mAdapter.getCount() + clos) - 1) / clos);
        }
    }

    public int getFirstItemIndex() {
        return computerStartIndex();
    }

    public int getFloorNumberPrePage() {
        int yNum;
        int itemH = this.mAdapter.getItemHeight();
        int itemW = this.mAdapter.getItemWidth();
        if (this.mSingleLine) {
            yNum = SLIDE_MODE_ARC;
        } else {
            yNum = this.mRealRect.height() / (this.mVerticalSpace + itemH);
            if (yNum < 1) {
                yNum = 1;
            }
        }
        int xNum = this.mRealRect.width() / (this.mHorizontalSpace + itemW);
        if (xNum < 1) {
            xNum = 1;
        }
        return xNum * yNum;
    }

    public Rect getFocusReferenceRect() {
        return this.mFocusRect;
    }

    public IGLGridAdapter getGridAdapter() {
        return this.mAdapter;
    }

    public int getHorizontalSpace() {
        return this.mHorizontalSpace;
    }

    public ArrayList<ItemAnimation> getItemAnimationGroup(int count, float centerX, float centerY) {
        int startIndex = computerStartIndex();
        int totalCount = this.mAdapter.getCount();
        ArrayList<ItemAnimation> groups = new ArrayList();
        float[] coord = new float[2];
        int i = startIndex;
        while (i < count + startIndex && i < totalCount) {
            findItemXY(i, coord);
            groups.add(new ItemAnimation(coord[0] - centerX, 0.0f, coord[1] - centerY, 0.0f, this.mZValue));
            i++;
        }
        return groups;
    }

    public int getItemPartCount() {
        return this.mItemPartCount;
    }

    public int getNumOfColPerPage() {
        float num;
        float itemW = (float) this.mAdapter.getItemWidth();
        if (this.mSlideMode == 1) {
            num = getAngleByLen((float) this.mRealRect.width(), this.mCurrentR) / getAngleByLen(((float) this.mHorizontalSpace) + itemW, this.mCurrentR);
        } else {
            num = ((float) this.mRealRect.width()) / (((float) this.mHorizontalSpace) + itemW);
        }
        int xNum = (int) num;
        if (this.mScrollMode == 0) {
            xNum = (int) Math.ceil((double) num);
        } else {
            xNum = (int) num;
        }
        return xNum == 0 ? SLIDE_MODE_ARC : xNum;
    }

    public int getNumOfEndCol() {
        int count = this.mAdapter.getCount();
        int num = count % getNumOfRowPerPage();
        return (count == 0 || num != 0) ? num : getNumOfColPerPage();
    }

    public int getNumOfEndRow() {
        int count = this.mAdapter.getCount();
        int num = count % getNumOfColPerPage();
        return (count == 0 || num != 0) ? num : getNumOfRowPerPage();
    }

    public int getNumOfRowPerPage() {
        if (this.mSingleLine) {
            return SLIDE_MODE_ARC;
        }
        int yNum;
        float num = ((float) this.mRealRect.height()) / (((float) this.mVerticalSpace) + ((float) this.mAdapter.getItemHeight()));
        if (this.mScrollMode == 0) {
            yNum = (int) num;
        } else {
            yNum = (int) Math.ceil((double) num);
        }
        return yNum == 0 ? SLIDE_MODE_ARC : yNum;
    }

    public IOnItemClickListener getOnItemClickListener() {
        return this.mIOnItemClickListener;
    }

    public IOnItemLongClickListener getOnItemLongClickListener() {
        return this.mIOnItemLongClickListener;
    }

    public Rect getPaddingRect() {
        return this.mPaddingRect;
    }

    public int getScrollMode() {
        return this.mScrollMode;
    }

    public float getScrollY() {
        return this.mScrollY;
    }

    public int getSelectedItem() {
        return this.mItemFocused;
    }

    public int getSlideMode() {
        return this.mSlideMode;
    }

    public int getStartIndex() {
        return this.mLayoutDetail.startIndex;
    }

    public int getVerticalSpace() {
        return this.mVerticalSpace;
    }

    public boolean isItemChecked(int position) {
        return (this.mChoiceMode == 0 || this.mCheckStates == null) ? DEBUG : this.mCheckStates.get(position);
    }

    public void moveFocus(FocusDirection direction) {
        switch (AnonymousClass_4.$SwitchMap$com$wmt$opengl$grid$GLGridView$FocusDirection[direction.ordinal()]) {
            case SLIDE_MODE_ARC:
                onLeftKeyPress();
            case SLIDE_MODE_TILT:
                onRightKeyPress();
            case CROSSFADE_BOTTOM:
                onUpKeyPress();
            case JsonWriteContext.STATUS_EXPECT_VALUE:
                onDownKeyPress();
            default:
                break;
        }
    }

    public void onDragUp(int x, int y) {
        int from = this.mLastDownItem;
        int to = findTouchItem((float) x, (float) y);
        if (from != -1 && to != -1 && from != to && this.mAdapter.dragItem(from, to)) {
            this.mDragAnimationMart = true;
            if (from > to) {
                this.mDragAnimationStart = to + 1;
                this.mDragAnimationEnd = from;
                this.mDragAnimationUp = false;
            } else {
                this.mDragAnimationStart = from;
                this.mDragAnimationEnd = to - 1;
                this.mDragAnimationUp = true;
            }
            this.mDragAnimationXYs.clear();
            int i = this.mDragAnimationStart;
            while (i <= this.mDragAnimationEnd) {
                DragAnimationXY dragAnimationXY;
                if (this.mDragAnimationUp) {
                    dragAnimationXY = getAnimationXY(i + 1, i);
                } else {
                    dragAnimationXY = getAnimationXY(i - 1, i);
                }
                this.mDragAnimationXYs.add(dragAnimationXY);
                i++;
            }
            this.mLastDragTime = System.currentTimeMillis();
        }
    }

    void onEnterKeyPress() {
        if (this.mIOnItemClickListener != null && this.mItemFocused >= 0 && this.mItemFocused < this.mAdapter.getCount()) {
            float[] coord = new float[2];
            findItemXY(this.mItemFocused, coord);
            performItemClick(this.mItemFocused);
            this.mIOnItemClickListener.onItemClick(this.mItemFocused, coord[0], coord[1]);
        }
    }

    protected void onFocusChanged(boolean getFocus) {
        if (getFocus && this.mItemFocused < 0 && this.mItemTouchDowm < 0) {
            this.mItemFocused = 0;
        }
        super.onFocusChanged(getFocus);
    }

    public boolean onKeyDown(KeyEvent event) {
        if (this.mAdapter == null || this.mAdapter.getCount() == 0) {
            return false;
        }
        this.mItemTouchDowm = -1;
        int xNum = getNumOfColPerPage();
        int yNum = getNumOfRowPerPage();
        int startIndex = computerStartIndex();
        int endIndex = startIndex + getCeilNumberPrePage();
        if (this.mLayoutDetail.startOffset < 0) {
            if (this.mScrollMode == 0) {
                endIndex += yNum;
            } else if (this.mScrollMode == 1) {
                endIndex += xNum;
            }
        }
        if (this.mItemFocused < startIndex || this.mItemFocused >= endIndex) {
            setFocusedItem(startIndex, true);
            return true;
        } else {
            switch (event.getKeyCode()) {
                case TimeUtils.HUNDRED_DAY_FIELD_LEN:
                case Opcodes.V1_7:
                    return event.getRepeatCount() % 4 == 0 ? onUpKeyPress() : true;
                case KeyTouchInputEvent.EV_REP:
                case Opcodes.V1_3:
                    return event.getRepeatCount() % 4 == 0 ? onDownKeyPress() : true;
                case Opcodes.ILOAD:
                case 29:
                    return event.getRepeatCount() % 4 == 0 ? onLeftKeyPress() : true;
                case Opcodes.LLOAD:
                case Opcodes.ACC_SYNCHRONIZED:
                    return event.getRepeatCount() % 4 == 0 ? onRightKeyPress() : true;
                case Opcodes.FLOAD:
                case DataManager.INCLUDE_LOCAL_VIDEO_ONLY:
                    if (event.getRepeatCount() != 0) {
                        return true;
                    }
                    onEnterKeyPress();
                    return true;
                case ConnectionInstance.SERVER_RESOLUTION_UPDATE:
                    if (event.getRepeatCount() % 4 != 0) {
                        return true;
                    }
                    onPageKeyPress(true);
                    return true;
                case ConnectionInstance.WRITING_TIME_OUT:
                    if (event.getRepeatCount() % 4 != 0) {
                        return true;
                    }
                    onPageKeyPress(DEBUG);
                    return true;
                case Opcodes.DASTORE:
                    return event.getRepeatCount() == 0 ? onMenuKeyPress() : true;
                default:
                    return false;
            }
        }
    }

    public boolean onKeyEvent(KeyEvent event) {
        boolean parentProcessEvent = DEBUG;
        if (this.mAdapter == null || this.mAdapter.getCount() == 0) {
            return DEBUG;
        }
        if (event.getAction() == 0) {
            parentProcessEvent = onKeyDown(event);
        }
        Log.i(this.TAG, "mItemFocused  : " + this.mItemFocused);
        return parentProcessEvent;
    }

    protected void onLayout(boolean changeSize) {
        this.mSizeOrPosChanged = true;
        this.mRealRect.left = this.mPaddingRect.left;
        this.mRealRect.top = this.mPaddingRect.top;
        this.mRealRect.right = getWidth() - this.mPaddingRect.right;
        this.mRealRect.bottom = (int) (((float) (getHeight() - this.mPaddingRect.bottom)) - this.mReflectionHeight);
        this.MINR = (float) ((this.mRealRect.width() * 3) / 2);
        if (this.mItemFocused >= 0) {
            setSelectedItem(this.mItemFocused);
        }
    }

    public void onPageDown() {
        onPageKeyPress(true);
    }

    public void onPageUp() {
        onPageKeyPress(DEBUG);
    }

    protected void onRender(GLCanvas glCanvas, int left, int top) {
        long start = System.currentTimeMillis();
        if (this.mAdapter == null || this.mAdapter.getCount() == 0) {
            onDrawBackground(glCanvas, left, top, true);
        } else {
            int curCount = this.mAdapter.getCount();
            if (this.mCount != curCount) {
                this.mCount = curCount;
                this.mSizeOrPosChanged = true;
                notifyCountChange();
            }
            onDrawFlatMode(glCanvas, left, top);
            long timeElapsed = Math.min(50, start - this.mLastFrameTime);
            if (this.mOnShowScrollBar || this.mTouchMoved) {
                this.mScrollBarAlpha = FloatUtils.animate(this.mScrollBarAlpha, 1.0f, ((float) timeElapsed) * 0.001f);
                invalidate();
            } else if (this.mScrollBarAlpha != 0.0f) {
                this.mScrollBarAlpha = FloatUtils.animate(this.mScrollBarAlpha, 0.0f, ((float) timeElapsed) * 0.001f);
                invalidate();
            }
            if (this.mOnShowScrollBar || this.mTouchMoved || this.mScrollBarAlpha != 0.0f) {
                glCanvas.setColor(this.mScrollBarAlpha, this.mScrollBarAlpha, this.mScrollBarAlpha, this.mScrollBarAlpha);
                onDrawScrollBar(glCanvas, left, top);
                glCanvas.resetColor();
            }
            this.mLastFrameTime = start;
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(event);
        if (this.mGestureDetector != null) {
            this.mGestureDetector.onTouchEvent(event);
        }
        int action = event.getAction();
        if (action == 1 || action == 3) {
            this.mTouchMoved = false;
            if (this.mScroller.isFinished()) {
                snapScreen(SLIDE_MODE_PLANE, SLIDE_MODE_PLANE);
                invalidate();
            }
            if (this.mVelocityTracker != null) {
                this.mVelocityTracker.recycle();
                this.mVelocityTracker = null;
            }
        }
        return true;
    }

    public boolean performLongClick() {
        int item = findTouchItem(this.mLastDownX, this.mLastDownY);
        this.mLastDownItem = item;
        if (this.mIOnItemLongClickListener == null || item < 0 || item >= this.mAdapter.getCount()) {
            return DEBUG;
        }
        this.mIOnItemLongClickListener.onLongItemClick(item, this.mLastDownX, this.mLastDownY);
        return true;
    }

    protected boolean pointInItem(int index, float xLen2ViewCenter, float yLen2ViewCenter, RectF rc, float x, float y) {
        return rc.contains(x, y) ? true : DEBUG;
    }

    public void reflesh() {
        resetScroll();
        this.mItemFocused = 0;
        this.mItemTouchDowm = -1;
        invalidate();
    }

    public void resetScroll() {
        this.mScroller.forceFinished(true);
        this.mScrollY = 0.0f;
        this.mScrollX = 0.0f;
        this.mScrollAdjust = 0.0f;
        this.mCount = -1;
    }

    public void setBackGround(int id) {
        this.mBackGroundID = id;
    }

    public void setChoiceMode(int choiceMode) {
        this.mChoiceMode = choiceMode;
        if (this.mChoiceMode != 0) {
            if (this.mCheckStates == null) {
                this.mCheckStates = new SparseBooleanArray();
            }
            if (this.mCheckedIdStates == null && this.mAdapter != null) {
                this.mCheckedIdStates = new SparseArray();
            }
        }
    }

    public void setCrossfadeRatio(float ratio) {
        this.mCrossfadeRatio = ratio;
    }

    public void setFocusReferenceRect(Rect rect) {
        this.mFocusRect = rect;
    }

    public void setGridAdapter(IGLGridAdapter adapter) {
        disableItemMenu();
        this.mAdapter = adapter;
        setFocusable(true);
        resetScroll();
        this.mItemFocused = 0;
        this.mItemTouchDowm = -1;
    }

    public void setHorizontalScrollBarRes(int hTrack, int hThumb) {
        this.mTrackHorizontal = hTrack;
        this.mThumbHorizontal = hThumb;
    }

    public void setHorizontalSpace(int mHorizontalSpace) {
        this.mHorizontalSpace = mHorizontalSpace;
    }

    public void setItemChangeListener(IOnItemFocusChangeListener l) {
        this.mItemChangeListener = l;
    }

    public void setItemChecked(int position, boolean value) {
        if (this.mChoiceMode != 0) {
            if (this.mChoiceMode == 2) {
                this.mCheckStates.put(position, value);
                if (this.mCheckedIdStates == null) {
                    return;
                }
                if (value) {
                    this.mCheckedIdStates.put(position, Boolean.TRUE);
                } else {
                    this.mCheckedIdStates.delete(position);
                }
            } else {
                boolean updateIds = this.mCheckedIdStates != null ? true : DEBUG;
                if (value || isItemChecked(position)) {
                    this.mCheckStates.clear();
                    if (updateIds) {
                        this.mCheckedIdStates.clear();
                    }
                }
                if (value) {
                    this.mCheckStates.put(position, true);
                    if (updateIds) {
                        this.mCheckedIdStates.put(position, Boolean.TRUE);
                    }
                }
            }
        }
    }

    public void setItemLongClickListener(IOnItemLongClickListener l) {
        this.mIOnItemLongClickListener = l;
    }

    public void setItemPartCount(int count) {
        this.mItemPartCount = count;
        this.mGridDrawArgs.arcVertex = new float[(((this.mItemPartCount * 2) + 2) * 3)];
    }

    public void setOnItemClickListener(IOnItemClickListener l) {
        this.mIOnItemClickListener = l;
    }

    public void setPadding(int left, int top, int right, int bottom) {
        this.mPaddingRect.set(left, top, right, bottom);
        onLayout(true);
    }

    public void setPopupMenu(PopupMenu pmenu) {
        if (this.mPopupMenu != null) {
            getRootView().removeView(this.mPopupMenu);
        }
        this.mPopupMenu = pmenu;
        getRootView().addView(this.mPopupMenu);
    }

    public void setReflectionHeight(float height) {
        if (height < 0.0f) {
            height = 0.0f;
        }
        this.mReflectionHeight = height;
        onLayout(true);
    }

    public void setScrollBarSize(int size) {
        this.mTrackBarSize = size;
    }

    public void setScrollMode(int mode) {
        if (mode > 1 || mode < 0) {
            throw new IllegalArgumentException("mode only support SCROLL_MODE_HORIZONTAL and SCROLL_MODE_VERTICAL");
        }
        this.mScrollMode = mode;
        this.mSizeOrPosChanged = true;
        if (1 == mode) {
            this.mItemPartCount = 1;
        }
    }

    public void setScrollY(float scrollY) {
        if (!this.mScroller.isFinished()) {
            this.mScroller.forceFinished(true);
        }
        int avilable = getContentLen() - this.mRealRect.height();
        if (avilable >= 0) {
            if (scrollY > ((float) avilable)) {
                scrollY = (float) avilable;
            }
            this.mScrollY = scrollY;
        }
    }

    public void setSelectedItem(int index) {
        setSelectedItem(index, true);
    }

    public void setSingleLine(boolean b) {
        this.mSingleLine = b;
    }

    public void setSlideMode(int mode) {
        this.mSlideMode = mode;
        if (1 == mode) {
            this.mItemPartCount = 10;
            this.mGridDrawArgs.arcVertex = new float[(((this.mItemPartCount * 2) + 2) * 3)];
        } else {
            this.mItemPartCount = 1;
        }
        initScroller();
    }

    public void setTag(String tag) {
        this.TAG = tag;
    }

    public void setVerticalScrollBarRes(int vTrack, int vThumb) {
        this.mTrackVertical = vTrack;
        this.mThumbVertical = vThumb;
    }

    public void setVerticalSpace(int mVerticalSpace) {
        this.mVerticalSpace = mVerticalSpace;
    }

    public void setVisibilityOfScrollBar(boolean visible) {
        this.mReqScrollBar = visible;
    }

    public void setZ(float z) {
        this.mZValue = z;
        if (this.mGridDrawArgs != null) {
            this.mGridDrawArgs.setZ(z);
        }
        if (this.mGLGridBackgroundDrawer != null) {
            this.mGLGridBackgroundDrawer.setZ(z);
        }
        if (this.mScrollBarTrackDrawer != null) {
            this.mScrollBarTrackDrawer.setZ(z);
        }
        if (this.mScrollBarThumbDrawer != null) {
            this.mScrollBarThumbDrawer.setZ(z);
        }
    }

    public void startDiffAnimation(int startX, int startY, float[] list) {
        if (this.mAdapter != null) {
            int num = getCeilNumberPrePage();
            if (num > this.mAdapter.getCount()) {
                num = this.mAdapter.getCount();
            }
            this.mDiffList.clear();
            float itemW = (float) this.mAdapter.getItemWidth();
            float itemH = (float) this.mAdapter.getItemHeight();
            int yNum = getNumOfRowPerPage();
            int i = SLIDE_MODE_PLANE;
            while (i < num) {
                float roatef;
                if (i < list.length) {
                    roatef = list[i];
                } else {
                    roatef = 0.0f;
                }
                ItemAnimation itemAnimation = new ItemAnimation(calculateCenterX(SLIDE_MODE_PLANE, i / yNum, itemW) - ((float) startX), 0.0f, ((float) startY) - calculateCenterY(SLIDE_MODE_PLANE, i % yNum, itemH), 0.0f, this.mZValue);
                itemAnimation.setRoatef(roatef, 0.0f);
                this.mDiffList.add(itemAnimation);
                i++;
            }
            this.mDiffAnimationMark = true;
        }
    }

    public void startGroupAnimation(int endIndex, ArrayList<ItemAnimation> groups) {
        this.mGroupList = groups;
        this.mGroupAnimationMark = true;
    }
}