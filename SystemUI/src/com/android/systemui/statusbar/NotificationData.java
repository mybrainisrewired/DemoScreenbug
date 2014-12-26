package com.android.systemui.statusbar;

import android.os.IBinder;
import android.view.View;
import android.widget.ImageView;
import com.android.internal.statusbar.StatusBarNotification;
import com.android.systemui.R;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

public class NotificationData {
    private final ArrayList<Entry> mEntries;
    private final Comparator<Entry> mEntryCmp;

    public static final class Entry {
        public View content;
        public View expanded;
        protected View expandedLarge;
        public StatusBarIconView icon;
        public IBinder key;
        public ImageView largeIcon;
        public StatusBarNotification notification;
        public View row;

        public Entry(IBinder key, StatusBarNotification n, StatusBarIconView ic) {
            this.key = key;
            this.notification = n;
            this.icon = ic;
        }

        public boolean expandable() {
            return NotificationData.getIsExpandable(this.row);
        }

        public View getLargeView() {
            return this.expandedLarge;
        }

        public void setLargeView(View expandedLarge) {
            this.expandedLarge = expandedLarge;
            NotificationData.writeBooleanTag(this.row, R.id.expandable_tag, expandedLarge != null);
        }

        public boolean setUserExpanded(boolean userExpanded) {
            return NotificationData.setUserExpanded(this.row, userExpanded);
        }

        public boolean userExpanded() {
            return NotificationData.getUserExpanded(this.row);
        }
    }

    public NotificationData() {
        this.mEntries = new ArrayList();
        this.mEntryCmp = new Comparator<Entry>() {
            public int compare(com.android.systemui.statusbar.NotificationData.Entry a, com.android.systemui.statusbar.NotificationData.Entry b) {
                StatusBarNotification na = a.notification;
                StatusBarNotification nb = b.notification;
                int d = na.score - nb.score;
                return d != 0 ? d : (int) (na.notification.when - nb.notification.when);
            }
        };
    }

    public static boolean getIsExpandable(View row) {
        return readBooleanTag(row, R.id.expandable_tag);
    }

    public static boolean getUserExpanded(View row) {
        return readBooleanTag(row, R.id.user_expanded_tag);
    }

    protected static boolean readBooleanTag(View view, int id) {
        if (view == null) {
            return false;
        }
        Object value = view.getTag(id);
        return value != null && value instanceof Boolean && ((Boolean) value).booleanValue();
    }

    public static boolean setUserExpanded(View row, boolean userExpanded) {
        return writeBooleanTag(row, R.id.user_expanded_tag, userExpanded);
    }

    protected static boolean writeBooleanTag(View view, int id, boolean value) {
        if (view == null) {
            return false;
        }
        view.setTag(id, Boolean.valueOf(value));
        return value;
    }

    public int add(IBinder key, StatusBarNotification notification, View row, View content, View expanded, StatusBarIconView icon) {
        Entry entry = new Entry();
        entry.key = key;
        entry.notification = notification;
        entry.row = row;
        entry.content = content;
        entry.expanded = expanded;
        entry.icon = icon;
        entry.largeIcon = null;
        return add(entry);
    }

    public int add(Entry entry) {
        int N = this.mEntries.size();
        int i = 0;
        while (i < N && this.mEntryCmp.compare(this.mEntries.get(i), entry) <= 0) {
            i++;
        }
        this.mEntries.add(i, entry);
        return i;
    }

    public Entry findByKey(IBinder key) {
        Iterator i$ = this.mEntries.iterator();
        while (i$.hasNext()) {
            Entry e = (Entry) i$.next();
            if (e.key == key) {
                return e;
            }
        }
        return null;
    }

    public Entry get(int i) {
        return (Entry) this.mEntries.get(i);
    }

    public boolean hasClearableItems() {
        Iterator i$ = this.mEntries.iterator();
        while (i$.hasNext()) {
            Entry e = (Entry) i$.next();
            if (e.expanded != null && e.notification.isClearable()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasVisibleItems() {
        Iterator i$ = this.mEntries.iterator();
        while (i$.hasNext()) {
            if (((Entry) i$.next()).expanded != null) {
                return true;
            }
        }
        return false;
    }

    public Entry remove(IBinder key) {
        Entry e = findByKey(key);
        if (e != null) {
            this.mEntries.remove(e);
        }
        return e;
    }

    public int size() {
        return this.mEntries.size();
    }
}