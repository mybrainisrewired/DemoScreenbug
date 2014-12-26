package com.google.android.gms.cast;

import com.google.android.gms.cast.Cast.MessageReceivedCallback;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.internal.en;
import com.google.android.gms.internal.es;
import com.google.android.gms.internal.et;
import com.google.android.gms.internal.eu;
import java.io.IOException;
import org.json.JSONObject;

public class RemoteMediaPlayer implements MessageReceivedCallback {
    public static final int RESUME_STATE_PAUSE = 2;
    public static final int RESUME_STATE_PLAY = 1;
    public static final int RESUME_STATE_UNCHANGED = 0;
    public static final int STATUS_CANCELED = 2;
    public static final int STATUS_FAILED = 1;
    public static final int STATUS_REPLACED = 4;
    public static final int STATUS_SUCCEEDED = 0;
    public static final int STATUS_TIMED_OUT = 3;
    private final Object li;
    private final es yE;
    private final a yF;
    private OnMetadataUpdatedListener yG;
    private OnStatusUpdatedListener yH;

    public static interface OnMetadataUpdatedListener {
        void onMetadataUpdated();
    }

    public static interface OnStatusUpdatedListener {
        void onStatusUpdated();
    }

    public static interface MediaChannelResult extends Result {
    }

    private class a implements et {
        private GoogleApiClient yS;
        private long yT;

        private final class a implements ResultCallback<Status> {
            private final long yU;

            a(long j) {
                this.yU = j;
            }

            public void i(Status status) {
                if (!status.isSuccess()) {
                    a.this.yI.yE.a(this.yU, status.getStatusCode());
                }
            }

            public /* synthetic */ void onResult(Result x0) {
                i((Status) x0);
            }
        }

        public a() {
            this.yT = 0;
        }

        public void a(String str, String str2, long j, String str3) throws IOException {
            if (this.yS == null) {
                throw new IOException("No GoogleApiClient available");
            }
            Cast.CastApi.sendMessage(this.yS, str, str2).setResultCallback(new a(j));
        }

        public void b(GoogleApiClient googleApiClient) {
            this.yS = googleApiClient;
        }

        public long dD() {
            long j = this.yT + 1;
            this.yT = j;
            return j;
        }
    }

    private static final class c implements com.google.android.gms.cast.RemoteMediaPlayer.MediaChannelResult {
        private final Status wJ;
        private final JSONObject yn;

        c(Status status, JSONObject jSONObject) {
            this.wJ = status;
            this.yn = jSONObject;
        }

        public Status getStatus() {
            return this.wJ;
        }
    }

    private static abstract class b extends a<com.google.android.gms.cast.RemoteMediaPlayer.MediaChannelResult> {
        eu yW;

        class AnonymousClass_2 implements com.google.android.gms.cast.RemoteMediaPlayer.MediaChannelResult {
            final /* synthetic */ Status wz;

            AnonymousClass_2(Status status) {
                this.wz = status;
            }

            public Status getStatus() {
                return this.wz;
            }
        }

        b() {
            this.yW = new eu() {
                public void a(long j, int i, JSONObject jSONObject) {
                    b.this.a(new c(new Status(i), jSONObject));
                }

                public void l(long j) {
                    b.this.a(b.this.j(new Status(4)));
                }
            };
        }

        public /* synthetic */ Result d(Status status) {
            return j(status);
        }

        public com.google.android.gms.cast.RemoteMediaPlayer.MediaChannelResult j(Status status) {
            return new AnonymousClass_2(status);
        }
    }

    class AnonymousClass_2 extends b {
        final /* synthetic */ GoogleApiClient yJ;
        final /* synthetic */ MediaInfo yK;
        final /* synthetic */ boolean yL;
        final /* synthetic */ long yM;
        final /* synthetic */ JSONObject yN;

        AnonymousClass_2(GoogleApiClient googleApiClient, MediaInfo mediaInfo, boolean z, long j, JSONObject jSONObject) {
            this.yJ = googleApiClient;
            this.yK = mediaInfo;
            this.yL = z;
            this.yM = j;
            this.yN = jSONObject;
        }

