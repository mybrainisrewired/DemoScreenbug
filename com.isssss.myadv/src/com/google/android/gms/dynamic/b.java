package com.google.android.gms.dynamic;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.android.gms.dynamic.c.a;

public final class b extends a {
    private Fragment Hv;

    private b(Fragment fragment) {
        this.Hv = fragment;
    }

    public static b a(Fragment fragment) {
        return fragment != null ? new b(fragment) : null;
    }

    public void b(d dVar) {
        this.Hv.registerForContextMenu((View) e.d(dVar));
    }

    public void c(d dVar) {
        this.Hv.unregisterForContextMenu((View) e.d(dVar));
    }

    public d fX() {
        return e.h(this.Hv.getActivity());
    }

    public c fY() {
        return a(this.Hv.getParentFragment());
    }

    public d fZ() {
        return e.h(this.Hv.getResources());
    }

    public c ga() {
        return a(this.Hv.getTargetFragment());
    }

    public Bundle getArguments() {
        return this.Hv.getArguments();
    }

    public int getId() {
        return this.Hv.getId();
    }

    public boolean getRetainInstance() {
        return this.Hv.getRetainInstance();
    }

    public String getTag() {
        return this.Hv.getTag();
    }

    public int getTargetRequestCode() {
        return this.Hv.getTargetRequestCode();
    }

    public boolean getUserVisibleHint() {
        return this.Hv.getUserVisibleHint();
    }

    public d getView() {
        return e.h(this.Hv.getView());
    }

    public boolean isAdded() {
        return this.Hv.isAdded();
    }

    public boolean isDetached() {
        return this.Hv.isDetached();
    }

    public boolean isHidden() {
        return this.Hv.isHidden();
    }

    public boolean isInLayout() {
        return this.Hv.isInLayout();
    }

    public boolean isRemoving() {
        return this.Hv.isRemoving();
    }

    public boolean isResumed() {
        return this.Hv.isResumed();
    }

    public boolean isVisible() {
        return this.Hv.isVisible();
    }

    public void setHasOptionsMenu(boolean hasMenu) {
        this.Hv.setHasOptionsMenu(hasMenu);
    }

    public void setMenuVisibility(boolean menuVisible) {
        this.Hv.setMenuVisibility(menuVisible);
    }

    public void setRetainInstance(boolean retain) {
        this.Hv.setRetainInstance(retain);
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        this.Hv.setUserVisibleHint(isVisibleToUser);
    }

    public void startActivity(Intent intent) {
        this.Hv.startActivity(intent);
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        this.Hv.startActivityForResult(intent, requestCode);
    }
}