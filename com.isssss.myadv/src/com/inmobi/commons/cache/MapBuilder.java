package com.inmobi.commons.cache;

import java.util.Map;

public interface MapBuilder {
    Map<String, Object> buildMap(String str) throws Exception;
}