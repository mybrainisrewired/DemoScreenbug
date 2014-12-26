package org.codehaus.jackson.util;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

public final class InternCache extends LinkedHashMap<String, String> {
    private static final int MAX_ENTRIES = 192;
    public static final InternCache instance;

    static {
        instance = new InternCache();
    }

    private InternCache() {
        super(192, 0.8f, true);
    }

    public synchronized String intern(String input) {
        String result;
        result = (String) get(input);
        if (result == null) {
            result = input.intern();
            put(result, result);
        }
        return result;
    }

    protected boolean removeEldestEntry(Entry<String, String> eldest) {
        return size() > 192;
    }
}