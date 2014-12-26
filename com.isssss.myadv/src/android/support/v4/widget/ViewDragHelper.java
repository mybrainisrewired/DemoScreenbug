package android.support.v4.widget;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewCompat;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import com.android.volley.DefaultRetryPolicy;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import java.util.Arrays;

public class ViewDragHelper {
    private static final int BASE_SETTLE_DURATION = 256;
    public static final int DIRECTION_ALL = 3;
    public static final int DIRECTION_HORIZONTAL = 1;
    public static final int DIRECTION_VERTICAL = 2;
    public static final int EDGE_ALL = 15;
    public static final int EDGE_BOTTOM = 8;
    public static final int EDGE_LEFT = 1;
    public static final int EDGE_RIGHT = 2;
    private static final int EDGE_SIZE = 20;
    public static final int EDGE_TOP = 4;
    public static final int INVALID_POINTER = -1;
    private static final int MAX_SETTLE_DURATION = 600;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_SETTLING = 2;
    private static final String TAG = "ViewDragHelper";
    private static final Interpolator sInterpolator;
    private int mActivePointerId;
    private final Callback mCallback;
    private View mCapturedView;
    private int mDragState;
    private int[] mEdgeDragsInProgress;
    private int[] mEdgeDragsLocked;
    private int mEdgeSize;
    private int[] mInitialEdgesTouched;
    private float[] mInitialMotionX;
    private float[] mInitialMotionY;
    private float[] mLastMotionX;
    private float[] mLastMotionY;
    private float mMaxVelocity;
    private float mMinVelocity;
    private final ViewGroup mParentView;
    private int mPointersDown;
    private boolean mReleaseInProgress;
    private ScrollerCompat mScroller;
    private final Runnable mSetIdleRunnable;
    private int mTouchSlop;
    private int mTrackingEdges;
    private VelocityTracker mVelocityTracker;

    public static abstract class Callback {
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return STATE_IDLE;
        }

        public int clampViewPositionVertical(View child, int top, int dy) {
            return STATE_IDLE;
        }

        public int getOrderedChildIndex(int index) {
            return index;
        }

        public int getViewHorizontalDragRange(View child) {
            return STATE_IDLE;
        }

