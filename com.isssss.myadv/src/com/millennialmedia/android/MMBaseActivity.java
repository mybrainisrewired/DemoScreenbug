package com.millennialmedia.android;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import java.io.File;

class MMBaseActivity {
    MMActivity activity;

    MMBaseActivity() {
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        return this.activity.dispatchTouchEventSuper(ev);
    }

    public void finish() {
        this.activity.finishSuper();
    }

    public File getCacheDir() {
        return this.activity.getCacheDir();
    }

    public ContentResolver getContentResolver() {
        return this.activity.getContentResolver();
    }

    public Intent getIntent() {
        return this.activity.getIntent();
    }

    public Object getLastNonConfigurationInstance() {
        return this.activity.getLastNonConfigurationInstance();
    }

    public Object getSystemService(String name) {
        return this.activity.getSystemService(name);
    }

    public Window getWindow() {
        return this.activity.getWindow();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.activity.onActivityResultSuper(requestCode, resultCode, data);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        this.activity.onConfigurationChangedSuper(newConfig);
    }

    protected void onCreate(Bundle savedInstanceState) {
        this.activity.onCreateSuper(savedInstanceState);
    }

    protected void onDestroy() {
        this.activity.onDestroySuper();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return this.activity.onKeyDownSuper(keyCode, event);
    }

    protected void onPause() {
        this.activity.onPauseSuper();
    }

    protected void onRestart() {
        this.activity.onRestartSuper();
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        this.activity.onRestoreInstanceStateSuper(savedInstanceState);
    }

    protected void onResume() {
        this.activity.onResumeSuper();
    }

    public Object onRetainNonConfigurationInstance() {
        return this.activity.onRetainNonConfigurationInstanceSuper();
    }

    protected void onSaveInstanceState(Bundle outState) {
        this.activity.onSaveInstanceStateSuper(outState);
    }

    protected void onStart() {
        this.activity.onStartSuper();
    }

    protected void onStop() {
        this.activity.onStopSuper();
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        this.activity.onWindowFocusChangedSuper(hasFocus);
    }

    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        return this.activity.registerReceiver(receiver, filter);
    }

    public final boolean requestWindowFeature(int featureId) {
        return this.activity.requestWindowFeature(featureId);
    }

    public final void runOnUiThread(Runnable action) {
        this.activity.runOnUiThread(action);
    }

    public void setContentView(View view) {
        this.activity.setContentView(view);
    }

    public void setRequestedOrientation(int requestedOrientation) {
        this.activity.setRequestedOrientation(requestedOrientation);
    }

    public final void setResult(int resultCode) {
        this.activity.setResult(resultCode);
    }

    public void setTheme(int resid) {
        this.activity.setTheme(resid);
    }

    public void startActivity(Intent intent) {
        this.activity.startActivity(intent);
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        this.activity.startActivityForResult(intent, requestCode);
    }

    public void unregisterReceiver(BroadcastReceiver receiver) {
        this.activity.unregisterReceiver(receiver);
    }
}