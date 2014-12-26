package com.mopub.nativeads;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.mopub.common.util.Dips;
import com.mopub.common.util.Views;

class SpinningProgressView extends ViewGroup {
    private final ProgressBar mProgressBar;
    private int mProgressIndicatorRadius;

    SpinningProgressView(Context context) {
        super(context);
        LayoutParams params = new LayoutParams(-1, -1);
        params.gravity = 17;
        setLayoutParams(params);
        setVisibility(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES);
        setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        getBackground().setAlpha(180);
        this.mProgressBar = new ProgressBar(context);
        this.mProgressIndicatorRadius = Dips.asIntPixels(25.0f, getContext());
        this.mProgressBar.setIndeterminate(true);
        addView(this.mProgressBar);
    }

    boolean addToRoot(View view) {
        if (view == null) {
            return false;
        }
        View rootView = view.getRootView();
        if (rootView == null || !(rootView instanceof ViewGroup)) {
            return false;
        }
        ViewGroup rootViewGroup = (ViewGroup) rootView;
        setVisibility(0);
        setMeasuredDimension(rootView.getWidth(), rootView.getHeight());
        rootViewGroup.addView(this);
        forceLayout();
        return true;
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed) {
            int centerX = (left + right) / 2;
            int centerY = (top + bottom) / 2;
            this.mProgressBar.layout(centerX - this.mProgressIndicatorRadius, centerY - this.mProgressIndicatorRadius, this.mProgressIndicatorRadius + centerX, this.mProgressIndicatorRadius + centerY);
        }
    }

    boolean removeFromRoot() {
        Views.removeFromParent(this);
        setVisibility(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES);
        return true;
    }
}