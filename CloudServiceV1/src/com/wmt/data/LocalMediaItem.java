package com.wmt.data;

import android.database.Cursor;
import com.wmt.opengl.grid.ItemAnimation;
import com.wmt.remotectrl.EventPacket;
import com.wmt.scene.Scene;
import com.wmt.util.Utils;
import java.text.DateFormat;
import java.util.Date;

public abstract class LocalMediaItem extends MediaItem {
    private static final String TAG = "LocalMediaItem";
    protected int bucketId;
    protected String caption;
    protected long dateAddedInSec;
    protected long dateModifiedInSec;
    protected long dateTakenInMs;
    protected String displayName;
    protected String filePath;
    protected long fileSize;
    protected int id;
    protected double latitude;
    protected double longitude;
    protected String mimeType;

    public LocalMediaItem(Path path, long version) {
        super(path, version);
        this.latitude = 0.0d;
        this.longitude = 0.0d;
    }

    public int getBucketId() {
        return this.bucketId;
    }

    public long getDateInMs() {
        return this.dateTakenInMs;
    }

    public MediaDetails getDetails() {
        MediaDetails details = super.getDetails();
        details.addDetail(EventPacket.SHOW_IME, this.filePath);
        details.addDetail(1, this.caption);
        details.addDetail(ItemAnimation.CUR_ALPHA, DateFormat.getDateTimeInstance().format(new Date(this.dateTakenInMs)));
        if (Utils.isValidLocation(this.latitude, this.longitude)) {
            details.addDetail(ItemAnimation.CUR_ARC, new Object{this.latitude, this.longitude});
        }
        if (this.fileSize > 0) {
            details.addDetail(Scene.FLING_LEFT, Long.valueOf(this.fileSize));
        }
        return details;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void getLatLong(double[] latLong) {
        latLong[0] = this.latitude;
        latLong[1] = this.longitude;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public String getName() {
        return this.caption;
    }

    public long getSize() {
        return this.fileSize;
    }

    protected void updateContent(Cursor cursor) {
        if (updateFromCursor(cursor)) {
            this.mDataVersion = nextVersionNumber();
        }
    }

    protected abstract boolean updateFromCursor(Cursor cursor);
}