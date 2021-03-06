package android.support.v4.app;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.support.v4.app.Fragment.SavedState;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.util.DebugUtils;
import android.support.v4.util.LogWriter;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import com.wmt.opengl.GLView;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

final class FragmentManagerImpl extends FragmentManager {
    static final Interpolator ACCELERATE_CUBIC;
    static final Interpolator ACCELERATE_QUINT;
    static final int ANIM_DUR = 220;
    public static final int ANIM_STYLE_CLOSE_ENTER = 3;
    public static final int ANIM_STYLE_CLOSE_EXIT = 4;
    public static final int ANIM_STYLE_FADE_ENTER = 5;
    public static final int ANIM_STYLE_FADE_EXIT = 6;
    public static final int ANIM_STYLE_OPEN_ENTER = 1;
    public static final int ANIM_STYLE_OPEN_EXIT = 2;
    static boolean DEBUG = false;
    static final Interpolator DECELERATE_CUBIC;
    static final Interpolator DECELERATE_QUINT;
    static final boolean HONEYCOMB;
    static final String TAG = "FragmentManager";
    static final String TARGET_REQUEST_CODE_STATE_TAG = "android:target_req_state";
    static final String TARGET_STATE_TAG = "android:target_state";
    static final String USER_VISIBLE_HINT_TAG = "android:user_visible_hint";
    static final String VIEW_STATE_TAG = "android:view_state";
    ArrayList<Fragment> mActive;
    FragmentActivity mActivity;
    ArrayList<Fragment> mAdded;
    ArrayList<Integer> mAvailBackStackIndices;
    ArrayList<Integer> mAvailIndices;
    ArrayList<BackStackRecord> mBackStack;
    ArrayList<OnBackStackChangedListener> mBackStackChangeListeners;
    ArrayList<BackStackRecord> mBackStackIndices;
    ArrayList<Fragment> mCreatedMenus;
    int mCurState;
    boolean mDestroyed;
    Runnable mExecCommit;
    boolean mExecutingActions;
    boolean mHavePendingDeferredStart;
    boolean mNeedMenuInvalidate;
    String mNoTransactionsBecause;
    ArrayList<Runnable> mPendingActions;
    SparseArray<Parcelable> mStateArray;
    Bundle mStateBundle;
    boolean mStateSaved;
    Runnable[] mTmpActions;

    class AnonymousClass_3 implements Runnable {
        final /* synthetic */ int val$flags;
        final /* synthetic */ String val$name;

        AnonymousClass_3(String str, int i) {
            this.val$name = str;
            this.val$flags = i;
        }

        public void run() {
            FragmentManagerImpl.this.popBackStackState(FragmentManagerImpl.this.mActivity.mHandler, this.val$name, -1, this.val$flags);
        }
    }

    class AnonymousClass_4 implements Runnable {
        final /* synthetic */ int val$flags;
        final /* synthetic */ int val$id;

        AnonymousClass_4(int i, int i2) {
            this.val$id = i;
            this.val$flags = i2;
        }

        public void run() {
            FragmentManagerImpl.this.popBackStackState(FragmentManagerImpl.this.mActivity.mHandler, null, this.val$id, this.val$flags);
        }
    }

    class AnonymousClass_5 implements AnimationListener {
        final /* synthetic */ Fragment val$fragment;

        AnonymousClass_5(Fragment fragment) {
            this.val$fragment = fragment;
        }

        public void onAnimationEnd(Animation animation) {
            if (this.val$fragment.mAnimatingAway != null) {
                this.val$fragment.mAnimatingAway = null;
                FragmentManagerImpl.this.moveToState(this.val$fragment, this.val$fragment.mStateAfterAnimating, 0, 0, false);
            }
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
        }
    }

    static {
        boolean z = HONEYCOMB;
        DEBUG = false;
        if (VERSION.SDK_INT >= 11) {
            z = true;
        }
        HONEYCOMB = z;
        DECELERATE_QUINT = new DecelerateInterpolator(2.5f);
        DECELERATE_CUBIC = new DecelerateInterpolator(1.5f);
        ACCELERATE_QUINT = new AccelerateInterpolator(2.5f);
        ACCELERATE_CUBIC = new AccelerateInterpolator(1.5f);
    }

    FragmentManagerImpl() {
        this.mCurState = 0;
        this.mStateBundle = null;
        this.mStateArray = null;
        this.mExecCommit = new Runnable() {
            public void run() {
                FragmentManagerImpl.this.execPendingActions();
            }
        };
    }

    private void checkStateLoss() {
        if (this.mStateSaved) {
            throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
        } else if (this.mNoTransactionsBecause != null) {
            throw new IllegalStateException("Can not perform this action inside of " + this.mNoTransactionsBecause);
        }
    }

    static Animation makeFadeAnimation(Context context, float start, float end) {
        AlphaAnimation anim = new AlphaAnimation(start, end);
        anim.setInterpolator(DECELERATE_CUBIC);
        anim.setDuration(220);
        return anim;
    }

    static Animation makeOpenCloseAnimation(Context context, float startScale, float endScale, float startAlpha, float endAlpha) {
        AnimationSet set = new AnimationSet(false);
        ScaleAnimation scale = new ScaleAnimation(startScale, endScale, startScale, endScale, 1, 0.5f, 1, 0.5f);
        scale.setInterpolator(DECELERATE_QUINT);
        scale.setDuration(220);
        set.addAnimation(scale);
        AlphaAnimation alpha = new AlphaAnimation(startAlpha, endAlpha);
        alpha.setInterpolator(DECELERATE_CUBIC);
        alpha.setDuration(220);
        set.addAnimation(alpha);
        return set;
    }

    public static int reverseTransit(int transit) {
        switch (transit) {
            case FragmentTransaction.TRANSIT_FRAGMENT_OPEN:
                return FragmentTransaction.TRANSIT_FRAGMENT_CLOSE;
            case FragmentTransaction.TRANSIT_FRAGMENT_FADE:
                return FragmentTransaction.TRANSIT_FRAGMENT_FADE;
            case FragmentTransaction.TRANSIT_FRAGMENT_CLOSE:
                return FragmentTransaction.TRANSIT_FRAGMENT_OPEN;
            default:
                return 0;
        }
    }

    public static int transitToStyleIndex(int transit, boolean enter) {
        int animAttr = -1;
        switch (transit) {
            case FragmentTransaction.TRANSIT_FRAGMENT_OPEN:
                animAttr = enter ? ANIM_STYLE_OPEN_ENTER : ANIM_STYLE_OPEN_EXIT;
                break;
            case FragmentTransaction.TRANSIT_FRAGMENT_FADE:
                animAttr = enter ? ANIM_STYLE_FADE_ENTER : ANIM_STYLE_FADE_EXIT;
                break;
            case FragmentTransaction.TRANSIT_FRAGMENT_CLOSE:
                animAttr = enter ? ANIM_STYLE_CLOSE_ENTER : ANIM_STYLE_CLOSE_EXIT;
                break;
        }
        return animAttr;
    }

    void addBackStackState(BackStackRecord state) {
        if (this.mBackStack == null) {
            this.mBackStack = new ArrayList();
        }
        this.mBackStack.add(state);
        reportBackStackChanged();
    }

