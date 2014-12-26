package com.google.android.gms.dynamic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import com.google.android.gms.dynamic.c.a;

public final class h extends a {
    private Fragment Hz;

    private h(Fragment fragment) {
        this.Hz = fragment;
    }

    public static h a(Fragment fragment) {
        return fragment != null ? new h(fragment) : null;
    }

    public void b(d dVar) {
        this.Hz.registerForContextMenu((View) e.d(dVar));
    }

    public void c(d dVar) {
        this.Hz.unregisterForContextMenu((View) e.d(dVar));
    }

    public d fX() {
        return e.h(this.Hz.getActivity());
    }

    public c fY() {
        return a(this.Hz.getParentFragment());
    }

    public d fZ() {
        return e.h(this.Hz.getResources());
    }

    public c ga() {
        return a(this.Hz.getTargetFragment());
    }

    public Bundle getArguments() {
        return this.Hz.getArguments();
    }

    public int getId() {
        return this.Hz.getId();
    }

    public boolean getRetainInstance() {
        return this.Hz.getRetainInstance();
    }

    public String getTag() {
        return this.Hz.getTag();
    }

    public int getTargetRequestCode() {
        return this.Hz.getTargetRequestCode();
    }

    public boolean getUserVisibleHint() {
        return this.Hz.getUserVisibleHint();
    }

    public d getView() {
        return e.h(this.Hz.getView());
    }

    public boolean isAdded() {
        return this.Hz.isAdded();
    }

    public boolean isDetached() {
        return this.Hz.isDetached();
    }

    public boolean isHidden() {
        return this.Hz.isHidden();
    }

    public boolean isInLayout() {
        return this.Hz.isInLayout();
    }

    public boolean isRemoving() {
        return this.Hz.isRemoving();
    }

    public boolean isResumed() {
        return this.Hz.isResumed();
    }

    public boolean isVisible() {
        return this.Hz.isVisible();
    }

    public void setHasOptionsMenu(boolean hasMenu) {
        this.Hz.setHasOptionsMenu(hasMenu);
    }

    public void setMenuVisibility(boolean menuVisible) {
        this.Hz.setMenuVisibility(menuVisible);
    }

    public void setRetainInstance(boolean retain) {
        this.Hz.setRetainInstance(retain);
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        this.Hz.setUserVisibleHint(isVisibleToUser);
    }

    public void startActivity(Intent intent) {
        this.Hz.startActivity(intent);
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        this.Hz.startActivityForResult(intent, requestCode);
    }
}