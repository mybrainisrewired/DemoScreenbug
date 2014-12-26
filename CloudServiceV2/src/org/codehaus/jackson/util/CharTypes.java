package org.codehaus.jackson.util;

import java.util.Arrays;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.smile.SmileConstants;

public final class CharTypes {
    private static final byte[] HEX_BYTES;
    private static final char[] HEX_CHARS;
    static final int[] sHexValues;
    static final int[] sInputCodes;
    static final int[] sInputCodesComment;
    static final int[] sInputCodesJsNames;
    static final int[] sInputCodesUtf8;
    static final int[] sInputCodesUtf8JsNames;
    static final int[] sOutputEscapes128;

    static {
        HEX_CHARS = "0123456789ABCDEF".toCharArray();
        int len = HEX_CHARS.length;
        HEX_BYTES = new byte[len];
        int i = 0;
        while (i < len) {
            HEX_BYTES[i] = (byte) HEX_CHARS[i];
            i++;
        }
        int[] table = new int[256];
        i = 0;
        while (i < 32) {
            table[i] = -1;
            i++;
        }
        table[34] = 1;
        table[92] = 1;
        sInputCodes = table;
        table = new int[sInputCodes.length];
        System.arraycopy(sInputCodes, 0, table, 0, sInputCodes.length);
        int c = SmileConstants.TOKEN_PREFIX_TINY_UNICODE;
        while (c < 256) {
            int code = (c & 224) == 192 ? ClassWriter.COMPUTE_FRAMES : (c & 240) == 224 ? JsonWriteContext.STATUS_OK_AFTER_SPACE : (c & 248) == 240 ? JsonWriteContext.STATUS_EXPECT_VALUE : -1;
            table[c] = code;
            c++;
        }
        sInputCodesUtf8 = table;
        table = new int[256];
        Arrays.fill(table, -1);
        i = 33;
        while (i < 256) {
            if (Character.isJavaIdentifierPart((char) i)) {
                table[i] = 0;
            }
            i++;
        }
        table[64] = 0;
        table[35] = 0;
        table[42] = 0;
        table[45] = 0;
        table[43] = 0;
        sInputCodesJsNames = table;
        table = new int[256];
        System.arraycopy(sInputCodesJsNames, 0, table, 0, sInputCodesJsNames.length);
        Arrays.fill(table, SmileConstants.TOKEN_PREFIX_TINY_UNICODE, SmileConstants.TOKEN_PREFIX_TINY_UNICODE, 0);
        sInputCodesUtf8JsNames = table;
        sInputCodesComment = new int[256];
        System.arraycopy(sInputCodesUtf8, SmileConstants.TOKEN_PREFIX_TINY_UNICODE, sInputCodesComment, SmileConstants.TOKEN_PREFIX_TINY_UNICODE, SmileConstants.TOKEN_PREFIX_TINY_UNICODE);
        Arrays.fill(sInputCodesComment, 0, Opcodes.ACC_SYNCHRONIZED, -1);
        sInputCodesComment[9] = 0;
        sInputCodesComment[10] = 10;
        sInputCodesComment[13] = 13;
        sInputCodesComment[42] = 42;
        table = new int[128];
        i = 0;
        while (i < 32) {
            table[i] = -1;
            i++;
        }
        table[34] = 34;
        table[92] = 92;
        table[8] = 98;
        table[9] = 116;
        table[12] = 102;
        table[10] = 110;
        table[13] = 114;
        sOutputEscapes128 = table;
        sHexValues = new int[128];
        Arrays.fill(sHexValues, -1);
        i = 0;
        while (i < 10) {
            sHexValues[i + 48] = i;
            i++;
        }
        i = 0;
        while (i < 6) {
            sHexValues[i + 97] = i + 10;
            sHexValues[i + 65] = i + 10;
            i++;
        }
    }

    public static void appendQuoted(StringBuilder sb, String content) {
        int[] escCodes = sOutputEscapes128;
        char escLen = escCodes.length;
        int i = 0;
        int len = content.length();
        while (i < len) {
            char c = content.charAt(i);
            if (c >= escLen || escCodes[c] == 0) {
                sb.append(c);
            } else {
                sb.append('\\');
                int escCode = escCodes[c];
                if (escCode < 0) {
                    sb.append('u');
                    sb.append('0');
                    sb.append('0');
                    int value = -(escCode + 1);
                    sb.append(HEX_CHARS[value >> 4]);
                    sb.append(HEX_CHARS[value & 15]);
                } else {
                    sb.append((char) escCode);
                }
            }
            i++;
        }
    }

    public static int charToHex(int ch) {
        return ch > 127 ? -1 : sHexValues[ch];
    }

    public static byte[] copyHexBytes() {
        return (byte[]) HEX_BYTES.clone();
    }

    public static char[] copyHexChars() {
        return (char[]) HEX_CHARS.clone();
    }

    public static final int[] get7BitOutputEscapes() {
        return sOutputEscapes128;
    }

    public static final int[] getInputCodeComment() {
        return sInputCodesComment;
    }

    public static final int[] getInputCodeLatin1() {
        return sInputCodes;
    }

    public static final int[] getInputCodeLatin1JsNames() {
        return sInputCodesJsNames;
    }

    public static final int[] getInputCodeUtf8() {
        return sInputCodesUtf8;
    }

    public static final int[] getInputCodeUtf8JsNames() {
        return sInputCodesUtf8JsNames;
    }
}