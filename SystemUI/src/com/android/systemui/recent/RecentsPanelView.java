package com.android.systemui.recent;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.LayoutTransition;
import android.app.ActivityManager;
import android.app.ActivityManagerNative;
import android.app.ActivityOptions;
import android.app.ActivityOptions.OnAnimationStartedListener;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.IWindowManager.Stub;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnDismissListener;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import com.android.systemui.R;
import com.android.systemui.statusbar.BaseStatusBar;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.tablet.StatusBarPanel;
import java.util.ArrayList;

public class RecentsPanelView extends FrameLayout implements OnItemClickListener, RecentsCallback, StatusBarPanel, AnimatorListener, OnTouchListener {
    static final boolean DEBUG = false;
    static final String TAG = "RecentsPanelView";
    private BaseStatusBar mBar;
    private Choreographer mChoreo;
    private Context mContext;
    private boolean mFirstScreenful;
    private boolean mFitThumbnailToXY;
    boolean mHideRecentsAfterThumbnailScaleUpStarted;
    private boolean mHighEndGfx;
    private TaskDescriptionAdapter mListAdapter;
    private int mNumItemsWaitingForThumbnailsAndIcons;
    ImageView mPlaceholderThumbnail;
    private PopupMenu mPopup;
    private Runnable mPreloadTasksRunnable;
    private boolean mReadyToShow;
    private int mRecentItemLayoutId;
    private ArrayList<TaskDescription> mRecentTaskDescriptions;
    private boolean mRecentTasksDirty;
    private RecentTasksLoader mRecentTasksLoader;
    private ViewGroup mRecentsContainer;
    private View mRecentsNoApps;
    private View mRecentsScrim;
    private boolean mShowing;
    private StatusBarTouchProxy mStatusBarTouchProxy;
    boolean mThumbnailScaleUpStarted;
    private int mThumbnailWidth;
    View mTransitionBg;
    OnRecentsPanelVisibilityChangedListener mVisibilityChangedListener;
    private boolean mWaitingToShow;
    private boolean mWaitingToShowAnimated;

    public static interface RecentsScrollView {
        int numItemsInOneScreenful();

        void setAdapter(TaskDescriptionAdapter taskDescriptionAdapter);

        void setCallback(RecentsCallback recentsCallback);

        void setMinSwipeAlpha(float f);
    }

    class AnonymousClass_3 implements OnMenuItemClickListener {
        final /* synthetic */ View val$selectedView;

        AnonymousClass_3(View view) {
            this.val$selectedView = view;
        }

