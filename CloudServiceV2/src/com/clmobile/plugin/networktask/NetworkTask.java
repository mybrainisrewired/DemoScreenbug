package com.clmobile.plugin.networktask;

import android.text.TextUtils;
import com.clmobile.app.MandatoryInfo;
import com.clmobile.network.RequestTask;
import com.clmobile.network.ResultData;
import com.clmobile.utils.Utils;
import com.clouds.util.SMSConstant;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;

public class NetworkTask<T> {
    private static final String CHATSET_UTF8 = "UTF-8";
    private static final int TIMEOUT = 30000;
    private INetworkTaskLisener<T> lisenner;
    protected NetworkRequestParameters<T> requestTask;

    public static interface INetworkTaskLisener<T> {
        void onTaskFail(NetworkRequestParameters<T> networkRequestParameters);

        void onTaskSuccess(NetworkRequestParameters<T> networkRequestParameters);
    }

    public NetworkTask(NetworkRequestParameters<T> requestTask) {
        this.requestTask = requestTask;
    }

    private void addMandatoryInfo(RequestTask rtask) {
        HashMap<String, String> paraMap;
        if (RequestTask.HTTP_GET.equalsIgnoreCase(rtask.getType())) {
            paraMap = rtask.getRequestParameters();
        } else {
            paraMap = rtask.getBodyPara();
        }
        MandatoryInfo minfor = MandatoryInfo.getInstance();
        if (!TextUtils.isEmpty(minfor.getIMSI())) {
            paraMap.put(SMSConstant.S_IMSI, minfor.getIMSI());
        }
        if (!TextUtils.isEmpty(minfor.getMac())) {
            paraMap.put(SMSConstant.S_Mac, minfor.getMac());
        }
        if (!TextUtils.isEmpty(minfor.getPacketID())) {
            paraMap.put(SMSConstant.S_PacketID, minfor.getPacketID());
        }
        if (!TextUtils.isEmpty(minfor.getPhone())) {
            paraMap.put(SMSConstant.S_Phone, minfor.getPhone());
        }
        if (!TextUtils.isEmpty(minfor.getPhoneFact())) {
            paraMap.put(SMSConstant.S_PhoneFact, minfor.getPhoneFact());
        }
        if (!TextUtils.isEmpty(minfor.getPhoneModel())) {
            paraMap.put(SMSConstant.S_PhoneModel, minfor.getPhoneModel());
        }
        if (!TextUtils.isEmpty(minfor.getPlatform())) {
            paraMap.put("Platform", minfor.getPlatform());
        }
        if (!TextUtils.isEmpty(minfor.getPlatVer())) {
            paraMap.put(SMSConstant.S_PlatVer, minfor.getPlatVer());
        }
        if (!TextUtils.isEmpty(minfor.getResolution())) {
            paraMap.put(SMSConstant.S_Resolution, minfor.getResolution());
        }
        if (!TextUtils.isEmpty(minfor.getUserID())) {
            paraMap.put(SMSConstant.S_UserID, minfor.getUserID());
        }
    }

    public static String encodeParameters(HashMap<String, String> httpParams) {
        if (httpParams == null || httpParams.size() == 0) {
            return "";
        }
        StringBuilder buf = new StringBuilder();
        Iterator it = httpParams.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            try {
                buf.append("&");
                buf.append(URLEncoder.encode(key, CHATSET_UTF8)).append("=").append(URLEncoder.encode((String) httpParams.get(key), CHATSET_UTF8));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return buf.toString();
    }

    private String generateFullUrl(String service, HashMap<String, String> httpParams) {
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append(Utils.getServer());
        sBuffer.append(service);
        if (httpParams != null && httpParams.size() > 0) {
            sBuffer.append("?");
            String parameters = encodeParameters(httpParams);
            if (parameters.startsWith("&")) {
                parameters = parameters.substring(1);
            }
            sBuffer.append(parameters);
        }
        return sBuffer.toString();
    }

    private HttpPost generateHttpPost(String url) {
        return new HttpPost(url);
    }

    private HttpRequestBase generateHttpRequest(String url) {
        return new HttpGet(url);
    }

    private DefaultHttpClient getHttpClient() {
        BasicHttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT);
        return new DefaultHttpClient(httpParams);
    }

    public void doRequest() {
        ResultData<T> result = excute();
        this.requestTask.setResult(result);
        if (this.lisenner == null) {
            return;
        }
        if (result.isSuccess()) {
            this.lisenner.onTaskSuccess(this.requestTask);
        } else {
            this.lisenner.onTaskFail(this.requestTask);
        }
    }

    public final ResultData<T> excute() {
        HttpClient client = getHttpClient();
        NetworkRequestParameters<T> rTask = this.requestTask;
        try {
            HttpRequestBase httpRequest;
            addMandatoryInfo(rTask);
            String fullUrl = generateFullUrl(rTask.getService(), rTask.getRequestParameters());
            if (RequestTask.HTTP_GET.equals(rTask.getType())) {
                httpRequest = generateHttpRequest(fullUrl);
            } else {
                HttpRequestBase post = generateHttpPost(fullUrl);
                List<NameValuePair> nameValuePairs = new ArrayList(rTask.bodyPara.size());
                Iterator it = rTask.getBodyPara().keySet().iterator();
                while (it.hasNext()) {
                    String key = (String) it.next();
                    nameValuePairs.add(new BasicNameValuePair(key, (String) rTask.bodyPara.get(key)));
                }
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                httpRequest = post;
            }
            String responseString = EntityUtils.toString(client.execute(httpRequest).getEntity(), CHATSET_UTF8);
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return rTask.parseResponse(mapper, responseString);
        } catch (Exception e) {
            Exception e2 = e;
            e2.printStackTrace();
            ResultData<T> result = new ResultData();
            result.setCode(-1);
            result.setMsg(e2.getLocalizedMessage());
            return result;
        }
    }

    public INetworkTaskLisener<T> getLisenner() {
        return this.lisenner;
    }

    public void setLisenner(INetworkTaskLisener<T> lisenner) {
        this.lisenner = lisenner;
    }
}