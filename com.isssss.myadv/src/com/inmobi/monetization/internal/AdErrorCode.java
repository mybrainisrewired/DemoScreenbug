package com.inmobi.monetization.internal;

public enum AdErrorCode {
    INVALID_REQUEST("Invalid ad request"),
    INTERNAL_ERROR("An error occurred while fetching the ad"),
    NO_FILL("The ad request was successful, but no ad was returned"),
    AD_CLICK_IN_PROGRESS("Ad click is in progress, cannot load new ad"),
    AD_DOWNLOAD_IN_PROGRESS("Ad download is in progress, cannot load new ad"),
    INVALID_APP_ID("Invalid App Id"),
    ADREQUEST_CANCELLED("Stop loading invoked on the ad"),
    AD_RENDERING_TIMEOUT("Failed to render ad"),
    DO_MONETIZE("Please load a mediation network"),
    DO_NOTHING("No Ads"),
    NETWORK_ERROR("Ad network failed to retrieve ad");
    private String a;

    static {
        String str = "Invalid ad request";
        INVALID_REQUEST = new AdErrorCode("INVALID_REQUEST", 0, "Invalid ad request");
        str = "An error occurred while fetching the ad";
        INTERNAL_ERROR = new AdErrorCode("INTERNAL_ERROR", 1, "An error occurred while fetching the ad");
        str = "The ad request was successful, but no ad was returned";
        NO_FILL = new AdErrorCode("NO_FILL", 2, "The ad request was successful, but no ad was returned");
        str = "Ad click is in progress, cannot load new ad";
        AD_CLICK_IN_PROGRESS = new AdErrorCode("AD_CLICK_IN_PROGRESS", 3, "Ad click is in progress, cannot load new ad");
        str = "Ad download is in progress, cannot load new ad";
        AD_DOWNLOAD_IN_PROGRESS = new AdErrorCode("AD_DOWNLOAD_IN_PROGRESS", 4, "Ad download is in progress, cannot load new ad");
        String str2 = "Invalid App Id";
        INVALID_APP_ID = new AdErrorCode("INVALID_APP_ID", 5, "Invalid App Id");
        str2 = "Stop loading invoked on the ad";
        ADREQUEST_CANCELLED = new AdErrorCode("ADREQUEST_CANCELLED", 6, "Stop loading invoked on the ad");
        str2 = "Failed to render ad";
        AD_RENDERING_TIMEOUT = new AdErrorCode("AD_RENDERING_TIMEOUT", 7, "Failed to render ad");
        str2 = "Please load a mediation network";
        DO_MONETIZE = new AdErrorCode("DO_MONETIZE", 8, "Please load a mediation network");
        str2 = "No Ads";
        DO_NOTHING = new AdErrorCode("DO_NOTHING", 9, "No Ads");
        str2 = "Ad network failed to retrieve ad";
        NETWORK_ERROR = new AdErrorCode("NETWORK_ERROR", 10, "Ad network failed to retrieve ad");
        b = new AdErrorCode[]{INVALID_REQUEST, INTERNAL_ERROR, NO_FILL, AD_CLICK_IN_PROGRESS, AD_DOWNLOAD_IN_PROGRESS, INVALID_APP_ID, ADREQUEST_CANCELLED, AD_RENDERING_TIMEOUT, DO_MONETIZE, DO_NOTHING, NETWORK_ERROR};
    }

    private AdErrorCode(String str) {
        this.a = str;
    }

    public void setMessage(String str) {
        this.a = str;
    }

    public String toString() {
        return this.a;
    }
}