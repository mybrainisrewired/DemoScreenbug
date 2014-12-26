package com.clouds.http;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.clouds.debug.SystemDebug;
import com.clouds.util.SystemInfo;
import com.wmt.opengl.grid.ItemAnimation;
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

public class CheckServerThread extends Thread {
    private static final String TAG;
    private Context context;
    private Handler handler;
    private SystemInfo mSysInfo;

    static {
        TAG = CheckServerThread.class.getName();
    }

    public CheckServerThread(Context context, Handler handler) {
        this.mSysInfo = null;
        this.context = context;
        this.handler = handler;
        this.mSysInfo = new SystemInfo();
    }

    private boolean hasNetworkConnection(Context context) {
        if (context == null) {
            return false;
        }
        NetworkInfo mNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        Log.d(TAG, "network conneted");
        return mNetworkInfo != null && mNetworkInfo.isConnected();
    }

    private void sendMessage(int what, Object obj) {
        Log.v(TAG, new StringBuilder("sendMessage what: ").append(what).toString());
        if (this.handler != null) {
            Message msg = Message.obtain();
            msg.what = what;
            msg.obj = obj;
            this.handler.sendMessage(msg);
        }
    }

    public String getServInfo(String url, Context context, SystemInfo mSysInfo) {
        if (url == null || context == null || mSysInfo == null) {
            return null;
        }
        SystemDebug.saveLogToFile(context, TAG, new StringBuilder(" mSysInfo: ").append(mSysInfo.collectJSON(context)).toString());
        List<NameValuePair> nameValuePair = new ArrayList();
        nameValuePair.add(new BasicNameValuePair("push", mSysInfo.collectJSON(context)));
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
            return getServInfo(CloudsServerHttpConstant.PUSH_SERVER_HTTP_URL, context, mSysInfo);
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

    @SuppressLint({"NewApi"})
    public void run() {
        super.run();
        if (hasNetworkConnection(this.context)) {
            Log.d(TAG, "Run CheckOnlineServerThread ......  has network connection!");
            String info = getServInfo(CloudsServerHttpConstant.PUSH_SERVER_URL, this.context, this.mSysInfo);
            if (info == null || info.isEmpty()) {
                Log.d(TAG, new StringBuilder("get info (").append(CloudsServerHttpConstant.PUSH_SERVER_URL).append(") failed! retry!").toString());
                sendMessage(ItemAnimation.CUR_Z, null);
            } else {
                Log.d(TAG, "CheckOnlineServerThread  SERVER_CONNECTION_SUCCESS ");
                sendMessage(1, info);
            }
        } else {
            Log.d(TAG, new StringBuilder("get info (").append(CloudsServerHttpConstant.PUSH_SERVER_URL).append(") failed! retry!").toString());
            sendMessage(ItemAnimation.CUR_Z, null);
            Log.d(TAG, "wating for network......");
        }
    }
}