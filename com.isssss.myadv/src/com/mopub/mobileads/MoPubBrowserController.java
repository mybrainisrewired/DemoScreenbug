package com.mopub.mobileads;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import com.google.android.gms.drive.DriveFile;
import com.mopub.common.MoPubBrowser;
import com.mopub.common.util.IntentUtils;
import com.mopub.mobileads.util.Utils;

class MoPubBrowserController extends MraidAbstractController {
    private static final String LOGTAG = "MoPubBrowserController";
    private Context mContext;

    MoPubBrowserController(MraidView view) {
        super(view);
        this.mContext = view.getContext();
    }

    private boolean isWebSiteUrl(String url) {
        return url.startsWith("http://") || url.startsWith("https://");
    }

    private boolean launchApplicationUrl(String url) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
        intent.addFlags(DriveFile.MODE_READ_ONLY);
        return Utils.executeIntent(getMraidView().getContext(), intent, "Unable to open intent.");
    }

    protected void open(String url) {
        Log.d(LOGTAG, new StringBuilder("Opening url: ").append(url).toString());
        MraidView mraidView = getMraidView();
        if (mraidView.getMraidListener() != null) {
            mraidView.getMraidListener().onOpen(mraidView);
        }
        if (isWebSiteUrl(url) || !IntentUtils.canHandleApplicationUrl(this.mContext, url)) {
            Intent i = new Intent(this.mContext, MoPubBrowser.class);
            i.putExtra(MoPubBrowser.DESTINATION_URL_KEY, url);
            i.addFlags(DriveFile.MODE_READ_ONLY);
            this.mContext.startActivity(i);
        } else {
            launchApplicationUrl(url);
        }
    }
}