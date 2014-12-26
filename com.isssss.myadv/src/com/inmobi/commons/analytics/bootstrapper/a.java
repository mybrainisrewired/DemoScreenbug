package com.inmobi.commons.analytics.bootstrapper;

import com.inmobi.commons.cache.CacheController.Validator;
import java.util.Map;

// compiled from: AnalyticsInitializer.java
static class a implements Validator {
    a() {
    }

    public boolean validate(Map map) {
        return AnalyticsInitializer.b(map);
    }
}