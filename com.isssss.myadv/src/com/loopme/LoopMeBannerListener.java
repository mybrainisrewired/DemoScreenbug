package com.loopme;

public interface LoopMeBannerListener {
    void onLoopMeBannerClicked(LoopMeBanner loopMeBanner);

    void onLoopMeBannerHide(LoopMeBanner loopMeBanner);

    void onLoopMeBannerLeaveApp(LoopMeBanner loopMeBanner);

    void onLoopMeBannerLoadFail(LoopMeBanner loopMeBanner, LoopMeError loopMeError);

    void onLoopMeBannerShow(LoopMeBanner loopMeBanner);
}