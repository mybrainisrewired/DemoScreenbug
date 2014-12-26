package com.google.android.gms.internal;

import android.os.RemoteException;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.mediation.MediationBannerAdapter;
import com.google.ads.mediation.MediationBannerListener;
import com.google.ads.mediation.MediationInterstitialAdapter;
import com.google.ads.mediation.MediationInterstitialListener;
import com.google.ads.mediation.MediationServerParameters;
import com.google.ads.mediation.NetworkExtras;

public final class bx<NETWORK_EXTRAS extends NetworkExtras, SERVER_PARAMETERS extends MediationServerParameters> implements MediationBannerListener, MediationInterstitialListener {
    private final bs nG;

    class AnonymousClass_10 implements Runnable {
        final /* synthetic */ ErrorCode nK;

        AnonymousClass_10(ErrorCode errorCode) {
            this.nK = errorCode;
        }

        public void run() {
            try {
                bx.this.nG.onAdFailedToLoad(by.a(this.nK));
            } catch (RemoteException e) {
                dw.c("Could not call onAdFailedToLoad.", e);
            }
        }
    }

    class AnonymousClass_5 implements Runnable {
        final /* synthetic */ ErrorCode nK;

        AnonymousClass_5(ErrorCode errorCode) {
            this.nK = errorCode;
        }

        public void run() {
            try {
                bx.this.nG.onAdFailedToLoad(by.a(this.nK));
            } catch (RemoteException e) {
                dw.c("Could not call onAdFailedToLoad.", e);
            }
        }
    }

    public bx(bs bsVar) {
        this.nG = bsVar;
    }

    public void onClick(MediationBannerAdapter<?, ?> adapter) {
        dw.v("Adapter called onClick.");
        if (dv.bD()) {
            try {
                this.nG.P();
            } catch (RemoteException e) {
                dw.c("Could not call onAdClicked.", e);
            }
        } else {
            dw.z("onClick must be called on the main UI thread.");
            dv.rp.post(new Runnable() {
                public void run() {
                    try {
                        bx.this.nG.P();
                    } catch (RemoteException e) {
                        dw.c("Could not call onAdClicked.", e);
                    }
                }
            });
        }
    }

    public void onDismissScreen(MediationBannerAdapter<?, ?> adapter) {
        dw.v("Adapter called onDismissScreen.");
        if (dv.bD()) {
            try {
                this.nG.onAdClosed();
            } catch (RemoteException e) {
                dw.c("Could not call onAdClosed.", e);
            }
        } else {
            dw.z("onDismissScreen must be called on the main UI thread.");
            dv.rp.post(new Runnable() {
                public void run() {
                    try {
                        bx.this.nG.onAdClosed();
                    } catch (RemoteException e) {
                        dw.c("Could not call onAdClosed.", e);
                    }
                }
            });
        }
    }

    public void onDismissScreen(MediationInterstitialAdapter<?, ?> adapter) {
        dw.v("Adapter called onDismissScreen.");
        if (dv.bD()) {
            try {
                this.nG.onAdClosed();
            } catch (RemoteException e) {
                dw.c("Could not call onAdClosed.", e);
            }
        } else {
            dw.z("onDismissScreen must be called on the main UI thread.");
            dv.rp.post(new Runnable() {
                public void run() {
                    try {
                        bx.this.nG.onAdClosed();
                    } catch (RemoteException e) {
                        dw.c("Could not call onAdClosed.", e);
                    }
                }
            });
        }
    }

    public void onFailedToReceiveAd(MediationBannerAdapter<?, ?> adapter, ErrorCode errorCode) {
        dw.v("Adapter called onFailedToReceiveAd with error. " + errorCode);
        if (dv.bD()) {
            try {
                this.nG.onAdFailedToLoad(by.a(errorCode));
            } catch (RemoteException e) {
                dw.c("Could not call onAdFailedToLoad.", e);
            }
        } else {
            dw.z("onFailedToReceiveAd must be called on the main UI thread.");
            dv.rp.post(new AnonymousClass_5(errorCode));
        }
    }

