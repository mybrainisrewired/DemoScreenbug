package com.mopub.mobileads;

import com.mopub.common.Preconditions;

abstract class MraidProperty {
    MraidProperty() {
    }

    private String sanitize(String str) {
        return str != null ? str.replaceAll("[^a-zA-Z0-9_,:\\s\\{\\}\\'\\\"]", Preconditions.EMPTY_ARGUMENTS) : Preconditions.EMPTY_ARGUMENTS;
    }

    public abstract String toJsonPair();

    public String toString() {
        return sanitize(toJsonPair());
    }
}