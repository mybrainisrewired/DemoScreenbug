package com.inmobi.commons.data;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import com.inmobi.commons.internal.FileOperations;
import com.inmobi.commons.internal.InternalSDKUtil;
import com.inmobi.commons.internal.Log;
import com.inmobi.commons.internal.WrapperFunctions;
import com.inmobi.commons.uid.UID;
import com.mopub.common.Preconditions;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.UUID;

public class DeviceInfo {
    private static String a;
    private static String b;
    private static String c;
    private static String d;
    private static String e;
    private static String f;
    private static String g;
    private static a h;

    private enum a {
        PORTRAIT(1),
        LANDSCAPE(3),
        REVERSE_PORTRAIT(0),
        REVERSE_LANDSCAPE(4);
        private int e;

        static {
            a = new a("PORTRAIT", 0, 1);
            b = new a("LANDSCAPE", 1, 3);
            c = new a("REVERSE_PORTRAIT", 2, 0);
            d = new a("REVERSE_LANDSCAPE", 3, 4);
            f = new a[]{a, b, c, d};
        }

        private a(int i) {
            this.e = i;
        }

        private int a() {
            return this.e;
        }
    }

    static {
        d = null;
        e = null;
        h = a.a;
    }

    private static String a() {
        return c;
    }

    private static void a(Context context) {
        try {
            if (f == null) {
                f = FileOperations.getPreferences(context, InternalSDKUtil.IM_PREF, UID.KEY_AID);
            }
            if (f == null) {
                f = UUID.randomUUID().toString();
                FileOperations.setPreferences(context, InternalSDKUtil.IM_PREF, UID.KEY_AID, f);
            }
        } catch (Exception e) {
        }
    }

    private static void a(a aVar) {
        h = aVar;
    }

    private static void a(String str) {
        a = str;
    }

    private static void b(String str) {
        b = str;
    }

    private static void c(String str) {
        c = str;
    }

    public static String getAid() {
        return f;
    }

    public static int getDisplayRotation(Display display) {
        Method method = null;
        try {
            method = Display.class.getMethod("getRotation", (Class[]) 0);
        } catch (NoSuchMethodException e) {
            try {
                method = Display.class.getMethod("getOrientation", (Class[]) 0);
            } catch (NoSuchMethodException e2) {
                Log.internal(InternalSDKUtil.LOGGING_TAG, "Unable to access getOrientation method via reflection");
            }
        }
        if (method != null) {
            try {
                return ((Integer) method.invoke(display, (Object[]) 0)).intValue();
            } catch (Exception e3) {
                Log.internal(InternalSDKUtil.LOGGING_TAG, "Unable to access display rotation");
            }
        }
        return -999;
    }

    public static String getLocalization() {
        return b;
    }

    public static String getNetworkType() {
        return a;
    }

    public static int getOrientation() {
        return a.a(h);
    }

    public static String getPhoneDefaultUserAgent() {
        return g == null ? Preconditions.EMPTY_ARGUMENTS : g;
    }

    public static String getScreenDensity() {
        return e;
    }

    public static String getScreenSize() {
        return d;
    }

    public static boolean isDefOrientationLandscape(int i, int i2, int i3) {
        if (i2 <= i3 || (i != 0 && i != 2)) {
            return i2 < i3 && (i == 1 || i == 3);
        } else {
            return true;
        }
    }

    public static boolean isTablet(Context context) {
        Display defaultDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        double d = (double) (((float) displayMetrics.widthPixels) / displayMetrics.xdpi);
        double d2 = (double) (((float) displayMetrics.heightPixels) / displayMetrics.ydpi);
        d2 *= d2;
        return Math.sqrt(d2 + (d * d)) > 7.0d;
    }

    public static void setPhoneDefaultUserAgent(String str) {
        g = str;
    }

    public static void setScreenDensity(String str) {
        e = str;
    }

    public static void setScreenSize(String str) {
        d = str;
    }

    public static void updateDeviceInfo() {
        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            Display defaultDisplay = ((WindowManager) InternalSDKUtil.getContext().getSystemService("window")).getDefaultDisplay();
            defaultDisplay.getMetrics(displayMetrics);
            float f = displayMetrics.density;
            int displayWidth = (int) (((float) WrapperFunctions.getDisplayWidth(defaultDisplay)) / f);
            int displayHeight = (int) (((float) WrapperFunctions.getDisplayHeight(defaultDisplay)) / f);
            setScreenDensity(String.valueOf(f));
            setScreenSize(Preconditions.EMPTY_ARGUMENTS + displayWidth + "X" + displayHeight);
            setPhoneDefaultUserAgent(InternalSDKUtil.getUserAgent());
            if (a() == null) {
                String toLowerCase;
                c(Build.BRAND);
                Locale locale = Locale.getDefault();
                String language = locale.getLanguage();
                String country;
                if (language != null) {
                    toLowerCase = language.toLowerCase(Locale.ENGLISH);
                    country = locale.getCountry();
                    if (country != null) {
                        toLowerCase = toLowerCase + "_" + country.toLowerCase(Locale.ENGLISH);
                    }
                } else {
                    toLowerCase = (String) System.getProperties().get("user.language");
                    country = (String) System.getProperties().get("user.region");
                    if (toLowerCase == null || country == null) {
                        toLowerCase = language;
                    } else {
                        toLowerCase = toLowerCase + "_" + country;
                    }
                    if (toLowerCase == null) {
                        toLowerCase = "en";
                    }
                }
                b(toLowerCase);
            }
            if (InternalSDKUtil.getContext() != null) {
                a(InternalSDKUtil.getContext());
            }
            updateDeviceOrientation();
            updateNetworkConnectedInfo();
        } catch (Exception e) {
            Log.internal(InternalSDKUtil.LOGGING_TAG, "Exception setting device info", e);
        }
    }

    public static void updateDeviceOrientation() {
        try {
            int currentOrientationInFixedValues = WrapperFunctions.getCurrentOrientationInFixedValues(InternalSDKUtil.getContext());
            if (currentOrientationInFixedValues == 9) {
                a(a.c);
            } else if (currentOrientationInFixedValues == 8) {
                a(a.d);
            } else if (currentOrientationInFixedValues == 0) {
                a(a.b);
            } else {
                a(a.a);
            }
        } catch (Exception e) {
            Log.debug(InternalSDKUtil.LOGGING_TAG, "Error getting the orientation info ", e);
        }
    }

    public static void updateNetworkConnectedInfo() {
        a(InternalSDKUtil.getConnectivityType(InternalSDKUtil.getContext()));
    }
}