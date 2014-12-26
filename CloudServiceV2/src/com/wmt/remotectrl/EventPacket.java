package com.wmt.remotectrl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import org.apache.http.util.ByteArrayBuffer;

public final class EventPacket {
    public static int BYTES_PER_INT = 0;
    public static final String CLIENT_PREFIX = "client: ";
    public static final String CURSOR_CHANGED = "com.wmt.remotectrlime.cursor.changed";
    public static final int EVENT_ALIVE_ACKNOWLEDGE = 101;
    public static final int EVENT_ALIVE_PING = 100;
    public static final int EVENT_G_SENSOR = 3;
    public static final int EVENT_KEYCODE = 0;
    public static final int EVENT_SCREEN_IMAGE = 4;
    public static final int EVENT_TOUCH_POINT_ABS = 2;
    public static final int EVENT_TOUCH_POINT_REL = 1;
    public static final int G_SENSOR_REGISTER = 202;
    public static int HEAD_MINI_BYTES = 0;
    public static final int HIDE_IME = 201;
    public static final String INPUT_HIDE = "com.wmt.remotectrlime.input.hide";
    public static final String INPUT_MSG = "com.wmt.remotectrlime.input.msg";
    public static final String INPUT_SHOW = "com.wmt.remotectrlime.input.show";
    public static String NAME_PREFIX = null;
    public static String PING_PREFIX = null;
    public static final int PORT_FOR_BROADCAST = 8710;
    public static final int PORT_FOR_TCP_G_SENSOR = 8713;
    public static final int PORT_FOR_TCP_IME = 8712;
    public static final int PORT_FOR_TCP_KEYTOUCH = 8711;
    public static final int PORT_FOR_TCP_OTHER = 8714;
    public static String PORT_PREFIX = null;
    public static final int REPORT_TIMER_MS = 8000;
    public static String SERVER_PREFIX = null;
    public static final int SERVER_RESOLUTION = 102;
    public static final int SERVER_SCREEN_IMAGE = 103;
    public static final int SHOW_IME = 200;
    public static final int SYNC_INFO = 203;
    public static final String SYNC_MSG = "com.wmt.remotectrlime.sync_msg";
    public static final int TIMEOUT_MS = 30000;
    public static String VERSION_PREFIX;

    public static class DataWrapper {
        public byte[] mData;
        public int mId;

        DataWrapper() {
            this.mId = -1;
            this.mData = null;
        }
    }

    public static interface OnSocketMsgListener {
        void onMySocketError();

        void receivedResponse(String str, String str2, int i);
    }

    static {
        SERVER_PREFIX = "server: ";
        PORT_PREFIX = "port: ";
        NAME_PREFIX = "name: ";
        VERSION_PREFIX = "version: ";
        PING_PREFIX = "ping: ";
        BYTES_PER_INT = 4;
        HEAD_MINI_BYTES = BYTES_PER_INT * 2;
    }

    public static int byteArrayToInt(byte[] b, int offset) {
        return (((b[offset] & 255) | ((b[offset + 1] & 255) << 8)) | ((b[offset + 2] & 255) << 16)) | ((b[offset + 3] & 255) << 24);
    }

    public static byte[] getSimpleDataFormat(int id, byte[] data) {
        ByteArrayBuffer bab = new ByteArrayBuffer(BYTES_PER_INT * 2 + data.length);
        bab.append(intToByteArray(id), EVENT_KEYCODE, BYTES_PER_INT);
        bab.append(intToByteArray(data.length), EVENT_KEYCODE, BYTES_PER_INT);
        bab.append(data, EVENT_KEYCODE, data.length);
        return bab.toByteArray();
    }

    public static byte[] intToByteArray(float value) {
        return intToByteArray(Float.floatToRawIntBits(value));
    }

    public static byte[] intToByteArray(int value) {
        return new byte[]{(byte) (value & 255), (byte) ((value >> 8) & 255), (byte) ((value >> 16) & 255), (byte) ((value >> 24) & 255)};
    }

    public static String intToIpAddress(int addr) {
        StringBuffer str = new StringBuffer();
        addr >>>= 8;
        addr >>>= 8;
        str.append(addr & 255).append('.').append(addr & 255).append('.').append(addr & 255).append('.').append((addr >>> 8) & 255);
        return str.toString();
    }

    public static int ipAddrToInt(String ipaddr) {
        InetAddress ia = null;
        try {
            ia = InetAddress.getByName(ipaddr);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        byte[] array = ia.getAddress();
        return (((array[0] & 255) | ((array[1] & 255) << 8)) | ((array[2] & 255) << 16)) | ((array[3] & 255) << 24);
    }

    public static DataWrapper readDataWrapper(InputStream is) throws IOException {
        DataWrapper dw = new DataWrapper();
        byte[] head = new byte[8];
        if (readUntil(is, head, BYTES_PER_INT * 2) == 0) {
            int id = byteArrayToInt(head, EVENT_KEYCODE);
            int len = byteArrayToInt(head, EVENT_SCREEN_IMAGE);
            dw.mData = new byte[len];
            if (readUntil(is, dw.mData, len) == 0) {
                dw.mId = id;
            }
        }
        return dw;
    }

    public static DataWrapper readDataWrapper(SocketChannel socket) throws IOException {
        DataWrapper dw = new DataWrapper();
        byte[] head = new byte[(BYTES_PER_INT * 2)];
        readUntil(socket, head);
        int id = byteArrayToInt(head, EVENT_KEYCODE);
        dw.mData = new byte[byteArrayToInt(head, EVENT_SCREEN_IMAGE)];
        readUntil(socket, dw.mData);
        dw.mId = id;
        return dw;
    }

    public static int readUntil(InputStream is, byte[] buf, int len) throws IOException {
        int dataRead = EVENT_KEYCODE;
        while (true) {
            int readRet = is.read(buf, dataRead, len);
            if (readRet == len) {
                return len - readRet;
            }
            if (readRet > 0) {
                dataRead += readRet;
                len -= readRet;
            } else if (readRet == -1) {
                return len;
            }
        }
    }

    public static void readUntil(SocketChannel socket, byte[] buffer) throws IOException {
        ByteBuffer buf = ByteBuffer.wrap(buffer);
        while (buf.position() != buf.limit()) {
            if (socket.read(buf) < 0) {
                throw new IOException("EOF");
            }
        }
    }

    public static int sendByteArrayData(OutputStream os, int id, byte[] data) throws IOException {
        os.write(intToByteArray(id));
        os.write(intToByteArray(data.length));
        os.write(data);
        os.flush();
        return EVENT_KEYCODE;
    }

    public static int sendOneInt(OutputStream os, int id, int value) throws IOException {
        os.write(intToByteArray(id));
        os.write(intToByteArray(BYTES_PER_INT));
        os.write(intToByteArray(value));
        os.flush();
        return EVENT_KEYCODE;
    }

    public static void writeData(SocketChannel socket, byte[] buffer) throws IOException {
        if (buffer != null && buffer.length > HEAD_MINI_BYTES) {
            ByteBuffer buf = ByteBuffer.wrap(buffer);
            while (buf.hasRemaining()) {
                if (socket.write(buf) < 0) {
                    throw new IOException();
                }
            }
        }
    }
}