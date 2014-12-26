package com.google.android.gms.plus.model.people;

import com.google.android.gms.common.data.Freezable;
import java.util.List;

public interface Person extends Freezable<Person> {

    public static final class Gender {
        public static final int FEMALE = 1;
        public static final int MALE = 0;
        public static final int OTHER = 2;

        private Gender() {
        }
    }

    public static final class ObjectType {
        public static final int PAGE = 1;
        public static final int PERSON = 0;

        private ObjectType() {
        }
    }

    public static final class RelationshipStatus {
        public static final int ENGAGED = 2;
        public static final int IN_A_RELATIONSHIP = 1;
        public static final int IN_CIVIL_UNION = 8;
        public static final int IN_DOMESTIC_PARTNERSHIP = 7;
        public static final int ITS_COMPLICATED = 4;
        public static final int MARRIED = 3;
        public static final int OPEN_RELATIONSHIP = 5;
        public static final int SINGLE = 0;
        public static final int WIDOWED = 6;

        private RelationshipStatus() {
        }
    }

    public static interface AgeRange extends Freezable<com.google.android.gms.plus.model.people.Person.AgeRange> {
        int getMax();

        int getMin();

        boolean hasMax();

        boolean hasMin();
    }

    public static interface Cover extends Freezable<com.google.android.gms.plus.model.people.Person.Cover> {

        public static final class Layout {
            public static final int BANNER = 0;

            private Layout() {
            }
        }

        public static interface CoverInfo extends Freezable<com.google.android.gms.plus.model.people.Person.Cover.CoverInfo> {
            int getLeftImageOffset();

            int getTopImageOffset();

            boolean hasLeftImageOffset();

            boolean hasTopImageOffset();
        }

        public static interface CoverPhoto extends Freezable<com.google.android.gms.plus.model.people.Person.Cover.CoverPhoto> {
            int getHeight();

            String getUrl();

            int getWidth();

            boolean hasHeight();

            boolean hasUrl();

            boolean hasWidth();
        }

        CoverInfo getCoverInfo();

        CoverPhoto getCoverPhoto();

        int getLayout();

        boolean hasCoverInfo();

        boolean hasCoverPhoto();

        boolean hasLayout();
    }

    public static interface Image extends Freezable<com.google.android.gms.plus.model.people.Person.Image> {
        String getUrl();

        boolean hasUrl();
    }

    public static interface Name extends Freezable<com.google.android.gms.plus.model.people.Person.Name> {
        String getFamilyName();

        String getFormatted();

        String getGivenName();

        String getHonorificPrefix();

        String getHonorificSuffix();

        String getMiddleName();

        boolean hasFamilyName();

        boolean hasFormatted();

        boolean hasGivenName();

        boolean hasHonorificPrefix();

        boolean hasHonorificSuffix();

        boolean hasMiddleName();
    }

    public static interface Organizations extends Freezable<com.google.android.gms.plus.model.people.Person.Organizations> {

        public static final class Type {
            public static final int SCHOOL = 1;
            public static final int WORK = 0;

            private Type() {
            }
        }

        String getDepartment();

        String getDescription();

        String getEndDate();

        String getLocation();

        String getName();

        String getStartDate();

        String getTitle();

        int getType();

        boolean hasDepartment();

        boolean hasDescription();

        boolean hasEndDate();

        boolean hasLocation();

        boolean hasName();

        boolean hasPrimary();

        boolean hasStartDate();

        boolean hasTitle();

        boolean hasType();

        boolean isPrimary();
    }

    public static interface PlacesLived extends Freezable<com.google.android.gms.plus.model.people.Person.PlacesLived> {
        String getValue();

        boolean hasPrimary();

        boolean hasValue();

        boolean isPrimary();
    }

    public static interface Urls extends Freezable<com.google.android.gms.plus.model.people.Person.Urls> {

        public static final class Type {
            public static final int CONTRIBUTOR = 6;
            public static final int OTHER = 4;
            public static final int OTHER_PROFILE = 5;
            public static final int WEBSITE = 7;

            private Type() {
            }
        }

        String getLabel();

        int getType();

        String getValue();

        boolean hasLabel();

        boolean hasType();

        boolean hasValue();
    }

    String getAboutMe();

    AgeRange getAgeRange();

    String getBirthday();

    String getBraggingRights();

    int getCircledByCount();

    Cover getCover();

    String getCurrentLocation();

    String getDisplayName();

    int getGender();

    String getId();

    Image getImage();

    String getLanguage();

    Name getName();

    String getNickname();

    int getObjectType();

    List<Organizations> getOrganizations();

    List<PlacesLived> getPlacesLived();

    int getPlusOneCount();

    int getRelationshipStatus();

    String getTagline();

    String getUrl();

    List<Urls> getUrls();

    boolean hasAboutMe();

    boolean hasAgeRange();

    boolean hasBirthday();

    boolean hasBraggingRights();

    boolean hasCircledByCount();

    boolean hasCover();

    boolean hasCurrentLocation();

    boolean hasDisplayName();

    boolean hasGender();

    boolean hasId();

    boolean hasImage();

    boolean hasIsPlusUser();

    boolean hasLanguage();

    boolean hasName();

    boolean hasNickname();

    boolean hasObjectType();

    boolean hasOrganizations();

    boolean hasPlacesLived();

    boolean hasPlusOneCount();

    boolean hasRelationshipStatus();

    boolean hasTagline();

    boolean hasUrl();

    boolean hasUrls();

    boolean hasVerified();

    boolean isPlusUser();

    boolean isVerified();
}