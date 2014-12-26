package com.millennialmedia.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import com.google.android.gms.drive.DriveFile;
import com.inmobi.commons.analytics.db.AnalyticsSQLiteHelper;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.mopub.common.Preconditions;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

class Utils {
    private static final String TAG = "Utils";

    static class HttpUtils {

        static class AnonymousClass_1 implements Runnable {
            final /* synthetic */ String val$url;

            AnonymousClass_1(String str) {
                this.val$url = str;
            }

            public void run() {
                try {
                    HttpResponse request = new DefaultHttpClient().execute(new HttpGet(this.val$url));
                    MMLog.d(TAG, "Executed Url :\"" + this.val$url + "\"");
                } catch (IOException e) {
                    MMLog.e(TAG, "Exception with HttpUtils: ", e);
                }
            }
        }

        HttpUtils() {
        }

        static void executeUrl(String url) {
            ThreadUtils.execute(new AnonymousClass_1(url));
        }
    }

    static class IntentUtils {
        IntentUtils() {
        }

        private static void fixDataAndTypeForVideo(Context context, Intent intent) {
            Uri data = intent.getData();
            if (data != null) {
                String lastPathSegment = data.getLastPathSegment();
                if (TextUtils.isEmpty(intent.getStringExtra("class")) && !TextUtils.isEmpty(lastPathSegment)) {
                    if (lastPathSegment.endsWith(".mp4") || lastPathSegment.endsWith(".3gp") || lastPathSegment.endsWith(".mkv") || lastPathSegment.endsWith("content.once")) {
                        intent.setDataAndType(intent.getData(), "video/*");
                    }
                }
            }
        }

        static Intent getIntentForUri(RedirectionListenerImpl listener) {
            if (listener == null) {
                return null;
            }
            Intent intent = null;
            Uri destinationUri = listener.destinationUri;
            Context context = (Context) listener.weakContext.get();
            if (context == null) {
                return null;
            }
            if (destinationUri != null) {
                String scheme = destinationUri.getScheme();
                if (scheme != null) {
                    if (scheme.equalsIgnoreCase(Event.INTENT_MARKET)) {
                        MMLog.v(TAG, "Creating Android Market intent.");
                        intent = new Intent("android.intent.action.VIEW", destinationUri);
                        Event.intentStarted(context, Event.INTENT_MARKET, listener.creatorAdImplInternalId);
                    } else if (scheme.equalsIgnoreCase("rtsp")) {
                        MMLog.v(TAG, "Creating streaming video player intent.");
                        intent = new Intent(context, MMActivity.class);
                        intent.setData(destinationUri);
                        intent.putExtra("class", "com.millennialmedia.android.VideoPlayerActivity");
                    } else if (scheme.equalsIgnoreCase(Event.INTENT_PHONE_CALL)) {
                        MMLog.v(TAG, "Creating telephone intent.");
                        intent = new Intent("android.intent.action.DIAL", destinationUri);
                        Event.intentStarted(context, Event.INTENT_PHONE_CALL, listener.creatorAdImplInternalId);
                    } else if (scheme.equalsIgnoreCase(Event.INTENT_TXT_MESSAGE)) {
                        MMLog.v(TAG, "Creating txt message intent.");
                        String schemeParts = destinationUri.getSchemeSpecificPart();
                        String address = Preconditions.EMPTY_ARGUMENTS;
                        int bodyIndex = schemeParts.indexOf("?body=");
                        if (bodyIndex != -1 && schemeParts.length() > bodyIndex) {
                            address = schemeParts.substring(0, bodyIndex).replace(',', ';');
                        }
                        intent = new Intent("android.intent.action.VIEW", Uri.parse("sms:" + address));
                        if (bodyIndex == -1) {
                            intent.putExtra("sms_body", schemeParts.substring(bodyIndex + 6));
                        }
                        Event.intentStarted(context, Event.INTENT_TXT_MESSAGE, listener.creatorAdImplInternalId);
                    } else if (scheme.equalsIgnoreCase("mailto")) {
                        intent = new Intent("android.intent.action.VIEW", destinationUri);
                        Event.intentStarted(context, Event.INTENT_EMAIL, listener.creatorAdImplInternalId);
                    } else if (scheme.equalsIgnoreCase(Event.INTENT_MAPS)) {
                        MMLog.v(TAG, "Creating Google Maps intent.");
                        intent = new Intent("android.intent.action.VIEW", destinationUri);
                        Event.intentStarted(context, Event.INTENT_MAPS, listener.creatorAdImplInternalId);
                    } else if (scheme.equalsIgnoreCase("https")) {
                        MMLog.v(TAG, "Creating launch browser intent.");
                        intent = new Intent("android.intent.action.VIEW", destinationUri);
                        Event.intentStarted(context, Event.INTENT_EXTERNAL_BROWSER, listener.creatorAdImplInternalId);
                    } else if (scheme.equalsIgnoreCase("mmbrowser")) {
                        String mmBrowserUrl = destinationUri.toString().substring(ApiEventType.API_MRAID_RESIZE);
                        if (!(mmBrowserUrl == null || mmBrowserUrl.contains("://"))) {
                            mmBrowserUrl = mmBrowserUrl.replaceFirst("//", "://");
                        }
                        MMLog.v(TAG, "MMBrowser - Creating launch browser intent.");
                        intent = new Intent("android.intent.action.VIEW", Uri.parse(mmBrowserUrl));
                        Event.intentStarted(context, Event.INTENT_EXTERNAL_BROWSER, listener.creatorAdImplInternalId);
                    } else if (!scheme.equalsIgnoreCase("http")) {
                        MMLog.v(TAG, String.format("Creating intent for unrecognized URI. %s", new Object[]{destinationUri}));
                        intent = new Intent("android.intent.action.VIEW", destinationUri);
                    } else if (destinationUri.getLastPathSegment() != null && (destinationUri.getLastPathSegment().endsWith(".mp4") || destinationUri.getLastPathSegment().endsWith(".3gp"))) {
                        MMLog.v(TAG, "Creating video player intent.");
                        intent = new Intent(context, MMActivity.class);
                        intent.setData(destinationUri);
                        intent.putExtra("class", "com.millennialmedia.android.VideoPlayerActivity");
                        Event.intentStarted(context, Event.INTENT_STREAMING_VIDEO, listener.creatorAdImplInternalId);
                    } else if (listener.canOpenOverlay()) {
                        MMLog.v(TAG, "Creating launch overlay intent.");
                        intent = new Intent(context, MMActivity.class);
                        intent.putExtra("class", AdViewOverlayActivity.class.getCanonicalName());
                        intent.setData(destinationUri);
                        return intent;
                    } else {
                        MMLog.v(TAG, "Creating launch browser intent.");
                        Event.intentStarted(context, Event.INTENT_EXTERNAL_BROWSER, listener.creatorAdImplInternalId);
                        intent = new Intent("android.intent.action.VIEW", destinationUri);
                    }
                }
            }
            if (intent != null) {
                MMLog.v(TAG, String.format("%s resolved to Intent: %s", new Object[]{destinationUri, intent}));
                return intent;
            } else {
                MMLog.v(TAG, String.format("%s", new Object[]{destinationUri}));
                return intent;
            }
        }

