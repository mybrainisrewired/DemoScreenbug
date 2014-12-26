package com.clmobile.network;

import android.os.AsyncTask;
import android.text.TextUtils;
import com.clmobile.app.MandatoryInfo;
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
import org.codehaus.jackson.type.TypeReference;

public abstract class NetworkAsyncTask<T> extends AsyncTask<RequestTask, Integer, ResultData<T>> {
    private static final String CHATSET_UTF8 = "UTF-8";
    private static final int TIMEOUT = 30000;

    private void addMandatoryInfo(RequestTask rtask) {
        MandatoryInfo minfor = MandatoryInfo.getInstance();
        if (!TextUtils.isEmpty(minfor.getIMSI())) {
            rtask.getRequestParameters().put(SMSConstant.S_IMSI, minfor.getIMSI());
        }
        if (!TextUtils.isEmpty(minfor.getMac())) {
            rtask.getRequestParameters().put(SMSConstant.S_Mac, minfor.getMac());
        }
        if (!TextUtils.isEmpty(minfor.getPacketID())) {
            rtask.getRequestParameters().put(SMSConstant.S_PacketID, minfor.getPacketID());
        }
        if (!TextUtils.isEmpty(minfor.getPhone())) {
            rtask.getRequestParameters().put(SMSConstant.S_Phone, minfor.getPhone());
        }
        if (!TextUtils.isEmpty(minfor.getPhoneFact())) {
            rtask.getRequestParameters().put(SMSConstant.S_PhoneFact, minfor.getPhoneFact());
        }
        if (!TextUtils.isEmpty(minfor.getPhoneModel())) {
            rtask.getRequestParameters().put(SMSConstant.S_PhoneModel, minfor.getPhoneModel());
        }
        if (!TextUtils.isEmpty(minfor.getPlatform())) {
            rtask.getRequestParameters().put("Platform", minfor.getPlatform());
        }
        if (!TextUtils.isEmpty(minfor.getPlatVer())) {
            rtask.getRequestParameters().put(SMSConstant.S_PlatVer, minfor.getPlatVer());
        }
        if (!TextUtils.isEmpty(minfor.getResolution())) {
            rtask.getRequestParameters().put(SMSConstant.S_Resolution, minfor.getResolution());
        }
        if (!TextUtils.isEmpty(minfor.getIMSI())) {
            rtask.getRequestParameters().put(SMSConstant.S_IMSI, minfor.getIMSI());
        }
        if (!TextUtils.isEmpty(minfor.getUserID())) {
            rtask.getRequestParameters().put(SMSConstant.S_UserID, minfor.getUserID());
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

    protected final ResultData<T> doInBackground(RequestTask... params) {
        HttpClient client = getHttpClient();
        RequestTask rTask = params[0];
        try {
            addMandatoryInfo(rTask);
            String fullUrl = generateFullUrl(rTask.getService(), rTask.getRequestParameters());
            HttpRequestBase httpRequest = null;
            if (RequestTask.HTTP_GET.equals(rTask.getType())) {
                httpRequest = generateHttpRequest(fullUrl);
            } else {
                HttpPost post = generateHttpPost(fullUrl);
                List<NameValuePair> nameValuePairs = new ArrayList(rTask.bodyPara.size());
                Iterator it = rTask.getBodyPara().keySet().iterator();
                while (it.hasNext()) {
                    String key = (String) it.next();
                    nameValuePairs.add(new BasicNameValuePair(key, (String) rTask.bodyPara.get(key)));
                }
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            }
            String responseString = EntityUtils.toString(client.execute(httpRequest).getEntity(), CHATSET_UTF8);
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return parseResponse(mapper, responseString);
        } catch (Exception e) {
            Exception e2 = e;
            e2.printStackTrace();
            ResultData<T> result = new ResultData();
            result.setCode(-1);
            result.setMsg(e2.getLocalizedMessage());
            return result;
        }
    }

    protected ResultData<T> parseResponse(ObjectMapper mappger, String str) throws Exception {
        return (ResultData) mappger.readValue(str, new TypeReference<ResultData<T>>() {
        });
    }
}