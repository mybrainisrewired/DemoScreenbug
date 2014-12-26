package com.android.systemui.usb;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.hardware.usb.UsbAccessory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import com.android.internal.app.AlertActivity;
import com.android.internal.app.AlertController.AlertParams;
import com.android.systemui.R;

public class UsbAccessoryUriActivity extends AlertActivity implements OnClickListener {
    private static final String TAG = "UsbAccessoryUriActivity";
    private UsbAccessory mAccessory;
    private Uri mUri;

    public void onClick(DialogInterface dialog, int which) {
        if (which == -1) {
            Intent intent = new Intent("android.intent.action.VIEW", this.mUri);
            intent.addCategory("android.intent.category.BROWSABLE");
            intent.addFlags(268435456);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Log.e(TAG, "startActivity failed for " + this.mUri);
            }
        }
        finish();
    }

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Intent intent = getIntent();
        this.mAccessory = (UsbAccessory) intent.getParcelableExtra("accessory");
        String uriString = intent.getStringExtra("uri");
        this.mUri = uriString == null ? null : Uri.parse(uriString);
        if (this.mUri == null) {
            Log.e(TAG, "could not parse Uri " + uriString);
            finish();
        } else {
            String scheme = this.mUri.getScheme();
            if ("http".equals(scheme) || "https".equals(scheme)) {
                AlertParams ap = this.mAlertParams;
                ap.mTitle = this.mAccessory.getDescription();
                if (ap.mTitle == null || ap.mTitle.length() == 0) {
                    ap.mTitle = getString(R.string.title_usb_accessory);
                }
                ap.mMessage = getString(R.string.usb_accessory_uri_prompt, new Object[]{this.mUri});
                ap.mPositiveButtonText = getString(R.string.label_view);
                ap.mNegativeButtonText = getString(17039360);
                ap.mPositiveButtonListener = this;
                ap.mNegativeButtonListener = this;
                setupAlert();
            } else {
                Log.e(TAG, "Uri not http or https: " + this.mUri);
                finish();
            }
        }
    }
}