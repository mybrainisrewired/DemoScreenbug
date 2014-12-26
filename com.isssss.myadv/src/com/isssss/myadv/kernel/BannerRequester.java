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
import com.isssss.myadv.dao.BannerConfigDao;
import com.isssss.myadv.dao.BannerConfigTable;
import com.isssss.myadv.model.BannerConfig;
import com.isssss.myadv.utils.ApkUtil;
import com.isssss.myadv.utils.SPUtil;
import com.isssss.myadv.utils.SimUtil;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BannerRequester {

    class AnonymousClass_1 implements Listener<JSONObject> {
        private final /* synthetic */ Context val$context;

        AnonymousClass_1(Context context) {
            this.val$context = context;
        }

        public void onResponse(JSONObject response) {
            try {
                if (response.getInt(URLConst.RET) == 0) {
                    JSONArray jsonArray = response.getJSONArray(BannerConfigTable.TABLE_NAME);
                    if (jsonArray.length() != 0) {
                        JSONObject config = jsonArray.getJSONObject(0);
                        BannerConfig bannerConfig = new BannerConfig();
                        bannerConfig.setShows(config.getInt(BannerConfigTable.COLUMN_SHOWS));
                        bannerConfig.setAdvId(config.getString(BannerConfigTable.COLUMN_ADV_ID));
                        bannerConfig.setAdvsign(config.getInt(BannerConfigTable.COLUMN_ADSIGN));
                        bannerConfig.setPosition(config.getInt(BannerConfigTable.COLUMN_POSITION));
                        bannerConfig.setShipmentTime(config.getLong("ShipmentTime"));
                        bannerConfig.setWhitelist(config.getString(BannerConfigTable.COLUMN_WHITE_LIST));
                        bannerConfig.setTimeDaily(config.getInt(BannerConfigTable.COLUMN_TIME_DAILY));
                        BannerConfigDao.getInstance(this.val$context).insertConfig(bannerConfig);
                        SPUtil.storeFirstPowerOnTime(this.val$context, System.currentTimeMillis());
                        Log.d("BannerRequester", "succeed");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private static ErrorListener errorListener() {
        return new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.d("BannerRequester", "error");
            }
        };
    }

    public static void getBannerData(Context context) {
        RequestQueue queue = RequestManager.getRequestQueue(context);
        Map<String, String> pas = new HashMap();
        pas.put(ParamConst.CID, AppConst.CID);
        pas.put(ParamConst.IMSI, SimUtil.getImsi(context));
        pas.put(ParamConst.IMEI, SimUtil.getImei(context));
        pas.put(ParamConst.VERSION_NO, String.valueOf(ApkUtil.getLocalVersionNo(context)));
        pas.put(ParamConst.MAC, new StringBuilder(String.valueOf(SPUtil.getMac())).append("_").append(SPUtil.getUnique(context)).toString());
        pas.put(ParamConst.PRODUCT_ID, AppConst.ProductId);
        if (!SPUtil.COUNTRY_DEFAULT.equalsIgnoreCase(SPUtil.getCountryCode(context))) {
            pas.put(ParamConst.COUNTRY_CODE, SPUtil.getCountryCode(context));
        }
        if (!SPUtil.DEFUALT_UID.equalsIgnoreCase(SPUtil.getUniqueID(context))) {
            pas.put(ParamConst.USER_ID, SPUtil.getUniqueID(context));
        }
        Log.d("BannerRequester", "request");
        queue.add(new JsonObjectRequest(1, "http://banner.iseeyuan.com/app/get_ad_banner", pas, successListener(context), errorListener()));
    }

    private static Listener<JSONObject> successListener(Context context) {
        return new AnonymousClass_1(context);
    }
}