package com.google.android.gms.games.internal.multiplayer;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.games.Game;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.InvitationEntity;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.internal.fb;
import com.google.android.gms.internal.fo;
import java.util.ArrayList;

public final class ZInvitationCluster implements SafeParcelable, Invitation {
    public static final InvitationClusterCreator CREATOR;
    private final ArrayList<InvitationEntity> LG;
    private final int xH;

    static {
        CREATOR = new InvitationClusterCreator();
    }

    ZInvitationCluster(int versionCode, ArrayList<InvitationEntity> invitationList) {
        this.xH = versionCode;
        this.LG = invitationList;
        hn();
    }

    private void hn() {
        fb.x(!this.LG.isEmpty());
        Invitation invitation = (Invitation) this.LG.get(0);
        int size = this.LG.size();
        int i = 1;
        while (i < size) {
            fb.a(invitation.getInviter().equals(((Invitation) this.LG.get(i)).getInviter()), "All the invitations must be from the same inviter");
            i++;
        }
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(ZInvitationCluster obj) {
        if (!(obj instanceof ZInvitationCluster)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        obj = obj;
        if (obj.LG.size() != this.LG.size()) {
            return false;
        }
        int size = this.LG.size();
        int i = 0;
        while (i < size) {
            if (!((Invitation) this.LG.get(i)).equals((Invitation) obj.LG.get(i))) {
                return false;
            }
            i++;
        }
        return true;
    }

    public Invitation freeze() {
        return this;
    }

    public int getAvailableAutoMatchSlots() {
        throw new UnsupportedOperationException("Method not supported on a cluster");
    }

    public long getCreationTimestamp() {
        throw new UnsupportedOperationException("Method not supported on a cluster");
    }

    public Game getGame() {
        throw new UnsupportedOperationException("Method not supported on a cluster");
    }

    public String getInvitationId() {
        return ((InvitationEntity) this.LG.get(0)).getInvitationId();
    }

    public int getInvitationType() {
        throw new UnsupportedOperationException("Method not supported on a cluster");
    }

    public Participant getInviter() {
        return ((InvitationEntity) this.LG.get(0)).getInviter();
    }

    public ArrayList<Participant> getParticipants() {
        throw new UnsupportedOperationException("Method not supported on a cluster");
    }

    public int getVariant() {
        throw new UnsupportedOperationException("Method not supported on a cluster");
    }

    public int getVersionCode() {
        return this.xH;
    }

    public int hashCode() {
        return fo.hashCode(this.LG.toArray());
    }

    public ArrayList<Invitation> ho() {
        return new ArrayList(this.LG);
    }

    public boolean isDataValid() {
        return true;
    }

    public void writeToParcel(Parcel dest, int flags) {
        InvitationClusterCreator.a(this, dest, flags);
    }
}