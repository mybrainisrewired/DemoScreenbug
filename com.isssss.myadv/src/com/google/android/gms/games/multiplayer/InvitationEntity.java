package com.google.android.gms.games.multiplayer;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.games.Game;
import com.google.android.gms.games.GameEntity;
import com.google.android.gms.games.internal.GamesDowngradeableSafeParcel;
import com.google.android.gms.internal.fo;
import com.google.android.gms.internal.fq;
import java.util.ArrayList;

public final class InvitationEntity extends GamesDowngradeableSafeParcel implements Invitation {
    public static final Creator<InvitationEntity> CREATOR;
    private final String IV;
    private final GameEntity Lt;
    private final long Mu;
    private final int Mv;
    private final ParticipantEntity Mw;
    private final ArrayList<ParticipantEntity> Mx;
    private final int My;
    private final int Mz;
    private final int xH;

    static final class InvitationEntityCreatorCompat extends InvitationEntityCreator {
        InvitationEntityCreatorCompat() {
        }

        public InvitationEntity au(Parcel parcel) {
            if (InvitationEntity.c(InvitationEntity.eJ()) || InvitationEntity.al(InvitationEntity.class.getCanonicalName())) {
                return super.au(parcel);
            }
            GameEntity gameEntity = (GameEntity) GameEntity.CREATOR.createFromParcel(parcel);
            String readString = parcel.readString();
            long readLong = parcel.readLong();
            int readInt = parcel.readInt();
            ParticipantEntity participantEntity = (ParticipantEntity) ParticipantEntity.CREATOR.createFromParcel(parcel);
            int readInt2 = parcel.readInt();
            ArrayList arrayList = new ArrayList(readInt2);
            int i = 0;
            while (i < readInt2) {
                arrayList.add(ParticipantEntity.CREATOR.createFromParcel(parcel));
                i++;
            }
            return new InvitationEntity(2, gameEntity, readString, readLong, readInt, participantEntity, arrayList, -1, 0);
        }

        public /* synthetic */ Object createFromParcel(Parcel x0) {
            return au(x0);
        }
    }

    static {
        CREATOR = new InvitationEntityCreatorCompat();
    }

    InvitationEntity(int versionCode, GameEntity game, String invitationId, long creationTimestamp, int invitationType, ParticipantEntity inviter, ArrayList<ParticipantEntity> participants, int variant, int availableAutoMatchSlots) {
        this.xH = versionCode;
        this.Lt = game;
        this.IV = invitationId;
        this.Mu = creationTimestamp;
        this.Mv = invitationType;
        this.Mw = inviter;
        this.Mx = participants;
        this.My = variant;
        this.Mz = availableAutoMatchSlots;
    }

    InvitationEntity(Invitation invitation) {
        this.xH = 2;
        this.Lt = new GameEntity(invitation.getGame());
        this.IV = invitation.getInvitationId();
        this.Mu = invitation.getCreationTimestamp();
        this.Mv = invitation.getInvitationType();
        this.My = invitation.getVariant();
        this.Mz = invitation.getAvailableAutoMatchSlots();
        String participantId = invitation.getInviter().getParticipantId();
        Participant participant = null;
        ArrayList participants = invitation.getParticipants();
        int size = participants.size();
        this.Mx = new ArrayList(size);
        int i = 0;
        while (i < size) {
            Participant participant2 = (Participant) participants.get(i);
            if (participant2.getParticipantId().equals(participantId)) {
                participant = participant2;
            }
            this.Mx.add((ParticipantEntity) participant2.freeze());
            i++;
        }
        fq.b(obj, (Object)"Must have a valid inviter!");
        this.Mw = (ParticipantEntity) obj.freeze();
    }

    static int a(Invitation invitation) {
        return fo.hashCode(new Object[]{invitation.getGame(), invitation.getInvitationId(), Long.valueOf(invitation.getCreationTimestamp()), Integer.valueOf(invitation.getInvitationType()), invitation.getInviter(), invitation.getParticipants(), Integer.valueOf(invitation.getVariant()), Integer.valueOf(invitation.getAvailableAutoMatchSlots())});
    }

    static boolean a(Invitation invitation, Invitation invitation2) {
        if (!(invitation2 instanceof Invitation)) {
            return false;
        }
        if (invitation == invitation2) {
            return true;
        }
        invitation2 = invitation2;
        return fo.equal(invitation2.getGame(), invitation.getGame()) && fo.equal(invitation2.getInvitationId(), invitation.getInvitationId()) && fo.equal(Long.valueOf(invitation2.getCreationTimestamp()), Long.valueOf(invitation.getCreationTimestamp())) && fo.equal(Integer.valueOf(invitation2.getInvitationType()), Integer.valueOf(invitation.getInvitationType())) && fo.equal(invitation2.getInviter(), invitation.getInviter()) && fo.equal(invitation2.getParticipants(), invitation.getParticipants()) && fo.equal(Integer.valueOf(invitation2.getVariant()), Integer.valueOf(invitation.getVariant())) && fo.equal(Integer.valueOf(invitation2.getAvailableAutoMatchSlots()), Integer.valueOf(invitation.getAvailableAutoMatchSlots()));
    }

    static String b(Invitation invitation) {
        return fo.e(invitation).a("Game", invitation.getGame()).a("InvitationId", invitation.getInvitationId()).a("CreationTimestamp", Long.valueOf(invitation.getCreationTimestamp())).a("InvitationType", Integer.valueOf(invitation.getInvitationType())).a("Inviter", invitation.getInviter()).a("Participants", invitation.getParticipants()).a("Variant", Integer.valueOf(invitation.getVariant())).a("AvailableAutoMatchSlots", Integer.valueOf(invitation.getAvailableAutoMatchSlots())).toString();
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        return a(this, obj);
    }

    public Invitation freeze() {
        return this;
    }

    public int getAvailableAutoMatchSlots() {
        return this.Mz;
    }

    public long getCreationTimestamp() {
        return this.Mu;
    }

    public Game getGame() {
        return this.Lt;
    }

    public String getInvitationId() {
        return this.IV;
    }

    public int getInvitationType() {
        return this.Mv;
    }

    public Participant getInviter() {
        return this.Mw;
    }

    public ArrayList<Participant> getParticipants() {
        return new ArrayList(this.Mx);
    }

    public int getVariant() {
        return this.My;
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

    public String toString() {
        return b(this);
    }

    public void writeToParcel(Parcel dest, int flags) {
        if (eK()) {
            this.Lt.writeToParcel(dest, flags);
            dest.writeString(this.IV);
            dest.writeLong(this.Mu);
            dest.writeInt(this.Mv);
            this.Mw.writeToParcel(dest, flags);
            int size = this.Mx.size();
            dest.writeInt(size);
            int i = 0;
            while (i < size) {
                ((ParticipantEntity) this.Mx.get(i)).writeToParcel(dest, flags);
                i++;
            }
        } else {
            InvitationEntityCreator.a(this, dest, flags);
        }
    }
}