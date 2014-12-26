package com.clmobile.utils;

import android.content.Context;
import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {
    private int FILESIZE;
    private String SDPATH;

    public FileUtils() {
        this.FILESIZE = 4096;
        this.SDPATH = Environment.getExternalStorageDirectory() + "/";
    }

    public static String getSDCardPath() {
        return isSDCardReady() ? new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getPath())).append(File.separator).toString() : null;
    }

    public static boolean isSDCardReady() {
        return "mounted".equals(Environment.getExternalStorageState());
    }

    public File createCacheFile(Context context, String fileName) throws IOException {
        File file = new File(new StringBuilder(String.valueOf(context.getCacheDir().getAbsolutePath())).append(fileName).toString());
        file.createNewFile();
        return file;
    }

    public File createSDDir(String dirName) {
        File dir = new File(new StringBuilder(String.valueOf(this.SDPATH)).append(dirName).toString());
        dir.mkdir();
        return dir;
    }

    public File createSDFile(String fileName) throws IOException {
        File file = new File(new StringBuilder(String.valueOf(this.SDPATH)).append(fileName).toString());
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        return file;
    }

    public String getSDPATH() {
        return this.SDPATH;
    }

    public boolean isFileExist(String fileName) {
        return new File(new StringBuilder(String.valueOf(this.SDPATH)).append(fileName).toString()).exists();
    }

    public File write2ContextDirFromInput(Context context, String fileName, InputStream input) {
        File file = null;
        OutputStream output = null;
        try {
            file = createCacheFile(context, fileName);
            OutputStream output2 = new FileOutputStream(file);
            try {
                byte[] buffer = new byte[this.FILESIZE];
                while (input.read(buffer) != -1) {
                    output2.write(buffer);
                }
                output2.flush();
                try {
                    output2.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e2) {
                e = e2;
                output = output2;
            } catch (Throwable th) {
                th = th;
                output = output2;
            }
        } catch (Exception e3) {
            e = e3;
            try {
                Exception e4;
                e4.printStackTrace();
                try {
                    output.close();
                } catch (IOException e5) {
                    e5.printStackTrace();
                }
            } catch (Throwable th2) {
                Throwable th3;
                th3 = th2;
                try {
                    output.close();
                } catch (IOException e6) {
                    e6.printStackTrace();
                }
                throw th3;
            }
            return file;
        }
        return file;
    }

    public File write2SDFromInput(String path, String fileName, InputStream input) {
        File file = null;
        OutputStream output = null;
        try {
            if (!path.equals("")) {
                createSDDir(path);
            }
            file = createSDFile(new StringBuilder(String.valueOf(path)).append(fileName).toString());
            OutputStream output2 = new FileOutputStream(file);
            try {
                byte[] buffer = new byte[this.FILESIZE];
            } catch (Exception e) {
                e = e;
                output = output2;
            } catch (Throwable th) {
                th = th;
                output = output2;
            }
            while (true) {
                int len = input.read(buffer);
                if (len == -1) {
                    output2.flush();
                    try {
                        output2.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                    return file;
                } else {
                    output2.write(buffer, 0, len);
                }
            }
        } catch (Exception e3) {
            e = e3;
            try {
                Exception e4;
                e4.printStackTrace();
                try {
                    output.close();
                } catch (IOException e5) {
                    e5.printStackTrace();
                }
            } catch (Throwable th2) {
                Throwable th3;
                th3 = th2;
                try {
                    output.close();
                } catch (IOException e6) {
                    e6.printStackTrace();
                }
                throw th3;
            }
            return file;
        }
    }
}