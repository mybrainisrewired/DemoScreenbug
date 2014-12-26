package com.google.android.gms.games.multiplayer.turnbased;

import android.database.CharArrayBuffer;
import android.os.Bundle;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.games.Game;
import com.google.android.gms.games.GameEntity;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.ParticipantEntity;
import com.google.android.gms.internal.fo;
import com.google.android.gms.internal.gm;
import java.util.ArrayList;

public final class TurnBasedMatchEntity implements SafeParcelable, TurnBasedMatch {
    public static final TurnBasedMatchEntityCreator CREATOR;
    private final String HD;
    private final String Jb;
    private final GameEntity Lt;
    private final Bundle MO;
    private final String MS;
    private final long Mu;
    private final ArrayList<ParticipantEntity> Mx;
    private final int My;
    private final String Na;
    private final long Nb;
    private final String Nc;
    private final int Nd;
    private final int Ne;
    private final byte[] Nf;
    private final String Ng;
    private final byte[] Nh;
    private final int Ni;
    private final int Nj;
    private final boolean Nk;
    private final String Nl;
    private final int xH;

    static {
        CREATOR = new TurnBasedMatchEntityCreator();
    }

    TurnBasedMatchEntity(int versionCode, GameEntity game, String matchId, String creatorId, long creationTimestamp, String lastUpdaterId, long lastUpdatedTimestamp, String pendingParticipantId, int matchStatus, int variant, int version, byte[] data, ArrayList<ParticipantEntity> participants, String rematchId, byte[] previousData, int matchNumber, Bundle autoMatchCriteria, int turnStatus, boolean isLocallyModified, String description, String descriptionParticipantId) {
        this.xH = versionCode;
        this.Lt = game;
        this.Jb = matchId;
        this.MS = creatorId;
        this.Mu = creationTimestamp;
        this.Na = lastUpdaterId;
        this.Nb = lastUpdatedTimestamp;
        this.Nc = pendingParticipantId;
        this.Nd = matchStatus;
        this.Nj = turnStatus;
        this.My = variant;
        this.Ne = version;
        this.Nf = data;
        this.Mx = participants;
        this.Ng = rematchId;
        this.Nh = previousData;
        this.Ni = matchNumber;
        this.MO = autoMatchCriteria;
        this.Nk = isLocallyModified;
        this.HD = description;
        this.Nl = descriptionParticipantId;
    }

    public TurnBasedMatchEntity(TurnBasedMatch match) {
        this.xH = 2;
        this.Lt = new GameEntity(match.getGame());
        this.Jb = match.getMatchId();
        this.MS = match.getCreatorId();
        this.Mu = match.getCreationTimestamp();
        this.Na = match.getLastUpdaterId();
        this.Nb = match.getLastUpdatedTimestamp();
        this.Nc = match.getPendingParticipantId();
        this.Nd = match.getStatus();
        this.Nj = match.getTurnStatus();
        this.My = match.getVariant();
        this.Ne = match.getVersion();
        this.Ng = match.getRematchId();
        this.Ni = match.getMatchNumber();
        this.MO = match.getAutoMatchCriteria();
        this.Nk = match.isLocallyModified();
        this.HD = match.getDescription();
        this.Nl = match.getDescriptionParticipantId();
        Object data = match.getData();
        if (data == null) {
            this.Nf = null;
        } else {
            this.Nf = new byte[data.length];
            System.arraycopy(data, 0, this.Nf, 0, data.length);
        }
        data = match.getPreviousMatchData();
        if (data == null) {
            this.Nh = null;
        } else {
            this.Nh = new byte[data.length];
            System.arraycopy(data, 0, this.Nh, 0, data.length);
        }
        ArrayList participants = match.getParticipants();
        int size = participants.size();
        this.Mx = new ArrayList(size);
        int i = 0;
        while (i < size) {
            this.Mx.add((ParticipantEntity) ((Participant) participants.get(i)).freeze());
            i++;
        }
    }

    static int a(TurnBasedMatch turnBasedMatch) {
        return fo.hashCode(new Object[]{turnBasedMatch.getGame(), turnBasedMatch.getMatchId(), turnBasedMatch.getCreatorId(), Long.valueOf(turnBasedMatch.getCreationTimestamp()), turnBasedMatch.getLastUpdaterId(), Long.valueOf(turnBasedMatch.getLastUpdatedTimestamp()), turnBasedMatch.getPendingParticipantId(), Integer.valueOf(turnBasedMatch.getStatus()), Integer.valueOf(turnBasedMatch.getTurnStatus()), turnBasedMatch.getDescription(), Integer.valueOf(turnBasedMatch.getVariant()), Integer.valueOf(turnBasedMatch.getVersion()), turnBasedMatch.getParticipants(), turnBasedMatch.getRematchId(), Integer.valueOf(turnBasedMatch.getMatchNumber()), turnBasedMatch.getAutoMatchCriteria(), Integer.valueOf(turnBasedMatch.getAvailableAutoMatchSlots()), Boolean.valueOf(turnBasedMatch.isLocallyModified())});
    }

