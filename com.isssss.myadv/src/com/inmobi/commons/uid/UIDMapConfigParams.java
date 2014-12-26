package com.inmobi.commons.uid;

import com.inmobi.commons.internal.InternalSDKUtil;
import java.util.Map;

public class UIDMapConfigParams {
    private Map<String, Boolean> a;
    private boolean b;
    private boolean c;
    private boolean d;
    private boolean e;
    private boolean f;
    private boolean g;
    private boolean h;
    private boolean i;
    private boolean j;

    public UIDMapConfigParams() {
        this.b = true;
        this.c = true;
        this.d = false;
        this.e = true;
        this.f = true;
        this.g = true;
        this.h = true;
        this.i = true;
        this.j = true;
    }

    public boolean getAIDL() {
        return this.j;
    }

    public boolean getFacebookID() {
        return this.d;
    }

    public boolean getGPID() {
        return this.h;
    }

    public boolean getIMID() {
        return this.i;
    }

    public boolean getLTVID() {
        return this.g;
    }

    public boolean getLoginID() {
        return this.b;
    }

    public Map<String, Boolean> getMap() {
        return this.a;
    }

    public boolean getODIN1() {
        return this.e;
    }

    public boolean getSessionID() {
        return this.c;
    }

    public boolean getUM5() {
        return this.f;
    }

    public void setAIDL(boolean z) {
        this.j = z;
    }

    public void setFacebookID(boolean z) {
        this.d = z;
    }

    public void setGPID(boolean z) {
        this.h = z;
    }

    public void setIMID(boolean z) {
        this.i = z;
    }

    public void setLTVID(boolean z) {
        this.g = z;
    }

    public void setLoginID(boolean z) {
        this.b = z;
    }

    public void setMap(Object obj) {
        Map map = (Map) obj;
        this.b = InternalSDKUtil.getBooleanFromMap(map, KEY_LOGIN_ID);
        this.c = InternalSDKUtil.getBooleanFromMap(map, KEY_SESSION_ID);
        this.d = InternalSDKUtil.getBooleanFromMap(map, KEY_FACEBOOK_ID);
        this.e = InternalSDKUtil.getBooleanFromMap(map, KEY_ODIN1);
        this.f = InternalSDKUtil.getBooleanFromMap(map, KEY_UM5_ID);
        this.g = InternalSDKUtil.getBooleanFromMap(map, KEY_LTVID);
        this.h = InternalSDKUtil.getBooleanFromMap(map, KEY_GPID);
        this.i = InternalSDKUtil.getBooleanFromMap(map, KEY_IMID);
        this.j = InternalSDKUtil.getBooleanFromMap(map, KEY_APPENDED_ID);
        this.a = (Map) obj;
    }

    public void setODIN1(boolean z) {
        this.e = z;
    }

    public void setSessionID(boolean z) {
        this.c = z;
    }

    public void setUM5(boolean z) {
        this.f = z;
    }
}