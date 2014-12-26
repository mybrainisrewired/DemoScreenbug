package com.google.android.gms.internal;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import com.millennialmedia.android.MMAdView;

public final class bz {
    public static boolean a(Context context, cb cbVar, ci ciVar) {
        if (cbVar == null) {
            dw.z("No intent data for launcher overlay.");
            return false;
        } else {
            Intent intent = new Intent();
            if (TextUtils.isEmpty(cbVar.nO)) {
                dw.z("Open GMSG did not contain a URL.");
                return false;
            } else {
                if (TextUtils.isEmpty(cbVar.mimeType)) {
                    intent.setData(Uri.parse(cbVar.nO));
                } else {
                    intent.setDataAndType(Uri.parse(cbVar.nO), cbVar.mimeType);
                }
                intent.setAction("android.intent.action.VIEW");
                if (!TextUtils.isEmpty(cbVar.packageName)) {
                    intent.setPackage(cbVar.packageName);
                }
                if (!TextUtils.isEmpty(cbVar.nP)) {
                    String[] split = cbVar.nP.split("/", MMAdView.TRANSITION_UP);
                    if (split.length < 2) {
                        dw.z("Could not parse component name from open GMSG: " + cbVar.nP);
                        return false;
                    } else {
                        intent.setClassName(split[0], split[1]);
                    }
                }
                try {
                    dw.y("Launching an intent: " + intent);
                    context.startActivity(intent);
                    ciVar.U();
                    return true;
                } catch (ActivityNotFoundException e) {
                    dw.z(e.getMessage());
                    return false;
                }
            }
        }
    }
}