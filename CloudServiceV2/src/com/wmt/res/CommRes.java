package com.wmt.res;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import java.lang.reflect.Field;
import java.util.HashMap;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;

public final class CommRes {
    private static final String PACKAGE_COMMRES = "com.wmt.commres";
    private static final String PACKAGE_COMMRES_R = "com.wmt.commres.R";
    private static final String TAG = "CommRes";
    private static Context sContext;
    private static HashMap<String, Class<?>> sInnerClassMap;

    static {
        sContext = null;
        sInnerClassMap = new HashMap();
    }

    public static Context getCommResApkContext(Context context) {
        try {
            Context context2;
            synchronized (sInnerClassMap) {
                if (sContext != null) {
                    context2 = sContext;
                } else {
                    sContext = context.createPackageContext(PACKAGE_COMMRES, JsonWriteContext.STATUS_OK_AFTER_SPACE);
                    Class<?>[] arr$ = sContext.getClassLoader().loadClass(PACKAGE_COMMRES_R).getClasses();
                    int len$ = arr$.length;
                    int i$ = 0;
                    while (i$ < len$) {
                        Class<?> c0 = arr$[i$];
                        sInnerClassMap.put(c0.getSimpleName(), c0);
                        i$++;
                    }
                    context2 = sContext;
                }
            }
            return context2;
        } catch (Exception e) {
            Exception e2 = e;
            Log.w(TAG, "getCommResApkContext Error: ", e2);
            sContext = null;
            sInnerClassMap.clear();
            throw new RuntimeException(e2.getMessage());
        }
    }

    public static String getPackageVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            Log.e(TAG, "getPackageVersion Error", e);
            return "unknown";
        }
    }

    public static Drawable getResourceDrawable(Context context, String strId) {
        Context c = getCommResApkContext(context);
        if (c == null) {
            return null;
        }
        int resId = getResourceResId(context, strId);
        return resId != -1 ? c.getResources().getDrawable(resId) : null;
    }

    public static int getResourceResId(Context context, String strId) {
        if (strId.charAt(0) != 'R' && strId.charAt(1) != '.') {
            return -1;
        }
        int innerDot = strId.indexOf(".", ClassWriter.COMPUTE_FRAMES);
        String innerClass = strId.substring(ClassWriter.COMPUTE_FRAMES, innerDot);
        String fieldId = strId.substring(innerDot + 1);
        try {
            synchronized (sInnerClassMap) {
                if (sContext == null) {
                    sContext = getCommResApkContext(context);
                }
            }
            Field[] arr$ = ((Class) sInnerClassMap.get(innerClass)).getFields();
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                Field f = arr$[i$];
                if (f.getName().equals(fieldId)) {
                    return f.getInt(null);
                }
                i$++;
            }
            return -1;
        } catch (Exception e) {
            Exception e2 = e;
            Log.w(TAG, "getResourceResId Error: ", e2);
            throw new RuntimeException(e2.getMessage());
        }
    }

    public static String getResourceString(Context context, String strId) {
        Context c = getCommResApkContext(context);
        if (c == null) {
            return null;
        }
        int resId = getResourceResId(context, strId);
        return resId != -1 ? c.getResources().getString(resId) : null;
    }

    public static void showAboutDialog(Context context, String AppName) {
        try {
            Context resContext = getCommResApkContext(context);
            int wmt_about_alert = getResourceResId(context, "R.layout.wmt_about_alert");
            if (wmt_about_alert == -1) {
                Log.i(TAG, "Can not get wmt_about_alert value ");
            }
            int textid = getResourceResId(context, "R.id.message");
            if (textid == -1) {
                Log.i(TAG, "Can not get R.id.message value ");
            }
            View aboutView = LayoutInflater.from(resContext).inflate(wmt_about_alert, null);
            TextView aboutTextView = (TextView) aboutView.findViewById(textid);
            String resourceString = getResourceString(context, "R.string.about_text_begin");
            Object[] objArr = new Object[2];
            objArr[0] = AppName;
            objArr[1] = getPackageVersion(context);
            String begin = String.format(resourceString, objArr);
            String middle = "";
            aboutTextView.setText(begin + middle + getResourceString(context, "R.string.about_text_end"));
            String aboutStr = getResourceString(context, "R.string.about");
            String okStr = getResourceString(context, "R.string.ok");
            Builder builder = new Builder(context);
            builder.setTitle(aboutStr);
            builder.setView(aboutView);
            builder.setPositiveButton(okStr, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.show();
        } catch (Exception e) {
            Log.w(TAG, "showAboutDialog Error: ", e);
        }
    }
}