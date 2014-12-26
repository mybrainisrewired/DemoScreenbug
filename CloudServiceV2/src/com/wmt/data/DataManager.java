package com.wmt.data;

import android.content.ContentProviderOperation;
import android.content.ContentProviderOperation.Builder;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.ContentObserver;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Handler;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.storage.IMountService;
import android.os.storage.IMountService.Stub;
import android.provider.MediaStore.Audio.Media;
import android.provider.MediaStore.Audio.Playlists;
import android.provider.MediaStore.Files;
import android.util.Log;
import com.wmt.MusicPlayer.MusicUtils;
import com.wmt.data.MediaSet.ItemConsumer;
import com.wmt.data.MediaSource.PathId;
import com.wmt.util.Utils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.WeakHashMap;

public class DataManager {
    public static final int CLUSTER_BY_ALBUM = 0;
    public static final int CLUSTER_BY_FACE = 3;
    public static final int CLUSTER_BY_LOCATION = 2;
    public static final int CLUSTER_BY_SIZE = 5;
    public static final int CLUSTER_BY_TAG = 4;
    public static final int CLUSTER_BY_TIME = 1;
    public static final String EXTERNAL_MOUNT_POINT = "/mnt/sdcard";
    public static final int INCLUDE_AUDIO = 4;
    public static final int INCLUDE_AUDIO_ALBUM = 8;
    public static final int INCLUDE_AUDIO_ARTIST = 16;
    public static final int INCLUDE_AUDIO_PLAYLIST = 32;
    public static final int INCLUDE_AUDIO_RECENTLIST = 512;
    public static final int INCLUDE_IMAGE = 1;
    public static final int INCLUDE_LOCAL_ALL_ONLY = 67;
    public static final int INCLUDE_LOCAL_AUDIO_ALBUM_ONLY = 72;
    public static final int INCLUDE_LOCAL_AUDIO_ARTIST_ONLY = 80;
    public static final int INCLUDE_LOCAL_AUDIO_ONLY = 68;
    public static final int INCLUDE_LOCAL_AUDIO_PLAYLIST_ONLY = 96;
    public static final int INCLUDE_LOCAL_AUDIO_RECENTLIST_ONLY = 576;
    public static final int INCLUDE_LOCAL_IMAGE_ONLY = 65;
    public static final int INCLUDE_LOCAL_ONLY = 64;
    public static final int INCLUDE_LOCAL_VIDEO_ONLY = 66;
    public static final int INCLUDE_SD_ALL_ONLY = 131;
    public static final int INCLUDE_SD_AUDIO_ALBUM_ONLY = 136;
    public static final int INCLUDE_SD_AUDIO_ARTIST_ONLY = 144;
    public static final int INCLUDE_SD_AUDIO_ONLY = 132;
    public static final int INCLUDE_SD_AUDIO_PLAYLIST_ONLY = 160;
    public static final int INCLUDE_SD_AUDIO_RECENTLIST_ONLY = 640;
    public static final int INCLUDE_SD_IMAGE_ONLY = 129;
    public static final int INCLUDE_SD_ONLY = 128;
    public static final int INCLUDE_SD_VIDEO_ONLY = 130;
    public static final int INCLUDE_USB_ALL_ONLY = 259;
    public static final int INCLUDE_USB_AUDIO_ALBUM_ONLY = 264;
    public static final int INCLUDE_USB_AUDIO_ARTIST_ONLY = 272;
    public static final int INCLUDE_USB_AUDIO_ONLY = 260;
    public static final int INCLUDE_USB_AUDIO_PLAYLIST_ONLY = 288;
    public static final int INCLUDE_USB_AUDIO_RECENTLIST_ONLY = 768;
    public static final int INCLUDE_USB_IMAGE_ONLY = 257;
    public static final int INCLUDE_USB_ONLY = 256;
    public static final int INCLUDE_USB_VIDEO_ONLY = 258;
    public static final int INCLUDE_VIDEO = 2;
    public static final int MEDIA_MOUNTED = 1;
    public static final int MEDIA_REMOVED = 2;
    public static final int MEDIA_UNKNOWSTATE = 0;
    public static final int MEDIA_UNMOUNTED = 3;
    public static final int STORAGE_DEVICE_INTERNAL = 0;
    public static final int STORAGE_DEVICE_SD = 1;
    public static final int STORAGE_DEVICE_USB = 2;
    private static final String TAG = "DataManager";
    private static final String TOP_LOCAL_AUDIO_ALBUM_PATH = "/local/audio/album";
    private static final String TOP_LOCAL_AUDIO_ARTIST_PATH = "/local/audio/artist";
    private static final String TOP_LOCAL_AUDIO_PATH = "/local/audio";
    private static final String TOP_LOCAL_AUDIO_PLAYLIST_PATH = "/local/audio/playlist";
    private static final String TOP_LOCAL_AUDIO_RECENTLIST_PATH = "/local/audio/recentlist";
    private static final String TOP_LOCAL_IMAGE_SET_PATH = "/local/image";
    private static final String TOP_LOCAL_SET_PATH = "/local/all";
    private static final String TOP_LOCAL_VIDEO_SET_PATH = "/local/video";
    private static final String TOP_SD_AUDIO_ALBUM_PATH = "/sd/audio/album";
    private static final String TOP_SD_AUDIO_ARTIST_PATH = "/sd/audio/artist";
    private static final String TOP_SD_AUDIO_PATH = "/sd/audio";
    private static final String TOP_SD_AUDIO_PLAYLIST_PATH = "/sd/audio/playlist";
    private static final String TOP_SD_AUDIO_RECENTLIST_PATH = "/sd/audio/recentlist";
    private static final String TOP_SD_IMAGE_SET_PATH = "/sd/image";
    private static final String TOP_SD_SET_PATH = "/sd/all";
    private static final String TOP_SD_VIDEO_SET_PATH = "/sd/video";
    private static final String TOP_USB_AUDIO_ALBUM_PATH = "/usb/audio/album";
    private static final String TOP_USB_AUDIO_ARTIST_PATH = "/usb/audio/artist";
    private static final String TOP_USB_AUDIO_PATH = "/usb/audio";
    private static final String TOP_USB_AUDIO_PLAYLIST_PATH = "/usb/audio/playlist";
    private static final String TOP_USB_AUDIO_RECENTLIST_PATH = "/usb/audio/recentlist";
    private static final String TOP_USB_IMAGE_SET_PATH = "/usb/image";
    private static final String TOP_USB_SET_PATH = "/usb/all";
    private static final String TOP_USB_VIDEO_SET_PATH = "/usb/video";
    public static final String UDISK_MOUNT_POINT = "/mnt/udisk";
    public static final String UnknownLocation = "UnknownLocation";
    public static final String UnknownPeople = "UnknownPeople";
    public static final String UnknownTAG = "UnknownTAG";
    public static final Comparator<MediaItem> sDateTakenComparator;
    public final Object LOCK;
    private int mActiveCount;
    private final Context mContext;
    private final Handler mDefaultMainHandler;
    private boolean mEnableBigThumb;
    private boolean mInDeleteMediaItems;
    private MediaItemListCacher mMediaItemListCacher;
    private HashMap<Uri, NotifyBroker> mNotifierMap;
    private HashMap<String, MediaSource> mSourceMap;

