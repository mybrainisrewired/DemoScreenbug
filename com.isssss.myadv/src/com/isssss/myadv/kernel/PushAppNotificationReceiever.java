package com.isssss.myadv.kernel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.plus.PlusShare;
import com.isssss.myadv.constant.AppConst;
import com.isssss.myadv.constant.ParamConst;
import com.isssss.myadv.constant.URLConst;
import com.isssss.myadv.dao.BannerInfoTable;
import com.isssss.myadv.dao.PushIdsDao;
import com.isssss.myadv.model.PushAppData;
import com.isssss.myadv.service.DownloadPushAppService;
import com.isssss.myadv.utils.ApkUtil;
import com.isssss.myadv.utils.FileUtil;
import com.isssss.myadv.utils.NetUtil;
import com.isssss.myadv.utils.SPUtil;
import com.isssss.myadv.utils.SimUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class PushAppNotificationReceiever extends BroadcastReceiver {
    private Context mContext;

    private void downloadApk(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            PushAppData data = (PushAppData) bundle.getSerializable("advertiseData");
            Intent intent2;
            if (data.getmOpenType() != 0) {
                intent2 = new Intent("android.intent.action.VIEW", Uri.parse(new StringBuilder("http://").append(data.getmApkURL()).toString()));
                intent2.setFlags(DriveFile.MODE_READ_ONLY);
                this.mContext.startActivity(intent2);
            } else if (NetUtil.isWifiConnected(this.mContext)) {
                File updateFile;
                if (FileUtil.isSDCardExist()) {
                    updateFile = new File(new File(Environment.getExternalStorageDirectory() + AppConst.APP_PUSH_APP_DIR) + "/" + getApkName(data.getmApkURL()));
                } else {
                    updateFile = new File(new File(new StringBuilder(String.valueOf(this.mContext.getCacheDir().getAbsolutePath())).append(AppConst.APP_PUSH_APP_DIR).toString()) + "/" + getApkName(data.getmApkURL()));
                }
                if (updateFile.exists()) {
                    Uri uri = Uri.fromFile(updateFile);
                    intent2 = new Intent("android.intent.action.VIEW");
                    intent2.addFlags(DriveFile.MODE_READ_ONLY);
                    intent2.setDataAndType(uri, "application/vnd.android.package-archive");
                    this.mContext.startActivity(intent2);
                } else {
                    return;
                }
            } else {
                startDownloadService(data);
            }
            uploadDLRecord(data.getmID());
        }
    }

    private ErrorListener errorListener() {
        return new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.d("*** AdvertisementReceiever ***", "\u4fdd\u5b58\u70b9\u51fb\u63a8\u9001\u5e94\u7528\u5931\u8d25");
            }
        };
    }

    private String getApkName(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    private void startDownloadService(PushAppData data) {
        Intent updateIntent = new Intent(this.mContext, DownloadPushAppService.class);
        updateIntent.putExtra("appname", getApkName(data.getmApkURL()));
        updateIntent.putExtra("entrance", false);
        updateIntent.putExtra(PlusShare.KEY_CALL_TO_ACTION_URL, data.getmApkURL());
        updateIntent.putExtra("advitiseID", data.getmID());
        updateIntent.putExtra(BannerInfoTable.COLUMN_TITLE, data.getmTitle());
        this.mContext.startService(updateIntent);
    }

    private Listener<JSONObject> successListener() {
        return new Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt(URLConst.RET) == 0) {
                        Log.d("*** AdvertisementReceiever ***", "uploadDLRecord succeed");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("*** AdvertisementReceiever ***", "uploadDLRecord fail");
                }
            }
        };
    }

    private void uploadDLRecord(long id) {
        ArrayList<Integer> list = PushIdsDao.getInstance(this.mContext).getIDS();
        int curr_id = new Long(id).intValue();
        if (!list.contains(Integer.valueOf(curr_id))) {
            list.add(Integer.valueOf(curr_id));
            PushIdsDao.getInstance(this.mContext).insertIds(list);
        }
        RequestQueue queue = RequestManager.getRequestQueue(this.mContext);
        Map<String, String> pas = new HashMap();
        pas.put(ParamConst.CID, AppConst.CID);
        pas.put(ParamConst.IMSI, SimUtil.getImsi(this.mContext));
        pas.put(ParamConst.IMEI, SimUtil.getImei(this.mContext));
        pas.put("appId", String.valueOf(id));
        pas.put("verisonNo", String.valueOf(ApkUtil.getLocalVersionNo(this.mContext)));
        pas.put(ParamConst.MAC, new StringBuilder(String.valueOf(SPUtil.getMac())).append("_").append(SPUtil.getUnique(this.mContext)).toString());
        if (!SPUtil.DEFUALT_UID.equalsIgnoreCase(SPUtil.getUniqueID(this.mContext))) {
            pas.put(ParamConst.USER_ID, SPUtil.getUniqueID(this.mContext));
        }
        Log.d("uploadDLRecord", "request");
        queue.add(new JsonObjectRequest(1, URLConst.RECORD_DOWNLOAD_APK_URL, pas, successListener(), errorListener()));
    }

    public void onReceive(Context context, Intent intent) {
        this.mContext = context;
        downloadApk(intent);
    }
}