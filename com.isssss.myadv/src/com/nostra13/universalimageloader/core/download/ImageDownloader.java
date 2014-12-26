package com.nostra13.universalimageloader.core.download;

import com.inmobi.commons.ads.cache.AdDatabaseHelper;
import com.mopub.common.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public interface ImageDownloader {

    public enum Scheme {
        HTTP("http"),
        HTTPS("https"),
        FILE("file"),
        CONTENT(AdDatabaseHelper.COLUMN_AD_CONTENT),
        ASSETS("assets"),
        DRAWABLE("drawable"),
        UNKNOWN(Preconditions.EMPTY_ARGUMENTS);
        private String scheme;
        private String uriPrefix;

        static {
            String str = "http";
            HTTP = new com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme("HTTP", 0, "http");
            str = "https";
            HTTPS = new com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme("HTTPS", 1, "https");
            str = "file";
            FILE = new com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme("FILE", 2, "file");
            String str2 = "CONTENT";
            str = AdDatabaseHelper.COLUMN_AD_CONTENT;
            CONTENT = new com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme(str2, 3, AdDatabaseHelper.COLUMN_AD_CONTENT);
            str = "assets";
            ASSETS = new com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme("ASSETS", 4, "assets");
            String str3 = "drawable";
            DRAWABLE = new com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme("DRAWABLE", 5, "drawable");
            str2 = "UNKNOWN";
            str3 = Preconditions.EMPTY_ARGUMENTS;
            UNKNOWN = new com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme(str2, 6, Preconditions.EMPTY_ARGUMENTS);
            ENUM$VALUES = new com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme[]{HTTP, HTTPS, FILE, CONTENT, ASSETS, DRAWABLE, UNKNOWN};
        }

        private Scheme(String scheme) {
            this.scheme = scheme;
            this.uriPrefix = new StringBuilder(String.valueOf(scheme)).append("://").toString();
        }

        private boolean belongsTo(String uri) {
            return uri.toLowerCase(Locale.US).startsWith(this.uriPrefix);
        }

        public static com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme ofUri(String uri) {
            if (uri != null) {
                com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme[] values = values();
                int length = values.length;
                int i = 0;
                while (i < length) {
                    com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme s = values[i];
                    if (s.belongsTo(uri)) {
                        return s;
                    }
                    i++;
                }
            }
            return UNKNOWN;
        }

        public String crop(String uri) {
            if (belongsTo(uri)) {
                return uri.substring(this.uriPrefix.length());
            }
            throw new IllegalArgumentException(String.format("URI [%1$s] doesn't have expected scheme [%2$s]", new Object[]{uri, this.scheme}));
        }

        public String wrap(String path) {
            return new StringBuilder(String.valueOf(this.uriPrefix)).append(path).toString();
        }
    }

    InputStream getStream(String str, Object obj) throws IOException;
}