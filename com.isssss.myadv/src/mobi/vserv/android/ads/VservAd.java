package mobi.vserv.android.ads;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.inmobi.commons.analytics.db.AnalyticsEvent;
import com.inmobi.monetization.internal.InvalidManifestConfigException;
import com.isssss.myadv.dao.AdvConfigTable;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.Preconditions;
import mobi.vserv.org.ormma.controller.Defines;

public class VservAd {
    private AdClickReceiver adClickReceiver;
    private AdDialog adDialog;
    private Context context;
    private VservAdController renderAdController;
    String zoneId;

    protected class AdClickReceiver extends BroadcastReceiver {
        protected AdClickReceiver() {
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onReceive(android.content.Context r4_context, android.content.Intent r5_intent) {
            throw new UnsupportedOperationException("Method not decompiled: mobi.vserv.android.ads.VservAd.AdClickReceiver.onReceive(android.content.Context, android.content.Intent):void");
            /*
            r3 = this;
            r0 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;
            if (r0 == 0) goto L_0x000b;
        L_0x0004:
            r0 = "sample";
            r1 = "Received Dismiss Broadcast";
            android.util.Log.i(r0, r1);
        L_0x000b:
            r0 = "clickToVideo";
            r0 = r5.hasExtra(r0);
            if (r0 == 0) goto L_0x005a;
        L_0x0013:
            r0 = "vserv";
            r1 = new java.lang.StringBuilder;
            r2 = "onReceive 1::";
            r1.<init>(r2);
            r2 = "clickToVideo";
            r2 = r5.hasExtra(r2);
            r1 = r1.append(r2);
            r1 = r1.toString();
            android.util.Log.i(r0, r1);
            r0 = mobi.vserv.android.ads.VservAd.this;
            r0 = r0.adDialog;
            r0 = r0.countDownTimer;
            if (r0 == 0) goto L_0x0059;
        L_0x0039:
            r0 = mobi.vserv.android.ads.VservAd.this;
            r0 = r0.adDialog;
            r0 = r0.countDownTimer;
            r0.onFinish();
            r0 = mobi.vserv.android.ads.VservAd.this;
            r0 = r0.adDialog;
            r1 = 0;
            r0.countDownTimer = r1;
            r0 = mobi.vserv.android.ads.VservAd.this;
            r0 = r0.adDialog;
            r0.setCloseButton();
        L_0x0059:
            return;
        L_0x005a:
            r0 = mobi.vserv.android.ads.VservAd.this;
            r0 = r0.adDialog;
            r0.setCloseButton();
            r0 = mobi.vserv.android.ads.VservAd.this;
            r0 = r0.renderAdController;
            if (r0 == 0) goto L_0x0074;
        L_0x006b:
            r0 = mobi.vserv.android.ads.VservAd.this;
            r0 = r0.renderAdController;
            r1 = 0;
            r0.currentSkipDelay = r1;
        L_0x0074:
            r0 = "browser";
            r0 = r5.hasExtra(r0);
            if (r0 != 0) goto L_0x00ae;
        L_0x007c:
            r0 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;
            if (r0 == 0) goto L_0x0087;
        L_0x0080:
            r0 = "sample";
            r1 = "will try to dismiss Dialog";
            android.util.Log.i(r0, r1);
        L_0x0087:
            r0 = mobi.vserv.android.ads.VservAd.this;
            r0 = r0.adDialog;
            r0.isShowing();
            r0 = mobi.vserv.android.ads.VservAd.this;
            r0 = r0.adDialog;
            if (r0 == 0) goto L_0x0059;
        L_0x0098:
            r0 = mobi.vserv.android.ads.VservAd.this;
            r0 = r0.adDialog;
            r0 = r0.isShowing();
            if (r0 == 0) goto L_0x0059;
        L_0x00a4:
            r0 = mobi.vserv.android.ads.VservAd.this;
            r0 = r0.adDialog;
            r0.dismiss();
            goto L_0x0059;
        L_0x00ae:
            r0 = mobi.vserv.android.ads.VservAd.this;
            r0 = r0.renderAdController;
            if (r0 == 0) goto L_0x0059;
        L_0x00b6:
            r0 = mobi.vserv.android.ads.VservAd.this;
            r0 = r0.renderAdController;
            r0 = r0.adComponentView;
            if (r0 == 0) goto L_0x0059;
        L_0x00c0:
            r0 = mobi.vserv.android.ads.VservAd.this;
            r0 = r0.renderAdController;
            r0 = r0.adComponentView;
            r0 = r0.videoPlayer;
            if (r0 == 0) goto L_0x0059;
        L_0x00cc:
            r0 = "vserv";
            r1 = "if onReceive";
            android.util.Log.i(r0, r1);
            r0 = mobi.vserv.android.ads.VservAd.this;
            r0 = r0.renderAdController;
            r0 = r0.adComponentView;
            r0 = r0.videoPlayer;
            r0.releasePlayer();
            goto L_0x0059;
            */
        }
    }

