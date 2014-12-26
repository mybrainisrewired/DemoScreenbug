package com.mopub.nativeads;

import android.content.Context;
import com.inmobi.commons.analytics.db.AnalyticsEvent;
import com.mopub.common.BaseUrlGenerator;
import com.mopub.common.ClientMetadata;

class PositioningUrlGenerator extends BaseUrlGenerator {
    private static final String POSITIONING_API_VERSION = "1";
    private String mAdUnitId;
    private final Context mContext;

    public PositioningUrlGenerator(Context context) {
        this.mContext = context;
    }

    private void setAdUnitId(String adUnitId) {
        addParam(AnalyticsEvent.EVENT_ID, adUnitId);
    }

    private void setSdkVersion(String sdkVersion) {
        addParam("nsv", sdkVersion);
    }

    public String generateUrlString(String serverHostname) {
        initUrlString(serverHostname, "/m/pos");
        setAdUnitId(this.mAdUnitId);
        setApiVersion(POSITIONING_API_VERSION);
        ClientMetadata clientMetadata = ClientMetadata.getInstance(this.mContext);
        setSdkVersion(clientMetadata.getSdkVersion());
        setDeviceInfo(new String[]{clientMetadata.getDeviceManufacturer(), clientMetadata.getDeviceModel(), clientMetadata.getDeviceProduct()});
        setUdid(clientMetadata.getUdid());
        setAppVersion(clientMetadata.getAppVersion());
        return getFinalUrlString();
    }

    public PositioningUrlGenerator withAdUnitId(String adUnitId) {
        this.mAdUnitId = adUnitId;
        return this;
    }
}