package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.maps.internal.v;
import com.google.android.gms.maps.model.internal.i;
import com.google.android.gms.maps.model.internal.i.a;

public final class TileOverlayOptions implements SafeParcelable {
    public static final TileOverlayOptionsCreator CREATOR;
    private float SN;
    private boolean SO;
    private i Tt;
    private TileProvider Tu;
    private boolean Tv;
    private final int xH;

    class AnonymousClass_2 extends a {
        final /* synthetic */ TileProvider Ty;

        AnonymousClass_2(TileProvider tileProvider) {
            this.Ty = tileProvider;
        }

        public Tile getTile(int x, int y, int zoom) {
            return this.Ty.getTile(x, y, zoom);
        }
    }

    static {
        CREATOR = new TileOverlayOptionsCreator();
    }

    public TileOverlayOptions() {
        this.SO = true;
        this.Tv = true;
        this.xH = 1;
    }

    TileOverlayOptions(int versionCode, IBinder delegate, boolean visible, float zIndex, boolean fadeIn) {
        this.SO = true;
        this.Tv = true;
        this.xH = versionCode;
        this.Tt = a.aK(delegate);
        this.Tu = this.Tt == null ? null : new TileProvider() {
            private final i Tw;

            {
                this.Tw = TileOverlayOptions.this.Tt;
            }

            public Tile getTile(int x, int y, int zoom) {
                try {
                    return this.Tw.getTile(x, y, zoom);
                } catch (RemoteException e) {
                    return null;
                }
            }
        };
        this.SO = visible;
        this.SN = zIndex;
        this.Tv = fadeIn;
    }

    public int describeContents() {
        return 0;
    }

    public TileOverlayOptions fadeIn(boolean fadeIn) {
        this.Tv = fadeIn;
        return this;
    }

    public boolean getFadeIn() {
        return this.Tv;
    }

    public TileProvider getTileProvider() {
        return this.Tu;
    }

    int getVersionCode() {
        return this.xH;
    }

    public float getZIndex() {
        return this.SN;
    }

    IBinder iG() {
        return this.Tt.asBinder();
    }

    public boolean isVisible() {
        return this.SO;
    }

    public TileOverlayOptions tileProvider(TileProvider tileProvider) {
        this.Tu = tileProvider;
        this.Tt = this.Tu == null ? null : new AnonymousClass_2(tileProvider);
        return this;
    }

    public TileOverlayOptions visible(boolean visible) {
        this.SO = visible;
        return this;
    }

    public void writeToParcel(Parcel out, int flags) {
        if (v.iB()) {
            j.a(this, out, flags);
        } else {
            TileOverlayOptionsCreator.a(this, out, flags);
        }
    }

    public TileOverlayOptions zIndex(float zIndex) {
        this.SN = zIndex;
        return this;
    }
}