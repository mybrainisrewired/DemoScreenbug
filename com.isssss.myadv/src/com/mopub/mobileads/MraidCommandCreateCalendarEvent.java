package com.mopub.mobileads;

import com.mopub.mobileads.MraidView.PlacementType;
import java.util.Map;

class MraidCommandCreateCalendarEvent extends MraidCommand {
    MraidCommandCreateCalendarEvent(Map<String, String> params, MraidView view) {
        super(params, view);
    }

    void execute() {
        this.mView.getDisplayController().createCalendarEvent(this.mParams);
    }

    protected boolean isCommandDependentOnUserClick(PlacementType placementType) {
        return true;
    }
}