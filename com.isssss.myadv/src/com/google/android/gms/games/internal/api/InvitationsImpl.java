package com.google.android.gms.games.internal.api;

import android.content.Intent;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Games.BaseGamesApiMethodImpl;
import com.google.android.gms.games.internal.GamesClientImpl;
import com.google.android.gms.games.multiplayer.InvitationBuffer;
import com.google.android.gms.games.multiplayer.Invitations;
import com.google.android.gms.games.multiplayer.Invitations.LoadInvitationsResult;
import com.google.android.gms.games.multiplayer.OnInvitationReceivedListener;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;

public final class InvitationsImpl implements Invitations {

    private static abstract class LoadInvitationsImpl extends BaseGamesApiMethodImpl<LoadInvitationsResult> {

        class AnonymousClass_1 implements LoadInvitationsResult {
            final /* synthetic */ Status wz;

            AnonymousClass_1(Status status) {
                this.wz = status;
            }

            public InvitationBuffer getInvitations() {
                return new InvitationBuffer(DataHolder.empty(ApiEventType.API_MRAID_IS_VIEWABLE));
            }

            public Status getStatus() {
                return this.wz;
            }

            public void release() {
            }
        }

        private LoadInvitationsImpl() {
        }

        public LoadInvitationsResult C(Status status) {
            return new AnonymousClass_1(status);
        }

        public /* synthetic */ Result d(Status status) {
            return C(status);
        }
    }

    class AnonymousClass_1 extends LoadInvitationsImpl {
        final /* synthetic */ int Kk;

        AnonymousClass_1(int i) {
            this.Kk = i;
            super();
        }

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.c(this, this.Kk);
        }
    }

    class AnonymousClass_2 extends LoadInvitationsImpl {
        final /* synthetic */ String JT;
        final /* synthetic */ int Kk;

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.c(this, this.JT, this.Kk);
        }
    }

    class AnonymousClass_3 extends LoadInvitationsImpl {
        final /* synthetic */ String Km;

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.l(this, this.Km);
        }
    }

    public Intent getInvitationInboxIntent(GoogleApiClient apiClient) {
        return Games.c(apiClient).gs();
    }

    public PendingResult<LoadInvitationsResult> loadInvitations(GoogleApiClient apiClient) {
        return loadInvitations(apiClient, 0);
    }

    public PendingResult<LoadInvitationsResult> loadInvitations(GoogleApiClient apiClient, int sortOrder) {
        return apiClient.a(new AnonymousClass_1(sortOrder));
    }

    public void registerInvitationListener(GoogleApiClient apiClient, OnInvitationReceivedListener listener) {
        Games.c(apiClient).a(listener);
    }

    public void unregisterInvitationListener(GoogleApiClient apiClient) {
        Games.c(apiClient).gt();
    }
}