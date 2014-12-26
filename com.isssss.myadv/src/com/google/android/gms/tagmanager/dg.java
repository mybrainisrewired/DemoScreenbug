package com.google.android.gms.tagmanager;

class dg extends Number implements Comparable<dg> {
    private double aaC;
    private long aaD;
    private boolean aaE;

    private dg(double d) {
        this.aaC = d;
        this.aaE = false;
    }

    private dg(long j) {
        this.aaD = j;
        this.aaE = true;
    }

    public static dg a(Double d) {
        return new dg(d.doubleValue());
    }

    public static dg bW(String str) throws NumberFormatException {
        try {
            return new dg(Long.parseLong(str));
        } catch (NumberFormatException e) {
            try {
                return new dg(Double.parseDouble(str));
            } catch (NumberFormatException e2) {
                throw new NumberFormatException(str + " is not a valid TypedNumber");
            }
        }
    }

    public static dg w(long j) {
        return new dg(j);
    }

    public int a(dg dgVar) {
        return (lJ() && dgVar.lJ()) ? new Long(this.aaD).compareTo(Long.valueOf(dgVar.aaD)) : Double.compare(doubleValue(), dgVar.doubleValue());
    }

    public byte byteValue() {
        return (byte) ((int) longValue());
    }

    public /* synthetic */ int compareTo(Object x0) {
        return a((dg) x0);
    }

    public double doubleValue() {
        return lJ() ? (double) this.aaD : this.aaC;
    }

    public boolean equals(Object other) {
        return other instanceof dg && a((dg) other) == 0;
    }

    public float floatValue() {
        return (float) doubleValue();
    }

    public int hashCode() {
        return new Long(longValue()).hashCode();
    }

    public int intValue() {
        return lL();
    }

    public boolean lI() {
        return !lJ();
    }

    public boolean lJ() {
        return this.aaE;
    }

    public long lK() {
        return lJ() ? this.aaD : (long) this.aaC;
    }

    public int lL() {
        return (int) longValue();
    }

    public short lM() {
        return (short) ((int) longValue());
    }

    public long longValue() {
        return lK();
    }

    public short shortValue() {
        return lM();
    }

    public String toString() {
        return lJ() ? Long.toString(this.aaD) : Double.toString(this.aaC);
    }
}