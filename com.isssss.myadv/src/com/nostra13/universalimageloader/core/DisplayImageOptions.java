package com.nostra13.universalimageloader.core;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.os.Handler;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;

public final class DisplayImageOptions {
    private final Bitmap bitmapForEmptyUri;
    private final Bitmap bitmapOnFail;
    private final Bitmap bitmapOnLoading;
    private final boolean cacheInMemory;
    private final boolean cacheOnDisc;
    private final Options decodingOptions;
    private final int delayBeforeLoading;
    private final BitmapDisplayer displayer;
    private final Object extraForDownloader;
    private final Handler handler;
    private final int imageResForEmptyUri;
    private final int imageResOnFail;
    private final int imageResOnLoading;
    private final ImageScaleType imageScaleType;
    private final BitmapProcessor postProcessor;
    private final BitmapProcessor preProcessor;
    private final boolean resetViewBeforeLoading;

    public static class Builder {
        private Bitmap bitmapForEmptyUri;
        private Bitmap bitmapOnFail;
        private Bitmap bitmapOnLoading;
        private boolean cacheInMemory;
        private boolean cacheOnDisc;
        private Options decodingOptions;
        private int delayBeforeLoading;
        private BitmapDisplayer displayer;
        private Object extraForDownloader;
        private Handler handler;
        private int imageResForEmptyUri;
        private int imageResOnFail;
        private int imageResOnLoading;
        private ImageScaleType imageScaleType;
        private BitmapProcessor postProcessor;
        private BitmapProcessor preProcessor;
        private boolean resetViewBeforeLoading;

        public Builder() {
            this.imageResOnLoading = 0;
            this.imageResForEmptyUri = 0;
            this.imageResOnFail = 0;
            this.bitmapOnLoading = null;
            this.bitmapForEmptyUri = null;
            this.bitmapOnFail = null;
            this.resetViewBeforeLoading = false;
            this.cacheInMemory = false;
            this.cacheOnDisc = false;
            this.imageScaleType = ImageScaleType.IN_SAMPLE_POWER_OF_2;
            this.decodingOptions = new Options();
            this.delayBeforeLoading = 0;
            this.extraForDownloader = null;
            this.preProcessor = null;
            this.postProcessor = null;
            this.displayer = DefaultConfigurationFactory.createBitmapDisplayer();
            this.handler = null;
            this.decodingOptions.inPurgeable = true;
            this.decodingOptions.inInputShareable = true;
        }

        public com.nostra13.universalimageloader.core.DisplayImageOptions.Builder bitmapConfig(Config bitmapConfig) {
            if (bitmapConfig == null) {
                throw new IllegalArgumentException("bitmapConfig can't be null");
            }
            this.decodingOptions.inPreferredConfig = bitmapConfig;
            return this;
        }

        public DisplayImageOptions build() {
            return new DisplayImageOptions(null);
        }

        public com.nostra13.universalimageloader.core.DisplayImageOptions.Builder cacheInMemory() {
            this.cacheInMemory = true;
            return this;
        }

        public com.nostra13.universalimageloader.core.DisplayImageOptions.Builder cacheInMemory(boolean cacheInMemory) {
            this.cacheInMemory = cacheInMemory;
            return this;
        }

        public com.nostra13.universalimageloader.core.DisplayImageOptions.Builder cacheOnDisc() {
            this.cacheOnDisc = true;
            return this;
        }

        public com.nostra13.universalimageloader.core.DisplayImageOptions.Builder cacheOnDisc(boolean cacheOnDisc) {
            this.cacheOnDisc = cacheOnDisc;
            return this;
        }

