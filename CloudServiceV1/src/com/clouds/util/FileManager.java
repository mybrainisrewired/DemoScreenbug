package com.clouds.util;

import android.content.Context;
import android.util.Log;
import com.clouds.debug.SystemDebug;
import com.clouds.install.FileInstall;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

public class FileManager {
    private static final String TAG;

    class AnonymousClass_1 implements Runnable {
        private final /* synthetic */ File val$dataFile;

        AnonymousClass_1(File file) {
            this.val$dataFile = file;
        }

        public void run() {
            try {
                Log.i(TAG, "delete Files");
                Thread.sleep(120000);
                File file = this.val$dataFile;
                if (file != null && file.exists()) {
                    Log.i(TAG, new StringBuilder("isDelete------isDelete: ").append(FileManager.deleteDir(file)).toString());
                }
                FileManager.setSystemR();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static {
        TAG = FileManager.class.getSimpleName();
    }

    public static String copyAssetsScript(Context context, String scriptSavePath, String fileName) {
        FileOutputStream fos;
        String scriptPath = new StringBuilder(String.valueOf(scriptSavePath)).append(File.separator).append(fileName).toString();
        SystemDebug.e(TAG, new StringBuilder("copyAssetsScript  scriptPath: ").append(scriptPath).toString());
        InputStream is = null;
        try {
            File fileDir = new File(scriptSavePath);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            File file = new File(scriptSavePath, fileName);
            if (file.exists()) {
                try {
                    is.close();
                    Log.i(TAG, "script copy finish");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                file.createNewFile();
                FileOutputStream fos2 = new FileOutputStream(file, false);
                try {
                    is = context.getAssets().open(fileName);
                    byte[] buffer = new byte[1024];
                    while (is != null) {
                        int length = is.read(buffer);
                        if (length != -1) {
                            fos2.write(buffer, 0, length);
                        }
                    }
                    try {
                        break;
                        is.close();
                        Log.i(TAG, "script copy finish");
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                } catch (Exception e3) {
                    e = e3;
                } catch (Throwable th) {
                    th = th;
                }
            }
        } catch (Exception e4) {
            e = e4;
            try {
                Exception e5;
                e5.printStackTrace();
                try {
                    is.close();
                    Log.i(TAG, "script copy finish");
                } catch (Exception e6) {
                    e6.printStackTrace();
                }
            } catch (Throwable th2) {
                Throwable th3;
                th3 = th2;
                try {
                    is.close();
                    Log.i(TAG, "script copy finish");
                } catch (Exception e7) {
                    e7.printStackTrace();
                }
                throw th3;
            }
            return scriptPath;
        }
        return scriptPath;
    }

    public static boolean createFileDir(Context context, String dirPath, String newPath) {
        File fileDir = new File(dirPath);
        if (!fileDir.exists()) {
            Log.i(TAG, new StringBuilder("createFileDir: ").append(fileDir.mkdirs()).toString());
        }
        File newFilePath = new File(dirPath, newPath);
        if (!newFilePath.exists()) {
            Log.i(TAG, new StringBuilder("createFileDir newFilePath: ").append(newFilePath.mkdirs()).toString());
        }
        return true;
    }

    public static boolean createFileDir(String dirPath) {
        File fileDir = new File(dirPath);
        if (!fileDir.exists()) {
            Log.i(TAG, new StringBuilder("createFileDir: ").append(fileDir.mkdirs()).toString());
        }
        File FileNomedia = new File(new StringBuilder(String.valueOf(dirPath)).append(File.separator).append(".nomedia").toString());
        if (!FileNomedia.exists()) {
            try {
                Log.i(TAG, new StringBuilder("FileNomedia createFileDir: ").append(FileNomedia.createNewFile()).toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static void deleteDataFiles(File dataFile) {
        new Thread(new AnonymousClass_1(dataFile)).start();
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            int i = 0;
            while (i < children.length) {
                if (!deleteDir(new File(dir, children[i]))) {
                    return false;
                }
                i++;
            }
        }
        return dir.delete();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void getOtherFile(java.lang.String r12_otherPath, java.lang.String r13_savepath) {
        throw new UnsupportedOperationException("Method not decompiled: com.clouds.util.FileManager.getOtherFile(java.lang.String, java.lang.String):void");
        /*
        r7 = new java.io.File;
        r7.<init>(r12);
        r8 = new java.io.File;
        r8.<init>(r13);
        r2 = 0;
        r4 = 0;
        r9 = r7.exists();	 Catch:{ FileNotFoundException -> 0x00af, IOException -> 0x0069 }
        if (r9 == 0) goto L_0x0035;
    L_0x0012:
        r9 = r8.exists();	 Catch:{ FileNotFoundException -> 0x00af, IOException -> 0x0069 }
        if (r9 != 0) goto L_0x001b;
    L_0x0018:
        r8.createNewFile();	 Catch:{ FileNotFoundException -> 0x00af, IOException -> 0x0069 }
    L_0x001b:
        r3 = new java.io.FileInputStream;	 Catch:{ FileNotFoundException -> 0x00af, IOException -> 0x0069 }
        r3.<init>(r7);	 Catch:{ FileNotFoundException -> 0x00af, IOException -> 0x0069 }
        r5 = new java.io.FileOutputStream;	 Catch:{ FileNotFoundException -> 0x00b1, IOException -> 0x00a8, all -> 0x00a1 }
        r5.<init>(r13);	 Catch:{ FileNotFoundException -> 0x00b1, IOException -> 0x00a8, all -> 0x00a1 }
        r9 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r0 = new byte[r9];	 Catch:{ FileNotFoundException -> 0x004c, IOException -> 0x00ab, all -> 0x00a4 }
        r6 = -1;
    L_0x002a:
        if (r3 == 0) goto L_0x00b4;
    L_0x002c:
        r6 = r3.read(r0);	 Catch:{ FileNotFoundException -> 0x004c, IOException -> 0x00ab, all -> 0x00a4 }
        r9 = -1;
        if (r6 != r9) goto L_0x0047;
    L_0x0033:
        r4 = r5;
        r2 = r3;
    L_0x0035:
        if (r2 == 0) goto L_0x003a;
    L_0x0037:
        r2.close();	 Catch:{ Exception -> 0x009c }
    L_0x003a:
        if (r4 == 0) goto L_0x003f;
    L_0x003c:
        r4.close();	 Catch:{ Exception -> 0x009c }
    L_0x003f:
        r9 = TAG;	 Catch:{ Exception -> 0x009c }
        r10 = "getOtherFile finish";
        android.util.Log.i(r9, r10);	 Catch:{ Exception -> 0x009c }
    L_0x0046:
        return;
    L_0x0047:
        r9 = 0;
        r5.write(r0, r9, r6);	 Catch:{ FileNotFoundException -> 0x004c, IOException -> 0x00ab, all -> 0x00a4 }
        goto L_0x002a;
    L_0x004c:
        r1 = move-exception;
        r4 = r5;
        r2 = r3;
    L_0x004f:
        r1.printStackTrace();	 Catch:{ all -> 0x0084 }
        if (r2 == 0) goto L_0x0057;
    L_0x0054:
        r2.close();	 Catch:{ Exception -> 0x0064 }
    L_0x0057:
        if (r4 == 0) goto L_0x005c;
    L_0x0059:
        r4.close();	 Catch:{ Exception -> 0x0064 }
    L_0x005c:
        r9 = TAG;	 Catch:{ Exception -> 0x0064 }
        r10 = "getOtherFile finish";
        android.util.Log.i(r9, r10);	 Catch:{ Exception -> 0x0064 }
        goto L_0x0046;
    L_0x0064:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0046;
    L_0x0069:
        r1 = move-exception;
    L_0x006a:
        r1.printStackTrace();	 Catch:{ all -> 0x0084 }
        if (r2 == 0) goto L_0x0072;
    L_0x006f:
        r2.close();	 Catch:{ Exception -> 0x007f }
    L_0x0072:
        if (r4 == 0) goto L_0x0077;
    L_0x0074:
        r4.close();	 Catch:{ Exception -> 0x007f }
    L_0x0077:
        r9 = TAG;	 Catch:{ Exception -> 0x007f }
        r10 = "getOtherFile finish";
        android.util.Log.i(r9, r10);	 Catch:{ Exception -> 0x007f }
        goto L_0x0046;
    L_0x007f:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0046;
    L_0x0084:
        r9 = move-exception;
    L_0x0085:
        if (r2 == 0) goto L_0x008a;
    L_0x0087:
        r2.close();	 Catch:{ Exception -> 0x0097 }
    L_0x008a:
        if (r4 == 0) goto L_0x008f;
    L_0x008c:
        r4.close();	 Catch:{ Exception -> 0x0097 }
    L_0x008f:
        r10 = TAG;	 Catch:{ Exception -> 0x0097 }
        r11 = "getOtherFile finish";
        android.util.Log.i(r10, r11);	 Catch:{ Exception -> 0x0097 }
    L_0x0096:
        throw r9;
    L_0x0097:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0096;
    L_0x009c:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0046;
    L_0x00a1:
        r9 = move-exception;
        r2 = r3;
        goto L_0x0085;
    L_0x00a4:
        r9 = move-exception;
        r4 = r5;
        r2 = r3;
        goto L_0x0085;
    L_0x00a8:
        r1 = move-exception;
        r2 = r3;
        goto L_0x006a;
    L_0x00ab:
        r1 = move-exception;
        r4 = r5;
        r2 = r3;
        goto L_0x006a;
    L_0x00af:
        r1 = move-exception;
        goto L_0x004f;
    L_0x00b1:
        r1 = move-exception;
        r2 = r3;
        goto L_0x004f;
    L_0x00b4:
        r4 = r5;
        r2 = r3;
        goto L_0x0035;
        */
    }

    public static String readFileContentInfo(String filePath) {
        String str = "";
        try {
            FileInputStream fis = new FileInputStream(new File(filePath));
            byte[] buffer = new byte[1024];
            str = new String();
            while (true) {
                int lenth = fis.read(buffer);
                if (lenth == -1) {
                    fis.close();
                    return str.trim();
                } else {
                    lenth = fis.read(buffer, 0, lenth);
                    str = new StringBuilder(String.valueOf(str)).append(new String(buffer)).toString();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public static boolean setPathToFile(String absPath, String absName, String filePath) {
        boolean isSuccess;
        SystemDebug.e(TAG, new StringBuilder("setPathToFile absPath: ").append(absPath).append(" absName: ").append(absName).append(" filePath: ").append(filePath).toString());
        try {
            File file = new File(absPath, absName);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file, false);
            fw.write(filePath);
            fw.close();
            isSuccess = true;
        } catch (IOException e) {
            e.printStackTrace();
            isSuccess = false;
        }
        SystemDebug.e(TAG, new StringBuilder("setPathToFile isSuccess: ").append(isSuccess).toString());
        return isSuccess;
    }

    public static void setSystemR() {
        try {
            FileInstall.getFileInstallInstance().remountSystemR();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e2) {
            e2.printStackTrace();
        }
    }

    public static void setSystemRW() {
        try {
            FileInstall.getFileInstallInstance().remountSystemRW();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e2) {
            e2.printStackTrace();
        }
    }
}