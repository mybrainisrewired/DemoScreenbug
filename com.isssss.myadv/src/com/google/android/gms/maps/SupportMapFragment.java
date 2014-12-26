package com.google.android.gms.maps;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.dynamic.LifecycleDelegate;
import com.google.android.gms.dynamic.e;
import com.google.android.gms.dynamic.f;
import com.google.android.gms.internal.fq;
import com.google.android.gms.maps.internal.IGoogleMapDelegate;
import com.google.android.gms.maps.internal.IMapFragmentDelegate;
import com.google.android.gms.maps.internal.u;
import com.google.android.gms.maps.model.RuntimeRemoteException;

public class SupportMapFragment extends Fragment {
    private GoogleMap RT;
    private final b Sw;

    static class a implements LifecycleDelegate {
        private final Fragment Hz;
        private final IMapFragmentDelegate RU;

        public a(Fragment fragment, IMapFragmentDelegate iMapFragmentDelegate) {
            this.RU = (IMapFragmentDelegate) fq.f(iMapFragmentDelegate);
            this.Hz = (Fragment) fq.f(fragment);
        }

        public IMapFragmentDelegate io() {
            return this.RU;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onCreate(android.os.Bundle r5_savedInstanceState) {
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.maps.SupportMapFragment.a.onCreate(android.os.Bundle):void");
            /*
            r4 = this;
            if (r5 != 0) goto L_0x0008;
        L_0x0002:
            r0 = new android.os.Bundle;	 Catch:{ RemoteException -> 0x0029 }
            r0.<init>();	 Catch:{ RemoteException -> 0x0029 }
            r5 = r0;
        L_0x0008:
            r1 = r4.Hz;	 Catch:{ RemoteException -> 0x0029 }
            r1 = r1.getArguments();	 Catch:{ RemoteException -> 0x0029 }
            if (r1 == 0) goto L_0x0023;
        L_0x0010:
            r2 = "MapOptions";
            r2 = r1.containsKey(r2);	 Catch:{ RemoteException -> 0x0029 }
            if (r2 == 0) goto L_0x0023;
        L_0x0018:
            r2 = "MapOptions";
            r3 = "MapOptions";
            r1 = r1.getParcelable(r3);	 Catch:{ RemoteException -> 0x0029 }
            com.google.android.gms.maps.internal.t.a(r5, r2, r1);	 Catch:{ RemoteException -> 0x0029 }
        L_0x0023:
            r1 = r4.RU;	 Catch:{ RemoteException -> 0x0029 }
            r1.onCreate(r5);	 Catch:{ RemoteException -> 0x0029 }
            return;
        L_0x0029:
            r1 = move-exception;
            r2 = new com.google.android.gms.maps.model.RuntimeRemoteException;
            r2.<init>(r1);
            throw r2;
            */
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            try {
                return (View) e.d(this.RU.onCreateView(e.h(inflater), e.h(container), savedInstanceState));
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onDestroy() {
            try {
                this.RU.onDestroy();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onDestroyView() {
            try {
                this.RU.onDestroyView();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onInflate(Activity activity, Bundle attrs, Bundle savedInstanceState) {
            try {
                this.RU.onInflate(e.h(activity), (GoogleMapOptions) attrs.getParcelable("MapOptions"), savedInstanceState);
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onLowMemory() {
            try {
                this.RU.onLowMemory();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onPause() {
            try {
                this.RU.onPause();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onResume() {
            try {
                this.RU.onResume();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onSaveInstanceState(Bundle outState) {
            try {
                this.RU.onSaveInstanceState(outState);
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onStart() {
        }

        public void onStop() {
        }
    }

    static class b extends com.google.android.gms.dynamic.a<a> {
        private final Fragment Hz;
        protected f<a> RV;
        private Activity nS;

        b(Fragment fragment) {
            this.Hz = fragment;
        }

        private void setActivity(Activity activity) {
            this.nS = activity;
            ip();
        }

        protected void a(f<a> fVar) {
            this.RV = fVar;
            ip();
        }

        public void ip() {
            if (this.nS != null && this.RV != null && fW() == null) {
                try {
                    MapsInitializer.initialize(this.nS);
                    this.RV.a(new a(this.Hz, u.A(this.nS).h(e.h(this.nS))));
                } catch (RemoteException e) {
                    throw new RuntimeRemoteException(e);
                } catch (GooglePlayServicesNotAvailableException e2) {
                }
            }
        }
    }

    public SupportMapFragment() {
        this.Sw = new b(this);
    }

    public static SupportMapFragment newInstance() {
        return new SupportMapFragment();
    }

    public static SupportMapFragment newInstance(GoogleMapOptions options) {
        SupportMapFragment supportMapFragment = new SupportMapFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("MapOptions", options);
        supportMapFragment.setArguments(bundle);
        return supportMapFragment;
    }

    public final GoogleMap getMap() {
        IMapFragmentDelegate io = io();
        if (io == null) {
            return null;
        }
        try {
            IGoogleMapDelegate map = io.getMap();
            if (map == null) {
                return null;
            }
            if (this.RT == null || this.RT.if().asBinder() != map.asBinder()) {
                this.RT = new GoogleMap(map);
            }
            return this.RT;
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    protected IMapFragmentDelegate io() {
        this.Sw.ip();
        return this.Sw.fW() == null ? null : ((a) this.Sw.fW()).io();
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState.setClassLoader(SupportMapFragment.class.getClassLoader());
        }
        super.onActivityCreated(savedInstanceState);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.Sw.setActivity(activity);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.Sw.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return this.Sw.onCreateView(inflater, container, savedInstanceState);
    }

    public void onDestroy() {
        this.Sw.onDestroy();
        super.onDestroy();
    }

    public void onDestroyView() {
        this.Sw.onDestroyView();
        super.onDestroyView();
    }

    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);
        this.Sw.setActivity(activity);
        Parcelable createFromAttributes = GoogleMapOptions.createFromAttributes(activity, attrs);
        Bundle bundle = new Bundle();
        bundle.putParcelable("MapOptions", createFromAttributes);
        this.Sw.onInflate(activity, bundle, savedInstanceState);
    }

    public void onLowMemory() {
        this.Sw.onLowMemory();
        super.onLowMemory();
    }

    public void onPause() {
        this.Sw.onPause();
        super.onPause();
    }

    public void onResume() {
        super.onResume();
        this.Sw.onResume();
    }

    public void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            outState.setClassLoader(SupportMapFragment.class.getClassLoader());
        }
        super.onSaveInstanceState(outState);
        this.Sw.onSaveInstanceState(outState);
    }

    public void setArguments(Bundle args) {
        super.setArguments(args);
    }
}