package com.mopub.common.event;

import com.mopub.common.ClientMetadata;
import com.mopub.common.event.BaseEvent.Type;
import java.util.concurrent.TimeUnit;

public class TimedEvent extends BaseEvent {
    public static final int SC_NOT_RELEVANT = 0;
    public static final int SC_NO_RESPONSE = -1;
    private long mDurationMillis;
    private int mHttpStatusCode;
    private final Listener mListener;
    private final long mStartTimeNanos;
    private State mState;

    static interface Listener {
        void onCancelled(TimedEvent timedEvent);

        void onStopped(TimedEvent timedEvent);
    }

    private enum State {
        WAITING,
        STOPPED,
        CANCELLED;

        static {
            WAITING = new State("WAITING", 0);
            STOPPED = new State("STOPPED", 1);
            CANCELLED = new State("CANCELLED", 2);
            ENUM$VALUES = new State[]{WAITING, STOPPED, CANCELLED};
        }
    }

    TimedEvent(Type eventType, String requestUrl, ClientMetadata clientMetadata, Listener listener) {
        super(eventType, requestUrl, clientMetadata);
        this.mState = State.WAITING;
        this.mStartTimeNanos = System.nanoTime();
        this.mListener = listener;
    }

    public synchronized void cancel() {
        if (this.mState == State.WAITING) {
            this.mState = State.CANCELLED;
            if (this.mListener != null) {
                this.mListener.onCancelled(this);
            }
        }
    }

    public final synchronized long getDurationMillis() {
        return this.mDurationMillis;
    }

    public final synchronized int getHttpStatusCode() {
        return this.mHttpStatusCode;
    }

    public synchronized void stop(int httpStatusCode) {
        if (this.mState == State.WAITING) {
            this.mState = State.STOPPED;
            this.mHttpStatusCode = httpStatusCode;
            this.mDurationMillis = TimeUnit.MILLISECONDS.convert(System.nanoTime() - this.mStartTimeNanos, TimeUnit.NANOSECONDS);
            if (this.mListener != null) {
                this.mListener.onStopped(this);
            }
        }
    }
}