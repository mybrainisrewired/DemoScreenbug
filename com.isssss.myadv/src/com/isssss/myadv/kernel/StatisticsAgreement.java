package com.isssss.myadv.kernel;

import android.content.Context;
import android.util.Log;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.isssss.myadv.utils.ApkUtil;
import com.isssss.myadv.utils.SPUtil;
import org.json.JSONException;
import org.json.JSONObject;

public class StatisticsAgreement {
    private static String AGREEMENT_URL;
    private static String AND;
    private static int COMPANYID;
    private static String DOLLAR;
    private static String EQUALS;
    private static String STATUS;
    private static String TAG;

    class AnonymousClass_1 implements Listener<JSONObject> {
        private final /* synthetic */ Context val$context;

        AnonymousClass_1(Context context) {
            this.val$context = context;
        }

        public void onResponse(JSONObject response) {
            try {
                if (1 == response.getInt(STATUS)) {
                    Log.e(TAG, "Statistics Succeeded");
                    SPUtil.setStatisticsResult(this.val$context, true);
                } else if (response.getInt(STATUS) == 0) {
                    Log.e(TAG, "Statistics Failed");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    static {
        TAG = "StatisticsAgreement";
        AGREEMENT_URL = "http://www.mwyes.com/index.php?";
        COMPANYID = 793137847;
        DOLLAR = "$";
        STATUS = "status";
        AND = "&";
        EQUALS = "=";
    }

    private static ErrorListener errorListener(Context context) {
        return new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "\u6570\u636e\u89e3\u6790\u5f02\u5e38");
            }
        };
    }

    public static void statistics(Context context) {
        long unixtime;
        RequestQueue queue = RequestManager.getRequestQueue(context);
        if (0 != SPUtil.getFirstLauncherTime(context)) {
            unixtime = SPUtil.getFirstLauncherTime(context);
        } else {
            unixtime = System.currentTimeMillis();
        }
        queue.add(new JsonObjectRequest(0, new StringBuilder(String.valueOf(AGREEMENT_URL)).append("uuid").append(EQUALS).append(SPUtil.getMac()).append("_").append(SPUtil.getUnique(context)).append(AND).append("request_time").append(EQUALS).append(String.valueOf(unixtime)).append(AND).append("packname").append(EQUALS).append(context.getPackageName()).append(AND).append("versioncode").append(EQUALS).append(String.valueOf(ApkUtil.getLocalVersionNo(context))).append(AND).append("companyid").append(EQUALS).append(String.valueOf(COMPANYID)).toString(), null, successListener(context), errorListener(context)));
    }

    private static Listener<JSONObject> successListener(Context context) {
        return new AnonymousClass_1(context);
    }
}