package com.google.android.gms.maps;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.dynamic.LifecycleDelegate;
import com.google.android.gms.dynamic.e;
import com.google.android.gms.dynamic.f;
import com.google.android.gms.internal.fq;
import com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate;
import com.google.android.gms.maps.internal.IStreetViewPanoramaFragmentDelegate;
import com.google.android.gms.maps.internal.u;
import com.google.android.gms.maps.model.RuntimeRemoteException;

public class StreetViewPanoramaFragment extends Fragment {
    private final b Si;
    private StreetViewPanorama Sj;

    static class a implements LifecycleDelegate {
        private final Fragment Hv;
        private final IStreetViewPanoramaFragmentDelegate Sk;

        public a(Fragment fragment, IStreetViewPanoramaFragmentDelegate iStreetViewPanoramaFragmentDelegate) {
            this.Sk = (IStreetViewPanoramaFragmentDelegate) fq.f(iStreetViewPanoramaFragmentDelegate);
            this.Hv = (Fragment) fq.f(fragment);
        }

        public IStreetViewPanoramaFragmentDelegate is() {
            return this.Sk;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onCreate(android.os.Bundle r5_savedInstanceState) {
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.maps.StreetViewPanoramaFragment.a.onCreate(android.os.Bundle):void");
            /*
            r4 = this;
            if (r5 != 0) goto L_0x0008;
        L_0x0002:
            r0 = new android.os.Bundle;	 Catch:{ RemoteException -> 0x0029 }
            r0.<init>();	 Catch:{ RemoteException -> 0x0029 }
            r5 = r0;
        L_0x0008:
            r1 = r4.Hv;	 Catch:{ RemoteException -> 0x0029 }
            r1 = r1.getArguments();	 Catch:{ RemoteException -> 0x0029 }
            if (r1 == 0) goto L_0x0023;
        L_0x0010:
            r2 = "StreetViewPanoramaOptions";
            r2 = r1.containsKey(r2);	 Catch:{ RemoteException -> 0x0029 }
            if (r2 == 0) goto L_0x0023;
        L_0x0018:
            r2 = "StreetViewPanoramaOptions";
            r3 = "StreetViewPanoramaOptions";
            r1 = r1.getParcelable(r3);	 Catch:{ RemoteException -> 0x0029 }
            com.google.android.gms.maps.internal.t.a(r5, r2, r1);	 Catch:{ RemoteException -> 0x0029 }
        L_0x0023:
            r1 = r4.Sk;	 Catch:{ RemoteException -> 0x0029 }
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
                return (View) e.d(this.Sk.onCreateView(e.h(inflater), e.h(container), savedInstanceState));
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onDestroy() {
            try {
                this.Sk.onDestroy();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onDestroyView() {
            try {
                this.Sk.onDestroyView();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onInflate(Activity activity, Bundle attrs, Bundle savedInstanceState) {
            try {
                this.Sk.onInflate(e.h(activity), null, savedInstanceState);
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onLowMemory() {
            try {
                this.Sk.onLowMemory();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onPause() {
            try {
                this.Sk.onPause();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onResume() {
            try {
                this.Sk.onResume();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onSaveInstanceState(Bundle outState) {
            try {
                this.Sk.onSaveInstanceState(outState);
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
        private final Fragment Hv;
        protected f<a> RV;
        private Activity nS;

        b(Fragment fragment) {
            this.Hv = fragment;
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
                    this.RV.a(new a(this.Hv, u.A(this.nS).i(e.h(this.nS))));
                } catch (RemoteException e) {
                    throw new RuntimeRemoteException(e);
                } catch (GooglePlayServicesNotAvailableException e2) {
                }
            }
        }
    }

    public StreetViewPanoramaFragment() {
        this.Si = new b(this);
    }

    public static StreetViewPanoramaFragment newInstance() {
        return new StreetViewPanoramaFragment();
    }

    public static StreetViewPanoramaFragment newInstance(StreetViewPanoramaOptions options) {
        StreetViewPanoramaFragment streetViewPanoramaFragment = new StreetViewPanoramaFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("StreetViewPanoramaOptions", options);
        streetViewPanoramaFragment.setArguments(bundle);
        return streetViewPanoramaFragment;
    }

    public final StreetViewPanorama getStreetViewPanorama() {
        IStreetViewPanoramaFragmentDelegate is = is();
        if (is == null) {
            return null;
        }
        try {
            IStreetViewPanoramaDelegate streetViewPanorama = is.getStreetViewPanorama();
            if (streetViewPanorama == null) {
                return null;
            }
            if (this.Sj == null || this.Sj.ir().asBinder() != streetViewPanorama.asBinder()) {
                this.Sj = new StreetViewPanorama(streetViewPanorama);
            }
            return this.Sj;
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    protected IStreetViewPanoramaFragmentDelegate is() {
        this.Si.ip();
        return this.Si.fW() == null ? null : ((a) this.Si.fW()).is();
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState.setClassLoader(StreetViewPanoramaFragment.class.getClassLoader());
        }
        super.onActivityCreated(savedInstanceState);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.Si.setActivity(activity);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.Si.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return this.Si.onCreateView(inflater, container, savedInstanceState);
    }

    public void onDestroy() {
        this.Si.onDestroy();
        super.onDestroy();
    }

    public void onDestroyView() {
        this.Si.onDestroyView();
        super.onDestroyView();
    }

    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);
        this.Si.setActivity(activity);
        this.Si.onInflate(activity, new Bundle(), savedInstanceState);
    }

    public void onLowMemory() {
        this.Si.onLowMemory();
        super.onLowMemory();
    }

    public void onPause() {
        this.Si.onPause();
        super.onPause();
    }

    public void onResume() {
        super.onResume();
        this.Si.onResume();
    }

    public void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            outState.setClassLoader(StreetViewPanoramaFragment.class.getClassLoader());
        }
        super.onSaveInstanceState(outState);
        this.Si.onSaveInstanceState(outState);
    }

    public void setArguments(Bundle args) {
        super.setArguments(args);
    }
}