        public com.nostra13.universalimageloader.core.DisplayImageOptions.Builder cloneFrom(DisplayImageOptions options) {
            this.imageResOnLoading = options.imageResOnLoading;
            this.imageResForEmptyUri = options.imageResForEmptyUri;
            this.imageResOnFail = options.imageResOnFail;
            this.bitmapOnLoading = options.bitmapOnLoading;
            this.bitmapForEmptyUri = options.bitmapForEmptyUri;
            this.bitmapOnFail = options.bitmapOnFail;
            this.resetViewBeforeLoading = options.resetViewBeforeLoading;
            this.cacheInMemory = options.cacheInMemory;
            this.cacheOnDisc = options.cacheOnDisc;
            this.imageScaleType = options.imageScaleType;
            this.decodingOptions = options.decodingOptions;
            this.delayBeforeLoading = options.delayBeforeLoading;
            this.extraForDownloader = options.extraForDownloader;
            this.preProcessor = options.preProcessor;
            this.postProcessor = options.postProcessor;
            this.displayer = options.displayer;
            this.handler = options.handler;
            return this;
        }

        public com.nostra13.universalimageloader.core.DisplayImageOptions.Builder decodingOptions(Options decodingOptions) {
            if (decodingOptions == null) {
                throw new IllegalArgumentException("decodingOptions can't be null");
            }
            this.decodingOptions = decodingOptions;
            return this;
        }

        public com.nostra13.universalimageloader.core.DisplayImageOptions.Builder delayBeforeLoading(int delayInMillis) {
            this.delayBeforeLoading = delayInMillis;
            return this;
        }

        public com.nostra13.universalimageloader.core.DisplayImageOptions.Builder displayer(BitmapDisplayer displayer) {
            if (displayer == null) {
                throw new IllegalArgumentException("displayer can't be null");
            }
            this.displayer = displayer;
            return this;
        }

        public com.nostra13.universalimageloader.core.DisplayImageOptions.Builder extraForDownloader(Object extra) {
            this.extraForDownloader = extra;
            return this;
        }

        public com.nostra13.universalimageloader.core.DisplayImageOptions.Builder handler(Handler handler) {
            this.handler = handler;
            return this;
        }

        public com.nostra13.universalimageloader.core.DisplayImageOptions.Builder imageScaleType(ImageScaleType imageScaleType) {
            this.imageScaleType = imageScaleType;
            return this;
        }

        public com.nostra13.universalimageloader.core.DisplayImageOptions.Builder postProcessor(BitmapProcessor postProcessor) {
            this.postProcessor = postProcessor;
            return this;
        }

        public com.nostra13.universalimageloader.core.DisplayImageOptions.Builder preProcessor(BitmapProcessor preProcessor) {
            this.preProcessor = preProcessor;
            return this;
        }

        public com.nostra13.universalimageloader.core.DisplayImageOptions.Builder resetViewBeforeLoading() {
            this.resetViewBeforeLoading = true;
            return this;
        }

        public com.nostra13.universalimageloader.core.DisplayImageOptions.Builder resetViewBeforeLoading(boolean resetViewBeforeLoading) {
            this.resetViewBeforeLoading = resetViewBeforeLoading;
            return this;
        }

        public com.nostra13.universalimageloader.core.DisplayImageOptions.Builder showImageForEmptyUri(int imageRes) {
            this.imageResForEmptyUri = imageRes;
            return this;
        }

        public com.nostra13.universalimageloader.core.DisplayImageOptions.Builder showImageForEmptyUri(Bitmap bitmap) {
            this.bitmapForEmptyUri = bitmap;
            return this;
        }

        public com.nostra13.universalimageloader.core.DisplayImageOptions.Builder showImageOnFail(int imageRes) {
            this.imageResOnFail = imageRes;
            return this;
        }

        public com.nostra13.universalimageloader.core.DisplayImageOptions.Builder showImageOnFail(Bitmap bitmap) {
            this.bitmapOnFail = bitmap;
            return this;
        }

        public com.nostra13.universalimageloader.core.DisplayImageOptions.Builder showImageOnLoading(int imageRes) {
            this.imageResOnLoading = imageRes;
            return this;
        }

