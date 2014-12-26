package android.support.v4.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;

public class FragmentActivity extends Activity {
    static final String FRAGMENTS_TAG = "android:support:fragments";
    private static final int HONEYCOMB = 11;
    static final int MSG_REALLY_STOPPED = 1;
    static final int MSG_RESUME_PENDING = 2;
    private static final String TAG = "FragmentActivity";
    SimpleArrayMap<String, LoaderManagerImpl> mAllLoaderManagers;
    boolean mCheckedForLoaderManager;
    final FragmentContainer mContainer;
    boolean mCreated;
    final FragmentManagerImpl mFragments;
    final Handler mHandler;
    LoaderManagerImpl mLoaderManager;
    boolean mLoadersStarted;
    boolean mOptionsMenuInvalidated;
    boolean mReallyStopped;
    boolean mResumed;
    boolean mRetaining;
    boolean mStopped;

    static class FragmentTag {
        public static final int[] Fragment;
        public static final int Fragment_id = 1;
        public static final int Fragment_name = 0;
        public static final int Fragment_tag = 2;

        static {
            Fragment = new int[]{16842755, 16842960, 16842961};
        }

        FragmentTag() {
        }
    }

    static final class NonConfigurationInstances {
        Object activity;
        SimpleArrayMap<String, Object> children;
        Object custom;
        ArrayList<Fragment> fragments;
        SimpleArrayMap<String, LoaderManagerImpl> loaders;

        NonConfigurationInstances() {
        }
    }

