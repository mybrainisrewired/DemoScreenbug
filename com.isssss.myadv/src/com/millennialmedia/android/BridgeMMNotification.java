package com.millennialmedia.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Vibrator;
import com.inmobi.monetization.internal.InvalidManifestConfigException;
import com.isssss.myadv.dao.BannerInfoTable;
import java.util.Map;
import java.util.concurrent.Callable;

class BridgeMMNotification extends MMJSObject implements OnClickListener {
    private String ALERT;
    private String VIBRATE;
    private int index;

    class AnonymousClass_1 implements Callable<MMJSResponse> {
        final /* synthetic */ Map val$arguments;

        AnonymousClass_1(Map map) {
            this.val$arguments = map;
        }

        public MMJSResponse call() {
            MMWebView mmWebView = (MMWebView) BridgeMMNotification.this.mmWebViewRef.get();
            if (mmWebView != null) {
                Activity activity = mmWebView.getActivity();
                Map<String, String> finalArguments = this.val$arguments;
                if (activity != null) {
                    if (!activity.isFinishing()) {
                        AlertDialog alertDialog = new Builder(activity).create();
                        if (finalArguments.containsKey(BannerInfoTable.COLUMN_TITLE)) {
                            alertDialog.setTitle((CharSequence) finalArguments.get(BannerInfoTable.COLUMN_TITLE));
                        }
                        if (finalArguments.containsKey("message")) {
                            alertDialog.setMessage((CharSequence) finalArguments.get("message"));
                        }
                        if (finalArguments.containsKey("cancelButton")) {
                            alertDialog.setButton(InvalidManifestConfigException.MISSING_ACTIVITY_DECLARATION, (CharSequence) finalArguments.get("cancelButton"), BridgeMMNotification.this);
                        }
                        if (finalArguments.containsKey("buttons")) {
                            String[] buttons = ((String) finalArguments.get("buttons")).split(",");
                            if (buttons.length > 0) {
                                alertDialog.setButton(InvalidManifestConfigException.MISSING_CONFIG_CHANGES, buttons[0], BridgeMMNotification.this);
                            }
                            if (buttons.length > 1) {
                                alertDialog.setButton(-1, buttons[1], BridgeMMNotification.this);
                            }
                        }
                        alertDialog.show();
                    }
                    MMJSResponse response = new MMJSResponse();
                    response.result = 1;
                    response.response = Integer.valueOf(BridgeMMNotification.this.index);
                    return response;
                }
            }
            return null;
        }
    }

    BridgeMMNotification() {
        this.ALERT = "alert";
        this.VIBRATE = "vibrate";
    }

    public synchronized MMJSResponse alert(Map<String, String> arguments) {
        return runOnUiThreadFuture(new AnonymousClass_1(arguments));
    }

    MMJSResponse executeCommand(String name, Map<String, String> arguments) {
        if (this.ALERT.equals(name)) {
            return alert(arguments);
        }
        return this.VIBRATE.equals(name) ? vibrate(arguments) : null;
    }

    public synchronized void onClick(DialogInterface dialog, int which) {
        if (which == -2) {
            this.index = 0;
        }
        if (which == -3) {
            this.index = 1;
        }
        if (which == -1) {
            this.index = 2;
        }
        dialog.cancel();
        notify();
    }

    public MMJSResponse vibrate(Map<String, String> arguments) {
        Context context = (Context) this.contextRef.get();
        long time = 0;
        if (arguments.containsKey("duration")) {
            time = (long) (((double) Float.parseFloat((String) arguments.get("duration"))) * 1000.0d);
        }
        if (context == null || time <= 0) {
            return null;
        }
        if (context.getPackageManager().checkPermission("android.permission.VIBRATE", context.getPackageName()) != 0) {
            return MMJSResponse.responseWithError("The required permissions to vibrate are not set.");
        }
        ((Vibrator) context.getSystemService("vibrator")).vibrate(time);
        return MMJSResponse.responseWithSuccess("Vibrating for " + time);
    }
}