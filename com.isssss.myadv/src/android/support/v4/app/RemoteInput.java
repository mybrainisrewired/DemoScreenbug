package android.support.v4.app;

import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.app.RemoteInputCompatBase.RemoteInput.Factory;
import android.util.Log;

public class RemoteInput extends android.support.v4.app.RemoteInputCompatBase.RemoteInput {
    public static final String EXTRA_RESULTS_DATA = "android.remoteinput.resultsData";
    public static final Factory FACTORY;
    private static final Impl IMPL;
    public static final String RESULTS_CLIP_LABEL = "android.remoteinput.results";
    private static final String TAG = "RemoteInput";
    private final boolean mAllowFreeFormInput;
    private final CharSequence[] mChoices;
    private final Bundle mExtras;
    private final CharSequence mLabel;
    private final String mResultKey;

    public static final class Builder {
        private boolean mAllowFreeFormInput;
        private CharSequence[] mChoices;
        private Bundle mExtras;
        private CharSequence mLabel;
        private final String mResultKey;

        public Builder(String resultKey) {
            this.mAllowFreeFormInput = true;
            this.mExtras = new Bundle();
            if (resultKey == null) {
                throw new IllegalArgumentException("Result key can't be null");
            }
            this.mResultKey = resultKey;
        }

        public android.support.v4.app.RemoteInput.Builder addExtras(Bundle extras) {
            if (extras != null) {
                this.mExtras.putAll(extras);
            }
            return this;
        }

        public RemoteInput build() {
            return new RemoteInput(this.mResultKey, this.mLabel, this.mChoices, this.mAllowFreeFormInput, this.mExtras);
        }

        public Bundle getExtras() {
            return this.mExtras;
        }

        public android.support.v4.app.RemoteInput.Builder setAllowFreeFormInput(boolean allowFreeFormInput) {
            this.mAllowFreeFormInput = allowFreeFormInput;
            return this;
        }

        public android.support.v4.app.RemoteInput.Builder setChoices(CharSequence[] choices) {
            this.mChoices = choices;
            return this;
        }

        public android.support.v4.app.RemoteInput.Builder setLabel(CharSequence label) {
            this.mLabel = label;
            return this;
        }
    }

    static interface Impl {
        void addResultsToIntent(RemoteInput[] remoteInputArr, Intent intent, Bundle bundle);

        Bundle getResultsFromIntent(Intent intent);
    }

    static class ImplApi20 implements Impl {
        ImplApi20() {
        }

        public void addResultsToIntent(RemoteInput[] remoteInputs, Intent intent, Bundle results) {
            RemoteInputCompatApi20.addResultsToIntent(remoteInputs, intent, results);
        }

        public Bundle getResultsFromIntent(Intent intent) {
            return RemoteInputCompatApi20.getResultsFromIntent(intent);
        }
    }

    static class ImplBase implements Impl {
        ImplBase() {
        }

        public void addResultsToIntent(RemoteInput[] remoteInputs, Intent intent, Bundle results) {
            Log.w(TAG, "RemoteInput is only supported from API Level 16");
        }

        public Bundle getResultsFromIntent(Intent intent) {
            Log.w(TAG, "RemoteInput is only supported from API Level 16");
            return null;
        }
    }

    static class ImplJellybean implements Impl {
        ImplJellybean() {
        }

        public void addResultsToIntent(RemoteInput[] remoteInputs, Intent intent, Bundle results) {
            RemoteInputCompatJellybean.addResultsToIntent(remoteInputs, intent, results);
        }

        public Bundle getResultsFromIntent(Intent intent) {
            return RemoteInputCompatJellybean.getResultsFromIntent(intent);
        }
    }

    static {
        if (VERSION.SDK_INT >= 20) {
            IMPL = new ImplApi20();
        } else if (VERSION.SDK_INT >= 16) {
            IMPL = new ImplJellybean();
        } else {
            IMPL = new ImplBase();
        }
        FACTORY = new Factory() {
            public RemoteInput build(String resultKey, CharSequence label, CharSequence[] choices, boolean allowFreeFormInput, Bundle extras) {
                return new RemoteInput(resultKey, label, choices, allowFreeFormInput, extras);
            }

            public RemoteInput[] newArray(int size) {
                return new RemoteInput[size];
            }
        };
    }

    RemoteInput(String resultKey, CharSequence label, CharSequence[] choices, boolean allowFreeFormInput, Bundle extras) {
        this.mResultKey = resultKey;
        this.mLabel = label;
        this.mChoices = choices;
        this.mAllowFreeFormInput = allowFreeFormInput;
        this.mExtras = extras;
    }

    public static void addResultsToIntent(RemoteInput[] remoteInputs, Intent intent, Bundle results) {
        IMPL.addResultsToIntent(remoteInputs, intent, results);
    }

    public static Bundle getResultsFromIntent(Intent intent) {
        return IMPL.getResultsFromIntent(intent);
    }

    public boolean getAllowFreeFormInput() {
        return this.mAllowFreeFormInput;
    }

    public CharSequence[] getChoices() {
        return this.mChoices;
    }

    public Bundle getExtras() {
        return this.mExtras;
    }

    public CharSequence getLabel() {
        return this.mLabel;
    }

    public String getResultKey() {
        return this.mResultKey;
    }
}