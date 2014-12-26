package com.millennialmedia.android;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.mopub.common.Preconditions;
import com.nuance.nmdp.speechkit.DataUploadCommand;
import com.nuance.nmdp.speechkit.DataUploadResult;
import com.nuance.nmdp.speechkit.GenericCommand;
import com.nuance.nmdp.speechkit.GenericResult;
import com.nuance.nmdp.speechkit.Recognition;
import com.nuance.nmdp.speechkit.Recognizer;
import com.nuance.nmdp.speechkit.SpeechError;
import com.nuance.nmdp.speechkit.SpeechKit;
import com.nuance.nmdp.speechkit.SpeechKit.CmdSetType;
import com.nuance.nmdp.speechkit.Vocalizer;
import com.nuance.nmdp.speechkit.recognitionresult.DetailedResult;
import com.nuance.nmdp.speechkit.util.dataupload.Action;
import com.nuance.nmdp.speechkit.util.dataupload.Action.ActionType;
import com.nuance.nmdp.speechkit.util.dataupload.Data;
import com.nuance.nmdp.speechkit.util.dataupload.Data.DataType;
import com.nuance.nmdp.speechkit.util.dataupload.DataBlock;
import com.nuance.nmdp.speechkit.util.pdx.PdxValue.Dictionary;
import java.lang.ref.WeakReference;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NVASpeechKit {
    static final float AUDIO_LEVEL_CHANGE_INTERVAL = 0.25f;
    static final float AUDIO_LEVEL_MAX = 90.0f;
    private static final int AUDIO_LEVEL_UPDATE_FREQUENCY = 50;
    private static final int AUDIO_SAMPLE_PERIOD = 2000;
    static final float SAMPLING_BG_INTERVAL = 0.1f;
    private static final String TAG = "NVASpeechKit";
    private static String nuanceIdCache;
    NuanceCredentials _credentials;
    public Result[] _results;
    private Runnable audioLevelCallback;
    private AudioLevelTracker audioLevelTracker;
    private Runnable audioSampleCallback;
    private com.nuance.nmdp.speechkit.GenericCommand.Listener commandListener;
    private com.nuance.nmdp.speechkit.DataUploadCommand.Listener dataUploadListener;
    private String nuance_transaction_session_id;
    private String packageName;
    private DataUploadCommand pendingDataUploadCommand;
    private CustomWordsOp pendingDataUploadCommandType;
    private SpeechKit sk;
    private Recognizer skCurrentRecognizer;
    private Vocalizer skCurrentVocalizer;
    private com.nuance.nmdp.speechkit.Recognizer.Listener skRecogListener;
    private com.nuance.nmdp.speechkit.Vocalizer.Listener skVocalListener;
    private Listener speechKitListener;
    private Handler speeckKitHandler;
    private State state;
    private WeakReference<MMWebView> webViewRef;

    static /* synthetic */ class AnonymousClass_8 {
        static final /* synthetic */ int[] $SwitchMap$com$millennialmedia$android$NVASpeechKit$State;

        static {
            $SwitchMap$com$millennialmedia$android$NVASpeechKit$State = new int[com.millennialmedia.android.NVASpeechKit.State.values().length];
            try {
                $SwitchMap$com$millennialmedia$android$NVASpeechKit$State[com.millennialmedia.android.NVASpeechKit.State.ERROR.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$millennialmedia$android$NVASpeechKit$State[com.millennialmedia.android.NVASpeechKit.State.PROCESSING.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$millennialmedia$android$NVASpeechKit$State[com.millennialmedia.android.NVASpeechKit.State.READY.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$millennialmedia$android$NVASpeechKit$State[com.millennialmedia.android.NVASpeechKit.State.RECORDING.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            $SwitchMap$com$millennialmedia$android$NVASpeechKit$State[com.millennialmedia.android.NVASpeechKit.State.VOCALIZING.ordinal()] = 5;
        }
    }

    static class AudioLevelTracker {
        private static final double MAX = 80.0d;
        private static final double MIN = 40.0d;
        private static final double NORMALIZE_FACTOR = 4.004004004004004d;
        private static final double SCALE = 9.99d;
        double audioLevel;
        int audioLevelCount;
        double averageLevel;
        boolean isTrackingAudioSample;

        public AudioLevelTracker() {
            reset();
        }

        private static double normalize(double level) {
            return Math.min(SCALE, Math.max(Math.floor(level - 40.0d) / 4.004004004004004d, 0.0d));
        }

        public boolean isTrackingAudioSample() {
            return this.isTrackingAudioSample;
        }

        public void reset() {
            this.averageLevel = 0.0d;
            this.audioLevelCount = 0;
            this.isTrackingAudioSample = false;
        }

        public void startTrackingAudioSample() {
            reset();
            this.isTrackingAudioSample = true;
        }

        public boolean update(double level) {
            double oldAverage = this.averageLevel;
            double oldLevel = this.audioLevel;
            this.audioLevel = level;
            this.audioLevelCount++;
            this.averageLevel = ((((double) (this.audioLevelCount - 1)) * oldAverage) + level) / ((double) this.audioLevelCount);
            return (this.isTrackingAudioSample || this.audioLevel == oldLevel) ? false : true;
        }
    }

    public enum CustomWordsOp {
        Add,
        Remove
    }

    public static interface Listener {
        void onAudioLevelUpdate(double d);

        void onAudioSampleUpdate(double d);

        void onCustomWordsAdded();

        void onCustomWordsDeleted();

        void onError();

        void onResults();

        void onStateChange(com.millennialmedia.android.NVASpeechKit.State state);
    }

    public class Result {
        public final int resultScore;
        public final String resultString;

        public Result(String resultString, double resultScore) {
            this.resultString = resultString;
            this.resultScore = (int) resultScore;
        }

        public int getResultScore() {
            return this.resultScore;
        }

        public String getResultString() {
            return this.resultString;
        }
    }

    public enum State {
        ERROR("error"),
        VOCALIZING("vocalizing"),
        RECORDING("recording"),
        READY("ready"),
        PROCESSING("processing");
        private String name;

        private State(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }

    static {
        nuanceIdCache = null;
    }

    public NVASpeechKit(MMWebView webView) {
        this._results = null;
        this.audioLevelTracker = new AudioLevelTracker();
        this.audioSampleCallback = new Runnable() {
            public void run() {
                NVASpeechKit.this.endRecording();
            }
        };
        this.audioLevelCallback = new Runnable() {
            public void run() {
                if (NVASpeechKit.this.skCurrentRecognizer != null) {
                    double normalizedLevel = AudioLevelTracker.normalize((double) NVASpeechKit.this.skCurrentRecognizer.getAudioLevel());
                    MMLog.d(TAG, "audiolevel changed: level=" + normalizedLevel);
                    if (NVASpeechKit.this.audioLevelTracker.update(normalizedLevel) && NVASpeechKit.this.speechKitListener != null) {
                        NVASpeechKit.this.speechKitListener.onAudioLevelUpdate(normalizedLevel);
                    }
                    if (NVASpeechKit.this.state == com.millennialmedia.android.NVASpeechKit.State.RECORDING || NVASpeechKit.this.audioLevelTracker.isTrackingAudioSample) {
                        NVASpeechKit.this.speeckKitHandler.postDelayed(NVASpeechKit.this.audioLevelCallback, 50);
                    }
                }
            }
        };
        this.commandListener = new com.nuance.nmdp.speechkit.GenericCommand.Listener() {
            public void onComplete(GenericCommand command, GenericResult result, SpeechError error) {
                if (error != null) {
                    MMLog.e(TAG, "GenericCommand listener. Error: " + error.getErrorDetail());
                } else {
                    MMLog.d(TAG, "GenericCommand listener. Success: " + result.getQueryResult());
                }
                NVASpeechKit.this.notifySpeechResults();
            }
        };
        this.dataUploadListener = new com.nuance.nmdp.speechkit.DataUploadCommand.Listener() {
            private void notifyListener(DataUploadCommand command) {
                if (NVASpeechKit.this.speechKitListener != null && NVASpeechKit.this.pendingDataUploadCommand == command) {
                    if (NVASpeechKit.this.pendingDataUploadCommandType == com.millennialmedia.android.NVASpeechKit.CustomWordsOp.Add) {
                        NVASpeechKit.this.speechKitListener.onCustomWordsAdded();
                    } else {
                        NVASpeechKit.this.speechKitListener.onCustomWordsDeleted();
                    }
                }
                NVASpeechKit.this.pendingDataUploadCommand = null;
            }

            public void onError(DataUploadCommand command, SpeechError error) {
                MMLog.e(TAG, "DataUploadCommand listener error. command:" + command.toString() + " Error:" + error.getErrorDetail());
                notifyListener(command);
            }

            public void onResults(DataUploadCommand command, DataUploadResult results) {
                MMLog.d(TAG, "DataUploadCommand listener successful command:" + command.toString() + " isVocRegenerated:" + results.isVocRegenerated() + " results:" + results.toString());
                notifyListener(command);
            }
        };
        this.speechKitListener = new Listener() {
            public void onAudioLevelUpdate(double audioLevel) {
                NVASpeechKit.this.audioLevelChange(audioLevel);
            }

            public void onAudioSampleUpdate(double averageAudioLevel) {
                NVASpeechKit.this.backgroundAudioLevel(averageAudioLevel);
            }

            public void onCustomWordsAdded() {
            }

            public void onCustomWordsDeleted() {
            }

            public void onError() {
            }

            public void onResults() {
                NVASpeechKit.this.recognitionResult(NVASpeechKit.this.resultsToJSON(NVASpeechKit.this.getResults()).toString());
            }

            public void onStateChange(com.millennialmedia.android.NVASpeechKit.State state) {
                switch (AnonymousClass_8.$SwitchMap$com$millennialmedia$android$NVASpeechKit$State[state.ordinal()]) {
                    case MMAdView.TRANSITION_FADE:
                        NVASpeechKit.this.voiceStateChangeError();
                    case MMAdView.TRANSITION_UP:
                        NVASpeechKit.this.voiceStateChangeProcessing();
                    case MMAdView.TRANSITION_DOWN:
                        NVASpeechKit.this.voiceStateChangeReady();
                    case MMAdView.TRANSITION_RANDOM:
                        NVASpeechKit.this.voiceStateChangeRecording();
                    case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                        NVASpeechKit.this.voiceStateChangeVocalizing();
                    default:
                        break;
                }
            }
        };
        if (webView != null) {
            this.webViewRef = new WeakReference(webView);
            initInternalData(webView.getContext().getApplicationContext());
        }
        this.state = State.READY;
    }

    private String byte2Str(byte[] byteArray) {
        if (byteArray == null) {
            return null;
        }
        StringBuffer hexString = new StringBuffer();
        int i = 0;
        while (i < byteArray.length) {
            int v = byteArray[i] & 255;
            String hex = Integer.toHexString(v);
            if (v < 16) {
                hexString.append('0');
            }
            hexString.append(hex);
            i++;
        }
        return hexString.toString();
    }

    private void cancelAudioLevelCallbacks() {
        if (this.speeckKitHandler != null) {
            this.speeckKitHandler.removeCallbacks(this.audioSampleCallback);
            this.speeckKitHandler.removeCallbacks(this.audioLevelCallback);
        }
    }

    private com.nuance.nmdp.speechkit.Recognizer.Listener createRecognizerListener() {
        return new com.nuance.nmdp.speechkit.Recognizer.Listener() {
            public void onError(Recognizer recognizer, SpeechError error) {
                MMLog.d(TAG, "Speech Kit Error code:" + error.getErrorCode() + " detail:" + error.getErrorDetail() + " suggestions:" + error.getSuggestion());
                NVASpeechKit.this.cancelAudioLevelCallbacks();
                NVASpeechKit.this.handleSpeechError(error);
                NVASpeechKit.this.skCurrentRecognizer = null;
                if (NVASpeechKit.this.sk != null) {
                    MMLog.d(TAG, "Recognizer.Listener.onError: session id [" + NVASpeechKit.this.sk.getSessionId() + "]");
                }
            }

            public void onRecordingBegin(Recognizer recognizer) {
                MMLog.d(TAG, "recording begins");
                NVASpeechKit.this._results = null;
                if (!NVASpeechKit.this.audioLevelTracker.isTrackingAudioSample()) {
                    NVASpeechKit.this.setState(com.millennialmedia.android.NVASpeechKit.State.RECORDING);
                }
                NVASpeechKit.this.startProgress(recognizer);
                if (NVASpeechKit.this.audioLevelTracker.isTrackingAudioSample()) {
                    NVASpeechKit.this.speeckKitHandler.removeCallbacks(NVASpeechKit.this.audioSampleCallback);
                    NVASpeechKit.this.speeckKitHandler.postDelayed(NVASpeechKit.this.audioSampleCallback, 2000);
                }
            }

            public void onRecordingDone(Recognizer recognizer) {
                MMLog.d(TAG, "recording has ended");
                NVASpeechKit.this.cancelAudioLevelCallbacks();
                if (!NVASpeechKit.this.audioLevelTracker.isTrackingAudioSample()) {
                    NVASpeechKit.this.setState(com.millennialmedia.android.NVASpeechKit.State.PROCESSING);
                }
                if (NVASpeechKit.this.sk != null) {
                    NVASpeechKit.this.nuance_transaction_session_id = NVASpeechKit.this.sk.getSessionId();
                }
            }

            public void onResults(Recognizer recognizer, Recognition results) {
                MMLog.d(TAG, "recording results returned.");
                NVASpeechKit.this.cancelAudioLevelCallbacks();
                if (NVASpeechKit.this.audioLevelTracker.isTrackingAudioSample) {
                    NVASpeechKit.this._results = new com.millennialmedia.android.NVASpeechKit.Result[0];
                    NVASpeechKit.this.notifySpeechResults();
                } else {
                    NVASpeechKit.this.processResults(results.getDetailedResults());
                    if (NVASpeechKit.this.nuance_transaction_session_id != null) {
                        MMLog.d(TAG, "Recognizer.Listener.onResults: session id [" + NVASpeechKit.this.nuance_transaction_session_id + "]");
                    }
                    NVASpeechKit.this.logEvent();
                }
            }
        };
    }

    private com.nuance.nmdp.speechkit.Vocalizer.Listener createVocalizerListener() {
        return new com.nuance.nmdp.speechkit.Vocalizer.Listener() {
            public void onSpeakingBegin(Vocalizer vocalizer, String text, Object context) {
                MMLog.d(TAG, "Vocalization begins. text=" + text);
                NVASpeechKit.this.setState(com.millennialmedia.android.NVASpeechKit.State.VOCALIZING);
            }

            public void onSpeakingDone(Vocalizer vocalizer, String text, SpeechError error, Object context) {
                MMLog.d(TAG, "Vocalization has ended.");
                if (error != null) {
                    MMLog.e(TAG, "Vocalizer error: " + error.getErrorDetail());
                    NVASpeechKit.this.handleSpeechError(error);
                } else {
                    NVASpeechKit.this.setState(com.millennialmedia.android.NVASpeechKit.State.READY);
                }
            }
        };
    }

    private String getAdId() {
        if (this.webViewRef != null) {
            MMWebView webView = (MMWebView) this.webViewRef.get();
            if (webView != null) {
                return webView.getAdId();
            }
        }
        return "DEFAULT_AD_ID";
    }

    private MMWebView getMMWebView() {
        return this.webViewRef != null ? (MMWebView) this.webViewRef.get() : null;
    }

    private String getSpeechError(SpeechError error) {
        return error == null ? "No Error given" : "Speech Kit Error code:" + error.getErrorCode() + " detail:" + error.getErrorDetail() + " suggestions:" + error.getSuggestion();
    }

    private void handleSpeechError(SpeechError error) {
        switch (error.getErrorCode()) {
            case MMAdView.TRANSITION_UP:
                if (!this.audioLevelTracker.isTrackingAudioSample) {
                    setState(State.PROCESSING);
                }
                this._results = new Result[0];
                notifySpeechResults();
            case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                setState(State.READY);
                this.skCurrentRecognizer = null;
            default:
                if (this.speechKitListener != null) {
                    this.speechKitListener.onError();
                    setState(State.ERROR);
                    voiceError(getSpeechError(error));
                }
        }
    }

    private void initInternalData(Context context) {
        if (this.packageName == null) {
            this.packageName = context.getApplicationContext().getPackageName();
        }
    }

    private void notifySpeechResults() {
        if (!(this.speechKitListener == null || this._results == null)) {
            if (this.audioLevelTracker.isTrackingAudioSample) {
                this.speechKitListener.onAudioSampleUpdate(this.audioLevelTracker.averageLevel);
                this.audioLevelTracker.reset();
            } else {
                this.speechKitListener.onResults();
            }
        }
        setState(State.READY);
        this.skCurrentRecognizer = null;
    }

    private void processResults(List<DetailedResult> inputResults) {
        MMLog.d(TAG, "processResults called.");
        this._results = new Result[inputResults.size()];
        int i = 0;
        Iterator i$ = inputResults.iterator();
        while (i$.hasNext()) {
            DetailedResult r = (DetailedResult) i$.next();
            int i2 = i + 1;
            this._results[i] = new Result(r.toString(), r.getConfidenceScore());
            i = i2;
        }
    }

    private void releaseWebView() {
        if (getMMWebView() != null) {
            this.webViewRef.clear();
        }
    }

    private JSONArray resultsToJSON(Result[] resultArray) {
        JSONArray list = new JSONArray();
        int i = 0;
        while (i < resultArray.length) {
            JSONObject object = new JSONObject();
            try {
                object.put("score", Preconditions.EMPTY_ARGUMENTS + resultArray[i].getResultScore());
                object.put("result", resultArray[i].getResultString());
                list.put(object);
                i++;
            } catch (JSONException e) {
                MMLog.e(TAG, "JSON creation error.", e);
                return null;
            }
        }
        return list;
    }

    private synchronized void setState(State state) {
        MMLog.d(TAG, "recording results returned. state=" + state);
        State old_state = this.state;
        this.state = state;
        if (!(this.speechKitListener == null || this.state == old_state)) {
            this.speechKitListener.onStateChange(state);
        }
    }

    private void startProgress(Recognizer recognizer) {
        this.speeckKitHandler.removeCallbacks(this.audioLevelCallback);
        this.speeckKitHandler.postDelayed(this.audioLevelCallback, 50);
    }

    private byte[] string2Byte(String string) {
        if (string == null) {
            return null;
        }
        byte[] bytes = new byte[(string.length() / 2)];
        int i = 0;
        while (i < bytes.length) {
            bytes[i] = (byte) Integer.parseInt(string.substring(i * 2, i * 2 + 2), ApiEventType.API_MRAID_GET_ORIENTATION);
            i++;
        }
        return bytes;
    }

    void audioLevelChange(double audioLevel) {
        MMWebView webView = getMMWebView();
        if (webView != null) {
            webView.loadUrl("javascript:MMJS.sdk.audioLevelChange(" + audioLevel + ")");
        }
    }

    void backgroundAudioLevel(double audioLevel) {
        MMWebView webView = getMMWebView();
        if (webView != null) {
            webView.loadUrl("javascript:MMJS.sdk.backgroundAudioLevel(" + audioLevel + ")");
        }
    }

    public void cancelRecording() {
        if (this.skCurrentRecognizer != null) {
            MMLog.d(TAG, "cancel RECORDING");
            this.skCurrentRecognizer.cancel();
            this.skCurrentRecognizer = null;
            setState(State.READY);
        }
    }

    public boolean endRecording() {
        if (this.skCurrentRecognizer == null) {
            return false;
        }
        MMLog.d(TAG, "end RECORDING");
        this.skCurrentRecognizer.stopRecording();
        this.skCurrentRecognizer = null;
        return true;
    }

    synchronized String getNuanceId() {
        String str = null;
        synchronized (this) {
            if (nuanceIdCache != null) {
                str = nuanceIdCache;
            } else {
                Context context = null;
                if (this.webViewRef != null) {
                    MMWebView webView = (MMWebView) this.webViewRef.get();
                    if (webView != null) {
                        context = webView.getContext();
                    }
                }
                if (context != null) {
                    String mmdid = Secure.getString(context.getContentResolver(), "android_id");
                    if (mmdid != null) {
                        try {
                            str = MMSDK.byteArrayToString(MessageDigest.getInstance("SHA1").digest(mmdid.getBytes()));
                            nuanceIdCache = str;
                        } catch (Exception e) {
                            MMLog.e(TAG, "Problem with nuanceid", e);
                        }
                    }
                }
            }
        }
        return str;
    }

    public Result[] getResults() {
        return this._results;
    }

    String getSessionId() {
        return this.sk != null ? this.sk.getSessionId() : Preconditions.EMPTY_ARGUMENTS;
    }

    public synchronized State getState() {
        return this.state;
    }

    public boolean initialize(NuanceCredentials credentials, Context context) {
        MMLog.d(TAG, "initialize called.");
        if (credentials == null || context == null) {
            return false;
        }
        this._credentials = credentials;
        if (this.sk != null) {
            try {
                this.sk.connect();
            } catch (IllegalStateException e) {
                this.sk = null;
            }
        }
        if (this.sk == null) {
            byte[] appKeyBytes = string2Byte(credentials.appKey);
            MMLog.d(TAG, credentials.toString());
            this.sk = SpeechKit.initialize(context, "1.0", credentials.appID, credentials.server, credentials.port, false, appKeyBytes, CmdSetType.NVC);
            this.skVocalListener = createVocalizerListener();
            this.skRecogListener = createRecognizerListener();
            this.speeckKitHandler = new Handler(Looper.getMainLooper());
            this.sk.connect();
            setState(State.READY);
            return true;
        } else {
            MMLog.d(TAG, "Already initialized. Skipping.");
            return false;
        }
    }

    public void logEvent() {
        if (this.sk != null) {
            Dictionary content = new Dictionary();
            content.put("nva_ad_network_id", "MillenialMedia");
            content.put("nva_device_id", getNuanceId());
            content.put("nva_ad_publisher_id", this.packageName);
            String mmSessionId = Preconditions.EMPTY_ARGUMENTS;
            if (!(this._credentials == null || TextUtils.isEmpty(this._credentials.sessionID))) {
                mmSessionId = this._credentials.sessionID;
                content.put("nva_ad_session_id", this._credentials.sessionID);
            }
            String adId = getAdId();
            if (!TextUtils.isEmpty(adId)) {
                content.put("nva_ad_id", adId);
            }
            String session_id_for_logging;
            if (this.nuance_transaction_session_id != null) {
                content.put("nva_nvc_session_id", this.nuance_transaction_session_id);
                session_id_for_logging = this.nuance_transaction_session_id;
                this.nuance_transaction_session_id = null;
            } else {
                session_id_for_logging = this.sk.getSessionId();
            }
            MMLog.d(TAG, "Sending log revision command to server. sessionId[" + this.sk.getSessionId() + "] deviceId[" + getNuanceId() + "] adId[" + adId + "] mmSessionId[" + mmSessionId + "]");
            this.sk.createLogRevisionCmd("NVA_LOG_EVENT", content, this.sk.getSessionId(), this.commandListener, this.speeckKitHandler).start();
        }
    }

    void recognitionResult(String jsonResults) {
        MMWebView webView = getMMWebView();
        if (webView != null) {
            webView.loadUrl("javascript:MMJS.sdk.recognitionResult(" + jsonResults + ")");
        }
    }

    public void release() {
        MMLog.d(TAG, "release called.");
        stopActions();
        cancelAudioLevelCallbacks();
        if (this.sk != null) {
            this.sk.release();
            setState(State.READY);
            this.sk = null;
        }
        this.pendingDataUploadCommand = null;
        releaseWebView();
    }

    public void setSpeechKitListener(Listener speechKitListener) {
        this.speechKitListener = speechKitListener;
    }

    public boolean startRecording(String languageCode) {
        MMLog.d(TAG, "RECORDING INVOKED.");
        if (this.state != State.READY || this.sk == null) {
            return false;
        }
        this.nuance_transaction_session_id = null;
        this.skCurrentRecognizer = this.sk.createRecognizer("dictation", 1, languageCode, this.skRecogListener, this.speeckKitHandler);
        MMLog.d(TAG, "START RECORDING");
        this.skCurrentRecognizer.start();
        return true;
    }

    public void startSampleRecording() {
        this.audioLevelTracker.startTrackingAudioSample();
        startRecording("en_US");
    }

    public void stopActions() {
        if (this.sk != null) {
            try {
                this.sk.cancelCurrent();
            } catch (Exception e) {
                MMLog.e(TAG, "No speech kit to disconnect.", e);
            }
        }
    }

    public boolean textToSpeech(String input, String languageCode) {
        MMLog.d(TAG, "TTS INVOKED.");
        if (this.state != State.READY || this.sk == null) {
            return false;
        }
        this.skCurrentVocalizer = this.sk.createVocalizerWithLanguage(languageCode, this.skVocalListener, this.speeckKitHandler);
        this.skCurrentVocalizer.speakString(input, this);
        return true;
    }

    public void updateCustomWords(CustomWordsOp op, String[] words) {
        if (this.sk != null) {
            DataBlock datablock = new DataBlock();
            MMLog.d(TAG, "Creating dataupload command and " + (op == CustomWordsOp.Add ? "adding" : "deleting") + " words.");
            Data data = new Data("nva_custom_word_uploads", DataType.CUSTOMWORDS);
            Action addAction = new Action(op == CustomWordsOp.Add ? ActionType.ADD : ActionType.REMOVE);
            String[] arr$ = words;
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                String word = arr$[i$];
                addAction.addWord(word);
                MMLog.d(TAG, "\tword: '" + word + "'");
                i$++;
            }
            data.addAction(addAction);
            datablock.addData(data);
            int checksum = datablock.getChecksum();
            this.pendingDataUploadCommandType = op;
            this.pendingDataUploadCommand = this.sk.createDataUploadCmd(datablock, checksum, checksum, this.dataUploadListener, this.speeckKitHandler);
            this.pendingDataUploadCommand.start();
        }
    }

    void voiceError(String error) {
        MMWebView webView = getMMWebView();
        if (webView != null) {
            webView.loadUrl("javascript:MMJS.sdk.voiceError('" + error + "')");
        }
    }

    void voiceStateChangeError() {
        MMWebView webView = getMMWebView();
        if (webView != null) {
            webView.loadUrl("javascript:MMJS.sdk.voiceStateChange('error')");
        }
    }

    void voiceStateChangeProcessing() {
        MMWebView webView = getMMWebView();
        if (webView != null) {
            webView.loadUrl("javascript:MMJS.sdk.voiceStateChange('processing')");
        }
    }

    void voiceStateChangeReady() {
        MMWebView webView = getMMWebView();
        if (webView != null) {
            webView.loadUrl("javascript:MMJS.sdk.voiceStateChange('ready')");
        }
    }

    void voiceStateChangeRecording() {
        MMWebView webView = getMMWebView();
        if (webView != null) {
            webView.loadUrl("javascript:MMJS.sdk.voiceStateChange('recording')");
        }
    }

    void voiceStateChangeVocalizing() {
        MMWebView webView = getMMWebView();
        if (webView != null) {
            webView.loadUrl("javascript:MMJS.sdk.voiceStateChange('vocalizing')");
        }
    }
}