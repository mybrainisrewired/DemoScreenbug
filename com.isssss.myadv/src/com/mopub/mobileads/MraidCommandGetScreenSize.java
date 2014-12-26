package com.mopub.mobileads;

import java.util.Map;

class MraidCommandGetScreenSize extends MraidCommand {
    MraidCommandGetScreenSize(Map<String, String> params, MraidView view) {
        super(params, view);
    }

    void execute() {
        this.mView.getDisplayController().getScreenSize();
    }
}