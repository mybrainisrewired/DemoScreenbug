package com.wmt.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Audio.Media;
import android.provider.MediaStore.Audio.Playlists;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;

public class LocalAudioGroupSet extends MediaSet {
    private static final String[] COUNT_PROJECTION;
    public static final String TAG = "LocalAudioGroupSet";
    private final Uri mBaseUri;
    private final ContentResolver mContentResolver;
    private final DataManager mDataManager;
    private final int mDevice;
    private int mFilterSize;
    private final int mGroupType;
    private ArrayList<MediaSet> mGroups;
    private final String mName;
    private final ChangeNotifier mNotifier;
    private final Path mPath;
    private boolean mReloadRequested;

    static {
        COUNT_PROJECTION = new String[]{"count(*)"};
    }

    public LocalAudioGroupSet(DataManager dataManager, Path path, ContentResolver resolver, String setName) {
        super(path, nextVersionNumber());
        this.mGroups = new ArrayList();
        this.mReloadRequested = false;
        this.mFilterSize = 0;
        this.mGroupType = LocalSource.getAudioGroupType(path);
        this.mDevice = DataManager.getDeviceFromPath(path);
        String devStr = DataManager.getDeviceString(this.mDevice);
        if (this.mGroupType == 1) {
            this.mBaseUri = Media.getContentUri(devStr);
        } else if (this.mGroupType == 2) {
            this.mBaseUri = Media.getContentUri(devStr);
        } else if (this.mGroupType == 3) {
            this.mBaseUri = Playlists.getContentUri(devStr);
        } else {
            throw new IllegalArgumentException("LocalAudioGroup is one which deal with Audio Group , path " + path.toString());
        }
        this.mNotifier = new ChangeNotifier(this, this.mBaseUri, dataManager);
        this.mName = setName;
        this.mContentResolver = resolver;
        this.mPath = path;
        this.mDataManager = dataManager;
    }

    private void loadDataIfNecessary() {
        if (!this.mDataManager.isDeleteMediaItems()) {
            if (this.mNotifier.isDirty() || this.mReloadRequested) {
                this.mDataVersion = nextVersionNumber();
                this.mGroups = loadSubMediaSets();
                this.mReloadRequested = false;
            }
        }
    }

    public void delete(int index) {
        if (index < this.mGroups.size()) {
            this.mGroups.remove(index);
        }
    }

    public void delete(long playlistId) {
        this.mContentResolver.delete(ContentUris.withAppendedId(Playlists.getContentUri(DataManager.getDeviceString(this.mDevice)), playlistId), null, null);
        this.mReloadRequested = true;
    }

    public String getName() {
        return this.mName;
    }

    public synchronized MediaSet getSubMediaSet(int index) {
        MediaSet mediaSet;
        loadDataIfNecessary();
        if (index < this.mGroups.size()) {
            mediaSet = (MediaSet) this.mGroups.get(index);
        } else {
            mediaSet = null;
        }
        return mediaSet;
    }

    public synchronized int getSubMediaSetCount() {
        loadDataIfNecessary();
        return this.mGroups.size();
    }

    protected ArrayList<MediaSet> loadSubMediaSets() {
        Uri uri = this.mBaseUri;
        Cursor cursor = null;
        ArrayList<MediaSet> groups = new ArrayList();
        try {
            String artistName;
            MediaObject object;
            MediaSet set;
            if (this.mGroupType == 1) {
                cursor = this.mContentResolver.query(uri, new String[]{"album", "album_id", "artist"}, "_size>? ) GROUP BY (album_key", new String[]{String.valueOf(this.mFilterSize)}, null);
                if (cursor == null) {
                    Log.w(TAG, "cannot open local database: " + uri);
                    if (cursor != null) {
                        cursor.close();
                    }
                }
                while (cursor.moveToNext()) {
                    String albumName = cursor.getString(0);
                    int albumId = cursor.getInt(1);
                    artistName = cursor.getString(ClassWriter.COMPUTE_FRAMES);
                    object = this.mPath.getChild(albumId).getObject();
                    if (object != null) {
                        set = (MediaSet) object;
                    } else {
                        set = new LocalAudioAlbum(this.mDataManager, this.mPath.getChild(String.valueOf(albumId)), this.mContentResolver, albumId, albumName, artistName);
                    }
                    set.setFilterSize(this.mFilterSize);
                    groups.add(set);
                }
                if (cursor != null) {
                    cursor.close();
                }
            } else if (this.mGroupType == 2) {
                cursor = this.mContentResolver.query(uri, new String[]{"artist", "artist_id", "artist_key"}, "_size>? ) GROUP BY (artist_key", new String[]{String.valueOf(this.mFilterSize)}, null);
                if (cursor == null) {
                    Log.w(TAG, "cannot open local database: " + uri);
                    if (cursor != null) {
                        cursor.close();
                    }
                }
                while (cursor.moveToNext()) {
                    artistName = cursor.getString(0);
                    int artistId = cursor.getInt(1);
                    String artistKey = cursor.getString(ClassWriter.COMPUTE_FRAMES);
                    object = this.mPath.getChild(artistId).getObject();
                    if (object != null) {
                        set = (MediaSet) object;
                    } else {
                        DataManager dataManager = this.mDataManager;
                        Path child = this.mPath.getChild(artistId);
                        ContentResolver contentResolver = this.mContentResolver;
                        MediaSet localAudioArtist = set;
                        String str = artistName;
                    }
                    set.setFilterSize(this.mFilterSize);
                    groups.add(set);
                }
                if (cursor != null) {
                    cursor.close();
                }
            } else {
                if (this.mGroupType == 3) {
                    ContentResolver contentResolver2 = this.mContentResolver;
                    String[] strArr = new String[3];
                    strArr[0] = "_id";
                    strArr[1] = "name";
                    strArr[2] = "_data";
                    cursor = contentResolver2.query(uri, strArr, null, null, null);
                    if (cursor == null) {
                        Log.w(TAG, "cannot open local database: " + uri);
                        if (cursor != null) {
                            cursor.close();
                        }
                    } else {
                        Log.v(TAG, "cursor.getCount = " + cursor.getCount());
                        while (cursor.moveToNext()) {
                            int playlistId = cursor.getInt(0);
                            String playlistName = cursor.getString(1);
                            String playlistData = cursor.getString(ClassWriter.COMPUTE_FRAMES);
                            Log.v(TAG, "found " + playlistId + ", " + playlistName + ", " + playlistData);
                            object = this.mPath.getChild(playlistId).getObject();
                            if (object != null) {
                                set = (MediaSet) object;
                                set.setName(playlistName);
                                groups.add(set);
                            } else {
                                groups.add(new LocalAudioPlaylist(this.mDataManager, this.mContentResolver, this.mPath.getChild(String.valueOf(playlistId)), playlistId, playlistName, playlistData));
                            }
                        }
                    }
                }
                if (cursor != null) {
                    cursor.close();
                }
            }
            return groups;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public long reload() {
        loadDataIfNecessary();
        return this.mDataVersion;
    }

    public void reloadRequest() {
        this.mReloadRequested = true;
    }

    public void setFilterSize(int sizeInByte) {
        if (this.mFilterSize != sizeInByte) {
            this.mFilterSize = sizeInByte;
            this.mReloadRequested = true;
        }
    }

    public synchronized void setOrderBy(String fieldName) {
        Iterator i$ = this.mGroups.iterator();
        while (i$.hasNext()) {
            ((MediaSet) i$.next()).setOrderBy(fieldName);
        }
    }
}