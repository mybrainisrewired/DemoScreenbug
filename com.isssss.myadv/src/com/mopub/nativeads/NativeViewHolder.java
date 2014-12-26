package com.mopub.nativeads;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.mopub.common.logging.MoPubLog;
import java.util.Iterator;

class NativeViewHolder {
    TextView callToActionView;
    ImageView iconImageView;
    ImageView mainImageView;
    TextView textView;
    TextView titleView;

    private NativeViewHolder() {
    }

    private void addTextView(TextView textView, String contents) {
        if (textView == null) {
            MoPubLog.d(new StringBuilder("Attempted to add text (").append(contents).append(") to null TextView.").toString());
        } else {
            textView.setText(null);
            if (contents == null) {
                MoPubLog.d("Attempted to set TextView contents to null.");
            } else {
                textView.setText(contents);
            }
        }
    }

    static NativeViewHolder fromViewBinder(View view, ViewBinder viewBinder) {
        NativeViewHolder nativeViewHolder = new NativeViewHolder();
        try {
            nativeViewHolder.titleView = (TextView) view.findViewById(viewBinder.titleId);
            nativeViewHolder.textView = (TextView) view.findViewById(viewBinder.textId);
            nativeViewHolder.callToActionView = (TextView) view.findViewById(viewBinder.callToActionId);
            nativeViewHolder.mainImageView = (ImageView) view.findViewById(viewBinder.mainImageId);
            nativeViewHolder.iconImageView = (ImageView) view.findViewById(viewBinder.iconImageId);
            return nativeViewHolder;
        } catch (ClassCastException e) {
            MoPubLog.d("Could not cast View from id in ViewBinder to expected View type", e);
            return null;
        }
    }

    void update(NativeResponse nativeResponse) {
        addTextView(this.titleView, nativeResponse.getTitle());
        addTextView(this.textView, nativeResponse.getText());
        addTextView(this.callToActionView, nativeResponse.getCallToAction());
        nativeResponse.loadMainImage(this.mainImageView);
        nativeResponse.loadIconImage(this.iconImageView);
    }

    void updateExtras(View outerView, NativeResponse nativeResponse, ViewBinder viewBinder) {
        Iterator it = viewBinder.extras.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            View view = outerView.findViewById(((Integer) viewBinder.extras.get(key)).intValue());
            Object content = nativeResponse.getExtra(key);
            if (view instanceof ImageView) {
                ((ImageView) view).setImageDrawable(null);
                nativeResponse.loadExtrasImage(key, (ImageView) view);
            } else if (view instanceof TextView) {
                ((TextView) view).setText(null);
                if (content instanceof String) {
                    addTextView((TextView) view, (String) content);
                }
            } else {
                MoPubLog.d(new StringBuilder("View bound to ").append(key).append(" should be an instance of TextView or ImageView.").toString());
            }
        }
    }
}