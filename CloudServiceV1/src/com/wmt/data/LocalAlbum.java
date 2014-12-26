package com.wmt.data;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Video;
import android.util.Log;
import com.wmt.MusicPlayer.MusicUtils;
import com.wmt.util.Utils;
import java.util.ArrayList;

public class LocalAlbum extends MediaSet {
    private static final String[] COUNT_PROJECTION;
    private static final String TAG = "LocalAlbum";
    private final Uri mBaseUri;
    private final int mBucketId;
    private final String mBucketName;
    private int mCachedCount;
    private final String mDefaultOrderClause;
    private final String mDefaultWhereClause;
    private int mDevice;
    private int mFilterSize;
    private final boolean mIsImage;
    private final Path mItemPath;
    private final ChangeNotifier mNotifier;
    private String mOrderByField;
    private final String[] mProjection;
    private final ContentResolver mResolver;

    static {
        COUNT_PROJECTION = new String[]{"count(*)"};
    }

    public LocalAlbum(DataManager dataManager, Path path, ContentResolver resolver, int bucketId, boolean isImage) {
        this(dataManager, path, resolver, bucketId, isImage, LocalAlbumSet.getBucketName(path, resolver, bucketId));
    }

    public LocalAlbum(DataManager dataManager, Path path, ContentResolver resolver, int bucketId, boolean isImage, String name) {
        super(path, nextVersionNumber());
        this.mCachedCount = -1;
        this.mFilterSize = 0;
        this.mOrderByField = null;
        this.mResolver = resolver;
        this.mBucketId = bucketId;
        this.mBucketName = name;
        this.mIsImage = isImage;
        this.mDevice = DataManager.getDeviceFromPath(path);
        if (isImage) {
            this.mDefaultWhereClause = "bucket_id = ?";
            this.mDefaultOrderClause = "datetaken DESC, _id DESC";
            this.mBaseUri = Media.getContentUri(DataManager.getDeviceString(this.mDevice));
            if (this.mDevice == 0) {
                this.mItemPath = LocalImage.LOCAL_ITEM_PATH;
            } else if (this.mDevice == 1) {
                this.mItemPath = LocalImage.SD_ITEM_PATH;
            } else {
                this.mItemPath = LocalImage.USB_ITEM_PATH;
            }
            this.mProjection = LocalImage.getProjection();
        } else {
            this.mDefaultWhereClause = "bucket_id = ?";
            this.mDefaultOrderClause = "datetaken DESC, _id DESC";
            this.mBaseUri = Video.Media.getContentUri(DataManager.getDeviceString(this.mDevice));
            if (this.mDevice == 0) {
                this.mItemPath = LocalVideo.LOCAL_ITEM_PATH;
            } else if (this.mDevice == 1) {
                this.mItemPath = LocalVideo.SD_ITEM_PATH;
            } else {
                this.mItemPath = LocalVideo.USB_ITEM_PATH;
            }
            this.mProjection = LocalVideo.getProjection();
        }
        this.mNotifier = new ChangeNotifier(this, this.mBaseUri, dataManager);
    }