    private static class DateTakenComparator implements Comparator<MediaItem> {
        private DateTakenComparator() {
        }

        public int compare(MediaItem item1, MediaItem item2) {
            return -Utils.compare(item1.getDateInMs(), item2.getDateInMs());
        }
    }

    public static interface MediaItemListCacher {
        ArrayList<MediaItem> getCachedList(String str);
    }

    private static class NotifyBroker extends ContentObserver {
        private WeakHashMap<ChangeNotifier, Object> mNotifiers;

        public NotifyBroker(Handler handler) {
            super(handler);
            this.mNotifiers = new WeakHashMap();
        }

        public synchronized void onChange(boolean selfChange) {
            Log.v(TAG, "onChange happen here " + selfChange + ", keySet size  " + this.mNotifiers.keySet().size());
            Iterator i$ = this.mNotifiers.keySet().iterator();
            while (i$.hasNext()) {
                ((ChangeNotifier) i$.next()).onChange(selfChange);
            }
        }

        public synchronized void registerNotifier(ChangeNotifier notifier) {
            this.mNotifiers.put(notifier, null);
        }
    }

    static {
        sDateTakenComparator = new DateTakenComparator();
    }

    public DataManager(Context context) {
        this.LOCK = new Object();
        this.mActiveCount = 0;
        this.mNotifierMap = new HashMap();
        this.mSourceMap = new LinkedHashMap();
        this.mMediaItemListCacher = null;
        this.mEnableBigThumb = true;
        this.mInDeleteMediaItems = false;
        this.mContext = context;
        this.mDefaultMainHandler = new Handler(context.getMainLooper());
    }

