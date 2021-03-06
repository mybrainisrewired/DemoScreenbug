package android.support.v4.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.KeyEventCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewGroupCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.ViewDragHelper.Callback;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import mobi.vserv.android.ads.VservConstants;

public class DrawerLayout extends ViewGroup {
    private static final boolean ALLOW_EDGE_LOCK = false;
    private static final boolean CHILDREN_DISALLOW_INTERCEPT = true;
    private static final int DEFAULT_SCRIM_COLOR = -1728053248;
    private static final int[] LAYOUT_ATTRS;
    public static final int LOCK_MODE_LOCKED_CLOSED = 1;
    public static final int LOCK_MODE_LOCKED_OPEN = 2;
    public static final int LOCK_MODE_UNLOCKED = 0;
    private static final int MIN_DRAWER_MARGIN = 64;
    private static final int MIN_FLING_VELOCITY = 400;
    private static final int PEEK_DELAY = 160;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_SETTLING = 2;
    private static final String TAG = "DrawerLayout";
    private static final float TOUCH_SLOP_SENSITIVITY = 1.0f;
    private final ChildAccessibilityDelegate mChildAccessibilityDelegate;
    private boolean mChildrenCanceledTouch;
    private boolean mDisallowInterceptRequested;
    private int mDrawerState;
    private boolean mFirstLayout;
    private boolean mInLayout;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private final ViewDragCallback mLeftCallback;
    private final ViewDragHelper mLeftDragger;
    private DrawerListener mListener;
    private int mLockModeLeft;
    private int mLockModeRight;
    private int mMinDrawerMargin;
    private final ViewDragCallback mRightCallback;
    private final ViewDragHelper mRightDragger;
    private int mScrimColor;
    private float mScrimOpacity;
    private Paint mScrimPaint;
    private Drawable mShadowLeft;
    private Drawable mShadowRight;
    private CharSequence mTitleLeft;
    private CharSequence mTitleRight;

    public static interface DrawerListener {
        void onDrawerClosed(View view);

        void onDrawerOpened(View view);

        void onDrawerSlide(View view, float f);

        void onDrawerStateChanged(int i);
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({3, 5, 8388611, 8388613})
    private static @interface EdgeGravity {
    }

    public static class LayoutParams extends MarginLayoutParams {
        public int gravity;
        boolean isPeeking;
        boolean knownOpen;
        float onScreen;

        public LayoutParams(int width, int height) {
            super(width, height);
            this.gravity = 0;
        }

        public LayoutParams(int width, int height, int gravity) {
            this(width, height);
            this.gravity = gravity;
        }

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            this.gravity = 0;
            TypedArray a = c.obtainStyledAttributes(attrs, LAYOUT_ATTRS);
            this.gravity = a.getInt(STATE_IDLE, STATE_IDLE);
            a.recycle();
        }

        public LayoutParams(android.support.v4.widget.DrawerLayout.LayoutParams source) {
            super(source);
            this.gravity = 0;
            this.gravity = source.gravity;
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
            super(source);
            this.gravity = 0;
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
            this.gravity = 0;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({0, 1, 2})
    private static @interface LockMode {
    }

    protected static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR;
        int lockModeLeft;
        int lockModeRight;
        int openDrawerGravity;

        static {
            CREATOR = new Creator<SavedState>() {
                public SavedState createFromParcel(Parcel source) {
                    return new SavedState(source);
                }

                public SavedState[] newArray(int size) {
                    return new SavedState[size];
                }
            };
        }

        public SavedState(Parcel in) {
            super(in);
            this.openDrawerGravity = 0;
            this.lockModeLeft = 0;
            this.lockModeRight = 0;
            this.openDrawerGravity = in.readInt();
        }

        public SavedState(Parcelable superState) {
            super(superState);
            this.openDrawerGravity = 0;
            this.lockModeLeft = 0;
            this.lockModeRight = 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.openDrawerGravity);
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({0, 1, 2})
    private static @interface State {
    }

    class AccessibilityDelegate extends AccessibilityDelegateCompat {
        private final Rect mTmpRect;

        AccessibilityDelegate() {
            this.mTmpRect = new Rect();
        }

        private void addChildrenForAccessibility(AccessibilityNodeInfoCompat info, ViewGroup v) {
            int childCount = v.getChildCount();
            int i = STATE_IDLE;
            while (i < childCount) {
                View child = v.getChildAt(i);
                if (DrawerLayout.includeChildForAccessibilitiy(child)) {
                    info.addChild(child);
                }
                i++;
            }
        }

        private void copyNodeInfoNoChildren(AccessibilityNodeInfoCompat dest, AccessibilityNodeInfoCompat src) {
            Rect rect = this.mTmpRect;
            src.getBoundsInParent(rect);
            dest.setBoundsInParent(rect);
            src.getBoundsInScreen(rect);
            dest.setBoundsInScreen(rect);
            dest.setVisibleToUser(src.isVisibleToUser());
            dest.setPackageName(src.getPackageName());
            dest.setClassName(src.getClassName());
            dest.setContentDescription(src.getContentDescription());
            dest.setEnabled(src.isEnabled());
            dest.setClickable(src.isClickable());
            dest.setFocusable(src.isFocusable());
            dest.setFocused(src.isFocused());
            dest.setAccessibilityFocused(src.isAccessibilityFocused());
            dest.setSelected(src.isSelected());
            dest.setLongClickable(src.isLongClickable());
            dest.addAction(src.getActions());
        }

        public boolean dispatchPopulateAccessibilityEvent(View host, AccessibilityEvent event) {
            if (event.getEventType() != 32) {
                return super.dispatchPopulateAccessibilityEvent(host, event);
            }
            List<CharSequence> eventText = event.getText();
            View visibleDrawer = DrawerLayout.this.findVisibleDrawer();
            if (visibleDrawer != null) {
                CharSequence title = DrawerLayout.this.getDrawerTitle(DrawerLayout.this.getDrawerViewAbsoluteGravity(visibleDrawer));
                if (title != null) {
                    eventText.add(title);
                }
            }
            return CHILDREN_DISALLOW_INTERCEPT;
        }

        public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
            super.onInitializeAccessibilityEvent(host, event);
            event.setClassName(DrawerLayout.class.getName());
        }

        public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
            AccessibilityNodeInfoCompat superNode = AccessibilityNodeInfoCompat.obtain(info);
            super.onInitializeAccessibilityNodeInfo(host, superNode);
            info.setClassName(DrawerLayout.class.getName());
            info.setSource(host);
            ViewParent parent = ViewCompat.getParentForAccessibility(host);
            if (parent instanceof View) {
                info.setParent((View) parent);
            }
            copyNodeInfoNoChildren(info, superNode);
            superNode.recycle();
            addChildrenForAccessibility(info, (ViewGroup) host);
        }

