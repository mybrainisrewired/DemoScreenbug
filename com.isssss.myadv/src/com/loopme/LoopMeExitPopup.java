package com.loopme;

import android.app.Activity;
import android.content.Intent;

public class LoopMeExitPopup {
    private static final String LOG_TAG;
    private static Activity mActivity;
    private LoopMeInterstitial mInterstitial;

    static {
        LOG_TAG = LoopMeExitPopup.class.getSimpleName();
    }

    public LoopMeExitPopup(Activity activity, LoopMeInterstitial interstitial) {
        if (activity == null || interstitial == null) {
            throw new IllegalArgumentException("wrong parameters");
        }
        mActivity = activity;
        this.mInterstitial = interstitial;
    }

    static void finishParentActivity() {
        if (mActivity != null) {
            mActivity.finish();
        }
    }

    private void openNotificationPopup() {
        BaseLoopMeHolder.put(this.mInterstitial);
        mActivity.startActivity(new Intent(mActivity, LoopMeExitPopupActivity.class));
    }

    public void show() {
        if (Utilities.isOnline(mActivity)) {
            openNotificationPopup();
        } else {
            mActivity.finish();
        }
    }
}