package com.inmobi.commons.network;

public enum ErrorCode {
    INVALID_REQUEST("Invalid request"),
    INTERNAL_ERROR("An internal error occurred while fetching"),
    CONNECTION_ERROR("Socket timeout exception"),
    NETWORK_ERROR("Network failure. Check your connection");
    private String a;

    static {
        String str = "Invalid request";
        INVALID_REQUEST = new ErrorCode("INVALID_REQUEST", 0, "Invalid request");
        str = "An internal error occurred while fetching";
        INTERNAL_ERROR = new ErrorCode("INTERNAL_ERROR", 1, "An internal error occurred while fetching");
        str = "Socket timeout exception";
        CONNECTION_ERROR = new ErrorCode("CONNECTION_ERROR", 2, "Socket timeout exception");
        str = "Network failure. Check your connection";
        NETWORK_ERROR = new ErrorCode("NETWORK_ERROR", 3, "Network failure. Check your connection");
        b = new ErrorCode[]{INVALID_REQUEST, INTERNAL_ERROR, CONNECTION_ERROR, NETWORK_ERROR};
    }

    private ErrorCode(String str) {
        this.a = str;
    }

    public void setMessage(String str) {
        this.a = str;
    }

    public String toString() {
        return this.a;
    }
}