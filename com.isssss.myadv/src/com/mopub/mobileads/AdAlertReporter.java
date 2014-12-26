package com.mopub.mobileads;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.view.View;
import com.google.android.gms.drive.DriveFile;
import com.inmobi.monetization.internal.imai.db.ClickDatabaseManager;
import com.mopub.common.Preconditions;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.DateAndTime;
import com.mopub.common.util.Streams;
import com.mopub.mobileads.util.Base64;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdAlertReporter {
    private static final String BODY_SEPARATOR = "\n=================\n";
    private static final String DATE_FORMAT_PATTERN = "M/d/yy hh:mm:ss a z";
    private static final String EMAIL_RECIPIENT = "creative-review@mopub.com";
    private static final String EMAIL_SCHEME = "mailto:";
    private static final int IMAGE_QUALITY = 25;
    private static final String MARKUP_FILENAME = "mp_adalert_markup.html";
    private static final String PARAMETERS_FILENAME = "mp_adalert_parameters.txt";
    private static final String SCREEN_SHOT_FILENAME = "mp_adalert_screenshot.png";
    private final AdConfiguration mAdConfiguration;
    private final Context mContext;
    private final String mDateString;
    private ArrayList<Uri> mEmailAttachments;
    private Intent mEmailIntent;
    private String mParameters;
    private String mResponse;
    private final View mView;

    public AdAlertReporter(Context context, View view, AdConfiguration adConfiguration) {
        this.mView = view;
        this.mContext = context;
        this.mAdConfiguration = adConfiguration;
        this.mEmailAttachments = new ArrayList();
        this.mDateString = new SimpleDateFormat(DATE_FORMAT_PATTERN).format(DateAndTime.now());
        initEmailIntent();
        Bitmap screenShot = takeScreenShot();
        String screenShotString = convertBitmapInWEBPToBase64EncodedString(screenShot);
        this.mParameters = formParameters();
        this.mResponse = getResponseString();
        addEmailSubject();
        addEmailBody(new String[]{this.mParameters, this.mResponse, screenShotString});
        addTextAttachment(PARAMETERS_FILENAME, this.mParameters);
        addTextAttachment(MARKUP_FILENAME, this.mResponse);
        addImageAttachment(SCREEN_SHOT_FILENAME, screenShot);
    }

    private void addEmailBody(String... data) {
        StringBuilder body = new StringBuilder();
        int i = 0;
        while (i < data.length) {
            body.append(data[i]);
            if (i != data.length - 1) {
                body.append(BODY_SEPARATOR);
            }
            i++;
        }
        this.mEmailIntent.putExtra("android.intent.extra.TEXT", body.toString());
    }

    private void addEmailSubject() {
        this.mEmailIntent.putExtra("android.intent.extra.SUBJECT", new StringBuilder("New creative violation report - ").append(this.mDateString).toString());
    }

    private void addImageAttachment(String fileName, Bitmap bitmap) {
        if (fileName != null && bitmap != null) {
            try {
                FileOutputStream fileOutputStream = this.mContext.openFileOutput(fileName, 1);
                bitmap.compress(CompressFormat.PNG, IMAGE_QUALITY, fileOutputStream);
                this.mEmailAttachments.add(Uri.fromFile(new File(this.mContext.getFilesDir() + File.separator + fileName)));
                Streams.closeStream(fileOutputStream);
            } catch (Exception e) {
                MoPubLog.d(new StringBuilder("Unable to write text attachment to file: ").append(fileName).toString());
                Streams.closeStream(null);
            }
        }
    }

    private void addTextAttachment(String fileName, String body) {
        if (fileName != null && body != null) {
            try {
                FileOutputStream fileOutputStream = this.mContext.openFileOutput(fileName, 1);
                fileOutputStream.write(body.getBytes());
                this.mEmailAttachments.add(Uri.fromFile(new File(this.mContext.getFilesDir() + File.separator + fileName)));
                Streams.closeStream(fileOutputStream);
            } catch (Exception e) {
                MoPubLog.d(new StringBuilder("Unable to write text attachment to file: ").append(fileName).toString());
                Streams.closeStream(null);
            }
        }
    }

    private void appendKeyValue(StringBuilder parameters, String key, String value) {
        parameters.append(key);
        parameters.append(" : ");
        parameters.append(value);
        parameters.append("\n");
    }

    private String convertBitmapInWEBPToBase64EncodedString(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.JPEG, IMAGE_QUALITY, byteArrayOutputStream);
            return Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0);
        } catch (Exception e) {
            return null;
        }
    }

    private String formParameters() {
        StringBuilder parameters = new StringBuilder();
        if (this.mAdConfiguration != null) {
            appendKeyValue(parameters, "sdk_version", this.mAdConfiguration.getSdkVersion());
            appendKeyValue(parameters, "creative_id", this.mAdConfiguration.getDspCreativeId());
            appendKeyValue(parameters, "platform_version", Integer.toString(this.mAdConfiguration.getPlatformVersion()));
            appendKeyValue(parameters, "device_model", this.mAdConfiguration.getDeviceModel());
            appendKeyValue(parameters, "ad_unit_id", this.mAdConfiguration.getAdUnitId());
            appendKeyValue(parameters, "device_locale", this.mAdConfiguration.getDeviceLocale());
            appendKeyValue(parameters, "device_id", this.mAdConfiguration.getHashedUdid());
            appendKeyValue(parameters, "network_type", this.mAdConfiguration.getNetworkType());
            appendKeyValue(parameters, "platform", this.mAdConfiguration.getPlatform());
            appendKeyValue(parameters, ClickDatabaseManager.COLUMN_TIMESTAMP, getFormattedTimeStamp(this.mAdConfiguration.getTimeStamp()));
            appendKeyValue(parameters, "ad_type", this.mAdConfiguration.getAdType());
            appendKeyValue(parameters, "ad_size", new StringBuilder("{").append(this.mAdConfiguration.getWidth()).append(", ").append(this.mAdConfiguration.getHeight()).append("}").toString());
        }
        return parameters.toString();
    }

    private String getFormattedTimeStamp(long timeStamp) {
        return timeStamp != -1 ? new SimpleDateFormat(DATE_FORMAT_PATTERN).format(new Date(timeStamp)) : null;
    }

    private String getResponseString() {
        return this.mAdConfiguration != null ? this.mAdConfiguration.getResponseString() : Preconditions.EMPTY_ARGUMENTS;
    }

    private void initEmailIntent() {
        this.mEmailIntent = new Intent("android.intent.action.SEND_MULTIPLE", Uri.parse(EMAIL_SCHEME));
        this.mEmailIntent.setType("plain/text");
        this.mEmailIntent.putExtra("android.intent.extra.EMAIL", new String[]{EMAIL_RECIPIENT});
    }

    private Bitmap takeScreenShot() {
        if (this.mView == null || this.mView.getRootView() == null) {
            return null;
        }
        View rootView = this.mView.getRootView();
        boolean wasDrawingCacheEnabled = rootView.isDrawingCacheEnabled();
        rootView.setDrawingCacheEnabled(true);
        Bitmap drawingCache = rootView.getDrawingCache();
        if (drawingCache == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(drawingCache);
        rootView.setDrawingCacheEnabled(wasDrawingCacheEnabled);
        return bitmap;
    }

    @Deprecated
    ArrayList<Uri> getEmailAttachments() {
        return this.mEmailAttachments;
    }

    @Deprecated
    Intent getEmailIntent() {
        return this.mEmailIntent;
    }

    @Deprecated
    String getParameters() {
        return this.mParameters;
    }

    @Deprecated
    String getResponse() {
        return this.mResponse;
    }

    public void send() {
        this.mEmailIntent.putParcelableArrayListExtra("android.intent.extra.STREAM", this.mEmailAttachments);
        Intent chooserIntent = Intent.createChooser(this.mEmailIntent, "Send Email...");
        chooserIntent.addFlags(DriveFile.MODE_READ_ONLY);
        this.mContext.startActivity(chooserIntent);
    }
}