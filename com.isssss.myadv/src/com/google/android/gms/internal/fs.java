package com.google.android.gms.internal;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;
import com.google.android.gms.R;
import com.millennialmedia.android.MMAdView;

public final class fs extends Button {
    public fs(Context context) {
        this(context, null);
    }

    public fs(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 16842824);
    }

    private int b(int i, int i2, int i3) {
        switch (i) {
            case MMAdView.TRANSITION_NONE:
                return i2;
            case MMAdView.TRANSITION_FADE:
                return i3;
            default:
                throw new IllegalStateException("Unknown color scheme: " + i);
        }
    }

    private void b(Resources resources, int i, int i2) {
        int b;
        switch (i) {
            case MMAdView.TRANSITION_NONE:
            case MMAdView.TRANSITION_FADE:
                b = b(i2, R.drawable.common_signin_btn_text_dark, R.drawable.common_signin_btn_text_light);
                break;
            case MMAdView.TRANSITION_UP:
                b = b(i2, R.drawable.common_signin_btn_icon_dark, R.drawable.common_signin_btn_icon_light);
                break;
            default:
                throw new IllegalStateException("Unknown button size: " + i);
        }
        if (b == -1) {
            throw new IllegalStateException("Could not find background resource!");
        }
        setBackgroundDrawable(resources.getDrawable(b));
    }

    private void c(Resources resources) {
        setTypeface(Typeface.DEFAULT_BOLD);
        setTextSize(14.0f);
        float f = resources.getDisplayMetrics().density;
        setMinHeight((int) (f * 48.0f + 0.5f));
        setMinWidth((int) (f * 48.0f + 0.5f));
    }

    private void c(Resources resources, int i, int i2) {
        setTextColor(resources.getColorStateList(b(i2, R.color.common_signin_btn_text_dark, R.color.common_signin_btn_text_light)));
        switch (i) {
            case MMAdView.TRANSITION_NONE:
                setText(resources.getString(R.string.common_signin_button_text));
            case MMAdView.TRANSITION_FADE:
                setText(resources.getString(R.string.common_signin_button_text_long));
            case MMAdView.TRANSITION_UP:
                setText(null);
            default:
                throw new IllegalStateException("Unknown button size: " + i);
        }
    }

    public void a(Resources resources, int i, int i2) {
        boolean z = true;
        boolean z2 = i >= 0 && i < 3;
        fq.a(z2, "Unknown button size " + i);
        if (i2 < 0 || i2 >= 2) {
            z = false;
        }
        fq.a(z, "Unknown color scheme " + i2);
        c(resources);
        b(resources, i, i2);
        c(resources, i, i2);
    }
}