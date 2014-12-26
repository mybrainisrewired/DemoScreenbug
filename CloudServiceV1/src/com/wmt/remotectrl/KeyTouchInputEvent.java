package com.wmt.remotectrl;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class KeyTouchInputEvent {
    public static final int ABS_MT_BLOB_ID = 56;
    public static final int ABS_MT_ORIENTATION = 52;
    public static final int ABS_MT_POSITION_X = 53;
    public static final int ABS_MT_POSITION_Y = 54;
    public static final int ABS_MT_PRESSURE = 58;
    public static final int ABS_MT_TOOL_TYPE = 55;
    public static final int ABS_MT_TOUCH_MAJOR = 48;
    public static final int ABS_MT_TOUCH_MINOR = 49;
    public static final int ABS_MT_TRACKING_ID = 57;
    public static final int ABS_MT_WIDTH_MAJOR = 50;
    public static final int ABS_MT_WIDTH_MINOR = 51;
    public static final int ABS_X = 0;
    public static final int ABS_Y = 1;
    public static final int ABS_Z = 2;
    public static final int BTN_BACK = 278;
    public static final int BTN_EXTRA = 276;
    public static final int BTN_FIRST = 256;
    public static final int BTN_FORWARD = 277;
    public static final int BTN_LAST = 351;
    public static final int BTN_LEFT = 272;
    public static final int BTN_MIDDLE = 274;
    public static final int BTN_MOUSE = 272;
    public static final int BTN_RIGHT = 273;
    public static final int BTN_SIDE = 275;
    public static final int BTN_TASK = 279;
    public static final int EV_ABS = 3;
    public static final int EV_FF = 21;
    public static final int EV_FF_STATUS = 23;
    public static final int EV_KEY = 1;
    public static final int EV_LED = 17;
    public static final int EV_MAX = 31;
    public static final int EV_MSC = 4;
    public static final int EV_PWR = 22;
    public static final int EV_REL = 2;
    public static final int EV_REP = 20;
    public static final int EV_SND = 18;
    public static final int EV_SW = 5;
    public static final int EV_SYN = 0;
    public static final int INPUT_EVENT_SIZE = 16;
    public static final int KEY_BACK = 158;
    public static final int KEY_BACKSPACE = 14;
    public static final int KEY_DOWN = 108;
    public static final int KEY_HOME = 102;
    public static final int KEY_LEFT = 105;
    public static final int KEY_MENU = 139;
    public static final int KEY_OK = 28;
    public static final int KEY_RIGHT = 106;
    public static final int KEY_UP = 103;
    public static final int MT_TOOL_FINGER = 0;
    public static final int MT_TOOL_PEN = 1;
    public static final int MT_TOUCH_END_VALUE = 0;
    public static final int MT_TOUCH_MAJOR_VALUE = 600;
    public static final int REL_X = 0;
    public static final int REL_Y = 1;
    public static final int REL_Z = 2;
    public static final int STATUS_DOWN = 1;
    public static final int STATUS_UP = 0;
    public static final int SYN_MT_REPORT = 2;
    private static final String TAG = "KeyTouchInputEvent";
    public static final int VOLUMN_DOWN = 114;
    public static final int VOLUMN_UP = 115;

    public static byte[] getAbsEventPackage(int x, int y) {
        byte[] result = new byte[64];
        int iValue = x;
        result[8] = (byte) true;
        result[9] = (byte) false;
        result[10] = (byte) 53;
        result[11] = (byte) false;
        result[12] = (byte) (iValue & 255);
        result[13] = (byte) ((iValue >> 8) & 255);
        result[14] = (byte) ((iValue >> 16) & 255);
        result[15] = (byte) ((iValue >> 24) & 255);
        iValue = y;
        result[24] = (byte) true;
        result[25] = (byte) false;
        result[26] = (byte) 54;
        result[27] = (byte) false;
        result[28] = (byte) (iValue & 255);
        result[29] = (byte) ((iValue >> 8) & 255);
        result[30] = (byte) ((iValue >> 16) & 255);
        result[31] = (byte) ((iValue >> 24) & 255);
        result[42] = (byte) 2;
        result[43] = (byte) false;
        return result;
    }

    public static byte[] getByteArray(int type, int code, int value) {
        short sType = (short) type;
        short sCode = (short) code;
        int iValue = value;
        byte[] result = new byte[16];
        result[8] = (byte) (sType & 255);
        result[9] = (byte) ((sType >> 8) & 255);
        result[10] = (byte) (sCode & 255);
        result[11] = (byte) ((sCode >> 8) & 255);
        result[12] = (byte) (iValue & 255);
        result[13] = (byte) ((iValue >> 8) & 255);
        result[14] = (byte) ((iValue >> 16) & 255);
        result[15] = (byte) ((iValue >> 24) & 255);
        return result;
    }

    public static byte[] getKeyEventPackage(int key, int status) {
        short sCode = (short) key;
        int iValue = status;
        byte[] result = new byte[32];
        result[8] = (byte) 1;
        result[9] = (byte) 0;
        result[10] = (byte) (sCode & 255);
        result[11] = (byte) ((sCode >> 8) & 255);
        result[12] = (byte) (iValue & 255);
        result[13] = (byte) ((iValue >> 8) & 255);
        result[14] = (byte) ((iValue >> 16) & 255);
        result[15] = (byte) ((iValue >> 24) & 255);
        return result;
    }

    public static byte[] getMouseEventPackage(int status) {
        byte[] result = new byte[48];
        result[8] = (byte) true;
        result[9] = (byte) false;
        result[10] = (byte) true;
        result[11] = (byte) false;
        result[12] = (byte) true;
        result[13] = (byte) false;
        result[14] = (byte) true;
        result[15] = (byte) false;
        int iValue = status;
        result[24] = (byte) true;
        result[25] = (byte) false;
        result[26] = (byte) 16;
        result[27] = (byte) true;
        result[28] = (byte) (iValue & 255);
        result[29] = (byte) ((iValue >> 8) & 255);
        result[30] = (byte) ((iValue >> 16) & 255);
        result[31] = (byte) ((iValue >> 24) & 255);
        return result;
    }

    public static byte[] getMultiEndEventPackage() {
        byte[] result = new byte[32];
        result[10] = (byte) 2;
        result[11] = (byte) 0;
        return result;
    }

    public static byte[] getMultiRelEventPackage(int x, int y) {
        byte[] result = new byte[80];
        result[8] = (byte) 3;
        result[9] = (byte) false;
        result[10] = (byte) 48;
        result[11] = (byte) false;
        result[12] = (byte) 88;
        result[13] = (byte) true;
        result[14] = (byte) false;
        result[15] = (byte) false;
        int iValue = x;
        result[24] = (byte) true;
        result[25] = (byte) false;
        result[26] = (byte) false;
        result[27] = (byte) false;
        result[28] = (byte) (iValue & 255);
        result[29] = (byte) ((iValue >> 8) & 255);
        result[30] = (byte) ((iValue >> 16) & 255);
        result[31] = (byte) ((iValue >> 24) & 255);
        iValue = y;
        result[40] = (byte) true;
        result[41] = (byte) false;
        result[42] = (byte) 1;
        result[43] = (byte) false;
        result[44] = (byte) (iValue & 255);
        result[45] = (byte) ((iValue >> 8) & 255);
        result[46] = (byte) ((iValue >> 16) & 255);
        result[47] = (byte) ((iValue >> 24) & 255);
        result[58] = (byte) true;
        result[59] = (byte) false;
        return result;
    }

    public static byte[] getMultiTouchEventPackage(int x0, int y0, int x1, int y1) {
        byte[] result = new byte[144];
        byte b = (byte) true;
        result[72] = b;
        result[8] = b;
        b = (byte) false;
        result[73] = b;
        result[9] = b;
        b = (byte) 48;
        result[74] = b;
        result[10] = b;
        b = (byte) false;
        result[75] = b;
        result[11] = b;
        b = (byte) 88;
        result[76] = b;
        result[12] = b;
        b = (byte) true;
        result[77] = b;
        result[13] = b;
        b = (byte) false;
        result[78] = b;
        result[14] = b;
        b = (byte) false;
        result[79] = b;
        result[15] = b;
        int iValue = x0;
        result[24] = (byte) true;
        result[25] = (byte) false;
        result[26] = (byte) true;
        result[27] = (byte) false;
        result[28] = (byte) (iValue & 255);
        result[29] = (byte) ((iValue >> 8) & 255);
        result[30] = (byte) ((iValue >> 16) & 255);
        result[31] = (byte) ((iValue >> 24) & 255);
        iValue = y0;
        result[40] = (byte) true;
        result[41] = (byte) false;
        result[42] = (byte) true;
        result[43] = (byte) false;
        result[44] = (byte) (iValue & 255);
        result[45] = (byte) ((iValue >> 8) & 255);
        result[46] = (byte) ((iValue >> 16) & 255);
        result[47] = (byte) ((iValue >> 24) & 255);
        b = (byte) true;
        result[122] = b;
        result[58] = b;
        b = (byte) false;
        result[123] = b;
        result[59] = b;
        iValue = x1;
        result[88] = (byte) true;
        result[89] = (byte) false;
        result[90] = (byte) true;
        result[91] = (byte) false;
        result[92] = (byte) (iValue & 255);
        result[93] = (byte) ((iValue >> 8) & 255);
        result[94] = (byte) ((iValue >> 16) & 255);
        result[95] = (byte) ((iValue >> 24) & 255);
        iValue = y1;
        result[104] = (byte) true;
        result[105] = (byte) false;
        result[106] = (byte) true;
        result[107] = (byte) false;
        result[108] = (byte) (iValue & 255);
        result[109] = (byte) ((iValue >> 8) & 255);
        result[110] = (byte) ((iValue >> 16) & 255);
        result[111] = (byte) ((iValue >> 24) & 255);
        return result;
    }

    public static byte[] getRelEventPackage(int x, int y) {
        int iValue = x;
        byte[] result = new byte[48];
        result[8] = (byte) true;
        result[9] = (byte) false;
        result[10] = (byte) false;
        result[11] = (byte) false;
        result[12] = (byte) (iValue & 255);
        result[13] = (byte) ((iValue >> 8) & 255);
        result[14] = (byte) ((iValue >> 16) & 255);
        result[15] = (byte) ((iValue >> 24) & 255);
        iValue = y;
        result[24] = (byte) true;
        result[25] = (byte) false;
        result[26] = (byte) 1;
        result[27] = (byte) false;
        result[28] = (byte) (iValue & 255);
        result[29] = (byte) ((iValue >> 8) & 255);
        result[30] = (byte) ((iValue >> 16) & 255);
        result[31] = (byte) ((iValue >> 24) & 255);
        return result;
    }

    public static byte[] intToByteArray(int i) {
        return new byte[]{(byte) ((i >> 24) & 255), (byte) ((i >> 16) & 255), (byte) ((i >> 8) & 255), (byte) (i & 255)};
    }

    public static byte[] intToByteArray2(int i) throws Exception {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(buf);
        out.writeInt(i);
        byte[] b = buf.toByteArray();
        out.close();
        buf.close();
        return b;
    }

    public static byte[] longToByteArray(int i) {
        return new byte[]{(byte) ((i >> 56) & 255), (byte) ((i >> 48) & 255), (byte) ((i >> 40) & 255), (byte) ((i >> 32) & 255), (byte) ((i >> 24) & 255), (byte) ((i >> 16) & 255), (byte) ((i >> 8) & 255), (byte) (i & 255)};
    }

    private static void setData(byte[] data, int index, int type, int code, int value) {
        data[index * 16 + 8] = (byte) (type & 255);
        data[index * 16 + 9] = (byte) ((type >> 8) & 255);
        data[index * 16 + 10] = (byte) (code & 255);
        data[index * 16 + 11] = (byte) ((code >> 8) & 255);
        data[index * 16 + 12] = (byte) (value & 255);
        data[index * 16 + 13] = (byte) ((value >> 8) & 255);
        data[index * 16 + 14] = (byte) ((value >> 16) & 255);
        data[index * 16 + 15] = (byte) ((value >> 24) & 255);
    }

    public static byte[] shortToByteArray(short s) {
        return new byte[]{(byte) ((s >> 8) & 255), (byte) (s & 255)};
    }
}