package com.loopme;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public final class LoopMeExitPopupActivity extends Activity {
    private static final String LOG_TAG;
    private ExipPopupLayout mLayout;

    static {
        LOG_TAG = LoopMeExitPopupActivity.class.getSimpleName();
    }

    private void setButtonListeners() {
        this.mLayout.getYesButton().setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                LoopMeInterstitial interstitial = (LoopMeInterstitial) BaseLoopMeHolder.get();
                interstitial.onLoopMeExitYesClicked(interstitial);
                LoopMeExitPopupActivity.this.finish();
                BaseLoopMe baseLoopMe = BaseLoopMeHolder.get();
            }
        });
        this.mLayout.getNoButton().setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                LoopMeInterstitial interstitial = (LoopMeInterstitial) BaseLoopMeHolder.get();
                interstitial.onLoopMeExitNoClicked(interstitial);
                LoopMeExitPopupActivity.this.finish();
                LoopMeExitPopup.finishParentActivity();
            }
        });
    }

    public void onBackPressed() {
        finish();
        LoopMeExitPopup.finishParentActivity();
    }

    public final void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        requestWindowFeature(1);
        if (this.mLayout == null) {
            this.mLayout = new ExipPopupLayout(this);
        }
        setContentView(this.mLayout);
        setButtonListeners();
    }
}