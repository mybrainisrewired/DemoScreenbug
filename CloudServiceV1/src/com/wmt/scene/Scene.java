package com.wmt.scene;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.wmt.opengl.GLContext;

public abstract class Scene {
    public static final int FLAG_HIDE_ACTION_BAR = 1;
    public static final int FLAG_HIDE_STATUS_BAR = 2;
    public static final int FLING_LEFT = 10;
    public static final int FLING_RIGHT = 11;
    public static final int NO_FLING = 9;
    protected Activity mActivity;
    protected Bundle mData;
    private boolean mDestroyed;
    protected int mFlags;
    protected ResultEntry mReceivedResults;
    protected ResultEntry mResult;

    protected static class ResultEntry {
        ResultEntry next;
        public int requestCode;
        public int resultCode;
        public Intent resultData;

        protected ResultEntry() {
            this.resultCode = 0;
        }
    }

    protected Scene() {
        this.mDestroyed = false;
    }

    void destroy() {
        this.mDestroyed = true;
        onDestroy();
    }

    public Bundle getData() {
        return this.mData;
    }

    void initialize(Activity activity, Bundle data) {
        this.mActivity = activity;
        this.mData = data;
    }

    boolean isDestroyed() {
        return this.mDestroyed;
    }

    protected void onBackPressed(SceneManager manager) {
        if (manager != null) {
            manager.finishScene(this);
        }
    }

    protected void onConfigurationChanged(Configuration config) {
    }

    protected void onCreate(GLContext glContext, Bundle data, Bundle storedState) {
    }

    protected boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    protected void onDestroy() {
    }

    protected void onFling(int direction) {
    }

    protected void onMountDevice(int dev, int state) {
    }

    protected boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    protected void onPause(boolean goingToBeDestroy) {
    }

    protected void onResume() {
    }

    protected void onSaveState(Bundle outState) {
    }

    protected void onSceneResult(int requestCode, int resultCode, Intent data) {
    }

    public void refreshMediaSet(boolean isStorageEject) {
    }

    void resume(GLContext glContext) {
        if (VERSION.SDK_INT >= 11) {
            glContext.postUIThreadRunnable(new Runnable() {
                public void run() {
                    Scene.this.mActivity.invalidateOptionsMenu();
                }
            }, 0);
        }
        ResultEntry entry = this.mReceivedResults;
        if (entry != null) {
            this.mReceivedResults = null;
            onSceneResult(entry.requestCode, entry.resultCode, entry.resultData);
        }
        onResume();
    }

    public void setSceneResult(int resultCode, Intent data) {
        if (this.mResult != null) {
            this.mResult.resultCode = resultCode;
            this.mResult.resultData = data;
        }
    }

    public boolean supportRestore() {
        return true;
    }
}