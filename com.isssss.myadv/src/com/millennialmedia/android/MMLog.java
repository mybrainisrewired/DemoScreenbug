package com.millennialmedia.android;

import android.util.Log;

public class MMLog {

    static interface LogHandler {
        void handleLog(String str);
    }

    static class LoggingComponent {
        private static final String TAG_STARTER = "MMSDK-";
        private static int logLevel;
        private LogHandler registeredLogHandler;

        static {
            logLevel = 4;
        }

        LoggingComponent() {
        }

        private void callLogHandler(String message) {
            if (this.registeredLogHandler != null) {
                this.registeredLogHandler.handleLog(message);
            }
        }

        private void dInternal(String tag, String message) {
            Log.d(TAG_STARTER + tag, message);
            callLogHandler(message);
        }

        private void eInternal(String tag, String message) {
            Log.e(TAG_STARTER + tag, message);
            callLogHandler(message);
        }

        private void iInternal(String tag, String message) {
            Log.i(TAG_STARTER + tag, message);
            callLogHandler(message);
        }

        private void vInternal(String tag, String message) {
            Log.v(TAG_STARTER + tag, message);
            callLogHandler(message);
        }

        private void wInternal(String tag, String message) {
            Log.w(TAG_STARTER + tag, message);
            callLogHandler(message);
        }

        void d(String classTag, String logMessage) {
            if (logLevel <= 3) {
                dInternal(classTag, logMessage);
            }
        }

        void d(String classTag, String logMessage, Throwable throwable) {
            if (logLevel <= 3) {
                dInternal(classTag, logMessage + ": " + Log.getStackTraceString(throwable));
            }
        }

        void e(String classTag, String logMessage) {
            if (logLevel <= 6) {
                eInternal(classTag, logMessage);
            }
        }

        void e(String classTag, String logMessage, Throwable throwable) {
            if (logLevel <= 6) {
                eInternal(classTag, logMessage + ": " + Log.getStackTraceString(throwable));
            }
        }

        public int getLogLevel() {
            return logLevel;
        }

        void i(String classTag, String logMessage) {
            if (logLevel <= 4) {
                iInternal(classTag, logMessage);
            }
        }

        void i(String classTag, String logMessage, Throwable throwable) {
            if (logLevel <= 4) {
                iInternal(classTag, logMessage + ": " + Log.getStackTraceString(throwable));
            }
        }

        void registerLogHandler(LogHandler logHandler) {
            this.registeredLogHandler = logHandler;
        }

        public void setLogLevel(int level) {
            logLevel = level;
        }

        void v(String classTag, String logMessage) {
            if (logLevel <= 2) {
                vInternal(classTag, logMessage);
            }
        }

        void v(String classTag, String logMessage, Throwable throwable) {
            if (logLevel <= 2) {
                vInternal(classTag, logMessage + ": " + Log.getStackTraceString(throwable));
            }
        }

        void w(String classTag, String logMessage) {
            if (logLevel <= 5) {
                wInternal(classTag, logMessage);
            }
        }

        void w(String classTag, String logMessage, Throwable throwable) {
            if (logLevel <= 5) {
                wInternal(classTag, logMessage + ": " + Log.getStackTraceString(throwable));
            }
        }
    }

    static {
        ComponentRegistry.addLoggingComponent(new LoggingComponent());
    }

    static void d(String classTag, String logMessage) {
        ComponentRegistry.getLoggingComponent().d(classTag, logMessage);
    }

    static void d(String classTag, String logMessage, Throwable throwable) {
        ComponentRegistry.getLoggingComponent().d(classTag, logMessage, throwable);
    }

    static void e(String classTag, String logMessage) {
        ComponentRegistry.getLoggingComponent().e(classTag, logMessage);
    }

    static void e(String classTag, String logMessage, Throwable throwable) {
        ComponentRegistry.getLoggingComponent().e(classTag, logMessage, throwable);
    }

    public static int getLogLevel() {
        return ComponentRegistry.getLoggingComponent().getLogLevel();
    }

    static void i(String classTag, String logMessage) {
        ComponentRegistry.getLoggingComponent().i(classTag, logMessage);
    }

    static void i(String classTag, String logMessage, Throwable throwable) {
        ComponentRegistry.getLoggingComponent().i(classTag, logMessage, throwable);
    }

    static void registerLogHandler(LogHandler logHandler) {
        ComponentRegistry.getLoggingComponent().registerLogHandler(logHandler);
    }

    public static void setLogLevel(int level) {
        ComponentRegistry.getLoggingComponent().setLogLevel(level);
    }

    static void v(String classTag, String logMessage) {
        ComponentRegistry.getLoggingComponent().v(classTag, logMessage);
    }

    static void v(String classTag, String logMessage, Throwable throwable) {
        ComponentRegistry.getLoggingComponent().v(classTag, logMessage, throwable);
    }

    static void w(String classTag, String logMessage) {
        ComponentRegistry.getLoggingComponent().w(classTag, logMessage);
    }

    static void w(String classTag, String logMessage, Throwable throwable) {
        ComponentRegistry.getLoggingComponent().w(classTag, logMessage, throwable);
    }
}