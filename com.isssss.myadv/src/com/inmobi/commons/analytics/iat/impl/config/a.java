package com.inmobi.commons.analytics.iat.impl.config;

import com.inmobi.commons.cache.CacheController.Validator;
import java.util.Map;

// compiled from: AdTrackerInitializer.java
static class a implements Validator {
    a() {
    }

    public boolean validate(Map map) {
        return AdTrackerInitializer.b(map);
    }
}