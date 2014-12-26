package android.support.v4.net;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.wmt.data.LocalAudioAll;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;

class ConnectivityManagerCompatGingerbread {
    ConnectivityManagerCompatGingerbread() {
    }

    public static boolean isActiveNetworkMetered(ConnectivityManager cm) {
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            return true;
        }
        switch (info.getType()) {
            case LocalAudioAll.SORT_BY_TITLE:
            case ClassWriter.COMPUTE_FRAMES:
            case JsonWriteContext.STATUS_OK_AFTER_SPACE:
            case JsonWriteContext.STATUS_EXPECT_VALUE:
            case JsonWriteContext.STATUS_EXPECT_NAME:
            case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
                return true;
            case LocalAudioAll.SORT_BY_DATE:
                return false;
            default:
                return true;
        }
    }
}