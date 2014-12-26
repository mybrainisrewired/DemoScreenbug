package com.clouds.install;

import android.util.Log;
import com.clouds.debug.SystemDebug;
import com.clouds.server.ShCmd;
import com.clouds.util.DistinguishDevice;

class StandardFileInstall {
    private static final String BOOTANIMATION_TAG_PATH = "/system/media/bootanimation.zip";
    private static final String BROWSER_HOME_PAPERS_TAG_PATH = "/system/etc/homepage";
    private static final String BROWSER_HOME_PAPER_TAG_PATH = "/system/etc/bhp";
    private static final String CHMOD_644 = "/system/bin/busybox chmod 644 ";
    private static final String CHMOD_755 = "/system/bin/busybox chmod 755 ";
    private static final String CP = "/system/bin/busybox cp ";
    private static final String DELETEAPPINFO_HOME_PAPER_TAG_PATH = "/system/etc/deleteappinfo.xml";
    private static final String INSTALL = "/system/bin/pm install -r ";
    private static final String RM = "/system/bin/busybox rm ";
    private static final String SECURITY_CERTIFICATE_PAPER_TAG_PATH = "/system/etc/security/cacerts/";
    private static final String SYSTEM_APP_TAG_PATH = "/system/app/";
    private static final String TAG;
    private static final String UNINSTALL = "/system/bin/pm uninstall ";

    static {
        TAG = FileInstall.class.getSimpleName();
    }

    StandardFileInstall() {
    }

