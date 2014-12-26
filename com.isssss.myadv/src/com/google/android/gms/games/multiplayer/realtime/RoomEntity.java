package com.google.android.gms.games.multiplayer.realtime;

import android.database.CharArrayBuffer;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.internal.GamesDowngradeableSafeParcel;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.ParticipantEntity;
import com.google.android.gms.internal.fo;
import com.google.android.gms.internal.gm;
import java.util.ArrayList;

public final class RoomEntity extends GamesDowngradeableSafeParcel implements Room {
    public static final Creator<RoomEntity> CREATOR;
    private final String HD;
    private final String Ja;
    private final Bundle MO;
    private final String MS;
    private final int MT;
    private final int MU;
    private final long Mu;
    private final ArrayList<ParticipantEntity> Mx;
    private final int My;
    private final int xH;

    static final class RoomEntityCreatorCompat extends RoomEntityCreator {
        RoomEntityCreatorCompat() {
        }

        public RoomEntity ax(Parcel parcel) {
            if (RoomEntity.c(RoomEntity.eJ()) || RoomEntity.al(RoomEntity.class.getCanonicalName())) {
                return super.ax(parcel);
            }
            String readString = parcel.readString();
            String readString2 = parcel.readString();
            long readLong = parcel.readLong();
            int readInt = parcel.readInt();
            String readString3 = parcel.readString();
            int readInt2 = parcel.readInt();
            Bundle readBundle = parcel.readBundle();
            int readInt3 = parcel.readInt();
            ArrayList arrayList = new ArrayList(readInt3);
            int i = 0;
            while (i < readInt3) {
                arrayList.add(ParticipantEntity.CREATOR.createFromParcel(parcel));
                i++;
            }
            return new RoomEntity(2, readString, readString2, readLong, readInt, readString3, readInt2, readBundle, arrayList, -1);
        }

        public /* synthetic */ Object createFromParcel(Parcel x0) {
            return ax(x0);
        }
    }

    static {
        CREATOR = new RoomEntityCreatorCompat();
    }

    RoomEntity(int versionCode, String roomId, String creatorId, long creationTimestamp, int roomStatus, String description, int variant, Bundle autoMatchCriteria, ArrayList<ParticipantEntity> participants, int autoMatchWaitEstimateSeconds) {
        this.xH = versionCode;
        this.Ja = roomId;
        this.MS = creatorId;
        this.Mu = creationTimestamp;
        this.MT = roomStatus;
        this.HD = description;
        this.My = variant;
        this.MO = autoMatchCriteria;
        this.Mx = participants;
        this.MU = autoMatchWaitEstimateSeconds;
    }

    public RoomEntity(Room room) {
        this.xH = 2;
        this.Ja = room.getRoomId();
        this.MS = room.getCreatorId();
        this.Mu = room.getCreationTimestamp();
        this.MT = room.getStatus();
        this.HD = room.getDescription();
        this.My = room.getVariant();
        this.MO = room.getAutoMatchCriteria();
        ArrayList participants = room.getParticipants();
        int size = participants.size();
        this.Mx = new ArrayList(size);
        int i = 0;
        while (i < size) {
            this.Mx.add((ParticipantEntity) ((Participant) participants.get(i)).freeze());
            i++;
        }
        this.MU = room.getAutoMatchWaitEstimateSeconds();
    }

    static int a(Room room) {
        return fo.hashCode(new Object[]{room.getRoomId(), room.getCreatorId(), Long.valueOf(room.getCreationTimestamp()), Integer.valueOf(room.getStatus()), room.getDescription(), Integer.valueOf(room.getVariant()), room.getAutoMatchCriteria(), room.getParticipants(), Integer.valueOf(room.getAutoMatchWaitEstimateSeconds())});
    }

