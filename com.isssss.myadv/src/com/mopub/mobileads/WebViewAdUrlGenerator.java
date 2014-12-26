package com.mopub.mobileads;

import android.content.Context;
import android.location.Location;
import com.mopub.common.AdUrlGenerator;
import com.mopub.common.ClientMetadata;
import com.mopub.common.LocationService;
import com.mopub.common.MoPub;
import com.mopub.common.util.DateAndTime;
import com.mopub.mobileads.util.Mraids;

public class WebViewAdUrlGenerator extends AdUrlGenerator {
    public WebViewAdUrlGenerator(Context context) {
        super(context);
    }

    private boolean detectIsMraidSupported() {
        try {
            Class.forName("com.mopub.mobileads.MraidView");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public String generateUrlString(String serverHostname) {
        initUrlString(serverHostname, MoPubView.AD_HANDLER);
        ClientMetadata clientMetadata = ClientMetadata.getInstance(this.mContext);
        setApiVersion("6");
        setAdUnitId(this.mAdUnitId);
        setSdkVersion(clientMetadata.getSdkVersion());
        setDeviceInfo(new String[]{clientMetadata.getDeviceManufacturer(), clientMetadata.getDeviceModel(), clientMetadata.getDeviceProduct()});
        setUdid(clientMetadata.getUdid());
        setDoNotTrack(clientMetadata.getDoNoTrack());
        setKeywords(this.mKeywords);
        Location location = this.mLocation;
        if (location == null) {
            location = LocationService.getLastKnownLocation(this.mContext, MoPub.getLocationPrecision(), MoPub.getLocationAwareness());
        }
        setLocation(location);
        setTimezone(DateAndTime.getTimeZoneOffsetString());
        setOrientation(clientMetadata.getOrientationString());
        setDensity(clientMetadata.getDensity());
        setMraidFlag(detectIsMraidSupported());
        String networkOperator = clientMetadata.getNetworkOperator();
        setMccCode(networkOperator);
        setMncCode(networkOperator);
        setIsoCountryCode(clientMetadata.getIsoCountryCode());
        setCarrierName(clientMetadata.getNetworkOperatorName());
        setNetworkType(clientMetadata.getActiveNetworkType());
        setAppVersion(clientMetadata.getAppVersion());
        setExternalStoragePermission(Mraids.isStorePictureSupported(this.mContext));
        setTwitterAppInstalledFlag();
        return getFinalUrlString();
    }
}