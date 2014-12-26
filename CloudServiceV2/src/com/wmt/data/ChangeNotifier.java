package com.wmt.data;

import android.net.Uri;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChangeNotifier {
    private static final String TAG = "ChangeNotifier";
    private AtomicBoolean mContentDirty;
    private MediaSet mMediaSet;
    private Uri mUri;

    public ChangeNotifier(MediaSet set, Uri uri, DataManager dataManager) {
        this.mContentDirty = new AtomicBoolean(true);
        this.mMediaSet = set;
        dataManager.registerChangeNotifier(uri, this);
        this.mUri = uri;
    }

    public void clearDirty() {
        this.mContentDirty.set(false);
    }

    public void fakeChange() {
        onChange(false);
    }

    public boolean isDirty() {
        return this.mContentDirty.compareAndSet(true, false);
    }

    protected void onChange(boolean selfChange) {
        if (this.mContentDirty.compareAndSet(false, true)) {
            this.mMediaSet.notifyContentChanged();
        }
    }
}