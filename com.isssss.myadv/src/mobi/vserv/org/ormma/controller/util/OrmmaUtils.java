package mobi.vserv.org.ormma.controller.util;

import android.os.Bundle;

public class OrmmaUtils {
    private static final String CHAR_SET = "ISO-8859-1";

    public static String byteToHex(byte b) {
        char[] hexDigit = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        return new String(new char[]{hexDigit[(b >> 4) & 15], hexDigit[b & 15]});
    }

    public static String convert(String str) {
        try {
            byte[] array = str.getBytes();
            StringBuffer buffer = new StringBuffer();
            int k = 0;
            while (k < array.length) {
                if ((array[k] & 128) > 0) {
                    buffer.append(new StringBuilder("%").append(byteToHex(array[k])).toString());
                } else {
                    buffer.append((char) array[k]);
                }
                k++;
            }
            return new String(buffer.toString().getBytes(), CHAR_SET);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getData(String key, Bundle data) {
        return data.getString(key);
    }
}