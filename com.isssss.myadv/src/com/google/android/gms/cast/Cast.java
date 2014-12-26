package com.google.android.gms.cast;

import android.content.Context;
import android.os.Looper;
import android.os.RemoteException;
import android.text.TextUtils;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.ApiOptions.HasOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.internal.en;
import com.google.android.gms.internal.fc;
import com.google.android.gms.internal.fq;
import com.mopub.common.Preconditions;
import com.mopub.nativeads.MoPubNativeAdPositioning.MoPubClientPositioning;
import java.io.IOException;

public final class Cast {
    public static final Api<CastOptions> API;
    public static final CastApi CastApi;
    public static final String EXTRA_APP_NO_LONGER_RUNNING = "com.google.android.gms.cast.EXTRA_APP_NO_LONGER_RUNNING";
    public static final int MAX_MESSAGE_LENGTH = 65536;
    public static final int MAX_NAMESPACE_LENGTH = 128;
    static final com.google.android.gms.common.api.Api.c<en> wx;
    private static final com.google.android.gms.common.api.Api.b<en, CastOptions> wy;

    public static interface CastApi {

        public static final class a implements com.google.android.gms.cast.Cast.CastApi {

            class AnonymousClass_1 extends b {
                final /* synthetic */ String xN;
                final /* synthetic */ String xO;

                AnonymousClass_1(String str, String str2) {
                    this.xN = str;
                    this.xO = str2;
                    super();
                }

                protected void a(en enVar) throws RemoteException {
                    int i = GamesStatusCodes.STATUS_REQUEST_UPDATE_TOTAL_FAILURE;
                    try {
                        enVar.a(this.xN, this.xO, this);
                    } catch (IllegalArgumentException e) {
                        x(i);
                    } catch (IllegalStateException e2) {
                        x(i);
                    }
                }
            }

            class AnonymousClass_2 extends c {
                final /* synthetic */ String xQ;

                AnonymousClass_2(String str) {
                    this.xQ = str;
                    super();
                }

                protected void a(en enVar) throws RemoteException {
                    try {
                        enVar.a(this.xQ, false, this);
                    } catch (IllegalStateException e) {
                        x(GamesStatusCodes.STATUS_REQUEST_UPDATE_TOTAL_FAILURE);
                    }
                }
            }

            class AnonymousClass_3 extends c {
                final /* synthetic */ String xQ;
                final /* synthetic */ boolean xR;

                AnonymousClass_3(String str, boolean z) {
                    this.xQ = str;
                    this.xR = z;
                    super();
                }

                protected void a(en enVar) throws RemoteException {
                    try {
                        enVar.a(this.xQ, this.xR, this);
                    } catch (IllegalStateException e) {
                        x(GamesStatusCodes.STATUS_REQUEST_UPDATE_TOTAL_FAILURE);
                    }
                }
            }

            class AnonymousClass_4 extends c {
                final /* synthetic */ String xQ;
                final /* synthetic */ String xS;

                AnonymousClass_4(String str, String str2) {
                    this.xQ = str;
                    this.xS = str2;
                    super();
                }

                protected void a(en enVar) throws RemoteException {
                    try {
                        enVar.b(this.xQ, this.xS, this);
                    } catch (IllegalStateException e) {
                        x(GamesStatusCodes.STATUS_REQUEST_UPDATE_TOTAL_FAILURE);
                    }
                }
            }

            class AnonymousClass_5 extends c {
                final /* synthetic */ String xQ;

                AnonymousClass_5(String str) {
                    this.xQ = str;
                    super();
                }

                protected void a(en enVar) throws RemoteException {
                    try {
                        enVar.b(this.xQ, null, this);
                    } catch (IllegalStateException e) {
                        x(GamesStatusCodes.STATUS_REQUEST_UPDATE_TOTAL_FAILURE);
                    }
                }
            }

            class AnonymousClass_9 extends b {
                final /* synthetic */ String xS;

                AnonymousClass_9(String str) {
                    this.xS = str;
                    super();
                }

                protected void a(en enVar) throws RemoteException {
                    if (TextUtils.isEmpty(this.xS)) {
                        c(GamesStatusCodes.STATUS_REQUEST_UPDATE_TOTAL_FAILURE, "IllegalArgument: sessionId cannot be null or empty");
                    } else {
                        try {
                            enVar.a(this.xS, this);
                        } catch (IllegalStateException e) {
                            x(GamesStatusCodes.STATUS_REQUEST_UPDATE_TOTAL_FAILURE);
                        }
                    }
                }
            }

            public ApplicationMetadata getApplicationMetadata(GoogleApiClient client) throws IllegalStateException {
                return ((en) client.a(wx)).getApplicationMetadata();
            }

            public String getApplicationStatus(GoogleApiClient client) throws IllegalStateException {
                return ((en) client.a(wx)).getApplicationStatus();
            }

            public double getVolume(GoogleApiClient client) throws IllegalStateException {
                return ((en) client.a(wx)).dI();
            }

