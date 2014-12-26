package com.mopub.mobileads.util.vast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VastVideoConfiguration implements Serializable {
    private static final long serialVersionUID = 0;
    private String mClickThroughUrl;
    private ArrayList<String> mClickTrackers;
    private ArrayList<String> mCompleteTrackers;
    private String mDiskMediaFileUrl;
    private ArrayList<String> mFirstQuartileTrackers;
    private ArrayList<String> mImpressionTrackers;
    private ArrayList<String> mMidpointTrackers;
    private String mNetworkMediaFileUrl;
    private ArrayList<String> mStartTrackers;
    private ArrayList<String> mThirdQuartileTrackers;
    private VastCompanionAd mVastCompanionAd;

    public VastVideoConfiguration() {
        this.mImpressionTrackers = new ArrayList();
        this.mStartTrackers = new ArrayList();
        this.mFirstQuartileTrackers = new ArrayList();
        this.mMidpointTrackers = new ArrayList();
        this.mThirdQuartileTrackers = new ArrayList();
        this.mCompleteTrackers = new ArrayList();
        this.mClickTrackers = new ArrayList();
    }

    public void addClickTrackers(List<String> clickTrackers) {
        this.mClickTrackers.addAll(clickTrackers);
    }

    public void addCompleteTrackers(List<String> completeTrackers) {
        this.mCompleteTrackers.addAll(completeTrackers);
    }

    public void addFirstQuartileTrackers(List<String> firstQuartileTrackers) {
        this.mFirstQuartileTrackers.addAll(firstQuartileTrackers);
    }

    public void addImpressionTrackers(List<String> impressionTrackers) {
        this.mImpressionTrackers.addAll(impressionTrackers);
    }

    public void addMidpointTrackers(List<String> midpointTrackers) {
        this.mMidpointTrackers.addAll(midpointTrackers);
    }

    public void addStartTrackers(List<String> startTrackers) {
        this.mStartTrackers.addAll(startTrackers);
    }

    public void addThirdQuartileTrackers(List<String> thirdQuartileTrackers) {
        this.mThirdQuartileTrackers.addAll(thirdQuartileTrackers);
    }

    public String getClickThroughUrl() {
        return this.mClickThroughUrl;
    }

    public List<String> getClickTrackers() {
        return this.mClickTrackers;
    }

    public List<String> getCompleteTrackers() {
        return this.mCompleteTrackers;
    }

    public String getDiskMediaFileUrl() {
        return this.mDiskMediaFileUrl;
    }

    public List<String> getFirstQuartileTrackers() {
        return this.mFirstQuartileTrackers;
    }

    public List<String> getImpressionTrackers() {
        return this.mImpressionTrackers;
    }

    public List<String> getMidpointTrackers() {
        return this.mMidpointTrackers;
    }

    public String getNetworkMediaFileUrl() {
        return this.mNetworkMediaFileUrl;
    }

    public List<String> getStartTrackers() {
        return this.mStartTrackers;
    }

    public List<String> getThirdQuartileTrackers() {
        return this.mThirdQuartileTrackers;
    }

    public VastCompanionAd getVastCompanionAd() {
        return this.mVastCompanionAd;
    }

    public void setClickThroughUrl(String clickThroughUrl) {
        this.mClickThroughUrl = clickThroughUrl;
    }

    public void setDiskMediaFileUrl(String diskMediaFileUrl) {
        this.mDiskMediaFileUrl = diskMediaFileUrl;
    }

    public void setNetworkMediaFileUrl(String networkMediaFileUrl) {
        this.mNetworkMediaFileUrl = networkMediaFileUrl;
    }

    public void setVastCompanionAd(VastCompanionAd vastCompanionAd) {
        this.mVastCompanionAd = vastCompanionAd;
    }
}