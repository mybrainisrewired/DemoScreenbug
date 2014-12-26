package android.support.v4.app;

import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.content.Loader.OnLoadCompleteListener;
import android.support.v4.util.DebugUtils;
import android.support.v4.util.SparseArrayCompat;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Modifier;

class LoaderManagerImpl extends LoaderManager {
    static boolean DEBUG = false;
    static final String TAG = "LoaderManager";
    FragmentActivity mActivity;
    boolean mCreatingLoader;
    final SparseArrayCompat<LoaderInfo> mInactiveLoaders;
    final SparseArrayCompat<LoaderInfo> mLoaders;
    boolean mRetaining;
    boolean mRetainingStarted;
    boolean mStarted;
    final String mWho;

    final class LoaderInfo implements OnLoadCompleteListener<Object> {
        final Bundle mArgs;
        LoaderCallbacks<Object> mCallbacks;
        Object mData;
        boolean mDeliveredData;
        boolean mDestroyed;
        boolean mHaveData;
        final int mId;
        boolean mListenerRegistered;
        Loader<Object> mLoader;
        LoaderInfo mPendingLoader;
        boolean mReportNextStart;
        boolean mRetaining;
        boolean mRetainingStarted;
        boolean mStarted;

        public LoaderInfo(int id, Bundle args, LoaderCallbacks<Object> callbacks) {
            this.mId = id;
            this.mArgs = args;
            this.mCallbacks = callbacks;
        }

        void callOnLoadFinished(Loader<Object> loader, Object data) {
            if (this.mCallbacks != null) {
                String lastBecause = null;
                if (LoaderManagerImpl.this.mActivity != null) {
                    lastBecause = LoaderManagerImpl.this.mActivity.mFragments.mNoTransactionsBecause;
                    LoaderManagerImpl.this.mActivity.mFragments.mNoTransactionsBecause = "onLoadFinished";
                }
                try {
                    if (DEBUG) {
                        Log.v(TAG, "  onLoadFinished in " + loader + ": " + loader.dataToString(data));
                    }
                    this.mCallbacks.onLoadFinished(loader, data);
                    if (LoaderManagerImpl.this.mActivity != null) {
                        LoaderManagerImpl.this.mActivity.mFragments.mNoTransactionsBecause = lastBecause;
                    }
                    this.mDeliveredData = true;
                } catch (Throwable th) {
                    if (LoaderManagerImpl.this.mActivity != null) {
                        LoaderManagerImpl.this.mActivity.mFragments.mNoTransactionsBecause = lastBecause;
                    }
                }
            }
        }

        void destroy() {
            if (DEBUG) {
                Log.v(TAG, "  Destroying: " + this);
            }
            this.mDestroyed = true;
            boolean needReset = this.mDeliveredData;
            this.mDeliveredData = false;
            if (this.mCallbacks != null && this.mLoader != null && this.mHaveData && needReset) {
                if (DEBUG) {
                    Log.v(TAG, "  Reseting: " + this);
                }
                String lastBecause = null;
                if (LoaderManagerImpl.this.mActivity != null) {
                    lastBecause = LoaderManagerImpl.this.mActivity.mFragments.mNoTransactionsBecause;
                    LoaderManagerImpl.this.mActivity.mFragments.mNoTransactionsBecause = "onLoaderReset";
                }
                try {
                    this.mCallbacks.onLoaderReset(this.mLoader);
                    if (LoaderManagerImpl.this.mActivity != null) {
                        LoaderManagerImpl.this.mActivity.mFragments.mNoTransactionsBecause = lastBecause;
                    }
                } catch (Throwable th) {
                    if (LoaderManagerImpl.this.mActivity != null) {
                        LoaderManagerImpl.this.mActivity.mFragments.mNoTransactionsBecause = lastBecause;
                    }
                }
            }
            this.mCallbacks = null;
            this.mData = null;
            this.mHaveData = false;
            if (this.mLoader != null) {
                if (this.mListenerRegistered) {
                    this.mListenerRegistered = false;
                    this.mLoader.unregisterListener(this);
                }
                this.mLoader.reset();
            }
            if (this.mPendingLoader != null) {
                this.mPendingLoader.destroy();
            }
        }

