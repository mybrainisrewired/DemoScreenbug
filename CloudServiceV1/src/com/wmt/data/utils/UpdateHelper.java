package com.wmt.data.utils;

import com.wmt.util.Utils;

public class UpdateHelper {
    private boolean mUpdated;

    public UpdateHelper() {
        this.mUpdated = false;
    }

    public boolean isUpdated() {
        return this.mUpdated;
    }

    public double update(double original, double update) {
        if (original == update) {
            return original;
        }
        this.mUpdated = true;
        return update;
    }

    public double update(float original, float update) {
        if (original != update) {
            this.mUpdated = true;
            original = update;
        }
        return (double) original;
    }

    public int update(int original, int update) {
        if (original == update) {
            return original;
        }
        this.mUpdated = true;
        return update;
    }

    public long update(long original, long update) {
        if (original == update) {
            return original;
        }
        this.mUpdated = true;
        return update;
    }

    public <T> T update(T original, T update) {
        if (Utils.equals(original, update)) {
            return original;
        }
        this.mUpdated = true;
        return update;
    }
}