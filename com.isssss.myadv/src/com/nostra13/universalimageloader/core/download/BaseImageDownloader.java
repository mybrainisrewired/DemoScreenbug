package com.nostra13.universalimageloader.core.download;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BaseImageDownloader implements ImageDownloader {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$nostra13$universalimageloader$core$download$ImageDownloader$Scheme = null;
    protected static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
    protected static final int BUFFER_SIZE = 32768;
    public static final int DEFAULT_HTTP_CONNECT_TIMEOUT = 5000;
    public static final int DEFAULT_HTTP_READ_TIMEOUT = 20000;
    private static final String ERROR_UNSUPPORTED_SCHEME = "UIL doesn't support scheme(protocol) by default [%s]. You should implement this support yourself (BaseImageDownloader.getStreamFromOtherSource(...))";
    protected static final int MAX_REDIRECT_COUNT = 5;
    protected final int connectTimeout;
    protected final Context context;
    protected final int readTimeout;

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

    public BaseImageDownloader(Context context) {
        this.context = context.getApplicationContext();
        this.connectTimeout = 5000;
        this.readTimeout = 20000;
    }

    public BaseImageDownloader(Context context, int connectTimeout, int readTimeout) {
        this.context = context.getApplicationContext();
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
    }

    protected HttpURLConnection createConnection(String url, Object extra) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(Uri.encode(url, ALLOWED_URI_CHARS)).openConnection();
        conn.setConnectTimeout(this.connectTimeout);
        conn.setReadTimeout(this.readTimeout);
        return conn;
    }

    public InputStream getStream(String imageUri, Object extra) throws IOException {
        switch ($SWITCH_TABLE$com$nostra13$universalimageloader$core$download$ImageDownloader$Scheme()[Scheme.ofUri(imageUri).ordinal()]) {
            case MMAdView.TRANSITION_FADE:
            case MMAdView.TRANSITION_UP:
                return getStreamFromNetwork(imageUri, extra);
            case MMAdView.TRANSITION_DOWN:
                return getStreamFromFile(imageUri, extra);
            case MMAdView.TRANSITION_RANDOM:
                return getStreamFromContent(imageUri, extra);
            case MAX_REDIRECT_COUNT:
                return getStreamFromAssets(imageUri, extra);
            case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                return getStreamFromDrawable(imageUri, extra);
            default:
                return getStreamFromOtherSource(imageUri, extra);
        }
    }

    protected InputStream getStreamFromAssets(String imageUri, Object extra) throws IOException {
        return this.context.getAssets().open(Scheme.ASSETS.crop(imageUri));
    }

    protected InputStream getStreamFromContent(String imageUri, Object extra) throws FileNotFoundException {
        return this.context.getContentResolver().openInputStream(Uri.parse(imageUri));
    }

    protected InputStream getStreamFromDrawable(String imageUri, Object extra) {
        Bitmap bitmap = ((BitmapDrawable) this.context.getResources().getDrawable(Integer.parseInt(Scheme.DRAWABLE.crop(imageUri)))).getBitmap();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 0, os);
        return new ByteArrayInputStream(os.toByteArray());
    }

    protected InputStream getStreamFromFile(String imageUri, Object extra) throws IOException {
        return new BufferedInputStream(new FileInputStream(Scheme.FILE.crop(imageUri)), 32768);
    }

    protected InputStream getStreamFromNetwork(String imageUri, Object extra) throws IOException {
        HttpURLConnection conn = createConnection(imageUri, extra);
        int redirectCount = 0;
        while (conn.getResponseCode() / 100 == 3 && redirectCount < 5) {
            conn = createConnection(conn.getHeaderField("Location"), extra);
            redirectCount++;
        }
        return new BufferedInputStream(conn.getInputStream(), 32768);
    }

    protected InputStream getStreamFromOtherSource(String imageUri, Object extra) throws IOException {
        throw new UnsupportedOperationException(String.format(ERROR_UNSUPPORTED_SCHEME, new Object[]{imageUri}));
    }
}