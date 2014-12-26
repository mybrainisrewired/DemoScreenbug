package com.google.android.gms.games.internal.api;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.a.d;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Games.BaseGamesApiMethodImpl;
import com.google.android.gms.games.internal.GamesClientImpl;
import com.google.android.gms.games.request.GameRequest;
import com.google.android.gms.games.request.GameRequestBuffer;
import com.google.android.gms.games.request.OnRequestReceivedListener;
import com.google.android.gms.games.request.Requests;
import com.google.android.gms.games.request.Requests.LoadRequestSummariesResult;
import com.google.android.gms.games.request.Requests.LoadRequestsResult;
import com.google.android.gms.games.request.Requests.SendRequestResult;
import com.google.android.gms.games.request.Requests.UpdateRequestsResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class RequestsImpl implements Requests {

    private static abstract class LoadRequestSummariesImpl extends BaseGamesApiMethodImpl<LoadRequestSummariesResult> {

        class AnonymousClass_1 implements LoadRequestSummariesResult {
            final /* synthetic */ Status wz;

            AnonymousClass_1(Status status) {
                this.wz = status;
            }

            public Status getStatus() {
                return this.wz;
            }

            public void release() {
            }
        }

        private LoadRequestSummariesImpl() {
        }

        public LoadRequestSummariesResult N(Status status) {
            return new AnonymousClass_1(status);
        }

        public /* synthetic */ Result d(Status status) {
            return N(status);
        }
    }

    private static abstract class LoadRequestsImpl extends BaseGamesApiMethodImpl<LoadRequestsResult> {

        class AnonymousClass_1 implements LoadRequestsResult {
            final /* synthetic */ Status wz;

            AnonymousClass_1(Status status) {
                this.wz = status;
            }

            public GameRequestBuffer getRequests(int type) {
                return null;
            }

            public Status getStatus() {
                return this.wz;
            }

            public void release() {
            }
        }

        private LoadRequestsImpl() {
        }

        public LoadRequestsResult O(Status status) {
            return new AnonymousClass_1(status);
        }

        public /* synthetic */ Result d(Status status) {
            return O(status);
        }
    }

    private static abstract class SendRequestImpl extends BaseGamesApiMethodImpl<SendRequestResult> {

        class AnonymousClass_1 implements SendRequestResult {
            final /* synthetic */ Status wz;

            AnonymousClass_1(Status status) {
                this.wz = status;
            }

            public Status getStatus() {
                return this.wz;
            }
        }

        private SendRequestImpl() {
        }

        public SendRequestResult P(Status status) {
            return new AnonymousClass_1(status);
        }

        public /* synthetic */ Result d(Status status) {
            return P(status);
        }
    }

    private static abstract class UpdateRequestsImpl extends BaseGamesApiMethodImpl<UpdateRequestsResult> {

        class AnonymousClass_1 implements UpdateRequestsResult {
            final /* synthetic */ Status wz;

            AnonymousClass_1(Status status) {
                this.wz = status;
            }

            public Set<String> getRequestIds() {
                return null;
            }

            public int getRequestOutcome(String requestId) {
                throw new IllegalArgumentException("Unknown request ID " + requestId);
            }

            public Status getStatus() {
                return this.wz;
            }

            public void release() {
            }
        }

        private UpdateRequestsImpl() {
        }

        public UpdateRequestsResult Q(Status status) {
            return new AnonymousClass_1(status);
        }

        public /* synthetic */ Result d(Status status) {
            return Q(status);
        }
    }

    class AnonymousClass_1 extends UpdateRequestsImpl {
        final /* synthetic */ String[] KO;

        AnonymousClass_1(String[] strArr) {
            this.KO = strArr;
            super();
        }

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.b(this, this.KO);
        }
    }

    class AnonymousClass_2 extends UpdateRequestsImpl {
        final /* synthetic */ String[] KO;

        AnonymousClass_2(String[] strArr) {
            this.KO = strArr;
            super();
        }

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.c(this, this.KO);
        }
    }

    class AnonymousClass_3 extends LoadRequestsImpl {
        final /* synthetic */ int KQ;
        final /* synthetic */ int KR;
        final /* synthetic */ int Kk;

        AnonymousClass_3(int i, int i2, int i3) {
            this.KQ = i;
            this.KR = i2;
            this.Kk = i3;
            super();
        }

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.a(this, this.KQ, this.KR, this.Kk);
        }
    }

    class AnonymousClass_4 extends SendRequestImpl {
        final /* synthetic */ String JT;
        final /* synthetic */ String[] KS;
        final /* synthetic */ int KT;
        final /* synthetic */ byte[] KU;
        final /* synthetic */ int KV;

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.a((d)this, this.JT, this.KS, this.KT, this.KU, this.KV);
        }
    }

    class AnonymousClass_5 extends SendRequestImpl {
        final /* synthetic */ String JT;
        final /* synthetic */ String[] KS;
        final /* synthetic */ int KT;
        final /* synthetic */ byte[] KU;
        final /* synthetic */ int KV;

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.a((d)this, this.JT, this.KS, this.KT, this.KU, this.KV);
        }
    }

    class AnonymousClass_6 extends UpdateRequestsImpl {
        final /* synthetic */ String JT;
        final /* synthetic */ String[] KO;
        final /* synthetic */ String KW;

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.a(this, this.JT, this.KW, this.KO);
        }
    }

    class AnonymousClass_7 extends LoadRequestsImpl {
        final /* synthetic */ String JT;
        final /* synthetic */ int KQ;
        final /* synthetic */ int KR;
        final /* synthetic */ String KW;
        final /* synthetic */ int Kk;

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.a((d)this, this.JT, this.KW, this.KQ, this.KR, this.Kk);
        }
    }

    class AnonymousClass_8 extends LoadRequestSummariesImpl {
        final /* synthetic */ int KR;
        final /* synthetic */ String KW;

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.d(this, this.KW, this.KR);
        }
    }

    public PendingResult<UpdateRequestsResult> acceptRequest(GoogleApiClient apiClient, String requestId) {
        List arrayList = new ArrayList();
        arrayList.add(requestId);
        return acceptRequests(apiClient, arrayList);
    }

    public PendingResult<UpdateRequestsResult> acceptRequests(GoogleApiClient apiClient, List<String> requestIds) {
        return apiClient.b(new AnonymousClass_1(requestIds == null ? null : (String[]) requestIds.toArray(new String[requestIds.size()])));
    }

    public PendingResult<UpdateRequestsResult> dismissRequest(GoogleApiClient apiClient, String requestId) {
        List arrayList = new ArrayList();
        arrayList.add(requestId);
        return dismissRequests(apiClient, arrayList);
    }

    public PendingResult<UpdateRequestsResult> dismissRequests(GoogleApiClient apiClient, List<String> requestIds) {
        return apiClient.b(new AnonymousClass_2(requestIds == null ? null : (String[]) requestIds.toArray(new String[requestIds.size()])));
    }

    public ArrayList<GameRequest> getGameRequestsFromBundle(Bundle extras) {
        if (extras == null || !extras.containsKey(Requests.EXTRA_REQUESTS)) {
            return new ArrayList();
        }
        ArrayList arrayList = (ArrayList) extras.get(Requests.EXTRA_REQUESTS);
        ArrayList<GameRequest> arrayList2 = new ArrayList();
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            arrayList2.add((GameRequest) arrayList.get(i));
            i++;
        }
        return arrayList2;
    }

    public ArrayList<GameRequest> getGameRequestsFromInboxResponse(Intent response) {
        return response == null ? new ArrayList() : getGameRequestsFromBundle(response.getExtras());
    }

    public Intent getInboxIntent(GoogleApiClient apiClient) {
        return Games.c(apiClient).gB();
    }

    public int getMaxLifetimeDays(GoogleApiClient apiClient) {
        return Games.c(apiClient).gD();
    }

    public int getMaxPayloadSize(GoogleApiClient apiClient) {
        return Games.c(apiClient).gC();
    }

    public Intent getSendIntent(GoogleApiClient apiClient, int type, byte[] payload, int requestLifetimeDays, Bitmap icon, String description) {
        return Games.c(apiClient).a(type, payload, requestLifetimeDays, icon, description);
    }

    public PendingResult<LoadRequestsResult> loadRequests(GoogleApiClient apiClient, int requestDirection, int types, int sortOrder) {
        return apiClient.a(new AnonymousClass_3(requestDirection, types, sortOrder));
    }

    public void registerRequestListener(GoogleApiClient apiClient, OnRequestReceivedListener listener) {
        Games.c(apiClient).a(listener);
    }

    public void unregisterRequestListener(GoogleApiClient apiClient) {
        Games.c(apiClient).gv();
    }
}