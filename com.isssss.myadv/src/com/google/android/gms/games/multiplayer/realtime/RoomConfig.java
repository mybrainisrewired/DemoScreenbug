package com.google.android.gms.games.multiplayer.realtime;

import android.os.Bundle;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.internal.fq;
import java.util.ArrayList;
import java.util.Arrays;

public final class RoomConfig {
    private final String IV;
    private final RoomUpdateListener MK;
    private final RoomStatusUpdateListener ML;
    private final RealTimeMessageReceivedListener MM;
    private final String[] MN;
    private final Bundle MO;
    private final boolean MP;
    private final int My;

    public static final class Builder {
        final RoomUpdateListener MK;
        RoomStatusUpdateListener ML;
        RealTimeMessageReceivedListener MM;
        Bundle MO;
        boolean MP;
        String MQ;
        ArrayList<String> MR;
        int My;

        private Builder(Object updateListener) {
            this.MQ = null;
            this.My = -1;
            this.MR = new ArrayList();
            this.MP = false;
            this.MK = (RoomUpdateListener) fq.b(updateListener, (Object)"Must provide a RoomUpdateListener");
        }

        public com.google.android.gms.games.multiplayer.realtime.RoomConfig.Builder addPlayersToInvite(ArrayList<String> playerIds) {
            fq.f(playerIds);
            this.MR.addAll(playerIds);
            return this;
        }

        public com.google.android.gms.games.multiplayer.realtime.RoomConfig.Builder addPlayersToInvite(String... playerIds) {
            fq.f(playerIds);
            this.MR.addAll(Arrays.asList(playerIds));
            return this;
        }

        public RoomConfig build() {
            return new RoomConfig(null);
        }

        public com.google.android.gms.games.multiplayer.realtime.RoomConfig.Builder setAutoMatchCriteria(Bundle autoMatchCriteria) {
            this.MO = autoMatchCriteria;
            return this;
        }

        public com.google.android.gms.games.multiplayer.realtime.RoomConfig.Builder setInvitationIdToAccept(String invitationId) {
            fq.f(invitationId);
            this.MQ = invitationId;
            return this;
        }

        public com.google.android.gms.games.multiplayer.realtime.RoomConfig.Builder setMessageReceivedListener(RealTimeMessageReceivedListener listener) {
            this.MM = listener;
            return this;
        }

        public com.google.android.gms.games.multiplayer.realtime.RoomConfig.Builder setRoomStatusUpdateListener(RoomStatusUpdateListener listener) {
            this.ML = listener;
            return this;
        }

        public com.google.android.gms.games.multiplayer.realtime.RoomConfig.Builder setSocketCommunicationEnabled(boolean enableSockets) {
            this.MP = enableSockets;
            return this;
        }

        public com.google.android.gms.games.multiplayer.realtime.RoomConfig.Builder setVariant(int variant) {
            boolean z = variant == -1 || variant > 0;
            fq.b(z, (Object)"Variant must be a positive integer or Room.ROOM_VARIANT_ANY");
            this.My = variant;
            return this;
        }
    }

    private RoomConfig(Builder builder) {
        this.MK = builder.MK;
        this.ML = builder.ML;
        this.MM = builder.MM;
        this.IV = builder.MQ;
        this.My = builder.My;
        this.MO = builder.MO;
        this.MP = builder.MP;
        this.MN = (String[]) builder.MR.toArray(new String[builder.MR.size()]);
        if (this.MM == null) {
            fq.a(this.MP, "Must either enable sockets OR specify a message listener");
        }
    }

    public static Builder builder(RoomUpdateListener listener) {
        return new Builder(null);
    }

    public static Bundle createAutoMatchCriteria(int minAutoMatchPlayers, int maxAutoMatchPlayers, long exclusiveBitMask) {
        Bundle bundle = new Bundle();
        bundle.putInt(Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS, minAutoMatchPlayers);
        bundle.putInt(Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS, maxAutoMatchPlayers);
        bundle.putLong(Multiplayer.EXTRA_EXCLUSIVE_BIT_MASK, exclusiveBitMask);
        return bundle;
    }

    public Bundle getAutoMatchCriteria() {
        return this.MO;
    }

    public String getInvitationId() {
        return this.IV;
    }

    public String[] getInvitedPlayerIds() {
        return this.MN;
    }

    public RealTimeMessageReceivedListener getMessageReceivedListener() {
        return this.MM;
    }

    public RoomStatusUpdateListener getRoomStatusUpdateListener() {
        return this.ML;
    }

    public RoomUpdateListener getRoomUpdateListener() {
        return this.MK;
    }

    public int getVariant() {
        return this.My;
    }

    public boolean isSocketEnabled() {
        return this.MP;
    }
}