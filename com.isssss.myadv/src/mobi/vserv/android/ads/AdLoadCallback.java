package mobi.vserv.android.ads;

public interface AdLoadCallback {
    void onLoadFailure();

    void onLoadSuccess(VservAd vservAd);

    void onNoFill();
}