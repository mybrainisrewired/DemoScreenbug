package com.facebook.ads.internal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import java.net.URL;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private static final String TAG;
    private final ImageView imageView;

    static {
        TAG = DownloadImageTask.class.getSimpleName();
    }

    public DownloadImageTask(ImageView imageView) {
        this.imageView = imageView;
    }

    protected Bitmap doInBackground(String... urls) {
        String url = urls[0];
        Bitmap bitmap = null;
        try {
            return BitmapFactory.decodeStream(new URL(url).openStream());
        } catch (Exception e) {
            Log.e(TAG, "Error downloading image: " + url, e);
            return bitmap;
        }
    }

    protected void onPostExecute(Bitmap result) {
        this.imageView.setImageBitmap(result);
    }
}