package android.support.v4.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.ViewDragHelper.Callback;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import com.android.volley.DefaultRetryPolicy;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.millennialmedia.android.MMAdView;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class SlidingPaneLayout extends ViewGroup {
    private static final int DEFAULT_FADE_COLOR = -858993460;
    private static final int DEFAULT_OVERHANG_SIZE = 32;
    static final SlidingPanelLayoutImpl IMPL;
    private static final int MIN_FLING_VELOCITY = 400;
    private static final String TAG = "SlidingPaneLayout";
    private boolean mCanSlide;
    private int mCoveredFadeColor;
    private final ViewDragHelper mDragHelper;
    private boolean mFirstLayout;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private boolean mIsUnableToDrag;
    private final int mOverhangSize;
    private PanelSlideListener mPanelSlideListener;
    private int mParallaxBy;
    private float mParallaxOffset;
    private final ArrayList<DisableLayerRunnable> mPostedRunnables;
    private boolean mPreservedOpenState;
    private Drawable mShadowDrawableLeft;
    private Drawable mShadowDrawableRight;
    private float mSlideOffset;
    private int mSlideRange;
    private View mSlideableView;
    private int mSliderFadeColor;
    private final Rect mTmpRect;

    private class DisableLayerRunnable implements Runnable {
        final View mChildView;

        DisableLayerRunnable(View childView) {
            this.mChildView = childView;
        }

        public void run() {
            if (this.mChildView.getParent() == SlidingPaneLayout.this) {
                ViewCompat.setLayerType(this.mChildView, 0, null);
                SlidingPaneLayout.this.invalidateChildRegion(this.mChildView);
            }
            SlidingPaneLayout.this.mPostedRunnables.remove(this);
        }
    }

    public static class LayoutParams extends MarginLayoutParams {
        private static final int[] ATTRS;
        Paint dimPaint;
        boolean dimWhenOffset;
        boolean slideable;
        public float weight;

        static {
            ATTRS = new int[]{16843137};
        }

        public LayoutParams() {
            super(-1, -1);
            this.weight = 0.0f;
        }

        public LayoutParams(int width, int height) {
            super(width, height);
            this.weight = 0.0f;
        }

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            this.weight = 0.0f;
            TypedArray a = c.obtainStyledAttributes(attrs, ATTRS);
            this.weight = a.getFloat(0, BitmapDescriptorFactory.HUE_RED);
            a.recycle();
        }

        public LayoutParams(android.support.v4.widget.SlidingPaneLayout.LayoutParams source) {
            super(source);
            this.weight = 0.0f;
            this.weight = source.weight;
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
            super(source);
            this.weight = 0.0f;
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
            this.weight = 0.0f;
        }
    }

    public static interface PanelSlideListener {
        void onPanelClosed(View view);

        void onPanelOpened(View view);

        void onPanelSlide(View view, float f);
    }

    static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR;
        boolean isOpen;

        static {
            CREATOR = new Creator<SavedState>() {
                public SavedState createFromParcel(Parcel in) {
                    return new SavedState(null);
                }

                public SavedState[] newArray(int size) {
                    return new SavedState[size];
                }
            };
        }

        private SavedState(Parcel in) {
            super(in);
            this.isOpen = in.readInt() != 0;
        }

        SavedState(Parcelable superState) {
            super(superState);
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.isOpen ? 1 : 0);
        }
    }

    static interface SlidingPanelLayoutImpl {
        void invalidateChildRegion(SlidingPaneLayout slidingPaneLayout, View view);
    }

    class AccessibilityDelegate extends AccessibilityDelegateCompat {
        private final Rect mTmpRect;

        AccessibilityDelegate() {
            this.mTmpRect = new Rect();
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
            dest.setMovementGranularities(src.getMovementGranularities());
        }

        public boolean filter(View child) {
            return SlidingPaneLayout.this.isDimmed(child);
        }

        public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
            super.onInitializeAccessibilityEvent(host, event);
            event.setClassName(SlidingPaneLayout.class.getName());
        }

        public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
            AccessibilityNodeInfoCompat superNode = AccessibilityNodeInfoCompat.obtain(info);
            super.onInitializeAccessibilityNodeInfo(host, superNode);
            copyNodeInfoNoChildren(info, superNode);
            superNode.recycle();
            info.setClassName(SlidingPaneLayout.class.getName());
            info.setSource(host);
            ViewParent parent = ViewCompat.getParentForAccessibility(host);
            if (parent instanceof View) {
                info.setParent((View) parent);
            }
            int childCount = SlidingPaneLayout.this.getChildCount();
            int i = 0;
            while (i < childCount) {
                View child = SlidingPaneLayout.this.getChildAt(i);
                if (!filter(child) && child.getVisibility() == 0) {
                    ViewCompat.setImportantForAccessibility(child, 1);
                    info.addChild(child);
                }
                i++;
            }
        }

        public boolean onRequestSendAccessibilityEvent(ViewGroup host, View child, AccessibilityEvent event) {
            return !filter(child) ? super.onRequestSendAccessibilityEvent(host, child, event) : false;
        }
    }

    private class DragHelperCallback extends Callback {
        private DragHelperCallback() {
        }

        public int clampViewPositionHorizontal(View child, int left, int dx) {
            android.support.v4.widget.SlidingPaneLayout.LayoutParams lp = (android.support.v4.widget.SlidingPaneLayout.LayoutParams) SlidingPaneLayout.this.mSlideableView.getLayoutParams();
            int startBound;
            if (SlidingPaneLayout.this.isLayoutRtlSupport()) {
                startBound = SlidingPaneLayout.this.getWidth() - SlidingPaneLayout.this.getPaddingRight() + lp.rightMargin + SlidingPaneLayout.this.mSlideableView.getWidth();
                return Math.max(Math.min(left, startBound), startBound - SlidingPaneLayout.this.mSlideRange);
            } else {
                startBound = SlidingPaneLayout.this.getPaddingLeft() + lp.leftMargin;
                return Math.min(Math.max(left, startBound), startBound + SlidingPaneLayout.this.mSlideRange);
            }
        }

        public int clampViewPositionVertical(View child, int top, int dy) {
            return child.getTop();
        }

        public int getViewHorizontalDragRange(View child) {
            return SlidingPaneLayout.this.mSlideRange;
        }

        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            SlidingPaneLayout.this.mDragHelper.captureChildView(SlidingPaneLayout.this.mSlideableView, pointerId);
        }

        public void onViewCaptured(View capturedChild, int activePointerId) {
            SlidingPaneLayout.this.setAllChildrenVisible();
        }

        public void onViewDragStateChanged(int state) {
            if (SlidingPaneLayout.this.mDragHelper.getViewDragState() != 0) {
                return;
            }
            if (SlidingPaneLayout.this.mSlideOffset == 0.0f) {
                SlidingPaneLayout.this.updateObscuredViewsVisibility(SlidingPaneLayout.this.mSlideableView);
                SlidingPaneLayout.this.dispatchOnPanelClosed(SlidingPaneLayout.this.mSlideableView);
                SlidingPaneLayout.this.mPreservedOpenState = false;
            } else {
                SlidingPaneLayout.this.dispatchOnPanelOpened(SlidingPaneLayout.this.mSlideableView);
                SlidingPaneLayout.this.mPreservedOpenState = true;
            }
        }

        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            SlidingPaneLayout.this.onPanelDragged(left);
            SlidingPaneLayout.this.invalidate();
        }

        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            int left;
            android.support.v4.widget.SlidingPaneLayout.LayoutParams lp = (android.support.v4.widget.SlidingPaneLayout.LayoutParams) releasedChild.getLayoutParams();
            if (SlidingPaneLayout.this.isLayoutRtlSupport()) {
                int startToRight = SlidingPaneLayout.this.getPaddingRight() + lp.rightMargin;
                if (xvel < 0.0f || (xvel == 0.0f && SlidingPaneLayout.this.mSlideOffset > 0.5f)) {
                    startToRight += SlidingPaneLayout.this.mSlideRange;
                }
                left = SlidingPaneLayout.this.getWidth() - startToRight - SlidingPaneLayout.this.mSlideableView.getWidth();
            } else {
                left = SlidingPaneLayout.this.getPaddingLeft() + lp.leftMargin;
                if (xvel > 0.0f || (xvel == 0.0f && SlidingPaneLayout.this.mSlideOffset > 0.5f)) {
                    left += SlidingPaneLayout.this.mSlideRange;
                }
            }
            SlidingPaneLayout.this.mDragHelper.settleCapturedViewAt(left, releasedChild.getTop());
            SlidingPaneLayout.this.invalidate();
        }

        public boolean tryCaptureView(View child, int pointerId) {
            return SlidingPaneLayout.this.mIsUnableToDrag ? false : ((android.support.v4.widget.SlidingPaneLayout.LayoutParams) child.getLayoutParams()).slideable;
        }
    }

    public static class SimplePanelSlideListener implements android.support.v4.widget.SlidingPaneLayout.PanelSlideListener {
        public void onPanelClosed(View panel) {
        }

        public void onPanelOpened(View panel) {
        }

        public void onPanelSlide(View panel, float slideOffset) {
        }
    }

    static class SlidingPanelLayoutImplBase implements SlidingPanelLayoutImpl {
        SlidingPanelLayoutImplBase() {
        }

        public void invalidateChildRegion(SlidingPaneLayout parent, View child) {
            ViewCompat.postInvalidateOnAnimation(parent, child.getLeft(), child.getTop(), child.getRight(), child.getBottom());
        }
    }

    static class SlidingPanelLayoutImplJB extends SlidingPanelLayoutImplBase {
        private Method mGetDisplayList;
        private Field mRecreateDisplayList;

        SlidingPanelLayoutImplJB() {
            try {
                this.mGetDisplayList = View.class.getDeclaredMethod("getDisplayList", (Class[]) 0);
            } catch (NoSuchMethodException e) {
                Log.e(TAG, "Couldn't fetch getDisplayList method; dimming won't work right.", e);
            }
            try {
                this.mRecreateDisplayList = View.class.getDeclaredField("mRecreateDisplayList");
                this.mRecreateDisplayList.setAccessible(true);
            } catch (NoSuchFieldException e2) {
                Log.e(TAG, "Couldn't fetch mRecreateDisplayList field; dimming will be slow.", e2);
            }
        }

        public void invalidateChildRegion(SlidingPaneLayout parent, View child) {
            if (this.mGetDisplayList == null || this.mRecreateDisplayList == null) {
                child.invalidate();
            } else {
                try {
                    this.mRecreateDisplayList.setBoolean(child, true);
                    this.mGetDisplayList.invoke(child, (Object[]) 0);
                } catch (Exception e) {
                    Log.e(TAG, "Error refreshing display list state", e);
                }
                super.invalidateChildRegion(parent, child);
            }
        }
    }

    static class SlidingPanelLayoutImplJBMR1 extends SlidingPanelLayoutImplBase {
        SlidingPanelLayoutImplJBMR1() {
        }

        public void invalidateChildRegion(SlidingPaneLayout parent, View child) {
            ViewCompat.setLayerPaint(child, ((android.support.v4.widget.SlidingPaneLayout.LayoutParams) child.getLayoutParams()).dimPaint);
        }
    }

    static {
        int deviceVersion = VERSION.SDK_INT;
        if (deviceVersion >= 17) {
            IMPL = new SlidingPanelLayoutImplJBMR1();
        } else if (deviceVersion >= 16) {
            IMPL = new SlidingPanelLayoutImplJB();
        } else {
            IMPL = new SlidingPanelLayoutImplBase();
        }
    }

    public SlidingPaneLayout(Context context) {
        this(context, null);
    }

    public SlidingPaneLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingPaneLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mSliderFadeColor = -858993460;
        this.mFirstLayout = true;
        this.mTmpRect = new Rect();
        this.mPostedRunnables = new ArrayList();
        float density = context.getResources().getDisplayMetrics().density;
        this.mOverhangSize = (int) (32.0f * density + 0.5f);
        ViewConfiguration viewConfig = ViewConfiguration.get(context);
        setWillNotDraw(false);
        ViewCompat.setAccessibilityDelegate(this, new AccessibilityDelegate());
        ViewCompat.setImportantForAccessibility(this, 1);
        this.mDragHelper = ViewDragHelper.create(this, 0.5f, new DragHelperCallback(null));
        this.mDragHelper.setMinVelocity(400.0f * density);
    }

    private boolean closePane(View pane, int initialVelocity) {
        if (!this.mFirstLayout && !smoothSlideTo(BitmapDescriptorFactory.HUE_RED, initialVelocity)) {
            return false;
        }
        this.mPreservedOpenState = false;
        return true;
    }

    private void dimChildView(View v, float mag, int fadeColor) {
        LayoutParams lp = (LayoutParams) v.getLayoutParams();
        if (mag > 0.0f && fadeColor != 0) {
            int color = (((int) (((float) ((-16777216 & fadeColor) >>> 24)) * mag)) << 24) | (16777215 & fadeColor);
            if (lp.dimPaint == null) {
                lp.dimPaint = new Paint();
            }
            lp.dimPaint.setColorFilter(new PorterDuffColorFilter(color, Mode.SRC_OVER));
            if (ViewCompat.getLayerType(v) != 2) {
                ViewCompat.setLayerType(v, MMAdView.TRANSITION_UP, lp.dimPaint);
            }
            invalidateChildRegion(v);
        } else if (ViewCompat.getLayerType(v) != 0) {
            if (lp.dimPaint != null) {
                lp.dimPaint.setColorFilter(null);
            }
            DisableLayerRunnable dlr = new DisableLayerRunnable(v);
            this.mPostedRunnables.add(dlr);
            ViewCompat.postOnAnimation(this, dlr);
        }
    }

    private void invalidateChildRegion(View v) {
        IMPL.invalidateChildRegion(this, v);
    }

    private boolean isLayoutRtlSupport() {
        return ViewCompat.getLayoutDirection(this) == 1;
    }

    private void onPanelDragged(int newLeft) {
        if (this.mSlideableView == null) {
            this.mSlideOffset = 0.0f;
        } else {
            int newStart;
            boolean isLayoutRtl = isLayoutRtlSupport();
            LayoutParams lp = (LayoutParams) this.mSlideableView.getLayoutParams();
            int childWidth = this.mSlideableView.getWidth();
            if (isLayoutRtl) {
                newStart = getWidth() - newLeft - childWidth;
            } else {
                newStart = newLeft;
            }
            this.mSlideOffset = ((float) (newStart - ((isLayoutRtl ? getPaddingRight() : getPaddingLeft()) + (isLayoutRtl ? lp.rightMargin : lp.leftMargin)))) / ((float) this.mSlideRange);
            if (this.mParallaxBy != 0) {
                parallaxOtherViews(this.mSlideOffset);
            }
            if (lp.dimWhenOffset) {
                dimChildView(this.mSlideableView, this.mSlideOffset, this.mSliderFadeColor);
            }
            dispatchOnPanelSlide(this.mSlideableView);
        }
    }

    private boolean openPane(View pane, int initialVelocity) {
        if (!this.mFirstLayout && !smoothSlideTo(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT, initialVelocity)) {
            return false;
        }
        this.mPreservedOpenState = true;
        return true;
    }

    private void parallaxOtherViews(float slideOffset) {
        boolean dimViews;
        int childCount;
        int i;
        View v;
        int dx;
        boolean isLayoutRtl = isLayoutRtlSupport();
        LayoutParams slideLp = (LayoutParams) this.mSlideableView.getLayoutParams();
        if (slideLp.dimWhenOffset) {
            if ((isLayoutRtl ? slideLp.rightMargin : slideLp.leftMargin) <= 0) {
                dimViews = true;
                childCount = getChildCount();
                i = 0;
                while (i < childCount) {
                    v = getChildAt(i);
                    if (v != this.mSlideableView) {
                        int oldOffset = (int) ((1.0f - this.mParallaxOffset) * ((float) this.mParallaxBy));
                        this.mParallaxOffset = slideOffset;
                        dx = oldOffset - ((int) ((1.0f - slideOffset) * ((float) this.mParallaxBy)));
                        if (isLayoutRtl) {
                            dx = -dx;
                        }
                        v.offsetLeftAndRight(dx);
                        if (dimViews) {
                            dimChildView(v, isLayoutRtl ? this.mParallaxOffset - 1.0f : 1.0f - this.mParallaxOffset, this.mCoveredFadeColor);
                        }
                    }
                    i++;
                }
            }
        }
        dimViews = false;
        childCount = getChildCount();
        i = 0;
        while (i < childCount) {
            v = getChildAt(i);
            if (v != this.mSlideableView) {
                int oldOffset2 = (int) ((1.0f - this.mParallaxOffset) * ((float) this.mParallaxBy));
                this.mParallaxOffset = slideOffset;
                dx = oldOffset2 - ((int) ((1.0f - slideOffset) * ((float) this.mParallaxBy)));
                if (isLayoutRtl) {
                    dx = -dx;
                }
                v.offsetLeftAndRight(dx);
                if (dimViews) {
                    if (isLayoutRtl) {
                    }
                    dimChildView(v, isLayoutRtl ? this.mParallaxOffset - 1.0f : 1.0f - this.mParallaxOffset, this.mCoveredFadeColor);
                }
            }
            i++;
        }
    }

    private static boolean viewIsOpaque(View v) {
        if (ViewCompat.isOpaque(v)) {
            return true;
        }
        if (VERSION.SDK_INT >= 18) {
            return false;
        }
        Drawable bg = v.getBackground();
        if (bg != null) {
            return bg.getOpacity() == -1;
        } else {
            return false;
        }
    }

    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (v instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) v;
            int scrollX = v.getScrollX();
            int scrollY = v.getScrollY();
            int i = group.getChildCount() - 1;
            while (i >= 0) {
                View child = group.getChildAt(i);
                if (x + scrollX >= child.getLeft() && x + scrollX < child.getRight() && y + scrollY >= child.getTop() && y + scrollY < child.getBottom()) {
                    if (canScroll(child, true, dx, x + scrollX - child.getLeft(), y + scrollY - child.getTop())) {
                        return true;
                    }
                }
                i--;
            }
        }
        if (checkV) {
            if (!isLayoutRtlSupport()) {
                dx = -dx;
            }
            if (ViewCompat.canScrollHorizontally(v, dx)) {
                return true;
            }
        }
        return false;
    }

    @Deprecated
    public boolean canSlide() {
        return this.mCanSlide;
    }

    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams && super.checkLayoutParams(p);
    }

    public boolean closePane() {
        return closePane(this.mSlideableView, 0);
    }

    public void computeScroll() {
        if (!this.mDragHelper.continueSettling(true)) {
            return;
        }
        if (this.mCanSlide) {
            ViewCompat.postInvalidateOnAnimation(this);
        } else {
            this.mDragHelper.abort();
        }
    }

    void dispatchOnPanelClosed(View panel) {
        if (this.mPanelSlideListener != null) {
            this.mPanelSlideListener.onPanelClosed(panel);
        }
        sendAccessibilityEvent(DEFAULT_OVERHANG_SIZE);
    }

    void dispatchOnPanelOpened(View panel) {
        if (this.mPanelSlideListener != null) {
            this.mPanelSlideListener.onPanelOpened(panel);
        }
        sendAccessibilityEvent(DEFAULT_OVERHANG_SIZE);
    }

    void dispatchOnPanelSlide(View panel) {
        if (this.mPanelSlideListener != null) {
            this.mPanelSlideListener.onPanelSlide(panel, this.mSlideOffset);
        }
    }

    public void draw(Canvas c) {
        Drawable shadowDrawable;
        super.draw(c);
        if (isLayoutRtlSupport()) {
            shadowDrawable = this.mShadowDrawableRight;
        } else {
            shadowDrawable = this.mShadowDrawableLeft;
        }
        View shadowView = getChildCount() > 1 ? getChildAt(1) : null;
        if (shadowView != null && shadowDrawable != null) {
            int left;
            int right;
            int top = shadowView.getTop();
            int bottom = shadowView.getBottom();
            int shadowWidth = shadowDrawable.getIntrinsicWidth();
            if (isLayoutRtlSupport()) {
                left = shadowView.getRight();
                right = left + shadowWidth;
            } else {
                right = shadowView.getLeft();
                left = right - shadowWidth;
            }
            shadowDrawable.setBounds(left, top, right, bottom);
            shadowDrawable.draw(c);
        }
    }

    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean result;
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        int save = canvas.save(MMAdView.TRANSITION_UP);
        if (!(!this.mCanSlide || lp.slideable || this.mSlideableView == null)) {
            canvas.getClipBounds(this.mTmpRect);
            if (isLayoutRtlSupport()) {
                this.mTmpRect.left = Math.max(this.mTmpRect.left, this.mSlideableView.getRight());
            } else {
                this.mTmpRect.right = Math.min(this.mTmpRect.right, this.mSlideableView.getLeft());
            }
            canvas.clipRect(this.mTmpRect);
        }
        if (VERSION.SDK_INT >= 11) {
            result = super.drawChild(canvas, child, drawingTime);
        } else if (!lp.dimWhenOffset || this.mSlideOffset <= 0.0f) {
            if (child.isDrawingCacheEnabled()) {
                child.setDrawingCacheEnabled(false);
            }
            result = super.drawChild(canvas, child, drawingTime);
        } else {
            if (!child.isDrawingCacheEnabled()) {
                child.setDrawingCacheEnabled(true);
            }
            Bitmap cache = child.getDrawingCache();
            if (cache != null) {
                canvas.drawBitmap(cache, (float) child.getLeft(), (float) child.getTop(), lp.dimPaint);
                result = false;
            } else {
                Log.e(TAG, "drawChild: child view " + child + " returned null drawing cache");
                result = super.drawChild(canvas, child, drawingTime);
            }
        }
        canvas.restoreToCount(save);
        return result;
    }

    protected android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return p instanceof MarginLayoutParams ? new LayoutParams((MarginLayoutParams) p) : new LayoutParams(p);
    }

    public int getCoveredFadeColor() {
        return this.mCoveredFadeColor;
    }

    public int getParallaxDistance() {
        return this.mParallaxBy;
    }

    public int getSliderFadeColor() {
        return this.mSliderFadeColor;
    }

    boolean isDimmed(View child) {
        if (child == null) {
            return false;
        }
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        return this.mCanSlide && lp.dimWhenOffset && this.mSlideOffset > 0.0f;
    }

    public boolean isOpen() {
        return !this.mCanSlide || this.mSlideOffset == 1.0f;
    }

    public boolean isSlideable() {
        return this.mCanSlide;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mFirstLayout = true;
        int i = 0;
        int count = this.mPostedRunnables.size();
        while (i < count) {
            ((DisableLayerRunnable) this.mPostedRunnables.get(i)).run();
            i++;
        }
        this.mPostedRunnables.clear();
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);
        if (!this.mCanSlide && action == 0 && getChildCount() > 1) {
            View secondChild = getChildAt(1);
            if (secondChild != null) {
                this.mPreservedOpenState = !this.mDragHelper.isViewUnder(secondChild, (int) ev.getX(), (int) ev.getY());
            }
        }
        if (!this.mCanSlide || (this.mIsUnableToDrag && action != 0)) {
            this.mDragHelper.cancel();
            return super.onInterceptTouchEvent(ev);
        } else if (action == 3 || action == 1) {
            this.mDragHelper.cancel();
            return false;
        } else {
            boolean interceptTap = false;
            float x;
            float y;
            switch (action) {
                case MMAdView.TRANSITION_NONE:
                    this.mIsUnableToDrag = false;
                    x = ev.getX();
                    y = ev.getY();
                    this.mInitialMotionX = x;
                    this.mInitialMotionY = y;
                    if (this.mDragHelper.isViewUnder(this.mSlideableView, (int) x, (int) y) && isDimmed(this.mSlideableView)) {
                        interceptTap = true;
                    }
                    break;
                case MMAdView.TRANSITION_UP:
                    x = ev.getX();
                    y = ev.getY();
                    float adx = Math.abs(x - this.mInitialMotionX);
                    float ady = Math.abs(y - this.mInitialMotionY);
                    if (adx > ((float) this.mDragHelper.getTouchSlop()) && ady > adx) {
                        this.mDragHelper.cancel();
                        this.mIsUnableToDrag = true;
                        return false;
                    }
            }
            return this.mDragHelper.shouldInterceptTouchEvent(ev) || interceptTap;
        }
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        boolean isLayoutRtl = isLayoutRtlSupport();
        if (isLayoutRtl) {
            this.mDragHelper.setEdgeTrackingEnabled(MMAdView.TRANSITION_UP);
        } else {
            this.mDragHelper.setEdgeTrackingEnabled(1);
        }
        int width = r - l;
        int paddingStart = isLayoutRtl ? getPaddingRight() : getPaddingLeft();
        int paddingEnd = isLayoutRtl ? getPaddingLeft() : getPaddingRight();
        int paddingTop = getPaddingTop();
        int childCount = getChildCount();
        int xStart = paddingStart;
        int nextXStart = xStart;
        if (this.mFirstLayout) {
            float f = (this.mCanSlide && this.mPreservedOpenState) ? 1065353216 : 0;
            this.mSlideOffset = f;
        }
        int i = 0;
        while (i < childCount) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                int childRight;
                int childLeft;
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                int childWidth = child.getMeasuredWidth();
                int offset = 0;
                if (lp.slideable) {
                    int range = Math.min(nextXStart, width - paddingEnd - this.mOverhangSize) - xStart - lp.leftMargin + lp.rightMargin;
                    this.mSlideRange = range;
                    int lpMargin = isLayoutRtl ? lp.rightMargin : lp.leftMargin;
                    lp.dimWhenOffset = ((xStart + lpMargin) + range) + (childWidth / 2) > width - paddingEnd ? 1 : 0;
                    int pos = (int) (((float) range) * this.mSlideOffset);
                    xStart += pos + lpMargin;
                    this.mSlideOffset = ((float) pos) / ((float) this.mSlideRange);
                } else if (!this.mCanSlide || this.mParallaxBy == 0) {
                    xStart = nextXStart;
                } else {
                    offset = (int) ((1.0f - this.mSlideOffset) * ((float) this.mParallaxBy));
                    xStart = nextXStart;
                }
                if (isLayoutRtl) {
                    childRight = width - xStart + offset;
                    childLeft = childRight - childWidth;
                } else {
                    childLeft = xStart - offset;
                    childRight = childLeft + childWidth;
                }
                child.layout(childLeft, paddingTop, childRight, paddingTop + child.getMeasuredHeight());
                nextXStart += child.getWidth();
            }
            i++;
        }
        if (this.mFirstLayout) {
            if (this.mCanSlide) {
                if (this.mParallaxBy != 0) {
                    parallaxOtherViews(this.mSlideOffset);
                }
                if (((LayoutParams) this.mSlideableView.getLayoutParams()).dimWhenOffset) {
                    dimChildView(this.mSlideableView, this.mSlideOffset, this.mSliderFadeColor);
                }
            } else {
                i = 0;
                while (i < childCount) {
                    dimChildView(getChildAt(i), 0.0f, this.mSliderFadeColor);
                    i++;
                }
            }
            updateObscuredViewsVisibility(this.mSlideableView);
        }
        this.mFirstLayout = false;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected void onMeasure(int r32_widthMeasureSpec, int r33_heightMeasureSpec) {
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.widget.SlidingPaneLayout.onMeasure(int, int):void");
        /*
        r31 = this;
        r25 = android.view.View.MeasureSpec.getMode(r32);
        r27 = android.view.View.MeasureSpec.getSize(r32);
        r12 = android.view.View.MeasureSpec.getMode(r33);
        r13 = android.view.View.MeasureSpec.getSize(r33);
        r29 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r0 = r25;
        r1 = r29;
        if (r0 == r1) goto L_0x008e;
    L_0x0018:
        r29 = r31.isInEditMode();
        if (r29 == 0) goto L_0x0086;
    L_0x001e:
        r29 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r0 = r25;
        r1 = r29;
        if (r0 != r1) goto L_0x007f;
    L_0x0026:
        r25 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
    L_0x0028:
        r16 = 0;
        r18 = -1;
        switch(r12) {
            case -2147483648: goto L_0x00b5;
            case 1073741824: goto L_0x00a5;
            default: goto L_0x002f;
        };
    L_0x002f:
        r23 = 0;
        r4 = 0;
        r29 = r31.getPaddingLeft();
        r29 = r27 - r29;
        r30 = r31.getPaddingRight();
        r24 = r29 - r30;
        r26 = r24;
        r6 = r31.getChildCount();
        r29 = 2;
        r0 = r29;
        if (r6 <= r0) goto L_0x0051;
    L_0x004a:
        r29 = "SlidingPaneLayout";
        r30 = "onMeasure: More than two child views are not supported.";
        android.util.Log.e(r29, r30);
    L_0x0051:
        r29 = 0;
        r0 = r29;
        r1 = r31;
        r1.mSlideableView = r0;
        r15 = 0;
    L_0x005a:
        if (r15 >= r6) goto L_0x019f;
    L_0x005c:
        r0 = r31;
        r5 = r0.getChildAt(r15);
        r17 = r5.getLayoutParams();
        r17 = (android.support.v4.widget.SlidingPaneLayout.LayoutParams) r17;
        r29 = r5.getVisibility();
        r30 = 8;
        r0 = r29;
        r1 = r30;
        if (r0 != r1) goto L_0x00c3;
    L_0x0074:
        r29 = 0;
        r0 = r29;
        r1 = r17;
        r1.dimWhenOffset = r0;
    L_0x007c:
        r15 = r15 + 1;
        goto L_0x005a;
    L_0x007f:
        if (r25 != 0) goto L_0x0028;
    L_0x0081:
        r25 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r27 = 300; // 0x12c float:4.2E-43 double:1.48E-321;
        goto L_0x0028;
    L_0x0086:
        r29 = new java.lang.IllegalStateException;
        r30 = "Width must have an exact value or MATCH_PARENT";
        r29.<init>(r30);
        throw r29;
    L_0x008e:
        if (r12 != 0) goto L_0x0028;
    L_0x0090:
        r29 = r31.isInEditMode();
        if (r29 == 0) goto L_0x009d;
    L_0x0096:
        if (r12 != 0) goto L_0x0028;
    L_0x0098:
        r12 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r13 = 300; // 0x12c float:4.2E-43 double:1.48E-321;
        goto L_0x0028;
    L_0x009d:
        r29 = new java.lang.IllegalStateException;
        r30 = "Height must not be UNSPECIFIED";
        r29.<init>(r30);
        throw r29;
    L_0x00a5:
        r29 = r31.getPaddingTop();
        r29 = r13 - r29;
        r30 = r31.getPaddingBottom();
        r18 = r29 - r30;
        r16 = r18;
        goto L_0x002f;
    L_0x00b5:
        r29 = r31.getPaddingTop();
        r29 = r13 - r29;
        r30 = r31.getPaddingBottom();
        r18 = r29 - r30;
        goto L_0x002f;
    L_0x00c3:
        r0 = r17;
        r0 = r0.weight;
        r29 = r0;
        r30 = 0;
        r29 = (r29 > r30 ? 1 : (r29 == r30 ? 0 : -1));
        if (r29 <= 0) goto L_0x00df;
    L_0x00cf:
        r0 = r17;
        r0 = r0.weight;
        r29 = r0;
        r23 = r23 + r29;
        r0 = r17;
        r0 = r0.width;
        r29 = r0;
        if (r29 == 0) goto L_0x007c;
    L_0x00df:
        r0 = r17;
        r0 = r0.leftMargin;
        r29 = r0;
        r0 = r17;
        r0 = r0.rightMargin;
        r30 = r0;
        r14 = r29 + r30;
        r0 = r17;
        r0 = r0.width;
        r29 = r0;
        r30 = -2;
        r0 = r29;
        r1 = r30;
        if (r0 != r1) goto L_0x0152;
    L_0x00fb:
        r29 = r24 - r14;
        r30 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r10 = android.view.View.MeasureSpec.makeMeasureSpec(r29, r30);
    L_0x0103:
        r0 = r17;
        r0 = r0.height;
        r29 = r0;
        r30 = -2;
        r0 = r29;
        r1 = r30;
        if (r0 != r1) goto L_0x0176;
    L_0x0111:
        r29 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r0 = r18;
        r1 = r29;
        r8 = android.view.View.MeasureSpec.makeMeasureSpec(r0, r1);
    L_0x011b:
        r5.measure(r10, r8);
        r9 = r5.getMeasuredWidth();
        r7 = r5.getMeasuredHeight();
        r29 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r0 = r29;
        if (r12 != r0) goto L_0x0136;
    L_0x012c:
        r0 = r16;
        if (r7 <= r0) goto L_0x0136;
    L_0x0130:
        r0 = r18;
        r16 = java.lang.Math.min(r7, r0);
    L_0x0136:
        r26 = r26 - r9;
        if (r26 >= 0) goto L_0x019c;
    L_0x013a:
        r29 = 1;
    L_0x013c:
        r0 = r29;
        r1 = r17;
        r1.slideable = r0;
        r4 = r4 | r29;
        r0 = r17;
        r0 = r0.slideable;
        r29 = r0;
        if (r29 == 0) goto L_0x007c;
    L_0x014c:
        r0 = r31;
        r0.mSlideableView = r5;
        goto L_0x007c;
    L_0x0152:
        r0 = r17;
        r0 = r0.width;
        r29 = r0;
        r30 = -1;
        r0 = r29;
        r1 = r30;
        if (r0 != r1) goto L_0x0169;
    L_0x0160:
        r29 = r24 - r14;
        r30 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r10 = android.view.View.MeasureSpec.makeMeasureSpec(r29, r30);
        goto L_0x0103;
    L_0x0169:
        r0 = r17;
        r0 = r0.width;
        r29 = r0;
        r30 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r10 = android.view.View.MeasureSpec.makeMeasureSpec(r29, r30);
        goto L_0x0103;
    L_0x0176:
        r0 = r17;
        r0 = r0.height;
        r29 = r0;
        r30 = -1;
        r0 = r29;
        r1 = r30;
        if (r0 != r1) goto L_0x018f;
    L_0x0184:
        r29 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r0 = r18;
        r1 = r29;
        r8 = android.view.View.MeasureSpec.makeMeasureSpec(r0, r1);
        goto L_0x011b;
    L_0x018f:
        r0 = r17;
        r0 = r0.height;
        r29 = r0;
        r30 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r8 = android.view.View.MeasureSpec.makeMeasureSpec(r29, r30);
        goto L_0x011b;
    L_0x019c:
        r29 = 0;
        goto L_0x013c;
    L_0x019f:
        if (r4 != 0) goto L_0x01a7;
    L_0x01a1:
        r29 = 0;
        r29 = (r23 > r29 ? 1 : (r23 == r29 ? 0 : -1));
        if (r29 <= 0) goto L_0x0323;
    L_0x01a7:
        r0 = r31;
        r0 = r0.mOverhangSize;
        r29 = r0;
        r11 = r24 - r29;
        r15 = 0;
    L_0x01b0:
        if (r15 >= r6) goto L_0x0323;
    L_0x01b2:
        r0 = r31;
        r5 = r0.getChildAt(r15);
        r29 = r5.getVisibility();
        r30 = 8;
        r0 = r29;
        r1 = r30;
        if (r0 != r1) goto L_0x01c7;
    L_0x01c4:
        r15 = r15 + 1;
        goto L_0x01b0;
    L_0x01c7:
        r17 = r5.getLayoutParams();
        r17 = (android.support.v4.widget.SlidingPaneLayout.LayoutParams) r17;
        r29 = r5.getVisibility();
        r30 = 8;
        r0 = r29;
        r1 = r30;
        if (r0 == r1) goto L_0x01c4;
    L_0x01d9:
        r0 = r17;
        r0 = r0.width;
        r29 = r0;
        if (r29 != 0) goto L_0x023d;
    L_0x01e1:
        r0 = r17;
        r0 = r0.weight;
        r29 = r0;
        r30 = 0;
        r29 = (r29 > r30 ? 1 : (r29 == r30 ? 0 : -1));
        if (r29 <= 0) goto L_0x023d;
    L_0x01ed:
        r22 = 1;
    L_0x01ef:
        if (r22 == 0) goto L_0x0240;
    L_0x01f1:
        r20 = 0;
    L_0x01f3:
        if (r4 == 0) goto L_0x0276;
    L_0x01f5:
        r0 = r31;
        r0 = r0.mSlideableView;
        r29 = r0;
        r0 = r29;
        if (r5 == r0) goto L_0x0276;
    L_0x01ff:
        r0 = r17;
        r0 = r0.width;
        r29 = r0;
        if (r29 >= 0) goto L_0x01c4;
    L_0x0207:
        r0 = r20;
        if (r0 > r11) goto L_0x0217;
    L_0x020b:
        r0 = r17;
        r0 = r0.weight;
        r29 = r0;
        r30 = 0;
        r29 = (r29 > r30 ? 1 : (r29 == r30 ? 0 : -1));
        if (r29 <= 0) goto L_0x01c4;
    L_0x0217:
        if (r22 == 0) goto L_0x026b;
    L_0x0219:
        r0 = r17;
        r0 = r0.height;
        r29 = r0;
        r30 = -2;
        r0 = r29;
        r1 = r30;
        if (r0 != r1) goto L_0x0245;
    L_0x0227:
        r29 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r0 = r18;
        r1 = r29;
        r8 = android.view.View.MeasureSpec.makeMeasureSpec(r0, r1);
    L_0x0231:
        r29 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r0 = r29;
        r10 = android.view.View.MeasureSpec.makeMeasureSpec(r11, r0);
        r5.measure(r10, r8);
        goto L_0x01c4;
    L_0x023d:
        r22 = 0;
        goto L_0x01ef;
    L_0x0240:
        r20 = r5.getMeasuredWidth();
        goto L_0x01f3;
    L_0x0245:
        r0 = r17;
        r0 = r0.height;
        r29 = r0;
        r30 = -1;
        r0 = r29;
        r1 = r30;
        if (r0 != r1) goto L_0x025e;
    L_0x0253:
        r29 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r0 = r18;
        r1 = r29;
        r8 = android.view.View.MeasureSpec.makeMeasureSpec(r0, r1);
        goto L_0x0231;
    L_0x025e:
        r0 = r17;
        r0 = r0.height;
        r29 = r0;
        r30 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r8 = android.view.View.MeasureSpec.makeMeasureSpec(r29, r30);
        goto L_0x0231;
    L_0x026b:
        r29 = r5.getMeasuredHeight();
        r30 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r8 = android.view.View.MeasureSpec.makeMeasureSpec(r29, r30);
        goto L_0x0231;
    L_0x0276:
        r0 = r17;
        r0 = r0.weight;
        r29 = r0;
        r30 = 0;
        r29 = (r29 > r30 ? 1 : (r29 == r30 ? 0 : -1));
        if (r29 <= 0) goto L_0x01c4;
    L_0x0282:
        r0 = r17;
        r0 = r0.width;
        r29 = r0;
        if (r29 != 0) goto L_0x02ef;
    L_0x028a:
        r0 = r17;
        r0 = r0.height;
        r29 = r0;
        r30 = -2;
        r0 = r29;
        r1 = r30;
        if (r0 != r1) goto L_0x02c9;
    L_0x0298:
        r29 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r0 = r18;
        r1 = r29;
        r8 = android.view.View.MeasureSpec.makeMeasureSpec(r0, r1);
    L_0x02a2:
        if (r4 == 0) goto L_0x02fa;
    L_0x02a4:
        r0 = r17;
        r0 = r0.leftMargin;
        r29 = r0;
        r0 = r17;
        r0 = r0.rightMargin;
        r30 = r0;
        r14 = r29 + r30;
        r21 = r24 - r14;
        r29 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r0 = r21;
        r1 = r29;
        r10 = android.view.View.MeasureSpec.makeMeasureSpec(r0, r1);
        r0 = r20;
        r1 = r21;
        if (r0 == r1) goto L_0x01c4;
    L_0x02c4:
        r5.measure(r10, r8);
        goto L_0x01c4;
    L_0x02c9:
        r0 = r17;
        r0 = r0.height;
        r29 = r0;
        r30 = -1;
        r0 = r29;
        r1 = r30;
        if (r0 != r1) goto L_0x02e2;
    L_0x02d7:
        r29 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r0 = r18;
        r1 = r29;
        r8 = android.view.View.MeasureSpec.makeMeasureSpec(r0, r1);
        goto L_0x02a2;
    L_0x02e2:
        r0 = r17;
        r0 = r0.height;
        r29 = r0;
        r30 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r8 = android.view.View.MeasureSpec.makeMeasureSpec(r29, r30);
        goto L_0x02a2;
    L_0x02ef:
        r29 = r5.getMeasuredHeight();
        r30 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r8 = android.view.View.MeasureSpec.makeMeasureSpec(r29, r30);
        goto L_0x02a2;
    L_0x02fa:
        r29 = 0;
        r0 = r29;
        r1 = r26;
        r28 = java.lang.Math.max(r0, r1);
        r0 = r17;
        r0 = r0.weight;
        r29 = r0;
        r0 = r28;
        r0 = (float) r0;
        r30 = r0;
        r29 = r29 * r30;
        r29 = r29 / r23;
        r0 = r29;
        r3 = (int) r0;
        r29 = r20 + r3;
        r30 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r10 = android.view.View.MeasureSpec.makeMeasureSpec(r29, r30);
        r5.measure(r10, r8);
        goto L_0x01c4;
    L_0x0323:
        r20 = r27;
        r29 = r31.getPaddingTop();
        r29 = r29 + r16;
        r30 = r31.getPaddingBottom();
        r19 = r29 + r30;
        r0 = r31;
        r1 = r20;
        r2 = r19;
        r0.setMeasuredDimension(r1, r2);
        r0 = r31;
        r0.mCanSlide = r4;
        r0 = r31;
        r0 = r0.mDragHelper;
        r29 = r0;
        r29 = r29.getViewDragState();
        if (r29 == 0) goto L_0x0355;
    L_0x034a:
        if (r4 != 0) goto L_0x0355;
    L_0x034c:
        r0 = r31;
        r0 = r0.mDragHelper;
        r29 = r0;
        r29.abort();
    L_0x0355:
        return;
        */
    }

    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        if (ss.isOpen) {
            openPane();
        } else {
            closePane();
        }
        this.mPreservedOpenState = ss.isOpen;
    }

    protected Parcelable onSaveInstanceState() {
        SavedState ss = new SavedState(super.onSaveInstanceState());
        ss.isOpen = isSlideable() ? isOpen() : this.mPreservedOpenState;
        return ss;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw) {
            this.mFirstLayout = true;
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (!this.mCanSlide) {
            return super.onTouchEvent(ev);
        }
        this.mDragHelper.processTouchEvent(ev);
        float x;
        float y;
        switch (ev.getAction() & 255) {
            case MMAdView.TRANSITION_NONE:
                x = ev.getX();
                y = ev.getY();
                this.mInitialMotionX = x;
                this.mInitialMotionY = y;
                return true;
            case MMAdView.TRANSITION_FADE:
                if (!isDimmed(this.mSlideableView)) {
                    return true;
                }
                x = ev.getX();
                y = ev.getY();
                float dx = x - this.mInitialMotionX;
                float dy = y - this.mInitialMotionY;
                int slop = this.mDragHelper.getTouchSlop();
                if (dx * dx + dy * dy >= ((float) (slop * slop)) || !this.mDragHelper.isViewUnder(this.mSlideableView, (int) x, (int) y)) {
                    return true;
                }
                closePane(this.mSlideableView, 0);
                return true;
            default:
                return true;
        }
    }

    public boolean openPane() {
        return openPane(this.mSlideableView, 0);
    }

    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child, focused);
        if (!isInTouchMode() && !this.mCanSlide) {
            this.mPreservedOpenState = child == this.mSlideableView;
        }
    }

    void setAllChildrenVisible() {
        int i = 0;
        int childCount = getChildCount();
        while (i < childCount) {
            View child = getChildAt(i);
            if (child.getVisibility() == 4) {
                child.setVisibility(0);
            }
            i++;
        }
    }

    public void setCoveredFadeColor(int color) {
        this.mCoveredFadeColor = color;
    }

    public void setPanelSlideListener(PanelSlideListener listener) {
        this.mPanelSlideListener = listener;
    }

    public void setParallaxDistance(int parallaxBy) {
        this.mParallaxBy = parallaxBy;
        requestLayout();
    }

    @Deprecated
    public void setShadowDrawable(Drawable d) {
        setShadowDrawableLeft(d);
    }

    public void setShadowDrawableLeft(Drawable d) {
        this.mShadowDrawableLeft = d;
    }

    public void setShadowDrawableRight(Drawable d) {
        this.mShadowDrawableRight = d;
    }

    @Deprecated
    public void setShadowResource(int resId) {
        setShadowDrawable(getResources().getDrawable(resId));
    }

    public void setShadowResourceLeft(int resId) {
        setShadowDrawableLeft(getResources().getDrawable(resId));
    }

    public void setShadowResourceRight(int resId) {
        setShadowDrawableRight(getResources().getDrawable(resId));
    }

    public void setSliderFadeColor(int color) {
        this.mSliderFadeColor = color;
    }

    @Deprecated
    public void smoothSlideClosed() {
        closePane();
    }

    @Deprecated
    public void smoothSlideOpen() {
        openPane();
    }

    boolean smoothSlideTo(float slideOffset, int velocity) {
        if (!this.mCanSlide) {
            return false;
        }
        int x;
        LayoutParams lp = (LayoutParams) this.mSlideableView.getLayoutParams();
        if (isLayoutRtlSupport()) {
            x = (int) (((float) getWidth()) - ((float) (getPaddingRight() + lp.rightMargin)) + ((float) this.mSlideRange) * slideOffset + ((float) this.mSlideableView.getWidth()));
        } else {
            x = (int) (((float) (getPaddingLeft() + lp.leftMargin)) + ((float) this.mSlideRange) * slideOffset);
        }
        if (!this.mDragHelper.smoothSlideViewTo(this.mSlideableView, x, this.mSlideableView.getTop())) {
            return false;
        }
        setAllChildrenVisible();
        ViewCompat.postInvalidateOnAnimation(this);
        return true;
    }

    void updateObscuredViewsVisibility(View panel) {
        int endBound;
        boolean isLayoutRtl = isLayoutRtlSupport();
        int startBound = isLayoutRtl ? getWidth() - getPaddingRight() : getPaddingLeft();
        if (isLayoutRtl) {
            endBound = getPaddingLeft();
        } else {
            endBound = getWidth() - getPaddingRight();
        }
        int topBound = getPaddingTop();
        int bottomBound = getHeight() - getPaddingBottom();
        int bottom;
        int top;
        int right;
        int left;
        if (panel == null || !viewIsOpaque(panel)) {
            bottom = 0;
            top = 0;
            right = 0;
            left = 0;
        } else {
            left = panel.getLeft();
            right = panel.getRight();
            top = panel.getTop();
            bottom = panel.getBottom();
        }
        int i = 0;
        int childCount = getChildCount();
        while (i < childCount) {
            View child = getChildAt(i);
            if (child != panel) {
                int i2;
                if (isLayoutRtl) {
                    i2 = endBound;
                } else {
                    i2 = startBound;
                }
                int clampedChildLeft = Math.max(i2, child.getLeft());
                int clampedChildTop = Math.max(topBound, child.getTop());
                if (isLayoutRtl) {
                    i2 = startBound;
                } else {
                    i2 = endBound;
                }
                int vis = (clampedChildLeft < left || clampedChildTop < top || Math.min(i, child.getRight()) > right || Math.min(bottomBound, child.getBottom()) > bottom) ? 0 : MMAdView.TRANSITION_RANDOM;
                child.setVisibility(vis);
                i++;
            } else {
                return;
            }
        }
    }
}