package com.mopub.common.util;

import android.os.Build.VERSION;

public enum VersionCode {
    BASE(1),
    BASE_1_1(2),
    CUPCAKE(3),
    DONUT(4),
    ECLAIR(5),
    ECLAIR_0_1(6),
    ECLAIR_MR1(7),
    FROYO(8),
    GINGERBREAD(9),
    GINGERBREAD_MR1(10),
    HONEYCOMB(11),
    HONEYCOMB_MR1(12),
    HONEYCOMB_MR2(13),
    ICE_CREAM_SANDWICH(14),
    ICE_CREAM_SANDWICH_MR1(15),
    JELLY_BEAN(16),
    JELLY_BEAN_MR1(17),
    JELLY_BEAN_MR2(18),
    KITKAT(19),
    CUR_DEVELOPMENT(10000);
    private int mApiLevel;

    static {
        BASE = new VersionCode("BASE", 0, 1);
        BASE_1_1 = new VersionCode("BASE_1_1", 1, 2);
        CUPCAKE = new VersionCode("CUPCAKE", 2, 3);
        DONUT = new VersionCode("DONUT", 3, 4);
        ECLAIR = new VersionCode("ECLAIR", 4, 5);
        ECLAIR_0_1 = new VersionCode("ECLAIR_0_1", 5, 6);
        ECLAIR_MR1 = new VersionCode("ECLAIR_MR1", 6, 7);
        FROYO = new VersionCode("FROYO", 7, 8);
        GINGERBREAD = new VersionCode("GINGERBREAD", 8, 9);
        GINGERBREAD_MR1 = new VersionCode("GINGERBREAD_MR1", 9, 10);
        HONEYCOMB = new VersionCode("HONEYCOMB", 10, 11);
        HONEYCOMB_MR1 = new VersionCode("HONEYCOMB_MR1", 11, 12);
        HONEYCOMB_MR2 = new VersionCode("HONEYCOMB_MR2", 12, 13);
        ICE_CREAM_SANDWICH = new VersionCode("ICE_CREAM_SANDWICH", 13, 14);
        ICE_CREAM_SANDWICH_MR1 = new VersionCode("ICE_CREAM_SANDWICH_MR1", 14, 15);
        JELLY_BEAN = new VersionCode("JELLY_BEAN", 15, 16);
        JELLY_BEAN_MR1 = new VersionCode("JELLY_BEAN_MR1", 16, 17);
        JELLY_BEAN_MR2 = new VersionCode("JELLY_BEAN_MR2", 17, 18);
        KITKAT = new VersionCode("KITKAT", 18, 19);
        CUR_DEVELOPMENT = new VersionCode("CUR_DEVELOPMENT", 19, 10000);
        ENUM$VALUES = new VersionCode[]{BASE, BASE_1_1, CUPCAKE, DONUT, ECLAIR, ECLAIR_0_1, ECLAIR_MR1, FROYO, GINGERBREAD, GINGERBREAD_MR1, HONEYCOMB, HONEYCOMB_MR1, HONEYCOMB_MR2, ICE_CREAM_SANDWICH, ICE_CREAM_SANDWICH_MR1, JELLY_BEAN, JELLY_BEAN_MR1, JELLY_BEAN_MR2, KITKAT, CUR_DEVELOPMENT};
    }

    private VersionCode(int apiLevel) {
        this.mApiLevel = apiLevel;
    }

    public static VersionCode currentApiLevel() {
        return forApiLevel(VERSION.SDK_INT);
    }

    private static VersionCode forApiLevel(int targetApiLevel) {
        VersionCode[] values = values();
        int length = values.length;
        int i = 0;
        while (i < length) {
            VersionCode versionCode = values[i];
            if (versionCode.getApiLevel() == targetApiLevel) {
                return versionCode;
            }
            i++;
        }
        return CUR_DEVELOPMENT;
    }

    public int getApiLevel() {
        return this.mApiLevel;
    }

    public boolean isAtLeast(VersionCode that) {
        return getApiLevel() >= that.getApiLevel();
    }

    public boolean isAtMost(VersionCode that) {
        return getApiLevel() <= that.getApiLevel();
    }

    public boolean isBelow(VersionCode that) {
        return getApiLevel() < that.getApiLevel();
    }
}