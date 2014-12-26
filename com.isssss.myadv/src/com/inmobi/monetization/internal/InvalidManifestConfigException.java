package com.inmobi.monetization.internal;

public final class InvalidManifestConfigException extends Exception {
    public static final int MISSING_ACCESS_NETWORK_STATE_PERMISSION = -9;
    public static final int MISSING_ACTIVITY_DECLARATION = -2;
    public static final int MISSING_CONFIG_CHANGES = -3;
    public static final int MISSING_CONFIG_KEYBOARD = -4;
    public static final int MISSING_CONFIG_KEYBOARDHIDDEN = -5;
    public static final int MISSING_CONFIG_ORIENTATION = -6;
    public static final int MISSING_CONFIG_SCREENSIZE = -7;
    public static final int MISSING_CONFIG_SMALLEST_SCREENSIZE = -8;
    public static final int MISSING_INTERNET_PERMISSION = -1;
    private static final long serialVersionUID = 1;
    private int a;

    InvalidManifestConfigException(int i) {
        this.a = i;
    }

    public int getExceptionCode() {
        return this.a;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("IMConfigException : ");
        switch (this.a) {
            case MISSING_ACCESS_NETWORK_STATE_PERMISSION:
                stringBuffer.append(InvalidManifestErrorMessages.MSG_MISSING_ACCESS_NETWORK_PERMISSION);
                break;
            case MISSING_CONFIG_SMALLEST_SCREENSIZE:
                stringBuffer.append(InvalidManifestErrorMessages.MSG_MISSING_CONFIG_SMALLEST_SCREENSIZE);
                break;
            case MISSING_CONFIG_SCREENSIZE:
                stringBuffer.append(InvalidManifestErrorMessages.MSG_MISSING_CONFIG_SCREENSIZE);
                break;
            case MISSING_CONFIG_ORIENTATION:
                stringBuffer.append(InvalidManifestErrorMessages.MSG_MISSING_CONFIG_ORIENTATION);
                break;
            case MISSING_CONFIG_KEYBOARDHIDDEN:
                stringBuffer.append(InvalidManifestErrorMessages.MSG_MISSING_CONFIG_KEYBOARDHIDDEN);
                break;
            case MISSING_CONFIG_KEYBOARD:
                stringBuffer.append(InvalidManifestErrorMessages.MSG_MISSING_CONFIG_KEYBOARD);
                break;
            case MISSING_CONFIG_CHANGES:
                stringBuffer.append(InvalidManifestErrorMessages.MSG_MISSING_CONFIG_CHANGES);
                break;
            case MISSING_ACTIVITY_DECLARATION:
                stringBuffer.append(InvalidManifestErrorMessages.MSG_MISSING_ACTIVITY_DECLARATION);
                break;
            case MISSING_INTERNET_PERMISSION:
                stringBuffer.append(InvalidManifestErrorMessages.MSG_MISSING_INTERNET_PERMISSION);
                break;
        }
        return stringBuffer.toString();
    }
}