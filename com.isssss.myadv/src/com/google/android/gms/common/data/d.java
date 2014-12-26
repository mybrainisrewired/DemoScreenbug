package com.google.android.gms.common.data;

import java.util.ArrayList;

public abstract class d<T> extends DataBuffer<T> {
    private boolean BW;
    private ArrayList<Integer> BX;

    protected d(DataHolder dataHolder) {
        super(dataHolder);
        this.BW = false;
    }

    private void eu() {
        synchronized (this) {
            if (!this.BW) {
                int count = this.BB.getCount();
                this.BX = new ArrayList();
                if (count > 0) {
                    this.BX.add(Integer.valueOf(0));
                    String primaryDataMarkerColumn = getPrimaryDataMarkerColumn();
                    Object string = this.BB.getString(primaryDataMarkerColumn, 0, this.BB.G(0));
                    int i = 1;
                    while (i < count) {
                        String string2 = this.BB.getString(primaryDataMarkerColumn, i, this.BB.G(i));
                        if (string2.equals(str)) {
                            string2 = str;
                        } else {
                            this.BX.add(Integer.valueOf(i));
                        }
                        i++;
                        String str2 = string2;
                    }
                }
                this.BW = true;
            }
        }
    }

    int H(int i) {
        if (i >= 0 && i < this.BX.size()) {
            return ((Integer) this.BX.get(i)).intValue();
        }
        throw new IllegalArgumentException("Position " + i + " is out of bounds for this buffer");
    }

    protected int I(int i) {
        return (i < 0 || i == this.BX.size()) ? 0 : i == this.BX.size() + -1 ? this.BB.getCount() - ((Integer) this.BX.get(i)).intValue() : ((Integer) this.BX.get(i + 1)).intValue() - ((Integer) this.BX.get(i)).intValue();
    }

    protected abstract T c(int i, int i2);

    public final T get(int position) {
        eu();
        return c(H(position), I(position));
    }

    public int getCount() {
        eu();
        return this.BX.size();
    }

    protected abstract String getPrimaryDataMarkerColumn();
}