package com.google.android.gms.drive.metadata.internal;

import android.os.Bundle;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.drive.metadata.b;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.json.JSONArray;
import org.json.JSONException;

public class i extends b<String> {
    public i(String str, int i) {
        super(str, i);
    }

    public static final Collection<String> ay(String str) throws JSONException {
        if (str == null) {
            return null;
        }
        Collection arrayList = new ArrayList();
        JSONArray jSONArray = new JSONArray(str);
        int i = 0;
        while (i < jSONArray.length()) {
            arrayList.add(jSONArray.getString(i));
            i++;
        }
        return Collections.unmodifiableCollection(arrayList);
    }

    protected void a(Bundle bundle, Collection<String> collection) {
        bundle.putStringArrayList(getName(), new ArrayList(collection));
    }

    protected /* synthetic */ Object b(DataHolder dataHolder, int i, int i2) {
        return c(dataHolder, i, i2);
    }

    protected Collection<String> c(DataHolder dataHolder, int i, int i2) {
        try {
            return ay(dataHolder.getString(getName(), i, i2));
        } catch (JSONException e) {
            throw new IllegalStateException("DataHolder supplied invalid JSON", e);
        }
    }

    protected /* synthetic */ Object e(Bundle bundle) {
        return j(bundle);
    }

    protected Collection<String> j(Bundle bundle) {
        return bundle.getStringArrayList(getName());
    }
}