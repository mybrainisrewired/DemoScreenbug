package com.millennialmedia.android;

import android.location.Location;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public final class MMRequest {
    public static final String EDUCATION_ASSOCIATES = "associates";
    public static final String EDUCATION_BACHELORS = "bachelors";
    public static final String EDUCATION_DOCTORATE = "doctorate";
    public static final String EDUCATION_HIGH_SCHOOL = "highschool";
    public static final String EDUCATION_IN_COLLEGE = "incollege";
    public static final String EDUCATION_MASTERS = "masters";
    public static final String EDUCATION_OTHER = "other";
    public static final String EDUCATION_SOME_COLLEGE = "somecollege";
    public static final String ETHNICITY_ASIAN = "asian";
    public static final String ETHNICITY_BLACK = "black";
    public static final String ETHNICITY_HISPANIC = "hispanic";
    public static final String ETHNICITY_INDIAN = "indian";
    public static final String ETHNICITY_MIDDLE_EASTERN = "middleeastern";
    public static final String ETHNICITY_NATIVE_AMERICAN = "nativeamerican";
    public static final String ETHNICITY_OTHER = "other";
    public static final String ETHNICITY_PACIFIC_ISLANDER = "pacificislander";
    public static final String ETHNICITY_WHITE = "white";
    public static final String GENDER_FEMALE = "female";
    public static final String GENDER_MALE = "male";
    public static final String GENDER_OTHER = "other";
    public static final String KEY_AGE = "age";
    public static final String KEY_CHILDREN = "children";
    public static final String KEY_EDUCATION = "education";
    public static final String KEY_ETHNICITY = "ethnicity";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_HEIGHT = "hsht";
    public static final String KEY_INCOME = "income";
    public static final String KEY_KEYWORDS = "keywords";
    public static final String KEY_MARITAL_STATUS = "marital";
    public static final String KEY_POLITICS = "politics";
    public static final String KEY_VENDOR = "vendor";
    public static final String KEY_WIDTH = "hswd";
    public static final String KEY_ZIP_CODE = "zip";
    public static final String MARITAL_DIVORCED = "divorced";
    public static final String MARITAL_ENGAGED = "engaged";
    public static final String MARITAL_MARRIED = "married";
    public static final String MARITAL_OTHER = "other";
    public static final String MARITAL_RELATIONSHIP = "relationship";
    public static final String MARITAL_SINGLE = "single";
    static Location location;
    String age;
    String children;
    String education;
    String ethnicity;
    String gender;
    String income;
    String keywords;
    String marital;
    String politics;
    private Map<String, String> values;
    String vendor;
    String zip;

    public MMRequest() {
        this.age = null;
        this.children = null;
        this.education = null;
        this.ethnicity = null;
        this.gender = null;
        this.income = null;
        this.keywords = null;
        this.marital = null;
        this.politics = null;
        this.vendor = null;
        this.zip = null;
        this.values = new HashMap();
    }

    public static Location getUserLocation() {
        return location;
    }

    static void insertLocation(Map<String, String> paramsMap) {
        if (location != null) {
            paramsMap.put("lat", Double.toString(location.getLatitude()));
            paramsMap.put("long", Double.toString(location.getLongitude()));
            if (location.hasAccuracy()) {
                paramsMap.put("ha", Float.toString(location.getAccuracy()));
                paramsMap.put("va", Float.toString(location.getAccuracy()));
            }
            if (location.hasSpeed()) {
                paramsMap.put("spd", Float.toString(location.getSpeed()));
            }
            if (location.hasBearing()) {
                paramsMap.put("brg", Float.toString(location.getBearing()));
            }
            if (location.hasAltitude()) {
                paramsMap.put("alt", Double.toString(location.getAltitude()));
            }
            paramsMap.put("tslr", Long.toString(location.getTime()));
            paramsMap.put("loc", "true");
            paramsMap.put("lsrc", location.getProvider());
        } else {
            paramsMap.put("loc", "false");
        }
    }

    public static void setUserLocation(Location userLocation) {
        if (userLocation != null) {
            location = userLocation;
        }
    }

    void getUrlParams(Map<String, String> paramsMap) {
        Iterator i$ = this.values.entrySet().iterator();
        while (i$.hasNext()) {
            Entry<String, String> entry = (Entry) i$.next();
            paramsMap.put(entry.getKey(), entry.getValue());
        }
        if (this.age != null) {
            paramsMap.put(KEY_AGE, this.age);
        }
        if (this.children != null) {
            paramsMap.put(KEY_CHILDREN, this.children);
        }
        if (this.education != null) {
            paramsMap.put(KEY_EDUCATION, this.education);
        }
        if (this.ethnicity != null) {
            paramsMap.put(KEY_ETHNICITY, this.ethnicity);
        }
        if (this.gender != null) {
            paramsMap.put(KEY_GENDER, this.gender);
        }
        if (this.income != null) {
            paramsMap.put(KEY_INCOME, this.income);
        }
        if (this.keywords != null) {
            paramsMap.put(KEY_KEYWORDS, this.keywords);
        }
        if (this.marital != null) {
            paramsMap.put(KEY_MARITAL_STATUS, this.marital);
        }
        if (this.politics != null) {
            paramsMap.put(KEY_POLITICS, this.politics);
        }
        if (this.vendor != null) {
            paramsMap.put(KEY_VENDOR, this.vendor);
        }
        if (this.zip != null) {
            paramsMap.put(KEY_ZIP_CODE, this.zip);
        }
    }

    public MMRequest put(String key, String value) {
        if (key.equals(KEY_AGE)) {
            this.age = value;
        } else if (key.equals(KEY_CHILDREN)) {
            this.children = value;
        } else if (key.equals(KEY_EDUCATION)) {
            this.education = value;
        } else if (key.equals(KEY_ETHNICITY)) {
            this.ethnicity = value;
        } else if (key.equals(KEY_GENDER)) {
            this.gender = value;
        } else if (key.equals(KEY_INCOME)) {
            this.income = value;
        } else if (key.equals(KEY_KEYWORDS)) {
            this.keywords = value;
        } else if (key.equals(KEY_MARITAL_STATUS)) {
            this.marital = value;
        } else if (key.equals(KEY_POLITICS)) {
            this.politics = value;
        } else if (key.equals(KEY_VENDOR)) {
            this.vendor = value;
        } else if (key.equals(KEY_ZIP_CODE)) {
            this.zip = value;
        } else if (value != null) {
            this.values.put(key, value);
        } else {
            this.values.remove(key);
        }
        return this;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setChildren(String children) {
        this.children = children;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public void setMarital(String marital) {
        this.marital = marital;
    }

    public void setMetaValues(Map<String, String> metaData) {
        if (metaData != null) {
            Iterator i$ = metaData.entrySet().iterator();
            while (i$.hasNext()) {
                Entry<String, String> entry = (Entry) i$.next();
                put((String) entry.getKey(), (String) entry.getValue());
            }
        }
    }

    public void setPolitics(String politics) {
        this.politics = politics;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public void setZip(String zipCode) {
        this.zip = zipCode;
    }
}