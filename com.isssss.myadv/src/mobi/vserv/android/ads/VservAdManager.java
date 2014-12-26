package mobi.vserv.android.ads;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.media.TransportMediator;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.Preconditions;
import java.util.Iterator;
import mobi.vserv.org.ormma.controller.Defines;

public class VservAdManager extends Activity implements VservConstants {
    protected static final int DATA_CONNECTION_UNAVAILABLE = 100;
    protected static int height;
    protected static int width;
    private String applicationName;
    protected boolean callKillProcess;
    protected boolean callSystemExit;
    boolean dataConnectionAvailable;
    protected int minimumSessionTimeInSeconds;
    protected String mustSeeAdMsg;
    protected boolean runExitInstance;
    protected int startAfterCountForEnd;
    protected int startAfterCountForStart;
    private boolean startAfterCountForStartCheckDone;
    public VservAdController vservAdController;
    protected Bundle vservConfigBundle;
    protected boolean wrapAd;

    static {
        width = 0;
        height = 0;
    }

    public VservAdManager() {
        this.minimumSessionTimeInSeconds = 1;
        this.mustSeeAdMsg = "You must see ad to use this app";
        this.callSystemExit = false;
        this.callKillProcess = false;
        this.runExitInstance = false;
        this.startAfterCountForStartCheckDone = false;
        this.dataConnectionAvailable = false;
    }

    @SuppressLint({"InlinedApi"})
    private void checkForAdBlocking() {
        SharedPreferences blockPreference;
        if (VERSION.SDK_INT >= 11) {
            blockPreference = getSharedPreferences(this.vservConfigBundle.getString("preferenceName"), MMAdView.TRANSITION_RANDOM);
        } else {
            blockPreference = getSharedPreferences(this.vservConfigBundle.getString("preferenceName"), 0);
        }
        String flagDataType = this.vservConfigBundle.getString("flagDataType");
        String targetValue;
        String preferenceValue;
        if (blockPreference.contains(this.vservConfigBundle.getString("preAdFlagName"))) {
            if (flagDataType.equals("boolean")) {
                if (blockPreference.getBoolean(this.vservConfigBundle.getString("preAdFlagName"), false) == Boolean.parseBoolean(this.vservConfigBundle.getString("flagValueToCompare"))) {
                    this.wrapAd = false;
                    finish();
                }
            } else if (flagDataType.equals("int")) {
                if (blockPreference.getInt(this.vservConfigBundle.getString("preAdFlagName"), 0) == Integer.parseInt(this.vservConfigBundle.getString("flagValueToCompare"))) {
                    this.wrapAd = false;
                    finish();
                }
            } else if (flagDataType.equals("string")) {
                targetValue = this.vservConfigBundle.getString("flagValueToCompare");
                preferenceValue = blockPreference.getString(this.vservConfigBundle.getString("preAdFlagName"), Preconditions.EMPTY_ARGUMENTS);
                if (preferenceValue.length() > 0 && preferenceValue.equalsIgnoreCase(targetValue)) {
                    this.wrapAd = false;
                    finish();
                }
            } else if (flagDataType.equals("float")) {
                if (blockPreference.getFloat(this.vservConfigBundle.getString("preAdFlagName"), BitmapDescriptorFactory.HUE_RED) == Float.parseFloat(this.vservConfigBundle.getString("flagValueToCompare"))) {
                    this.wrapAd = false;
                    finish();
                }
            }
        } else if (blockPreference.contains(this.vservConfigBundle.getString("postAdFlagName"))) {
            if (flagDataType.equals("boolean")) {
                if (blockPreference.getBoolean(this.vservConfigBundle.getString("postAdFlagName"), false) == Boolean.parseBoolean(this.vservConfigBundle.getString("flagValueToCompare"))) {
                    this.wrapAd = false;
                    finish();
                }
            } else if (flagDataType.equals("int")) {
                if (blockPreference.getInt(this.vservConfigBundle.getString("postAdFlagName"), 0) == Integer.parseInt(this.vservConfigBundle.getString("flagValueToCompare"))) {
                    this.wrapAd = false;
                    finish();
                }
            } else if (flagDataType.equals("string")) {
                targetValue = this.vservConfigBundle.getString("flagValueToCompare");
                preferenceValue = blockPreference.getString(this.vservConfigBundle.getString("postAdFlagName"), Preconditions.EMPTY_ARGUMENTS);
                if (preferenceValue.length() > 0 && preferenceValue.equalsIgnoreCase(targetValue)) {
                    this.wrapAd = false;
                    finish();
                }
            } else if (flagDataType.equals("float")) {
                if (blockPreference.getFloat(this.vservConfigBundle.getString("postAdFlagName"), BitmapDescriptorFactory.HUE_RED) == Float.parseFloat(this.vservConfigBundle.getString("flagValueToCompare"))) {
                    this.wrapAd = false;
                    finish();
                }
            }
        } else if (this.vservConfigBundle.getString("showAdsOnNoFlags").contains("false")) {
            this.wrapAd = false;
            finish();
        }
    }

