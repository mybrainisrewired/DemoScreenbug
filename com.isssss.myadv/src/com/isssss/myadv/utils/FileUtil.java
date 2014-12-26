package com.isssss.myadv.utils;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {
    public static void copyFile(String source, String dest) {
        File from = new File(source);
        File to = new File(dest);
        if (from.exists() && from.isFile() && from.canRead()) {
            if (!to.getParentFile().exists()) {
                to.getParentFile().mkdirs();
            }
            try {
                FileInputStream fosfrom = new FileInputStream(from);
                FileOutputStream fosto = new FileOutputStream(to);
                byte[] bt = new byte[1024];
                while (true) {
                    int c = fosfrom.read(bt);
                    if (c <= 0) {
                        fosfrom.close();
                        fosto.close();
                        return;
                    } else {
                        fosto.write(bt, 0, c);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static File createFile(Context context, String root, String name) {
        File dir;
        if (isSDCardExist()) {
            dir = new File(Environment.getExternalStorageDirectory() + root);
        } else {
            dir = new File(new StringBuilder(String.valueOf(context.getCacheDir().getAbsolutePath())).append(root).toString());
        }
        if (!(dir == null || dir.exists())) {
            dir.mkdirs();
        }
        File file = new File(Environment.getExternalStorageDirectory() + root + "/" + name);
        try {
            file.createNewFile();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void deleteDir(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            int length = files.length;
            int i = 0;
            while (i < length) {
                deleteDir(files[i]);
                i++;
            }
        } else {
            dir.delete();
        }
    }

    public static String getFileName(String pathandName) {
        int start = pathandName.lastIndexOf("/");
        return -1 != start ? pathandName.substring(start + 1) : null;
    }

    public static int getFileSize(String pathandName) {
        try {
            return new FileInputStream(new File(pathandName)).available();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getFileSuffixType(String pathandName) {
        int start = pathandName.lastIndexOf(".");
        return -1 != start ? pathandName.substring(start) : null;
    }

    public static BitmapDrawable getImageFromAssetsFile(String fileName, Context context) {
        BitmapDrawable drawable = null;
        try {
            InputStream is = context.getResources().getAssets().open(fileName);
            BitmapDrawable drawable2 = new BitmapDrawable(BitmapFactory.decodeStream(is));
            try {
                is.close();
                return drawable2;
            } catch (IOException e) {
                e = e;
                drawable = drawable2;
                e.printStackTrace();
                return drawable;
            }
        } catch (IOException e2) {
            IOException e3 = e2;
            e3.printStackTrace();
            return drawable;
        }
    }

    public static boolean isSDCardExist() {
        return Environment.getExternalStorageState().equals("mounted");
    }
}