package com.google.android.gms.games.request;

import android.os.Bundle;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.games.Game;
import com.google.android.gms.games.GameEntity;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayerEntity;
import com.google.android.gms.internal.fo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class GameRequestEntity implements SafeParcelable, GameRequest {
    public static final GameRequestEntityCreator CREATOR;
    private final String Jo;
    private final int LF;
    private final GameEntity Lt;
    private final int MB;
    private final long Mu;
    private final byte[] Nf;
    private final PlayerEntity Nm;
    private final ArrayList<PlayerEntity> Nn;
    private final long No;
    private final Bundle Np;
    private final int xH;

    static {
        CREATOR = new GameRequestEntityCreator();
    }

    GameRequestEntity(int versionCode, GameEntity game, PlayerEntity sender, byte[] data, String requestId, ArrayList<PlayerEntity> recipients, int type, long creationTimestamp, long expirationTimestamp, Bundle recipientStatuses, int status) {
        this.xH = versionCode;
        this.Lt = game;
        this.Nm = sender;
        this.Nf = data;
        this.Jo = requestId;
        this.Nn = recipients;
        this.LF = type;
        this.Mu = creationTimestamp;
        this.No = expirationTimestamp;
        this.Np = recipientStatuses;
        this.MB = status;
    }

    public GameRequestEntity(GameRequest request) {
        this.xH = 2;
        this.Lt = new GameEntity(request.getGame());
        this.Nm = new PlayerEntity(request.getSender());
        this.Jo = request.getRequestId();
        this.LF = request.getType();
        this.Mu = request.getCreationTimestamp();
        this.No = request.getExpirationTimestamp();
        this.MB = request.getStatus();
        Object data = request.getData();
        if (data == null) {
            this.Nf = null;
        } else {
            this.Nf = new byte[data.length];
            System.arraycopy(data, 0, this.Nf, 0, data.length);
        }
        List recipients = request.getRecipients();
        int size = recipients.size();
        this.Nn = new ArrayList(size);
        this.Np = new Bundle();
        int i = 0;
        while (i < size) {
            Player player = (Player) ((Player) recipients.get(i)).freeze();
            String playerId = player.getPlayerId();
            this.Nn.add((PlayerEntity) player);
            this.Np.putInt(playerId, request.getRecipientStatus(playerId));
            i++;
        }
    }

    static int a(GameRequest gameRequest) {
        return fo.hashCode(new Object[]{gameRequest.getGame(), gameRequest.getRecipients(), gameRequest.getRequestId(), gameRequest.getSender(), b(gameRequest), Integer.valueOf(gameRequest.getType()), Long.valueOf(gameRequest.getCreationTimestamp()), Long.valueOf(gameRequest.getExpirationTimestamp())});
    }

    static boolean a(GameRequest gameRequest, GameRequest gameRequest2) {
        if (!(gameRequest2 instanceof GameRequest)) {
            return false;
        }
        if (gameRequest == gameRequest2) {
            return true;
        }
        gameRequest2 = gameRequest2;
        return fo.equal(gameRequest2.getGame(), gameRequest.getGame()) && fo.equal(gameRequest2.getRecipients(), gameRequest.getRecipients()) && fo.equal(gameRequest2.getRequestId(), gameRequest.getRequestId()) && fo.equal(gameRequest2.getSender(), gameRequest.getSender()) && Arrays.equals(b(gameRequest2), b(gameRequest)) && fo.equal(Integer.valueOf(gameRequest2.getType()), Integer.valueOf(gameRequest.getType())) && fo.equal(Long.valueOf(gameRequest2.getCreationTimestamp()), Long.valueOf(gameRequest.getCreationTimestamp())) && fo.equal(Long.valueOf(gameRequest2.getExpirationTimestamp()), Long.valueOf(gameRequest.getExpirationTimestamp()));
    }

    private static int[] b(GameRequest gameRequest) {
        List recipients = gameRequest.getRecipients();
        int size = recipients.size();
        int[] iArr = new int[size];
        int i = 0;
        while (i < size) {
            iArr[i] = gameRequest.getRecipientStatus(((Player) recipients.get(i)).getPlayerId());
            i++;
        }
        return iArr;
    }

    static String c(GameRequest gameRequest) {
        return fo.e(gameRequest).a("Game", gameRequest.getGame()).a("Sender", gameRequest.getSender()).a("Recipients", gameRequest.getRecipients()).a("Data", gameRequest.getData()).a("RequestId", gameRequest.getRequestId()).a("Type", Integer.valueOf(gameRequest.getType())).a("CreationTimestamp", Long.valueOf(gameRequest.getCreationTimestamp())).a("ExpirationTimestamp", Long.valueOf(gameRequest.getExpirationTimestamp())).toString();
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        return a(this, obj);
    }

    public GameRequest freeze() {
        return this;
    }

    public long getCreationTimestamp() {
        return this.Mu;
    }

    public byte[] getData() {
        return this.Nf;
    }

    public long getExpirationTimestamp() {
        return this.No;
    }

    public Game getGame() {
        return this.Lt;
    }

    public int getRecipientStatus(String playerId) {
        return this.Np.getInt(playerId, 0);
    }

    public List<Player> getRecipients() {
        return new ArrayList(this.Nn);
    }

    public String getRequestId() {
        return this.Jo;
    }

    public Player getSender() {
        return this.Nm;
    }

    public int getStatus() {
        return this.MB;
    }

    public int getType() {
        return this.LF;
    }

    public int getVersionCode() {
        return this.xH;
    }

    public Bundle hK() {
        return this.Np;
    }

    public int hashCode() {
        return a(this);
    }

    public boolean isConsumed(String playerId) {
        return getRecipientStatus(playerId) == 1;
    }

    public boolean isDataValid() {
        return true;
    }

    public String toString() {
        return c(this);
    }

    public void writeToParcel(Parcel dest, int flags) {
        GameRequestEntityCreator.a(this, dest, flags);
    }
}