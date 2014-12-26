package mobi.vserv.android.ads;

public enum AdPosition {
    START,
    IN,
    END;

    static {
        START = new AdPosition("START", 0);
        IN = new AdPosition("IN", 1);
        END = new AdPosition("END", 2);
        ENUM$VALUES = new AdPosition[]{START, IN, END};
    }
}