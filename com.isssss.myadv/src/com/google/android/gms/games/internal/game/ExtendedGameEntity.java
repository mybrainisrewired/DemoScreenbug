package com.google.android.gms.games.internal.game;

import android.os.Parcel;
import com.google.android.gms.games.Game;
import com.google.android.gms.games.GameEntity;
import com.google.android.gms.games.internal.GamesDowngradeableSafeParcel;
import com.google.android.gms.internal.fo;
import java.util.ArrayList;

public final class ExtendedGameEntity extends GamesDowngradeableSafeParcel implements ExtendedGame {
    public static final ExtendedGameEntityCreator CREATOR;
    private final long LA;
    private final String LB;
    private final ArrayList<GameBadgeEntity> LC;
    private final GameEntity Lt;
    private final int Lu;
    private final boolean Lv;
    private final int Lw;
    private final long Lx;
    private final long Ly;
    private final String Lz;
    private final int xH;

    static final class ExtendedGameEntityCreatorCompat extends ExtendedGameEntityCreator {
        ExtendedGameEntityCreatorCompat() {
        }

        public ExtendedGameEntity aq(Parcel parcel) {
            if (ExtendedGameEntity.c(ExtendedGameEntity.eJ()) || ExtendedGameEntity.al(ExtendedGameEntity.class.getCanonicalName())) {
                return super.aq(parcel);
            }
            GameEntity gameEntity = (GameEntity) GameEntity.CREATOR.createFromParcel(parcel);
            int readInt = parcel.readInt();
            boolean z = parcel.readInt() == 1;
            int readInt2 = parcel.readInt();
            long readLong = parcel.readLong();
            long readLong2 = parcel.readLong();
            String readString = parcel.readString();
            long readLong3 = parcel.readLong();
            String readString2 = parcel.readString();
            int readInt3 = parcel.readInt();
            ArrayList arrayList = arrayList;
            int i = 0;
            while (i < readInt3) {
                arrayList.add(GameBadgeEntity.CREATOR.ar(parcel));
                i++;
            }
            return new ExtendedGameEntity(1, gameEntity, readInt, z, readInt2, readLong, readLong2, readString, readLong3, readString2, arrayList);
        }

        public /* synthetic */ Object createFromParcel(Parcel x0) {
            return aq(x0);
        }
    }

    static {
        CREATOR = new ExtendedGameEntityCreatorCompat();
    }

    ExtendedGameEntity(int versionCode, GameEntity game, int availability, boolean owned, int achievementUnlockedCount, long lastPlayedServerTimestamp, long priceMicros, String formattedPrice, long fullPriceMicros, String formattedFullPrice, ArrayList<GameBadgeEntity> badges) {
        this.xH = versionCode;
        this.Lt = game;
        this.Lu = availability;
        this.Lv = owned;
        this.Lw = achievementUnlockedCount;
        this.Lx = lastPlayedServerTimestamp;
        this.Ly = priceMicros;
        this.Lz = formattedPrice;
        this.LA = fullPriceMicros;
        this.LB = formattedFullPrice;
        this.LC = badges;
    }

    public ExtendedGameEntity(ExtendedGame extendedGame) {
        this.xH = 1;
        Game game = extendedGame.getGame();
        this.Lt = game == null ? null : new GameEntity(game);
        this.Lu = extendedGame.gX();
        this.Lv = extendedGame.gY();
        this.Lw = extendedGame.gZ();
        this.Lx = extendedGame.ha();
        this.Ly = extendedGame.hb();
        this.Lz = extendedGame.hc();
        this.LA = extendedGame.hd();
        this.LB = extendedGame.he();
        ArrayList gW = extendedGame.gW();
        int size = gW.size();
        this.LC = new ArrayList(size);
        int i = 0;
        while (i < size) {
            this.LC.add((GameBadgeEntity) ((GameBadge) gW.get(i)).freeze());
            i++;
        }
    }

