package com.loopme;

public class LoopMeError {
    private static final String LOG_TAG;
    private final String mMessage;

    static {
        LOG_TAG = LoopMeError.class.getSimpleName();
    }

    public LoopMeError(String mes) {
        this.mMessage = mes;
        if (mes == null) {
            Utilities.log(LOG_TAG, "Wrong parameter", LogLevel.ERROR);
        }
    }

    public String getMessage() {
        return this.mMessage;
    }
}