package com.wmt.data;

import android.net.Uri;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class EnhancedUriMatcher {
    private static final int EXACT = 0;
    public static final int NO_MATCH = -1;
    private static final int NUMBER = 1;
    static final Pattern PATH_SPLIT_PATTERN;
    private static final int TEXT = 2;
    private ArrayList<EnhancedUriMatcher> mChildren;
    private int mCode;
    private String mText;
    private ArrayList<String> mVariables;
    private int mWhich;

    static {
        PATH_SPLIT_PATTERN = Pattern.compile("/");
    }

    private EnhancedUriMatcher() {
        this.mCode = -1;
        this.mWhich = -1;
        this.mChildren = new ArrayList();
        this.mText = null;
    }

    public EnhancedUriMatcher(int code) {
        this.mCode = code;
        this.mWhich = -1;
        this.mChildren = new ArrayList();
        this.mVariables = new ArrayList();
        this.mText = null;
    }

    public void addURI(String authority, String path, int code) {
        if (code < 0) {
            throw new IllegalArgumentException("code " + code + " is invalid: it must be positive");
        }
        int numTokens;
        String[] tokens = path != null ? PATH_SPLIT_PATTERN.split(path) : null;
        if (tokens != null) {
            numTokens = tokens.length;
        } else {
            numTokens = 0;
        }
        EnhancedUriMatcher node = this;
        int i = NO_MATCH;
        while (i < numTokens) {
            String token = i < 0 ? authority : tokens[i];
            ArrayList<EnhancedUriMatcher> children = node.mChildren;
            int numChildren = children.size();
            int j = EXACT;
            while (j < numChildren) {
                EnhancedUriMatcher child = (EnhancedUriMatcher) children.get(j);
                if (token.equals(child.mText)) {
                    node = child;
                    break;
                } else {
                    j++;
                }
            }
            if (j == numChildren) {
                child = new EnhancedUriMatcher();
                if (token.equals("#")) {
                    child.mWhich = 1;
                } else if (token.equals("*")) {
                    child.mWhich = 2;
                } else {
                    child.mWhich = 0;
                }
                child.mText = token;
                node.mChildren.add(child);
                node = child;
            }
            i++;
        }
        node.mCode = code;
    }

    public int getIntVar(int index) {
        return Integer.parseInt((String) this.mVariables.get(index));
    }

    public long getLongVar(int index) {
        return Long.parseLong((String) this.mVariables.get(index));
    }

    public String getVar(int index) {
        return (String) this.mVariables.get(index);
    }

    public int match(Uri uri) {
        List<String> pathSegments = uri.getPathSegments();
        int li = pathSegments.size();
        EnhancedUriMatcher node = this;
        this.mVariables.clear();
        if (li == 0 && uri.getAuthority() == null) {
            return this.mCode;
        }
        int i = NO_MATCH;
        while (i < li) {
            String u = i < 0 ? uri.getAuthority() : (String) pathSegments.get(i);
            ArrayList<EnhancedUriMatcher> list = node.mChildren;
            if (list == null) {
                return node.mCode;
            }
            node = null;
            int lj = list.size();
            int j = EXACT;
            while (j < lj) {
                EnhancedUriMatcher n = (EnhancedUriMatcher) list.get(j);
                switch (n.mWhich) {
                    case EXACT:
                        if (n.mText.equals(u)) {
                            node = n;
                        }
                        break;
                    case NUMBER:
                        int lk = u.length();
                        int k = EXACT;
                        while (k < lk) {
                            char c = u.charAt(k);
                            if (c >= '0' && c <= '9') {
                                k++;
                            }
                        }
                        node = n;
                        this.mVariables.add(u);
                        break;
                    case TEXT:
                        node = n;
                        this.mVariables.add(u);
                        break;
                }
                if (node == null) {
                    j++;
                } else if (node == null) {
                    return NO_MATCH;
                } else {
                    i++;
                }
            }
            if (node == null) {
                return NO_MATCH;
            }
            i++;
        }
        return node.mCode;
    }
}