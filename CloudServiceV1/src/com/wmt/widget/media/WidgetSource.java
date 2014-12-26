package com.wmt.widget.media;

import android.net.Uri;
import com.wmt.data.ContentListener;
import com.wmt.data.MediaItem;
import java.util.ArrayList;

public interface WidgetSource {
    void close();

    Uri getContentUri(int i);

    MediaItem getMediaItem(int i);

    ArrayList<String> getPlayList();

    void reload();

    void setContentListener(ContentListener contentListener);

    int size();
}