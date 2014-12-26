package com.facebook.ads.internal;

import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import java.security.MessageDigest;

public class StringUtils {
    public static boolean isNullOrEmpty(String checkString) {
        return checkString == null || checkString.length() <= 0;
    }

    public static String md5(String input) {
        try {
            byte[] hash = MessageDigest.getInstance("MD5").digest(input.getBytes("utf-8"));
            StringBuilder sb = new StringBuilder();
            int i = 0;
            while (i < hash.length) {
                sb.append(Integer.toString(hash[i] & 255 + 256, ApiEventType.API_MRAID_GET_ORIENTATION).substring(1));
                i++;
            }
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }
}