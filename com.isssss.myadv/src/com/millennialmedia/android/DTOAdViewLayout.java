package com.millennialmedia.android;

import com.millennialmedia.google.gson.annotations.SerializedName;

class DTOAdViewLayout {
    int height;
    int width;
    @SerializedName("x")
    int xWindowPosition;
    @SerializedName("y")
    int yWindowPosition;

    DTOAdViewLayout(int x, int y, int width, int height) {
        this.xWindowPosition = x;
        this.yWindowPosition = y;
        this.width = width;
        this.height = height;
    }
}