        public boolean onRequestSendAccessibilityEvent(ViewGroup host, View child, AccessibilityEvent event) {
            return DrawerLayout.includeChildForAccessibilitiy(child) ? super.onRequestSendAccessibilityEvent(host, child, event) : ALLOW_EDGE_LOCK;
        }
    }

    final class ChildAccessibilityDelegate extends AccessibilityDelegateCompat {
        ChildAccessibilityDelegate() {
        }

        public void onInitializeAccessibilityNodeInfo(View child, AccessibilityNodeInfoCompat info) {
            super.onInitializeAccessibilityNodeInfo(child, info);
            if (!DrawerLayout.includeChildForAccessibilitiy(child)) {
                info.setParent(null);
            }
        }
    }

    public static abstract class SimpleDrawerListener implements android.support.v4.widget.DrawerLayout.DrawerListener {
        public void onDrawerClosed(View drawerView) {
        }

        public void onDrawerOpened(View drawerView) {
        }

        public void onDrawerSlide(View drawerView, float slideOffset) {
        }

        public void onDrawerStateChanged(int newState) {
        }
    }

    private class ViewDragCallback extends Callback {
        private final int mAbsGravity;
        private ViewDragHelper mDragger;
        private final Runnable mPeekRunnable;

        public ViewDragCallback(int gravity) {
            this.mPeekRunnable = new Runnable() {
                public void run() {
                    ViewDragCallback.this.peekDrawer();
                }
            };
            this.mAbsGravity = gravity;
        }

        private void closeOtherDrawer() {
            int otherGrav = MMAdView.TRANSITION_DOWN;
            if (this.mAbsGravity == 3) {
                otherGrav = ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES;
            }
            View toClose = DrawerLayout.this.findDrawerWithGravity(otherGrav);
            if (toClose != null) {
                DrawerLayout.this.closeDrawer(toClose);
            }
        }

        private void peekDrawer() {
            boolean leftEdge;
            View toCapture;
            int childLeft;
            int i = STATE_IDLE;
            int peekDistance = this.mDragger.getEdgeSize();
            if (this.mAbsGravity == 3) {
                leftEdge = true;
            } else {
                leftEdge = false;
            }
            if (leftEdge) {
                toCapture = DrawerLayout.this.findDrawerWithGravity(MMAdView.TRANSITION_DOWN);
                if (toCapture != null) {
                    i = -toCapture.getWidth();
                }
                childLeft = i + peekDistance;
            } else {
                toCapture = DrawerLayout.this.findDrawerWithGravity(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES);
                childLeft = DrawerLayout.this.getWidth() - peekDistance;
            }
            if (toCapture == null) {
                return;
            }
            if (((leftEdge && toCapture.getLeft() < childLeft) || (!leftEdge && toCapture.getLeft() > childLeft)) && DrawerLayout.this.getDrawerLockMode(toCapture) == 0) {
                android.support.v4.widget.DrawerLayout.LayoutParams lp = (android.support.v4.widget.DrawerLayout.LayoutParams) toCapture.getLayoutParams();
                this.mDragger.smoothSlideViewTo(toCapture, childLeft, toCapture.getTop());
                lp.isPeeking = true;
                DrawerLayout.this.invalidate();
                closeOtherDrawer();
                DrawerLayout.this.cancelChildViewTouch();
            }
        }

        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (DrawerLayout.this.checkDrawerViewAbsoluteGravity(child, MMAdView.TRANSITION_DOWN)) {
                return Math.max(-child.getWidth(), Math.min(left, STATE_IDLE));
            }
            int width = DrawerLayout.this.getWidth();
            return Math.max(width - child.getWidth(), Math.min(left, width));
        }

        public int clampViewPositionVertical(View child, int top, int dy) {
            return child.getTop();
        }

