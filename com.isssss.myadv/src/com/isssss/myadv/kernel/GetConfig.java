package com.isssss.myadv.kernel;

import android.content.Context;
import android.util.Log;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.isssss.myadv.constant.AppConst;
import com.isssss.myadv.constant.ParamConst;
import com.isssss.myadv.constant.URLConst;
import com.isssss.myadv.dao.AdvConfigDao;
import com.isssss.myadv.dao.AdvConfigTable;
import com.isssss.myadv.dao.BannerConfigTable;
import com.isssss.myadv.model.AdvConfig;
import com.isssss.myadv.utils.ApkUtil;
import com.isssss.myadv.utils.LocationUtils;
import com.isssss.myadv.utils.NetUtil;
import com.isssss.myadv.utils.SPUtil;
import com.isssss.myadv.utils.SimUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import org.json.JSONArray;
import org.json.JSONObject;

public class GetConfig extends TimerTask {
    public static String COUNTRY_DEFAULT;
    Context mContext;

    static {
        COUNTRY_DEFAULT = "IANA";
    }

    public GetConfig(Context context) {
        this.mContext = context;
    }

    private ErrorListener errorListener() {
        return new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.e("GetConfig", "Error");
            }
        };
    }

    private void getAdvConfigure() {
        RequestQueue queue = RequestManager.getRequestQueue(this.mContext);
        Map<String, String> pas = new HashMap();
        pas.put(ParamConst.CID, AppConst.CID);
        pas.put(ParamConst.IMSI, SimUtil.getImsi(this.mContext));
        pas.put(ParamConst.IMEI, SimUtil.getImei(this.mContext));
        pas.put(ParamConst.VERSION_NO, String.valueOf(ApkUtil.getLocalVersionNo(this.mContext)));
        pas.put(ParamConst.MAC, new StringBuilder(String.valueOf(SPUtil.getMac())).append("_").append(SPUtil.getUnique(this.mContext)).toString());
        pas.put(ParamConst.PRODUCT_ID, AppConst.ProductId);
        if (!SPUtil.COUNTRY_DEFAULT.equalsIgnoreCase(SPUtil.getCountryCode(this.mContext))) {
            pas.put(ParamConst.COUNTRY_CODE, SPUtil.getCountryCode(this.mContext));
        }
        if (!SPUtil.DEFUALT_UID.equalsIgnoreCase(SPUtil.getUniqueID(this.mContext))) {
            pas.put(ParamConst.USER_ID, SPUtil.getUniqueID(this.mContext));
        }
        if (0 == SPUtil.getFirstLauncherTime(this.mContext)) {
            SPUtil.storeFirstLauncherTime(this.mContext, System.currentTimeMillis());
        }
        Log.d("GetConfig", "Get Config request");
        queue.add(new JsonObjectRequest(1, URLConst.REQ_IA_URL, pas, successListener(), errorListener()));
    }

    private Listener<JSONObject> successListener() {
        return new Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt(URLConst.RET) == 0) {
                        JSONArray jsonArray = new JSONArray(response.getString(ParamConst.AD_SCREEN));
                        if (jsonArray.length() != 0) {
                            JSONObject jobj = jsonArray.getJSONObject(0);
                            AdvConfig config = new AdvConfig();
                            config.setShow(jobj.getInt(BannerConfigTable.COLUMN_SHOWS));
                            config.setTimely(jobj.getInt(BannerConfigTable.COLUMN_TIME_DAILY));
                            config.setWhitelist(jobj.getString(BannerConfigTable.COLUMN_WHITE_LIST));
                            config.setAdvCompany(jobj.getInt(ParamConst.AD_SIGN));
                            config.setAdvid(jobj.getString(AdvConfigTable.COLUMN_ADV_ID));
                            config.setIp(response.getString(ParamConst.IP));
                            config.setShipmentTime(jobj.getLong(ParamConst.SHIPMENT_TIME));
                            AdvConfigDao.getInstance(GetConfig.this.mContext).insertConfig(config);
                            SPUtil.storeFirstPowerOnTime(GetConfig.this.mContext, System.currentTimeMillis());
                            Log.d("GetConfig", "Get Config succeed");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public void run() {
        if (NetUtil.isNetworkConnected(this.mContext)) {
            if (!SPUtil.getStatisticsAgreement(this.mContext)) {
                StatisticsAgreement.statistics(this.mContext);
            }
            if ((SPUtil.COUNTRY_DEFAULT.equalsIgnoreCase(SPUtil.getCountryCode(this.mContext)) | SPUtil.DEFUALT_UID.equalsIgnoreCase(SPUtil.getUniqueID(this.mContext))) != 0) {
                LocationUtils.getCountryCodeViaOurs(this.mContext);
                Log.d("SystemService", "Request_Location_Code");
            }
        }
        getAdvConfigure();
        BannerRequester.getBannerData(this.mContext);
    }
}