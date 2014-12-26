package com.google.android.gms.wallet.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import com.google.android.gms.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.dynamic.LifecycleDelegate;
import com.google.android.gms.dynamic.e;
import com.google.android.gms.dynamic.f;
import com.google.android.gms.internal.iz;
import com.google.android.gms.wallet.MaskedWalletRequest;
import com.inmobi.monetization.internal.InvalidManifestConfigException;

public final class WalletFragment extends Fragment {
    private final Fragment Hv;
    private WalletFragmentInitParams acA;
    private MaskedWalletRequest acB;
    private Boolean acC;
    private b acH;
    private final com.google.android.gms.dynamic.b acI;
    private final c acJ;
    private a acK;
    private WalletFragmentOptions acz;
    private boolean mCreated;

    public static interface OnStateChangedListener {
        void onStateChanged(WalletFragment walletFragment, int i, int i2, Bundle bundle);
    }

    private static class b implements LifecycleDelegate {
        private final iz acF;

        private b(iz izVar) {
            this.acF = izVar;
        }

        private int getState() {
            try {
                return this.acF.getState();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        private void initialize(WalletFragmentInitParams startParams) {
            try {
                this.acF.initialize(startParams);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        private void onActivityResult(int requestCode, int resultCode, Intent data) {
            try {
                this.acF.onActivityResult(requestCode, resultCode, data);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        private void setEnabled(boolean enabled) {
            try {
                this.acF.setEnabled(enabled);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        private void updateMaskedWalletRequest(MaskedWalletRequest request) {
            try {
                this.acF.updateMaskedWalletRequest(request);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        public void onCreate(Bundle savedInstanceState) {
            try {
                this.acF.onCreate(savedInstanceState);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            try {
                return (View) e.d(this.acF.onCreateView(e.h(inflater), e.h(container), savedInstanceState));
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        public void onDestroy() {
        }

        public void onDestroyView() {
        }

        public void onInflate(Activity activity, Bundle attrs, Bundle savedInstanceState) {
            try {
                this.acF.a(e.h(activity), (WalletFragmentOptions) attrs.getParcelable("extraWalletFragmentOptions"), savedInstanceState);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        public void onLowMemory() {
        }

        public void onPause() {
            try {
                this.acF.onPause();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        public void onResume() {
            try {
                this.acF.onResume();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        public void onSaveInstanceState(Bundle outState) {
            try {
                this.acF.onSaveInstanceState(outState);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        public void onStart() {
            try {
                this.acF.onStart();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        public void onStop() {
            try {
                this.acF.onStop();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private class c extends com.google.android.gms.dynamic.a<b> implements OnClickListener {
        private c() {
        }

        protected void a(FrameLayout frameLayout) {
            View button = new Button(WalletFragment.this.Hv.getActivity());
            button.setText(R.string.wallet_buy_button_place_holder);
            int i = -1;
            int i2 = InvalidManifestConfigException.MISSING_ACTIVITY_DECLARATION;
            if (WalletFragment.this.acz != null) {
                WalletFragmentStyle fragmentStyle = WalletFragment.this.acz.getFragmentStyle();
                if (fragmentStyle != null) {
                    DisplayMetrics displayMetrics = WalletFragment.this.Hv.getResources().getDisplayMetrics();
                    i = fragmentStyle.a("buyButtonWidth", displayMetrics, -1);
                    i2 = fragmentStyle.a("buyButtonHeight", displayMetrics, (int)InvalidManifestConfigException.MISSING_ACTIVITY_DECLARATION);
                }
            }
            button.setLayoutParams(new LayoutParams(i, i2));
            button.setOnClickListener(this);
            frameLayout.addView(button);
        }

        protected void a(f<b> fVar) {
            Activity activity = WalletFragment.this.Hv.getActivity();
            if (WalletFragment.this.acH == null && WalletFragment.this.mCreated && activity != null) {
                try {
                    WalletFragment.this.acH = new b(null);
                    WalletFragment.this.acz = null;
                    fVar.a(WalletFragment.this.acH);
                    if (WalletFragment.this.acA != null) {
                        WalletFragment.this.acH.initialize(WalletFragment.this.acA);
                        WalletFragment.this.acA = null;
                    }
                    if (WalletFragment.this.acB != null) {
                        WalletFragment.this.acH.updateMaskedWalletRequest(WalletFragment.this.acB);
                        WalletFragment.this.acB = null;
                    }
                    if (WalletFragment.this.acC != null) {
                        WalletFragment.this.acH.setEnabled(WalletFragment.this.acC.booleanValue());
                        WalletFragment.this.acC = null;
                    }
                } catch (GooglePlayServicesNotAvailableException e) {
                }
            }
        }

        public void onClick(View view) {
            Context activity = WalletFragment.this.Hv.getActivity();
            GooglePlayServicesUtil.showErrorDialogFragment(GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity), activity, -1);
        }
    }

    static class a extends com.google.android.gms.internal.ja.a {
        private com.google.android.gms.wallet.fragment.WalletFragment.OnStateChangedListener acL;
        private final WalletFragment acM;

        a(WalletFragment walletFragment) {
            this.acM = walletFragment;
        }

        public void a(int i, int i2, Bundle bundle) {
            if (this.acL != null) {
                this.acL.onStateChanged(this.acM, i, i2, bundle);
            }
        }

        public void a(com.google.android.gms.wallet.fragment.WalletFragment.OnStateChangedListener onStateChangedListener) {
            this.acL = onStateChangedListener;
        }
    }

    public WalletFragment() {
        this.mCreated = false;
        this.acI = com.google.android.gms.dynamic.b.a(this);
        this.acJ = new c(null);
        this.acK = new a(this);
        this.Hv = this;
    }

    public static WalletFragment newInstance(WalletFragmentOptions options) {
        WalletFragment walletFragment = new WalletFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("extraWalletFragmentOptions", options);
        walletFragment.Hv.setArguments(bundle);
        return walletFragment;
    }

    public int getState() {
        return this.acH != null ? this.acH.getState() : 0;
    }

    public void initialize(WalletFragmentInitParams initParams) {
        if (this.acH != null) {
            this.acH.initialize(initParams);
            this.acA = null;
        } else if (this.acA == null) {
            this.acA = initParams;
            if (this.acB != null) {
                Log.w("WalletFragment", "updateMaskedWallet() was called before initialize()");
            }
        } else {
            Log.w("WalletFragment", "initialize(WalletFragmentInitParams) was called more than once. Ignoring.");
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.acH != null) {
            this.acH.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            savedInstanceState.setClassLoader(WalletFragmentOptions.class.getClassLoader());
            WalletFragmentInitParams walletFragmentInitParams = (WalletFragmentInitParams) savedInstanceState.getParcelable("walletFragmentInitParams");
            if (walletFragmentInitParams != null) {
                if (this.acA != null) {
                    Log.w("WalletFragment", "initialize(WalletFragmentInitParams) was called more than once.Ignoring.");
                }
                this.acA = walletFragmentInitParams;
            }
            if (this.acB == null) {
                this.acB = (MaskedWalletRequest) savedInstanceState.getParcelable("maskedWalletRequest");
            }
            if (savedInstanceState.containsKey("walletFragmentOptions")) {
                this.acz = (WalletFragmentOptions) savedInstanceState.getParcelable("walletFragmentOptions");
            }
            if (savedInstanceState.containsKey("enabled")) {
                this.acC = Boolean.valueOf(savedInstanceState.getBoolean("enabled"));
            }
        } else if (this.Hv.getArguments() != null) {
            WalletFragmentOptions walletFragmentOptions = (WalletFragmentOptions) this.Hv.getArguments().getParcelable("extraWalletFragmentOptions");
            if (walletFragmentOptions != null) {
                walletFragmentOptions.I(this.Hv.getActivity());
                this.acz = walletFragmentOptions;
            }
        }
        this.mCreated = true;
        this.acJ.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return this.acJ.onCreateView(inflater, container, savedInstanceState);
    }

    public void onDestroy() {
        super.onDestroy();
        this.mCreated = false;
    }

    public void onInflate(Context activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);
        if (this.acz == null) {
            this.acz = WalletFragmentOptions.a(activity, attrs);
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable("attrKeyWalletFragmentOptions", this.acz);
        this.acJ.onInflate(activity, bundle, savedInstanceState);
    }

    public void onPause() {
        super.onPause();
        this.acJ.onPause();
    }

    public void onResume() {
        super.onResume();
        this.acJ.onResume();
        FragmentManager fragmentManager = this.Hv.getActivity().getFragmentManager();
        Fragment findFragmentByTag = fragmentManager.findFragmentByTag(GooglePlayServicesUtil.GMS_ERROR_DIALOG);
        if (findFragmentByTag != null) {
            fragmentManager.beginTransaction().remove(findFragmentByTag).commit();
            GooglePlayServicesUtil.showErrorDialogFragment(GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.Hv.getActivity()), this.Hv.getActivity(), -1);
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.setClassLoader(WalletFragmentOptions.class.getClassLoader());
        this.acJ.onSaveInstanceState(outState);
        if (this.acA != null) {
            outState.putParcelable("walletFragmentInitParams", this.acA);
            this.acA = null;
        }
        if (this.acB != null) {
            outState.putParcelable("maskedWalletRequest", this.acB);
            this.acB = null;
        }
        if (this.acz != null) {
            outState.putParcelable("walletFragmentOptions", this.acz);
            this.acz = null;
        }
        if (this.acC != null) {
            outState.putBoolean("enabled", this.acC.booleanValue());
            this.acC = null;
        }
    }

    public void onStart() {
        super.onStart();
        this.acJ.onStart();
    }

    public void onStop() {
        super.onStop();
        this.acJ.onStop();
    }

    public void setEnabled(boolean enabled) {
        if (this.acH != null) {
            this.acH.setEnabled(enabled);
            this.acC = null;
        } else {
            this.acC = Boolean.valueOf(enabled);
        }
    }

    public void setOnStateChangedListener(OnStateChangedListener listener) {
        this.acK.a(listener);
    }

    public void updateMaskedWalletRequest(MaskedWalletRequest request) {
        if (this.acH != null) {
            this.acH.updateMaskedWalletRequest(request);
            this.acB = null;
        } else {
            this.acB = request;
        }
    }
}