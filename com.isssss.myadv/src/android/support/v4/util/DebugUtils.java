package android.support.v4.util;

import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;

public class DebugUtils {
    public static void buildShortClassTag(Object cls, StringBuilder out) {
        if (cls == null) {
            out.append("null");
        } else {
            String simpleName = cls.getClass().getSimpleName();
            if (simpleName == null || simpleName.length() <= 0) {
                simpleName = cls.getClass().getName();
                int end = simpleName.lastIndexOf(ApiEventType.API_MRAID_SEEK_VIDEO);
                if (end > 0) {
                    simpleName = simpleName.substring(end + 1);
                }
            }
            out.append(simpleName);
            out.append('{');
            out.append(Integer.toHexString(System.identityHashCode(cls)));
        }
    }
}