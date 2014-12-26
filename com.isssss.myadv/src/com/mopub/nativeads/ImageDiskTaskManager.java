package com.mopub.nativeads;

import android.graphics.Bitmap;
import com.mopub.common.CacheService;
import com.mopub.common.CacheService.DiskLruCacheGetListener;
import java.util.Iterator;
import java.util.List;

class ImageDiskTaskManager extends TaskManager<Bitmap> {
    private final int mMaxImageWidth;
    private final List<String> mUrls;

    private class ImageDiskTaskListener implements DiskLruCacheGetListener {
        private final int mTargetWidth;

        ImageDiskTaskListener(int targetWidth) {
            this.mTargetWidth = targetWidth;
        }

        public void onComplete(String key, byte[] content) {
            if (key == null) {
                ImageDiskTaskManager.this.failAllTasks();
            } else {
                Bitmap bitmap = null;
                if (content != null) {
                    bitmap = ImageService.byteArrayToBitmap(content, this.mTargetWidth);
                }
                ImageDiskTaskManager.this.mResults.put(key, bitmap);
                if (ImageDiskTaskManager.this.mCompletedCount.incrementAndGet() == ImageDiskTaskManager.this.mSize) {
                    ImageDiskTaskManager.this.mImageTaskManagerListener.onSuccess(ImageDiskTaskManager.this.mResults);
                }
            }
        }
    }

    ImageDiskTaskManager(List<String> urls, TaskManagerListener<Bitmap> imageTaskManagerListener, int maxImageWidth) throws IllegalArgumentException {
        super(urls, imageTaskManagerListener);
        this.mMaxImageWidth = maxImageWidth;
        this.mUrls = urls;
    }

    void execute() {
        if (this.mUrls.isEmpty()) {
            this.mImageTaskManagerListener.onSuccess(this.mResults);
        }
        ImageDiskTaskListener imageDiskTaskListener = new ImageDiskTaskListener(this.mMaxImageWidth);
        Iterator it = this.mUrls.iterator();
        while (it.hasNext()) {
            CacheService.getFromDiskCacheAsync((String) it.next(), imageDiskTaskListener);
        }
    }

    void failAllTasks() {
        if (this.mFailed.compareAndSet(false, true)) {
            this.mImageTaskManagerListener.onFail();
        }
    }
}