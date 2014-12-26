package com.android.volley.toolbox;

import com.android.volley.Cache.Entry;
import com.android.volley.NetworkResponse;
import java.util.Map;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;

public class HttpHeaderParser {
    public static Entry parseCacheHeaders(NetworkResponse response) {
        long now = System.currentTimeMillis();
        Map<String, String> headers = response.headers;
        long serverDate = 0;
        long serverExpires = 0;
        long softExpire = 0;
        long maxAge = 0;
        boolean hasCacheControl = false;
        String headerValue = (String) headers.get("Date");
        if (headerValue != null) {
            serverDate = parseDateAsEpoch(headerValue);
        }
        headerValue = headers.get("Cache-Control");
        if (headerValue != null) {
            hasCacheControl = true;
            String[] tokens = headerValue.split(",");
            int i = 0;
            while (i < tokens.length) {
                String token = tokens[i].trim();
                if (!token.equals("no-cache") && !token.equals("no-store")) {
                    if (token.startsWith("max-age=")) {
                        try {
                            maxAge = Long.parseLong(token.substring(8));
                        } catch (Exception e) {
                        }
                    } else if (token.equals("must-revalidate") || token.equals("proxy-revalidate")) {
                        maxAge = 0;
                    }
                    i++;
                }
                return null;
            }
        }
        headerValue = headers.get("Expires");
        if (headerValue != null) {
            serverExpires = parseDateAsEpoch(headerValue);
        }
        String serverEtag = headers.get("ETag");
        if (hasCacheControl) {
            softExpire = now + 1000 * maxAge;
        } else if (serverDate > 0 && serverExpires >= serverDate) {
            softExpire = now + serverExpires - serverDate;
        }
        Entry entry = new Entry();
        entry.data = response.data;
        entry.etag = serverEtag;
        entry.softTtl = softExpire;
        entry.ttl = entry.softTtl;
        entry.serverDate = serverDate;
        entry.responseHeaders = headers;
        return entry;
    }

    public static String parseCharset(Map<String, String> headers) {
        String contentType = (String) headers.get(MraidCommandStorePicture.MIME_TYPE_HEADER);
        if (contentType != null) {
            String[] params = contentType.split(";");
            int i = 1;
            while (i < params.length) {
                String[] pair = params[i].trim().split("=");
                if (pair.length == 2 && pair[0].equals("charset")) {
                    return pair[1];
                }
                i++;
            }
        }
        return "ISO-8859-1";
    }

    public static long parseDateAsEpoch(String dateStr) {
        try {
            return DateUtils.parseDate(dateStr).getTime();
        } catch (DateParseException e) {
            return 0;
        }
    }
}