    static int a(TurnBasedMatch turnBasedMatch, String str) {
        ArrayList participants = turnBasedMatch.getParticipants();
        int size = participants.size();
        int i = 0;
        while (i < size) {
            Participant participant = (Participant) participants.get(i);
            if (participant.getParticipantId().equals(str)) {
                return participant.getStatus();
            }
            i++;
        }
        throw new IllegalStateException("Participant " + str + " is not in match " + turnBasedMatch.getMatchId());
    }

    static boolean a(TurnBasedMatch turnBasedMatch, TurnBasedMatch turnBasedMatch2) {
        if (!(turnBasedMatch2 instanceof TurnBasedMatch)) {
            return false;
        }
        if (turnBasedMatch == turnBasedMatch2) {
            return true;
        }
        turnBasedMatch2 = turnBasedMatch2;
        return fo.equal(turnBasedMatch2.getGame(), turnBasedMatch.getGame()) && fo.equal(turnBasedMatch2.getMatchId(), turnBasedMatch.getMatchId()) && fo.equal(turnBasedMatch2.getCreatorId(), turnBasedMatch.getCreatorId()) && fo.equal(Long.valueOf(turnBasedMatch2.getCreationTimestamp()), Long.valueOf(turnBasedMatch.getCreationTimestamp())) && fo.equal(turnBasedMatch2.getLastUpdaterId(), turnBasedMatch.getLastUpdaterId()) && fo.equal(Long.valueOf(turnBasedMatch2.getLastUpdatedTimestamp()), Long.valueOf(turnBasedMatch.getLastUpdatedTimestamp())) && fo.equal(turnBasedMatch2.getPendingParticipantId(), turnBasedMatch.getPendingParticipantId()) && fo.equal(Integer.valueOf(turnBasedMatch2.getStatus()), Integer.valueOf(turnBasedMatch.getStatus())) && fo.equal(Integer.valueOf(turnBasedMatch2.getTurnStatus()), Integer.valueOf(turnBasedMatch.getTurnStatus())) && fo.equal(turnBasedMatch2.getDescription(), turnBasedMatch.getDescription()) && fo.equal(Integer.valueOf(turnBasedMatch2.getVariant()), Integer.valueOf(turnBasedMatch.getVariant())) && fo.equal(Integer.valueOf(turnBasedMatch2.getVersion()), Integer.valueOf(turnBasedMatch.getVersion())) && fo.equal(turnBasedMatch2.getParticipants(), turnBasedMatch.getParticipants()) && fo.equal(turnBasedMatch2.getRematchId(), turnBasedMatch.getRematchId()) && fo.equal(Integer.valueOf(turnBasedMatch2.getMatchNumber()), Integer.valueOf(turnBasedMatch.getMatchNumber())) && fo.equal(turnBasedMatch2.getAutoMatchCriteria(), turnBasedMatch.getAutoMatchCriteria()) && fo.equal(Integer.valueOf(turnBasedMatch2.getAvailableAutoMatchSlots()), Integer.valueOf(turnBasedMatch.getAvailableAutoMatchSlots())) && fo.equal(Boolean.valueOf(turnBasedMatch2.isLocallyModified()), Boolean.valueOf(turnBasedMatch.isLocallyModified()));
    }

    static String b(TurnBasedMatch turnBasedMatch) {
        return fo.e(turnBasedMatch).a("Game", turnBasedMatch.getGame()).a("MatchId", turnBasedMatch.getMatchId()).a("CreatorId", turnBasedMatch.getCreatorId()).a("CreationTimestamp", Long.valueOf(turnBasedMatch.getCreationTimestamp())).a("LastUpdaterId", turnBasedMatch.getLastUpdaterId()).a("LastUpdatedTimestamp", Long.valueOf(turnBasedMatch.getLastUpdatedTimestamp())).a("PendingParticipantId", turnBasedMatch.getPendingParticipantId()).a("MatchStatus", Integer.valueOf(turnBasedMatch.getStatus())).a("TurnStatus", Integer.valueOf(turnBasedMatch.getTurnStatus())).a("Description", turnBasedMatch.getDescription()).a("Variant", Integer.valueOf(turnBasedMatch.getVariant())).a("Data", turnBasedMatch.getData()).a("Version", Integer.valueOf(turnBasedMatch.getVersion())).a("Participants", turnBasedMatch.getParticipants()).a("RematchId", turnBasedMatch.getRematchId()).a("PreviousData", turnBasedMatch.getPreviousMatchData()).a("MatchNumber", Integer.valueOf(turnBasedMatch.getMatchNumber())).a("AutoMatchCriteria", turnBasedMatch.getAutoMatchCriteria()).a("AvailableAutoMatchSlots", Integer.valueOf(turnBasedMatch.getAvailableAutoMatchSlots())).a("LocallyModified", Boolean.valueOf(turnBasedMatch.isLocallyModified())).a("DescriptionParticipantId", turnBasedMatch.getDescriptionParticipantId()).toString();
    }

