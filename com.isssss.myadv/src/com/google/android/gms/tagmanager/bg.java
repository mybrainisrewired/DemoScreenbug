package com.google.android.gms.tagmanager;

interface bg<T> {

    public enum a {
        NOT_AVAILABLE,
        IO_ERROR,
        SERVER_ERROR;

        static {
            Yy = new com.google.android.gms.tagmanager.bg.a("NOT_AVAILABLE", 0);
            Yz = new com.google.android.gms.tagmanager.bg.a("IO_ERROR", 1);
            YA = new com.google.android.gms.tagmanager.bg.a("SERVER_ERROR", 2);
            YB = new com.google.android.gms.tagmanager.bg.a[]{Yy, Yz, YA};
        }
    }

    void a(a aVar);

    void i(T t);

    void kl();
}