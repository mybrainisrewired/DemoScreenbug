package com.inmobi.commons.network;

import java.util.List;
import java.util.Map;

public class Response {
    String a;
    int b;
    Map<String, List<String>> c;
    ErrorCode d;

    public Response(ErrorCode errorCode) {
        this.a = null;
        this.b = 0;
        this.c = null;
        this.d = errorCode;
    }

    public Response(String str, int i, Map<String, List<String>> map) {
        this.a = null;
        this.b = 0;
        this.c = null;
        this.a = str;
        this.b = i;
        this.c = map;
    }

    public ErrorCode getError() {
        return this.d;
    }

    public Map<String, List<String>> getHeaders() {
        return this.c;
    }

    public String getResponseBody() {
        return this.a;
    }

    public int getStatusCode() {
        return this.b;
    }
}