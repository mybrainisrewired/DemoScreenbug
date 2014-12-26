package com.inmobi.monetization.internal;

import java.util.HashMap;

public class LtvpRulesObject {
    private String a;
    private long b;
    private HashMap<String, Integer> c;
    private long d;
    private long e;

    public long getHardExpiry() {
        return this.e;
    }

    public String getRuleId() {
        return this.a;
    }

    public HashMap<String, Integer> getRules() {
        return this.c;
    }

    public long getSoftExpiry() {
        return this.d;
    }

    public long getTimeStamp() {
        return this.b;
    }

    public void setHardExpiry(long j) {
        this.e = j;
    }

    public void setRuleId(String str) {
        this.a = str;
    }

    public void setRules(HashMap<String, Integer> hashMap) {
        this.c = hashMap;
    }

    public void setSoftExpiry(long j) {
        this.d = j;
    }

    public void setTimeStamp(long j) {
        this.b = j;
    }
}