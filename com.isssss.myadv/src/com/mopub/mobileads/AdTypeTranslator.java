package com.mopub.mobileads;

import android.support.v4.os.EnvironmentCompat;
import com.inmobi.re.configs.Initializer;
import com.mopub.common.Preconditions;
import com.mopub.mobileads.MoPubInterstitial.MoPubInterstitialView;

public class AdTypeTranslator {

    public enum CustomEventType {
        GOOGLE_PLAY_SERVICES_BANNER("admob_native_banner", "com.mopub.mobileads.GooglePlayServicesBanner"),
        GOOGLE_PLAY_SERVICES_INTERSTITIAL("admob_full_interstitial", "com.mopub.mobileads.GooglePlayServicesInterstitial"),
        MILLENNIAL_BANNER("millennial_native_banner", "com.mopub.mobileads.MillennialBanner"),
        MILLENNIAL_INTERSTITIAL("millennial_full_interstitial", "com.mopub.mobileads.MillennialInterstitial"),
        MRAID_BANNER("mraid_banner", "com.mopub.mobileads.MraidBanner"),
        MRAID_INTERSTITIAL("mraid_interstitial", "com.mopub.mobileads.MraidInterstitial"),
        HTML_BANNER("html_banner", "com.mopub.mobileads.HtmlBanner"),
        HTML_INTERSTITIAL("html_interstitial", "com.mopub.mobileads.HtmlInterstitial"),
        VAST_VIDEO_INTERSTITIAL("vast_interstitial", "com.mopub.mobileads.VastVideoInterstitial"),
        UNSPECIFIED(Preconditions.EMPTY_ARGUMENTS, null);
        private final String mClassName;
        private final String mKey;

        static {
            String str = "admob_native_banner";
            String str2 = "com.mopub.mobileads.GooglePlayServicesBanner";
            GOOGLE_PLAY_SERVICES_BANNER = new com.mopub.mobileads.AdTypeTranslator.CustomEventType("GOOGLE_PLAY_SERVICES_BANNER", 0, "admob_native_banner", "com.mopub.mobileads.GooglePlayServicesBanner");
            str = "admob_full_interstitial";
            str2 = "com.mopub.mobileads.GooglePlayServicesInterstitial";
            GOOGLE_PLAY_SERVICES_INTERSTITIAL = new com.mopub.mobileads.AdTypeTranslator.CustomEventType("GOOGLE_PLAY_SERVICES_INTERSTITIAL", 1, "admob_full_interstitial", "com.mopub.mobileads.GooglePlayServicesInterstitial");
            str = "millennial_native_banner";
            str2 = "com.mopub.mobileads.MillennialBanner";
            MILLENNIAL_BANNER = new com.mopub.mobileads.AdTypeTranslator.CustomEventType("MILLENNIAL_BANNER", 2, "millennial_native_banner", "com.mopub.mobileads.MillennialBanner");
            str = "millennial_full_interstitial";
            str2 = "com.mopub.mobileads.MillennialInterstitial";
            MILLENNIAL_INTERSTITIAL = new com.mopub.mobileads.AdTypeTranslator.CustomEventType("MILLENNIAL_INTERSTITIAL", 3, "millennial_full_interstitial", "com.mopub.mobileads.MillennialInterstitial");
            str = "mraid_banner";
            str2 = "com.mopub.mobileads.MraidBanner";
            MRAID_BANNER = new com.mopub.mobileads.AdTypeTranslator.CustomEventType("MRAID_BANNER", 4, "mraid_banner", "com.mopub.mobileads.MraidBanner");
            str2 = "mraid_interstitial";
            String str3 = "com.mopub.mobileads.MraidInterstitial";
            MRAID_INTERSTITIAL = new com.mopub.mobileads.AdTypeTranslator.CustomEventType("MRAID_INTERSTITIAL", 5, "mraid_interstitial", "com.mopub.mobileads.MraidInterstitial");
            str2 = "html_banner";
            str3 = "com.mopub.mobileads.HtmlBanner";
            HTML_BANNER = new com.mopub.mobileads.AdTypeTranslator.CustomEventType("HTML_BANNER", 6, "html_banner", "com.mopub.mobileads.HtmlBanner");
            str2 = "html_interstitial";
            str3 = "com.mopub.mobileads.HtmlInterstitial";
            HTML_INTERSTITIAL = new com.mopub.mobileads.AdTypeTranslator.CustomEventType("HTML_INTERSTITIAL", 7, "html_interstitial", "com.mopub.mobileads.HtmlInterstitial");
            str2 = "vast_interstitial";
            str3 = "com.mopub.mobileads.VastVideoInterstitial";
            VAST_VIDEO_INTERSTITIAL = new com.mopub.mobileads.AdTypeTranslator.CustomEventType("VAST_VIDEO_INTERSTITIAL", 8, "vast_interstitial", "com.mopub.mobileads.VastVideoInterstitial");
            String str4 = "UNSPECIFIED";
            str2 = Preconditions.EMPTY_ARGUMENTS;
            UNSPECIFIED = new com.mopub.mobileads.AdTypeTranslator.CustomEventType(str4, 9, Preconditions.EMPTY_ARGUMENTS, null);
            ENUM$VALUES = new com.mopub.mobileads.AdTypeTranslator.CustomEventType[]{GOOGLE_PLAY_SERVICES_BANNER, GOOGLE_PLAY_SERVICES_INTERSTITIAL, MILLENNIAL_BANNER, MILLENNIAL_INTERSTITIAL, MRAID_BANNER, MRAID_INTERSTITIAL, HTML_BANNER, HTML_INTERSTITIAL, VAST_VIDEO_INTERSTITIAL, UNSPECIFIED};
        }

        private CustomEventType(String key, String className) {
            this.mKey = key;
            this.mClassName = className;
        }

        private static com.mopub.mobileads.AdTypeTranslator.CustomEventType fromString(String key) {
            com.mopub.mobileads.AdTypeTranslator.CustomEventType[] values = values();
            int length = values.length;
            int i = 0;
            while (i < length) {
                com.mopub.mobileads.AdTypeTranslator.CustomEventType customEventType = values[i];
                if (customEventType.mKey.equals(key)) {
                    return customEventType;
                }
                i++;
            }
            return UNSPECIFIED;
        }

        public String toString() {
            return this.mClassName;
        }
    }

    static String getAdNetworkType(String adType, String fullAdType) {
        String adNetworkType;
        if ("interstitial".equals(adType)) {
            adNetworkType = fullAdType;
        } else {
            adNetworkType = adType;
        }
        return adNetworkType != null ? adNetworkType : EnvironmentCompat.MEDIA_UNKNOWN;
    }

    static String getCustomEventNameForAdType(MoPubView moPubView, String adType, String fullAdType) {
        CustomEventType customEventType;
        if ("html".equals(adType) || Initializer.PRODUCT_MRAID.equals(adType)) {
            if (isInterstitial(moPubView)) {
                customEventType = CustomEventType.access$2(new StringBuilder(String.valueOf(adType)).append("_interstitial").toString());
            } else {
                customEventType = CustomEventType.access$2(new StringBuilder(String.valueOf(adType)).append("_banner").toString());
            }
        } else if ("interstitial".equals(adType)) {
            customEventType = CustomEventType.access$2(new StringBuilder(String.valueOf(fullAdType)).append("_interstitial").toString());
        } else {
            customEventType = CustomEventType.access$2(new StringBuilder(String.valueOf(adType)).append("_banner").toString());
        }
        return customEventType.toString();
    }

    private static boolean isInterstitial(MoPubView moPubView) {
        return moPubView instanceof MoPubInterstitialView;
    }
}