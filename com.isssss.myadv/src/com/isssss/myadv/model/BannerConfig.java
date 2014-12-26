package com.isssss.myadv.model;

import com.mopub.common.Preconditions;

public class BannerConfig {
    private String advId;
    private int advsign;
    private int position;
    private long shipmentTime;
    private int shows;
    private int timeDaily;
    private String whitelist;

    public BannerConfig() {
        this.shows = 0;
        this.shipmentTime = 0;
        this.timeDaily = 0;
        this.whitelist = Preconditions.EMPTY_ARGUMENTS;
        this.advId = Preconditions.EMPTY_ARGUMENTS;
        this.advsign = 0;
        this.position = 0;
    }

    public String getAdvId() {
        return this.advId;
    }

    public int getAdvsign() {
        return this.advsign;
    }

    public int getPosition() {
        return this.position;
    }

    public long getShipmentTime() {
        return this.shipmentTime;
    }

    public int getShows() {
        return this.shows;
    }

    public int getTimeDaily() {
        return this.timeDaily;
    }

    public String getWhitelist() {
        return this.whitelist;
    }

    public void setAdvId(String advId) {
        this.advId = advId;
    }

    public void setAdvsign(int advsign) {
        this.advsign = advsign;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setShipmentTime(long shipmentTime) {
        this.shipmentTime = shipmentTime;
    }

    public void setShows(int shows) {
        this.shows = shows;
    }

    public void setTimeDaily(int timeDaily) {
        this.timeDaily = timeDaily;
    }

    public void setWhitelist(String whitelist) {
        this.whitelist = whitelist;
    }
}