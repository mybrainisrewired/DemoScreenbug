package com.mopub.mobileads;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.google.android.gms.drive.DriveFile;
import com.inmobi.re.configs.Initializer;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.Preconditions;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.Strings;
import com.mopub.mobileads.factories.HttpClientFactory;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;

public class MraidView extends BaseWebView implements UserClickListener {
    private static final String LOGTAG = "MraidView";
    private AdConfiguration mAdConfiguration;
    private MoPubBrowserController mBrowserController;
    private boolean mClicked;
    private MraidDisplayController mDisplayController;
    private boolean mHasFiredReadyEvent;
    private boolean mIsVisible;
    private MraidListenerInfo mListenerInfo;
    private final PlacementType mPlacementType;
    private ViewGestureDetector mViewGestureDetector;
    private WebViewClient mWebViewClient;

    public enum ExpansionStyle {
        ENABLED,
        DISABLED;

        static {
            ENABLED = new com.mopub.mobileads.MraidView.ExpansionStyle("ENABLED", 0);
            DISABLED = new com.mopub.mobileads.MraidView.ExpansionStyle("DISABLED", 1);
            ENUM$VALUES = new com.mopub.mobileads.MraidView.ExpansionStyle[]{ENABLED, DISABLED};
        }
    }

    public static interface MraidListener {
        void onClose(MraidView mraidView, com.mopub.mobileads.MraidView.ViewState viewState);

        void onExpand(MraidView mraidView);

        void onFailure(MraidView mraidView);

        void onOpen(MraidView mraidView);

        void onReady(MraidView mraidView);
    }

    static class MraidListenerInfo {
        private com.mopub.mobileads.MraidView.MraidListener mMraidListener;
        private com.mopub.mobileads.MraidView.OnCloseButtonStateChangeListener mOnCloseButtonListener;

        MraidListenerInfo() {
        }
    }

    private class MraidWebViewClient extends WebViewClient {
        private MraidWebViewClient() {
        }

        public void onLoadResource(WebView view, String url) {
            Log.d(LOGTAG, new StringBuilder("Loaded resource: ").append(url).toString());
        }

        public void onPageFinished(WebView view, String url) {
            if (!MraidView.this.mHasFiredReadyEvent) {
                MraidView.this.mDisplayController.initializeJavaScriptState();
                MraidView.this.fireChangeEventForProperty(MraidPlacementTypeProperty.createWithType(MraidView.this.mPlacementType));
                MraidView.this.fireReadyEvent();
                if (MraidView.this.getMraidListener() != null) {
                    MraidView.this.getMraidListener().onReady(MraidView.this);
                }
                MraidView.this.mIsVisible = MraidView.this.getVisibility() == 0;
                MraidView.this.fireChangeEventForProperty(MraidViewableProperty.createWithViewable(MraidView.this.mIsVisible));
                MraidView.this.mHasFiredReadyEvent = true;
            }
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Log.d(LOGTAG, new StringBuilder("Error: ").append(description).toString());
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            String scheme = Uri.parse(url).getScheme();
            if ("mopub".equals(scheme)) {
                return true;
            }
            if (Initializer.PRODUCT_MRAID.equals(scheme)) {
                MraidView.this.tryCommand(URI.create(url));
                return true;
            } else if (!MraidView.this.wasClicked()) {
                return false;
            } else {
                Intent i = new Intent();
                i.setAction("android.intent.action.VIEW");
                i.setData(Uri.parse(url));
                i.addFlags(DriveFile.MODE_READ_ONLY);
                try {
                    MraidView.this.getContext().startActivity(i);
                    return true;
                } catch (ActivityNotFoundException e) {
                    return false;
                }
            }
        }
    }

    public enum NativeCloseButtonStyle {
        ALWAYS_VISIBLE,
        ALWAYS_HIDDEN,
        AD_CONTROLLED;

        static {
            ALWAYS_VISIBLE = new com.mopub.mobileads.MraidView.NativeCloseButtonStyle("ALWAYS_VISIBLE", 0);
            ALWAYS_HIDDEN = new com.mopub.mobileads.MraidView.NativeCloseButtonStyle("ALWAYS_HIDDEN", 1);
            AD_CONTROLLED = new com.mopub.mobileads.MraidView.NativeCloseButtonStyle("AD_CONTROLLED", 2);
            ENUM$VALUES = new com.mopub.mobileads.MraidView.NativeCloseButtonStyle[]{ALWAYS_VISIBLE, ALWAYS_HIDDEN, AD_CONTROLLED};
        }
    }

    public static interface OnCloseButtonStateChangeListener {
        void onCloseButtonStateChange(MraidView mraidView, boolean z);
    }