        public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
            writer.print(prefix);
            writer.print("mId=");
            writer.print(this.mId);
            writer.print(" mArgs=");
            writer.println(this.mArgs);
            writer.print(prefix);
            writer.print("mCallbacks=");
            writer.println(this.mCallbacks);
            writer.print(prefix);
            writer.print("mLoader=");
            writer.println(this.mLoader);
            if (this.mLoader != null) {
                this.mLoader.dump(prefix + "  ", fd, writer, args);
            }
            if (this.mHaveData || this.mDeliveredData) {
                writer.print(prefix);
                writer.print("mHaveData=");
                writer.print(this.mHaveData);
                writer.print("  mDeliveredData=");
                writer.println(this.mDeliveredData);
                writer.print(prefix);
                writer.print("mData=");
                writer.println(this.mData);
            }
            writer.print(prefix);
            writer.print("mStarted=");
            writer.print(this.mStarted);
            writer.print(" mReportNextStart=");
            writer.print(this.mReportNextStart);
            writer.print(" mDestroyed=");
            writer.println(this.mDestroyed);
            writer.print(prefix);
            writer.print("mRetaining=");
            writer.print(this.mRetaining);
            writer.print(" mRetainingStarted=");
            writer.print(this.mRetainingStarted);
            writer.print(" mListenerRegistered=");
            writer.println(this.mListenerRegistered);
            if (this.mPendingLoader != null) {
                writer.print(prefix);
                writer.println("Pending Loader ");
                writer.print(this.mPendingLoader);
                writer.println(":");
                this.mPendingLoader.dump(prefix + "  ", fd, writer, args);
            }
        }

        void finishRetain() {
            if (this.mRetaining) {
                if (DEBUG) {
                    Log.v(TAG, "  Finished Retaining: " + this);
                }
                this.mRetaining = false;
                if (!(this.mStarted == this.mRetainingStarted || this.mStarted)) {
                    stop();
                }
            }
            if (this.mStarted && this.mHaveData && !this.mReportNextStart) {
                callOnLoadFinished(this.mLoader, this.mData);
            }
        }

        public void onLoadComplete(Loader<Object> loader, Object data) {
            if (DEBUG) {
                Log.v(TAG, "onLoadComplete: " + this);
            }
            if (this.mDestroyed) {
                if (DEBUG) {
                    Log.v(TAG, "  Ignoring load complete -- destroyed");
                }
            } else if (LoaderManagerImpl.this.mLoaders.get(this.mId) == this) {
                LoaderInfo pending = this.mPendingLoader;
                if (pending != null) {
                    if (DEBUG) {
                        Log.v(TAG, "  Switching to pending loader: " + pending);
                    }
                    this.mPendingLoader = null;
                    LoaderManagerImpl.this.mLoaders.put(this.mId, null);
                    destroy();
                    LoaderManagerImpl.this.installLoader(pending);
                } else {
                    this.mData = data;
                    this.mHaveData = true;
                    if (this.mStarted) {
                        callOnLoadFinished(loader, data);
                    }
                    LoaderInfo info = (LoaderInfo) LoaderManagerImpl.this.mInactiveLoaders.get(this.mId);
                    if (!(info == null || info == this)) {
                        info.mDeliveredData = false;
                        info.destroy();
                        LoaderManagerImpl.this.mInactiveLoaders.remove(this.mId);
                    }
                    if (LoaderManagerImpl.this.mActivity != null && !LoaderManagerImpl.this.hasRunningLoaders()) {
                        LoaderManagerImpl.this.mActivity.mFragments.startPendingDeferredFragments();
                    }
                }
            } else if (DEBUG) {
                Log.v(TAG, "  Ignoring load complete -- not active");
            }
        }

