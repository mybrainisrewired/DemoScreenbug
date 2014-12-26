package com.google.android.gms.appstate;

import com.google.android.gms.internal.fo;

public final class a implements AppState {
    private final int wr;
    private final String ws;
    private final byte[] wt;
    private final boolean wu;
    private final String wv;
    private final byte[] ww;

    public a(AppState appState) {
        this.wr = appState.getKey();
        this.ws = appState.getLocalVersion();
        this.wt = appState.getLocalData();
        this.wu = appState.hasConflict();
        this.wv = appState.getConflictVersion();
        this.ww = appState.getConflictData();
    }

    static int a(AppState appState) {
        return fo.hashCode(new Object[]{Integer.valueOf(appState.getKey()), appState.getLocalVersion(), appState.getLocalData(), Boolean.valueOf(appState.hasConflict()), appState.getConflictVersion(), appState.getConflictData()});
    }

    static boolean a(AppState appState, AppState appState2) {
        if (!(appState2 instanceof AppState)) {
            return false;
        }
        if (appState == appState2) {
            return true;
        }
        appState2 = appState2;
        return fo.equal(Integer.valueOf(appState2.getKey()), Integer.valueOf(appState.getKey())) && fo.equal(appState2.getLocalVersion(), appState.getLocalVersion()) && fo.equal(appState2.getLocalData(), appState.getLocalData()) && fo.equal(Boolean.valueOf(appState2.hasConflict()), Boolean.valueOf(appState.hasConflict())) && fo.equal(appState2.getConflictVersion(), appState.getConflictVersion()) && fo.equal(appState2.getConflictData(), appState.getConflictData());
    }

    static String b(AppState appState) {
        return fo.e(appState).a("Key", Integer.valueOf(appState.getKey())).a("LocalVersion", appState.getLocalVersion()).a("LocalData", appState.getLocalData()).a("HasConflict", Boolean.valueOf(appState.hasConflict())).a("ConflictVersion", appState.getConflictVersion()).a("ConflictData", appState.getConflictData()).toString();
    }

    public AppState dt() {
        return this;
    }

    public boolean equals(Object obj) {
        return a(this, obj);
    }

    public /* synthetic */ Object freeze() {
        return dt();
    }

    public byte[] getConflictData() {
        return this.ww;
    }

    public String getConflictVersion() {
        return this.wv;
    }

    public int getKey() {
        return this.wr;
    }

    public byte[] getLocalData() {
        return this.wt;
    }

    public String getLocalVersion() {
        return this.ws;
    }

    public boolean hasConflict() {
        return this.wu;
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
}