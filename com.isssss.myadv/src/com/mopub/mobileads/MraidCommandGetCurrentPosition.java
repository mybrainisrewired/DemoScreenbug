package com.mopub.mobileads;

import java.util.Map;

class MraidCommandGetCurrentPosition extends MraidCommand {
    MraidCommandGetCurrentPosition(Map<String, String> params, MraidView view) {
        super(params, view);
    }

    void execute() {
        this.mView.getDisplayController().getCurrentPosition();
    }
}