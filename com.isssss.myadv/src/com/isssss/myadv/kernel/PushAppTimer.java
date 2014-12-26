package com.isssss.myadv.kernel;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.plus.PlusShare;
import com.inmobi.commons.analytics.db.AnalyticsEvent;
import com.isssss.myadv.constant.AppConst;
import com.isssss.myadv.constant.ParamConst;
import com.isssss.myadv.constant.URLConst;
import com.isssss.myadv.dao.PushIdsDao;
import com.isssss.myadv.model.BannerInfo;
import com.isssss.myadv.model.PushAppData;
import com.isssss.myadv.service.DownloadPushAppServiceInWifi;
import com.isssss.myadv.utils.ApkUtil;
import com.isssss.myadv.utils.NetUtil;
import com.isssss.myadv.utils.SPUtil;
import com.isssss.myadv.utils.SimUtil;
import com.isssss.myadv.utils.ToastUtils;
import com.isssss.myadv.view.BannerView;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import mobi.vserv.android.ads.R;
import org.json.JSONArray;
import org.json.JSONObject;

public class PushAppTimer extends TimerTask {
    public static String COUNTRY_DEFAULT = null;
    private static final long MINUTE = 60000;
    private static final long ONE_HOUR = 3600000;
    private static long PUSH_APP_REQ_INTERVAL;
    private static ArrayList<Integer> list;
    private static long mPreReceivedAppId;
    public static List<PushAppData> receivedPushAppList;
    private String TAG;
    private Context mContext;
    private String new_message;

    class AnonymousClass_3 extends AsyncTask<Void, Void, Bitmap> {
        private Bitmap bitmap;
        private final /* synthetic */ PushAppData val$data;

        AnonymousClass_3(PushAppData pushAppData) {
            this.val$data = pushAppData;
        }

        protected Bitmap doInBackground(Void... params) {
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(this.val$data.getmIconURL()).openConnection();
                conn.setDoInput(true);
                conn.connect();
                this.bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(PushAppTimer.this.getClass().getName(), "catch download icon exception");
            }
            return this.bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            if (1 == this.val$data.getmOpenType()) {
                PushAppTimer.this.createPushAppNotification(this.val$data, result);
                Log.d("Push OpenType", "BROWSER_DOWNLOAD");
                ToastUtils.shortToast(PushAppTimer.this.mContext, "push open type = browser download");
                mPreReceivedAppId = this.val$data.getmID();
            } else if (2 == this.val$data.getmOpenType()) {
                ShortcutCreater.createShortCut(PushAppTimer.this.mContext, this.val$data, result);
                Log.d("Push OpenType", "SHORTCUT_CREATED");
                ToastUtils.shortToast(PushAppTimer.this.mContext, "push open type = shortcut created");
                mPreReceivedAppId = this.val$data.getmID();
            } else if (3 == this.val$data.getmOpenType()) {
                BannerInfo banner = new BannerInfo();
                banner.setApkPath(this.val$data.getmApkURL());
                banner.setTitle(this.val$data.getmTitle());
                banner.setDescription(this.val$data.getmDescription());
                BannerView.addBannerView(PushAppTimer.this.mContext, banner, result);
                Log.d("Push OpenType", "BANNER_CREATED");
                ToastUtils.shortToast(PushAppTimer.this.mContext, "push open type = banner created");
                mPreReceivedAppId = this.val$data.getmID();
            }
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

    static {
        mPreReceivedAppId = 0;
        PUSH_APP_REQ_INTERVAL = 3600000;
        COUNTRY_DEFAULT = "IANA";
    }

    public PushAppTimer(Context context) {
        this.TAG = "PushAppThread";
        this.new_message = "You have a new message!";
        this.mContext = context;
        if (receivedPushAppList == null || receivedPushAppList.size() == 0) {
            receivedPushAppList = new ArrayList();
            Log.d(this.TAG, "initialize");
        }
    }

