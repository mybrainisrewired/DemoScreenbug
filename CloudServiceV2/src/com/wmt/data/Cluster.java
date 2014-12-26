package com.wmt.data;

import android.content.Context;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import java.util.ArrayList;
import org.codehaus.jackson.impl.JsonWriteContext;

// compiled from: TimeClustering.java
class Cluster {
    private static final String MMDDYY_FORMAT = "MMddyy";
    private static final String TAG = "Cluster";
    public boolean mGeographicallySeparatedFromPrevCluster;
    private ArrayList<SmallItem> mItems;

    public Cluster() {
        this.mGeographicallySeparatedFromPrevCluster = false;
        this.mItems = new ArrayList();
    }

    public void addItem(SmallItem item) {
        this.mItems.add(item);
    }

    public String generateCaption(Context context) {
        int n = this.mItems.size();
        long minTimestamp = 0;
        long maxTimestamp = 0;
        int i = 0;
        while (i < n) {
            long t = ((SmallItem) this.mItems.get(i)).dateInMs;
            if (t != 0) {
                if (minTimestamp == 0) {
                    maxTimestamp = t;
                    minTimestamp = t;
                } else {
                    minTimestamp = Math.min(minTimestamp, t);
                    maxTimestamp = Math.max(maxTimestamp, t);
                }
            }
            i++;
        }
        if (minTimestamp == 0) {
            return "";
        }
        String minDay = DateFormat.format(MMDDYY_FORMAT, minTimestamp).toString();
        String maxDay = DateFormat.format(MMDDYY_FORMAT, maxTimestamp).toString();
        if (!minDay.substring(JsonWriteContext.STATUS_EXPECT_VALUE).equals(maxDay.substring(JsonWriteContext.STATUS_EXPECT_VALUE))) {
            return DateUtils.formatDateRange(context, minTimestamp, maxTimestamp, 65584);
        }
        String formatDateRange = DateUtils.formatDateRange(context, minTimestamp, maxTimestamp, 524288);
        if (!minDay.equals(maxDay) || DateUtils.formatDateTime(context, minTimestamp, 65552).equals(DateUtils.formatDateTime(context, minTimestamp, 65556))) {
            return formatDateRange;
        }
        long midTimestamp = (minTimestamp + maxTimestamp) / 2;
        return DateUtils.formatDateRange(context, midTimestamp, midTimestamp, 65553);
    }

    public ArrayList<SmallItem> getItems() {
        return this.mItems;
    }

    public SmallItem getLastItem() {
        int n = this.mItems.size();
        return n == 0 ? null : (SmallItem) this.mItems.get(n - 1);
    }

    public int size() {
        return this.mItems.size();
    }
}