    static void setStandardSecurityCertificate(String path, String certificateName) {
        String certificateAbsPath = new StringBuilder(SECURITY_CERTIFICATE_PAPER_TAG_PATH).append(certificateName).toString();
        SystemDebug.e(TAG, new StringBuilder("isRKDevice: ").append(DistinguishDevice.isRKDevice()).append(" certificateAbsPath: ").append(certificateAbsPath).toString());
        if (ShCmd.shellCmmandIsSafe(new StringBuilder(CP).append(path).append(" ").append(certificateAbsPath).toString()) != 0) {
            Log.e(TAG, "failed to copy browser homepaper");
        }
        if (ShCmd.shellCmmandIsSafe(new StringBuilder(CHMOD_755).append(certificateAbsPath).toString()) != 0) {
            Log.e(TAG, "failed to change mod browser homepaper");
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static void standardDeleteApplication(java.lang.String r4_appName, java.lang.String r5_type) {
        throw new UnsupportedOperationException("Method not decompiled: com.clouds.install.StandardFileInstall.standardDeleteApplication(java.lang.String, java.lang.String):void");
        /*
        r1 = TAG;
        r2 = new java.lang.StringBuilder;
        r3 = "appName: ";
        r2.<init>(r3);
        r2 = r2.append(r4);
        r2 = r2.toString();
        com.clouds.debug.SystemDebug.e(r1, r2);
        r1 = new java.lang.StringBuilder;
        r2 = "/system/app/";
        r1.<init>(r2);
        r1 = r1.append(r4);
        r0 = r1.toString();
        if (r5 == 0) goto L_0x004a;
    L_0x0025:
        r1 = "user";
        r1 = r5.equals(r1);
        if (r1 == 0) goto L_0x004a;
    L_0x002d:
        r1 = new java.lang.StringBuilder;
        r2 = "/system/bin/pm uninstall ";
        r1.<init>(r2);
        r1 = r1.append(r4);
        r1 = r1.toString();
        r1 = com.clouds.server.ShCmd.shellCmmandIsSafe(r1);
        if (r1 == 0) goto L_0x0049;
    L_0x0042:
        r1 = TAG;
        r2 = "failed to install package";
        android.util.Log.e(r1, r2);
    L_0x0049:
        return;
    L_0x004a:
        if (r5 == 0) goto L_0x0049;
    L_0x004c:
        r1 = "system";
        r1 = r5.equals(r1);
        if (r1 == 0) goto L_0x0049;
    L_0x0054:
        r1 = new java.lang.StringBuilder;
        r2 = "/system/bin/busybox rm ";
        r1.<init>(r2);
        r1 = r1.append(r0);
        r1 = r1.toString();
        r1 = com.clouds.server.ShCmd.shellCmmandIsSafe(r1);
        if (r1 == 0) goto L_0x0049;
    L_0x0069:
        r1 = TAG;
        r2 = "failed to remove app";
        android.util.Log.e(r1, r2);
        goto L_0x0049;
        */
    }

    static void standardInstallAnimation(String path) {
        if (ShCmd.shellCmmandIsSafe(new StringBuilder(CP).append(path).append(" ").append(BOOTANIMATION_TAG_PATH).toString()) != 0) {
            Log.e(TAG, "failed to copy bootanimation");
        }
        if (ShCmd.shellCmmandIsSafe("/system/bin/busybox chmod 755 /system/media/bootanimation.zip") != 0) {
            Log.e(TAG, "failed to change mod bootanimation");
        }
        ShCmd.shellCmmandIsSafe("/system/bin/busybox chmod 755 /system/media/bootanimation.zip");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static void standardInstallApplication(java.lang.String r5_path, java.lang.String r6_type) {
        throw new UnsupportedOperationException("Method not decompiled: com.clouds.install.StandardFileInstall.standardInstallApplication(java.lang.String, java.lang.String):void");
        /*
        r2 = TAG;
        r3 = new java.lang.StringBuilder;
        r4 = "isRKDevice: ";
        r3.<init>(r4);
        r4 = com.clouds.util.DistinguishDevice.isRKDevice();
        r3 = r3.append(r4);
        r4 = "  type\uff1a ";
        r3 = r3.append(r4);
        r3 = r3.append(r6);
        r3 = r3.toString();
        com.clouds.debug.SystemDebug.e(r2, r3);
        r2 = "/";
        r1 = r5.lastIndexOf(r2);
        r2 = new java.lang.StringBuilder;
        r3 = "/system/app/";
        r2.<init>(r3);
        r3 = r5.substring(r1);
        r2 = r2.append(r3);
        r0 = r2.toString();
        r2 = new java.lang.StringBuilder;
        r3 = "/system/bin/busybox chmod 755 ";
        r2.<init>(r3);
        r2 = r2.append(r5);
        r2 = r2.toString();
        r2 = com.clouds.server.ShCmd.shellCmmandIsSafe(r2);
        if (r2 == 0) goto L_0x0057;
    L_0x0050:
        r2 = TAG;
        r3 = "failed to change mod package";
        android.util.Log.e(r2, r3);
    L_0x0057:
        if (r6 == 0) goto L_0x007e;
    L_0x0059:
        r2 = "user";
        r2 = r6.equals(r2);
        if (r2 == 0) goto L_0x007e;
    L_0x0061:
        r2 = new java.lang.StringBuilder;
        r3 = "/system/bin/pm install -r ";
        r2.<init>(r3);
        r2 = r2.append(r5);
        r2 = r2.toString();
        r2 = com.clouds.server.ShCmd.shellCmmandIsSafe(r2);
        if (r2 == 0) goto L_0x007d;
    L_0x0076:
        r2 = TAG;
        r3 = "failed to install package";
        android.util.Log.e(r2, r3);
    L_0x007d:
        return;
    L_0x007e:
        if (r6 == 0) goto L_0x007d;
    L_0x0080:
        r2 = "system";
        r2 = r6.equals(r2);
        if (r2 == 0) goto L_0x007d;
    L_0x0088:
        r2 = new java.lang.StringBuilder;
        r3 = "/system/bin/busybox cp ";
        r2.<init>(r3);
        r2 = r2.append(r5);
        r3 = " ";
        r2 = r2.append(r3);
        r3 = "/system/app/";
        r2 = r2.append(r3);
        r2 = r2.toString();
        r2 = com.clouds.server.ShCmd.shellCmmandIsSafe(r2);
        if (r2 == 0) goto L_0x00b0;
    L_0x00a9:
        r2 = TAG;
        r3 = "failed to install package";
        android.util.Log.e(r2, r3);
    L_0x00b0:
        r2 = new java.lang.StringBuilder;
        r3 = "/system/bin/busybox chmod 644 ";
        r2.<init>(r3);
        r2 = r2.append(r0);
        r2 = r2.toString();
        r2 = com.clouds.server.ShCmd.shellCmmandIsSafe(r2);
        if (r2 == 0) goto L_0x007d;
    L_0x00c5:
        r2 = TAG;
        r3 = "failed to install package";
        android.util.Log.e(r2, r3);
        goto L_0x007d;
        */
    }

    static void standardInstallCopyDeleteAPPInfo(String path) {
        SystemDebug.e(TAG, new StringBuilder("isRKDevice: ").append(DistinguishDevice.isRKDevice()).append(" path: ").append(path).toString());
        if (ShCmd.shellCmmandIsSafe(new StringBuilder(CP).append(path).append(" ").append(DELETEAPPINFO_HOME_PAPER_TAG_PATH).toString()) != 0) {
            Log.e(TAG, "failed to copy browser homepaper");
        }
        if (ShCmd.shellCmmandIsSafe("/system/bin/busybox chmod 755 /system/etc/deleteappinfo.xml") != 0) {
            Log.e(TAG, "failed to change mod browser homepaper");
        }
    }

    static void standardInstallHomePage(String path) {
        SystemDebug.e(TAG, new StringBuilder("isRKDevice: ").append(DistinguishDevice.isRKDevice()).append(" path: ").append(path).toString());
        if (ShCmd.shellCmmandIsSafe(new StringBuilder(CP).append(path).append(" ").append(BROWSER_HOME_PAPER_TAG_PATH).toString()) != 0) {
            Log.e(TAG, "failed to copy browser homepaper");
        }
        if (ShCmd.shellCmmandIsSafe("/system/bin/busybox chmod 755 /system/etc/bhp") != 0) {
            Log.e(TAG, "failed to change mod browser homepaper");
        }
    }

    static void standardInstallHomePages(String path) {
        SystemDebug.e(TAG, new StringBuilder("isRKDevice: ").append(DistinguishDevice.isRKDevice()).append(" path: ").append(path).toString());
        if (ShCmd.shellCmmandIsSafe(new StringBuilder(CP).append(path).append(" ").append(BROWSER_HOME_PAPERS_TAG_PATH).toString()) != 0) {
            Log.e(TAG, "failed to copy browser homepaper");
        }
        if (ShCmd.shellCmmandIsSafe("/system/bin/busybox chmod 755 /system/etc/homepage") != 0) {
            Log.e(TAG, "failed to change mod browser homepaper");
        }
    }
}