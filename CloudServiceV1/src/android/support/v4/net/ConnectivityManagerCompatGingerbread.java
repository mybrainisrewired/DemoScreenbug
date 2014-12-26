package android.support.v4.net;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.wmt.data.LocalAudioAll;
import com.wmt.opengl.grid.ItemAnimation;

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
            case ItemAnimation.CUR_Z:
            case ItemAnimation.CUR_ALPHA:
            case ItemAnimation.CUR_ARC:
            case FragmentManagerImpl.ANIM_STYLE_FADE_ENTER:
            case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
                return true;
            case LocalAudioAll.SORT_BY_DATE:
                return false;
            default:
                return true;
        }
    }
}