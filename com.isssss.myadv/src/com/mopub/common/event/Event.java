package com.mopub.common.event;

import com.mopub.common.ClientMetadata;
import com.mopub.common.event.BaseEvent.Type;

public class Event extends BaseEvent {
    Event(Type eventType, String requestUrl, ClientMetadata metadata) {
        super(eventType, requestUrl, metadata);
    }
}