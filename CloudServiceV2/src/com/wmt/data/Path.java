package com.wmt.data;

import android.util.Log;
import com.wmt.data.utils.IdentityCache;
import com.wmt.util.Utils;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

public class Path {
    private static final String TAG = "Path";
    private static Path sRoot;
    private String mCachedPathString;
    private IdentityCache<String, Path> mChildren;
    private WeakReference<MediaObject> mObject;
    private final Path mParent;
    private final String mSegment;

    static {
        sRoot = new Path(null, "ROOT");
    }

    private Path(Path parent, String segment) {
        this.mCachedPathString = null;
        this.mParent = parent;
        this.mSegment = segment;
    }

    static void clearAll() {
        synchronized (Path.class) {
            sRoot = new Path(null, "");
        }
    }

    static void dumpAll() {
        dumpAll(sRoot, "", "");
    }

    static void dumpAll(Path p, String prefix1, String prefix2) {
        synchronized (Path.class) {
            String str;
            MediaObject obj = p.getObject();
            String str2 = TAG;
            StringBuilder append = new StringBuilder().append(prefix1).append(p.mSegment).append(":");
            if (obj == null) {
                str = "null";
            } else {
                str = obj.getClass().getSimpleName();
            }
            Log.d(str2, append.append(str).toString());
            if (p.mChildren != null) {
                ArrayList<String> childrenKeys = p.mChildren.keys();
                int i = 0;
                int n = childrenKeys.size();
                Iterator i$ = childrenKeys.iterator();
                while (i$.hasNext()) {
                    Path child = (Path) p.mChildren.get((String) i$.next());
                    if (child == null) {
                        i++;
                    } else {
                        Log.d(TAG, prefix2 + "|");
                        i++;
                        if (i < n) {
                            dumpAll(child, prefix2 + "+-- ", prefix2 + "|   ");
                        } else {
                            dumpAll(child, prefix2 + "+-- ", prefix2 + "    ");
                        }
                    }
                }
            }
        }
    }

    public static Path fromString(String s) {
        Path current;
        synchronized (Path.class) {
            String[] segments = split(s);
            current = sRoot;
            int i = 0;
            while (i < segments.length) {
                current = current.getChild(segments[i]);
                i++;
            }
        }
        return current;
    }

    public static String[] split(String s) {
        int n = s.length();
        if (n == 0) {
            return new String[0];
        }
        if (s.charAt(0) != '/') {
            throw new RuntimeException("malformed path:" + s);
        }
        ArrayList<String> segments = new ArrayList();
        int i = 1;
        while (i < n) {
            int brace = 0;
            int j = i;
            while (j < n) {
                char c = s.charAt(j);
                if (c != '{') {
                    if (c != '}') {
                        if (brace == 0 && c == '/') {
                            break;
                        }
                    } else {
                        brace--;
                    }
                } else {
                    brace++;
                }
                j++;
            }
            if (brace != 0) {
                throw new RuntimeException("unbalanced brace in path:" + s);
            }
            segments.add(s.substring(i, j));
            i = j + 1;
        }
        String[] result = new String[segments.size()];
        segments.toArray(result);
        return result;
    }

    public static String[] splitSequence(String s) {
        int n = s.length();
        if (s.charAt(0) == '{' && s.charAt(n - 1) == '}') {
            ArrayList<String> segments = new ArrayList();
            int i = 1;
            while (i < n - 1) {
                int brace = 0;
                int j = i;
                while (j < n - 1) {
                    char c = s.charAt(j);
                    if (c != '{') {
                        if (c != '}') {
                            if (brace == 0 && c == ',') {
                                break;
                            }
                        } else {
                            brace--;
                        }
                    } else {
                        brace++;
                    }
                    j++;
                }
                if (brace != 0) {
                    throw new RuntimeException("unbalanced brace in path:" + s);
                }
                segments.add(s.substring(i, j));
                i = j + 1;
            }
            String[] result = new String[segments.size()];
            segments.toArray(result);
            return result;
        } else {
            throw new RuntimeException("bad sequence: " + s);
        }
    }

    public Path getChild(int segment) {
        return getChild(String.valueOf(segment));
    }

    public Path getChild(long segment) {
        return getChild(String.valueOf(segment));
    }

    public Path getChild(String segment) {
        synchronized (Path.class) {
            Path p;
            if (this.mChildren == null) {
                this.mChildren = new IdentityCache();
            } else {
                p = this.mChildren.get(segment);
                if (p != null) {
                    return p;
                }
            }
            p = new Path(this, segment);
            this.mChildren.put(segment, p);
            return p;
        }
    }

    public MediaObject getObject() {
        MediaObject mediaObject;
        synchronized (Path.class) {
            mediaObject = this.mObject == null ? null : (MediaObject) this.mObject.get();
        }
        return mediaObject;
    }

    public Path getParent() {
        Path path;
        synchronized (Path.class) {
            path = this.mParent;
        }
        return path;
    }

    public String getPrefix() {
        String str;
        synchronized (Path.class) {
            Path current;
            if (this == sRoot) {
                str = "";
            }
            while (current.mParent != sRoot) {
                current = current.mParent;
            }
            str = current.mSegment;
        }
        return str;
    }

    public String getSuffix() {
        return this.mSegment;
    }

    public String getSuffix(int level) {
        Path p = this;
        int level2 = level;
        while (true) {
            level = level2 - 1;
            if (level2 == 0) {
                return p.mSegment;
            }
            p = p.mParent;
            level2 = level;
        }
    }

    public void setObject(MediaObject object) {
        synchronized (Path.class) {
            boolean z = this.mObject == null || this.mObject.get() == null;
            Utils.assertTrue(z);
            this.mObject = new WeakReference(object);
        }
    }

    public String[] split() {
        String[] segments;
        synchronized (Path.class) {
            int n = 0;
            Path p = this;
            while (p != sRoot) {
                n++;
                p = p.mParent;
            }
            segments = new String[n];
            p = this;
            int i = n - 1;
            while (p != sRoot) {
                int i2 = i - 1;
                segments[i] = p.mSegment;
                p = p.mParent;
                i = i2;
            }
        }
        return segments;
    }

    public String toString() {
        String str;
        synchronized (Path.class) {
            if (this.mCachedPathString == null) {
                StringBuilder sb = new StringBuilder();
                String[] segments = split();
                int i = 0;
                while (i < segments.length) {
                    sb.append("/");
                    sb.append(segments[i]);
                    i++;
                }
                this.mCachedPathString = sb.toString();
            }
            str = this.mCachedPathString;
        }
        return str;
    }
}