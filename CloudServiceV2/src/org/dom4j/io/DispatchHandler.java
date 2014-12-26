package org.dom4j.io;

import java.util.ArrayList;
import java.util.HashMap;
import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;

class DispatchHandler implements ElementHandler {
    private boolean atRoot;
    private ElementHandler defaultHandler;
    private ArrayList handlerStack;
    private HashMap handlers;
    private String path;
    private ArrayList pathStack;

    public DispatchHandler() {
        this.atRoot = true;
        this.path = "/";
        this.pathStack = new ArrayList();
        this.handlerStack = new ArrayList();
        this.handlers = new HashMap();
    }

    public void addHandler(String handlerPath, ElementHandler handler) {
        this.handlers.put(handlerPath, handler);
    }

    public boolean containsHandler(String handlerPath) {
        return this.handlers.containsKey(handlerPath);
    }

    public int getActiveHandlerCount() {
        return this.handlerStack.size();
    }

    public ElementHandler getHandler(String handlerPath) {
        return (ElementHandler) this.handlers.get(handlerPath);
    }

    public String getPath() {
        return this.path;
    }

    public void onEnd(ElementPath elementPath) {
        if (this.handlers != null && this.handlers.containsKey(this.path)) {
            ElementHandler handler = (ElementHandler) this.handlers.get(this.path);
            this.handlerStack.remove(this.handlerStack.size() - 1);
            handler.onEnd(elementPath);
        } else if (this.handlerStack.isEmpty() && this.defaultHandler != null) {
            this.defaultHandler.onEnd(elementPath);
        }
        this.path = (String) this.pathStack.remove(this.pathStack.size() - 1);
        if (this.pathStack.size() == 0) {
            this.atRoot = true;
        }
    }

    public void onStart(ElementPath elementPath) {
        Element element = elementPath.getCurrent();
        this.pathStack.add(this.path);
        if (this.atRoot) {
            this.path += element.getName();
            this.atRoot = false;
        } else {
            this.path += "/" + element.getName();
        }
        if (this.handlers != null && this.handlers.containsKey(this.path)) {
            ElementHandler handler = (ElementHandler) this.handlers.get(this.path);
            this.handlerStack.add(handler);
            handler.onStart(elementPath);
        } else if (this.handlerStack.isEmpty() && this.defaultHandler != null) {
            this.defaultHandler.onStart(elementPath);
        }
    }

    public ElementHandler removeHandler(String handlerPath) {
        return (ElementHandler) this.handlers.remove(handlerPath);
    }

    public void resetHandlers() {
        this.atRoot = true;
        this.path = "/";
        this.pathStack.clear();
        this.handlerStack.clear();
        this.handlers.clear();
        this.defaultHandler = null;
    }

    public void setDefaultHandler(ElementHandler handler) {
        this.defaultHandler = handler;
    }
}