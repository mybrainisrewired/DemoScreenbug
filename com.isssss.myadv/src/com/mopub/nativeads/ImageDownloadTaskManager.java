package com.mopub.nativeads;

import com.mopub.common.DownloadResponse;
import com.mopub.common.DownloadTask;
import com.mopub.common.DownloadTask.DownloadTaskListener;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.AsyncTasks;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

class ImageDownloadTaskManager extends TaskManager<DownloadResponse> {
    private final Map<HttpUriRequest, DownloadTask> mDownloadTasks;
    private final int mRequestedWidth;

    private class ImageDownloadTaskListener implements DownloadTaskListener {
        private ImageDownloadTaskListener() {
        }

        public void onComplete(String url, DownloadResponse downloadResponse) {
            if (downloadResponse == null || downloadResponse.getStatusCode() != 200) {
                MoPubLog.d(new StringBuilder("Failed to download image: ").append(url).toString());
                ImageDownloadTaskManager.this.failAllTasks();
            } else {
                MoPubLog.d(new StringBuilder("Successfully downloaded image bye array: ").append(url).toString());
                ImageDownloadTaskManager.this.mResults.put(url, downloadResponse);
                if (ImageDownloadTaskManager.this.mCompletedCount.incrementAndGet() == ImageDownloadTaskManager.this.mSize) {
                    ImageDownloadTaskManager.this.mImageTaskManagerListener.onSuccess(ImageDownloadTaskManager.this.mResults);
                }
            }
        }
    }

    ImageDownloadTaskManager(List<String> urls, TaskManagerListener<DownloadResponse> imageTaskManagerListener, int requestedWidth) throws IllegalArgumentException {
        super(urls, imageTaskManagerListener);
        this.mRequestedWidth = requestedWidth;
        DownloadTaskListener downloadTaskListener = new ImageDownloadTaskListener(null);
        this.mDownloadTasks = new HashMap(urls.size());
        Iterator it = urls.iterator();
        while (it.hasNext()) {
            this.mDownloadTasks.put(new HttpGet((String) it.next()), new DownloadTask(downloadTaskListener));
        }
    }

    void execute() {
        if (this.mDownloadTasks.isEmpty()) {
            this.mImageTaskManagerListener.onSuccess(this.mResults);
        }
        Iterator it = this.mDownloadTasks.entrySet().iterator();
        while (it.hasNext()) {
            try {
                AsyncTasks.safeExecuteOnExecutor((DownloadTask) ((Entry) it.next()).getValue(), new HttpUriRequest[]{(HttpUriRequest) entry.getKey()});
            } catch (Exception e) {
                MoPubLog.d("Failed to download image", e);
                this.mImageTaskManagerListener.onFail();
            }
        }
    }

    void failAllTasks() {
        if (this.mFailed.compareAndSet(false, true)) {
            Iterator it = this.mDownloadTasks.values().iterator();
            while (it.hasNext()) {
                ((DownloadTask) it.next()).cancel(true);
            }
            this.mImageTaskManagerListener.onFail();
        }
    }
}