package android.support.v4.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Parcelable;
import android.support.v4.content.IntentCompat;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;

public class ShareCompat {
    public static final String EXTRA_CALLING_ACTIVITY = "android.support.v4.app.EXTRA_CALLING_ACTIVITY";
    public static final String EXTRA_CALLING_PACKAGE = "android.support.v4.app.EXTRA_CALLING_PACKAGE";
    private static ShareCompatImpl IMPL;

    public static class IntentBuilder {
        private Activity mActivity;
        private ArrayList<String> mBccAddresses;
        private ArrayList<String> mCcAddresses;
        private CharSequence mChooserTitle;
        private Intent mIntent;
        private ArrayList<Uri> mStreams;
        private ArrayList<String> mToAddresses;

        private IntentBuilder(Activity launchingActivity) {
            this.mActivity = launchingActivity;
            this.mIntent = new Intent().setAction("android.intent.action.SEND");
            this.mIntent.putExtra(EXTRA_CALLING_PACKAGE, launchingActivity.getPackageName());
            this.mIntent.putExtra(EXTRA_CALLING_ACTIVITY, launchingActivity.getComponentName());
            this.mIntent.addFlags(524288);
        }

        private void combineArrayExtra(String extra, ArrayList<String> add) {
            int currentLength;
            String[] currentAddresses = this.mIntent.getStringArrayExtra(extra);
            if (currentAddresses != null) {
                currentLength = currentAddresses.length;
            } else {
                currentLength = 0;
            }
            String[] finalAddresses = new String[(add.size() + currentLength)];
            add.toArray(finalAddresses);
            if (currentAddresses != null) {
                System.arraycopy(currentAddresses, 0, finalAddresses, add.size(), currentLength);
            }
            this.mIntent.putExtra(extra, finalAddresses);
        }

        private void combineArrayExtra(String extra, String[] add) {
            int oldLength;
            Intent intent = getIntent();
            String[] old = intent.getStringArrayExtra(extra);
            if (old != null) {
                oldLength = old.length;
            } else {
                oldLength = 0;
            }
            String[] result = new String[(add.length + oldLength)];
            if (old != null) {
                System.arraycopy(old, 0, result, 0, oldLength);
            }
            System.arraycopy(add, 0, result, oldLength, add.length);
            intent.putExtra(extra, result);
        }

        public static android.support.v4.app.ShareCompat.IntentBuilder from(Activity launchingActivity) {
            return new android.support.v4.app.ShareCompat.IntentBuilder(launchingActivity);
        }

        public android.support.v4.app.ShareCompat.IntentBuilder addEmailBcc(String address) {
            if (this.mBccAddresses == null) {
                this.mBccAddresses = new ArrayList();
            }
            this.mBccAddresses.add(address);
            return this;
        }

        public android.support.v4.app.ShareCompat.IntentBuilder addEmailBcc(String[] addresses) {
            combineArrayExtra("android.intent.extra.BCC", addresses);
            return this;
        }

        public android.support.v4.app.ShareCompat.IntentBuilder addEmailCc(String address) {
            if (this.mCcAddresses == null) {
                this.mCcAddresses = new ArrayList();
            }
            this.mCcAddresses.add(address);
            return this;
        }

        public android.support.v4.app.ShareCompat.IntentBuilder addEmailCc(String[] addresses) {
            combineArrayExtra("android.intent.extra.CC", addresses);
            return this;
        }

        public android.support.v4.app.ShareCompat.IntentBuilder addEmailTo(String address) {
            if (this.mToAddresses == null) {
                this.mToAddresses = new ArrayList();
            }
            this.mToAddresses.add(address);
            return this;
        }

        public android.support.v4.app.ShareCompat.IntentBuilder addEmailTo(String[] addresses) {
            combineArrayExtra("android.intent.extra.EMAIL", addresses);
            return this;
        }

        public android.support.v4.app.ShareCompat.IntentBuilder addStream(Uri streamUri) {
            Uri currentStream = (Uri) this.mIntent.getParcelableExtra("android.intent.extra.STREAM");
            if (currentStream == null) {
                return setStream(streamUri);
            }
            if (this.mStreams == null) {
                this.mStreams = new ArrayList();
            }
            if (currentStream != null) {
                this.mIntent.removeExtra("android.intent.extra.STREAM");
                this.mStreams.add(currentStream);
            }
            this.mStreams.add(streamUri);
            return this;
        }

