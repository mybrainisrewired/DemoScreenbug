package com.millennialmedia.android;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.gms.plus.PlusShare;
import java.io.Externalizable;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class VideoAd extends CachedAd implements Parcelable, Externalizable {
    public static final Creator<VideoAd> CREATOR;
    private static final String TAG = "VideoAd";
    static final String VIDEO_FILE_ID = "video.dat";
    static final long serialVersionUID = 2679125946930815832L;
    ArrayList<VideoLogEvent> activities;
    ArrayList<VideoImage> buttons;
    String[] cacheComplete;
    String[] cacheFailed;
    String cacheMissURL;
    DTOCachedVideo cachedVideoDto;
    long closeDelayMillis;
    long contentLength;
    long duration;
    String[] endActivity;
    String endOverlayURL;
    String onCompletionUrl;
    boolean reloadNonEndOverlayOnRestart;
    boolean showControls;
    boolean showCountdown;
    String[] startActivity;
    boolean stayInPlayer;
    boolean usingInternalStorage;
    String[] videoError;
    private String videoFileId;
    String webOverlayURL;

    private static class VideoFilenameFilter implements FilenameFilter {
        private WeakReference<VideoAd> videoAdRef;

        public VideoFilenameFilter(VideoAd videoAd) {
            this.videoAdRef = new WeakReference(videoAd);
        }

        public boolean accept(File dir, String filename) {
            VideoAd videoAd = (VideoAd) this.videoAdRef.get();
            if (videoAd == null) {
                return false;
            }
            String id = videoAd.getId();
            return id == null ? false : filename.startsWith(id);
        }
    }

    private static class VideoIterator extends Iterator {
        private WeakReference<Context> contextRef;
        boolean hasSharedVideoFile;
        private WeakReference<VideoAd> videoAdRef;

        public VideoIterator(Context context, VideoAd videoAd) {
            this.hasSharedVideoFile = false;
            this.videoAdRef = new WeakReference(videoAd);
            this.contextRef = new WeakReference(context);
        }

        boolean callback(CachedAd cachedAd) {
            if (cachedAd != null && cachedAd instanceof VideoAd) {
                VideoAd videoAd = (VideoAd) cachedAd;
                VideoAd requestingVideoAd = (VideoAd) this.videoAdRef.get();
                if (requestingVideoAd != null && videoAd.videoFileId.equals(requestingVideoAd.videoFileId)) {
                    this.hasSharedVideoFile = true;
                }
            }
            return super.callback(cachedAd);
        }

        void deleteVideoFile(Context context) {
            VideoAd requestingVideoAd = (VideoAd) this.videoAdRef.get();
            if (requestingVideoAd != null && AdCache.deleteFile(context, requestingVideoAd.videoFileId + VIDEO_FILE_ID)) {
                MMLog.v(TAG, String.format("VideoAd video file %s was deleted.", new Object[]{requestingVideoAd.videoFileId}));
            }
        }

        void done() {
            if (!this.hasSharedVideoFile) {
                Context context = (Context) this.contextRef.get();
                if (context != null) {
                    deleteVideoFile(context);
                }
            }
            super.done();
        }
    }

    static {
        CREATOR = new Creator<VideoAd>() {
            public VideoAd createFromParcel(Parcel in) {
                return new VideoAd(in);
            }

            public VideoAd[] newArray(int size) {
                return new VideoAd[size];
            }
        };
    }

    public VideoAd() {
        this.buttons = new ArrayList();
        this.activities = new ArrayList();
    }

    VideoAd(Parcel in) {
        super(in);
        this.buttons = new ArrayList();
        this.activities = new ArrayList();
        try {
            this.startActivity = new String[in.readInt()];
            in.readStringArray(this.startActivity);
            this.endActivity = new String[in.readInt()];
            in.readStringArray(this.endActivity);
            boolean[] boolArray = new boolean[5];
            in.readBooleanArray(boolArray);
            this.showControls = boolArray[0];
            this.stayInPlayer = boolArray[1];
            this.showCountdown = boolArray[2];
            this.reloadNonEndOverlayOnRestart = boolArray[3];
            this.usingInternalStorage = boolArray[4];
            this.onCompletionUrl = in.readString();
            this.webOverlayURL = in.readString();
            this.endOverlayURL = in.readString();
            this.cacheMissURL = in.readString();
            this.videoFileId = in.readString();
            this.duration = in.readLong();
            this.contentLength = in.readLong();
            this.closeDelayMillis = in.readLong();
            this.buttons = in.readArrayList(VideoImage.class.getClassLoader());
            this.activities = in.readArrayList(VideoLogEvent.class.getClassLoader());
            this.cacheComplete = new String[in.readInt()];
            in.readStringArray(this.cacheComplete);
            this.cacheFailed = new String[in.readInt()];
            in.readStringArray(this.cacheFailed);
            this.videoError = new String[in.readInt()];
            in.readStringArray(this.videoError);
        } catch (Exception e) {
            MMLog.e(TAG, "Exception with videoad parcel creation: ", e);
        }
    }

    VideoAd(String jsonString) {
        this.buttons = new ArrayList();
        this.activities = new ArrayList();
        if (jsonString != null) {
            JSONObject jsonAdObject = null;
            try {
                jsonAdObject = new JSONObject(jsonString);
            } catch (JSONException e) {
                MMLog.e(TAG, "VideoAd json exception: ", e);
            }
            if (jsonAdObject != null) {
                JSONObject videoObject = jsonAdObject.optJSONObject("video");
                if (videoObject != null) {
                    deserializeFromObj(videoObject);
                }
            }
        }
    }

    static boolean downloadVideoFile(Context context, String contentUrl, String videoFileId) {
        boolean success = AdCache.downloadComponentExternalStorage(contentUrl, videoFileId + VIDEO_FILE_ID, context);
        MMLog.v(TAG, String.format("Caching completed successfully (" + videoFileId + VIDEO_FILE_ID + ")? %b", new Object[]{Boolean.valueOf(success)}));
        return success;
    }

    static Uri getVideoUri(Context context, String videoFileId) {
        return Uri.fromFile(AdCache.getDownloadFile(context, videoFileId + VIDEO_FILE_ID));
    }

    private void handleSharedVideoFile(Context context) {
        AdCache.iterateCachedAds(context, MMAdView.TRANSITION_UP, new VideoIterator(context, this));
    }

    static boolean hasVideoFile(Context context, String videoFileId) {
        return AdCache.hasDownloadFile(context, videoFileId + VIDEO_FILE_ID);
    }

    static void playAd(Context context, String id, RedirectionListenerImpl listener) {
        if (id != null) {
            VideoAd videoAd = (VideoAd) AdCache.load(context, id);
            if (videoAd == null || !videoAd.canShow(context, null, false)) {
                MMLog.w(TAG, String.format("mmvideo: Ad %s cannot be shown at this time.", new Object[]{id}));
            } else {
                listener.updateLastVideoViewedTime();
                MMLog.v(TAG, String.format("mmvideo: attempting to play video %s", new Object[]{id}));
                videoAd.show(context, listener.creatorAdImplInternalId);
                listener.startingVideo();
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    boolean canShow(android.content.Context r7_context, com.millennialmedia.android.MMAdImpl r8_adImpl, boolean r9_checkDeferredViewStart) {
        throw new UnsupportedOperationException("Method not decompiled: com.millennialmedia.android.VideoAd.canShow(android.content.Context, com.millennialmedia.android.MMAdImpl, boolean):boolean");
        /*
        r6 = this;
        r0 = 1;
        r1 = 0;
        if (r9 == 0) goto L_0x0021;
    L_0x0004:
        r2 = r6.isExpired();
        if (r2 != 0) goto L_0x001f;
    L_0x000a:
        r2 = r6.isOnDisk(r7);
        if (r2 == 0) goto L_0x001f;
    L_0x0010:
        r2 = com.millennialmedia.android.HandShake.sharedHandShake(r7);
        r3 = r8.adType;
        r4 = r6.deferredViewStart;
        r2 = r2.canDisplayCachedAd(r3, r4);
        if (r2 == 0) goto L_0x001f;
    L_0x001e:
        return r0;
    L_0x001f:
        r0 = r1;
        goto L_0x001e;
    L_0x0021:
        r2 = r6.isExpired();
        if (r2 != 0) goto L_0x002d;
    L_0x0027:
        r2 = r6.isOnDisk(r7);
        if (r2 != 0) goto L_0x001e;
    L_0x002d:
        r0 = r1;
        goto L_0x001e;
        */
    }

    void delete(Context context) {
        super.delete(context);
        handleSharedVideoFile(context);
        AdCache.cachedVideoWasRemoved(context, this.acid);
        MMLog.v(TAG, String.format("Ad %s was deleted.", new Object[]{getId()}));
    }

    public int describeContents() {
        return 0;
    }

    protected void deserializeFromObj(JSONObject videoObject) {
        int i;
        super.deserializeFromObj(videoObject);
        JSONArray jsonArray = videoObject.optJSONArray("startActivity");
        this.webOverlayURL = videoObject.optString("overlayURL", null);
        this.endOverlayURL = videoObject.optString("endURL", null);
        this.cacheMissURL = videoObject.optString("cacheMissURL", null);
        this.videoFileId = videoObject.optString("videoFileId", null);
        if (jsonArray != null) {
            this.startActivity = new String[jsonArray.length()];
            i = 0;
            while (i < jsonArray.length()) {
                this.startActivity[i] = jsonArray.optString(i);
                i++;
            }
        } else {
            this.startActivity = new String[0];
        }
        jsonArray = videoObject.optJSONArray("endActivity");
        if (jsonArray != null) {
            this.endActivity = new String[jsonArray.length()];
            i = 0;
            while (i < jsonArray.length()) {
                this.endActivity[i] = jsonArray.optString(i);
                i++;
            }
        } else {
            this.endActivity = new String[0];
        }
        jsonArray = videoObject.optJSONArray("cacheComplete");
        if (jsonArray != null) {
            this.cacheComplete = new String[jsonArray.length()];
            i = 0;
            while (i < jsonArray.length()) {
                this.cacheComplete[i] = jsonArray.optString(i);
                i++;
            }
        } else {
            this.cacheComplete = new String[0];
        }
        jsonArray = videoObject.optJSONArray("cacheFailed");
        if (jsonArray != null) {
            this.cacheFailed = new String[jsonArray.length()];
            i = 0;
            while (i < jsonArray.length()) {
                this.cacheFailed[i] = jsonArray.optString(i);
                i++;
            }
        } else {
            this.cacheFailed = new String[0];
        }
        jsonArray = videoObject.optJSONArray("videoError");
        if (jsonArray != null) {
            this.videoError = new String[jsonArray.length()];
            i = 0;
            while (i < jsonArray.length()) {
                this.videoError[i] = jsonArray.optString(i);
                i++;
            }
        } else {
            this.videoError = new String[0];
        }
        this.showControls = videoObject.optBoolean("showVideoPlayerControls");
        this.showCountdown = videoObject.optBoolean("showCountdownHUD");
        this.reloadNonEndOverlayOnRestart = videoObject.optBoolean("reloadOverlayOnRestart");
        JSONObject jsonObject = videoObject.optJSONObject("onCompletion");
        if (jsonObject != null) {
            this.onCompletionUrl = jsonObject.optString(PlusShare.KEY_CALL_TO_ACTION_URL, null);
            this.stayInPlayer = jsonObject.optBoolean("stayInPlayer");
        }
        this.duration = (long) (videoObject.optDouble("duration", 0.0d) * 1000.0d);
        this.contentLength = videoObject.optLong("contentLength");
        this.closeDelayMillis = videoObject.optLong("closeAfterDelay");
        jsonArray = videoObject.optJSONArray("buttons");
        if (jsonArray != null) {
            i = 0;
            while (i < jsonArray.length()) {
                jsonObject = jsonArray.optJSONObject(i);
                if (jsonObject != null) {
                    this.buttons.add(new VideoImage(jsonObject));
                }
                i++;
            }
        }
        jsonArray = videoObject.optJSONArray("log");
        if (jsonArray != null) {
            i = 0;
            while (i < jsonArray.length()) {
                jsonObject = jsonArray.optJSONObject(i);
                if (jsonObject != null) {
                    this.activities.add(new VideoLogEvent(jsonObject));
                }
                i++;
            }
        }
    }

    boolean download(Context context) {
        boolean success = AdCache.downloadComponentExternalStorage(this.contentUrl, this.videoFileId + VIDEO_FILE_ID, context);
        if (success) {
            int i = 0;
            while (i < this.buttons.size()) {
                VideoImage button = (VideoImage) this.buttons.get(i);
                success = AdCache.downloadComponentInternalCache(button.imageUrl, getId() + button.getImageName(), context);
                if (!success) {
                    break;
                }
                i++;
            }
        }
        if (!success) {
            if (this.downloadAllOrNothing) {
                delete(context);
            }
            HttpGetRequest.log(this.cacheFailed);
        } else if (success) {
            if (this.acid != null && this.acid.length() > 0) {
                AdCache.cachedVideoWasAdded(context, this.acid);
            }
            HttpGetRequest.log(this.cacheComplete);
        }
        MMLog.v(TAG, String.format("Caching completed successfully? %b", new Object[]{Boolean.valueOf(success)}));
        return success;
    }

    int getType() {
        return 1;
    }

    String getTypeString() {
        return "Video";
    }

    Intent getVideoExtrasIntent(Context context, long adImplInternalId) {
        Intent intent = new Intent();
        intent.putExtra("videoId", getId());
        if (adImplInternalId != -4) {
            intent.putExtra("internalId", adImplInternalId);
        }
        intent.setData(Uri.parse(AdCache.getExternalCacheDirectory(context).getAbsolutePath() + File.separator + this.videoFileId + VIDEO_FILE_ID));
        return intent;
    }

    boolean hasEndCard() {
        Iterator i$ = this.buttons.iterator();
        while (i$.hasNext()) {
            if (((VideoImage) i$.next()).isLeaveBehind) {
                return true;
            }
        }
        return false;
    }

    boolean isOnDisk(Context context) {
        boolean existsInFilesystem = false;
        int numExpectedButtons = this.buttons.size() + 1;
        File internalDir = AdCache.getInternalCacheDirectory(context);
        File externalDir = AdCache.getExternalCacheDirectory(context);
        if (internalDir == null || externalDir == null) {
            return false;
        }
        String[] listInternal = internalDir.list(new VideoFilenameFilter(this));
        if (listInternal != null && listInternal.length >= numExpectedButtons) {
            existsInFilesystem = true;
        }
        if (!existsInFilesystem || externalDir == null) {
            return existsInFilesystem;
        }
        if (!new File(externalDir, this.videoFileId + VIDEO_FILE_ID).exists()) {
            return false;
        }
        Iterator i$ = this.buttons.iterator();
        while (i$.hasNext()) {
            if (!new File(internalDir, getId() + ((VideoImage) i$.next()).getImageName()).exists()) {
                return false;
            }
        }
        return existsInFilesystem;
    }

    void logBeginEvent() {
        if (this.startActivity != null) {
            MMLog.d(TAG, "Cached video begin event logged");
            int i = 0;
            while (i < this.startActivity.length) {
                Event.logEvent(this.startActivity[i]);
                i++;
            }
        }
    }

    void logEndEvent() {
        if (this.endActivity != null) {
            MMLog.d(TAG, "Cached video end event logged");
            int i = 0;
            while (i < this.endActivity.length) {
                Event.logEvent(this.endActivity[i]);
                i++;
            }
        }
    }

    public void readExternal(ObjectInput input) throws IOException, ClassNotFoundException {
        super.readExternal(input);
        this.showControls = input.readBoolean();
        this.onCompletionUrl = (String) input.readObject();
        this.webOverlayURL = (String) input.readObject();
        this.endOverlayURL = (String) input.readObject();
        this.cacheMissURL = (String) input.readObject();
        this.videoFileId = (String) input.readObject();
        this.stayInPlayer = input.readBoolean();
        this.showCountdown = input.readBoolean();
        this.reloadNonEndOverlayOnRestart = input.readBoolean();
        int count = input.readInt();
        this.startActivity = new String[count];
        int i = 0;
        while (i < count) {
            this.startActivity[i] = (String) input.readObject();
            i++;
        }
        count = input.readInt();
        this.endActivity = new String[count];
        i = 0;
        while (i < count) {
            this.endActivity[i] = (String) input.readObject();
            i++;
        }
        this.duration = input.readLong();
        this.usingInternalStorage = input.readBoolean();
        this.contentLength = input.readLong();
        this.closeDelayMillis = input.readLong();
        count = input.readInt();
        this.cacheComplete = new String[count];
        i = 0;
        while (i < count) {
            this.cacheComplete[i] = (String) input.readObject();
            i++;
        }
        count = input.readInt();
        this.cacheFailed = new String[count];
        i = 0;
        while (i < count) {
            this.cacheFailed[i] = (String) input.readObject();
            i++;
        }
        count = input.readInt();
        this.videoError = new String[count];
        i = 0;
        while (i < count) {
            this.videoError[i] = (String) input.readObject();
            i++;
        }
        this.buttons.clear();
        count = input.readInt();
        i = 0;
        while (i < count) {
            this.buttons.add((VideoImage) input.readObject());
            i++;
        }
        this.activities.clear();
        count = input.readInt();
        i = 0;
        while (i < count) {
            this.activities.add((VideoLogEvent) input.readObject());
            i++;
        }
    }

    boolean saveAssets(Context context) {
        return true;
    }

    void setDtoCachedVideo(DTOCachedVideo dto) {
        this.cachedVideoDto = dto;
    }

    void show(Context context, long adImplInternalId) {
        IntentUtils.startCachedVideoPlayerActivity(context, getVideoExtrasIntent(context, adImplInternalId));
    }

    public void writeExternal(ObjectOutput output) throws IOException {
        super.writeExternal(output);
        output.writeBoolean(this.showControls);
        output.writeObject(this.onCompletionUrl);
        output.writeObject(this.webOverlayURL);
        output.writeObject(this.endOverlayURL);
        output.writeObject(this.cacheMissURL);
        output.writeObject(this.videoFileId);
        output.writeBoolean(this.stayInPlayer);
        output.writeBoolean(this.showCountdown);
        output.writeBoolean(this.reloadNonEndOverlayOnRestart);
        output.writeInt(this.startActivity.length);
        String[] arr$ = this.startActivity;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            output.writeObject(arr$[i$]);
            i$++;
        }
        output.writeInt(this.endActivity.length);
        arr$ = this.endActivity;
        len$ = arr$.length;
        i$ = 0;
        while (i$ < len$) {
            output.writeObject(arr$[i$]);
            i$++;
        }
        output.writeLong(this.duration);
        output.writeBoolean(this.usingInternalStorage);
        output.writeLong(this.contentLength);
        output.writeLong(this.closeDelayMillis);
        output.writeInt(this.cacheComplete.length);
        arr$ = this.cacheComplete;
        len$ = arr$.length;
        i$ = 0;
        while (i$ < len$) {
            output.writeObject(arr$[i$]);
            i$++;
        }
        output.writeInt(this.cacheFailed.length);
        arr$ = this.cacheFailed;
        len$ = arr$.length;
        i$ = 0;
        while (i$ < len$) {
            output.writeObject(arr$[i$]);
            i$++;
        }
        output.writeInt(this.videoError.length);
        arr$ = this.videoError;
        len$ = arr$.length;
        i$ = 0;
        while (i$ < len$) {
            output.writeObject(arr$[i$]);
            i$++;
        }
        output.writeInt(this.buttons.size());
        Iterator i$2 = this.buttons.iterator();
        while (i$2.hasNext()) {
            output.writeObject((VideoImage) i$2.next());
        }
        output.writeInt(this.activities.size());
        i$2 = this.activities.iterator();
        while (i$2.hasNext()) {
            output.writeObject((VideoLogEvent) i$2.next());
        }
    }

    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.startActivity.length);
        dest.writeStringArray(this.startActivity);
        dest.writeInt(this.endActivity.length);
        dest.writeStringArray(this.endActivity);
        dest.writeBooleanArray(new boolean[]{this.showControls, this.stayInPlayer, this.showCountdown, this.reloadNonEndOverlayOnRestart, this.usingInternalStorage});
        dest.writeString(this.onCompletionUrl);
        dest.writeString(this.endOverlayURL);
        dest.writeString(this.webOverlayURL);
        dest.writeString(this.cacheMissURL);
        dest.writeString(this.videoFileId);
        dest.writeLong(this.duration);
        dest.writeLong(this.contentLength);
        dest.writeLong(this.closeDelayMillis);
        dest.writeList(this.buttons);
        dest.writeList(this.activities);
        dest.writeInt(this.cacheComplete.length);
        dest.writeStringArray(this.cacheComplete);
        dest.writeInt(this.cacheFailed.length);
        dest.writeStringArray(this.cacheFailed);
        dest.writeInt(this.videoError.length);
        dest.writeStringArray(this.videoError);
    }
}