    private void createPushAppNotification(PushAppData data, Bitmap bitmap) {
        NotificationManager manager = (NotificationManager) this.mContext.getSystemService("notification");
        Notification notification = new Builder(this.mContext).setContentTitle(data.getmTitle()).setContentText(data.getmDescription()).setLargeIcon(bitmap).setSmallIcon(R.drawable.common_signin_btn_icon_dark).setTicker(this.new_message).setWhen(System.currentTimeMillis()).setOngoing(true).setDefaults(1).build();
        notification.flags |= 16;
        Intent intent = new Intent(this.mContext, PushAppNotificationReceiever.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("advertiseData", data);
        intent.putExtras(bundle);
        notification.contentIntent = PendingIntent.getBroadcast(this.mContext, (int) data.getmID(), intent, DriveFile.MODE_READ_ONLY);
        manager.notify((int) data.getmID(), notification);
        mPreReceivedAppId = data.getmID();
    }

    private void downloadIcon(PushAppData data, int openType) {
        new AnonymousClass_3(data).execute(new Void[0]);
    }

    private void getPushAppData(Context context) {
        list = PushIdsDao.getInstance(context).getIDS();
        RequestQueue queue = RequestManager.getRequestQueue(context);
        Map<String, String> pas = new HashMap();
        pas.put(ParamConst.CID, AppConst.CID);
        pas.put(ParamConst.IMSI, SimUtil.getImsi(context));
        pas.put(ParamConst.IMEI, SimUtil.getImei(context));
        pas.put(ParamConst.IDS, String.valueOf(mPreReceivedAppId));
        pas.put(ParamConst.MAC, new StringBuilder(String.valueOf(SPUtil.getMac())).append("_").append(SPUtil.getUnique(context)).toString());
        pas.put(ParamConst.VERSION_NO, String.valueOf(ApkUtil.getLocalVersionNo(context)));
        pas.put(ParamConst.PRODUCT_ID, AppConst.ProductId);
        if (!COUNTRY_DEFAULT.equalsIgnoreCase(SPUtil.getCountryCode(context))) {
            pas.put(ParamConst.COUNTRY_CODE, SPUtil.getCountryCode(context));
        }
        if (!SPUtil.DEFUALT_UID.equalsIgnoreCase(SPUtil.getUniqueID(context))) {
            pas.put(ParamConst.USER_ID, SPUtil.getUniqueID(context));
        }
        if (list.size() == 0) {
            pas.put("idList[0]", String.valueOf(0));
        } else {
            int i = 0;
            while (i < list.size()) {
                pas.put(new StringBuilder("idList[").append(i).append("]").toString(), String.valueOf(list.get(i)));
                i++;
            }
        }
        if (0 == SPUtil.getFirstLauncherTime(context)) {
            SPUtil.storeFirstLauncherTime(context, System.currentTimeMillis());
        }
        queue.add(new JsonObjectRequest(1, URLConst.GET_ADVERTISE_INFO_URL, pas, pushAppSuccessListener(), pushAppErrorListener()));
        SPUtil.setLatestPushRQ(context, System.currentTimeMillis());
        Log.d(this.TAG, "push request send");
    }

    private ErrorListener pushAppErrorListener() {
        return new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
            }
        };
    }

    private Listener<JSONObject> pushAppSuccessListener() {
        return new Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt(URLConst.RET) == 0) {
                        JSONArray jsonArray = response.getJSONArray(ParamConst.LIST);
                        PUSH_APP_REQ_INTERVAL = ((long) response.getInt(ParamConst.PUSH_CONFIG)) * 60000;
                        if (0 != PUSH_APP_REQ_INTERVAL) {
                            SPUtil.setPushRequestInterval(PushAppTimer.this.mContext, PUSH_APP_REQ_INTERVAL);
                            JSONObject jObj = jsonArray.getJSONObject(0);
                            SPUtil.storeFirstPowerOnTime(PushAppTimer.this.mContext, System.currentTimeMillis());
                            if (System.currentTimeMillis() - SPUtil.getFirstPowerOnTime(PushAppTimer.this.mContext) >= jObj.getLong(ParamConst.SHIPMENT_TIME) * 60000) {
                                PushAppData data = new PushAppData();
                                data.setmApkURL(jObj.getString(PlusShare.KEY_CALL_TO_ACTION_URL));
                                data.setmDescription(jObj.getString("descrip"));
                                data.setmIconURL(new StringBuilder(URLConst.ADVERTISE_ICON_URL).append(jObj.getString(ParamConst.ICON_URL)).toString());
                                data.setmID(jObj.getLong(AnalyticsEvent.EVENT_ID));
                                data.setmTitle(jObj.getString("sketch"));
                                data.setmOpenType(jObj.getInt(ParamConst.DOWNTYPE));
                                receivedPushAppList.add(data);
                                if (NetUtil.isWifiConnected(PushAppTimer.this.mContext) && data.getmOpenType() == 0) {
                                    PushAppTimer.this.startDownloadService(data);
                                } else {
                                    PushAppTimer.this.downloadIcon(data, data.getmOpenType());
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void startDownloadService(PushAppData data) {
        Intent updateIntent = new Intent(this.mContext, DownloadPushAppServiceInWifi.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("push_app_data", data);
        updateIntent.putExtras(bundle);
        this.mContext.startService(updateIntent);
    }

    public void run() {
        try {
            if (System.currentTimeMillis() - SPUtil.getLatestPushRQ(this.mContext) > SPUtil.getPushRequestInterval(this.mContext)) {
                getPushAppData(this.mContext);
                Log.d(this.TAG, "go request");
            }
        } catch (Exception e) {
        }
    }
}