    static String b(TurnBasedMatch turnBasedMatch, String str) {
        ArrayList participants = turnBasedMatch.getParticipants();
        int size = participants.size();
        int i = 0;
        while (i < size) {
            Participant participant = (Participant) participants.get(i);
            Player player = participant.getPlayer();
            if (player != null && player.getPlayerId().equals(str)) {
                return participant.getParticipantId();
            }
            i++;
        }
        return null;
    }

    static Participant c(TurnBasedMatch turnBasedMatch, String str) {
        ArrayList participants = turnBasedMatch.getParticipants();
        int size = participants.size();
        int i = 0;
        while (i < size) {
            Participant participant = (Participant) participants.get(i);
            if (participant.getParticipantId().equals(str)) {
                return participant;
            }
            i++;
        }
        throw new IllegalStateException("Participant " + str + " is not in match " + turnBasedMatch.getMatchId());
    }

    static ArrayList<String> c(TurnBasedMatch turnBasedMatch) {
        ArrayList participants = turnBasedMatch.getParticipants();
        int size = participants.size();
        ArrayList<String> arrayList = new ArrayList(size);
        int i = 0;
        while (i < size) {
            arrayList.add(((Participant) participants.get(i)).getParticipantId());
            i++;
        }
        return arrayList;
    }

    public boolean canRematch() {
        return this.Nd == 2 && this.Ng == null;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        return a(this, obj);
    }

    public TurnBasedMatch freeze() {
        return this;
    }

    public Bundle getAutoMatchCriteria() {
        return this.MO;
    }

    public int getAvailableAutoMatchSlots() {
        return this.MO == null ? 0 : this.MO.getInt(Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS);
    }

    public long getCreationTimestamp() {
        return this.Mu;
    }

    public String getCreatorId() {
        return this.MS;
    }

    public byte[] getData() {
        return this.Nf;
    }

    public String getDescription() {
        return this.HD;
    }

    public void getDescription(CharArrayBuffer dataOut) {
        gm.b(this.HD, dataOut);
    }

    public Participant getDescriptionParticipant() {
        return getParticipant(getDescriptionParticipantId());
    }

    public String getDescriptionParticipantId() {
        return this.Nl;
    }

    public Game getGame() {
        return this.Lt;
    }

    public long getLastUpdatedTimestamp() {
        return this.Nb;
    }

    public String getLastUpdaterId() {
        return this.Na;
    }

    public String getMatchId() {
        return this.Jb;
    }

    public int getMatchNumber() {
        return this.Ni;
    }

    public Participant getParticipant(String participantId) {
        return c(this, participantId);
    }

    public String getParticipantId(String playerId) {
        return b(this, playerId);
    }

    public ArrayList<String> getParticipantIds() {
        return c(this);
    }

    public int getParticipantStatus(String participantId) {
        return a(this, participantId);
    }

    public ArrayList<Participant> getParticipants() {
        return new ArrayList(this.Mx);
    }

    public String getPendingParticipantId() {
        return this.Nc;
    }

    public byte[] getPreviousMatchData() {
        return this.Nh;
    }

    public String getRematchId() {
        return this.Ng;
    }

    public int getStatus() {
        return this.Nd;
    }

    public int getTurnStatus() {
        return this.Nj;
    }

    public int getVariant() {
        return this.My;
    }

    public int getVersion() {
        return this.Ne;
    }

    public int getVersionCode() {
        return this.xH;
    }

    public int hashCode() {
        return a(this);
    }

    public boolean isDataValid() {
        return true;
    }

    public boolean isLocallyModified() {
        return this.Nk;
    }

    public String toString() {
        return b(this);
    }

    public void writeToParcel(Parcel out, int flags) {
        TurnBasedMatchEntityCreator.a(this, out, flags);
    }
}