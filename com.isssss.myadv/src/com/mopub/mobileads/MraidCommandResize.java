package com.mopub.mobileads;

import java.util.Map;

class MraidCommandResize extends MraidCommand {
    MraidCommandResize(Map<String, String> params, MraidView view) {
        super(params, view);
    }

    void execute() {
        this.mView.fireErrorEvent(MraidJavascriptCommand.RESIZE, "Unsupported action resize.");
    }
}