package com.android.systemui;

import android.app.ActivityManagerNative;
import android.app.IActivityManager;
import android.app.IApplicationController.Stub;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class SoftwinnerService extends Service {
    private static final String ACTION_CONNECT = "android.net.conn.CONNECTIVITY_CHANGE";
    private static final boolean DEBUG = false;
    private static final String TAG = "softwinnerService";
    private static final String Url = "http://www.softwinners.com/devinfosrv";
    private IActivityManager mAM;
    private String mChipID;
    private List<PackageInfo> mExtraPackageInfo;
    private SharedPreferences mPackage;
    private Editor mPackageEditor;
    private PackageManager mPackageManager;
    private SharedPreferences mSign;
    private Editor mSignEditor;

    private class ApplicationController extends Stub {
        private ApplicationController() {
        }

        public void applicationStarting(Intent intent) {
            if (!SoftwinnerService.this.mSign.getBoolean("post-finish", DEBUG)) {
                SoftwinnerService.this.isExtraPackage(intent.getComponent().getPackageName());
            }
        }
    }

    private class ConnectivityReceive extends BroadcastReceiver {
        private ConnectivityReceive() {
        }

        public void onReceive(Context context, Intent intent) {
            if (SoftwinnerService.this.checkConnectivity(context)) {
                SoftwinnerService softwinnerService;
                String str;
                String access$600;
                String ip = SoftwinnerService.this.getLocalIpAddress();
                if (!(ip == null || ip.equals(SoftwinnerService.this.mSign.getString("ipAddress", "null")))) {
                    softwinnerService = SoftwinnerService.this;
                    str = "IpChange";
                    access$600 = SoftwinnerService.this.mChipID;
                }
                if (!SoftwinnerService.this.mSign.getBoolean("post-list", DEBUG)) {
                    StringBuffer data = new StringBuffer();
                    int i = 0;
                    while (i < SoftwinnerService.this.mExtraPackageInfo.size()) {
                        data.append(((PackageInfo) SoftwinnerService.this.mExtraPackageInfo.get(i)).packageName + ",");
                        i++;
                    }
                    softwinnerService = SoftwinnerService.this;
                    str = "PackageList";
                    access$600 = SoftwinnerService.this.mChipID;
                    String substring = data.substring(0, data.length() - 1);
                }
                SoftwinnerService.this.checkToPost();
            }
        }
    }

    private class PostTask extends Thread {
        int CHECK_TIMEOUT;
        String chipid;
        String command;
        String data;

        public PostTask(String command, String chipid, String data) {
            this.CHECK_TIMEOUT = 10000;
            this.command = command;
            this.chipid = chipid;
            this.data = data;
            start();
        }

        private void sentPost() {
            HttpPost post = new HttpPost(Url);
            List<NameValuePair> params = new ArrayList();
            params.add(new BasicNameValuePair("command", this.command));
            params.add(new BasicNameValuePair("chipid", this.chipid));
            params.add(new BasicNameValuePair("data", this.data));
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setSoTimeout(httpParameters, this.CHECK_TIMEOUT);
            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            try {
                post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                if (httpClient.execute(post).getStatusLine().getStatusCode() != 200) {
                    return;
                }
                if ("PackageList".equals(this.command)) {
                    SoftwinnerService.this.mSignEditor.putBoolean("post-list", true);
                    SoftwinnerService.this.mSignEditor.commit();
                } else if ("PackageStatus".equals(this.command)) {
                    String[] packageName = this.data.split(",");
                    int i = 0;
                    while (i < packageName.length) {
                        SoftwinnerService.this.mPackageEditor.remove(packageName[i]);
                        SoftwinnerService.this.mPackageEditor.commit();
                        i++;
                    }
                } else if ("IpChange".equals(this.command)) {
                    SoftwinnerService.this.mSignEditor.putString("ipAddress", this.data);
                    SoftwinnerService.this.mSignEditor.commit();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void run() {
            sentPost();
        }
    }

    public SoftwinnerService() {
        this.mChipID = null;
    }

    private void checkToPost() {
        if (checkConnectivity(this)) {
            int nullNum = 0;
            Map<String, String> mMap = this.mPackage.getAll();
            Set<String> key = mMap.keySet();
            StringBuffer data = new StringBuffer();
            Iterator it = key.iterator();
            while (it.hasNext()) {
                String packName = (String) it.next();
                if (packName != null) {
                    String status = (String) mMap.get(packName);
                    if (status != null) {
                        if (status.equals("run")) {
                            data.append(packName + ",");
                        } else if (status.equals("null")) {
                            nullNum++;
                        }
                    }
                }
            }
            if (data.length() > 4) {
                String str = "PackageStatus";
                String str2 = this.mChipID;
                String substring = data.substring(0, data.length() - 1);
            }
            if (nullNum == 0) {
                this.mSignEditor.putBoolean("post-finish", true);
                this.mSignEditor.commit();
            }
        }
    }

    private String getChipIDHex() {
        IOException io;
        String chipid = null;
        StringBuilder cpuinfo = new StringBuilder();
        try {
            FileInputStream fis = new FileInputStream("/proc/cpuinfo");
            try {
                byte[] buf = new byte[1024];
                while (true) {
                    int len = fis.read(buf);
                    if (len <= 0) {
                        break;
                    }
                    cpuinfo.append(new String(buf, 0, len));
                }
                chipid = cpuinfo.toString();
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                io = e;
                io.printStackTrace();
                chipid = chipid.substring(chipid.indexOf("Serial"));
                int index = chipid.indexOf(": ");
                return chipid.substring(index + 2, index + 34);
            }
        } catch (IOException e2) {
            io = e2;
            io.printStackTrace();
            chipid = chipid.substring(chipid.indexOf("Serial"));
            int index2 = chipid.indexOf(": ");
            return chipid.substring(index2 + 2, index2 + 34);
        }
        chipid = chipid.substring(chipid.indexOf("Serial"));
        int index22 = chipid.indexOf(": ");
        return chipid.substring(index22 + 2, index22 + 34);
    }

    private List<PackageInfo> getExtraPackage() {
        List<PackageInfo> extraPackageInfo = new ArrayList();
        List<PackageInfo> allPackageInfo = this.mPackageManager.getInstalledPackages(1);
        String[] googlePackages = getResources().getStringArray(R.array.google_package);
        int i = 0;
        while (i < allPackageInfo.size()) {
            PackageInfo pki = (PackageInfo) allPackageInfo.get(i);
            int i2 = pki.applicationInfo.flags;
            ApplicationInfo applicationInfo = pki.applicationInfo;
            if ((i2 & 1) > 0) {
                int j = 0;
                while (j < googlePackages.length && !pki.packageName.equals(googlePackages[j])) {
                    if (j == googlePackages.length - 1) {
                        extraPackageInfo.add(pki);
                    }
                    j++;
                }
            }
            i++;
        }
        return extraPackageInfo;
    }

    private void initPreference() {
        if (!this.mSign.getBoolean("storage", DEBUG)) {
            int i = 0;
            while (i < this.mExtraPackageInfo.size()) {
                this.mPackageEditor.putString(((PackageInfo) this.mExtraPackageInfo.get(i)).packageName, "null");
                this.mPackageEditor.commit();
                i++;
            }
            this.mSignEditor.putBoolean("storage", true);
            this.mSignEditor.commit();
        }
    }

    private boolean isExtraPackage(String packagename) {
        if (packagename == null) {
            return DEBUG;
        }
        Map<String, String> mMap = this.mPackage.getAll();
        Iterator it = mMap.keySet().iterator();
        while (it.hasNext()) {
            String packName = (String) it.next();
            if (packagename.equals(packName)) {
                if ("null".equals((String) mMap.get(packName))) {
                    this.mPackageEditor.putString(packagename, "run");
                    this.mPackageEditor.commit();
                }
                return true;
            }
        }
        return DEBUG;
    }

    public boolean checkConnectivity(Context context) {
        NetworkInfo info = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return (info == null || !info.isConnected()) ? DEBUG : true;
    }

    public String getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                Enumeration<InetAddress> enumIpAddr = ((NetworkInterface) en.nextElement()).getInetAddresses();
                while (enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (inetAddress.isSiteLocalAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public IBinder onBind(Intent intent) {
        Log.v(TAG, "onBind");
        return null;
    }

    public void onCreate() {
        this.mPackageManager = getPackageManager();
        this.mPackage = getSharedPreferences("mPackage", 0);
        this.mSign = getSharedPreferences("mSign", 0);
        this.mPackageEditor = this.mPackage.edit();
        this.mSignEditor = this.mSign.edit();
        this.mAM = ActivityManagerNative.getDefault();
        try {
            this.mAM.setApplicationController(new ApplicationController(null));
        } catch (RemoteException e) {
            Log.e(TAG, e.toString());
        }
        this.mExtraPackageInfo = getExtraPackage();
        initPreference();
        this.mChipID = getChipIDHex();
        ConnectivityReceive mConReceive = new ConnectivityReceive(null);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_CONNECT);
        registerReceiver(mConReceive, intentFilter);
    }
}