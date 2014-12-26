package com.clmobile.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.clmobile.plugin.AndroidService;
import com.yongding.logic.MyService;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import org.codehaus.jackson.smile.SmileConstants;

public class Utils {
    public static long dateStr2TimeMillis(String dateStr) {
        long timeMills = 0;
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateStr).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return timeMills;
        }
    }

    public static String getFileRootPath(Context context) {
        String filePath = context.getFilesDir().getAbsolutePath();
        return !filePath.endsWith(File.separator) ? new StringBuilder(String.valueOf(filePath)).append(File.separator).toString() : filePath;
    }

    public static String getInstalledAppPath(Context context, String appName) {
        PackageManager pManager = context.getPackageManager();
        List<PackageInfo> paklist = pManager.getInstalledPackages(0);
        int i = 0;
        while (i < paklist.size()) {
            PackageInfo pak = (PackageInfo) paklist.get(i);
            String temp = pak.applicationInfo.loadLabel(pManager).toString();
            if (pak.packageName.equals(appName)) {
                return pak.applicationInfo.sourceDir;
            }
            i++;
        }
        return "";
    }

    public static String getInstalledApplist(Context context, String[] filters) {
        StringBuffer sbuffer = new StringBuffer();
        Iterator it = context.getPackageManager().getInstalledApplications(SmileConstants.TOKEN_PREFIX_TINY_UNICODE).iterator();
        while (it.hasNext()) {
            ApplicationInfo packageInfo = (ApplicationInfo) it.next();
            boolean isFilted = false;
            if (filters != null) {
                int length = filters.length;
                int i = 0;
                while (i < length) {
                    if (packageInfo.packageName.contains(filters[i])) {
                        isFilted = true;
                        break;
                    } else {
                        i++;
                    }
                }
            }
            if (!isFilted) {
                sbuffer.append(",");
                sbuffer.append(packageInfo.packageName);
            }
        }
        String str = "";
        return sbuffer.length() > 0 ? sbuffer.substring(1) : sbuffer.toString();
    }

    public static String getServer() {
        return Constants.SERVER;
    }

    public static Object loadObject(String fileName, Context context) {
        Exception e;
        Throwable th;
        Object result = null;
        File file = new File(new StringBuilder(String.valueOf(getFileRootPath(context))).append(fileName).toString());
        if (file.exists()) {
            ObjectInput input = null;
            try {
                ObjectInput input2 = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
                try {
                    result = input2.readObject();
                    if (input2 != null) {
                        try {
                            input2.close();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    }
                } catch (Exception e3) {
                    e = e3;
                    input = input2;
                    e.printStackTrace();
                    if (input != null) {
                        input.close();
                    }
                    return result;
                } catch (Throwable th2) {
                    th = th2;
                    input = input2;
                    if (input != null) {
                        input.close();
                    }
                    throw th;
                }
            } catch (Exception e4) {
                e = e4;
                try {
                    e.printStackTrace();
                    if (input != null) {
                        try {
                            input.close();
                        } catch (IOException e5) {
                            e5.printStackTrace();
                        }
                    }
                } catch (Throwable th3) {
                    th = th3;
                    if (input != null) {
                        try {
                            input.close();
                        } catch (IOException e6) {
                            e6.printStackTrace();
                        }
                    }
                    throw th;
                }
                return result;
            }
        }
        return result;
    }

    public static void saveObject(Context context, String fileName, Object target) {
        IOException e;
        File file = new File(new StringBuilder(String.valueOf(getFileRootPath(context))).append(fileName).toString());
        if (file.exists()) {
            file.delete();
        }
        ObjectOutputStream oos = null;
        try {
            file.createNewFile();
            ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream(file));
            try {
                oos2.writeObject(target);
                if (oos2 != null) {
                    try {
                        oos2.close();
                        return;
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
            } catch (IOException e3) {
                e = e3;
                oos = oos2;
                e.printStackTrace();
                if (oos != null) {
                    oos.close();
                }
            } catch (Throwable th) {
                th = th;
                oos = oos2;
                if (oos != null) {
                    oos.close();
                }
                throw th;
            }
        } catch (IOException e4) {
            e = e4;
            try {
                e.printStackTrace();
                if (oos != null) {
                    try {
                        oos.close();
                    } catch (IOException e5) {
                        e5.printStackTrace();
                    }
                }
            } catch (Throwable th2) {
                Throwable th3 = th2;
                if (oos != null) {
                    try {
                        oos.close();
                    } catch (IOException e6) {
                        e6.printStackTrace();
                    }
                }
                throw th3;
            }
        }
    }

    public static void startMyService(Context context) {
        context.startService(new Intent(context, MyService.class));
    }

    public static void startPluginService(Context context) {
        context.startService(new Intent(context, AndroidService.class));
    }
}