package com.inmobi.monetization.internal.configs;

import com.inmobi.commons.internal.InternalSDKUtil;
import com.inmobi.commons.metric.MetricConfigParams;
import com.inmobi.commons.uid.UIDMapConfigParams;
import com.inmobi.monetization.internal.Ad;
import com.isssss.myadv.constant.ParamConst;
import java.util.Map;

public class ConfigParams {
    int a;
    int b;
    int c;
    int d;
    IMAIConfigParams e;
    MetricConfigParams f;
    NativeConfigParams g;
    PlayableAdsConfigParams h;
    private UIDMapConfigParams i;

    public ConfigParams() {
        this.a = 20;
        this.b = 60;
        this.c = 60;
        this.d = 60;
        this.e = new IMAIConfigParams();
        this.f = new MetricConfigParams();
        this.i = new UIDMapConfigParams();
        this.g = new NativeConfigParams();
        this.h = new PlayableAdsConfigParams();
    }

    public int getDefaultRefreshRate() {
        return this.b;
    }

    public Map<String, Boolean> getDeviceIdMaskMap() {
        return this.i.getMap();
    }

    public int getFetchTimeOut() {
        return this.c * 1000;
    }

    public IMAIConfigParams getImai() {
        return this.e;
    }

    public MetricConfigParams getMetric() {
        return this.f;
    }

    public int getMinimumRefreshRate() {
        return this.a;
    }

    public NativeConfigParams getNativeSdkConfigParams() {
        return this.g;
    }

    public PlayableAdsConfigParams getPlayableConfigParams() {
        return this.h;
    }

    public int getRenderTimeOut() {
        return this.d * 1000;
    }

    public void setFromMap(Map<String, Object> map) {
        this.a = InternalSDKUtil.getIntFromMap(map, "mrr", 1, 2147483647L);
        this.b = InternalSDKUtil.getIntFromMap(map, "drr", -1, 2147483647L);
        this.c = InternalSDKUtil.getIntFromMap(map, "fto", 1, 2147483647L);
        this.d = InternalSDKUtil.getIntFromMap(map, "rto", 1, 2147483647L);
        this.e.setFromMap((Map) map.get("imai"));
        this.f.setFromMap((Map) map.get("metric"));
        this.i.setMap(InternalSDKUtil.getObjectFromMap(map, ParamConst.IDS));
        this.g.setFromMap((Map) map.get(Ad.AD_TYPE_NATIVE));
        this.h.setFromMap((Map) map.get("playable"));
    }
}