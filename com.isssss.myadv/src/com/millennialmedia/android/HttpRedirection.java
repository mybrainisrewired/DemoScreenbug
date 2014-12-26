package com.millennialmedia.android;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import com.mopub.mobileads.CustomEventBannerAdapter;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

final class HttpRedirection {
    private static final String HEADER_LOCATION = "Location";
    private static final String HTTPS = "https";
    private static final String LOG_URL_FORMAT = "Redirecting to: %s";
    private static final String METHOD_GET = "GET";
    private static final String TAG = "HttpRedirection";

    static class AnonymousClass_1 implements Runnable {
        final /* synthetic */ WeakReference val$listenerReference;

        AnonymousClass_1(WeakReference weakReference) {
            this.val$listenerReference = weakReference;
        }

        private void handleDestinationUri(RedirectionListenerImpl listener) {
            Intent intent = null;
            Context context = (Context) listener.weakContext.get();
            if (context != null) {
                String scheme = listener.destinationUri.getScheme();
                if (scheme != null) {
                    if (!scheme.equalsIgnoreCase("mmvideo")) {
                        intent = IntentUtils.getIntentForUri(listener);
                    } else if (!listener.isHandlingMMVideo(listener.destinationUri)) {
                        VideoAd.playAd(context, listener.destinationUri.getHost(), listener);
                    }
                }
                if (intent != null) {
                    OverlaySettings settings = listener.getOverlaySettings();
                    if (!(intent == null || settings == null)) {
                        if (listener.orientation != null) {
                            settings.orientation = listener.orientation;
                        }
                        intent.putExtra("settings", settings);
                    }
                    String clazz = intent.getStringExtra("class");
                    if (clazz == null || !clazz.equals(AdViewOverlayActivity.class.getCanonicalName())) {
                        try {
                            if (listener.isActivityStartable(listener.destinationUri)) {
                                IntentUtils.startActivity(context, intent);
                                listener.startingActivity(listener.destinationUri);
                            }
                        } catch (ActivityNotFoundException e) {
                            ActivityNotFoundException e2 = e;
                            MMLog.e(TAG, String.format("No activity found for %s", new Object[]{listener.destinationUri}), e2);
                        }
                    }
                }
            }
        }

        public void run() {
            RedirectionListenerImpl listener = (RedirectionListenerImpl) this.val$listenerReference.get();
            if (listener != null) {
                String destination = HttpRedirection.navigateRedirects(listener.url);
                if (destination != null) {
                    listener.destinationUri = Uri.parse(destination);
                    if (listener.destinationUri != null) {
                        handleDestinationUri(listener);
                    } else {
                        MMLog.e(TAG, String.format("Could not start activity for %s", new Object[]{destination}));
                    }
                }
            }
        }
    }

    private static interface Listener {
        boolean canOpenOverlay();

        OverlaySettings getOverlaySettings();

        boolean isActivityStartable(Uri uri);

        boolean isExpandingToUrl();

        boolean isHandlingMMVideo(Uri uri);

        void startingActivity(Uri uri);

        void startingVideo();

        void updateLastVideoViewedTime();
    }

    static class RedirectionListenerImpl implements Listener {
        long creatorAdImplInternalId;
        Uri destinationUri;
        String orientation;
        String url;
        WeakReference<Context> weakContext;

        public boolean canOpenOverlay() {
            return false;
        }

        public OverlaySettings getOverlaySettings() {
            return null;
        }

        public boolean isActivityStartable(Uri uri) {
            return true;
        }

        public boolean isExpandingToUrl() {
            return false;
        }

        public boolean isHandlingMMVideo(Uri uri) {
            return false;
        }

        public void startingActivity(Uri destinationUri) {
            MMLog.d(TAG, String.format("Starting activity for %s", new Object[]{destinationUri}));
        }

        public void startingVideo() {
        }

        public void updateLastVideoViewedTime() {
        }
    }

    HttpRedirection() {
    }

    static final String navigateRedirects(String urlString) {
        if (urlString == null) {
            return null;
        }
        HttpURLConnection.setFollowRedirects(false);
        while (!urlString.startsWith(HTTPS)) {
            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(CustomEventBannerAdapter.DEFAULT_BANNER_TIMEOUT_DELAY);
                conn.setRequestMethod(METHOD_GET);
                conn.connect();
                int rc = conn.getResponseCode();
                String locationUrl = conn.getHeaderField(HEADER_LOCATION);
                if (rc < 300 || rc >= 400 || TextUtils.isEmpty(locationUrl)) {
                    return urlString;
                }
                URI locationUri = new URI(locationUrl);
                if (!locationUri.isAbsolute()) {
                    urlString = url.toURI().resolve(locationUri).toString();
                } else if (locationUrl != null) {
                    urlString = locationUrl;
                }
                MMLog.v(TAG, String.format(LOG_URL_FORMAT, new Object[]{urlString}));
            } catch (MalformedURLException e) {
                MMLog.e(TAG, "Bad url scheme", e);
                return urlString;
            } catch (SocketTimeoutException e2) {
                MMLog.e(TAG, "Connection timeout.", e2);
                return urlString;
            } catch (IOException e3) {
                MMLog.e(TAG, "IOException following redirects: ", e3);
                return urlString;
            } catch (URISyntaxException e4) {
                MMLog.e(TAG, "URI Syntax incorrect.", e4);
                return urlString;
            }
        }
        return urlString;
    }

    static void startActivityFromUri(RedirectionListenerImpl listener) {
        if (listener != null && listener.url != null && listener.weakContext != null) {
            ThreadUtils.execute(new AnonymousClass_1(new WeakReference(listener)));
        }
    }
}