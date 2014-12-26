package com.mopub.common.event;

import com.mopub.common.ClientMetadata;

public abstract class BaseEvent {
    private final String mEventName;
    private final long mEventTimeUtcMillis;
    private final ClientMetadata mMetadata;
    private final String mRequestUrl;

    public enum Type {
        NETWORK_REQUEST("request"),
        DATA_ERROR("invalid_data");
        public final String mName;

        static {
            String str = "request";
            NETWORK_REQUEST = new com.mopub.common.event.BaseEvent.Type("NETWORK_REQUEST", 0, "request");
            str = "invalid_data";
            DATA_ERROR = new com.mopub.common.event.BaseEvent.Type("DATA_ERROR", 1, "invalid_data");
            ENUM$VALUES = new com.mopub.common.event.BaseEvent.Type[]{NETWORK_REQUEST, DATA_ERROR};
        }

        private Type(String name) {
            this.mName = name;
        }
    }

    BaseEvent(Type eventType, String requestUrl, ClientMetadata metadata) {
        this.mEventTimeUtcMillis = System.currentTimeMillis();
        this.mEventName = eventType.mName;
        this.mRequestUrl = requestUrl;
        this.mMetadata = metadata;
    }

    public String getEventName() {
        return this.mEventName;
    }

    public long getEventTimeUtcMillis() {
        return this.mEventTimeUtcMillis;
    }

    public ClientMetadata getMetadata() {
        return this.mMetadata;
    }

    public String getRequestUrl() {
        return this.mRequestUrl;
    }
}