package com.loopme;

public enum LogLevel {
    INFO(1),
    DEBUG(2),
    ERROR(3);
    int mLevel;

    static {
        INFO = new LogLevel("INFO", 0, 1);
        DEBUG = new LogLevel("DEBUG", 1, 2);
        ERROR = new LogLevel("ERROR", 2, 3);
        ENUM$VALUES = new LogLevel[]{INFO, DEBUG, ERROR};
    }

    private LogLevel(int level) {
        this.mLevel = level;
    }
}