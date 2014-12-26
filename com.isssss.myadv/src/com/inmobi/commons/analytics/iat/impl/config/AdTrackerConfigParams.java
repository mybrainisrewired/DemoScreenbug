package com.inmobi.commons.analytics.iat.impl.config;

import com.inmobi.commons.internal.InternalSDKUtil;
import com.inmobi.commons.metric.MetricConfigParams;
import com.inmobi.commons.uid.UIDMapConfigParams;
import com.isssss.myadv.constant.ParamConst;
import com.millennialmedia.android.MMAdView;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import java.util.Map;

public class AdTrackerConfigParams {
    private static final String a;
    private int b;
    private int c;
    private String d;
    private AdTrackerGoalRetryParams e;
    private MetricConfigParams f;
    private UIDMapConfigParams g;

    static {
        a = "Starting.*: Intent.*(?:http://market.android.com/details|market://details|play.google.com).*(?:id=" + InternalSDKUtil.getContext().getPackageName() + ").*referrer=([^&\\s]+)";
    }

    public AdTrackerConfigParams() {
        this.b = 60;
        this.c = 300;
        this.d = a;
        this.e = new AdTrackerGoalRetryParams();
        this.f = new MetricConfigParams();
        this.g = new UIDMapConfigParams();
    }

    public int getConnectionTimeout() {
        return this.b * 1000;
    }

    public Map<String, Boolean> getDeviceIdMaskMap() {
        return this.g.getMap();
    }

    public String getLogcatPattern() {
        return this.d;
    }

    public MetricConfigParams getMetric() {
        return this.f;
    }

    public int getReferrerWaitTime() {
        return 300000;
    }

    public int getReferrerWaitTimeRetryCount() {
        return MMAdView.TRANSITION_DOWN;
    }

    public int getReferrerWaitTimeRetryInterval() {
        return BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT;
    }

    public AdTrackerGoalRetryParams getRetryParams() {
        return this.e;
    }

    public int getWebviewTimeout() {
        return this.c * 1000;
    }

    public void setFromMap(Map<String, Object> map) {
        this.b = InternalSDKUtil.getIntFromMap(map, "cto", 1, 2147483647L);
        this.c = InternalSDKUtil.getIntFromMap(map, "wto", 1, 2147483647L);
        this.d = InternalSDKUtil.getStringFromMap(map, "rlp").replace("$PKG", InternalSDKUtil.getContext().getPackageName());
        this.e.setFromMap((Map) map.get("rp"));
        this.f.setFromMap((Map) map.get("metric"));
        this.g.setMap(InternalSDKUtil.getObjectFromMap(map, ParamConst.IDS));
    }
}