    public enum PlacementType {
        INLINE,
        INTERSTITIAL;

        static {
            INLINE = new com.mopub.mobileads.MraidView.PlacementType("INLINE", 0);
            INTERSTITIAL = new com.mopub.mobileads.MraidView.PlacementType("INTERSTITIAL", 1);
            ENUM$VALUES = new com.mopub.mobileads.MraidView.PlacementType[]{INLINE, INTERSTITIAL};
        }
    }

    public enum ViewState {
        LOADING,
        DEFAULT,
        EXPANDED,
        HIDDEN;

        static {
            LOADING = new com.mopub.mobileads.MraidView.ViewState("LOADING", 0);
            DEFAULT = new com.mopub.mobileads.MraidView.ViewState("DEFAULT", 1);
            EXPANDED = new com.mopub.mobileads.MraidView.ViewState("EXPANDED", 2);
            HIDDEN = new com.mopub.mobileads.MraidView.ViewState("HIDDEN", 3);
            ENUM$VALUES = new com.mopub.mobileads.MraidView.ViewState[]{LOADING, DEFAULT, EXPANDED, HIDDEN};
        }
    }

    public static class BaseMraidListener implements com.mopub.mobileads.MraidView.MraidListener {
        public void onClose(MraidView view, com.mopub.mobileads.MraidView.ViewState newViewState) {
        }

        public void onExpand(MraidView view) {
        }

        public void onFailure(MraidView view) {
        }

        public void onOpen(MraidView view) {
        }

        public void onReady(MraidView view) {
        }
    }

    public MraidView(Context context, AdConfiguration adConfiguration) {
        this(context, adConfiguration, ExpansionStyle.ENABLED, NativeCloseButtonStyle.AD_CONTROLLED, PlacementType.INLINE);
    }

    public MraidView(Context context, AdConfiguration adConfiguration, ExpansionStyle expStyle, NativeCloseButtonStyle buttonStyle, PlacementType placementType) {
        super(context);
        this.mPlacementType = placementType;
        this.mAdConfiguration = adConfiguration;
        this.mViewGestureDetector = new ViewGestureDetector(context, this, adConfiguration);
        this.mViewGestureDetector.setUserClickListener(this);
        this.mIsVisible = getVisibility() == 0;
        initialize(expStyle, buttonStyle);
    }

