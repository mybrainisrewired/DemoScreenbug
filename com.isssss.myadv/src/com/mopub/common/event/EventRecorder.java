package com.mopub.common.event;

public interface EventRecorder {
    void recordEvent(Event event);

    void recordTimedEvent(TimedEvent timedEvent);
}