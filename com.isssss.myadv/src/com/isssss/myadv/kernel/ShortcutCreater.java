package com.isssss.myadv.kernel;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import com.google.android.gms.drive.DriveFile;
import com.isssss.myadv.model.PushAppData;

public class ShortcutCreater {
    private static String ACTION_INSTALL_SHORTCUT;
    private static String ACTION_UNINSTALL_SHORTCUT;

    static {
        ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
        ACTION_UNINSTALL_SHORTCUT = "com.android.launcher.action.UNINSTALL_SHORTCUT";
    }

    public static void createShortCut(Context context, PushAppData pushData, Bitmap bitmap) {
        Intent shortcutIntent = new Intent(ACTION_INSTALL_SHORTCUT);
        shortcutIntent.putExtra("android.intent.extra.shortcut.NAME", pushData.getmTitle());
        shortcutIntent.putExtra("android.intent.extra.shortcut.ICON", bitmap);
        shortcutIntent.putExtra("duplicate", false);
        Intent clickIntent = new Intent("android.intent.action.VIEW", Uri.parse(new StringBuilder("http://").append(pushData.getmApkURL()).toString()));
        clickIntent.setFlags(DriveFile.MODE_READ_ONLY);
        shortcutIntent.putExtra("android.intent.extra.shortcut.INTENT", clickIntent);
        context.sendBroadcast(shortcutIntent);
    }

    public static void deleteShortCut(Context context) {
    }
}