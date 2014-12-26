package com.loopme;

import android.app.Activity;
import com.mopub.common.Preconditions;
import com.unity3d.player.UnityPlayer;

public class LoopMeInterstitialUnity implements LoopMeInterstitialListener {
    private static final String LOG_TAG;
    private volatile Activity mActivity;
    private volatile LoopMeInterstitial mLoopMeInterstitial;

    static {
        LOG_TAG = LoopMeInterstitialUnity.class.getSimpleName();
    }

    public LoopMeInterstitialUnity(Activity activity) {
        if (activity == null) {
            throw new IllegalArgumentException("activity is null");
        }
        this.mActivity = activity;
    }

    public static void resumeGame() {
        UnityPlayer.UnitySendMessage("LoopMe_Android", "DoResumeGame", Preconditions.EMPTY_ARGUMENTS);
    }

    public void load() {
        this.mActivity.runOnUiThread(new Runnable() {
            public void run() {
                if (LoopMeInterstitialUnity.this.mLoopMeInterstitial != null) {
                    LoopMeInterstitialUnity.this.mLoopMeInterstitial.load();
                }
            }
        });
    }

    public void onLoopMeExitNoClicked(LoopMeInterstitial interstitial) {
        Utilities.log(LOG_TAG, "onLoopMeExitNoClicked", LogLevel.INFO);
        UnityPlayer.UnitySendMessage("LoopMeEventsManager", "exitNoClickedNotification", Preconditions.EMPTY_ARGUMENTS);
    }

    public void onLoopMeExitYesClicked(LoopMeInterstitial interstitial) {
        Utilities.log(LOG_TAG, "onLoopMeExitYesClicked", LogLevel.INFO);
        UnityPlayer.UnitySendMessage("LoopMeEventsManager", "exitYesClickedNotification", Preconditions.EMPTY_ARGUMENTS);
    }

    public void onLoopMeInterstitialClicked(LoopMeInterstitial interstitial) {
        Utilities.log(LOG_TAG, "onLoopMeInterstitialClicked", LogLevel.INFO);
        UnityPlayer.UnitySendMessage("LoopMeEventsManager", "interstitialDidReceiveTapNotification", Preconditions.EMPTY_ARGUMENTS);
    }

    public void onLoopMeInterstitialExpired(LoopMeInterstitial interstitial) {
        Utilities.log(LOG_TAG, "onLoopMeInterstitialExpired", LogLevel.INFO);
        this.mLoopMeInterstitial.setReadyStatus(false);
        UnityPlayer.UnitySendMessage("LoopMeEventsManager", "interstitialDidExpiredNotification", Preconditions.EMPTY_ARGUMENTS);
    }

    public void onLoopMeInterstitialHide(LoopMeInterstitial interstitial) {
        Utilities.log(LOG_TAG, "onLoopMeInterstitialHide", LogLevel.INFO);
        this.mLoopMeInterstitial.setReadyStatus(false);
        UnityPlayer.UnitySendMessage("LoopMeEventsManager", "interstitialDidDisappearNotification", Preconditions.EMPTY_ARGUMENTS);
    }

    public void onLoopMeInterstitialLeaveApp(LoopMeInterstitial interstitial) {
        Utilities.log(LOG_TAG, "onLoopMeInterstitialLeaveApp", LogLevel.INFO);
        UnityPlayer.UnitySendMessage("LoopMeEventsManager", "interstitialWillLeaveApplicationNotification", Preconditions.EMPTY_ARGUMENTS);
    }

    public void onLoopMeInterstitialLoadFail(LoopMeInterstitial interstitial, LoopMeError error) {
        Utilities.log(LOG_TAG, "onLoopMeInterstitialLoadFail", LogLevel.INFO);
        this.mLoopMeInterstitial.setReadyStatus(false);
        UnityPlayer.UnitySendMessage("LoopMeEventsManager", "interstitialDidFailToLoadAdNotification", Preconditions.EMPTY_ARGUMENTS);
    }

    public void onLoopMeInterstitialLoadSuccess(LoopMeInterstitial interstitial) {
        Utilities.log(LOG_TAG, "onLoopMeInterstitialLoadSuccess", LogLevel.INFO);
        this.mLoopMeInterstitial.setReadyStatus(true);
        UnityPlayer.UnitySendMessage("LoopMeEventsManager", "interstitialDidLoadNotification", Preconditions.EMPTY_ARGUMENTS);
    }

    public void onLoopMeInterstitialShow(LoopMeInterstitial interstitial) {
        Utilities.log(LOG_TAG, "onLoopMeInterstitialShow", LogLevel.INFO);
        UnityPlayer.UnitySendMessage("LoopMeEventsManager", "interstitialDidAppearNotification", Preconditions.EMPTY_ARGUMENTS);
    }

    public void setAppKey(String appKey) {
        Utilities.log(LOG_TAG, new StringBuilder("setAppKey = ").append(appKey).toString(), LogLevel.DEBUG);
        if (appKey == null || appKey == Preconditions.EMPTY_ARGUMENTS) {
            Utilities.log(LOG_TAG, "wrong app key", LogLevel.ERROR);
        } else {
            this.mLoopMeInterstitial = new LoopMeInterstitial(this.mActivity, appKey);
            this.mLoopMeInterstitial.addListener(this);
        }
    }

    public void show() {
        this.mActivity.runOnUiThread(new Runnable() {
            public void run() {
                if (LoopMeInterstitialUnity.this.mLoopMeInterstitial != null) {
                    LoopMeInterstitialUnity.this.mLoopMeInterstitial.show();
                }
            }
        });
    }
}