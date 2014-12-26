package com.android.systemui.statusbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.android.systemui.R;
import com.android.systemui.statusbar.policy.NetworkController;
import com.android.systemui.statusbar.policy.NetworkController.SignalCluster;

public class SignalClusterView extends LinearLayout implements SignalCluster {
    static final boolean DEBUG = false;
    static final String TAG = "SignalClusterView";
    ImageView mAirplane;
    private int mAirplaneIconId;
    ImageView mEthernet;
    private int mEthernetActivityId;
    private String mEthernetDescription;
    ViewGroup mEthernetGroup;
    private int mEthernetStateId;
    private boolean mEthernetVisible;
    private boolean mIsAirplaneMode;
    ImageView mMobile;
    ImageView mMobileActivity;
    private int mMobileActivityId;
    private String mMobileDescription;
    ViewGroup mMobileGroup;
    private int mMobileStrengthId;
    ImageView mMobileType;
    private String mMobileTypeDescription;
    private int mMobileTypeId;
    private boolean mMobileVisible;
    NetworkController mNC;
    View mSpacer;
    ImageView mWifi;
    ImageView mWifiActivity;
    private int mWifiActivityId;
    private String mWifiDescription;
    ViewGroup mWifiGroup;
    private int mWifiStrengthId;
    private boolean mWifiVisible;

    public SignalClusterView(Context context) {
        this(context, null);
    }

    public SignalClusterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SignalClusterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mWifiVisible = false;
        this.mWifiStrengthId = 0;
        this.mWifiActivityId = 0;
        this.mEthernetVisible = false;
        this.mEthernetStateId = 0;
        this.mEthernetActivityId = 0;
        this.mMobileVisible = false;
        this.mMobileStrengthId = 0;
        this.mMobileActivityId = 0;
        this.mMobileTypeId = 0;
        this.mIsAirplaneMode = false;
        this.mAirplaneIconId = 0;
    }

    private void apply() {
        int i = 0;
        if (this.mWifiGroup != null) {
            ImageView imageView;
            if (this.mWifiVisible) {
                this.mWifiGroup.setVisibility(0);
                this.mWifi.setImageResource(this.mWifiStrengthId);
                this.mWifiActivity.setImageResource(this.mWifiActivityId);
                this.mWifiGroup.setContentDescription(this.mWifiDescription);
            } else {
                this.mWifiGroup.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
            }
            if (this.mEthernetVisible) {
                this.mEthernetGroup.setVisibility(0);
                this.mEthernet.setImageResource(this.mEthernetStateId);
                this.mEthernetGroup.setContentDescription(this.mEthernetDescription);
            } else {
                this.mEthernetGroup.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
            }
            if (!this.mMobileVisible || this.mIsAirplaneMode) {
                this.mMobileGroup.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
            } else {
                this.mMobileGroup.setVisibility(0);
                this.mMobile.setImageResource(this.mMobileStrengthId);
                this.mMobileActivity.setImageResource(this.mMobileActivityId);
                this.mMobileType.setImageResource(this.mMobileTypeId);
                this.mMobileGroup.setContentDescription(this.mMobileTypeDescription + " " + this.mMobileDescription);
            }
            if (this.mIsAirplaneMode) {
                this.mAirplane.setVisibility(0);
                this.mAirplane.setImageResource(this.mAirplaneIconId);
            } else {
                this.mAirplane.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
            }
            if (this.mMobileVisible) {
                if ((this.mWifiVisible || this.mEthernetVisible) && this.mIsAirplaneMode) {
                    this.mSpacer.setVisibility(CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL);
                    imageView = this.mMobileType;
                    if (this.mWifiVisible || this.mEthernetVisible) {
                        i = 8;
                    }
                    imageView.setVisibility(i);
                }
            }
            this.mSpacer.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
            imageView = this.mMobileType;
            i = 8;
            imageView.setVisibility(i);
        }
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        if (this.mWifiVisible && this.mWifiGroup.getContentDescription() != null) {
            event.getText().add(this.mWifiGroup.getContentDescription());
        }
        if (this.mMobileVisible && this.mMobileGroup.getContentDescription() != null) {
            event.getText().add(this.mMobileGroup.getContentDescription());
        }
        return super.dispatchPopulateAccessibilityEvent(event);
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mWifiGroup = (ViewGroup) findViewById(R.id.wifi_combo);
        this.mWifi = (ImageView) findViewById(R.id.wifi_signal);
        this.mWifiActivity = (ImageView) findViewById(R.id.wifi_inout);
        this.mMobileGroup = (ViewGroup) findViewById(R.id.mobile_combo);
        this.mMobile = (ImageView) findViewById(R.id.mobile_signal);
        this.mMobileActivity = (ImageView) findViewById(R.id.mobile_inout);
        this.mMobileType = (ImageView) findViewById(R.id.mobile_type);
        this.mSpacer = findViewById(R.id.spacer);
        this.mAirplane = (ImageView) findViewById(R.id.airplane);
        this.mEthernetGroup = (ViewGroup) findViewById(R.id.ethernet_combo);
        this.mEthernet = (ImageView) findViewById(R.id.ethernet_state);
        apply();
    }

    protected void onDetachedFromWindow() {
        this.mWifiGroup = null;
        this.mWifi = null;
        this.mWifiActivity = null;
        this.mMobileGroup = null;
        this.mMobile = null;
        this.mMobileActivity = null;
        this.mMobileType = null;
        this.mSpacer = null;
        this.mAirplane = null;
        super.onDetachedFromWindow();
    }

    public void setEthernetIndicators(boolean visible, int strengthIcon, int activityIcon, String contentDescription) {
        this.mEthernetVisible = visible;
        this.mEthernetStateId = strengthIcon;
        this.mEthernetActivityId = activityIcon;
        this.mEthernetDescription = contentDescription;
        apply();
    }

    public void setIsAirplaneMode(boolean is, int airplaneIconId) {
        this.mIsAirplaneMode = is;
        this.mAirplaneIconId = airplaneIconId;
        apply();
    }

    public void setMobileDataIndicators(boolean visible, int strengthIcon, int activityIcon, int typeIcon, String contentDescription, String typeContentDescription) {
        this.mMobileVisible = visible;
        this.mMobileStrengthId = strengthIcon;
        this.mMobileActivityId = activityIcon;
        this.mMobileTypeId = typeIcon;
        this.mMobileDescription = contentDescription;
        this.mMobileTypeDescription = typeContentDescription;
        apply();
    }

    public void setNetworkController(NetworkController nc) {
        this.mNC = nc;
    }

    public void setWifiIndicators(boolean visible, int strengthIcon, int activityIcon, String contentDescription) {
        this.mWifiVisible = visible;
        this.mWifiStrengthId = strengthIcon;
        this.mWifiActivityId = activityIcon;
        this.mWifiDescription = contentDescription;
        apply();
    }
}