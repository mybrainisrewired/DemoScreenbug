package android.support.v4.print;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.pdf.PdfDocument.Page;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.CancellationSignal.OnCancelListener;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintAttributes.MediaSize;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentAdapter.LayoutResultCallback;
import android.print.PrintDocumentAdapter.WriteResultCallback;
import android.print.PrintDocumentInfo;
import android.print.PrintDocumentInfo.Builder;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.util.Log;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

class PrintHelperKitkat {
    public static final int COLOR_MODE_COLOR = 2;
    public static final int COLOR_MODE_MONOCHROME = 1;
    private static final String LOG_TAG = "PrintHelperKitkat";
    private static final int MAX_PRINT_SIZE = 3500;
    public static final int ORIENTATION_LANDSCAPE = 1;
    public static final int ORIENTATION_PORTRAIT = 2;
    public static final int SCALE_MODE_FILL = 2;
    public static final int SCALE_MODE_FIT = 1;
    int mColorMode;
    final Context mContext;
    Options mDecodeOptions;
    private final Object mLock;
    int mOrientation;
    int mScaleMode;

    class AnonymousClass_1 extends PrintDocumentAdapter {
        private PrintAttributes mAttributes;
        final /* synthetic */ Bitmap val$bitmap;
        final /* synthetic */ int val$fittingMode;
        final /* synthetic */ String val$jobName;

        AnonymousClass_1(String str, Bitmap bitmap, int i) {
            this.val$jobName = str;
            this.val$bitmap = bitmap;
            this.val$fittingMode = i;
        }

        public void onLayout(PrintAttributes oldPrintAttributes, PrintAttributes newPrintAttributes, CancellationSignal cancellationSignal, LayoutResultCallback layoutResultCallback, Bundle bundle) {
            boolean changed = true;
            this.mAttributes = newPrintAttributes;
            PrintDocumentInfo info = new Builder(this.val$jobName).setContentType(SCALE_MODE_FIT).setPageCount(SCALE_MODE_FIT).build();
            if (newPrintAttributes.equals(oldPrintAttributes)) {
                changed = false;
            }
            layoutResultCallback.onLayoutFinished(info, changed);
        }

        public void onWrite(PageRange[] pageRanges, ParcelFileDescriptor fileDescriptor, CancellationSignal cancellationSignal, WriteResultCallback writeResultCallback) {
            PrintedPdfDocument pdfDocument = new PrintedPdfDocument(PrintHelperKitkat.this.mContext, this.mAttributes);
            try {
                Page page = pdfDocument.startPage(SCALE_MODE_FIT);
                page.getCanvas().drawBitmap(this.val$bitmap, PrintHelperKitkat.this.getMatrix(this.val$bitmap.getWidth(), this.val$bitmap.getHeight(), new RectF(page.getInfo().getContentRect()), this.val$fittingMode), null);
                pdfDocument.finishPage(page);
                try {
                    pdfDocument.writeTo(new FileOutputStream(fileDescriptor.getFileDescriptor()));
                    writeResultCallback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error writing printed content", e);
                    writeResultCallback.onWriteFailed(null);
                }
                if (pdfDocument != null) {
                    pdfDocument.close();
                }
                if (fileDescriptor != null) {
                    try {
                        fileDescriptor.close();
                    } catch (IOException e2) {
                    }
                }
            } catch (Throwable th) {
                if (pdfDocument != null) {
                    pdfDocument.close();
                }
                if (fileDescriptor != null) {
                    try {
                        fileDescriptor.close();
                    } catch (IOException e3) {
                    }
                }
            }
        }
    }

    class AnonymousClass_2 extends PrintDocumentAdapter {
        AsyncTask<Uri, Boolean, Bitmap> loadBitmap;
        private PrintAttributes mAttributes;
        Bitmap mBitmap;
        final /* synthetic */ int val$fittingMode;
        final /* synthetic */ Uri val$imageFile;
        final /* synthetic */ String val$jobName;

        class AnonymousClass_1 extends AsyncTask<Uri, Boolean, Bitmap> {
            final /* synthetic */ CancellationSignal val$cancellationSignal;
            final /* synthetic */ LayoutResultCallback val$layoutResultCallback;
            final /* synthetic */ PrintAttributes val$newPrintAttributes;
            final /* synthetic */ PrintAttributes val$oldPrintAttributes;

            AnonymousClass_1(CancellationSignal cancellationSignal, PrintAttributes printAttributes, PrintAttributes printAttributes2, LayoutResultCallback layoutResultCallback) {
                this.val$cancellationSignal = cancellationSignal;
                this.val$newPrintAttributes = printAttributes;
                this.val$oldPrintAttributes = printAttributes2;
                this.val$layoutResultCallback = layoutResultCallback;
            }

