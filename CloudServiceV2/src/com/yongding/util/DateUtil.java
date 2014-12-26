package com.yongding.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;

public class DateUtil {
    public long firstMonthDays(Date date) {
        String time = new SimpleDateFormat("yyyy-MM-dd").format(date);
        return ((long) ((((getDayCount(time) - Integer.parseInt(time.split("-")[2])) * 24) * 60) * 60)) * 1000;
    }

    public int getDayCount(String dateTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Calendar calendar = new GregorianCalendar();
        try {
            calendar.setTime(sdf.parse(dateTime));
        } catch (Exception e) {
            Trace.e(e.toString());
        }
        return calendar.getActualMaximum(JsonWriteContext.STATUS_EXPECT_NAME);
    }

    public long getFirstRandDay() {
        return ((long) (((((int) ((Math.random() * 15.0d) + 1.0d)) * 24) * 60) * 60)) * 1000;
    }

    public Date getNextMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(ClassWriter.COMPUTE_FRAMES, 1);
        return c.getTime();
    }

    public long getSecondRandDay(Date date) {
        return ((long) (((((int) ((Math.random() * ((double) (getDayCount(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date)) - 15))) + 16.0d)) * 24) * 60) * 60)) * 1000;
    }
}