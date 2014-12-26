package com.mopub.nativeads;

import android.content.Context;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.Numbers;
import com.mopub.nativeads.CustomEventNative.CustomEventNativeListener;
import com.mopub.nativeads.CustomEventNative.ImageListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class MoPubCustomEventNative extends CustomEventNative {

    static class MoPubForwardingNativeAd extends BaseForwardingNativeAd {
        private static /* synthetic */ int[] $SWITCH_TABLE$com$mopub$nativeads$NativeResponse$Parameter;
        private final Context mContext;
        private final CustomEventNativeListener mCustomEventNativeListener;
        private final String mJsonString;

        static /* synthetic */ int[] $SWITCH_TABLE$com$mopub$nativeads$NativeResponse$Parameter() {
            int[] iArr = $SWITCH_TABLE$com$mopub$nativeads$NativeResponse$Parameter;
            if (iArr == null) {
                iArr = new int[Parameter.values().length];
                try {
                    iArr[Parameter.CALL_TO_ACTION.ordinal()] = 9;
                } catch (NoSuchFieldError e) {
                }
                try {
                    iArr[Parameter.CLICK_DESTINATION.ordinal()] = 7;
                } catch (NoSuchFieldError e2) {
                }
                try {
                    iArr[Parameter.CLICK_TRACKER.ordinal()] = 2;
                } catch (NoSuchFieldError e3) {
                }
                try {
                    iArr[Parameter.FALLBACK.ordinal()] = 8;
                } catch (NoSuchFieldError e4) {
                }
                try {
                    iArr[Parameter.ICON_IMAGE.ordinal()] = 6;
                } catch (NoSuchFieldError e5) {
                }
                try {
                    iArr[Parameter.IMPRESSION_TRACKER.ordinal()] = 1;
                } catch (NoSuchFieldError e6) {
                }
                try {
                    iArr[Parameter.MAIN_IMAGE.ordinal()] = 5;
                } catch (NoSuchFieldError e7) {
                }
                try {
                    iArr[Parameter.STAR_RATING.ordinal()] = 10;
                } catch (NoSuchFieldError e8) {
                }
                try {
                    iArr[Parameter.TEXT.ordinal()] = 4;
                } catch (NoSuchFieldError e9) {
                }
                try {
                    iArr[Parameter.TITLE.ordinal()] = 3;
                } catch (NoSuchFieldError e10) {
                }
                $SWITCH_TABLE$com$mopub$nativeads$NativeResponse$Parameter = iArr;
            }
            return iArr;
        }

        MoPubForwardingNativeAd(Context context, String jsonString, CustomEventNativeListener customEventNativeListener) {
            this.mContext = context;
            this.mJsonString = jsonString;
            this.mCustomEventNativeListener = customEventNativeListener;
        }

        private void addImpressionTrackers(Object impressionTrackers) throws ClassCastException {
            if (impressionTrackers instanceof JSONArray) {
                JSONArray trackers = (JSONArray) impressionTrackers;
                int i = 0;
                while (i < trackers.length()) {
                    try {
                        addImpressionTracker(trackers.getString(i));
                    } catch (JSONException e) {
                        MoPubLog.d("Unable to parse impression trackers.");
                    }
                    i++;
                }
            } else {
                throw new ClassCastException("Expected impression trackers of type JSONArray.");
            }
        }

        private void addInstanceVariable(Parameter key, Object value) throws ClassCastException {
            try {
                switch ($SWITCH_TABLE$com$mopub$nativeads$NativeResponse$Parameter()[key.ordinal()]) {
                    case MMAdView.TRANSITION_FADE:
                        addImpressionTrackers(value);
                    case MMAdView.TRANSITION_UP:
                        break;
                    case MMAdView.TRANSITION_DOWN:
                        setTitle((String) value);
                    case MMAdView.TRANSITION_RANDOM:
                        setText((String) value);
                    case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                        setMainImageUrl((String) value);
                    case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                        setIconImageUrl((String) value);
                    case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                        setClickDestinationUrl((String) value);
                    case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                        setCallToAction((String) value);
                    case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                        setStarRating(Numbers.parseDouble(value));
                    default:
                        MoPubLog.d(new StringBuilder("Unable to add JSON key to internal mapping: ").append(key.name).toString());
                }
            } catch (ClassCastException e) {
                e = e;
                if (key.required) {
                    ClassCastException e2;
                    throw e2;
                }
                MoPubLog.d(new StringBuilder("Ignoring class cast exception for optional key: ").append(key.name).toString());
            }
        }

        private boolean containsRequiredKeys(JSONObject jsonObject) {
            Set<String> keys = new HashSet();
            Iterator<String> jsonKeys = jsonObject.keys();
            while (jsonKeys.hasNext()) {
                keys.add((String) jsonKeys.next());
            }
            return keys.containsAll(Parameter.requiredKeys);
        }

        private boolean isImageKey(String name) {
            return name != null && name.toLowerCase().endsWith("image");
        }

        List<String> getAllImageUrls() {
            List<String> imageUrls = new ArrayList();
            if (getMainImageUrl() != null) {
                imageUrls.add(getMainImageUrl());
            }
            if (getIconImageUrl() != null) {
                imageUrls.add(getIconImageUrl());
            }
            imageUrls.addAll(getExtrasImageUrls());
            return imageUrls;
        }

        List<String> getExtrasImageUrls() {
            List<String> extrasBitmapUrls = new ArrayList(getExtras().size());
            Iterator it = getExtras().entrySet().iterator();
            while (it.hasNext()) {
                Entry<String, Object> entry = (Entry) it.next();
                if (isImageKey((String) entry.getKey()) && entry.getValue() instanceof String) {
                    extrasBitmapUrls.add((String) entry.getValue());
                }
            }
            return extrasBitmapUrls;
        }

        void loadAd() throws IllegalArgumentException, JSONException {
            if (this.mJsonString == null) {
                throw new IllegalArgumentException("Json String cannot be null");
            }
            JSONObject jsonObject = new JSONObject(new JSONTokener(this.mJsonString));
            if (containsRequiredKeys(jsonObject)) {
                Iterator<String> keys = jsonObject.keys();
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    Parameter parameter = Parameter.from(key);
                    if (parameter != null) {
                        try {
                            addInstanceVariable(parameter, jsonObject.opt(key));
                        } catch (ClassCastException e) {
                            throw new IllegalArgumentException(new StringBuilder("JSONObject key (").append(key).append(") contained unexpected value.").toString());
                        }
                    } else {
                        addExtra(key, jsonObject.opt(key));
                    }
                }
                preCacheImages(this.mContext, getAllImageUrls(), new ImageListener() {
                    public void onImagesCached() {
                        MoPubForwardingNativeAd.this.mCustomEventNativeListener.onNativeAdLoaded(MoPubForwardingNativeAd.this);
                    }

                    public void onImagesFailedToCache(NativeErrorCode errorCode) {
                        MoPubForwardingNativeAd.this.mCustomEventNativeListener.onNativeAdFailed(errorCode);
                    }
                });
            } else {
                throw new IllegalArgumentException("JSONObject did not contain required keys.");
            }
        }
    }

    protected void loadNativeAd(Context context, CustomEventNativeListener customEventNativeListener, Map<String, Object> localExtras, Map<String, String> serverExtras) {
        try {
            new MoPubForwardingNativeAd(context.getApplicationContext(), (String) serverExtras.get("response_body_key"), customEventNativeListener).loadAd();
        } catch (IllegalArgumentException e) {
            customEventNativeListener.onNativeAdFailed(NativeErrorCode.UNSPECIFIED);
        } catch (JSONException e2) {
            customEventNativeListener.onNativeAdFailed(NativeErrorCode.INVALID_JSON);
        }
    }
}