package com.android.systemui.usb;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.hardware.usb.IUsbManager;
import android.hardware.usb.IUsbManager.Stub;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.os.ServiceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import com.android.internal.app.AlertActivity;
import com.android.internal.app.AlertController.AlertParams;
import com.android.systemui.R;
import com.android.systemui.statusbar.CommandQueue;

public class UsbConfirmActivity extends AlertActivity implements OnClickListener, OnCheckedChangeListener {
    private static final String TAG = "UsbConfirmActivity";
    private UsbAccessory mAccessory;
    private CheckBox mAlwaysUse;
    private TextView mClearDefaultHint;
    private UsbDevice mDevice;
    private UsbDisconnectedReceiver mDisconnectedReceiver;
    private boolean mPermissionGranted;
    private ResolveInfo mResolveInfo;

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (this.mClearDefaultHint != null) {
            if (isChecked) {
                this.mClearDefaultHint.setVisibility(0);
            } else {
                this.mClearDefaultHint.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
            }
        }
    }

    public void onClick(DialogInterface dialog, int which) {
        if (which == -1) {
            try {
                IUsbManager service = Stub.asInterface(ServiceManager.getService("usb"));
                int uid = this.mResolveInfo.activityInfo.applicationInfo.uid;
                boolean alwaysUse = this.mAlwaysUse.isChecked();
                Intent intent = null;
                if (this.mDevice != null) {
                    intent = new Intent("android.hardware.usb.action.USB_DEVICE_ATTACHED");
                    intent.putExtra("device", this.mDevice);
                    service.grantDevicePermission(this.mDevice, uid);
                    if (alwaysUse) {
                        service.setDevicePackage(this.mDevice, this.mResolveInfo.activityInfo.packageName);
                    } else {
                        service.setDevicePackage(this.mDevice, null);
                    }
                } else if (this.mAccessory != null) {
                    intent = new Intent("android.hardware.usb.action.USB_ACCESSORY_ATTACHED");
                    intent.putExtra("accessory", this.mAccessory);
                    service.grantAccessoryPermission(this.mAccessory, uid);
                    if (alwaysUse) {
                        service.setAccessoryPackage(this.mAccessory, this.mResolveInfo.activityInfo.packageName);
                    } else {
                        service.setAccessoryPackage(this.mAccessory, null);
                    }
                }
                intent.addFlags(268435456);
                intent.setComponent(new ComponentName(this.mResolveInfo.activityInfo.packageName, this.mResolveInfo.activityInfo.name));
                startActivity(intent);
            } catch (Exception e) {
                Log.e(TAG, "Unable to start activity", e);
            }
        }
        finish();
    }

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Intent intent = getIntent();
        this.mDevice = (UsbDevice) intent.getParcelableExtra("device");
        this.mAccessory = (UsbAccessory) intent.getParcelableExtra("accessory");
        this.mResolveInfo = (ResolveInfo) intent.getParcelableExtra("rinfo");
        PackageManager packageManager = getPackageManager();
        String appName = this.mResolveInfo.loadLabel(packageManager).toString();
        AlertParams ap = this.mAlertParams;
        ap.mIcon = this.mResolveInfo.loadIcon(packageManager);
        ap.mTitle = appName;
        if (this.mDevice == null) {
            ap.mMessage = getString(R.string.usb_accessory_confirm_prompt, new Object[]{appName});
            this.mDisconnectedReceiver = new UsbDisconnectedReceiver(this, this.mAccessory);
        } else {
            ap.mMessage = getString(R.string.usb_device_confirm_prompt, new Object[]{appName});
            this.mDisconnectedReceiver = new UsbDisconnectedReceiver(this, this.mDevice);
        }
        ap.mPositiveButtonText = getString(17039370);
        ap.mNegativeButtonText = getString(17039360);
        ap.mPositiveButtonListener = this;
        ap.mNegativeButtonListener = this;
        ap.mView = ((LayoutInflater) getSystemService("layout_inflater")).inflate(17367079, null);
        this.mAlwaysUse = (CheckBox) ap.mView.findViewById(16908888);
        if (this.mDevice == null) {
            this.mAlwaysUse.setText(R.string.always_use_accessory);
        } else {
            this.mAlwaysUse.setText(R.string.always_use_device);
        }
        this.mAlwaysUse.setOnCheckedChangeListener(this);
        this.mClearDefaultHint = (TextView) ap.mView.findViewById(16908889);
        this.mClearDefaultHint.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
        setupAlert();
    }

    protected void onDestroy() {
        if (this.mDisconnectedReceiver != null) {
            unregisterReceiver(this.mDisconnectedReceiver);
        }
        super.onDestroy();
    }
}