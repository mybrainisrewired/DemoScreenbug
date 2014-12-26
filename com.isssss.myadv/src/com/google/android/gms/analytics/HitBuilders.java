package com.google.android.gms.analytics;

import android.text.TextUtils;
import com.google.android.gms.analytics.u.a;
import com.google.android.gms.tagmanager.DataLayer;
import java.util.HashMap;
import java.util.Map;
import mobi.vserv.android.ads.VservConstants;

public class HitBuilders {

    protected static class HitBuilder<T extends HitBuilder> {
        private Map<String, String> vl;

        protected HitBuilder() {
            this.vl = new HashMap();
        }

        public Map<String, String> build() {
            return this.vl;
        }

        protected String get(String paramName) {
            return (String) this.vl.get(paramName);
        }

        public final T set(String paramName, String paramValue) {
            u.cy().a(a.tI);
            if (paramName != null) {
                this.vl.put(paramName, paramValue);
            } else {
                aa.z(" HitBuilder.set() called with a null paramName.");
            }
            return this;
        }

        public final T setAll(Map<String, String> params) {
            u.cy().a(a.tJ);
            if (params != null) {
                this.vl.putAll(new HashMap(params));
            }
            return this;
        }

        public T setCampaignParamsFromUrl(String utmParams) {
            u.cy().a(a.tL);
            Object O = ak.O(utmParams);
            if (!TextUtils.isEmpty(O)) {
                Map N = ak.N(O);
                set("&cc", (String) N.get("utm_content"));
                set("&cm", (String) N.get("utm_medium"));
                set("&cn", (String) N.get("utm_campaign"));
                set("&cs", (String) N.get("utm_source"));
                set("&ck", (String) N.get("utm_term"));
                set("&ci", (String) N.get("utm_id"));
                set("&gclid", (String) N.get("gclid"));
                set("&dclid", (String) N.get("dclid"));
                set("&gmob_t", (String) N.get("gmob_t"));
            }
            return this;
        }

        public T setCustomDimension(int index, String dimension) {
            set(o.q(index), dimension);
            return this;
        }

        public T setCustomMetric(int index, float metric) {
            set(o.r(index), Float.toString(metric));
            return this;
        }

        protected T setHitType(String hitType) {
            set("&t", hitType);
            return this;
        }

        public T setNewSession() {
            set("&sc", VservConstants.VPLAY0);
            return this;
        }

        public T setNonInteraction(boolean nonInteraction) {
            set("&ni", ak.u(nonInteraction));
            return this;
        }
    }

    @Deprecated
    public static class AppViewBuilder extends HitBuilder<com.google.android.gms.analytics.HitBuilders.AppViewBuilder> {
        public AppViewBuilder() {
            u.cy().a(a.uS);
            set("&t", "appview");
        }

        public /* bridge */ /* synthetic */ Map build() {
            return super.build();
        }
    }

    public static class EventBuilder extends HitBuilder<com.google.android.gms.analytics.HitBuilders.EventBuilder> {
        public EventBuilder() {
            u.cy().a(a.uG);
            set("&t", DataLayer.EVENT_KEY);
        }

        public EventBuilder(String category, String action) {
            this();
            setCategory(category);
            setAction(action);
        }

        public /* bridge */ /* synthetic */ Map build() {
            return super.build();
        }

        public com.google.android.gms.analytics.HitBuilders.EventBuilder setAction(String action) {
            set("&ea", action);
            return this;
        }

        public com.google.android.gms.analytics.HitBuilders.EventBuilder setCategory(String category) {
            set("&ec", category);
            return this;
        }

        public com.google.android.gms.analytics.HitBuilders.EventBuilder setLabel(String label) {
            set("&el", label);
            return this;
        }

        public com.google.android.gms.analytics.HitBuilders.EventBuilder setValue(long value) {
            set("&ev", Long.toString(value));
            return this;
        }
    }

    public static class ExceptionBuilder extends HitBuilder<com.google.android.gms.analytics.HitBuilders.ExceptionBuilder> {
        public ExceptionBuilder() {
            u.cy().a(a.up);
            set("&t", "exception");
        }

        public /* bridge */ /* synthetic */ Map build() {
            return super.build();
        }

        public com.google.android.gms.analytics.HitBuilders.ExceptionBuilder setDescription(String description) {
            set("&exd", description);
            return this;
        }

        public com.google.android.gms.analytics.HitBuilders.ExceptionBuilder setFatal(boolean fatal) {
            set("&exf", ak.u(fatal));
            return this;
        }
    }

    public static class ItemBuilder extends HitBuilder<com.google.android.gms.analytics.HitBuilders.ItemBuilder> {
        public ItemBuilder() {
            u.cy().a(a.uH);
            set("&t", "item");
        }