    public void onFailedToReceiveAd(MediationInterstitialAdapter<?, ?> adapter, ErrorCode errorCode) {
        dw.v("Adapter called onFailedToReceiveAd with error " + errorCode + ".");
        if (dv.bD()) {
            try {
                this.nG.onAdFailedToLoad(by.a(errorCode));
            } catch (RemoteException e) {
                dw.c("Could not call onAdFailedToLoad.", e);
            }
        } else {
            dw.z("onFailedToReceiveAd must be called on the main UI thread.");
            dv.rp.post(new AnonymousClass_10(errorCode));
        }
    }

    public void onLeaveApplication(MediationBannerAdapter<?, ?> adapter) {
        dw.v("Adapter called onLeaveApplication.");
        if (dv.bD()) {
            try {
                this.nG.onAdLeftApplication();
            } catch (RemoteException e) {
                dw.c("Could not call onAdLeftApplication.", e);
            }
        } else {
            dw.z("onLeaveApplication must be called on the main UI thread.");
            dv.rp.post(new Runnable() {
                public void run() {
                    try {
                        bx.this.nG.onAdLeftApplication();
                    } catch (RemoteException e) {
                        dw.c("Could not call onAdLeftApplication.", e);
                    }
                }
            });
        }
    }

    public void onLeaveApplication(MediationInterstitialAdapter<?, ?> adapter) {
        dw.v("Adapter called onLeaveApplication.");
        if (dv.bD()) {
            try {
                this.nG.onAdLeftApplication();
            } catch (RemoteException e) {
                dw.c("Could not call onAdLeftApplication.", e);
            }
        } else {
            dw.z("onLeaveApplication must be called on the main UI thread.");
            dv.rp.post(new Runnable() {
                public void run() {
                    try {
                        bx.this.nG.onAdLeftApplication();
                    } catch (RemoteException e) {
                        dw.c("Could not call onAdLeftApplication.", e);
                    }
                }
            });
        }
    }

    public void onPresentScreen(MediationBannerAdapter<?, ?> adapter) {
        dw.v("Adapter called onPresentScreen.");
        if (dv.bD()) {
            try {
                this.nG.onAdOpened();
            } catch (RemoteException e) {
                dw.c("Could not call onAdOpened.", e);
            }
        } else {
            dw.z("onPresentScreen must be called on the main UI thread.");
            dv.rp.post(new Runnable() {
                public void run() {
                    try {
                        bx.this.nG.onAdOpened();
                    } catch (RemoteException e) {
                        dw.c("Could not call onAdOpened.", e);
                    }
                }
            });
        }
    }

    public void onPresentScreen(MediationInterstitialAdapter<?, ?> adapter) {
        dw.v("Adapter called onPresentScreen.");
        if (dv.bD()) {
            try {
                this.nG.onAdOpened();
            } catch (RemoteException e) {
                dw.c("Could not call onAdOpened.", e);
            }
        } else {
            dw.z("onPresentScreen must be called on the main UI thread.");
            dv.rp.post(new Runnable() {
                public void run() {
                    try {
                        bx.this.nG.onAdOpened();
                    } catch (RemoteException e) {
                        dw.c("Could not call onAdOpened.", e);
                    }
                }
            });
        }
    }

    public void onReceivedAd(MediationBannerAdapter<?, ?> adapter) {
        dw.v("Adapter called onReceivedAd.");
        if (dv.bD()) {
            try {
                this.nG.onAdLoaded();
            } catch (RemoteException e) {
                dw.c("Could not call onAdLoaded.", e);
            }
        } else {
            dw.z("onReceivedAd must be called on the main UI thread.");
            dv.rp.post(new Runnable() {
                public void run() {
                    try {
                        bx.this.nG.onAdLoaded();
                    } catch (RemoteException e) {
                        dw.c("Could not call onAdLoaded.", e);
                    }
                }
            });
        }
    }

    public void onReceivedAd(MediationInterstitialAdapter<?, ?> adapter) {
        dw.v("Adapter called onReceivedAd.");
        if (dv.bD()) {
            try {
                this.nG.onAdLoaded();
            } catch (RemoteException e) {
                dw.c("Could not call onAdLoaded.", e);
            }
        } else {
            dw.z("onReceivedAd must be called on the main UI thread.");
            dv.rp.post(new Runnable() {
                public void run() {
                    try {
                        bx.this.nG.onAdLoaded();
                    } catch (RemoteException e) {
                        dw.c("Could not call onAdLoaded.", e);
                    }
                }
            });
        }
    }
}