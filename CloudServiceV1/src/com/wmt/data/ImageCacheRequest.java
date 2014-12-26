package com.wmt.data;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.util.Log;
import com.wmt.data.ImageCacheService.ImageData;
import com.wmt.data.utils.BitmapUtils;
import com.wmt.data.utils.DecodeUtils;
import com.wmt.data.utils.ThreadPool.Job;
import com.wmt.data.utils.ThreadPool.JobContext;

abstract class ImageCacheRequest implements Job<Bitmap> {
    private static final String TAG = "ImageCacheRequest";
    protected ImageCacheService mCacheService;
    private Path mPath;
    private int mTargetSize;
    private int mType;

    public ImageCacheRequest(ImageCacheService service, Path path, int type, int targetSize) {
        this.mCacheService = service;
        this.mPath = path;
        this.mType = type;
        this.mTargetSize = targetSize;
    }

    public abstract Bitmap onDecodeOriginal(JobContext jobContext, int i);

    public Bitmap run(JobContext jc) {
        StringBuilder append = new StringBuilder().append(this.mPath).append(",");
        String str = this.mType == 1 ? "THUMB" : this.mType == 2 ? "MICROTHUMB" : "?";
        String debugTag = append.append(str).toString();
        ImageData data = this.mCacheService.getImageData(this.mPath, this.mType);
        if (jc.isCancelled()) {
            return null;
        }
        Bitmap bitmap;
        if (data != null) {
            Options options = new Options();
            options.inPreferredConfig = Config.ARGB_8888;
            bitmap = DecodeUtils.requestDecode(jc, data.mData, data.mOffset, data.mData.length - data.mOffset, options);
            if (bitmap != null || jc.isCancelled()) {
                return bitmap;
            }
            Log.w(TAG, "decode cached failed " + debugTag);
            return bitmap;
        } else {
            bitmap = onDecodeOriginal(jc, this.mType);
            if (jc.isCancelled()) {
                return null;
            }
            if (bitmap == null) {
                Log.w(TAG, "decode orig failed " + debugTag);
                return null;
            } else {
                if (this.mType == 2) {
                    bitmap = BitmapUtils.resizeDownAndCropCenter(bitmap, this.mTargetSize, true);
                } else {
                    bitmap = BitmapUtils.resizeDownBySideLength(bitmap, this.mTargetSize, true);
                }
                if (jc.isCancelled()) {
                    return null;
                }
                byte[] array = BitmapUtils.compressBitmap(bitmap);
                if (jc.isCancelled()) {
                    return null;
                }
                this.mCacheService.putImageData(this.mPath, this.mType, array);
                return bitmap;
            }
        }
    }
}