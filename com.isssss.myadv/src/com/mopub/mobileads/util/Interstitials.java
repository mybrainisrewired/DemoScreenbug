package com.mopub.mobileads.util;

import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

public class Interstitials {
    private Interstitials() {
    }

    public static boolean addCloseEventRegion(ViewGroup viewGroup, LayoutParams layoutParams, OnClickListener onClickListener) {
        if (viewGroup == null || viewGroup.getContext() == null) {
            return false;
        }
        Button closeEventRegion = new Button(viewGroup.getContext());
        closeEventRegion.setVisibility(0);
        closeEventRegion.setBackgroundColor(0);
        closeEventRegion.setOnClickListener(onClickListener);
        viewGroup.addView(closeEventRegion, layoutParams);
        return true;
    }
}