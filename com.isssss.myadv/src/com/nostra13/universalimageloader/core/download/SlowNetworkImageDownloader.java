package com.nostra13.universalimageloader.core.download;

import com.millennialmedia.android.MMAdView;
import com.nostra13.universalimageloader.core.assist.FlushedInputStream;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import java.io.IOException;
import java.io.InputStream;

public class SlowNetworkImageDownloader implements ImageDownloader {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$nostra13$universalimageloader$core$download$ImageDownloader$Scheme;
    private final ImageDownloader wrappedDownloader;

    static /* synthetic */ int[] $SWITCH_TABLE$com$nostra13$universalimageloader$core$download$ImageDownloader$Scheme() {
        int[] iArr = $SWITCH_TABLE$com$nostra13$universalimageloader$core$download$ImageDownloader$Scheme;
        if (iArr == null) {
            iArr = new int[Scheme.values().length];
            try {
                iArr[Scheme.ASSETS.ordinal()] = 5;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[Scheme.CONTENT.ordinal()] = 4;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[Scheme.DRAWABLE.ordinal()] = 6;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[Scheme.FILE.ordinal()] = 3;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[Scheme.HTTP.ordinal()] = 1;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[Scheme.HTTPS.ordinal()] = 2;
            } catch (NoSuchFieldError e6) {
            }
            try {
                iArr[Scheme.UNKNOWN.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            $SWITCH_TABLE$com$nostra13$universalimageloader$core$download$ImageDownloader$Scheme = iArr;
        }
        return iArr;
    }

    public SlowNetworkImageDownloader(ImageDownloader wrappedDownloader) {
        this.wrappedDownloader = wrappedDownloader;
    }

    public InputStream getStream(String imageUri, Object extra) throws IOException {
        InputStream imageStream = this.wrappedDownloader.getStream(imageUri, extra);
        switch ($SWITCH_TABLE$com$nostra13$universalimageloader$core$download$ImageDownloader$Scheme()[Scheme.ofUri(imageUri).ordinal()]) {
            case MMAdView.TRANSITION_FADE:
            case MMAdView.TRANSITION_UP:
                return new FlushedInputStream(imageStream);
            default:
                return imageStream;
        }
    }
}