    public static int getDeviceFromPath(Path path) {
        String[] name = path.split();
        if (name.length < 1) {
            throw new IllegalArgumentException(path.toString());
        } else if (MusicUtils.LOCAL_VOLUME.equals(name[0])) {
            return STORAGE_DEVICE_INTERNAL;
        } else {
            if ("sd".equals(name[0])) {
                return 1;
            }
            if ("usb".equals(name[0])) {
                return STORAGE_DEVICE_USB;
            }
            throw new IllegalArgumentException(path.toString());
        }
    }

    public static int getDeviceState(int dev_id) {
        IMountService mountService = Stub.asInterface(ServiceManager.getService("mount"));
        String state = null;
        switch (dev_id) {
            case STORAGE_DEVICE_INTERNAL:
                return 1;
            case STORAGE_DEVICE_SD:
                try {
                    state = mountService.getVolumeState(EXTERNAL_MOUNT_POINT);
                    Log.d(TAG, "sdcard:" + state);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                return state.equals("mounted") ? 1 : MEDIA_UNMOUNTED;
            case STORAGE_DEVICE_USB:
                try {
                    state = mountService.getVolumeState(UDISK_MOUNT_POINT);
                    Log.d(TAG, "udisk: " + state);
                } catch (RemoteException e2) {
                    e2.printStackTrace();
                }
                return state.equals("mounted") ? 1 : MEDIA_UNMOUNTED;
            default:
                return MEDIA_UNMOUNTED;
        }
    }

    public static String getDeviceString(int deviceId) {
        if (deviceId == 0) {
            return MusicUtils.LOCAL_VOLUME;
        }
        if (deviceId == 1) {
            return MusicUtils.EXTERNAL_VOLUME;
        }
        if (deviceId == 2) {
            return MusicUtils.UDISK_VOLUME;
        }
        throw new IllegalArgumentException("invalid device id " + deviceId);
    }

    private int getIdxInItems(long[] ids, int id) {
        int i = STORAGE_DEVICE_INTERNAL;
        while (i < ids.length) {
            if (ids[i] == ((long) id)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    private Path getItemPathFromDevice(int id) {
        switch (id) {
            case STORAGE_DEVICE_INTERNAL:
                return Path.fromString("/local/audio/item");
            case STORAGE_DEVICE_SD:
                return Path.fromString("/sd/audio/item");
            case STORAGE_DEVICE_USB:
                return Path.fromString("/usb/audio/item");
            default:
                throw new IllegalArgumentException("invalid device id " + id);
        }
    }

    public static String newClusterPath(String base, int clusterType) {
        String kind;
        switch (clusterType) {
            case STORAGE_DEVICE_SD:
                kind = "time";
                break;
            case STORAGE_DEVICE_USB:
                kind = "location";
                break;
            case MEDIA_UNMOUNTED:
                kind = "face";
                break;
            case INCLUDE_AUDIO:
                kind = "tag";
                break;
            case CLUSTER_BY_SIZE:
                kind = "size";
                break;
            default:
                return base;
        }
        return "/cluster/{" + base + "}/" + kind;
    }

    private static String removeOneClusterFromPath(String base) {
        return removeOneClusterFromPath(base, new boolean[1]);
    }

    private static String removeOneClusterFromPath(String base, boolean[] done) {
        if (done[0]) {
            return base;
        }
        String[] segments = Path.split(base);
        if (segments[0].equals("cluster")) {
            done[0] = true;
            return Path.splitSequence(segments[1])[0];
        } else {
            StringBuilder sb = new StringBuilder();
            int i = STORAGE_DEVICE_INTERNAL;
            while (i < segments.length) {
                sb.append("/");
                if (segments[i].startsWith("{")) {
                    sb.append("{");
                    String[] sets = Path.splitSequence(segments[i]);
                    int j = STORAGE_DEVICE_INTERNAL;
                    while (j < sets.length) {
                        if (j > 0) {
                            sb.append(",");
                        }
                        sb.append(removeOneClusterFromPath(sets[j], done));
                        j++;
                    }
                    sb.append("}");
                } else {
                    sb.append(segments[i]);
                }
                i++;
            }
            return sb.toString();
        }
    }

    public static String switchClusterPath(String base, int clusterType) {
        return newClusterPath(removeOneClusterFromPath(base), clusterType);
    }

    void addSource(MediaSource source) {
        String[] prefixs = source.getPrefixs();
        if (prefixs != null && prefixs.length > 0) {
            String[] arr$ = prefixs;
            int len$ = arr$.length;
            int i$ = STORAGE_DEVICE_INTERNAL;
            while (i$ < len$) {
                this.mSourceMap.put(arr$[i$], source);
                i$++;
            }
        }
    }

    public Path createNewAudioPlayList(int deviceId, String listName) {
        ContentValues values = new ContentValues(1);
        values.put("name", listName);
        Uri resulturi = this.mContext.getContentResolver().insert(Playlists.getContentUri(getDeviceString(deviceId)), values);
        Log.v(TAG, "createNewAudioPlayList result uri is " + resulturi);
        return findPathByUri(resulturi);
    }

    public void delete(Path path) {
        getMediaObject(path).delete();
    }

    public void deleteMediaItems(ArrayList<MediaItem> items) {
        this.mInDeleteMediaItems = true;
        ArrayList<ContentProviderOperation> operations = new ArrayList();
        Iterator i$ = items.iterator();
        while (i$.hasNext()) {
            operations.add(ContentProviderOperation.newDelete(Files.getContentUri(getDeviceString(getDeviceFromPath(((MediaItem) i$.next()).getPath())))).withSelection(MusicUtils.THUMBDATA_WHERE_CLAUSE, new String[]{String.valueOf(item.getID())}).build());
        }
        try {
            this.mContext.getContentResolver().applyBatch("media", operations);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e2) {
            e2.printStackTrace();
        }
        this.mInDeleteMediaItems = false;
    }

    public void deleteMediaSets(ArrayList<MediaSet> mediaSets) {
        Iterator i$ = mediaSets.iterator();
        while (i$.hasNext()) {
            MediaSet mediaSet = (MediaSet) i$.next();
            int count = mediaSet.getMediaItemCount();
            if (count > 0) {
                deleteMediaItems(mediaSet.getMediaItem(STORAGE_DEVICE_INTERNAL, count));
            }
        }
    }

    public void enableBigThumbData(boolean enable) {
        this.mEnableBigThumb = enable;
    }

    public Path findPathByUri(Uri uri) {
        if (uri == null) {
            return null;
        }
        Iterator i$ = this.mSourceMap.values().iterator();
        while (i$.hasNext()) {
            Path path = ((MediaSource) i$.next()).findPathByUri(uri);
            if (path != null) {
                return path;
            }
        }
        return null;
    }

    public String getAudioPath(int device, int sortType) {
        switch (device) {
            case STORAGE_DEVICE_INTERNAL:
                return "/local/audio/" + sortType;
            case STORAGE_DEVICE_SD:
                return "/sd/audio/" + sortType;
            case STORAGE_DEVICE_USB:
                return "/usb/audio/" + sortType;
            default:
                throw new IllegalArgumentException();
        }
    }

    public MediaItem[] getAudioPlayingList(int deviceId, long[] itemIds) {
        if (itemIds == null || itemIds.length == 0) {
            throw new IllegalArgumentException("itemIds is null");
        }
        ContentResolver resolver = this.mContext.getContentResolver();
        Uri baseUri = Media.getContentUri(getDeviceString(deviceId));
        Path parentPath = getItemPathFromDevice(deviceId);
        MediaItem[] result = new MediaItem[itemIds.length];
        int removedNum = STORAGE_DEVICE_INTERNAL;
        StringBuilder where = new StringBuilder();
        where.append("_id IN (");
        int i = STORAGE_DEVICE_INTERNAL;
        while (i < itemIds.length) {
            where.append(itemIds[i]);
            if (i < itemIds.length - 1) {
                where.append(",");
            }
            i++;
        }
        where.append(")");
        Cursor cursor = null;
        try {
            Log.v(TAG, "where is " + where);
            cursor = resolver.query(baseUri, LocalAudio.getProjection(this.mEnableBigThumb), where.toString(), null, null);
            if (cursor == null) {
                Log.v(TAG, "cannot get playing list cursor");
                if (cursor == null) {
                    return null;
                }
                cursor.close();
                return null;
            }
            while (cursor.moveToNext()) {
                int id = cursor.getInt(STORAGE_DEVICE_INTERNAL);
                LocalAudio item = parentPath.getChild(id).getObject();
                if (item == null) {
                    boolean z = this.mEnableBigThumb;
                    LocalAudio localAudio = item;
                } else {
                    if (item.getIsDeleted()) {
                        item.setIsDeleted(false);
                    }
                    item.updateContent(cursor);
                }
                int idx = getIdxInItems(itemIds, id);
                if (idx < 0) {
                    throw new RuntimeException("invalid id " + id);
                }
                result[idx] = item;
            }
            try {
                if (MusicUtils.sService != null) {
                    i = STORAGE_DEVICE_INTERNAL;
                    while (i < result.length) {
                        if (result[i] == null) {
                            removedNum++;
                            MusicUtils.sService.removeTrack(itemIds[i], deviceId);
                        }
                        i++;
                    }
                    if (removedNum <= 0) {
                        if (cursor != null) {
                            cursor.close();
                        }
                        return result;
                    } else if (itemIds.length - removedNum > 0) {
                        MediaItem[] newResult = new MediaItem[(itemIds.length - removedNum)];
                        i = STORAGE_DEVICE_INTERNAL;
                        int index = 0;
                        while (i < result.length) {
                            int index2;
                            if (result[i] != null) {
                                index2 = index + 1;
                                newResult[index] = result[i];
                            } else {
                                index2 = index;
                            }
                            i++;
                            index = index2;
                        }
                        if (cursor == null) {
                            return newResult;
                        }
                        cursor.close();
                        return newResult;
                    } else if (cursor == null) {
                        return null;
                    } else {
                        cursor.close();
                        return null;
                    }
                } else if (cursor == null) {
                    return null;
                } else {
                    cursor.close();
                    return null;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public String getAudioRecentListPath(int typeBits, long date) {
        Utils.assertTrue(date > 0);
        switch (typeBits) {
            case INCLUDE_LOCAL_AUDIO_RECENTLIST_ONLY:
                return "/local/audio/recentlist/" + date;
            case INCLUDE_SD_AUDIO_RECENTLIST_ONLY:
                return "/sd/audio/recentlist/" + date;
            case INCLUDE_USB_AUDIO_RECENTLIST_ONLY:
                return "/usb/audio/recentlist/" + date;
            default:
                Log.v(TAG, "typeBits is " + Integer.toHexString(typeBits));
                throw new IllegalArgumentException();
        }
    }

    public Path getBucketPathFromFilePath(String filePath, int deviceId, int fileType) {
        if (filePath == null) {
            throw new IllegalArgumentException("filePath is null");
        }
        Uri baseUri = Files.getContentUri(getDeviceString(deviceId));
        Cursor cursor = this.mContext.getContentResolver().query(baseUri, new String[]{"bucket_id"}, "_data=?", new String[]{filePath}, null);
        if (cursor == null) {
            Log.w(TAG, "query fail");
            return null;
        } else {
            try {
                Utils.assertTrue(cursor.moveToNext());
                int bucketId = cursor.getInt(STORAGE_DEVICE_INTERNAL);
                cursor.close();
                Path parentPath = null;
                if (deviceId == 0) {
                    if (fileType == 2) {
                        parentPath = LocalAlbumSet.LOCAL_PATH_IMAGE;
                    } else if (fileType == 4) {
                        parentPath = LocalAlbumSet.LOCAL_PATH_VIDEO;
                    } else {
                        parentPath = LocalAlbumSet.LOCAL_PATH_ALL;
                    }
                } else if (deviceId == 1) {
                    if (fileType == 2) {
                        parentPath = LocalAlbumSet.SD_PATH_IMAGE;
                    } else if (fileType == 4) {
                        parentPath = LocalAlbumSet.SD_PATH_VIDEO;
                    } else {
                        parentPath = LocalAlbumSet.SD_PATH_ALL;
                    }
                } else if (deviceId == 2) {
                    if (fileType == 2) {
                        parentPath = LocalAlbumSet.USB_PATH_IMAGE;
                    } else if (fileType == 4) {
                        parentPath = LocalAlbumSet.USB_PATH_VIDEO;
                    } else {
                        parentPath = LocalAlbumSet.USB_PATH_ALL;
                    }
                }
                return parentPath.getChild(bucketId);
            } catch (AssertionError e) {
                e.printStackTrace();
                cursor.close();
                return null;
            } catch (Exception e2) {
                e2.printStackTrace();
                cursor.close();
                return null;
            }
        }
    }

    public ArrayList<MediaItem> getCachedMediaItemList(Path path) {
        return this.mMediaItemListCacher != null ? this.mMediaItemListCacher.getCachedList(path.toString()) : null;
    }

    public ArrayList<MediaItem> getCachedMediaItemList(String path) {
        return this.mMediaItemListCacher != null ? this.mMediaItemListCacher.getCachedList(path) : null;
    }

    public Uri getContentUri(Path path) {
        return getMediaObject(path).getContentUri();
    }

    public Path getDefaultSetOf(Path item) {
        MediaSource source = (MediaSource) this.mSourceMap.get(item.getPrefix());
        return source == null ? null : source.getDefaultSetOf(item);
    }

    public MediaObject getMediaObject(Path path) {
        MediaObject obj = path.getObject();
        if (obj != null) {
            return obj;
        }
        MediaSource source = (MediaSource) this.mSourceMap.get(path.getPrefix());
        if (source == null) {
            Log.w(TAG, "cannot find media source for path prefix: " + path.getPrefix());
            return null;
        } else {
            MediaObject object = source.createMediaObject(path);
            if (object == null) {
                Log.w(TAG, "cannot create media object: " + path);
            }
            return object;
        }
    }

    public MediaObject getMediaObject(String s) {
        return getMediaObject(Path.fromString(s));
    }

    public MediaSet getMediaSet(Path path) {
        return (MediaSet) getMediaObject(path);
    }

    public MediaSet getMediaSet(String s) {
        return (MediaSet) getMediaObject(s);
    }

    public MediaSet[] getMediaSetsFromString(String segment) {
        String[] seq = Path.splitSequence(segment);
        int n = seq.length;
        MediaSet[] sets = new MediaSet[n];
        int i = STORAGE_DEVICE_INTERNAL;
        while (i < n) {
            sets[i] = getMediaSet(seq[i]);
            i++;
        }
        return sets;
    }

    public int getSupportedOperations(Path path) {
        return getMediaObject(path).getSupportedOperations();
    }

    public String getTopSetPath(int typeBits) {
        switch (typeBits) {
            case INCLUDE_LOCAL_IMAGE_ONLY:
                return TOP_LOCAL_IMAGE_SET_PATH;
            case INCLUDE_LOCAL_VIDEO_ONLY:
                return TOP_LOCAL_VIDEO_SET_PATH;
            case INCLUDE_LOCAL_ALL_ONLY:
                return TOP_LOCAL_SET_PATH;
            case INCLUDE_LOCAL_AUDIO_ONLY:
                return TOP_LOCAL_AUDIO_PATH;
            case INCLUDE_LOCAL_AUDIO_ALBUM_ONLY:
                return TOP_LOCAL_AUDIO_ALBUM_PATH;
            case INCLUDE_LOCAL_AUDIO_ARTIST_ONLY:
                return TOP_LOCAL_AUDIO_ARTIST_PATH;
            case INCLUDE_LOCAL_AUDIO_PLAYLIST_ONLY:
                return TOP_LOCAL_AUDIO_PLAYLIST_PATH;
            case INCLUDE_SD_IMAGE_ONLY:
                return TOP_SD_IMAGE_SET_PATH;
            case INCLUDE_SD_VIDEO_ONLY:
                return TOP_SD_VIDEO_SET_PATH;
            case INCLUDE_SD_ALL_ONLY:
                return TOP_SD_SET_PATH;
            case INCLUDE_SD_AUDIO_ONLY:
                return TOP_SD_AUDIO_PATH;
            case INCLUDE_SD_AUDIO_ALBUM_ONLY:
                return TOP_SD_AUDIO_ALBUM_PATH;
            case INCLUDE_SD_AUDIO_ARTIST_ONLY:
                return TOP_SD_AUDIO_ARTIST_PATH;
            case INCLUDE_SD_AUDIO_PLAYLIST_ONLY:
                return TOP_SD_AUDIO_PLAYLIST_PATH;
            case INCLUDE_USB_IMAGE_ONLY:
                return TOP_USB_IMAGE_SET_PATH;
            case INCLUDE_USB_VIDEO_ONLY:
                return TOP_USB_VIDEO_SET_PATH;
            case INCLUDE_USB_ALL_ONLY:
                return TOP_USB_SET_PATH;
            case INCLUDE_USB_AUDIO_ONLY:
                return TOP_USB_AUDIO_PATH;
            case INCLUDE_USB_AUDIO_ALBUM_ONLY:
                return TOP_USB_AUDIO_ALBUM_PATH;
            case INCLUDE_USB_AUDIO_ARTIST_ONLY:
                return TOP_USB_AUDIO_ARTIST_PATH;
            case INCLUDE_USB_AUDIO_PLAYLIST_ONLY:
                return TOP_USB_AUDIO_PLAYLIST_PATH;
            default:
                Log.v(TAG, "typeBits is " + Integer.toHexString(typeBits));
                throw new IllegalArgumentException();
        }
    }

    public synchronized void initializeSourceMap() {
        if (this.mSourceMap.isEmpty()) {
            addSource(new LocalSource(this.mContext.getContentResolver(), this));
            addSource(new ClusterSource(this.mContext, this));
            if (this.mActiveCount > 0) {
                Iterator i$ = this.mSourceMap.values().iterator();
                while (i$.hasNext()) {
                    ((MediaSource) i$.next()).resume();
                }
            }
        }
    }

    boolean isBigThumbDataEnabled() {
        return this.mEnableBigThumb;
    }

    public boolean isDeleteMediaItems() {
        return this.mInDeleteMediaItems;
    }

    public void mapMediaItems(ArrayList<Path> list, ItemConsumer consumer, int startIndex) {
        HashMap<String, ArrayList<PathId>> map = new HashMap();
        int n = list.size();
        int i = STORAGE_DEVICE_INTERNAL;
        while (i < n) {
            Path path = (Path) list.get(i);
            String prefix = path.getPrefix();
            ArrayList<PathId> group = (ArrayList) map.get(prefix);
            if (group == null) {
                group = new ArrayList();
                map.put(prefix, group);
            }
            group.add(new PathId(path, i + startIndex));
            i++;
        }
        Iterator i$ = map.entrySet().iterator();
        while (i$.hasNext()) {
            Entry<String, ArrayList<PathId>> entry = (Entry) i$.next();
            ((MediaSource) this.mSourceMap.get(entry.getKey())).mapMediaItems((ArrayList) entry.getValue(), consumer);
        }
    }

    public void pause() {
        int i = this.mActiveCount - 1;
        this.mActiveCount = i;
        if (i == 0) {
            Iterator i$ = this.mSourceMap.values().iterator();
            while (i$.hasNext()) {
                ((MediaSource) i$.next()).pause();
            }
        }
    }

    public MediaObject peekMediaObject(Path path) {
        return path.getObject();
    }

    public MediaSet peekMediaSet(Path path) {
        return (MediaSet) path.getObject();
    }

    public void registerChangeNotifier(Uri uri, ChangeNotifier notifier) {
        NotifyBroker broker;
        Throwable th;
        HashMap hashMap = this.mNotifierMap;
        synchronized (hashMap) {
            try {
                broker = (NotifyBroker) this.mNotifierMap.get(uri);
                if (broker == null) {
                    NotifyBroker broker2 = new NotifyBroker(this.mDefaultMainHandler);
                    try {
                        this.mContext.getContentResolver().registerContentObserver(uri, true, broker2);
                        this.mNotifierMap.put(uri, broker2);
                        broker = broker2;
                    } catch (Throwable th2) {
                        th = th2;
                        throw th;
                    }
                }
            } catch (Throwable th3) {
                th = th3;
                throw th;
            }
        }
        broker.registerNotifier(notifier);
    }

    public void resume() {
        int i = this.mActiveCount + 1;
        this.mActiveCount = i;
        if (i == 1) {
            Iterator i$ = this.mSourceMap.values().iterator();
            while (i$.hasNext()) {
                ((MediaSource) i$.next()).resume();
            }
        }
    }

    public void rotate(Path path, int degrees) {
        getMediaObject(path).rotate(degrees);
    }

    public void rotateMediaItems(ArrayList<MediaItem> items, int degrees) {
        ArrayList<ContentProviderOperation> operations = new ArrayList();
        Iterator i$ = items.iterator();
        while (i$.hasNext()) {
            MediaItem item = (MediaItem) i$.next();
            Uri uri = Files.getContentUri(getDeviceString(getDeviceFromPath(item.getPath())));
            ContentValues values = new ContentValues();
            int rotation = (item.getRotation() + degrees) % 360;
            if (rotation < 0) {
                rotation += 360;
            }
            if (item.getMimeType().equalsIgnoreCase("image/jpeg")) {
                String filePath = item.getFilePath();
                try {
                    ExifInterface exif = new ExifInterface(filePath);
                    exif.setAttribute("Orientation", LocalImage.getExifOrientation(rotation));
                    exif.saveAttributes();
                } catch (IOException e) {
                    Log.w(TAG, "cannot set exif data: " + filePath);
                }
                values.put("_size", Long.valueOf(new File(filePath).length()));
            }
            values.put("orientation", Integer.valueOf(rotation));
            Builder withValues = ContentProviderOperation.newUpdate(uri).withValues(values);
            String str = MusicUtils.THUMBDATA_WHERE_CLAUSE;
            String[] strArr = new String[1];
            strArr[0] = String.valueOf(item.getID());
            operations.add(withValues.withSelection(str, strArr).build());
        }
        try {
            this.mContext.getContentResolver().applyBatch("media", operations);
        } catch (RemoteException e2) {
            e2.printStackTrace();
        } catch (OperationApplicationException e3) {
            e3.printStackTrace();
        }
    }

    public void setMediaItemListCacher(MediaItemListCacher listCacher) {
        this.mMediaItemListCacher = listCacher;
    }
}