package android.support.v4.text;

import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import java.nio.CharBuffer;
import java.util.Locale;

public class TextDirectionHeuristicsCompat {
    public static final TextDirectionHeuristicCompat ANYRTL_LTR;
    public static final TextDirectionHeuristicCompat FIRSTSTRONG_LTR;
    public static final TextDirectionHeuristicCompat FIRSTSTRONG_RTL;
    public static final TextDirectionHeuristicCompat LOCALE;
    public static final TextDirectionHeuristicCompat LTR;
    public static final TextDirectionHeuristicCompat RTL;
    private static final int STATE_FALSE = 1;
    private static final int STATE_TRUE = 0;
    private static final int STATE_UNKNOWN = 2;

    private static interface TextDirectionAlgorithm {
        int checkRtl(CharSequence charSequence, int i, int i2);
    }

    private static class AnyStrong implements TextDirectionAlgorithm {
        public static final AnyStrong INSTANCE_LTR;
        public static final AnyStrong INSTANCE_RTL;
        private final boolean mLookForRtl;

        static {
            INSTANCE_RTL = new AnyStrong(true);
            INSTANCE_LTR = new AnyStrong(false);
        }

        private AnyStrong(boolean lookForRtl) {
            this.mLookForRtl = lookForRtl;
        }

        public int checkRtl(CharSequence cs, int start, int count) {
            boolean haveUnlookedFor = false;
            int i = start;
            int e = start + count;
            while (i < e) {
                switch (TextDirectionHeuristicsCompat.isRtlText(Character.getDirectionality(cs.charAt(i)))) {
                    case STATE_TRUE:
                        if (this.mLookForRtl) {
                            return 0;
                        }
                        haveUnlookedFor = true;
                        break;
                    case STATE_FALSE:
                        if (!this.mLookForRtl) {
                            return STATE_FALSE;
                        }
                        haveUnlookedFor = true;
                        break;
                }
                i++;
            }
            if (haveUnlookedFor) {
                return !this.mLookForRtl ? 0 : STATE_FALSE;
            } else {
                return STATE_UNKNOWN;
            }
        }
    }

    private static class FirstStrong implements TextDirectionAlgorithm {
        public static final FirstStrong INSTANCE;

        static {
            INSTANCE = new FirstStrong();
        }

        private FirstStrong() {
        }

        public int checkRtl(CharSequence cs, int start, int count) {
            int result = STATE_UNKNOWN;
            int i = start;
            int e = start + count;
            while (i < e && result == 2) {
                result = TextDirectionHeuristicsCompat.isRtlTextOrFormat(Character.getDirectionality(cs.charAt(i)));
                i++;
            }
            return result;
        }
    }

    private static abstract class TextDirectionHeuristicImpl implements TextDirectionHeuristicCompat {
        private final TextDirectionAlgorithm mAlgorithm;

        public TextDirectionHeuristicImpl(TextDirectionAlgorithm algorithm) {
            this.mAlgorithm = algorithm;
        }

        private boolean doCheck(CharSequence cs, int start, int count) {
            switch (this.mAlgorithm.checkRtl(cs, start, count)) {
                case STATE_TRUE:
                    return true;
                case STATE_FALSE:
                    return false;
                default:
                    return defaultIsRtl();
            }
        }

        protected abstract boolean defaultIsRtl();

        public boolean isRtl(CharSequence cs, int start, int count) {
            if (cs != null && start >= 0 && count >= 0 && cs.length() - count >= start) {
                return this.mAlgorithm == null ? defaultIsRtl() : doCheck(cs, start, count);
            } else {
                throw new IllegalArgumentException();
            }
        }

        public boolean isRtl(char[] array, int start, int count) {
            return isRtl(CharBuffer.wrap(array), start, count);
        }
    }

    private static class TextDirectionHeuristicInternal extends TextDirectionHeuristicImpl {
        private final boolean mDefaultIsRtl;

        private TextDirectionHeuristicInternal(TextDirectionAlgorithm algorithm, boolean defaultIsRtl) {
            super(algorithm);
            this.mDefaultIsRtl = defaultIsRtl;
        }

        protected boolean defaultIsRtl() {
            return this.mDefaultIsRtl;
        }
    }

    private static class TextDirectionHeuristicLocale extends TextDirectionHeuristicImpl {
        public static final TextDirectionHeuristicLocale INSTANCE;

        static {
            INSTANCE = new TextDirectionHeuristicLocale();
        }

        public TextDirectionHeuristicLocale() {
            super(null);
        }

        protected boolean defaultIsRtl() {
            return TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == 1;
        }
    }

    static {
        LTR = new TextDirectionHeuristicInternal(false, null);
        RTL = new TextDirectionHeuristicInternal(true, null);
        FIRSTSTRONG_LTR = new TextDirectionHeuristicInternal(false, null);
        FIRSTSTRONG_RTL = new TextDirectionHeuristicInternal(true, null);
        ANYRTL_LTR = new TextDirectionHeuristicInternal(false, null);
        LOCALE = TextDirectionHeuristicLocale.INSTANCE;
    }

    private static int isRtlText(int directionality) {
        switch (directionality) {
            case STATE_TRUE:
                return STATE_FALSE;
            case STATE_FALSE:
            case STATE_UNKNOWN:
                return STATE_TRUE;
            default:
                return STATE_UNKNOWN;
        }
    }

    private static int isRtlTextOrFormat(int directionality) {
        switch (directionality) {
            case STATE_TRUE:
            case ApiEventType.API_MRAID_IS_VIEWABLE:
            case ApiEventType.API_MRAID_GET_PLACEMENT_TYPE:
                return STATE_FALSE;
            case STATE_FALSE:
            case STATE_UNKNOWN:
            case ApiEventType.API_MRAID_GET_ORIENTATION:
            case ApiEventType.API_MRAID_GET_SCREEN_SIZE:
                return STATE_TRUE;
            default:
                return STATE_UNKNOWN;
        }
    }
}