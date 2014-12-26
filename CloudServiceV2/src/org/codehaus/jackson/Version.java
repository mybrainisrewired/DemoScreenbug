package org.codehaus.jackson;

import org.codehaus.jackson.org.objectweb.asm.signature.SignatureVisitor;

public class Version implements Comparable<Version> {
    private static final Version UNKNOWN_VERSION;
    protected final int _majorVersion;
    protected final int _minorVersion;
    protected final int _patchLevel;
    protected final String _snapshotInfo;

    static {
        UNKNOWN_VERSION = new Version(0, 0, 0, null);
    }

    public Version(int major, int minor, int patchLevel, String snapshotInfo) {
        this._majorVersion = major;
        this._minorVersion = minor;
        this._patchLevel = patchLevel;
        this._snapshotInfo = snapshotInfo;
    }

    public static Version unknownVersion() {
        return UNKNOWN_VERSION;
    }

    public int compareTo(Version other) {
        int diff = this._majorVersion - other._majorVersion;
        if (diff != 0) {
            return diff;
        }
        diff = this._minorVersion - other._minorVersion;
        return diff == 0 ? this._patchLevel - other._patchLevel : diff;
    }

    public boolean equals(Version o) {
        if (o == this) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (o.getClass() != getClass()) {
            return false;
        }
        Version other = o;
        return other._majorVersion == this._majorVersion && other._minorVersion == this._minorVersion && other._patchLevel == this._patchLevel;
    }

    public int getMajorVersion() {
        return this._majorVersion;
    }

    public int getMinorVersion() {
        return this._minorVersion;
    }

    public int getPatchLevel() {
        return this._patchLevel;
    }

    public int hashCode() {
        return this._majorVersion + this._minorVersion + this._patchLevel;
    }

    public boolean isSnapshot() {
        return this._snapshotInfo != null && this._snapshotInfo.length() > 0;
    }

    public boolean isUknownVersion() {
        return this == UNKNOWN_VERSION;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this._majorVersion).append('.');
        sb.append(this._minorVersion).append('.');
        sb.append(this._patchLevel);
        if (isSnapshot()) {
            sb.append(SignatureVisitor.SUPER).append(this._snapshotInfo);
        }
        return sb.toString();
    }
}