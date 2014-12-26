package com.loopme;

class BaseLoopMeHolder {
    private static final String LOG_TAG;
    private static BaseLoopMe mBaseLoopMe;

    static {
        LOG_TAG = BaseLoopMeHolder.class.getSimpleName();
    }

    BaseLoopMeHolder() {
    }

    public static BaseLoopMe get() {
        return mBaseLoopMe;
    }

    public static void put(BaseLoopMe baseLoopme) {
        if (baseLoopme == null) {
            Utilities.log(LOG_TAG, "Wrong parameter", LogLevel.ERROR);
        } else {
            mBaseLoopMe = baseLoopme;
        }
    }
}