package com.mopub.nativeads;

import android.location.Location;
import android.text.TextUtils;
import com.isssss.myadv.dao.BannerInfoTable;
import com.mopub.common.Preconditions;
import java.util.EnumSet;

public final class RequestParameters {
    private final EnumSet<NativeAdAsset> mDesiredAssets;
    private final String mKeywords;
    private final Location mLocation;

    public static final class Builder {
        private EnumSet<com.mopub.nativeads.RequestParameters.NativeAdAsset> desiredAssets;
        private String keywords;
        private Location location;

        public final RequestParameters build() {
            return new RequestParameters(null);
        }

        public final com.mopub.nativeads.RequestParameters.Builder desiredAssets(EnumSet<com.mopub.nativeads.RequestParameters.NativeAdAsset> desiredAssets) {
            this.desiredAssets = EnumSet.copyOf(desiredAssets);
            return this;
        }

        public final com.mopub.nativeads.RequestParameters.Builder keywords(String keywords) {
            this.keywords = keywords;
            return this;
        }

        public final com.mopub.nativeads.RequestParameters.Builder location(Location location) {
            this.location = location;
            return this;
        }
    }

    public enum NativeAdAsset {
        TITLE(BannerInfoTable.COLUMN_TITLE),
        TEXT("text"),
        ICON_IMAGE("iconimage"),
        MAIN_IMAGE("mainimage"),
        CALL_TO_ACTION_TEXT("ctatext"),
        STAR_RATING("starrating");
        private final String mAssetName;

        static {
            String str = "TITLE";
            String str2 = BannerInfoTable.COLUMN_TITLE;
            TITLE = new com.mopub.nativeads.RequestParameters.NativeAdAsset(str, 0, BannerInfoTable.COLUMN_TITLE);
            str2 = "text";
            TEXT = new com.mopub.nativeads.RequestParameters.NativeAdAsset("TEXT", 1, "text");
            str2 = "iconimage";
            ICON_IMAGE = new com.mopub.nativeads.RequestParameters.NativeAdAsset("ICON_IMAGE", 2, "iconimage");
            str2 = "mainimage";
            MAIN_IMAGE = new com.mopub.nativeads.RequestParameters.NativeAdAsset("MAIN_IMAGE", 3, "mainimage");
            str2 = "ctatext";
            CALL_TO_ACTION_TEXT = new com.mopub.nativeads.RequestParameters.NativeAdAsset("CALL_TO_ACTION_TEXT", 4, "ctatext");
            String str3 = "starrating";
            STAR_RATING = new com.mopub.nativeads.RequestParameters.NativeAdAsset("STAR_RATING", 5, "starrating");
            ENUM$VALUES = new com.mopub.nativeads.RequestParameters.NativeAdAsset[]{TITLE, TEXT, ICON_IMAGE, MAIN_IMAGE, CALL_TO_ACTION_TEXT, STAR_RATING};
        }

        private NativeAdAsset(String assetName) {
            this.mAssetName = assetName;
        }

        public String toString() {
            return this.mAssetName;
        }
    }

    private RequestParameters(Builder builder) {
        this.mKeywords = builder.keywords;
        this.mLocation = builder.location;
        this.mDesiredAssets = builder.desiredAssets;
    }

    public final String getDesiredAssets() {
        return this.mDesiredAssets != null ? TextUtils.join(",", this.mDesiredAssets.toArray()) : Preconditions.EMPTY_ARGUMENTS;
    }

    public final String getKeywords() {
        return this.mKeywords;
    }

    public final Location getLocation() {
        return this.mLocation;
    }
}