    public static Cursor getItemCursor(ContentResolver resolver, Uri uri, String[] projection, int id) {
        return resolver.query(uri, projection, MusicUtils.THUMBDATA_WHERE_CLAUSE, new String[]{String.valueOf(id)}, null);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.wmt.data.MediaItem[] getMediaItemById(android.content.ContentResolver r18_resolver, int r19_device, boolean r20_isImage, java.util.ArrayList<java.lang.Integer> r21_ids) {
        throw new UnsupportedOperationException("Method not decompiled: com.wmt.data.LocalAlbum.getMediaItemById(android.content.ContentResolver, int, boolean, java.util.ArrayList):com.wmt.data.MediaItem[]");
        /*
        r2 = r21.size();
        r0 = new com.wmt.data.MediaItem[r2];
        r17 = r0;
        r2 = r21.isEmpty();
        if (r2 == 0) goto L_0x000f;
    L_0x000e:
        return r17;
    L_0x000f:
        r2 = 0;
        r0 = r21;
        r2 = r0.get(r2);
        r2 = (java.lang.Integer) r2;
        r13 = r2.intValue();
        r2 = r21.size();
        r2 = r2 + -1;
        r0 = r21;
        r2 = r0.get(r2);
        r2 = (java.lang.Integer) r2;
        r12 = r2.intValue();
        if (r20 == 0) goto L_0x0081;
    L_0x0030:
        r2 = com.wmt.data.DataManager.getDeviceString(r19);
        r3 = android.provider.MediaStore.Images.Media.getContentUri(r2);
        if (r19 != 0) goto L_0x0076;
    L_0x003a:
        r15 = com.wmt.data.LocalImage.LOCAL_ITEM_PATH;
    L_0x003c:
        r4 = com.wmt.data.LocalImage.getProjection();
    L_0x0040:
        r5 = "_id BETWEEN ? AND ?";
        r2 = 2;
        r6 = new java.lang.String[r2];
        r2 = 0;
        r7 = java.lang.String.valueOf(r13);
        r6[r2] = r7;
        r2 = 1;
        r7 = java.lang.String.valueOf(r12);
        r6[r2] = r7;
        r7 = "_id";
        r2 = r18;
        r9 = r2.query(r3, r4, r5, r6, r7);
        if (r9 != 0) goto L_0x009d;
    L_0x005d:
        r2 = "LocalAlbum";
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "query fail";
        r5 = r5.append(r6);
        r5 = r5.append(r3);
        r5 = r5.toString();
        android.util.Log.w(r2, r5);
        goto L_0x000e;
    L_0x0076:
        r2 = 1;
        r0 = r19;
        if (r0 != r2) goto L_0x007e;
    L_0x007b:
        r15 = com.wmt.data.LocalImage.SD_ITEM_PATH;
        goto L_0x003c;
    L_0x007e:
        r15 = com.wmt.data.LocalImage.USB_ITEM_PATH;
        goto L_0x003c;
    L_0x0081:
        r2 = com.wmt.data.DataManager.getDeviceString(r19);
        r3 = android.provider.MediaStore.Video.Media.getContentUri(r2);
        if (r19 != 0) goto L_0x0092;
    L_0x008b:
        r15 = com.wmt.data.LocalVideo.LOCAL_ITEM_PATH;
    L_0x008d:
        r4 = com.wmt.data.LocalVideo.getProjection();
        goto L_0x0040;
    L_0x0092:
        r2 = 1;
        r0 = r19;
        if (r0 != r2) goto L_0x009a;
    L_0x0097:
        r15 = com.wmt.data.LocalVideo.SD_ITEM_PATH;
        goto L_0x008d;
    L_0x009a:
        r15 = com.wmt.data.LocalVideo.USB_ITEM_PATH;
        goto L_0x008d;
    L_0x009d:
        r16 = r21.size();	 Catch:{ all -> 0x00ee }
        r10 = 0;
    L_0x00a2:
        r0 = r16;
        if (r10 >= r0) goto L_0x00e9;
    L_0x00a6:
        r2 = r9.moveToNext();	 Catch:{ all -> 0x00ee }
        if (r2 == 0) goto L_0x00e9;
    L_0x00ac:
        r2 = 0;
        r11 = r9.getInt(r2);	 Catch:{ all -> 0x00ee }
        r0 = r21;
        r2 = r0.get(r10);	 Catch:{ all -> 0x00ee }
        r2 = (java.lang.Integer) r2;	 Catch:{ all -> 0x00ee }
        r2 = r2.intValue();	 Catch:{ all -> 0x00ee }
        if (r2 > r11) goto L_0x00a2;
    L_0x00bf:
        r0 = r21;
        r2 = r0.get(r10);	 Catch:{ all -> 0x00ee }
        r2 = (java.lang.Integer) r2;	 Catch:{ all -> 0x00ee }
        r2 = r2.intValue();	 Catch:{ all -> 0x00ee }
        if (r2 >= r11) goto L_0x00d8;
    L_0x00cd:
        r10 = r10 + 1;
        r0 = r16;
        if (r10 < r0) goto L_0x00bf;
    L_0x00d3:
        r9.close();
        goto L_0x000e;
    L_0x00d8:
        r8 = r15.getChild(r11);	 Catch:{ all -> 0x00ee }
        r0 = r18;
        r1 = r20;
        r14 = loadOrUpdateItem(r0, r8, r9, r1);	 Catch:{ all -> 0x00ee }
        r17[r10] = r14;	 Catch:{ all -> 0x00ee }
        r10 = r10 + 1;
        goto L_0x00a2;
    L_0x00e9:
        r9.close();
        goto L_0x000e;
    L_0x00ee:
        r2 = move-exception;
        r9.close();
        throw r2;
        */
    }

    private String getOrderByClause() {
        return this.mOrderByField != null ? this.mOrderByField + " DESC, _id DESC" : this.mDefaultOrderClause;
    }

    private String[] getWhereArgs() {
        if (this.mFilterSize > 0) {
            return new String[]{String.valueOf(this.mBucketId), String.valueOf(this.mFilterSize)};
        } else {
            return new String[]{String.valueOf(this.mBucketId)};
        }
    }

    private String getWhereClause() {
        return this.mFilterSize > 0 ? "bucket_id = ? and _size > ?" : this.mDefaultWhereClause;
    }

    private void loadIfNecessary() {
        if (this.mNotifier.isDirty()) {
            this.mDataVersion = nextVersionNumber();
            this.mCachedCount = -1;
        }
    }

    private static MediaItem loadOrUpdateItem(ContentResolver resolver, Path path, Cursor cursor, boolean isImage) {
        LocalMediaItem item = (LocalMediaItem) path.getObject();
        if (item == null) {
            return isImage ? new LocalImage(path, resolver, cursor) : new LocalVideo(path, resolver, cursor);
        } else {
            item.updateContent(cursor);
            return item;
        }
    }

    public void delete() {
        this.mResolver.delete(this.mBaseUri, getWhereClause(), getWhereArgs());
    }

    public ArrayList<MediaItem> getMediaItem(int start, int count) {
        loadIfNecessary();
        Uri uri = this.mBaseUri.buildUpon().appendQueryParameter("limit", start + "," + count).build();
        ArrayList<MediaItem> list = new ArrayList();
        Cursor cursor = null;
        try {
            cursor = this.mResolver.query(uri, this.mProjection, getWhereClause(), getWhereArgs(), getOrderByClause());
            if (cursor == null) {
                Log.w(TAG, "query fail: " + uri);
                if (cursor != null) {
                    cursor.close();
                }
                return list;
            }
            while (cursor.moveToNext()) {
                list.add(loadOrUpdateItem(this.mResolver, this.mItemPath.getChild(cursor.getInt(0)), cursor, this.mIsImage));
            }
            if (cursor != null) {
                cursor.close();
            }
            return list;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public int getMediaItemCount() {
        loadIfNecessary();
        if (this.mCachedCount == -1) {
            Cursor cursor = this.mResolver.query(this.mBaseUri, COUNT_PROJECTION, getWhereClause(), getWhereArgs(), null);
            if (cursor == null) {
                Log.w(TAG, "query fail");
                return 0;
            } else {
                try {
                    Utils.assertTrue(cursor.moveToNext());
                    this.mCachedCount = cursor.getInt(0);
                    cursor.close();
                } catch (Exception e) {
                    Exception e2 = e;
                    Log.v(TAG, "----------- exception --------------");
                    e2.printStackTrace();
                    cursor.close();
                }
            }
        }
        return this.mCachedCount;
    }

    public String getName() {
        return this.mBucketName;
    }

    public int getSupportedOperations() {
        return 1029;
    }

    public boolean isLeafAlbum() {
        return true;
    }

    public long reload() {
        loadIfNecessary();
        return this.mDataVersion;
    }

    public void setFilterSize(int sizeInByte) {
        if (this.mFilterSize != sizeInByte) {
            this.mFilterSize = sizeInByte;
            this.mCachedCount = -1;
        }
    }

    public void setOrderBy(String filedName) {
        this.mOrderByField = filedName;
    }
}