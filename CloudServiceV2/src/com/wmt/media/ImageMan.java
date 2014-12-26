package com.wmt.media;

import android.graphics.Bitmap;
import java.io.BufferedInputStream;
import java.io.FileDescriptor;
import java.io.InputStream;

public final class ImageMan {
    public static final int bitmap_default = 0;
    public static final int bitmap_hwbuf = 512;
    public static final int bitmap_malloc = 1024;
    public static final int defaultFlags = 278;
    public static final int dither_floyd = 256;
    public static final int dither_none = 0;
    public static final int format_rgb565 = 0;
    public static final int format_rgb8888 = 262144;
    public static final int jpeg_swdec = 4096;
    public static final int judge_fileext = 8192;
    public static final int scale_bilinear = 16;
    public static final int scale_linear = 0;
    public static final int thumbclip = 65810;
    public static final int zoom_crop_outside = 65536;
    public static final int zoom_keepratio = 2;
    public static final int zoom_orignal = 1;
    public static final int zoom_shrinkonly = 4;
    public static final int zoom_straight = 0;

    static {
        System.load("libwmtmedia.so");
    }

    public static Bitmap cloneBitmap(Bitmap source) {
        return native_cloneBitmap(source, source.hasAlpha() ? FragmentManagerImpl.ANIM_STYLE_FADE_EXIT : zoom_shrinkonly, true);
    }

    public static int colorMajorSlot(Bitmap source) {
        return native_colorMajorSlot(source);
    }

    public static Bitmap loadFile(String filename, int destWidth, int destHeight) {
        return loadFile(filename, destWidth, destHeight, defaultFlags);
    }

    public static Bitmap loadFile(String filename, int destWidth, int destHeight, int flags) {
        return native_loadFile(filename, destWidth, destHeight, flags);
    }

    public static Bitmap loadFileDescriptor(FileDescriptor fd, int destWidth, int destHeight, int flags) {
        return native_loadFileDescriptor(fd, destWidth, destHeight, flags);
    }

    public static Bitmap loadInputStream(InputStream input, int destWidth, int destHeight, int flags) {
        byte[] storage = new byte[16384];
        if (!input.markSupported()) {
            input = new BufferedInputStream(input, 16384);
        }
        input.mark(bitmap_malloc);
        return native_loadInputStream(input, storage, destWidth, destHeight, flags);
    }

    public static Bitmap loadMem(byte[] data, int offset, int length, int destWidth, int destHeight) {
        return loadMem(data, offset, length, destWidth, destHeight, defaultFlags);
    }

    public static Bitmap loadMem(byte[] data, int offset, int length, int destWidth, int destHeight, int flags) {
        return native_loadMem(data, offset, length, destWidth, destHeight, flags);
    }

    private static native Bitmap native_cloneBitmap(Bitmap bitmap, int i, boolean z);

    private static native int native_colorMajorSlot(Bitmap bitmap);

    private static native Bitmap native_loadFile(String str, int i, int i2, int i3);

    private static native Bitmap native_loadFileDescriptor(FileDescriptor fileDescriptor, int i, int i2, int i3);

    private static native Bitmap native_loadInputStream(InputStream inputStream, byte[] bArr, int i, int i2, int i3);

    private static native Bitmap native_loadMem(byte[] bArr, int i, int i2, int i3, int i4, int i5);

    public static void touch() {
    }
}