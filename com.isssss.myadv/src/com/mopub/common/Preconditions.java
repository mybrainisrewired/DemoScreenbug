package com.mopub.common;

import android.os.Looper;
import com.mopub.common.logging.MoPubLog;
import java.util.IllegalFormatException;

public final class Preconditions {
    public static final String EMPTY_ARGUMENTS = "";

    public static final class NoThrow {
        private static volatile boolean sStrictMode;

        static {
            sStrictMode = false;
        }

        public static boolean checkArgument(boolean expression) {
            return Preconditions.checkArgumentInternal(expression, sStrictMode, "Illegal argument", new Object[]{EMPTY_ARGUMENTS});
        }

        public static boolean checkArgument(boolean expression, String errorMessage) {
            return Preconditions.checkArgumentInternal(expression, sStrictMode, errorMessage, new Object[]{EMPTY_ARGUMENTS});
        }

        public static boolean checkArgument(boolean expression, String errorMessageTemplate, Object... errorMessageArgs) {
            return Preconditions.checkArgumentInternal(expression, sStrictMode, errorMessageTemplate, errorMessageArgs);
        }

        public static boolean checkNotNull(Object reference) {
            return Preconditions.checkNotNullInternal(reference, sStrictMode, "Object can not be null.", new Object[]{EMPTY_ARGUMENTS});
        }

        public static boolean checkNotNull(Object reference, String errorMessage) {
            return Preconditions.checkNotNullInternal(reference, sStrictMode, errorMessage, new Object[]{EMPTY_ARGUMENTS});
        }

        public static boolean checkNotNull(Object reference, String errorMessageTemplate, Object... errorMessageArgs) {
            return Preconditions.checkNotNullInternal(reference, sStrictMode, errorMessageTemplate, errorMessageArgs);
        }

        public static boolean checkState(boolean expression) {
            return Preconditions.checkStateInternal(expression, sStrictMode, "Illegal state.", new Object[]{EMPTY_ARGUMENTS});
        }

        public static boolean checkState(boolean expression, String errorMessage) {
            return Preconditions.checkStateInternal(expression, sStrictMode, errorMessage, new Object[]{EMPTY_ARGUMENTS});
        }

        public static boolean checkState(boolean expression, String errorMessageTemplate, Object... errorMessageArgs) {
            return Preconditions.checkStateInternal(expression, sStrictMode, errorMessageTemplate, errorMessageArgs);
        }

        public static boolean checkUiThread() {
            return Preconditions.checkUiThreadInternal(sStrictMode, "This method must be called from the UI thread.", new Object[]{EMPTY_ARGUMENTS});
        }

        public static boolean checkUiThread(String errorMessage) {
            return Preconditions.checkUiThreadInternal(sStrictMode, errorMessage, new Object[]{EMPTY_ARGUMENTS});
        }

        public static boolean checkUiThread(String errorMessageTemplate, Object... errorMessageArgs) {
            return Preconditions.checkUiThreadInternal(false, errorMessageTemplate, errorMessageArgs);
        }

        public static void setStrictMode(boolean strictMode) {
            sStrictMode = strictMode;
        }
    }

    private Preconditions() {
    }

    public static void checkArgument(boolean expression) {
        checkArgumentInternal(expression, true, "Illegal argument.", new Object[]{EMPTY_ARGUMENTS});
    }

    public static void checkArgument(boolean expression, String errorMessage) {
        checkArgumentInternal(expression, true, errorMessage, new Object[]{EMPTY_ARGUMENTS});
    }

    public static void checkArgument(boolean expression, String errorMessageTemplate, Object... errorMessageArgs) {
        checkArgumentInternal(expression, true, errorMessageTemplate, errorMessageArgs);
    }

    private static boolean checkArgumentInternal(boolean expression, boolean allowThrow, String errorMessageTemplate, Object... errorMessageArgs) {
        if (expression) {
            return true;
        }
        String errorMessage = format(errorMessageTemplate, errorMessageArgs);
        if (allowThrow) {
            throw new IllegalArgumentException(errorMessage);
        }
        MoPubLog.e(errorMessage);
        return false;
    }

    public static void checkNotNull(Object reference) {
        checkNotNullInternal(reference, true, "Object can not be null.", new Object[]{EMPTY_ARGUMENTS});
    }

    public static void checkNotNull(Object reference, String errorMessage) {
        checkNotNullInternal(reference, true, errorMessage, new Object[]{EMPTY_ARGUMENTS});
    }

    public static void checkNotNull(Object reference, String errorMessageTemplate, Object... errorMessageArgs) {
        checkNotNullInternal(reference, true, errorMessageTemplate, errorMessageArgs);
    }

    private static boolean checkNotNullInternal(Object reference, boolean allowThrow, String errorMessageTemplate, Object... errorMessageArgs) {
        if (reference != null) {
            return true;
        }
        String errorMessage = format(errorMessageTemplate, errorMessageArgs);
        if (allowThrow) {
            throw new NullPointerException(errorMessage);
        }
        MoPubLog.e(errorMessage);
        return false;
    }

    public static void checkState(boolean expression) {
        checkStateInternal(expression, true, "Illegal state.", new Object[]{EMPTY_ARGUMENTS});
    }

    public static void checkState(boolean expression, String errorMessage) {
        checkStateInternal(expression, true, errorMessage, new Object[]{EMPTY_ARGUMENTS});
    }

    public static void checkState(boolean expression, String errorMessageTemplate, Object... errorMessageArgs) {
        checkStateInternal(expression, true, errorMessageTemplate, errorMessageArgs);
    }

    private static boolean checkStateInternal(boolean expression, boolean allowThrow, String errorMessageTemplate, Object... errorMessageArgs) {
        if (expression) {
            return true;
        }
        String errorMessage = format(errorMessageTemplate, errorMessageArgs);
        if (allowThrow) {
            throw new IllegalStateException(errorMessage);
        }
        MoPubLog.e(errorMessage);
        return false;
    }

    public static void checkUiThread() {
        checkUiThreadInternal(true, "This method must be called from the UI thread.", new Object[]{EMPTY_ARGUMENTS});
    }

    public static void checkUiThread(String errorMessage) {
        checkUiThreadInternal(true, errorMessage, new Object[]{EMPTY_ARGUMENTS});
    }

    public static void checkUiThread(String errorMessageTemplate, Object... errorMessageArgs) {
        checkUiThreadInternal(true, errorMessageTemplate, errorMessageArgs);
    }

    private static boolean checkUiThreadInternal(boolean allowThrow, String errorMessageTemplate, Object... errorMessageArgs) {
        if (Looper.getMainLooper().equals(Looper.myLooper())) {
            return true;
        }
        String errorMessage = format(errorMessageTemplate, errorMessageArgs);
        if (allowThrow) {
            throw new IllegalStateException(errorMessage);
        }
        MoPubLog.e(errorMessage);
        return false;
    }

    private static String format(String template, Object... args) {
        template = String.valueOf(template);
        try {
            return String.format(template, args);
        } catch (IllegalFormatException e) {
            MoPubLog.e(new StringBuilder("MoPub preconditions had a format exception: ").append(e.getMessage()).toString());
            return template;
        }
    }
}