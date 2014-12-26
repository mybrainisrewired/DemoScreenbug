package com.google.android.gms.games.internal.game;

import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.common.data.d;

public final class ExtendedGameBuffer extends d<ExtendedGame> {
    public ExtendedGameBuffer(DataHolder dataHolder) {
        super(dataHolder);
    }

    protected /* synthetic */ Object c(int i, int i2) {
        return d(i, i2);
    }

    protected ExtendedGame d(int i, int i2) {
        return new ExtendedGameRef(this.BB, i, i2);
    }

    protected String getPrimaryDataMarkerColumn() {
        return "external_game_id";
    }
}