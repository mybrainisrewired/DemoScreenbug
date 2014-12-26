package com.mopub.nativeads;

public enum NativeErrorCode {
    EMPTY_AD_RESPONSE("Server returned empty response."),
    INVALID_JSON("Unable to parse JSON response from server."),
    IMAGE_DOWNLOAD_FAILURE("Unable to download images associated with ad."),
    INVALID_REQUEST_URL("Invalid request url."),
    UNEXPECTED_RESPONSE_CODE("Received unexpected response code from server."),
    SERVER_ERROR_RESPONSE_CODE("Server returned erroneous response code."),
    CONNECTION_ERROR("Network is unavailable."),
    UNSPECIFIED("Unspecified error occurred."),
    NETWORK_INVALID_REQUEST("Third-party network received invalid request."),
    NETWORK_TIMEOUT("Third-party network failed to respond in a timely manner."),
    NETWORK_NO_FILL("Third-party network failed to provide an ad."),
    NETWORK_INVALID_STATE("Third-party network failed due to invalid internal state."),
    NATIVE_ADAPTER_CONFIGURATION_ERROR("Custom Event Native was configured incorrectly."),
    NATIVE_ADAPTER_NOT_FOUND("Unable to find Custom Event Native.");
    private final String message;

    static {
        String str = "Server returned empty response.";
        EMPTY_AD_RESPONSE = new NativeErrorCode("EMPTY_AD_RESPONSE", 0, "Server returned empty response.");
        str = "Unable to parse JSON response from server.";
        INVALID_JSON = new NativeErrorCode("INVALID_JSON", 1, "Unable to parse JSON response from server.");
        str = "Unable to download images associated with ad.";
        IMAGE_DOWNLOAD_FAILURE = new NativeErrorCode("IMAGE_DOWNLOAD_FAILURE", 2, "Unable to download images associated with ad.");
        str = "Invalid request url.";
        INVALID_REQUEST_URL = new NativeErrorCode("INVALID_REQUEST_URL", 3, "Invalid request url.");
        str = "Received unexpected response code from server.";
        UNEXPECTED_RESPONSE_CODE = new NativeErrorCode("UNEXPECTED_RESPONSE_CODE", 4, "Received unexpected response code from server.");
        String str2 = "Server returned erroneous response code.";
        SERVER_ERROR_RESPONSE_CODE = new NativeErrorCode("SERVER_ERROR_RESPONSE_CODE", 5, "Server returned erroneous response code.");
        str2 = "Network is unavailable.";
        CONNECTION_ERROR = new NativeErrorCode("CONNECTION_ERROR", 6, "Network is unavailable.");
        str2 = "Unspecified error occurred.";
        UNSPECIFIED = new NativeErrorCode("UNSPECIFIED", 7, "Unspecified error occurred.");
        str2 = "Third-party network received invalid request.";
        NETWORK_INVALID_REQUEST = new NativeErrorCode("NETWORK_INVALID_REQUEST", 8, "Third-party network received invalid request.");
        str2 = "Third-party network failed to respond in a timely manner.";
        NETWORK_TIMEOUT = new NativeErrorCode("NETWORK_TIMEOUT", 9, "Third-party network failed to respond in a timely manner.");
        str2 = "Third-party network failed to provide an ad.";
        NETWORK_NO_FILL = new NativeErrorCode("NETWORK_NO_FILL", 10, "Third-party network failed to provide an ad.");
        str2 = "Third-party network failed due to invalid internal state.";
        NETWORK_INVALID_STATE = new NativeErrorCode("NETWORK_INVALID_STATE", 11, "Third-party network failed due to invalid internal state.");
        str2 = "Custom Event Native was configured incorrectly.";
        NATIVE_ADAPTER_CONFIGURATION_ERROR = new NativeErrorCode("NATIVE_ADAPTER_CONFIGURATION_ERROR", 12, "Custom Event Native was configured incorrectly.");
        str2 = "Unable to find Custom Event Native.";
        NATIVE_ADAPTER_NOT_FOUND = new NativeErrorCode("NATIVE_ADAPTER_NOT_FOUND", 13, "Unable to find Custom Event Native.");
        ENUM$VALUES = new NativeErrorCode[]{EMPTY_AD_RESPONSE, INVALID_JSON, IMAGE_DOWNLOAD_FAILURE, INVALID_REQUEST_URL, UNEXPECTED_RESPONSE_CODE, SERVER_ERROR_RESPONSE_CODE, CONNECTION_ERROR, UNSPECIFIED, NETWORK_INVALID_REQUEST, NETWORK_TIMEOUT, NETWORK_NO_FILL, NETWORK_INVALID_STATE, NATIVE_ADAPTER_CONFIGURATION_ERROR, NATIVE_ADAPTER_NOT_FOUND};
    }

    private NativeErrorCode(String message) {
        this.message = message;
    }

    public final String toString() {
        return this.message;
    }
}