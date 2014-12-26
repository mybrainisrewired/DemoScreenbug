package android.support.v4.view;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.os.Parcelable;
import android.view.View;

public abstract class PagerAdapter {
    public static final int POSITION_NONE = -2;
    public static final int POSITION_UNCHANGED = -1;
    private DataSetObservable mObservable;

    public PagerAdapter() {
        this.mObservable = new DataSetObservable();
    }

    public void destroyItem(View container, int position, Object object) {
        throw new UnsupportedOperationException("Required method destroyItem was not overridden");
    }

    public void destroyItem(View container, int position, Object object) {
        destroyItem(container, position, object);
    }

    public void finishUpdate(View container) {
    }

    public void finishUpdate(View container) {
        finishUpdate(container);
    }

    public abstract int getCount();

    public int getItemPosition(Object object) {
        return POSITION_UNCHANGED;
    }

    public CharSequence getPageTitle(int position) {
        return null;
    }

    public float getPageWidth(int position) {
        return 1.0f;
    }

    public Object instantiateItem(View container, int position) {
        throw new UnsupportedOperationException("Required method instantiateItem was not overridden");
    }

    public Object instantiateItem(View container, int position) {
        return instantiateItem(container, position);
    }

    public abstract boolean isViewFromObject(View view, Object obj);

    public void notifyDataSetChanged() {
        this.mObservable.notifyChanged();
    }

    void registerDataSetObserver(DataSetObserver observer) {
        this.mObservable.registerObserver(observer);
    }

    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    public Parcelable saveState() {
        return null;
    }

    public void setPrimaryItem(View container, int position, Object object) {
    }

    public void setPrimaryItem(View container, int position, Object object) {
        setPrimaryItem(container, position, object);
    }

    public void startUpdate(View container) {
    }

    public void startUpdate(View container) {
        startUpdate(container);
    }

    void unregisterDataSetObserver(DataSetObserver observer) {
        this.mObservable.unregisterObserver(observer);
    }
}