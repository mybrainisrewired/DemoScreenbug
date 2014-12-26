package com.android.ex.carousel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CarouselViewUtilities {
    public static boolean writeBitmapToFile(Context context, Bitmap bitmap, String filename) {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File file = new File(path, filename);
        boolean result = false;
        try {
            path.mkdirs();
            OutputStream os = new FileOutputStream(file);
            MediaScannerConnection.scanFile(context, new String[]{file.toString()}, null, null);
            bitmap.compress(CompressFormat.PNG, CarouselRS.CMD_CARD_SELECTED, os);
            return true;
        } catch (IOException e) {
            Log.w("ExternalStorage", "Error writing " + file, e);
            return result;
        }
    }
}