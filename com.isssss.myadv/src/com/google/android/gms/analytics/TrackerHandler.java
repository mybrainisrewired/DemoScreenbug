package com.google.android.gms.analytics;

import java.util.Map;

abstract class TrackerHandler {
    TrackerHandler() {
    }

    abstract void q(Map<String, String> map);
}