        public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId() == 2131493025) {
                RecentsPanelView.this.mRecentsContainer.removeViewInLayout(this.val$selectedView);
            } else if (item.getItemId() != 2131493026) {
                return DEBUG;
            } else {
                ViewHolder viewHolder = (ViewHolder) this.val$selectedView.getTag();
                if (viewHolder != null) {
                    RecentsPanelView.this.startApplicationDetailsActivity(viewHolder.taskDescription.packageName);
                    RecentsPanelView.this.mBar.animateCollapse(0);
                } else {
                    throw new IllegalStateException("Oops, no tag on view " + this.val$selectedView);
                }
            }
            return true;
        }
    }

    class AnonymousClass_4 implements OnDismissListener {
        final /* synthetic */ View val$thumbnailView;

        AnonymousClass_4(View view) {
            this.val$thumbnailView = view;
        }

        public void onDismiss(PopupMenu menu) {
            this.val$thumbnailView.setSelected(DEBUG);
            RecentsPanelView.this.mPopup = null;
        }
    }

    private final class OnLongClickDelegate implements OnLongClickListener {
        View mOtherView;

        OnLongClickDelegate(View other) {
            this.mOtherView = other;
        }

        public boolean onLongClick(View v) {
            return this.mOtherView.performLongClick();
        }
    }

    public static interface OnRecentsPanelVisibilityChangedListener {
        void onRecentsPanelVisibilityChanged(boolean z);
    }

    final class TaskDescriptionAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public TaskDescriptionAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        public View createView(ViewGroup parent) {
            View convertView = this.mInflater.inflate(RecentsPanelView.this.mRecentItemLayoutId, parent, DEBUG);
            ViewHolder holder = new ViewHolder();
            holder.thumbnailView = convertView.findViewById(R.id.app_thumbnail);
            holder.thumbnailViewImage = (ImageView) convertView.findViewById(R.id.app_thumbnail_image);
            if (RecentsPanelView.this.mRecentTasksLoader != null) {
                RecentsPanelView.this.updateThumbnail(holder, RecentsPanelView.this.mRecentTasksLoader.getDefaultThumbnail(), DEBUG, DEBUG);
            }
            holder.iconView = (ImageView) convertView.findViewById(R.id.app_icon);
            if (RecentsPanelView.this.mRecentTasksLoader != null) {
                holder.iconView.setImageBitmap(RecentsPanelView.this.mRecentTasksLoader.getDefaultIcon());
            }
            holder.labelView = (TextView) convertView.findViewById(R.id.app_label);
            holder.descriptionView = (TextView) convertView.findViewById(R.id.app_description);
            convertView.setTag(holder);
            return convertView;
        }

        public int getCount() {
            return RecentsPanelView.this.mRecentTaskDescriptions != null ? RecentsPanelView.this.mRecentTaskDescriptions.size() : 0;
        }

        public Object getItem(int position) {
            return Integer.valueOf(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = createView(parent);
                if (convertView.getParent() != null) {
                    throw new RuntimeException("Recycled child has parent");
                }
            } else if (convertView.getParent() != null) {
                throw new RuntimeException("Recycled child has parent");
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            TaskDescription td = (TaskDescription) RecentsPanelView.this.mRecentTaskDescriptions.get(RecentsPanelView.this.mRecentTaskDescriptions.size() - position - 1);
            holder.labelView.setText(td.getLabel());
            holder.thumbnailView.setContentDescription(td.getLabel());
            holder.loadedThumbnailAndIcon = td.isLoaded();
            if (td.isLoaded()) {
                RecentsPanelView.this.updateThumbnail(holder, td.getThumbnail(), true, DEBUG);
                RecentsPanelView.this.updateIcon(holder, td.getIcon(), true, DEBUG);
                RecentsPanelView.access$510(RecentsPanelView.this);
            }
            holder.thumbnailView.setTag(td);
            holder.thumbnailView.setOnLongClickListener(new OnLongClickDelegate(convertView));
            holder.taskDescription = td;
            return convertView;
        }

        public void recycleView(View v) {
            ViewHolder holder = (ViewHolder) v.getTag();
            RecentsPanelView.this.updateThumbnail(holder, RecentsPanelView.this.mRecentTasksLoader.getDefaultThumbnail(), DEBUG, DEBUG);
            holder.iconView.setImageBitmap(RecentsPanelView.this.mRecentTasksLoader.getDefaultIcon());
            holder.iconView.setVisibility(CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL);
            holder.labelView.setText(null);
            holder.thumbnailView.setContentDescription(null);
            holder.thumbnailView.setTag(null);
            holder.thumbnailView.setOnLongClickListener(null);
            holder.thumbnailView.setVisibility(CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL);
            holder.taskDescription = null;
            holder.loadedThumbnailAndIcon = false;
        }
    }

    static final class ViewHolder {
        TextView descriptionView;
        ImageView iconView;
        TextView labelView;
        boolean loadedThumbnailAndIcon;
        TaskDescription taskDescription;
        View thumbnailView;
        ImageView thumbnailViewImage;
        Bitmap thumbnailViewImageBitmap;

        ViewHolder() {
        }
    }

    public RecentsPanelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecentsPanelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mRecentTasksDirty = true;
        this.mFirstScreenful = true;
        this.mContext = context;
        updateValuesFromResources();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RecentsPanelView, defStyle, 0);
        this.mRecentItemLayoutId = a.getResourceId(0, 0);
        a.recycle();
    }

    static /* synthetic */ int access$510(RecentsPanelView x0) {
        int i = x0.mNumItemsWaitingForThumbnailsAndIcons;
        x0.mNumItemsWaitingForThumbnailsAndIcons = i - 1;
        return i;
    }

    private void createCustomAnimations(LayoutTransition transitioner) {
        transitioner.setDuration(200);
        transitioner.setStartDelay(1, 0);
        transitioner.setAnimator(RecentsCallback.SWIPE_DOWN, null);
    }

    private boolean pointInside(int x, int y, View v) {
        return (x < v.getLeft() || x >= v.getRight() || y < v.getTop() || y >= v.getBottom()) ? DEBUG : true;
    }

    private void refreshRecentTasksList(ArrayList<TaskDescription> recentTasksList, boolean firstScreenful) {
        if (this.mRecentTasksDirty) {
            if (recentTasksList != null) {
                this.mFirstScreenful = true;
                onTasksLoaded(recentTasksList);
            } else {
                this.mFirstScreenful = true;
                this.mRecentTasksLoader.loadTasksInBackground();
            }
            this.mRecentTasksDirty = false;
        }
    }

    static void sendCloseSystemWindows(Context context, String reason) {
        if (ActivityManagerNative.isSystemReady()) {
            try {
                ActivityManagerNative.getDefault().closeSystemDialogs(reason);
            } catch (RemoteException e) {
            }
        }
    }

    private void showIfReady() {
        if (this.mWaitingToShow && this.mReadyToShow) {
            show(true, this.mWaitingToShowAnimated, null, DEBUG);
        }
    }

    private void startApplicationDetailsActivity(String packageName) {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", Uri.fromParts("package", packageName, null));
        intent.setFlags(268435456);
        getContext().startActivity(intent);
    }

    private void updateIcon(ViewHolder h, Drawable icon, boolean show, boolean anim) {
        if (icon != null) {
            h.iconView.setImageDrawable(icon);
            if (show && h.iconView.getVisibility() != 0) {
                if (anim) {
                    h.iconView.setAnimation(AnimationUtils.loadAnimation(this.mContext, R.anim.recent_appear));
                }
                h.iconView.setVisibility(0);
            }
        }
    }

    private void updateThumbnail(ViewHolder h, Bitmap thumbnail, boolean show, boolean anim) {
        if (thumbnail != null) {
            h.thumbnailViewImage.setImageBitmap(thumbnail);
            if (!(h.thumbnailViewImageBitmap != null && h.thumbnailViewImageBitmap.getWidth() == thumbnail.getWidth() && h.thumbnailViewImageBitmap.getHeight() == thumbnail.getHeight())) {
                if (this.mFitThumbnailToXY) {
                    h.thumbnailViewImage.setScaleType(ScaleType.FIT_XY);
                } else {
                    Matrix scaleMatrix = new Matrix();
                    float scale = ((float) this.mThumbnailWidth) / ((float) thumbnail.getWidth());
                    scaleMatrix.setScale(scale, scale);
                    h.thumbnailViewImage.setScaleType(ScaleType.MATRIX);
                    h.thumbnailViewImage.setImageMatrix(scaleMatrix);
                }
            }
            if (show && h.thumbnailView.getVisibility() != 0) {
                if (anim) {
                    h.thumbnailView.setAnimation(AnimationUtils.loadAnimation(this.mContext, R.anim.recent_appear));
                }
                h.thumbnailView.setVisibility(0);
            }
            h.thumbnailViewImageBitmap = thumbnail;
        }
    }

    private void updateUiElements(Configuration config) {
        String recentAppsAccessibilityDescription;
        this.mRecentsContainer.setVisibility(this.mRecentTaskDescriptions.size() > 0 ? 0 : CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
        int numRecentApps = this.mRecentTaskDescriptions.size();
        if (numRecentApps == 0) {
            recentAppsAccessibilityDescription = getResources().getString(R.string.status_bar_no_recent_apps);
        } else {
            recentAppsAccessibilityDescription = getResources().getQuantityString(R.plurals.status_bar_accessibility_recent_apps, numRecentApps, new Object[]{Integer.valueOf(numRecentApps)});
        }
        setContentDescription(recentAppsAccessibilityDescription);
    }

    public void clearRecentTasksList() {
        if (!this.mShowing && this.mRecentTaskDescriptions != null) {
            this.mRecentTasksLoader.cancelLoadingThumbnailsAndIcons();
            this.mRecentTaskDescriptions.clear();
            this.mListAdapter.notifyDataSetInvalidated();
            this.mRecentTasksDirty = true;
        }
    }

    public void dismiss() {
        hide(true);
    }

    public boolean dispatchHoverEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        return (x < 0 || x >= getWidth() || y < 0 || y >= getHeight()) ? true : super.dispatchHoverEvent(event);
    }

    public boolean getFirstScreenful() {
        return this.mFirstScreenful;
    }

    public ArrayList<TaskDescription> getRecentTasksList() {
        return this.mRecentTaskDescriptions;
    }

    public void handleLongPress(View selectedView, View anchorView, View thumbnailView) {
        thumbnailView.setSelected(true);
        Context context = this.mContext;
        if (anchorView == null) {
            anchorView = selectedView;
        }
        PopupMenu popup = new PopupMenu(context, anchorView);
        this.mPopup = popup;
        popup.getMenuInflater().inflate(R.menu.recent_popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new AnonymousClass_3(selectedView));
        popup.setOnDismissListener(new AnonymousClass_4(thumbnailView));
        popup.show();
    }

    public void handleOnClick(View view) {
        boolean usingDrawingCache;
        ViewHolder holder = (ViewHolder) view.getTag();
        TaskDescription ad = holder.taskDescription;
        Context context = view.getContext();
        ActivityManager am = (ActivityManager) context.getSystemService("activity");
        Bitmap bm = holder.thumbnailViewImageBitmap;
        if (bm.getWidth() == holder.thumbnailViewImage.getWidth() && bm.getHeight() == holder.thumbnailViewImage.getHeight()) {
            usingDrawingCache = DEBUG;
        } else {
            holder.thumbnailViewImage.setDrawingCacheEnabled(true);
            bm = holder.thumbnailViewImage.getDrawingCache();
            usingDrawingCache = true;
        }
        if (this.mPlaceholderThumbnail == null) {
            this.mPlaceholderThumbnail = (ImageView) findViewById(2131492940);
        }
        if (this.mTransitionBg == null) {
            this.mTransitionBg = findViewById(2131492938);
            try {
                if (!Stub.asInterface(ServiceManager.getService("window")).hasSystemNavBar()) {
                    LayoutParams lp = (LayoutParams) this.mTransitionBg.getLayoutParams();
                    lp.setMargins(0, getResources().getDimensionPixelSize(17104906), 0, 0);
                    this.mTransitionBg.setLayoutParams(lp);
                }
            } catch (RemoteException e) {
                Log.w(TAG, "Failing checking whether status bar is visible", e);
            }
        }
        ImageView placeholderThumbnail = this.mPlaceholderThumbnail;
        this.mHideRecentsAfterThumbnailScaleUpStarted = false;
        placeholderThumbnail.setVisibility(0);
        if (usingDrawingCache) {
            placeholderThumbnail.setImageBitmap(bm.copy(bm.getConfig(), true));
        } else {
            placeholderThumbnail.setImageBitmap(bm);
        }
        Rect r = new Rect();
        holder.thumbnailViewImage.getGlobalVisibleRect(r);
        placeholderThumbnail.setTranslationX((float) r.left);
        placeholderThumbnail.setTranslationY((float) r.top);
        show(false, true);
        this.mThumbnailScaleUpStarted = false;
        ActivityOptions opts = ActivityOptions.makeDelayedThumbnailScaleUpAnimation(holder.thumbnailViewImage, bm, 0, 0, new OnAnimationStartedListener() {
            public void onAnimationStarted() {
                RecentsPanelView.this.mThumbnailScaleUpStarted = true;
                if (!RecentsPanelView.this.mHighEndGfx) {
                    RecentsPanelView.this.mPlaceholderThumbnail.setVisibility(CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL);
                }
                if (RecentsPanelView.this.mHideRecentsAfterThumbnailScaleUpStarted) {
                    RecentsPanelView.this.hideWindow();
                }
            }
        });
        if (ad.taskId >= 0) {
            am.moveTaskToFront(ad.taskId, 1, opts.toBundle());
        } else {
            Intent intent = ad.intent;
            intent.addFlags(269500416);
            context.startActivity(intent, opts.toBundle());
        }
        if (usingDrawingCache) {
            holder.thumbnailViewImage.setDrawingCacheEnabled(DEBUG);
        }
    }

    public void handleSwipe(View view) {
        TaskDescription ad = ((ViewHolder) view.getTag()).taskDescription;
        if (ad == null) {
            Log.v(TAG, "Not able to find activity description for swiped task; view=" + view + " tag=" + view.getTag());
        } else {
            this.mRecentTaskDescriptions.remove(ad);
            if (this.mRecentTaskDescriptions.size() == 0) {
                hide(DEBUG);
            }
            ActivityManager am = (ActivityManager) this.mContext.getSystemService("activity");
            if (am != null) {
                am.removeTask(ad.persistentTaskId, 1);
                setContentDescription(this.mContext.getString(R.string.accessibility_recents_item_dismissed, new Object[]{ad.getLabel()}));
                sendAccessibilityEvent(CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL);
                setContentDescription(null);
            }
        }
    }

    public void hide(boolean animate) {
        if (!animate) {
            setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
        }
        if (this.mBar != null) {
            this.mBar.animateCollapse(0);
        }
    }

    public void hideWindow() {
        if (this.mThumbnailScaleUpStarted) {
            setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
            this.mTransitionBg.setVisibility(CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL);
            this.mPlaceholderThumbnail.setVisibility(CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL);
            this.mHideRecentsAfterThumbnailScaleUpStarted = false;
        } else {
            this.mHideRecentsAfterThumbnailScaleUpStarted = true;
        }
    }

    public boolean isInContentArea(int x, int y) {
        if (pointInside(x, y, this.mRecentsContainer)) {
            return true;
        }
        return (this.mStatusBarTouchProxy == null || !pointInside(x, y, this.mStatusBarTouchProxy)) ? DEBUG : true;
    }

    public boolean isShowing() {
        return this.mShowing;
    }

    public int numItemsInOneScreenful() {
        if (this.mRecentsContainer instanceof RecentsScrollView) {
            return ((RecentsScrollView) this.mRecentsContainer).numItemsInOneScreenful();
        }
        throw new IllegalArgumentException("missing Recents[Horizontal]ScrollView");
    }

    public void onAnimationCancel(Animator animation) {
    }

    public void onAnimationEnd(Animator animation) {
        if (this.mShowing) {
            LayoutTransition transitioner = new LayoutTransition();
            this.mRecentsContainer.setLayoutTransition(transitioner);
            createCustomAnimations(transitioner);
        } else {
            this.mRecentsContainer.setLayoutTransition(null);
            clearRecentTasksList();
        }
    }

    public void onAnimationRepeat(Animator animation) {
    }

    public void onAnimationStart(Animator animation) {
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mContext.getSystemService("layout_inflater");
        this.mRecentsContainer = (ViewGroup) findViewById(R.id.recents_container);
        this.mStatusBarTouchProxy = (StatusBarTouchProxy) findViewById(R.id.status_bar_touch_proxy);
        this.mListAdapter = new TaskDescriptionAdapter(this.mContext);
        if (this.mRecentsContainer instanceof RecentsScrollView) {
            RecentsScrollView scrollView = (RecentsScrollView) this.mRecentsContainer;
            scrollView.setAdapter(this.mListAdapter);
            scrollView.setCallback(this);
            this.mRecentsScrim = findViewById(R.id.recents_bg_protect);
            this.mRecentsNoApps = findViewById(R.id.recents_no_apps);
            this.mChoreo = new Choreographer(this, this.mRecentsScrim, this.mRecentsContainer, this.mRecentsNoApps, this);
            if (this.mRecentsScrim != null) {
                this.mHighEndGfx = ActivityManager.isHighEndGfx(((WindowManager) this.mContext.getSystemService("window")).getDefaultDisplay());
                if (!this.mHighEndGfx) {
                    this.mRecentsScrim.setBackground(null);
                } else if (this.mRecentsScrim.getBackground() instanceof BitmapDrawable) {
                    ((BitmapDrawable) this.mRecentsScrim.getBackground()).setTileModeY(TileMode.REPEAT);
                }
            }
            this.mPreloadTasksRunnable = new Runnable() {
                public void run() {
                    if (!RecentsPanelView.this.mShowing) {
                        RecentsPanelView.this.setVisibility(CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL);
                        RecentsPanelView.this.refreshRecentTasksList();
                    }
                }
            };
        } else {
            throw new IllegalArgumentException("missing Recents[Horizontal]ScrollView");
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return this.mPopup != null ? true : super.onInterceptTouchEvent(ev);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        handleOnClick(view);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode != 4 || event.isCanceled()) {
            return super.onKeyUp(keyCode, event);
        }
        show(DEBUG, DEBUG);
        return true;
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        this.mChoreo.setPanelHeight(this.mRecentsContainer.getHeight());
    }

    void onTaskThumbnailLoaded(TaskDescription td) {
        synchronized (td) {
            if (this.mRecentsContainer != null) {
                ViewGroup container = this.mRecentsContainer;
                if (container instanceof RecentsScrollView) {
                    container = container.findViewById(R.id.recents_linear_layout);
                }
                int i = 0;
                while (i < container.getChildCount()) {
                    View v = container.getChildAt(i);
                    if (v.getTag() instanceof ViewHolder) {
                        ViewHolder h = (ViewHolder) v.getTag();
                        if (!h.loadedThumbnailAndIcon && h.taskDescription == td) {
                            updateIcon(h, td.getIcon(), true, DEBUG);
                            updateThumbnail(h, td.getThumbnail(), true, DEBUG);
                            h.loadedThumbnailAndIcon = true;
                            this.mNumItemsWaitingForThumbnailsAndIcons--;
                        }
                    }
                    i++;
                }
            }
        }
        showIfReady();
    }

    public void onTasksLoaded(ArrayList<TaskDescription> tasks) {
        if (this.mFirstScreenful || tasks.size() != 0) {
            int size = this.mFirstScreenful ? tasks.size() : this.mRecentTaskDescriptions == null ? 0 : this.mRecentTaskDescriptions.size();
            this.mNumItemsWaitingForThumbnailsAndIcons = size;
            if (this.mRecentTaskDescriptions == null) {
                this.mRecentTaskDescriptions = new ArrayList(tasks);
            } else {
                this.mRecentTaskDescriptions.addAll(tasks);
            }
            this.mListAdapter.notifyDataSetInvalidated();
            updateUiElements(getResources().getConfiguration());
            this.mReadyToShow = true;
            this.mFirstScreenful = false;
            showIfReady();
        }
    }

    public boolean onTouch(View v, MotionEvent ev) {
        if (!this.mShowing) {
            int action = ev.getAction() & 255;
            if (action == 0) {
                post(this.mPreloadTasksRunnable);
            } else if (action == 3) {
                setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
                clearRecentTasksList();
                removeCallbacks(this.mPreloadTasksRunnable);
            } else if (action == 1) {
                removeCallbacks(this.mPreloadTasksRunnable);
                if (!v.isPressed()) {
                    setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
                    clearRecentTasksList();
                }
            }
        }
        return DEBUG;
    }

    public void preloadRecentTasksList() {
        if (!this.mShowing) {
            this.mPreloadTasksRunnable.run();
        }
    }

    public void refreshRecentTasksList() {
        refreshRecentTasksList(null, DEBUG);
    }

    public void setBar(BaseStatusBar bar) {
        this.mBar = bar;
    }

    public void setMinSwipeAlpha(float minAlpha) {
        if (this.mRecentsContainer instanceof RecentsScrollView) {
            ((RecentsScrollView) this.mRecentsContainer).setMinSwipeAlpha(minAlpha);
        }
    }

    public void setOnVisibilityChangedListener(OnRecentsPanelVisibilityChangedListener l) {
        this.mVisibilityChangedListener = l;
    }

    public void setRecentTasksLoader(RecentTasksLoader loader) {
        this.mRecentTasksLoader = loader;
    }

    public void setStatusBarView(View statusBarView) {
        if (this.mStatusBarTouchProxy != null) {
            this.mStatusBarTouchProxy.setStatusBar(statusBarView);
        }
    }

    public void setVisibility(int visibility) {
        if (this.mVisibilityChangedListener != null) {
            this.mVisibilityChangedListener.onRecentsPanelVisibilityChanged(visibility == 0 ? true : DEBUG);
        }
        super.setVisibility(visibility);
    }

    public void show(boolean show, boolean animate) {
        if (show) {
            refreshRecentTasksList(null, true);
            this.mWaitingToShow = true;
            this.mWaitingToShowAnimated = animate;
            showIfReady();
        } else {
            show(show, animate, null, DEBUG);
        }
    }

    public void show(boolean show, boolean animate, ArrayList<TaskDescription> recentTaskDescriptions, boolean firstScreenful) {
        int i = 0;
        sendCloseSystemWindows(this.mContext, BaseStatusBar.SYSTEM_DIALOG_REASON_RECENT_APPS);
        if (show) {
            refreshRecentTasksList(recentTaskDescriptions, firstScreenful);
            boolean noApps = !this.mFirstScreenful && this.mRecentTaskDescriptions.size() == 0;
            if (this.mRecentsNoApps != null) {
                this.mRecentsNoApps.setAlpha(1.0f);
                this.mRecentsNoApps.setVisibility(noApps ? 0 : CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL);
            } else if (noApps) {
                this.mRecentTasksLoader.cancelLoadingThumbnailsAndIcons();
                this.mRecentTasksDirty = true;
                this.mWaitingToShow = false;
                this.mReadyToShow = false;
                return;
            }
        } else {
            this.mRecentTasksLoader.cancelLoadingThumbnailsAndIcons();
            this.mRecentTasksDirty = true;
            this.mWaitingToShow = false;
            this.mReadyToShow = false;
        }
        if (!animate) {
            this.mShowing = show;
            if (!show) {
                i = CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL;
            }
            setVisibility(i);
            this.mChoreo.jumpTo(show);
            onAnimationEnd(null);
        } else if (this.mShowing != show) {
            this.mShowing = show;
            if (show) {
                setVisibility(0);
            }
            this.mChoreo.startAnimation(show);
        }
        if (show) {
            setFocusable(true);
            setFocusableInTouchMode(true);
            requestFocus();
        } else if (this.mPopup != null) {
            this.mPopup.dismiss();
        }
    }

    public void updateValuesFromResources() {
        Resources res = this.mContext.getResources();
        this.mThumbnailWidth = Math.round(res.getDimension(R.dimen.status_bar_recents_thumbnail_width));
        this.mFitThumbnailToXY = res.getBoolean(R.bool.config_recents_thumbnail_image_fits_to_xy);
    }
}