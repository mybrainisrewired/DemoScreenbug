package com.wmt.util;

public class MmInfo {
    public static final String MMINFO_EDCODING_STRING = "utf8";
    public static final int MMINFO_MEM_SIZE = 16;
    public int duration;
    public int height;
    public int level;
    public int width;

    public static MmInfo fromByteArray(byte[] data) {
        if (data == null || data.length != 16) {
            return null;
        }
        MmInfo mmInfo = new MmInfo();
        int byteIndex = 0 + 1;
        int byteIndex2 = byteIndex + 1;
        byteIndex = byteIndex2 + 1;
        byteIndex2 = byteIndex + 1;
        mmInfo.width = ((unsignedByteToInt(data[0]) | (unsignedByteToInt(data[byteIndex]) << 8)) | (unsignedByteToInt(data[byteIndex2]) << 16)) | (unsignedByteToInt(data[byteIndex]) << 24);
        byteIndex = byteIndex2 + 1;
        byteIndex2 = byteIndex + 1;
        byteIndex = byteIndex2 + 1;
        byteIndex2 = byteIndex + 1;
        mmInfo.height = ((unsignedByteToInt(data[byteIndex2]) | (unsignedByteToInt(data[byteIndex]) << 8)) | (unsignedByteToInt(data[byteIndex2]) << 16)) | (unsignedByteToInt(data[byteIndex]) << 24);
        byteIndex = byteIndex2 + 1;
        byteIndex2 = byteIndex + 1;
        byteIndex = byteIndex2 + 1;
        byteIndex2 = byteIndex + 1;
        mmInfo.duration = ((unsignedByteToInt(data[byteIndex2]) | (unsignedByteToInt(data[byteIndex]) << 8)) | (unsignedByteToInt(data[byteIndex2]) << 16)) | (unsignedByteToInt(data[byteIndex]) << 24);
        byteIndex = byteIndex2 + 1;
        byteIndex2 = byteIndex + 1;
        byteIndex = byteIndex2 + 1;
        byteIndex2 = byteIndex + 1;
        mmInfo.level = ((unsignedByteToInt(data[byteIndex2]) | (unsignedByteToInt(data[byteIndex]) << 8)) | (unsignedByteToInt(data[byteIndex2]) << 16)) | (unsignedByteToInt(data[byteIndex]) << 24);
        return mmInfo;
    }

    public static int unsignedByteToInt(byte b) {
        return b & 255;
    }
}