        protected void a(en enVar) {
            synchronized (RemoteMediaPlayer.this.li) {
                try {
                    RemoteMediaPlayer.this.yF.b(this.yJ);
                    try {
                        RemoteMediaPlayer.this.yE.a(this.yW, this.yK, this.yL, this.yM, this.yN);
                        RemoteMediaPlayer.this.yF.b(null);
                    } catch (IOException e) {
                        a(j(new Status(1)));
                        RemoteMediaPlayer.this.yF.b(null);
                    }
                } catch (Throwable th) {
                }
            }
        }
    }

    class AnonymousClass_3 extends b {
        final /* synthetic */ GoogleApiClient yJ;
        final /* synthetic */ JSONObject yN;

        AnonymousClass_3(GoogleApiClient googleApiClient, JSONObject jSONObject) {
            this.yJ = googleApiClient;
            this.yN = jSONObject;
        }

        protected void a(en enVar) {
            synchronized (RemoteMediaPlayer.this.li) {
                try {
                    RemoteMediaPlayer.this.yF.b(this.yJ);
                    try {
                        RemoteMediaPlayer.this.yE.a(this.yW, this.yN);
                        RemoteMediaPlayer.this.yF.b(null);
                    } catch (IOException e) {
                        a(j(new Status(1)));
                        RemoteMediaPlayer.this.yF.b(null);
                    }
                } catch (Throwable th) {
                }
            }
        }
    }

    class AnonymousClass_4 extends b {
        final /* synthetic */ GoogleApiClient yJ;
        final /* synthetic */ JSONObject yN;

        AnonymousClass_4(GoogleApiClient googleApiClient, JSONObject jSONObject) {
            this.yJ = googleApiClient;
            this.yN = jSONObject;
        }

        protected void a(en enVar) {
            synchronized (RemoteMediaPlayer.this.li) {
                try {
                    RemoteMediaPlayer.this.yF.b(this.yJ);
                    try {
                        RemoteMediaPlayer.this.yE.b(this.yW, this.yN);
                        RemoteMediaPlayer.this.yF.b(null);
                    } catch (IOException e) {
                        a(j(new Status(1)));
                        RemoteMediaPlayer.this.yF.b(null);
                    }
                } catch (Throwable th) {
                }
            }
        }
    }

    class AnonymousClass_5 extends b {
        final /* synthetic */ GoogleApiClient yJ;
        final /* synthetic */ JSONObject yN;

        AnonymousClass_5(GoogleApiClient googleApiClient, JSONObject jSONObject) {
            this.yJ = googleApiClient;
            this.yN = jSONObject;
        }

        protected void a(en enVar) {
            synchronized (RemoteMediaPlayer.this.li) {
                try {
                    RemoteMediaPlayer.this.yF.b(this.yJ);
                    try {
                        RemoteMediaPlayer.this.yE.c(this.yW, this.yN);
                        RemoteMediaPlayer.this.yF.b(null);
                    } catch (IOException e) {
                        a(j(new Status(1)));
                        RemoteMediaPlayer.this.yF.b(null);
                    }
                } catch (Throwable th) {
                }
            }
        }
    }

    class AnonymousClass_6 extends b {
        final /* synthetic */ GoogleApiClient yJ;
        final /* synthetic */ JSONObject yN;
        final /* synthetic */ long yO;
        final /* synthetic */ int yP;

        AnonymousClass_6(GoogleApiClient googleApiClient, long j, int i, JSONObject jSONObject) {
            this.yJ = googleApiClient;
            this.yO = j;
            this.yP = i;
            this.yN = jSONObject;
        }

        protected void a(en enVar) {
            synchronized (RemoteMediaPlayer.this.li) {
                try {
                    RemoteMediaPlayer.this.yF.b(this.yJ);
                    try {
                        RemoteMediaPlayer.this.yE.a(this.yW, this.yO, this.yP, this.yN);
                        RemoteMediaPlayer.this.yF.b(null);
                    } catch (IOException e) {
                        a(j(new Status(1)));
                        RemoteMediaPlayer.this.yF.b(null);
                    }
                } catch (Throwable th) {
                }
            }
        }
    }

    class AnonymousClass_7 extends b {
        final /* synthetic */ GoogleApiClient yJ;
        final /* synthetic */ JSONObject yN;
        final /* synthetic */ double yQ;

