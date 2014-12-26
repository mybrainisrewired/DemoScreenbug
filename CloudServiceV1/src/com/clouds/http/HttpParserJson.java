package com.clouds.http;

import com.clouds.debug.SystemDebug;
import com.clouds.object.JSONFileInfo;
import com.clouds.object.NotificationInfo;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class HttpParserJson {
    private static final int SERVER_STATUS_BUSY = 3;
    private static final int SERVER_STATUS_NO = 2;
    private static final int SERVER_STATUS_OK = 1;
    private static final String TAG;
    public static final int defineTimerTime = 180;
    private static JSONObject jsonObject;

    public class Update {
        public boolean isApp;
        public boolean isLogo;
        public boolean isScreenshots;
        public boolean isUrl;
        public boolean isWall;
        public boolean isclient;
    }

    public class Verinfo {
        public int appsver;
        public int bhurlver;
        public int clientver;
        public int logover;
        public int screenshotsver;
        public int wallppver;
    }

    static {
        TAG = HttpParserJson.class.getSimpleName();
    }

    public HttpParserJson(String servInfo) {
        jsonObject = getJSONObject(servInfo);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.util.List<com.clouds.object.JSONFileInfo> getAppFileInfo(org.json.JSONObject r21_jsonObject, java.lang.String r22_key) {
        throw new UnsupportedOperationException("Method not decompiled: com.clouds.http.HttpParserJson.getAppFileInfo(org.json.JSONObject, java.lang.String):java.util.List<com.clouds.object.JSONFileInfo>");
        /*
        r20 = this;
        r17 = TAG;
        r18 = new java.lang.StringBuilder;
        r19 = "getFileInfo key: ";
        r18.<init>(r19);
        r0 = r18;
        r1 = r22;
        r18 = r0.append(r1);
        r18 = r18.toString();
        com.clouds.debug.SystemDebug.e(r17, r18);
        r11 = 0;
        if (r21 == 0) goto L_0x002e;
    L_0x001b:
        r12 = new java.util.ArrayList;	 Catch:{ JSONException -> 0x0082 }
        r12.<init>();	 Catch:{ JSONException -> 0x0082 }
        r15 = r21.getJSONArray(r22);	 Catch:{ JSONException -> 0x0070 }
        r14 = 0;
    L_0x0025:
        r17 = r15.length();	 Catch:{ JSONException -> 0x0070 }
        r0 = r17;
        if (r14 < r0) goto L_0x0030;
    L_0x002d:
        r11 = r12;
    L_0x002e:
        r12 = r11;
    L_0x002f:
        return r12;
    L_0x0030:
        r16 = r15.optJSONObject(r14);	 Catch:{ JSONException -> 0x0070 }
        r17 = "sha256";
        r3 = r16.getString(r17);	 Catch:{ JSONException -> 0x0070 }
        r17 = "url";
        r4 = r16.getString(r17);	 Catch:{ JSONException -> 0x0070 }
        r17 = "size";
        r10 = r16.getInt(r17);	 Catch:{ JSONException -> 0x0070 }
        r7 = "";
        r8 = "";
        r9 = "";
        r5 = 0;
        r6 = 0;
        r17 = "type";
        r5 = r16.getString(r17);	 Catch:{ Exception -> 0x006b }
    L_0x0054:
        r17 = "package";
        r8 = r16.getString(r17);	 Catch:{ Exception -> 0x0078 }
    L_0x005a:
        r17 = "class";
        r9 = r16.getString(r17);	 Catch:{ Exception -> 0x007d }
    L_0x0060:
        r2 = new com.clouds.object.JSONFileInfo;	 Catch:{ JSONException -> 0x0070 }
        r2.<init>(r3, r4, r5, r6, r7, r8, r9, r10);	 Catch:{ JSONException -> 0x0070 }
        r12.add(r2);	 Catch:{ JSONException -> 0x0070 }
        r14 = r14 + 1;
        goto L_0x0025;
    L_0x006b:
        r13 = move-exception;
        r13.printStackTrace();	 Catch:{ JSONException -> 0x0070 }
        goto L_0x0054;
    L_0x0070:
        r13 = move-exception;
        r11 = r12;
    L_0x0072:
        r13.printStackTrace();
        r11 = 0;
        r12 = r11;
        goto L_0x002f;
    L_0x0078:
        r13 = move-exception;
        r13.printStackTrace();	 Catch:{ JSONException -> 0x0070 }
        goto L_0x005a;
    L_0x007d:
        r13 = move-exception;
        r13.printStackTrace();	 Catch:{ JSONException -> 0x0070 }
        goto L_0x0060;
    L_0x0082:
        r13 = move-exception;
        goto L_0x0072;
        */
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.util.List<com.clouds.object.JSONFileInfo> getCertificateInfo(org.json.JSONObject r14_jsonObject, java.lang.String r15_key) {
        throw new UnsupportedOperationException("Method not decompiled: com.clouds.http.HttpParserJson.getCertificateInfo(org.json.JSONObject, java.lang.String):java.util.List<com.clouds.object.JSONFileInfo>");
        /*
        r13 = this;
        r7 = new java.util.ArrayList;
        r7.<init>();
        if (r14 == 0) goto L_0x0012;
    L_0x0007:
        r10 = r14.getJSONArray(r15);	 Catch:{ JSONException -> 0x0049 }
        r9 = 0;
    L_0x000c:
        r12 = r10.length();	 Catch:{ JSONException -> 0x0049 }
        if (r9 < r12) goto L_0x0013;
    L_0x0012:
        return r7;
    L_0x0013:
        r11 = r10.optJSONObject(r9);	 Catch:{ JSONException -> 0x0049 }
        r12 = "sha256";
        r1 = r11.getString(r12);	 Catch:{ JSONException -> 0x0049 }
        r12 = "url";
        r2 = r11.getString(r12);	 Catch:{ JSONException -> 0x0049 }
        r12 = "size";
        r6 = r11.getInt(r12);	 Catch:{ JSONException -> 0x0049 }
        r5 = "";
        r3 = 0;
        r4 = 0;
        r12 = "type";
        r3 = r11.getString(r12);	 Catch:{ Exception -> 0x0044 }
    L_0x0033:
        r12 = "serverurl";
        r4 = r11.getString(r12);	 Catch:{ Exception -> 0x004f }
    L_0x0039:
        r0 = new com.clouds.object.JSONFileInfo;	 Catch:{ JSONException -> 0x0049 }
        r0.<init>(r1, r2, r3, r4, r5, r6);	 Catch:{ JSONException -> 0x0049 }
        r7.add(r0);	 Catch:{ JSONException -> 0x0049 }
        r9 = r9 + 1;
        goto L_0x000c;
    L_0x0044:
        r8 = move-exception;
        r8.printStackTrace();	 Catch:{ JSONException -> 0x0049 }
        goto L_0x0033;
    L_0x0049:
        r8 = move-exception;
        r8.printStackTrace();
        r7 = 0;
        goto L_0x0012;
    L_0x004f:
        r8 = move-exception;
        r8.printStackTrace();	 Catch:{ JSONException -> 0x0049 }
        goto L_0x0039;
        */
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.util.List<com.clouds.object.JSONFileInfo> getFileInfo(org.json.JSONObject r18_jsonObject, java.lang.String r19_key) {
        throw new UnsupportedOperationException("Method not decompiled: com.clouds.http.HttpParserJson.getFileInfo(org.json.JSONObject, java.lang.String):java.util.List<com.clouds.object.JSONFileInfo>");
        /*
        r17 = this;
        r14 = TAG;
        r15 = new java.lang.StringBuilder;
        r16 = "getFileInfo key: ";
        r15.<init>(r16);
        r0 = r19;
        r15 = r15.append(r0);
        r15 = r15.toString();
        com.clouds.debug.SystemDebug.e(r14, r15);
        r8 = 0;
        if (r18 == 0) goto L_0x002a;
    L_0x0019:
        r9 = new java.util.ArrayList;	 Catch:{ JSONException -> 0x0064 }
        r9.<init>();	 Catch:{ JSONException -> 0x0064 }
        r12 = r18.getJSONArray(r19);	 Catch:{ JSONException -> 0x005c }
        r11 = 0;
    L_0x0023:
        r14 = r12.length();	 Catch:{ JSONException -> 0x005c }
        if (r11 < r14) goto L_0x002c;
    L_0x0029:
        r8 = r9;
    L_0x002a:
        r9 = r8;
    L_0x002b:
        return r9;
    L_0x002c:
        r13 = r12.optJSONObject(r11);	 Catch:{ JSONException -> 0x005c }
        r14 = "sha256";
        r2 = r13.getString(r14);	 Catch:{ JSONException -> 0x005c }
        r14 = "url";
        r3 = r13.getString(r14);	 Catch:{ JSONException -> 0x005c }
        r14 = "size";
        r7 = r13.getInt(r14);	 Catch:{ JSONException -> 0x005c }
        r6 = "";
        r4 = 0;
        r5 = 0;
        r14 = "type";
        r4 = r13.getString(r14);	 Catch:{ Exception -> 0x0057 }
    L_0x004c:
        r1 = new com.clouds.object.JSONFileInfo;	 Catch:{ JSONException -> 0x005c }
        r1.<init>(r2, r3, r4, r5, r6, r7);	 Catch:{ JSONException -> 0x005c }
        r9.add(r1);	 Catch:{ JSONException -> 0x005c }
        r11 = r11 + 1;
        goto L_0x0023;
    L_0x0057:
        r10 = move-exception;
        r10.printStackTrace();	 Catch:{ JSONException -> 0x005c }
        goto L_0x004c;
    L_0x005c:
        r10 = move-exception;
        r8 = r9;
    L_0x005e:
        r10.printStackTrace();
        r8 = 0;
        r9 = r8;
        goto L_0x002b;
    L_0x0064:
        r10 = move-exception;
        goto L_0x005e;
        */
    }

    private JSONObject getJSONObject(String servInfo) {
        try {
            jsonObject = new JSONObject(servInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private int getJsonIntegerValue(JSONObject jsonObject, String key) {
        if (jsonObject == null) {
            return 0;
        }
        try {
            return jsonObject.getInt(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private String getJsonStringValue(JSONObject jsonObject, String key) {
        if (jsonObject == null) {
            return null;
        }
        try {
            return jsonObject.getString(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.util.List<com.clouds.object.NotificationInfo> getNotificationInfo(org.json.JSONObject r11_jsonObject, java.lang.String r12_key) {
        throw new UnsupportedOperationException("Method not decompiled: com.clouds.http.HttpParserJson.getNotificationInfo(org.json.JSONObject, java.lang.String):java.util.List<com.clouds.object.NotificationInfo>");
        /*
        r10 = this;
        r0 = new java.util.ArrayList;
        r0.<init>();
        if (r11 == 0) goto L_0x0012;
    L_0x0007:
        r3 = r11.getJSONArray(r12);	 Catch:{ JSONException -> 0x0034 }
        r2 = 0;
    L_0x000c:
        r9 = r3.length();	 Catch:{ JSONException -> 0x0034 }
        if (r2 < r9) goto L_0x0013;
    L_0x0012:
        return r0;
    L_0x0013:
        r6 = r3.optJSONObject(r2);	 Catch:{ JSONException -> 0x0034 }
        r9 = "title";
        r8 = r6.getString(r9);	 Catch:{ JSONException -> 0x0034 }
        r9 = "text";
        r7 = r6.getString(r9);	 Catch:{ JSONException -> 0x0034 }
        r9 = "link";
        r4 = r6.getString(r9);	 Catch:{ JSONException -> 0x0034 }
        r5 = new com.clouds.object.NotificationInfo;	 Catch:{ JSONException -> 0x0034 }
        r5.<init>(r8, r7, r4);	 Catch:{ JSONException -> 0x0034 }
        r0.add(r5);	 Catch:{ JSONException -> 0x0034 }
        r2 = r2 + 1;
        goto L_0x000c;
    L_0x0034:
        r1 = move-exception;
        r1.printStackTrace();
        r0 = 0;
        goto L_0x0012;
        */
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.util.List<com.clouds.object.JSONFileInfo> getScreenshotsInfo(org.json.JSONObject r14_jsonObject, java.lang.String r15_key) {
        throw new UnsupportedOperationException("Method not decompiled: com.clouds.http.HttpParserJson.getScreenshotsInfo(org.json.JSONObject, java.lang.String):java.util.List<com.clouds.object.JSONFileInfo>");
        /*
        r13 = this;
        r7 = new java.util.ArrayList;
        r7.<init>();
        if (r14 == 0) goto L_0x0012;
    L_0x0007:
        r10 = r14.getJSONArray(r15);	 Catch:{ JSONException -> 0x0049 }
        r9 = 0;
    L_0x000c:
        r12 = r10.length();	 Catch:{ JSONException -> 0x0049 }
        if (r9 < r12) goto L_0x0013;
    L_0x0012:
        return r7;
    L_0x0013:
        r11 = r10.optJSONObject(r9);	 Catch:{ JSONException -> 0x0049 }
        r12 = "sha256";
        r1 = r11.getString(r12);	 Catch:{ JSONException -> 0x0049 }
        r12 = "url";
        r2 = r11.getString(r12);	 Catch:{ JSONException -> 0x0049 }
        r12 = "size";
        r6 = r11.getInt(r12);	 Catch:{ JSONException -> 0x0049 }
        r5 = "TV";
        r3 = 0;
        r4 = 0;
        r12 = "type";
        r3 = r11.getString(r12);	 Catch:{ Exception -> 0x0044 }
    L_0x0033:
        r12 = "serverurl";
        r4 = r11.getString(r12);	 Catch:{ Exception -> 0x004f }
    L_0x0039:
        r0 = new com.clouds.object.JSONFileInfo;	 Catch:{ JSONException -> 0x0049 }
        r0.<init>(r1, r2, r3, r4, r5, r6);	 Catch:{ JSONException -> 0x0049 }
        r7.add(r0);	 Catch:{ JSONException -> 0x0049 }
        r9 = r9 + 1;
        goto L_0x000c;
    L_0x0044:
        r8 = move-exception;
        r8.printStackTrace();	 Catch:{ JSONException -> 0x0049 }
        goto L_0x0033;
    L_0x0049:
        r8 = move-exception;
        r8.printStackTrace();
        r7 = 0;
        goto L_0x0012;
    L_0x004f:
        r8 = move-exception;
        r8.printStackTrace();	 Catch:{ JSONException -> 0x0049 }
        goto L_0x0039;
        */
    }

    private int getServerStatus() {
        String status = getJsonStringValue(jsonObject, "status");
        SystemDebug.e(TAG, new StringBuilder("getServerStatus  status: ").append(status).toString());
        if (status == null) {
            return 0;
        }
        if (status.equals("ok")) {
            return SERVER_STATUS_OK;
        }
        if (status.equals("no")) {
            return SERVER_STATUS_NO;
        }
        return status.equals("busy") ? SERVER_STATUS_BUSY : 0;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.clouds.http.HttpParserJson.Update getUpdateInfo(org.json.JSONObject r9_jsonObject) {
        throw new UnsupportedOperationException("Method not decompiled: com.clouds.http.HttpParserJson.getUpdateInfo(org.json.JSONObject):com.clouds.http.HttpParserJson$Update");
        /*
        r8 = this;
        r4 = new com.clouds.http.HttpParserJson$Update;
        r4.<init>();
        if (r9 == 0) goto L_0x0044;
    L_0x0007:
        r5 = TAG;	 Catch:{ JSONException -> 0x0085 }
        r6 = new java.lang.StringBuilder;	 Catch:{ JSONException -> 0x0085 }
        r7 = "UpdateInfo jsonObject length:";
        r6.<init>(r7);	 Catch:{ JSONException -> 0x0085 }
        r7 = r9.length();	 Catch:{ JSONException -> 0x0085 }
        r6 = r6.append(r7);	 Catch:{ JSONException -> 0x0085 }
        r6 = r6.toString();	 Catch:{ JSONException -> 0x0085 }
        com.clouds.debug.SystemDebug.e(r5, r6);	 Catch:{ JSONException -> 0x0085 }
        r5 = "update";
        r2 = r9.getJSONArray(r5);	 Catch:{ JSONException -> 0x0085 }
        r1 = 0;
    L_0x0026:
        r5 = r2.length();	 Catch:{ JSONException -> 0x0085 }
        if (r1 < r5) goto L_0x0045;
    L_0x002c:
        r5 = TAG;	 Catch:{ JSONException -> 0x0085 }
        r6 = new java.lang.StringBuilder;	 Catch:{ JSONException -> 0x0085 }
        r7 = "UpdateInfo update:";
        r6.<init>(r7);	 Catch:{ JSONException -> 0x0085 }
        r7 = r4.toString();	 Catch:{ JSONException -> 0x0085 }
        r6 = r6.append(r7);	 Catch:{ JSONException -> 0x0085 }
        r6 = r6.toString();	 Catch:{ JSONException -> 0x0085 }
        com.clouds.debug.SystemDebug.e(r5, r6);	 Catch:{ JSONException -> 0x0085 }
    L_0x0044:
        return r4;
    L_0x0045:
        r3 = r2.optJSONObject(r1);	 Catch:{ JSONException -> 0x0085 }
        r5 = TAG;	 Catch:{ JSONException -> 0x0085 }
        r6 = r3.toString();	 Catch:{ JSONException -> 0x0085 }
        com.clouds.debug.SystemDebug.e(r5, r6);	 Catch:{ JSONException -> 0x0085 }
        r5 = "client";
        r5 = r3.getBoolean(r5);	 Catch:{ JSONException -> 0x0085 }
        r4.isclient = r5;	 Catch:{ JSONException -> 0x0085 }
        r5 = "logo";
        r5 = r3.getBoolean(r5);	 Catch:{ JSONException -> 0x0085 }
        r4.isLogo = r5;	 Catch:{ JSONException -> 0x0085 }
        r5 = "homepage";
        r5 = r3.getBoolean(r5);	 Catch:{ JSONException -> 0x0085 }
        r4.isUrl = r5;	 Catch:{ JSONException -> 0x0085 }
        r5 = "wall";
        r5 = r3.getBoolean(r5);	 Catch:{ JSONException -> 0x0085 }
        r4.isWall = r5;	 Catch:{ JSONException -> 0x0085 }
        r5 = "app";
        r5 = r3.getBoolean(r5);	 Catch:{ JSONException -> 0x0085 }
        r4.isApp = r5;	 Catch:{ JSONException -> 0x0085 }
        r5 = "screenshots";
        r5 = r3.getBoolean(r5);	 Catch:{ JSONException -> 0x0085 }
        r4.isScreenshots = r5;	 Catch:{ JSONException -> 0x0085 }
        r1 = r1 + 1;
        goto L_0x0026;
    L_0x0085:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x0044;
        */
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.clouds.http.HttpParserJson.Verinfo getVerinfoInfo(org.json.JSONObject r10_jsonObject) {
        throw new UnsupportedOperationException("Method not decompiled: com.clouds.http.HttpParserJson.getVerinfoInfo(org.json.JSONObject):com.clouds.http.HttpParserJson$Verinfo");
        /*
        r9 = this;
        r4 = 0;
        if (r10 == 0) goto L_0x002e;
    L_0x0003:
        r6 = TAG;	 Catch:{ JSONException -> 0x0066 }
        r7 = new java.lang.StringBuilder;	 Catch:{ JSONException -> 0x0066 }
        r8 = "VerinfoInfo jsonObject length:";
        r7.<init>(r8);	 Catch:{ JSONException -> 0x0066 }
        r8 = r10.length();	 Catch:{ JSONException -> 0x0066 }
        r7 = r7.append(r8);	 Catch:{ JSONException -> 0x0066 }
        r7 = r7.toString();	 Catch:{ JSONException -> 0x0066 }
        com.clouds.debug.SystemDebug.e(r6, r7);	 Catch:{ JSONException -> 0x0066 }
        r6 = "verinfo";
        r2 = r10.getJSONArray(r6);	 Catch:{ JSONException -> 0x0066 }
        r5 = new com.clouds.http.HttpParserJson$Verinfo;	 Catch:{ JSONException -> 0x0066 }
        r5.<init>();	 Catch:{ JSONException -> 0x0066 }
        r1 = 0;
    L_0x0027:
        r6 = r2.length();	 Catch:{ JSONException -> 0x006b }
        if (r1 < r6) goto L_0x002f;
    L_0x002d:
        r4 = r5;
    L_0x002e:
        return r4;
    L_0x002f:
        r3 = r2.optJSONObject(r1);	 Catch:{ JSONException -> 0x006b }
        r6 = "clientver";
        r6 = r3.getInt(r6);	 Catch:{ JSONException -> 0x006b }
        r5.clientver = r6;	 Catch:{ JSONException -> 0x006b }
        r6 = "logover";
        r6 = r3.getInt(r6);	 Catch:{ JSONException -> 0x006b }
        r5.logover = r6;	 Catch:{ JSONException -> 0x006b }
        r6 = "homepagever";
        r6 = r3.getInt(r6);	 Catch:{ JSONException -> 0x006b }
        r5.bhurlver = r6;	 Catch:{ JSONException -> 0x006b }
        r6 = "wallppver";
        r6 = r3.getInt(r6);	 Catch:{ JSONException -> 0x006b }
        r5.wallppver = r6;	 Catch:{ JSONException -> 0x006b }
        r6 = "appsver";
        r6 = r3.getInt(r6);	 Catch:{ JSONException -> 0x006b }
        r5.appsver = r6;	 Catch:{ JSONException -> 0x006b }
        r6 = "screenshotsver";
        r6 = r3.getInt(r6);	 Catch:{ JSONException -> 0x006b }
        r5.screenshotsver = r6;	 Catch:{ JSONException -> 0x006b }
        r1 = r1 + 1;
        goto L_0x0027;
    L_0x0066:
        r0 = move-exception;
    L_0x0067:
        r0.printStackTrace();
        goto L_0x002e;
    L_0x006b:
        r0 = move-exception;
        r4 = r5;
        goto L_0x0067;
        */
    }

    public List<JSONFileInfo> getAppListInfo() {
        return getAppFileInfo(jsonObject, "applist");
    }

    public List<JSONFileInfo> getCertificateInfo() {
        return getCertificateInfo(jsonObject, "certificatelist");
    }

    public List<JSONFileInfo> getClientListInfo() {
        return getFileInfo(jsonObject, "clientlist");
    }

    public List<JSONFileInfo> getHomepageListInfo() {
        return getFileInfo(jsonObject, "homepagelist");
    }

    public List<JSONFileInfo> getHomepageListInfos() {
        return getFileInfo(jsonObject, "homepagelists");
    }

    public List<JSONFileInfo> getLogoListInfo() {
        return getFileInfo(jsonObject, "logolist");
    }

    public List<NotificationInfo> getNotificationList() {
        return getNotificationInfo(jsonObject, "notification");
    }

    public List<JSONFileInfo> getScreenshotsInfo() {
        return getScreenshotsInfo(jsonObject, "screenshotslist");
    }

    public int getServerFrequency() {
        int frequency = getJsonIntegerValue(jsonObject, "frequency");
        SystemDebug.e(TAG, new StringBuilder("getServerFrequency  frequency: ").append(frequency).toString());
        return frequency < 180 ? defineTimerTime : frequency;
    }

    public Update getUpdateInfo() {
        return getUpdateInfo(jsonObject);
    }

    public Verinfo getVerinfoInfo() {
        return getVerinfoInfo(jsonObject);
    }

    public List<JSONFileInfo> getWallListInfo() {
        return getFileInfo(jsonObject, "walllist");
    }

    public boolean isServerBusy() {
        return getServerStatus() == 3;
    }
}