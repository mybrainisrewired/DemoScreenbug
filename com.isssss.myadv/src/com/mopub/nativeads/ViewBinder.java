package com.mopub.nativeads;

import java.util.HashMap;
import java.util.Map;

public final class ViewBinder {
    final int callToActionId;
    final Map<String, Integer> extras;
    final int iconImageId;
    final int layoutId;
    final int mainImageId;
    final int textId;
    final int titleId;

    public static final class Builder {
        private int callToActionId;
        private Map<String, Integer> extras;
        private int iconImageId;
        private final int layoutId;
        private int mainImageId;
        private int textId;
        private int titleId;

        public Builder(int layoutId) {
            this.layoutId = layoutId;
            this.extras = new HashMap();
        }

        public final com.mopub.nativeads.ViewBinder.Builder addExtra(String key, int resourceId) {
            this.extras.put(key, Integer.valueOf(resourceId));
            return this;
        }

        public final com.mopub.nativeads.ViewBinder.Builder addExtras(Map<String, Integer> resourceIds) {
            this.extras = new HashMap(resourceIds);
            return this;
        }

        public final ViewBinder build() {
            return new ViewBinder(null);
        }

        public final com.mopub.nativeads.ViewBinder.Builder callToActionId(int callToActionId) {
            this.callToActionId = callToActionId;
            return this;
        }

        public final com.mopub.nativeads.ViewBinder.Builder iconImageId(int iconImageId) {
            this.iconImageId = iconImageId;
            return this;
        }

        public final com.mopub.nativeads.ViewBinder.Builder mainImageId(int mainImageId) {
            this.mainImageId = mainImageId;
            return this;
        }

        public final com.mopub.nativeads.ViewBinder.Builder textId(int textId) {
            this.textId = textId;
            return this;
        }

        public final com.mopub.nativeads.ViewBinder.Builder titleId(int titleId) {
            this.titleId = titleId;
            return this;
        }
    }

    private ViewBinder(Builder builder) {
        this.layoutId = builder.layoutId;
        this.titleId = builder.titleId;
        this.textId = builder.textId;
        this.callToActionId = builder.callToActionId;
        this.mainImageId = builder.mainImageId;
        this.iconImageId = builder.iconImageId;
        this.extras = builder.extras;
    }
}