package com.mopub.mobileads;

import java.util.Map;

class MraidCommandSetResizeProperties extends MraidCommand {
    MraidCommandSetResizeProperties(Map<String, String> params, MraidView view) {
        super(params, view);
    }

    void execute() {
        this.mView.fireErrorEvent(MraidJavascriptCommand.SET_RESIZE_PROPERTIES, "Unsupported action setResizeProperties.");
    }
}