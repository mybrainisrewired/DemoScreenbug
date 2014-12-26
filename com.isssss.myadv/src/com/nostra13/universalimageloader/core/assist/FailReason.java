package com.nostra13.universalimageloader.core.assist;

public class FailReason {
    private final Throwable cause;
    private final FailType type;

    public enum FailType {
        IO_ERROR,
        DECODING_ERROR,
        NETWORK_DENIED,
        OUT_OF_MEMORY,
        UNKNOWN;

        static {
            IO_ERROR = new com.nostra13.universalimageloader.core.assist.FailReason.FailType("IO_ERROR", 0);
            DECODING_ERROR = new com.nostra13.universalimageloader.core.assist.FailReason.FailType("DECODING_ERROR", 1);
            NETWORK_DENIED = new com.nostra13.universalimageloader.core.assist.FailReason.FailType("NETWORK_DENIED", 2);
            OUT_OF_MEMORY = new com.nostra13.universalimageloader.core.assist.FailReason.FailType("OUT_OF_MEMORY", 3);
            UNKNOWN = new com.nostra13.universalimageloader.core.assist.FailReason.FailType("UNKNOWN", 4);
            ENUM$VALUES = new com.nostra13.universalimageloader.core.assist.FailReason.FailType[]{IO_ERROR, DECODING_ERROR, NETWORK_DENIED, OUT_OF_MEMORY, UNKNOWN};
        }
    }

    public FailReason(FailType type, Throwable cause) {
        this.type = type;
        this.cause = cause;
    }

    public Throwable getCause() {
        return this.cause;
    }

    public FailType getType() {
        return this.type;
    }
}