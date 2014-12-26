package com.clmobile.plugin.task;

import android.content.Context;
import com.clmobile.utils.FileUtils;
import com.clmobile.utils.Helper;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AppDownLoadTask {
    public static File downFile(Context context, String urlStr, String fileName) {
        InputStream inputStream = null;
        File resultFile = null;
        try {
            FileUtils fileUtils = new FileUtils();
            inputStream = getInputStreamFromURL(urlStr);
            if (inputStream == null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            } else {
                if (FileUtils.isSDCardReady()) {
                    resultFile = fileUtils.write2SDFromInput("", fileName, inputStream);
                } else {
                    resultFile = fileUtils.write2ContextDirFromInput(context, fileName, inputStream);
                }
                try {
                    inputStream.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                return resultFile;
            }
        } catch (Exception e3) {
            try {
                e3.printStackTrace();
                try {
                    inputStream.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            } catch (Throwable th) {
                try {
                    inputStream.close();
                } catch (IOException e5) {
                    e5.printStackTrace();
                }
            }
        }
    }

    public static InputStream getInputStreamFromURL(String urlStr) {
        int i = 0;
        while (i < 3) {
            try {
                return ((HttpURLConnection) new URL(urlStr).openConnection()).getInputStream();
            } catch (Exception e) {
                try {
                    Thread.sleep((long) ((i * 30) * 1000));
                } catch (InterruptedException e2) {
                }
                i++;
            }
        }
        return null;
    }

    public static boolean performInstall(Context context, String url, String apkName, String packageName) {
        File file = downFile(context, url, apkName);
        if (file == null) {
            return false;
        }
        boolean result = Helper.installApp(context, packageName, apkName, file);
        file.delete();
        return result;
    }
}