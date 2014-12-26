package com.android.systemui.recent;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.Configuration;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import com.android.systemui.R;
import com.android.systemui.SwipeHelper;
import com.android.systemui.SwipeHelper.Callback;
import com.android.systemui.recent.RecentsPanelView.RecentsScrollView;
import java.util.HashSet;
import java.util.Iterator;

public class RecentsHorizontalScrollView extends HorizontalScrollView implements Callback, RecentsScrollView {
    private static final boolean DEBUG = false;
    private static final String TAG = "RecentsPanelView";
    private TaskDescriptionAdapter mAdapter;
    private RecentsCallback mCallback;
    protected int mLastScrollPosition;
    private LinearLayout mLinearLayout;
    private int mNumItemsInOneScreenful;
    private RecentsScrollViewPerformanceHelper mPerformanceHelper;
    private HashSet<View> mRecycledViews;
    private SwipeHelper mSwipeHelper;

    class AnonymousClass_3 implements OnClickListener {
        final /* synthetic */ View val$view;

        AnonymousClass_3(View view) {
            this.val$view = view;
        }

        public void onClick(View v) {
            RecentsHorizontalScrollView.this.mCallback.handleOnClick(this.val$view);
        }
    }

    class AnonymousClass_4 implements OnLongClickListener {
        final /* synthetic */ View val$thumbnailView;
        final /* synthetic */ View val$view;

        AnonymousClass_4(View view, View view2) {
            this.val$view = view;
            this.val$thumbnailView = view2;
        }

        public boolean onLongClick(View v) {
            RecentsHorizontalScrollView.this.mCallback.handleLongPress(this.val$view, this.val$view.findViewById(R.id.app_description), this.val$thumbnailView);
            return true;
        }
    }

    class AnonymousClass_5 implements OnGlobalLayoutListener {
        final /* synthetic */ ViewTreeObserver val$observer;

        AnonymousClass_5(ViewTreeObserver viewTreeObserver) {
            this.val$observer = viewTreeObserver;
        }

        public void onGlobalLayout() {
            RecentsHorizontalScrollView.this.mLastScrollPosition = RecentsHorizontalScrollView.this.scrollPositionOfMostRecent();
            RecentsHorizontalScrollView.this.scrollTo(RecentsHorizontalScrollView.this.mLastScrollPosition, 0);
            if (this.val$observer.isAlive()) {
                this.val$observer.removeOnGlobalLayoutListener(this);
            }
        }
    }

