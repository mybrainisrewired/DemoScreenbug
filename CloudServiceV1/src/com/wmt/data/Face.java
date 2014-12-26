package com.wmt.data;

import com.wmt.util.Utils;

public class Face implements Comparable<Face> {
    private String mName;
    private String mPersonId;

    public Face(String name, String personId) {
        this.mName = name;
        this.mPersonId = personId;
        boolean z = (this.mName == null || this.mPersonId == null) ? false : true;
        Utils.assertTrue(z);
    }

    public int compareTo(Face another) {
        return this.mPersonId.compareTo(another.mPersonId);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Face)) {
            return false;
        }
        return this.mPersonId.equals(((Face) obj).mPersonId);
    }

    public String getName() {
        return this.mName;
    }

    public String getPersonId() {
        return this.mPersonId;
    }

    public int hashCode() {
        return this.mPersonId.hashCode();
    }
}