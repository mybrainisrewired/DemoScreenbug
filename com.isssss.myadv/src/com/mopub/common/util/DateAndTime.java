package com.mopub.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateAndTime {
    protected static DateAndTime instance;

    static {
        instance = new DateAndTime();
    }

    public static String getTimeZoneOffsetString() {
        SimpleDateFormat format = new SimpleDateFormat("Z");
        format.setTimeZone(localTimeZone());
        return format.format(now());
    }

    public static TimeZone localTimeZone() {
        return instance.internalLocalTimeZone();
    }

    public static Date now() {
        return instance.internalNow();
    }

    @Deprecated
    public static void setInstance(DateAndTime newInstance) {
        instance = newInstance;
    }

    public TimeZone internalLocalTimeZone() {
        return TimeZone.getDefault();
    }

    public Date internalNow() {
        return new Date();
    }
}