            protected Bitmap doInBackground(Uri... uris) {
                try {
                    return AnonymousClass_2.this.this$0.loadConstrainedBitmap(AnonymousClass_2.this.val$imageFile, MAX_PRINT_SIZE);
                } catch (FileNotFoundException e) {
                    return null;
                }
            }

            protected void onCancelled(Bitmap result) {
                this.val$layoutResultCallback.onLayoutCancelled();
            }

            protected void onPostExecute(Bitmap bitmap) {
                boolean changed = true;
                super.onPostExecute(bitmap);
                AnonymousClass_2.this.mBitmap = bitmap;
                if (bitmap != null) {
                    PrintDocumentInfo info = new Builder(AnonymousClass_2.this.val$jobName).setContentType(SCALE_MODE_FIT).setPageCount(SCALE_MODE_FIT).build();
                    if (this.val$newPrintAttributes.equals(this.val$oldPrintAttributes)) {
                        changed = false;
                    }
                    this.val$layoutResultCallback.onLayoutFinished(info, changed);
                } else {
                    this.val$layoutResultCallback.onLayoutFailed(null);
                }
            }

            protected void onPreExecute() {
                this.val$cancellationSignal.setOnCancelListener(new OnCancelListener() {
                    public void onCancel() {
                        AnonymousClass_1.this.this$1.cancelLoad();
                        AnonymousClass_1.this.cancel(false);
                    }
                });
            }
        }

        AnonymousClass_2(String str, Uri uri, int i) {
            this.val$jobName = str;
            this.val$imageFile = uri;
            this.val$fittingMode = i;
            this.mBitmap = null;
        }

        private void cancelLoad() {
            synchronized (PrintHelperKitkat.this.mLock) {
                if (PrintHelperKitkat.this.mDecodeOptions != null) {
                    PrintHelperKitkat.this.mDecodeOptions.requestCancelDecode();
                    PrintHelperKitkat.this.mDecodeOptions = null;
                }
            }
        }

        public void onFinish() {
            super.onFinish();
            cancelLoad();
            this.loadBitmap.cancel(true);
        }

        public void onLayout(PrintAttributes oldPrintAttributes, PrintAttributes newPrintAttributes, CancellationSignal cancellationSignal, LayoutResultCallback layoutResultCallback, Bundle bundle) {
            boolean changed = true;
            if (cancellationSignal.isCanceled()) {
                layoutResultCallback.onLayoutCancelled();
                this.mAttributes = newPrintAttributes;
            } else if (this.mBitmap != null) {
                PrintDocumentInfo info = new Builder(this.val$jobName).setContentType(SCALE_MODE_FIT).setPageCount(SCALE_MODE_FIT).build();
                if (newPrintAttributes.equals(oldPrintAttributes)) {
                    changed = false;
                }
                layoutResultCallback.onLayoutFinished(info, changed);
            } else {
                this.loadBitmap = new AnonymousClass_1(cancellationSignal, newPrintAttributes, oldPrintAttributes, layoutResultCallback);
                this.loadBitmap.execute(new Uri[0]);
                this.mAttributes = newPrintAttributes;
            }
        }

        public void onWrite(PageRange[] pageRanges, ParcelFileDescriptor fileDescriptor, CancellationSignal cancellationSignal, WriteResultCallback writeResultCallback) {
            PrintedPdfDocument pdfDocument = new PrintedPdfDocument(PrintHelperKitkat.this.mContext, this.mAttributes);
            try {
                Page page = pdfDocument.startPage(SCALE_MODE_FIT);
                page.getCanvas().drawBitmap(this.mBitmap, PrintHelperKitkat.this.getMatrix(this.mBitmap.getWidth(), this.mBitmap.getHeight(), new RectF(page.getInfo().getContentRect()), this.val$fittingMode), null);
                pdfDocument.finishPage(page);
                try {
                    pdfDocument.writeTo(new FileOutputStream(fileDescriptor.getFileDescriptor()));
                    writeResultCallback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error writing printed content", e);
                    writeResultCallback.onWriteFailed(null);
                }
                if (pdfDocument != null) {
                    pdfDocument.close();
                }
                if (fileDescriptor != null) {
                    try {
                        fileDescriptor.close();
                    } catch (IOException e2) {
                    }
                }
            } catch (Throwable th) {
                if (pdfDocument != null) {
                    pdfDocument.close();
                }
                if (fileDescriptor != null) {
                    try {
                        fileDescriptor.close();
                    } catch (IOException e3) {
                    }
                }
            }
        }
    }

