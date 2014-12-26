package com.clouds.http;

public class CloudsServerHttpConstant {
    public static final int CHECK_SERVER_TIMER = 5;
    public static final int DOWNLOAD_ALONE_DATA = 3;
    public static final int DOWNLOAD_FINISH_DATA = 4;
    public static String PUSH_SERVER_BACKUP_URL = null;
    public static String PUSH_SERVER_HTTP_URL = null;
    public static String PUSH_SERVER_URL = null;
    public static final int SERVER_CONNECTION_FAIL = 2;
    public static final int SERVER_CONNECTION_SUCCESS = 1;
    public static final int UPDATA_WALLPAPER = 6;

    static {
        PUSH_SERVER_URL = "https://www.cloudsota.com/push/";
        PUSH_SERVER_HTTP_URL = "http://www.cloudsota.com/push/";
        PUSH_SERVER_BACKUP_URL = "https://www.cloudsota.com/push/";
    }
}