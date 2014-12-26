package com.wmt.util;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

public class CheckAccount {
    private static final String ACCOUNT_STR = "account";
    public static final int ADMIN = 0;
    private static final String ADMIN_STR = "admin";
    private static final Uri CONTENT_URI;
    public static final int GUEST = 1;
    private static final String TAG = "CheckAccount";

    static {
        CONTENT_URI = Uri.parse("content://com.wmt.AccountSwitcher.Provider/Account");
    }

    public static int getActiveAccount(ContentResolver resolver) {
        if (0 == 0 || null.equals("0") || null.equals("")) {
            Log.v(TAG, "do not support guest account");
            return 0;
        } else {
            Log.v(TAG, "getActiveAccount resolver = " + resolver + ", supportGuest = (" + null + ")");
            if (resolver == null) {
                return 1;
            }
            Cursor c = null;
            String str = null;
            try {
                c = resolver.query(CONTENT_URI, new String[]{ACCOUNT_STR}, null, null, null);
                if (c != null && c.getCount() > 0 && c.moveToFirst()) {
                    str = c.getString(ADMIN);
                }
                if (c != null) {
                    c.close();
                }
            } catch (Exception e) {
                str = null;
                try {
                    e.printStackTrace();
                    if (c != null) {
                        c.close();
                    }
                } catch (Throwable th) {
                    if (c != null) {
                        c.close();
                    }
                }
            }
            return (currentAccount == null || !currentAccount.equals(ADMIN_STR)) ? 1 : 0;
        }
    }
}