    public RecentsHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        this.mSwipeHelper = new SwipeHelper(1, this, getResources().getDisplayMetrics().density, (float) ViewConfiguration.get(this.mContext).getScaledPagingTouchSlop());
        this.mPerformanceHelper = RecentsScrollViewPerformanceHelper.create(context, attrs, this, DEBUG);
        this.mRecycledViews = new HashSet();
    }

    private void addToRecycledViews(View v) {
        if (this.mRecycledViews.size() < this.mNumItemsInOneScreenful) {
            this.mRecycledViews.add(v);
        }
    }

    private int scrollPositionOfMostRecent() {
        return this.mLinearLayout.getWidth() - getWidth();
    }

    private void setOverScrollEffectPadding(int leftPadding, int i) {
    }

    private void update() {
        int i = 0;
        while (i < this.mLinearLayout.getChildCount()) {
            View v = this.mLinearLayout.getChildAt(i);
            addToRecycledViews(v);
            this.mAdapter.recycleView(v);
            i++;
        }
        LayoutTransition transitioner = getLayoutTransition();
        setLayoutTransition(null);
        this.mLinearLayout.removeAllViews();
        Iterator<View> recycledViews = this.mRecycledViews.iterator();
        i = 0;
        while (i < this.mAdapter.getCount()) {
            View old = null;
            if (recycledViews.hasNext()) {
                old = recycledViews.next();
                recycledViews.remove();
                old.setVisibility(0);
            }
            View view = this.mAdapter.getView(i, old, this.mLinearLayout);
            if (this.mPerformanceHelper != null) {
                this.mPerformanceHelper.addViewCallback(view);
            }
            OnTouchListener noOpListener = new OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            };
            view.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    RecentsHorizontalScrollView.this.mCallback.dismiss();
                }
            });
            view.setSoundEffectsEnabled(DEBUG);
            OnClickListener launchAppListener = new AnonymousClass_3(view);
            View thumbnailView = ((ViewHolder) view.getTag()).thumbnailView;
            OnLongClickListener longClickListener = new AnonymousClass_4(view, thumbnailView);
            thumbnailView.setClickable(true);
            thumbnailView.setOnClickListener(launchAppListener);
            thumbnailView.setOnLongClickListener(longClickListener);
            View appTitle = view.findViewById(R.id.app_label);
            appTitle.setContentDescription(" ");
            appTitle.setOnTouchListener(noOpListener);
            this.mLinearLayout.addView(view);
            i++;
        }
        setLayoutTransition(transitioner);
        ViewTreeObserver observer = getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new AnonymousClass_5(observer));
    }

    public boolean canChildBeDismissed(View v) {
        return true;
    }

    public void dismissChild(View v) {
        this.mSwipeHelper.dismissChild(v, 0.0f);
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (this.mPerformanceHelper != null) {
            int paddingLeft = this.mPaddingLeft;
            boolean offsetRequired = isPaddingOffsetRequired();
            if (offsetRequired) {
                paddingLeft += getLeftPaddingOffset();
            }
            int left = this.mScrollX + paddingLeft;
            int right = this.mRight + left - this.mLeft - this.mPaddingRight - paddingLeft;
            int top = this.mScrollY + getFadeTop(offsetRequired);
            int bottom = top + getFadeHeight(offsetRequired);
            if (offsetRequired) {
                right += getRightPaddingOffset();
                bottom += getBottomPaddingOffset();
            }
            this.mPerformanceHelper.drawCallback(canvas, left, right, top, bottom, this.mScrollX, this.mScrollY, 0.0f, 0.0f, getLeftFadingEdgeStrength(), getRightFadingEdgeStrength());
        }
    }

    public View getChildAtPosition(MotionEvent ev) {
        float x = ev.getX() + ((float) getScrollX());
        float y = ev.getY() + ((float) getScrollY());
        int i = 0;
        while (i < this.mLinearLayout.getChildCount()) {
            View item = this.mLinearLayout.getChildAt(i);
            if (x >= ((float) item.getLeft()) && x < ((float) item.getRight()) && y >= ((float) item.getTop()) && y < ((float) item.getBottom())) {
                return item;
            }
            i++;
        }
        return null;
    }

    public View getChildContentView(View v) {
        return v.findViewById(R.id.recent_item);
    }

    public int getHorizontalFadingEdgeLength() {
        return this.mPerformanceHelper != null ? this.mPerformanceHelper.getHorizontalFadingEdgeLengthCallback() : super.getHorizontalFadingEdgeLength();
    }

    public int getVerticalFadingEdgeLength() {
        return this.mPerformanceHelper != null ? this.mPerformanceHelper.getVerticalFadingEdgeLengthCallback() : super.getVerticalFadingEdgeLength();
    }

    public int numItemsInOneScreenful() {
        return this.mNumItemsInOneScreenful;
    }

    public void onAttachedToWindow() {
        if (this.mPerformanceHelper != null) {
            this.mPerformanceHelper.onAttachedToWindowCallback(this.mCallback, this.mLinearLayout, isHardwareAccelerated());
        }
    }

    public void onBeginDrag(View v) {
        requestDisallowInterceptTouchEvent(true);
    }

    public void onChildDismissed(View v) {
        addToRecycledViews(v);
        this.mLinearLayout.removeView(v);
        this.mCallback.handleSwipe(v);
        View contentView = getChildContentView(v);
        contentView.setAlpha(1.0f);
        contentView.setTranslationY(0.0f);
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.mSwipeHelper.setDensityScale(getResources().getDisplayMetrics().density);
        this.mSwipeHelper.setPagingTouchSlop((float) ViewConfiguration.get(this.mContext).getScaledPagingTouchSlop());
    }

    public void onDragCancelled(View v) {
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        setScrollbarFadingEnabled(true);
        this.mLinearLayout = (LinearLayout) findViewById(R.id.recents_linear_layout);
        setOverScrollEffectPadding(this.mContext.getResources().getDimensionPixelOffset(R.dimen.status_bar_recents_thumbnail_left_margin), 0);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return (this.mSwipeHelper.onInterceptTouchEvent(ev) || super.onInterceptTouchEvent(ev)) ? true : DEBUG;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        LayoutTransition transition = this.mLinearLayout.getLayoutTransition();
        if (transition == null || !transition.isRunning()) {
            this.mLastScrollPosition = scrollPositionOfMostRecent();
            post(new Runnable() {
                public void run() {
                    LayoutTransition transition = RecentsHorizontalScrollView.this.mLinearLayout.getLayoutTransition();
                    if (transition == null || !transition.isRunning()) {
                        RecentsHorizontalScrollView.this.scrollTo(RecentsHorizontalScrollView.this.mLastScrollPosition, 0);
                    }
                }
            });
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        return (this.mSwipeHelper.onTouchEvent(ev) || super.onTouchEvent(ev)) ? true : DEBUG;
    }

    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == 0 && changedView == this) {
            post(new Runnable() {
                public void run() {
                    RecentsHorizontalScrollView.this.update();
                }
            });
        }
    }

    public void removeViewInLayout(View view) {
        dismissChild(view);
    }

    public void setAdapter(TaskDescriptionAdapter adapter) {
        this.mAdapter = adapter;
        this.mAdapter.registerDataSetObserver(new DataSetObserver() {
            public void onChanged() {
                RecentsHorizontalScrollView.this.update();
            }

            public void onInvalidated() {
                RecentsHorizontalScrollView.this.update();
            }
        });
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(dm.widthPixels, Integer.MIN_VALUE);
        int childheightMeasureSpec = MeasureSpec.makeMeasureSpec(dm.heightPixels, Integer.MIN_VALUE);
        View child = this.mAdapter.createView(this.mLinearLayout);
        child.measure(childWidthMeasureSpec, childheightMeasureSpec);
        this.mNumItemsInOneScreenful = (int) FloatMath.ceil(((float) dm.widthPixels) / ((float) child.getMeasuredWidth()));
        addToRecycledViews(child);
        int i = 0;
        while (i < this.mNumItemsInOneScreenful - 1) {
            addToRecycledViews(this.mAdapter.createView(this.mLinearLayout));
            i++;
        }
    }

    public void setCallback(RecentsCallback callback) {
        this.mCallback = callback;
    }

    public void setLayoutTransition(LayoutTransition transition) {
        this.mLinearLayout.setLayoutTransition(transition);
    }

    public void setMinSwipeAlpha(float minAlpha) {
        this.mSwipeHelper.setMinAlpha(minAlpha);
    }
}