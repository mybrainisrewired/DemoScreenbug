package com.mopub.common.util;

import com.mopub.common.Preconditions;
import java.security.MessageDigest;
import java.util.concurrent.atomic.AtomicLong;

public class Utils {
    private static final AtomicLong sNextGeneratedId;

    static {
        sNextGeneratedId = new AtomicLong(1);
    }

    public static boolean bitMaskContainsFlag(int bitMask, int flag) {
        return (bitMask & flag) != 0;
    }

    public static long generateUniqueId() {
        long result;
        long newValue;
        do {
            result = sNextGeneratedId.get();
            newValue = result + 1;
            if (newValue > 9223372036854775806L) {
                newValue = 1;
            }
        } while (!sNextGeneratedId.compareAndSet(result, newValue));
        return result;
    }

    public static String sha1(String string) {
        int i = 0;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] bytes = string.getBytes("UTF-8");
            digest.update(bytes, 0, bytes.length);
            int length = digest.digest().length;
            while (i < length) {
                stringBuilder.append(String.format("%02X", new Object[]{Byte.valueOf(bytes[i])}));
                i++;
            }
            return stringBuilder.toString().toLowerCase();
        } catch (Exception e) {
            return Preconditions.EMPTY_ARGUMENTS;
        }
    }
}