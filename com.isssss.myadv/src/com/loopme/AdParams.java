package com.loopme;

class AdParams {
    private static final String LOG_TAG;
    private final int mExpiredDate;
    private final String mFormat;
    private final String mHtml;
    private final String mOrientation;
    private final int mRefreshTime;

    static class AdParamsBuilder {
        private static final int DEFAULT_EXPIRED_TIME = 600000;
        private static final int MAX_REFRESH_TIME = 300000;
        private static final int MIN_REFRESH_TIME = 30000;
        private int mExpiredDate;
        private final String mFormat;
        private String mHtml;
        private String mOrientation;
        private int mRefreshTime;

        public AdParamsBuilder(String format) {
            this.mFormat = format;
        }

        public AdParams build() {
            return new AdParams(null);
        }

        public AdParamsBuilder expiredTime(int time) {
            if (time == 0) {
                this.mExpiredDate = 600000;
            } else {
                time *= 1000;
                if (time < 600000) {
                    this.mExpiredDate = 600000;
                } else {
                    this.mExpiredDate = time;
                }
            }
            return this;
        }

        public AdParamsBuilder html(String html) {
            this.mHtml = html;
            return this;
        }

        public AdParamsBuilder orientation(String orientation) {
            this.mOrientation = orientation;
            return this;
        }

        public AdParamsBuilder refreshTime(int time) {
            if (time == 0) {
                this.mRefreshTime = 30000;
            } else {
                time *= 1000;
                if (time > 300000) {
                    this.mRefreshTime = 300000;
                } else if (time < 30000) {
                    this.mRefreshTime = 30000;
                } else {
                    this.mRefreshTime = time;
                }
            }
            return this;
        }
    }

    static {
        LOG_TAG = AdParams.class.getSimpleName();
    }

    private AdParams(AdParamsBuilder builder) {
        this.mHtml = builder.mHtml;
        this.mFormat = builder.mFormat;
        this.mOrientation = builder.mOrientation;
        this.mRefreshTime = builder.mRefreshTime;
        this.mExpiredDate = builder.mExpiredDate;
        Utilities.log(LOG_TAG, new StringBuilder("Server response indicates  ad params: format: ").append(this.mFormat).append(", orientation: ").append(this.mOrientation).append(", refresh time: ").append(this.mRefreshTime).append(", expire in: ").append(this.mExpiredDate).toString(), LogLevel.DEBUG);
    }

    public String getAdFormat() {
        return this.mFormat;
    }

    public String getAdOrientation() {
        return this.mOrientation;
    }

    public int getAdRefreshTime() {
        return this.mRefreshTime;
    }

    public int getExpiredTime() {
        return this.mExpiredDate;
    }

    public String getHtml() {
        return this.mHtml;
    }
}