            public boolean isMute(GoogleApiClient client) throws IllegalStateException {
                return ((en) client.a(wx)).isMute();
            }

            public PendingResult<com.google.android.gms.cast.Cast.ApplicationConnectionResult> joinApplication(GoogleApiClient client) {
                return client.b(new c() {
                    {
                        super();
                    }

                    protected void a(en enVar) throws RemoteException {
                        try {
                            enVar.b(null, null, this);
                        } catch (IllegalStateException e) {
                            x(GamesStatusCodes.STATUS_REQUEST_UPDATE_TOTAL_FAILURE);
                        }
                    }
                });
            }

            public PendingResult<com.google.android.gms.cast.Cast.ApplicationConnectionResult> joinApplication(GoogleApiClient client, String applicationId) {
                return client.b(new AnonymousClass_5(applicationId));
            }

            public PendingResult<com.google.android.gms.cast.Cast.ApplicationConnectionResult> joinApplication(GoogleApiClient client, String applicationId, String sessionId) {
                return client.b(new AnonymousClass_4(applicationId, sessionId));
            }

            public PendingResult<com.google.android.gms.cast.Cast.ApplicationConnectionResult> launchApplication(GoogleApiClient client, String applicationId) {
                return client.b(new AnonymousClass_2(applicationId));
            }

            public PendingResult<com.google.android.gms.cast.Cast.ApplicationConnectionResult> launchApplication(GoogleApiClient client, String applicationId, boolean relaunchIfRunning) {
                return client.b(new AnonymousClass_3(applicationId, relaunchIfRunning));
            }

            public PendingResult<Status> leaveApplication(GoogleApiClient client) {
                return client.b(new b() {
                    {
                        super();
                    }

                    protected void a(en enVar) throws RemoteException {
                        try {
                            enVar.e(this);
                        } catch (IllegalStateException e) {
                            x(GamesStatusCodes.STATUS_REQUEST_UPDATE_TOTAL_FAILURE);
                        }
                    }
                });
            }

            public void removeMessageReceivedCallbacks(GoogleApiClient client, String namespace) throws IOException, IllegalArgumentException {
                try {
                    ((en) client.a(wx)).V(namespace);
                } catch (RemoteException e) {
                    throw new IOException("service error");
                }
            }

            public void requestStatus(GoogleApiClient client) throws IOException, IllegalStateException {
                try {
                    ((en) client.a(wx)).dH();
                } catch (RemoteException e) {
                    throw new IOException("service error");
                }
            }

            public PendingResult<Status> sendMessage(GoogleApiClient client, String namespace, String message) {
                return client.b(new AnonymousClass_1(namespace, message));
            }

            public void setMessageReceivedCallbacks(GoogleApiClient client, String namespace, com.google.android.gms.cast.Cast.MessageReceivedCallback callbacks) throws IOException, IllegalStateException {
                try {
                    ((en) client.a(wx)).a(namespace, callbacks);
                } catch (RemoteException e) {
                    throw new IOException("service error");
                }
            }

            public void setMute(GoogleApiClient client, boolean mute) throws IOException, IllegalStateException {
                try {
                    ((en) client.a(wx)).v(mute);
                } catch (RemoteException e) {
                    throw new IOException("service error");
                }
            }

            public void setVolume(GoogleApiClient client, double volume) throws IOException, IllegalArgumentException, IllegalStateException {
                try {
                    ((en) client.a(wx)).a(volume);
                } catch (RemoteException e) {
                    throw new IOException("service error");
                }
            }

            public PendingResult<Status> stopApplication(GoogleApiClient client) {
                return client.b(new b() {
                    {
                        super();
                    }

                    protected void a(en enVar) throws RemoteException {
                        try {
                            enVar.a(Preconditions.EMPTY_ARGUMENTS, this);
                        } catch (IllegalStateException e) {
                            x(GamesStatusCodes.STATUS_REQUEST_UPDATE_TOTAL_FAILURE);
                        }
                    }
                });
            }

            public PendingResult<Status> stopApplication(GoogleApiClient client, String sessionId) {
                return client.b(new AnonymousClass_9(sessionId));
            }
        }

        ApplicationMetadata getApplicationMetadata(GoogleApiClient googleApiClient) throws IllegalStateException;

        String getApplicationStatus(GoogleApiClient googleApiClient) throws IllegalStateException;

        double getVolume(GoogleApiClient googleApiClient) throws IllegalStateException;

        boolean isMute(GoogleApiClient googleApiClient) throws IllegalStateException;

        PendingResult<com.google.android.gms.cast.Cast.ApplicationConnectionResult> joinApplication(GoogleApiClient googleApiClient);

        PendingResult<com.google.android.gms.cast.Cast.ApplicationConnectionResult> joinApplication(GoogleApiClient googleApiClient, String str);

        PendingResult<com.google.android.gms.cast.Cast.ApplicationConnectionResult> joinApplication(GoogleApiClient googleApiClient, String str, String str2);

        PendingResult<com.google.android.gms.cast.Cast.ApplicationConnectionResult> launchApplication(GoogleApiClient googleApiClient, String str);

