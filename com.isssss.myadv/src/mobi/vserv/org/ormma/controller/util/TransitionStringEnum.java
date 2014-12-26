package mobi.vserv.org.ormma.controller.util;

public enum TransitionStringEnum {
    DEFAULT("default"),
    DISSOLVE("dissolve"),
    FADE("fade"),
    ROLL("roll"),
    SLIDE("slide"),
    ZOOM("zoom"),
    NONE("none");
    private String text;

    static {
        String str = "default";
        DEFAULT = new TransitionStringEnum("DEFAULT", 0, "default");
        str = "dissolve";
        DISSOLVE = new TransitionStringEnum("DISSOLVE", 1, "dissolve");
        str = "fade";
        FADE = new TransitionStringEnum("FADE", 2, "fade");
        str = "roll";
        ROLL = new TransitionStringEnum("ROLL", 3, "roll");
        str = "slide";
        SLIDE = new TransitionStringEnum("SLIDE", 4, "slide");
        String str2 = "zoom";
        ZOOM = new TransitionStringEnum("ZOOM", 5, "zoom");
        str2 = "none";
        NONE = new TransitionStringEnum("NONE", 6, "none");
        ENUM$VALUES = new TransitionStringEnum[]{DEFAULT, DISSOLVE, FADE, ROLL, SLIDE, ZOOM, NONE};
    }

    private TransitionStringEnum(String text) {
        this.text = text;
    }

    public static TransitionStringEnum fromString(String text) {
        if (text != null) {
            TransitionStringEnum[] values = values();
            int length = values.length;
            int i = 0;
            while (i < length) {
                TransitionStringEnum b = values[i];
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