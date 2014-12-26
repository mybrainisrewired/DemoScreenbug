package com.android.systemui.statusbar.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

public class CarrierLabel extends TextView {
    private boolean mAttached;
    private final BroadcastReceiver mIntentReceiver;

    public CarrierLabel(Context context) {
        this(context, null);
    }

    public CarrierLabel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CarrierLabel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mIntentReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if ("android.provider.Telephony.SPN_STRINGS_UPDATED".equals(intent.getAction())) {
                    CarrierLabel.this.updateNetworkName(intent.getBooleanExtra("showSpn", false), intent.getStringExtra("spn"), intent.getBooleanExtra("showPlmn", false), intent.getStringExtra("plmn"));
                }
            }
        };
        updateNetworkName(false, null, false, null);
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!this.mAttached) {
            this.mAttached = true;
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.provider.Telephony.SPN_STRINGS_UPDATED");
            getContext().registerReceiver(this.mIntentReceiver, filter, null, getHandler());
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mAttached) {
            getContext().unregisterReceiver(this.mIntentReceiver);
            this.mAttached = false;
        }
    }

    void updateNetworkName(boolean showSpn, String spn, boolean showPlmn, String plmn) {
        boolean plmnValid;
        String str;
        boolean spnValid = true;
        if (!showPlmn || TextUtils.isEmpty(plmn)) {
            plmnValid = false;
        } else {
            plmnValid = true;
        }
        if (!showSpn || TextUtils.isEmpty(spn)) {
            spnValid = false;
        }
        if (plmnValid && spnValid) {
            str = plmn + "|" + spn;
        } else if (plmnValid) {
            str = plmn;
        } else if (spnValid) {
            str = spn;
        } else {
            str = "";
        }
        setText(str);
    }
}