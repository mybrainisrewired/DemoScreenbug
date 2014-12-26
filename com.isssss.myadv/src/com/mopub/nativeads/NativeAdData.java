package com.mopub.nativeads;

class NativeAdData {
    private final MoPubAdRenderer adRenderer;
    private final NativeResponse adResponse;
    private final String adUnitId;

    NativeAdData(String adUnitId, MoPubAdRenderer adRenderer, NativeResponse adResponse) {
        this.adUnitId = adUnitId;
        this.adRenderer = adRenderer;
        this.adResponse = adResponse;
    }

    NativeResponse getAd() {
        return this.adResponse;
    }

    MoPubAdRenderer getAdRenderer() {
        return this.adRenderer;
    }

    String getAdUnitId() {
        return this.adUnitId;
    }
}