        public Intent createChooserIntent() {
            return Intent.createChooser(getIntent(), this.mChooserTitle);
        }

        Activity getActivity() {
            return this.mActivity;
        }

        public Intent getIntent() {
            boolean needsSendMultiple = true;
            if (this.mToAddresses != null) {
                combineArrayExtra("android.intent.extra.EMAIL", this.mToAddresses);
                this.mToAddresses = null;
            }
            if (this.mCcAddresses != null) {
                combineArrayExtra("android.intent.extra.CC", this.mCcAddresses);
                this.mCcAddresses = null;
            }
            if (this.mBccAddresses != null) {
                combineArrayExtra("android.intent.extra.BCC", this.mBccAddresses);
                this.mBccAddresses = null;
            }
            if (this.mStreams == null || this.mStreams.size() <= 1) {
                needsSendMultiple = false;
            }
            boolean isSendMultiple = this.mIntent.getAction().equals("android.intent.action.SEND_MULTIPLE");
            if (!needsSendMultiple && isSendMultiple) {
                this.mIntent.setAction("android.intent.action.SEND");
                if (this.mStreams == null || this.mStreams.isEmpty()) {
                    this.mIntent.removeExtra("android.intent.extra.STREAM");
                } else {
                    this.mIntent.putExtra("android.intent.extra.STREAM", (Parcelable) this.mStreams.get(0));
                }
                this.mStreams = null;
            }
            if (needsSendMultiple && !isSendMultiple) {
                this.mIntent.setAction("android.intent.action.SEND_MULTIPLE");
                if (this.mStreams == null || this.mStreams.isEmpty()) {
                    this.mIntent.removeExtra("android.intent.extra.STREAM");
                } else {
                    this.mIntent.putParcelableArrayListExtra("android.intent.extra.STREAM", this.mStreams);
                }
            }
            return this.mIntent;
        }

        public android.support.v4.app.ShareCompat.IntentBuilder setChooserTitle(int resId) {
            return setChooserTitle(this.mActivity.getText(resId));
        }

        public android.support.v4.app.ShareCompat.IntentBuilder setChooserTitle(CharSequence title) {
            this.mChooserTitle = title;
            return this;
        }

        public android.support.v4.app.ShareCompat.IntentBuilder setEmailBcc(String[] addresses) {
            this.mIntent.putExtra("android.intent.extra.BCC", addresses);
            return this;
        }

        public android.support.v4.app.ShareCompat.IntentBuilder setEmailCc(String[] addresses) {
            this.mIntent.putExtra("android.intent.extra.CC", addresses);
            return this;
        }

        public android.support.v4.app.ShareCompat.IntentBuilder setEmailTo(String[] addresses) {
            if (this.mToAddresses != null) {
                this.mToAddresses = null;
            }
            this.mIntent.putExtra("android.intent.extra.EMAIL", addresses);
            return this;
        }

        public android.support.v4.app.ShareCompat.IntentBuilder setHtmlText(String htmlText) {
            this.mIntent.putExtra(IntentCompat.EXTRA_HTML_TEXT, htmlText);
            if (!this.mIntent.hasExtra("android.intent.extra.TEXT")) {
                setText(Html.fromHtml(htmlText));
            }
            return this;
        }

        public android.support.v4.app.ShareCompat.IntentBuilder setStream(Uri streamUri) {
            if (!this.mIntent.getAction().equals("android.intent.action.SEND")) {
                this.mIntent.setAction("android.intent.action.SEND");
            }
            this.mStreams = null;
            this.mIntent.putExtra("android.intent.extra.STREAM", streamUri);
            return this;
        }

        public android.support.v4.app.ShareCompat.IntentBuilder setSubject(String subject) {
            this.mIntent.putExtra("android.intent.extra.SUBJECT", subject);
            return this;
        }

        public android.support.v4.app.ShareCompat.IntentBuilder setText(CharSequence text) {
            this.mIntent.putExtra("android.intent.extra.TEXT", text);
            return this;
        }

