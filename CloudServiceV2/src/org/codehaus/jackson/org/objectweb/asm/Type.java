package org.codehaus.jackson.org.objectweb.asm;

import com.wmt.data.DataManager;
import com.wmt.data.LocalAudioAll;
import com.wmt.remotectrl.ConnectionInstance;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class Type {
    public static final int ARRAY = 9;
    public static final int BOOLEAN = 1;
    public static final Type BOOLEAN_TYPE;
    public static final int BYTE = 3;
    public static final Type BYTE_TYPE;
    public static final int CHAR = 2;
    public static final Type CHAR_TYPE;
    public static final int DOUBLE = 8;
    public static final Type DOUBLE_TYPE;
    public static final int FLOAT = 6;
    public static final Type FLOAT_TYPE;
    public static final int INT = 5;
    public static final Type INT_TYPE;
    public static final int LONG = 7;
    public static final Type LONG_TYPE;
    public static final int OBJECT = 10;
    public static final int SHORT = 4;
    public static final Type SHORT_TYPE;
    public static final int VOID = 0;
    public static final Type VOID_TYPE;
    private final int a;
    private final char[] b;
    private final int c;
    private final int d;

    static {
        VOID_TYPE = new Type(0, null, 1443168256, 1);
        BOOLEAN_TYPE = new Type(1, null, 1509950721, 1);
        CHAR_TYPE = new Type(2, null, 1124075009, 1);
        BYTE_TYPE = new Type(3, null, 1107297537, 1);
        SHORT_TYPE = new Type(4, null, 1392510721, 1);
        INT_TYPE = new Type(5, null, 1224736769, 1);
        FLOAT_TYPE = new Type(6, null, 1174536705, 1);
        LONG_TYPE = new Type(7, null, 1241579778, 1);
        DOUBLE_TYPE = new Type(8, null, 1141048066, 1);
    }

    private Type(int i, char[] cArr, int i2, int i3) {
        this.a = i;
        this.b = cArr;
        this.c = i2;
        this.d = i3;
    }

    private static Type a(char[] cArr, int i) {
        int i2 = BOOLEAN;
        switch (cArr[i]) {
            case DataManager.INCLUDE_LOCAL_VIDEO_ONLY:
                return BYTE_TYPE;
            case DataManager.INCLUDE_LOCAL_ALL_ONLY:
                return CHAR_TYPE;
            case DataManager.INCLUDE_LOCAL_AUDIO_ONLY:
                return DOUBLE_TYPE;
            case 'F':
                return FLOAT_TYPE;
            case 'I':
                return INT_TYPE;
            case 'J':
                return LONG_TYPE;
            case Opcodes.AASTORE:
                return SHORT_TYPE;
            case Opcodes.SASTORE:
                return VOID_TYPE;
            case Opcodes.DUP_X1:
                return BOOLEAN_TYPE;
            case Opcodes.DUP_X2:
                while (cArr[i + i2] == '[') {
                    i2++;
                }
                if (cArr[i + i2] == 'L') {
                    i2++;
                    while (cArr[i + i2] != ';') {
                        i2++;
                    }
                }
                return new Type(9, cArr, i, i2 + 1);
            default:
                while (cArr[i + i2] != ';') {
                    i2++;
                }
                return new Type(10, cArr, i + 1, i2 - 1);
        }
    }

    private void a(StringBuffer stringBuffer) {
        if (this.b == null) {
            stringBuffer.append((char) ((this.c & -16777216) >>> 24));
        } else if (this.a == 9) {
            stringBuffer.append(this.b, this.c, this.d);
        } else {
            stringBuffer.append('L');
            stringBuffer.append(this.b, this.c, this.d);
            stringBuffer.append(';');
        }
    }

    private static void a(StringBuffer stringBuffer, Class cls) {
        char charAt;
        while (!cls.isPrimitive()) {
            if (cls.isArray()) {
                stringBuffer.append('[');
                cls = cls.getComponentType();
            } else {
                stringBuffer.append('L');
                String name = cls.getName();
                int length = name.length();
                int i = 0;
                while (i < length) {
                    charAt = name.charAt(i);
                    if (charAt == '.') {
                        charAt = '/';
                    }
                    stringBuffer.append(charAt);
                    i++;
                }
                stringBuffer.append(';');
                return;
            }
        }
        charAt = cls == Integer.TYPE ? 'I' : cls == Void.TYPE ? 'V' : cls == Boolean.TYPE ? 'Z' : cls == Byte.TYPE ? 'B' : cls == Character.TYPE ? 'C' : cls == Short.TYPE ? 'S' : cls == Double.TYPE ? 'D' : cls == Float.TYPE ? 'F' : 'J';
        stringBuffer.append(charAt);
    }

    public static Type[] getArgumentTypes(String str) {
        int i = BOOLEAN;
        char[] toCharArray = str.toCharArray();
        int i2 = 0;
        int i3 = 1;
        while (true) {
            int i4 = i3 + 1;
            char c = toCharArray[i3];
            if (c == ')') {
                break;
            } else if (c == 'L') {
                i3 = i4;
                while (true) {
                    i4 = i3 + 1;
                    if (toCharArray[i3] == ';') {
                        break;
                    }
                    i3 = i4;
                }
                i2++;
                i3 = i4;
            } else if (c != '[') {
                i2++;
                i3 = i4;
            } else {
                i3 = i4;
            }
        }
        Type[] typeArr = new Type[i2];
        i2 = 0;
        while (toCharArray[i] != ')') {
            typeArr[i2] = a(toCharArray, i);
            i += (typeArr[i2].a == 10 ? CHAR : 0) + typeArr[i2].d;
            i2++;
        }
        return typeArr;
    }

    public static Type[] getArgumentTypes(Method method) {
        Class[] parameterTypes = method.getParameterTypes();
        Type[] typeArr = new Type[parameterTypes.length];
        int length = parameterTypes.length - 1;
        while (length >= 0) {
            typeArr[length] = getType(parameterTypes[length]);
            length--;
        }
        return typeArr;
    }

    public static int getArgumentsAndReturnSizes(String str) {
        int i;
        char charAt;
        int i2 = BOOLEAN;
        int i3 = 1;
        int i4 = 1;
        while (true) {
            i = i3 + 1;
            charAt = str.charAt(i3);
            if (charAt == ')') {
                break;
            } else if (charAt == 'L') {
                i3 = i;
                while (true) {
                    i = i3 + 1;
                    if (str.charAt(i3) == ';') {
                        break;
                    }
                    i3 = i;
                }
                i4++;
                i3 = i;
            } else {
                if (charAt != '[') {
                    if (charAt == 'D' || charAt == 'J') {
                        i4 += 2;
                        i3 = i;
                    } else {
                        i4++;
                        i3 = i;
                    }
                }
                while (true) {
                    charAt = str.charAt(i);
                    if (charAt != '[') {
                        break;
                    }
                    i++;
                }
                if (charAt == 'D' || charAt == 'J') {
                    i4--;
                    i3 = i;
                } else {
                    i3 = i;
                }
            }
        }
        charAt = str.charAt(i);
        i4 <<= 2;
        if (charAt == 'V') {
            i2 = 0;
        } else if (charAt == 'D' || charAt == 'J') {
            i2 = CHAR;
        }
        return i4 | i2;
    }

    public static String getConstructorDescriptor(Constructor constructor) {
        Class[] parameterTypes = constructor.getParameterTypes();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append('(');
        int i = 0;
        while (i < parameterTypes.length) {
            a(stringBuffer, parameterTypes[i]);
            i++;
        }
        return stringBuffer.append(")V").toString();
    }

    public static String getDescriptor(Class cls) {
        StringBuffer stringBuffer = new StringBuffer();
        a(stringBuffer, cls);
        return stringBuffer.toString();
    }

    public static String getInternalName(Class cls) {
        return cls.getName().replace('.', '/');
    }

    public static String getMethodDescriptor(Method method) {
        Class[] parameterTypes = method.getParameterTypes();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append('(');
        int i = 0;
        while (i < parameterTypes.length) {
            a(stringBuffer, parameterTypes[i]);
            i++;
        }
        stringBuffer.append(')');
        a(stringBuffer, method.getReturnType());
        return stringBuffer.toString();
    }

    public static String getMethodDescriptor(Type type, Type[] typeArr) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append('(');
        int i = 0;
        while (i < typeArr.length) {
            typeArr[i].a(stringBuffer);
            i++;
        }
        stringBuffer.append(')');
        type.a(stringBuffer);
        return stringBuffer.toString();
    }

    public static Type getObjectType(String str) {
        char[] toCharArray = str.toCharArray();
        return new Type(toCharArray[0] == '[' ? ARRAY : OBJECT, toCharArray, 0, toCharArray.length);
    }

    public static Type getReturnType(String str) {
        return a(str.toCharArray(), str.indexOf(ConnectionInstance.ALL_DISCONNECTED) + 1);
    }

    public static Type getReturnType(Method method) {
        return getType(method.getReturnType());
    }

    public static Type getType(Class cls) {
        return cls.isPrimitive() ? cls == Integer.TYPE ? INT_TYPE : cls == Void.TYPE ? VOID_TYPE : cls == Boolean.TYPE ? BOOLEAN_TYPE : cls == Byte.TYPE ? BYTE_TYPE : cls == Character.TYPE ? CHAR_TYPE : cls == Short.TYPE ? SHORT_TYPE : cls == Double.TYPE ? DOUBLE_TYPE : cls == Float.TYPE ? FLOAT_TYPE : LONG_TYPE : getType(getDescriptor(cls));
    }

    public static Type getType(String str) {
        return a(str.toCharArray(), 0);
    }

    public boolean equals(Type type) {
        if (this == type) {
            return true;
        }
        if (!(type instanceof Type)) {
            return false;
        }
        type = type;
        if (this.a != type.a) {
            return false;
        }
        if (this.a != 10 && this.a != 9) {
            return true;
        }
        if (this.d != type.d) {
            return false;
        }
        int i = this.c;
        int i2 = type.c;
        int i3 = this.d + i;
        while (i < i3) {
            if (this.b[i] != type.b[i2]) {
                return false;
            }
            i++;
            i2++;
        }
        return true;
    }

    public String getClassName() {
        switch (this.a) {
            case LocalAudioAll.SORT_BY_TITLE:
                return "void";
            case BOOLEAN:
                return "boolean";
            case CHAR:
                return "char";
            case BYTE:
                return "byte";
            case SHORT:
                return "short";
            case INT:
                return "int";
            case FLOAT:
                return "float";
            case LONG:
                return "long";
            case DOUBLE:
                return "double";
            case ARRAY:
                StringBuffer stringBuffer = new StringBuffer(getElementType().getClassName());
                int dimensions = getDimensions();
                while (dimensions > 0) {
                    stringBuffer.append("[]");
                    dimensions--;
                }
                return stringBuffer.toString();
            default:
                return new String(this.b, this.c, this.d).replace('/', '.');
        }
    }

    public String getDescriptor() {
        StringBuffer stringBuffer = new StringBuffer();
        a(stringBuffer);
        return stringBuffer.toString();
    }

    public int getDimensions() {
        int i = BOOLEAN;
        while (this.b[this.c + i] == '[') {
            i++;
        }
        return i;
    }

    public Type getElementType() {
        return a(this.b, this.c + getDimensions());
    }

    public String getInternalName() {
        return new String(this.b, this.c, this.d);
    }

    public int getOpcode(int i) {
        int i2 = SHORT;
        if (i == 46 || i == 79) {
            if (this.b == null) {
                i2 = (this.c & 65280) >> 8;
            }
            return i2 + i;
        } else {
            if (this.b == null) {
                i2 = (this.c & 16711680) >> 16;
            }
            return i2 + i;
        }
    }

    public int getSize() {
        return this.b == null ? this.c & 255 : BOOLEAN;
    }

    public int getSort() {
        return this.a;
    }

    public int hashCode() {
        int i = this.a * 13;
        if (this.a == 10 || this.a == 9) {
            int i2 = this.c;
            int i3 = i2 + this.d;
            while (i2 < i3) {
                int i4 = (i + this.b[i2]) * 17;
                i2++;
                i = i4;
            }
        }
        return i;
    }

    public String toString() {
        return getDescriptor();
    }
}