    private void handleExitAdButton() {
        if (!(this.vservAdController.adComponentView == null || this.vservAdController.adComponentView.videoPlayer == null)) {
            this.vservAdController.adComponentView.videoPlayer.releasePlayer();
        }
        this.vservAdController.loadMainApp(103, false);
    }

    private void handleSkipAdButton() {
        if (!(this.vservAdController.adComponentView == null || this.vservAdController.adComponentView.videoPlayer == null)) {
            this.vservAdController.adComponentView.videoPlayer.releasePlayer();
        }
        this.vservAdController.loadMainApp(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY, false);
    }

    private void initManager() {
        try {
            SharedPreferences settings;
            int startAfterCount;
            Editor editor;
            DisplayMetrics dm;
            if (!this.vservConfigBundle.getString("showAt").equalsIgnoreCase(VservConstants.VPLAY0) || this.startAfterCountForStart <= 0) {
                if (this.vservConfigBundle.getString("showAt").equalsIgnoreCase("end") && this.startAfterCountForEnd > 0) {
                    settings = getSharedPreferences(new StringBuilder(String.valueOf(this.applicationName)).append("_startAfterCount_end").toString(), 0);
                    startAfterCount = settings.getInt("startAfterCount", this.startAfterCountForEnd);
                    if (startAfterCount > 0) {
                        if (!(getSharedPreferences("vserv_unique_add_app_session", 0).getString("context", Preconditions.EMPTY_ARGUMENTS) == null || getSharedPreferences("vserv_unique_add_app_session", 0).getString("context", Preconditions.EMPTY_ARGUMENTS).length() == 0)) {
                            getSharedPreferences("vserv_unique_add_app_session", 0).edit().putString("context", Preconditions.EMPTY_ARGUMENTS).commit();
                        }
                        editor = settings.edit();
                        editor.putInt("startAfterCount", startAfterCount - 1);
                        editor.commit();
                        finish();
                        return;
                    } else {
                        this.startAfterCountForEnd = 0;
                    }
                }
                dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                width = dm.widthPixels;
                height = dm.heightPixels;
                this.vservAdController = new VservAdController(getApplicationContext(), this);
                this.vservAdController.invokeApplication();
            } else {
                settings = getSharedPreferences(new StringBuilder(String.valueOf(this.applicationName)).append("_startAfterCount_start").toString(), 0);
                startAfterCount = settings.getInt("startAfterCount", this.startAfterCountForStart);
                if (startAfterCount > 0) {
                    if (!(getSharedPreferences("vserv_unique_add_app_session", 0).getString("context", Preconditions.EMPTY_ARGUMENTS) == null || getSharedPreferences("vserv_unique_add_app_session", 0).getString("context", Preconditions.EMPTY_ARGUMENTS).length() == 0)) {
                        getSharedPreferences("vserv_unique_add_app_session", 0).edit().putString("context", Preconditions.EMPTY_ARGUMENTS).commit();
                    }
                    editor = settings.edit();
                    editor.putInt("startAfterCount", startAfterCount - 1);
                    editor.commit();
                    this.startAfterCountForStartCheckDone = true;
                    finish();
                } else {
                    this.startAfterCountForStart = 0;
                    dm = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(dm);
                    width = dm.widthPixels;
                    height = dm.heightPixels;
                    this.vservAdController = new VservAdController(getApplicationContext(), this);
                    this.vservAdController.invokeApplication();
                }
            }
        } catch (Exception e) {
        }
    }

