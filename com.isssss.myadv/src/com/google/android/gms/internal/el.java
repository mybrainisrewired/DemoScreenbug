package com.google.android.gms.internal;

public enum el {
    SUCCESS("Ok"),
    BAD_AUTHENTICATION("BadAuthentication"),
    NEEDS_2F("InvalidSecondFactor"),
    NOT_VERIFIED("NotVerified"),
    TERMS_NOT_AGREED("TermsNotAgreed"),
    UNKNOWN("Unknown"),
    UNKNOWN_ERROR("UNKNOWN_ERR"),
    ACCOUNT_DELETED("AccountDeleted"),
    ACCOUNT_DISABLED("AccountDisabled"),
    SERVICE_DISABLED("ServiceDisabled"),
    SERVICE_UNAVAILABLE("ServiceUnavailable"),
    CAPTCHA("CaptchaRequired"),
    NETWORK_ERROR("NetworkError"),
    USER_CANCEL("UserCancel"),
    PERMISSION_DENIED("PermissionDenied"),
    DEVICE_MANAGEMENT_REQUIRED("DeviceManagementRequiredOrSyncDisabled"),
    CLIENT_LOGIN_DISABLED("ClientLoginDisabled"),
    NEED_PERMISSION("NeedPermission"),
    BAD_PASSWORD("WeakPassword"),
    ALREADY_HAS_GMAIL("ALREADY_HAS_GMAIL"),
    BAD_REQUEST("BadRequest"),
    BAD_USERNAME("BadUsername"),
    LOGIN_FAIL("LoginFail"),
    NOT_LOGGED_IN("NotLoggedIn"),
    NO_GMAIL("NoGmail"),
    REQUEST_DENIED("RequestDenied"),
    SERVER_ERROR("ServerError"),
    USERNAME_UNAVAILABLE("UsernameUnavailable"),
    DELETED_GMAIL("DeletedGmail"),
    SOCKET_TIMEOUT("SocketTimeout"),
    EXISTING_USERNAME("ExistingUsername"),
    NEEDS_BROWSER("NeedsBrowser"),
    GPLUS_OTHER("GPlusOther"),
    GPLUS_NICKNAME("GPlusNickname"),
    GPLUS_INVALID_CHAR("GPlusInvalidChar"),
    GPLUS_INTERSTITIAL("GPlusInterstitial"),
    GPLUS_PROFILE_ERROR("ProfileUpgradeError"),
    INVALID_SCOPE("INVALID_SCOPE");
    public static String xD;
    public static String xE;
    private final String xF;

    static {
        String str = "Ok";
        wR = new el("SUCCESS", 0, "Ok");
        str = "BadAuthentication";
        wS = new el("BAD_AUTHENTICATION", 1, "BadAuthentication");
        str = "InvalidSecondFactor";
        wT = new el("NEEDS_2F", 2, "InvalidSecondFactor");
        str = "NotVerified";
        wU = new el("NOT_VERIFIED", 3, "NotVerified");
        str = "TermsNotAgreed";
        wV = new el("TERMS_NOT_AGREED", 4, "TermsNotAgreed");
        String str2 = "Unknown";
        wW = new el("UNKNOWN", 5, "Unknown");
        str2 = "UNKNOWN_ERR";
        wX = new el("UNKNOWN_ERROR", 6, "UNKNOWN_ERR");
        str2 = "AccountDeleted";
        wY = new el("ACCOUNT_DELETED", 7, "AccountDeleted");
        str2 = "AccountDisabled";
        wZ = new el("ACCOUNT_DISABLED", 8, "AccountDisabled");
        str2 = "ServiceDisabled";
        xa = new el("SERVICE_DISABLED", 9, "ServiceDisabled");
        str2 = "ServiceUnavailable";
        xb = new el("SERVICE_UNAVAILABLE", 10, "ServiceUnavailable");
        str2 = "CaptchaRequired";
        xc = new el("CAPTCHA", 11, "CaptchaRequired");
        str2 = "NetworkError";
        xd = new el("NETWORK_ERROR", 12, "NetworkError");
        str2 = "UserCancel";
        xe = new el("USER_CANCEL", 13, "UserCancel");
        str2 = "PermissionDenied";
        xf = new el("PERMISSION_DENIED", 14, "PermissionDenied");
        str2 = "DeviceManagementRequiredOrSyncDisabled";
        xg = new el("DEVICE_MANAGEMENT_REQUIRED", 15, "DeviceManagementRequiredOrSyncDisabled");
        str2 = "ClientLoginDisabled";
        xh = new el("CLIENT_LOGIN_DISABLED", 16, "ClientLoginDisabled");
        str2 = "NeedPermission";
        xi = new el("NEED_PERMISSION", 17, "NeedPermission");
        str2 = "WeakPassword";
        xj = new el("BAD_PASSWORD", 18, "WeakPassword");
        str2 = "ALREADY_HAS_GMAIL";
        xk = new el("ALREADY_HAS_GMAIL", 19, "ALREADY_HAS_GMAIL");
        str2 = "BadRequest";
        xl = new el("BAD_REQUEST", 20, "BadRequest");
        str2 = "BadUsername";
        xm = new el("BAD_USERNAME", 21, "BadUsername");
        str2 = "LoginFail";
        xn = new el("LOGIN_FAIL", 22, "LoginFail");
        str2 = "NotLoggedIn";
        xo = new el("NOT_LOGGED_IN", 23, "NotLoggedIn");
        str2 = "NoGmail";
        xp = new el("NO_GMAIL", 24, "NoGmail");
        str2 = "RequestDenied";
        xq = new el("REQUEST_DENIED", 25, "RequestDenied");
        str2 = "ServerError";
        xr = new el("SERVER_ERROR", 26, "ServerError");
        str2 = "UsernameUnavailable";
        xs = new el("USERNAME_UNAVAILABLE", 27, "UsernameUnavailable");
        str2 = "DeletedGmail";
        xt = new el("DELETED_GMAIL", 28, "DeletedGmail");
        str2 = "SocketTimeout";
        xu = new el("SOCKET_TIMEOUT", 29, "SocketTimeout");
        str2 = "ExistingUsername";
        xv = new el("EXISTING_USERNAME", 30, "ExistingUsername");
        str2 = "NeedsBrowser";
        xw = new el("NEEDS_BROWSER", 31, "NeedsBrowser");
        str2 = "GPlusOther";
        xx = new el("GPLUS_OTHER", 32, "GPlusOther");
        str2 = "GPlusNickname";
        xy = new el("GPLUS_NICKNAME", 33, "GPlusNickname");
        str2 = "GPlusInvalidChar";
        xz = new el("GPLUS_INVALID_CHAR", 34, "GPlusInvalidChar");
        str2 = "GPlusInterstitial";
        xA = new el("GPLUS_INTERSTITIAL", 35, "GPlusInterstitial");
        str2 = "ProfileUpgradeError";
        xB = new el("GPLUS_PROFILE_ERROR", 36, "ProfileUpgradeError");
        str2 = "INVALID_SCOPE";
        xC = new el("INVALID_SCOPE", 37, "INVALID_SCOPE");
        xG = new el[]{wR, wS, wT, wU, wV, wW, wX, wY, wZ, xa, xb, xc, xd, xe, xf, xg, xh, xi, xj, xk, xl, xm, xn, xo, xp, xq, xr, xs, xt, xu, xv, xw, xx, xy, xz, xA, xB, xC};
        xD = "Error";
        xE = "status";
    }

    private el(String str) {
        this.xF = str;
    }
}