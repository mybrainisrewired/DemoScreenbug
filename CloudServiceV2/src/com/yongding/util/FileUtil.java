package com.yongding.util;

import android.content.Context;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    public static List<String[]> readTxt(Context context) {
        List<String[]> list = new ArrayList();
        try {
            InputStream is = context.getResources().getAssets().open("list.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
            String str = "";
            while (true) {
                str = br.readLine();
                if (str == null) {
                    break;
                } else if (!"".equals(str)) {
                    list.add(str.trim().split(","));
                }
            }
            br.close();
            is.close();
        } catch (Exception e) {
            Trace.e(e.toString());
        }
        return list;
    }
}