package mobi.vserv.android.ads;

import mobi.vserv.org.ormma.controller.util.OrmmaPlayer;

public class VservAdCacheVideo {
    public OrmmaPlayer getCachePlayer() {
        return VservManager.getAdVideo();
    }

    public void removeCachePlayer() {
        VservManager.removeAdVideo();
    }
}