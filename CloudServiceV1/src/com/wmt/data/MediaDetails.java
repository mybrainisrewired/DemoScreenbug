package com.wmt.data;

import android.media.ExifInterface;
import android.util.Log;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

public class MediaDetails implements Iterable<Entry<Integer, Object>> {
    public static final int INDEX_APERTURE = 105;
    public static final int INDEX_DATETIME = 3;
    public static final int INDEX_DESCRIPTION = 2;
    public static final int INDEX_DURATION = 8;
    public static final int INDEX_EXPOSURE_TIME = 107;
    public static final int INDEX_FLASH = 102;
    public static final int INDEX_FOCAL_LENGTH = 103;
    public static final int INDEX_HEIGHT = 6;
    public static final int INDEX_ISO = 108;
    public static final int INDEX_LOCATION = 4;
    public static final int INDEX_MAKE = 100;
    public static final int INDEX_MIMETYPE = 9;
    public static final int INDEX_MODEL = 101;
    public static final int INDEX_ORIENTATION = 7;
    public static final int INDEX_PATH = 200;
    public static final int INDEX_SHUTTER_SPEED = 106;
    public static final int INDEX_SIZE = 10;
    public static final int INDEX_TITLE = 1;
    public static final int INDEX_WHITE_BALANCE = 104;
    public static final int INDEX_WIDTH = 5;
    public static final int LENGTH_UNIT_MM = 0;
    private static final String TAG = "MediaDetails";
    private TreeMap<Integer, Object> mDetails;
    private HashMap<Integer, Integer> mUnits;

    public static class FlashState {
        private static int FLASH_FIRED_MASK;
        private static int FLASH_FUNCTION_MASK;
        private static int FLASH_MODE_MASK;
        private static int FLASH_RED_EYE_MASK;
        private static int FLASH_RETURN_MASK;
        private int mState;

        static {
            FLASH_FIRED_MASK = 1;
            FLASH_RETURN_MASK = 6;
            FLASH_MODE_MASK = 24;
            FLASH_FUNCTION_MASK = 32;
            FLASH_RED_EYE_MASK = 64;
        }

        public FlashState(int state) {
            this.mState = state;
        }

        public int getFlashMode() {
            return (this.mState & FLASH_MODE_MASK) >> 3;
        }

        public int getFlashReturn() {
            return (this.mState & FLASH_RETURN_MASK) >> 1;
        }

        public boolean isFlashFired() {
            return (this.mState & FLASH_FIRED_MASK) != 0;
        }

        public boolean isFlashPresent() {
            return (this.mState & FLASH_FUNCTION_MASK) != 0;
        }

        public boolean isRedEyeModePresent() {
            return (this.mState & FLASH_RED_EYE_MASK) != 0;
        }
    }

    public MediaDetails() {
        this.mDetails = new TreeMap();
        this.mUnits = new HashMap();
    }

    public static void extractExifInfo(MediaDetails details, String filePath) {
        try {
            ExifInterface exif = new ExifInterface(filePath);
            setExifData(details, exif, "Flash", INDEX_FLASH);
            setExifData(details, exif, "ImageWidth", INDEX_WIDTH);
            setExifData(details, exif, "ImageLength", INDEX_HEIGHT);
            setExifData(details, exif, "Make", INDEX_MAKE);
            setExifData(details, exif, "Model", INDEX_MODEL);
            setExifData(details, exif, "FNumber", INDEX_APERTURE);
            setExifData(details, exif, "ISOSpeedRatings", INDEX_ISO);
            setExifData(details, exif, "WhiteBalance", INDEX_WHITE_BALANCE);
            setExifData(details, exif, "ExposureTime", INDEX_EXPOSURE_TIME);
            double data = exif.getAttributeDouble("FocalLength", MediaItem.INVALID_LATLNG);
            if (data != 0.0d) {
                details.addDetail(INDEX_FOCAL_LENGTH, Double.valueOf(data));
                details.setUnit(INDEX_FOCAL_LENGTH, LENGTH_UNIT_MM);
            }
        } catch (IOException e) {
            Log.w(TAG, "", e);
        }
    }

    private static void setExifData(MediaDetails details, ExifInterface exif, String tag, int key) {
        String value = exif.getAttribute(tag);
        if (value == null) {
            return;
        }
        if (key == 102) {
            details.addDetail(key, new FlashState(Integer.valueOf(value.toString()).intValue()));
        } else {
            details.addDetail(key, value);
        }
    }

    public void addDetail(int index, Object value) {
        this.mDetails.put(Integer.valueOf(index), value);
    }

    public Object getDetail(int index) {
        return this.mDetails.get(Integer.valueOf(index));
    }

    public int getUnit(int index) {
        return ((Integer) this.mUnits.get(Integer.valueOf(index))).intValue();
    }

    public boolean hasUnit(int index) {
        return this.mUnits.containsKey(Integer.valueOf(index));
    }

    public Iterator<Entry<Integer, Object>> iterator() {
        return this.mDetails.entrySet().iterator();
    }

    public void setUnit(int index, int unit) {
        this.mUnits.put(Integer.valueOf(index), Integer.valueOf(unit));
    }

    public int size() {
        return this.mDetails.size();
    }
}