    public void addFragment(Fragment fragment, boolean moveToStateNow) {
        if (this.mAdded == null) {
            this.mAdded = new ArrayList();
        }
        if (DEBUG) {
            Log.v(TAG, "add: " + fragment);
        }
        makeActive(fragment);
        if (!fragment.mDetached) {
            this.mAdded.add(fragment);
            fragment.mAdded = true;
            fragment.mRemoving = false;
            if (fragment.mHasMenu && fragment.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
            if (moveToStateNow) {
                moveToState(fragment);
            }
        }
    }

    public void addOnBackStackChangedListener(OnBackStackChangedListener listener) {
        if (this.mBackStackChangeListeners == null) {
            this.mBackStackChangeListeners = new ArrayList();
        }
        this.mBackStackChangeListeners.add(listener);
    }

    public int allocBackStackIndex(BackStackRecord bse) {
        synchronized (this) {
            int index;
            if (this.mAvailBackStackIndices == null || this.mAvailBackStackIndices.size() <= 0) {
                if (this.mBackStackIndices == null) {
                    this.mBackStackIndices = new ArrayList();
                }
                index = this.mBackStackIndices.size();
                if (DEBUG) {
                    Log.v(TAG, "Setting back stack index " + index + " to " + bse);
                }
                this.mBackStackIndices.add(bse);
                return index;
            } else {
                index = ((Integer) this.mAvailBackStackIndices.remove(this.mAvailBackStackIndices.size() - 1)).intValue();
                if (DEBUG) {
                    Log.v(TAG, "Adding back stack index " + index + " with " + bse);
                }
                this.mBackStackIndices.set(index, bse);
                return index;
            }
        }
    }

    public void attachActivity(FragmentActivity activity) {
        if (this.mActivity != null) {
            throw new IllegalStateException();
        }
        this.mActivity = activity;
    }

    public void attachFragment(Fragment fragment, int transition, int transitionStyle) {
        if (DEBUG) {
            Log.v(TAG, "attach: " + fragment);
        }
        if (fragment.mDetached) {
            fragment.mDetached = false;
            if (!fragment.mAdded) {
                if (this.mAdded == null) {
                    this.mAdded = new ArrayList();
                }
                this.mAdded.add(fragment);
                fragment.mAdded = true;
                if (fragment.mHasMenu && fragment.mMenuVisible) {
                    this.mNeedMenuInvalidate = true;
                }
                moveToState(fragment, this.mCurState, transition, transitionStyle, HONEYCOMB);
            }
        }
    }

    public FragmentTransaction beginTransaction() {
        return new BackStackRecord(this);
    }

    public void detachFragment(Fragment fragment, int transition, int transitionStyle) {
        if (DEBUG) {
            Log.v(TAG, "detach: " + fragment);
        }
        if (!fragment.mDetached) {
            fragment.mDetached = true;
            if (fragment.mAdded) {
                if (this.mAdded != null) {
                    this.mAdded.remove(fragment);
                }
                if (fragment.mHasMenu && fragment.mMenuVisible) {
                    this.mNeedMenuInvalidate = true;
                }
                fragment.mAdded = false;
                moveToState(fragment, ANIM_STYLE_OPEN_ENTER, transition, transitionStyle, HONEYCOMB);
            }
        }
    }

    public void dispatchActivityCreated() {
        this.mStateSaved = false;
        moveToState(ANIM_STYLE_OPEN_EXIT, HONEYCOMB);
    }

    public void dispatchConfigurationChanged(Configuration newConfig) {
        if (this.mAdded != null) {
            int i = 0;
            while (i < this.mAdded.size()) {
                Fragment f = (Fragment) this.mAdded.get(i);
                if (f != null) {
                    f.onConfigurationChanged(newConfig);
                }
                i++;
            }
        }
    }

    public boolean dispatchContextItemSelected(MenuItem item) {
        if (this.mAdded != null) {
            int i = 0;
            while (i < this.mAdded.size()) {
                Fragment f = (Fragment) this.mAdded.get(i);
                if (f != null && !f.mHidden && f.mUserVisibleHint && f.onContextItemSelected(item)) {
                    return true;
                }
                i++;
            }
        }
        return HONEYCOMB;
    }

    public void dispatchCreate() {
        this.mStateSaved = false;
        moveToState(ANIM_STYLE_OPEN_ENTER, HONEYCOMB);
    }

    public boolean dispatchCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        int i;
        Fragment f;
        boolean show = HONEYCOMB;
        ArrayList<Fragment> newMenus = null;
        if (this.mAdded != null) {
            i = 0;
            while (i < this.mAdded.size()) {
                f = (Fragment) this.mAdded.get(i);
                if (f != null && !f.mHidden && f.mHasMenu && f.mMenuVisible) {
                    show = true;
                    f.onCreateOptionsMenu(menu, inflater);
                    if (newMenus == null) {
                        newMenus = new ArrayList();
                    }
                    newMenus.add(f);
                }
                i++;
            }
        }
        if (this.mCreatedMenus != null) {
            i = 0;
            while (i < this.mCreatedMenus.size()) {
                f = this.mCreatedMenus.get(i);
                if (newMenus == null || !newMenus.contains(f)) {
                    f.onDestroyOptionsMenu();
                }
                i++;
            }
        }
        this.mCreatedMenus = newMenus;
        return show;
    }

    public void dispatchDestroy() {
        this.mDestroyed = true;
        execPendingActions();
        moveToState(0, HONEYCOMB);
        this.mActivity = null;
    }

    public void dispatchLowMemory() {
        if (this.mAdded != null) {
            int i = 0;
            while (i < this.mAdded.size()) {
                Fragment f = (Fragment) this.mAdded.get(i);
                if (f != null) {
                    f.onLowMemory();
                }
                i++;
            }
        }
    }

    public boolean dispatchOptionsItemSelected(MenuItem item) {
        if (this.mAdded != null) {
            int i = 0;
            while (i < this.mAdded.size()) {
                Fragment f = (Fragment) this.mAdded.get(i);
                if (f != null && !f.mHidden && f.mHasMenu && f.mMenuVisible && f.onOptionsItemSelected(item)) {
                    return true;
                }
                i++;
            }
        }
        return HONEYCOMB;
    }

    public void dispatchOptionsMenuClosed(Menu menu) {
        if (this.mAdded != null) {
            int i = 0;
            while (i < this.mAdded.size()) {
                Fragment f = (Fragment) this.mAdded.get(i);
                if (f != null && !f.mHidden && f.mHasMenu && f.mMenuVisible) {
                    f.onOptionsMenuClosed(menu);
                }
                i++;
            }
        }
    }

    public void dispatchPause() {
        moveToState(ANIM_STYLE_CLOSE_EXIT, HONEYCOMB);
    }

    public boolean dispatchPrepareOptionsMenu(Menu menu) {
        boolean show = HONEYCOMB;
        if (this.mAdded != null) {
            int i = 0;
            while (i < this.mAdded.size()) {
                Fragment f = (Fragment) this.mAdded.get(i);
                if (f != null && !f.mHidden && f.mHasMenu && f.mMenuVisible) {
                    show = true;
                    f.onPrepareOptionsMenu(menu);
                }
                i++;
            }
        }
        return show;
    }

    public void dispatchReallyStop() {
        moveToState(ANIM_STYLE_OPEN_EXIT, HONEYCOMB);
    }

    public void dispatchResume() {
        this.mStateSaved = false;
        moveToState(ANIM_STYLE_FADE_ENTER, HONEYCOMB);
    }

    public void dispatchStart() {
        this.mStateSaved = false;
        moveToState(ANIM_STYLE_CLOSE_EXIT, HONEYCOMB);
    }

