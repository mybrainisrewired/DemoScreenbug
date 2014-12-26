package com.nostra13.universalimageloader.core.assist;

public enum ImageScaleType {
    NONE,
    IN_SAMPLE_POWER_OF_2,
    IN_SAMPLE_INT,
    EXACTLY,
    EXACTLY_STRETCHED;

    static {
        NONE = new ImageScaleType("NONE", 0);
        IN_SAMPLE_POWER_OF_2 = new ImageScaleType("IN_SAMPLE_POWER_OF_2", 1);
        IN_SAMPLE_INT = new ImageScaleType("IN_SAMPLE_INT", 2);
        EXACTLY = new ImageScaleType("EXACTLY", 3);
        EXACTLY_STRETCHED = new ImageScaleType("EXACTLY_STRETCHED", 4);
        ENUM$VALUES = new ImageScaleType[]{NONE, IN_SAMPLE_POWER_OF_2, IN_SAMPLE_INT, EXACTLY, EXACTLY_STRETCHED};
    }
}