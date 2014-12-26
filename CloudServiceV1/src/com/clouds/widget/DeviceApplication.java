package com.clouds.widget;

import com.clouds.util.SysProperty;

public class DeviceApplication {
    private static final String TAG;
    private static final String eken = "eken";
    private static final String ro = "ro";

    static {
        TAG = DeviceApplication.class.getSimpleName();
    }

    public static String getDevice() {
        String device = SysProperty.getSysProperty("eken.product.device");
        return (device == null || device.equals("")) ? SysProperty.getSysProperty("ro.product.device") : device;
    }

    public static String getDeviceBuiledTime() {
        String buildTime = SysProperty.getSysProperty("eken.product.type");
        return (buildTime == null || buildTime.equals("")) ? SysProperty.getSysProperty("ro.build.date.utc") : buildTime;
    }

    public static String getDeviceName() {
        String name = SysProperty.getSysProperty("eken.product.name");
        return (name == null || name.equals("")) ? SysProperty.getSysProperty("ro.product.name") : name;
    }

    public static String getDeviceType() {
        String manufacturer = SysProperty.getSysProperty("eken.product.type");
        return (manufacturer == null || manufacturer.equals("")) ? "" : manufacturer;
    }

    public static String getHardware() {
        String hardware = SysProperty.getSysProperty("eken.hardware");
        return (hardware == null || hardware.equals("")) ? SysProperty.getSysProperty("ro.hardware") : hardware;
    }

    public static String getManufacturer() {
        String manufacturer = SysProperty.getSysProperty("eken.product.manufacturer");
        return (manufacturer == null || manufacturer.equals("")) ? SysProperty.getSysProperty("ro.product.manufacturer") : manufacturer;
    }

    public static String getModel() {
        String model = SysProperty.getSysProperty("eken.product.model");
        return (model == null || model.equals("")) ? SysProperty.getSysProperty("ro.product.model") : model;
    }

    public static String getPlatform() {
        String platform = SysProperty.getSysProperty("eken.board.platform");
        return (platform == null || platform.equals("")) ? SysProperty.getSysProperty("ro.board.platform") : platform;
    }