        public com.nostra13.universalimageloader.core.DisplayImageOptions.Builder showImageOnLoading(Bitmap bitmap) {
            this.bitmapOnLoading = bitmap;
            return this;
        }

        @Deprecated
        public com.nostra13.universalimageloader.core.DisplayImageOptions.Builder showStubImage(int imageRes) {
            this.imageResOnLoading = imageRes;
            return this;
        }
    }

    private DisplayImageOptions(Builder builder) {
        this.imageResOnLoading = builder.imageResOnLoading;
        this.imageResForEmptyUri = builder.imageResForEmptyUri;
        this.imageResOnFail = builder.imageResOnFail;
        this.bitmapOnLoading = builder.bitmapOnLoading;
        this.bitmapForEmptyUri = builder.bitmapForEmptyUri;
        this.bitmapOnFail = builder.bitmapOnFail;
        this.resetViewBeforeLoading = builder.resetViewBeforeLoading;
        this.cacheInMemory = builder.cacheInMemory;
        this.cacheOnDisc = builder.cacheOnDisc;
        this.imageScaleType = builder.imageScaleType;
        this.decodingOptions = builder.decodingOptions;
        this.delayBeforeLoading = builder.delayBeforeLoading;
        this.extraForDownloader = builder.extraForDownloader;
        this.preProcessor = builder.preProcessor;
        this.postProcessor = builder.postProcessor;
        this.displayer = builder.displayer;
        this.handler = builder.handler;
    }

    public static DisplayImageOptions createSimple() {
        return new Builder().build();
    }

    public Bitmap getBitmapForEmptyUri() {
        return this.bitmapForEmptyUri;
    }

    public Bitmap getBitmapOnFail() {
        return this.bitmapOnFail;
    }

    public Bitmap getBitmapOnLoading() {
        return this.bitmapOnLoading;
    }

    public Options getDecodingOptions() {
        return this.decodingOptions;
    }

    public int getDelayBeforeLoading() {
        return this.delayBeforeLoading;
    }

    public BitmapDisplayer getDisplayer() {
        return this.displayer;
    }

    public Object getExtraForDownloader() {
        return this.extraForDownloader;
    }

    public Handler getHandler() {
        return this.handler == null ? new Handler() : this.handler;
    }

    public int getImageResForEmptyUri() {
        return this.imageResForEmptyUri;
    }

    public int getImageResOnFail() {
        return this.imageResOnFail;
    }

    public int getImageResOnLoading() {
        return this.imageResOnLoading;
    }

    public ImageScaleType getImageScaleType() {
        return this.imageScaleType;
    }

    public BitmapProcessor getPostProcessor() {
        return this.postProcessor;
    }

    public BitmapProcessor getPreProcessor() {
        return this.preProcessor;
    }

    public boolean isCacheInMemory() {
        return this.cacheInMemory;
    }

    public boolean isCacheOnDisc() {
        return this.cacheOnDisc;
    }

    public boolean isResetViewBeforeLoading() {
        return this.resetViewBeforeLoading;
    }

    public boolean shouldDelayBeforeLoading() {
        return this.delayBeforeLoading > 0;
    }

    public boolean shouldPostProcess() {
        return this.postProcessor != null;
    }

    public boolean shouldPreProcess() {
        return this.preProcessor != null;
    }

    public boolean shouldShowBitmapForEmptyUri() {
        return this.bitmapForEmptyUri != null;
    }

    public boolean shouldShowBitmapOnFail() {
        return this.bitmapOnFail != null;
    }

    public boolean shouldShowBitmapOnLoading() {
        return this.bitmapOnLoading != null;
    }

    public boolean shouldShowImageResForEmptyUri() {
        return this.imageResForEmptyUri != 0;
    }

    public boolean shouldShowImageResOnFail() {
        return this.imageResOnFail != 0;
    }

    public boolean shouldShowImageResOnLoading() {
        return this.imageResOnLoading != 0;
    }
}