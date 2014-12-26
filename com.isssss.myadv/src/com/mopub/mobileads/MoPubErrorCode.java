package com.mopub.mobileads;

public enum MoPubErrorCode {
    NO_FILL("No ads found."),
    SERVER_ERROR("Unable to connect to MoPub adserver."),
    INTERNAL_ERROR("Unable to serve ad due to invalid internal state."),
    CANCELLED("Ad request was cancelled."),
    ADAPTER_NOT_FOUND("Unable to find Native Network or Custom Event adapter."),
    ADAPTER_CONFIGURATION_ERROR("Native Network or Custom Event adapter was configured incorrectly."),
    NETWORK_TIMEOUT("Third-party network failed to respond in a timely manner."),
    NETWORK_NO_FILL("Third-party network failed to provide an ad."),
    NETWORK_INVALID_STATE("Third-party network failed due to invalid internal state."),
    MRAID_LOAD_ERROR("Error loading MRAID ad."),
    VIDEO_CACHE_ERROR("Error creating a cache to store downloaded videos."),
    VIDEO_DOWNLOAD_ERROR("Error downloading video."),
    UNSPECIFIED("Unspecified error.");
    private final String message;

    static {
        String str = "No ads found.";
        NO_FILL = new MoPubErrorCode("NO_FILL", 0, "No ads found.");
        str = "Unable to connect to MoPub adserver.";
        SERVER_ERROR = new MoPubErrorCode("SERVER_ERROR", 1, "Unable to connect to MoPub adserver.");
        str = "Unable to serve ad due to invalid internal state.";
        INTERNAL_ERROR = new MoPubErrorCode("INTERNAL_ERROR", 2, "Unable to serve ad due to invalid internal state.");
        str = "Ad request was cancelled.";
        CANCELLED = new MoPubErrorCode("CANCELLED", 3, "Ad request was cancelled.");
        str = "Unable to find Native Network or Custom Event adapter.";
        ADAPTER_NOT_FOUND = new MoPubErrorCode("ADAPTER_NOT_FOUND", 4, "Unable to find Native Network or Custom Event adapter.");
        String str2 = "Native Network or Custom Event adapter was configured incorrectly.";
        ADAPTER_CONFIGURATION_ERROR = new MoPubErrorCode("ADAPTER_CONFIGURATION_ERROR", 5, "Native Network or Custom Event adapter was configured incorrectly.");
        str2 = "Third-party network failed to respond in a timely manner.";
        NETWORK_TIMEOUT = new MoPubErrorCode("NETWORK_TIMEOUT", 6, "Third-party network failed to respond in a timely manner.");
        str2 = "Third-party network failed to provide an ad.";
        NETWORK_NO_FILL = new MoPubErrorCode("NETWORK_NO_FILL", 7, "Third-party network failed to provide an ad.");
        str2 = "Third-party network failed due to invalid internal state.";
        NETWORK_INVALID_STATE = new MoPubErrorCode("NETWORK_INVALID_STATE", 8, "Third-party network failed due to invalid internal state.");
        str2 = "Error loading MRAID ad.";
        MRAID_LOAD_ERROR = new MoPubErrorCode("MRAID_LOAD_ERROR", 9, "Error loading MRAID ad.");
        str2 = "Error creating a cache to store downloaded videos.";
        VIDEO_CACHE_ERROR = new MoPubErrorCode("VIDEO_CACHE_ERROR", 10, "Error creating a cache to store downloaded videos.");
        str2 = "Error downloading video.";
        VIDEO_DOWNLOAD_ERROR = new MoPubErrorCode("VIDEO_DOWNLOAD_ERROR", 11, "Error downloading video.");
        str2 = "Unspecified error.";
        UNSPECIFIED = new MoPubErrorCode("UNSPECIFIED", 12, "Unspecified error.");
        ENUM$VALUES = new MoPubErrorCode[]{NO_FILL, SERVER_ERROR, INTERNAL_ERROR, CANCELLED, ADAPTER_NOT_FOUND, ADAPTER_CONFIGURATION_ERROR, NETWORK_TIMEOUT, NETWORK_NO_FILL, NETWORK_INVALID_STATE, MRAID_LOAD_ERROR, VIDEO_CACHE_ERROR, VIDEO_DOWNLOAD_ERROR, UNSPECIFIED};
    }

    private MoPubErrorCode(String message) {
        this.message = message;
    }

    public String toString() {
        return this.message;
    }
}