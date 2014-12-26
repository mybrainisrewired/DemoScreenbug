package com.wmt.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.util.Log;
import com.wmt.MusicPlayer.MusicUtils;
import com.wmt.data.MediaSet.ItemConsumer;
import com.wmt.data.MediaSource.PathId;
import com.wmt.util.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class LocalSource extends MediaSource {
    public static final int ALBUM_GROUP = 1;
    private static final int ALL_GROUP = 0;
    public static final int ARTIST_GROUP = 2;
    public static final String KEY_BUCKET_ID = "bucketId";
    public static final String KEY_MEDIA_TYPES = "mediaTypes";
    private static final int LOCAL_ALL_ALBUM = 7;
    private static final int LOCAL_ALL_ALBUMSET = 6;
    private static final int LOCAL_AUDIO_ALBUM = 25;
    private static final int LOCAL_AUDIO_ALBUMSET = 24;
    private static final int LOCAL_AUDIO_ALL = 27;
    private static final int LOCAL_AUDIO_ARTIST = 29;
    private static final int LOCAL_AUDIO_ARTISTSET = 28;
    private static final int LOCAL_AUDIO_ITEM = 26;
    private static final int LOCAL_AUDIO_PLAYLIST = 31;
    private static final int LOCAL_AUDIO_PLAYLISTSET = 30;
    private static final int LOCAL_AUDIO_RECENTLIST = 32;
    private static final int LOCAL_IMAGE_ALBUM = 2;
    private static final int LOCAL_IMAGE_ALBUMSET = 0;
    private static final int LOCAL_IMAGE_ITEM = 4;
    private static final int LOCAL_VIDEO_ALBUM = 3;
    private static final int LOCAL_VIDEO_ALBUMSET = 1;
    private static final int LOCAL_VIDEO_ITEM = 5;
    private static final int MEDIA_TYPE_IMAGE = 1;
    private static final int MEDIA_TYPE_VIDEO = 4;
    private static final int NO_MATCH = -1;
    public static final int PLAYLIST_GROUP = 3;
    private static final int SD_ALL_ALBUM = 15;
    private static final int SD_ALL_ALBUMSET = 14;
    private static final int SD_AUDIO_ALBUM = 34;
    private static final int SD_AUDIO_ALBUMSET = 33;
    private static final int SD_AUDIO_ALL = 36;
    private static final int SD_AUDIO_ARTIST = 38;
    private static final int SD_AUDIO_ARTISTSET = 37;
    private static final int SD_AUDIO_ITEM = 35;
    private static final int SD_AUDIO_PLAYLIST = 40;
    private static final int SD_AUDIO_PLAYLISTSET = 39;
    private static final int SD_AUDIO_RECENTLIST = 41;
    private static final int SD_IMAGE_ALBUM = 10;
    private static final int SD_IMAGE_ALBUMSET = 8;
    private static final int SD_IMAGE_ITEM = 12;
    private static final int SD_VIDEO_ALBUM = 11;
    private static final int SD_VIDEO_ALBUMSET = 9;
    private static final int SD_VIDEO_ITEM = 13;
    private static final String TAG = "LocalSource";
    private static final int USB_ALL_ALBUM = 23;
    private static final int USB_ALL_ALBUMSET = 22;
    private static final int USB_AUDIO_ALBUM = 43;
    private static final int USB_AUDIO_ALBUMSET = 42;
    private static final int USB_AUDIO_ALL = 45;
    private static final int USB_AUDIO_ARTIST = 47;
    private static final int USB_AUDIO_ARTISTSET = 46;
    private static final int USB_AUDIO_ITEM = 44;
    private static final int USB_AUDIO_PLAYLIST = 49;
    private static final int USB_AUDIO_PLAYLISTSET = 48;
    private static final int USB_AUDIO_RECENTLIST = 50;
    private static final int USB_IMAGE_ALBUM = 18;
    private static final int USB_IMAGE_ALBUMSET = 16;
    private static final int USB_IMAGE_ITEM = 20;
    private static final int USB_VIDEO_ALBUM = 19;
    private static final int USB_VIDEO_ALBUMSET = 17;
    private static final int USB_VIDEO_ITEM = 21;
    public static final Comparator<PathId> sIdComparator;
    private ContentResolver mContentResolver;
    private final DataManager mDataManager;
    private PathMatcher mMatcher;
    private final EnhancedUriMatcher mUriMatcher;

    private static class IdComparator implements Comparator<PathId> {
        private IdComparator() {
        }

        public int compare(PathId p1, PathId p2) {
            String s1 = p1.path.getSuffix();
            String s2 = p2.path.getSuffix();
            int len1 = s1.length();
            int len2 = s2.length();
            if (len1 < len2) {
                return NO_MATCH;
            }
            return len1 > len2 ? MEDIA_TYPE_IMAGE : s1.compareTo(s2);
        }
    }

    static {
        sIdComparator = new IdComparator();
    }

    public LocalSource(ContentResolver resolver, DataManager dataManager) {
        super(new String[]{MusicUtils.LOCAL_VOLUME, "sd", "usb"});
        this.mUriMatcher = new EnhancedUriMatcher(-1);
        this.mContentResolver = resolver;
        this.mDataManager = dataManager;
        this.mMatcher = new PathMatcher();
        this.mMatcher.add("/local/image", LOCAL_IMAGE_ALBUMSET);
        this.mMatcher.add("/local/video", MEDIA_TYPE_IMAGE);
        this.mMatcher.add("/local/all", LOCAL_ALL_ALBUMSET);
        this.mMatcher.add("/local/image/*", LOCAL_IMAGE_ALBUM);
        this.mMatcher.add("/local/video/*", PLAYLIST_GROUP);
        this.mMatcher.add("/local/all/*", LOCAL_ALL_ALBUM);
        this.mMatcher.add("/local/image/item/*", MEDIA_TYPE_VIDEO);
        this.mMatcher.add("/local/video/item/*", LOCAL_VIDEO_ITEM);
        this.mMatcher.add("/sd/image", SD_IMAGE_ALBUMSET);
        this.mMatcher.add("/sd/video", SD_VIDEO_ALBUMSET);
        this.mMatcher.add("/sd/all", SD_ALL_ALBUMSET);
        this.mMatcher.add("/sd/image/*", SD_IMAGE_ALBUM);
        this.mMatcher.add("/sd/video/*", SD_VIDEO_ALBUM);
        this.mMatcher.add("/sd/all/*", SD_ALL_ALBUM);
        this.mMatcher.add("/sd/image/item/*", SD_IMAGE_ITEM);
        this.mMatcher.add("/sd/video/item/*", SD_VIDEO_ITEM);
        this.mMatcher.add("/usb/image", USB_IMAGE_ALBUMSET);
        this.mMatcher.add("/usb/video", USB_VIDEO_ALBUMSET);
        this.mMatcher.add("/usb/all", USB_ALL_ALBUMSET);
        this.mMatcher.add("/usb/image/*", USB_IMAGE_ALBUM);
        this.mMatcher.add("/usb/video/*", USB_VIDEO_ALBUM);
        this.mMatcher.add("/usb/all/*", USB_ALL_ALBUM);
        this.mMatcher.add("/usb/image/item/*", USB_IMAGE_ITEM);
        this.mMatcher.add("/usb/video/item/*", USB_VIDEO_ITEM);
        this.mMatcher.add("/local/audio/album", LOCAL_AUDIO_ALBUMSET);
        this.mMatcher.add("/local/audio/album/*", LOCAL_AUDIO_ALBUM);
        this.mMatcher.add("/local/audio/item/*", LOCAL_AUDIO_ITEM);
        this.mMatcher.add("/local/audio/*", LOCAL_AUDIO_ALL);
        this.mMatcher.add("/local/audio/artist", LOCAL_AUDIO_ARTISTSET);
        this.mMatcher.add("/local/audio/artist/*", LOCAL_AUDIO_ARTIST);
        this.mMatcher.add("/local/audio/playlist", LOCAL_AUDIO_PLAYLISTSET);
        this.mMatcher.add("/local/audio/playlist/*", LOCAL_AUDIO_PLAYLIST);
        this.mMatcher.add("/local/audio/recentlist/*", LOCAL_AUDIO_RECENTLIST);
        this.mMatcher.add("/sd/audio/album", SD_AUDIO_ALBUMSET);
        this.mMatcher.add("/sd/audio/album/*", SD_AUDIO_ALBUM);
        this.mMatcher.add("/sd/audio/item/*", SD_AUDIO_ITEM);
        this.mMatcher.add("/sd/audio/*", SD_AUDIO_ALL);
        this.mMatcher.add("/sd/audio/artist", SD_AUDIO_ARTISTSET);
        this.mMatcher.add("/sd/audio/artist/*", SD_AUDIO_ARTIST);
        this.mMatcher.add("/sd/audio/playlist", SD_AUDIO_PLAYLISTSET);
        this.mMatcher.add("/sd/audio/playlist/*", SD_AUDIO_PLAYLIST);
        this.mMatcher.add("/sd/audio/recentlist/*", SD_AUDIO_RECENTLIST);
        this.mMatcher.add("/usb/audio/album", USB_AUDIO_ALBUMSET);
        this.mMatcher.add("/usb/audio/album/*", USB_AUDIO_ALBUM);
        this.mMatcher.add("/usb/audio/item/*", USB_AUDIO_ITEM);
        this.mMatcher.add("/usb/audio/*", USB_AUDIO_ALL);
        this.mMatcher.add("/usb/audio/artist", USB_AUDIO_ARTISTSET);
        this.mMatcher.add("/usb/audio/artist/*", USB_AUDIO_ARTIST);
        this.mMatcher.add("/usb/audio/playlist", USB_AUDIO_PLAYLISTSET);
        this.mMatcher.add("/usb/audio/playlist/*", USB_AUDIO_PLAYLIST);
        this.mMatcher.add("/usb/audio/recentlist/*", USB_AUDIO_RECENTLIST);
        this.mUriMatcher.addURI("media", "external/images/media/#", SD_IMAGE_ITEM);
        this.mUriMatcher.addURI("media", "external/video/media/#", SD_VIDEO_ITEM);
        this.mUriMatcher.addURI("media", "external/images/media", SD_IMAGE_ALBUM);
        this.mUriMatcher.addURI("media", "external/video/media", SD_VIDEO_ALBUM);
        this.mUriMatcher.addURI("media", "local/images/media/#", MEDIA_TYPE_VIDEO);
        this.mUriMatcher.addURI("media", "local/video/media/#", LOCAL_VIDEO_ITEM);
        this.mUriMatcher.addURI("media", "local/images/media", LOCAL_IMAGE_ALBUM);
        this.mUriMatcher.addURI("media", "local/video/media", PLAYLIST_GROUP);
        this.mUriMatcher.addURI("media", "udisk/images/media/#", USB_IMAGE_ITEM);
        this.mUriMatcher.addURI("media", "udisk/video/media/#", USB_VIDEO_ITEM);
        this.mUriMatcher.addURI("media", "udisk/images/media", USB_IMAGE_ALBUM);
        this.mUriMatcher.addURI("media", "udisk/video/media", USB_VIDEO_ALBUM);
        this.mUriMatcher.addURI("media", "external/audio/media/#", SD_AUDIO_ITEM);
        this.mUriMatcher.addURI("media", "local/audio/media/#", LOCAL_AUDIO_ITEM);
        this.mUriMatcher.addURI("media", "udisk/audio/media/#", USB_AUDIO_ITEM);
        this.mUriMatcher.addURI("media", "external/audio/playlists/#", SD_AUDIO_PLAYLIST);
        this.mUriMatcher.addURI("media", "local/audio/playlists/#", LOCAL_AUDIO_PLAYLIST);
        this.mUriMatcher.addURI("media", "udisk/audio/playlists/#", USB_AUDIO_PLAYLIST);
    }

    private Path getAlbumPath(String device, Uri uri, int defaultType) {
        int mediaType = getMediaType(uri.getQueryParameter(KEY_MEDIA_TYPES), defaultType);
        String bucketId = uri.getQueryParameter(KEY_BUCKET_ID);
        try {
            int id = Integer.parseInt(bucketId);
            switch (mediaType) {
                case MEDIA_TYPE_IMAGE:
                    return Path.fromString("/" + device + "/image").getChild(id);
                case MEDIA_TYPE_VIDEO:
                    return Path.fromString("/" + device + "/video").getChild(id);
                default:
                    return Path.fromString("/" + device + "/all").getChild(id);
            }
        } catch (NumberFormatException e) {
            Log.w(TAG, "invalid bucket id: " + bucketId, e);
            return null;
        }
    }

    public static int getAudioGroupType(Path path) {
        PathMatcher matcher = new PathMatcher();
        matcher.add("/*/audio/album", MEDIA_TYPE_IMAGE);
        matcher.add("/*/audio/album/*", MEDIA_TYPE_IMAGE);
        matcher.add("/*/audio/artist", LOCAL_IMAGE_ALBUM);
        matcher.add("/*/audio/artist/*", LOCAL_IMAGE_ALBUM);
        matcher.add("/*/audio/playlist", PLAYLIST_GROUP);
        matcher.add("/*/audio/playlist/*", PLAYLIST_GROUP);
        matcher.add("/*/audio", MEDIA_TYPE_IMAGE);
        return matcher.match(path);
    }

    private Path getAudioPlaylistPath(String device, EnhancedUriMatcher matcher) {
        return Path.fromString("/" + device + "/audio/playlist").getChild(matcher.getIntVar(LOCAL_IMAGE_ALBUMSET));
    }

    private static int getMediaType(String type, int defaultType) {
        if (type == null) {
            return defaultType;
        }
        try {
            int value = Integer.parseInt(type);
            return (value & 5) != 0 ? value : defaultType;
        } catch (NumberFormatException e) {
            Log.w(TAG, "invalid type: " + type, e);
            return defaultType;
        }
    }

    private void processMapMediaItems(int device, ArrayList<PathId> list, ItemConsumer consumer, boolean isImage) {
        Collections.sort(list, sIdComparator);
        int n = list.size();
        int i = LOCAL_IMAGE_ALBUMSET;
        while (i < n) {
            PathId pid = (PathId) list.get(i);
            ArrayList<Integer> ids = new ArrayList();
            int startId = Integer.parseInt(pid.path.getSuffix());
            ids.add(Integer.valueOf(startId));
            int j = i + 1;
            while (j < n) {
                int curId = Integer.parseInt(((PathId) list.get(j)).path.getSuffix());
                if (curId - startId >= 500) {
                    break;
                }
                ids.add(Integer.valueOf(curId));
                j++;
            }
            MediaItem[] items = LocalAlbum.getMediaItemById(this.mContentResolver, device, isImage, ids);
            int k = i;
            while (k < j) {
                consumer.consume(list.get(k).id, items[k - i]);
                k++;
            }
            i = j;
        }
    }

    public MediaObject createMediaObject(Path path) {
        switch (this.mMatcher.match(path)) {
            case LOCAL_IMAGE_ALBUMSET:
            case MEDIA_TYPE_IMAGE:
            case LOCAL_ALL_ALBUMSET:
            case SD_IMAGE_ALBUMSET:
            case SD_VIDEO_ALBUMSET:
            case SD_ALL_ALBUMSET:
            case USB_IMAGE_ALBUMSET:
            case USB_VIDEO_ALBUMSET:
            case USB_ALL_ALBUMSET:
                return new LocalAlbumSet(this.mDataManager, path, this.mContentResolver, "");
            case LOCAL_IMAGE_ALBUM:
            case SD_IMAGE_ALBUM:
            case USB_IMAGE_ALBUM:
                return new LocalAlbum(this.mDataManager, path, this.mContentResolver, this.mMatcher.getIntVar(LOCAL_IMAGE_ALBUMSET), true);
            case PLAYLIST_GROUP:
            case SD_VIDEO_ALBUM:
            case USB_VIDEO_ALBUM:
                return new LocalAlbum(this.mDataManager, path, this.mContentResolver, this.mMatcher.getIntVar(LOCAL_IMAGE_ALBUMSET), false);
            case MEDIA_TYPE_VIDEO:
            case SD_IMAGE_ITEM:
            case USB_IMAGE_ITEM:
                return new LocalImage(path, this.mContentResolver, this.mMatcher.getIntVar(LOCAL_IMAGE_ALBUMSET));
            case LOCAL_VIDEO_ITEM:
            case SD_VIDEO_ITEM:
            case USB_VIDEO_ITEM:
                return new LocalVideo(path, this.mContentResolver, this.mMatcher.getIntVar(LOCAL_IMAGE_ALBUMSET));
            case LOCAL_ALL_ALBUM:
                MediaSet imageSet;
                MediaSet videoSet;
                int bucketId = this.mMatcher.getIntVar(LOCAL_IMAGE_ALBUMSET);
                String device = this.mMatcher.getVar(LOCAL_IMAGE_ALBUMSET);
                if (MusicUtils.LOCAL_VOLUME.equals(device)) {
                    imageSet = LocalAlbumSet.LOCAL_PATH_IMAGE.getChild(bucketId).getObject();
                    videoSet = LocalAlbumSet.LOCAL_PATH_VIDEO.getChild(bucketId).getObject();
                } else if ("sd".equals(device)) {
                    imageSet = LocalAlbumSet.SD_PATH_IMAGE.getChild(bucketId).getObject();
                    videoSet = LocalAlbumSet.SD_PATH_VIDEO.getChild(bucketId).getObject();
                } else {
                    imageSet = LocalAlbumSet.USB_PATH_IMAGE.getChild(bucketId).getObject();
                    videoSet = LocalAlbumSet.USB_PATH_VIDEO.getChild(bucketId).getObject();
                }
                return new LocalMergeAlbum(path, DataManager.sDateTakenComparator, new MediaSet[]{imageSet, videoSet});
            case LOCAL_AUDIO_ALBUMSET:
            case LOCAL_AUDIO_ARTISTSET:
            case LOCAL_AUDIO_PLAYLISTSET:
            case SD_AUDIO_ALBUMSET:
            case SD_AUDIO_ARTISTSET:
            case SD_AUDIO_PLAYLISTSET:
            case USB_AUDIO_ALBUMSET:
            case USB_AUDIO_ARTISTSET:
            case USB_AUDIO_PLAYLISTSET:
                return new LocalAudioGroupSet(this.mDataManager, path, this.mContentResolver, "");
            case LOCAL_AUDIO_ALBUM:
            case SD_AUDIO_ALBUM:
            case USB_AUDIO_ALBUM:
                return new LocalAudioAlbum(this.mDataManager, path, this.mContentResolver, this.mMatcher.getIntVar(LOCAL_IMAGE_ALBUMSET));
            case LOCAL_AUDIO_ITEM:
            case SD_AUDIO_ITEM:
            case USB_AUDIO_ITEM:
                return new LocalAudio(path, this.mContentResolver, this.mMatcher.getIntVar(LOCAL_IMAGE_ALBUMSET), this.mDataManager.isBigThumbDataEnabled());
            case LOCAL_AUDIO_ALL:
            case SD_AUDIO_ALL:
            case USB_AUDIO_ALL:
                return new LocalAudioAll(this.mDataManager, path, this.mContentResolver, "", this.mMatcher.getIntVar(LOCAL_IMAGE_ALBUMSET));
            case LOCAL_AUDIO_ARTIST:
            case SD_AUDIO_ARTIST:
            case USB_AUDIO_ARTIST:
                return new LocalAudioArtist(this.mDataManager, path, this.mContentResolver, this.mMatcher.getIntVar(LOCAL_IMAGE_ALBUMSET));
            case LOCAL_AUDIO_PLAYLIST:
            case SD_AUDIO_PLAYLIST:
            case USB_AUDIO_PLAYLIST:
                return new LocalAudioPlaylist(this.mDataManager, path, this.mContentResolver, this.mMatcher.getIntVar(LOCAL_IMAGE_ALBUMSET));
            case LOCAL_AUDIO_RECENTLIST:
            case SD_AUDIO_RECENTLIST:
            case USB_AUDIO_RECENTLIST:
                return new LocalAudioAll(this.mDataManager, path, this.mContentResolver, "", this.mMatcher.getLongVar(LOCAL_IMAGE_ALBUMSET));
            default:
                throw new RuntimeException("bad path: " + path);
        }
    }

    public Path findPathByUri(Uri uri) {
        Path path = null;
        try {
            Log.i(TAG, "mUriMatcher.match(uri)=" + this.mUriMatcher.match(uri));
            long id;
            switch (this.mUriMatcher.match(uri)) {
                case LOCAL_IMAGE_ALBUM:
                    return getAlbumPath(MusicUtils.LOCAL_VOLUME, uri, MEDIA_TYPE_IMAGE);
                case PLAYLIST_GROUP:
                    return getAlbumPath(MusicUtils.LOCAL_VOLUME, uri, MEDIA_TYPE_VIDEO);
                case MEDIA_TYPE_VIDEO:
                    id = ContentUris.parseId(uri);
                    return id >= 0 ? LocalImage.LOCAL_ITEM_PATH.getChild(id) : path;
                case LOCAL_VIDEO_ITEM:
                    id = ContentUris.parseId(uri);
                    return id >= 0 ? LocalVideo.LOCAL_ITEM_PATH.getChild(id) : path;
                case SD_IMAGE_ALBUM:
                    return getAlbumPath("sd", uri, MEDIA_TYPE_IMAGE);
                case SD_VIDEO_ALBUM:
                    return getAlbumPath("sd", uri, MEDIA_TYPE_VIDEO);
                case SD_IMAGE_ITEM:
                    id = ContentUris.parseId(uri);
                    return id >= 0 ? LocalImage.SD_ITEM_PATH.getChild(id) : path;
                case SD_VIDEO_ITEM:
                    id = ContentUris.parseId(uri);
                    return id >= 0 ? LocalVideo.SD_ITEM_PATH.getChild(id) : path;
                case USB_IMAGE_ALBUM:
                    return getAlbumPath("usb", uri, MEDIA_TYPE_IMAGE);
                case USB_VIDEO_ALBUM:
                    return getAlbumPath("usb", uri, MEDIA_TYPE_VIDEO);
                case USB_IMAGE_ITEM:
                    id = ContentUris.parseId(uri);
                    return id >= 0 ? LocalImage.USB_ITEM_PATH.getChild(id) : path;
                case USB_VIDEO_ITEM:
                    id = ContentUris.parseId(uri);
                    return id >= 0 ? LocalVideo.USB_ITEM_PATH.getChild(id) : path;
                case LOCAL_AUDIO_PLAYLIST:
                    return getAudioPlaylistPath(MusicUtils.LOCAL_VOLUME, this.mUriMatcher);
                case SD_AUDIO_PLAYLIST:
                    return getAudioPlaylistPath("sd", this.mUriMatcher);
                case USB_AUDIO_PLAYLIST:
                    return getAudioPlaylistPath("usb", this.mUriMatcher);
                default:
                    Log.w(TAG, "unhandle uri : " + uri.toString());
                    return path;
            }
        } catch (NumberFormatException e) {
            Log.w(TAG, "uri: " + uri.toString(), e);
            return path;
        }
    }

    public Path getDefaultSetOf(Path item) {
        MediaObject object = item.getObject();
        return object instanceof LocalMediaItem ? Path.fromString(item.getParent().getParent().toString()).getChild(String.valueOf(((LocalMediaItem) object).getBucketId())) : null;
    }

    public void mapMediaItems(ArrayList<PathId> list, ItemConsumer consumer) {
        ArrayList<PathId> imageList = new ArrayList();
        ArrayList<PathId> videoList = new ArrayList();
        int n = list.size();
        int device = MEDIA_TYPE_IMAGE;
        boolean sameDevice = true;
        int i = LOCAL_IMAGE_ALBUMSET;
        while (i < n) {
            PathId pid = (PathId) list.get(i);
            Path parent = pid.path.getParent();
            if (i <= 0 || DataManager.getDeviceFromPath(parent) == device) {
                device = DataManager.getDeviceFromPath(parent);
            } else {
                sameDevice = false;
            }
            if (parent == LocalImage.LOCAL_ITEM_PATH || parent == LocalImage.SD_ITEM_PATH || parent == LocalImage.USB_ITEM_PATH) {
                imageList.add(pid);
            } else if (parent == LocalVideo.LOCAL_ITEM_PATH || parent == LocalVideo.SD_ITEM_PATH || parent == LocalVideo.USB_ITEM_PATH) {
                videoList.add(pid);
            }
            i++;
        }
        Utils.assertTrue(sameDevice);
        processMapMediaItems(device, imageList, consumer, true);
        processMapMediaItems(device, videoList, consumer, false);
    }

    public void pause() {
    }

    public void resume() {
    }
}