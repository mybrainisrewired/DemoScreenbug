package mobi.vserv.android.ads;

public enum AdType {
    INTERSTITIAL,
    OVERLAY;

    static {
        INTERSTITIAL = new AdType("INTERSTITIAL", 0);
        OVERLAY = new AdType("OVERLAY", 1);
        ENUM$VALUES = new AdType[]{INTERSTITIAL, OVERLAY};
    }
}