        public android.support.v4.app.ShareCompat.IntentBuilder setType(String mimeType) {
            this.mIntent.setType(mimeType);
            return this;
        }

        public void startChooser() {
            this.mActivity.startActivity(createChooserIntent());
        }
    }

    public static class IntentReader {
        private static final String TAG = "IntentReader";
        private Activity mActivity;
        private ComponentName mCallingActivity;
        private String mCallingPackage;
        private Intent mIntent;
        private ArrayList<Uri> mStreams;

        private IntentReader(Activity activity) {
            this.mActivity = activity;
            this.mIntent = activity.getIntent();
            this.mCallingPackage = ShareCompat.getCallingPackage(activity);
            this.mCallingActivity = ShareCompat.getCallingActivity(activity);
        }

        public static android.support.v4.app.ShareCompat.IntentReader from(Activity activity) {
            return new android.support.v4.app.ShareCompat.IntentReader(activity);
        }

        public ComponentName getCallingActivity() {
            return this.mCallingActivity;
        }

        public Drawable getCallingActivityIcon() {
            if (this.mCallingActivity == null) {
                return null;
            }
            try {
                return this.mActivity.getPackageManager().getActivityIcon(this.mCallingActivity);
            } catch (NameNotFoundException e) {
                Log.e(TAG, "Could not retrieve icon for calling activity", e);
                return null;
            }
        }

        public Drawable getCallingApplicationIcon() {
            if (this.mCallingPackage == null) {
                return null;
            }
            try {
                return this.mActivity.getPackageManager().getApplicationIcon(this.mCallingPackage);
            } catch (NameNotFoundException e) {
                Log.e(TAG, "Could not retrieve icon for calling application", e);
                return null;
            }
        }

        public CharSequence getCallingApplicationLabel() {
            if (this.mCallingPackage == null) {
                return null;
            }
            PackageManager pm = this.mActivity.getPackageManager();
            try {
                return pm.getApplicationLabel(pm.getApplicationInfo(this.mCallingPackage, 0));
            } catch (NameNotFoundException e) {
                Log.e(TAG, "Could not retrieve label for calling application", e);
                return null;
            }
        }

        public String getCallingPackage() {
            return this.mCallingPackage;
        }

        public String[] getEmailBcc() {
            return this.mIntent.getStringArrayExtra("android.intent.extra.BCC");
        }

        public String[] getEmailCc() {
            return this.mIntent.getStringArrayExtra("android.intent.extra.CC");
        }

        public String[] getEmailTo() {
            return this.mIntent.getStringArrayExtra("android.intent.extra.EMAIL");
        }

        public String getHtmlText() {
            String result = this.mIntent.getStringExtra(IntentCompat.EXTRA_HTML_TEXT);
            if (this.mIntent != null) {
                return result;
            }
            CharSequence text = getText();
            if (text instanceof Spanned) {
                return Html.toHtml((Spanned) text);
            }
            return text != null ? IMPL.escapeHtml(text) : result;
        }

        public Uri getStream() {
            return (Uri) this.mIntent.getParcelableExtra("android.intent.extra.STREAM");
        }

        public Uri getStream(int index) {
            if (this.mStreams == null && isMultipleShare()) {
                this.mStreams = this.mIntent.getParcelableArrayListExtra("android.intent.extra.STREAM");
            }
            if (this.mStreams != null) {
                return (Uri) this.mStreams.get(index);
            }
            if (index == 0) {
                return (Uri) this.mIntent.getParcelableExtra("android.intent.extra.STREAM");
            }
            throw new IndexOutOfBoundsException("Stream items available: " + getStreamCount() + " index requested: " + index);
        }

        public int getStreamCount() {
            if (this.mStreams == null && isMultipleShare()) {
                this.mStreams = this.mIntent.getParcelableArrayListExtra("android.intent.extra.STREAM");
            }
            if (this.mStreams != null) {
                return this.mStreams.size();
            }
            return this.mIntent.hasExtra("android.intent.extra.STREAM") ? 1 : 0;
        }

        public String getSubject() {
            return this.mIntent.getStringExtra("android.intent.extra.SUBJECT");
        }

