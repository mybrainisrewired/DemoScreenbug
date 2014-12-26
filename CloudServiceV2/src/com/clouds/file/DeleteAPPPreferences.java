package com.clouds.file;

import android.content.Context;
import android.content.SharedPreferences;
import com.clouds.object.DeleteAppInfo;

public class DeleteAPPPreferences {
    private static final String APPNAME = "appname";
    private static final String CLASS = "classname";
    private static final String FILENAME = "deleteappinfo";
    private static final String PACKAGE = "package";
    private static final String TAG;
    private static final String TYPE = "type";

    static {
        TAG = BootSharedPreferences.class.getName();
    }

    public static DeleteAppInfo getDeleteAppInfo(Context context) {
        SharedPreferences appFile = context.getSharedPreferences(FILENAME, 0);
        return new DeleteAppInfo(appFile.getString(APPNAME, null), appFile.getString(PACKAGE, null), appFile.getString(CLASS, null), appFile.getString(TYPE, null));
    }

    public static void setDeleteAppInfo(Context context, String appName, String packageName, String className, String type) {
        context.getSharedPreferences(FILENAME, 0).edit().putString(APPNAME, appName).putString(PACKAGE, packageName).putString(CLASS, className).putString(TYPE, type).commit();
    }
}