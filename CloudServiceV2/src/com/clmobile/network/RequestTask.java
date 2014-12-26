package com.clmobile.network;

import java.util.HashMap;

public class RequestTask {
    public static final String HTTP_GET = "GET";
    public static final String HTTP_POST = "POST";
    public HashMap<String, String> bodyPara;
    public HashMap<String, String> requestPara;
    private String service;
    private long serviceId;
    private String type;

    public RequestTask(String service, String type) {
        this.bodyPara = new HashMap();
        this.requestPara = new HashMap();
        this.service = service;
        this.serviceId = generateId();
        this.type = type;
    }

    public static RequestTask createRequestTask(String service, String type) {
        return new RequestTask(service, type);
    }

    private long generateId() {
        return System.currentTimeMillis();
    }

    public void addBodyPara(String key, String value) {
        this.bodyPara.put(key, value);
    }

    public void addRequestParameters(String key, String value) {
        this.requestPara.put(key, value);
    }

    public HashMap<String, String> getBodyPara() {
        return this.bodyPara;
    }

    public HashMap<String, String> getRequestParameters() {
        return this.requestPara;
    }

    public String getService() {
        return this.service;
    }

    public long getServiceId() {
        return this.serviceId;
    }

    public String getType() {
        return this.type;
    }
}