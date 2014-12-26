package com.loopme.utilites;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import com.inmobi.commons.analytics.db.AnalyticsEvent;
import com.mopub.common.Preconditions;
import java.util.Locale;

public class LanguageManager {
    private static final String EXIT_NO = "_exit_no";
    private static final String EXIT_TEXT = "_exit_text";
    private static final String EXIT_TITLE = "_exit_title";
    private static final String EXIT_YES = "_exit_yes";
    private static final String IS_PARSED = "is_parsed";
    private static final String LANG_SHARED_PREFS = "loopme_lang_prefs";
    private static final String NOTIFICATION_TEXT = "_notification_text";
    private static LanguageManager instance;
    private boolean mIsInit;
    private Editor prefsEditor;
    private SharedPreferences sharedPrefs;

    private class LanguageParseThread extends AsyncTask<Void, Void, Void> {
        private LanguageParseThread() {
        }

        protected Void doInBackground(Void... voids) {
            LanguageManager.this.prefsEditor.putString("en", "apps, offers");
            LanguageManager.this.prefsEditor.putString(AnalyticsEvent.TYPE_END_SESSION, "apps, ofertas");
            LanguageManager.this.prefsEditor.putString("de", "Apps, Angebote");
            LanguageManager.this.prefsEditor.putString("fr", "apps, offre");
            LanguageManager.this.prefsEditor.putString("ru", "\u041f\u0440\u0438\u043b\u043e\u0436\u0435\u043d\u0438\u044f, \u041f\u0440\u0435\u0434\u043b\u043e\u0436\u0435\u043d\u0438\u044f");
            LanguageManager.this.prefsEditor.putString("it", "apps, offerta");
            LanguageManager.this.prefsEditor.putString("fi", "apps, tarjous");
            LanguageManager.this.prefsEditor.putString("pt", "apps, ofertas");
            LanguageManager.this.prefsEditor.putString("da", "apps, tilbud");
            LanguageManager.this.prefsEditor.putString("en_exit_title", "Exit");
            LanguageManager.this.prefsEditor.putString("en_notification_text", "Ad Notification");
            LanguageManager.this.prefsEditor.putString("en_exit_text", "Do you want to discover new free apps and offers?");
            LanguageManager.this.prefsEditor.putString("en_exit_yes", "Yes");
            LanguageManager.this.prefsEditor.putString("en_exit_no", "No");
            LanguageManager.this.prefsEditor.putString("es_exit_title", "Salir");
            LanguageManager.this.prefsEditor.putString("es_notification_text", "Notificaci\u00f3n de anuncio");
            LanguageManager.this.prefsEditor.putString("es_exit_text", "Quieres descubrir nuevas aplicaciones y ofertas?");
            LanguageManager.this.prefsEditor.putString("es_exit_yes", "Si");
            LanguageManager.this.prefsEditor.putString("es_exit_no", "No");
            LanguageManager.this.prefsEditor.putString("de_exit_title", "App verlassen");
            LanguageManager.this.prefsEditor.putString("de_notification_text", "-");
            LanguageManager.this.prefsEditor.putString("de_exit_text", "Neue kostenlose Apps und Angebote entdecken?");
            LanguageManager.this.prefsEditor.putString("de_exit_yes", "Ja");
            LanguageManager.this.prefsEditor.putString("de_exit_no", "Nein");
            LanguageManager.this.prefsEditor.putString("fr_exit_title", "Sortie");
            LanguageManager.this.prefsEditor.putString("fr_notification_text", "-");
            LanguageManager.this.prefsEditor.putString("fr_exit_text", "Voulez vous decouvrir de nouvelles offres et apps?");
            LanguageManager.this.prefsEditor.putString("fr_exit_yes", "Oui");
            LanguageManager.this.prefsEditor.putString("fr_exit_no", "Non");
            LanguageManager.this.prefsEditor.putString("ru_exit_title", "\u0412\u044b\u0445\u043e\u0434");
            LanguageManager.this.prefsEditor.putString("ru_notification_text", "-");
            LanguageManager.this.prefsEditor.putString("ru_exit_text", "\u0412\u044b \u0445\u043e\u0442\u0438\u0442\u0435 \u043e\u0442\u043a\u0440\u044b\u0442\u044c \u043d\u043e\u0432\u044b\u0435 \u0431\u0435\u0441\u043f\u043b\u0430\u0442\u043d\u044b\u0435 \u043f\u0440\u0438\u043b\u043e\u0436\u0435\u043d\u0438\u044f \u0438 \u043f\u0440\u0435\u0434\u043b\u043e\u0436\u0435\u043d\u0438\u044f?");
            LanguageManager.this.prefsEditor.putString("ru_exit_yes", "\u0414\u0430");
            LanguageManager.this.prefsEditor.putString("ru_exit_no", "\u041d\u0435\u0442");
            LanguageManager.this.prefsEditor.putString("it_exit_title", "Esci");
            LanguageManager.this.prefsEditor.putString("it_notification_text", "-");
            LanguageManager.this.prefsEditor.putString("it_exit_text", "Scopri nuove app e offerte?");
            LanguageManager.this.prefsEditor.putString("it_exit_yes", "Si");
            LanguageManager.this.prefsEditor.putString("it_exit_no", "No");
            LanguageManager.this.prefsEditor.putString("pt_exit_title", "Salir");
            LanguageManager.this.prefsEditor.putString("pt_notification_text", "Notifica\u00e7\u00e3o de anuncio");
            LanguageManager.this.prefsEditor.putString("pt_exit_text", "Quieres descubrir nuevas aplicaciones y ofertas?");
            LanguageManager.this.prefsEditor.putString("pt_exit_yes", "Si");
            LanguageManager.this.prefsEditor.putString("pt_exit_no", "No");
            LanguageManager.this.prefsEditor.putString("da_exit_title", "Exit");
            LanguageManager.this.prefsEditor.putString("da_notification_text", "Advertentie");
            LanguageManager.this.prefsEditor.putString("da_exit_text", "Wilt u nieuwe gratis apps and aanbieding ontdekken?");
            LanguageManager.this.prefsEditor.putString("da_exit_yes", "Ja");
            LanguageManager.this.prefsEditor.putString("da_exit_no", "Nee");
            LanguageManager.this.prefsEditor.putBoolean(IS_PARSED, true);
            LanguageManager.this.prefsEditor.commit();
            LanguageManager.this.mIsInit = true;
            return null;
        }
    }