        void reportStart() {
            if (this.mStarted && this.mReportNextStart) {
                this.mReportNextStart = false;
                if (this.mHaveData) {
                    callOnLoadFinished(this.mLoader, this.mData);
                }
            }
        }

        void retain() {
            if (DEBUG) {
                Log.v(TAG, "  Retaining: " + this);
            }
            this.mRetaining = true;
            this.mRetainingStarted = this.mStarted;
            this.mStarted = false;
            this.mCallbacks = null;
        }

        void start() {
            if (this.mRetaining && this.mRetainingStarted) {
                this.mStarted = true;
            } else if (!this.mStarted) {
                this.mStarted = true;
                if (DEBUG) {
                    Log.v(TAG, "  Starting: " + this);
                }
                if (this.mLoader == null && this.mCallbacks != null) {
                    this.mLoader = this.mCallbacks.onCreateLoader(this.mId, this.mArgs);
                }
                if (this.mLoader == null) {
                    return;
                }
                if (!this.mLoader.getClass().isMemberClass() || Modifier.isStatic(this.mLoader.getClass().getModifiers())) {
                    if (!this.mListenerRegistered) {
                        this.mLoader.registerListener(this.mId, this);
                        this.mListenerRegistered = true;
                    }
                    this.mLoader.startLoading();
                } else {
                    throw new IllegalArgumentException("Object returned from onCreateLoader must not be a non-static inner member class: " + this.mLoader);
                }
            }
        }