        static void startActionView(Context context, String nextUrl) {
            startActivity(context, new Intent("android.intent.action.VIEW", Uri.parse(nextUrl)));
        }

        static void startActivity(Context context, Intent intent) {
            if (!context instanceof Activity) {
                intent.addFlags(DriveFile.MODE_READ_ONLY);
            }
            fixDataAndTypeForVideo(context, intent);
            context.startActivity(intent);
        }

        static void startAdViewOverlayActivity(Context context) {
            Intent intent = new Intent(context, MMActivity.class);
            intent.putExtra("class", "com.millennialmedia.android.AdViewOverlayActivity");
            startActivity(context, intent);
        }

        static void startAdViewOverlayActivity(Context context, Intent extrasAddedIntent) {
            extrasAddedIntent.setClass(context, MMActivity.class);
            extrasAddedIntent.putExtra("class", "com.millennialmedia.android.AdViewOverlayActivity");
            startActivity(context, extrasAddedIntent);
        }

        static void startAdViewOverlayActivityWithData(Context context, String url) {
            Intent intent = new Intent(context, MMActivity.class);
            intent.putExtra("class", "com.millennialmedia.android.AdViewOverlayActivity");
            intent.setData(Uri.parse(url));
            startActivity(context, intent);
        }

        static void startCachedVideoPlayerActivity(Context context, Intent extrasAddedIntent) {
            extrasAddedIntent.setClass(context, MMActivity.class);
            extrasAddedIntent.putExtra("class", "com.millennialmedia.android.CachedVideoPlayerActivity");
            startActivity(context, extrasAddedIntent);
        }

        static void startPickerActivity(Context context, File file, String type) {
            Intent intent = new Intent(context, MMActivity.class);
            intent.setData(Uri.fromFile(file));
            intent.putExtra(AnalyticsSQLiteHelper.EVENT_LIST_TYPE, type);
            intent.putExtra("class", "com.millennialmedia.android.BridgeMMMedia$PickerActivity");
            startActivity(context, intent);
        }

        static void startVideoPlayerActivityWithData(Context context, Uri dataUri) {
            Intent intent = new Intent(context, MMActivity.class);
            intent.setData(dataUri);
            intent.putExtra("class", "com.millennialmedia.android.VideoPlayerActivity");
            startActivity(context, intent);
        }

        static void startVideoPlayerActivityWithData(Context context, File file) {
            startVideoPlayerActivityWithData(context, Uri.fromFile(file));
        }

        static void startVideoPlayerActivityWithData(Context context, String data) {
            startVideoPlayerActivityWithData(context, Uri.parse(data));
        }
    }

    static class ThreadUtils {
        private static final ExecutorService cachedThreadExecutor;

        static {
            cachedThreadExecutor = Executors.newCachedThreadPool();
        }

        ThreadUtils() {
        }

        static void execute(Runnable runnable) {
            cachedThreadExecutor.execute(runnable);
        }
    }

    Utils() {
    }

    static JSONObject getViewDimensions(View view) {
        JSONObject dimensions = new JSONObject();
        if (view == null) {
            MMLog.e(TAG, "Unable to calculate view dimensions for null view");
        } else {
            DisplayMetrics metrics = view.getContext().getResources().getDisplayMetrics();
            if (metrics != null) {
                int[] location = new int[2];
                view.getLocationInWindow(location);
                try {
                    dimensions.put("x", (int) (((float) location[0]) / metrics.density));
                    dimensions.put("y", (int) (((float) location[1]) / metrics.density));
                    dimensions.put(MMLayout.KEY_WIDTH, (int) (((float) view.getWidth()) / metrics.density));
                    dimensions.put(MMLayout.KEY_HEIGHT, (int) (((float) view.getHeight()) / metrics.density));
                } catch (JSONException e) {
                    MMLog.e(TAG, "Unable to build view dimensions", e);
                }
            }
        }
        return dimensions;
    }
}