    public FragmentActivity() {
        this.mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_REALLY_STOPPED:
                        if (FragmentActivity.this.mStopped) {
                            FragmentActivity.this.doReallyStop(false);
                        }
                    case MSG_RESUME_PENDING:
                        FragmentActivity.this.onResumeFragments();
                        FragmentActivity.this.mFragments.execPendingActions();
                    default:
                        super.handleMessage(msg);
                }
            }
        };
        this.mFragments = new FragmentManagerImpl();
        this.mContainer = new FragmentContainer() {
            public View findViewById(int id) {
                return FragmentActivity.this.findViewById(id);
            }
        };
    }

    private void dumpViewHierarchy(String prefix, PrintWriter writer, View view) {
        writer.print(prefix);
        if (view == null) {
            writer.println("null");
        } else {
            writer.println(viewToString(view));
            if (view instanceof ViewGroup) {
                ViewGroup grp = (ViewGroup) view;
                int N = grp.getChildCount();
                if (N > 0) {
                    prefix = prefix + "  ";
                    int i = 0;
                    while (i < N) {
                        dumpViewHierarchy(prefix, writer, grp.getChildAt(i));
                        i++;
                    }
                }
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String viewToString(android.view.View r12_view) {
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.app.FragmentActivity.viewToString(android.view.View):java.lang.String");
        /*
        r9 = 86;
        r7 = 70;
        r11 = 44;
        r10 = 32;
        r8 = 46;
        r2 = new java.lang.StringBuilder;
        r6 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        r2.<init>(r6);
        r6 = r12.getClass();
        r6 = r6.getName();
        r2.append(r6);
        r6 = 123; // 0x7b float:1.72E-43 double:6.1E-322;
        r2.append(r6);
        r6 = java.lang.System.identityHashCode(r12);
        r6 = java.lang.Integer.toHexString(r6);
        r2.append(r6);
        r2.append(r10);
        r6 = r12.getVisibility();
        switch(r6) {
            case 0: goto L_0x011e;
            case 4: goto L_0x0123;
            case 8: goto L_0x012a;
            default: goto L_0x0036;
        };
    L_0x0036:
        r2.append(r8);
    L_0x0039:
        r6 = r12.isFocusable();
        if (r6 == 0) goto L_0x0131;
    L_0x003f:
        r6 = r7;
    L_0x0040:
        r2.append(r6);
        r6 = r12.isEnabled();
        if (r6 == 0) goto L_0x0134;
    L_0x0049:
        r6 = 69;
    L_0x004b:
        r2.append(r6);
        r6 = r12.willNotDraw();
        if (r6 == 0) goto L_0x0137;
    L_0x0054:
        r6 = r8;
    L_0x0055:
        r2.append(r6);
        r6 = r12.isHorizontalScrollBarEnabled();
        if (r6 == 0) goto L_0x013b;
    L_0x005e:
        r6 = 72;
    L_0x0060:
        r2.append(r6);
        r6 = r12.isVerticalScrollBarEnabled();
        if (r6 == 0) goto L_0x013e;
    L_0x0069:
        r6 = r9;
    L_0x006a:
        r2.append(r6);
        r6 = r12.isClickable();
        if (r6 == 0) goto L_0x0141;
    L_0x0073:
        r6 = 67;
    L_0x0075:
        r2.append(r6);
        r6 = r12.isLongClickable();
        if (r6 == 0) goto L_0x0144;
    L_0x007e:
        r6 = 76;
    L_0x0080:
        r2.append(r6);
        r2.append(r10);
        r6 = r12.isFocused();
        if (r6 == 0) goto L_0x0147;
    L_0x008c:
        r2.append(r7);
        r6 = r12.isSelected();
        if (r6 == 0) goto L_0x014a;
    L_0x0095:
        r6 = 83;
    L_0x0097:
        r2.append(r6);
        r6 = r12.isPressed();
        if (r6 == 0) goto L_0x00a2;
    L_0x00a0:
        r8 = 80;
    L_0x00a2:
        r2.append(r8);
        r2.append(r10);
        r6 = r12.getLeft();
        r2.append(r6);
        r2.append(r11);
        r6 = r12.getTop();
        r2.append(r6);
        r6 = 45;
        r2.append(r6);
        r6 = r12.getRight();
        r2.append(r6);
        r2.append(r11);
        r6 = r12.getBottom();
        r2.append(r6);
        r1 = r12.getId();
        r6 = -1;
        if (r1 == r6) goto L_0x0114;
    L_0x00d6:
        r6 = " #";
        r2.append(r6);
        r6 = java.lang.Integer.toHexString(r1);
        r2.append(r6);
        r4 = r12.getResources();
        if (r1 == 0) goto L_0x0114;
    L_0x00e8:
        if (r4 == 0) goto L_0x0114;
    L_0x00ea:
        r6 = -16777216; // 0xffffffffff000000 float:-1.7014118E38 double:NaN;
        r6 = r6 & r1;
        switch(r6) {
            case 16777216: goto L_0x0150;
            case 2130706432: goto L_0x014d;
            default: goto L_0x00f0;
        };
    L_0x00f0:
        r3 = r4.getResourcePackageName(r1);	 Catch:{ NotFoundException -> 0x0153 }
    L_0x00f4:
        r5 = r4.getResourceTypeName(r1);	 Catch:{ NotFoundException -> 0x0153 }
        r0 = r4.getResourceEntryName(r1);	 Catch:{ NotFoundException -> 0x0153 }
        r6 = " ";
        r2.append(r6);	 Catch:{ NotFoundException -> 0x0153 }
        r2.append(r3);	 Catch:{ NotFoundException -> 0x0153 }
        r6 = ":";
        r2.append(r6);	 Catch:{ NotFoundException -> 0x0153 }
        r2.append(r5);	 Catch:{ NotFoundException -> 0x0153 }
        r6 = "/";
        r2.append(r6);	 Catch:{ NotFoundException -> 0x0153 }
        r2.append(r0);	 Catch:{ NotFoundException -> 0x0153 }
    L_0x0114:
        r6 = "}";
        r2.append(r6);
        r6 = r2.toString();
        return r6;
    L_0x011e:
        r2.append(r9);
        goto L_0x0039;
    L_0x0123:
        r6 = 73;
        r2.append(r6);
        goto L_0x0039;
    L_0x012a:
        r6 = 71;
        r2.append(r6);
        goto L_0x0039;
    L_0x0131:
        r6 = r8;
        goto L_0x0040;
    L_0x0134:
        r6 = r8;
        goto L_0x004b;
    L_0x0137:
        r6 = 68;
        goto L_0x0055;
    L_0x013b:
        r6 = r8;
        goto L_0x0060;
    L_0x013e:
        r6 = r8;
        goto L_0x006a;
    L_0x0141:
        r6 = r8;
        goto L_0x0075;
    L_0x0144:
        r6 = r8;
        goto L_0x0080;
    L_0x0147:
        r7 = r8;
        goto L_0x008c;
    L_0x014a:
        r6 = r8;
        goto L_0x0097;
    L_0x014d:
        r3 = "app";
        goto L_0x00f4;
    L_0x0150:
        r3 = "android";
        goto L_0x00f4;
    L_0x0153:
        r6 = move-exception;
        goto L_0x0114;
        */
    }

    void doReallyStop(boolean retaining) {
        if (!this.mReallyStopped) {
            this.mReallyStopped = true;
            this.mRetaining = retaining;
            this.mHandler.removeMessages(MSG_REALLY_STOPPED);
            onReallyStop();
        }
    }

    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        String innerPrefix;
        if (VERSION.SDK_INT >= 11) {
            writer.print(prefix);
            writer.print("Local FragmentActivity ");
            writer.print(Integer.toHexString(System.identityHashCode(this)));
            writer.println(" State:");
            innerPrefix = prefix + "  ";
            writer.print(innerPrefix);
            writer.print("mCreated=");
            writer.print(this.mCreated);
            writer.print("mResumed=");
            writer.print(this.mResumed);
            writer.print(" mStopped=");
            writer.print(this.mStopped);
            writer.print(" mReallyStopped=");
            writer.println(this.mReallyStopped);
            writer.print(innerPrefix);
            writer.print("mLoadersStarted=");
            writer.println(this.mLoadersStarted);
        } else {
            writer.print(prefix);
            writer.print("Local FragmentActivity ");
            writer.print(Integer.toHexString(System.identityHashCode(this)));
            writer.println(" State:");
            innerPrefix = prefix + "  ";
            writer.print(innerPrefix);
            writer.print("mCreated=");
            writer.print(this.mCreated);
            writer.print("mResumed=");
            writer.print(this.mResumed);
            writer.print(" mStopped=");
            writer.print(this.mStopped);
            writer.print(" mReallyStopped=");
            writer.println(this.mReallyStopped);
            writer.print(innerPrefix);
            writer.print("mLoadersStarted=");
            writer.println(this.mLoadersStarted);
        }
        if (this.mLoaderManager != null) {
            writer.print(prefix);
            writer.print("Loader Manager ");
            writer.print(Integer.toHexString(System.identityHashCode(this.mLoaderManager)));
            writer.println(":");
            this.mLoaderManager.dump(prefix + "  ", fd, writer, args);
        }
        this.mFragments.dump(prefix, fd, writer, args);
        writer.print(prefix);
        writer.println("View Hierarchy:");
        dumpViewHierarchy(prefix + "  ", writer, getWindow().getDecorView());
    }

    public Object getLastCustomNonConfigurationInstance() {
        NonConfigurationInstances nc = (NonConfigurationInstances) getLastNonConfigurationInstance();
        return nc != null ? nc.custom : null;
    }

    LoaderManagerImpl getLoaderManager(String who, boolean started, boolean create) {
        if (this.mAllLoaderManagers == null) {
            this.mAllLoaderManagers = new SimpleArrayMap();
        }
        LoaderManagerImpl lm = (LoaderManagerImpl) this.mAllLoaderManagers.get(who);
        if (lm != null) {
            lm.updateActivity(this);
            return lm;
        } else if (!create) {
            return lm;
        } else {
            lm = new LoaderManagerImpl(who, this, started);
            this.mAllLoaderManagers.put(who, lm);
            return lm;
        }
    }

    public FragmentManager getSupportFragmentManager() {
        return this.mFragments;
    }

    public LoaderManager getSupportLoaderManager() {
        if (this.mLoaderManager != null) {
            return this.mLoaderManager;
        }
        this.mCheckedForLoaderManager = true;
        this.mLoaderManager = getLoaderManager("(root)", this.mLoadersStarted, true);
        return this.mLoaderManager;
    }

    void invalidateSupportFragment(String who) {
        if (this.mAllLoaderManagers != null) {
            LoaderManagerImpl lm = (LoaderManagerImpl) this.mAllLoaderManagers.get(who);
            if (lm != null && !lm.mRetaining) {
                lm.doDestroy();
                this.mAllLoaderManagers.remove(who);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.mFragments.noteStateNotSaved();
        int index = requestCode >> 16;
        if (index != 0) {
            index--;
            if (this.mFragments.mActive == null || index < 0 || index >= this.mFragments.mActive.size()) {
                Log.w(TAG, "Activity result fragment index out of range: 0x" + Integer.toHexString(requestCode));
            } else {
                Fragment frag = (Fragment) this.mFragments.mActive.get(index);
                if (frag == null) {
                    Log.w(TAG, "Activity result no fragment exists for index: 0x" + Integer.toHexString(requestCode));
                } else {
                    frag.onActivityResult(65535 & requestCode, resultCode, data);
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onAttachFragment(Fragment fragment) {
    }

    public void onBackPressed() {
        if (!this.mFragments.popBackStackImmediate()) {
            finish();
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.mFragments.dispatchConfigurationChanged(newConfig);
    }

    protected void onCreate(Bundle savedInstanceState) {
        ArrayList arrayList = null;
        this.mFragments.attachActivity(this, this.mContainer, null);
        if (getLayoutInflater().getFactory() == null) {
            getLayoutInflater().setFactory(this);
        }
        super.onCreate(savedInstanceState);
        NonConfigurationInstances nc = (NonConfigurationInstances) getLastNonConfigurationInstance();
        if (nc != null) {
            this.mAllLoaderManagers = nc.loaders;
        }
        if (savedInstanceState != null) {
            Parcelable p = savedInstanceState.getParcelable(FRAGMENTS_TAG);
            FragmentManagerImpl fragmentManagerImpl = this.mFragments;
            if (nc != null) {
                arrayList = nc.fragments;
            }
            fragmentManagerImpl.restoreAllState(p, arrayList);
        }
        this.mFragments.dispatchCreate();
    }

    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        if (featureId != 0) {
            return super.onCreatePanelMenu(featureId, menu);
        }
        return VERSION.SDK_INT >= 11 ? super.onCreatePanelMenu(featureId, menu) | this.mFragments.dispatchCreateOptionsMenu(menu, getMenuInflater()) : true;
    }

    public View onCreateView(String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        Fragment fragment = null;
        int containerId = 0;
        if (!"fragment".equals(name)) {
            return super.onCreateView(name, context, attrs);
        }
        String fname = attrs.getAttributeValue(null, "class");
        TypedArray a = context.obtainStyledAttributes(attrs, FragmentTag.Fragment);
        if (fname == null) {
            fname = a.getString(0);
        }
        int id = a.getResourceId(MSG_REALLY_STOPPED, -1);
        String tag = a.getString(MSG_RESUME_PENDING);
        a.recycle();
        if (!Fragment.isSupportFragmentClass(this, fname)) {
            return super.onCreateView(name, context, attrs);
        }
        if (0 != 0) {
            containerId = null.getId();
        }
        if (containerId == -1 && id == -1 && tag == null) {
            throw new IllegalArgumentException(attrs.getPositionDescription() + ": Must specify unique android:id, android:tag, or have a parent with an id for " + fname);
        }
        if (id != -1) {
            fragment = this.mFragments.findFragmentById(id);
        }
        if (fragment == null && tag != null) {
            fragment = this.mFragments.findFragmentByTag(tag);
        }
        if (fragment == null && containerId != -1) {
            fragment = this.mFragments.findFragmentById(containerId);
        }
        if (FragmentManagerImpl.DEBUG) {
            Log.v(TAG, "onCreateView: id=0x" + Integer.toHexString(id) + " fname=" + fname + " existing=" + fragment);
        }
        if (fragment == null) {
            fragment = Fragment.instantiate(this, fname);
            fragment.mFromLayout = true;
            fragment.mFragmentId = id != 0 ? id : containerId;
            fragment.mContainerId = containerId;
            fragment.mTag = tag;
            fragment.mInLayout = true;
            fragment.mFragmentManager = this.mFragments;
            fragment.onInflate(this, attrs, fragment.mSavedFragmentState);
            this.mFragments.addFragment(fragment, true);
        } else if (fragment.mInLayout) {
            throw new IllegalArgumentException(attrs.getPositionDescription() + ": Duplicate id 0x" + Integer.toHexString(id) + ", tag " + tag + ", or parent id 0x" + Integer.toHexString(containerId) + " with another fragment for " + fname);
        } else {
            fragment.mInLayout = true;
            if (!fragment.mRetaining) {
                fragment.onInflate(this, attrs, fragment.mSavedFragmentState);
            }
            this.mFragments.moveToState(fragment);
        }
        if (fragment.mView == null) {
            throw new IllegalStateException("Fragment " + fname + " did not create a view.");
        }
        if (id != 0) {
            fragment.mView.setId(id);
        }
        if (fragment.mView.getTag() == null) {
            fragment.mView.setTag(tag);
        }
        return fragment.mView;
    }

    protected void onDestroy() {
        super.onDestroy();
        doReallyStop(false);
        this.mFragments.dispatchDestroy();
        if (this.mLoaderManager != null) {
            this.mLoaderManager.doDestroy();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (VERSION.SDK_INT >= 5 || keyCode != 4 || event.getRepeatCount() != 0) {
            return super.onKeyDown(keyCode, event);
        }
        onBackPressed();
        return true;
    }

    public void onLowMemory() {
        super.onLowMemory();
        this.mFragments.dispatchLowMemory();
    }

    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (super.onMenuItemSelected(featureId, item)) {
            return true;
        }
        switch (featureId) {
            case MMAdView.TRANSITION_NONE:
                return this.mFragments.dispatchOptionsItemSelected(item);
            case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                return this.mFragments.dispatchContextItemSelected(item);
            default:
                return false;
        }
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.mFragments.noteStateNotSaved();
    }

    public void onPanelClosed(int featureId, Menu menu) {
        switch (featureId) {
            case MMAdView.TRANSITION_NONE:
                this.mFragments.dispatchOptionsMenuClosed(menu);
                break;
        }
        super.onPanelClosed(featureId, menu);
    }

    protected void onPause() {
        super.onPause();
        this.mResumed = false;
        if (this.mHandler.hasMessages(MSG_RESUME_PENDING)) {
            this.mHandler.removeMessages(MSG_RESUME_PENDING);
            onResumeFragments();
        }
        this.mFragments.dispatchPause();
    }

    protected void onPostResume() {
        super.onPostResume();
        this.mHandler.removeMessages(MSG_RESUME_PENDING);
        onResumeFragments();
        this.mFragments.execPendingActions();
    }

    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        return super.onPreparePanel(0, view, menu);
    }

    public boolean onPreparePanel(int featureId, View view, Menu menu) {
        if (featureId != 0 || menu == null) {
            return super.onPreparePanel(featureId, view, menu);
        }
        if (this.mOptionsMenuInvalidated) {
            this.mOptionsMenuInvalidated = false;
            menu.clear();
            onCreatePanelMenu(featureId, menu);
        }
        return onPrepareOptionsPanel(view, menu) | this.mFragments.dispatchPrepareOptionsMenu(menu);
    }

    void onReallyStop() {
        if (this.mLoadersStarted) {
            this.mLoadersStarted = false;
            if (this.mLoaderManager != null) {
                if (this.mRetaining) {
                    this.mLoaderManager.doRetain();
                } else {
                    this.mLoaderManager.doStop();
                }
            }
        }
        this.mFragments.dispatchReallyStop();
    }

    protected void onResume() {
        super.onResume();
        this.mHandler.sendEmptyMessage(MSG_RESUME_PENDING);
        this.mResumed = true;
        this.mFragments.execPendingActions();
    }

    protected void onResumeFragments() {
        this.mFragments.dispatchResume();
    }

    public Object onRetainCustomNonConfigurationInstance() {
        return null;
    }

    public final Object onRetainNonConfigurationInstance() {
        if (this.mStopped) {
            doReallyStop(true);
        }
        Object custom = onRetainCustomNonConfigurationInstance();
        ArrayList<Fragment> fragments = this.mFragments.retainNonConfig();
        boolean retainLoaders = false;
        if (this.mAllLoaderManagers != null) {
            int N = this.mAllLoaderManagers.size();
            LoaderManagerImpl[] loaders = new LoaderManagerImpl[N];
            int i = N - 1;
            while (i >= 0) {
                loaders[i] = (LoaderManagerImpl) this.mAllLoaderManagers.valueAt(i);
                i--;
            }
            i = 0;
            while (i < N) {
                LoaderManagerImpl lm = loaders[i];
                if (lm.mRetaining) {
                    retainLoaders = true;
                } else {
                    lm.doDestroy();
                    this.mAllLoaderManagers.remove(lm.mWho);
                }
                i++;
            }
        }
        if (fragments == null && !retainLoaders && custom == null) {
            return null;
        }
        Object nci = new NonConfigurationInstances();
        nci.activity = null;
        nci.custom = custom;
        nci.children = null;
        nci.fragments = fragments;
        nci.loaders = this.mAllLoaderManagers;
        return nci;
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Parcelable p = this.mFragments.saveAllState();
        if (p != null) {
            outState.putParcelable(FRAGMENTS_TAG, p);
        }
    }

    protected void onStart() {
        super.onStart();
        this.mStopped = false;
        this.mReallyStopped = false;
        this.mHandler.removeMessages(MSG_REALLY_STOPPED);
        if (!this.mCreated) {
            this.mCreated = true;
            this.mFragments.dispatchActivityCreated();
        }
        this.mFragments.noteStateNotSaved();
        this.mFragments.execPendingActions();
        if (!this.mLoadersStarted) {
            this.mLoadersStarted = true;
            if (this.mLoaderManager != null) {
                this.mLoaderManager.doStart();
            } else if (!this.mCheckedForLoaderManager) {
                this.mLoaderManager = getLoaderManager("(root)", this.mLoadersStarted, false);
                if (!(this.mLoaderManager == null || this.mLoaderManager.mStarted)) {
                    this.mLoaderManager.doStart();
                }
            }
            this.mCheckedForLoaderManager = true;
        }
        this.mFragments.dispatchStart();
        if (this.mAllLoaderManagers != null) {
            int N = this.mAllLoaderManagers.size();
            LoaderManagerImpl[] loaders = new LoaderManagerImpl[N];
            int i = N - 1;
            while (i >= 0) {
                loaders[i] = (LoaderManagerImpl) this.mAllLoaderManagers.valueAt(i);
                i--;
            }
            i = 0;
            while (i < N) {
                LoaderManagerImpl lm = loaders[i];
                lm.finishRetain();
                lm.doReportStart();
                i++;
            }
        }
    }

    protected void onStop() {
        super.onStop();
        this.mStopped = true;
        this.mHandler.sendEmptyMessage(MSG_REALLY_STOPPED);
        this.mFragments.dispatchStop();
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        if (requestCode == -1 || (-65536 & requestCode) == 0) {
            super.startActivityForResult(intent, requestCode);
        } else {
            throw new IllegalArgumentException("Can only use lower 16 bits for requestCode");
        }
    }

    public void startActivityFromFragment(Fragment fragment, Intent intent, int requestCode) {
        if (requestCode == -1) {
            super.startActivityForResult(intent, -1);
        } else if ((-65536 & requestCode) != 0) {
            throw new IllegalArgumentException("Can only use lower 16 bits for requestCode");
        } else {
            super.startActivityForResult(intent, (fragment.mIndex + 1) << 16 + 65535 & requestCode);
        }
    }

    public void supportInvalidateOptionsMenu() {
        if (VERSION.SDK_INT >= 11) {
            ActivityCompatHoneycomb.invalidateOptionsMenu(this);
        } else {
            this.mOptionsMenuInvalidated = true;
        }
    }
}