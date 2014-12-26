package com.google.android.gms.games.internal;

import android.content.Context;
import android.content.Intent;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.view.View;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Releasable;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.a.d;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.games.Game;
import com.google.android.gms.games.GameBuffer;
import com.google.android.gms.games.GameEntity;
import com.google.android.gms.games.GamesMetadata.LoadExtendedGamesResult;
import com.google.android.gms.games.GamesMetadata.LoadGameInstancesResult;
import com.google.android.gms.games.GamesMetadata.LoadGameSearchSuggestionsResult;
import com.google.android.gms.games.GamesMetadata.LoadGamesResult;
import com.google.android.gms.games.Notifications.ContactSettingLoadResult;
import com.google.android.gms.games.Notifications.GameMuteStatusChangeResult;
import com.google.android.gms.games.Notifications.GameMuteStatusLoadResult;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayerBuffer;
import com.google.android.gms.games.PlayerEntity;
import com.google.android.gms.games.Players.LoadExtendedPlayersResult;
import com.google.android.gms.games.Players.LoadOwnerCoverPhotoUrisResult;
import com.google.android.gms.games.Players.LoadPlayersResult;
import com.google.android.gms.games.achievement.AchievementBuffer;
import com.google.android.gms.games.achievement.Achievements.LoadAchievementsResult;
import com.google.android.gms.games.achievement.Achievements.UpdateAchievementResult;
import com.google.android.gms.games.internal.IGamesService.Stub;
import com.google.android.gms.games.internal.constants.RequestType;
import com.google.android.gms.games.internal.game.Acls.LoadAclResult;
import com.google.android.gms.games.internal.game.ExtendedGameBuffer;
import com.google.android.gms.games.internal.game.GameInstanceBuffer;
import com.google.android.gms.games.internal.player.ExtendedPlayerBuffer;
import com.google.android.gms.games.internal.request.RequestUpdateOutcomes;
import com.google.android.gms.games.leaderboard.Leaderboard;
import com.google.android.gms.games.leaderboard.LeaderboardBuffer;
import com.google.android.gms.games.leaderboard.LeaderboardEntity;
import com.google.android.gms.games.leaderboard.LeaderboardScore;
import com.google.android.gms.games.leaderboard.LeaderboardScoreBuffer;
import com.google.android.gms.games.leaderboard.LeaderboardScoreEntity;
import com.google.android.gms.games.leaderboard.Leaderboards.LeaderboardMetadataResult;
import com.google.android.gms.games.leaderboard.Leaderboards.LoadPlayerScoreResult;
import com.google.android.gms.games.leaderboard.Leaderboards.LoadScoresResult;
import com.google.android.gms.games.leaderboard.Leaderboards.SubmitScoreResult;
import com.google.android.gms.games.leaderboard.ScoreSubmissionData;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.InvitationBuffer;
import com.google.android.gms.games.multiplayer.Invitations.LoadInvitationsResult;
import com.google.android.gms.games.multiplayer.OnInvitationReceivedListener;
import com.google.android.gms.games.multiplayer.ParticipantResult;
import com.google.android.gms.games.multiplayer.ParticipantUtils;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMultiplayer.ReliableMessageSentCallback;
import com.google.android.gms.games.multiplayer.realtime.RealTimeSocket;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomBuffer;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomEntity;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateListener;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateListener;
import com.google.android.gms.games.multiplayer.turnbased.LoadMatchesResponse;
import com.google.android.gms.games.multiplayer.turnbased.OnTurnBasedMatchUpdateReceivedListener;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatchBuffer;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatchConfig;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer.CancelMatchResult;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer.InitiateMatchResult;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer.LeaveMatchResult;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer.LoadMatchResult;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer.LoadMatchesResult;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer.UpdateMatchResult;
import com.google.android.gms.games.request.GameRequest;
import com.google.android.gms.games.request.GameRequestBuffer;
import com.google.android.gms.games.request.OnRequestReceivedListener;
import com.google.android.gms.games.request.Requests.LoadRequestSummariesResult;
import com.google.android.gms.games.request.Requests.LoadRequestsResult;
import com.google.android.gms.games.request.Requests.SendRequestResult;
import com.google.android.gms.games.request.Requests.UpdateRequestsResult;
import com.google.android.gms.internal.ff;
import com.google.android.gms.internal.ff.e;
import com.google.android.gms.internal.fm;
import com.google.android.gms.internal.fq;
import com.millennialmedia.android.MMAdView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public final class GamesClientImpl extends ff<IGamesService> implements ConnectionCallbacks, OnConnectionFailedListener {
    private boolean IA;
    private int IB;
    private final Binder IC;
    private final long IE;
    private final boolean IF;
    private final int IG;
    private final boolean IH;
    private final String Iu;
    private final Map<String, RealTimeSocket> Iv;
    private PlayerEntity Iw;
    private GameEntity Ix;
    private final PopupManager Iy;
    private boolean Iz;
    private final String wG;

    final class ContactSettingsUpdatedCallback extends b<d<Status>> {
        private final Status wJ;

        ContactSettingsUpdatedCallback(d<Status> resultHolder, int statusCode) {
            super(resultHolder);
            this.wJ = new Status(statusCode);
        }

        protected /* synthetic */ void a(Object obj) {
            c((d) obj);
        }

        protected void c(d<Status> dVar) {
            dVar.b(this.wJ);
        }

        protected void dx() {
        }
    }

    final class InvitationReceivedCallback extends b<OnInvitationReceivedListener> {
        private final Invitation IU;

        InvitationReceivedCallback(OnInvitationReceivedListener listener, Invitation invitation) {
            super(listener);
            this.IU = invitation;
        }

        protected /* synthetic */ void a(Object obj) {
            b((OnInvitationReceivedListener) obj);
        }

        protected void b(OnInvitationReceivedListener onInvitationReceivedListener) {
            onInvitationReceivedListener.onInvitationReceived(this.IU);
        }

        protected void dx() {
        }
    }

    final class InvitationRemovedCallback extends b<OnInvitationReceivedListener> {
        private final String IV;

        InvitationRemovedCallback(OnInvitationReceivedListener listener, String invitationId) {
            super(listener);
            this.IV = invitationId;
        }

        protected /* synthetic */ void a(Object obj) {
            b((OnInvitationReceivedListener) obj);
        }

        protected void b(OnInvitationReceivedListener onInvitationReceivedListener) {
            onInvitationReceivedListener.onInvitationRemoved(this.IV);
        }

        protected void dx() {
        }
    }

    final class LeftRoomCallback extends b<RoomUpdateListener> {
        private final int Ah;
        private final String Ja;

        LeftRoomCallback(RoomUpdateListener listener, int statusCode, String roomId) {
            super(listener);
            this.Ah = statusCode;
            this.Ja = roomId;
        }

        public void a(RoomUpdateListener roomUpdateListener) {
            roomUpdateListener.onLeftRoom(this.Ah, this.Ja);
        }

        protected void dx() {
        }
    }

    final class MatchRemovedCallback extends b<OnTurnBasedMatchUpdateReceivedListener> {
        private final String Jb;

        MatchRemovedCallback(OnTurnBasedMatchUpdateReceivedListener listener, String matchId) {
            super(listener);
            this.Jb = matchId;
        }

        protected /* synthetic */ void a(Object obj) {
            b((OnTurnBasedMatchUpdateReceivedListener) obj);
        }

        protected void b(OnTurnBasedMatchUpdateReceivedListener onTurnBasedMatchUpdateReceivedListener) {
            onTurnBasedMatchUpdateReceivedListener.onTurnBasedMatchRemoved(this.Jb);
        }

        protected void dx() {
        }
    }

    final class MatchUpdateReceivedCallback extends b<OnTurnBasedMatchUpdateReceivedListener> {
        private final TurnBasedMatch Jd;

        MatchUpdateReceivedCallback(OnTurnBasedMatchUpdateReceivedListener listener, TurnBasedMatch match) {
            super(listener);
            this.Jd = match;
        }

        protected /* synthetic */ void a(Object obj) {
            b((OnTurnBasedMatchUpdateReceivedListener) obj);
        }

        protected void b(OnTurnBasedMatchUpdateReceivedListener onTurnBasedMatchUpdateReceivedListener) {
            onTurnBasedMatchUpdateReceivedListener.onTurnBasedMatchReceived(this.Jd);
        }

        protected void dx() {
        }
    }

    final class MessageReceivedCallback extends b<RealTimeMessageReceivedListener> {
        private final RealTimeMessage Je;

        MessageReceivedCallback(RealTimeMessageReceivedListener listener, RealTimeMessage message) {
            super(listener);
            this.Je = message;
        }

        public void a(RealTimeMessageReceivedListener realTimeMessageReceivedListener) {
            if (realTimeMessageReceivedListener != null) {
                realTimeMessageReceivedListener.onRealTimeMessageReceived(this.Je);
            }
        }

        protected void dx() {
        }
    }

    final class NotifyAclUpdatedCallback extends b<d<Status>> {
        private final Status wJ;

        NotifyAclUpdatedCallback(d<Status> resultHolder, int statusCode) {
            super(resultHolder);
            this.wJ = new Status(statusCode);
        }

        protected /* synthetic */ void a(Object obj) {
            c((d) obj);
        }

        protected void c(d<Status> dVar) {
            dVar.b(this.wJ);
        }

        protected void dx() {
        }
    }

    final class P2PConnectedCallback extends b<RoomStatusUpdateListener> {
        private final String Jg;

        P2PConnectedCallback(RoomStatusUpdateListener listener, String participantId) {
            super(listener);
            this.Jg = participantId;
        }

        public void a(RoomStatusUpdateListener roomStatusUpdateListener) {
            if (roomStatusUpdateListener != null) {
                roomStatusUpdateListener.onP2PConnected(this.Jg);
            }
        }

        protected void dx() {
        }
    }

    final class P2PDisconnectedCallback extends b<RoomStatusUpdateListener> {
        private final String Jg;

        P2PDisconnectedCallback(RoomStatusUpdateListener listener, String participantId) {
            super(listener);
            this.Jg = participantId;
        }

        public void a(RoomStatusUpdateListener roomStatusUpdateListener) {
            if (roomStatusUpdateListener != null) {
                roomStatusUpdateListener.onP2PDisconnected(this.Jg);
            }
        }

        protected void dx() {
        }
    }

    final class RealTimeMessageSentCallback extends b<ReliableMessageSentCallback> {
        private final int Ah;
        private final String Jj;
        private final int Jk;

        RealTimeMessageSentCallback(ReliableMessageSentCallback listener, int statusCode, int token, String recipientParticipantId) {
            super(listener);
            this.Ah = statusCode;
            this.Jk = token;
            this.Jj = recipientParticipantId;
        }

        public void a(ReliableMessageSentCallback reliableMessageSentCallback) {
            if (reliableMessageSentCallback != null) {
                reliableMessageSentCallback.onRealTimeMessageSent(this.Ah, this.Jk, this.Jj);
            }
        }

        protected void dx() {
        }
    }

    final class RequestReceivedCallback extends b<OnRequestReceivedListener> {
        private final GameRequest Jn;

        RequestReceivedCallback(OnRequestReceivedListener listener, GameRequest request) {
            super(listener);
            this.Jn = request;
        }

        protected /* synthetic */ void a(Object obj) {
            b((OnRequestReceivedListener) obj);
        }

        protected void b(OnRequestReceivedListener onRequestReceivedListener) {
            onRequestReceivedListener.onRequestReceived(this.Jn);
        }

        protected void dx() {
        }
    }

    final class RequestRemovedCallback extends b<OnRequestReceivedListener> {
        private final String Jo;

        RequestRemovedCallback(OnRequestReceivedListener listener, String requestId) {
            super(listener);
            this.Jo = requestId;
        }

        protected /* synthetic */ void a(Object obj) {
            b((OnRequestReceivedListener) obj);
        }

        protected void b(OnRequestReceivedListener onRequestReceivedListener) {
            onRequestReceivedListener.onRequestRemoved(this.Jo);
        }

        protected void dx() {
        }
    }

    final class SignOutCompleteCallback extends b<d<Status>> {
        private final Status wJ;

        public SignOutCompleteCallback(d<Status> resultHolder, Status status) {
            super(resultHolder);
            this.wJ = status;
        }

        public /* synthetic */ void a(Object obj) {
            c((d) obj);
        }

        public void c(d<Status> dVar) {
            dVar.b(this.wJ);
        }

        protected void dx() {
        }
    }

    abstract class AbstractRoomCallback extends ff.d<RoomUpdateListener> {
        AbstractRoomCallback(RoomUpdateListener listener, DataHolder dataHolder) {
            super(listener, dataHolder);
        }

        protected void a(RoomUpdateListener roomUpdateListener, DataHolder dataHolder) {
            a(roomUpdateListener, GamesClientImpl.this.G(dataHolder), dataHolder.getStatusCode());
        }

        protected abstract void a(RoomUpdateListener roomUpdateListener, Room room, int i);
    }

    abstract class AbstractRoomStatusCallback extends ff.d<RoomStatusUpdateListener> {
        AbstractRoomStatusCallback(RoomStatusUpdateListener listener, DataHolder dataHolder) {
            super(listener, dataHolder);
        }

        protected void a(RoomStatusUpdateListener roomStatusUpdateListener, DataHolder dataHolder) {
            a(roomStatusUpdateListener, GamesClientImpl.this.G(dataHolder));
        }

        protected abstract void a(RoomStatusUpdateListener roomStatusUpdateListener, Room room);
    }

    final class AchievementUpdatedCallback extends b<d<UpdateAchievementResult>> implements UpdateAchievementResult {
        private final String IK;
        private final Status wJ;

        AchievementUpdatedCallback(d<UpdateAchievementResult> resultHolder, int statusCode, String achievementId) {
            super(resultHolder);
            this.wJ = new Status(statusCode);
            this.IK = achievementId;
        }

        protected /* synthetic */ void a(Object obj) {
            c((d) obj);
        }

        protected void c(d<UpdateAchievementResult> dVar) {
            dVar.b(this);
        }

        protected void dx() {
        }

        public String getAchievementId() {
            return this.IK;
        }

        public Status getStatus() {
            return this.wJ;
        }
    }

    final class GameMuteStatusChangedCallback extends b<d<GameMuteStatusChangeResult>> implements GameMuteStatusChangeResult {
        private final String IP;
        private final boolean IQ;
        private final Status wJ;

        public GameMuteStatusChangedCallback(d<GameMuteStatusChangeResult> resultHolder, int statusCode, String externalGameId, boolean isMuted) {
            super(resultHolder);
            this.wJ = new Status(statusCode);
            this.IP = externalGameId;
            this.IQ = isMuted;
        }

        protected /* synthetic */ void a(Object obj) {
            c((d) obj);
        }

        protected void c(d<GameMuteStatusChangeResult> dVar) {
            dVar.b(this);
        }

        protected void dx() {
        }

        public Status getStatus() {
            return this.wJ;
        }
    }

    final class GameMuteStatusLoadedCallback extends b<d<GameMuteStatusLoadResult>> implements GameMuteStatusLoadResult {
        private final String IP;
        private final boolean IQ;
        private final Status wJ;

        public GameMuteStatusLoadedCallback(d<GameMuteStatusLoadResult> resultHolder, DataHolder dataHolder) {
            super(resultHolder);
            this.wJ = new Status(dataHolder.getStatusCode());
            if (dataHolder.getCount() > 0) {
                this.IP = dataHolder.getString("external_game_id", 0, 0);
                this.IQ = dataHolder.getBoolean("muted", 0, 0);
            } else {
                this.IP = null;
                this.IQ = false;
            }
            dataHolder.close();
        }

        protected /* synthetic */ void a(Object obj) {
            c((d) obj);
        }

        protected void c(d<GameMuteStatusLoadResult> dVar) {
            dVar.b(this);
        }

        protected void dx() {
        }

        public Status getStatus() {
            return this.wJ;
        }
    }

    final class OwnerCoverPhotoUrisLoadedCallback extends b<d<LoadOwnerCoverPhotoUrisResult>> implements LoadOwnerCoverPhotoUrisResult {
        private final Bundle Jf;
        private final Status wJ;

        OwnerCoverPhotoUrisLoadedCallback(d<LoadOwnerCoverPhotoUrisResult> resultHolder, int statusCode, Bundle bundle) {
            super(resultHolder);
            this.wJ = new Status(statusCode);
            this.Jf = bundle;
        }

        protected /* synthetic */ void a(Object obj) {
            c((d) obj);
        }

        protected void c(d<LoadOwnerCoverPhotoUrisResult> dVar) {
            dVar.b(this);
        }

        protected void dx() {
        }

        public Status getStatus() {
            return this.wJ;
        }
    }

    final class PlayerLeaderboardScoreLoadedCallback extends ff.d<d<LoadPlayerScoreResult>> implements LoadPlayerScoreResult {
        private final LeaderboardScoreEntity Jh;
        private final Status wJ;

        PlayerLeaderboardScoreLoadedCallback(d<LoadPlayerScoreResult> resultHolder, DataHolder scoreHolder) {
            super(resultHolder, scoreHolder);
            this.wJ = new Status(scoreHolder.getStatusCode());
            LeaderboardScoreBuffer leaderboardScoreBuffer = new LeaderboardScoreBuffer(scoreHolder);
            if (leaderboardScoreBuffer.getCount() > 0) {
                this.Jh = (LeaderboardScoreEntity) leaderboardScoreBuffer.get(0).freeze();
            } else {
                this.Jh = null;
            }
            leaderboardScoreBuffer.close();
        }

        protected void a(d<LoadPlayerScoreResult> dVar, DataHolder dataHolder) {
            dVar.b(this);
        }

        public LeaderboardScore getScore() {
            return this.Jh;
        }

        public Status getStatus() {
            return this.wJ;
        }
    }

    final class RequestsLoadedCallback extends b<d<LoadRequestsResult>> implements LoadRequestsResult {
        private final Bundle Js;
        private final Status wJ;

        RequestsLoadedCallback(d<LoadRequestsResult> resultHolder, Status status, Bundle requestData) {
            super(resultHolder);
            this.wJ = status;
            this.Js = requestData;
        }

        protected /* synthetic */ void a(Object obj) {
            c((d) obj);
        }

        protected void c(d<LoadRequestsResult> dVar) {
            dVar.b(this);
        }

        protected void dx() {
            release();
        }

        public GameRequestBuffer getRequests(int requestType) {
            String bd = RequestType.bd(requestType);
            return !this.Js.containsKey(bd) ? null : new GameRequestBuffer((DataHolder) this.Js.get(bd));
        }

        public Status getStatus() {
            return this.wJ;
        }

        public void release() {
            Iterator it = this.Js.keySet().iterator();
            while (it.hasNext()) {
                DataHolder dataHolder = (DataHolder) this.Js.getParcelable((String) it.next());
                if (dataHolder != null) {
                    dataHolder.close();
                }
            }
        }
    }

    abstract class ResultDataHolderCallback<R extends d<?>> extends ff.d<R> implements Releasable, Result {
        final DataHolder BB;
        final Status wJ;

        public ResultDataHolderCallback(R resultHolder, DataHolder dataHolder) {
            super(resultHolder, dataHolder);
            this.wJ = new Status(dataHolder.getStatusCode());
            this.BB = dataHolder;
        }

        public Status getStatus() {
            return this.wJ;
        }

        public void release() {
            if (this.BB != null) {
                this.BB.close();
            }
        }
    }

    final class TurnBasedMatchCanceledCallback extends b<d<CancelMatchResult>> implements CancelMatchResult {
        private final String JA;
        private final Status wJ;

        TurnBasedMatchCanceledCallback(d<CancelMatchResult> resultHolder, Status status, String externalMatchId) {
            super(resultHolder);
            this.wJ = status;
            this.JA = externalMatchId;
        }

        public /* synthetic */ void a(Object obj) {
            c((d) obj);
        }

        public void c(d<CancelMatchResult> dVar) {
            dVar.b(this);
        }

        protected void dx() {
        }

        public String getMatchId() {
            return this.JA;
        }

        public Status getStatus() {
            return this.wJ;
        }
    }

    final class TurnBasedMatchesLoadedCallback extends b<d<LoadMatchesResult>> implements LoadMatchesResult {
        private final LoadMatchesResponse JG;
        private final Status wJ;

        TurnBasedMatchesLoadedCallback(d<LoadMatchesResult> resultHolder, Status status, Bundle matchData) {
            super(resultHolder);
            this.wJ = status;
            this.JG = new LoadMatchesResponse(matchData);
        }

        protected /* synthetic */ void a(Object obj) {
            c((d) obj);
        }

        protected void c(d<LoadMatchesResult> dVar) {
            dVar.b(this);
        }

        protected void dx() {
        }

        public LoadMatchesResponse getMatches() {
            return this.JG;
        }

        public Status getStatus() {
            return this.wJ;
        }

        public void release() {
            this.JG.close();
        }
    }

    abstract class AbstractPeerStatusCallback extends AbstractRoomStatusCallback {
        private final ArrayList<String> II;

        AbstractPeerStatusCallback(RoomStatusUpdateListener listener, DataHolder dataHolder, String[] participantIds) {
            super(listener, dataHolder);
            this.II = new ArrayList();
            int i = 0;
            int length = participantIds.length;
            while (i < length) {
                this.II.add(participantIds[i]);
                i++;
            }
        }

        protected void a(RoomStatusUpdateListener roomStatusUpdateListener, Room room) {
            a(roomStatusUpdateListener, room, this.II);
        }

        protected abstract void a(RoomStatusUpdateListener roomStatusUpdateListener, Room room, ArrayList<String> arrayList);
    }

    final class AchievementUpdatedBinderCallback extends AbstractGamesCallbacks {
        private final d<UpdateAchievementResult> wH;

        AchievementUpdatedBinderCallback(Object resultHolder) {
            this.wH = (d) fq.b(resultHolder, (Object)"Holder must not be null");
        }

        public void e(int i, String str) {
            GamesClientImpl.this.a(new AchievementUpdatedCallback(this.wH, i, str));
        }
    }

    final class AchievementsLoadedBinderCallback extends AbstractGamesCallbacks {
        private final d<LoadAchievementsResult> wH;

        AchievementsLoadedBinderCallback(Object resultHolder) {
            this.wH = (d) fq.b(resultHolder, (Object)"Holder must not be null");
        }

        public void b(DataHolder dataHolder) {
            GamesClientImpl.this.a(new AchievementsLoadedCallback(this.wH, dataHolder));
        }
    }

    final class AchievementsLoadedCallback extends ResultDataHolderCallback<d<LoadAchievementsResult>> implements LoadAchievementsResult {
        private final AchievementBuffer IL;

        AchievementsLoadedCallback(d<LoadAchievementsResult> resultHolder, DataHolder dataHolder) {
            super(resultHolder, dataHolder);
            this.IL = new AchievementBuffer(dataHolder);
        }

        protected void a(d<LoadAchievementsResult> dVar, DataHolder dataHolder) {
            dVar.b(this);
        }

        public AchievementBuffer getAchievements() {
            return this.IL;
        }
    }

    final class ConnectedToRoomCallback extends AbstractRoomStatusCallback {
        ConnectedToRoomCallback(RoomStatusUpdateListener listener, DataHolder dataHolder) {
            super(listener, dataHolder);
        }

        public void a(RoomStatusUpdateListener roomStatusUpdateListener, Room room) {
            roomStatusUpdateListener.onConnectedToRoom(room);
        }
    }

    final class ContactSettingsLoadedBinderCallback extends AbstractGamesCallbacks {
        private final d<ContactSettingLoadResult> wH;

        ContactSettingsLoadedBinderCallback(Object holder) {
            this.wH = (d) fq.b(holder, (Object)"Holder must not be null");
        }

        public void B(DataHolder dataHolder) {
            GamesClientImpl.this.a(new ContactSettingsLoadedCallback(this.wH, dataHolder));
        }
    }

    final class ContactSettingsLoadedCallback extends ResultDataHolderCallback<d<ContactSettingLoadResult>> implements ContactSettingLoadResult {
        ContactSettingsLoadedCallback(d<ContactSettingLoadResult> resultHolder, DataHolder dataHolder) {
            super(resultHolder, dataHolder);
        }

        protected void a(d<ContactSettingLoadResult> dVar, DataHolder dataHolder) {
            dVar.b(this);
        }
    }

    final class ContactSettingsUpdatedBinderCallback extends AbstractGamesCallbacks {
        private final d<Status> wH;

        ContactSettingsUpdatedBinderCallback(Object holder) {
            this.wH = (d) fq.b(holder, (Object)"Holder must not be null");
        }

        public void aV(int i) {
            GamesClientImpl.this.a(new ContactSettingsUpdatedCallback(this.wH, i));
        }
    }

    final class DisconnectedFromRoomCallback extends AbstractRoomStatusCallback {
        DisconnectedFromRoomCallback(RoomStatusUpdateListener listener, DataHolder dataHolder) {
            super(listener, dataHolder);
        }

        public void a(RoomStatusUpdateListener roomStatusUpdateListener, Room room) {
            roomStatusUpdateListener.onDisconnectedFromRoom(room);
        }
    }

    final class ExtendedGamesLoadedBinderCallback extends AbstractGamesCallbacks {
        private final d<LoadExtendedGamesResult> wH;

        ExtendedGamesLoadedBinderCallback(Object holder) {
            this.wH = (d) fq.b(holder, (Object)"Holder must not be null");
        }

        public void h(DataHolder dataHolder) {
            GamesClientImpl.this.a(new ExtendedGamesLoadedCallback(this.wH, dataHolder));
        }
    }

    final class ExtendedGamesLoadedCallback extends ResultDataHolderCallback<d<LoadExtendedGamesResult>> implements LoadExtendedGamesResult {
        private final ExtendedGameBuffer IM;

        ExtendedGamesLoadedCallback(d<LoadExtendedGamesResult> resultHolder, DataHolder dataHolder) {
            super(resultHolder, dataHolder);
            this.IM = new ExtendedGameBuffer(dataHolder);
        }

        protected void a(d<LoadExtendedGamesResult> dVar, DataHolder dataHolder) {
            dVar.b(this);
        }
    }

    final class ExtendedPlayersLoadedBinderCallback extends AbstractGamesCallbacks {
        private final d<LoadExtendedPlayersResult> wH;

        ExtendedPlayersLoadedBinderCallback(Object holder) {
            this.wH = (d) fq.b(holder, (Object)"Holder must not be null");
        }

        public void f(DataHolder dataHolder) {
            GamesClientImpl.this.a(new ExtendedPlayersLoadedCallback(this.wH, dataHolder));
        }
    }

    final class ExtendedPlayersLoadedCallback extends ResultDataHolderCallback<d<LoadExtendedPlayersResult>> implements LoadExtendedPlayersResult {
        private final ExtendedPlayerBuffer IN;

        ExtendedPlayersLoadedCallback(d<LoadExtendedPlayersResult> resultHolder, DataHolder dataHolder) {
            super(resultHolder, dataHolder);
            this.IN = new ExtendedPlayerBuffer(dataHolder);
        }

        protected void a(d<LoadExtendedPlayersResult> dVar, DataHolder dataHolder) {
            dVar.b(this);
        }
    }

    final class GameInstancesLoadedBinderCallback extends AbstractGamesCallbacks {
        private final d<LoadGameInstancesResult> wH;

        GameInstancesLoadedBinderCallback(Object holder) {
            this.wH = (d) fq.b(holder, (Object)"Holder must not be null");
        }

        public void i(DataHolder dataHolder) {
            GamesClientImpl.this.a(new GameInstancesLoadedCallback(this.wH, dataHolder));
        }
    }

    final class GameInstancesLoadedCallback extends ResultDataHolderCallback<d<LoadGameInstancesResult>> implements LoadGameInstancesResult {
        private final GameInstanceBuffer IO;

        GameInstancesLoadedCallback(d<LoadGameInstancesResult> resultHolder, DataHolder dataHolder) {
            super(resultHolder, dataHolder);
            this.IO = new GameInstanceBuffer(dataHolder);
        }

        protected void a(d<LoadGameInstancesResult> dVar, DataHolder dataHolder) {
            dVar.b(this);
        }
    }

    final class GameMuteStatusChangedBinderCallback extends AbstractGamesCallbacks {
        private final d<GameMuteStatusChangeResult> wH;

        GameMuteStatusChangedBinderCallback(Object holder) {
            this.wH = (d) fq.b(holder, (Object)"Holder must not be null");
        }

        public void a(int i, String str, boolean z) {
            GamesClientImpl.this.a(new GameMuteStatusChangedCallback(this.wH, i, str, z));
        }
    }

    final class GameMuteStatusLoadedBinderCallback extends AbstractGamesCallbacks {
        private final d<GameMuteStatusLoadResult> wH;

        GameMuteStatusLoadedBinderCallback(Object holder) {
            this.wH = (d) fq.b(holder, (Object)"Holder must not be null");
        }

        public void z(DataHolder dataHolder) {
            GamesClientImpl.this.a(new GameMuteStatusLoadedCallback(this.wH, dataHolder));
        }
    }

    final class GameSearchSuggestionsLoadedBinderCallback extends AbstractGamesCallbacks {
        private final d<LoadGameSearchSuggestionsResult> wH;

        GameSearchSuggestionsLoadedBinderCallback(Object holder) {
            this.wH = (d) fq.b(holder, (Object)"Holder must not be null");
        }

        public void j(DataHolder dataHolder) {
            GamesClientImpl.this.a(new GameSearchSuggestionsLoadedCallback(this.wH, dataHolder));
        }
    }

    final class GameSearchSuggestionsLoadedCallback extends ResultDataHolderCallback<d<LoadGameSearchSuggestionsResult>> implements LoadGameSearchSuggestionsResult {
        private final DataHolder IR;

        GameSearchSuggestionsLoadedCallback(d<LoadGameSearchSuggestionsResult> resultHolder, DataHolder data) {
            super(resultHolder, data);
            this.IR = data;
        }

        protected void a(d<LoadGameSearchSuggestionsResult> dVar, DataHolder dataHolder) {
            dVar.b(this);
        }
    }

    final class GamesLoadedBinderCallback extends AbstractGamesCallbacks {
        private final d<LoadGamesResult> wH;

        GamesLoadedBinderCallback(Object holder) {
            this.wH = (d) fq.b(holder, (Object)"Holder must not be null");
        }

        public void g(DataHolder dataHolder) {
            GamesClientImpl.this.a(new GamesLoadedCallback(this.wH, dataHolder));
        }
    }

    final class GamesLoadedCallback extends ResultDataHolderCallback<d<LoadGamesResult>> implements LoadGamesResult {
        private final GameBuffer IS;

        GamesLoadedCallback(d<LoadGamesResult> resultHolder, DataHolder dataHolder) {
            super(resultHolder, dataHolder);
            this.IS = new GameBuffer(dataHolder);
        }

        protected void a(d<LoadGamesResult> dVar, DataHolder dataHolder) {
            dVar.b(this);
        }

        public GameBuffer getGames() {
            return this.IS;
        }
    }

    final class InvitationReceivedBinderCallback extends AbstractGamesCallbacks {
        private final OnInvitationReceivedListener IT;

        InvitationReceivedBinderCallback(OnInvitationReceivedListener listener) {
            this.IT = listener;
        }

        public void l(DataHolder dataHolder) {
            InvitationBuffer invitationBuffer = new InvitationBuffer(dataHolder);
            Invitation invitation = null;
            if (invitationBuffer.getCount() > 0) {
                invitation = (Invitation) ((Invitation) invitationBuffer.get(0)).freeze();
            }
            invitationBuffer.close();
            if (invitation != null) {
                GamesClientImpl.this.a(new InvitationReceivedCallback(this.IT, invitation));
            }
        }

        public void onInvitationRemoved(String invitationId) {
            GamesClientImpl.this.a(new InvitationRemovedCallback(this.IT, invitationId));
        }
    }

    final class InvitationsLoadedBinderCallback extends AbstractGamesCallbacks {
        private final d<LoadInvitationsResult> wH;

        InvitationsLoadedBinderCallback(Object resultHolder) {
            this.wH = (d) fq.b(resultHolder, (Object)"Holder must not be null");
        }

        public void k(DataHolder dataHolder) {
            GamesClientImpl.this.a(new InvitationsLoadedCallback(this.wH, dataHolder));
        }
    }

    final class InvitationsLoadedCallback extends ResultDataHolderCallback<d<LoadInvitationsResult>> implements LoadInvitationsResult {
        private final InvitationBuffer IW;

        InvitationsLoadedCallback(d<LoadInvitationsResult> resultHolder, DataHolder dataHolder) {
            super(resultHolder, dataHolder);
            this.IW = new InvitationBuffer(dataHolder);
        }

        protected void a(d<LoadInvitationsResult> dVar, DataHolder dataHolder) {
            dVar.b(this);
        }

        public InvitationBuffer getInvitations() {
            return this.IW;
        }
    }

    final class JoinedRoomCallback extends AbstractRoomCallback {
        public JoinedRoomCallback(RoomUpdateListener listener, DataHolder dataHolder) {
            super(listener, dataHolder);
        }

        public void a(RoomUpdateListener roomUpdateListener, Room room, int i) {
            roomUpdateListener.onJoinedRoom(i, room);
        }
    }

    final class LeaderboardScoresLoadedBinderCallback extends AbstractGamesCallbacks {
        private final d<LoadScoresResult> wH;

        LeaderboardScoresLoadedBinderCallback(Object resultHolder) {
            this.wH = (d) fq.b(resultHolder, (Object)"Holder must not be null");
        }

        public void a(DataHolder dataHolder, DataHolder dataHolder2) {
            GamesClientImpl.this.a(new LeaderboardScoresLoadedCallback(this.wH, dataHolder, dataHolder2));
        }
    }

    final class LeaderboardScoresLoadedCallback extends ResultDataHolderCallback<d<LoadScoresResult>> implements LoadScoresResult {
        private final LeaderboardEntity IX;
        private final LeaderboardScoreBuffer IY;

        LeaderboardScoresLoadedCallback(d<LoadScoresResult> resultHolder, DataHolder leaderboard, DataHolder scores) {
            super(resultHolder, scores);
            LeaderboardBuffer leaderboardBuffer = new LeaderboardBuffer(leaderboard);
            if (leaderboardBuffer.getCount() > 0) {
                this.IX = (LeaderboardEntity) ((Leaderboard) leaderboardBuffer.get(0)).freeze();
            } else {
                this.IX = null;
            }
            leaderboardBuffer.close();
            this.IY = new LeaderboardScoreBuffer(scores);
        }

        protected void a(d<LoadScoresResult> dVar, DataHolder dataHolder) {
            dVar.b(this);
        }

        public Leaderboard getLeaderboard() {
            return this.IX;
        }

        public LeaderboardScoreBuffer getScores() {
            return this.IY;
        }
    }

    final class LeaderboardsLoadedBinderCallback extends AbstractGamesCallbacks {
        private final d<LeaderboardMetadataResult> wH;

        LeaderboardsLoadedBinderCallback(Object resultHolder) {
            this.wH = (d) fq.b(resultHolder, (Object)"Holder must not be null");
        }

        public void c(DataHolder dataHolder) {
            GamesClientImpl.this.a(new LeaderboardsLoadedCallback(this.wH, dataHolder));
        }
    }

    final class LeaderboardsLoadedCallback extends ResultDataHolderCallback<d<LeaderboardMetadataResult>> implements LeaderboardMetadataResult {
        private final LeaderboardBuffer IZ;

        LeaderboardsLoadedCallback(d<LeaderboardMetadataResult> resultHolder, DataHolder dataHolder) {
            super(resultHolder, dataHolder);
            this.IZ = new LeaderboardBuffer(dataHolder);
        }

        protected void a(d<LeaderboardMetadataResult> dVar, DataHolder dataHolder) {
            dVar.b(this);
        }

        public LeaderboardBuffer getLeaderboards() {
            return this.IZ;
        }
    }

    final class MatchUpdateReceivedBinderCallback extends AbstractGamesCallbacks {
        private final OnTurnBasedMatchUpdateReceivedListener Jc;

        MatchUpdateReceivedBinderCallback(OnTurnBasedMatchUpdateReceivedListener listener) {
            this.Jc = listener;
        }

        public void onTurnBasedMatchRemoved(String matchId) {
            GamesClientImpl.this.a(new MatchRemovedCallback(this.Jc, matchId));
        }

        public void r(DataHolder dataHolder) {
            TurnBasedMatchBuffer turnBasedMatchBuffer = new TurnBasedMatchBuffer(dataHolder);
            TurnBasedMatch turnBasedMatch = null;
            if (turnBasedMatchBuffer.getCount() > 0) {
                turnBasedMatch = (TurnBasedMatch) ((TurnBasedMatch) turnBasedMatchBuffer.get(0)).freeze();
            }
            turnBasedMatchBuffer.close();
            if (turnBasedMatch != null) {
                GamesClientImpl.this.a(new MatchUpdateReceivedCallback(this.Jc, turnBasedMatch));
            }
        }
    }

    final class NotifyAclLoadedBinderCallback extends AbstractGamesCallbacks {
        private final d<LoadAclResult> wH;

        NotifyAclLoadedBinderCallback(Object resultHolder) {
            this.wH = (d) fq.b(resultHolder, (Object)"Holder must not be null");
        }

        public void A(DataHolder dataHolder) {
            GamesClientImpl.this.a(new NotifyAclLoadedCallback(this.wH, dataHolder));
        }
    }

    final class NotifyAclLoadedCallback extends ResultDataHolderCallback<d<LoadAclResult>> implements LoadAclResult {
        NotifyAclLoadedCallback(d<LoadAclResult> resultHolder, DataHolder dataHolder) {
            super(resultHolder, dataHolder);
        }

        protected void a(d<LoadAclResult> dVar, DataHolder dataHolder) {
            dVar.b(this);
        }
    }

    final class NotifyAclUpdatedBinderCallback extends AbstractGamesCallbacks {
        private final d<Status> wH;

        NotifyAclUpdatedBinderCallback(Object resultHolder) {
            this.wH = (d) fq.b(resultHolder, (Object)"Holder must not be null");
        }

        public void aU(int i) {
            GamesClientImpl.this.a(new NotifyAclUpdatedCallback(this.wH, i));
        }
    }

    final class OwnerCoverPhotoUrisLoadedBinderCallback extends AbstractGamesCallbacks {
        private final d<LoadOwnerCoverPhotoUrisResult> wH;

        OwnerCoverPhotoUrisLoadedBinderCallback(Object holder) {
            this.wH = (d) fq.b(holder, (Object)"Holder must not be null");
        }

        public void c(int i, Bundle bundle) {
            bundle.setClassLoader(getClass().getClassLoader());
            GamesClientImpl.this.a(new OwnerCoverPhotoUrisLoadedCallback(this.wH, i, bundle));
        }
    }

    final class PlayerLeaderboardScoreLoadedBinderCallback extends AbstractGamesCallbacks {
        private final d<LoadPlayerScoreResult> wH;

        PlayerLeaderboardScoreLoadedBinderCallback(Object resultHolder) {
            this.wH = (d) fq.b(resultHolder, (Object)"Holder must not be null");
        }

        public void C(DataHolder dataHolder) {
            GamesClientImpl.this.a(new PlayerLeaderboardScoreLoadedCallback(this.wH, dataHolder));
        }
    }

    final class PlayersLoadedBinderCallback extends AbstractGamesCallbacks {
        private final d<LoadPlayersResult> wH;

        PlayersLoadedBinderCallback(Object holder) {
            this.wH = (d) fq.b(holder, (Object)"Holder must not be null");
        }

        public void e(DataHolder dataHolder) {
            GamesClientImpl.this.a(new PlayersLoadedCallback(this.wH, dataHolder));
        }
    }

    final class PlayersLoadedCallback extends ResultDataHolderCallback<d<LoadPlayersResult>> implements LoadPlayersResult {
        private final PlayerBuffer Ji;

        PlayersLoadedCallback(d<LoadPlayersResult> resultHolder, DataHolder dataHolder) {
            super(resultHolder, dataHolder);
            this.Ji = new PlayerBuffer(dataHolder);
        }

        protected void a(d<LoadPlayersResult> dVar, DataHolder dataHolder) {
            dVar.b(this);
        }

        public PlayerBuffer getPlayers() {
            return this.Ji;
        }
    }

    final class RealTimeReliableMessageBinderCallbacks extends AbstractGamesCallbacks {
        final ReliableMessageSentCallback Jl;

        public RealTimeReliableMessageBinderCallbacks(ReliableMessageSentCallback messageSentCallbacks) {
            this.Jl = messageSentCallbacks;
        }

        public void b(int i, int i2, String str) {
            GamesClientImpl.this.a(new RealTimeMessageSentCallback(this.Jl, i, i2, str));
        }
    }

    final class RequestReceivedBinderCallback extends AbstractGamesCallbacks {
        private final OnRequestReceivedListener Jm;

        RequestReceivedBinderCallback(OnRequestReceivedListener listener) {
            this.Jm = listener;
        }

        public void m(DataHolder dataHolder) {
            GameRequestBuffer gameRequestBuffer = new GameRequestBuffer(dataHolder);
            GameRequest gameRequest = null;
            if (gameRequestBuffer.getCount() > 0) {
                gameRequest = (GameRequest) ((GameRequest) gameRequestBuffer.get(0)).freeze();
            }
            gameRequestBuffer.close();
            if (gameRequest != null) {
                GamesClientImpl.this.a(new RequestReceivedCallback(this.Jm, gameRequest));
            }
        }

        public void onRequestRemoved(String requestId) {
            GamesClientImpl.this.a(new RequestRemovedCallback(this.Jm, requestId));
        }
    }

    final class RequestSentBinderCallbacks extends AbstractGamesCallbacks {
        private final d<SendRequestResult> Jp;

        public RequestSentBinderCallbacks(Object resultHolder) {
            this.Jp = (d) fq.b(resultHolder, (Object)"Holder must not be null");
        }

        public void E(DataHolder dataHolder) {
            GamesClientImpl.this.a(new RequestSentCallback(this.Jp, dataHolder));
        }
    }

    final class RequestSentCallback extends ResultDataHolderCallback<d<SendRequestResult>> implements SendRequestResult {
        private final GameRequest Jn;

        RequestSentCallback(d<SendRequestResult> resultHolder, DataHolder dataHolder) {
            super(resultHolder, dataHolder);
            GameRequestBuffer gameRequestBuffer = new GameRequestBuffer(dataHolder);
            if (gameRequestBuffer.getCount() > 0) {
                this.Jn = (GameRequest) ((GameRequest) gameRequestBuffer.get(0)).freeze();
            } else {
                this.Jn = null;
            }
            gameRequestBuffer.close();
        }

        protected void a(d<SendRequestResult> dVar, DataHolder dataHolder) {
            dVar.b(this);
        }
    }

    final class RequestSummariesLoadedBinderCallbacks extends AbstractGamesCallbacks {
        private final d<LoadRequestSummariesResult> Jq;

        public RequestSummariesLoadedBinderCallbacks(Object resultHolder) {
            this.Jq = (d) fq.b(resultHolder, (Object)"Holder must not be null");
        }

        public void F(DataHolder dataHolder) {
            GamesClientImpl.this.a(new RequestSummariesLoadedCallback(this.Jq, dataHolder));
        }
    }

    final class RequestSummariesLoadedCallback extends ResultDataHolderCallback<d<LoadRequestSummariesResult>> implements LoadRequestSummariesResult {
        RequestSummariesLoadedCallback(d<LoadRequestSummariesResult> resultHolder, DataHolder dataHolder) {
            super(resultHolder, dataHolder);
        }

        protected void a(d<LoadRequestSummariesResult> dVar, DataHolder dataHolder) {
            dVar.b(this);
        }
    }

    final class RequestsLoadedBinderCallbacks extends AbstractGamesCallbacks {
        private final d<LoadRequestsResult> Jr;

        public RequestsLoadedBinderCallbacks(Object resultHolder) {
            this.Jr = (d) fq.b(resultHolder, (Object)"Holder must not be null");
        }

        public void b(int i, Bundle bundle) {
            bundle.setClassLoader(getClass().getClassLoader());
            GamesClientImpl.this.a(new RequestsLoadedCallback(this.Jr, new Status(i), bundle));
        }
    }

    final class RequestsUpdatedBinderCallbacks extends AbstractGamesCallbacks {
        private final d<UpdateRequestsResult> Jt;

        public RequestsUpdatedBinderCallbacks(Object resultHolder) {
            this.Jt = (d) fq.b(resultHolder, (Object)"Holder must not be null");
        }

        public void D(DataHolder dataHolder) {
            GamesClientImpl.this.a(new RequestsUpdatedCallback(this.Jt, dataHolder));
        }
    }

    final class RequestsUpdatedCallback extends ResultDataHolderCallback<d<UpdateRequestsResult>> implements UpdateRequestsResult {
        private final RequestUpdateOutcomes Ju;

        RequestsUpdatedCallback(d<UpdateRequestsResult> resultHolder, DataHolder dataHolder) {
            super(resultHolder, dataHolder);
            this.Ju = RequestUpdateOutcomes.J(dataHolder);
        }

        protected void a(d<UpdateRequestsResult> dVar, DataHolder dataHolder) {
            dVar.b(this);
        }

        public Set<String> getRequestIds() {
            return this.Ju.getRequestIds();
        }

        public int getRequestOutcome(String requestId) {
            return this.Ju.getRequestOutcome(requestId);
        }
    }

    final class RoomAutoMatchingCallback extends AbstractRoomStatusCallback {
        RoomAutoMatchingCallback(RoomStatusUpdateListener listener, DataHolder dataHolder) {
            super(listener, dataHolder);
        }

        public void a(RoomStatusUpdateListener roomStatusUpdateListener, Room room) {
            roomStatusUpdateListener.onRoomAutoMatching(room);
        }
    }

    final class RoomBinderCallbacks extends AbstractGamesCallbacks {
        private final RoomUpdateListener Jv;
        private final RoomStatusUpdateListener Jw;
        private final RealTimeMessageReceivedListener Jx;

        public RoomBinderCallbacks(Object roomCallbacks) {
            this.Jv = (RoomUpdateListener) fq.b(roomCallbacks, (Object)"Callbacks must not be null");
            this.Jw = null;
            this.Jx = null;
        }

        public RoomBinderCallbacks(Object roomCallbacks, RoomStatusUpdateListener roomStatusCallbacks, RealTimeMessageReceivedListener realTimeMessageReceivedCallbacks) {
            this.Jv = (RoomUpdateListener) fq.b(roomCallbacks, (Object)"Callbacks must not be null");
            this.Jw = roomStatusCallbacks;
            this.Jx = realTimeMessageReceivedCallbacks;
        }

        public void a(DataHolder dataHolder, String[] strArr) {
            GamesClientImpl.this.a(new PeerInvitedToRoomCallback(this.Jw, dataHolder, strArr));
        }

        public void b(DataHolder dataHolder, String[] strArr) {
            GamesClientImpl.this.a(new PeerJoinedRoomCallback(this.Jw, dataHolder, strArr));
        }

        public void c(DataHolder dataHolder, String[] strArr) {
            GamesClientImpl.this.a(new PeerLeftRoomCallback(this.Jw, dataHolder, strArr));
        }

        public void d(DataHolder dataHolder, String[] strArr) {
            GamesClientImpl.this.a(new PeerDeclinedCallback(this.Jw, dataHolder, strArr));
        }

        public void e(DataHolder dataHolder, String[] strArr) {
            GamesClientImpl.this.a(new PeerConnectedCallback(this.Jw, dataHolder, strArr));
        }

        public void f(DataHolder dataHolder, String[] strArr) {
            GamesClientImpl.this.a(new PeerDisconnectedCallback(this.Jw, dataHolder, strArr));
        }

        public void onLeftRoom(int statusCode, String externalRoomId) {
            GamesClientImpl.this.a(new LeftRoomCallback(this.Jv, statusCode, externalRoomId));
        }

        public void onP2PConnected(String participantId) {
            GamesClientImpl.this.a(new P2PConnectedCallback(this.Jw, participantId));
        }

        public void onP2PDisconnected(String participantId) {
            GamesClientImpl.this.a(new P2PDisconnectedCallback(this.Jw, participantId));
        }

        public void onRealTimeMessageReceived(RealTimeMessage message) {
            GamesClientImpl.this.a(new MessageReceivedCallback(this.Jx, message));
        }

        public void s(DataHolder dataHolder) {
            GamesClientImpl.this.a(new RoomCreatedCallback(this.Jv, dataHolder));
        }

        public void t(DataHolder dataHolder) {
            GamesClientImpl.this.a(new JoinedRoomCallback(this.Jv, dataHolder));
        }

        public void u(DataHolder dataHolder) {
            GamesClientImpl.this.a(new RoomConnectingCallback(this.Jw, dataHolder));
        }

        public void v(DataHolder dataHolder) {
            GamesClientImpl.this.a(new RoomAutoMatchingCallback(this.Jw, dataHolder));
        }

        public void w(DataHolder dataHolder) {
            GamesClientImpl.this.a(new RoomConnectedCallback(this.Jv, dataHolder));
        }

        public void x(DataHolder dataHolder) {
            GamesClientImpl.this.a(new ConnectedToRoomCallback(this.Jw, dataHolder));
        }

        public void y(DataHolder dataHolder) {
            GamesClientImpl.this.a(new DisconnectedFromRoomCallback(this.Jw, dataHolder));
        }
    }

    final class RoomConnectedCallback extends AbstractRoomCallback {
        RoomConnectedCallback(RoomUpdateListener listener, DataHolder dataHolder) {
            super(listener, dataHolder);
        }

        public void a(RoomUpdateListener roomUpdateListener, Room room, int i) {
            roomUpdateListener.onRoomConnected(i, room);
        }
    }

    final class RoomConnectingCallback extends AbstractRoomStatusCallback {
        RoomConnectingCallback(RoomStatusUpdateListener listener, DataHolder dataHolder) {
            super(listener, dataHolder);
        }

        public void a(RoomStatusUpdateListener roomStatusUpdateListener, Room room) {
            roomStatusUpdateListener.onRoomConnecting(room);
        }
    }

    final class RoomCreatedCallback extends AbstractRoomCallback {
        public RoomCreatedCallback(RoomUpdateListener listener, DataHolder dataHolder) {
            super(listener, dataHolder);
        }

        public void a(RoomUpdateListener roomUpdateListener, Room room, int i) {
            roomUpdateListener.onRoomCreated(i, room);
        }
    }

    final class SignOutCompleteBinderCallbacks extends AbstractGamesCallbacks {
        private final d<Status> wH;

        public SignOutCompleteBinderCallbacks(Object resultHolder) {
            this.wH = (d) fq.b(resultHolder, (Object)"Holder must not be null");
        }

        public void du() {
            GamesClientImpl.this.a(new SignOutCompleteCallback(this.wH, new Status(0)));
        }
    }

    final class SubmitScoreBinderCallbacks extends AbstractGamesCallbacks {
        private final d<SubmitScoreResult> wH;

        public SubmitScoreBinderCallbacks(Object resultHolder) {
            this.wH = (d) fq.b(resultHolder, (Object)"Holder must not be null");
        }

        public void d(DataHolder dataHolder) {
            GamesClientImpl.this.a(new SubmitScoreCallback(this.wH, dataHolder));
        }
    }

    final class SubmitScoreCallback extends ResultDataHolderCallback<d<SubmitScoreResult>> implements SubmitScoreResult {
        private final ScoreSubmissionData Jy;

        public SubmitScoreCallback(d<SubmitScoreResult> resultHolder, DataHolder dataHolder) {
            super(resultHolder, dataHolder);
            this.Jy = new ScoreSubmissionData(dataHolder);
            dataHolder.close();
        }

        public void a(d<SubmitScoreResult> dVar, DataHolder dataHolder) {
            dVar.b(this);
        }

        public ScoreSubmissionData getScoreData() {
            return this.Jy;
        }
    }

    abstract class TurnBasedMatchCallback<T extends d<?>> extends ResultDataHolderCallback<T> {
        final TurnBasedMatch Jd;

        TurnBasedMatchCallback(T listener, DataHolder dataHolder) {
            super(listener, dataHolder);
            TurnBasedMatchBuffer turnBasedMatchBuffer = new TurnBasedMatchBuffer(dataHolder);
            if (turnBasedMatchBuffer.getCount() > 0) {
                this.Jd = (TurnBasedMatch) ((TurnBasedMatch) turnBasedMatchBuffer.get(0)).freeze();
            } else {
                this.Jd = null;
            }
            turnBasedMatchBuffer.close();
        }

        protected void a(T t, DataHolder dataHolder) {
            k(t);
        }

        public TurnBasedMatch getMatch() {
            return this.Jd;
        }

        abstract void k(T t);
    }

    final class TurnBasedMatchCanceledBinderCallbacks extends AbstractGamesCallbacks {
        private final d<CancelMatchResult> Jz;

        public TurnBasedMatchCanceledBinderCallbacks(Object resultHolder) {
            this.Jz = (d) fq.b(resultHolder, (Object)"Holder must not be null");
        }

        public void f(int i, String str) {
            GamesClientImpl.this.a(new TurnBasedMatchCanceledCallback(this.Jz, new Status(i), str));
        }
    }

    final class TurnBasedMatchInitiatedBinderCallbacks extends AbstractGamesCallbacks {
        private final d<InitiateMatchResult> JB;

        public TurnBasedMatchInitiatedBinderCallbacks(Object resultHolder) {
            this.JB = (d) fq.b(resultHolder, (Object)"Holder must not be null");
        }

        public void o(DataHolder dataHolder) {
            GamesClientImpl.this.a(new TurnBasedMatchInitiatedCallback(this.JB, dataHolder));
        }
    }

    final class TurnBasedMatchLeftBinderCallbacks extends AbstractGamesCallbacks {
        private final d<LeaveMatchResult> JC;

        public TurnBasedMatchLeftBinderCallbacks(Object resultHolder) {
            this.JC = (d) fq.b(resultHolder, (Object)"Holder must not be null");
        }

        public void q(DataHolder dataHolder) {
            GamesClientImpl.this.a(new TurnBasedMatchLeftCallback(this.JC, dataHolder));
        }
    }

    final class TurnBasedMatchLoadedBinderCallbacks extends AbstractGamesCallbacks {
        private final d<LoadMatchResult> JD;

        public TurnBasedMatchLoadedBinderCallbacks(Object resultHolder) {
            this.JD = (d) fq.b(resultHolder, (Object)"Holder must not be null");
        }

        public void n(DataHolder dataHolder) {
            GamesClientImpl.this.a(new TurnBasedMatchLoadedCallback(this.JD, dataHolder));
        }
    }

    final class TurnBasedMatchUpdatedBinderCallbacks extends AbstractGamesCallbacks {
        private final d<UpdateMatchResult> JE;

        public TurnBasedMatchUpdatedBinderCallbacks(Object resultHolder) {
            this.JE = (d) fq.b(resultHolder, (Object)"Holder must not be null");
        }

        public void p(DataHolder dataHolder) {
            GamesClientImpl.this.a(new TurnBasedMatchUpdatedCallback(this.JE, dataHolder));
        }
    }

    final class TurnBasedMatchesLoadedBinderCallbacks extends AbstractGamesCallbacks {
        private final d<LoadMatchesResult> JF;

        public TurnBasedMatchesLoadedBinderCallbacks(Object resultHolder) {
            this.JF = (d) fq.b(resultHolder, (Object)"Holder must not be null");
        }

        public void a(int i, Bundle bundle) {
            bundle.setClassLoader(getClass().getClassLoader());
            GamesClientImpl.this.a(new TurnBasedMatchesLoadedCallback(this.JF, new Status(i), bundle));
        }
    }

    final class PeerConnectedCallback extends AbstractPeerStatusCallback {
        PeerConnectedCallback(RoomStatusUpdateListener listener, DataHolder dataHolder, String[] participantIds) {
            super(listener, dataHolder, participantIds);
        }

        protected void a(RoomStatusUpdateListener roomStatusUpdateListener, Room room, ArrayList<String> arrayList) {
            roomStatusUpdateListener.onPeersConnected(room, arrayList);
        }
    }

    final class PeerDeclinedCallback extends AbstractPeerStatusCallback {
        PeerDeclinedCallback(RoomStatusUpdateListener listener, DataHolder dataHolder, String[] participantIds) {
            super(listener, dataHolder, participantIds);
        }

        protected void a(RoomStatusUpdateListener roomStatusUpdateListener, Room room, ArrayList<String> arrayList) {
            roomStatusUpdateListener.onPeerDeclined(room, arrayList);
        }
    }

    final class PeerDisconnectedCallback extends AbstractPeerStatusCallback {
        PeerDisconnectedCallback(RoomStatusUpdateListener listener, DataHolder dataHolder, String[] participantIds) {
            super(listener, dataHolder, participantIds);
        }

        protected void a(RoomStatusUpdateListener roomStatusUpdateListener, Room room, ArrayList<String> arrayList) {
            roomStatusUpdateListener.onPeersDisconnected(room, arrayList);
        }
    }

    final class PeerInvitedToRoomCallback extends AbstractPeerStatusCallback {
        PeerInvitedToRoomCallback(RoomStatusUpdateListener listener, DataHolder dataHolder, String[] participantIds) {
            super(listener, dataHolder, participantIds);
        }

        protected void a(RoomStatusUpdateListener roomStatusUpdateListener, Room room, ArrayList<String> arrayList) {
            roomStatusUpdateListener.onPeerInvitedToRoom(room, arrayList);
        }
    }

    final class PeerJoinedRoomCallback extends AbstractPeerStatusCallback {
        PeerJoinedRoomCallback(RoomStatusUpdateListener listener, DataHolder dataHolder, String[] participantIds) {
            super(listener, dataHolder, participantIds);
        }

        protected void a(RoomStatusUpdateListener roomStatusUpdateListener, Room room, ArrayList<String> arrayList) {
            roomStatusUpdateListener.onPeerJoined(room, arrayList);
        }
    }

    final class PeerLeftRoomCallback extends AbstractPeerStatusCallback {
        PeerLeftRoomCallback(RoomStatusUpdateListener listener, DataHolder dataHolder, String[] participantIds) {
            super(listener, dataHolder, participantIds);
        }

        protected void a(RoomStatusUpdateListener roomStatusUpdateListener, Room room, ArrayList<String> arrayList) {
            roomStatusUpdateListener.onPeerLeft(room, arrayList);
        }
    }

    final class TurnBasedMatchInitiatedCallback extends TurnBasedMatchCallback<d<InitiateMatchResult>> implements InitiateMatchResult {
        TurnBasedMatchInitiatedCallback(d<InitiateMatchResult> resultHolder, DataHolder dataHolder) {
            super(resultHolder, dataHolder);
        }

        protected void k(d<InitiateMatchResult> dVar) {
            dVar.b(this);
        }
    }

    final class TurnBasedMatchLeftCallback extends TurnBasedMatchCallback<d<LeaveMatchResult>> implements LeaveMatchResult {
        TurnBasedMatchLeftCallback(d<LeaveMatchResult> resultHolder, DataHolder dataHolder) {
            super(resultHolder, dataHolder);
        }

        protected void k(d<LeaveMatchResult> dVar) {
            dVar.b(this);
        }
    }

    final class TurnBasedMatchLoadedCallback extends TurnBasedMatchCallback<d<LoadMatchResult>> implements LoadMatchResult {
        TurnBasedMatchLoadedCallback(d<LoadMatchResult> resultHolder, DataHolder dataHolder) {
            super(resultHolder, dataHolder);
        }

        protected void k(d<LoadMatchResult> dVar) {
            dVar.b(this);
        }
    }

    final class TurnBasedMatchUpdatedCallback extends TurnBasedMatchCallback<d<UpdateMatchResult>> implements UpdateMatchResult {
        TurnBasedMatchUpdatedCallback(d<UpdateMatchResult> resultHolder, DataHolder dataHolder) {
            super(resultHolder, dataHolder);
        }

        protected void k(d<UpdateMatchResult> dVar) {
            dVar.b(this);
        }
    }

    public GamesClientImpl(Context context, Looper looper, String gamePackageName, String accountName, ConnectionCallbacks connectedListener, OnConnectionFailedListener connectionFailedListener, String[] scopes, int gravity, View gamesContentView, boolean isHeadless, boolean showConnectingPopup, int connectingPopupGravity, boolean retryingSignIn, int sdkVariant) {
        super(context, looper, connectedListener, connectionFailedListener, scopes);
        this.Iz = false;
        this.IA = false;
        this.Iu = gamePackageName;
        this.wG = (String) fq.f(accountName);
        this.IC = new Binder();
        this.Iv = new HashMap();
        this.Iy = PopupManager.a(this, gravity);
        f(gamesContentView);
        this.IA = showConnectingPopup;
        this.IB = connectingPopupGravity;
        this.IE = (long) hashCode();
        this.IF = isHeadless;
        this.IH = retryingSignIn;
        this.IG = sdkVariant;
        registerConnectionCallbacks(this);
        registerConnectionFailedListener(this);
    }

    private Room G(DataHolder dataHolder) {
        RoomBuffer roomBuffer = new RoomBuffer(dataHolder);
        Room room = null;
        if (roomBuffer.getCount() > 0) {
            room = (Room) ((Room) roomBuffer.get(0)).freeze();
        }
        roomBuffer.close();
        return room;
    }

    private RealTimeSocket aC(String str) {
        RealTimeSocket realTimeSocket = null;
        try {
            ParcelFileDescriptor aJ = ((IGamesService) eM()).aJ(str);
            RealTimeSocket libjingleNativeSocket;
            if (aJ != null) {
                GamesLog.f("GamesClientImpl", "Created native libjingle socket.");
                libjingleNativeSocket = new LibjingleNativeSocket(aJ);
                this.Iv.put(str, libjingleNativeSocket);
                return libjingleNativeSocket;
            } else {
                GamesLog.f("GamesClientImpl", "Unable to create native libjingle socket, resorting to old socket.");
                String aE = ((IGamesService) eM()).aE(str);
                if (aE == null) {
                    return realTimeSocket;
                }
                LocalSocket localSocket = new LocalSocket();
                try {
                    localSocket.connect(new LocalSocketAddress(aE));
                    libjingleNativeSocket = new RealTimeSocketImpl(localSocket, str);
                    this.Iv.put(str, libjingleNativeSocket);
                    return libjingleNativeSocket;
                } catch (IOException e) {
                    GamesLog.h("GamesClientImpl", "connect() call failed on socket: " + e.getMessage());
                    return realTimeSocket;
                }
            }
        } catch (RemoteException e2) {
            GamesLog.h("GamesClientImpl", "Unable to create socket. Service died.");
            return realTimeSocket;
        }
    }

    private void gE() {
        Iterator it = this.Iv.values().iterator();
        while (it.hasNext()) {
            try {
                ((RealTimeSocket) it.next()).close();
            } catch (IOException e) {
                GamesLog.a("GamesClientImpl", "IOException:", e);
            }
        }
        this.Iv.clear();
    }

    private void gk() {
        this.Iw = null;
    }

    protected IGamesService L(IBinder iBinder) {
        return Stub.N(iBinder);
    }

    public int a(ReliableMessageSentCallback reliableMessageSentCallback, byte[] bArr, String str, String str2) {
        try {
            return ((IGamesService) eM()).a(new RealTimeReliableMessageBinderCallbacks(reliableMessageSentCallback), bArr, str, str2);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
            return -1;
        }
    }

    public int a(byte[] bArr, String str, String[] strArr) {
        fq.b((Object)strArr, (Object)"Participant IDs must not be null");
        try {
            return ((IGamesService) eM()).b(bArr, str, strArr);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
            return -1;
        }
    }

    public Intent a(int i, int i2, boolean z) {
        try {
            return ((IGamesService) eM()).a(i, i2, z);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
            return null;
        }
    }

    public Intent a(int i, byte[] bArr, int i2, Object obj, String str) {
        try {
            Intent a = ((IGamesService) eM()).a(i, bArr, i2, str);
            fq.b(obj, (Object)"Must provide a non null icon");
            a.putExtra("com.google.android.gms.games.REQUEST_ITEM_ICON", obj);
            return a;
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
            return null;
        }
    }

    public Intent a(Room room, int i) {
        try {
            return ((IGamesService) eM()).a((RoomEntity) room.freeze(), i);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
            return null;
        }
    }

    protected void a(int i, IBinder iBinder, Bundle bundle) {
        if (i == 0 && bundle != null) {
            this.Iz = bundle.getBoolean("show_welcome_popup");
        }
        super.a(i, iBinder, bundle);
    }

    public void a(IBinder iBinder, Bundle bundle) {
        if (isConnected()) {
            try {
                ((IGamesService) eM()).a(iBinder, bundle);
            } catch (RemoteException e) {
                GamesLog.g("GamesClientImpl", "service died");
            }
        }
    }

    public void a(d<LoadRequestsResult> dVar, int i, int i2, int i3) {
        try {
            ((IGamesService) eM()).a(new RequestsLoadedBinderCallbacks(dVar), i, i2, i3);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void a(d<LoadExtendedGamesResult> dVar, int i, int i2, boolean z, boolean z2) {
        try {
            ((IGamesService) eM()).a(new ExtendedGamesLoadedBinderCallback(dVar), i, i2, z, z2);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void a(d<LoadPlayersResult> dVar, int i, boolean z, boolean z2) {
        try {
            ((IGamesService) eM()).a(new PlayersLoadedBinderCallback(dVar), i, z, z2);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void a(d<LoadMatchesResult> dVar, int i, int[] iArr) {
        try {
            ((IGamesService) eM()).a(new TurnBasedMatchesLoadedBinderCallbacks(dVar), i, iArr);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void a(d<LoadScoresResult> dVar, LeaderboardScoreBuffer leaderboardScoreBuffer, int i, int i2) {
        try {
            ((IGamesService) eM()).a(new LeaderboardScoresLoadedBinderCallback(dVar), leaderboardScoreBuffer.hD().hE(), i, i2);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void a(d<InitiateMatchResult> dVar, TurnBasedMatchConfig turnBasedMatchConfig) {
        try {
            ((IGamesService) eM()).a(new TurnBasedMatchInitiatedBinderCallbacks(dVar), turnBasedMatchConfig.getVariant(), turnBasedMatchConfig.getMinPlayers(), turnBasedMatchConfig.getInvitedPlayerIds(), turnBasedMatchConfig.getAutoMatchCriteria());
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void a(d<LoadPlayersResult> dVar, String str) {
        try {
            ((IGamesService) eM()).a(new PlayersLoadedBinderCallback(dVar), str);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void a(com.google.android.gms.common.api.a.d<com.google.android.gms.games.achievement.Achievements.UpdateAchievementResult> r7, java.lang.String r8, int r9) {
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.games.internal.GamesClientImpl.a(com.google.android.gms.common.api.a$d, java.lang.String, int):void");
        /*
        r6 = this;
        if (r7 != 0) goto L_0x001b;
    L_0x0002:
        r1 = 0;
    L_0x0003:
        r0 = r6.eM();	 Catch:{ RemoteException -> 0x0021 }
        r0 = (com.google.android.gms.games.internal.IGamesService) r0;	 Catch:{ RemoteException -> 0x0021 }
        r2 = r6.Iy;	 Catch:{ RemoteException -> 0x0021 }
        r4 = r2.gU();	 Catch:{ RemoteException -> 0x0021 }
        r2 = r6.Iy;	 Catch:{ RemoteException -> 0x0021 }
        r5 = r2.gT();	 Catch:{ RemoteException -> 0x0021 }
        r2 = r8;
        r3 = r9;
        r0.a(r1, r2, r3, r4, r5);	 Catch:{ RemoteException -> 0x0021 }
    L_0x001a:
        return;
    L_0x001b:
        r1 = new com.google.android.gms.games.internal.GamesClientImpl$AchievementUpdatedBinderCallback;	 Catch:{ RemoteException -> 0x0021 }
        r1.<init>(r7);	 Catch:{ RemoteException -> 0x0021 }
        goto L_0x0003;
    L_0x0021:
        r0 = move-exception;
        r0 = "GamesClientImpl";
        r1 = "service died";
        com.google.android.gms.games.internal.GamesLog.g(r0, r1);
        goto L_0x001a;
        */
    }

    public void a(d<LoadScoresResult> dVar, String str, int i, int i2, int i3, boolean z) {
        try {
            ((IGamesService) eM()).a(new LeaderboardScoresLoadedBinderCallback(dVar), str, i, i2, i3, z);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void a(d<LoadPlayersResult> dVar, String str, int i, boolean z) {
        try {
            ((IGamesService) eM()).a(new PlayersLoadedBinderCallback(dVar), str, i, z);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void a(d<LoadPlayersResult> dVar, String str, int i, boolean z, boolean z2) {
        if (str.equals("playedWith")) {
            try {
                ((IGamesService) eM()).d(new PlayersLoadedBinderCallback(dVar), str, i, z, z2);
            } catch (RemoteException e) {
                GamesLog.g("GamesClientImpl", "service died");
            }
        } else {
            throw new IllegalArgumentException("Invalid player collection: " + str);
        }
    }

    public void a(d<LoadExtendedGamesResult> dVar, String str, int i, boolean z, boolean z2, boolean z3, boolean z4) {
        try {
            ((IGamesService) eM()).a(new ExtendedGamesLoadedBinderCallback(dVar), str, i, z, z2, z3, z4);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void a(d<LoadMatchesResult> dVar, String str, int i, int[] iArr) {
        try {
            ((IGamesService) eM()).a(new TurnBasedMatchesLoadedBinderCallbacks(dVar), str, i, iArr);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void a(com.google.android.gms.common.api.a.d<com.google.android.gms.games.leaderboard.Leaderboards.SubmitScoreResult> r7, java.lang.String r8, long r9, java.lang.String r11) {
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.games.internal.GamesClientImpl.a(com.google.android.gms.common.api.a$d, java.lang.String, long, java.lang.String):void");
        /*
        r6 = this;
        if (r7 != 0) goto L_0x0010;
    L_0x0002:
        r1 = 0;
    L_0x0003:
        r0 = r6.eM();	 Catch:{ RemoteException -> 0x0016 }
        r0 = (com.google.android.gms.games.internal.IGamesService) r0;	 Catch:{ RemoteException -> 0x0016 }
        r2 = r8;
        r3 = r9;
        r5 = r11;
        r0.a(r1, r2, r3, r5);	 Catch:{ RemoteException -> 0x0016 }
    L_0x000f:
        return;
    L_0x0010:
        r1 = new com.google.android.gms.games.internal.GamesClientImpl$SubmitScoreBinderCallbacks;	 Catch:{ RemoteException -> 0x0016 }
        r1.<init>(r7);	 Catch:{ RemoteException -> 0x0016 }
        goto L_0x0003;
    L_0x0016:
        r0 = move-exception;
        r0 = "GamesClientImpl";
        r1 = "service died";
        com.google.android.gms.games.internal.GamesLog.g(r0, r1);
        goto L_0x000f;
        */
    }

    public void a(d<LeaveMatchResult> dVar, String str, String str2) {
        try {
            ((IGamesService) eM()).c(new TurnBasedMatchLeftBinderCallbacks(dVar), str, str2);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void a(d<LoadPlayerScoreResult> dVar, String str, String str2, int i, int i2) {
        try {
            ((IGamesService) eM()).a(new PlayerLeaderboardScoreLoadedBinderCallback(dVar), str, str2, i, i2);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void a(d<LoadRequestsResult> dVar, String str, String str2, int i, int i2, int i3) {
        try {
            ((IGamesService) eM()).a(new RequestsLoadedBinderCallbacks(dVar), str, str2, i, i2, i3);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void a(d<LoadScoresResult> dVar, String str, String str2, int i, int i2, int i3, boolean z) {
        try {
            ((IGamesService) eM()).a(new LeaderboardScoresLoadedBinderCallback(dVar), str, str2, i, i2, i3, z);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void a(d<LoadPlayersResult> dVar, String str, String str2, int i, boolean z, boolean z2) {
        if (str.equals("playedWith") || str.equals("circled")) {
            try {
                ((IGamesService) eM()).a(new PlayersLoadedBinderCallback(dVar), str, str2, i, z, z2);
            } catch (RemoteException e) {
                GamesLog.g("GamesClientImpl", "service died");
            }
        } else {
            throw new IllegalArgumentException("Invalid player collection: " + str);
        }
    }

    public void a(d<LeaderboardMetadataResult> dVar, String str, String str2, boolean z) {
        try {
            ((IGamesService) eM()).b(new LeaderboardsLoadedBinderCallback(dVar), str, str2, z);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void a(d<UpdateRequestsResult> dVar, String str, String str2, String[] strArr) {
        try {
            ((IGamesService) eM()).a(new RequestsUpdatedBinderCallbacks(dVar), str, str2, strArr);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void a(d<LeaderboardMetadataResult> dVar, String str, boolean z) {
        try {
            ((IGamesService) eM()).c(new LeaderboardsLoadedBinderCallback(dVar), str, z);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void a(d<UpdateMatchResult> dVar, String str, byte[] bArr, String str2, ParticipantResult[] participantResultArr) {
        try {
            ((IGamesService) eM()).a(new TurnBasedMatchUpdatedBinderCallbacks(dVar), str, bArr, str2, participantResultArr);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void a(d<UpdateMatchResult> dVar, String str, byte[] bArr, ParticipantResult[] participantResultArr) {
        try {
            ((IGamesService) eM()).a(new TurnBasedMatchUpdatedBinderCallbacks(dVar), str, bArr, participantResultArr);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void a(d<SendRequestResult> dVar, String str, String[] strArr, int i, byte[] bArr, int i2) {
        try {
            ((IGamesService) eM()).a(new RequestSentBinderCallbacks(dVar), str, strArr, i, bArr, i2);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void a(d<LoadPlayersResult> dVar, boolean z) {
        try {
            ((IGamesService) eM()).c(new PlayersLoadedBinderCallback(dVar), z);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void a(d<Status> dVar, boolean z, Bundle bundle) {
        try {
            ((IGamesService) eM()).a(new ContactSettingsUpdatedBinderCallback(dVar), z, bundle);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void a(d<LoadPlayersResult> dVar, String[] strArr) {
        try {
            ((IGamesService) eM()).c(new PlayersLoadedBinderCallback(dVar), strArr);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void a(OnInvitationReceivedListener onInvitationReceivedListener) {
        try {
            ((IGamesService) eM()).a(new InvitationReceivedBinderCallback(onInvitationReceivedListener), this.IE);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void a(RoomConfig roomConfig) {
        try {
            ((IGamesService) eM()).a(new RoomBinderCallbacks(roomConfig.getRoomUpdateListener(), roomConfig.getRoomStatusUpdateListener(), roomConfig.getMessageReceivedListener()), this.IC, roomConfig.getVariant(), roomConfig.getInvitedPlayerIds(), roomConfig.getAutoMatchCriteria(), roomConfig.isSocketEnabled(), this.IE);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void a(RoomUpdateListener roomUpdateListener, String str) {
        try {
            ((IGamesService) eM()).c(new RoomBinderCallbacks(roomUpdateListener), str);
            gE();
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void a(OnTurnBasedMatchUpdateReceivedListener onTurnBasedMatchUpdateReceivedListener) {
        try {
            ((IGamesService) eM()).b(new MatchUpdateReceivedBinderCallback(onTurnBasedMatchUpdateReceivedListener), this.IE);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void a(OnRequestReceivedListener onRequestReceivedListener) {
        try {
            ((IGamesService) eM()).c(new RequestReceivedBinderCallback(onRequestReceivedListener), this.IE);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    protected void a(fm fmVar, e eVar) throws RemoteException {
        String toString = getContext().getResources().getConfiguration().locale.toString();
        Bundle bundle = new Bundle();
        bundle.putBoolean("com.google.android.gms.games.key.isHeadless", this.IF);
        bundle.putBoolean("com.google.android.gms.games.key.showConnectingPopup", this.IA);
        bundle.putInt("com.google.android.gms.games.key.connectingPopupGravity", this.IB);
        bundle.putBoolean("com.google.android.gms.games.key.retryingSignIn", this.IH);
        bundle.putInt("com.google.android.gms.games.key.sdkVariant", this.IG);
        fmVar.a(eVar, GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE, getContext().getPackageName(), this.wG, eL(), this.Iu, this.Iy.gU(), toString, bundle);
    }

    public Intent aA(String str) {
        try {
            return ((IGamesService) eM()).aA(str);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
            return null;
        }
    }

    public void aB(String str) {
        try {
            ((IGamesService) eM()).aI(str);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void aX(int i) {
        this.Iy.setGravity(i);
    }

    public void aY(int i) {
        try {
            ((IGamesService) eM()).aY(i);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public Intent b(int i, int i2, boolean z) {
        try {
            return ((IGamesService) eM()).b(i, i2, z);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
            return null;
        }
    }

    public void b(d<Status> dVar) {
        try {
            ((IGamesService) eM()).a(new SignOutCompleteBinderCallbacks(dVar));
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void b(d<LoadPlayersResult> dVar, int i, boolean z, boolean z2) {
        try {
            ((IGamesService) eM()).b(new PlayersLoadedBinderCallback(dVar), i, z, z2);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void b(com.google.android.gms.common.api.a.d<com.google.android.gms.games.achievement.Achievements.UpdateAchievementResult> r5, java.lang.String r6) {
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.games.internal.GamesClientImpl.b(com.google.android.gms.common.api.a$d, java.lang.String):void");
        /*
        r4 = this;
        if (r5 != 0) goto L_0x001a;
    L_0x0002:
        r0 = 0;
        r1 = r0;
    L_0x0004:
        r0 = r4.eM();	 Catch:{ RemoteException -> 0x0021 }
        r0 = (com.google.android.gms.games.internal.IGamesService) r0;	 Catch:{ RemoteException -> 0x0021 }
        r2 = r4.Iy;	 Catch:{ RemoteException -> 0x0021 }
        r2 = r2.gU();	 Catch:{ RemoteException -> 0x0021 }
        r3 = r4.Iy;	 Catch:{ RemoteException -> 0x0021 }
        r3 = r3.gT();	 Catch:{ RemoteException -> 0x0021 }
        r0.a(r1, r6, r2, r3);	 Catch:{ RemoteException -> 0x0021 }
    L_0x0019:
        return;
    L_0x001a:
        r0 = new com.google.android.gms.games.internal.GamesClientImpl$AchievementUpdatedBinderCallback;	 Catch:{ RemoteException -> 0x0021 }
        r0.<init>(r5);	 Catch:{ RemoteException -> 0x0021 }
        r1 = r0;
        goto L_0x0004;
    L_0x0021:
        r0 = move-exception;
        r0 = "GamesClientImpl";
        r1 = "service died";
        com.google.android.gms.games.internal.GamesLog.g(r0, r1);
        goto L_0x0019;
        */
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void b(com.google.android.gms.common.api.a.d<com.google.android.gms.games.achievement.Achievements.UpdateAchievementResult> r7, java.lang.String r8, int r9) {
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.games.internal.GamesClientImpl.b(com.google.android.gms.common.api.a$d, java.lang.String, int):void");
        /*
        r6 = this;
        if (r7 != 0) goto L_0x001b;
    L_0x0002:
        r1 = 0;
    L_0x0003:
        r0 = r6.eM();	 Catch:{ RemoteException -> 0x0021 }
        r0 = (com.google.android.gms.games.internal.IGamesService) r0;	 Catch:{ RemoteException -> 0x0021 }
        r2 = r6.Iy;	 Catch:{ RemoteException -> 0x0021 }
        r4 = r2.gU();	 Catch:{ RemoteException -> 0x0021 }
        r2 = r6.Iy;	 Catch:{ RemoteException -> 0x0021 }
        r5 = r2.gT();	 Catch:{ RemoteException -> 0x0021 }
        r2 = r8;
        r3 = r9;
        r0.b(r1, r2, r3, r4, r5);	 Catch:{ RemoteException -> 0x0021 }
    L_0x001a:
        return;
    L_0x001b:
        r1 = new com.google.android.gms.games.internal.GamesClientImpl$AchievementUpdatedBinderCallback;	 Catch:{ RemoteException -> 0x0021 }
        r1.<init>(r7);	 Catch:{ RemoteException -> 0x0021 }
        goto L_0x0003;
    L_0x0021:
        r0 = move-exception;
        r0 = "GamesClientImpl";
        r1 = "service died";
        com.google.android.gms.games.internal.GamesLog.g(r0, r1);
        goto L_0x001a;
        */
    }

    public void b(d<LoadScoresResult> dVar, String str, int i, int i2, int i3, boolean z) {
        try {
            ((IGamesService) eM()).b(new LeaderboardScoresLoadedBinderCallback(dVar), str, i, i2, i3, z);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void b(d<LoadExtendedGamesResult> dVar, String str, int i, boolean z, boolean z2) {
        try {
            ((IGamesService) eM()).a(new ExtendedGamesLoadedBinderCallback(dVar), str, i, z, z2);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void b(d<InitiateMatchResult> dVar, String str, String str2) {
        try {
            ((IGamesService) eM()).d(new TurnBasedMatchInitiatedBinderCallbacks(dVar), str, str2);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void b(d<LoadScoresResult> dVar, String str, String str2, int i, int i2, int i3, boolean z) {
        try {
            ((IGamesService) eM()).b(new LeaderboardScoresLoadedBinderCallback(dVar), str, str2, i, i2, i3, z);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void b(d<LoadAchievementsResult> dVar, String str, String str2, boolean z) {
        try {
            ((IGamesService) eM()).a(new AchievementsLoadedBinderCallback(dVar), str, str2, z);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void b(d<LeaderboardMetadataResult> dVar, String str, boolean z) {
        try {
            ((IGamesService) eM()).d(new LeaderboardsLoadedBinderCallback(dVar), str, z);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void b(d<LeaderboardMetadataResult> dVar, boolean z) {
        try {
            ((IGamesService) eM()).b(new LeaderboardsLoadedBinderCallback(dVar), z);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void b(d<UpdateRequestsResult> dVar, String[] strArr) {
        try {
            ((IGamesService) eM()).a(new RequestsUpdatedBinderCallbacks(dVar), strArr);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void b(RoomConfig roomConfig) {
        try {
            ((IGamesService) eM()).a(new RoomBinderCallbacks(roomConfig.getRoomUpdateListener(), roomConfig.getRoomStatusUpdateListener(), roomConfig.getMessageReceivedListener()), this.IC, roomConfig.getInvitationId(), roomConfig.isSocketEnabled(), this.IE);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    protected void b(String... strArr) {
        int i = 0;
        boolean z = false;
        boolean z2 = false;
        while (i < strArr.length) {
            String str = strArr[i];
            if (str.equals(Scopes.GAMES)) {
                z2 = true;
            } else if (str.equals("https://www.googleapis.com/auth/games.firstparty")) {
                z = true;
            }
            i++;
        }
        if (i != 0) {
            fq.a(!z2, String.format("Cannot have both %s and %s!", new Object[]{Scopes.GAMES, "https://www.googleapis.com/auth/games.firstparty"}));
        } else {
            fq.a(z2, String.format("Games APIs requires %s to function.", new Object[]{Scopes.GAMES}));
        }
    }

    protected String bg() {
        return "com.google.android.gms.games.service.START";
    }

    protected String bh() {
        return "com.google.android.gms.games.internal.IGamesService";
    }

    public void c(d<LoadInvitationsResult> dVar, int i) {
        try {
            ((IGamesService) eM()).a(new InvitationsLoadedBinderCallback(dVar), i);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void c(d<LoadPlayersResult> dVar, int i, boolean z, boolean z2) {
        try {
            ((IGamesService) eM()).c(new PlayersLoadedBinderCallback(dVar), i, z, z2);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void c(com.google.android.gms.common.api.a.d<com.google.android.gms.games.achievement.Achievements.UpdateAchievementResult> r5, java.lang.String r6) {
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.games.internal.GamesClientImpl.c(com.google.android.gms.common.api.a$d, java.lang.String):void");
        /*
        r4 = this;
        if (r5 != 0) goto L_0x001a;
    L_0x0002:
        r0 = 0;
        r1 = r0;
    L_0x0004:
        r0 = r4.eM();	 Catch:{ RemoteException -> 0x0021 }
        r0 = (com.google.android.gms.games.internal.IGamesService) r0;	 Catch:{ RemoteException -> 0x0021 }
        r2 = r4.Iy;	 Catch:{ RemoteException -> 0x0021 }
        r2 = r2.gU();	 Catch:{ RemoteException -> 0x0021 }
        r3 = r4.Iy;	 Catch:{ RemoteException -> 0x0021 }
        r3 = r3.gT();	 Catch:{ RemoteException -> 0x0021 }
        r0.b(r1, r6, r2, r3);	 Catch:{ RemoteException -> 0x0021 }
    L_0x0019:
        return;
    L_0x001a:
        r0 = new com.google.android.gms.games.internal.GamesClientImpl$AchievementUpdatedBinderCallback;	 Catch:{ RemoteException -> 0x0021 }
        r0.<init>(r5);	 Catch:{ RemoteException -> 0x0021 }
        r1 = r0;
        goto L_0x0004;
    L_0x0021:
        r0 = move-exception;
        r0 = "GamesClientImpl";
        r1 = "service died";
        com.google.android.gms.games.internal.GamesLog.g(r0, r1);
        goto L_0x0019;
        */
    }

    public void c(d<LoadInvitationsResult> dVar, String str, int i) {
        try {
            ((IGamesService) eM()).b(new InvitationsLoadedBinderCallback(dVar), str, i, false);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void c(d<LoadExtendedGamesResult> dVar, String str, int i, boolean z, boolean z2) {
        try {
            ((IGamesService) eM()).c(new ExtendedGamesLoadedBinderCallback(dVar), str, i, z, z2);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void c(d<InitiateMatchResult> dVar, String str, String str2) {
        try {
            ((IGamesService) eM()).e(new TurnBasedMatchInitiatedBinderCallbacks(dVar), str, str2);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void c(d<GameMuteStatusChangeResult> dVar, String str, boolean z) {
        try {
            ((IGamesService) eM()).a(new GameMuteStatusChangedBinderCallback(dVar), str, z);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void c(d<LoadAchievementsResult> dVar, boolean z) {
        try {
            ((IGamesService) eM()).a(new AchievementsLoadedBinderCallback(dVar), z);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void c(d<UpdateRequestsResult> dVar, String[] strArr) {
        try {
            ((IGamesService) eM()).b(new RequestsUpdatedBinderCallbacks(dVar), strArr);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void connect() {
        gk();
        super.connect();
    }

    public int d(byte[] bArr, String str) {
        try {
            return ((IGamesService) eM()).b(bArr, str, null);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
            return -1;
        }
    }

    public void d(d<LoadPlayersResult> dVar, int i, boolean z, boolean z2) {
        try {
            ((IGamesService) eM()).e(new PlayersLoadedBinderCallback(dVar), i, z, z2);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void d(d<InitiateMatchResult> dVar, String str) {
        try {
            ((IGamesService) eM()).l(new TurnBasedMatchInitiatedBinderCallbacks(dVar), str);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void d(d<LoadRequestSummariesResult> dVar, String str, int i) {
        try {
            ((IGamesService) eM()).a(new RequestSummariesLoadedBinderCallbacks(dVar), str, i);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void d(d<LoadPlayersResult> dVar, String str, int i, boolean z, boolean z2) {
        try {
            ((IGamesService) eM()).b(new PlayersLoadedBinderCallback(dVar), str, i, z, z2);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public Bundle dG() {
        try {
            Bundle dG = ((IGamesService) eM()).dG();
            if (dG == null) {
                return dG;
            }
            dG.setClassLoader(GamesClientImpl.class.getClassLoader());
            return dG;
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
            return null;
        }
    }

    public void disconnect() {
        this.Iz = false;
        if (isConnected()) {
            try {
                IGamesService iGamesService = (IGamesService) eM();
                iGamesService.gF();
                iGamesService.o(this.IE);
            } catch (RemoteException e) {
                GamesLog.g("GamesClientImpl", "Failed to notify client disconnect.");
            }
        }
        gE();
        super.disconnect();
    }

    public void e(d<LoadExtendedPlayersResult> dVar, int i, boolean z, boolean z2) {
        try {
            ((IGamesService) eM()).d(new ExtendedPlayersLoadedBinderCallback(dVar), i, z, z2);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void e(d<InitiateMatchResult> dVar, String str) {
        try {
            ((IGamesService) eM()).m(new TurnBasedMatchInitiatedBinderCallbacks(dVar), str);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void f(View view) {
        this.Iy.g(view);
    }

    public void f(d<LeaveMatchResult> dVar, String str) {
        try {
            ((IGamesService) eM()).o(new TurnBasedMatchLeftBinderCallbacks(dVar), str);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void g(d<LoadGamesResult> dVar) {
        try {
            ((IGamesService) eM()).d(new GamesLoadedBinderCallback(dVar));
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void g(d<CancelMatchResult> dVar, String str) {
        try {
            ((IGamesService) eM()).n(new TurnBasedMatchCanceledBinderCallbacks(dVar), str);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public int gA() {
        int i = MMAdView.TRANSITION_UP;
        try {
            return ((IGamesService) eM()).gA();
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
            return i;
        }
    }

    public Intent gB() {
        try {
            return ((IGamesService) eM()).gB();
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
            return null;
        }
    }

    public int gC() {
        int i = MMAdView.TRANSITION_UP;
        try {
            return ((IGamesService) eM()).gC();
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
            return i;
        }
    }

    public int gD() {
        int i = MMAdView.TRANSITION_UP;
        try {
            return ((IGamesService) eM()).gD();
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
            return i;
        }
    }

    public void gF() {
        if (isConnected()) {
            try {
                ((IGamesService) eM()).gF();
            } catch (RemoteException e) {
                GamesLog.g("GamesClientImpl", "service died");
            }
        }
    }

    public String gl() {
        try {
            return ((IGamesService) eM()).gl();
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
            return null;
        }
    }

    public String gm() {
        try {
            return ((IGamesService) eM()).gm();
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
            return null;
        }
    }

    public Player gn() {
        bT();
        synchronized (this) {
            if (this.Iw == null) {
                try {
                    PlayerBuffer playerBuffer = new PlayerBuffer(((IGamesService) eM()).gG());
                    if (playerBuffer.getCount() > 0) {
                        this.Iw = (PlayerEntity) playerBuffer.get(0).freeze();
                    }
                    playerBuffer.close();
                } catch (RemoteException e) {
                    GamesLog.g("GamesClientImpl", "service died");
                }
            }
        }
        return this.Iw;
    }

    public Game go() {
        bT();
        synchronized (this) {
            if (this.Ix == null) {
                try {
                    GameBuffer gameBuffer = new GameBuffer(((IGamesService) eM()).gI());
                    if (gameBuffer.getCount() > 0) {
                        this.Ix = (GameEntity) gameBuffer.get(0).freeze();
                    }
                    gameBuffer.close();
                } catch (RemoteException e) {
                    GamesLog.g("GamesClientImpl", "service died");
                }
            }
        }
        return this.Ix;
    }

    public Intent gp() {
        try {
            return ((IGamesService) eM()).gp();
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
            return null;
        }
    }

    public Intent gq() {
        try {
            return ((IGamesService) eM()).gq();
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
            return null;
        }
    }

    public Intent gr() {
        try {
            return ((IGamesService) eM()).gr();
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
            return null;
        }
    }

    public Intent gs() {
        try {
            return ((IGamesService) eM()).gs();
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
            return null;
        }
    }

    public void gt() {
        try {
            ((IGamesService) eM()).p(this.IE);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void gu() {
        try {
            ((IGamesService) eM()).q(this.IE);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void gv() {
        try {
            ((IGamesService) eM()).r(this.IE);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public Intent gw() {
        try {
            return ((IGamesService) eM()).gw();
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
            return null;
        }
    }

    public Intent gx() {
        try {
            return ((IGamesService) eM()).gx();
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
            return null;
        }
    }

    public int gy() {
        int i = 4368;
        try {
            return ((IGamesService) eM()).gy();
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
            return i;
        }
    }

    public String gz() {
        try {
            return ((IGamesService) eM()).gz();
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
            return null;
        }
    }

    public void h(d<LoadOwnerCoverPhotoUrisResult> dVar) {
        try {
            ((IGamesService) eM()).j(new OwnerCoverPhotoUrisLoadedBinderCallback(dVar));
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void h(d<LoadMatchResult> dVar, String str) {
        try {
            ((IGamesService) eM()).p(new TurnBasedMatchLoadedBinderCallbacks(dVar), str);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public RealTimeSocket i(String str, String str2) {
        if (str2 == null || !ParticipantUtils.aV(str2)) {
            throw new IllegalArgumentException("Bad participant ID");
        }
        RealTimeSocket realTimeSocket = (RealTimeSocket) this.Iv.get(str2);
        return (realTimeSocket == null || realTimeSocket.isClosed()) ? aC(str2) : realTimeSocket;
    }

    public void i(d<LoadAclResult> dVar) {
        try {
            ((IGamesService) eM()).h(new NotifyAclLoadedBinderCallback(dVar));
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void i(d<LoadExtendedGamesResult> dVar, String str) {
        try {
            ((IGamesService) eM()).e(new ExtendedGamesLoadedBinderCallback(dVar), str);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void j(d<ContactSettingLoadResult> dVar) {
        try {
            ((IGamesService) eM()).i(new ContactSettingsLoadedBinderCallback(dVar));
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void j(d<LoadGameInstancesResult> dVar, String str) {
        try {
            ((IGamesService) eM()).f(new GameInstancesLoadedBinderCallback(dVar), str);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void k(d<LoadGameSearchSuggestionsResult> dVar, String str) {
        try {
            ((IGamesService) eM()).q(new GameSearchSuggestionsLoadedBinderCallback(dVar), str);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void l(d<LoadInvitationsResult> dVar, String str) {
        try {
            ((IGamesService) eM()).k(new InvitationsLoadedBinderCallback(dVar), str);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void l(String str, int i) {
        try {
            ((IGamesService) eM()).l(str, i);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void m(d<Status> dVar, String str) {
        try {
            ((IGamesService) eM()).j(new NotifyAclUpdatedBinderCallback(dVar), str);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void m(String str, int i) {
        try {
            ((IGamesService) eM()).m(str, i);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void n(d<GameMuteStatusLoadResult> dVar, String str) {
        try {
            ((IGamesService) eM()).i(new GameMuteStatusLoadedBinderCallback(dVar), str);
        } catch (RemoteException e) {
            GamesLog.g("GamesClientImpl", "service died");
        }
    }

    public void onConnected(Bundle connectionHint) {
        if (this.Iz) {
            this.Iy.gS();
            this.Iz = false;
        }
    }

    public void onConnectionFailed(ConnectionResult result) {
        this.Iz = false;
    }

    public void onConnectionSuspended(int cause) {
    }

    protected /* synthetic */ IInterface r(IBinder iBinder) {
        return L(iBinder);
    }
}