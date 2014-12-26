package android.support.v4.view;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityRecordCompat;
import android.support.v4.widget.AutoScrollHelper;
import android.support.v4.widget.EdgeEffectCompat;
import android.support.v4.widget.ExploreByTouchHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;
import com.android.volley.DefaultRetryPolicy;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import com.mopub.mobileads.factories.HttpClientFactory;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import mobi.vserv.android.ads.VservConstants;

public class ViewPager extends ViewGroup {
    private static final int CLOSE_ENOUGH = 2;
    private static final Comparator<ItemInfo> COMPARATOR;
    private static final boolean DEBUG = false;
    private static final int DEFAULT_GUTTER_SIZE = 16;
    private static final int DEFAULT_OFFSCREEN_PAGES = 1;
    private static final int DRAW_ORDER_DEFAULT = 0;
    private static final int DRAW_ORDER_FORWARD = 1;
    private static final int DRAW_ORDER_REVERSE = 2;
    private static final int INVALID_POINTER = -1;
    private static final int[] LAYOUT_ATTRS;
    private static final int MAX_SETTLE_DURATION = 600;
    private static final int MIN_DISTANCE_FOR_FLING = 25;
    private static final int MIN_FLING_VELOCITY = 400;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_SETTLING = 2;
    private static final String TAG = "ViewPager";
    private static final boolean USE_CACHE = false;
    private static final Interpolator sInterpolator;
    private static final ViewPositionComparator sPositionComparator;
    private int mActivePointerId;
    private PagerAdapter mAdapter;
    private OnAdapterChangeListener mAdapterChangeListener;
    private int mBottomPageBounds;
    private boolean mCalledSuper;
    private int mChildHeightMeasureSpec;
    private int mChildWidthMeasureSpec;
    private int mCloseEnough;
    private int mCurItem;
    private int mDecorChildCount;
    private int mDefaultGutterSize;
    private int mDrawingOrder;
    private ArrayList<View> mDrawingOrderedChildren;
    private final Runnable mEndScrollRunnable;
    private int mExpectedAdapterCount;
    private long mFakeDragBeginTime;
    private boolean mFakeDragging;
    private boolean mFirstLayout;
    private float mFirstOffset;
    private int mFlingDistance;
    private int mGutterSize;
    private boolean mIgnoreGutter;
    private boolean mInLayout;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private OnPageChangeListener mInternalPageChangeListener;
    private boolean mIsBeingDragged;
    private boolean mIsUnableToDrag;
    private final ArrayList<ItemInfo> mItems;
    private float mLastMotionX;
    private float mLastMotionY;
    private float mLastOffset;
    private EdgeEffectCompat mLeftEdge;
    private Drawable mMarginDrawable;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    private boolean mNeedCalculatePageOffsets;
    private PagerObserver mObserver;
    private int mOffscreenPageLimit;
    private OnPageChangeListener mOnPageChangeListener;
    private int mPageMargin;
    private PageTransformer mPageTransformer;
    private boolean mPopulatePending;
    private Parcelable mRestoredAdapterState;
    private ClassLoader mRestoredClassLoader;
    private int mRestoredCurItem;
    private EdgeEffectCompat mRightEdge;
    private int mScrollState;
    private Scroller mScroller;
    private boolean mScrollingCacheEnabled;
    private Method mSetChildrenDrawingOrderEnabled;
    private final ItemInfo mTempItem;
    private final Rect mTempRect;
    private int mTopPageBounds;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;

    static interface Decor {
    }

    static class ItemInfo {
        Object object;
        float offset;
        int position;
        boolean scrolling;
        float widthFactor;

        ItemInfo() {
        }
    }

    public static class LayoutParams extends android.view.ViewGroup.LayoutParams {
        int childIndex;
        public int gravity;
        public boolean isDecor;
        boolean needsMeasure;
        int position;
        float widthFactor;

        public LayoutParams() {
            super(-1, -1);
            this.widthFactor = 0.0f;
        }

