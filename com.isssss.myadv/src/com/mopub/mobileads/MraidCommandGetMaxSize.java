package com.mopub.mobileads;

import java.util.Map;

class MraidCommandGetMaxSize extends MraidCommand {
    MraidCommandGetMaxSize(Map<String, String> params, MraidView view) {
        super(params, view);
    }

    void execute() {
        this.mView.getDisplayController().getMaxSize();
    }
}