package com.clmobile.app;

import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import com.clmobile.plugin.networktask.UserModel;
import com.clmobile.plugin.task.StoreManager;
import com.yongding.util.PhoneState;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import org.codehaus.jackson.smile.SmileConstants;

public class MandatoryInfo {
    private static Context context;
    private static MandatoryInfo instance;
    private String IMSI;
    private String Mac;
    private String PacketID;
    private String Phone;
    private String PhoneFact;
    private String PhoneModel;
    private String PlatVer;
    private String Platform;
    private String Resolution;
    private String UserID;

    private MandatoryInfo() {
        init();
    }

    public static MandatoryInfo getInstance() {
        if (instance == null) {
            synchronized (MandatoryInfo.class) {
                if (instance == null) {
                    instance = new MandatoryInfo();
                }
            }
        }
        return instance;
    }

    private void init() {
        TelephonyManager phoneMgr = (TelephonyManager) context.getSystemService("phone");
        this.PhoneModel = Build.MODEL;
        this.PlatVer = VERSION.RELEASE;
        this.Platform = "android";
        this.PhoneFact = Build.MANUFACTURER;
        this.Phone = phoneMgr.getLine1Number();
        this.IMSI = new PhoneState(context).getIMSI(context);
        this.Resolution = retrieveResolution(context);
        this.Mac = retrieveMac();
        this.PacketID = retrieveChannerId();
        if (this.IMSI.equalsIgnoreCase(PhoneState.defaultSeri)) {
            this.IMSI = this.Mac;
        }
        if (this.Mac.equalsIgnoreCase("macSerial")) {
            this.Mac = this.IMSI;
        }
        UserModel user = StoreManager.getIntance(context).getUser();
        if (user != null) {
            this.UserID = user.getUserID();
        }
    }

    public static synchronized void initContext(Context c) {
        synchronized (MandatoryInfo.class) {
            context = c;
        }
    }

    public static String retrieveChannerId() {
        try {
            return String.valueOf(context.getPackageManager().getApplicationInfo(context.getPackageName(), SmileConstants.TOKEN_PREFIX_TINY_UNICODE).metaData.getInt("android_channel", 1010));
        } catch (Exception e) {
            return "1010";
        }
    }

    public static String retrieveMac() {
        String macSerial = null;
        String str = "";
        try {
            LineNumberReader input = new LineNumberReader(new InputStreamReader(Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ").getInputStream()));
            while (str != null) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return TextUtils.isEmpty(macSerial) ? "macSerial" : macSerial;
    }

    public static String retrieveResolution(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        return screenWidth + "*" + dm.heightPixels;
    }

    public String getIMSI() {
        return this.IMSI;
    }

    public String getMac() {
        return this.Mac;
    }

    public String getPacketID() {
        return this.PacketID;
    }

    public String getPhone() {
        return this.Phone;
    }

    public String getPhoneFact() {
        return this.PhoneFact;
    }

    public String getPhoneModel() {
        return this.PhoneModel;
    }

    public String getPlatVer() {
        return this.PlatVer;
    }

    public String getPlatform() {
        return this.Platform;
    }

    public String getResolution() {
        return this.Resolution;
    }

    public synchronized String getUserID() {
        return this.UserID;
    }

    public synchronized boolean isRegisted() {
        return !TextUtils.isEmpty(this.UserID);
    }

    public synchronized void setUserID(String userID) {
        this.UserID = userID;
    }
}