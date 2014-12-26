package com.google.android.gms.games.multiplayer;

import com.google.android.gms.games.Player;
import com.google.android.gms.internal.fq;
import java.util.ArrayList;

public final class ParticipantUtils {
    private ParticipantUtils() {
    }

    public static boolean aV(Object obj) {
        fq.b(obj, (Object)"Participant ID must not be null");
        return obj.startsWith("p_");
    }

    public static String getParticipantId(ArrayList<Participant> participants, String playerId) {
        int size = participants.size();
        int i = 0;
        while (i < size) {
            Participant participant = (Participant) participants.get(i);
            Player player = participant.getPlayer();
            if (player != null && player.getPlayerId().equals(playerId)) {
                return participant.getParticipantId();
            }
            i++;
        }
        return null;
    }
}