package com.isssss.myadv.utils;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.media.TransportMediator;
import android.util.Log;
import com.mopub.common.Preconditions;
import java.io.IOException;
import java.io.InputStream;

public class TextUtils {
    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != -1 ? Config.ARGB_8888 : Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static String getAdvID(Context context) {
        String advId = Preconditions.EMPTY_ARGUMENTS;
        try {
            return context.getPackageManager().getApplicationInfo(context.getPackageName(), TransportMediator.FLAG_KEY_MEDIA_NEXT).metaData.getString("adv_id");
        } catch (NameNotFoundException e) {
            Log.e("fail", new StringBuilder("Failed to load meta-data, APPKEY_NameNotFound: ").append(e.getMessage()).toString());
            return advId;
        } catch (NullPointerException e2) {
            Log.e("fail", new StringBuilder("Failed to load meta-data, APPKEY_NullPointer: ").append(e2.getMessage()).toString());
            return advId;
        }
    }

    public static BitmapDrawable getImageFromAssetsFile(Context context, String fileName) {
        BitmapDrawable drawable = null;
        try {
            InputStream is = context.getResources().getAssets().open(fileName);
            BitmapDrawable drawable2 = new BitmapDrawable(BitmapFactory.decodeStream(is));
            try {
                is.close();
                return drawable2;
            } catch (IOException e) {
                e = e;
                drawable = drawable2;
                e.printStackTrace();
                return drawable;
            }
        } catch (IOException e2) {
            IOException e3 = e2;
            e3.printStackTrace();
            return drawable;
        }
    }

    public static boolean isEmpty(String str) {
        return str == null || str.equals(Preconditions.EMPTY_ARGUMENTS);
    }

    public static String makeAmount(int fee, String amount) {
        String strUnit = new String();
        String strValue = new String();
        if (fee <= 1) {
            return amount;
        }
        amount = amount.trim();
        if (!(amount == null || Preconditions.EMPTY_ARGUMENTS.equals(amount))) {
            int i = 0;
            while (i < amount.length()) {
                if ((amount.charAt(i) < '0' || amount.charAt(i) > '9') && amount.charAt(i) != '.') {
                    strUnit = new StringBuilder(String.valueOf(strUnit)).append(amount.charAt(i)).toString();
                } else {
                    strValue = new StringBuilder(String.valueOf(strValue)).append(amount.charAt(i)).toString();
                }
                i++;
            }
        }
        try {
            strValue = String.valueOf(Double.parseDouble(strValue) * ((double) fee));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new StringBuilder(String.valueOf(strUnit)).append(strValue).toString();
    }
}