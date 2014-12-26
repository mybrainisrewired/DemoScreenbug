package android.support.v4.text;

import android.os.Build.VERSION;

public class ICUCompat {
    private static final ICUCompatImpl IMPL;

    static interface ICUCompatImpl {
        String addLikelySubtags(String str);

        String getScript(String str);
    }

    static class ICUCompatImplBase implements ICUCompatImpl {
        ICUCompatImplBase() {
        }

        public String addLikelySubtags(String locale) {
            return locale;
        }

        public String getScript(String locale) {
            return null;
        }
    }

    static class ICUCompatImplIcs implements ICUCompatImpl {
        ICUCompatImplIcs() {
        }

        public String addLikelySubtags(String locale) {
            return ICUCompatIcs.addLikelySubtags(locale);
        }

        public String getScript(String locale) {
            return ICUCompatIcs.getScript(locale);
        }
    }

    static {
        if (VERSION.SDK_INT >= 14) {
            IMPL = new ICUCompatImplIcs();
        } else {
            IMPL = new ICUCompatImplBase();
        }
    }

    public static String addLikelySubtags(String locale) {
        return IMPL.addLikelySubtags(locale);
    }

    public static String getScript(String locale) {
        return IMPL.getScript(locale);
    }
}