        public CharSequence getText() {
            return this.mIntent.getCharSequenceExtra("android.intent.extra.TEXT");
        }

        public String getType() {
            return this.mIntent.getType();
        }

        public boolean isMultipleShare() {
            return this.mIntent.getAction().equals("android.intent.action.SEND_MULTIPLE");
        }

        public boolean isShareIntent() {
            String action = this.mIntent.getAction();
            return action.equals("android.intent.action.SEND") || action.equals("android.intent.action.SEND_MULTIPLE");
        }

        public boolean isSingleShare() {
            return this.mIntent.getAction().equals("android.intent.action.SEND");
        }
    }

    static interface ShareCompatImpl {
        void configureMenuItem(MenuItem menuItem, android.support.v4.app.ShareCompat.IntentBuilder intentBuilder);

        String escapeHtml(CharSequence charSequence);
    }

    static class ShareCompatImplBase implements ShareCompatImpl {
        ShareCompatImplBase() {
        }

        private static void withinStyle(StringBuilder out, CharSequence text, int start, int end) {
            int i = start;
            while (i < end) {
                char c = text.charAt(i);
                if (c == '<') {
                    out.append("&lt;");
                } else if (c == '>') {
                    out.append("&gt;");
                } else if (c == '&') {
                    out.append("&amp;");
                } else if (c > '~' || c < ' ') {
                    out.append("&#" + c + ";");
                } else {
                    if (c != ' ') {
                        out.append(c);
                    }
                    while (i + 1 < end && text.charAt(i + 1) == ' ') {
                        out.append("&nbsp;");
                        i++;
                    }
                    out.append(' ');
                }
                i++;
            }
        }

        public void configureMenuItem(MenuItem item, android.support.v4.app.ShareCompat.IntentBuilder shareIntent) {
            item.setIntent(shareIntent.createChooserIntent());
        }

        public String escapeHtml(CharSequence text) {
            StringBuilder out = new StringBuilder();
            withinStyle(out, text, 0, text.length());
            return out.toString();
        }
    }

    static class ShareCompatImplICS extends ShareCompatImplBase {
        ShareCompatImplICS() {
        }

        public void configureMenuItem(MenuItem item, android.support.v4.app.ShareCompat.IntentBuilder shareIntent) {
            ShareCompatICS.configureMenuItem(item, shareIntent.getActivity(), shareIntent.getIntent());
            if (shouldAddChooserIntent(item)) {
                item.setIntent(shareIntent.createChooserIntent());
            }
        }

        boolean shouldAddChooserIntent(MenuItem item) {
            return !item.hasSubMenu();
        }
    }

    static class ShareCompatImplJB extends ShareCompatImplICS {
        ShareCompatImplJB() {
        }

        public String escapeHtml(CharSequence html) {
            return ShareCompatJB.escapeHtml(html);
        }

        boolean shouldAddChooserIntent(MenuItem item) {
            return false;
        }
    }

    static {
        if (VERSION.SDK_INT >= 16) {
            IMPL = new ShareCompatImplJB();
        } else if (VERSION.SDK_INT >= 14) {
            IMPL = new ShareCompatImplICS();
        } else {
            IMPL = new ShareCompatImplBase();
        }
    }

    public static void configureMenuItem(Menu menu, int menuItemId, IntentBuilder shareIntent) {
        MenuItem item = menu.findItem(menuItemId);
        if (item == null) {
            throw new IllegalArgumentException("Could not find menu item with id " + menuItemId + " in the supplied menu");
        }
        configureMenuItem(item, shareIntent);
    }

    public static void configureMenuItem(MenuItem item, IntentBuilder shareIntent) {
        IMPL.configureMenuItem(item, shareIntent);
    }

    public static ComponentName getCallingActivity(Activity calledActivity) {
        ComponentName result = calledActivity.getCallingActivity();
        return result == null ? calledActivity.getIntent().getParcelableExtra(EXTRA_CALLING_ACTIVITY) : result;
    }

    public static String getCallingPackage(Activity calledActivity) {
        String result = calledActivity.getCallingPackage();
        return result == null ? calledActivity.getIntent().getStringExtra(EXTRA_CALLING_PACKAGE) : result;
    }
}