    public void dispatchStop() {
        this.mStateSaved = true;
        moveToState(ANIM_STYLE_CLOSE_ENTER, HONEYCOMB);
    }

    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        int N;
        int i;
        Fragment f;
        String innerPrefix = prefix + "    ";
        if (this.mActive != null) {
            N = this.mActive.size();
            if (N > 0) {
                writer.print(prefix);
                writer.print("Active Fragments in ");
                writer.print(Integer.toHexString(System.identityHashCode(this)));
                writer.println(":");
                i = 0;
                while (i < N) {
                    f = (Fragment) this.mActive.get(i);
                    writer.print(prefix);
                    writer.print("  #");
                    writer.print(i);
                    writer.print(": ");
                    writer.println(f);
                    if (f != null) {
                        f.dump(innerPrefix, fd, writer, args);
                    }
                    i++;
                }
            }
        }
        if (this.mAdded != null) {
            N = this.mAdded.size();
            if (N > 0) {
                writer.print(prefix);
                writer.println("Added Fragments:");
                i = 0;
                while (i < N) {
                    f = this.mAdded.get(i);
                    writer.print(prefix);
                    writer.print("  #");
                    writer.print(i);
                    writer.print(": ");
                    writer.println(f.toString());
                    i++;
                }
            }
        }
        if (this.mCreatedMenus != null) {
            N = this.mCreatedMenus.size();
            if (N > 0) {
                writer.print(prefix);
                writer.println("Fragments Created Menus:");
                i = 0;
                while (i < N) {
                    f = this.mCreatedMenus.get(i);
                    writer.print(prefix);
                    writer.print("  #");
                    writer.print(i);
                    writer.print(": ");
                    writer.println(f.toString());
                    i++;
                }
            }
        }
        if (this.mBackStack != null) {
            N = this.mBackStack.size();
            if (N > 0) {
                writer.print(prefix);
                writer.println("Back Stack:");
                i = 0;
                while (i < N) {
                    BackStackRecord bs = (BackStackRecord) this.mBackStack.get(i);
                    writer.print(prefix);
                    writer.print("  #");
                    writer.print(i);
                    writer.print(": ");
                    writer.println(bs.toString());
                    bs.dump(innerPrefix, fd, writer, args);
                    i++;
                }
            }
        }
        synchronized (this) {
            if (this.mBackStackIndices != null) {
                N = this.mBackStackIndices.size();
                if (N > 0) {
                    writer.print(prefix);
                    writer.println("Back Stack Indices:");
                    i = 0;
                    while (i < N) {
                        bs = this.mBackStackIndices.get(i);
                        writer.print(prefix);
                        writer.print("  #");
                        writer.print(i);
                        writer.print(": ");
                        writer.println(bs);
                        i++;
                    }
                }
            }
            if (this.mAvailBackStackIndices != null && this.mAvailBackStackIndices.size() > 0) {
                writer.print(prefix);
                writer.print("mAvailBackStackIndices: ");
                writer.println(Arrays.toString(this.mAvailBackStackIndices.toArray()));
            }
        }
        if (this.mPendingActions != null) {
            N = this.mPendingActions.size();
            if (N > 0) {
                writer.print(prefix);
                writer.println("Pending Actions:");
                i = 0;
                while (i < N) {
                    Runnable r = (Runnable) this.mPendingActions.get(i);
                    writer.print(prefix);
                    writer.print("  #");
                    writer.print(i);
                    writer.print(": ");
                    writer.println(r);
                    i++;
                }
            }
        }
        writer.print(prefix);
        writer.println("FragmentManager misc state:");
        writer.print(prefix);
        writer.print("  mCurState=");
        writer.print(this.mCurState);
        writer.print(" mStateSaved=");
        writer.print(this.mStateSaved);
        writer.print(" mDestroyed=");
        writer.println(this.mDestroyed);
        if (this.mNeedMenuInvalidate) {
            writer.print(prefix);
            writer.print("  mNeedMenuInvalidate=");
            writer.println(this.mNeedMenuInvalidate);
        }
        if (this.mNoTransactionsBecause != null) {
            writer.print(prefix);
            writer.print("  mNoTransactionsBecause=");
            writer.println(this.mNoTransactionsBecause);
        }
        if (this.mAvailIndices != null && this.mAvailIndices.size() > 0) {
            writer.print(prefix);
            writer.print("  mAvailIndices: ");
            writer.println(Arrays.toString(this.mAvailIndices.toArray()));
        }
    }

    public void enqueueAction(Runnable action, boolean allowStateLoss) {
        if (!allowStateLoss) {
            checkStateLoss();
        }
        synchronized (this) {
            if (this.mActivity == null) {
                throw new IllegalStateException("Activity has been destroyed");
            }
            if (this.mPendingActions == null) {
                this.mPendingActions = new ArrayList();
            }
            this.mPendingActions.add(action);
            if (this.mPendingActions.size() == 1) {
                this.mActivity.mHandler.removeCallbacks(this.mExecCommit);
                this.mActivity.mHandler.post(this.mExecCommit);
            }
        }
    }

    public boolean execPendingActions() {
        if (this.mExecutingActions) {
            throw new IllegalStateException("Recursive entry to executePendingTransactions");
        } else if (Looper.myLooper() != this.mActivity.mHandler.getLooper()) {
            throw new IllegalStateException("Must be called from main thread of process");
        } else {
            boolean didSomething = HONEYCOMB;
            while (true) {
                synchronized (this) {
                    int i;
                    if (this.mPendingActions != null && this.mPendingActions.size() != 0) {
                        int numActions = this.mPendingActions.size();
                        if (this.mTmpActions == null || this.mTmpActions.length < numActions) {
                            this.mTmpActions = new Runnable[numActions];
                        }
                        this.mPendingActions.toArray(this.mTmpActions);
                        this.mPendingActions.clear();
                        this.mActivity.mHandler.removeCallbacks(this.mExecCommit);
                        this.mExecutingActions = true;
                        i = 0;
                        while (i < numActions) {
                            this.mTmpActions[i].run();
                            this.mTmpActions[i] = null;
                            i++;
                        }
                        this.mExecutingActions = false;
                        didSomething = true;
                    }
                    if (this.mHavePendingDeferredStart) {
                        boolean loadersRunning = HONEYCOMB;
                        i = 0;
                        while (i < this.mActive.size()) {
                            Fragment f = (Fragment) this.mActive.get(i);
                            if (!(f == null || f.mLoaderManager == null)) {
                                loadersRunning |= f.mLoaderManager.hasRunningLoaders();
                            }
                            i++;
                        }
                        if (!loadersRunning) {
                            this.mHavePendingDeferredStart = false;
                            startPendingDeferredFragments();
                        }
                    }
                    return didSomething;
                }
            }
        }
    }

    public boolean executePendingTransactions() {
        return execPendingActions();
    }

    public Fragment findFragmentById(int id) {
        int i;
        Fragment f;
        if (this.mAdded != null) {
            i = this.mAdded.size() - 1;
            while (i >= 0) {
                f = (Fragment) this.mAdded.get(i);
                if (f != null && f.mFragmentId == id) {
                    return f;
                }
                i--;
            }
        }
        if (this.mActive != null) {
            i = this.mActive.size() - 1;
            while (i >= 0) {
                f = this.mActive.get(i);
                if (f != null && f.mFragmentId == id) {
                    return f;
                }
                i--;
            }
        }
        return null;
    }

    public Fragment findFragmentByTag(String tag) {
        int i;
        Fragment f;
        if (!(this.mAdded == null || tag == null)) {
            i = this.mAdded.size() - 1;
            while (i >= 0) {
                f = (Fragment) this.mAdded.get(i);
                if (f != null && tag.equals(f.mTag)) {
                    return f;
                }
                i--;
            }
        }
        if (!(this.mActive == null || tag == null)) {
            i = this.mActive.size() - 1;
            while (i >= 0) {
                f = this.mActive.get(i);
                if (f != null && tag.equals(f.mTag)) {
                    return f;
                }
                i--;
            }
        }
        return null;
    }

    public Fragment findFragmentByWho(String who) {
        if (!(this.mActive == null || who == null)) {
            int i = this.mActive.size() - 1;
            while (i >= 0) {
                Fragment f = (Fragment) this.mActive.get(i);
                if (f != null && who.equals(f.mWho)) {
                    return f;
                }
                i--;
            }
        }
        return null;
    }

    public void freeBackStackIndex(int index) {
        synchronized (this) {
            this.mBackStackIndices.set(index, null);
            if (this.mAvailBackStackIndices == null) {
                this.mAvailBackStackIndices = new ArrayList();
            }
            if (DEBUG) {
                Log.v(TAG, "Freeing back stack index " + index);
            }
            this.mAvailBackStackIndices.add(Integer.valueOf(index));
        }
    }

    public BackStackEntry getBackStackEntryAt(int index) {
        return (BackStackEntry) this.mBackStack.get(index);
    }

    public int getBackStackEntryCount() {
        return this.mBackStack != null ? this.mBackStack.size() : 0;
    }

    public Fragment getFragment(Bundle bundle, String key) {
        int index = bundle.getInt(key, -1);
        if (index == -1) {
            return null;
        }
        if (index >= this.mActive.size()) {
            throw new IllegalStateException("Fragement no longer exists for key " + key + ": index " + index);
        }
        Fragment f = (Fragment) this.mActive.get(index);
        if (f != null) {
            return f;
        }
        throw new IllegalStateException("Fragement no longer exists for key " + key + ": index " + index);
    }

    public void hideFragment(Fragment fragment, int transition, int transitionStyle) {
        if (DEBUG) {
            Log.v(TAG, "hide: " + fragment);
        }
        if (!fragment.mHidden) {
            fragment.mHidden = true;
            if (fragment.mView != null) {
                Animation anim = loadAnimation(fragment, transition, true, transitionStyle);
                if (anim != null) {
                    fragment.mView.startAnimation(anim);
                }
                fragment.mView.setVisibility(GLView.FLAG_POPUP);
            }
            if (fragment.mAdded && fragment.mHasMenu && fragment.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
            fragment.onHiddenChanged(true);
        }
    }

    Animation loadAnimation(Fragment fragment, int transit, boolean enter, int transitionStyle) {
        Animation animObj = fragment.onCreateAnimation(transit, enter, fragment.mNextAnim);
        if (animObj != null) {
            return animObj;
        }
        if (fragment.mNextAnim != 0) {
            Animation anim = AnimationUtils.loadAnimation(this.mActivity, fragment.mNextAnim);
            if (anim != null) {
                return anim;
            }
        }
        if (transit == 0) {
            return null;
        }
        int styleIndex = transitToStyleIndex(transit, enter);
        if (styleIndex < 0) {
            return null;
        }
        switch (styleIndex) {
            case ANIM_STYLE_OPEN_ENTER:
                return makeOpenCloseAnimation(this.mActivity, 1.125f, 1.0f, 0.0f, 1.0f);
            case ANIM_STYLE_OPEN_EXIT:
                return makeOpenCloseAnimation(this.mActivity, 1.0f, 0.975f, 1.0f, 0.0f);
            case ANIM_STYLE_CLOSE_ENTER:
                return makeOpenCloseAnimation(this.mActivity, 0.975f, 1.0f, 0.0f, 1.0f);
            case ANIM_STYLE_CLOSE_EXIT:
                return makeOpenCloseAnimation(this.mActivity, 1.0f, 1.075f, 1.0f, 0.0f);
            case ANIM_STYLE_FADE_ENTER:
                return makeFadeAnimation(this.mActivity, 0.0f, 1.0f);
            case ANIM_STYLE_FADE_EXIT:
                return makeFadeAnimation(this.mActivity, 1.0f, 0.0f);
            default:
                if (transitionStyle == 0 && this.mActivity.getWindow() != null) {
                    transitionStyle = this.mActivity.getWindow().getAttributes().windowAnimations;
                }
                return transitionStyle == 0 ? null : null;
        }
    }

    void makeActive(Fragment f) {
        if (f.mIndex < 0) {
            if (this.mAvailIndices == null || this.mAvailIndices.size() <= 0) {
                if (this.mActive == null) {
                    this.mActive = new ArrayList();
                }
                f.setIndex(this.mActive.size());
                this.mActive.add(f);
            } else {
                f.setIndex(((Integer) this.mAvailIndices.remove(this.mAvailIndices.size() - 1)).intValue());
                this.mActive.set(f.mIndex, f);
            }
            if (DEBUG) {
                Log.v(TAG, "Allocated fragment index " + f);
            }
        }
    }

    void makeInactive(Fragment f) {
        if (f.mIndex >= 0) {
            if (DEBUG) {
                Log.v(TAG, "Freeing fragment index " + f);
            }
            this.mActive.set(f.mIndex, null);
            if (this.mAvailIndices == null) {
                this.mAvailIndices = new ArrayList();
            }
            this.mAvailIndices.add(Integer.valueOf(f.mIndex));
            this.mActivity.invalidateSupportFragmentIndex(f.mIndex);
            f.initState();
        }
    }

    void moveToState(int newState, int transit, int transitStyle, boolean always) {
        if (this.mActivity == null && newState != 0) {
            throw new IllegalStateException("No activity");
        } else if (always || this.mCurState != newState) {
            this.mCurState = newState;
            if (this.mActive != null) {
                boolean loadersRunning = HONEYCOMB;
                int i = 0;
                while (i < this.mActive.size()) {
                    Fragment f = (Fragment) this.mActive.get(i);
                    if (f != null) {
                        moveToState(f, newState, transit, transitStyle, HONEYCOMB);
                        if (f.mLoaderManager != null) {
                            loadersRunning |= f.mLoaderManager.hasRunningLoaders();
                        }
                    }
                    i++;
                }
                if (!loadersRunning) {
                    startPendingDeferredFragments();
                }
                if (this.mNeedMenuInvalidate && this.mActivity != null && this.mCurState == 5) {
                    this.mActivity.supportInvalidateOptionsMenu();
                    this.mNeedMenuInvalidate = false;
                }
            }
        }
    }

    void moveToState(int newState, boolean always) {
        moveToState(newState, 0, 0, always);
    }

    void moveToState(Fragment f) {
        moveToState(f, this.mCurState, 0, 0, false);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    void moveToState(android.support.v4.app.Fragment r11_f, int r12_newState, int r13_transit, int r14_transitionStyle, boolean r15_keepActive) {
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.app.FragmentManagerImpl.moveToState(android.support.v4.app.Fragment, int, int, int, boolean):void");
        /*
        r10 = this;
        r0 = r11.mAdded;
        if (r0 != 0) goto L_0x0008;
    L_0x0004:
        r0 = 1;
        if (r12 <= r0) goto L_0x0008;
    L_0x0007:
        r12 = 1;
    L_0x0008:
        r0 = r11.mRemoving;
        if (r0 == 0) goto L_0x0012;
    L_0x000c:
        r0 = r11.mState;
        if (r12 <= r0) goto L_0x0012;
    L_0x0010:
        r12 = r11.mState;
    L_0x0012:
        r0 = r11.mDeferStart;
        if (r0 == 0) goto L_0x001f;
    L_0x0016:
        r0 = r11.mState;
        r1 = 4;
        if (r0 >= r1) goto L_0x001f;
    L_0x001b:
        r0 = 3;
        if (r12 <= r0) goto L_0x001f;
    L_0x001e:
        r12 = 3;
    L_0x001f:
        r0 = r11.mState;
        if (r0 >= r12) goto L_0x02bd;
    L_0x0023:
        r0 = r11.mFromLayout;
        if (r0 == 0) goto L_0x002c;
    L_0x0027:
        r0 = r11.mInLayout;
        if (r0 != 0) goto L_0x002c;
    L_0x002b:
        return;
    L_0x002c:
        r0 = r11.mAnimatingAway;
        if (r0 == 0) goto L_0x003d;
    L_0x0030:
        r0 = 0;
        r11.mAnimatingAway = r0;
        r2 = r11.mStateAfterAnimating;
        r3 = 0;
        r4 = 0;
        r5 = 1;
        r0 = r10;
        r1 = r11;
        r0.moveToState(r1, r2, r3, r4, r5);
    L_0x003d:
        r0 = r11.mState;
        switch(r0) {
            case 0: goto L_0x0045;
            case 1: goto L_0x013f;
            case 2: goto L_0x0222;
            case 3: goto L_0x0222;
            case 4: goto L_0x026a;
            default: goto L_0x0042;
        };
    L_0x0042:
        r11.mState = r12;
        goto L_0x002b;
    L_0x0045:
        r0 = DEBUG;
        if (r0 == 0) goto L_0x0061;
    L_0x0049:
        r0 = "FragmentManager";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "moveto CREATED: ";
        r1 = r1.append(r2);
        r1 = r1.append(r11);
        r1 = r1.toString();
        android.util.Log.v(r0, r1);
    L_0x0061:
        r0 = r11.mSavedFragmentState;
        if (r0 == 0) goto L_0x009e;
    L_0x0065:
        r0 = r11.mSavedFragmentState;
        r1 = "android:view_state";
        r0 = r0.getSparseParcelableArray(r1);
        r11.mSavedViewState = r0;
        r0 = r11.mSavedFragmentState;
        r1 = "android:target_state";
        r0 = r10.getFragment(r0, r1);
        r11.mTarget = r0;
        r0 = r11.mTarget;
        if (r0 == 0) goto L_0x0088;
    L_0x007d:
        r0 = r11.mSavedFragmentState;
        r1 = "android:target_req_state";
        r2 = 0;
        r0 = r0.getInt(r1, r2);
        r11.mTargetRequestCode = r0;
    L_0x0088:
        r0 = r11.mSavedFragmentState;
        r1 = "android:user_visible_hint";
        r2 = 1;
        r0 = r0.getBoolean(r1, r2);
        r11.mUserVisibleHint = r0;
        r0 = r11.mUserVisibleHint;
        if (r0 != 0) goto L_0x009e;
    L_0x0097:
        r0 = 1;
        r11.mDeferStart = r0;
        r0 = 3;
        if (r12 <= r0) goto L_0x009e;
    L_0x009d:
        r12 = 3;
    L_0x009e:
        r0 = r10.mActivity;
        r11.mActivity = r0;
        r0 = r10.mActivity;
        r0 = r0.mFragments;
        r11.mFragmentManager = r0;
        r0 = 0;
        r11.mCalled = r0;
        r0 = r10.mActivity;
        r11.onAttach(r0);
        r0 = r11.mCalled;
        if (r0 != 0) goto L_0x00d3;
    L_0x00b4:
        r0 = new android.support.v4.app.SuperNotCalledException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Fragment ";
        r1 = r1.append(r2);
        r1 = r1.append(r11);
        r2 = " did not call through to super.onAttach()";
        r1 = r1.append(r2);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
    L_0x00d3:
        r0 = r10.mActivity;
        r0.onAttachFragment(r11);
        r0 = r11.mRetaining;
        if (r0 != 0) goto L_0x0107;
    L_0x00dc:
        r0 = 0;
        r11.mCalled = r0;
        r0 = r11.mSavedFragmentState;
        r11.onCreate(r0);
        r0 = r11.mCalled;
        if (r0 != 0) goto L_0x0107;
    L_0x00e8:
        r0 = new android.support.v4.app.SuperNotCalledException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Fragment ";
        r1 = r1.append(r2);
        r1 = r1.append(r11);
        r2 = " did not call through to super.onCreate()";
        r1 = r1.append(r2);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
    L_0x0107:
        r0 = 0;
        r11.mRetaining = r0;
        r0 = r11.mFromLayout;
        if (r0 == 0) goto L_0x013f;
    L_0x010e:
        r0 = r11.mSavedFragmentState;
        r0 = r11.getLayoutInflater(r0);
        r1 = 0;
        r2 = r11.mSavedFragmentState;
        r0 = r11.onCreateView(r0, r1, r2);
        r11.mView = r0;
        r0 = r11.mView;
        if (r0 == 0) goto L_0x01a0;
    L_0x0121:
        r0 = r11.mView;
        r11.mInnerView = r0;
        r0 = r11.mView;
        r0 = android.support.v4.app.NoSaveStateFrameLayout.wrap(r0);
        r11.mView = r0;
        r0 = r11.mHidden;
        if (r0 == 0) goto L_0x0138;
    L_0x0131:
        r0 = r11.mView;
        r1 = 8;
        r0.setVisibility(r1);
    L_0x0138:
        r0 = r11.mView;
        r1 = r11.mSavedFragmentState;
        r11.onViewCreated(r0, r1);
    L_0x013f:
        r0 = 1;
        if (r12 <= r0) goto L_0x0222;
    L_0x0142:
        r0 = DEBUG;
        if (r0 == 0) goto L_0x015e;
    L_0x0146:
        r0 = "FragmentManager";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "moveto ACTIVITY_CREATED: ";
        r1 = r1.append(r2);
        r1 = r1.append(r11);
        r1 = r1.toString();
        android.util.Log.v(r0, r1);
    L_0x015e:
        r0 = r11.mFromLayout;
        if (r0 != 0) goto L_0x01e9;
    L_0x0162:
        r7 = 0;
        r0 = r11.mContainerId;
        if (r0 == 0) goto L_0x01a4;
    L_0x0167:
        r0 = r10.mActivity;
        r1 = r11.mContainerId;
        r7 = r0.findViewById(r1);
        r7 = (android.view.ViewGroup) r7;
        if (r7 != 0) goto L_0x01a4;
    L_0x0173:
        r0 = r11.mRestored;
        if (r0 != 0) goto L_0x01a4;
    L_0x0177:
        r0 = new java.lang.IllegalArgumentException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "No view found for id 0x";
        r1 = r1.append(r2);
        r2 = r11.mContainerId;
        r2 = java.lang.Integer.toHexString(r2);
        r1 = r1.append(r2);
        r2 = " for fragment ";
        r1 = r1.append(r2);
        r1 = r1.append(r11);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
    L_0x01a0:
        r0 = 0;
        r11.mInnerView = r0;
        goto L_0x013f;
    L_0x01a4:
        r11.mContainer = r7;
        r0 = r11.mSavedFragmentState;
        r0 = r11.getLayoutInflater(r0);
        r1 = r11.mSavedFragmentState;
        r0 = r11.onCreateView(r0, r7, r1);
        r11.mView = r0;
        r0 = r11.mView;
        if (r0 == 0) goto L_0x0214;
    L_0x01b8:
        r0 = r11.mView;
        r11.mInnerView = r0;
        r0 = r11.mView;
        r0 = android.support.v4.app.NoSaveStateFrameLayout.wrap(r0);
        r11.mView = r0;
        if (r7 == 0) goto L_0x01d7;
    L_0x01c6:
        r0 = 1;
        r6 = r10.loadAnimation(r11, r13, r0, r14);
        if (r6 == 0) goto L_0x01d2;
    L_0x01cd:
        r0 = r11.mView;
        r0.startAnimation(r6);
    L_0x01d2:
        r0 = r11.mView;
        r7.addView(r0);
    L_0x01d7:
        r0 = r11.mHidden;
        if (r0 == 0) goto L_0x01e2;
    L_0x01db:
        r0 = r11.mView;
        r1 = 8;
        r0.setVisibility(r1);
    L_0x01e2:
        r0 = r11.mView;
        r1 = r11.mSavedFragmentState;
        r11.onViewCreated(r0, r1);
    L_0x01e9:
        r0 = 0;
        r11.mCalled = r0;
        r0 = r11.mSavedFragmentState;
        r11.onActivityCreated(r0);
        r0 = r11.mCalled;
        if (r0 != 0) goto L_0x0218;
    L_0x01f5:
        r0 = new android.support.v4.app.SuperNotCalledException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Fragment ";
        r1 = r1.append(r2);
        r1 = r1.append(r11);
        r2 = " did not call through to super.onActivityCreated()";
        r1 = r1.append(r2);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
    L_0x0214:
        r0 = 0;
        r11.mInnerView = r0;
        goto L_0x01e9;
    L_0x0218:
        r0 = r11.mView;
        if (r0 == 0) goto L_0x021f;
    L_0x021c:
        r11.restoreViewState();
    L_0x021f:
        r0 = 0;
        r11.mSavedFragmentState = r0;
    L_0x0222:
        r0 = 3;
        if (r12 <= r0) goto L_0x026a;
    L_0x0225:
        r0 = DEBUG;
        if (r0 == 0) goto L_0x0241;
    L_0x0229:
        r0 = "FragmentManager";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "moveto STARTED: ";
        r1 = r1.append(r2);
        r1 = r1.append(r11);
        r1 = r1.toString();
        android.util.Log.v(r0, r1);
    L_0x0241:
        r0 = 0;
        r11.mCalled = r0;
        r11.performStart();
        r0 = r11.mCalled;
        if (r0 != 0) goto L_0x026a;
    L_0x024b:
        r0 = new android.support.v4.app.SuperNotCalledException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Fragment ";
        r1 = r1.append(r2);
        r1 = r1.append(r11);
        r2 = " did not call through to super.onStart()";
        r1 = r1.append(r2);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
    L_0x026a:
        r0 = 4;
        if (r12 <= r0) goto L_0x0042;
    L_0x026d:
        r0 = DEBUG;
        if (r0 == 0) goto L_0x0289;
    L_0x0271:
        r0 = "FragmentManager";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "moveto RESUMED: ";
        r1 = r1.append(r2);
        r1 = r1.append(r11);
        r1 = r1.toString();
        android.util.Log.v(r0, r1);
    L_0x0289:
        r0 = 0;
        r11.mCalled = r0;
        r0 = 1;
        r11.mResumed = r0;
        r11.onResume();
        r0 = r11.mCalled;
        if (r0 != 0) goto L_0x02b5;
    L_0x0296:
        r0 = new android.support.v4.app.SuperNotCalledException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Fragment ";
        r1 = r1.append(r2);
        r1 = r1.append(r11);
        r2 = " did not call through to super.onResume()";
        r1 = r1.append(r2);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
    L_0x02b5:
        r0 = 0;
        r11.mSavedFragmentState = r0;
        r0 = 0;
        r11.mSavedViewState = r0;
        goto L_0x0042;
    L_0x02bd:
        r0 = r11.mState;
        if (r0 <= r12) goto L_0x0042;
    L_0x02c1:
        r0 = r11.mState;
        switch(r0) {
            case 1: goto L_0x02c8;
            case 2: goto L_0x0399;
            case 3: goto L_0x0377;
            case 4: goto L_0x032f;
            case 5: goto L_0x02e4;
            default: goto L_0x02c6;
        };
    L_0x02c6:
        goto L_0x0042;
    L_0x02c8:
        r0 = 1;
        if (r12 >= r0) goto L_0x0042;
    L_0x02cb:
        r0 = r10.mDestroyed;
        if (r0 == 0) goto L_0x02db;
    L_0x02cf:
        r0 = r11.mAnimatingAway;
        if (r0 == 0) goto L_0x02db;
    L_0x02d3:
        r9 = r11.mAnimatingAway;
        r0 = 0;
        r11.mAnimatingAway = r0;
        r9.clearAnimation();
    L_0x02db:
        r0 = r11.mAnimatingAway;
        if (r0 == 0) goto L_0x0432;
    L_0x02df:
        r11.mStateAfterAnimating = r12;
        r12 = 1;
        goto L_0x0042;
    L_0x02e4:
        r0 = 5;
        if (r12 >= r0) goto L_0x032f;
    L_0x02e7:
        r0 = DEBUG;
        if (r0 == 0) goto L_0x0303;
    L_0x02eb:
        r0 = "FragmentManager";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "movefrom RESUMED: ";
        r1 = r1.append(r2);
        r1 = r1.append(r11);
        r1 = r1.toString();
        android.util.Log.v(r0, r1);
    L_0x0303:
        r0 = 0;
        r11.mCalled = r0;
        r11.onPause();
        r0 = r11.mCalled;
        if (r0 != 0) goto L_0x032c;
    L_0x030d:
        r0 = new android.support.v4.app.SuperNotCalledException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Fragment ";
        r1 = r1.append(r2);
        r1 = r1.append(r11);
        r2 = " did not call through to super.onPause()";
        r1 = r1.append(r2);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
    L_0x032c:
        r0 = 0;
        r11.mResumed = r0;
    L_0x032f:
        r0 = 4;
        if (r12 >= r0) goto L_0x0377;
    L_0x0332:
        r0 = DEBUG;
        if (r0 == 0) goto L_0x034e;
    L_0x0336:
        r0 = "FragmentManager";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "movefrom STARTED: ";
        r1 = r1.append(r2);
        r1 = r1.append(r11);
        r1 = r1.toString();
        android.util.Log.v(r0, r1);
    L_0x034e:
        r0 = 0;
        r11.mCalled = r0;
        r11.performStop();
        r0 = r11.mCalled;
        if (r0 != 0) goto L_0x0377;
    L_0x0358:
        r0 = new android.support.v4.app.SuperNotCalledException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Fragment ";
        r1 = r1.append(r2);
        r1 = r1.append(r11);
        r2 = " did not call through to super.onStop()";
        r1 = r1.append(r2);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
    L_0x0377:
        r0 = 3;
        if (r12 >= r0) goto L_0x0399;
    L_0x037a:
        r0 = DEBUG;
        if (r0 == 0) goto L_0x0396;
    L_0x037e:
        r0 = "FragmentManager";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "movefrom STOPPED: ";
        r1 = r1.append(r2);
        r1 = r1.append(r11);
        r1 = r1.toString();
        android.util.Log.v(r0, r1);
    L_0x0396:
        r11.performReallyStop();
    L_0x0399:
        r0 = 2;
        if (r12 >= r0) goto L_0x02c8;
    L_0x039c:
        r0 = DEBUG;
        if (r0 == 0) goto L_0x03b8;
    L_0x03a0:
        r0 = "FragmentManager";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "movefrom ACTIVITY_CREATED: ";
        r1 = r1.append(r2);
        r1 = r1.append(r11);
        r1 = r1.toString();
        android.util.Log.v(r0, r1);
    L_0x03b8:
        r0 = r11.mView;
        if (r0 == 0) goto L_0x03cb;
    L_0x03bc:
        r0 = r10.mActivity;
        r0 = r0.isFinishing();
        if (r0 != 0) goto L_0x03cb;
    L_0x03c4:
        r0 = r11.mSavedViewState;
        if (r0 != 0) goto L_0x03cb;
    L_0x03c8:
        r10.saveFragmentViewState(r11);
    L_0x03cb:
        r0 = 0;
        r11.mCalled = r0;
        r11.performDestroyView();
        r0 = r11.mCalled;
        if (r0 != 0) goto L_0x03f4;
    L_0x03d5:
        r0 = new android.support.v4.app.SuperNotCalledException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Fragment ";
        r1 = r1.append(r2);
        r1 = r1.append(r11);
        r2 = " did not call through to super.onDestroyView()";
        r1 = r1.append(r2);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
    L_0x03f4:
        r0 = r11.mView;
        if (r0 == 0) goto L_0x0427;
    L_0x03f8:
        r0 = r11.mContainer;
        if (r0 == 0) goto L_0x0427;
    L_0x03fc:
        r6 = 0;
        r0 = r10.mCurState;
        if (r0 <= 0) goto L_0x040a;
    L_0x0401:
        r0 = r10.mDestroyed;
        if (r0 != 0) goto L_0x040a;
    L_0x0405:
        r0 = 0;
        r6 = r10.loadAnimation(r11, r13, r0, r14);
    L_0x040a:
        if (r6 == 0) goto L_0x0420;
    L_0x040c:
        r8 = r11;
        r0 = r11.mView;
        r11.mAnimatingAway = r0;
        r11.mStateAfterAnimating = r12;
        r0 = new android.support.v4.app.FragmentManagerImpl$5;
        r0.<init>(r8);
        r6.setAnimationListener(r0);
        r0 = r11.mView;
        r0.startAnimation(r6);
    L_0x0420:
        r0 = r11.mContainer;
        r1 = r11.mView;
        r0.removeView(r1);
    L_0x0427:
        r0 = 0;
        r11.mContainer = r0;
        r0 = 0;
        r11.mView = r0;
        r0 = 0;
        r11.mInnerView = r0;
        goto L_0x02c8;
    L_0x0432:
        r0 = DEBUG;
        if (r0 == 0) goto L_0x044e;
    L_0x0436:
        r0 = "FragmentManager";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "movefrom CREATED: ";
        r1 = r1.append(r2);
        r1 = r1.append(r11);
        r1 = r1.toString();
        android.util.Log.v(r0, r1);
    L_0x044e:
        r0 = r11.mRetaining;
        if (r0 != 0) goto L_0x047b;
    L_0x0452:
        r0 = 0;
        r11.mCalled = r0;
        r11.onDestroy();
        r0 = r11.mCalled;
        if (r0 != 0) goto L_0x047b;
    L_0x045c:
        r0 = new android.support.v4.app.SuperNotCalledException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Fragment ";
        r1 = r1.append(r2);
        r1 = r1.append(r11);
        r2 = " did not call through to super.onDestroy()";
        r1 = r1.append(r2);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
    L_0x047b:
        r0 = 0;
        r11.mCalled = r0;
        r11.onDetach();
        r0 = r11.mCalled;
        if (r0 != 0) goto L_0x04a4;
    L_0x0485:
        r0 = new android.support.v4.app.SuperNotCalledException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Fragment ";
        r1 = r1.append(r2);
        r1 = r1.append(r11);
        r2 = " did not call through to super.onDetach()";
        r1 = r1.append(r2);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
    L_0x04a4:
        if (r15 != 0) goto L_0x0042;
    L_0x04a6:
        r0 = r11.mRetaining;
        if (r0 != 0) goto L_0x04af;
    L_0x04aa:
        r10.makeInactive(r11);
        goto L_0x0042;
    L_0x04af:
        r0 = 0;
        r11.mActivity = r0;
        r0 = 0;
        r11.mFragmentManager = r0;
        goto L_0x0042;
        */
    }

    public void noteStateNotSaved() {
        this.mStateSaved = false;
    }

    public void performPendingDeferredStart(Fragment f) {
        if (!f.mDeferStart) {
            return;
        }
        if (this.mExecutingActions) {
            this.mHavePendingDeferredStart = true;
        } else {
            f.mDeferStart = false;
            moveToState(f, this.mCurState, 0, 0, false);
        }
    }

    public void popBackStack() {
        enqueueAction(new Runnable() {
            public void run() {
                FragmentManagerImpl.this.popBackStackState(FragmentManagerImpl.this.mActivity.mHandler, null, -1, 0);
            }
        }, HONEYCOMB);
    }

    public void popBackStack(int id, int flags) {
        if (id < 0) {
            throw new IllegalArgumentException("Bad id: " + id);
        }
        enqueueAction(new AnonymousClass_4(id, flags), HONEYCOMB);
    }

    public void popBackStack(String name, int flags) {
        enqueueAction(new AnonymousClass_3(name, flags), HONEYCOMB);
    }

    public boolean popBackStackImmediate() {
        checkStateLoss();
        executePendingTransactions();
        return popBackStackState(this.mActivity.mHandler, null, -1, 0);
    }

    public boolean popBackStackImmediate(int id, int flags) {
        checkStateLoss();
        executePendingTransactions();
        if (id >= 0) {
            return popBackStackState(this.mActivity.mHandler, null, id, flags);
        }
        throw new IllegalArgumentException("Bad id: " + id);
    }

    public boolean popBackStackImmediate(String name, int flags) {
        checkStateLoss();
        executePendingTransactions();
        return popBackStackState(this.mActivity.mHandler, name, -1, flags);
    }

    boolean popBackStackState(Handler handler, String name, int id, int flags) {
        if (this.mBackStack == null) {
            return HONEYCOMB;
        }
        if (name == null && id < 0 && (flags & 1) == 0) {
            int last = this.mBackStack.size() - 1;
            if (last < 0) {
                return HONEYCOMB;
            }
            ((BackStackRecord) this.mBackStack.remove(last)).popFromBackStack(true);
            reportBackStackChanged();
        } else {
            int index = -1;
            if (name != null || id >= 0) {
                BackStackRecord bss;
                index = this.mBackStack.size() - 1;
                while (index >= 0) {
                    bss = this.mBackStack.get(index);
                    if (name == null || !name.equals(bss.getName())) {
                        if (id >= 0 && id == bss.mIndex) {
                            break;
                        }
                        index--;
                    } else {
                        break;
                    }
                }
                if (index < 0) {
                    return HONEYCOMB;
                }
                if ((flags & 1) != 0) {
                    index--;
                    while (index >= 0) {
                        bss = this.mBackStack.get(index);
                        if ((name == null || !name.equals(bss.getName())) && (id < 0 || id != bss.mIndex)) {
                            break;
                        }
                        index--;
                    }
                }
            }
            if (index == this.mBackStack.size() - 1) {
                return HONEYCOMB;
            }
            ArrayList<BackStackRecord> states = new ArrayList();
            int i = this.mBackStack.size() - 1;
            while (i > index) {
                states.add(this.mBackStack.remove(i));
                i--;
            }
            int LAST = states.size() - 1;
            i = 0;
            while (i <= LAST) {
                boolean z;
                if (DEBUG) {
                    Log.v(TAG, "Popping back stack state: " + states.get(i));
                }
                BackStackRecord backStackRecord = (BackStackRecord) states.get(i);
                if (i == LAST) {
                    z = true;
                } else {
                    z = false;
                }
                backStackRecord.popFromBackStack(z);
                i++;
            }
            reportBackStackChanged();
        }
        return true;
    }

    public void putFragment(Bundle bundle, String key, Fragment fragment) {
        if (fragment.mIndex < 0) {
            throw new IllegalStateException("Fragment " + fragment + " is not currently in the FragmentManager");
        }
        bundle.putInt(key, fragment.mIndex);
    }

    public void removeFragment(Fragment fragment, int transition, int transitionStyle) {
        boolean inactive;
        if (DEBUG) {
            Log.v(TAG, "remove: " + fragment + " nesting=" + fragment.mBackStackNesting);
        }
        if (fragment.isInBackStack()) {
            inactive = false;
        } else {
            inactive = true;
        }
        if (!fragment.mDetached || inactive) {
            int i;
            if (this.mAdded != null) {
                this.mAdded.remove(fragment);
            }
            if (fragment.mHasMenu && fragment.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
            fragment.mAdded = false;
            fragment.mRemoving = true;
            if (inactive) {
                i = 0;
            } else {
                i = 1;
            }
            moveToState(fragment, i, transition, transitionStyle, HONEYCOMB);
        }
    }

    public void removeOnBackStackChangedListener(OnBackStackChangedListener listener) {
        if (this.mBackStackChangeListeners != null) {
            this.mBackStackChangeListeners.remove(listener);
        }
    }

    void reportBackStackChanged() {
        if (this.mBackStackChangeListeners != null) {
            int i = 0;
            while (i < this.mBackStackChangeListeners.size()) {
                ((OnBackStackChangedListener) this.mBackStackChangeListeners.get(i)).onBackStackChanged();
                i++;
            }
        }
    }

    void restoreAllState(Parcelable state, ArrayList<Fragment> nonConfig) {
        if (state != null) {
            FragmentManagerState fms = (FragmentManagerState) state;
            if (fms.mActive != null) {
                int i;
                Fragment f;
                FragmentState fs;
                if (nonConfig != null) {
                    i = 0;
                    while (i < nonConfig.size()) {
                        f = (Fragment) nonConfig.get(i);
                        if (DEBUG) {
                            Log.v(TAG, "restoreAllState: re-attaching retained " + f);
                        }
                        fs = fms.mActive[f.mIndex];
                        fs.mInstance = f;
                        f.mSavedViewState = null;
                        f.mBackStackNesting = 0;
                        f.mInLayout = false;
                        f.mAdded = false;
                        f.mTarget = null;
                        if (fs.mSavedFragmentState != null) {
                            fs.mSavedFragmentState.setClassLoader(this.mActivity.getClassLoader());
                            f.mSavedViewState = fs.mSavedFragmentState.getSparseParcelableArray(VIEW_STATE_TAG);
                        }
                        i++;
                    }
                }
                this.mActive = new ArrayList(fms.mActive.length);
                if (this.mAvailIndices != null) {
                    this.mAvailIndices.clear();
                }
                i = 0;
                while (i < fms.mActive.length) {
                    fs = fms.mActive[i];
                    if (fs != null) {
                        f = fs.instantiate(this.mActivity);
                        if (DEBUG) {
                            Log.v(TAG, "restoreAllState: adding #" + i + ": " + f);
                        }
                        this.mActive.add(f);
                        fs.mInstance = null;
                    } else {
                        if (DEBUG) {
                            Log.v(TAG, "restoreAllState: adding #" + i + ": (null)");
                        }
                        this.mActive.add(null);
                        if (this.mAvailIndices == null) {
                            this.mAvailIndices = new ArrayList();
                        }
                        if (DEBUG) {
                            Log.v(TAG, "restoreAllState: adding avail #" + i);
                        }
                        this.mAvailIndices.add(Integer.valueOf(i));
                    }
                    i++;
                }
                if (nonConfig != null) {
                    i = 0;
                    while (i < nonConfig.size()) {
                        f = nonConfig.get(i);
                        if (f.mTargetIndex >= 0) {
                            if (f.mTargetIndex < this.mActive.size()) {
                                f.mTarget = (Fragment) this.mActive.get(f.mTargetIndex);
                            } else {
                                Log.w(TAG, "Re-attaching retained fragment " + f + " target no longer exists: " + f.mTargetIndex);
                                f.mTarget = null;
                            }
                        }
                        i++;
                    }
                }
                if (fms.mAdded != null) {
                    this.mAdded = new ArrayList(fms.mAdded.length);
                    i = 0;
                    while (i < fms.mAdded.length) {
                        f = this.mActive.get(fms.mAdded[i]);
                        if (f == null) {
                            throw new IllegalStateException("No instantiated fragment for index #" + fms.mAdded[i]);
                        }
                        f.mAdded = true;
                        if (DEBUG) {
                            Log.v(TAG, "restoreAllState: making added #" + i + ": " + f);
                        }
                        this.mAdded.add(f);
                        i++;
                    }
                } else {
                    this.mAdded = null;
                }
                if (fms.mBackStack != null) {
                    this.mBackStack = new ArrayList(fms.mBackStack.length);
                    i = 0;
                    while (i < fms.mBackStack.length) {
                        BackStackRecord bse = fms.mBackStack[i].instantiate(this);
                        if (DEBUG) {
                            Log.v(TAG, "restoreAllState: adding bse #" + i + " (index " + bse.mIndex + "): " + bse);
                        }
                        this.mBackStack.add(bse);
                        if (bse.mIndex >= 0) {
                            setBackStackIndex(bse.mIndex, bse);
                        }
                        i++;
                    }
                } else {
                    this.mBackStack = null;
                }
            }
        }
    }

    ArrayList<Fragment> retainNonConfig() {
        ArrayList<Fragment> fragments = null;
        if (this.mActive != null) {
            int i = 0;
            while (i < this.mActive.size()) {
                Fragment f = (Fragment) this.mActive.get(i);
                if (f != null && f.mRetainInstance) {
                    if (fragments == null) {
                        fragments = new ArrayList();
                    }
                    fragments.add(f);
                    f.mRetaining = true;
                    f.mTargetIndex = f.mTarget != null ? f.mTarget.mIndex : -1;
                    if (DEBUG) {
                        Log.v(TAG, "retainNonConfig: keeping retained " + f);
                    }
                }
                i++;
            }
        }
        return fragments;
    }

    Parcelable saveAllState() {
        execPendingActions();
        if (HONEYCOMB) {
            this.mStateSaved = true;
        }
        if (this.mActive == null || this.mActive.size() <= 0) {
            return null;
        }
        String msg;
        int N = this.mActive.size();
        FragmentState[] active = new FragmentState[N];
        boolean haveFragments = HONEYCOMB;
        int i = 0;
        while (i < N) {
            Fragment f = (Fragment) this.mActive.get(i);
            if (f == null || f.mIndex >= 0) {
                haveFragments = true;
                FragmentState fs = new FragmentState(f);
                active[i] = fs;
                if (f.mState <= 0 || fs.mSavedFragmentState != null) {
                    fs.mSavedFragmentState = f.mSavedFragmentState;
                } else {
                    fs.mSavedFragmentState = saveFragmentBasicState(f);
                    if (f.mTarget == null || f.mTarget.mIndex >= 0) {
                        if (fs.mSavedFragmentState == null) {
                            fs.mSavedFragmentState = new Bundle();
                        }
                        putFragment(fs.mSavedFragmentState, TARGET_STATE_TAG, f.mTarget);
                        if (f.mTargetRequestCode != 0) {
                            fs.mSavedFragmentState.putInt(TARGET_REQUEST_CODE_STATE_TAG, f.mTargetRequestCode);
                        }
                    } else {
                        msg = "Failure saving state: " + f + " has target not in fragment manager: " + f.mTarget;
                        Log.e(TAG, msg);
                        dump("  ", null, new PrintWriter(new LogWriter(TAG)), new String[0]);
                        throw new IllegalStateException(msg);
                    }
                }
                if (DEBUG) {
                    Log.v(TAG, "Saved state of " + f + ": " + fs.mSavedFragmentState);
                }
            } else {
                msg = "Failure saving state: active " + f + " has cleared index: " + f.mIndex;
                Log.e(TAG, msg);
                dump("  ", null, new PrintWriter(new LogWriter(TAG)), new String[0]);
                throw new IllegalStateException(msg);
            }
            i++;
        }
        if (haveFragments) {
            int[] added = null;
            BackStackState[] backStack = null;
            if (this.mAdded != null) {
                N = this.mAdded.size();
                if (N > 0) {
                    added = new int[N];
                    i = 0;
                    while (i < N) {
                        added[i] = ((Fragment) this.mAdded.get(i)).mIndex;
                        if (added[i] < 0) {
                            msg = "Failure saving state: active " + this.mAdded.get(i) + " has cleared index: " + added[i];
                            Log.e(TAG, msg);
                            dump("  ", null, new PrintWriter(new LogWriter(TAG)), new String[0]);
                            throw new IllegalStateException(msg);
                        } else {
                            if (DEBUG) {
                                Log.v(TAG, "saveAllState: adding fragment #" + i + ": " + this.mAdded.get(i));
                            }
                            i++;
                        }
                    }
                }
            }
            if (this.mBackStack != null) {
                N = this.mBackStack.size();
                if (N > 0) {
                    backStack = new BackStackState[N];
                    i = 0;
                    while (i < N) {
                        backStack[i] = new BackStackState(this, (BackStackRecord) this.mBackStack.get(i));
                        if (DEBUG) {
                            Log.v(TAG, "saveAllState: adding back stack #" + i + ": " + this.mBackStack.get(i));
                        }
                        i++;
                    }
                }
            }
            Parcelable fms = new FragmentManagerState();
            fms.mActive = active;
            fms.mAdded = added;
            fms.mBackStack = backStack;
            return fms;
        } else if (!DEBUG) {
            return null;
        } else {
            Log.v(TAG, "saveAllState: no fragments!");
            return null;
        }
    }

    Bundle saveFragmentBasicState(Fragment f) {
        Bundle result = null;
        if (this.mStateBundle == null) {
            this.mStateBundle = new Bundle();
        }
        f.onSaveInstanceState(this.mStateBundle);
        if (!this.mStateBundle.isEmpty()) {
            result = this.mStateBundle;
            this.mStateBundle = null;
        }
        if (f.mView != null) {
            saveFragmentViewState(f);
        }
        if (f.mSavedViewState != null) {
            if (result == null) {
                result = new Bundle();
            }
            result.putSparseParcelableArray(VIEW_STATE_TAG, f.mSavedViewState);
        }
        if (!f.mUserVisibleHint) {
            if (result == null) {
                result = new Bundle();
            }
            result.putBoolean(USER_VISIBLE_HINT_TAG, f.mUserVisibleHint);
        }
        return result;
    }

    public SavedState saveFragmentInstanceState(Fragment fragment) {
        if (fragment.mIndex < 0) {
            throw new IllegalStateException("Fragment " + fragment + " is not currently in the FragmentManager");
        } else if (fragment.mState <= 0) {
            return null;
        } else {
            Bundle result = saveFragmentBasicState(fragment);
            return result != null ? new SavedState(result) : null;
        }
    }

    void saveFragmentViewState(Fragment f) {
        if (f.mInnerView != null) {
            if (this.mStateArray == null) {
                this.mStateArray = new SparseArray();
            } else {
                this.mStateArray.clear();
            }
            f.mInnerView.saveHierarchyState(this.mStateArray);
            if (this.mStateArray.size() > 0) {
                f.mSavedViewState = this.mStateArray;
                this.mStateArray = null;
            }
        }
    }

    public void setBackStackIndex(int index, BackStackRecord bse) {
        synchronized (this) {
            if (this.mBackStackIndices == null) {
                this.mBackStackIndices = new ArrayList();
            }
            int N = this.mBackStackIndices.size();
            if (index < N) {
                if (DEBUG) {
                    Log.v(TAG, "Setting back stack index " + index + " to " + bse);
                }
                this.mBackStackIndices.set(index, bse);
            }
            while (N < index) {
                this.mBackStackIndices.add(null);
                if (this.mAvailBackStackIndices == null) {
                    this.mAvailBackStackIndices = new ArrayList();
                }
                if (DEBUG) {
                    Log.v(TAG, "Adding available back stack index " + N);
                }
                this.mAvailBackStackIndices.add(Integer.valueOf(N));
                N++;
            }
            if (DEBUG) {
                Log.v(TAG, "Adding back stack index " + index + " with " + bse);
            }
            this.mBackStackIndices.add(bse);
        }
    }

    public void showFragment(Fragment fragment, int transition, int transitionStyle) {
        if (DEBUG) {
            Log.v(TAG, "show: " + fragment);
        }
        if (fragment.mHidden) {
            fragment.mHidden = false;
            if (fragment.mView != null) {
                Animation anim = loadAnimation(fragment, transition, true, transitionStyle);
                if (anim != null) {
                    fragment.mView.startAnimation(anim);
                }
                fragment.mView.setVisibility(0);
            }
            if (fragment.mAdded && fragment.mHasMenu && fragment.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
            fragment.onHiddenChanged(HONEYCOMB);
        }
    }

    void startPendingDeferredFragments() {
        if (this.mActive != null) {
            int i = 0;
            while (i < this.mActive.size()) {
                Fragment f = (Fragment) this.mActive.get(i);
                if (f != null) {
                    performPendingDeferredStart(f);
                }
                i++;
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("FragmentManager{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(" in ");
        DebugUtils.buildShortClassTag(this.mActivity, sb);
        sb.append("}}");
        return sb.toString();
    }
}