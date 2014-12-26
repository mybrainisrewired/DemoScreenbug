package com.google.android.gms.games.multiplayer;

import android.os.Parcel;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.common.data.b;
import com.google.android.gms.games.Game;
import com.google.android.gms.games.GameRef;
import com.google.android.gms.internal.fq;
import com.inmobi.commons.analytics.db.AnalyticsSQLiteHelper;
import java.util.ArrayList;

public final class InvitationRef extends b implements Invitation {
    private final Game LS;
    private final ParticipantRef MA;
    private final ArrayList<Participant> Mx;

    InvitationRef(DataHolder holder, int dataRow, int numChildren) {
        super(holder, dataRow);
        this.LS = new GameRef(holder, dataRow);
        this.Mx = new ArrayList(numChildren);
        String string = getString("external_inviter_id");
        int i = 0;
        Object obj = null;
        while (i < numChildren) {
            ParticipantRef participantRef = new ParticipantRef(this.BB, this.BD + i);
            if (participantRef.getParticipantId().equals(string)) {
                ParticipantRef participantRef2 = participantRef;
            }
            this.Mx.add(participantRef);
            i++;
        }
        this.MA = (ParticipantRef) fq.b(obj, (Object)"Must have a valid inviter!");
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        return InvitationEntity.a(this, obj);
    }

    public Invitation freeze() {
        return new InvitationEntity(this);
    }

    public int getAvailableAutoMatchSlots() {
        return !getBoolean("has_automatch_criteria") ? 0 : getInteger("automatch_max_players");
    }

    public long getCreationTimestamp() {
        return Math.max(getLong("creation_timestamp"), getLong("last_modified_timestamp"));
    }

    public Game getGame() {
        return this.LS;
    }

    public String getInvitationId() {
        return getString("external_invitation_id");
    }

    public int getInvitationType() {
        return getInteger(AnalyticsSQLiteHelper.EVENT_LIST_TYPE);
    }

    public Participant getInviter() {
        return this.MA;
    }

    public ArrayList<Participant> getParticipants() {
        return this.Mx;
    }

    public int getVariant() {
        return getInteger("variant");
    }

    public int hashCode() {
        return InvitationEntity.a(this);
    }

    public String toString() {
        return InvitationEntity.b(this);
    }

    public void writeToParcel(Parcel dest, int flags) {
        ((InvitationEntity) freeze()).writeToParcel(dest, flags);
    }
}