package com.mopub.mobileads;

import com.google.android.gms.plus.PlusShare;
import com.mopub.mobileads.MraidView.PlacementType;
import java.util.Map;

class MraidCommandOpen extends MraidCommand {
    MraidCommandOpen(Map<String, String> params, MraidView view) {
        super(params, view);
    }

    void execute() {
        String url = getStringFromParamsForKey(PlusShare.KEY_CALL_TO_ACTION_URL);
        if (url == null) {
            this.mView.fireErrorEvent(MraidJavascriptCommand.OPEN, "Url can not be null.");
        } else {
            this.mView.getBrowserController().open(url);
        }
    }

    protected boolean isCommandDependentOnUserClick(PlacementType placementType) {
        return true;
    }
}