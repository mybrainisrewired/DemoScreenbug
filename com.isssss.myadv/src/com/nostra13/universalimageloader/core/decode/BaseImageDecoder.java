package com.nostra13.universalimageloader.core.decode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Build.VERSION;
import com.android.volley.DefaultRetryPolicy;
import com.google.ads.AdSize;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.nostra13.universalimageloader.utils.ImageSizeUtils;
import com.nostra13.universalimageloader.utils.IoUtils;
import com.nostra13.universalimageloader.utils.L;
import java.io.IOException;
import java.io.InputStream;

public class BaseImageDecoder implements ImageDecoder {
    protected static final String ERROR_CANT_DECODE_IMAGE = "Image can't be decoded [%s]";
    protected static final String LOG_FLIP_IMAGE = "Flip image horizontally [%s]";
    protected static final String LOG_ROTATE_IMAGE = "Rotate image on %1$d\u00b0 [%2$s]";
    protected static final String LOG_SABSAMPLE_IMAGE = "Subsample original image (%1$s) to %2$s (scale = %3$d) [%4$s]";
    protected static final String LOG_SCALE_IMAGE = "Scale subsampled image (%1$s) to %2$s (scale = %3$.5f) [%4$s]";
    protected final boolean loggingEnabled;

    protected static class ExifInfo {
        public final boolean flipHorizontal;
        public final int rotation;

        protected ExifInfo() {
            this.rotation = 0;
            this.flipHorizontal = false;
        }

        protected ExifInfo(int rotation, boolean flipHorizontal) {
            this.rotation = rotation;
            this.flipHorizontal = flipHorizontal;
        }
    }

    protected static class ImageFileInfo {
        public final ExifInfo exif;
        public final ImageSize imageSize;

        protected ImageFileInfo(ImageSize imageSize, ExifInfo exif) {
            this.imageSize = imageSize;
            this.exif = exif;
        }
    }

    public BaseImageDecoder(boolean loggingEnabled) {
        this.loggingEnabled = loggingEnabled;
    }