    private void initialize(ExpansionStyle expStyle, NativeCloseButtonStyle buttonStyle) {
        setScrollContainer(false);
        setBackgroundColor(0);
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
        setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                MraidView.this.mViewGestureDetector.sendTouchEvent(event);
                switch (event.getAction()) {
                    case MMAdView.TRANSITION_NONE:
                    case MMAdView.TRANSITION_FADE:
                        if (!v.hasFocus()) {
                            v.requestFocus();
                        }
                        break;
                }
                return false;
            }
        });
        getSettings().setJavaScriptEnabled(true);
        this.mBrowserController = new MoPubBrowserController(this);
        this.mDisplayController = new MraidDisplayController(this, expStyle, buttonStyle);
        this.mWebViewClient = new MraidWebViewClient(null);
        setWebViewClient(this.mWebViewClient);
        this.mListenerInfo = new MraidListenerInfo();
    }

    private void notifyOnFailureListener() {
        if (this.mListenerInfo.mMraidListener != null) {
            this.mListenerInfo.mMraidListener.onFailure(this);
        }
    }

    private boolean tryCommand(URI uri) {
        String commandType = uri.getHost();
        List<NameValuePair> list = URLEncodedUtils.parse(uri, "UTF-8");
        Map<String, String> params = new HashMap();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            NameValuePair pair = (NameValuePair) it.next();
            params.put(pair.getName(), pair.getValue());
        }
        MraidCommand command = MraidCommandFactory.create(commandType, params, this);
        if (command == null) {
            fireNativeCommandCompleteEvent(commandType);
            return false;
        } else if (command.isCommandDependentOnUserClick(this.mPlacementType) && !wasClicked()) {
            return false;
        } else {
            command.execute();
            fireNativeCommandCompleteEvent(commandType);
            return true;
        }
    }

    public void destroy() {
        this.mDisplayController.destroy();
        super.destroy();
    }

    protected void fireChangeEventForProperties(ArrayList<MraidProperty> properties) {
        String props = properties.toString();
        if (props.length() >= 2) {
            String json = new StringBuilder("{").append(props.substring(1, props.length() - 1)).append("}").toString();
            injectJavaScript(new StringBuilder("window.mraidbridge.fireChangeEvent(").append(json).append(");").toString());
            Log.d(LOGTAG, new StringBuilder("Fire changes: ").append(json).toString());
        }
    }

    protected void fireChangeEventForProperty(MraidProperty property) {
        String json = new StringBuilder("{").append(property.toString()).append("}").toString();
        injectJavaScript(new StringBuilder("window.mraidbridge.fireChangeEvent(").append(json).append(");").toString());
        Log.d(LOGTAG, new StringBuilder("Fire change: ").append(json).toString());
    }

    protected void fireErrorEvent(MraidJavascriptCommand mraidJavascriptCommand, String message) {
        injectJavaScript(new StringBuilder("window.mraidbridge.fireErrorEvent('").append(mraidJavascriptCommand.getCommand()).append("', '").append(message).append("');").toString());
    }

    protected void fireNativeCommandCompleteEvent(String command) {
        injectJavaScript(new StringBuilder("window.mraidbridge.nativeCallComplete('").append(command).append("');").toString());
    }

    protected void fireReadyEvent() {
        injectJavaScript("window.mraidbridge.fireReadyEvent();");
    }

    AdConfiguration getAdConfiguration() {
        return this.mAdConfiguration;
    }

    protected MoPubBrowserController getBrowserController() {
        return this.mBrowserController;
    }

    protected MraidDisplayController getDisplayController() {
        return this.mDisplayController;
    }

    public boolean getIsVisible() {
        return this.mIsVisible;
    }

    public MraidListener getMraidListener() {
        return this.mListenerInfo.mMraidListener;
    }

    @Deprecated
    WebViewClient getMraidWebViewClient() {
        return this.mWebViewClient;
    }

    public OnCloseButtonStateChangeListener getOnCloseButtonStateChangeListener() {
        return this.mListenerInfo.mOnCloseButtonListener;
    }

    protected void injectJavaScript(String js) {
        if (js != null) {
            super.loadUrl(new StringBuilder("javascript:").append(js).toString());
        }
    }

    public void loadHtmlData(String data) {
        if (data != null) {
            if (!data.contains("<html>")) {
                data = new StringBuilder("<html><head></head><body style='margin:0;padding:0;'>").append(data).append("</body></html>").toString();
            }
            loadDataWithBaseURL(null, data.replace("<head>", "<head><script>(function() {\n  var isIOS = (/iphone|ipad|ipod/i).test(window.navigator.userAgent.toLowerCase());\n  if (isIOS) {\n    console = {};\n    console.log = function(log) {\n      var iframe = document.createElement('iframe');\n      iframe.setAttribute('src', 'ios-log: ' + log);\n      document.documentElement.appendChild(iframe);\n      iframe.parentNode.removeChild(iframe);\n      iframe = null;\n    };\n    console.debug = console.info = console.warn = console.error = console.log;\n  }\n}());\n\n(function() {\n  // Establish the root mraidbridge object.\n  var mraidbridge = window.mraidbridge = {};\n\n  // native SDK is ready to process mraid commands.\n  var nativeSDKFiredReady = false;\n\n  // Listeners for bridge events.\n  var listeners = {};\n\n  // Queue to track pending calls to the native SDK.\n  var nativeCallQueue = [];\n\n  // Whether a native call is currently in progress.\n  var nativeCallInFlight = false;\n\n  //////////////////////////////////////////////////////////////////////////////////////////////////\n\n  mraidbridge.fireReadyEvent = function() {\n    nativeSDKFiredReady = true;\n    mraidbridge.fireEvent('ready');\n  };\n\n  mraidbridge.fireChangeEvent = function(properties) {\n    mraidbridge.fireEvent('change', properties);\n  };\n\n  mraidbridge.fireErrorEvent = function(message, action) {\n    mraidbridge.fireEvent('error', message, action);\n  };\n\n  mraidbridge.fireEvent = function(type) {\n    var ls = listeners[type];\n    if (ls) {\n      var args = Array.prototype.slice.call(arguments);\n      args.shift();\n      var l = ls.length;\n      for (var i = 0; i < l; i++) {\n        ls[i].apply(null, args);\n      }\n    }\n  };\n\n  mraidbridge.nativeCallComplete = function(command) {\n    if (nativeCallQueue.length === 0) {\n      nativeCallInFlight = false;\n      return;\n    }\n\n    var nextCall = nativeCallQueue.pop();\n    window.location = nextCall;\n  };\n\n  mraidbridge.executeNativeCall = function(command) {\n    if (!nativeSDKFiredReady) {\n        console.log('rejecting ' + command + ' because mraid is not ready');\n        mraidbridge.fireErrorEvent('mraid is not ready', command);\n        return;\n    }\n\n    var call = 'mraid://' + command;\n\n    var key, value;\n    var isFirstArgument = true;\n\n    for (var i = 1; i < arguments.length; i += 2) {\n      key = arguments[i];\n      value = arguments[i + 1];\n\n      if (value === null) continue;\n\n      if (isFirstArgument) {\n        call += '?';\n        isFirstArgument = false;\n      } else {\n        call += '&';\n      }\n\n      call += encodeURIComponent(key) + '=' + encodeURIComponent(value);\n    }\n\n    if (nativeCallInFlight) {\n      nativeCallQueue.push(call);\n    } else {\n      nativeCallInFlight = true;\n      window.location = call;\n    }\n  };\n\n  //////////////////////////////////////////////////////////////////////////////////////////////////\n\n  mraidbridge.addEventListener = function(event, listener) {\n    var eventListeners;\n    listeners[event] = listeners[event] || [];\n    eventListeners = listeners[event];\n\n    for (var l in eventListeners) {\n      // Listener already registered, so no need to add it.\n      if (listener === l) return;\n    }\n\n    eventListeners.push(listener);\n  };\n\n  mraidbridge.removeEventListener = function(event, listener) {\n    if (listeners.hasOwnProperty(event)) {\n      var eventListeners = listeners[event];\n      if (eventListeners) {\n        var idx = eventListeners.indexOf(listener);\n        if (idx !== -1) {\n          eventListeners.splice(idx, 1);\n        }\n      }\n    }\n  };\n}());\n\n(function() {\n  var mraid = window.mraid = {};\n  var bridge = window.mraidbridge;\n\n  // Constants. ////////////////////////////////////////////////////////////////////////////////////\n\n  var VERSION = mraid.VERSION = '1.0';\n\n  var STATES = mraid.STATES = {\n    LOADING: 'loading',     // Initial state.\n    DEFAULT: 'default',\n    EXPANDED: 'expanded',\n    HIDDEN: 'hidden'\n  };\n\n  var EVENTS = mraid.EVENTS = {\n    ERROR: 'error',\n    INFO: 'info',\n    READY: 'ready',\n    STATECHANGE: 'stateChange',\n    VIEWABLECHANGE: 'viewableChange'\n  };\n\n  var PLACEMENT_TYPES = mraid.PLACEMENT_TYPES = {\n    UNKNOWN: 'unknown',\n    INLINE: 'inline',\n    INTERSTITIAL: 'interstitial'\n  };\n\n  // External MRAID state: may be directly or indirectly modified by the ad JS. ////////////////////\n\n  // Properties which define the behavior of an expandable ad.\n  var expandProperties = {\n    width: -1,\n    height: -1,\n    useCustomClose: false,\n    isModal: true,\n    lockOrientation: false\n  };\n\n  var hasSetCustomSize = false;\n\n  var hasSetCustomClose = false;\n\n  var listeners = {};\n\n  // Internal MRAID state. Modified by the native SDK. /////////////////////////////////////////////\n\n  var state = STATES.LOADING;\n\n  var isViewable = false;\n\n  var screenSize = { width: -1, height: -1 };\n\n  var placementType = PLACEMENT_TYPES.UNKNOWN;\n\n  var supports = {\n    sms: false,\n    tel: false,\n    calendar: false,\n    storePicture: false,\n    inlineVideo: false\n  };\n\n  //////////////////////////////////////////////////////////////////////////////////////////////////\n\n  var EventListeners = function(event) {\n    this.event = event;\n    this.count = 0;\n    var listeners = {};\n\n    this.add = function(func) {\n      var id = String(func);\n      if (!listeners[id]) {\n        listeners[id] = func;\n        this.count++;\n      }\n    };\n\n    this.remove = function(func) {\n      var id = String(func);\n      if (listeners[id]) {\n        listeners[id] = null;\n        delete listeners[id];\n        this.count--;\n        return true;\n      } else {\n        return false;\n      }\n    };\n\n    this.removeAll = function() {\n      for (var id in listeners) {\n        if (listeners.hasOwnProperty(id)) this.remove(listeners[id]);\n      }\n    };\n\n    this.broadcast = function(args) {\n      for (var id in listeners) {\n        if (listeners.hasOwnProperty(id)) listeners[id].apply({}, args);\n      }\n    };\n\n    this.toString = function() {\n      var out = [event, ':'];\n      for (var id in listeners) {\n        if (listeners.hasOwnProperty(id)) out.push('|', id, '|');\n      }\n      return out.join('');\n    };\n  };\n\n  var broadcastEvent = function() {\n    var args = new Array(arguments.length);\n    var l = arguments.length;\n    for (var i = 0; i < l; i++) args[i] = arguments[i];\n    var event = args.shift();\n    if (listeners[event]) listeners[event].broadcast(args);\n  };\n\n  var contains = function(value, array) {\n    for (var i in array) {\n      if (array[i] === value) return true;\n    }\n    return false;\n  };\n\n  var clone = function(obj) {\n    if (obj === null) return null;\n    var f = function() {};\n    f.prototype = obj;\n    return new f();\n  };\n\n  var stringify = function(obj) {\n    if (typeof obj === 'object') {\n      var out = [];\n      if (obj.push) {\n        // Array.\n        for (var p in obj) out.push(obj[p]);\n        return '[' + out.join(',') + ']';\n      } else {\n        // Other object.\n        for (var p in obj) out.push(\"'\" + p + \"': \" + obj[p]);\n        return '{' + out.join(',') + '}';\n      }\n    } else return String(obj);\n  };\n\n  var trim = function(str) {\n    return str.replace(/^\\s+|\\s+$/g, '');\n  };\n\n  // Functions that will be invoked by the native SDK whenever a \"change\" event occurs.\n  var changeHandlers = {\n    state: function(val) {\n      if (state === STATES.LOADING) {\n        broadcastEvent(EVENTS.INFO, 'Native SDK initialized.');\n      }\n      state = val;\n      broadcastEvent(EVENTS.INFO, 'Set state to ' + stringify(val));\n      broadcastEvent(EVENTS.STATECHANGE, state);\n    },\n\n    viewable: function(val) {\n      isViewable = val;\n      broadcastEvent(EVENTS.INFO, 'Set isViewable to ' + stringify(val));\n      broadcastEvent(EVENTS.VIEWABLECHANGE, isViewable);\n    },\n\n    placementType: function(val) {\n      broadcastEvent(EVENTS.INFO, 'Set placementType to ' + stringify(val));\n      placementType = val;\n    },\n\n    screenSize: function(val) {\n      broadcastEvent(EVENTS.INFO, 'Set screenSize to ' + stringify(val));\n      for (var key in val) {\n        if (val.hasOwnProperty(key)) screenSize[key] = val[key];\n      }\n\n      if (!hasSetCustomSize) {\n        expandProperties['width'] = screenSize['width'];\n        expandProperties['height'] = screenSize['height'];\n      }\n    },\n\n    expandProperties: function(val) {\n      broadcastEvent(EVENTS.INFO, 'Merging expandProperties with ' + stringify(val));\n      for (var key in val) {\n        if (val.hasOwnProperty(key)) expandProperties[key] = val[key];\n      }\n    },\n\n    supports: function(val) {\n      broadcastEvent(EVENTS.INFO, 'Set supports to ' + stringify(val));\n        supports = val;\n    },\n  };\n\n  var validate = function(obj, validators, action, merge) {\n    if (!merge) {\n      // Check to see if any required properties are missing.\n      if (obj === null) {\n        broadcastEvent(EVENTS.ERROR, 'Required object not provided.', action);\n        return false;\n      } else {\n        for (var i in validators) {\n          if (validators.hasOwnProperty(i) && obj[i] === undefined) {\n            broadcastEvent(EVENTS.ERROR, 'Object is missing required property: ' + i + '.', action);\n            return false;\n          }\n        }\n      }\n    }\n\n    for (var prop in obj) {\n      var validator = validators[prop];\n      var value = obj[prop];\n      if (validator && !validator(value)) {\n        // Failed validation.\n        broadcastEvent(EVENTS.ERROR, 'Value of property ' + prop + ' is invalid.',\n          action);\n        return false;\n      }\n    }\n    return true;\n  };\n\n  var expandPropertyValidators = {\n    width: function(v) { return !isNaN(v) && v >= 0; },\n    height: function(v) { return !isNaN(v) && v >= 0; },\n    useCustomClose: function(v) { return (typeof v === 'boolean'); },\n    lockOrientation: function(v) { return (typeof v === 'boolean'); }\n  };\n\n  //////////////////////////////////////////////////////////////////////////////////////////////////\n\n  bridge.addEventListener('change', function(properties) {\n    for (var p in properties) {\n      if (properties.hasOwnProperty(p)) {\n        var handler = changeHandlers[p];\n        handler(properties[p]);\n      }\n    }\n  });\n\n  bridge.addEventListener('error', function(message, action) {\n    broadcastEvent(EVENTS.ERROR, message, action);\n  });\n\n  bridge.addEventListener('ready', function() {\n    broadcastEvent(EVENTS.READY);\n  });\n\n  //////////////////////////////////////////////////////////////////////////////////////////////////\n\n  mraid.addEventListener = function(event, listener) {\n    if (!event || !listener) {\n      broadcastEvent(EVENTS.ERROR, 'Both event and listener are required.', 'addEventListener');\n    } else if (!contains(event, EVENTS)) {\n      broadcastEvent(EVENTS.ERROR, 'Unknown MRAID event: ' + event, 'addEventListener');\n    } else {\n      if (!listeners[event]) listeners[event] = new EventListeners(event);\n      listeners[event].add(listener);\n    }\n  };\n\n  mraid.close = function() {\n    if (state === STATES.HIDDEN) {\n      broadcastEvent(EVENTS.ERROR, 'Ad cannot be closed when it is already hidden.',\n        'close');\n    } else bridge.executeNativeCall('close');\n  };\n\n  mraid.expand = function(URL) {\n    if (this.getState() !== STATES.DEFAULT) {\n      broadcastEvent(EVENTS.ERROR, 'Ad can only be expanded from the default state.', 'expand');\n    } else {\n      var args = ['expand'];\n\n      if (this.getHasSetCustomClose()) {\n        args = args.concat(['shouldUseCustomClose', expandProperties.useCustomClose ? 'true' : 'false']);\n      }\n\n      if (this.getHasSetCustomSize()) {\n        if (expandProperties.width >= 0 && expandProperties.height >= 0) {\n          args = args.concat(['w', expandProperties.width, 'h', expandProperties.height]);\n        }\n      }\n\n      if (typeof expandProperties.lockOrientation !== 'undefined') {\n        args = args.concat(['lockOrientation', expandProperties.lockOrientation]);\n      }\n\n      if (URL) {\n        args = args.concat(['url', URL]);\n      }\n\n      bridge.executeNativeCall.apply(this, args);\n    }\n  };\n\n  mraid.getHasSetCustomClose = function() {\n      return hasSetCustomClose;\n  };\n\n  mraid.getHasSetCustomSize = function() {\n      return hasSetCustomSize;\n  };\n\n  mraid.getExpandProperties = function() {\n    var properties = {\n      width: expandProperties.width,\n      height: expandProperties.height,\n      useCustomClose: expandProperties.useCustomClose,\n      isModal: expandProperties.isModal\n    };\n    return properties;\n  };\n\n  mraid.getPlacementType = function() {\n    return placementType;\n  };\n\n  mraid.getState = function() {\n    return state;\n  };\n\n  mraid.getVersion = function() {\n    return mraid.VERSION;\n  };\n\n  mraid.isViewable = function() {\n    return isViewable;\n  };\n\n  mraid.open = function(URL) {\n    if (!URL) broadcastEvent(EVENTS.ERROR, 'URL is required.', 'open');\n    else bridge.executeNativeCall('open', 'url', URL);\n  };\n\n  mraid.removeEventListener = function(event, listener) {\n    if (!event) broadcastEvent(EVENTS.ERROR, 'Event is required.', 'removeEventListener');\n    else {\n      if (listener && (!listeners[event] || !listeners[event].remove(listener))) {\n        broadcastEvent(EVENTS.ERROR, 'Listener not currently registered for event.',\n          'removeEventListener');\n        return;\n      } else if (listeners[event]) listeners[event].removeAll();\n\n      if (listeners[event] && listeners[event].count === 0) {\n        listeners[event] = null;\n        delete listeners[event];\n      }\n    }\n  };\n\n  mraid.setExpandProperties = function(properties) {\n    if (validate(properties, expandPropertyValidators, 'setExpandProperties', true)) {\n      if (properties.hasOwnProperty('width') || properties.hasOwnProperty('height')) {\n        hasSetCustomSize = true;\n      }\n\n      if (properties.hasOwnProperty('useCustomClose')) hasSetCustomClose = true;\n\n      var desiredProperties = ['width', 'height', 'useCustomClose', 'lockOrientation'];\n      var length = desiredProperties.length;\n      for (var i = 0; i < length; i++) {\n        var propname = desiredProperties[i];\n        if (properties.hasOwnProperty(propname)) expandProperties[propname] = properties[propname];\n      }\n    }\n  };\n\n  mraid.useCustomClose = function(shouldUseCustomClose) {\n    expandProperties.useCustomClose = shouldUseCustomClose;\n    hasSetCustomClose = true;\n    bridge.executeNativeCall('usecustomclose', 'shouldUseCustomClose', shouldUseCustomClose);\n  };\n\n  // MRAID 2.0 APIs ////////////////////////////////////////////////////////////////////////////////\n\n  mraid.createCalendarEvent = function(parameters) {\n    CalendarEventParser.initialize(parameters);\n    if (CalendarEventParser.parse()) {\n      bridge.executeNativeCall.apply(this, CalendarEventParser.arguments);\n    } else {\n      broadcastEvent(EVENTS.ERROR, CalendarEventParser.errors[0], 'createCalendarEvent');\n    }\n  };\n\n  mraid.supports = function(feature) {\n    return supports[feature];\n  };\n\n  mraid.playVideo = function(uri) {\n    if (!mraid.isViewable()) {\n      broadcastEvent(EVENTS.ERROR, 'playVideo cannot be called until the ad is viewable', 'playVideo');\n      return;\n    }\n\n    if (!uri) {\n      broadcastEvent(EVENTS.ERROR, 'playVideo must be called with a valid URI', 'playVideo');\n    } else {\n      bridge.executeNativeCall.apply(this, ['playVideo', 'uri', uri]);\n    }\n  };\n\n  mraid.storePicture = function(uri) {\n    if (!mraid.isViewable()) {\n      broadcastEvent(EVENTS.ERROR, 'storePicture cannot be called until the ad is viewable', 'storePicture');\n      return;\n    }\n\n    if (!uri) {\n      broadcastEvent(EVENTS.ERROR, 'storePicture must be called with a valid URI', 'storePicture');\n    } else {\n      bridge.executeNativeCall.apply(this, ['storePicture', 'uri', uri]);\n    }\n  };\n\n  mraid.resize = function() {\n    bridge.executeNativeCall('resize');\n  };\n\n  mraid.getResizeProperties = function() {\n    bridge.executeNativeCall('getResizeProperties');\n  };\n\n  mraid.setResizeProperties = function(resizeProperties) {\n    bridge.executeNativeCall('setResizeProperties', 'resizeProperties', resizeProperties);\n  };\n\n  mraid.getCurrentPosition = function() {\n    bridge.executeNativeCall('getCurrentPosition');\n  };\n\n  mraid.getDefaultPosition = function() {\n    bridge.executeNativeCall('getDefaultPosition');\n  };\n\n  mraid.getMaxSize = function() {\n    bridge.executeNativeCall('getMaxSize');\n  };\n\n  mraid.getScreenSize = function() {\n    bridge.executeNativeCall('getScreenSize');\n  };\n\n  var CalendarEventParser = {\n    initialize: function(parameters) {\n      this.parameters = parameters;\n      this.errors = [];\n      this.arguments = ['createCalendarEvent'];\n    },\n\n    parse: function() {\n      if (!this.parameters) {\n        this.errors.push('The object passed to createCalendarEvent cannot be null.');\n      } else {\n        this.parseDescription();\n        this.parseLocation();\n        this.parseSummary();\n        this.parseStartAndEndDates();\n        this.parseReminder();\n        this.parseRecurrence();\n        this.parseTransparency();\n      }\n\n      var errorCount = this.errors.length;\n      if (errorCount) {\n        this.arguments.length = 0;\n      }\n\n      return (errorCount === 0);\n    },\n\n    parseDescription: function() {\n      this._processStringValue('description');\n    },\n\n    parseLocation: function() {\n      this._processStringValue('location');\n    },\n\n    parseSummary: function() {\n      this._processStringValue('summary');\n    },\n\n    parseStartAndEndDates: function() {\n      this._processDateValue('start');\n      this._processDateValue('end');\n    },\n\n    parseReminder: function() {\n      var reminder = this._getParameter('reminder');\n      if (!reminder) {\n        return;\n      }\n\n      if (reminder < 0) {\n        this.arguments.push('relativeReminder');\n        this.arguments.push(parseInt(reminder) / 1000);\n      } else {\n        this.arguments.push('absoluteReminder');\n        this.arguments.push(reminder);\n      }\n    },\n\n    parseRecurrence: function() {\n      var recurrenceDict = this._getParameter('recurrence');\n      if (!recurrenceDict) {\n        return;\n      }\n\n      this.parseRecurrenceInterval(recurrenceDict);\n      this.parseRecurrenceFrequency(recurrenceDict);\n      this.parseRecurrenceEndDate(recurrenceDict);\n      this.parseRecurrenceArrayValue(recurrenceDict, 'daysInWeek');\n      this.parseRecurrenceArrayValue(recurrenceDict, 'daysInMonth');\n      this.parseRecurrenceArrayValue(recurrenceDict, 'daysInYear');\n      this.parseRecurrenceArrayValue(recurrenceDict, 'monthsInYear');\n    },\n\n    parseTransparency: function() {\n      var validValues = ['opaque', 'transparent'];\n\n      if (this.parameters.hasOwnProperty('transparency')) {\n        var transparency = this.parameters['transparency'];\n        if (contains(transparency, validValues)) {\n          this.arguments.push('transparency');\n          this.arguments.push(transparency);\n        } else {\n          this.errors.push('transparency must be opaque or transparent');\n        }\n      }\n    },\n\n    parseRecurrenceArrayValue: function(recurrenceDict, kind) {\n      if (recurrenceDict.hasOwnProperty(kind)) {\n        var array = recurrenceDict[kind];\n        if (!array || !(array instanceof Array)) {\n          this.errors.push(kind + ' must be an array.');\n        } else {\n          var arrayStr = array.join(',');\n          this.arguments.push(kind);\n          this.arguments.push(arrayStr);\n        }\n      }\n    },\n\n    parseRecurrenceInterval: function(recurrenceDict) {\n      if (recurrenceDict.hasOwnProperty('interval')) {\n        var interval = recurrenceDict['interval'];\n        if (!interval) {\n          this.errors.push('Recurrence interval cannot be null.');\n        } else {\n          this.arguments.push('interval');\n          this.arguments.push(interval);\n        }\n      } else {\n        // If a recurrence rule was specified without an interval, use a default value of 1.\n        this.arguments.push('interval');\n        this.arguments.push(1);\n      }\n    },\n\n    parseRecurrenceFrequency: function(recurrenceDict) {\n      if (recurrenceDict.hasOwnProperty('frequency')) {\n        var frequency = recurrenceDict['frequency'];\n        var validFrequencies = ['daily', 'weekly', 'monthly', 'yearly'];\n        if (contains(frequency, validFrequencies)) {\n          this.arguments.push('frequency');\n          this.arguments.push(frequency);\n        } else {\n          this.errors.push('Recurrence frequency must be one of: \"daily\", \"weekly\", \"monthly\", \"yearly\".');\n        }\n      }\n    },\n\n    parseRecurrenceEndDate: function(recurrenceDict) {\n      var expires = recurrenceDict['expires'];\n\n      if (!expires) {\n        return;\n      }\n\n      this.arguments.push('expires');\n      this.arguments.push(expires);\n    },\n\n    _getParameter: function(key) {\n      if (this.parameters.hasOwnProperty(key)) {\n        return this.parameters[key];\n      }\n\n      return null;\n    },\n\n    _processStringValue: function(kind) {\n      if (this.parameters.hasOwnProperty(kind)) {\n        var value = this.parameters[kind];\n        this.arguments.push(kind);\n        this.arguments.push(value);\n      }\n    },\n\n    _processDateValue: function(kind) {\n      if (this.parameters.hasOwnProperty(kind)) {\n        var dateString = this._getParameter(kind);\n        this.arguments.push(kind);\n        this.arguments.push(dateString);\n      }\n    },\n  };\n}());\n</script>"), "text/html", "UTF-8", null);
        }
    }

    public void loadUrl(String url) {
        if (url != null) {
            if (url.startsWith("javascript:")) {
                super.loadUrl(url);
            } else {
                HttpClient httpClient = HttpClientFactory.create();
                String outString = Preconditions.EMPTY_ARGUMENTS;
                try {
                    HttpEntity entity = httpClient.execute(new HttpGet(url)).getEntity();
                    if (entity != null) {
                        outString = Strings.fromStream(entity.getContent());
                    }
                    loadHtmlData(outString);
                } catch (IllegalArgumentException e) {
                    MoPubLog.d(new StringBuilder("Mraid loadUrl failed (IllegalArgumentException): ").append(url).toString());
                    notifyOnFailureListener();
                } catch (ClientProtocolException e2) {
                    notifyOnFailureListener();
                } catch (IOException e3) {
                    notifyOnFailureListener();
                }
            }
        }
    }

    public void onResetUserClick() {
        this.mClicked = false;
    }

    public void onUserClick() {
        this.mClicked = true;
    }

    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        boolean newIsVisible = visibility == 0;
        if (newIsVisible != this.mIsVisible) {
            this.mIsVisible = newIsVisible;
            if (this.mHasFiredReadyEvent) {
                fireChangeEventForProperty(MraidViewableProperty.createWithViewable(this.mIsVisible));
            }
        }
    }

    @Deprecated
    void setHasFiredReadyEvent(boolean hasFired) {
        this.mHasFiredReadyEvent = hasFired;
    }

    @Deprecated
    void setMraidDisplayController(MraidDisplayController mraidDisplayController) {
        this.mDisplayController = mraidDisplayController;
    }

    public void setMraidListener(MraidListener mraidListener) {
        this.mListenerInfo.mMraidListener = mraidListener;
    }

    public void setOnCloseButtonStateChange(OnCloseButtonStateChangeListener listener) {
        this.mListenerInfo.mOnCloseButtonListener = listener;
    }

    public boolean wasClicked() {
        return this.mClicked;
    }
}