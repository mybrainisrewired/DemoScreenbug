package com.android.systemui.screenshot;

import android.app.Notification;
import android.app.Notification.BigPictureStyle;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Process;
import android.provider.MediaStore.Images.Media;
import com.android.ex.carousel.CarouselRS;
import com.android.systemui.R;
import java.io.File;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

// compiled from: GlobalScreenshot.java
class SaveImageInBackgroundTask extends AsyncTask<SaveImageInBackgroundData, Void, SaveImageInBackgroundData> {
    private static final String SCREENSHOTS_DIR_NAME = "Screenshots";
    private static final String SCREENSHOT_FILE_NAME_TEMPLATE = "Screenshot_%s.png";
    private static final String SCREENSHOT_FILE_PATH_TEMPLATE = "%s/%s/%s";
    private static boolean mTickerAddSpace;
    private String mImageFileName;
    private String mImageFilePath;
    private long mImageTime;
    private Builder mNotificationBuilder;
    private int mNotificationId;
    private NotificationManager mNotificationManager;
    private BigPictureStyle mNotificationStyle;

    SaveImageInBackgroundTask(Context context, SaveImageInBackgroundData data, NotificationManager nManager, int nId) {
        int shortSide;
        Resources r = context.getResources();
        this.mImageTime = System.currentTimeMillis();
        String imageDate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date(this.mImageTime));
        String imageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        String str = SCREENSHOT_FILE_NAME_TEMPLATE;
        Object[] objArr = new Object[1];
        objArr[0] = imageDate;
        this.mImageFileName = String.format(str, objArr);
        str = SCREENSHOT_FILE_PATH_TEMPLATE;
        objArr = new Object[3];
        objArr[0] = imageDir;
        objArr[1] = SCREENSHOTS_DIR_NAME;
        objArr[2] = this.mImageFileName;
        this.mImageFilePath = String.format(str, objArr);
        int imageWidth = data.image.getWidth();
        int imageHeight = data.image.getHeight();
        int iconSize = data.iconSize;
        if (imageWidth < imageHeight) {
            shortSide = imageWidth;
        } else {
            shortSide = imageHeight;
        }
        Bitmap preview = Bitmap.createBitmap(shortSide, shortSide, data.image.getConfig());
        Canvas c = new Canvas(preview);
        Paint paint = new Paint();
        new ColorMatrix().setSaturation(0.25f);
        ColorFilter colorMatrixColorFilter = colorFilter;
        paint.setColorFilter(colorFilter);
        Matrix matrix = new Matrix();
        matrix.postTranslate((float) ((shortSide - imageWidth) / 2), (float) ((shortSide - imageHeight) / 2));
        c.drawBitmap(data.image, matrix, paint);
        c.drawColor(1090519039);
        Bitmap croppedIcon = Bitmap.createScaledBitmap(preview, iconSize, iconSize, true);
        mTickerAddSpace = !mTickerAddSpace;
        this.mNotificationId = nId;
        this.mNotificationManager = nManager;
        Builder builder = builder;
        Context context2 = context;
        this.mNotificationBuilder = builder.setTicker(r.getString(2131296300) + (mTickerAddSpace ? " " : "")).setContentTitle(r.getString(2131296301)).setContentText(r.getString(2131296302)).setSmallIcon(R.drawable.stat_notify_image).setWhen(System.currentTimeMillis());
        this.mNotificationStyle = new BigPictureStyle().bigPicture(preview);
        this.mNotificationBuilder.setStyle(this.mNotificationStyle);
        Notification n = this.mNotificationBuilder.build();
        n.flags |= 32;
        this.mNotificationManager.notify(nId, n);
        this.mNotificationBuilder.setLargeIcon(croppedIcon);
        this.mNotificationStyle.bigLargeIcon(null);
    }

    protected SaveImageInBackgroundData doInBackground(SaveImageInBackgroundData... params) {
        if (params.length != 1) {
            return null;
        }
        Process.setThreadPriority(-2);
        Context context = params[0].context;
        Bitmap image = params[0].image;
        Resources r = context.getResources();
        try {
            ContentValues values = new ContentValues();
            ContentResolver resolver = context.getContentResolver();
            values.put("_data", this.mImageFilePath);
            values.put("title", this.mImageFileName);
            values.put("_display_name", this.mImageFileName);
            values.put("datetaken", Long.valueOf(this.mImageTime));
            values.put("date_added", Long.valueOf(this.mImageTime));
            values.put("date_modified", Long.valueOf(this.mImageTime));
            values.put("mime_type", "image/png");
            Uri uri = resolver.insert(Media.EXTERNAL_CONTENT_URI, values);
            Intent sharingIntent = new Intent("android.intent.action.SEND");
            sharingIntent.setType("image/png");
            sharingIntent.putExtra("android.intent.extra.STREAM", uri);
            Intent chooserIntent = Intent.createChooser(sharingIntent, null);
            chooserIntent.addFlags(268468224);
            this.mNotificationBuilder.addAction(R.drawable.ic_menu_share, r.getString(17040537), PendingIntent.getActivity(context, 0, chooserIntent, 268435456));
            OutputStream out = resolver.openOutputStream(uri);
            image.compress(CompressFormat.PNG, CarouselRS.CMD_CARD_SELECTED, out);
            out.flush();
            out.close();
            values.clear();
            values.put("_size", Long.valueOf(new File(this.mImageFilePath).length()));
            resolver.update(uri, values, null, null);
            params[0].imageUri = uri;
            params[0].result = 0;
        } catch (Exception e) {
            params[0].result = 1;
        }
        return params[0];
    }

    protected void onPostExecute(SaveImageInBackgroundData params) {
        if (params.result > 0) {
            GlobalScreenshot.notifyScreenshotError(params.context, this.mNotificationManager);
        } else {
            Resources r = params.context.getResources();
            Intent launchIntent = new Intent("android.intent.action.VIEW");
            launchIntent.setDataAndType(params.imageUri, "image/png");
            launchIntent.setFlags(268435456);
            this.mNotificationBuilder.setContentTitle(r.getString(R.string.screenshot_saved_title)).setContentText(r.getString(R.string.screenshot_saved_text)).setContentIntent(PendingIntent.getActivity(params.context, 0, launchIntent, 0)).setWhen(System.currentTimeMillis()).setAutoCancel(true);
            Notification n = this.mNotificationBuilder.build();
            n.flags &= -33;
            this.mNotificationManager.notify(this.mNotificationId, n);
        }
        params.finisher.run();
    }
}