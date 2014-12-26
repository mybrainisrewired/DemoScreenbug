package com.mopub.mobileads.util;

import org.apache.http.client.HttpClient;

public class HttpClients {

    class AnonymousClass_1 implements Runnable {
        private final /* synthetic */ HttpClient val$httpClient;

        AnonymousClass_1(HttpClient httpClient) {
            this.val$httpClient = httpClient;
        }

        public void run() {
            if (this.val$httpClient != null && this.val$httpClient.getConnectionManager() != null) {
                this.val$httpClient.getConnectionManager().shutdown();
            }
        }
    }

    public static void safeShutdown(HttpClient httpClient) {
        new Thread(new AnonymousClass_1(httpClient)).start();
    }
}