        public int getViewHorizontalDragRange(View child) {
            return child.getWidth();
        }

        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            View toCapture;
            if ((edgeFlags & 1) == 1) {
                toCapture = DrawerLayout.this.findDrawerWithGravity(MMAdView.TRANSITION_DOWN);
            } else {
                toCapture = DrawerLayout.this.findDrawerWithGravity(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES);
            }
            if (toCapture != null && DrawerLayout.this.getDrawerLockMode(toCapture) == 0) {
                this.mDragger.captureChildView(toCapture, pointerId);
            }
        }

        public boolean onEdgeLock(int edgeFlags) {
            return ALLOW_EDGE_LOCK;
        }

        public void onEdgeTouched(int edgeFlags, int pointerId) {
            DrawerLayout.this.postDelayed(this.mPeekRunnable, 160);
        }

        public void onViewCaptured(View capturedChild, int activePointerId) {
            ((android.support.v4.widget.DrawerLayout.LayoutParams) capturedChild.getLayoutParams()).isPeeking = false;
            closeOtherDrawer();
        }

        public void onViewDragStateChanged(int state) {
            DrawerLayout.this.updateDrawerState(this.mAbsGravity, state, this.mDragger.getCapturedView());
        }

        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            float offset;
            int childWidth = changedView.getWidth();
            if (DrawerLayout.this.checkDrawerViewAbsoluteGravity(changedView, MMAdView.TRANSITION_DOWN)) {
                offset = ((float) (childWidth + left)) / ((float) childWidth);
            } else {
                offset = ((float) (DrawerLayout.this.getWidth() - left)) / ((float) childWidth);
            }
            DrawerLayout.this.setDrawerViewOffset(changedView, offset);
            changedView.setVisibility(offset == 0.0f ? MMAdView.TRANSITION_RANDOM : STATE_IDLE);
            DrawerLayout.this.invalidate();
        }

        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            int left;
            float offset = DrawerLayout.this.getDrawerViewOffset(releasedChild);
            int childWidth = releasedChild.getWidth();
            if (DrawerLayout.this.checkDrawerViewAbsoluteGravity(releasedChild, MMAdView.TRANSITION_DOWN)) {
                left = (xvel > 0.0f || (xvel == 0.0f && offset > 0.5f)) ? STATE_IDLE : -childWidth;
            } else {
                int width = DrawerLayout.this.getWidth();
                left = (xvel < 0.0f || (xvel == 0.0f && offset > 0.5f)) ? width - childWidth : width;
            }
            this.mDragger.settleCapturedViewAt(left, releasedChild.getTop());
            DrawerLayout.this.invalidate();
        }

        public void removeCallbacks() {
            DrawerLayout.this.removeCallbacks(this.mPeekRunnable);
        }

        public void setDragger(ViewDragHelper dragger) {
            this.mDragger = dragger;
        }

        public boolean tryCaptureView(View child, int pointerId) {
            return (DrawerLayout.this.isDrawerView(child) && DrawerLayout.this.checkDrawerViewAbsoluteGravity(child, this.mAbsGravity) && DrawerLayout.this.getDrawerLockMode(child) == 0) ? CHILDREN_DISALLOW_INTERCEPT : ALLOW_EDGE_LOCK;
        }
    }

    static {
        LAYOUT_ATTRS = new int[]{16842931};
    }

    public DrawerLayout(Context context) {
        this(context, null);
    }

    public DrawerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mChildAccessibilityDelegate = new ChildAccessibilityDelegate();
        this.mScrimColor = -1728053248;
        this.mScrimPaint = new Paint();
        this.mFirstLayout = true;
        float density = getResources().getDisplayMetrics().density;
        this.mMinDrawerMargin = (int) (64.0f * density + 0.5f);
        float minVel = 400.0f * density;
        this.mLeftCallback = new ViewDragCallback(3);
        this.mRightCallback = new ViewDragCallback(5);
        this.mLeftDragger = ViewDragHelper.create(this, TOUCH_SLOP_SENSITIVITY, this.mLeftCallback);
        this.mLeftDragger.setEdgeTrackingEnabled(STATE_DRAGGING);
        this.mLeftDragger.setMinVelocity(minVel);
        this.mLeftCallback.setDragger(this.mLeftDragger);
        this.mRightDragger = ViewDragHelper.create(this, TOUCH_SLOP_SENSITIVITY, this.mRightCallback);
        this.mRightDragger.setEdgeTrackingEnabled(STATE_SETTLING);
        this.mRightDragger.setMinVelocity(minVel);
        this.mRightCallback.setDragger(this.mRightDragger);
        setFocusableInTouchMode(CHILDREN_DISALLOW_INTERCEPT);
        ViewCompat.setImportantForAccessibility(this, STATE_DRAGGING);
        ViewCompat.setAccessibilityDelegate(this, new AccessibilityDelegate());
        ViewGroupCompat.setMotionEventSplittingEnabled(this, ALLOW_EDGE_LOCK);
    }

    private View findVisibleDrawer() {
        int childCount = getChildCount();
        int i = STATE_IDLE;
        while (i < childCount) {
            View child = getChildAt(i);
            if (isDrawerView(child) && isDrawerVisible(child)) {
                return child;
            }
            i++;
        }
        return null;
    }

    static String gravityToString(int gravity) {
        if ((gravity & 3) == 3) {
            return "LEFT";
        }
        return (gravity & 5) == 5 ? "RIGHT" : Integer.toHexString(gravity);
    }

    private static boolean hasOpaqueBackground(View v) {
        Drawable bg = v.getBackground();
        return (bg == null || bg.getOpacity() != -1) ? ALLOW_EDGE_LOCK : CHILDREN_DISALLOW_INTERCEPT;
    }

    private boolean hasPeekingDrawer() {
        int childCount = getChildCount();
        int i = STATE_IDLE;
        while (i < childCount) {
            if (((LayoutParams) getChildAt(i).getLayoutParams()).isPeeking) {
                return CHILDREN_DISALLOW_INTERCEPT;
            }
            i++;
        }
        return ALLOW_EDGE_LOCK;
    }

    private boolean hasVisibleDrawer() {
        return findVisibleDrawer() != null ? CHILDREN_DISALLOW_INTERCEPT : ALLOW_EDGE_LOCK;
    }

    private static boolean includeChildForAccessibilitiy(View child) {
        return (ViewCompat.getImportantForAccessibility(child) == 4 || ViewCompat.getImportantForAccessibility(child) == 2) ? ALLOW_EDGE_LOCK : CHILDREN_DISALLOW_INTERCEPT;
    }

    public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
        if (index > 0 || (index < 0 && getChildCount() > 0)) {
            ViewCompat.setImportantForAccessibility(child, MMAdView.TRANSITION_RANDOM);
            ViewCompat.setAccessibilityDelegate(child, this.mChildAccessibilityDelegate);
        } else {
            ViewCompat.setImportantForAccessibility(child, STATE_DRAGGING);
        }
        super.addView(child, index, params);
    }

    void cancelChildViewTouch() {
        if (!this.mChildrenCanceledTouch) {
            long now = SystemClock.uptimeMillis();
            MotionEvent cancelEvent = MotionEvent.obtain(now, now, MMAdView.TRANSITION_DOWN, BitmapDescriptorFactory.HUE_RED, 0.0f, STATE_IDLE);
            int childCount = getChildCount();
            int i = STATE_IDLE;
            while (i < childCount) {
                getChildAt(i).dispatchTouchEvent(cancelEvent);
                i++;
            }
            cancelEvent.recycle();
            this.mChildrenCanceledTouch = true;
        }
    }

    boolean checkDrawerViewAbsoluteGravity(View drawerView, int checkFor) {
        return (getDrawerViewAbsoluteGravity(drawerView) & checkFor) == checkFor ? CHILDREN_DISALLOW_INTERCEPT : ALLOW_EDGE_LOCK;
    }

    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return (p instanceof LayoutParams && super.checkLayoutParams(p)) ? CHILDREN_DISALLOW_INTERCEPT : ALLOW_EDGE_LOCK;
    }

    public void closeDrawer(int gravity) {
        View drawerView = findDrawerWithGravity(gravity);
        if (drawerView == null) {
            throw new IllegalArgumentException("No drawer view found with gravity " + gravityToString(gravity));
        }
        closeDrawer(drawerView);
    }

    public void closeDrawer(View drawerView) {
        if (isDrawerView(drawerView)) {
            if (this.mFirstLayout) {
                LayoutParams lp = (LayoutParams) drawerView.getLayoutParams();
                lp.onScreen = 0.0f;
                lp.knownOpen = false;
            } else if (checkDrawerViewAbsoluteGravity(drawerView, MMAdView.TRANSITION_DOWN)) {
                this.mLeftDragger.smoothSlideViewTo(drawerView, -drawerView.getWidth(), drawerView.getTop());
            } else {
                this.mRightDragger.smoothSlideViewTo(drawerView, getWidth(), drawerView.getTop());
            }
            invalidate();
        } else {
            throw new IllegalArgumentException("View " + drawerView + " is not a sliding drawer");
        }
    }

    public void closeDrawers() {
        closeDrawers(ALLOW_EDGE_LOCK);
    }

    void closeDrawers(boolean peekingOnly) {
        boolean needsInvalidate = ALLOW_EDGE_LOCK;
        int childCount = getChildCount();
        int i = STATE_IDLE;
        while (i < childCount) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if (isDrawerView(child)) {
                if (!peekingOnly || lp.isPeeking) {
                    int childWidth = child.getWidth();
                    if (checkDrawerViewAbsoluteGravity(child, MMAdView.TRANSITION_DOWN)) {
                        needsInvalidate |= this.mLeftDragger.smoothSlideViewTo(child, -childWidth, child.getTop());
                    } else {
                        needsInvalidate |= this.mRightDragger.smoothSlideViewTo(child, getWidth(), child.getTop());
                    }
                    lp.isPeeking = false;
                }
            }
            i++;
        }
        this.mLeftCallback.removeCallbacks();
        this.mRightCallback.removeCallbacks();
        if (needsInvalidate) {
            invalidate();
        }
    }

    public void computeScroll() {
        int childCount = getChildCount();
        float scrimOpacity = BitmapDescriptorFactory.HUE_RED;
        int i = STATE_IDLE;
        while (i < childCount) {
            scrimOpacity = Math.max(scrimOpacity, ((LayoutParams) getChildAt(i).getLayoutParams()).onScreen);
            i++;
        }
        this.mScrimOpacity = scrimOpacity;
        if ((this.mLeftDragger.continueSettling(CHILDREN_DISALLOW_INTERCEPT) | this.mRightDragger.continueSettling(CHILDREN_DISALLOW_INTERCEPT)) != 0) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    void dispatchOnDrawerClosed(View drawerView) {
        LayoutParams lp = (LayoutParams) drawerView.getLayoutParams();
        if (lp.knownOpen) {
            lp.knownOpen = false;
            if (this.mListener != null) {
                this.mListener.onDrawerClosed(drawerView);
            }
            View content = getChildAt(STATE_IDLE);
            if (content != null) {
                ViewCompat.setImportantForAccessibility(content, STATE_DRAGGING);
            }
            ViewCompat.setImportantForAccessibility(drawerView, MMAdView.TRANSITION_RANDOM);
            if (hasWindowFocus()) {
                View rootView = getRootView();
                if (rootView != null) {
                    rootView.sendAccessibilityEvent(ApiEventType.API_MRAID_PLAY_AUDIO);
                }
            }
        }
    }

    void dispatchOnDrawerOpened(View drawerView) {
        LayoutParams lp = (LayoutParams) drawerView.getLayoutParams();
        if (!lp.knownOpen) {
            lp.knownOpen = true;
            if (this.mListener != null) {
                this.mListener.onDrawerOpened(drawerView);
            }
            View content = getChildAt(STATE_IDLE);
            if (content != null) {
                ViewCompat.setImportantForAccessibility(content, MMAdView.TRANSITION_RANDOM);
            }
            ViewCompat.setImportantForAccessibility(drawerView, STATE_DRAGGING);
            sendAccessibilityEvent(ApiEventType.API_MRAID_PLAY_AUDIO);
            drawerView.requestFocus();
        }
    }

    void dispatchOnDrawerSlide(View drawerView, float slideOffset) {
        if (this.mListener != null) {
            this.mListener.onDrawerSlide(drawerView, slideOffset);
        }
    }

    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        int height = getHeight();
        boolean drawingContent = isContentView(child);
        int clipLeft = STATE_IDLE;
        int clipRight = getWidth();
        int restoreCount = canvas.save();
        if (drawingContent) {
            int childCount = getChildCount();
            int i = STATE_IDLE;
            while (i < childCount) {
                View v = getChildAt(i);
                if (v != child && v.getVisibility() == 0 && hasOpaqueBackground(v) && isDrawerView(v) && v.getHeight() >= height) {
                    if (checkDrawerViewAbsoluteGravity(v, MMAdView.TRANSITION_DOWN)) {
                        int vright = v.getRight();
                        if (vright > clipLeft) {
                            clipLeft = vright;
                        }
                    } else {
                        int vleft = v.getLeft();
                        if (vleft < clipRight) {
                            clipRight = vleft;
                        }
                    }
                }
                i++;
            }
            canvas.clipRect(clipLeft, STATE_IDLE, clipRight, getHeight());
        }
        boolean result = super.drawChild(canvas, child, drawingTime);
        canvas.restoreToCount(restoreCount);
        if (this.mScrimOpacity > 0.0f && drawingContent) {
            this.mScrimPaint.setColor((((int) (((float) ((this.mScrimColor & -16777216) >>> 24)) * this.mScrimOpacity)) << 24) | (this.mScrimColor & 16777215));
            canvas.drawRect((float) clipLeft, BitmapDescriptorFactory.HUE_RED, (float) clipRight, (float) getHeight(), this.mScrimPaint);
        } else if (this.mShadowLeft != null && checkDrawerViewAbsoluteGravity(child, MMAdView.TRANSITION_DOWN)) {
            shadowWidth = this.mShadowLeft.getIntrinsicWidth();
            int childRight = child.getRight();
            alpha = Math.max(BitmapDescriptorFactory.HUE_RED, Math.min(((float) childRight) / ((float) this.mLeftDragger.getEdgeSize()), TOUCH_SLOP_SENSITIVITY));
            this.mShadowLeft.setBounds(childRight, child.getTop(), childRight + shadowWidth, child.getBottom());
            this.mShadowLeft.setAlpha((int) (255.0f * alpha));
            this.mShadowLeft.draw(canvas);
        } else if (this.mShadowRight != null && checkDrawerViewAbsoluteGravity(child, ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES)) {
            shadowWidth = this.mShadowRight.getIntrinsicWidth();
            int childLeft = child.getLeft();
            alpha = Math.max(BitmapDescriptorFactory.HUE_RED, Math.min(((float) (getWidth() - childLeft)) / ((float) this.mRightDragger.getEdgeSize()), TOUCH_SLOP_SENSITIVITY));
            this.mShadowRight.setBounds(childLeft - shadowWidth, child.getTop(), childLeft, child.getBottom());
            this.mShadowRight.setAlpha((int) (255.0f * alpha));
            this.mShadowRight.draw(canvas);
        }
        return result;
    }

    View findDrawerWithGravity(int gravity) {
        int absHorizGravity = GravityCompat.getAbsoluteGravity(gravity, ViewCompat.getLayoutDirection(this)) & 7;
        int childCount = getChildCount();
        int i = STATE_IDLE;
        while (i < childCount) {
            View child = getChildAt(i);
            if ((getDrawerViewAbsoluteGravity(child) & 7) == absHorizGravity) {
                return child;
            }
            i++;
        }
        return null;
    }

    View findOpenDrawer() {
        int childCount = getChildCount();
        int i = STATE_IDLE;
        while (i < childCount) {
            View child = getChildAt(i);
            if (((LayoutParams) child.getLayoutParams()).knownOpen) {
                return child;
            }
            i++;
        }
        return null;
    }

    protected android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -1);
    }

    public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        if (p instanceof LayoutParams) {
            return new LayoutParams((LayoutParams) p);
        }
        return p instanceof MarginLayoutParams ? new LayoutParams((MarginLayoutParams) p) : new LayoutParams(p);
    }

    public int getDrawerLockMode(int edgeGravity) {
        int absGravity = GravityCompat.getAbsoluteGravity(edgeGravity, ViewCompat.getLayoutDirection(this));
        if (absGravity == 3) {
            return this.mLockModeLeft;
        }
        return absGravity == 5 ? this.mLockModeRight : STATE_IDLE;
    }

    public int getDrawerLockMode(View drawerView) {
        int absGravity = getDrawerViewAbsoluteGravity(drawerView);
        if (absGravity == 3) {
            return this.mLockModeLeft;
        }
        return absGravity == 5 ? this.mLockModeRight : STATE_IDLE;
    }

    @Nullable
    public CharSequence getDrawerTitle(int edgeGravity) {
        int absGravity = GravityCompat.getAbsoluteGravity(edgeGravity, ViewCompat.getLayoutDirection(this));
        if (absGravity == 3) {
            return this.mTitleLeft;
        }
        return absGravity == 5 ? this.mTitleRight : null;
    }

    int getDrawerViewAbsoluteGravity(View drawerView) {
        return GravityCompat.getAbsoluteGravity(((LayoutParams) drawerView.getLayoutParams()).gravity, ViewCompat.getLayoutDirection(this));
    }

    float getDrawerViewOffset(View drawerView) {
        return ((LayoutParams) drawerView.getLayoutParams()).onScreen;
    }

    boolean isContentView(View child) {
        return ((LayoutParams) child.getLayoutParams()).gravity == 0 ? CHILDREN_DISALLOW_INTERCEPT : ALLOW_EDGE_LOCK;
    }

    public boolean isDrawerOpen(int drawerGravity) {
        View drawerView = findDrawerWithGravity(drawerGravity);
        return drawerView != null ? isDrawerOpen(drawerView) : ALLOW_EDGE_LOCK;
    }

    public boolean isDrawerOpen(View drawer) {
        if (isDrawerView(drawer)) {
            return ((LayoutParams) drawer.getLayoutParams()).knownOpen;
        }
        throw new IllegalArgumentException("View " + drawer + " is not a drawer");
    }

    boolean isDrawerView(View child) {
        return (GravityCompat.getAbsoluteGravity(((LayoutParams) child.getLayoutParams()).gravity, ViewCompat.getLayoutDirection(child)) & 7) != 0 ? CHILDREN_DISALLOW_INTERCEPT : ALLOW_EDGE_LOCK;
    }

    public boolean isDrawerVisible(int drawerGravity) {
        View drawerView = findDrawerWithGravity(drawerGravity);
        return drawerView != null ? isDrawerVisible(drawerView) : ALLOW_EDGE_LOCK;
    }

    public boolean isDrawerVisible(View drawer) {
        if (isDrawerView(drawer)) {
            return ((LayoutParams) drawer.getLayoutParams()).onScreen > 0.0f ? CHILDREN_DISALLOW_INTERCEPT : ALLOW_EDGE_LOCK;
        } else {
            throw new IllegalArgumentException("View " + drawer + " is not a drawer");
        }
    }

    void moveDrawerToOffset(View drawerView, float slideOffset) {
        float oldOffset = getDrawerViewOffset(drawerView);
        int width = drawerView.getWidth();
        int dx = ((int) (((float) width) * slideOffset)) - ((int) (((float) width) * oldOffset));
        if (!checkDrawerViewAbsoluteGravity(drawerView, MMAdView.TRANSITION_DOWN)) {
            dx = -dx;
        }
        drawerView.offsetLeftAndRight(dx);
        setDrawerViewOffset(drawerView, slideOffset);
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mFirstLayout = true;
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);
        boolean interceptForDrag = this.mLeftDragger.shouldInterceptTouchEvent(ev) | this.mRightDragger.shouldInterceptTouchEvent(ev);
        boolean interceptForTap = ALLOW_EDGE_LOCK;
        switch (action) {
            case STATE_IDLE:
                float x = ev.getX();
                float y = ev.getY();
                this.mInitialMotionX = x;
                this.mInitialMotionY = y;
                if (this.mScrimOpacity > 0.0f && isContentView(this.mLeftDragger.findTopChildUnder((int) x, (int) y))) {
                    interceptForTap = CHILDREN_DISALLOW_INTERCEPT;
                }
                this.mDisallowInterceptRequested = false;
                this.mChildrenCanceledTouch = false;
                break;
            case STATE_DRAGGING:
            case MMAdView.TRANSITION_DOWN:
                closeDrawers(CHILDREN_DISALLOW_INTERCEPT);
                this.mDisallowInterceptRequested = false;
                this.mChildrenCanceledTouch = false;
                break;
            case STATE_SETTLING:
                if (this.mLeftDragger.checkTouchSlop(MMAdView.TRANSITION_DOWN)) {
                    this.mLeftCallback.removeCallbacks();
                    this.mRightCallback.removeCallbacks();
                }
                break;
        }
        return (interceptForDrag || interceptForTap || hasPeekingDrawer() || this.mChildrenCanceledTouch) ? true : ALLOW_EDGE_LOCK;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 || !hasVisibleDrawer()) {
            return super.onKeyDown(keyCode, event);
        }
        KeyEventCompat.startTracking(event);
        return CHILDREN_DISALLOW_INTERCEPT;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return super.onKeyUp(keyCode, event);
        }
        View visibleDrawer = findVisibleDrawer();
        if (visibleDrawer != null && getDrawerLockMode(visibleDrawer) == 0) {
            closeDrawers();
        }
        return visibleDrawer != null ? CHILDREN_DISALLOW_INTERCEPT : ALLOW_EDGE_LOCK;
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        this.mInLayout = true;
        int width = r - l;
        int childCount = getChildCount();
        int i = STATE_IDLE;
        while (i < childCount) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (isContentView(child)) {
                    child.layout(lp.leftMargin, lp.topMargin, lp.leftMargin + child.getMeasuredWidth(), lp.topMargin + child.getMeasuredHeight());
                } else {
                    int childLeft;
                    float newOffset;
                    int childWidth = child.getMeasuredWidth();
                    int childHeight = child.getMeasuredHeight();
                    if (checkDrawerViewAbsoluteGravity(child, 3)) {
                        childLeft = (-childWidth) + ((int) (((float) childWidth) * lp.onScreen));
                        newOffset = ((float) (childWidth + childLeft)) / ((float) childWidth);
                    } else {
                        childLeft = width - ((int) (((float) childWidth) * lp.onScreen));
                        newOffset = ((float) (width - childLeft)) / ((float) childWidth);
                    }
                    boolean changeOffset = newOffset != lp.onScreen ? CHILDREN_DISALLOW_INTERCEPT : ALLOW_EDGE_LOCK;
                    int height;
                    switch (lp.gravity & 112) {
                        case ApiEventType.API_MRAID_GET_ORIENTATION:
                            height = b - t;
                            int childTop = (height - childHeight) / 2;
                            if (childTop < lp.topMargin) {
                                childTop = lp.topMargin;
                            } else {
                                if (childTop + childHeight > height - lp.bottomMargin) {
                                    childTop = height - lp.bottomMargin - childHeight;
                                }
                            }
                            child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
                            break;
                        case VservConstants.THREAD_SLEEP:
                            height = b - t;
                            child.layout(childLeft, height - lp.bottomMargin - child.getMeasuredHeight(), childLeft + childWidth, height - lp.bottomMargin);
                            break;
                        default:
                            child.layout(childLeft, lp.topMargin, childLeft + childWidth, lp.topMargin + childHeight);
                            break;
                    }
                    if (changeOffset) {
                        setDrawerViewOffset(child, newOffset);
                    }
                    int newVisibility = lp.onScreen > 0.0f ? STATE_IDLE : MMAdView.TRANSITION_RANDOM;
                    if (child.getVisibility() != newVisibility) {
                        child.setVisibility(newVisibility);
                    }
                }
            }
            i++;
        }
        this.mInLayout = false;
        this.mFirstLayout = false;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if ((widthMode == 1073741824 && heightMode == 1073741824) || isInEditMode()) {
            if (widthMode != Integer.MIN_VALUE) {
                if (widthMode == 0) {
                    widthSize = 300;
                }
            }
            if (heightMode != Integer.MIN_VALUE) {
                if (heightMode == 0) {
                    heightSize = 300;
                }
            }
            setMeasuredDimension(widthSize, heightSize);
            int childCount = getChildCount();
            int i = STATE_IDLE;
            while (i < childCount) {
                View child = getChildAt(i);
                if (child.getVisibility() != 8) {
                    LayoutParams lp = (LayoutParams) child.getLayoutParams();
                    if (isContentView(child)) {
                        child.measure(MeasureSpec.makeMeasureSpec(widthSize - lp.leftMargin - lp.rightMargin, 1073741824), MeasureSpec.makeMeasureSpec(heightSize - lp.topMargin - lp.bottomMargin, 1073741824));
                    } else if (isDrawerView(child)) {
                        int childGravity = getDrawerViewAbsoluteGravity(child) & 7;
                        if ((0 & childGravity) != 0) {
                            throw new IllegalStateException("Child drawer has absolute gravity " + gravityToString(childGravity) + " but this " + TAG + " already has a " + "drawer view along that edge");
                        }
                        child.measure(getChildMeasureSpec(widthMeasureSpec, this.mMinDrawerMargin + lp.leftMargin + lp.rightMargin, lp.width), getChildMeasureSpec(heightMeasureSpec, lp.topMargin + lp.bottomMargin, lp.height));
                    } else {
                        throw new IllegalStateException("Child " + child + " at index " + i + " does not have a valid layout_gravity - must be Gravity.LEFT, " + "Gravity.RIGHT or Gravity.NO_GRAVITY");
                    }
                }
                i++;
            }
        } else {
            throw new IllegalArgumentException("DrawerLayout must be measured with MeasureSpec.EXACTLY.");
        }
    }

    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        if (ss.openDrawerGravity != 0) {
            View toOpen = findDrawerWithGravity(ss.openDrawerGravity);
            if (toOpen != null) {
                openDrawer(toOpen);
            }
        }
        setDrawerLockMode(ss.lockModeLeft, (int)MMAdView.TRANSITION_DOWN);
        setDrawerLockMode(ss.lockModeRight, (int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES);
    }

    protected Parcelable onSaveInstanceState() {
        SavedState ss = new SavedState(super.onSaveInstanceState());
        int childCount = getChildCount();
        int i = STATE_IDLE;
        while (i < childCount) {
            View child = getChildAt(i);
            if (isDrawerView(child)) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (lp.knownOpen) {
                    ss.openDrawerGravity = lp.gravity;
                    break;
                }
            }
            i++;
        }
        ss.lockModeLeft = this.mLockModeLeft;
        ss.lockModeRight = this.mLockModeRight;
        return ss;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        this.mLeftDragger.processTouchEvent(ev);
        this.mRightDragger.processTouchEvent(ev);
        float x;
        float y;
        switch (ev.getAction() & 255) {
            case STATE_IDLE:
                x = ev.getX();
                y = ev.getY();
                this.mInitialMotionX = x;
                this.mInitialMotionY = y;
                this.mDisallowInterceptRequested = false;
                this.mChildrenCanceledTouch = false;
                break;
            case STATE_DRAGGING:
                x = ev.getX();
                y = ev.getY();
                boolean peekingOnly = CHILDREN_DISALLOW_INTERCEPT;
                View touchedView = this.mLeftDragger.findTopChildUnder((int) x, (int) y);
                if (touchedView != null && isContentView(touchedView)) {
                    float dx = x - this.mInitialMotionX;
                    float dy = y - this.mInitialMotionY;
                    int slop = this.mLeftDragger.getTouchSlop();
                    if (dx * dx + dy * dy < ((float) (slop * slop))) {
                        View openDrawer = findOpenDrawer();
                        if (openDrawer != null) {
                            peekingOnly = getDrawerLockMode(openDrawer) == 2 ? CHILDREN_DISALLOW_INTERCEPT : ALLOW_EDGE_LOCK;
                        }
                    }
                }
                closeDrawers(peekingOnly);
                this.mDisallowInterceptRequested = false;
                break;
            case MMAdView.TRANSITION_DOWN:
                closeDrawers(CHILDREN_DISALLOW_INTERCEPT);
                this.mDisallowInterceptRequested = false;
                this.mChildrenCanceledTouch = false;
                break;
        }
        return CHILDREN_DISALLOW_INTERCEPT;
    }

    public void openDrawer(int gravity) {
        View drawerView = findDrawerWithGravity(gravity);
        if (drawerView == null) {
            throw new IllegalArgumentException("No drawer view found with gravity " + gravityToString(gravity));
        }
        openDrawer(drawerView);
    }

    public void openDrawer(View drawerView) {
        if (isDrawerView(drawerView)) {
            if (this.mFirstLayout) {
                LayoutParams lp = (LayoutParams) drawerView.getLayoutParams();
                lp.onScreen = 1.0f;
                lp.knownOpen = true;
            } else if (checkDrawerViewAbsoluteGravity(drawerView, MMAdView.TRANSITION_DOWN)) {
                this.mLeftDragger.smoothSlideViewTo(drawerView, STATE_IDLE, drawerView.getTop());
            } else {
                this.mRightDragger.smoothSlideViewTo(drawerView, getWidth() - drawerView.getWidth(), drawerView.getTop());
            }
            invalidate();
        } else {
            throw new IllegalArgumentException("View " + drawerView + " is not a sliding drawer");
        }
    }

    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
        this.mDisallowInterceptRequested = disallowIntercept;
        if (disallowIntercept) {
            closeDrawers(CHILDREN_DISALLOW_INTERCEPT);
        }
    }

    public void requestLayout() {
        if (!this.mInLayout) {
            super.requestLayout();
        }
    }

    public void setDrawerListener(DrawerListener listener) {
        this.mListener = listener;
    }

    public void setDrawerLockMode(int lockMode) {
        setDrawerLockMode(lockMode, (int)MMAdView.TRANSITION_DOWN);
        setDrawerLockMode(lockMode, (int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES);
    }

    public void setDrawerLockMode(int lockMode, int edgeGravity) {
        int absGravity = GravityCompat.getAbsoluteGravity(edgeGravity, ViewCompat.getLayoutDirection(this));
        if (absGravity == 3) {
            this.mLockModeLeft = lockMode;
        } else if (absGravity == 5) {
            this.mLockModeRight = lockMode;
        }
        if (lockMode != 0) {
            (absGravity == 3 ? this.mLeftDragger : this.mRightDragger).cancel();
        }
        switch (lockMode) {
            case STATE_DRAGGING:
                View toClose = findDrawerWithGravity(absGravity);
                if (toClose != null) {
                    closeDrawer(toClose);
                }
            case STATE_SETTLING:
                View toOpen = findDrawerWithGravity(absGravity);
                if (toOpen != null) {
                    openDrawer(toOpen);
                }
            default:
                break;
        }
    }

    public void setDrawerLockMode(int lockMode, View drawerView) {
        if (isDrawerView(drawerView)) {
            setDrawerLockMode(lockMode, ((LayoutParams) drawerView.getLayoutParams()).gravity);
        } else {
            throw new IllegalArgumentException("View " + drawerView + " is not a " + "drawer with appropriate layout_gravity");
        }
    }

    public void setDrawerShadow(int resId, int gravity) {
        setDrawerShadow(getResources().getDrawable(resId), gravity);
    }

    public void setDrawerShadow(Drawable shadowDrawable, int gravity) {
        int absGravity = GravityCompat.getAbsoluteGravity(gravity, ViewCompat.getLayoutDirection(this));
        if ((absGravity & 3) == 3) {
            this.mShadowLeft = shadowDrawable;
            invalidate();
        }
        if ((absGravity & 5) == 5) {
            this.mShadowRight = shadowDrawable;
            invalidate();
        }
    }

    public void setDrawerTitle(int edgeGravity, CharSequence title) {
        int absGravity = GravityCompat.getAbsoluteGravity(edgeGravity, ViewCompat.getLayoutDirection(this));
        if (absGravity == 3) {
            this.mTitleLeft = title;
        } else if (absGravity == 5) {
            this.mTitleRight = title;
        }
    }

    void setDrawerViewOffset(View drawerView, float slideOffset) {
        LayoutParams lp = (LayoutParams) drawerView.getLayoutParams();
        if (slideOffset != lp.onScreen) {
            lp.onScreen = slideOffset;
            dispatchOnDrawerSlide(drawerView, slideOffset);
        }
    }

    public void setScrimColor(int color) {
        this.mScrimColor = color;
        invalidate();
    }

    void updateDrawerState(int forGravity, int activeState, View activeDrawer) {
        int leftState = this.mLeftDragger.getViewDragState();
        int rightState = this.mRightDragger.getViewDragState();
        int state = (leftState == 1 || rightState == 1) ? STATE_DRAGGING : (leftState == 2 || rightState == 2) ? STATE_SETTLING : STATE_IDLE;
        if (activeDrawer != null && activeState == 0) {
            LayoutParams lp = (LayoutParams) activeDrawer.getLayoutParams();
            if (lp.onScreen == 0.0f) {
                dispatchOnDrawerClosed(activeDrawer);
            } else if (lp.onScreen == 1.0f) {
                dispatchOnDrawerOpened(activeDrawer);
            }
        }
        if (state != this.mDrawerState) {
            this.mDrawerState = state;
            if (this.mListener != null) {
                this.mListener.onDrawerStateChanged(state);
            }
        }
    }
}