    private LanguageManager(Context context) {
        this.sharedPrefs = context.getSharedPreferences(LANG_SHARED_PREFS, 0);
        this.prefsEditor = this.sharedPrefs.edit();
        if (!this.mIsInit) {
            new LanguageParseThread(null).execute(new Void[0]);
        }
    }

    public static synchronized LanguageManager getInstance(Context context) {
        LanguageManager languageManager;
        synchronized (LanguageManager.class) {
            if (instance == null) {
                instance = new LanguageManager(context);
            }
            languageManager = instance;
        }
        return languageManager;
    }

    public String getExitNo() {
        String lang = Locale.getDefault().getLanguage();
        return this.sharedPrefs.getString(new StringBuilder(String.valueOf(lang)).append(EXIT_NO).toString(), this.sharedPrefs.getString("en_exit_no", Preconditions.EMPTY_ARGUMENTS));
    }

    public String getExitText() {
        String lang = Locale.getDefault().getLanguage();
        return this.sharedPrefs.getString(new StringBuilder(String.valueOf(lang)).append(EXIT_TEXT).toString(), this.sharedPrefs.getString("en_exit_text", Preconditions.EMPTY_ARGUMENTS));
    }

    public String getExitTitle() {
        String lang = Locale.getDefault().getLanguage();
        return this.sharedPrefs.getString(new StringBuilder(String.valueOf(lang)).append(EXIT_TITLE).toString(), this.sharedPrefs.getString("en_exit_title", Preconditions.EMPTY_ARGUMENTS));
    }

    public String getExitYes() {
        String lang = Locale.getDefault().getLanguage();
        return this.sharedPrefs.getString(new StringBuilder(String.valueOf(lang)).append(EXIT_YES).toString(), this.sharedPrefs.getString("en_exit_yes", Preconditions.EMPTY_ARGUMENTS));
    }

    public String getNotificationText() {
        String lang = Locale.getDefault().getLanguage();
        return this.sharedPrefs.getString(new StringBuilder(String.valueOf(lang)).append(NOTIFICATION_TEXT).toString(), this.sharedPrefs.getString("en_notification_text", Preconditions.EMPTY_ARGUMENTS));
    }
}