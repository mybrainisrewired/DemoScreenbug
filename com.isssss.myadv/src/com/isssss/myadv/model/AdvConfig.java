package com.isssss.myadv.model;

import com.mopub.common.Preconditions;

public class AdvConfig {
    private int advCompany;
    private String advid;
    private String ip;
    private long shipmentTime;
    private int show;
    private int timely;
    private String whiteList;

    public AdvConfig() {
        this.timely = 0;
        this.show = 0;
        this.whiteList = Preconditions.EMPTY_ARGUMENTS;
        this.advCompany = 0;
        this.advid = Preconditions.EMPTY_ARGUMENTS;
        this.ip = Preconditions.EMPTY_ARGUMENTS;
        this.shipmentTime = 0;
    }

    public int getAdvCompany() {
        return this.advCompany;
    }

    public String getAdvid() {
        return this.advid;
    }

    public String getIp() {
        return this.ip;
    }

    public long getShipmentTime() {
        return this.shipmentTime;
    }

    public int getShow() {
        return this.show;
    }

    public int getTimely() {
        return this.timely;
    }

    public String[] getWhiteListArray(String whiteList) {
        return whiteList.split(",");
    }

    public String getWhitelist() {
        return this.whiteList;
    }

    public void setAdvCompany(int advCompany) {
        this.advCompany = advCompany;
    }

    public void setAdvid(String advid) {
        this.advid = advid;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setShipmentTime(long shipmentTime) {
        this.shipmentTime = shipmentTime;
    }

    public void setShow(int show) {
        this.show = show;
    }

    public void setTimely(int timely) {
        this.timely = timely;
    }

    public void setWhitelist(String whiteList) {
        this.whiteList = whiteList;
    }
}