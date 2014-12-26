package com.wmt.widget.media;

import android.net.Uri;
import com.wmt.data.ContentListener;
import com.wmt.data.MediaItem;
import java.util.ArrayList;

public class EmptySource implements WidgetSource {
    public void close() {
    }

    public Uri getContentUri(int index) {
        return null;
    }

    public MediaItem getMediaItem(int index) {
        return null;
    }

    public ArrayList<String> getPlayList() {
        return null;
    }

    public void reload() {
    }

    public void setContentListener(ContentListener listener) {
    }

    public int size() {
        return 0;
    }
}