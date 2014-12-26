package com.clouds.http;

import android.content.Context;
import android.os.Handler;
import com.clouds.debug.SystemDebug;
import com.clouds.object.FileDownloadinfo;
import com.clouds.object.JSONFileInfo;
import java.util.List;

public class HttpLoadAction {
    private static final String TAG;
    private static HttpLoadAction httpLoadAction;
    private Context context;
    private Handler handler;
    private HttpFileDownloader httpFileDownloader;

    static {
        TAG = HttpLoadAction.class.getSimpleName();
        httpLoadAction = null;
    }

    private HttpLoadAction(Context context, Handler handler) {
        this.httpFileDownloader = null;
        this.httpFileDownloader = new HttpFileDownloader(context, handler);
        this.context = context;
        this.handler = handler;
    }

    public static HttpLoadAction getHttpLoadActionInstance(Context context, Handler handler) {
        if (httpLoadAction == null) {
            httpLoadAction = new HttpLoadAction(context, handler);
        }
        return httpLoadAction;
    }

    public List<FileDownloadinfo> getDownloadFileInfo() {
        return this.httpFileDownloader != null ? this.httpFileDownloader.getDownloadFileInfo() : null;
    }

    public void toLoadAction(boolean isUpdata, List<JSONFileInfo> jsonListInfos) {
        SystemDebug.e(TAG, "httpLoadAction  toLoadAction");
        SystemDebug.e(TAG, new StringBuilder("httpLoadAction  toLoadAction isUpdata: ").append(isUpdata).append("  jsonListInfos: ").append(jsonListInfos).toString());
        if (isUpdata && jsonListInfos != null) {
            SystemDebug.e(TAG, new StringBuilder("httpLoadAction  toLoadAction isUpdata: ").append(isUpdata).toString());
            if (this.httpFileDownloader != null) {
                SystemDebug.e(TAG, "httpFileDownloader  startFileDownloadThread");
                this.httpFileDownloader.startFileDownloadThread(jsonListInfos);
            }
        }
    }
}