    protected Bitmap considerExactScaleAndOrientaiton(Bitmap subsampledBitmap, ImageDecodingInfo decodingInfo, int rotation, boolean flipHorizontal) {
        Matrix m = new Matrix();
        ImageScaleType scaleType = decodingInfo.getImageScaleType();
        if (scaleType == ImageScaleType.EXACTLY || scaleType == ImageScaleType.EXACTLY_STRETCHED) {
            float scale = ImageSizeUtils.computeImageScale(new ImageSize(subsampledBitmap.getWidth(), subsampledBitmap.getHeight(), rotation), decodingInfo.getTargetSize(), decodingInfo.getViewScaleType(), scaleType == ImageScaleType.EXACTLY_STRETCHED);
            if (Float.compare(scale, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT) != 0) {
                m.setScale(scale, scale);
                if (this.loggingEnabled) {
                    L.d(LOG_SCALE_IMAGE, new Object[]{srcSize, srcSize.scale(scale), Float.valueOf(scale), decodingInfo.getImageKey()});
                }
            }
        }
        if (flipHorizontal) {
            m.postScale(GroundOverlayOptions.NO_DIMENSION, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            if (this.loggingEnabled) {
                L.d(LOG_FLIP_IMAGE, new Object[]{decodingInfo.getImageKey()});
            }
        }
        if (rotation != 0) {
            m.postRotate((float) rotation);
            if (this.loggingEnabled) {
                L.d(LOG_ROTATE_IMAGE, new Object[]{Integer.valueOf(rotation), decodingInfo.getImageKey()});
            }
        }
        Bitmap finalBitmap = Bitmap.createBitmap(subsampledBitmap, 0, 0, subsampledBitmap.getWidth(), subsampledBitmap.getHeight(), m, true);
        if (finalBitmap != subsampledBitmap) {
            subsampledBitmap.recycle();
        }
        return finalBitmap;
    }

    public Bitmap decode(ImageDecodingInfo decodingInfo) throws IOException {
        InputStream imageStream = getImageStream(decodingInfo);
        ImageFileInfo imageInfo = defineImageSizeAndRotation(imageStream, decodingInfo.getImageUri());
        Bitmap decodedBitmap = decodeStream(resetStream(imageStream, decodingInfo), prepareDecodingOptions(imageInfo.imageSize, decodingInfo));
        if (decodedBitmap != null) {
            return considerExactScaleAndOrientaiton(decodedBitmap, decodingInfo, imageInfo.exif.rotation, imageInfo.exif.flipHorizontal);
        }
        L.e(ERROR_CANT_DECODE_IMAGE, new Object[]{decodingInfo.getImageKey()});
        return decodedBitmap;
    }

    protected Bitmap decodeStream(InputStream imageStream, Options decodingOptions) throws IOException {
        Bitmap decodeStream = BitmapFactory.decodeStream(imageStream, null, decodingOptions);
        IoUtils.closeSilently(imageStream);
        return decodeStream;
    }

    protected ExifInfo defineExifOrientation(String imageUri, String mimeType) {
        int rotation = 0;
        boolean flip = false;
        if ("image/jpeg".equalsIgnoreCase(mimeType) && Scheme.ofUri(imageUri) == Scheme.FILE) {
            try {
                switch (new ExifInterface(Scheme.FILE.crop(imageUri)).getAttributeInt("Orientation", 1)) {
                    case MMAdView.TRANSITION_FADE:
                        rotation = 0;
                        break;
                    case MMAdView.TRANSITION_UP:
                        flip = true;
                        rotation = 0;
                        break;
                    case MMAdView.TRANSITION_DOWN:
                        rotation = 180;
                        break;
                    case MMAdView.TRANSITION_RANDOM:
                        flip = true;
                        rotation = 180;
                        break;
                    case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                        flip = true;
                        rotation = 270;
                        break;
                    case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                        rotation = AdSize.LARGE_AD_HEIGHT;
                        break;
                    case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                        flip = true;
                        rotation = AdSize.LARGE_AD_HEIGHT;
                        break;
                    case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                        rotation = 270;
                        break;
                }
            } catch (IOException e) {
                L.w("Can't read EXIF tags from file [%s]", new Object[]{imageUri});
            }
        }
        return new ExifInfo(rotation, flip);
    }

    protected ImageFileInfo defineImageSizeAndRotation(InputStream imageStream, String imageUri) throws IOException {
        ExifInfo exif;
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(imageStream, null, options);
        if (VERSION.SDK_INT >= 5) {
            exif = defineExifOrientation(imageUri, options.outMimeType);
        } else {
            exif = new ExifInfo();
        }
        return new ImageFileInfo(new ImageSize(options.outWidth, options.outHeight, exif.rotation), exif);
    }

    protected InputStream getImageStream(ImageDecodingInfo decodingInfo) throws IOException {
        return decodingInfo.getDownloader().getStream(decodingInfo.getImageUri(), decodingInfo.getExtraForDownloader());
    }

    protected Options prepareDecodingOptions(ImageSize imageSize, ImageDecodingInfo decodingInfo) {
        ImageScaleType scaleType = decodingInfo.getImageScaleType();
        ImageSize targetSize = decodingInfo.getTargetSize();
        int scale = 1;
        if (scaleType != ImageScaleType.NONE) {
            boolean powerOf2;
            if (scaleType == ImageScaleType.IN_SAMPLE_POWER_OF_2) {
                powerOf2 = true;
            } else {
                powerOf2 = false;
            }
            scale = ImageSizeUtils.computeImageSampleSize(imageSize, targetSize, decodingInfo.getViewScaleType(), powerOf2);
            if (this.loggingEnabled) {
                L.d(LOG_SABSAMPLE_IMAGE, new Object[]{imageSize, imageSize.scaleDown(scale), Integer.valueOf(scale), decodingInfo.getImageKey()});
            }
        }
        Options decodingOptions = decodingInfo.getDecodingOptions();
        decodingOptions.inSampleSize = scale;
        return decodingOptions;
    }

    protected InputStream resetStream(InputStream imageStream, ImageDecodingInfo decodingInfo) throws IOException {
        try {
            imageStream.reset();
            return imageStream;
        } catch (IOException e) {
            return getImageStream(decodingInfo);
        }
    }
}