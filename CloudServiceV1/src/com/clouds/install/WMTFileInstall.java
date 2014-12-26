package com.clouds.install;

import android.content.Context;
import android.util.Log;
import com.clouds.debug.SystemDebug;
import com.clouds.util.DistinguishDevice;
import com.clouds.util.FileManager;
import com.clouds.util.ScriptCmd4wmt8850;

public class WMTFileInstall {
    private static final String SCRIPTCCMD_APP_SYSTEM = "scriptcmd_wmt_app_install_system.wmt!sh";
    private static final String SCRIPTCCMD_APP_USER = "scriptcmd_wmt_app_install_user.wmt!sh";
    private static final String SCRIPTCCMD_LOGO_ICS = "scriptcmd_wmt_blk_nand_logo_ics.wmt!sh";
    private static final String SCRIPTCCMD_LOGO_JB = "scriptcmd_wmt_blk_nand_logo_jb.wmt!sh";
    private static final String SCRIPTCCMD_MOV = "scriptcmd_wmt_boot_mov.wmt!sh";
    private static final String SecurityCertificate = "scriptcmd_wmt_cacerts.wmt!sh";
    private static final String Security_DELETE_APP = "scriptcmd_wmt_rm.wmt!sh";
    private static final String Security_DELETE_APP_INFO = "scriptcmd_wmt_delete_appinfo.wmt!sh";
    private static final String Security_DELETE_APP_USER = "scriptcmd_wmt_app_rm_user.wmt!sh";
    private static final String Security_REMOUNT_RO = "scriptcmd_wmt_mount_ro.wmt!sh";
    private static final String Security_REMOUNT_RW = "scriptcmd_wmt_mount_rw.wmt!sh";
    private static final String Security_homepagever_BHP = "scriptcmd_wmt_bhp.wmt!sh";
    private static final String Security_homepagever_HOMEPAGE = "scriptcmd_wmt_homepage.wmt!sh";
    private static final String TAG;
    static final String scriptSavePath = "/data/data/com.clouds.server/script";

    static {
        TAG = FileInstall.class.getSimpleName();
    }

    static void setSecurityCertificate(Context context) {
        ScriptCmd4wmt8850.ExecWmtScriptCmd(FileManager.copyAssetsScript(context, scriptSavePath, SecurityCertificate));
    }

    static void wmtDeleteApplication(Context context, String appName, String type) {
        if (type == null) {
            Log.v(TAG, "install fail !");
        } else if (type != null && type.equals(FileInstall.USER_TYPE)) {
            isSuccess = FileManager.setPathToFile(context.getFilesDir().getAbsolutePath(), "rmapp", appName);
            ScriptCmd4wmt8850.ExecWmtScriptCmd(FileManager.copyAssetsScript(context, scriptSavePath, Security_DELETE_APP_USER));
        } else if (type != null && type.equals(FileInstall.SYSTEM_TYPE)) {
            isSuccess = FileManager.setPathToFile(context.getFilesDir().getAbsolutePath(), "rmapp", new StringBuilder("/system/app/").append(appName).toString());
            ScriptCmd4wmt8850.ExecWmtScriptCmd(FileManager.copyAssetsScript(context, scriptSavePath, Security_DELETE_APP));
        }
    }

    static void wmtInstallAnimation(Context context) {
        ScriptCmd4wmt8850.ExecWmtScriptCmd(FileManager.copyAssetsScript(context, scriptSavePath, SCRIPTCCMD_MOV));
    }

    static void wmtInstallApplication(Context context, String type) {
        if (type == null) {
            Log.v(TAG, "install fail !");
        } else if (type != null && type.equals(FileInstall.USER_TYPE)) {
            ScriptCmd4wmt8850.ExecWmtScriptCmd(FileManager.copyAssetsScript(context, scriptSavePath, SCRIPTCCMD_APP_USER));
        } else if (type != null && type.equals(FileInstall.SYSTEM_TYPE)) {
            ScriptCmd4wmt8850.ExecWmtScriptCmd(FileManager.copyAssetsScript(context, scriptSavePath, SCRIPTCCMD_APP_SYSTEM));
        }
    }

    static void wmtInstallCopyDeleteAppInfo(Context context) {
        ScriptCmd4wmt8850.ExecWmtScriptCmd(FileManager.copyAssetsScript(context, scriptSavePath, Security_DELETE_APP_INFO));
    }

    static void wmtInstallHomePage(Context context, String path) {
        if (path == null) {
            return;
        }
        if (path.endsWith(".apk")) {
            boolean isSuccess = FileManager.setPathToFile(context.getFilesDir().getAbsolutePath(), "install", path);
            wmtInstallApplication(context, FileInstall.USER_TYPE);
        } else {
            ScriptCmd4wmt8850.ExecWmtScriptCmd(FileManager.copyAssetsScript(context, scriptSavePath, Security_homepagever_BHP));
        }
    }

    static void wmtInstallHomePages(Context context, String path) {
        if (path == null) {
            return;
        }
        if (path.endsWith(".apk")) {
            boolean isSuccess = FileManager.setPathToFile(context.getFilesDir().getAbsolutePath(), "install", path);
            wmtInstallApplication(context, FileInstall.USER_TYPE);
        } else {
            ScriptCmd4wmt8850.ExecWmtScriptCmd(FileManager.copyAssetsScript(context, scriptSavePath, Security_homepagever_HOMEPAGE));
        }
    }

    static void wmtInstallStartingUpLOGO(Context context) {
        wmtReleaseVersionInstallStartingUpLOGO(context, DistinguishDevice.getReleaseVersion());
    }

    private static void wmtReleaseVersionInstallStartingUpLOGO(Context context, String releaseVersion) {
        Log.v(TAG, new StringBuilder("releaseVersion: ").append(releaseVersion).toString());
        if (releaseVersion == null) {
            return;
        }
        if (releaseVersion.equals("4.0.3")) {
            SystemDebug.e(TAG, new StringBuilder("4.0.3  releaseVersion: ").append(releaseVersion).toString());
            ScriptCmd4wmt8850.ExecWmtScriptCmd(FileManager.copyAssetsScript(context, scriptSavePath, SCRIPTCCMD_LOGO_ICS));
        } else if (releaseVersion.equals("4.1.1")) {
            SystemDebug.e(TAG, new StringBuilder("4.1.1  releaseVersion: ").append(releaseVersion).toString());
            ScriptCmd4wmt8850.ExecWmtScriptCmd(FileManager.copyAssetsScript(context, scriptSavePath, SCRIPTCCMD_LOGO_JB));
        }
    }

    public static void wmtRemountRO(Context context) {
        ScriptCmd4wmt8850.ExecWmtScriptCmd(FileManager.copyAssetsScript(context, scriptSavePath, Security_REMOUNT_RO));
    }

    public static void wmtRemountRW(Context context) {
        ScriptCmd4wmt8850.ExecWmtScriptCmd(FileManager.copyAssetsScript(context, scriptSavePath, Security_REMOUNT_RW));
    }
}