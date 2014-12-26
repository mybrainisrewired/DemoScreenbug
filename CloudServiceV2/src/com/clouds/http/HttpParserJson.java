package com.clouds.http;

import android.util.Log;
import com.clouds.debug.SystemDebug;
import com.clouds.object.JSONFileInfo;
import com.clouds.object.NotificationInfo;
import java.util.List;
import org.json.JSONArray;
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
    private java.util.List<com.clouds.object.JSONFileInfo> getAppFileInfo(org.json.JSONObject r19_jsonObject, java.lang.String r20_key) {
        throw new UnsupportedOperationException("Method not decompiled: com.clouds.http.HttpParserJson.getAppFileInfo(org.json.JSONObject, java.lang.String):java.util.List<com.clouds.object.JSONFileInfo>");
        /*
        r18 = this;
        r9 = 0;
        if (r19 == 0) goto L_0x002c;
    L_0x0003:
        r10 = new java.util.ArrayList;	 Catch:{ JSONException -> 0x0069 }
        r10.<init>();	 Catch:{ JSONException -> 0x0069 }
        r13 = r19.getJSONArray(r20);	 Catch:{ JSONException -> 0x006d }
        r15 = TAG;	 Catch:{ JSONException -> 0x006d }
        r16 = new java.lang.StringBuilder;	 Catch:{ JSONException -> 0x006d }
        r17 = "====\u9700\u4e0b\u8f7d\u7684\u6587\u4ef6\u4fe1\u606f\u7684json\u6570\u636e==getAppFileInfo==jsonArray ==== ";
        r16.<init>(r17);	 Catch:{ JSONException -> 0x006d }
        r17 = r13.toString();	 Catch:{ JSONException -> 0x006d }
        r16 = r16.append(r17);	 Catch:{ JSONException -> 0x006d }
        r16 = r16.toString();	 Catch:{ JSONException -> 0x006d }
        com.clouds.debug.SystemDebug.e(r15, r16);	 Catch:{ JSONException -> 0x006d }
        r12 = 0;
    L_0x0025:
        r15 = r13.length();	 Catch:{ JSONException -> 0x006d }
        if (r12 < r15) goto L_0x002e;
    L_0x002b:
        r9 = r10;
    L_0x002c:
        r10 = r9;
    L_0x002d:
        return r10;
    L_0x002e:
        r14 = r13.optJSONObject(r12);	 Catch:{ JSONException -> 0x006d }
        r15 = "sha256";
        r1 = r14.getString(r15);	 Catch:{ JSONException -> 0x006d }
        r15 = "url";
        r2 = r14.getString(r15);	 Catch:{ JSONException -> 0x006d }
        r15 = "size";
        r8 = r14.getInt(r15);	 Catch:{ JSONException -> 0x006d }
        r5 = "";
        r6 = "";
        r7 = "";
        r3 = 0;
        r4 = 0;
        r15 = "type";
        r3 = r14.getString(r15);	 Catch:{ Exception -> 0x0074 }
    L_0x0052:
        r15 = "package";
        r6 = r14.getString(r15);	 Catch:{ Exception -> 0x0072 }
    L_0x0058:
        r15 = "class";
        r7 = r14.getString(r15);	 Catch:{ Exception -> 0x0070 }
    L_0x005e:
        r0 = new com.clouds.object.JSONFileInfo;	 Catch:{ JSONException -> 0x006d }
        r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8);	 Catch:{ JSONException -> 0x006d }
        r10.add(r0);	 Catch:{ JSONException -> 0x006d }
        r12 = r12 + 1;
        goto L_0x0025;
    L_0x0069:
        r11 = move-exception;
    L_0x006a:
        r9 = 0;
        r10 = r9;
        goto L_0x002d;
    L_0x006d:
        r11 = move-exception;
        r9 = r10;
        goto L_0x006a;
    L_0x0070:
        r15 = move-exception;
        goto L_0x005e;
    L_0x0072:
        r15 = move-exception;
        goto L_0x0058;
    L_0x0074:
        r15 = move-exception;
        goto L_0x0052;
        */
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.util.List<com.clouds.object.JSONFileInfo> getCertificateInfo(org.json.JSONObject r16_jsonObject, java.lang.String r17_key) {
        throw new UnsupportedOperationException("Method not decompiled: com.clouds.http.HttpParserJson.getCertificateInfo(org.json.JSONObject, java.lang.String):java.util.List<com.clouds.object.JSONFileInfo>");
        /*
        r15 = this;
        r7 = new java.util.ArrayList;
        r7.<init>();
        if (r16 == 0) goto L_0x002a;
    L_0x0007:
        r10 = r16.getJSONArray(r17);	 Catch:{ JSONException -> 0x005c }
        r12 = TAG;	 Catch:{ JSONException -> 0x005c }
        r13 = new java.lang.StringBuilder;	 Catch:{ JSONException -> 0x005c }
        r14 = "====\u670d\u52a1\u5668\u7e41\u5fd9\u65f6\u7684json\u6570\u636e==getAppFileInfo==jsonArray ==== ";
        r13.<init>(r14);	 Catch:{ JSONException -> 0x005c }
        r14 = r10.toString();	 Catch:{ JSONException -> 0x005c }
        r13 = r13.append(r14);	 Catch:{ JSONException -> 0x005c }
        r13 = r13.toString();	 Catch:{ JSONException -> 0x005c }
        com.clouds.debug.SystemDebug.e(r12, r13);	 Catch:{ JSONException -> 0x005c }
        r9 = 0;
    L_0x0024:
        r12 = r10.length();	 Catch:{ JSONException -> 0x005c }
        if (r9 < r12) goto L_0x002b;
    L_0x002a:
        return r7;
    L_0x002b:
        r11 = r10.optJSONObject(r9);	 Catch:{ JSONException -> 0x005c }
        r12 = "sha256";
        r1 = r11.getString(r12);	 Catch:{ JSONException -> 0x005c }
        r12 = "url";
        r2 = r11.getString(r12);	 Catch:{ JSONException -> 0x005c }
        r12 = "size";
        r6 = r11.getInt(r12);	 Catch:{ JSONException -> 0x005c }
        r5 = "";
        r3 = 0;
        r4 = 0;
        r12 = "type";
        r3 = r11.getString(r12);	 Catch:{ Exception -> 0x0061 }
    L_0x004b:
        r12 = "serverurl";
        r4 = r11.getString(r12);	 Catch:{ Exception -> 0x005f }
    L_0x0051:
        r0 = new com.clouds.object.JSONFileInfo;	 Catch:{ JSONException -> 0x005c }
        r0.<init>(r1, r2, r3, r4, r5, r6);	 Catch:{ JSONException -> 0x005c }
        r7.add(r0);	 Catch:{ JSONException -> 0x005c }
        r9 = r9 + 1;
        goto L_0x0024;
    L_0x005c:
        r8 = move-exception;
        r7 = 0;
        goto L_0x002a;
    L_0x005f:
        r12 = move-exception;
        goto L_0x0051;
    L_0x0061:
        r12 = move-exception;
        goto L_0x004b;
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
        android.util.Log.e(r14, r15);
        r8 = 0;
        if (r18 == 0) goto L_0x0042;
    L_0x0019:
        r9 = new java.util.ArrayList;	 Catch:{ JSONException -> 0x0087 }
        r9.<init>();	 Catch:{ JSONException -> 0x0087 }
        r12 = r18.getJSONArray(r19);	 Catch:{ JSONException -> 0x008b }
        r14 = TAG;	 Catch:{ JSONException -> 0x008b }
        r15 = new java.lang.StringBuilder;	 Catch:{ JSONException -> 0x008b }
        r16 = "====\u9700\u4e0b\u8f7d\u7684\u6587\u4ef6\u4fe1\u606f\u7684json\u6570\u636e====jsonArray ==== ";
        r15.<init>(r16);	 Catch:{ JSONException -> 0x008b }
        r16 = r12.toString();	 Catch:{ JSONException -> 0x008b }
        r15 = r15.append(r16);	 Catch:{ JSONException -> 0x008b }
        r15 = r15.toString();	 Catch:{ JSONException -> 0x008b }
        com.clouds.debug.SystemDebug.d(r14, r15);	 Catch:{ JSONException -> 0x008b }
        r11 = 0;
    L_0x003b:
        r14 = r12.length();	 Catch:{ JSONException -> 0x008b }
        if (r11 < r14) goto L_0x0044;
    L_0x0041:
        r8 = r9;
    L_0x0042:
        r9 = r8;
    L_0x0043:
        return r9;
    L_0x0044:
        r14 = TAG;	 Catch:{ JSONException -> 0x008b }
        r15 = new java.lang.StringBuilder;	 Catch:{ JSONException -> 0x008b }
        r16 = "====\u9700\u4e0b\u8f7d\u7684\u6587\u4ef6\u4fe1\u606f\u7684json\u6570\u636e\u7684\u5927\u5c0f====jsonArray ==== ";
        r15.<init>(r16);	 Catch:{ JSONException -> 0x008b }
        r16 = r12.length();	 Catch:{ JSONException -> 0x008b }
        r15 = r15.append(r16);	 Catch:{ JSONException -> 0x008b }
        r15 = r15.toString();	 Catch:{ JSONException -> 0x008b }
        com.clouds.debug.SystemDebug.d(r14, r15);	 Catch:{ JSONException -> 0x008b }
        r13 = r12.optJSONObject(r11);	 Catch:{ JSONException -> 0x008b }
        r14 = "sha256";
        r2 = r13.getString(r14);	 Catch:{ JSONException -> 0x008b }
        r14 = "url";
        r3 = r13.getString(r14);	 Catch:{ JSONException -> 0x008b }
        r14 = "size";
        r7 = r13.getInt(r14);	 Catch:{ JSONException -> 0x008b }
        r6 = "";
        r4 = 0;
        r5 = 0;
        r14 = "type";
        r4 = r13.getString(r14);	 Catch:{ Exception -> 0x008e }
    L_0x007c:
        r1 = new com.clouds.object.JSONFileInfo;	 Catch:{ JSONException -> 0x008b }
        r1.<init>(r2, r3, r4, r5, r6, r7);	 Catch:{ JSONException -> 0x008b }
        r9.add(r1);	 Catch:{ JSONException -> 0x008b }
        r11 = r11 + 1;
        goto L_0x003b;
    L_0x0087:
        r10 = move-exception;
    L_0x0088:
        r8 = 0;
        r9 = r8;
        goto L_0x0043;
    L_0x008b:
        r10 = move-exception;
        r8 = r9;
        goto L_0x0088;
    L_0x008e:
        r14 = move-exception;
        goto L_0x007c;
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
            return null;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.util.List<com.clouds.object.NotificationInfo> getNotificationInfo(org.json.JSONObject r13_jsonObject, java.lang.String r14_key) {
        throw new UnsupportedOperationException("Method not decompiled: com.clouds.http.HttpParserJson.getNotificationInfo(org.json.JSONObject, java.lang.String):java.util.List<com.clouds.object.NotificationInfo>");
        /*
        r12 = this;
        r0 = new java.util.ArrayList;
        r0.<init>();
        if (r13 == 0) goto L_0x0012;
    L_0x0007:
        r3 = r13.getJSONArray(r14);	 Catch:{ JSONException -> 0x004c }
        r2 = 0;
    L_0x000c:
        r9 = r3.length();	 Catch:{ JSONException -> 0x004c }
        if (r2 < r9) goto L_0x0013;
    L_0x0012:
        return r0;
    L_0x0013:
        r6 = r3.optJSONObject(r2);	 Catch:{ JSONException -> 0x004c }
        r9 = TAG;	 Catch:{ JSONException -> 0x004c }
        r10 = new java.lang.StringBuilder;	 Catch:{ JSONException -> 0x004c }
        r11 = "tempJson0000000\u83b7\u53d6\u901a\u77e5\u680f\u4e0a\u663e\u793a\u7684\u4fe1\u606f00000000000= ";
        r10.<init>(r11);	 Catch:{ JSONException -> 0x004c }
        r11 = r6.toString();	 Catch:{ JSONException -> 0x004c }
        r10 = r10.append(r11);	 Catch:{ JSONException -> 0x004c }
        r10 = r10.toString();	 Catch:{ JSONException -> 0x004c }
        com.clouds.debug.SystemDebug.e(r9, r10);	 Catch:{ JSONException -> 0x004c }
        r9 = "title";
        r8 = r6.getString(r9);	 Catch:{ JSONException -> 0x004c }
        r9 = "text";
        r7 = r6.getString(r9);	 Catch:{ JSONException -> 0x004c }
        r9 = "link";
        r4 = r6.getString(r9);	 Catch:{ JSONException -> 0x004c }
        r5 = new com.clouds.object.NotificationInfo;	 Catch:{ JSONException -> 0x004c }
        r5.<init>(r8, r7, r4);	 Catch:{ JSONException -> 0x004c }
        r0.add(r5);	 Catch:{ JSONException -> 0x004c }
        r2 = r2 + 1;
        goto L_0x000c;
    L_0x004c:
        r1 = move-exception;
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
        r3 = r11.getString(r12);	 Catch:{ Exception -> 0x004c }
    L_0x0033:
        r12 = "serverurl";
        r4 = r11.getString(r12);	 Catch:{ Exception -> 0x0044 }
    L_0x0039:
        r0 = new com.clouds.object.JSONFileInfo;	 Catch:{ JSONException -> 0x0049 }
        r0.<init>(r1, r2, r3, r4, r5, r6);	 Catch:{ JSONException -> 0x0049 }
        r7.add(r0);	 Catch:{ JSONException -> 0x0049 }
        r9 = r9 + 1;
        goto L_0x000c;
    L_0x0044:
        r8 = move-exception;
        r8.printStackTrace();	 Catch:{ JSONException -> 0x0049 }
        goto L_0x0039;
    L_0x0049:
        r8 = move-exception;
        r7 = 0;
        goto L_0x0012;
    L_0x004c:
        r12 = move-exception;
        goto L_0x0033;
        */
    }

    private int getServerStatus() {
        String status = getJsonStringValue(jsonObject, "status");
        SystemDebug.e(TAG, new StringBuilder("----\u670d\u52a1\u5668\u5f53\u524d\u7684\u72b6\u6001---getServerStatus  status: ").append(status).toString());
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

    private Update getUpdateInfo(JSONObject jsonObject) {
        Update update = null;
        if (jsonObject != null) {
            try {
                SystemDebug.e(TAG, new StringBuilder("UpdateInfo jsonObject length:").append(jsonObject.length()).toString());
                JSONArray jsonArray = jsonObject.getJSONArray("update");
                Update update2 = new Update();
                int i = 0;
                while (i < jsonArray.length()) {
                    try {
                        JSONObject tempJson = jsonArray.optJSONObject(i);
                        Log.e(TAG, new StringBuilder("========\u662f\u5426\u9700\u8981\u66f4\u65b0=====verinfo json ======= ").append(tempJson.toString()).toString());
                        update2.isWall = tempJson.getBoolean("wall");
                        update2.isUrl = tempJson.getBoolean("homepage");
                        update2.isApp = tempJson.getBoolean("app");
                        update2.isLogo = tempJson.getBoolean("logo");
                        update2.isclient = tempJson.getBoolean("client");
                        Log.d(TAG, "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                        update2.isScreenshots = tempJson.getBoolean("screenshots");
                        SystemDebug.d(TAG, new StringBuilder("----isclient:").append(update2.isclient).append("----isUrl:").append(update2.isUrl).append("-----isWall:").append(update2.isWall).toString());
                        i++;
                    } catch (JSONException e) {
                        update = update2;
                    }
                }
                update = update2;
            } catch (JSONException e2) {
            }
            SystemDebug.e(TAG, new StringBuilder("UpdateInfo update:").append(update.toString()).toString());
        }
        return update;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.clouds.http.HttpParserJson.Verinfo getVerinfoInfo(org.json.JSONObject r9_jsonObject) {
        throw new UnsupportedOperationException("Method not decompiled: com.clouds.http.HttpParserJson.getVerinfoInfo(org.json.JSONObject):com.clouds.http.HttpParserJson$Verinfo");
        /*
        r8 = this;
        r3 = 0;
        if (r9 == 0) goto L_0x002e;
    L_0x0003:
        r5 = TAG;	 Catch:{ JSONException -> 0x007e }
        r6 = new java.lang.StringBuilder;	 Catch:{ JSONException -> 0x007e }
        r7 = "VerinfoInfo jsonObject length:";
        r6.<init>(r7);	 Catch:{ JSONException -> 0x007e }
        r7 = r9.length();	 Catch:{ JSONException -> 0x007e }
        r6 = r6.append(r7);	 Catch:{ JSONException -> 0x007e }
        r6 = r6.toString();	 Catch:{ JSONException -> 0x007e }
        android.util.Log.d(r5, r6);	 Catch:{ JSONException -> 0x007e }
        r5 = "verinfo";
        r1 = r9.getJSONArray(r5);	 Catch:{ JSONException -> 0x007e }
        r4 = new com.clouds.http.HttpParserJson$Verinfo;	 Catch:{ JSONException -> 0x007e }
        r4.<init>();	 Catch:{ JSONException -> 0x007e }
        r0 = 0;
    L_0x0027:
        r5 = r1.length();	 Catch:{ JSONException -> 0x0080 }
        if (r0 < r5) goto L_0x002f;
    L_0x002d:
        r3 = r4;
    L_0x002e:
        return r3;
    L_0x002f:
        r2 = r1.optJSONObject(r0);	 Catch:{ JSONException -> 0x0080 }
        r5 = TAG;	 Catch:{ JSONException -> 0x0080 }
        r6 = new java.lang.StringBuilder;	 Catch:{ JSONException -> 0x0080 }
        r7 = "--\u5f53\u524d\u6570\u636e\u66f4\u65b0\u7684\u65f6\u95f4\u6233--tempJson:";
        r6.<init>(r7);	 Catch:{ JSONException -> 0x0080 }
        r7 = r2.toString();	 Catch:{ JSONException -> 0x0080 }
        r6 = r6.append(r7);	 Catch:{ JSONException -> 0x0080 }
        r6 = r6.toString();	 Catch:{ JSONException -> 0x0080 }
        android.util.Log.d(r5, r6);	 Catch:{ JSONException -> 0x0080 }
        r5 = "clientver";
        r5 = r2.getInt(r5);	 Catch:{ JSONException -> 0x0080 }
        r4.clientver = r5;	 Catch:{ JSONException -> 0x0080 }
        r5 = "logover";
        r5 = r2.getInt(r5);	 Catch:{ JSONException -> 0x0080 }
        r4.logover = r5;	 Catch:{ JSONException -> 0x0080 }
        r5 = "homepagever";
        r5 = r2.getInt(r5);	 Catch:{ JSONException -> 0x0080 }
        r4.bhurlver = r5;	 Catch:{ JSONException -> 0x0080 }
        r5 = "wallppver";
        r5 = r2.getInt(r5);	 Catch:{ JSONException -> 0x0080 }
        r4.wallppver = r5;	 Catch:{ JSONException -> 0x0080 }
        r5 = "appsver";
        r5 = r2.getInt(r5);	 Catch:{ JSONException -> 0x0080 }
        r4.appsver = r5;	 Catch:{ JSONException -> 0x0080 }
        r5 = "screenshotsver";
        r5 = r2.getInt(r5);	 Catch:{ JSONException -> 0x0080 }
        r4.screenshotsver = r5;	 Catch:{ JSONException -> 0x0080 }
        r0 = r0 + 1;
        goto L_0x0027;
    L_0x007e:
        r5 = move-exception;
        goto L_0x002e;
    L_0x0080:
        r5 = move-exception;
        r3 = r4;
        goto L_0x002e;
        */
    }

    public List<JSONFileInfo> getAppListInfo() {
        return getAppFileInfo(jsonObject, "applist");
    }

    public List<JSONFileInfo> getBrowerJsListInfo() {
        return getFileInfo(jsonObject, "browerjslist");
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