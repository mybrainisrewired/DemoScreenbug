package com.google.android.gms.plus.model.people;

import com.google.android.gms.common.data.DataBuffer;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.common.data.c;
import com.google.android.gms.internal.ih;
import com.google.android.gms.internal.is;

public final class PersonBuffer extends DataBuffer<Person> {
    private final c<ih> Wr;

    public PersonBuffer(DataHolder dataHolder) {
        super(dataHolder);
        if (dataHolder.getMetadata() == null || !dataHolder.getMetadata().getBoolean("com.google.android.gms.plus.IsSafeParcelable", false)) {
            this.Wr = null;
        } else {
            this.Wr = new c(dataHolder, ih.CREATOR);
        }
    }

    public Person get(int position) {
        return this.Wr != null ? (Person) this.Wr.F(position) : new is(this.BB, position);
    }
}