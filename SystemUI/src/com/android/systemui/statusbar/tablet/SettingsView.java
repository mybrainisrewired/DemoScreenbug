package com.android.systemui.statusbar.tablet;

import android.app.StatusBarManager;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import com.android.systemui.R;
import com.android.systemui.statusbar.policy.AirplaneModeController;
import com.android.systemui.statusbar.policy.AutoRotateController;
import com.android.systemui.statusbar.policy.AutoRotateController.RotationLockCallbacks;
import com.android.systemui.statusbar.policy.BrightnessController;
import com.android.systemui.statusbar.policy.DoNotDisturbController;
import com.android.systemui.statusbar.policy.ToggleSlider;

public class SettingsView extends LinearLayout implements OnClickListener {
    static final String TAG = "SettingsView";
    AirplaneModeController mAirplane;
    BrightnessController mBrightness;
    DoNotDisturbController mDoNotDisturb;
    AutoRotateController mRotate;
    View mRotationLockContainer;
    View mRotationLockSeparator;

    public SettingsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private StatusBarManager getStatusBarManager() {
        return (StatusBarManager) getContext().getSystemService("statusbar");
    }

    private void onClickNetwork() {
        getContext().startActivity(new Intent("android.settings.WIFI_SETTINGS").setFlags(268435456));
        getStatusBarManager().collapse();
    }

    private void onClickSettings() {
        getContext().startActivity(new Intent("android.settings.SETTINGS").setFlags(268435456));
        getStatusBarManager().collapse();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.network:
                onClickNetwork();
            case R.id.settings:
                onClickSettings();
            default:
                break;
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mAirplane.release();
        this.mDoNotDisturb.release();
        this.mRotate.release();
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        Context context = getContext();
        this.mAirplane = new AirplaneModeController(context, (CompoundButton) findViewById(R.id.airplane_checkbox));
        findViewById(R.id.network).setOnClickListener(this);
        this.mRotationLockContainer = findViewById(R.id.rotate);
        this.mRotationLockSeparator = findViewById(R.id.rotate_separator);
        this.mRotate = new AutoRotateController(context, (CompoundButton) findViewById(R.id.rotate_checkbox), new RotationLockCallbacks() {
            public void setRotationLockControlVisibility(boolean show) {
                int i;
                int i2 = 0;
                View view = SettingsView.this.mRotationLockContainer;
                if (show) {
                    i = 0;
                } else {
                    i = 8;
                }
                view.setVisibility(i);
                View view2 = SettingsView.this.mRotationLockSeparator;
                if (!show) {
                    i2 = 8;
                }
                view2.setVisibility(i2);
            }
        });
        this.mBrightness = new BrightnessController(context, (ToggleSlider) findViewById(R.id.brightness));
        this.mDoNotDisturb = new DoNotDisturbController(context, (CompoundButton) findViewById(R.id.do_not_disturb_checkbox));
        findViewById(R.id.settings).setOnClickListener(this);
    }
}