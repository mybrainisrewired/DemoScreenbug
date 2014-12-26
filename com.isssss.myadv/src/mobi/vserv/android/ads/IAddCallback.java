package mobi.vserv.android.ads;

import android.view.View;

public interface IAddCallback {
    void TimeOutOccured();

    void onLoadFailure();

    void onLoadSuccess(View view);

    void onNoFill();

    void showProgressBar();
}