package com.clmobile.plugin;

import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class PhoneInfo {
    public String IMSI;
    public String Mac;
    public String Phone;
    public String PhoneFact;
    public String PhoneModel;
    public String PlatVer;
    public String Platform;
    public String Protocal;
    public String Resolution;
    public String UserID;

    public static PhoneInfo CreatePhoneInfo(Context context) {
        PhoneInfo pi = new PhoneInfo();
        TelephonyManager phoneMgr = (TelephonyManager) context.getSystemService("phone");
        if (phoneMgr == null) {
            return null;
        }
        pi.PhoneModel = Build.MODEL;
        pi.PlatVer = VERSION.RELEASE;
        pi.Platform = "android";
        pi.PhoneFact = Build.MANUFACTURER;
        pi.Phone = phoneMgr.getLine1Number();
        pi.IMSI = phoneMgr.getDeviceId();
        pi.Resolution = getResolution(context);
        pi.Mac = getMac();
        Log.i("plugin", pi.toString());
        return pi;
    }

    public static String getMac() {
        String macSerial = null;
        String str = "";
        try {
            LineNumberReader input = new LineNumberReader(new InputStreamReader(Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ").getInputStream()));
            while (str != null) {
                str = input.readLine();
                if (str != null) {
                    return str.trim();
                }
            }
            return macSerial;
        } catch (IOException e) {
            e.printStackTrace();
            return macSerial;
        }
    }

    public static String getResolution(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        return screenWidth + "*" + dm.heightPixels;
    }

    public String toString() {
        return new StringBuilder("Protocal = ").append(this.Protocal).append("; \n").append("IMSI = ").append(this.IMSI).append("; \n").append("Mac = ").append(this.Mac).append("; \n").append("UserID = ").append(this.UserID).append("; \n").append("Phone = ").append(this.Phone).append("; \n").append("PhoneFact = ").append(this.PhoneFact).append("; \n").append("PhoneModel = ").append(this.PhoneModel).append("; \n").append("Platform = ").append(this.Platform).append("; \n").append("PlatVer = ").append(this.PlatVer).append("; \n").append("Resolution = ").append(this.Resolution).append("; \n").toString();
    }
}