    PrintHelperKitkat(Context context) {
        this.mDecodeOptions = null;
        this.mLock = new Object();
        this.mScaleMode = 2;
        this.mColorMode = 2;
        this.mOrientation = 1;
        this.mContext = context;
    }

    private Matrix getMatrix(int imageWidth, int imageHeight, RectF content, int fittingMode) {
        Matrix matrix = new Matrix();
        float scale = content.width() / ((float) imageWidth);
        if (fittingMode == 2) {
            scale = Math.max(scale, content.height() / ((float) imageHeight));
        } else {
            scale = Math.min(scale, content.height() / ((float) imageHeight));
        }
        matrix.postScale(scale, scale);
        matrix.postTranslate((content.width() - (((float) imageWidth) * scale)) / 2.0f, (content.height() - (((float) imageHeight) * scale)) / 2.0f);
        return matrix;
    }

    private Bitmap loadBitmap(Uri uri, Options o) throws FileNotFoundException {
        if (uri == null || this.mContext == null) {
            throw new IllegalArgumentException("bad argument to loadBitmap");
        }
        InputStream is = null;
        try {
            is = this.mContext.getContentResolver().openInputStream(uri);
            Bitmap decodeStream = BitmapFactory.decodeStream(is, null, o);
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    Log.w(LOG_TAG, "close fail ", e);
                }
            }
            return decodeStream;
        } catch (Throwable th) {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e2) {
                    Log.w(LOG_TAG, "close fail ", e2);
                }
            }
        }
    }

    private Bitmap loadConstrainedBitmap(Uri uri, int maxSideLength) throws FileNotFoundException {
        Bitmap bitmap = null;
        if (maxSideLength <= 0 || uri == null || this.mContext == null) {
            throw new IllegalArgumentException("bad argument to getScaledBitmap");
        }
        Options opt = new Options();
        opt.inJustDecodeBounds = true;
        loadBitmap(uri, opt);
        int w = opt.outWidth;
        int h = opt.outHeight;
        if (w > 0 && h > 0) {
            int imageSide = Math.max(w, h);
            int sampleSize = SCALE_MODE_FIT;
            while (imageSide > maxSideLength) {
                imageSide >>>= 1;
                sampleSize <<= 1;
            }
            if (sampleSize > 0 && Math.min(w, h) / sampleSize > 0) {
                Options decodeOptions;
                synchronized (this.mLock) {
                    this.mDecodeOptions = new Options();
                    this.mDecodeOptions.inMutable = true;
                    this.mDecodeOptions.inSampleSize = sampleSize;
                    decodeOptions = this.mDecodeOptions;
                }
                try {
                    bitmap = loadBitmap(uri, decodeOptions);
                    synchronized (this.mLock) {
                        this.mDecodeOptions = null;
                    }
                } catch (Throwable th) {
                    synchronized (this.mLock) {
                    }
                    this.mDecodeOptions = null;
                }
            }
        }
        return bitmap;
    }

    public int getColorMode() {
        return this.mColorMode;
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public int getScaleMode() {
        return this.mScaleMode;
    }

    public void printBitmap(String jobName, Bitmap bitmap) {
        if (bitmap != null) {
            int fittingMode = this.mScaleMode;
            PrintManager printManager = (PrintManager) this.mContext.getSystemService("print");
            MediaSize mediaSize = MediaSize.UNKNOWN_PORTRAIT;
            if (bitmap.getWidth() > bitmap.getHeight()) {
                mediaSize = MediaSize.UNKNOWN_LANDSCAPE;
            }
            printManager.print(jobName, new AnonymousClass_1(jobName, bitmap, fittingMode), new PrintAttributes.Builder().setMediaSize(mediaSize).setColorMode(this.mColorMode).build());
        }
    }

    public void printBitmap(String jobName, Uri imageFile) throws FileNotFoundException {
        PrintDocumentAdapter printDocumentAdapter = new AnonymousClass_2(jobName, imageFile, this.mScaleMode);
        PrintManager printManager = (PrintManager) this.mContext.getSystemService("print");
        PrintAttributes.Builder builder = new PrintAttributes.Builder();
        builder.setColorMode(this.mColorMode);
        if (this.mOrientation == 1) {
            builder.setMediaSize(MediaSize.UNKNOWN_LANDSCAPE);
        } else if (this.mOrientation == 2) {
            builder.setMediaSize(MediaSize.UNKNOWN_PORTRAIT);
        }
        printManager.print(jobName, printDocumentAdapter, builder.build());
    }

    public void setColorMode(int colorMode) {
        this.mColorMode = colorMode;
    }

    public void setOrientation(int orientation) {
        this.mOrientation = orientation;
    }

    public void setScaleMode(int scaleMode) {
        this.mScaleMode = scaleMode;
    }
}