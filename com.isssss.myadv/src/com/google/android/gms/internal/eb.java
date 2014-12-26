package com.google.android.gms.internal;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.os.Message;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.ConsoleMessage.MessageLevel;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebStorage.QuotaUpdater;
import android.webkit.WebView;
import android.webkit.WebView.WebViewTransport;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public class eb extends WebChromeClient {
    private final dz lC;

    static class AnonymousClass_1 implements OnCancelListener {
        final /* synthetic */ JsResult rI;

        AnonymousClass_1(JsResult jsResult) {
            this.rI = jsResult;
        }

        public void onCancel(DialogInterface dialog) {
            this.rI.cancel();
        }
    }

    static class AnonymousClass_2 implements OnClickListener {
        final /* synthetic */ JsResult rI;

        AnonymousClass_2(JsResult jsResult) {
            this.rI = jsResult;
        }

        public void onClick(DialogInterface dialog, int which) {
            this.rI.cancel();
        }
    }

    static class AnonymousClass_3 implements OnClickListener {
        final /* synthetic */ JsResult rI;

        AnonymousClass_3(JsResult jsResult) {
            this.rI = jsResult;
        }

        public void onClick(DialogInterface dialog, int which) {
            this.rI.confirm();
        }
    }

    static class AnonymousClass_4 implements OnCancelListener {
        final /* synthetic */ JsPromptResult rJ;

        AnonymousClass_4(JsPromptResult jsPromptResult) {
            this.rJ = jsPromptResult;
        }

        public void onCancel(DialogInterface dialog) {
            this.rJ.cancel();
        }
    }

    static class AnonymousClass_5 implements OnClickListener {
        final /* synthetic */ JsPromptResult rJ;

        AnonymousClass_5(JsPromptResult jsPromptResult) {
            this.rJ = jsPromptResult;
        }

        public void onClick(DialogInterface dialog, int which) {
            this.rJ.cancel();
        }
    }

    static class AnonymousClass_6 implements OnClickListener {
        final /* synthetic */ JsPromptResult rJ;
        final /* synthetic */ EditText rK;

        AnonymousClass_6(JsPromptResult jsPromptResult, EditText editText) {
            this.rJ = jsPromptResult;
            this.rK = editText;
        }

        public void onClick(DialogInterface dialog, int which) {
            this.rJ.confirm(this.rK.getText().toString());
        }
    }

    static /* synthetic */ class AnonymousClass_7 {
        static final /* synthetic */ int[] rL;

        static {
            rL = new int[MessageLevel.values().length];
            try {
                rL[MessageLevel.ERROR.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                rL[MessageLevel.WARNING.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                rL[MessageLevel.LOG.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                rL[MessageLevel.TIP.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            rL[MessageLevel.DEBUG.ordinal()] = 5;
        }
    }

    public eb(dz dzVar) {
        this.lC = dzVar;
    }

    private static void a(Builder builder, String str, JsResult jsResult) {
        builder.setMessage(str).setPositiveButton(17039370, new AnonymousClass_3(jsResult)).setNegativeButton(17039360, new AnonymousClass_2(jsResult)).setOnCancelListener(new AnonymousClass_1(jsResult)).create().show();
    }

    private static void a(Context context, Builder builder, String str, String str2, JsPromptResult jsPromptResult) {
        View linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        View textView = new TextView(context);
        textView.setText(str);
        View editText = new EditText(context);
        editText.setText(str2);
        linearLayout.addView(textView);
        linearLayout.addView(editText);
        builder.setView(linearLayout).setPositiveButton(17039370, new AnonymousClass_6(jsPromptResult, editText)).setNegativeButton(17039360, new AnonymousClass_5(jsPromptResult)).setOnCancelListener(new AnonymousClass_4(jsPromptResult)).create().show();
    }

    protected final void a(View view, int i, CustomViewCallback customViewCallback) {
        cc bH = this.lC.bH();
        if (bH == null) {
            dw.z("Could not get ad overlay when showing custom view.");
            customViewCallback.onCustomViewHidden();
        } else {
            bH.a(view, customViewCallback);
            bH.setRequestedOrientation(i);
        }
    }

    protected boolean a(Context context, String str, String str2, String str3, JsResult jsResult, JsPromptResult jsPromptResult, boolean z) {
        Builder builder = new Builder(context);
        builder.setTitle(str);
        if (z) {
            a(context, builder, str2, str3, jsPromptResult);
        } else {
            a(builder, str2, jsResult);
        }
        return true;
    }

    public final void onCloseWindow(WebView webView) {
        if (webView instanceof dz) {
            cc bH = ((dz) webView).bH();
            if (bH == null) {
                dw.z("Tried to close an AdWebView not associated with an overlay.");
            } else {
                bH.close();
            }
        } else {
            dw.z("Tried to close a WebView that wasn't an AdWebView.");
        }
    }

    public final boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        String str = "JS: " + consoleMessage.message() + " (" + consoleMessage.sourceId() + ":" + consoleMessage.lineNumber() + ")";
        switch (AnonymousClass_7.rL[consoleMessage.messageLevel().ordinal()]) {
            case MMAdView.TRANSITION_FADE:
                dw.w(str);
                break;
            case MMAdView.TRANSITION_UP:
                dw.z(str);
                break;
            case MMAdView.TRANSITION_DOWN:
            case MMAdView.TRANSITION_RANDOM:
                dw.x(str);
                break;
            case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                dw.v(str);
                break;
            default:
                dw.x(str);
                break;
        }
        return super.onConsoleMessage(consoleMessage);
    }

    public final boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        WebViewTransport webViewTransport = (WebViewTransport) resultMsg.obj;
        WebView webView = new WebView(view.getContext());
        webView.setWebViewClient(this.lC.bI());
        webViewTransport.setWebView(webView);
        resultMsg.sendToTarget();
        return true;
    }

    public final void onExceededDatabaseQuota(String url, String databaseIdentifier, long currentQuota, long estimatedSize, long totalUsedQuota, QuotaUpdater quotaUpdater) {
        long j = 5242880 - totalUsedQuota;
        if (j <= 0) {
            quotaUpdater.updateQuota(currentQuota);
        } else {
            if (currentQuota == 0) {
                estimatedSize = 0;
            } else if (estimatedSize == 0) {
                estimatedSize = Math.min(Math.min(131072, j) + currentQuota, 1048576);
            } else {
                if (estimatedSize <= Math.min(1048576 - currentQuota, j)) {
                    currentQuota += estimatedSize;
                }
                estimatedSize = currentQuota;
            }
            quotaUpdater.updateQuota(estimatedSize);
        }
    }

    public final void onHideCustomView() {
        cc bH = this.lC.bH();
        if (bH == null) {
            dw.z("Could not get ad overlay when hiding custom view.");
        } else {
            bH.aL();
        }
    }

    public final boolean onJsAlert(WebView webView, String url, String message, JsResult result) {
        return a(webView.getContext(), url, message, null, result, null, false);
    }

    public final boolean onJsBeforeUnload(WebView webView, String url, String message, JsResult result) {
        return a(webView.getContext(), url, message, null, result, null, false);
    }

    public final boolean onJsConfirm(WebView webView, String url, String message, JsResult result) {
        return a(webView.getContext(), url, message, null, result, null, false);
    }

    public final boolean onJsPrompt(WebView webView, String url, String message, String defaultValue, JsPromptResult result) {
        return a(webView.getContext(), url, message, defaultValue, null, result, true);
    }

    public final void onReachedMaxAppCacheSize(long spaceNeeded, long totalUsedQuota, QuotaUpdater quotaUpdater) {
        long j = 131072 + spaceNeeded;
        if (5242880 - totalUsedQuota < j) {
            quotaUpdater.updateQuota(0);
        } else {
            quotaUpdater.updateQuota(j);
        }
    }

    public final void onShowCustomView(View view, CustomViewCallback customViewCallback) {
        a(view, -1, customViewCallback);
    }
}