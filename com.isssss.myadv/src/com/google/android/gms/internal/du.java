package com.google.android.gms.internal;

import android.content.Context;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public final class du extends do_ {
    private final String lh;
    private final Context mContext;
    private final String ro;

    public du(Context context, String str, String str2) {
        this.mContext = context;
        this.lh = str;
        this.ro = str2;
    }

    public void aY() {
        try {
            dw.y("Pinging URL: " + this.ro);
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(this.ro).openConnection();
            dq.a(this.mContext, this.lh, true, httpURLConnection);
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode < 200 || responseCode >= 300) {
                dw.z("Received non-success response code " + responseCode + " from pinging URL: " + this.ro);
            }
            httpURLConnection.disconnect();
        } catch (IndexOutOfBoundsException e) {
            dw.z("Error while parsing ping URL: " + this.ro + ". " + e.getMessage());
        } catch (IOException e2) {
            dw.z("Error while pinging URL: " + this.ro + ". " + e2.getMessage());
        }
    }

    public void onStop() {
    }
}