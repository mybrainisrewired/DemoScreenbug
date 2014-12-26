package com.mopub.mobileads;

import com.mopub.common.Preconditions;
import com.mopub.common.logging.MoPubLog;
import com.mopub.mobileads.MraidView.PlacementType;
import java.util.Map;

class MraidCommandStorePicture extends MraidCommand {
    public static final String MIME_TYPE_HEADER = "Content-Type";

    public MraidCommandStorePicture(Map<String, String> params, MraidView view) {
        super(params, view);
    }

    void execute() {
        String url = getStringFromParamsForKey("uri");
        if (url == null || url.equals(Preconditions.EMPTY_ARGUMENTS)) {
            this.mView.fireErrorEvent(MraidJavascriptCommand.STORE_PICTURE, "Image can't be stored with null or empty URL");
            MoPubLog.d("Invalid URI for Mraid Store Picture.");
        } else {
            this.mView.getDisplayController().showUserDownloadImageAlert(url);
        }
    }

    protected boolean isCommandDependentOnUserClick(PlacementType placementType) {
        return true;
    }
}