package com.clouds.debug;

import android.content.Context;
import android.util.Log;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SystemDebug {
    public static void d(String tag, String msg) {
        Log.d(tag, msg);
    }

    public static void e(String tag, String msg) {
        Log.e(tag, msg);
    }

    public static void saveLogToFile(Context context, String tag, String msg) {
    }

    public static boolean setPathToFile(Context context, String msg) {
        try {
            File file = new File("data/data/com.clouds.server/", "Cloud_LOG.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file, true);
            fw.write(new StringBuilder(String.valueOf(msg)).append("\n").toString());
            fw.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}