    protected class AdDialog extends Dialog {
        private IClickNotify clickNotify;
        private VservCountDownTimer countDownTimer;
        private int tempSkipdelay;
        private View view;

        public AdDialog(Context context, View view, IClickNotify clickNotify) {
            super(context);
            this.countDownTimer = null;
            this.tempSkipdelay = 0;
            this.view = view;
            this.clickNotify = clickNotify;
        }

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(1);
            getWindow().setBackgroundDrawable(new ColorDrawable(0));
            setContentView(VservAd.this.context.getResources().getIdentifier("vserv_dialog_layout", "layout", VservAd.this.context.getPackageName()));
            ((FrameLayout) findViewById(VservAd.this.context.getResources().getIdentifier("adContainer", AnalyticsEvent.EVENT_ID, VservAd.this.context.getPackageName()))).addView(this.view);
            new Thread() {
                public void run() {
                    if (AdDialog.this.this$0.renderAdController == null) {
                        ((Activity) AdDialog.this.this$0.context).runOnUiThread(new Runnable() {
                            public void run() {
                                ((ImageView) AnonymousClass_1.this.this$1.findViewById(AnonymousClass_1.this.this$1.this$0.context.getResources().getIdentifier("closeButton", AnalyticsEvent.EVENT_ID, AnonymousClass_1.this.this$1.this$0.context.getPackageName()))).setVisibility(0);
                            }
                        });
                    } else if (AdDialog.this.this$0.renderAdController.currentSkipDelay != -1) {
                        ((Activity) AdDialog.this.this$0.context).runOnUiThread(new Runnable() {

                            class AnonymousClass_1 extends VservCountDownTimer {
                                AnonymousClass_1(long $anonymous0, long $anonymous1) {
                                    super($anonymous0, $anonymous1);
                                }

                                public void onFinish() {
                                    if (Defines.ENABLE_lOGGING) {
                                        Log.i("vserv", "onFinish ");
                                    }
                                    AnonymousClass_1.this.this$2.this$1.this$0.renderAdController.currentSkipDelay = 0;
                                    AnonymousClass_1.this.this$2.this$1.tempSkipdelay = 0;
                                    ((TextView) AnonymousClass_1.this.this$2.this$1.findViewById(AnonymousClass_1.this.this$2.this$1.this$0.context.getResources().getIdentifier("skiplabel", AnalyticsEvent.EVENT_ID, AnonymousClass_1.this.this$2.this$1.this$0.context.getPackageName()))).setText(Preconditions.EMPTY_ARGUMENTS);
                                    ((TextView) AnonymousClass_1.this.this$2.this$1.findViewById(AnonymousClass_1.this.this$2.this$1.this$0.context.getResources().getIdentifier("skiplabel", AnalyticsEvent.EVENT_ID, AnonymousClass_1.this.this$2.this$1.this$0.context.getPackageName()))).setVisibility(MMAdView.TRANSITION_RANDOM);
                                    ((ImageView) AnonymousClass_1.this.this$2.this$1.findViewById(AnonymousClass_1.this.this$2.this$1.this$0.context.getResources().getIdentifier("closeButton", AnalyticsEvent.EVENT_ID, AnonymousClass_1.this.this$2.this$1.this$0.context.getPackageName()))).setVisibility(0);
                                }

                                public void onTick(long millisUntilFinished) {
                                    if (Defines.ENABLE_lOGGING) {
                                        Log.i("vserv", new StringBuilder("onTick ").append(millisUntilFinished / 1000).toString());
                                    }
                                    TextView textView = (TextView) AnonymousClass_1.this.this$2.this$1.findViewById(AnonymousClass_1.this.this$2.this$1.this$0.context.getResources().getIdentifier("skiplabel", AnalyticsEvent.EVENT_ID, AnonymousClass_1.this.this$2.this$1.this$0.context.getPackageName()));
                                    StringBuilder stringBuilder = new StringBuilder("Skip in ");
                                    AdDialog access$0 = AnonymousClass_1.this.this$2.this$1;
                                    int access$1 = access$0.tempSkipdelay;
                                    access$0.tempSkipdelay = access$1 - 1;
                                    textView.setText(stringBuilder.append(access$1).toString());
                                }
                            }

                            public void run() {
                                if (AnonymousClass_1.this.this$1.this$0.renderAdController.currentSkipDelay > 0) {
                                    if (Defines.ENABLE_lOGGING) {
                                        Log.i("vserv", new StringBuilder("currentSkipDelay ").append(AnonymousClass_1.this.this$1.this$0.renderAdController.currentSkipDelay).toString());
                                    }
                                    ((TextView) AnonymousClass_1.this.this$1.findViewById(AnonymousClass_1.this.this$1.this$0.context.getResources().getIdentifier("skiplabel", AnalyticsEvent.EVENT_ID, AnonymousClass_1.this.this$1.this$0.context.getPackageName()))).setVisibility(0);
                                    ((TextView) AnonymousClass_1.this.this$1.findViewById(AnonymousClass_1.this.this$1.this$0.context.getResources().getIdentifier("skiplabel", AnalyticsEvent.EVENT_ID, AnonymousClass_1.this.this$1.this$0.context.getPackageName()))).setText(new StringBuilder("Skip in ").append(AnonymousClass_1.this.this$1.this$0.renderAdController.currentSkipDelay).toString());
                                    AnonymousClass_1.this.this$1.tempSkipdelay = AnonymousClass_1.this.this$1.this$0.renderAdController.currentSkipDelay;
                                    AnonymousClass_1.this.this$1.countDownTimer = new AnonymousClass_1((long) (AnonymousClass_1.this.this$1.this$0.renderAdController.currentSkipDelay * 1000), 1000).start();
                                } else {
                                    ((ImageView) AnonymousClass_1.this.this$1.findViewById(AnonymousClass_1.this.this$1.this$0.context.getResources().getIdentifier("closeButton", AnalyticsEvent.EVENT_ID, AnonymousClass_1.this.this$1.this$0.context.getPackageName()))).setVisibility(0);
                                }
                            }
                        });
                    }
                }
            }.start();
            ((ImageView) findViewById(VservAd.this.context.getResources().getIdentifier("closeButton", AnalyticsEvent.EVENT_ID, VservAd.this.context.getPackageName()))).setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    AdDialog.this.clickNotify.onClick();
                    AdDialog.this.dismiss();
                }
            });
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    AdDialog.this.getWindow().setLayout(-1, InvalidManifestConfigException.MISSING_ACTIVITY_DECLARATION);
                }
            }, 150);
        }

        public boolean onKeyDown(int keyCode, KeyEvent event) {
            try {
                if (VservAd.this.renderAdController == null || VservAd.this.renderAdController.currentSkipDelay == -1) {
                    Toast.makeText(VservAd.this.context, "Please wait while the video completes", 0).show();
                    return false;
                } else {
                    if (VservAd.this.renderAdController.currentSkipDelay == 0) {
                        Log.i("vserv", "adDialog onCancel :: ");
                        VservManager.getInstance(VservAd.this.context).adOrientation = null;
                        if (VservAd.this.adClickReceiver != null) {
                            VservAd.this.context.unregisterReceiver(VservAd.this.adClickReceiver);
                            VservAd.this.adClickReceiver = null;
                            VservAd.this.adDialog = null;
                        }
                        if (!(VservAd.this.renderAdController == null || VservAd.this.renderAdController.adComponentView == null || VservAd.this.renderAdController.adComponentView.videoPlayer == null)) {
                            VservAd.this.renderAdController.adComponentView.videoPlayer.releasePlayer();
                        }
                    } else if (VservAd.this.renderAdController.currentSkipDelay >= -1) {
                        return false;
                    } else {
                        Log.i("vserv", "adDialog onCancel :: ");
                        VservManager.getInstance(VservAd.this.context).adOrientation = null;
                        if (VservAd.this.adClickReceiver != null) {
                            VservAd.this.context.unregisterReceiver(VservAd.this.adClickReceiver);
                            VservAd.this.adClickReceiver = null;
                            VservAd.this.adDialog = null;
                        }
                        if (!(VservAd.this.renderAdController == null || VservAd.this.renderAdController.adComponentView == null || VservAd.this.renderAdController.adComponentView.videoPlayer == null)) {
                            VservAd.this.renderAdController.adComponentView.videoPlayer.releasePlayer();
                        }
                    }
                    return super.onKeyDown(keyCode, event);
                }
            } catch (Exception e) {
            }
        }

        public void setCloseButton() {
            ((ImageView) findViewById(VservAd.this.context.getResources().getIdentifier("closeButton", AnalyticsEvent.EVENT_ID, VservAd.this.context.getPackageName()))).setVisibility(0);
        }
    }

    protected static interface IClickNotify {
        void onClick();
    }

    class AnonymousClass_1 implements IAddCallback {
        private final /* synthetic */ ViewGroup val$group;

        AnonymousClass_1(ViewGroup viewGroup) {
            this.val$group = viewGroup;
        }

        public void TimeOutOccured() {
        }

        public void onLoadFailure() {
        }

        public void onLoadSuccess(View view) {
            if (this.val$group != null) {
                if (this.val$group.getChildCount() > 0) {
                    this.val$group.removeAllViews();
                }
                if (view != null) {
                    this.val$group.addView(view);
                }
            }
        }

        public void onNoFill() {
        }

        public void showProgressBar() {
        }
    }

    class AnonymousClass_2 implements IAddCallback {
        private final /* synthetic */ Context val$context;
        private final /* synthetic */ ProgressDialog val$progressBar;

        class AnonymousClass_2 implements OnDismissListener {
            private final /* synthetic */ Context val$context;

            AnonymousClass_2(Context context) {
                this.val$context = context;
            }

            public void onDismiss(DialogInterface dialog) {
                try {
                    VservManager.getInstance(this.val$context).adOrientation = null;
                    if (AnonymousClass_2.this.this$0.adClickReceiver != null) {
                        Log.i("sample", "context.unregisterReceiver(adClickReceiver)");
                        this.val$context.unregisterReceiver(AnonymousClass_2.this.this$0.adClickReceiver);
                        AnonymousClass_2.this.this$0.adClickReceiver = null;
                        AnonymousClass_2.this.this$0.adDialog = null;
                    }
                    if (AnonymousClass_2.this.this$0.renderAdController != null && AnonymousClass_2.this.this$0.renderAdController.adComponentView != null && AnonymousClass_2.this.this$0.renderAdController.adComponentView.videoPlayer != null) {
                        AnonymousClass_2.this.this$0.renderAdController.adComponentView.videoPlayer.releasePlayer();
                    }
                } catch (Exception e) {
                }
            }
        }

        class AnonymousClass_3 implements OnCancelListener {
            private final /* synthetic */ Context val$context;

            AnonymousClass_3(Context context) {
                this.val$context = context;
            }

            public void onCancel(DialogInterface arg0) {
                try {
                    VservManager.getInstance(this.val$context).adOrientation = null;
                    if (AnonymousClass_2.this.this$0.adClickReceiver != null) {
                        Log.i("sample", "context.unregisterReceiver(adClickReceiver)");
                        this.val$context.unregisterReceiver(AnonymousClass_2.this.this$0.adClickReceiver);
                        AnonymousClass_2.this.this$0.adClickReceiver = null;
                        AnonymousClass_2.this.this$0.adDialog = null;
                    }
                    if (AnonymousClass_2.this.this$0.renderAdController != null && AnonymousClass_2.this.this$0.renderAdController.adComponentView != null && AnonymousClass_2.this.this$0.renderAdController.adComponentView.videoPlayer != null) {
                        AnonymousClass_2.this.this$0.renderAdController.adComponentView.videoPlayer.releasePlayer();
                    }
                } catch (Exception e) {
                }
            }
        }

        AnonymousClass_2(ProgressDialog progressDialog, Context context) {
            this.val$progressBar = progressDialog;
            this.val$context = context;
        }

        public void TimeOutOccured() {
        }

        public void onLoadFailure() {
            VservManager.getInstance(this.val$context).adOrientation = null;
            if (this.val$progressBar.isShowing()) {
                this.val$progressBar.dismiss();
            }
        }

        public void onLoadSuccess(View view) {
            if (VservAd.this.adDialog == null) {
                Log.i("sample", "adDialog==null");
            } else {
                Log.i("sample", "adDialog!=null");
            }
            if (VservAd.this.adDialog == null) {
                if (this.val$progressBar.isShowing()) {
                    this.val$progressBar.dismiss();
                }
                VservAd.this.adDialog = new AdDialog(this.val$context, view, new IClickNotify() {
                    public void onClick() {
                        if (AnonymousClass_2.this.this$0.renderAdController.adComponentView != null && AnonymousClass_2.this.this$0.renderAdController.adComponentView.videoPlayer != null) {
                            AnonymousClass_2.this.this$0.renderAdController.adComponentView.videoPlayer.releasePlayer();
                        }
                    }
                });
                VservAd.this.adClickReceiver = new AdClickReceiver();
                this.val$context.registerReceiver(VservAd.this.adClickReceiver, new IntentFilter("mobi.vserv.ad.dismiss_screen"));
                VservAd.this.adDialog.setCanceledOnTouchOutside(false);
                VservAd.this.adDialog.setOnDismissListener(new AnonymousClass_2(this.val$context));
                VservAd.this.adDialog.setOnCancelListener(new AnonymousClass_3(this.val$context));
                VservAd.this.adDialog.show();
            }
        }

        public void onNoFill() {
            VservManager.getInstance(this.val$context).adOrientation = null;
            if (this.val$progressBar.isShowing()) {
                this.val$progressBar.dismiss();
            }
        }

        public void showProgressBar() {
            this.val$progressBar.show();
        }
    }

    public String getZoneId() {
        return this.zoneId;
    }

    public void overlay(Context context) {
        this.context = context;
        overlay(context, null);
    }

    public void overlay(Context context, AdType mAdType) {
        this.context = context;
        try {
            if (!(this.zoneId == null || this.zoneId.equals(Preconditions.EMPTY_ARGUMENTS))) {
                VservManager.getInstance(context).setZoneID(this.zoneId);
            }
            if (VservManager.getInstance(context).getAdview() != null) {
                Bundle bundle = VservManager.getInstance(context).getPreparedBundle();
                bundle.putString("zoneId", this.zoneId);
                if (VservManager.getInstance(context).getShowAt() != null) {
                    bundle.putString("showAt", VservManager.getInstance(context).getShowAt());
                } else {
                    bundle.putString("showAt", "mid");
                }
                bundle.putString("adType", "overlay");
                if (mAdType == AdType.OVERLAY) {
                    ProgressDialog progressBar = new ProgressDialog(context);
                    progressBar.setProgressStyle(0);
                    bundle.putString("refreshInterval", "no");
                    this.renderAdController = new VservAdController(context, bundle, new AnonymousClass_2(progressBar, context));
                    this.renderAdController.requestAddView();
                } else {
                    Intent vservAdIntent = new Intent(context, VservAdManager.class);
                    vservAdIntent.putExtras(bundle);
                    ((Activity) context).startActivityForResult(vservAdIntent, VservManager.REQUEST_CODE);
                }
            }
        } catch (Exception e) {
            Exception e2 = e;
            VservManager.getInstance(context).trackExceptions(e2, "overlay");
            e2.printStackTrace();
        }
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    @SuppressLint({"InlinedApi"})
    public void show(Context context, ViewGroup group) throws ViewNotEmptyException {
        this.context = context;
        try {
            if (!(this.zoneId == null || this.zoneId.equals(Preconditions.EMPTY_ARGUMENTS))) {
                VservManager.getInstance(context).setZoneID(this.zoneId);
            }
            if (VservManager.getInstance(context).getAdview() != null) {
                Bundle bundle = VservManager.getInstance(context).getPreparedBundle();
                bundle.putString("zoneId", this.zoneId);
                bundle.putString("showAt", AnalyticsEvent.IN_APP);
                bundle.putString("adType", AdvConfigTable.COLUMN_SHOW);
                VservAdController adController = new VservAdController(context, bundle, new AnonymousClass_1(group));
                if (group.getChildCount() > 0) {
                    throw new ViewNotEmptyException("ViewGroup should be empty");
                }
                adController.requestAddView();
            }
        } catch (Exception e) {
            Exception e2 = e;
            VservManager.getInstance(context).trackExceptions(e2, AdvConfigTable.COLUMN_SHOW);
            e2.printStackTrace();
        }
    }
}