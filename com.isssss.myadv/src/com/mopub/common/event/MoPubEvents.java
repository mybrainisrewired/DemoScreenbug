package com.mopub.common.event;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Message;
import com.mopub.common.ClientMetadata;
import com.mopub.common.VisibleForTesting;
import com.mopub.common.event.BaseEvent.Type;
import java.util.ArrayList;
import java.util.Iterator;

public class MoPubEvents {
    private static volatile EventDispatcher sEventDispatcher;

    @VisibleForTesting
    public static class EventDispatcher implements Listener {
        private final Iterable<EventRecorder> mEventRecorders;
        @VisibleForTesting
        Callback mHandlerCallback;
        private final HandlerThread mHandlerThread;
        private final Handler mMessageHandler;

        @VisibleForTesting
        EventDispatcher(Iterable<EventRecorder> recorders, HandlerThread handlerThread) {
            this.mEventRecorders = recorders;
            this.mHandlerCallback = new Callback() {
                public boolean handleMessage(Message msg) {
                    Iterator it;
                    if (msg.obj instanceof TimedEvent) {
                        TimedEvent event = (TimedEvent) msg.obj;
                        it = com.mopub.common.event.MoPubEvents.EventDispatcher.this.mEventRecorders.iterator();
                        while (it.hasNext()) {
                            ((EventRecorder) it.next()).recordTimedEvent(event);
                        }
                    } else if (msg.obj instanceof Event) {
                        Event event2 = (Event) msg.obj;
                        it = com.mopub.common.event.MoPubEvents.EventDispatcher.this.mEventRecorders.iterator();
                        while (it.hasNext()) {
                            it.next().recordEvent(event2);
                        }
                    }
                    return true;
                }
            };
            this.mHandlerThread = handlerThread;
            this.mHandlerThread.start();
            this.mMessageHandler = new Handler(this.mHandlerThread.getLooper(), this.mHandlerCallback);
        }

        private void sendEventToHandlerThread(BaseEvent event) {
            Message.obtain(this.mMessageHandler, 0, event).sendToTarget();
        }

        public void onCancelled(TimedEvent event) {
        }

        public void onStopped(TimedEvent event) {
            sendEventToHandlerThread(event);
        }
    }

    private static class NoopEventRecorder implements EventRecorder {
        private NoopEventRecorder() {
        }

        public void recordEvent(Event event) {
        }

        public void recordTimedEvent(TimedEvent event) {
        }
    }

    public static void event(Type eventType, String requestUrl) {
        getDispatcher().sendEventToHandlerThread(new Event(eventType, requestUrl, ClientMetadata.getInstance()));
    }

    private static EventDispatcher getDispatcher() {
        EventDispatcher result = sEventDispatcher;
        if (result == null) {
            synchronized (MoPubEvents.class) {
                result = sEventDispatcher;
                if (result == null) {
                    ArrayList<EventRecorder> recorders = new ArrayList();
                    recorders.add(new NoopEventRecorder());
                    EventDispatcher result2 = new EventDispatcher(recorders, new HandlerThread("mopub_event_queue"));
                    sEventDispatcher = result2;
                    result = result2;
                }
            }
        }
        return result;
    }

    @VisibleForTesting
    public static void setEventDispatcher(EventDispatcher dispatcher) {
        sEventDispatcher = dispatcher;
    }

    public static TimedEvent timedEvent(Type eventType, String requestUrl) {
        return new TimedEvent(eventType, requestUrl, ClientMetadata.getInstance(), getDispatcher());
    }
}