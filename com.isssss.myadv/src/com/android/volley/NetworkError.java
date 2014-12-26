package com.android.volley;

public class NetworkError extends VolleyError {
    public NetworkError(NetworkResponse networkResponse) {
        super(networkResponse);
    }

    public NetworkError(Throwable cause) {
        super(cause);
    }
}