        public LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
            this.widthFactor = 0.0f;
            TypedArray a = context.obtainStyledAttributes(attrs, LAYOUT_ATTRS);
            this.gravity = a.getInteger(SCROLL_STATE_IDLE, ApiEventType.API_MRAID_HIDE_VIDEO);
            a.recycle();
        }
    }

    static interface OnAdapterChangeListener {
        void onAdapterChanged(PagerAdapter pagerAdapter, PagerAdapter pagerAdapter2);
    }

    public static interface OnPageChangeListener {
        void onPageScrollStateChanged(int i);

        void onPageScrolled(int i, float f, int i2);

        void onPageSelected(int i);
    }

    public static interface PageTransformer {
        void transformPage(View view, float f);
    }

    private class PagerObserver extends DataSetObserver {
        private PagerObserver() {
        }

        public void onChanged() {
            ViewPager.this.dataSetChanged();
        }

        public void onInvalidated() {
            ViewPager.this.dataSetChanged();
        }
    }

    public static class SavedState extends BaseSavedState {
        public static final Creator<android.support.v4.view.ViewPager.SavedState> CREATOR;
        Parcelable adapterState;
        ClassLoader loader;
        int position;

        static {
            CREATOR = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks<android.support.v4.view.ViewPager.SavedState>() {
                public android.support.v4.view.ViewPager.SavedState createFromParcel(Parcel in, ClassLoader loader) {
                    return new android.support.v4.view.ViewPager.SavedState(in, loader);
                }

                public android.support.v4.view.ViewPager.SavedState[] newArray(int size) {
                    return new android.support.v4.view.ViewPager.SavedState[size];
                }
            });
        }

        SavedState(Parcel in, ClassLoader loader) {
            super(in);
            if (loader == null) {
                loader = getClass().getClassLoader();
            }
            this.position = in.readInt();
            this.adapterState = in.readParcelable(loader);
            this.loader = loader;
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public String toString() {
            return "FragmentPager.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " position=" + this.position + "}";
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.position);
            out.writeParcelable(this.adapterState, flags);
        }
    }

    static class ViewPositionComparator implements Comparator<View> {
        ViewPositionComparator() {
        }

        public int compare(View lhs, View rhs) {
            android.support.v4.view.ViewPager.LayoutParams llp = (android.support.v4.view.ViewPager.LayoutParams) lhs.getLayoutParams();
            android.support.v4.view.ViewPager.LayoutParams rlp = (android.support.v4.view.ViewPager.LayoutParams) rhs.getLayoutParams();
            if (llp.isDecor != rlp.isDecor) {
                return llp.isDecor ? SCROLL_STATE_DRAGGING : INVALID_POINTER;
            } else {
                return llp.position - rlp.position;
            }
        }
    }

    class MyAccessibilityDelegate extends AccessibilityDelegateCompat {
        MyAccessibilityDelegate() {
        }

        private boolean canScroll() {
            return (ViewPager.this.mAdapter == null || ViewPager.this.mAdapter.getCount() <= 1) ? DEBUG : true;
        }

        public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
            super.onInitializeAccessibilityEvent(host, event);
            event.setClassName(ViewPager.class.getName());
            AccessibilityRecordCompat recordCompat = AccessibilityRecordCompat.obtain();
            recordCompat.setScrollable(canScroll());
            if (event.getEventType() == 4096 && ViewPager.this.mAdapter != null) {
                recordCompat.setItemCount(ViewPager.this.mAdapter.getCount());
                recordCompat.setFromIndex(ViewPager.this.mCurItem);
                recordCompat.setToIndex(ViewPager.this.mCurItem);
            }
        }

        public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
            super.onInitializeAccessibilityNodeInfo(host, info);
            info.setClassName(ViewPager.class.getName());
            info.setScrollable(canScroll());
            if (ViewPager.this.canScrollHorizontally(SCROLL_STATE_DRAGGING)) {
                info.addAction(AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD);
            }
            if (ViewPager.this.canScrollHorizontally(INVALID_POINTER)) {
                info.addAction(HttpClientFactory.SOCKET_SIZE);
            }
        }

        public boolean performAccessibilityAction(View host, int action, Bundle args) {
            if (super.performAccessibilityAction(host, action, args)) {
                return true;
            }
            switch (action) {
                case AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD:
                    if (!ViewPager.this.canScrollHorizontally(SCROLL_STATE_DRAGGING)) {
                        return false;
                    }
                    ViewPager.this.setCurrentItem(ViewPager.this.mCurItem + 1);
                    return true;
                case HttpClientFactory.SOCKET_SIZE:
                    if (!ViewPager.this.canScrollHorizontally(INVALID_POINTER)) {
                        return false;
                    }
                    ViewPager.this.setCurrentItem(ViewPager.this.mCurItem - 1);
                    return true;
                default:
                    return false;
            }
        }
    }

    public static class SimpleOnPageChangeListener implements android.support.v4.view.ViewPager.OnPageChangeListener {
        public void onPageScrollStateChanged(int state) {
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        public void onPageSelected(int position) {
        }
    }

    static {
        LAYOUT_ATTRS = new int[]{16842931};
        COMPARATOR = new Comparator<ItemInfo>() {
            public int compare(ItemInfo lhs, ItemInfo rhs) {
                return lhs.position - rhs.position;
            }
        };
        sInterpolator = new Interpolator() {
            public float getInterpolation(float t) {
                t -= 1.0f;
                return (((t * t) * t) * t) * t + 1.0f;
            }
        };
        sPositionComparator = new ViewPositionComparator();
    }

    public ViewPager(Context context) {
        super(context);
        this.mItems = new ArrayList();
        this.mTempItem = new ItemInfo();
        this.mTempRect = new Rect();
        this.mRestoredCurItem = -1;
        this.mRestoredAdapterState = null;
        this.mRestoredClassLoader = null;
        this.mFirstOffset = -3.4028235E38f;
        this.mLastOffset = Float.MAX_VALUE;
        this.mOffscreenPageLimit = 1;
        this.mActivePointerId = -1;
        this.mFirstLayout = true;
        this.mNeedCalculatePageOffsets = false;
        this.mEndScrollRunnable = new Runnable() {
            public void run() {
                ViewPager.this.setScrollState(SCROLL_STATE_IDLE);
                ViewPager.this.populate();
            }
        };
        this.mScrollState = 0;
        initViewPager();
    }

    public ViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mItems = new ArrayList();
        this.mTempItem = new ItemInfo();
        this.mTempRect = new Rect();
        this.mRestoredCurItem = -1;
        this.mRestoredAdapterState = null;
        this.mRestoredClassLoader = null;
        this.mFirstOffset = -3.4028235E38f;
        this.mLastOffset = Float.MAX_VALUE;
        this.mOffscreenPageLimit = 1;
        this.mActivePointerId = -1;
        this.mFirstLayout = true;
        this.mNeedCalculatePageOffsets = false;
        this.mEndScrollRunnable = new Runnable() {
            public void run() {
                ViewPager.this.setScrollState(SCROLL_STATE_IDLE);
                ViewPager.this.populate();
            }
        };
        this.mScrollState = 0;
        initViewPager();
    }

    private void calculatePageOffsets(ItemInfo curItem, int curIndex, ItemInfo oldCurInfo) {
        float offset;
        int pos;
        ItemInfo ii;
        int N = this.mAdapter.getCount();
        int width = getClientWidth();
        float marginOffset = width > 0 ? ((float) this.mPageMargin) / ((float) width) : BitmapDescriptorFactory.HUE_RED;
        if (oldCurInfo != null) {
            int oldCurPosition = oldCurInfo.position;
            int itemIndex;
            if (oldCurPosition < curItem.position) {
                itemIndex = SCROLL_STATE_IDLE;
                offset = oldCurInfo.offset + oldCurInfo.widthFactor + marginOffset;
                pos = oldCurPosition + 1;
                while (pos <= curItem.position && itemIndex < this.mItems.size()) {
                    ii = this.mItems.get(itemIndex);
                    while (pos > ii.position && itemIndex < this.mItems.size() - 1) {
                        itemIndex++;
                        ii = this.mItems.get(itemIndex);
                    }
                    while (pos < ii.position) {
                        offset += this.mAdapter.getPageWidth(pos) + marginOffset;
                        pos++;
                    }
                    ii.offset = offset;
                    offset += ii.widthFactor + marginOffset;
                    pos++;
                }
            } else if (oldCurPosition > curItem.position) {
                itemIndex = this.mItems.size() - 1;
                offset = oldCurInfo.offset;
                pos = oldCurPosition - 1;
                while (pos >= curItem.position && itemIndex >= 0) {
                    ii = this.mItems.get(itemIndex);
                    while (pos < ii.position && itemIndex > 0) {
                        itemIndex--;
                        ii = this.mItems.get(itemIndex);
                    }
                    while (pos > ii.position) {
                        offset -= this.mAdapter.getPageWidth(pos) + marginOffset;
                        pos--;
                    }
                    offset -= ii.widthFactor + marginOffset;
                    ii.offset = offset;
                    pos--;
                }
            }
        }
        int itemCount = this.mItems.size();
        offset = curItem.offset;
        pos = curItem.position - 1;
        this.mFirstOffset = curItem.position == 0 ? curItem.offset : -3.4028235E38f;
        this.mLastOffset = curItem.position == N + -1 ? curItem.offset + curItem.widthFactor - 1.0f : AutoScrollHelper.NO_MAX;
        int i = curIndex - 1;
        while (i >= 0) {
            ii = this.mItems.get(i);
            while (pos > ii.position) {
                offset -= this.mAdapter.getPageWidth(pos) + marginOffset;
                pos--;
            }
            offset -= ii.widthFactor + marginOffset;
            ii.offset = offset;
            if (ii.position == 0) {
                this.mFirstOffset = offset;
            }
            i--;
            pos--;
        }
        offset = curItem.offset + curItem.widthFactor + marginOffset;
        pos = curItem.position + 1;
        i = curIndex + 1;
        while (i < itemCount) {
            ii = this.mItems.get(i);
            while (pos < ii.position) {
                offset += this.mAdapter.getPageWidth(pos) + marginOffset;
                pos++;
            }
            if (ii.position == N - 1) {
                this.mLastOffset = ii.widthFactor + offset - 1.0f;
            }
            ii.offset = offset;
            offset += ii.widthFactor + marginOffset;
            i++;
            pos++;
        }
        this.mNeedCalculatePageOffsets = false;
    }

    private void completeScroll(boolean postEvents) {
        boolean needPopulate;
        if (this.mScrollState == 2) {
            needPopulate = true;
        } else {
            needPopulate = false;
        }
        if (needPopulate) {
            setScrollingCacheEnabled(DEBUG);
            this.mScroller.abortAnimation();
            int oldX = getScrollX();
            int oldY = getScrollY();
            int x = this.mScroller.getCurrX();
            int y = this.mScroller.getCurrY();
            if (!(oldX == x && oldY == y)) {
                scrollTo(x, y);
            }
        }
        this.mPopulatePending = false;
        int i = SCROLL_STATE_IDLE;
        while (i < this.mItems.size()) {
            ItemInfo ii = (ItemInfo) this.mItems.get(i);
            if (ii.scrolling) {
                needPopulate = true;
                ii.scrolling = false;
            }
            i++;
        }
        if (!needPopulate) {
            return;
        }
        if (postEvents) {
            ViewCompat.postOnAnimation(this, this.mEndScrollRunnable);
        } else {
            this.mEndScrollRunnable.run();
        }
    }

    private int determineTargetPage(int currentPage, float pageOffset, int velocity, int deltaX) {
        int targetPage;
        if (Math.abs(deltaX) <= this.mFlingDistance || Math.abs(velocity) <= this.mMinimumVelocity) {
            targetPage = (int) (((float) currentPage) + pageOffset + (currentPage >= this.mCurItem ? 0.4f : 0.6f));
        } else {
            targetPage = velocity > 0 ? currentPage : currentPage + 1;
        }
        if (this.mItems.size() <= 0) {
            return targetPage;
        }
        return Math.max(((ItemInfo) this.mItems.get(SCROLL_STATE_IDLE)).position, Math.min(targetPage, ((ItemInfo) this.mItems.get(this.mItems.size() - 1)).position));
    }

    private void enableLayers(boolean enable) {
        int childCount = getChildCount();
        int i = SCROLL_STATE_IDLE;
        while (i < childCount) {
            ViewCompat.setLayerType(getChildAt(i), enable ? SCROLL_STATE_SETTLING : SCROLL_STATE_IDLE, null);
            i++;
        }
    }

    private void endDrag() {
        this.mIsBeingDragged = false;
        this.mIsUnableToDrag = false;
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    private Rect getChildRectInPagerCoordinates(Rect outRect, View child) {
        if (outRect == null) {
            outRect = new Rect();
        }
        if (child == null) {
            outRect.set(SCROLL_STATE_IDLE, SCROLL_STATE_IDLE, SCROLL_STATE_IDLE, SCROLL_STATE_IDLE);
        } else {
            outRect.left = child.getLeft();
            outRect.right = child.getRight();
            outRect.top = child.getTop();
            outRect.bottom = child.getBottom();
            ViewParent parent = child.getParent();
            while (parent instanceof ViewGroup && parent != this) {
                ViewGroup group = (ViewGroup) parent;
                outRect.left += group.getLeft();
                outRect.right += group.getRight();
                outRect.top += group.getTop();
                outRect.bottom += group.getBottom();
                parent = group.getParent();
            }
        }
        return outRect;
    }

    private int getClientWidth() {
        return getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }

    private ItemInfo infoForCurrentScrollPosition() {
        float scrollOffset;
        float marginOffset = BitmapDescriptorFactory.HUE_RED;
        int width = getClientWidth();
        if (width > 0) {
            scrollOffset = ((float) getScrollX()) / ((float) width);
        } else {
            scrollOffset = 0.0f;
        }
        if (width > 0) {
            marginOffset = ((float) this.mPageMargin) / ((float) width);
        }
        int lastPos = INVALID_POINTER;
        float lastOffset = BitmapDescriptorFactory.HUE_RED;
        float lastWidth = BitmapDescriptorFactory.HUE_RED;
        boolean first = true;
        ItemInfo lastItem = null;
        int i = SCROLL_STATE_IDLE;
        while (i < this.mItems.size()) {
            ItemInfo ii = (ItemInfo) this.mItems.get(i);
            if (!(first || ii.position == lastPos + 1)) {
                ii = this.mTempItem;
                ii.offset = lastOffset + lastWidth + marginOffset;
                ii.position = lastPos + 1;
                ii.widthFactor = this.mAdapter.getPageWidth(ii.position);
                i--;
            }
            float offset = ii.offset;
            float leftBound = offset;
            float rightBound = ii.widthFactor + offset + marginOffset;
            if (!first && scrollOffset < leftBound) {
                return lastItem;
            }
            if (scrollOffset >= rightBound && i != this.mItems.size() - 1) {
                first = DEBUG;
                lastPos = ii.position;
                lastOffset = offset;
                lastWidth = ii.widthFactor;
                lastItem = ii;
                i++;
            }
            return ii;
        }
        return lastItem;
    }

    private boolean isGutterDrag(float x, float dx) {
        return ((x >= ((float) this.mGutterSize) || dx <= 0.0f) && (x <= ((float) (getWidth() - this.mGutterSize)) || dx >= 0.0f)) ? DEBUG : true;
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        int pointerIndex = MotionEventCompat.getActionIndex(ev);
        if (MotionEventCompat.getPointerId(ev, pointerIndex) == this.mActivePointerId) {
            int newPointerIndex = pointerIndex == 0 ? SCROLL_STATE_DRAGGING : SCROLL_STATE_IDLE;
            this.mLastMotionX = MotionEventCompat.getX(ev, newPointerIndex);
            this.mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
            if (this.mVelocityTracker != null) {
                this.mVelocityTracker.clear();
            }
        }
    }

    private boolean pageScrolled(int xpos) {
        if (this.mItems.size() == 0) {
            this.mCalledSuper = false;
            onPageScrolled(SCROLL_STATE_IDLE, BitmapDescriptorFactory.HUE_RED, SCROLL_STATE_IDLE);
            if (this.mCalledSuper) {
                return DEBUG;
            }
            throw new IllegalStateException("onPageScrolled did not call superclass implementation");
        } else {
            ItemInfo ii = infoForCurrentScrollPosition();
            int width = getClientWidth();
            int widthWithMargin = width + this.mPageMargin;
            float marginOffset = ((float) this.mPageMargin) / ((float) width);
            int currentPage = ii.position;
            float pageOffset = ((((float) xpos) / ((float) width)) - ii.offset) / (ii.widthFactor + marginOffset);
            int offsetPixels = (int) (((float) widthWithMargin) * pageOffset);
            this.mCalledSuper = false;
            onPageScrolled(currentPage, pageOffset, offsetPixels);
            if (this.mCalledSuper) {
                return true;
            }
            throw new IllegalStateException("onPageScrolled did not call superclass implementation");
        }
    }

    private boolean performDrag(float x) {
        boolean needsInvalidate = DEBUG;
        float deltaX = this.mLastMotionX - x;
        this.mLastMotionX = x;
        float scrollX = ((float) getScrollX()) + deltaX;
        int width = getClientWidth();
        float leftBound = ((float) width) * this.mFirstOffset;
        float rightBound = ((float) width) * this.mLastOffset;
        boolean leftAbsolute = true;
        boolean rightAbsolute = true;
        ItemInfo firstItem = (ItemInfo) this.mItems.get(SCROLL_STATE_IDLE);
        ItemInfo lastItem = (ItemInfo) this.mItems.get(this.mItems.size() - 1);
        if (firstItem.position != 0) {
            leftAbsolute = DEBUG;
            leftBound = firstItem.offset * ((float) width);
        }
        if (lastItem.position != this.mAdapter.getCount() - 1) {
            rightAbsolute = DEBUG;
            rightBound = lastItem.offset * ((float) width);
        }
        if (scrollX < leftBound) {
            if (leftAbsolute) {
                needsInvalidate = this.mLeftEdge.onPull(Math.abs(leftBound - scrollX) / ((float) width));
            }
            scrollX = leftBound;
        } else if (scrollX > rightBound) {
            if (rightAbsolute) {
                needsInvalidate = this.mRightEdge.onPull(Math.abs(scrollX - rightBound) / ((float) width));
            }
            scrollX = rightBound;
        }
        this.mLastMotionX += scrollX - ((float) ((int) scrollX));
        scrollTo((int) scrollX, getScrollY());
        pageScrolled((int) scrollX);
        return needsInvalidate;
    }

    private void recomputeScrollPosition(int width, int oldWidth, int margin, int oldMargin) {
        if (oldWidth <= 0 || this.mItems.isEmpty()) {
            ItemInfo ii = infoForPosition(this.mCurItem);
            int scrollPos = (int) (((float) ((width - getPaddingLeft()) - getPaddingRight())) * (ii != null ? Math.min(ii.offset, this.mLastOffset) : BitmapDescriptorFactory.HUE_RED));
            if (scrollPos != getScrollX()) {
                completeScroll(DEBUG);
                scrollTo(scrollPos, getScrollY());
            }
        } else {
            int newOffsetPixels = (int) (((float) (((width - getPaddingLeft()) - getPaddingRight()) + margin)) * (((float) getScrollX()) / ((float) (((oldWidth - getPaddingLeft()) - getPaddingRight()) + oldMargin))));
            scrollTo(newOffsetPixels, getScrollY());
            if (!this.mScroller.isFinished()) {
                this.mScroller.startScroll(newOffsetPixels, SCROLL_STATE_IDLE, (int) (infoForPosition(this.mCurItem).offset * ((float) width)), SCROLL_STATE_IDLE, this.mScroller.getDuration() - this.mScroller.timePassed());
            }
        }
    }

    private void removeNonDecorViews() {
        int i = SCROLL_STATE_IDLE;
        while (i < getChildCount()) {
            if (!((LayoutParams) getChildAt(i).getLayoutParams()).isDecor) {
                removeViewAt(i);
                i--;
            }
            i++;
        }
    }

    private void requestParentDisallowInterceptTouchEvent(boolean disallowIntercept) {
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(disallowIntercept);
        }
    }

    private void scrollToItem(int item, boolean smoothScroll, int velocity, boolean dispatchSelected) {
        ItemInfo curInfo = infoForPosition(item);
        int destX = SCROLL_STATE_IDLE;
        if (curInfo != null) {
            destX = (int) (((float) getClientWidth()) * Math.max(this.mFirstOffset, Math.min(curInfo.offset, this.mLastOffset)));
        }
        if (smoothScroll) {
            smoothScrollTo(destX, SCROLL_STATE_IDLE, velocity);
            if (dispatchSelected && this.mOnPageChangeListener != null) {
                this.mOnPageChangeListener.onPageSelected(item);
            }
            if (dispatchSelected && this.mInternalPageChangeListener != null) {
                this.mInternalPageChangeListener.onPageSelected(item);
            }
        } else {
            if (dispatchSelected && this.mOnPageChangeListener != null) {
                this.mOnPageChangeListener.onPageSelected(item);
            }
            if (dispatchSelected && this.mInternalPageChangeListener != null) {
                this.mInternalPageChangeListener.onPageSelected(item);
            }
            completeScroll(DEBUG);
            scrollTo(destX, SCROLL_STATE_IDLE);
            pageScrolled(destX);
        }
    }

    private void setScrollState(int newState) {
        if (this.mScrollState != newState) {
            this.mScrollState = newState;
            if (this.mPageTransformer != null) {
                enableLayers(newState != 0 ? true : DEBUG);
            }
            if (this.mOnPageChangeListener != null) {
                this.mOnPageChangeListener.onPageScrollStateChanged(newState);
            }
        }
    }

    private void setScrollingCacheEnabled(boolean enabled) {
        if (this.mScrollingCacheEnabled != enabled) {
            this.mScrollingCacheEnabled = enabled;
        }
    }

    private void sortChildDrawingOrder() {
        if (this.mDrawingOrder != 0) {
            if (this.mDrawingOrderedChildren == null) {
                this.mDrawingOrderedChildren = new ArrayList();
            } else {
                this.mDrawingOrderedChildren.clear();
            }
            int childCount = getChildCount();
            int i = SCROLL_STATE_IDLE;
            while (i < childCount) {
                this.mDrawingOrderedChildren.add(getChildAt(i));
                i++;
            }
            Collections.sort(this.mDrawingOrderedChildren, sPositionComparator);
        }
    }

    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        int focusableCount = views.size();
        int descendantFocusability = getDescendantFocusability();
        if (descendantFocusability != 393216) {
            int i = SCROLL_STATE_IDLE;
            while (i < getChildCount()) {
                View child = getChildAt(i);
                if (child.getVisibility() == 0) {
                    ItemInfo ii = infoForChild(child);
                    if (ii != null && ii.position == this.mCurItem) {
                        child.addFocusables(views, direction, focusableMode);
                    }
                }
                i++;
            }
        }
        if ((descendantFocusability == 262144 && focusableCount != views.size()) || !isFocusable()) {
            return;
        }
        if (((focusableMode & 1) != 1 || !isInTouchMode() || isFocusableInTouchMode()) && views != null) {
            views.add(this);
        }
    }

    ItemInfo addNewItem(int position, int index) {
        ItemInfo ii = new ItemInfo();
        ii.position = position;
        ii.object = this.mAdapter.instantiateItem(this, position);
        ii.widthFactor = this.mAdapter.getPageWidth(position);
        if (index < 0 || index >= this.mItems.size()) {
            this.mItems.add(ii);
        } else {
            this.mItems.add(index, ii);
        }
        return ii;
    }

    public void addTouchables(ArrayList<View> views) {
        int i = SCROLL_STATE_IDLE;
        while (i < getChildCount()) {
            View child = getChildAt(i);
            if (child.getVisibility() == 0) {
                ItemInfo ii = infoForChild(child);
                if (ii != null && ii.position == this.mCurItem) {
                    child.addTouchables(views);
                }
            }
            i++;
        }
    }

    public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
        if (!checkLayoutParams(params)) {
            params = generateLayoutParams(params);
        }
        LayoutParams lp = (LayoutParams) params;
        lp.isDecor |= child instanceof Decor;
        if (!this.mInLayout) {
            super.addView(child, index, params);
        } else if (lp == null || !lp.isDecor) {
            lp.needsMeasure = true;
            addViewInLayout(child, index, params);
        } else {
            throw new IllegalStateException("Cannot add pager decor view during layout");
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean arrowScroll(int r14_direction) {
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.view.ViewPager.arrowScroll(int):boolean");
        /*
        r13 = this;
        r12 = 66;
        r11 = 17;
        r1 = r13.findFocus();
        if (r1 != r13) goto L_0x003c;
    L_0x000a:
        r1 = 0;
    L_0x000b:
        r2 = 0;
        r8 = android.view.FocusFinder.getInstance();
        r4 = r8.findNextFocus(r13, r1, r14);
        if (r4 == 0) goto L_0x00c5;
    L_0x0016:
        if (r4 == r1) goto L_0x00c5;
    L_0x0018:
        if (r14 != r11) goto L_0x00a3;
    L_0x001a:
        r8 = r13.mTempRect;
        r8 = r13.getChildRectInPagerCoordinates(r8, r4);
        r5 = r8.left;
        r8 = r13.mTempRect;
        r8 = r13.getChildRectInPagerCoordinates(r8, r1);
        r0 = r8.left;
        if (r1 == 0) goto L_0x009e;
    L_0x002c:
        if (r5 < r0) goto L_0x009e;
    L_0x002e:
        r2 = r13.pageLeft();
    L_0x0032:
        if (r2 == 0) goto L_0x003b;
    L_0x0034:
        r8 = android.view.SoundEffectConstants.getContantForFocusDirection(r14);
        r13.playSoundEffect(r8);
    L_0x003b:
        return r2;
    L_0x003c:
        if (r1 == 0) goto L_0x000b;
    L_0x003e:
        r3 = 0;
        r6 = r1.getParent();
    L_0x0043:
        r8 = r6 instanceof android.view.ViewGroup;
        if (r8 == 0) goto L_0x004a;
    L_0x0047:
        if (r6 != r13) goto L_0x007a;
    L_0x0049:
        r3 = 1;
    L_0x004a:
        if (r3 != 0) goto L_0x000b;
    L_0x004c:
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r8 = r1.getClass();
        r8 = r8.getSimpleName();
        r7.append(r8);
        r6 = r1.getParent();
    L_0x0060:
        r8 = r6 instanceof android.view.ViewGroup;
        if (r8 == 0) goto L_0x007f;
    L_0x0064:
        r8 = " => ";
        r8 = r7.append(r8);
        r9 = r6.getClass();
        r9 = r9.getSimpleName();
        r8.append(r9);
        r6 = r6.getParent();
        goto L_0x0060;
    L_0x007a:
        r6 = r6.getParent();
        goto L_0x0043;
    L_0x007f:
        r8 = "ViewPager";
        r9 = new java.lang.StringBuilder;
        r9.<init>();
        r10 = "arrowScroll tried to find focus based on non-child current focused view ";
        r9 = r9.append(r10);
        r10 = r7.toString();
        r9 = r9.append(r10);
        r9 = r9.toString();
        android.util.Log.e(r8, r9);
        r1 = 0;
        goto L_0x000b;
    L_0x009e:
        r2 = r4.requestFocus();
        goto L_0x0032;
    L_0x00a3:
        if (r14 != r12) goto L_0x0032;
    L_0x00a5:
        r8 = r13.mTempRect;
        r8 = r13.getChildRectInPagerCoordinates(r8, r4);
        r5 = r8.left;
        r8 = r13.mTempRect;
        r8 = r13.getChildRectInPagerCoordinates(r8, r1);
        r0 = r8.left;
        if (r1 == 0) goto L_0x00bf;
    L_0x00b7:
        if (r5 > r0) goto L_0x00bf;
    L_0x00b9:
        r2 = r13.pageRight();
        goto L_0x0032;
    L_0x00bf:
        r2 = r4.requestFocus();
        goto L_0x0032;
    L_0x00c5:
        if (r14 == r11) goto L_0x00ca;
    L_0x00c7:
        r8 = 1;
        if (r14 != r8) goto L_0x00d0;
    L_0x00ca:
        r2 = r13.pageLeft();
        goto L_0x0032;
    L_0x00d0:
        if (r14 == r12) goto L_0x00d5;
    L_0x00d2:
        r8 = 2;
        if (r14 != r8) goto L_0x0032;
    L_0x00d5:
        r2 = r13.pageRight();
        goto L_0x0032;
        */
    }

    public boolean beginFakeDrag() {
        if (this.mIsBeingDragged) {
            return DEBUG;
        }
        this.mFakeDragging = true;
        setScrollState(SCROLL_STATE_DRAGGING);
        this.mLastMotionX = 0.0f;
        this.mInitialMotionX = 0.0f;
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        } else {
            this.mVelocityTracker.clear();
        }
        long time = SystemClock.uptimeMillis();
        MotionEvent ev = MotionEvent.obtain(time, time, SCROLL_STATE_IDLE, BitmapDescriptorFactory.HUE_RED, 0.0f, 0);
        this.mVelocityTracker.addMovement(ev);
        ev.recycle();
        this.mFakeDragBeginTime = time;
        return true;
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
        return (checkV && ViewCompat.canScrollHorizontally(v, -dx)) ? true : DEBUG;
    }

    public boolean canScrollHorizontally(int direction) {
        boolean z = SCROLL_STATE_DRAGGING;
        if (this.mAdapter == null) {
            return DEBUG;
        }
        int width = getClientWidth();
        int scrollX = getScrollX();
        if (direction < 0) {
            if (scrollX <= ((int) (((float) width) * this.mFirstOffset))) {
                z = false;
            }
            return z;
        } else if (direction <= 0) {
            return DEBUG;
        } else {
            if (scrollX >= ((int) (((float) width) * this.mLastOffset))) {
                z = false;
            }
            return z;
        }
    }

    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return (p instanceof LayoutParams && super.checkLayoutParams(p)) ? true : DEBUG;
    }

    public void computeScroll() {
        if (this.mScroller.isFinished() || !this.mScroller.computeScrollOffset()) {
            completeScroll(true);
        } else {
            int oldX = getScrollX();
            int oldY = getScrollY();
            int x = this.mScroller.getCurrX();
            int y = this.mScroller.getCurrY();
            if (!(oldX == x && oldY == y)) {
                scrollTo(x, y);
                if (!pageScrolled(x)) {
                    this.mScroller.abortAnimation();
                    scrollTo(SCROLL_STATE_IDLE, y);
                }
            }
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    void dataSetChanged() {
        boolean needPopulate;
        int adapterCount = this.mAdapter.getCount();
        this.mExpectedAdapterCount = adapterCount;
        if (this.mItems.size() >= this.mOffscreenPageLimit * 2 + 1 || this.mItems.size() >= adapterCount) {
            needPopulate = false;
        } else {
            needPopulate = true;
        }
        int newCurrItem = this.mCurItem;
        boolean isUpdating = DEBUG;
        int i = SCROLL_STATE_IDLE;
        while (i < this.mItems.size()) {
            ItemInfo ii = (ItemInfo) this.mItems.get(i);
            int newPos = this.mAdapter.getItemPosition(ii.object);
            if (newPos != -1) {
                if (newPos == -2) {
                    this.mItems.remove(i);
                    i--;
                    if (!isUpdating) {
                        this.mAdapter.startUpdate(this);
                        isUpdating = true;
                    }
                    this.mAdapter.destroyItem(this, ii.position, ii.object);
                    needPopulate = true;
                    if (this.mCurItem == ii.position) {
                        newCurrItem = Math.max(SCROLL_STATE_IDLE, Math.min(this.mCurItem, adapterCount - 1));
                        needPopulate = true;
                    }
                } else if (ii.position != newPos) {
                    if (ii.position == this.mCurItem) {
                        newCurrItem = newPos;
                    }
                    ii.position = newPos;
                    needPopulate = true;
                }
            }
            i++;
        }
        if (isUpdating) {
            this.mAdapter.finishUpdate(this);
        }
        Collections.sort(this.mItems, COMPARATOR);
        if (needPopulate) {
            int childCount = getChildCount();
            i = SCROLL_STATE_IDLE;
            while (i < childCount) {
                LayoutParams lp = (LayoutParams) getChildAt(i).getLayoutParams();
                if (!lp.isDecor) {
                    lp.widthFactor = 0.0f;
                }
                i++;
            }
            setCurrentItemInternal(newCurrItem, DEBUG, true);
            requestLayout();
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        return (super.dispatchKeyEvent(event) || executeKeyEvent(event)) ? true : DEBUG;
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == 4096) {
            return super.dispatchPopulateAccessibilityEvent(event);
        }
        int childCount = getChildCount();
        int i = SCROLL_STATE_IDLE;
        while (i < childCount) {
            View child = getChildAt(i);
            if (child.getVisibility() == 0) {
                ItemInfo ii = infoForChild(child);
                if (ii != null && ii.position == this.mCurItem && child.dispatchPopulateAccessibilityEvent(event)) {
                    return true;
                }
            }
            i++;
        }
        return DEBUG;
    }

    float distanceInfluenceForSnapDuration(float f) {
        return (float) Math.sin((double) ((float) (((double) (f - 0.5f)) * 0.4712389167638204d)));
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        boolean needsInvalidate = DEBUG;
        int overScrollMode = ViewCompat.getOverScrollMode(this);
        if (overScrollMode == 0 || (overScrollMode == 1 && this.mAdapter != null && this.mAdapter.getCount() > 1)) {
            int restoreCount;
            int height;
            int width;
            if (!this.mLeftEdge.isFinished()) {
                restoreCount = canvas.save();
                height = getHeight() - getPaddingTop() - getPaddingBottom();
                width = getWidth();
                canvas.rotate(BitmapDescriptorFactory.HUE_VIOLET);
                canvas.translate((float) ((-height) + getPaddingTop()), this.mFirstOffset * ((float) width));
                this.mLeftEdge.setSize(height, width);
                needsInvalidate = false | this.mLeftEdge.draw(canvas);
                canvas.restoreToCount(restoreCount);
            }
            if (!this.mRightEdge.isFinished()) {
                restoreCount = canvas.save();
                width = getWidth();
                height = getHeight() - getPaddingTop() - getPaddingBottom();
                canvas.rotate(90.0f);
                canvas.translate((float) (-getPaddingTop()), (-(this.mLastOffset + 1.0f)) * ((float) width));
                this.mRightEdge.setSize(height, width);
                needsInvalidate |= this.mRightEdge.draw(canvas);
                canvas.restoreToCount(restoreCount);
            }
        } else {
            this.mLeftEdge.finish();
            this.mRightEdge.finish();
        }
        if (needsInvalidate) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable d = this.mMarginDrawable;
        if (d != null && d.isStateful()) {
            d.setState(getDrawableState());
        }
    }

    public void endFakeDrag() {
        if (this.mFakeDragging) {
            VelocityTracker velocityTracker = this.mVelocityTracker;
            velocityTracker.computeCurrentVelocity(GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE, (float) this.mMaximumVelocity);
            int initialVelocity = (int) VelocityTrackerCompat.getXVelocity(velocityTracker, this.mActivePointerId);
            this.mPopulatePending = true;
            int width = getClientWidth();
            int scrollX = getScrollX();
            ItemInfo ii = infoForCurrentScrollPosition();
            setCurrentItemInternal(determineTargetPage(ii.position, ((((float) scrollX) / ((float) width)) - ii.offset) / ii.widthFactor, initialVelocity, (int) (this.mLastMotionX - this.mInitialMotionX)), true, true, initialVelocity);
            endDrag();
            this.mFakeDragging = false;
        } else {
            throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
        }
    }

    public boolean executeKeyEvent(KeyEvent event) {
        if (event.getAction() != 0) {
            return DEBUG;
        }
        switch (event.getKeyCode()) {
            case ApiEventType.API_MRAID_POST_TO_SOCIAL:
                return arrowScroll(ApiEventType.API_MRAID_GET_SCREEN_SIZE);
            case ApiEventType.API_MRAID_SUPPORTS:
                return arrowScroll(66);
            case 61:
                if (VERSION.SDK_INT < 11) {
                    return DEBUG;
                }
                if (KeyEventCompat.hasNoModifiers(event)) {
                    return arrowScroll(SCROLL_STATE_SETTLING);
                }
                return KeyEventCompat.hasModifiers(event, SCROLL_STATE_DRAGGING) ? arrowScroll(SCROLL_STATE_DRAGGING) : DEBUG;
            default:
                return DEBUG;
        }
    }

    public void fakeDragBy(float xOffset) {
        if (this.mFakeDragging) {
            this.mLastMotionX += xOffset;
            float scrollX = ((float) getScrollX()) - xOffset;
            int width = getClientWidth();
            float leftBound = ((float) width) * this.mFirstOffset;
            float rightBound = ((float) width) * this.mLastOffset;
            ItemInfo firstItem = (ItemInfo) this.mItems.get(SCROLL_STATE_IDLE);
            ItemInfo lastItem = (ItemInfo) this.mItems.get(this.mItems.size() - 1);
            if (firstItem.position != 0) {
                leftBound = firstItem.offset * ((float) width);
            }
            if (lastItem.position != this.mAdapter.getCount() - 1) {
                rightBound = lastItem.offset * ((float) width);
            }
            if (scrollX < leftBound) {
                scrollX = leftBound;
            } else if (scrollX > rightBound) {
                scrollX = rightBound;
            }
            this.mLastMotionX += scrollX - ((float) ((int) scrollX));
            scrollTo((int) scrollX, getScrollY());
            pageScrolled((int) scrollX);
            MotionEvent ev = MotionEvent.obtain(this.mFakeDragBeginTime, SystemClock.uptimeMillis(), SCROLL_STATE_SETTLING, this.mLastMotionX, BitmapDescriptorFactory.HUE_RED, SCROLL_STATE_IDLE);
            this.mVelocityTracker.addMovement(ev);
            ev.recycle();
        } else {
            throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
        }
    }

    protected android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return generateDefaultLayoutParams();
    }

    public PagerAdapter getAdapter() {
        return this.mAdapter;
    }

    protected int getChildDrawingOrder(int childCount, int i) {
        int index;
        if (this.mDrawingOrder == 2) {
            index = childCount - 1 - i;
        } else {
            index = i;
        }
        return ((LayoutParams) ((View) this.mDrawingOrderedChildren.get(index)).getLayoutParams()).childIndex;
    }

    public int getCurrentItem() {
        return this.mCurItem;
    }

    public int getOffscreenPageLimit() {
        return this.mOffscreenPageLimit;
    }

    public int getPageMargin() {
        return this.mPageMargin;
    }

    ItemInfo infoForAnyChild(View child) {
        while (true) {
            ViewParent parent = child.getParent();
            if (parent == this) {
                return infoForChild(child);
            }
            if (parent != null && parent instanceof View) {
                child = (View) parent;
            }
            return null;
        }
    }

    ItemInfo infoForChild(View child) {
        int i = SCROLL_STATE_IDLE;
        while (i < this.mItems.size()) {
            ItemInfo ii = (ItemInfo) this.mItems.get(i);
            if (this.mAdapter.isViewFromObject(child, ii.object)) {
                return ii;
            }
            i++;
        }
        return null;
    }

    ItemInfo infoForPosition(int position) {
        int i = SCROLL_STATE_IDLE;
        while (i < this.mItems.size()) {
            ItemInfo ii = (ItemInfo) this.mItems.get(i);
            if (ii.position == position) {
                return ii;
            }
            i++;
        }
        return null;
    }

    void initViewPager() {
        setWillNotDraw(DEBUG);
        setDescendantFocusability(AccessibilityEventCompat.TYPE_GESTURE_DETECTION_START);
        setFocusable(true);
        Context context = getContext();
        this.mScroller = new Scroller(context, sInterpolator);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        float density = context.getResources().getDisplayMetrics().density;
        this.mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
        this.mMinimumVelocity = (int) (400.0f * density);
        this.mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        this.mLeftEdge = new EdgeEffectCompat(context);
        this.mRightEdge = new EdgeEffectCompat(context);
        this.mFlingDistance = (int) (25.0f * density);
        this.mCloseEnough = (int) (2.0f * density);
        this.mDefaultGutterSize = (int) (16.0f * density);
        ViewCompat.setAccessibilityDelegate(this, new MyAccessibilityDelegate());
        if (ViewCompat.getImportantForAccessibility(this) == 0) {
            ViewCompat.setImportantForAccessibility(this, SCROLL_STATE_DRAGGING);
        }
    }

    public boolean isFakeDragging() {
        return this.mFakeDragging;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
    }

    protected void onDetachedFromWindow() {
        removeCallbacks(this.mEndScrollRunnable);
        super.onDetachedFromWindow();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mPageMargin > 0 && this.mMarginDrawable != null && this.mItems.size() > 0 && this.mAdapter != null) {
            int scrollX = getScrollX();
            int width = getWidth();
            float marginOffset = ((float) this.mPageMargin) / ((float) width);
            int itemIndex = SCROLL_STATE_IDLE;
            ItemInfo ii = (ItemInfo) this.mItems.get(SCROLL_STATE_IDLE);
            float offset = ii.offset;
            int itemCount = this.mItems.size();
            int firstPos = ii.position;
            int lastPos = ((ItemInfo) this.mItems.get(itemCount - 1)).position;
            int pos = firstPos;
            while (pos < lastPos) {
                float drawAt;
                while (pos > ii.position && itemIndex < itemCount) {
                    itemIndex++;
                    ii = this.mItems.get(itemIndex);
                }
                if (pos == ii.position) {
                    drawAt = (ii.offset + ii.widthFactor) * ((float) width);
                    offset = ii.offset + ii.widthFactor + marginOffset;
                } else {
                    float widthFactor = this.mAdapter.getPageWidth(pos);
                    drawAt = (offset + widthFactor) * ((float) width);
                    offset += widthFactor + marginOffset;
                }
                if (((float) this.mPageMargin) + drawAt > ((float) scrollX)) {
                    this.mMarginDrawable.setBounds((int) drawAt, this.mTopPageBounds, (int) (((float) this.mPageMargin) + drawAt + 0.5f), this.mBottomPageBounds);
                    this.mMarginDrawable.draw(canvas);
                }
                if (drawAt <= ((float) (scrollX + width))) {
                    pos++;
                } else {
                    return;
                }
            }
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction() & 255;
        if (action == 3 || action == 1) {
            this.mIsBeingDragged = false;
            this.mIsUnableToDrag = false;
            this.mActivePointerId = -1;
            if (this.mVelocityTracker != null) {
                this.mVelocityTracker.recycle();
                this.mVelocityTracker = null;
            }
            return DEBUG;
        } else {
            if (action != 0 && this.mIsBeingDragged) {
                return true;
            }
            if (this.mIsUnableToDrag) {
                return DEBUG;
            }
            switch (action) {
                case SCROLL_STATE_IDLE:
                    float x = ev.getX();
                    this.mInitialMotionX = x;
                    this.mLastMotionX = x;
                    x = ev.getY();
                    this.mInitialMotionY = x;
                    this.mLastMotionY = x;
                    this.mActivePointerId = MotionEventCompat.getPointerId(ev, SCROLL_STATE_IDLE);
                    this.mIsUnableToDrag = false;
                    this.mScroller.computeScrollOffset();
                    if (this.mScrollState != 2 || Math.abs(this.mScroller.getFinalX() - this.mScroller.getCurrX()) <= this.mCloseEnough) {
                        completeScroll(DEBUG);
                        this.mIsBeingDragged = false;
                    } else {
                        this.mScroller.abortAnimation();
                        this.mPopulatePending = false;
                        populate();
                        this.mIsBeingDragged = true;
                        requestParentDisallowInterceptTouchEvent(true);
                        setScrollState(SCROLL_STATE_DRAGGING);
                    }
                    break;
                case SCROLL_STATE_SETTLING:
                    int activePointerId = this.mActivePointerId;
                    if (activePointerId != -1) {
                        int pointerIndex = MotionEventCompat.findPointerIndex(ev, activePointerId);
                        float x2 = MotionEventCompat.getX(ev, pointerIndex);
                        float dx = x2 - this.mLastMotionX;
                        float xDiff = Math.abs(dx);
                        float y = MotionEventCompat.getY(ev, pointerIndex);
                        float yDiff = Math.abs(y - this.mInitialMotionY);
                        if (dx == 0.0f || isGutterDrag(this.mLastMotionX, dx) || !canScroll(this, DEBUG, (int) dx, (int) x2, (int) y)) {
                            if (xDiff > ((float) this.mTouchSlop) && 0.5f * xDiff > yDiff) {
                                this.mIsBeingDragged = true;
                                requestParentDisallowInterceptTouchEvent(true);
                                setScrollState(SCROLL_STATE_DRAGGING);
                                this.mLastMotionX = dx > 0.0f ? this.mInitialMotionX + ((float) this.mTouchSlop) : this.mInitialMotionX - ((float) this.mTouchSlop);
                                this.mLastMotionY = y;
                                setScrollingCacheEnabled(true);
                            } else if (yDiff > ((float) this.mTouchSlop)) {
                                this.mIsUnableToDrag = true;
                            }
                            if (this.mIsBeingDragged && performDrag(x2)) {
                                ViewCompat.postInvalidateOnAnimation(this);
                            }
                        } else {
                            this.mLastMotionX = x2;
                            this.mLastMotionY = y;
                            this.mIsUnableToDrag = true;
                            return DEBUG;
                        }
                    }
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    onSecondaryPointerUp(ev);
                    break;
            }
            if (this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            }
            this.mVelocityTracker.addMovement(ev);
            return this.mIsBeingDragged;
        }
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        LayoutParams lp;
        int childLeft;
        int childTop;
        int count = getChildCount();
        int width = r - l;
        int height = b - t;
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        int scrollX = getScrollX();
        int decorCount = SCROLL_STATE_IDLE;
        int i = SCROLL_STATE_IDLE;
        while (i < count) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                lp = (LayoutParams) child.getLayoutParams();
                if (lp.isDecor) {
                    int vgrav = lp.gravity & 112;
                    switch (lp.gravity & 7) {
                        case SCROLL_STATE_DRAGGING:
                            childLeft = Math.max((width - child.getMeasuredWidth()) / 2, paddingLeft);
                            break;
                        case MMAdView.TRANSITION_DOWN:
                            childLeft = paddingLeft;
                            paddingLeft += child.getMeasuredWidth();
                            break;
                        case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                            childLeft = width - paddingRight - child.getMeasuredWidth();
                            paddingRight += child.getMeasuredWidth();
                            break;
                        default:
                            childLeft = paddingLeft;
                            break;
                    }
                    switch (vgrav) {
                        case DEFAULT_GUTTER_SIZE:
                            childTop = Math.max((height - child.getMeasuredHeight()) / 2, paddingTop);
                            break;
                        case ApiEventType.API_MRAID_HIDE_VIDEO:
                            childTop = paddingTop;
                            paddingTop += child.getMeasuredHeight();
                            break;
                        case VservConstants.THREAD_SLEEP:
                            childTop = height - paddingBottom - child.getMeasuredHeight();
                            paddingBottom += child.getMeasuredHeight();
                            break;
                        default:
                            childTop = paddingTop;
                            break;
                    }
                    childLeft += scrollX;
                    child.layout(childLeft, childTop, child.getMeasuredWidth() + childLeft, child.getMeasuredHeight() + childTop);
                    decorCount++;
                }
            }
            i++;
        }
        int childWidth = width - paddingLeft - paddingRight;
        i = SCROLL_STATE_IDLE;
        while (i < count) {
            child = getChildAt(i);
            if (child.getVisibility() != 8) {
                lp = child.getLayoutParams();
                if (!lp.isDecor) {
                    ItemInfo ii = infoForChild(child);
                    if (ii != null) {
                        childLeft = paddingLeft + ((int) (((float) childWidth) * ii.offset));
                        childTop = paddingTop;
                        if (lp.needsMeasure) {
                            lp.needsMeasure = false;
                            child.measure(MeasureSpec.makeMeasureSpec((int) (((float) childWidth) * lp.widthFactor), 1073741824), MeasureSpec.makeMeasureSpec(height - paddingTop - paddingBottom, 1073741824));
                        }
                        child.layout(childLeft, childTop, child.getMeasuredWidth() + childLeft, child.getMeasuredHeight() + childTop);
                    }
                }
            }
            i++;
        }
        this.mTopPageBounds = paddingTop;
        this.mBottomPageBounds = height - paddingBottom;
        this.mDecorChildCount = decorCount;
        if (this.mFirstLayout) {
            scrollToItem(this.mCurItem, false, 0, false);
        }
        this.mFirstLayout = false;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        int measuredWidth = getMeasuredWidth();
        this.mGutterSize = Math.min(measuredWidth / 10, this.mDefaultGutterSize);
        int childWidthSize = measuredWidth - getPaddingLeft() - getPaddingRight();
        int childHeightSize = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        int size = getChildCount();
        int i = SCROLL_STATE_IDLE;
        while (i < size) {
            LayoutParams lp;
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                lp = (LayoutParams) child.getLayoutParams();
                if (lp != null && lp.isDecor) {
                    int hgrav = lp.gravity & 7;
                    int vgrav = lp.gravity & 112;
                    int i2 = ExploreByTouchHelper.INVALID_ID;
                    int heightMode = ExploreByTouchHelper.INVALID_ID;
                    boolean consumeVertical = (vgrav == 48 || vgrav == 80) ? true : DEBUG;
                    boolean consumeHorizontal = (hgrav == 3 || hgrav == 5) ? true : DEBUG;
                    if (consumeVertical) {
                        i2 = 1073741824;
                    } else if (consumeHorizontal) {
                        heightMode = 1073741824;
                    }
                    int widthSize = childWidthSize;
                    int heightSize = childHeightSize;
                    if (lp.width != -2) {
                        i2 = 1073741824;
                        if (lp.width != -1) {
                            widthSize = lp.width;
                        }
                    }
                    if (lp.height != -2) {
                        heightMode = 1073741824;
                        if (lp.height != -1) {
                            heightSize = lp.height;
                        }
                    }
                    child.measure(MeasureSpec.makeMeasureSpec(widthSize, i2), MeasureSpec.makeMeasureSpec(heightSize, heightMode));
                    if (consumeVertical) {
                        childHeightSize -= child.getMeasuredHeight();
                    } else if (consumeHorizontal) {
                        childWidthSize -= child.getMeasuredWidth();
                    }
                }
            }
            i++;
        }
        this.mChildWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, 1073741824);
        this.mChildHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize, 1073741824);
        this.mInLayout = true;
        populate();
        this.mInLayout = false;
        size = getChildCount();
        i = SCROLL_STATE_IDLE;
        while (i < size) {
            child = getChildAt(i);
            if (child.getVisibility() != 8) {
                lp = child.getLayoutParams();
                if (lp == null || !lp.isDecor) {
                    child.measure(MeasureSpec.makeMeasureSpec((int) (((float) childWidthSize) * lp.widthFactor), 1073741824), this.mChildHeightMeasureSpec);
                }
            }
            i++;
        }
    }

    protected void onPageScrolled(int position, float offset, int offsetPixels) {
        int scrollX;
        int childCount;
        int i;
        View child;
        if (this.mDecorChildCount > 0) {
            scrollX = getScrollX();
            int paddingLeft = getPaddingLeft();
            int paddingRight = getPaddingRight();
            int width = getWidth();
            childCount = getChildCount();
            i = SCROLL_STATE_IDLE;
            while (i < childCount) {
                child = getChildAt(i);
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (lp.isDecor) {
                    int childLeft;
                    switch (lp.gravity & 7) {
                        case SCROLL_STATE_DRAGGING:
                            childLeft = Math.max((width - child.getMeasuredWidth()) / 2, paddingLeft);
                            break;
                        case MMAdView.TRANSITION_DOWN:
                            childLeft = paddingLeft;
                            paddingLeft += child.getWidth();
                            break;
                        case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                            childLeft = width - paddingRight - child.getMeasuredWidth();
                            paddingRight += child.getMeasuredWidth();
                            break;
                        default:
                            childLeft = paddingLeft;
                            break;
                    }
                    int childOffset = childLeft + scrollX - child.getLeft();
                    if (childOffset != 0) {
                        child.offsetLeftAndRight(childOffset);
                    }
                }
                i++;
            }
        }
        if (this.mOnPageChangeListener != null) {
            this.mOnPageChangeListener.onPageScrolled(position, offset, offsetPixels);
        }
        if (this.mInternalPageChangeListener != null) {
            this.mInternalPageChangeListener.onPageScrolled(position, offset, offsetPixels);
        }
        if (this.mPageTransformer != null) {
            scrollX = getScrollX();
            childCount = getChildCount();
            i = SCROLL_STATE_IDLE;
            while (i < childCount) {
                child = getChildAt(i);
                if (!child.getLayoutParams().isDecor) {
                    this.mPageTransformer.transformPage(child, ((float) (child.getLeft() - scrollX)) / ((float) getClientWidth()));
                }
                i++;
            }
        }
        this.mCalledSuper = true;
    }

    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        int index;
        int increment;
        int end;
        int count = getChildCount();
        if ((direction & 2) != 0) {
            index = SCROLL_STATE_IDLE;
            increment = SCROLL_STATE_DRAGGING;
            end = count;
        } else {
            index = count - 1;
            increment = INVALID_POINTER;
            end = INVALID_POINTER;
        }
        int i = index;
        while (i != end) {
            View child = getChildAt(i);
            if (child.getVisibility() == 0) {
                ItemInfo ii = infoForChild(child);
                if (ii != null && ii.position == this.mCurItem && child.requestFocus(direction, previouslyFocusedRect)) {
                    return true;
                }
            }
            i += increment;
        }
        return DEBUG;
    }

    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {
            SavedState ss = (SavedState) state;
            super.onRestoreInstanceState(ss.getSuperState());
            if (this.mAdapter != null) {
                this.mAdapter.restoreState(ss.adapterState, ss.loader);
                setCurrentItemInternal(ss.position, DEBUG, true);
            } else {
                this.mRestoredCurItem = ss.position;
                this.mRestoredAdapterState = ss.adapterState;
                this.mRestoredClassLoader = ss.loader;
            }
        } else {
            super.onRestoreInstanceState(state);
        }
    }

    public Parcelable onSaveInstanceState() {
        SavedState ss = new SavedState(super.onSaveInstanceState());
        ss.position = this.mCurItem;
        if (this.mAdapter != null) {
            ss.adapterState = this.mAdapter.saveState();
        }
        return ss;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw) {
            recomputeScrollPosition(w, oldw, this.mPageMargin, this.mPageMargin);
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (this.mFakeDragging) {
            return true;
        }
        if (ev.getAction() == 0 && ev.getEdgeFlags() != 0) {
            return DEBUG;
        }
        if (this.mAdapter == null || this.mAdapter.getCount() == 0) {
            return DEBUG;
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(ev);
        int action = ev.getAction();
        boolean needsInvalidate = DEBUG;
        float x;
        switch (action & 255) {
            case SCROLL_STATE_IDLE:
                this.mScroller.abortAnimation();
                this.mPopulatePending = false;
                populate();
                x = ev.getX();
                this.mInitialMotionX = x;
                this.mLastMotionX = x;
                x = ev.getY();
                this.mInitialMotionY = x;
                this.mLastMotionY = x;
                this.mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                break;
            case SCROLL_STATE_DRAGGING:
                if (this.mIsBeingDragged) {
                    VelocityTracker velocityTracker = this.mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
                    int initialVelocity = (int) VelocityTrackerCompat.getXVelocity(velocityTracker, this.mActivePointerId);
                    this.mPopulatePending = true;
                    int width = getClientWidth();
                    int scrollX = getScrollX();
                    ItemInfo ii = infoForCurrentScrollPosition();
                    setCurrentItemInternal(determineTargetPage(ii.position, ((((float) scrollX) / ((float) width)) - ii.offset) / ii.widthFactor, initialVelocity, (int) (MotionEventCompat.getX(ev, MotionEventCompat.findPointerIndex(ev, this.mActivePointerId)) - this.mInitialMotionX)), true, true, initialVelocity);
                    this.mActivePointerId = -1;
                    endDrag();
                    needsInvalidate = this.mLeftEdge.onRelease() | this.mRightEdge.onRelease();
                }
                break;
            case SCROLL_STATE_SETTLING:
                if (!this.mIsBeingDragged) {
                    int pointerIndex = MotionEventCompat.findPointerIndex(ev, this.mActivePointerId);
                    float x2 = MotionEventCompat.getX(ev, pointerIndex);
                    float xDiff = Math.abs(x2 - this.mLastMotionX);
                    float y = MotionEventCompat.getY(ev, pointerIndex);
                    float yDiff = Math.abs(y - this.mLastMotionY);
                    if (xDiff > ((float) this.mTouchSlop) && xDiff > yDiff) {
                        this.mIsBeingDragged = true;
                        requestParentDisallowInterceptTouchEvent(true);
                        if (x2 - this.mInitialMotionX > 0.0f) {
                            x = this.mInitialMotionX + ((float) this.mTouchSlop);
                        } else {
                            x = this.mInitialMotionX - ((float) this.mTouchSlop);
                        }
                        this.mLastMotionX = x;
                        this.mLastMotionY = y;
                        setScrollState(1);
                        setScrollingCacheEnabled(true);
                        ViewParent parent = getParent();
                        if (parent != null) {
                            parent.requestDisallowInterceptTouchEvent(true);
                        }
                    }
                }
                if (this.mIsBeingDragged) {
                    needsInvalidate = false | performDrag(MotionEventCompat.getX(ev, MotionEventCompat.findPointerIndex(ev, this.mActivePointerId)));
                }
                break;
            case MMAdView.TRANSITION_DOWN:
                if (this.mIsBeingDragged) {
                    scrollToItem(this.mCurItem, true, 0, false);
                    this.mActivePointerId = -1;
                    endDrag();
                    needsInvalidate = this.mLeftEdge.onRelease() | this.mRightEdge.onRelease();
                }
                break;
            case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                int index = MotionEventCompat.getActionIndex(ev);
                this.mLastMotionX = MotionEventCompat.getX(ev, index);
                this.mActivePointerId = MotionEventCompat.getPointerId(ev, index);
                break;
            case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                onSecondaryPointerUp(ev);
                this.mLastMotionX = MotionEventCompat.getX(ev, MotionEventCompat.findPointerIndex(ev, this.mActivePointerId));
                break;
        }
        if (needsInvalidate) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
        return true;
    }

    boolean pageLeft() {
        if (this.mCurItem <= 0) {
            return DEBUG;
        }
        setCurrentItem(this.mCurItem - 1, true);
        return true;
    }

    boolean pageRight() {
        if (this.mAdapter == null || this.mCurItem >= this.mAdapter.getCount() - 1) {
            return DEBUG;
        }
        setCurrentItem(this.mCurItem + 1, true);
        return true;
    }

    void populate() {
        populate(this.mCurItem);
    }

    void populate(int newCurrentItem) {
        ItemInfo oldCurInfo = null;
        int focusDirection = SCROLL_STATE_SETTLING;
        if (this.mCurItem != newCurrentItem) {
            focusDirection = this.mCurItem < newCurrentItem ? 66 : ApiEventType.API_MRAID_GET_SCREEN_SIZE;
            oldCurInfo = infoForPosition(this.mCurItem);
            this.mCurItem = newCurrentItem;
        }
        if (this.mAdapter == null) {
            sortChildDrawingOrder();
        } else if (this.mPopulatePending) {
            sortChildDrawingOrder();
        } else if (getWindowToken() != null) {
            this.mAdapter.startUpdate(this);
            int pageLimit = this.mOffscreenPageLimit;
            int startPos = Math.max(SCROLL_STATE_IDLE, this.mCurItem - pageLimit);
            int N = this.mAdapter.getCount();
            int endPos = Math.min(N - 1, this.mCurItem + pageLimit);
            if (N != this.mExpectedAdapterCount) {
                String resName;
                try {
                    resName = getResources().getResourceName(getId());
                } catch (NotFoundException e) {
                    resName = Integer.toHexString(getId());
                }
                throw new IllegalStateException("The application's PagerAdapter changed the adapter's contents without calling PagerAdapter#notifyDataSetChanged! Expected adapter item count: " + this.mExpectedAdapterCount + ", found: " + N + " Pager id: " + resName + " Pager class: " + getClass() + " Problematic adapter: " + this.mAdapter.getClass());
            } else {
                ItemInfo ii;
                Object obj;
                View child;
                ItemInfo curItem = null;
                int curIndex = SCROLL_STATE_IDLE;
                while (curIndex < this.mItems.size()) {
                    ii = (ItemInfo) this.mItems.get(curIndex);
                    if (ii.position >= this.mCurItem) {
                        if (ii.position == this.mCurItem) {
                            curItem = ii;
                            break;
                        }
                    } else {
                        curIndex++;
                    }
                }
                if (curItem == null && N > 0) {
                    curItem = addNewItem(this.mCurItem, curIndex);
                }
                if (curItem != null) {
                    float extraWidthLeft = BitmapDescriptorFactory.HUE_RED;
                    int itemIndex = curIndex - 1;
                    ii = itemIndex >= 0 ? (ItemInfo) this.mItems.get(itemIndex) : null;
                    int clientWidth = getClientWidth();
                    float leftWidthNeeded = clientWidth <= 0 ? BitmapDescriptorFactory.HUE_RED : 2.0f - curItem.widthFactor + ((float) getPaddingLeft()) / ((float) clientWidth);
                    int pos = this.mCurItem - 1;
                    while (pos >= 0) {
                        if (extraWidthLeft < leftWidthNeeded || pos >= startPos) {
                            if (ii == null || pos != ii.position) {
                                extraWidthLeft += addNewItem(pos, itemIndex + 1).widthFactor;
                                curIndex++;
                                ii = itemIndex >= 0 ? (ItemInfo) this.mItems.get(itemIndex) : null;
                            } else {
                                extraWidthLeft += ii.widthFactor;
                                itemIndex--;
                                ii = itemIndex >= 0 ? (ItemInfo) this.mItems.get(itemIndex) : null;
                            }
                        } else if (ii == null) {
                            break;
                        } else {
                            if (pos == ii.position && !ii.scrolling) {
                                this.mItems.remove(itemIndex);
                                this.mAdapter.destroyItem(this, pos, ii.object);
                                itemIndex--;
                                curIndex--;
                                if (itemIndex >= 0) {
                                    ii = (ItemInfo) this.mItems.get(itemIndex);
                                } else {
                                    ii = null;
                                }
                            }
                        }
                        pos--;
                    }
                    float extraWidthRight = curItem.widthFactor;
                    itemIndex = curIndex + 1;
                    if (extraWidthRight < 2.0f) {
                        ii = itemIndex < this.mItems.size() ? (ItemInfo) this.mItems.get(itemIndex) : null;
                        float rightWidthNeeded = clientWidth <= 0 ? BitmapDescriptorFactory.HUE_RED : ((float) getPaddingRight()) / ((float) clientWidth) + 2.0f;
                        pos = this.mCurItem + 1;
                        while (pos < N) {
                            if (extraWidthRight < rightWidthNeeded || pos <= endPos) {
                                if (ii == null || pos != ii.position) {
                                    itemIndex++;
                                    extraWidthRight += addNewItem(pos, itemIndex).widthFactor;
                                    ii = itemIndex < this.mItems.size() ? (ItemInfo) this.mItems.get(itemIndex) : null;
                                } else {
                                    extraWidthRight += ii.widthFactor;
                                    itemIndex++;
                                    ii = itemIndex < this.mItems.size() ? (ItemInfo) this.mItems.get(itemIndex) : null;
                                }
                            } else if (ii == null) {
                                break;
                            } else {
                                if (pos == ii.position && !ii.scrolling) {
                                    this.mItems.remove(itemIndex);
                                    this.mAdapter.destroyItem(this, pos, ii.object);
                                    if (itemIndex < this.mItems.size()) {
                                        ii = (ItemInfo) this.mItems.get(itemIndex);
                                    } else {
                                        ii = null;
                                    }
                                }
                            }
                            pos++;
                        }
                    }
                    calculatePageOffsets(curItem, curIndex, oldCurInfo);
                }
                PagerAdapter pagerAdapter = this.mAdapter;
                int i = this.mCurItem;
                if (curItem != null) {
                    obj = curItem.object;
                } else {
                    boolean z = SCROLL_STATE_IDLE;
                }
                pagerAdapter.setPrimaryItem(this, i, obj);
                this.mAdapter.finishUpdate(this);
                int childCount = getChildCount();
                int i2 = SCROLL_STATE_IDLE;
                while (i2 < childCount) {
                    child = getChildAt(i2);
                    LayoutParams lp = (LayoutParams) child.getLayoutParams();
                    lp.childIndex = i2;
                    if (!lp.isDecor && lp.widthFactor == 0.0f) {
                        ii = infoForChild(child);
                        if (ii != null) {
                            lp.widthFactor = ii.widthFactor;
                            lp.position = ii.position;
                        }
                    }
                    i2++;
                }
                sortChildDrawingOrder();
                if (hasFocus()) {
                    View currentFocused = findFocus();
                    ii = currentFocused != null ? infoForAnyChild(currentFocused) : null;
                    if (ii == null || ii.position != this.mCurItem) {
                        i2 = SCROLL_STATE_IDLE;
                        while (i2 < getChildCount()) {
                            child = getChildAt(i2);
                            ii = infoForChild(child);
                            if (ii == null || ii.position != this.mCurItem || !child.requestFocus(focusDirection)) {
                                i2++;
                            } else {
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    public void removeView(View view) {
        if (this.mInLayout) {
            removeViewInLayout(view);
        } else {
            super.removeView(view);
        }
    }

    public void setAdapter(PagerAdapter adapter) {
        if (this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(this.mObserver);
            this.mAdapter.startUpdate(this);
            int i = SCROLL_STATE_IDLE;
            while (i < this.mItems.size()) {
                ItemInfo ii = (ItemInfo) this.mItems.get(i);
                this.mAdapter.destroyItem(this, ii.position, ii.object);
                i++;
            }
            this.mAdapter.finishUpdate(this);
            this.mItems.clear();
            removeNonDecorViews();
            this.mCurItem = 0;
            scrollTo(SCROLL_STATE_IDLE, SCROLL_STATE_IDLE);
        }
        PagerAdapter oldAdapter = this.mAdapter;
        this.mAdapter = adapter;
        this.mExpectedAdapterCount = 0;
        if (this.mAdapter != null) {
            if (this.mObserver == null) {
                this.mObserver = new PagerObserver(null);
            }
            this.mAdapter.registerDataSetObserver(this.mObserver);
            this.mPopulatePending = false;
            boolean wasFirstLayout = this.mFirstLayout;
            this.mFirstLayout = true;
            this.mExpectedAdapterCount = this.mAdapter.getCount();
            if (this.mRestoredCurItem >= 0) {
                this.mAdapter.restoreState(this.mRestoredAdapterState, this.mRestoredClassLoader);
                setCurrentItemInternal(this.mRestoredCurItem, DEBUG, true);
                this.mRestoredCurItem = -1;
                this.mRestoredAdapterState = null;
                this.mRestoredClassLoader = null;
            } else if (wasFirstLayout) {
                requestLayout();
            } else {
                populate();
            }
        }
        if (this.mAdapterChangeListener != null && oldAdapter != adapter) {
            this.mAdapterChangeListener.onAdapterChanged(oldAdapter, adapter);
        }
    }

    void setChildrenDrawingOrderEnabledCompat(boolean enable) {
        if (VERSION.SDK_INT >= 7) {
            if (this.mSetChildrenDrawingOrderEnabled == null) {
                try {
                    this.mSetChildrenDrawingOrderEnabled = ViewGroup.class.getDeclaredMethod("setChildrenDrawingOrderEnabled", new Class[]{Boolean.TYPE});
                } catch (NoSuchMethodException e) {
                    Log.e(TAG, "Can't find setChildrenDrawingOrderEnabled", e);
                }
            }
            try {
                this.mSetChildrenDrawingOrderEnabled.invoke(this, new Object[]{Boolean.valueOf(enable)});
            } catch (Exception e2) {
                Log.e(TAG, "Error changing children drawing order", e2);
            }
        }
    }

    public void setCurrentItem(int item) {
        boolean z;
        this.mPopulatePending = false;
        if (this.mFirstLayout) {
            z = false;
        } else {
            z = true;
        }
        setCurrentItemInternal(item, z, DEBUG);
    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        this.mPopulatePending = false;
        setCurrentItemInternal(item, smoothScroll, DEBUG);
    }

    void setCurrentItemInternal(int item, boolean smoothScroll, boolean always) {
        setCurrentItemInternal(item, smoothScroll, always, SCROLL_STATE_IDLE);
    }

    void setCurrentItemInternal(int item, boolean smoothScroll, boolean always, int velocity) {
        boolean dispatchSelected = true;
        if (this.mAdapter == null || this.mAdapter.getCount() <= 0) {
            setScrollingCacheEnabled(DEBUG);
        } else if (always || this.mCurItem != item || this.mItems.size() == 0) {
            if (item < 0) {
                item = SCROLL_STATE_IDLE;
            } else if (item >= this.mAdapter.getCount()) {
                item = this.mAdapter.getCount() - 1;
            }
            int pageLimit = this.mOffscreenPageLimit;
            if (item > this.mCurItem + pageLimit || item < this.mCurItem - pageLimit) {
                int i = SCROLL_STATE_IDLE;
                while (i < this.mItems.size()) {
                    ((ItemInfo) this.mItems.get(i)).scrolling = true;
                    i++;
                }
            }
            if (this.mCurItem == item) {
                dispatchSelected = false;
            }
            if (this.mFirstLayout) {
                this.mCurItem = item;
                if (dispatchSelected && this.mOnPageChangeListener != null) {
                    this.mOnPageChangeListener.onPageSelected(item);
                }
                if (dispatchSelected && this.mInternalPageChangeListener != null) {
                    this.mInternalPageChangeListener.onPageSelected(item);
                }
                requestLayout();
            } else {
                populate(item);
                scrollToItem(item, smoothScroll, velocity, dispatchSelected);
            }
        } else {
            setScrollingCacheEnabled(DEBUG);
        }
    }

    OnPageChangeListener setInternalPageChangeListener(OnPageChangeListener listener) {
        OnPageChangeListener oldListener = this.mInternalPageChangeListener;
        this.mInternalPageChangeListener = listener;
        return oldListener;
    }

    public void setOffscreenPageLimit(int limit) {
        if (limit < 1) {
            Log.w(TAG, "Requested offscreen page limit " + limit + " too small; defaulting to " + SCROLL_STATE_DRAGGING);
            limit = SCROLL_STATE_DRAGGING;
        }
        if (limit != this.mOffscreenPageLimit) {
            this.mOffscreenPageLimit = limit;
            populate();
        }
    }

    void setOnAdapterChangeListener(OnAdapterChangeListener listener) {
        this.mAdapterChangeListener = listener;
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.mOnPageChangeListener = listener;
    }

    public void setPageMargin(int marginPixels) {
        int oldMargin = this.mPageMargin;
        this.mPageMargin = marginPixels;
        int width = getWidth();
        recomputeScrollPosition(width, width, marginPixels, oldMargin);
        requestLayout();
    }

    public void setPageMarginDrawable(int resId) {
        setPageMarginDrawable(getContext().getResources().getDrawable(resId));
    }

    public void setPageMarginDrawable(Drawable d) {
        this.mMarginDrawable = d;
        if (d != null) {
            refreshDrawableState();
        }
        setWillNotDraw(d == null ? true : DEBUG);
        invalidate();
    }

    public void setPageTransformer(boolean reverseDrawingOrder, PageTransformer transformer) {
        int i = SCROLL_STATE_DRAGGING;
        if (VERSION.SDK_INT >= 11) {
            boolean hasTransformer = transformer != null;
            boolean z;
            if (this.mPageTransformer != null) {
                z = true;
            } else {
                z = false;
            }
            boolean needsPopulate = hasTransformer != z;
            this.mPageTransformer = transformer;
            setChildrenDrawingOrderEnabledCompat(hasTransformer);
            if (hasTransformer) {
                if (reverseDrawingOrder) {
                    i = SCROLL_STATE_SETTLING;
                }
                this.mDrawingOrder = i;
            } else {
                this.mDrawingOrder = 0;
            }
            if (needsPopulate) {
                populate();
            }
        }
    }

    void smoothScrollTo(int x, int y) {
        smoothScrollTo(x, y, SCROLL_STATE_IDLE);
    }

    void smoothScrollTo(int x, int y, int velocity) {
        if (getChildCount() == 0) {
            setScrollingCacheEnabled(DEBUG);
        } else {
            int sx = getScrollX();
            int sy = getScrollY();
            int dx = x - sx;
            int dy = y - sy;
            if (dx == 0 && dy == 0) {
                completeScroll(DEBUG);
                populate();
                setScrollState(SCROLL_STATE_IDLE);
            } else {
                int duration;
                setScrollingCacheEnabled(true);
                setScrollState(SCROLL_STATE_SETTLING);
                int width = getClientWidth();
                int halfWidth = width / 2;
                float distance = ((float) halfWidth) + ((float) halfWidth) * distanceInfluenceForSnapDuration(Math.min(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT, (1.0f * ((float) Math.abs(dx))) / ((float) width)));
                velocity = Math.abs(velocity);
                if (velocity > 0) {
                    duration = Math.round(1000.0f * Math.abs(distance / ((float) velocity))) * 4;
                } else {
                    duration = (int) ((1.0f + (((float) Math.abs(dx)) / (((float) this.mPageMargin) + (((float) width) * this.mAdapter.getPageWidth(this.mCurItem))))) * 100.0f);
                }
                this.mScroller.startScroll(sx, sy, dx, dy, Math.min(duration, MAX_SETTLE_DURATION));
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }
    }

    protected boolean verifyDrawable(Drawable who) {
        return (super.verifyDrawable(who) || who == this.mMarginDrawable) ? true : DEBUG;
    }
}