        void stop() {
            if (DEBUG) {
                Log.v(TAG, "  Stopping: " + this);
            }
            this.mStarted = false;
            if (!this.mRetaining && this.mLoader != null && this.mListenerRegistered) {
                this.mListenerRegistered = false;
                this.mLoader.unregisterListener(this);
                this.mLoader.stopLoading();
            }
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(64);
            sb.append("LoaderInfo{");
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append(" #");
            sb.append(this.mId);
            sb.append(" : ");
            DebugUtils.buildShortClassTag(this.mLoader, sb);
            sb.append("}}");
            return sb.toString();
        }
    }

    static {
        DEBUG = false;
    }

    LoaderManagerImpl(String who, FragmentActivity activity, boolean started) {
        this.mLoaders = new SparseArrayCompat();
        this.mInactiveLoaders = new SparseArrayCompat();
        this.mWho = who;
        this.mActivity = activity;
        this.mStarted = started;
    }

    private LoaderInfo createAndInstallLoader(int id, Bundle args, LoaderCallbacks<Object> callback) {
        boolean z = false;
        this.mCreatingLoader = true;
        LoaderInfo info = createLoader(id, args, callback);
        installLoader(info);
        this.mCreatingLoader = z;
        return info;
    }

    private LoaderInfo createLoader(int id, Bundle args, LoaderCallbacks<Object> callback) {
        LoaderInfo info = new LoaderInfo(id, args, callback);
        info.mLoader = callback.onCreateLoader(id, args);
        return info;
    }

    public void destroyLoader(int id) {
        if (this.mCreatingLoader) {
            throw new IllegalStateException("Called while creating a loader");
        }
        if (DEBUG) {
            Log.v(TAG, "destroyLoader in " + this + " of " + id);
        }
        int idx = this.mLoaders.indexOfKey(id);
        if (idx >= 0) {
            LoaderInfo info = (LoaderInfo) this.mLoaders.valueAt(idx);
            this.mLoaders.removeAt(idx);
            info.destroy();
        }
        idx = this.mInactiveLoaders.indexOfKey(id);
        if (idx >= 0) {
            info = this.mInactiveLoaders.valueAt(idx);
            this.mInactiveLoaders.removeAt(idx);
            info.destroy();
        }
        if (this.mActivity != null && !hasRunningLoaders()) {
            this.mActivity.mFragments.startPendingDeferredFragments();
        }
    }

    void doDestroy() {
        int i;
        if (!this.mRetaining) {
            if (DEBUG) {
                Log.v(TAG, "Destroying Active in " + this);
            }
            i = this.mLoaders.size() - 1;
            while (i >= 0) {
                ((LoaderInfo) this.mLoaders.valueAt(i)).destroy();
                i--;
            }
            this.mLoaders.clear();
        }
        if (DEBUG) {
            Log.v(TAG, "Destroying Inactive in " + this);
        }
        i = this.mInactiveLoaders.size() - 1;
        while (i >= 0) {
            ((LoaderInfo) this.mInactiveLoaders.valueAt(i)).destroy();
            i--;
        }
        this.mInactiveLoaders.clear();
    }

    void doReportNextStart() {
        int i = this.mLoaders.size() - 1;
        while (i >= 0) {
            ((LoaderInfo) this.mLoaders.valueAt(i)).mReportNextStart = true;
            i--;
        }
    }

    void doReportStart() {
        int i = this.mLoaders.size() - 1;
        while (i >= 0) {
            ((LoaderInfo) this.mLoaders.valueAt(i)).reportStart();
            i--;
        }
    }

    void doRetain() {
        if (DEBUG) {
            Log.v(TAG, "Retaining in " + this);
        }
        if (this.mStarted) {
            this.mRetaining = true;
            this.mStarted = false;
            int i = this.mLoaders.size() - 1;
            while (i >= 0) {
                ((LoaderInfo) this.mLoaders.valueAt(i)).retain();
                i--;
            }
        } else {
            RuntimeException e = new RuntimeException("here");
            e.fillInStackTrace();
            Log.w(TAG, "Called doRetain when not started: " + this, e);
        }
    }

    void doStart() {
        if (DEBUG) {
            Log.v(TAG, "Starting in " + this);
        }
        if (this.mStarted) {
            RuntimeException e = new RuntimeException("here");
            e.fillInStackTrace();
            Log.w(TAG, "Called doStart when already started: " + this, e);
        } else {
            this.mStarted = true;
            int i = this.mLoaders.size() - 1;
            while (i >= 0) {
                ((LoaderInfo) this.mLoaders.valueAt(i)).start();
                i--;
            }
        }
    }

    void doStop() {
        if (DEBUG) {
            Log.v(TAG, "Stopping in " + this);
        }
        if (this.mStarted) {
            int i = this.mLoaders.size() - 1;
            while (i >= 0) {
                ((LoaderInfo) this.mLoaders.valueAt(i)).stop();
                i--;
            }
            this.mStarted = false;
        } else {
            RuntimeException e = new RuntimeException("here");
            e.fillInStackTrace();
            Log.w(TAG, "Called doStop when not started: " + this, e);
        }
    }

    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        String innerPrefix;
        int i;
        if (this.mLoaders.size() > 0) {
            writer.print(prefix);
            writer.println("Active Loaders:");
            innerPrefix = prefix + "    ";
            i = 0;
            while (i < this.mLoaders.size()) {
                LoaderInfo li = (LoaderInfo) this.mLoaders.valueAt(i);
                writer.print(prefix);
                writer.print("  #");
                writer.print(this.mLoaders.keyAt(i));
                writer.print(": ");
                writer.println(li.toString());
                li.dump(innerPrefix, fd, writer, args);
                i++;
            }
        }
        if (this.mInactiveLoaders.size() > 0) {
            writer.print(prefix);
            writer.println("Inactive Loaders:");
            innerPrefix = prefix + "    ";
            i = 0;
            while (i < this.mInactiveLoaders.size()) {
                li = this.mInactiveLoaders.valueAt(i);
                writer.print(prefix);
                writer.print("  #");
                writer.print(this.mInactiveLoaders.keyAt(i));
                writer.print(": ");
                writer.println(li.toString());
                li.dump(innerPrefix, fd, writer, args);
                i++;
            }
        }
    }

    void finishRetain() {
        if (this.mRetaining) {
            if (DEBUG) {
                Log.v(TAG, "Finished Retaining in " + this);
            }
            this.mRetaining = false;
            int i = this.mLoaders.size() - 1;
            while (i >= 0) {
                ((LoaderInfo) this.mLoaders.valueAt(i)).finishRetain();
                i--;
            }
        }
    }

    public <D> Loader<D> getLoader(int id) {
        if (this.mCreatingLoader) {
            throw new IllegalStateException("Called while creating a loader");
        }
        LoaderInfo loaderInfo = (LoaderInfo) this.mLoaders.get(id);
        if (loaderInfo != null) {
            return loaderInfo.mPendingLoader != null ? loaderInfo.mPendingLoader.mLoader : loaderInfo.mLoader;
        } else {
            return null;
        }
    }

    public boolean hasRunningLoaders() {
        boolean loadersRunning = false;
        int count = this.mLoaders.size();
        int i = 0;
        while (i < count) {
            LoaderInfo li = (LoaderInfo) this.mLoaders.valueAt(i);
            int i2 = (!li.mStarted || li.mDeliveredData) ? 0 : 1;
            loadersRunning |= i2;
            i++;
        }
        return loadersRunning;
    }

    public <D> Loader<D> initLoader(int id, Bundle args, LoaderCallbacks<D> callback) {
        if (this.mCreatingLoader) {
            throw new IllegalStateException("Called while creating a loader");
        }
        LoaderInfo info = (LoaderInfo) this.mLoaders.get(id);
        if (DEBUG) {
            Log.v(TAG, "initLoader in " + this + ": args=" + args);
        }
        if (info == null) {
            info = createAndInstallLoader(id, args, callback);
            if (DEBUG) {
                Log.v(TAG, "  Created new loader " + info);
            }
        } else {
            if (DEBUG) {
                Log.v(TAG, "  Re-using existing loader " + info);
            }
            info.mCallbacks = callback;
        }
        if (info.mHaveData && this.mStarted) {
            info.callOnLoadFinished(info.mLoader, info.mData);
        }
        return info.mLoader;
    }

    void installLoader(LoaderInfo info) {
        this.mLoaders.put(info.mId, info);
        if (this.mStarted) {
            info.start();
        }
    }

    public <D> Loader<D> restartLoader(int id, Bundle args, LoaderCallbacks<D> callback) {
        if (this.mCreatingLoader) {
            throw new IllegalStateException("Called while creating a loader");
        }
        LoaderInfo info = (LoaderInfo) this.mLoaders.get(id);
        if (DEBUG) {
            Log.v(TAG, "restartLoader in " + this + ": args=" + args);
        }
        if (info != null) {
            LoaderInfo inactive = (LoaderInfo) this.mInactiveLoaders.get(id);
            if (inactive == null) {
                if (DEBUG) {
                    Log.v(TAG, "  Making last loader inactive: " + info);
                }
                info.mLoader.abandon();
                this.mInactiveLoaders.put(id, info);
            } else if (info.mHaveData) {
                if (DEBUG) {
                    Log.v(TAG, "  Removing last inactive loader: " + info);
                }
                inactive.mDeliveredData = false;
                inactive.destroy();
                info.mLoader.abandon();
                this.mInactiveLoaders.put(id, info);
            } else if (info.mStarted) {
                if (info.mPendingLoader != null) {
                    if (DEBUG) {
                        Log.v(TAG, "  Removing pending loader: " + info.mPendingLoader);
                    }
                    info.mPendingLoader.destroy();
                    info.mPendingLoader = null;
                }
                if (DEBUG) {
                    Log.v(TAG, "  Enqueuing as new pending loader");
                }
                info.mPendingLoader = createLoader(id, args, callback);
                return info.mPendingLoader.mLoader;
            } else {
                if (DEBUG) {
                    Log.v(TAG, "  Current loader is stopped; replacing");
                }
                this.mLoaders.put(id, null);
                info.destroy();
            }
        }
        return createAndInstallLoader(id, args, callback).mLoader;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("LoaderManager{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(" in ");
        DebugUtils.buildShortClassTag(this.mActivity, sb);
        sb.append("}}");
        return sb.toString();
    }

    void updateActivity(FragmentActivity activity) {
        this.mActivity = activity;
    }
}