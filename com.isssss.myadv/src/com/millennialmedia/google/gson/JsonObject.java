package com.millennialmedia.google.gson;

import com.millennialmedia.google.gson.internal.LinkedHashTreeMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public final class JsonObject extends JsonElement {
    private final LinkedHashTreeMap<String, JsonElement> members;

    public JsonObject() {
        this.members = new LinkedHashTreeMap();
    }

    private JsonElement createJsonElement(Object value) {
        return value == null ? JsonNull.INSTANCE : new JsonPrimitive(value);
    }

    public void add(String property, JsonElement value) {
        if (value == null) {
            value = JsonNull.INSTANCE;
        }
        this.members.put(property, value);
    }

    public void addProperty(String property, Boolean value) {
        add(property, createJsonElement(value));
    }

    public void addProperty(String property, Character value) {
        add(property, createJsonElement(value));
    }

    public void addProperty(String property, Number value) {
        add(property, createJsonElement(value));
    }

    public void addProperty(String property, String value) {
        add(property, createJsonElement(value));
    }

    JsonObject deepCopy() {
        JsonObject result = new JsonObject();
        Iterator i$ = this.members.entrySet().iterator();
        while (i$.hasNext()) {
            Entry<String, JsonElement> entry = (Entry) i$.next();
            result.add((String) entry.getKey(), ((JsonElement) entry.getValue()).deepCopy());
        }
        return result;
    }

    public Set<Entry<String, JsonElement>> entrySet() {
        return this.members.entrySet();
    }

    public boolean equals(JsonObject o) {
        return o == this || (o instanceof JsonObject && o.members.equals(this.members));
    }

    public JsonElement get(String memberName) {
        return (JsonElement) this.members.get(memberName);
    }

    public JsonArray getAsJsonArray(String memberName) {
        return (JsonArray) this.members.get(memberName);
    }

    public JsonObject getAsJsonObject(String memberName) {
        return (JsonObject) this.members.get(memberName);
    }

    public JsonPrimitive getAsJsonPrimitive(String memberName) {
        return (JsonPrimitive) this.members.get(memberName);
    }

    public boolean has(String memberName) {
        return this.members.containsKey(memberName);
    }

    public int hashCode() {
        return this.members.hashCode();
    }

    public JsonElement remove(String property) {
        return (JsonElement) this.members.remove(property);
    }
}