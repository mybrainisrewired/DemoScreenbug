package com.mopub.mobileads;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils.TruncateAt;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.Dips;
import com.mopub.common.util.Utils;
import com.mopub.mobileads.resource.TextDrawable;

class ToolbarWidget extends RelativeLayout {
    private static final int IMAGE_PADDING_DIPS = 5;
    private static final int IMAGE_SIDE_LENGTH_DIPS = 37;
    private static final int TEXT_PADDING_DIPS = 5;
    private final int mImagePadding;
    private final int mImageSideLength;
    private ImageView mImageView;
    private final int mTextPadding;
    private TextView mTextView;

    static class Builder {
        private final Context context;
        private String defaultText;
        private Drawable drawable;
        private int drawableAlign;
        private boolean hasDrawable;
        private boolean hasText;
        private OnTouchListener onTouchListener;
        private int textAlign;
        private int visibility;
        private float weight;
        private int widgetGravity;

        Builder(Context context) {
            this.context = context;
            this.weight = 1.0f;
            this.widgetGravity = 17;
            this.visibility = 0;
            this.textAlign = 9;
            this.drawableAlign = 11;
        }

        ToolbarWidget build() {
            return new ToolbarWidget(null);
        }

        Builder defaultText(String defaultText) {
            this.hasText = true;
            this.defaultText = defaultText;
            return this;
        }

        Builder drawable(Drawable drawable) {
            this.hasDrawable = true;
            this.drawable = drawable;
            return this;
        }

        Builder drawableAlign(int rule) {
            this.drawableAlign = rule;
            return this;
        }

        Builder hasDrawable() {
            this.hasDrawable = true;
            return this;
        }

        Builder hasText() {
            this.hasText = true;
            return this;
        }

        Builder onTouchListener(OnTouchListener onTouchListener) {
            this.onTouchListener = onTouchListener;
            return this;
        }

        Builder textAlign(int rule) {
            this.textAlign = rule;
            return this;
        }

        Builder visibility(int visibility) {
            this.visibility = visibility;
            return this;
        }

        Builder weight(float weight) {
            this.weight = weight;
            return this;
        }

        Builder widgetGravity(int widgetGravity) {
            this.widgetGravity = widgetGravity;
            return this;
        }
    }

    private ToolbarWidget(Builder builder) {
        super(builder.context);
        LayoutParams toolbarLayoutParams = new LayoutParams(0, -2, builder.weight);
        toolbarLayoutParams.gravity = builder.widgetGravity;
        setLayoutParams(toolbarLayoutParams);
        this.mTextPadding = Dips.dipsToIntPixels(5.0f, getContext());
        this.mImagePadding = Dips.dipsToIntPixels(5.0f, getContext());
        this.mImageSideLength = Dips.dipsToIntPixels(37.0f, getContext());
        setVisibility(builder.visibility);
        if (builder.hasDrawable && builder.drawable != null) {
            this.mImageView = new ImageView(getContext());
            this.mImageView.setId((int) Utils.generateUniqueId());
            RelativeLayout.LayoutParams iconLayoutParams = new RelativeLayout.LayoutParams(this.mImageSideLength, this.mImageSideLength);
            iconLayoutParams.addRule(ApiEventType.API_MRAID_GET_PLACEMENT_TYPE);
            iconLayoutParams.addRule(builder.drawableAlign);
            this.mImageView.setPadding(this.mImagePadding, this.mImagePadding, this.mImagePadding, this.mImagePadding);
            this.mImageView.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
            this.mImageView.getBackground().setAlpha(0);
            this.mImageView.setImageDrawable(builder.drawable);
            addView(this.mImageView, iconLayoutParams);
        }
        if (builder.hasText) {
            this.mTextView = new TextView(getContext());
            this.mTextView.setSingleLine();
            this.mTextView.setEllipsize(TruncateAt.END);
            this.mTextView.setText(builder.defaultText);
            RelativeLayout.LayoutParams textLayoutParams = new RelativeLayout.LayoutParams(-2, -2);
            textLayoutParams.addRule(ApiEventType.API_MRAID_GET_PLACEMENT_TYPE);
            if (this.mImageView != null) {
                textLayoutParams.addRule(0, this.mImageView.getId());
            } else {
                textLayoutParams.addRule(builder.textAlign);
            }
            this.mTextView.setPadding(this.mTextPadding, this.mTextPadding, this.mTextPadding, this.mTextPadding);
            addView(this.mTextView, textLayoutParams);
        }
        if (builder.onTouchListener != null) {
            setOnTouchListener(builder.onTouchListener);
        }
    }

    @Deprecated
    TextDrawable getImageViewDrawable() {
        return (TextDrawable) this.mImageView.getDrawable();
    }

    @Deprecated
    void setImageViewDrawable(TextDrawable drawable) {
        this.mImageView.setImageDrawable((Drawable) drawable);
    }

    void updateImageText(String text) {
        try {
            ((TextDrawable) this.mImageView.getDrawable()).updateText(text);
        } catch (Exception e) {
            MoPubLog.d("Unable to update ToolbarWidget text.");
        }
    }

    void updateText(String text) {
        if (this.mTextView != null) {
            this.mTextView.setText(text);
        }
    }
}