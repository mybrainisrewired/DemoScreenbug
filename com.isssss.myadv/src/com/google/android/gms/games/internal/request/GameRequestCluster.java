package com.google.android.gms.games.internal.request;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.games.Game;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.request.GameRequest;
import com.google.android.gms.games.request.GameRequestEntity;
import com.google.android.gms.internal.fb;
import com.google.android.gms.internal.fo;
import java.util.ArrayList;
import java.util.List;

public final class GameRequestCluster implements SafeParcelable, GameRequest {
    public static final GameRequestClusterCreator CREATOR;
    private final ArrayList<GameRequestEntity> LM;
    private final int xH;

    static {
        CREATOR = new GameRequestClusterCreator();
    }

    GameRequestCluster(int versionCode, ArrayList<GameRequestEntity> requestList) {
        this.xH = versionCode;
        this.LM = requestList;
        hn();
    }

    private void hn() {
        fb.x(!this.LM.isEmpty());
        GameRequest gameRequest = (GameRequest) this.LM.get(0);
        int size = this.LM.size();
        int i = 1;
        while (i < size) {
            GameRequest gameRequest2 = (GameRequest) this.LM.get(i);
            fb.a(gameRequest.getType() == gameRequest2.getType(), "All the requests must be of the same type");
            fb.a(gameRequest.getSender().equals(gameRequest2.getSender()), "All the requests must be from the same sender");
            i++;
        }
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(GameRequestCluster obj) {
        if (!(obj instanceof GameRequestCluster)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        obj = obj;
        if (obj.LM.size() != this.LM.size()) {
            return false;
        }
        int size = this.LM.size();
        int i = 0;
        while (i < size) {
            if (!((GameRequest) this.LM.get(i)).equals((GameRequest) obj.LM.get(i))) {
                return false;
            }
            i++;
        }
        return true;
    }

    public GameRequest freeze() {
        return this;
    }

    public long getCreationTimestamp() {
        throw new UnsupportedOperationException("Method not supported on a cluster");
    }

    public byte[] getData() {
        throw new UnsupportedOperationException("Method not supported on a cluster");
    }

    public long getExpirationTimestamp() {
        throw new UnsupportedOperationException("Method not supported on a cluster");
    }

    public Game getGame() {
        throw new UnsupportedOperationException("Method not supported on a cluster");
    }

    public int getRecipientStatus(String playerId) {
        throw new UnsupportedOperationException("Method not supported on a cluster");
    }

    public /* synthetic */ List getRecipients() {
        return hA();
    }

    public String getRequestId() {
        return ((GameRequestEntity) this.LM.get(0)).getRequestId();
    }

    public Player getSender() {
        return ((GameRequestEntity) this.LM.get(0)).getSender();
    }

    public int getStatus() {
        throw new UnsupportedOperationException("Method not supported on a cluster");
    }

    public int getType() {
        return ((GameRequestEntity) this.LM.get(0)).getType();
    }

    public int getVersionCode() {
        return this.xH;
    }

    public ArrayList<Player> hA() {
        throw new UnsupportedOperationException("Method not supported on a cluster");
    }

    public int hashCode() {
        return fo.hashCode(this.LM.toArray());
    }

    public ArrayList<GameRequest> hz() {
        return new ArrayList(this.LM);
    }

    public boolean isConsumed(String playerId) {
        throw new UnsupportedOperationException("Method not supported on a cluster");
    }

    public boolean isDataValid() {
        return true;
    }

    public void writeToParcel(Parcel dest, int flags) {
        GameRequestClusterCreator.a(this, dest, flags);
    }
}