        AnonymousClass_7(GoogleApiClient googleApiClient, double d, JSONObject jSONObject) {
            this.yJ = googleApiClient;
            this.yQ = d;
            this.yN = jSONObject;
        }

        protected void a(en enVar) {
            synchronized (RemoteMediaPlayer.this.li) {
                try {
                    RemoteMediaPlayer.this.yF.b(this.yJ);
                    try {
                        RemoteMediaPlayer.this.yE.a(this.yW, this.yQ, this.yN);
                        RemoteMediaPlayer.this.yF.b(null);
                    } catch (IllegalStateException e) {
                        a(j(new Status(1)));
                        RemoteMediaPlayer.this.yF.b(null);
                    } catch (IllegalArgumentException e2) {
                        a(j(new Status(1)));
                        RemoteMediaPlayer.this.yF.b(null);
                    } catch (IOException e3) {
                        a(j(new Status(1)));
                        RemoteMediaPlayer.this.yF.b(null);
                    }
                } catch (Throwable th) {
                }
            }
        }
    }

    class AnonymousClass_8 extends b {
        final /* synthetic */ GoogleApiClient yJ;
        final /* synthetic */ JSONObject yN;
        final /* synthetic */ boolean yR;

        AnonymousClass_8(GoogleApiClient googleApiClient, boolean z, JSONObject jSONObject) {
            this.yJ = googleApiClient;
            this.yR = z;
            this.yN = jSONObject;
        }

        protected void a(en enVar) {
            synchronized (RemoteMediaPlayer.this.li) {
                try {
                    RemoteMediaPlayer.this.yF.b(this.yJ);
                    try {
                        RemoteMediaPlayer.this.yE.a(this.yW, this.yR, this.yN);
                        RemoteMediaPlayer.this.yF.b(null);
                    } catch (IllegalStateException e) {
                        a(j(new Status(1)));
                        RemoteMediaPlayer.this.yF.b(null);
                    } catch (IOException e2) {
                        a(j(new Status(1)));
                        RemoteMediaPlayer.this.yF.b(null);
                    }
                } catch (Throwable th) {
                }
            }
        }
    }

    class AnonymousClass_9 extends b {
        final /* synthetic */ GoogleApiClient yJ;

        AnonymousClass_9(GoogleApiClient googleApiClient) {
            this.yJ = googleApiClient;
        }

        protected void a(en enVar) {
            synchronized (RemoteMediaPlayer.this.li) {
                try {
                    RemoteMediaPlayer.this.yF.b(this.yJ);
                    try {
                        RemoteMediaPlayer.this.yE.a(this.yW);
                        RemoteMediaPlayer.this.yF.b(null);
                    } catch (IOException e) {
                        a(j(new Status(1)));
                        RemoteMediaPlayer.this.yF.b(null);
                    }
                } catch (Throwable th) {
                }
            }
        }
    }

    public RemoteMediaPlayer() {
        this.li = new Object();
        this.yF = new a();
        this.yE = new es() {
            protected void onMetadataUpdated() {
                RemoteMediaPlayer.this.onMetadataUpdated();
            }

            protected void onStatusUpdated() {
                RemoteMediaPlayer.this.onStatusUpdated();
            }
        };
        this.yE.a(this.yF);
    }

    private void onMetadataUpdated() {
        if (this.yG != null) {
            this.yG.onMetadataUpdated();
        }
    }

    private void onStatusUpdated() {
        if (this.yH != null) {
            this.yH.onStatusUpdated();
        }
    }

    public long getApproximateStreamPosition() {
        long approximateStreamPosition;
        synchronized (this.li) {
            approximateStreamPosition = this.yE.getApproximateStreamPosition();
        }
        return approximateStreamPosition;
    }

    public MediaInfo getMediaInfo() {
        MediaInfo mediaInfo;
        synchronized (this.li) {
            mediaInfo = this.yE.getMediaInfo();
        }
        return mediaInfo;
    }

    public MediaStatus getMediaStatus() {
        MediaStatus mediaStatus;
        synchronized (this.li) {
            mediaStatus = this.yE.getMediaStatus();
        }
        return mediaStatus;
    }

