package mobi.vserv.android.ads;

public enum AdOrientation {
    PORTRAIT,
    LANDSCAPE;

    static {
        PORTRAIT = new AdOrientation("PORTRAIT", 0);
        LANDSCAPE = new AdOrientation("LANDSCAPE", 1);
        ENUM$VALUES = new AdOrientation[]{PORTRAIT, LANDSCAPE};
    }
}