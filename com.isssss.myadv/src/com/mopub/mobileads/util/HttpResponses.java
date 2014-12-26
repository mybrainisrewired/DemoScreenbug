package com.mopub.mobileads.util;

import com.mopub.common.util.ResponseHeader;
import java.text.NumberFormat;
import java.util.Locale;
import org.apache.http.Header;
import org.apache.http.HttpResponse;

public class HttpResponses {
    public static boolean extractBooleanHeader(HttpResponse response, ResponseHeader responseHeader, boolean defaultValue) {
        String header = extractHeader(response, responseHeader);
        return header == null ? defaultValue : header.equals("1");
    }

    public static String extractHeader(HttpResponse response, ResponseHeader responseHeader) {
        Header header = response.getFirstHeader(responseHeader.getKey());
        return header != null ? header.getValue() : null;
    }

    public static int extractIntHeader(HttpResponse response, ResponseHeader responseHeader, int defaultValue) {
        Integer headerValue = extractIntegerHeader(response, responseHeader);
        return headerValue == null ? defaultValue : headerValue.intValue();
    }

    public static Integer extractIntegerHeader(HttpResponse response, ResponseHeader responseHeader) {
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
        numberFormat.setParseIntegerOnly(true);
        try {
            return Integer.valueOf(numberFormat.parse(extractHeader(response, responseHeader).trim()).intValue());
        } catch (Exception e) {
            return null;
        }
    }
}