    public static String getRelease() {
        String release = SysProperty.getSysProperty("eken.build.version.release");
        return (release == null || release.equals("")) ? SysProperty.getSysProperty("ro.build.version.release") : release;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getScrres(android.content.Context r15_context) {
        throw new UnsupportedOperationException("Method not decompiled: com.clouds.widget.DeviceApplication.getScrres(android.content.Context):java.lang.String");
        /*
        r12 = "eken.hardware.screen.res";
        r11 = com.clouds.util.SysProperty.getSysProperty(r12);
        if (r11 == 0) goto L_0x0010;
    L_0x0008:
        r12 = "";
        r12 = r11.equals(r12);
        if (r12 == 0) goto L_0x0096;
    L_0x0010:
        r12 = r15.getResources();	 Catch:{ NumberFormatException -> 0x00a4, IllegalArgumentException -> 0x00a9, NotFoundException -> 0x00ae, ClassNotFoundException -> 0x00b3, InstantiationException -> 0x00b8, IllegalAccessException -> 0x00bd, NoSuchFieldException -> 0x00c2 }
        r2 = r12.getDisplayMetrics();	 Catch:{ NumberFormatException -> 0x00a4, IllegalArgumentException -> 0x00a9, NotFoundException -> 0x00ae, ClassNotFoundException -> 0x00b3, InstantiationException -> 0x00b8, IllegalAccessException -> 0x00bd, NoSuchFieldException -> 0x00c2 }
        r12 = "com.android.internal.R$dimen";
        r1 = java.lang.Class.forName(r12);	 Catch:{ NumberFormatException -> 0x00a4, IllegalArgumentException -> 0x00a9, NotFoundException -> 0x00ae, ClassNotFoundException -> 0x00b3, InstantiationException -> 0x00b8, IllegalAccessException -> 0x00bd, NoSuchFieldException -> 0x00c2 }
        r8 = r1.newInstance();	 Catch:{ NumberFormatException -> 0x00a4, IllegalArgumentException -> 0x00a9, NotFoundException -> 0x00ae, ClassNotFoundException -> 0x00b3, InstantiationException -> 0x00b8, IllegalAccessException -> 0x00bd, NoSuchFieldException -> 0x00c2 }
        r12 = "status_bar_height";
        r4 = r1.getField(r12);	 Catch:{ NumberFormatException -> 0x00a4, IllegalArgumentException -> 0x00a9, NotFoundException -> 0x00ae, ClassNotFoundException -> 0x00b3, InstantiationException -> 0x00b8, IllegalAccessException -> 0x00bd, NoSuchFieldException -> 0x00c2 }
        r12 = "navigation_bar_height";
        r12 = r1.getField(r12);	 Catch:{ NumberFormatException -> 0x00a4, IllegalArgumentException -> 0x00a9, NotFoundException -> 0x00ae, ClassNotFoundException -> 0x00b3, InstantiationException -> 0x00b8, IllegalAccessException -> 0x00bd, NoSuchFieldException -> 0x00c2 }
        r12 = r12.get(r8);	 Catch:{ NumberFormatException -> 0x00a4, IllegalArgumentException -> 0x00a9, NotFoundException -> 0x00ae, ClassNotFoundException -> 0x00b3, InstantiationException -> 0x00b8, IllegalAccessException -> 0x00bd, NoSuchFieldException -> 0x00c2 }
        r12 = r12.toString();	 Catch:{ NumberFormatException -> 0x00a4, IllegalArgumentException -> 0x00a9, NotFoundException -> 0x00ae, ClassNotFoundException -> 0x00b3, InstantiationException -> 0x00b8, IllegalAccessException -> 0x00bd, NoSuchFieldException -> 0x00c2 }
        r5 = java.lang.Integer.parseInt(r12);	 Catch:{ NumberFormatException -> 0x00a4, IllegalArgumentException -> 0x00a9, NotFoundException -> 0x00ae, ClassNotFoundException -> 0x00b3, InstantiationException -> 0x00b8, IllegalAccessException -> 0x00bd, NoSuchFieldException -> 0x00c2 }
        r12 = r4.get(r8);	 Catch:{ NumberFormatException -> 0x00a4, IllegalArgumentException -> 0x00a9, NotFoundException -> 0x00ae, ClassNotFoundException -> 0x00b3, InstantiationException -> 0x00b8, IllegalAccessException -> 0x00bd, NoSuchFieldException -> 0x00c2 }
        r12 = r12.toString();	 Catch:{ NumberFormatException -> 0x00a4, IllegalArgumentException -> 0x00a9, NotFoundException -> 0x00ae, ClassNotFoundException -> 0x00b3, InstantiationException -> 0x00b8, IllegalAccessException -> 0x00bd, NoSuchFieldException -> 0x00c2 }
        r6 = java.lang.Integer.parseInt(r12);	 Catch:{ NumberFormatException -> 0x00a4, IllegalArgumentException -> 0x00a9, NotFoundException -> 0x00ae, ClassNotFoundException -> 0x00b3, InstantiationException -> 0x00b8, IllegalAccessException -> 0x00bd, NoSuchFieldException -> 0x00c2 }
        r10 = r2.widthPixels;	 Catch:{ NumberFormatException -> 0x00a4, IllegalArgumentException -> 0x00a9, NotFoundException -> 0x00ae, ClassNotFoundException -> 0x00b3, InstantiationException -> 0x00b8, IllegalAccessException -> 0x00bd, NoSuchFieldException -> 0x00c2 }
        r7 = com.clouds.util.DistinguishDevice.isWMTDevice();	 Catch:{ NumberFormatException -> 0x00a4, IllegalArgumentException -> 0x00a9, NotFoundException -> 0x00ae, ClassNotFoundException -> 0x00b3, InstantiationException -> 0x00b8, IllegalAccessException -> 0x00bd, NoSuchFieldException -> 0x00c2 }
        r9 = r2.heightPixels;	 Catch:{ NumberFormatException -> 0x00a4, IllegalArgumentException -> 0x00a9, NotFoundException -> 0x00ae, ClassNotFoundException -> 0x00b3, InstantiationException -> 0x00b8, IllegalAccessException -> 0x00bd, NoSuchFieldException -> 0x00c2 }
        if (r7 == 0) goto L_0x0097;
    L_0x0050:
        r12 = r2.heightPixels;	 Catch:{ NumberFormatException -> 0x00a4, IllegalArgumentException -> 0x00a9, NotFoundException -> 0x00ae, ClassNotFoundException -> 0x00b3, InstantiationException -> 0x00b8, IllegalAccessException -> 0x00bd, NoSuchFieldException -> 0x00c2 }
        r13 = r15.getResources();	 Catch:{ NumberFormatException -> 0x00a4, IllegalArgumentException -> 0x00a9, NotFoundException -> 0x00ae, ClassNotFoundException -> 0x00b3, InstantiationException -> 0x00b8, IllegalAccessException -> 0x00bd, NoSuchFieldException -> 0x00c2 }
        r13 = r13.getDimensionPixelSize(r5);	 Catch:{ NumberFormatException -> 0x00a4, IllegalArgumentException -> 0x00a9, NotFoundException -> 0x00ae, ClassNotFoundException -> 0x00b3, InstantiationException -> 0x00b8, IllegalAccessException -> 0x00bd, NoSuchFieldException -> 0x00c2 }
        r9 = r12 + r13;
    L_0x005c:
        if (r10 >= r9) goto L_0x0061;
    L_0x005e:
        r0 = r10;
        r10 = r9;
        r9 = r0;
    L_0x0061:
        r12 = TAG;	 Catch:{ NumberFormatException -> 0x00a4, IllegalArgumentException -> 0x00a9, NotFoundException -> 0x00ae, ClassNotFoundException -> 0x00b3, InstantiationException -> 0x00b8, IllegalAccessException -> 0x00bd, NoSuchFieldException -> 0x00c2 }
        r13 = new java.lang.StringBuilder;	 Catch:{ NumberFormatException -> 0x00a4, IllegalArgumentException -> 0x00a9, NotFoundException -> 0x00ae, ClassNotFoundException -> 0x00b3, InstantiationException -> 0x00b8, IllegalAccessException -> 0x00bd, NoSuchFieldException -> 0x00c2 }
        r14 = "screenWidth: ";
        r13.<init>(r14);	 Catch:{ NumberFormatException -> 0x00a4, IllegalArgumentException -> 0x00a9, NotFoundException -> 0x00ae, ClassNotFoundException -> 0x00b3, InstantiationException -> 0x00b8, IllegalAccessException -> 0x00bd, NoSuchFieldException -> 0x00c2 }
        r13 = r13.append(r10);	 Catch:{ NumberFormatException -> 0x00a4, IllegalArgumentException -> 0x00a9, NotFoundException -> 0x00ae, ClassNotFoundException -> 0x00b3, InstantiationException -> 0x00b8, IllegalAccessException -> 0x00bd, NoSuchFieldException -> 0x00c2 }
        r14 = "  screenHeight: ";
        r13 = r13.append(r14);	 Catch:{ NumberFormatException -> 0x00a4, IllegalArgumentException -> 0x00a9, NotFoundException -> 0x00ae, ClassNotFoundException -> 0x00b3, InstantiationException -> 0x00b8, IllegalAccessException -> 0x00bd, NoSuchFieldException -> 0x00c2 }
        r13 = r13.append(r9);	 Catch:{ NumberFormatException -> 0x00a4, IllegalArgumentException -> 0x00a9, NotFoundException -> 0x00ae, ClassNotFoundException -> 0x00b3, InstantiationException -> 0x00b8, IllegalAccessException -> 0x00bd, NoSuchFieldException -> 0x00c2 }
        r13 = r13.toString();	 Catch:{ NumberFormatException -> 0x00a4, IllegalArgumentException -> 0x00a9, NotFoundException -> 0x00ae, ClassNotFoundException -> 0x00b3, InstantiationException -> 0x00b8, IllegalAccessException -> 0x00bd, NoSuchFieldException -> 0x00c2 }
        com.clouds.debug.SystemDebug.e(r12, r13);	 Catch:{ NumberFormatException -> 0x00a4, IllegalArgumentException -> 0x00a9, NotFoundException -> 0x00ae, ClassNotFoundException -> 0x00b3, InstantiationException -> 0x00b8, IllegalAccessException -> 0x00bd, NoSuchFieldException -> 0x00c2 }
        r12 = new java.lang.StringBuilder;	 Catch:{ NumberFormatException -> 0x00a4, IllegalArgumentException -> 0x00a9, NotFoundException -> 0x00ae, ClassNotFoundException -> 0x00b3, InstantiationException -> 0x00b8, IllegalAccessException -> 0x00bd, NoSuchFieldException -> 0x00c2 }
        r13 = java.lang.String.valueOf(r10);	 Catch:{ NumberFormatException -> 0x00a4, IllegalArgumentException -> 0x00a9, NotFoundException -> 0x00ae, ClassNotFoundException -> 0x00b3, InstantiationException -> 0x00b8, IllegalAccessException -> 0x00bd, NoSuchFieldException -> 0x00c2 }
        r12.<init>(r13);	 Catch:{ NumberFormatException -> 0x00a4, IllegalArgumentException -> 0x00a9, NotFoundException -> 0x00ae, ClassNotFoundException -> 0x00b3, InstantiationException -> 0x00b8, IllegalAccessException -> 0x00bd, NoSuchFieldException -> 0x00c2 }
        r13 = "x";
        r12 = r12.append(r13);	 Catch:{ NumberFormatException -> 0x00a4, IllegalArgumentException -> 0x00a9, NotFoundException -> 0x00ae, ClassNotFoundException -> 0x00b3, InstantiationException -> 0x00b8, IllegalAccessException -> 0x00bd, NoSuchFieldException -> 0x00c2 }
        r12 = r12.append(r9);	 Catch:{ NumberFormatException -> 0x00a4, IllegalArgumentException -> 0x00a9, NotFoundException -> 0x00ae, ClassNotFoundException -> 0x00b3, InstantiationException -> 0x00b8, IllegalAccessException -> 0x00bd, NoSuchFieldException -> 0x00c2 }
        r11 = r12.toString();	 Catch:{ NumberFormatException -> 0x00a4, IllegalArgumentException -> 0x00a9, NotFoundException -> 0x00ae, ClassNotFoundException -> 0x00b3, InstantiationException -> 0x00b8, IllegalAccessException -> 0x00bd, NoSuchFieldException -> 0x00c2 }
    L_0x0096:
        return r11;
    L_0x0097:
        r12 = r2.heightPixels;	 Catch:{ NumberFormatException -> 0x00a4, IllegalArgumentException -> 0x00a9, NotFoundException -> 0x00ae, ClassNotFoundException -> 0x00b3, InstantiationException -> 0x00b8, IllegalAccessException -> 0x00bd, NoSuchFieldException -> 0x00c2 }
        r13 = r15.getResources();	 Catch:{ NumberFormatException -> 0x00a4, IllegalArgumentException -> 0x00a9, NotFoundException -> 0x00ae, ClassNotFoundException -> 0x00b3, InstantiationException -> 0x00b8, IllegalAccessException -> 0x00bd, NoSuchFieldException -> 0x00c2 }
        r13 = r13.getDimensionPixelSize(r5);	 Catch:{ NumberFormatException -> 0x00a4, IllegalArgumentException -> 0x00a9, NotFoundException -> 0x00ae, ClassNotFoundException -> 0x00b3, InstantiationException -> 0x00b8, IllegalAccessException -> 0x00bd, NoSuchFieldException -> 0x00c2 }
        r9 = r12 + r13;
        goto L_0x005c;
    L_0x00a4:
        r3 = move-exception;
        r3.printStackTrace();
        goto L_0x0096;
    L_0x00a9:
        r3 = move-exception;
        r3.printStackTrace();
        goto L_0x0096;
    L_0x00ae:
        r3 = move-exception;
        r3.printStackTrace();
        goto L_0x0096;
    L_0x00b3:
        r3 = move-exception;
        r3.printStackTrace();
        goto L_0x0096;
    L_0x00b8:
        r3 = move-exception;
        r3.printStackTrace();
        goto L_0x0096;
    L_0x00bd:
        r3 = move-exception;
        r3.printStackTrace();
        goto L_0x0096;
    L_0x00c2:
        r3 = move-exception;
        r3.printStackTrace();
        goto L_0x0096;
        */
    }

    public static String getScrsize() {
        String scrsize = SysProperty.getSysProperty("eken.hardware.screen.size", "7");
        return (scrsize == null || scrsize.equals("")) ? SysProperty.getSysProperty("ro.hardware.screen.size") : scrsize;
    }

    public static String getSdk() {
        String sdk = SysProperty.getSysProperty("eken.build.version.sdk");
        return (sdk == null || sdk.equals("")) ? SysProperty.getSysProperty("ro.build.version.sdk") : sdk;
    }
}