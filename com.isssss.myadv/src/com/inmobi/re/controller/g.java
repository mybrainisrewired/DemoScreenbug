package com.inmobi.re.controller;

import com.inmobi.re.container.mraidimpl.AudioTriggerCallback;

// compiled from: JSUtilityController.java
class g implements AudioTriggerCallback {
    final /* synthetic */ JSUtilityController a;

    g(JSUtilityController jSUtilityController) {
        this.a = jSUtilityController;
    }

    public void audioLevel(double d) {
        this.a.imWebView.raiseMicEvent(d);
    }
}