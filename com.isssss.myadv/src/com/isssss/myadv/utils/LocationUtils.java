package com.isssss.myadv.utils;

import android.content.Context;
import android.util.Log;
import com.inmobi.androidsdk.IMBrowserActivity;
import com.isssss.myadv.constant.ParamConst;
import com.isssss.myadv.constant.URLConst;
import com.mopub.common.Preconditions;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class LocationUtils {
    private static HttpURLConnection connection;
    static String get_unique;
    private static BufferedReader reader;
    static String taobao_address;

    static {
        taobao_address = "http://ip.taobao.com/service/getIpInfo2.php?ip=myip";
        get_unique = "http://ip.iseeyuan.com/ip/getCode";
    }

    public static void getCountryCodeViaOurs(Context context) {
        try {
            URL url = new URL(get_unique);
            String host = url.getHost();
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty(MraidCommandStorePicture.MIME_TYPE_HEADER, "text/plain");
            connection.connect();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer sb = new StringBuffer(Preconditions.EMPTY_ARGUMENTS);
            while (true) {
                String lines = reader.readLine();
                if (lines == null) {
                    JSONObject jsonObject = new JSONObject(sb.toString());
                    if (jsonObject.getInt(URLConst.RET) == 0) {
                        SPUtil.setCountryCode(context, jsonObject.getString(ParamConst.COUNTRY_CODE));
                        SPUtil.setUniqueID(context, jsonObject.getString(ParamConst.USER_ID));
                        Log.d("LocationUtils", "getCC_UID");
                    } else {
                        getCountryCodeViaTB(context);
                    }
                    try {
                        if (reader != null) {
                            reader.close();
                        }
                        if (connection != null) {
                            connection.disconnect();
                        }
                        getCountryCodeViaTB(context);
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    sb.append(new String(lines.getBytes(), "utf-8"));
                }
            }
        } catch (Exception e2) {
            try {
                e2.printStackTrace();
                try {
                    if (reader != null) {
                        reader.close();
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                    getCountryCodeViaTB(context);
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            } catch (Throwable th) {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                    getCountryCodeViaTB(context);
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
        }
    }

    public static void getCountryCodeViaTB(Context context) {
        try {
            URL url = new URL(taobao_address);
            String host = url.getHost();
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty(MraidCommandStorePicture.MIME_TYPE_HEADER, "text/plain");
            connection.connect();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer sb = new StringBuffer(Preconditions.EMPTY_ARGUMENTS);
            while (true) {
                String lines = reader.readLine();
                if (lines == null) {
                    JSONObject jsonObject = new JSONObject(sb.toString());
                    if (jsonObject != null && jsonObject.getInt("code") == 0) {
                        JSONObject data = jsonObject.getJSONObject(IMBrowserActivity.EXPANDDATA);
                        String country_id = data.getString("country_id");
                        if (!(data == null || country_id == null)) {
                            SPUtil.setCountryCode(context, country_id);
                        }
                    }
                    try {
                        if (reader != null) {
                            reader.close();
                        }
                        if (connection != null) {
                            connection.disconnect();
                            return;
                        } else {
                            return;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    sb.append(new String(lines.getBytes(), "utf-8"));
                }
            }
        } catch (Exception e2) {
            try {
                e2.printStackTrace();
                try {
                    if (reader != null) {
                        reader.close();
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            } catch (Throwable th) {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
        }
    }
}