        public /* bridge */ /* synthetic */ Map build() {
            return super.build();
        }

        public com.google.android.gms.analytics.HitBuilders.ItemBuilder setCategory(String category) {
            set("&iv", category);
            return this;
        }

        public com.google.android.gms.analytics.HitBuilders.ItemBuilder setCurrencyCode(String currencyCode) {
            set("&cu", currencyCode);
            return this;
        }

        public com.google.android.gms.analytics.HitBuilders.ItemBuilder setName(String name) {
            set("&in", name);
            return this;
        }

        public com.google.android.gms.analytics.HitBuilders.ItemBuilder setPrice(double price) {
            set("&ip", Double.toString(price));
            return this;
        }

        public com.google.android.gms.analytics.HitBuilders.ItemBuilder setQuantity(long quantity) {
            set("&iq", Long.toString(quantity));
            return this;
        }

        public com.google.android.gms.analytics.HitBuilders.ItemBuilder setSku(String sku) {
            set("&ic", sku);
            return this;
        }

        public com.google.android.gms.analytics.HitBuilders.ItemBuilder setTransactionId(String transactionid) {
            set("&ti", transactionid);
            return this;
        }
    }

    public static class ScreenViewBuilder extends HitBuilder<com.google.android.gms.analytics.HitBuilders.ScreenViewBuilder> {
        public ScreenViewBuilder() {
            u.cy().a(a.uS);
            set("&t", "appview");
        }

        public /* bridge */ /* synthetic */ Map build() {
            return super.build();
        }
    }

    public static class SocialBuilder extends HitBuilder<com.google.android.gms.analytics.HitBuilders.SocialBuilder> {
        public SocialBuilder() {
            u.cy().a(a.us);
            set("&t", "social");
        }

        public /* bridge */ /* synthetic */ Map build() {
            return super.build();
        }

        public com.google.android.gms.analytics.HitBuilders.SocialBuilder setAction(String action) {
            set("&sa", action);
            return this;
        }

        public com.google.android.gms.analytics.HitBuilders.SocialBuilder setNetwork(String network) {
            set("&sn", network);
            return this;
        }

        public com.google.android.gms.analytics.HitBuilders.SocialBuilder setTarget(String target) {
            set("&st", target);
            return this;
        }
    }

    public static class TimingBuilder extends HitBuilder<com.google.android.gms.analytics.HitBuilders.TimingBuilder> {
        public TimingBuilder() {
            u.cy().a(a.ur);
            set("&t", "timing");
        }

        public TimingBuilder(String category, String variable, long value) {
            this();
            setVariable(variable);
            setValue(value);
            setCategory(category);
        }

        public /* bridge */ /* synthetic */ Map build() {
            return super.build();
        }

        public com.google.android.gms.analytics.HitBuilders.TimingBuilder setCategory(String category) {
            set("&utc", category);
            return this;
        }

        public com.google.android.gms.analytics.HitBuilders.TimingBuilder setLabel(String label) {
            set("&utl", label);
            return this;
        }

        public com.google.android.gms.analytics.HitBuilders.TimingBuilder setValue(long value) {
            set("&utt", Long.toString(value));
            return this;
        }

        public com.google.android.gms.analytics.HitBuilders.TimingBuilder setVariable(String variable) {
            set("&utv", variable);
            return this;
        }
    }

    public static class TransactionBuilder extends HitBuilder<com.google.android.gms.analytics.HitBuilders.TransactionBuilder> {
        public TransactionBuilder() {
            u.cy().a(a.uo);
            set("&t", "transaction");
        }

        public /* bridge */ /* synthetic */ Map build() {
            return super.build();
        }

        public com.google.android.gms.analytics.HitBuilders.TransactionBuilder setAffiliation(String affiliation) {
            set("&ta", affiliation);
            return this;
        }

        public com.google.android.gms.analytics.HitBuilders.TransactionBuilder setCurrencyCode(String currencyCode) {
            set("&cu", currencyCode);
            return this;
        }

        public com.google.android.gms.analytics.HitBuilders.TransactionBuilder setRevenue(double revenue) {
            set("&tr", Double.toString(revenue));
            return this;
        }

        public com.google.android.gms.analytics.HitBuilders.TransactionBuilder setShipping(double shipping) {
            set("&ts", Double.toString(shipping));
            return this;
        }

        public com.google.android.gms.analytics.HitBuilders.TransactionBuilder setTax(double tax) {
            set("&tt", Double.toString(tax));
            return this;
        }

        public com.google.android.gms.analytics.HitBuilders.TransactionBuilder setTransactionId(String transactionid) {
            set("&ti", transactionid);
            return this;
        }
    }
}