package android.support.v4.accessibilityservice;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.pm.ResolveInfo;
import android.os.Build.VERSION;
import com.wmt.data.LocalAudioAll;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.codehaus.jackson.org.objectweb.asm.Type;

public class AccessibilityServiceInfoCompat {
    public static final int FEEDBACK_ALL_MASK = -1;
    private static final AccessibilityServiceInfoVersionImpl IMPL;

    static interface AccessibilityServiceInfoVersionImpl {
        boolean getCanRetrieveWindowContent(AccessibilityServiceInfo accessibilityServiceInfo);

        String getDescription(AccessibilityServiceInfo accessibilityServiceInfo);

        String getId(AccessibilityServiceInfo accessibilityServiceInfo);

        ResolveInfo getResolveInfo(AccessibilityServiceInfo accessibilityServiceInfo);

        String getSettingsActivityName(AccessibilityServiceInfo accessibilityServiceInfo);
    }

    static class AccessibilityServiceInfoStubImpl implements AccessibilityServiceInfoVersionImpl {
        AccessibilityServiceInfoStubImpl() {
        }

        public boolean getCanRetrieveWindowContent(AccessibilityServiceInfo info) {
            return false;
        }

        public String getDescription(AccessibilityServiceInfo info) {
            return null;
        }

        public String getId(AccessibilityServiceInfo info) {
            return null;
        }

        public ResolveInfo getResolveInfo(AccessibilityServiceInfo info) {
            return null;
        }

        public String getSettingsActivityName(AccessibilityServiceInfo info) {
            return null;
        }
    }

    static class AccessibilityServiceInfoIcsImpl extends AccessibilityServiceInfoStubImpl {
        AccessibilityServiceInfoIcsImpl() {
        }

        public boolean getCanRetrieveWindowContent(AccessibilityServiceInfo info) {
            return AccessibilityServiceInfoCompatIcs.getCanRetrieveWindowContent(info);
        }

        public String getDescription(AccessibilityServiceInfo info) {
            return AccessibilityServiceInfoCompatIcs.getDescription(info);
        }

        public String getId(AccessibilityServiceInfo info) {
            return AccessibilityServiceInfoCompatIcs.getId(info);
        }

        public ResolveInfo getResolveInfo(AccessibilityServiceInfo info) {
            return AccessibilityServiceInfoCompatIcs.getResolveInfo(info);
        }

        public String getSettingsActivityName(AccessibilityServiceInfo info) {
            return AccessibilityServiceInfoCompatIcs.getSettingsActivityName(info);
        }
    }

    static {
        if (VERSION.SDK_INT >= 14) {
            IMPL = new AccessibilityServiceInfoIcsImpl();
        } else {
            IMPL = new AccessibilityServiceInfoStubImpl();
        }
    }

    private AccessibilityServiceInfoCompat() {
    }

    public static String feedbackTypeToString(int feedbackType) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        while (feedbackType > 0) {
            int feedbackTypeFlag = 1 << Integer.numberOfTrailingZeros(feedbackType);
            feedbackType &= feedbackTypeFlag ^ -1;
            if (builder.length() > 1) {
                builder.append(", ");
            }
            switch (feedbackTypeFlag) {
                case LocalAudioAll.SORT_BY_DATE:
                    builder.append("FEEDBACK_SPOKEN");
                    break;
                case ClassWriter.COMPUTE_FRAMES:
                    builder.append("FEEDBACK_HAPTIC");
                    break;
                case JsonWriteContext.STATUS_EXPECT_VALUE:
                    builder.append("FEEDBACK_AUDIBLE");
                    break;
                case Type.DOUBLE:
                    builder.append("FEEDBACK_VISUAL");
                    break;
                case Segment.TOKENS_PER_SEGMENT:
                    builder.append("FEEDBACK_GENERIC");
                    break;
                default:
                    break;
            }
        }
        builder.append("]");
        return builder.toString();
    }

    public static String flagToString(int flag) {
        switch (flag) {
            case LocalAudioAll.SORT_BY_DATE:
                return "DEFAULT";
            default:
                return null;
        }
    }

    public static boolean getCanRetrieveWindowContent(AccessibilityServiceInfo info) {
        return IMPL.getCanRetrieveWindowContent(info);
    }

    public static String getDescription(AccessibilityServiceInfo info) {
        return IMPL.getDescription(info);
    }

    public static String getId(AccessibilityServiceInfo info) {
        return IMPL.getId(info);
    }

    public static ResolveInfo getResolveInfo(AccessibilityServiceInfo info) {
        return IMPL.getResolveInfo(info);
    }

    public static String getSettingsActivityName(AccessibilityServiceInfo info) {
        return IMPL.getSettingsActivityName(info);
    }
}