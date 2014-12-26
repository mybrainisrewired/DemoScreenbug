package com.yongding.util;

import android.content.Context;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsMessage;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.codehaus.jackson.util.MinimalPrettyPrinter;

public class PhoneState {
    public static final String defaultSeri = "123456";
    private TelephonyManager mPhoneManager;

    public PhoneState(Context context) {
        this.mPhoneManager = (TelephonyManager) context.getSystemService("phone");
    }

    private String getImsi(Context context, int cardNum) {
        String sRet = null;
        Method getSubscriberIdGemini = null;
        try {
            try {
                getSubscriberIdGemini = TelephonyManager.class.getDeclaredMethod("getSubscriberIdGemini", new Class[]{Integer.TYPE});
                getSubscriberIdGemini.setAccessible(true);
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e2) {
                e2.printStackTrace();
            }
            try {
                return (String) getSubscriberIdGemini.invoke((TelephonyManager) context.getSystemService("phone"), new Object[]{Integer.valueOf(cardNum)});
            } catch (IllegalArgumentException e3) {
                e3.printStackTrace();
                return sRet;
            } catch (IllegalAccessException e4) {
                e4.printStackTrace();
                return sRet;
            } catch (SecurityException e5) {
                e5.printStackTrace();
                return sRet;
            } catch (InvocationTargetException e6) {
                e6.printStackTrace();
                return sRet;
            }
        } catch (Exception e7) {
            e7.printStackTrace();
            return sRet;
        }
    }

    public static void initIsDoubleTelephone(Context context) {
        boolean isDouble = true;
        Object obj = null;
        Object result_1 = null;
        TelephonyManager tm = (TelephonyManager) context.getSystemService("phone");
        try {
            Method method = TelephonyManager.class.getMethod("getSimStateGemini", new Class[]{Integer.TYPE});
            obj = method.invoke(tm, new Object[]{new Integer(0)});
            result_1 = method.invoke(tm, new Object[]{new Integer(1)});
        } catch (SecurityException e) {
            isDouble = false;
            e.printStackTrace();
        } catch (NoSuchMethodException e2) {
            isDouble = false;
            e2.printStackTrace();
        } catch (IllegalArgumentException e3) {
            isDouble = false;
            e3.printStackTrace();
        } catch (IllegalAccessException e4) {
            isDouble = false;
            e4.printStackTrace();
        } catch (InvocationTargetException e5) {
            isDouble = false;
            e5.printStackTrace();
        } catch (Exception e6) {
            isDouble = false;
            e6.printStackTrace();
        }
        if (!isDouble) {
            return;
        }
        if (!obj.toString().equals("5") || !result_1.toString().equals("5")) {
            if ((obj.toString().equals("5") || !result_1.toString().equals("5")) && obj.toString().equals("5")) {
                result_1.toString().equals("5");
            }
        }
    }

    public String getAndroidId(Context context) {
        return Secure.getString(context.getContentResolver(), "android_id");
    }

    public String getIMSI(Context context) {
        String myIMSI = this.mPhoneManager.getSubscriberId();
        if (myIMSI != null) {
            return myIMSI;
        }
        myIMSI = getImsi(context, ClassWriter.COMPUTE_FRAMES);
        return myIMSI == null ? defaultSeri : myIMSI;
    }

    public String getImei() {
        String imei = this.mPhoneManager.getDeviceId();
        return imei == null ? defaultSeri : imei;
    }

    public String getIp() {
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                Enumeration<InetAddress> enumIpAddr = ((NetworkInterface) en.nextElement()).getInetAddresses();
                while (enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e) {
        }
        return "0.0.0.0";
    }

    public String getMobile() {
        String mobile = this.mPhoneManager.getLine1Number();
        return mobile == null ? "13000000000" : mobile;
    }

    public String getMoblieType() {
        return Build.MODEL.replace(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, "_");
    }

    public String getModel() {
        return Build.MODEL;
    }

    public String getPhoneNumbet() {
        return this.mPhoneManager.getLine1Number();
    }

    public String getSimSerialNumber() {
        return this.mPhoneManager.getSimSerialNumber();
    }

    public String getSmsCenterNum() {
        String imsc = new SmsMessage().getServiceCenterAddress();
        return imsc == null ? defaultSeri : imsc;
    }
}