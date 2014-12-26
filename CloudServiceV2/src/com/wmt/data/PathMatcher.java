package com.wmt.data;

import java.util.ArrayList;
import java.util.HashMap;

public class PathMatcher {
    public static final int NOT_FOUND = -1;
    private Node mRoot;
    private ArrayList<String> mVariables;

    private static class Node {
        private int mKind;
        private HashMap<String, Node> mMap;

        private Node() {
            this.mKind = -1;
        }

        Node addChild(String segment) {
            if (this.mMap == null) {
                this.mMap = new HashMap();
            } else {
                Node node = (Node) this.mMap.get(segment);
                if (node != null) {
                    return node;
                }
            }
            Node n = new Node();
            this.mMap.put(segment, n);
            return n;
        }

        Node getChild(String segment) {
            return this.mMap == null ? null : (Node) this.mMap.get(segment);
        }

        int getKind() {
            return this.mKind;
        }

        void setKind(int kind) {
            this.mKind = kind;
        }
    }

    public PathMatcher() {
        this.mVariables = new ArrayList();
        this.mRoot = new Node();
        this.mRoot = new Node();
    }

    public void add(String pattern, int kind) {
        String[] segments = Path.split(pattern);
        Node current = this.mRoot;
        int i = 0;
        while (i < segments.length) {
            current = current.addChild(segments[i]);
            i++;
        }
        current.setKind(kind);
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

    public int match(Path path) {
        String[] segments = path.split();
        this.mVariables.clear();
        Node current = this.mRoot;
        int i = 0;
        while (i < segments.length) {
            Node next = current.getChild(segments[i]);
            if (next == null) {
                next = current.getChild("*");
                if (next == null) {
                    return NOT_FOUND;
                }
                this.mVariables.add(segments[i]);
            }
            current = next;
            i++;
        }
        return current.getKind();
    }
}