        public int getViewVerticalDragRange(View child) {
            return STATE_IDLE;
        }

        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
        }

        public boolean onEdgeLock(int edgeFlags) {
            return false;
        }

        public void onEdgeTouched(int edgeFlags, int pointerId) {
        }

        public void onViewCaptured(View capturedChild, int activePointerId) {
        }

        public void onViewDragStateChanged(int state) {
        }

        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
        }

        public void onViewReleased(View releasedChild, float xvel, float yvel) {
        }

        public abstract boolean tryCaptureView(View view, int i);
    }

    static {
        sInterpolator = new Interpolator() {
            public float getInterpolation(float t) {
                t -= 1.0f;
                return (((t * t) * t) * t) * t + 1.0f;
            }
        };
    }

    private ViewDragHelper(Context context, ViewGroup forParent, Callback cb) {
        this.mActivePointerId = -1;
        this.mSetIdleRunnable = new Runnable() {
            public void run() {
                ViewDragHelper.this.setDragState(STATE_IDLE);
            }
        };
        if (forParent == null) {
            throw new IllegalArgumentException("Parent view may not be null");
        } else if (cb == null) {
            throw new IllegalArgumentException("Callback may not be null");
        } else {
            this.mParentView = forParent;
            this.mCallback = cb;
            ViewConfiguration vc = ViewConfiguration.get(context);
            this.mEdgeSize = (int) (20.0f * context.getResources().getDisplayMetrics().density + 0.5f);
            this.mTouchSlop = vc.getScaledTouchSlop();
            this.mMaxVelocity = (float) vc.getScaledMaximumFlingVelocity();
            this.mMinVelocity = (float) vc.getScaledMinimumFlingVelocity();
            this.mScroller = ScrollerCompat.create(context, sInterpolator);
        }
    }

    private boolean checkNewEdgeDrag(float delta, float odelta, int pointerId, int edge) {
        float absDelta = Math.abs(delta);
        float absODelta = Math.abs(odelta);
        if ((this.mInitialEdgesTouched[pointerId] & edge) != edge || (this.mTrackingEdges & edge) == 0 || (this.mEdgeDragsLocked[pointerId] & edge) == edge || (this.mEdgeDragsInProgress[pointerId] & edge) == edge) {
            return false;
        }
        if (absDelta <= ((float) this.mTouchSlop) && absODelta <= ((float) this.mTouchSlop)) {
            return false;
        }
        if (absDelta >= 0.5f * absODelta || !this.mCallback.onEdgeLock(edge)) {
            return (this.mEdgeDragsInProgress[pointerId] & edge) == 0 && absDelta > ((float) this.mTouchSlop);
        } else {
            int[] iArr = this.mEdgeDragsLocked;
            iArr[pointerId] = iArr[pointerId] | edge;
            return false;
        }
    }

    private boolean checkTouchSlop(View child, float dx, float dy) {
        if (child == null) {
            return false;
        }
        boolean checkHorizontal;
        boolean checkVertical;
        if (this.mCallback.getViewHorizontalDragRange(child) > 0) {
            checkHorizontal = true;
        } else {
            checkHorizontal = false;
        }
        if (this.mCallback.getViewVerticalDragRange(child) > 0) {
            checkVertical = true;
        } else {
            checkVertical = false;
        }
        if (checkHorizontal && checkVertical) {
            return (dx * dx) + (dy * dy) > ((float) (this.mTouchSlop * this.mTouchSlop));
        } else {
            if (checkHorizontal) {
                return Math.abs(dx) > ((float) this.mTouchSlop);
            } else {
                if (checkVertical) {
                    return Math.abs(dy) > ((float) this.mTouchSlop);
                } else {
                    return false;
                }
            }
        }
    }

    private float clampMag(float value, float absMin, float absMax) {
        float absValue = Math.abs(value);
        if (absValue < absMin) {
            return 0.0f;
        }
        if (absValue > absMax) {
            return value <= 0.0f ? -absMax : absMax;
        } else {
            return value;
        }
    }

    private int clampMag(int value, int absMin, int absMax) {
        int absValue = Math.abs(value);
        if (absValue < absMin) {
            return STATE_IDLE;
        }
        if (absValue > absMax) {
            return value <= 0 ? -absMax : absMax;
        } else {
            return value;
        }
    }

    private void clearMotionHistory() {
        if (this.mInitialMotionX != null) {
            Arrays.fill(this.mInitialMotionX, BitmapDescriptorFactory.HUE_RED);
            Arrays.fill(this.mInitialMotionY, BitmapDescriptorFactory.HUE_RED);
            Arrays.fill(this.mLastMotionX, BitmapDescriptorFactory.HUE_RED);
            Arrays.fill(this.mLastMotionY, BitmapDescriptorFactory.HUE_RED);
            Arrays.fill(this.mInitialEdgesTouched, STATE_IDLE);
            Arrays.fill(this.mEdgeDragsInProgress, STATE_IDLE);
            Arrays.fill(this.mEdgeDragsLocked, STATE_IDLE);
            this.mPointersDown = 0;
        }
    }

    private void clearMotionHistory(int pointerId) {
        if (this.mInitialMotionX != null) {
            this.mInitialMotionX[pointerId] = 0.0f;
            this.mInitialMotionY[pointerId] = 0.0f;
            this.mLastMotionX[pointerId] = 0.0f;
            this.mLastMotionY[pointerId] = 0.0f;
            this.mInitialEdgesTouched[pointerId] = 0;
            this.mEdgeDragsInProgress[pointerId] = 0;
            this.mEdgeDragsLocked[pointerId] = 0;
            this.mPointersDown &= (1 << pointerId) ^ -1;
        }
    }

    private int computeAxisDuration(int delta, int velocity, int motionRange) {
        if (delta == 0) {
            return STATE_IDLE;
        }
        int duration;
        int width = this.mParentView.getWidth();
        int halfWidth = width / 2;
        float distance = ((float) halfWidth) + ((float) halfWidth) * distanceInfluenceForSnapDuration(Math.min(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT, ((float) Math.abs(delta)) / ((float) width)));
        velocity = Math.abs(velocity);
        if (velocity > 0) {
            duration = Math.round(1000.0f * Math.abs(distance / ((float) velocity))) * 4;
        } else {
            duration = (int) (((((float) Math.abs(delta)) / ((float) motionRange)) + 1.0f) * 256.0f);
        }
        return Math.min(duration, MAX_SETTLE_DURATION);
    }

    private int computeSettleDuration(View child, int dx, int dy, int xvel, int yvel) {
        xvel = clampMag(xvel, (int) this.mMinVelocity, (int) this.mMaxVelocity);
        yvel = clampMag(yvel, (int) this.mMinVelocity, (int) this.mMaxVelocity);
        int absDx = Math.abs(dx);
        int absDy = Math.abs(dy);
        int absXVel = Math.abs(xvel);
        int absYVel = Math.abs(yvel);
        int addedVel = absXVel + absYVel;
        int addedDistance = absDx + absDy;
        return (int) (((float) computeAxisDuration(dx, xvel, this.mCallback.getViewHorizontalDragRange(child))) * (xvel != 0 ? ((float) absXVel) / ((float) addedVel) : ((float) absDx) / ((float) addedDistance)) + ((float) computeAxisDuration(dy, yvel, this.mCallback.getViewVerticalDragRange(child))) * (yvel != 0 ? ((float) absYVel) / ((float) addedVel) : ((float) absDy) / ((float) addedDistance)));
    }

    public static ViewDragHelper create(ViewGroup forParent, float sensitivity, Callback cb) {
        ViewDragHelper helper = create(forParent, cb);
        helper.mTouchSlop = (int) (((float) helper.mTouchSlop) * (1.0f / sensitivity));
        return helper;
    }

    public static ViewDragHelper create(ViewGroup forParent, Callback cb) {
        return new ViewDragHelper(forParent.getContext(), forParent, cb);
    }

    private void dispatchViewReleased(float xvel, float yvel) {
        this.mReleaseInProgress = true;
        this.mCallback.onViewReleased(this.mCapturedView, xvel, yvel);
        this.mReleaseInProgress = false;
        if (this.mDragState == 1) {
            setDragState(STATE_IDLE);
        }
    }

    private float distanceInfluenceForSnapDuration(float f) {
        return (float) Math.sin((double) ((float) (((double) (f - 0.5f)) * 0.4712389167638204d)));
    }

    private void dragTo(int left, int top, int dx, int dy) {
        int clampedX = left;
        int clampedY = top;
        int oldLeft = this.mCapturedView.getLeft();
        int oldTop = this.mCapturedView.getTop();
        if (dx != 0) {
            clampedX = this.mCallback.clampViewPositionHorizontal(this.mCapturedView, left, dx);
            this.mCapturedView.offsetLeftAndRight(clampedX - oldLeft);
        }
        if (dy != 0) {
            clampedY = this.mCallback.clampViewPositionVertical(this.mCapturedView, top, dy);
            this.mCapturedView.offsetTopAndBottom(clampedY - oldTop);
        }
        if (dx != 0 || dy != 0) {
            this.mCallback.onViewPositionChanged(this.mCapturedView, clampedX, clampedY, clampedX - oldLeft, clampedY - oldTop);
        }
    }

    private void ensureMotionHistorySizeForId(int pointerId) {
        if (this.mInitialMotionX == null || this.mInitialMotionX.length <= pointerId) {
            float[] imx = new float[(pointerId + 1)];
            float[] imy = new float[(pointerId + 1)];
            float[] lmx = new float[(pointerId + 1)];
            float[] lmy = new float[(pointerId + 1)];
            int[] iit = new int[(pointerId + 1)];
            int[] edip = new int[(pointerId + 1)];
            int[] edl = new int[(pointerId + 1)];
            if (this.mInitialMotionX != null) {
                System.arraycopy(this.mInitialMotionX, STATE_IDLE, imx, STATE_IDLE, this.mInitialMotionX.length);
                System.arraycopy(this.mInitialMotionY, STATE_IDLE, imy, STATE_IDLE, this.mInitialMotionY.length);
                System.arraycopy(this.mLastMotionX, STATE_IDLE, lmx, STATE_IDLE, this.mLastMotionX.length);
                System.arraycopy(this.mLastMotionY, STATE_IDLE, lmy, STATE_IDLE, this.mLastMotionY.length);
                System.arraycopy(this.mInitialEdgesTouched, STATE_IDLE, iit, STATE_IDLE, this.mInitialEdgesTouched.length);
                System.arraycopy(this.mEdgeDragsInProgress, STATE_IDLE, edip, STATE_IDLE, this.mEdgeDragsInProgress.length);
                System.arraycopy(this.mEdgeDragsLocked, STATE_IDLE, edl, STATE_IDLE, this.mEdgeDragsLocked.length);
            }
            this.mInitialMotionX = imx;
            this.mInitialMotionY = imy;
            this.mLastMotionX = lmx;
            this.mLastMotionY = lmy;
            this.mInitialEdgesTouched = iit;
            this.mEdgeDragsInProgress = edip;
            this.mEdgeDragsLocked = edl;
        }
    }

    private boolean forceSettleCapturedViewAt(int finalLeft, int finalTop, int xvel, int yvel) {
        int startLeft = this.mCapturedView.getLeft();
        int startTop = this.mCapturedView.getTop();
        int dx = finalLeft - startLeft;
        int dy = finalTop - startTop;
        if (dx == 0 && dy == 0) {
            this.mScroller.abortAnimation();
            setDragState(STATE_IDLE);
            return false;
        } else {
            this.mScroller.startScroll(startLeft, startTop, dx, dy, computeSettleDuration(this.mCapturedView, dx, dy, xvel, yvel));
            setDragState(STATE_SETTLING);
            return true;
        }
    }

    private int getEdgesTouched(int x, int y) {
        int result = STATE_IDLE;
        if (x < this.mParentView.getLeft() + this.mEdgeSize) {
            result = 0 | 1;
        }
        if (y < this.mParentView.getTop() + this.mEdgeSize) {
            result |= 4;
        }
        if (x > this.mParentView.getRight() - this.mEdgeSize) {
            result |= 2;
        }
        return y > this.mParentView.getBottom() - this.mEdgeSize ? result | 8 : result;
    }

    private void releaseViewForPointerUp() {
        this.mVelocityTracker.computeCurrentVelocity(GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE, this.mMaxVelocity);
        dispatchViewReleased(clampMag(VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, this.mActivePointerId), this.mMinVelocity, this.mMaxVelocity), clampMag(VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, this.mActivePointerId), this.mMinVelocity, this.mMaxVelocity));
    }

    private void reportNewEdgeDrags(float dx, float dy, int pointerId) {
        int dragsStarted = STATE_IDLE;
        if (checkNewEdgeDrag(dx, dy, pointerId, STATE_DRAGGING)) {
            dragsStarted = 0 | 1;
        }
        if (checkNewEdgeDrag(dy, dx, pointerId, EDGE_TOP)) {
            dragsStarted |= 4;
        }
        if (checkNewEdgeDrag(dx, dy, pointerId, STATE_SETTLING)) {
            dragsStarted |= 2;
        }
        if (checkNewEdgeDrag(dy, dx, pointerId, EDGE_BOTTOM)) {
            dragsStarted |= 8;
        }
        if (dragsStarted != 0) {
            int[] iArr = this.mEdgeDragsInProgress;
            iArr[pointerId] = iArr[pointerId] | dragsStarted;
            this.mCallback.onEdgeDragStarted(dragsStarted, pointerId);
        }
    }

    private void saveInitialMotion(float x, float y, int pointerId) {
        ensureMotionHistorySizeForId(pointerId);
        float[] fArr = this.mInitialMotionX;
        this.mLastMotionX[pointerId] = x;
        fArr[pointerId] = x;
        fArr = this.mInitialMotionY;
        this.mLastMotionY[pointerId] = y;
        fArr[pointerId] = y;
        this.mInitialEdgesTouched[pointerId] = getEdgesTouched((int) x, (int) y);
        this.mPointersDown |= 1 << pointerId;
    }

    private void saveLastMotion(MotionEvent ev) {
        int pointerCount = MotionEventCompat.getPointerCount(ev);
        int i = STATE_IDLE;
        while (i < pointerCount) {
            int pointerId = MotionEventCompat.getPointerId(ev, i);
            float x = MotionEventCompat.getX(ev, i);
            float y = MotionEventCompat.getY(ev, i);
            this.mLastMotionX[pointerId] = x;
            this.mLastMotionY[pointerId] = y;
            i++;
        }
    }

    public void abort() {
        cancel();
        if (this.mDragState == 2) {
            int oldX = this.mScroller.getCurrX();
            int oldY = this.mScroller.getCurrY();
            this.mScroller.abortAnimation();
            int newX = this.mScroller.getCurrX();
            int newY = this.mScroller.getCurrY();
            this.mCallback.onViewPositionChanged(this.mCapturedView, newX, newY, newX - oldX, newY - oldY);
        }
        setDragState(STATE_IDLE);
    }

    protected boolean canScroll(View v, boolean checkV, int dx, int dy, int x, int y) {
        if (v instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) v;
            int scrollX = v.getScrollX();
            int scrollY = v.getScrollY();
            int i = group.getChildCount() - 1;
            while (i >= 0) {
                View child = group.getChildAt(i);
                if (x + scrollX >= child.getLeft() && x + scrollX < child.getRight() && y + scrollY >= child.getTop() && y + scrollY < child.getBottom()) {
                    if (canScroll(child, true, dx, dy, x + scrollX - child.getLeft(), y + scrollY - child.getTop())) {
                        return true;
                    }
                }
                i--;
            }
        }
        return checkV && (ViewCompat.canScrollHorizontally(v, -dx) || ViewCompat.canScrollVertically(v, -dy));
    }

    public void cancel() {
        this.mActivePointerId = -1;
        clearMotionHistory();
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    public void captureChildView(View childView, int activePointerId) {
        if (childView.getParent() != this.mParentView) {
            throw new IllegalArgumentException("captureChildView: parameter must be a descendant of the ViewDragHelper's tracked parent view (" + this.mParentView + ")");
        }
        this.mCapturedView = childView;
        this.mActivePointerId = activePointerId;
        this.mCallback.onViewCaptured(childView, activePointerId);
        setDragState(STATE_DRAGGING);
    }

    public boolean checkTouchSlop(int directions) {
        int count = this.mInitialMotionX.length;
        int i = STATE_IDLE;
        while (i < count) {
            if (checkTouchSlop(directions, i)) {
                return true;
            }
            i++;
        }
        return false;
    }

    public boolean checkTouchSlop(int directions, int pointerId) {
        if (!isPointerDown(pointerId)) {
            return false;
        }
        boolean checkHorizontal;
        boolean checkVertical;
        if ((directions & 1) == 1) {
            checkHorizontal = true;
        } else {
            checkHorizontal = false;
        }
        if ((directions & 2) == 2) {
            checkVertical = true;
        } else {
            checkVertical = false;
        }
        float dx = this.mLastMotionX[pointerId] - this.mInitialMotionX[pointerId];
        float dy = this.mLastMotionY[pointerId] - this.mInitialMotionY[pointerId];
        if (checkHorizontal && checkVertical) {
            return (dx * dx) + (dy * dy) > ((float) (this.mTouchSlop * this.mTouchSlop));
        } else {
            if (checkHorizontal) {
                return Math.abs(dx) > ((float) this.mTouchSlop);
            } else {
                if (checkVertical) {
                    return Math.abs(dy) > ((float) this.mTouchSlop);
                } else {
                    return false;
                }
            }
        }
    }

    public boolean continueSettling(boolean deferCallbacks) {
        if (this.mDragState == 2) {
            boolean keepGoing = this.mScroller.computeScrollOffset();
            int x = this.mScroller.getCurrX();
            int y = this.mScroller.getCurrY();
            int dx = x - this.mCapturedView.getLeft();
            int dy = y - this.mCapturedView.getTop();
            if (dx != 0) {
                this.mCapturedView.offsetLeftAndRight(dx);
            }
            if (dy != 0) {
                this.mCapturedView.offsetTopAndBottom(dy);
            }
            if (!(dx == 0 && dy == 0)) {
                this.mCallback.onViewPositionChanged(this.mCapturedView, x, y, dx, dy);
            }
            if (keepGoing && x == this.mScroller.getFinalX() && y == this.mScroller.getFinalY()) {
                this.mScroller.abortAnimation();
                keepGoing = this.mScroller.isFinished();
            }
            if (!keepGoing) {
                if (deferCallbacks) {
                    this.mParentView.post(this.mSetIdleRunnable);
                } else {
                    setDragState(STATE_IDLE);
                }
            }
        }
        return this.mDragState == 2;
    }

    public View findTopChildUnder(int x, int y) {
        int i = this.mParentView.getChildCount() - 1;
        while (i >= 0) {
            View child = this.mParentView.getChildAt(this.mCallback.getOrderedChildIndex(i));
            if (x >= child.getLeft() && x < child.getRight() && y >= child.getTop() && y < child.getBottom()) {
                return child;
            }
            i--;
        }
        return null;
    }

    public void flingCapturedView(int minLeft, int minTop, int maxLeft, int maxTop) {
        if (this.mReleaseInProgress) {
            this.mScroller.fling(this.mCapturedView.getLeft(), this.mCapturedView.getTop(), (int) VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, this.mActivePointerId), (int) VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, this.mActivePointerId), minLeft, maxLeft, minTop, maxTop);
            setDragState(STATE_SETTLING);
        } else {
            throw new IllegalStateException("Cannot flingCapturedView outside of a call to Callback#onViewReleased");
        }
    }

    public int getActivePointerId() {
        return this.mActivePointerId;
    }

    public View getCapturedView() {
        return this.mCapturedView;
    }

    public int getEdgeSize() {
        return this.mEdgeSize;
    }

    public float getMinVelocity() {
        return this.mMinVelocity;
    }

    public int getTouchSlop() {
        return this.mTouchSlop;
    }

    public int getViewDragState() {
        return this.mDragState;
    }

    public boolean isCapturedViewUnder(int x, int y) {
        return isViewUnder(this.mCapturedView, x, y);
    }

    public boolean isEdgeTouched(int edges) {
        int count = this.mInitialEdgesTouched.length;
        int i = STATE_IDLE;
        while (i < count) {
            if (isEdgeTouched(edges, i)) {
                return true;
            }
            i++;
        }
        return false;
    }

    public boolean isEdgeTouched(int edges, int pointerId) {
        return isPointerDown(pointerId) && (this.mInitialEdgesTouched[pointerId] & edges) != 0;
    }

    public boolean isPointerDown(int pointerId) {
        return (this.mPointersDown & (1 << pointerId)) != 0;
    }

    public boolean isViewUnder(View view, int x, int y) {
        return view != null && x >= view.getLeft() && x < view.getRight() && y >= view.getTop() && y < view.getBottom();
    }

    public void processTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);
        int actionIndex = MotionEventCompat.getActionIndex(ev);
        if (action == 0) {
            cancel();
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(ev);
        float x;
        float y;
        int pointerId;
        View toCapture;
        int edgesTouched;
        int pointerCount;
        int i;
        switch (action) {
            case STATE_IDLE:
                x = ev.getX();
                y = ev.getY();
                pointerId = MotionEventCompat.getPointerId(ev, 0);
                toCapture = findTopChildUnder((int) x, (int) y);
                saveInitialMotion(x, y, pointerId);
                tryCaptureViewForDrag(toCapture, pointerId);
                edgesTouched = this.mInitialEdgesTouched[pointerId];
                if ((this.mTrackingEdges & edgesTouched) != 0) {
                    this.mCallback.onEdgeTouched(this.mTrackingEdges & edgesTouched, pointerId);
                }
            case STATE_DRAGGING:
                if (this.mDragState == 1) {
                    releaseViewForPointerUp();
                }
                cancel();
            case STATE_SETTLING:
                if (this.mDragState == 1) {
                    int index = MotionEventCompat.findPointerIndex(ev, this.mActivePointerId);
                    x = MotionEventCompat.getX(ev, index);
                    int idx = (int) (x - this.mLastMotionX[this.mActivePointerId]);
                    int idy = (int) (MotionEventCompat.getY(ev, index) - this.mLastMotionY[this.mActivePointerId]);
                    dragTo(this.mCapturedView.getLeft() + idx, this.mCapturedView.getTop() + idy, idx, idy);
                    saveLastMotion(ev);
                } else {
                    pointerCount = MotionEventCompat.getPointerCount(ev);
                    i = STATE_IDLE;
                    while (i < pointerCount) {
                        pointerId = MotionEventCompat.getPointerId(ev, i);
                        x = MotionEventCompat.getX(ev, i);
                        y = MotionEventCompat.getY(ev, i);
                        float dx = x - this.mInitialMotionX[pointerId];
                        float dy = y - this.mInitialMotionY[pointerId];
                        reportNewEdgeDrags(dx, dy, pointerId);
                        if (this.mDragState != 1) {
                            toCapture = findTopChildUnder((int) x, (int) y);
                            if (!checkTouchSlop(toCapture, dx, dy) || !tryCaptureViewForDrag(toCapture, pointerId)) {
                                i++;
                            }
                        }
                        saveLastMotion(ev);
                    }
                    saveLastMotion(ev);
                }
            case DIRECTION_ALL:
                if (this.mDragState == 1) {
                    dispatchViewReleased(0.0f, 0.0f);
                }
                cancel();
            case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                pointerId = MotionEventCompat.getPointerId(ev, actionIndex);
                x = MotionEventCompat.getX(ev, actionIndex);
                y = MotionEventCompat.getY(ev, actionIndex);
                saveInitialMotion(x, y, pointerId);
                if (this.mDragState == 0) {
                    tryCaptureViewForDrag(findTopChildUnder((int) x, (int) y), pointerId);
                    edgesTouched = this.mInitialEdgesTouched[pointerId];
                    if ((this.mTrackingEdges & edgesTouched) != 0) {
                        this.mCallback.onEdgeTouched(this.mTrackingEdges & edgesTouched, pointerId);
                    }
                } else {
                    if (isCapturedViewUnder((int) x, (int) y)) {
                        tryCaptureViewForDrag(this.mCapturedView, pointerId);
                    }
                }
            case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                pointerId = MotionEventCompat.getPointerId(ev, actionIndex);
                if (this.mDragState == 1 && pointerId == this.mActivePointerId) {
                    int newActivePointer = INVALID_POINTER;
                    pointerCount = MotionEventCompat.getPointerCount(ev);
                    i = STATE_IDLE;
                    while (i < pointerCount) {
                        int id = MotionEventCompat.getPointerId(ev, i);
                        if (id != this.mActivePointerId) {
                            x = MotionEventCompat.getX(ev, i);
                            if (findTopChildUnder((int) x, (int) MotionEventCompat.getY(ev, i)) == this.mCapturedView) {
                                if (tryCaptureViewForDrag(this.mCapturedView, id)) {
                                    newActivePointer = this.mActivePointerId;
                                    if (newActivePointer == -1) {
                                        releaseViewForPointerUp();
                                    }
                                }
                            }
                        }
                        i++;
                    }
                    if (newActivePointer == -1) {
                        releaseViewForPointerUp();
                    }
                }
                clearMotionHistory(pointerId);
            default:
                break;
        }
    }

    void setDragState(int state) {
        if (this.mDragState != state) {
            this.mDragState = state;
            this.mCallback.onViewDragStateChanged(state);
            if (state == 0) {
                this.mCapturedView = null;
            }
        }
    }

    public void setEdgeTrackingEnabled(int edgeFlags) {
        this.mTrackingEdges = edgeFlags;
    }

    public void setMinVelocity(float minVel) {
        this.mMinVelocity = minVel;
    }

    public boolean settleCapturedViewAt(int finalLeft, int finalTop) {
        if (this.mReleaseInProgress) {
            return forceSettleCapturedViewAt(finalLeft, finalTop, (int) VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, this.mActivePointerId), (int) VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, this.mActivePointerId));
        }
        throw new IllegalStateException("Cannot settleCapturedViewAt outside of a call to Callback#onViewReleased");
    }

    public boolean shouldInterceptTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);
        int actionIndex = MotionEventCompat.getActionIndex(ev);
        if (action == 0) {
            cancel();
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(ev);
        float x;
        float y;
        int pointerId;
        View toCapture;
        int edgesTouched;
        switch (action) {
            case STATE_IDLE:
                x = ev.getX();
                y = ev.getY();
                pointerId = MotionEventCompat.getPointerId(ev, STATE_IDLE);
                saveInitialMotion(x, y, pointerId);
                toCapture = findTopChildUnder((int) x, (int) y);
                if (toCapture == this.mCapturedView && this.mDragState == 2) {
                    tryCaptureViewForDrag(toCapture, pointerId);
                }
                edgesTouched = this.mInitialEdgesTouched[pointerId];
                if ((this.mTrackingEdges & edgesTouched) != 0) {
                    this.mCallback.onEdgeTouched(this.mTrackingEdges & edgesTouched, pointerId);
                }
                break;
            case STATE_DRAGGING:
            case DIRECTION_ALL:
                cancel();
                break;
            case STATE_SETTLING:
                int pointerCount = MotionEventCompat.getPointerCount(ev);
                int i = STATE_IDLE;
                while (i < pointerCount) {
                    pointerId = MotionEventCompat.getPointerId(ev, i);
                    x = MotionEventCompat.getX(ev, i);
                    y = MotionEventCompat.getY(ev, i);
                    float dx = x - this.mInitialMotionX[pointerId];
                    float dy = y - this.mInitialMotionY[pointerId];
                    reportNewEdgeDrags(dx, dy, pointerId);
                    if (this.mDragState != 1) {
                        toCapture = findTopChildUnder((int) x, (int) y);
                        if (toCapture == null || !checkTouchSlop(toCapture, dx, dy) || !tryCaptureViewForDrag(toCapture, pointerId)) {
                            i++;
                        }
                    }
                    saveLastMotion(ev);
                }
                saveLastMotion(ev);
                break;
            case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                pointerId = MotionEventCompat.getPointerId(ev, actionIndex);
                x = MotionEventCompat.getX(ev, actionIndex);
                y = MotionEventCompat.getY(ev, actionIndex);
                saveInitialMotion(x, y, pointerId);
                if (this.mDragState == 0) {
                    edgesTouched = this.mInitialEdgesTouched[pointerId];
                    if ((this.mTrackingEdges & edgesTouched) != 0) {
                        this.mCallback.onEdgeTouched(this.mTrackingEdges & edgesTouched, pointerId);
                    }
                } else if (this.mDragState == 2) {
                    toCapture = findTopChildUnder((int) x, (int) y);
                    if (toCapture == this.mCapturedView) {
                        tryCaptureViewForDrag(toCapture, pointerId);
                    }
                }
                break;
            case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                clearMotionHistory(MotionEventCompat.getPointerId(ev, actionIndex));
                break;
        }
        return this.mDragState == 1;
    }

    public boolean smoothSlideViewTo(View child, int finalLeft, int finalTop) {
        this.mCapturedView = child;
        this.mActivePointerId = -1;
        return forceSettleCapturedViewAt(finalLeft, finalTop, STATE_IDLE, STATE_IDLE);
    }

    boolean tryCaptureViewForDrag(View toCapture, int pointerId) {
        if (toCapture == this.mCapturedView && this.mActivePointerId == pointerId) {
            return true;
        }
        if (toCapture == null || !this.mCallback.tryCaptureView(toCapture, pointerId)) {
            return false;
        }
        this.mActivePointerId = pointerId;
        captureChildView(toCapture, pointerId);
        return true;
    }
}