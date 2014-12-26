package com.millennialmedia.android;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import java.lang.ref.WeakReference;

public class MMActivity extends Activity {
    private static final String TAG = "MMActivity";
    long creatorAdImplInternalId;
    private MMBaseActivity mmBaseActivity;
    GestureDetector tapDetector;

    private static class InterstitialGestureListener extends SimpleOnGestureListener {
        WeakReference<MMActivity> mmActivityRef;

        public InterstitialGestureListener(MMActivity mmActivity) {
            this.mmActivityRef = new WeakReference(mmActivity);
        }

        public boolean onSingleTapConfirmed(MotionEvent e) {
            MMActivity mmActivity = (MMActivity) this.mmActivityRef.get();
            if (mmActivity != null) {
                Event.adSingleTap(MMAdImplController.getAdImplWithId(mmActivity.creatorAdImplInternalId));
            }
            return false;
        }
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (this.tapDetector != null) {
            this.tapDetector.onTouchEvent(ev);
        }
        return this.mmBaseActivity != null ? this.mmBaseActivity.dispatchTouchEvent(ev) : super.dispatchTouchEvent(ev);
    }

    boolean dispatchTouchEventSuper(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    public void finish() {
        if (this.mmBaseActivity != null) {
            this.mmBaseActivity.finish();
        } else {
            Event.overlayClosed(MMAdImplController.getAdImplWithId(this.creatorAdImplInternalId));
            super.finish();
        }
    }

    public void finishSuper() {
        Event.overlayClosed(MMAdImplController.getAdImplWithId(this.creatorAdImplInternalId));
        super.finish();
    }

    protected MMBaseActivity getWrappedActivity() {
        return this.mmBaseActivity;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (this.mmBaseActivity != null) {
            this.mmBaseActivity.onActivityResult(requestCode, resultCode, data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    void onActivityResultSuper(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        if (this.mmBaseActivity != null) {
            this.mmBaseActivity.onConfigurationChanged(newConfig);
        } else {
            super.onConfigurationChanged(newConfig);
        }
    }

    void onConfigurationChangedSuper(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String className = null;
        this.creatorAdImplInternalId = getIntent().getLongExtra("internalId", -4);
        try {
            this.mmBaseActivity = (MMBaseActivity) Class.forName(getIntent().getStringExtra("class")).newInstance();
            this.mmBaseActivity.activity = this;
            this.mmBaseActivity.onCreate(savedInstanceState);
            this.tapDetector = new GestureDetector(getApplicationContext(), new InterstitialGestureListener(this));
        } catch (Exception e) {
            Exception e2 = e;
            MMLog.e(TAG, String.format("Could not start activity for %s. ", new Object[]{className}), e2);
            e2.printStackTrace();
            finish();
        }
    }

    void onCreateSuper(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void onDestroy() {
        if (this.mmBaseActivity != null) {
            this.mmBaseActivity.onDestroy();
        } else {
            super.onDestroy();
        }
    }

    void onDestroySuper() {
        super.onDestroy();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return this.mmBaseActivity != null ? this.mmBaseActivity.onKeyDown(keyCode, event) : super.onKeyDown(keyCode, event);
    }

    boolean onKeyDownSuper(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    protected void onPause() {
        if (this.mmBaseActivity != null) {
            this.mmBaseActivity.onPause();
        } else {
            super.onPause();
        }
    }

    void onPauseSuper() {
        super.onPause();
    }

    protected void onRestart() {
        if (this.mmBaseActivity != null) {
            this.mmBaseActivity.onRestart();
        } else {
            super.onRestart();
        }
    }

    void onRestartSuper() {
        super.onRestart();
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (this.mmBaseActivity != null) {
            this.mmBaseActivity.onRestoreInstanceState(savedInstanceState);
        } else {
            super.onRestoreInstanceState(savedInstanceState);
        }
    }

    void onRestoreInstanceStateSuper(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    protected void onResume() {
        if (this.mmBaseActivity != null) {
            this.mmBaseActivity.onResume();
        } else {
            super.onResume();
        }
    }

    void onResumeSuper() {
        super.onResume();
    }

    public Object onRetainNonConfigurationInstance() {
        return this.mmBaseActivity != null ? this.mmBaseActivity.onRetainNonConfigurationInstance() : super.onRetainNonConfigurationInstance();
    }

    Object onRetainNonConfigurationInstanceSuper() {
        return super.onRetainNonConfigurationInstance();
    }

    protected void onSaveInstanceState(Bundle outState) {
        if (this.mmBaseActivity != null) {
            this.mmBaseActivity.onSaveInstanceState(outState);
        } else {
            super.onSaveInstanceState(outState);
        }
    }

    void onSaveInstanceStateSuper(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    protected void onStart() {
        if (this.mmBaseActivity != null) {
            this.mmBaseActivity.onStart();
        } else {
            super.onStart();
        }
    }

    void onStartSuper() {
        super.onStart();
    }

    protected void onStop() {
        if (this.mmBaseActivity != null) {
            this.mmBaseActivity.onStop();
        } else {
            super.onStop();
        }
    }

    void onStopSuper() {
        super.onStop();
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        if (this.mmBaseActivity != null) {
            this.mmBaseActivity.onWindowFocusChanged(hasFocus);
        } else {
            super.onWindowFocusChanged(hasFocus);
        }
    }

    void onWindowFocusChangedSuper(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }
}