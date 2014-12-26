package com.mopub.mobileads;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.logging.MoPubLog;

public class ViewGestureDetector extends GestureDetector {
    private AdAlertGestureListener mAdAlertGestureListener;
    private UserClickListener mUserClickListener;
    private final View mView;

    static interface UserClickListener {
        void onResetUserClick();

        void onUserClick();

        boolean wasClicked();
    }

    private ViewGestureDetector(Context context, View view, AdAlertGestureListener adAlertGestureListener) {
        super(context, adAlertGestureListener);
        this.mAdAlertGestureListener = adAlertGestureListener;
        this.mView = view;
        setIsLongpressEnabled(false);
    }

    public ViewGestureDetector(Context context, View view, AdConfiguration adConfiguration) {
        this(context, view, new AdAlertGestureListener(view, adConfiguration));
    }

    private boolean isMotionEventInView(MotionEvent motionEvent, View view) {
        if (motionEvent == null || view == null) {
            return false;
        }
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        return x >= 0.0f && x <= ((float) view.getWidth()) && y >= 0.0f && y <= ((float) view.getHeight());
    }

    void resetAdFlaggingGesture() {
        this.mAdAlertGestureListener.reset();
    }

    void sendTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MMAdView.TRANSITION_NONE:
                onTouchEvent(motionEvent);
            case MMAdView.TRANSITION_FADE:
                if (this.mUserClickListener != null) {
                    this.mUserClickListener.onUserClick();
                } else {
                    MoPubLog.d("View's onUserClick() is not registered.");
                }
                this.mAdAlertGestureListener.finishGestureDetection();
            case MMAdView.TRANSITION_UP:
                if (isMotionEventInView(motionEvent, this.mView)) {
                    onTouchEvent(motionEvent);
                } else {
                    resetAdFlaggingGesture();
                }
            default:
                break;
        }
    }

    @Deprecated
    void setAdAlertGestureListener(AdAlertGestureListener adAlertGestureListener) {
        this.mAdAlertGestureListener = adAlertGestureListener;
    }

    void setUserClickListener(UserClickListener listener) {
        this.mUserClickListener = listener;
    }
}