    static int a(ExtendedGame extendedGame) {
        return fo.hashCode(new Object[]{extendedGame.getGame(), Integer.valueOf(extendedGame.gX()), Boolean.valueOf(extendedGame.gY()), Integer.valueOf(extendedGame.gZ()), Long.valueOf(extendedGame.ha()), Long.valueOf(extendedGame.hb()), extendedGame.hc(), Long.valueOf(extendedGame.hd()), extendedGame.he()});
    }

    static boolean a(ExtendedGame extendedGame, ExtendedGame extendedGame2) {
        if (!(extendedGame2 instanceof ExtendedGame)) {
            return false;
        }
        if (extendedGame == extendedGame2) {
            return true;
        }
        extendedGame2 = extendedGame2;
        return fo.equal(extendedGame2.getGame(), extendedGame.getGame()) && fo.equal(Integer.valueOf(extendedGame2.gX()), Integer.valueOf(extendedGame.gX())) && fo.equal(Boolean.valueOf(extendedGame2.gY()), Boolean.valueOf(extendedGame.gY())) && fo.equal(Integer.valueOf(extendedGame2.gZ()), Integer.valueOf(extendedGame.gZ())) && fo.equal(Long.valueOf(extendedGame2.ha()), Long.valueOf(extendedGame.ha())) && fo.equal(Long.valueOf(extendedGame2.hb()), Long.valueOf(extendedGame.hb())) && fo.equal(extendedGame2.hc(), extendedGame.hc()) && fo.equal(Long.valueOf(extendedGame2.hd()), Long.valueOf(extendedGame.hd())) && fo.equal(extendedGame2.he(), extendedGame.he());
    }

    static String b(ExtendedGame extendedGame) {
        return fo.e(extendedGame).a("Game", extendedGame.getGame()).a("Availability", Integer.valueOf(extendedGame.gX())).a("Owned", Boolean.valueOf(extendedGame.gY())).a("AchievementUnlockedCount", Integer.valueOf(extendedGame.gZ())).a("LastPlayedServerTimestamp", Long.valueOf(extendedGame.ha())).a("PriceMicros", Long.valueOf(extendedGame.hb())).a("FormattedPrice", extendedGame.hc()).a("FullPriceMicros", Long.valueOf(extendedGame.hd())).a("FormattedFullPrice", extendedGame.he()).toString();
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        return a(this, obj);
    }

    public /* synthetic */ Object freeze() {
        return hg();
    }

    public ArrayList<GameBadge> gW() {
        return new ArrayList(this.LC);
    }

    public int gX() {
        return this.Lu;
    }

    public boolean gY() {
        return this.Lv;
    }

    public int gZ() {
        return this.Lw;
    }

    public /* synthetic */ Game getGame() {
        return hf();
    }

    public int getVersionCode() {
        return this.xH;
    }

    public long ha() {
        return this.Lx;
    }

    public int hashCode() {
        return a(this);
    }

    public long hb() {
        return this.Ly;
    }

    public String hc() {
        return this.Lz;
    }

    public long hd() {
        return this.LA;
    }

    public String he() {
        return this.LB;
    }

    public GameEntity hf() {
        return this.Lt;
    }

    public ExtendedGame hg() {
        return this;
    }

    public boolean isDataValid() {
        return true;
    }

    public String toString() {
        return b(this);
    }

    public void writeToParcel(Parcel dest, int flags) {
        int i = 0;
        if (eK()) {
            this.Lt.writeToParcel(dest, flags);
            dest.writeInt(this.Lu);
            dest.writeInt(this.Lv ? 1 : 0);
            dest.writeInt(this.Lw);
            dest.writeLong(this.Lx);
            dest.writeLong(this.Ly);
            dest.writeString(this.Lz);
            dest.writeLong(this.LA);
            dest.writeString(this.LB);
            int size = this.LC.size();
            dest.writeInt(size);
            while (i < size) {
                ((GameBadgeEntity) this.LC.get(i)).writeToParcel(dest, flags);
                i++;
            }
        } else {
            ExtendedGameEntityCreator.a(this, dest, flags);
        }
    }
}