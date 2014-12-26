package mobi.vserv.android.ads;

public enum Gender {
    MALE,
    FEMALE;

    static {
        MALE = new Gender("MALE", 0);
        FEMALE = new Gender("FEMALE", 1);
        ENUM$VALUES = new Gender[]{MALE, FEMALE};
    }
}