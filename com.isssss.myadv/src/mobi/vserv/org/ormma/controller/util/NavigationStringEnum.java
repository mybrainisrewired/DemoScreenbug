package mobi.vserv.org.ormma.controller.util;

public enum NavigationStringEnum {
    NONE("none"),
    CLOSE("close"),
    BACK("back"),
    FORWARD("forward"),
    REFRESH("refresh");
    private String text;

    static {
        String str = "none";
        NONE = new NavigationStringEnum("NONE", 0, "none");
        str = "close";
        CLOSE = new NavigationStringEnum("CLOSE", 1, "close");
        str = "back";
        BACK = new NavigationStringEnum("BACK", 2, "back");
        str = "forward";
        FORWARD = new NavigationStringEnum("FORWARD", 3, "forward");
        str = "refresh";
        REFRESH = new NavigationStringEnum("REFRESH", 4, "refresh");
        ENUM$VALUES = new NavigationStringEnum[]{NONE, CLOSE, BACK, FORWARD, REFRESH};
    }

    private NavigationStringEnum(String text) {
        this.text = text;
    }

    public static NavigationStringEnum fromString(String text) {
        if (text != null) {
            NavigationStringEnum[] values = values();
            int length = values.length;
            int i = 0;
            while (i < length) {
                NavigationStringEnum b = values[i];
                if (text.equalsIgnoreCase(b.text)) {
                    return b;
                }
                i++;
            }
        }
        return null;
    }

    public String getText() {
        return this.text;
    }
}