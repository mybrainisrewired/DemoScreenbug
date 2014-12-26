package com.mopub.nativeads;

import android.content.Context;
import android.location.Location;
import android.text.TextUtils;
import com.mopub.common.AdUrlGenerator;
import com.mopub.common.ClientMetadata;
import com.mopub.common.LocationService;
import com.mopub.common.MoPub;
import com.mopub.common.util.DateAndTime;
import com.mopub.common.util.Strings;
import com.mopub.mobileads.MoPubView;

class NativeUrlGenerator extends AdUrlGenerator {
    private String mDesiredAssets;
    private String mSequenceNumber;

    NativeUrlGenerator(Context context) {
        super(context);
    }

    private void setDesiredAssets() {
        if (this.mDesiredAssets != null && !Strings.isEmpty(this.mDesiredAssets)) {
            addParam("assets", this.mDesiredAssets);
        }
    }

    private void setSequenceNumber() {
        if (!TextUtils.isEmpty(this.mSequenceNumber)) {
            addParam("MAGIC_NO", this.mSequenceNumber);
        }
    }

    public String generateUrlString(String serverHostname) {
        initUrlString(serverHostname, MoPubView.AD_HANDLER);
        setAdUnitId(this.mAdUnitId);
        setKeywords(this.mKeywords);
        Location location = this.mLocation;
        if (location == null) {
            location = LocationService.getLastKnownLocation(this.mContext, MoPub.getLocationPrecision(), MoPub.getLocationAwareness());
        }
        setLocation(location);
        ClientMetadata clientMetadata = ClientMetadata.getInstance(this.mContext);
        setSdkVersion(clientMetadata.getSdkVersion());
        setDeviceInfo(new String[]{clientMetadata.getDeviceManufacturer(), clientMetadata.getDeviceModel(), clientMetadata.getDeviceProduct()});
        setUdid(clientMetadata.getUdid());
        setDoNotTrack(clientMetadata.getDoNoTrack());
        setTimezone(DateAndTime.getTimeZoneOffsetString());
        setOrientation(clientMetadata.getOrientationString());
        setDensity(clientMetadata.getDensity());
        String networkOperator = clientMetadata.getNetworkOperator();
        setMccCode(networkOperator);
        setMncCode(networkOperator);
        setIsoCountryCode(clientMetadata.getIsoCountryCode());
        setCarrierName(clientMetadata.getNetworkOperatorName());
        setNetworkType(clientMetadata.getActiveNetworkType());
        setAppVersion(clientMetadata.getAppVersion());
        setTwitterAppInstalledFlag();
        setDesiredAssets();
        setSequenceNumber();
        return getFinalUrlString();
    }

    protected void setSdkVersion(String sdkVersion) {
        addParam("nsv", sdkVersion);
    }

    public NativeUrlGenerator withAdUnitId(String adUnitId) {
        this.mAdUnitId = adUnitId;
        return this;
    }

    NativeUrlGenerator withRequest(RequestParameters requestParameters) {
        if (requestParameters != null) {
            this.mKeywords = requestParameters.getKeywords();
            this.mLocation = requestParameters.getLocation();
            this.mDesiredAssets = requestParameters.getDesiredAssets();
        }
        return this;
    }

    NativeUrlGenerator withSequenceNumber(int sequenceNumber) {
        this.mSequenceNumber = String.valueOf(sequenceNumber);
        return this;
    }
}