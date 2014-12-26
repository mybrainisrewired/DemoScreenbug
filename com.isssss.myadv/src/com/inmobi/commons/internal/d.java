package com.inmobi.commons.internal;

import com.inmobi.commons.cache.CacheController.Validator;
import java.util.Map;

// compiled from: InternalSDKUtil.java
static class d implements Validator {
    d() {
    }

    public boolean validate(Map<String, Object> map) {
        return InternalSDKUtil.a(map);
    }
}