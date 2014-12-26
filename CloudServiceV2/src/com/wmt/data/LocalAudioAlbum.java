package com.wmt.data;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Audio.Albums;
import android.provider.MediaStore.Audio.Media;
import android.util.Log;
import com.wmt.MusicPlayer.MusicUtils;
import java.util.ArrayList;
import java.util.Iterator;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;

public class LocalAudioAlbum extends LocalAudioGroup {
    private static final String TAG = "LocalAudioAlbum";
    private final String mAlbumArt;
    private final int mAlbumID;
    private final ContentResolver mContentResolver;
    private final DataManager mDataManager;
    private final String mName;

    public LocalAudioAlbum(DataManager dataManager, Path path, ContentResolver resolver, int albumId) {
        super(path, dataManager, albumId);
        this.mContentResolver = resolver;
        this.mDataManager = dataManager;
        this.mAlbumID = albumId;
        Uri uri = Albums.getContentUri(DataManager.getDeviceString(DataManager.getDeviceFromPath(path)));
        Cursor cursor = this.mContentResolver.query(uri, new String[]{"album", "_id", "artist"}, MusicUtils.THUMBDATA_WHERE_CLAUSE, new String[]{String.valueOf(this.mId)}, null);
        if (cursor != null) {
            cursor.moveToNext();
            this.mName = cursor.getString(0);
            this.mAlbumArt = cursor.getString(ClassWriter.COMPUTE_FRAMES);
            cursor.close();
        } else {
            this.mName = "";
            this.mAlbumArt = "";
            Log.w(TAG, "cannot open local database: " + this.mBaseUri);
        }
    }

    public LocalAudioAlbum(DataManager dataManager, Path path, ContentResolver resolver, int albumId, String albumName, String albumArt) {
        super(path, dataManager, albumId);
        this.mContentResolver = resolver;
        this.mName = albumName;
        this.mAlbumArt = albumArt;
        this.mDataManager = dataManager;
        this.mAlbumID = albumId;
    }

    public void delete() {
        this.mContentResolver.delete(this.mBaseUri, "album_id=? ", new String[]{String.valueOf(this.mId)});
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
        ArrayList<MediaItem> albumList = new ArrayList();
        Iterator i$ = cachedAllList.iterator();
        while (i$.hasNext()) {
            MediaItem item = (MediaItem) i$.next();
            if (item.getAlbumID() == this.mAlbumID) {
                albumList.add(item);
            }
        }
        return albumList;
    }

    protected Cursor getCountCursor() {
        return this.mContentResolver.query(this.mBaseUri, COUNT_PROJECTION, "album_id=? AND _size> ?", new String[]{String.valueOf(this.mId), String.valueOf(this.mFilterSize)}, null);
    }

    public String getName() {
        return this.mName;
    }

    protected Cursor getQueryCursor(int start, int count) {
        Uri uri = this.mBaseUri.buildUpon().appendQueryParameter("limit", start + "," + count).build();
        return this.mContentResolver.query(uri, LocalAudio.getProjection(this.mDataManager.isBigThumbDataEnabled()), "album_id=? and _size> ?", new String[]{String.valueOf(this.mId), String.valueOf(this.mFilterSize)}, this.mOrderByField);
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