package com.loopme;

import android.content.Context;
import android.os.Build.VERSION;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import com.loopme.utilites.Drawables;
import com.loopme.utilites.ScreenMetrics;
import com.millennialmedia.android.MMAdView;

class AdListLayout extends FrameLayout {
    private int mCloseBtnMargin;
    private ImageButton mCloseButton;
    private View mView;

    public AdListLayout(Context context) {
        super(context);
        setLayoutParams(new LayoutParams(-1, -1));
        setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        this.mCloseButton = new ImageButton(context);
        LayoutParams btn_close_params = new LayoutParams(ScreenMetrics.getInstance().getBtnCloseHeight(context), ScreenMetrics.getInstance().getBtnCloseHeight(context), 5);
        this.mCloseBtnMargin = 20;
        btn_close_params.rightMargin = this.mCloseBtnMargin;
        btn_close_params.topMargin = this.mCloseBtnMargin;
        this.mCloseButton.setLayoutParams(btn_close_params);
        if (VERSION.SDK_INT < 16) {
            this.mCloseButton.setBackgroundDrawable(Drawables.BTN_CLOSE_LIST.decodeImage(context));
        } else {
            this.mCloseButton.setBackground(Drawables.BTN_CLOSE_LIST.decodeImage(context));
        }
        this.mCloseButton.setVisibility(MMAdView.TRANSITION_RANDOM);
        addView(this.mCloseButton);
        this.mView = new View(context);
        this.mView.setLayoutParams(new LayoutParams(ScreenMetrics.getInstance().getCloseTouchAreaHeight(context) + this.mCloseBtnMargin, ScreenMetrics.getInstance().getCloseTouchAreaHeight(context) + this.mCloseBtnMargin, 5));
        this.mView.setBackgroundColor(0);
        addView(this.mView);
    }

    public View getCloseArea() {
        return this.mView;
    }

    public ImageButton getCloseButton() {
        return this.mCloseButton;
    }
}