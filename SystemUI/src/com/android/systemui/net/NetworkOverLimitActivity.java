package com.android.systemui.net;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.net.INetworkPolicyManager.Stub;
import android.net.NetworkTemplate;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Slog;
import com.android.systemui.R;
import com.android.systemui.recent.RecentsCallback;
import com.android.systemui.statusbar.CommandQueue;

public class NetworkOverLimitActivity extends Activity {
    private static final String TAG = "NetworkOverLimitActivity";

    class AnonymousClass_1 implements OnClickListener {
        final /* synthetic */ NetworkTemplate val$template;

        AnonymousClass_1(NetworkTemplate networkTemplate) {
            this.val$template = networkTemplate;
        }

        public void onClick(DialogInterface dialog, int which) {
            NetworkOverLimitActivity.this.snoozePolicy(this.val$template);
        }
    }

    private static int getLimitedDialogTitleForTemplate(NetworkTemplate template) {
        switch (template.getMatchRule()) {
            case CommandQueue.FLAG_EXCLUDE_SEARCH_PANEL:
                return R.string.data_usage_disabled_dialog_mobile_title;
            case CommandQueue.FLAG_EXCLUDE_RECENTS_PANEL:
                return R.string.data_usage_disabled_dialog_3g_title;
            case RecentsCallback.SWIPE_DOWN:
                return R.string.data_usage_disabled_dialog_4g_title;
            default:
                return R.string.data_usage_disabled_dialog_title;
        }
    }

    private void snoozePolicy(NetworkTemplate template) {
        try {
            Stub.asInterface(ServiceManager.getService("netpolicy")).snoozeLimit(template);
        } catch (RemoteException e) {
            Slog.w(TAG, "problem snoozing network policy", e);
        }
    }

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        NetworkTemplate template = (NetworkTemplate) getIntent().getParcelableExtra("android.net.NETWORK_TEMPLATE");
        Builder builder = new Builder(this);
        builder.setTitle(getLimitedDialogTitleForTemplate(template));
        builder.setMessage(R.string.data_usage_disabled_dialog);
        builder.setPositiveButton(17039370, null);
        builder.setNegativeButton(R.string.data_usage_disabled_dialog_enable, new AnonymousClass_1(template));
        Dialog dialog = builder.create();
        dialog.setOnDismissListener(new OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                NetworkOverLimitActivity.this.finish();
            }
        });
        dialog.getWindow().setType(2003);
        dialog.show();
    }
}