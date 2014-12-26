package com.clouds.http;

import com.wmt.opengl.GLView;
import com.wmt.scene.Scene;
import com.wmt.util.ColorBaseSlot;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class JsonUtil {
    static String array2Json(Object[] array) {
        if (array.length == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder(array.length << 4);
        sb.append('[');
        int length = array.length;
        int i = 0;
        while (i < length) {
            sb.append(toJson(array[i]));
            sb.append(',');
            i++;
        }
        sb.setCharAt(sb.length() - 1, ']');
        return sb.toString();
    }

    static String boolean2Json(Boolean bool) {
        return bool.toString();
    }

    static String booleanArray2Json(boolean[] array) {
        if (array.length == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder(array.length << 4);
        sb.append('[');
        int length = array.length;
        int i = 0;
        while (i < length) {
            sb.append(Boolean.toString(array[i]));
            sb.append(',');
            i++;
        }
        sb.setCharAt(sb.length() - 1, ']');
        return sb.toString();
    }

    static String byteArray2Json(byte[] array) {
        if (array.length == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder(array.length << 4);
        sb.append('[');
        int length = array.length;
        int i = 0;
        while (i < length) {
            sb.append(Byte.toString(array[i]));
            sb.append(',');
            i++;
        }
        sb.setCharAt(sb.length() - 1, ']');
        return sb.toString();
    }

    static String collection2Json(Collection<Object> c) {
        return toJson(c.toArray());
    }

    static String doubleArray2Json(double[] array) {
        if (array.length == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder(array.length << 4);
        sb.append('[');
        int length = array.length;
        int i = 0;
        while (i < length) {
            sb.append(Double.toString(array[i]));
            sb.append(',');
            i++;
        }
        sb.setCharAt(sb.length() - 1, ']');
        return sb.toString();
    }

    static String floatArray2Json(float[] array) {
        if (array.length == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder(array.length << 4);
        sb.append('[');
        int length = array.length;
        int i = 0;
        while (i < length) {
            sb.append(Float.toString(array[i]));
            sb.append(',');
            i++;
        }
        sb.setCharAt(sb.length() - 1, ']');
        return sb.toString();
    }

    static String intArray2Json(int[] array) {
        if (array.length == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder(array.length << 4);
        sb.append('[');
        int length = array.length;
        int i = 0;
        while (i < length) {
            sb.append(Integer.toString(array[i]));
            sb.append(',');
            i++;
        }
        sb.setCharAt(sb.length() - 1, ']');
        return sb.toString();
    }

    static String longArray2Json(long[] array) {
        if (array.length == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder(array.length << 4);
        sb.append('[');
        int length = array.length;
        int i = 0;
        while (i < length) {
            sb.append(Long.toString(array[i]));
            sb.append(',');
            i++;
        }
        sb.setCharAt(sb.length() - 1, ']');
        return sb.toString();
    }

    static String map2Json(Map<String, Object> map) {
        if (map.isEmpty()) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder(map.size() << 4);
        sb.append('{');
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            Object value = map.get(key);
            sb.append('\"');
            sb.append(key);
            sb.append('\"');
            sb.append(':');
            sb.append(toJson(value));
            sb.append(',');
        }
        sb.setCharAt(sb.length() - 1, '}');
        return sb.toString();
    }

    static String number2Json(Number number) {
        return number.toString();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String object2Json(java.lang.Object r15_bean) {
        throw new UnsupportedOperationException("Method not decompiled: com.clouds.http.JsonUtil.object2Json(java.lang.Object):java.lang.String");
        /*
        if (r15 != 0) goto L_0x0005;
    L_0x0002:
        r10 = "{}";
    L_0x0004:
        return r10;
    L_0x0005:
        r10 = r15.getClass();
        r6 = r10.getMethods();
        r9 = new java.lang.StringBuilder;
        r10 = r6.length;
        r10 = r10 << 4;
        r9.<init>(r10);
        r10 = 123; // 0x7b float:1.72E-43 double:6.1E-322;
        r9.append(r10);
        r12 = r6.length;
        r10 = 0;
        r11 = r10;
    L_0x001d:
        if (r11 < r12) goto L_0x002b;
    L_0x001f:
        r10 = r9.length();
        r11 = 1;
        if (r10 != r11) goto L_0x00fb;
    L_0x0026:
        r10 = r15.toString();
        goto L_0x0004;
    L_0x002b:
        r5 = r6[r11];
        r7 = r5.getName();	 Catch:{ Exception -> 0x00b5 }
        r4 = "";
        r10 = "get";
        r10 = r7.startsWith(r10);	 Catch:{ Exception -> 0x00b5 }
        if (r10 == 0) goto L_0x005f;
    L_0x003b:
        r10 = 3;
        r4 = r7.substring(r10);	 Catch:{ Exception -> 0x00b5 }
        r10 = 1;
        r0 = new java.lang.String[r10];	 Catch:{ Exception -> 0x00b5 }
        r10 = 0;
        r13 = "Class";
        r0[r10] = r13;	 Catch:{ Exception -> 0x00b5 }
        r1 = 0;
        r13 = r0.length;	 Catch:{ Exception -> 0x00b5 }
        r10 = 0;
    L_0x004b:
        if (r10 < r13) goto L_0x0053;
    L_0x004d:
        if (r1 == 0) goto L_0x006c;
    L_0x004f:
        r10 = r11 + 1;
        r11 = r10;
        goto L_0x001d;
    L_0x0053:
        r8 = r0[r10];	 Catch:{ Exception -> 0x00b5 }
        r14 = r8.equals(r4);	 Catch:{ Exception -> 0x00b5 }
        if (r14 == 0) goto L_0x005c;
    L_0x005b:
        r1 = 1;
    L_0x005c:
        r10 = r10 + 1;
        goto L_0x004b;
    L_0x005f:
        r10 = "is";
        r10 = r7.startsWith(r10);	 Catch:{ Exception -> 0x00b5 }
        if (r10 == 0) goto L_0x006c;
    L_0x0067:
        r10 = 2;
        r4 = r7.substring(r10);	 Catch:{ Exception -> 0x00b5 }
    L_0x006c:
        r10 = r4.length();	 Catch:{ Exception -> 0x00b5 }
        if (r10 <= 0) goto L_0x004f;
    L_0x0072:
        r10 = 0;
        r10 = r4.charAt(r10);	 Catch:{ Exception -> 0x00b5 }
        r10 = java.lang.Character.isUpperCase(r10);	 Catch:{ Exception -> 0x00b5 }
        if (r10 == 0) goto L_0x004f;
    L_0x007d:
        r10 = r5.getParameterTypes();	 Catch:{ Exception -> 0x00b5 }
        r10 = r10.length;	 Catch:{ Exception -> 0x00b5 }
        if (r10 != 0) goto L_0x004f;
    L_0x0084:
        r10 = r4.length();	 Catch:{ Exception -> 0x00b5 }
        r13 = 1;
        if (r10 != r13) goto L_0x00cf;
    L_0x008b:
        r4 = r4.toLowerCase();	 Catch:{ Exception -> 0x00b5 }
    L_0x008f:
        r10 = 0;
        r10 = new java.lang.Object[r10];	 Catch:{ Exception -> 0x00b5 }
        r3 = r5.invoke(r15, r10);	 Catch:{ Exception -> 0x00b5 }
        r10 = 34;
        r9.append(r10);	 Catch:{ Exception -> 0x00b5 }
        r9.append(r4);	 Catch:{ Exception -> 0x00b5 }
        r10 = 34;
        r9.append(r10);	 Catch:{ Exception -> 0x00b5 }
        r10 = 58;
        r9.append(r10);	 Catch:{ Exception -> 0x00b5 }
        r10 = toJson(r3);	 Catch:{ Exception -> 0x00b5 }
        r9.append(r10);	 Catch:{ Exception -> 0x00b5 }
        r10 = 44;
        r9.append(r10);	 Catch:{ Exception -> 0x00b5 }
        goto L_0x004f;
    L_0x00b5:
        r2 = move-exception;
        r10 = new java.lang.RuntimeException;
        r11 = new java.lang.StringBuilder;
        r12 = "\u5728\u5c06bean\u5c01\u88c5\u6210JSON\u683c\u5f0f\u65f6\u5f02\u5e38\uff1a";
        r11.<init>(r12);
        r12 = r2.getMessage();
        r11 = r11.append(r12);
        r11 = r11.toString();
        r10.<init>(r11, r2);
        throw r10;
    L_0x00cf:
        r10 = 1;
        r10 = r4.charAt(r10);	 Catch:{ Exception -> 0x00b5 }
        r10 = java.lang.Character.isUpperCase(r10);	 Catch:{ Exception -> 0x00b5 }
        if (r10 != 0) goto L_0x008f;
    L_0x00da:
        r10 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00b5 }
        r13 = 0;
        r14 = 1;
        r13 = r4.substring(r13, r14);	 Catch:{ Exception -> 0x00b5 }
        r13 = r13.toLowerCase();	 Catch:{ Exception -> 0x00b5 }
        r13 = java.lang.String.valueOf(r13);	 Catch:{ Exception -> 0x00b5 }
        r10.<init>(r13);	 Catch:{ Exception -> 0x00b5 }
        r13 = 1;
        r13 = r4.substring(r13);	 Catch:{ Exception -> 0x00b5 }
        r10 = r10.append(r13);	 Catch:{ Exception -> 0x00b5 }
        r4 = r10.toString();	 Catch:{ Exception -> 0x00b5 }
        goto L_0x008f;
    L_0x00fb:
        r10 = r9.length();
        r10 = r10 + -1;
        r11 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
        r9.setCharAt(r10, r11);
        r10 = r9.toString();
        goto L_0x0004;
        */
    }

    static String shortArray2Json(short[] array) {
        if (array.length == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder(array.length << 4);
        sb.append('[');
        int length = array.length;
        int i = 0;
        while (i < length) {
            sb.append(Short.toString(array[i]));
            sb.append(',');
            i++;
        }
        sb.setCharAt(sb.length() - 1, ']');
        return sb.toString();
    }

    static String string2Json(String s) {
        StringBuilder sb = new StringBuilder(s.length() + 20);
        sb.append('\"');
        int i = 0;
        while (i < s.length()) {
            char c = s.charAt(i);
            switch (c) {
                case GLView.FLAG_POPUP:
                    sb.append("\\b");
                    break;
                case Scene.NO_FLING:
                    sb.append("\\t");
                    break;
                case Scene.FLING_LEFT:
                    sb.append("\\n");
                    break;
                case ColorBaseSlot.CB_INDEX_BLACK:
                    sb.append("\\f");
                    break;
                case ColorBaseSlot.CB_INDEX_GRAY:
                    sb.append("\\r");
                    break;
                case '\"':
                    sb.append("\\\"");
                    break;
                case '/':
                    sb.append("\\/");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                default:
                    sb.append(c);
                    break;
            }
            i++;
        }
        sb.append('\"');
        return sb.toString();
    }

    public static String toJson(Object o) {
        if (o == null) {
            return "null";
        }
        if (o instanceof String) {
            return string2Json((String) o);
        }
        if (o instanceof Boolean) {
            return boolean2Json((Boolean) o);
        }
        if (o instanceof Number) {
            return number2Json((Number) o);
        }
        if (o instanceof Map) {
            return map2Json((Map) o);
        }
        if (o instanceof Collection) {
            return collection2Json((Collection) o);
        }
        if (o instanceof Object[]) {
            return array2Json((Object[]) o);
        }
        if (o instanceof int[]) {
            return intArray2Json((int[]) o);
        }
        if (o instanceof boolean[]) {
            return booleanArray2Json((boolean[]) o);
        }
        if (o instanceof long[]) {
            return longArray2Json((long[]) o);
        }
        if (o instanceof float[]) {
            return floatArray2Json((float[]) o);
        }
        if (o instanceof double[]) {
            return doubleArray2Json((double[]) o);
        }
        if (o instanceof short[]) {
            return shortArray2Json((short[]) o);
        }
        if (o instanceof byte[]) {
            return byteArray2Json((byte[]) o);
        }
        if (o instanceof Object) {
            return object2Json(o);
        }
        throw new RuntimeException(new StringBuilder("\u4e0d\u652f\u6301\u7684\u7c7b\u578b: ").append(o.getClass().getName()).toString());
    }
}