    private void myOnCreate() {
        try {
            this.runExitInstance = false;
            this.startAfterCountForStart = 0;
            this.startAfterCountForEnd = 0;
            this.callSystemExit = false;
            this.wrapAd = true;
            this.callKillProcess = false;
            this.vservConfigBundle = getIntent().getExtras();
            Iterator it = this.vservConfigBundle.keySet().iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                if (Defines.ENABLE_lOGGING) {
                    Log.i("bundle", new StringBuilder(String.valueOf(key)).append(" => ").append(this.vservConfigBundle.get(key)).toString());
                }
            }
            if (!this.vservConfigBundle.containsKey("showAt")) {
                this.vservConfigBundle.putString("showAt", VservConstants.VPLAY0);
            }
            if (!this.vservConfigBundle.containsKey("zoneId")) {
                this.wrapAd = false;
            }
            if (!this.vservConfigBundle.containsKey("viewMandatory")) {
                Bundle bundle = getIntent().getExtras();
                bundle.putString("viewMandatory", "false");
                getIntent().putExtras(bundle);
                this.vservConfigBundle.putString("viewMandatory", "false");
            }
            if (!this.vservConfigBundle.containsKey("skipLabel")) {
                this.vservConfigBundle.putString("skipLabel", "Skip Ad");
            }
            if (!this.vservConfigBundle.containsKey("exitLabel")) {
                this.vservConfigBundle.putString("exitLabel", "Exit");
            }
            if (!this.vservConfigBundle.containsKey("cancelLabel")) {
                this.vservConfigBundle.putString("cancelLabel", "Cancel");
            }
            if (this.vservConfigBundle.containsKey("timeout")) {
                try {
                    if (Integer.parseInt(this.vservConfigBundle.getString("timeout")) < 0) {
                        if (Defines.ENABLE_lOGGING) {
                            Log.v("VservAdLogs", "timeout should be positive integer number.");
                        }
                        this.vservConfigBundle.putString("timeout", "20");
                    }
                } catch (Exception e) {
                    if (Defines.ENABLE_lOGGING) {
                        Log.v("VservAdLogs", "timeout should be positive integer number.");
                    }
                    this.vservConfigBundle.putString("timeout", "20");
                }
            } else {
                this.vservConfigBundle.putString("timeout", "20");
            }
            if (this.vservConfigBundle.containsKey("proceedTime")) {
                try {
                    if (Integer.parseInt(this.vservConfigBundle.getString("proceedTime")) < 0) {
                        if (Defines.ENABLE_lOGGING) {
                            Log.v("VservAdLogs", "proceedTime should be positive integer number.");
                        }
                        this.vservConfigBundle.putString("proceedTime", "20");
                    }
                } catch (Exception e2) {
                    if (Defines.ENABLE_lOGGING) {
                        Log.v("VservAdLogs", "proceedTime should be positive integer number.");
                    }
                    this.vservConfigBundle.putString("proceedTime", "20");
                }
            } else {
                this.vservConfigBundle.putString("proceedTime", "20");
            }
            if (!this.vservConfigBundle.containsKey("viewMandatoryMessage")) {
                this.vservConfigBundle.putString("viewMandatoryMessage", "Data connection unavailable");
            }
            if (!this.vservConfigBundle.containsKey("viewMandatoryRetryLabel")) {
                this.vservConfigBundle.putString("viewMandatoryRetryLabel", "Retry");
            }
            if (!this.vservConfigBundle.containsKey("viewMandatoryExitLabel")) {
                this.vservConfigBundle.putString("viewMandatoryExitLabel", "Exit");
            }
            if (!this.vservConfigBundle.containsKey("locationAds")) {
                this.vservConfigBundle.putString("locationAds", "true");
            }
            if (this.wrapAd) {
                this.vservConfigBundle.putString("showAds", "true");
            } else {
                this.vservConfigBundle.putString("showAds", "false");
            }
            if (this.vservConfigBundle.containsKey("vservPremiumAds") && this.vservConfigBundle.getString("vservPremiumAds").equalsIgnoreCase("false")) {
                this.wrapAd = false;
            }
            String showAtPosition = VservConstants.VPLAY0;
            if (this.vservConfigBundle.containsKey("showAt")) {
                showAtPosition = this.vservConfigBundle.getString("showAt");
            }
            if (showAtPosition.equalsIgnoreCase(VservConstants.VPLAY0) && this.vservConfigBundle.containsKey("startAfterCount")) {
                this.startAfterCountForStart = Integer.parseInt(this.vservConfigBundle.getString("startAfterCount"));
            }
            if (showAtPosition.equalsIgnoreCase("end") && this.vservConfigBundle.containsKey("startAfterCount")) {
                this.startAfterCountForEnd = Integer.parseInt(this.vservConfigBundle.getString("startAfterCount"));
            }
            PackageManager pm = getApplicationContext().getPackageManager();
            this.applicationName = (String) pm.getApplicationInfo(getApplicationContext().getPackageName(), 128).loadLabel(pm);
            if (this.vservConfigBundle.containsKey("mccExclusionList")) {
                String codes = this.vservConfigBundle.getString("mccExclusionList");
                if (codes != null && codes.trim().length() > 0) {
                    String[] arrCodes = codes.split(",");
                    String mcc = null;
                    try {
                        mcc = ((TelephonyManager) getSystemService("phone")).getNetworkOperator();
                    } catch (Exception e3) {
                    }
                    if (mcc != null) {
                        if (mcc.trim().length() > 0) {
                            mcc = mcc.substring(0, 3);
                            int i = 0;
                            while (i < arrCodes.length) {
                                if (arrCodes[i].trim().equals(mcc.trim())) {
                                    this.wrapAd = false;
                                    if (Defines.ENABLE_lOGGING) {
                                        Log.i("ads", "Setting wrapAd & wrapStaticAd to false");
                                    }
                                    finish();
                                    break;
                                } else {
                                    i++;
                                }
                            }
                        }
                    }
                }
            }
            if (this.vservConfigBundle.containsKey("blockAds") && Boolean.parseBoolean(this.vservConfigBundle.getString("blockAds"))) {
                checkForAdBlocking();
            }
            if (this.wrapAd) {
                initManager();
            }
        } catch (Exception e4) {
        }
    }

    private void myOnFinish() {
        if (this.vservAdController != null) {
            this.vservAdController.startThread = false;
            if (this.vservConfigBundle.getString("showAt").equalsIgnoreCase("end")) {
                this.vservAdController.clearAdPanel();
                this.vservAdController = null;
            }
        }
    }

    public void finish() {
        if (this.dataConnectionAvailable || !this.wrapAd || this.startAfterCountForStartCheckDone) {
            setResult(-1, getIntent());
        } else {
            setResult(DATA_CONNECTION_UNAVAILABLE, getIntent());
        }
        myOnFinish();
        sendBroadcast(new Intent("mobi.vserv.ad.dismiss_screen"));
        super.finish();
    }

    protected int getResourceIdentifier(String resourceName) {
        try {
            return getResources().getIdentifier(resourceName, "drawable", getPackageName());
        } catch (Exception e) {
            return -1;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected void onActivityResult(int r5_requestCode, int r6_resultCode, android.content.Intent r7_data) {
        throw new UnsupportedOperationException("Method not decompiled: mobi.vserv.android.ads.VservAdManager.onActivityResult(int, int, android.content.Intent):void");
        /*
        r4 = this;
        r3 = 111; // 0x6f float:1.56E-43 double:5.5E-322;
        r2 = 0;
        super.onActivityResult(r5, r6, r7);
        r0 = r4.vservAdController;
        if (r0 == 0) goto L_0x001e;
    L_0x000a:
        r0 = 100;
        if (r6 != r0) goto L_0x001e;
    L_0x000e:
        r0 = r4.vservAdController;
        r0.loadMainApp(r3, r2);
        mobi.vserv.android.ads.VservAdController.closeVservActivity = r2;
    L_0x0015:
        if (r5 != r3) goto L_0x001d;
    L_0x0017:
        r0 = -1;
        if (r6 != r0) goto L_0x001d;
    L_0x001a:
        r4.finish();
    L_0x001d:
        return;
    L_0x001e:
        r0 = r4.vservAdController;
        if (r0 == 0) goto L_0x0066;
    L_0x0022:
        r0 = 101; // 0x65 float:1.42E-43 double:5.0E-322;
        if (r6 != r0) goto L_0x0066;
    L_0x0026:
        r0 = r4.vservConfigBundle;
        r1 = "showAt";
        r0 = r0.getString(r1);
        r1 = "start";
        r0 = r0.equalsIgnoreCase(r1);
        if (r0 == 0) goto L_0x003a;
    L_0x0036:
        r4.handleSkipAdButton();
        goto L_0x0015;
    L_0x003a:
        r0 = r4.vservConfigBundle;
        r1 = "showAt";
        r0 = r0.getString(r1);
        r1 = "end";
        r0 = r0.equalsIgnoreCase(r1);
        if (r0 == 0) goto L_0x004e;
    L_0x004a:
        r4.handleExitAdButton();
        goto L_0x0015;
    L_0x004e:
        r0 = r4.vservConfigBundle;
        r1 = "showAt";
        r0 = r0.getString(r1);
        r1 = "mid";
        r0 = r0.equalsIgnoreCase(r1);
        if (r0 == 0) goto L_0x0015;
    L_0x005e:
        r0 = r4.vservAdController;
        r1 = 105; // 0x69 float:1.47E-43 double:5.2E-322;
        r0.loadMainApp(r1, r2);
        goto L_0x0015;
    L_0x0066:
        r0 = r4.vservAdController;
        if (r0 == 0) goto L_0x0015;
    L_0x006a:
        r0 = 102; // 0x66 float:1.43E-43 double:5.04E-322;
        if (r6 != r0) goto L_0x0015;
    L_0x006e:
        r0 = r4.vservAdController;
        r0.showClose();
        goto L_0x0015;
        */
    }

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(1);
        super.onCreate(savedInstanceState);
        if (Defines.ENABLE_lOGGING) {
            Log.i("vserv", "VservAdManager onCreate Called!!");
        }
        getWindow().addFlags(TransportMediator.FLAG_KEY_MEDIA_NEXT);
        VservAdController.snsPageLoaded = false;
        myOnCreate();
    }

    protected void onDestroy() {
        if (Defines.ENABLE_lOGGING) {
            Log.i("vserv", new StringBuilder("vservAdManager onDestroy ::").append(isFinishing()).toString());
        }
        super.onDestroy();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onKeyDown(int r5_keyCode, android.view.KeyEvent r6_event) {
        throw new UnsupportedOperationException("Method not decompiled: mobi.vserv.android.ads.VservAdManager.onKeyDown(int, android.view.KeyEvent):boolean");
        /*
        r4 = this;
        r2 = -1;
        r0 = 0;
        r1 = 4;
        if (r5 != r1) goto L_0x0050;
    L_0x0005:
        r1 = r4.vservAdController;
        if (r1 == 0) goto L_0x0050;
    L_0x0009:
        r1 = r4.vservAdController;
        r1 = r1.currentSkipDelay;
        if (r1 == r2) goto L_0x008f;
    L_0x000f:
        r1 = r4.vservAdController;
        r1 = r1.currentSkipDelay;
        if (r1 != 0) goto L_0x0051;
    L_0x0015:
        r0 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;
        if (r0 == 0) goto L_0x0030;
    L_0x0019:
        r0 = r4.vservAdController;
        r0 = r0.adComponentView;
        if (r0 == 0) goto L_0x0030;
    L_0x001f:
        r0 = r4.vservAdController;
        r0 = r0.adComponentView;
        r0 = r0.videoPlayer;
        if (r0 == 0) goto L_0x0030;
    L_0x0027:
        r0 = r4.vservAdController;
        r0 = r0.adComponentView;
        r0 = r0.videoPlayer;
        r0.releasePlayer();
    L_0x0030:
        r0 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;
        if (r0 == 0) goto L_0x004c;
    L_0x0034:
        r0 = "vserv";
        r1 = new java.lang.StringBuilder;
        r2 = "(vservAdController.currentSkipDelay == 0 : ";
        r1.<init>(r2);
        r2 = r4.vservAdController;
        r2 = r2.currentSkipDelay;
        r1 = r1.append(r2);
        r1 = r1.toString();
        android.util.Log.i(r0, r1);
    L_0x004c:
        r0 = super.onKeyDown(r5, r6);
    L_0x0050:
        return r0;
    L_0x0051:
        r1 = r4.vservAdController;
        r1 = r1.currentSkipDelay;
        if (r1 >= r2) goto L_0x0050;
    L_0x0057:
        r0 = r4.vservAdController;
        r0 = r0.adComponentView;
        if (r0 == 0) goto L_0x006e;
    L_0x005d:
        r0 = r4.vservAdController;
        r0 = r0.adComponentView;
        r0 = r0.videoPlayer;
        if (r0 == 0) goto L_0x006e;
    L_0x0065:
        r0 = r4.vservAdController;
        r0 = r0.adComponentView;
        r0 = r0.videoPlayer;
        r0.releasePlayer();
    L_0x006e:
        r0 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;
        if (r0 == 0) goto L_0x008a;
    L_0x0072:
        r0 = "vserv";
        r1 = new java.lang.StringBuilder;
        r2 = "vservAdController.currentSkipDelay < -1 : ";
        r1.<init>(r2);
        r2 = r4.vservAdController;
        r2 = r2.currentSkipDelay;
        r1 = r1.append(r2);
        r1 = r1.toString();
        android.util.Log.i(r0, r1);
    L_0x008a:
        r0 = super.onKeyDown(r5, r6);
        goto L_0x0050;
    L_0x008f:
        r1 = mobi.vserv.org.ormma.controller.Defines.ENABLE_lOGGING;
        if (r1 == 0) goto L_0x00ab;
    L_0x0093:
        r1 = "vserv";
        r2 = new java.lang.StringBuilder;
        r3 = "else vseradmanager onKeyDown vservAdView.currentSkipDelay:: ";
        r2.<init>(r3);
        r3 = r4.vservAdController;
        r3 = r3.currentSkipDelay;
        r2 = r2.append(r3);
        r2 = r2.toString();
        android.util.Log.i(r1, r2);
    L_0x00ab:
        r1 = "Please wait while the video completes";
        r1 = android.widget.Toast.makeText(r4, r1, r0);
        r1.show();
        goto L_0x0050;
        */
    }

    protected void onResume() {
        if (Defines.ENABLE_lOGGING) {
            Log.i("vserv", "vservAdManager onResume ::");
        }
        super.onResume();
    }
}