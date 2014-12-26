package com.inmobi.commons.data;

import android.location.Location;
import com.inmobi.commons.EducationType;
import com.inmobi.commons.EthnicityType;
import com.inmobi.commons.GenderType;
import com.inmobi.commons.HasChildren;
import com.inmobi.commons.IMIDType;
import com.inmobi.commons.MaritalStatus;
import com.inmobi.commons.SexualOrientation;
import com.inmobi.commons.analytics.bootstrapper.AnalyticsInitializer;
import com.mopub.common.Preconditions;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public final class DemogInfo {
    private static int a;
    private static Location b;
    private static EducationType c;
    private static EthnicityType d;
    private static GenderType e;
    private static Calendar f;
    private static Integer g;
    private static Integer h;
    private static String i;
    private static String j;
    private static String k;
    private static String l;
    private static HasChildren m;
    private static MaritalStatus n;
    private static String o;
    private static SexualOrientation p;
    private static Map<IMIDType, String> q;

    static {
        a = 1;
        g = Integer.valueOf(0);
        h = Integer.valueOf(0);
        q = new HashMap();
    }

    public static void addIDType(IMIDType iMIDType, String str) {
        if (q != null) {
            q.put(iMIDType, str);
        }
    }

    public static Integer getAge() {
        return h;
    }

    public static String getAreaCode() {
        return l;
    }

    public static Location getCurrentLocation() {
        return b;
    }

    public static Calendar getDateOfBirth() {
        return f;
    }

    public static int getDeviceIDMask() {
        return a;
    }

    public static EducationType getEducation() {
        return c;
    }

    public static EthnicityType getEthnicity() {
        return d;
    }

    public static GenderType getGender() {
        return e;
    }

    public static HasChildren getHasChildren() {
        return m;
    }

    public static String getIDType(IMIDType iMIDType) {
        return q != null ? (String) q.get(iMIDType) : null;
    }

    public static Integer getIncome() {
        return g;
    }

    public static String getInterests() {
        return i;
    }

    public static String getLanguage() {
        return o;
    }

    public static String getLocationWithCityStateCountry() {
        return j;
    }

    public static MaritalStatus getMaritalStatus() {
        return n;
    }

    public static String getPostalCode() {
        return k;
    }

    public static SexualOrientation getSexualOrientation() {
        return p;
    }

    public static boolean isLocationInquiryAllowed() {
        return AnalyticsInitializer.getConfigParams().getAutomaticCapture().isAutoLocationCaptureEnabled();
    }

    public static void removeIDType(IMIDType iMIDType) {
        if (q != null) {
            q.remove(iMIDType);
        }
    }

    public static void setAge(Integer num) {
        h = num;
    }

    public static void setAreaCode(String str) {
        l = str;
    }

    public static void setCurrentLocation(Location location) {
        b = location;
    }

    public static void setDateOfBirth(Calendar calendar) {
        f = calendar;
    }

    public static void setDeviceIDMask(int i) {
        a = i;
    }

    public static void setEducation(EducationType educationType) {
        c = educationType;
    }

    public static void setEthnicity(EthnicityType ethnicityType) {
        d = ethnicityType;
    }

    public static void setGender(GenderType genderType) {
        e = genderType;
    }

    public static void setHasChildren(HasChildren hasChildren) {
        m = hasChildren;
    }

    public static void setIncome(Integer num) {
        g = num;
    }

    public static void setInterests(String str) {
        i = str;
    }

    public static void setLanguage(String str) {
        o = str;
    }

    public static void setLocationWithCityStateCountry(String str, String str2, String str3) {
        if (!(str == null || Preconditions.EMPTY_ARGUMENTS.equals(str.trim()))) {
            j = str;
        }
        if (!(str2 == null || Preconditions.EMPTY_ARGUMENTS.equals(str2.trim()))) {
            j += "-" + str2;
        }
        if (str3 != null && !Preconditions.EMPTY_ARGUMENTS.equals(str3.trim())) {
            j += "-" + str3;
        }
    }

    public static void setMaritalStatus(MaritalStatus maritalStatus) {
        n = maritalStatus;
    }

    public static void setPostalCode(String str) {
        k = str;
    }

    public static void setSexualOrientation(SexualOrientation sexualOrientation) {
        p = sexualOrientation;
    }
}