        PendingResult<com.google.android.gms.cast.Cast.ApplicationConnectionResult> launchApplication(GoogleApiClient googleApiClient, String str, boolean z);

        PendingResult<Status> leaveApplication(GoogleApiClient googleApiClient);

        void removeMessageReceivedCallbacks(GoogleApiClient googleApiClient, String str) throws IOException, IllegalArgumentException;

        void requestStatus(GoogleApiClient googleApiClient) throws IOException, IllegalStateException;

        PendingResult<Status> sendMessage(GoogleApiClient googleApiClient, String str, String str2);

        void setMessageReceivedCallbacks(GoogleApiClient googleApiClient, String str, com.google.android.gms.cast.Cast.MessageReceivedCallback messageReceivedCallback) throws IOException, IllegalStateException;

        void setMute(GoogleApiClient googleApiClient, boolean z) throws IOException, IllegalStateException;

        void setVolume(GoogleApiClient googleApiClient, double d) throws IOException, IllegalArgumentException, IllegalStateException;

        PendingResult<Status> stopApplication(GoogleApiClient googleApiClient);

        PendingResult<Status> stopApplication(GoogleApiClient googleApiClient, String str);
    }

    public static abstract class Listener {
        public void onApplicationDisconnected(int statusCode) {
        }

        public void onApplicationStatusChanged() {
        }

        public void onVolumeChanged() {
        }
    }

    public static interface MessageReceivedCallback {
        void onMessageReceived(CastDevice castDevice, String str, String str2);
    }

    public static interface ApplicationConnectionResult extends Result {
        ApplicationMetadata getApplicationMetadata();

        String getApplicationStatus();

        String getSessionId();

        boolean getWasLaunched();
    }

    public static final class CastOptions implements HasOptions {
        final CastDevice xT;
        final com.google.android.gms.cast.Cast.Listener xU;
        private final int xV;

        public static final class Builder {
            CastDevice xW;
            com.google.android.gms.cast.Cast.Listener xX;
            private int xY;

            private Builder(Object castDevice, Object castListener) {
                fq.b(castDevice, (Object)"CastDevice parameter cannot be null");
                fq.b(castListener, (Object)"CastListener parameter cannot be null");
                this.xW = castDevice;
                this.xX = castListener;
                this.xY = 0;
            }

            public com.google.android.gms.cast.Cast.CastOptions build() {
                return new com.google.android.gms.cast.Cast.CastOptions(null);
            }

            public com.google.android.gms.cast.Cast.CastOptions.Builder setVerboseLoggingEnabled(boolean enabled) {
                if (enabled) {
                    this.xY |= 1;
                } else {
                    this.xY &= -2;
                }
                return this;
            }
        }

        private CastOptions(Builder builder) {
            this.xT = builder.xW;
            this.xU = builder.xX;
            this.xV = builder.xY;
        }

        public static Builder builder(CastDevice castDevice, com.google.android.gms.cast.Cast.Listener castListener) {
            return new Builder(castListener, null);
        }
    }

    protected static abstract class a<R extends Result> extends com.google.android.gms.common.api.a.b<R, en> {
        public a() {
            super(wx);
        }

        public void c(int i, String str) {
            a(d(new Status(i, str, null)));
        }

        public void x(int i) {
            a(d(new Status(i)));
        }
    }

    private static abstract class b extends a<Status> {
        private b() {
        }

        public /* synthetic */ Result d(Status status) {
            return f(status);
        }

        public Status f(Status status) {
            return status;
        }
    }

    private static abstract class c extends a<com.google.android.gms.cast.Cast.ApplicationConnectionResult> {

        class AnonymousClass_1 implements com.google.android.gms.cast.Cast.ApplicationConnectionResult {
            final /* synthetic */ Status wz;

            AnonymousClass_1(Status status) {
                this.wz = status;
            }

            public ApplicationMetadata getApplicationMetadata() {
                return null;
            }

            public String getApplicationStatus() {
                return null;
            }

            public String getSessionId() {
                return null;
            }

            public Status getStatus() {
                return this.wz;
            }

            public boolean getWasLaunched() {
                return false;
            }
        }

        private c() {
        }

        public /* synthetic */ Result d(Status status) {
            return h(status);
        }

        public com.google.android.gms.cast.Cast.ApplicationConnectionResult h(Status status) {
            return new AnonymousClass_1(status);
        }
    }

    static {
        wx = new com.google.android.gms.common.api.Api.c();
        wy = new com.google.android.gms.common.api.Api.b<en, CastOptions>() {
            public en a(Context context, Looper looper, fc fcVar, Object obj, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
                fq.b(obj, (Object)"Setting the API options is required.");
                return new en(context, looper, obj.xT, (long) obj.xV, obj.xU, connectionCallbacks, onConnectionFailedListener);
            }

            public int getPriority() {
                return MoPubClientPositioning.NO_REPEAT;
            }
        };
        API = new Api(wy, wx, new Scope[0]);
        CastApi = new a();
    }

    private Cast() {
    }
}