    public String getNamespace() {
        return this.yE.getNamespace();
    }

    public long getStreamDuration() {
        long streamDuration;
        synchronized (this.li) {
            streamDuration = this.yE.getStreamDuration();
        }
        return streamDuration;
    }

    public PendingResult<MediaChannelResult> load(GoogleApiClient apiClient, MediaInfo mediaInfo) {
        return load(apiClient, mediaInfo, true, 0, null);
    }

    public PendingResult<MediaChannelResult> load(GoogleApiClient apiClient, MediaInfo mediaInfo, boolean autoplay) {
        return load(apiClient, mediaInfo, autoplay, 0, null);
    }

    public PendingResult<MediaChannelResult> load(GoogleApiClient apiClient, MediaInfo mediaInfo, boolean autoplay, long playPosition) {
        return load(apiClient, mediaInfo, autoplay, playPosition, null);
    }

    public PendingResult<MediaChannelResult> load(GoogleApiClient apiClient, MediaInfo mediaInfo, boolean autoplay, long playPosition, JSONObject customData) {
        return apiClient.b(new AnonymousClass_2(apiClient, mediaInfo, autoplay, playPosition, customData));
    }

    public void onMessageReceived(CastDevice castDevice, String namespace, String message) {
        this.yE.U(message);
    }

    public PendingResult<MediaChannelResult> pause(GoogleApiClient apiClient) {
        return pause(apiClient, null);
    }

    public PendingResult<MediaChannelResult> pause(GoogleApiClient apiClient, JSONObject customData) {
        return apiClient.b(new AnonymousClass_3(apiClient, customData));
    }

    public PendingResult<MediaChannelResult> play(GoogleApiClient apiClient) {
        return play(apiClient, null);
    }

    public PendingResult<MediaChannelResult> play(GoogleApiClient apiClient, JSONObject customData) {
        return apiClient.b(new AnonymousClass_5(apiClient, customData));
    }

    public PendingResult<MediaChannelResult> requestStatus(GoogleApiClient apiClient) {
        return apiClient.b(new AnonymousClass_9(apiClient));
    }

    public PendingResult<MediaChannelResult> seek(GoogleApiClient apiClient, long position) {
        return seek(apiClient, position, STATUS_SUCCEEDED, null);
    }

    public PendingResult<MediaChannelResult> seek(GoogleApiClient apiClient, long position, int resumeState) {
        return seek(apiClient, position, resumeState, null);
    }

    public PendingResult<MediaChannelResult> seek(GoogleApiClient apiClient, long position, int resumeState, JSONObject customData) {
        return apiClient.b(new AnonymousClass_6(apiClient, position, resumeState, customData));
    }

    public void setOnMetadataUpdatedListener(OnMetadataUpdatedListener listener) {
        this.yG = listener;
    }

    public void setOnStatusUpdatedListener(OnStatusUpdatedListener listener) {
        this.yH = listener;
    }

    public PendingResult<MediaChannelResult> setStreamMute(GoogleApiClient apiClient, boolean muteState) {
        return setStreamMute(apiClient, muteState, null);
    }

    public PendingResult<MediaChannelResult> setStreamMute(GoogleApiClient apiClient, boolean muteState, JSONObject customData) {
        return apiClient.b(new AnonymousClass_8(apiClient, muteState, customData));
    }

    public PendingResult<MediaChannelResult> setStreamVolume(GoogleApiClient apiClient, double volume) throws IllegalArgumentException {
        return setStreamVolume(apiClient, volume, null);
    }

    public PendingResult<MediaChannelResult> setStreamVolume(GoogleApiClient apiClient, double volume, JSONObject customData) throws IllegalArgumentException {
        if (!Double.isInfinite(volume) && !Double.isNaN(volume)) {
            return apiClient.b(new AnonymousClass_7(apiClient, volume, customData));
        }
        throw new IllegalArgumentException("Volume cannot be " + volume);
    }

    public PendingResult<MediaChannelResult> stop(GoogleApiClient apiClient) {
        return stop(apiClient, null);
    }

    public PendingResult<MediaChannelResult> stop(GoogleApiClient apiClient, JSONObject customData) {
        return apiClient.b(new AnonymousClass_4(apiClient, customData));
    }
}