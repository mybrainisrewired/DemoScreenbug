package com.mopub.mobileads;

import java.util.Map;

class MraidCommandUseCustomClose extends MraidCommand {
    MraidCommandUseCustomClose(Map<String, String> params, MraidView view) {
        super(params, view);
    }

    void execute() {
        this.mView.getDisplayController().useCustomClose(getBooleanFromParamsForKey("shouldUseCustomClose"));
    }
}