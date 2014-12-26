package com.mopub.mobileads;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import com.mopub.mobileads.CustomEventInterstitial.CustomEventInterstitialListener;

class EventForwardingBroadcastReceiver extends BroadcastReceiver {
    static final String ACTION_INTERSTITIAL_CLICK = "com.mopub.action.interstitial.click";
    static final String ACTION_INTERSTITIAL_DISMISS = "com.mopub.action.interstitial.dismiss";
    static final String ACTION_INTERSTITIAL_FAIL = "com.mopub.action.interstitial.fail";
    static final String ACTION_INTERSTITIAL_SHOW = "com.mopub.action.interstitial.show";
    static final String BROADCAST_IDENTIFIER_KEY = "broadcastIdentifier";
    private static IntentFilter sIntentFilter;
    private final long mBroadcastIdentifier;
    private Context mContext;
    private final CustomEventInterstitialListener mCustomEventInterstitialListener;

    public EventForwardingBroadcastReceiver(CustomEventInterstitialListener customEventInterstitialListener, long broadcastIdentifier) {
        this.mCustomEventInterstitialListener = customEventInterstitialListener;
        this.mBroadcastIdentifier = broadcastIdentifier;
        sIntentFilter = getHtmlInterstitialIntentFilter();
    }

    static void broadcastAction(Context context, long broadcastIdentifier, String action) {
        Intent intent = new Intent(action);
        intent.putExtra(BROADCAST_IDENTIFIER_KEY, broadcastIdentifier);
        LocalBroadcastManager.getInstance(context.getApplicationContext()).sendBroadcast(intent);
    }

    static IntentFilter getHtmlInterstitialIntentFilter() {
        if (sIntentFilter == null) {
            sIntentFilter = new IntentFilter();
            sIntentFilter.addAction(ACTION_INTERSTITIAL_FAIL);
            sIntentFilter.addAction(ACTION_INTERSTITIAL_SHOW);
            sIntentFilter.addAction(ACTION_INTERSTITIAL_DISMISS);
            sIntentFilter.addAction(ACTION_INTERSTITIAL_CLICK);
        }
        return sIntentFilter;
    }

    public void onReceive(Context context, Intent intent) {
        if (this.mCustomEventInterstitialListener != null) {
            if (this.mBroadcastIdentifier == intent.getLongExtra(BROADCAST_IDENTIFIER_KEY, -1)) {
                String action = intent.getAction();
                if (ACTION_INTERSTITIAL_FAIL.equals(action)) {
                    this.mCustomEventInterstitialListener.onInterstitialFailed(MoPubErrorCode.NETWORK_INVALID_STATE);
                } else if (ACTION_INTERSTITIAL_SHOW.equals(action)) {
                    this.mCustomEventInterstitialListener.onInterstitialShown();
                } else if (ACTION_INTERSTITIAL_DISMISS.equals(action)) {
                    this.mCustomEventInterstitialListener.onInterstitialDismissed();
                    unregister();
                } else if (ACTION_INTERSTITIAL_CLICK.equals(action)) {
                    this.mCustomEventInterstitialListener.onInterstitialClicked();
                }
            }
        }
    }

    public void register(Context context) {
        this.mContext = context;
        LocalBroadcastManager.getInstance(this.mContext).registerReceiver(this, sIntentFilter);
    }

    public void unregister() {
        if (this.mContext != null) {
            LocalBroadcastManager.getInstance(this.mContext).unregisterReceiver(this);
            this.mContext = null;
        }
    }
}