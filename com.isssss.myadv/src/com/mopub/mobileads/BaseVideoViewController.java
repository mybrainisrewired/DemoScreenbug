package com.mopub.mobileads;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.VideoView;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.mopub.common.logging.MoPubLog;

abstract class BaseVideoViewController {
    private final BaseVideoViewControllerListener mBaseVideoViewControllerListener;
    private long mBroadcastIdentifier;
    private final Context mContext;
    private final RelativeLayout mLayout;

    static interface BaseVideoViewControllerListener {
        void onFinish();

        void onSetContentView(View view);

        void onSetRequestedOrientation(int i);

        void onStartActivityForResult(Class<? extends Activity> cls, int i, Bundle bundle);
    }

    BaseVideoViewController(Context context, long broadcastIdentifier, BaseVideoViewControllerListener baseVideoViewControllerListener) {
        this.mContext = context.getApplicationContext();
        this.mBroadcastIdentifier = broadcastIdentifier;
        this.mBaseVideoViewControllerListener = baseVideoViewControllerListener;
        this.mLayout = new RelativeLayout(this.mContext);
    }

    boolean backButtonEnabled() {
        return true;
    }

    void broadcastAction(String action) {
        EventForwardingBroadcastReceiver.broadcastAction(this.mContext, this.mBroadcastIdentifier, action);
    }

    BaseVideoViewControllerListener getBaseVideoViewControllerListener() {
        return this.mBaseVideoViewControllerListener;
    }

    Context getContext() {
        return this.mContext;
    }

    ViewGroup getLayout() {
        return this.mLayout;
    }

    abstract VideoView getVideoView();

    void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    void onCreate() {
        LayoutParams adViewLayout = new LayoutParams(-1, -2);
        adViewLayout.addRule(ApiEventType.API_MRAID_CLOSE);
        this.mLayout.addView(getVideoView(), 0, adViewLayout);
        this.mBaseVideoViewControllerListener.onSetContentView(this.mLayout);
    }

    abstract void onDestroy();

    abstract void onPause();

    abstract void onResume();

    void videoClicked() {
        broadcastAction("com.mopub.action.interstitial.click");
    }

    void videoCompleted(boolean shouldFinish) {
        if (shouldFinish) {
            this.mBaseVideoViewControllerListener.onFinish();
        }
    }

    void videoError(boolean shouldFinish) {
        MoPubLog.d("Error: video can not be played.");
        broadcastAction("com.mopub.action.interstitial.fail");
        if (shouldFinish) {
            this.mBaseVideoViewControllerListener.onFinish();
        }
    }
}