package com.wmt.data;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.MediaStore.Audio.Media;
import android.provider.MediaStore.Audio.Playlists;
import android.provider.MediaStore.Audio.Playlists.Members;
import android.util.Log;
import com.wmt.MusicPlayer.MusicUtils;
import java.util.ArrayList;
import java.util.Iterator;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;

public class LocalAudioPlaylist extends LocalAudioGroup {
    private static final String[] COUNT_PROJECTION;
    private static final String[] PROJECTION;
    private static final String TAG = "LocalAudioPlaylistGroup";
    private static ContentValues[] sContentValuesCache;
    private final String WHERE_CLAUSE;
    private final ContentResolver mContentResolver;
    private final String mData;
    private String mName;

    static {
        PROJECTION = new String[]{"audio_id", "play_order"};
        COUNT_PROJECTION = new String[]{"count(*)"};
        sContentValuesCache = null;
    }

    public LocalAudioPlaylist(DataManager dataManager, ContentResolver resolver, Path path, int playlistId, String playlistName, String playlistData) {
        super(path, dataManager, playlistId);
        this.WHERE_CLAUSE = "playlist_id=?";
        this.mContentResolver = resolver;
        this.mName = playlistName;
        this.mData = playlistData;
    }

    public LocalAudioPlaylist(DataManager dataManager, Path path, ContentResolver resolver, int playlistId) {
        super(path, dataManager, playlistId);
        this.WHERE_CLAUSE = "playlist_id=?";
        this.mContentResolver = resolver;
        Uri uri = Playlists.getContentUri(DataManager.getDeviceString(DataManager.getDeviceFromPath(path)));
        String where = MusicUtils.THUMBDATA_WHERE_CLAUSE;
        Cursor cursor = this.mContentResolver.query(uri, new String[]{"_id", "name", "_data"}, where, new String[]{String.valueOf(playlistId)}, null);
        if (cursor != null) {
            cursor.moveToNext();
            this.mName = cursor.getString(1);
            this.mData = cursor.getString(ClassWriter.COMPUTE_FRAMES);
            cursor.close();
        } else {
            this.mName = "";
            this.mData = "";
            Log.w(TAG, "cannot open local database: " + this.mBaseUri);
        }
    }

    private static void makeInsertItems(ArrayList<MediaItem> items, int offset, int len, int base) {
        if (offset + len > items.size()) {
            len = items.size() - offset;
        }
        if (sContentValuesCache == null || sContentValuesCache.length != len) {
            sContentValuesCache = new ContentValues[len];
        }
        int i = 0;
        while (i < len) {
            if (sContentValuesCache[i] == null) {
                sContentValuesCache[i] = new ContentValues();
            }
            sContentValuesCache[i].put("play_order", Integer.valueOf(base + offset + i));
            sContentValuesCache[i].put("audio_id", Integer.valueOf(((MediaItem) items.get(offset + i)).getID()));
            i++;
        }
    }

    public void addItems(ArrayList<MediaItem> items) {
        if (items != null && items.size() > 0) {
            int size = items.size();
            String[] cols = new String[]{"count(*)"};
            Uri uri = Members.getContentUri(DataManager.getDeviceString(DataManager.getDeviceFromPath(this.mItemPath)), (long) this.mId);
            Cursor cur = this.mContentResolver.query(uri, cols, null, null, null);
            cur.moveToFirst();
            int base = cur.getInt(0);
            cur.close();
            int numinserted = 0;
            int i = 0;
            while (i < size) {
                makeInsertItems(items, i, 1000, base);
                numinserted += this.mContentResolver.bulkInsert(uri, sContentValuesCache);
                i += 1000;
            }
            Log.v(TAG, numinserted + "items insert into playlist " + this.mName);
        }
    }

    public void delete() {
        this.mContentResolver.delete(this.mBaseUri, "playlist_id=?", new String[]{String.valueOf(this.mId)});
    }

    public void deleteMediaItems(ArrayList<MediaItem> items) {
        ArrayList<ContentProviderOperation> operations = new ArrayList();
        Iterator i$ = items.iterator();
        while (i$.hasNext()) {
            MediaItem item = (MediaItem) i$.next();
            operations.add(ContentProviderOperation.newDelete(this.mBaseUri).withSelection("audio_id=?", new String[]{String.valueOf(item.getID())}).build());
        }
        try {
            this.mContentResolver.applyBatch("media", operations);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e2) {
            e2.printStackTrace();
        }
    }

    protected Uri getBaseUri(Path path, int id) {
        return Members.getContentUri(DataManager.getDeviceString(DataManager.getDeviceFromPath(path)), (long) id);
    }

    protected ArrayList<MediaItem> getCachedList() {
        return null;
    }

    protected Cursor getCountCursor() {
        return this.mContentResolver.query(this.mBaseUri, COUNT_PROJECTION, "playlist_id=?", new String[]{String.valueOf(this.mId)}, null);
    }

    public String getName() {
        return this.mName;
    }

    protected Cursor getQueryCursor(int start, int count) {
        Uri uri = this.mBaseUri.buildUpon().appendQueryParameter("limit", start + "," + count).build();
        return this.mContentResolver.query(uri, PROJECTION, "playlist_id=?", new String[]{String.valueOf(this.mId)}, "play_order");
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
            return new LocalAudio(path, this.mContentResolver, id, this.mDataManager.isBigThumbDataEnabled());
        }
        Uri uri = Media.getContentUri(DataManager.getDeviceString(DataManager.getDeviceFromPath(path)));
        Cursor cursor = this.mContentResolver.query(uri, LocalAudio.getProjection(this.mDataManager.isBigThumbDataEnabled()), MusicUtils.THUMBDATA_WHERE_CLAUSE, new String[]{String.valueOf(id)}, null);
        cursor.moveToFirst();
        item.updateContent(cursor);
        if (cursor == null) {
            return item;
        }
        cursor.close();
        return item;
    }

    public void setName(String name) {
        this.mName = name;
    }
}