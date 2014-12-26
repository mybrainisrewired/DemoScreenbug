package com.loopme;

public interface LoopMeInterstitialListener {
    void onLoopMeExitNoClicked(LoopMeInterstitial loopMeInterstitial);

    void onLoopMeExitYesClicked(LoopMeInterstitial loopMeInterstitial);

    void onLoopMeInterstitialClicked(LoopMeInterstitial loopMeInterstitial);

    void onLoopMeInterstitialExpired(LoopMeInterstitial loopMeInterstitial);

    void onLoopMeInterstitialHide(LoopMeInterstitial loopMeInterstitial);

    void onLoopMeInterstitialLeaveApp(LoopMeInterstitial loopMeInterstitial);

    void onLoopMeInterstitialLoadFail(LoopMeInterstitial loopMeInterstitial, LoopMeError loopMeError);

    void onLoopMeInterstitialLoadSuccess(LoopMeInterstitial loopMeInterstitial);

    void onLoopMeInterstitialShow(LoopMeInterstitial loopMeInterstitial);
}