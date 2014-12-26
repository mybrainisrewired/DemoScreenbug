package com.wmt.data;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Audio.Artists;
import android.provider.MediaStore.Audio.Media;
import android.util.Log;
import com.wmt.MusicPlayer.MusicUtils;
import java.util.ArrayList;
import java.util.Iterator;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;

public class LocalAudioArtist extends LocalAudioGroup {
    private static final String TAG = "LocalAudioAlbum";
    private final int mArtistID;
    private final String mArtistKey;
    private final ContentResolver mContentResolver;
    private final String mName;

    public LocalAudioArtist(DataManager dataManager, Path path, ContentResolver resolver, int artistId) {
        super(path, dataManager, artistId);
        this.mContentResolver = resolver;
        this.mArtistID = artistId;
        Uri uri = Artists.getContentUri(DataManager.getDeviceString(DataManager.getDeviceFromPath(path)));
        Cursor cursor = this.mContentResolver.query(uri, new String[]{"artist", "artist_key"}, MusicUtils.THUMBDATA_WHERE_CLAUSE, new String[]{String.valueOf(this.mId)}, null);
        if (cursor != null) {
            cursor.moveToNext();
            this.mName = cursor.getString(0);
            this.mArtistKey = cursor.getString(1);
            cursor.close();
        } else {
            this.mName = "";
            this.mArtistKey = "";
            Log.w(TAG, "cannot open local database: " + this.mBaseUri);
        }
    }

    public LocalAudioArtist(DataManager dataManager, Path path, ContentResolver resolver, int artistId, String artistKey, String artistName) {
        super(path, dataManager, artistId);
        this.mContentResolver = resolver;
        this.mName = artistName;
        this.mArtistKey = artistKey;
        this.mArtistID = artistId;
    }

    public void delete() {
        this.mContentResolver.delete(this.mBaseUri, "artist_id=?", new String[]{String.valueOf(this.mId)});
    }

    protected Uri getBaseUri(Path path, int id) {
        return Media.getContentUri(DataManager.getDeviceString(DataManager.getDeviceFromPath(path)));
    }

    protected ArrayList<MediaItem> getCachedList() {
        int device = DataManager.getDeviceFromPath(this.mItemPath);
        int typeBits = device == 1 ? Opcodes.IINC : device == 2 ? DataManager.INCLUDE_USB_AUDIO_ONLY : DataManager.INCLUDE_LOCAL_AUDIO_ONLY;
        ArrayList<MediaItem> cachedAllList = this.mDataManager.getCachedMediaItemList(this.mDataManager.getTopSetPath(typeBits));
        if (cachedAllList == null) {
            return null;
        }
        ArrayList<MediaItem> artistList = new ArrayList();
        Iterator i$ = cachedAllList.iterator();
        while (i$.hasNext()) {
            MediaItem item = (MediaItem) i$.next();
            if (item.getArtistID() == this.mArtistID) {
                artistList.add(item);
            }
        }
        return artistList;
    }

    protected Cursor getCountCursor() {
        if (this.mFilterSize > 0) {
            return this.mContentResolver.query(this.mBaseUri, COUNT_PROJECTION, "artist_id=? and _size>?", new String[]{String.valueOf(this.mId), String.valueOf(this.mFilterSize)}, null);
        } else {
            return this.mContentResolver.query(this.mBaseUri, COUNT_PROJECTION, "artist_id=?", new String[]{String.valueOf(this.mId)}, null);
        }
    }

    public String getName() {
        return this.mName;
    }

    protected Cursor getQueryCursor(int start, int count) {
        Uri uri = this.mBaseUri.buildUpon().appendQueryParameter("limit", start + "," + count).build();
        if (this.mFilterSize > 0) {
            return this.mContentResolver.query(uri, LocalAudio.getProjection(this.mDataManager.isBigThumbDataEnabled()), "artist_id=? and _size>?", new String[]{String.valueOf(this.mId), String.valueOf(this.mFilterSize)}, this.mOrderByField);
        } else {
            return this.mContentResolver.query(uri, LocalAudio.getProjection(this.mDataManager.isBigThumbDataEnabled()), "artist_id=?", new String[]{String.valueOf(this.mId)}, this.mOrderByField);
        }
    }

    public int getSupportedOperations() {
        return 1029;
    }

    public boolean isLeafAlbum() {
        return true;
    }

    protected MediaItem loadOrUpdateItem(Path path, Cursor queryCursor, int id) {
        LocalMediaItem item = (LocalMediaItem) path.getObject();
        if (item == null) {
            return new LocalAudio(path, this.mContentResolver, queryCursor, this.mDataManager.isBigThumbDataEnabled());
        }
        item.updateContent(queryCursor);
        return item;
    }
}