    static int a(Room room, String str) {
        ArrayList participants = room.getParticipants();
        int size = participants.size();
        int i = 0;
        while (i < size) {
            Participant participant = (Participant) participants.get(i);
            if (participant.getParticipantId().equals(str)) {
                return participant.getStatus();
            }
            i++;
        }
        throw new IllegalStateException("Participant " + str + " is not in room " + room.getRoomId());
    }

    static boolean a(Room room, Room room2) {
        if (!(room2 instanceof Room)) {
            return false;
        }
        if (room == room2) {
            return true;
        }
        room2 = room2;
        return fo.equal(room2.getRoomId(), room.getRoomId()) && fo.equal(room2.getCreatorId(), room.getCreatorId()) && fo.equal(Long.valueOf(room2.getCreationTimestamp()), Long.valueOf(room.getCreationTimestamp())) && fo.equal(Integer.valueOf(room2.getStatus()), Integer.valueOf(room.getStatus())) && fo.equal(room2.getDescription(), room.getDescription()) && fo.equal(Integer.valueOf(room2.getVariant()), Integer.valueOf(room.getVariant())) && fo.equal(room2.getAutoMatchCriteria(), room.getAutoMatchCriteria()) && fo.equal(room2.getParticipants(), room.getParticipants()) && fo.equal(Integer.valueOf(room2.getAutoMatchWaitEstimateSeconds()), Integer.valueOf(room.getAutoMatchWaitEstimateSeconds()));
    }

    static String b(Room room) {
        return fo.e(room).a("RoomId", room.getRoomId()).a("CreatorId", room.getCreatorId()).a("CreationTimestamp", Long.valueOf(room.getCreationTimestamp())).a("RoomStatus", Integer.valueOf(room.getStatus())).a("Description", room.getDescription()).a("Variant", Integer.valueOf(room.getVariant())).a("AutoMatchCriteria", room.getAutoMatchCriteria()).a("Participants", room.getParticipants()).a("AutoMatchWaitEstimateSeconds", Integer.valueOf(room.getAutoMatchWaitEstimateSeconds())).toString();
    }

    static String b(Room room, String str) {
        ArrayList participants = room.getParticipants();
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

    static Participant c(Room room, String str) {
        ArrayList participants = room.getParticipants();
        int size = participants.size();
        int i = 0;
        while (i < size) {
            Participant participant = (Participant) participants.get(i);
            if (participant.getParticipantId().equals(str)) {
                return participant;
            }
            i++;
        }
        throw new IllegalStateException("Participant " + str + " is not in match " + room.getRoomId());
    }

    static ArrayList<String> c(Room room) {
        ArrayList participants = room.getParticipants();
        int size = participants.size();
        ArrayList<String> arrayList = new ArrayList(size);
        int i = 0;
        while (i < size) {
            arrayList.add(((Participant) participants.get(i)).getParticipantId());
            i++;
        }
        return arrayList;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        return a(this, obj);
    }

    public Room freeze() {
        return this;
    }

    public Bundle getAutoMatchCriteria() {
        return this.MO;
    }

    public int getAutoMatchWaitEstimateSeconds() {
        return this.MU;
    }

    public long getCreationTimestamp() {
        return this.Mu;
    }

    public String getCreatorId() {
        return this.MS;
    }

    public String getDescription() {
        return this.HD;
    }

    public void getDescription(CharArrayBuffer dataOut) {
        gm.b(this.HD, dataOut);
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

    public String getRoomId() {
        return this.Ja;
    }

    public int getStatus() {
        return this.MT;
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
            dest.writeString(this.Ja);
            dest.writeString(this.MS);
            dest.writeLong(this.Mu);
            dest.writeInt(this.MT);
            dest.writeString(this.HD);
            dest.writeInt(this.My);
            dest.writeBundle(this.MO);
            int size = this.Mx.size();
            dest.writeInt(size);
            int i = 0;
            while (i < size) {
                ((ParticipantEntity) this.Mx.get(i)).writeToParcel(dest, flags);
                i++;
            }
        } else {
            RoomEntityCreator.a(this, dest, flags);
        }
    }
}