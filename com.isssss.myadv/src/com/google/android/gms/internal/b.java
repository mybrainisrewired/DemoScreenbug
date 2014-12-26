package com.google.android.gms.internal;

import com.google.android.gms.plus.PlusShare;
import com.inmobi.commons.analytics.db.AnalyticsEvent;
import com.inmobi.commons.analytics.db.AnalyticsSQLiteHelper;
import com.inmobi.commons.analytics.iat.impl.AdTrackerConstants;
import com.isssss.myadv.dao.BannerInfoTable;
import com.mopub.common.MoPubBrowser;

public enum b {
    ACCOUNT("account"),
    ACTIVITY("activity"),
    ADDITIONAL_PARAMS("additional_params"),
    ADVERTISER("advertiser"),
    ALGORITHM("algorithm"),
    ALLOW_ANCHOR("allow_anchor"),
    ALLOW_HASH("allow_hash"),
    ALLOW_LINKER("allow_linker"),
    ANALYTICS_FIELDS("analytics_fields"),
    ANALYTICS_PASS_THROUGH("analytics_pass_through"),
    ANONYMIZE_IP("anonymize_ip"),
    APP_NAME("app_name"),
    APP_VERSION("app_version"),
    ARG0("arg0"),
    ARG1("arg1"),
    ATTRIBUTE(AnalyticsSQLiteHelper.TABLE_USER_ATTRIBUTE),
    AUTO_LINK_DOMAINS("auto_link_domains"),
    CACHE_BUSTER("cache_buster"),
    CACHE_BUSTER_VALUE("cache_buster_value"),
    CAMPAIGN_CONTENT_KEY("campaign_content_key"),
    CAMPAIGN_CONTENT_OVERRIDE("campaign_content_override"),
    CAMPAIGN_COOKIE_TIMEOUT("campaign_cookie_timeout"),
    CAMPAIGN_MEDIUM_KEY("campaign_medium_key"),
    CAMPAIGN_MEDIUM_OVERRIDE("campaign_medium_override"),
    CAMPAIGN_NAME_KEY("campaign_name_key"),
    CAMPAIGN_NAME_OVERRIDE("campaign_name_override"),
    CAMPAIGN_NO_KEY("campaign_no_key"),
    CAMPAIGN_SOURCE_KEY("campaign_source_key"),
    CAMPAIGN_SOURCE_OVERRIDE("campaign_source_override"),
    CAMPAIGN_TERM_KEY("campaign_term_key"),
    CAMPAIGN_TERM_OVERRIDE("campaign_term_override"),
    CAMPAIGN_TRACK("campaign_track"),
    CATEGORY("category"),
    CHECK_VALIDATION("check_validation"),
    CLEAR_PERSISTENT_DATA_LAYER_PREFIX("clear_data_layer_prefix"),
    CLICK_ID("click_id"),
    CLIENT_INFO("client_info"),
    COMPANY("company"),
    COMPONENT("component"),
    CONTENT_DESCRIPTION("content_description"),
    CONTENT_GROUP("content_group"),
    CONVERSION_ID("conversion_id"),
    COOKIE_DOMAIN("cookie_domain"),
    COOKIE_EXPIRATION("cookie_expiration"),
    COOKIE_NAME("cookie_name"),
    COOKIE_PATH("cookie_path"),
    COOKIE_PATH_COPY("cookie_path_copy"),
    COUNTRY("country"),
    CURRENCY_CODE("currency_code"),
    CUSTOM_VARS("custom_vars"),
    CUSTOMER_ID("customer_id"),
    DATA_LAYER_VERSION("data_layer_version"),
    DATA_PROVIDER_ID("data_provider_id"),
    DEBUG("debug"),
    DECORATE_FORM("decorate_form"),
    DECORATE_FORMS_AUTO_LINK("decorate_forms_auto_link"),
    DECORATE_LINK("decorate_link"),
    DEFAULT_PAGES("default_pages"),
    DEFAULT_VALUE("default_value"),
    DEPENDENCIES("dependencies"),
    DETECT_FLASH("detect_flash"),
    DETECT_TITLE("detect_title"),
    DIMENSION("dimension"),
    DOMAIN_NAME("domain_name"),
    DOUBLE_CLICK("double_click"),
    ECOMMERCE_MACRO_DATA("ecommerce_macro_data"),
    ECOMMERCE_USE_DATA_LAYER("ecommerce_use_data_layer"),
    ELEMENT("element"),
    EMAIL(Event.INTENT_EMAIL),
    EMPLOYEE_RANGE("employee_range"),
    ENABLE_ECOMMERCE("enable_ecommerce"),
    ESCAPE("escape"),
    EVENT_ACTION("event_action"),
    EVENT_CATEGORY("event_category"),
    EVENT_LABEL("event_label"),
    EVENT_VALUE("event_value"),
    EXCEPTION_DESCRIPTION("exception_description"),
    EXCEPTION_FATAL("exception_fatal"),
    FIELDS_TO_SET("fields_to_set"),
    FORM_OBJECT("form_object"),
    FUNCTION("function"),
    FUNCTION_CALL_NAME("function_call_macro_name"),
    GROUP("group"),
    HIT_CALLBACK("hit_callback"),
    HTML("html"),
    ID(AnalyticsEvent.EVENT_ID),
    IGNORED_ORGANIC("ignored_organic"),
    IGNORED_REF("ignored_ref"),
    IGNORE_CASE("ignore_case"),
    INPUT("input"),
    INPUT_FORMAT("input_format"),
    INSTANCE_NAME("instance_name"),
    INSTANCE_LABEL("instance_label"),
    INTERVAL("interval"),
    ITEM_SEPARATOR("item_separator"),
    JAVASCRIPT("javascript"),
    KEYWORD("keyword"),
    KEY_VALUE_SEPARATOR("key_value_separator"),
    LABEL(PlusShare.KEY_CALL_TO_ACTION_LABEL),
    LANGUAGE("language"),
    LIMIT("limit"),
    LINK("link"),
    LINK_BY_POST("link_by_post"),
    LINK_ID("link_id"),
    LIVE_ONLY("live_only"),
    LOCAL_GIF_PATH("local_gif_path"),
    LOCATION("location"),
    MAP("map"),
    MAX("max"),
    MIN("min"),
    METRIC("metric"),
    NAME("name"),
    NAMESPACE_CODE("namespace_code"),
    NAMESPACE_ID("namespace_id"),
    NAMESPACE_VALUE("namespace_value"),
    NONINTERACTION("noninteraction"),
    NOT_DEFAULT_MACRO("not_default_macro"),
    NO_PADDING("no_padding"),
    NUMBER("number"),
    OPTOUT("optout"),
    ORDER("order"),
    ORDER_ID("order_id"),
    ORDER_VALUE("order_value"),
    ORDINAL("ordinal"),
    ORGANIC("organic"),
    OUTPUT_FORMAT("output_format"),
    PAGE("page"),
    PAGE_PATH("page_path"),
    PARTITION("partition"),
    PIXEL("pixel"),
    PLATFORM("platform"),
    PRICES("prices"),
    PRIORITY("priority"),
    PRODUCT("product"),
    PRODUCT_ID("product_id"),
    PRODUCT_IDS("product_ids"),
    PUSH_AFTER_EVALUATE("push_after_evaluate"),
    QUANTITY("quantity"),
    QUERY_KEY("query_key"),
    REFERRER(AdTrackerConstants.REFERRER),
    REFERRER_OVERRIDE("referrer_override"),
    REVENUE("revenue"),
    SAMPLE_RATE("sample_rate"),
    SEND_HITS_TO_GOOGLE("send_hits_to_google"),
    SESSION_CONTROL("session_control"),
    SESSION_COOKIE_TIMEOUT("session_cookie_timeout"),
    SITE_SPEED_SAMPLE_RATE("site_speed_sample_rate"),
    SOCIAL_ACTION("social_action"),
    SOCIAL_ACTION_TARGET("social_action_target"),
    SOCIAL_NETWORK("social_network"),
    SOCIAL_USE_DATA_LAYER("social_use_data_layer"),
    SERVER_SIDE("server_side"),
    STANDARD_INDUSTRIAL_CLASSIFICATION("standard_industrial_classification"),
    STRIP_WWW("strip_www"),
    TAG_ID("tag_id"),
    TARGET_URL("target_url"),
    TIMING_CATEGORY("timing_category"),
    TIMING_LABEL("timing_label"),
    TIMING_SAMPLE_RATE("timing_sample_rate"),
    TIMING_VALUE("timing_value"),
    TIMING_VAR("timing_var"),
    TITLE(BannerInfoTable.COLUMN_TITLE),
    TRACK_APPVIEW("track_appview"),
    TRACK_EVENT("track_event"),
    TRACK_EXCEPTION("track_exception"),
    TRACK_SOCIAL("track_social"),
    TRACK_TIMING("track_timing"),
    TRACK_TRANSACTION("track_transaction"),
    TRACKER_NAME("tracker_name"),
    TRANSACTION_DATALAYER_MAP("transaction_datalayer_map"),
    TRANSACTION_ID("transaction_id"),
    TRANSACTION_ITEM_DATALAYER_MAP("transaction_item_datalayer_map"),
    TRANSACTION_VARIABLE("transaction_variable"),
    TYPE(AnalyticsSQLiteHelper.EVENT_LIST_TYPE),
    UNREPEATABLE("unrepeatable"),
    URL(PlusShare.KEY_CALL_TO_ACTION_URL),
    USE_DATA_LAYER("use_data_layer"),
    USE_HASH("use_hash"),
    USE_IFRAME("use_iframe"),
    USE_IMAGE_TAG("use_image_tag"),
    USE_POSTSCRIBE("use_postscribe"),
    USER_ID("user_id"),
    USER_VARIABLE("user_variable"),
    VALUE("value"),
    VALUE_IN_DOLLARS("value_in_dollars"),
    VISITOR_COOKIE_TIMEOUT("visitor_cookie_timeout"),
    WAIT_FOR_TAGS("wait_for_tags"),
    WAIT_FOR_TAGS_TIMEOUT("wait_for_tags_timeout"),
    WIDGET_IDS("widget_ids");
    private final String eC;

    static {
        String str = "account";
        aV = new b("ACCOUNT", 0, "account");
        str = "activity";
        aW = new b("ACTIVITY", 1, "activity");
        str = "additional_params";
        aX = new b("ADDITIONAL_PARAMS", 2, "additional_params");
        str = "advertiser";
        aY = new b("ADVERTISER", 3, "advertiser");
        str = "algorithm";
        aZ = new b("ALGORITHM", 4, "algorithm");
        String str2 = "allow_anchor";
        ba = new b("ALLOW_ANCHOR", 5, "allow_anchor");
        str2 = "allow_hash";
        bb = new b("ALLOW_HASH", 6, "allow_hash");
        str2 = "allow_linker";
        bc = new b("ALLOW_LINKER", 7, "allow_linker");
        str2 = "analytics_fields";
        bd = new b("ANALYTICS_FIELDS", 8, "analytics_fields");
        str2 = "analytics_pass_through";
        be = new b("ANALYTICS_PASS_THROUGH", 9, "analytics_pass_through");
        str2 = "anonymize_ip";
        bf = new b("ANONYMIZE_IP", 10, "anonymize_ip");
        str2 = "app_name";
        bg = new b("APP_NAME", 11, "app_name");
        str2 = "app_version";
        bh = new b("APP_VERSION", 12, "app_version");
        str2 = "arg0";
        bi = new b("ARG0", 13, "arg0");
        str2 = "arg1";
        bj = new b("ARG1", 14, "arg1");
        String str3 = "ATTRIBUTE";
        str2 = AnalyticsSQLiteHelper.TABLE_USER_ATTRIBUTE;
        bk = new b(str3, 15, AnalyticsSQLiteHelper.TABLE_USER_ATTRIBUTE);
        str2 = "auto_link_domains";
        bl = new b("AUTO_LINK_DOMAINS", 16, "auto_link_domains");
        str2 = "cache_buster";
        bm = new b("CACHE_BUSTER", 17, "cache_buster");
        str2 = "cache_buster_value";
        bn = new b("CACHE_BUSTER_VALUE", 18, "cache_buster_value");
        str2 = "campaign_content_key";
        bo = new b("CAMPAIGN_CONTENT_KEY", 19, "campaign_content_key");
        str2 = "campaign_content_override";
        bp = new b("CAMPAIGN_CONTENT_OVERRIDE", 20, "campaign_content_override");
        str2 = "campaign_cookie_timeout";
        bq = new b("CAMPAIGN_COOKIE_TIMEOUT", 21, "campaign_cookie_timeout");
        str2 = "campaign_medium_key";
        br = new b("CAMPAIGN_MEDIUM_KEY", 22, "campaign_medium_key");
        str2 = "campaign_medium_override";
        bs = new b("CAMPAIGN_MEDIUM_OVERRIDE", 23, "campaign_medium_override");
        str2 = "campaign_name_key";
        bt = new b("CAMPAIGN_NAME_KEY", 24, "campaign_name_key");
        str2 = "campaign_name_override";
        bu = new b("CAMPAIGN_NAME_OVERRIDE", 25, "campaign_name_override");
        str2 = "campaign_no_key";
        bv = new b("CAMPAIGN_NO_KEY", 26, "campaign_no_key");
        str2 = "campaign_source_key";
        bw = new b("CAMPAIGN_SOURCE_KEY", 27, "campaign_source_key");
        str2 = "campaign_source_override";
        bx = new b("CAMPAIGN_SOURCE_OVERRIDE", 28, "campaign_source_override");
        str2 = "campaign_term_key";
        by = new b("CAMPAIGN_TERM_KEY", 29, "campaign_term_key");
        str2 = "campaign_term_override";
        bz = new b("CAMPAIGN_TERM_OVERRIDE", 30, "campaign_term_override");
        str2 = "campaign_track";
        bA = new b("CAMPAIGN_TRACK", 31, "campaign_track");
        str2 = "category";
        bB = new b("CATEGORY", 32, "category");
        str2 = "check_validation";
        bC = new b("CHECK_VALIDATION", 33, "check_validation");
        str2 = "clear_data_layer_prefix";
        bD = new b("CLEAR_PERSISTENT_DATA_LAYER_PREFIX", 34, "clear_data_layer_prefix");
        str2 = "click_id";
        bE = new b("CLICK_ID", 35, "click_id");
        str2 = "client_info";
        bF = new b("CLIENT_INFO", 36, "client_info");
        str2 = "company";
        bG = new b("COMPANY", 37, "company");
        str2 = "component";
        bH = new b("COMPONENT", 38, "component");
        str2 = "content_description";
        bI = new b("CONTENT_DESCRIPTION", 39, "content_description");
        str2 = "content_group";
        bJ = new b("CONTENT_GROUP", 40, "content_group");
        str2 = "conversion_id";
        bK = new b("CONVERSION_ID", 41, "conversion_id");
        str2 = "cookie_domain";
        bL = new b("COOKIE_DOMAIN", 42, "cookie_domain");
        str2 = "cookie_expiration";
        bM = new b("COOKIE_EXPIRATION", 43, "cookie_expiration");
        str2 = "cookie_name";
        bN = new b("COOKIE_NAME", 44, "cookie_name");
        str2 = "cookie_path";
        bO = new b("COOKIE_PATH", 45, "cookie_path");
        str2 = "cookie_path_copy";
        bP = new b("COOKIE_PATH_COPY", 46, "cookie_path_copy");
        str2 = "country";
        bQ = new b("COUNTRY", 47, "country");
        str2 = "currency_code";
        bR = new b("CURRENCY_CODE", 48, "currency_code");
        str2 = "custom_vars";
        bS = new b("CUSTOM_VARS", 49, "custom_vars");
        str2 = "customer_id";
        bT = new b("CUSTOMER_ID", 50, "customer_id");
        str2 = "data_layer_version";
        bU = new b("DATA_LAYER_VERSION", 51, "data_layer_version");
        str2 = "data_provider_id";
        bV = new b("DATA_PROVIDER_ID", 52, "data_provider_id");
        str2 = "debug";
        bW = new b("DEBUG", 53, "debug");
        str2 = "decorate_form";
        bX = new b("DECORATE_FORM", 54, "decorate_form");
        str2 = "decorate_forms_auto_link";
        bY = new b("DECORATE_FORMS_AUTO_LINK", 55, "decorate_forms_auto_link");
        str2 = "decorate_link";
        bZ = new b("DECORATE_LINK", 56, "decorate_link");
        str2 = "default_pages";
        ca = new b("DEFAULT_PAGES", 57, "default_pages");
        str2 = "default_value";
        cb = new b("DEFAULT_VALUE", 58, "default_value");
        str2 = "dependencies";
        cc = new b("DEPENDENCIES", 59, "dependencies");
        str2 = "detect_flash";
        cd = new b("DETECT_FLASH", 60, "detect_flash");
        str2 = "detect_title";
        ce = new b("DETECT_TITLE", 61, "detect_title");
        str2 = "dimension";
        cf = new b("DIMENSION", 62, "dimension");
        str2 = "domain_name";
        cg = new b("DOMAIN_NAME", 63, "domain_name");
        str2 = "double_click";
        ch = new b("DOUBLE_CLICK", 64, "double_click");
        str2 = "ecommerce_macro_data";
        ci = new b("ECOMMERCE_MACRO_DATA", 65, "ecommerce_macro_data");
        str2 = "ecommerce_use_data_layer";
        cj = new b("ECOMMERCE_USE_DATA_LAYER", 66, "ecommerce_use_data_layer");
        str2 = "element";
        ck = new b("ELEMENT", 67, "element");
        str3 = "EMAIL";
        str2 = Event.INTENT_EMAIL;
        cl = new b(str3, 68, Event.INTENT_EMAIL);
        str2 = "employee_range";
        cm = new b("EMPLOYEE_RANGE", 69, "employee_range");
        str2 = "enable_ecommerce";
        cn = new b("ENABLE_ECOMMERCE", 70, "enable_ecommerce");
        str2 = "escape";
        co = new b("ESCAPE", 71, "escape");
        str2 = "event_action";
        cp = new b("EVENT_ACTION", 72, "event_action");
        str2 = "event_category";
        cq = new b("EVENT_CATEGORY", 73, "event_category");
        str2 = "event_label";
        cr = new b("EVENT_LABEL", 74, "event_label");
        str2 = "event_value";
        cs = new b("EVENT_VALUE", 75, "event_value");
        str2 = "exception_description";
        ct = new b("EXCEPTION_DESCRIPTION", 76, "exception_description");
        str2 = "exception_fatal";
        cu = new b("EXCEPTION_FATAL", 77, "exception_fatal");
        str2 = "fields_to_set";
        cv = new b("FIELDS_TO_SET", 78, "fields_to_set");
        str2 = "form_object";
        cw = new b("FORM_OBJECT", 79, "form_object");
        str2 = "function";
        cx = new b("FUNCTION", 80, "function");
        str2 = "function_call_macro_name";
        cy = new b("FUNCTION_CALL_NAME", 81, "function_call_macro_name");
        str2 = "group";
        cz = new b("GROUP", 82, "group");
        str2 = "hit_callback";
        cA = new b("HIT_CALLBACK", 83, "hit_callback");
        str2 = "html";
        cB = new b("HTML", 84, "html");
        str3 = "ID";
        str2 = AnalyticsEvent.EVENT_ID;
        cC = new b(str3, 85, AnalyticsEvent.EVENT_ID);
        str2 = "ignored_organic";
        cD = new b("IGNORED_ORGANIC", 86, "ignored_organic");
        str2 = "ignored_ref";
        cE = new b("IGNORED_REF", 87, "ignored_ref");
        str2 = "ignore_case";
        cF = new b("IGNORE_CASE", 88, "ignore_case");
        str2 = "input";
        cG = new b("INPUT", 89, "input");
        str2 = "input_format";
        cH = new b("INPUT_FORMAT", 90, "input_format");
        str2 = "instance_name";
        cI = new b("INSTANCE_NAME", 91, "instance_name");
        str2 = "instance_label";
        cJ = new b("INSTANCE_LABEL", 92, "instance_label");
        str2 = "interval";
        cK = new b("INTERVAL", 93, "interval");
        str2 = "item_separator";
        cL = new b("ITEM_SEPARATOR", 94, "item_separator");
        str2 = "javascript";
        cM = new b("JAVASCRIPT", 95, "javascript");
        str2 = "keyword";
        cN = new b("KEYWORD", 96, "keyword");
        str2 = "key_value_separator";
        cO = new b("KEY_VALUE_SEPARATOR", 97, "key_value_separator");
        str3 = "LABEL";
        str2 = PlusShare.KEY_CALL_TO_ACTION_LABEL;
        cP = new b(str3, 98, PlusShare.KEY_CALL_TO_ACTION_LABEL);
        str2 = "language";
        cQ = new b("LANGUAGE", 99, "language");
        str2 = "limit";
        cR = new b("LIMIT", 100, "limit");
        str2 = "link";
        cS = new b("LINK", 101, "link");
        str2 = "link_by_post";
        cT = new b("LINK_BY_POST", 102, "link_by_post");
        str2 = "link_id";
        cU = new b("LINK_ID", 103, "link_id");
        str2 = "live_only";
        cV = new b("LIVE_ONLY", 104, "live_only");
        str2 = "local_gif_path";
        cW = new b("LOCAL_GIF_PATH", 105, "local_gif_path");
        str2 = "location";
        cX = new b("LOCATION", 106, "location");
        str2 = "map";
        cY = new b("MAP", 107, "map");
        str2 = "max";
        cZ = new b("MAX", 108, "max");
        str2 = "min";
        da = new b("MIN", 109, "min");
        str2 = "metric";
        db = new b("METRIC", 110, "metric");
        str2 = "name";
        dc = new b("NAME", 111, "name");
        str2 = "namespace_code";
        dd = new b("NAMESPACE_CODE", 112, "namespace_code");
        str2 = "namespace_id";
        de = new b("NAMESPACE_ID", 113, "namespace_id");
        str2 = "namespace_value";
        df = new b("NAMESPACE_VALUE", 114, "namespace_value");
        str2 = "noninteraction";
        dg = new b("NONINTERACTION", 115, "noninteraction");
        str2 = "not_default_macro";
        dh = new b("NOT_DEFAULT_MACRO", 116, "not_default_macro");
        str2 = "no_padding";
        di = new b("NO_PADDING", 117, "no_padding");
        str2 = "number";
        dj = new b("NUMBER", 118, "number");
        str2 = "optout";
        dk = new b("OPTOUT", 119, "optout");
        str2 = "order";
        dl = new b("ORDER", 120, "order");
        str2 = "order_id";
        dm = new b("ORDER_ID", 121, "order_id");
        str2 = "order_value";
        dn = new b("ORDER_VALUE", 122, "order_value");
        str2 = "ordinal";
        do = new b("ORDINAL", 123, "ordinal");
        str2 = "organic";
        dp = new b("ORGANIC", 124, "organic");
        str2 = "output_format";
        dq = new b("OUTPUT_FORMAT", 125, "output_format");
        str2 = "page";
        dr = new b("PAGE", 126, "page");
        str2 = "page_path";
        ds = new b("PAGE_PATH", 127, "page_path");
        str2 = "partition";
        dt = new b("PARTITION", 128, "partition");
        str2 = "pixel";
        du = new b("PIXEL", 129, "pixel");
        str2 = "platform";
        dv = new b("PLATFORM", 130, "platform");
        str2 = "prices";
        dw = new b("PRICES", 131, "prices");
        str2 = "priority";
        dx = new b("PRIORITY", 132, "priority");
        str2 = "product";
        dy = new b("PRODUCT", 133, "product");
        str2 = "product_id";
        dz = new b("PRODUCT_ID", 134, "product_id");
        str2 = "product_ids";
        dA = new b("PRODUCT_IDS", 135, "product_ids");
        str2 = "push_after_evaluate";
        dB = new b("PUSH_AFTER_EVALUATE", 136, "push_after_evaluate");
        str2 = "quantity";
        dC = new b("QUANTITY", 137, "quantity");
        str2 = "query_key";
        dD = new b("QUERY_KEY", 138, "query_key");
        str3 = "REFERRER";
        str2 = AdTrackerConstants.REFERRER;
        dE = new b(str3, 139, AdTrackerConstants.REFERRER);
        str2 = "referrer_override";
        dF = new b("REFERRER_OVERRIDE", 140, "referrer_override");
        str2 = "revenue";
        dG = new b("REVENUE", 141, "revenue");
        str2 = "sample_rate";
        dH = new b("SAMPLE_RATE", 142, "sample_rate");
        str2 = "send_hits_to_google";
        dI = new b("SEND_HITS_TO_GOOGLE", 143, "send_hits_to_google");
        str2 = "session_control";
        dJ = new b("SESSION_CONTROL", 144, "session_control");
        str2 = "session_cookie_timeout";
        dK = new b("SESSION_COOKIE_TIMEOUT", 145, "session_cookie_timeout");
        str2 = "site_speed_sample_rate";
        dL = new b("SITE_SPEED_SAMPLE_RATE", 146, "site_speed_sample_rate");
        str2 = "social_action";
        dM = new b("SOCIAL_ACTION", 147, "social_action");
        str2 = "social_action_target";
        dN = new b("SOCIAL_ACTION_TARGET", 148, "social_action_target");
        str2 = "social_network";
        dO = new b("SOCIAL_NETWORK", 149, "social_network");
        str2 = "social_use_data_layer";
        dP = new b("SOCIAL_USE_DATA_LAYER", 150, "social_use_data_layer");
        str2 = "server_side";
        dQ = new b("SERVER_SIDE", 151, "server_side");
        str2 = "standard_industrial_classification";
        dR = new b("STANDARD_INDUSTRIAL_CLASSIFICATION", 152, "standard_industrial_classification");
        str2 = "strip_www";
        dS = new b("STRIP_WWW", 153, "strip_www");
        str2 = "tag_id";
        dT = new b("TAG_ID", 154, "tag_id");
        str2 = "target_url";
        dU = new b("TARGET_URL", 155, "target_url");
        str2 = "timing_category";
        dV = new b("TIMING_CATEGORY", 156, "timing_category");
        str2 = "timing_label";
        dW = new b("TIMING_LABEL", 157, "timing_label");
        str2 = "timing_sample_rate";
        dX = new b("TIMING_SAMPLE_RATE", 158, "timing_sample_rate");
        str2 = "timing_value";
        dY = new b("TIMING_VALUE", 159, "timing_value");
        str2 = "timing_var";
        dZ = new b("TIMING_VAR", 160, "timing_var");
        str3 = "TITLE";
        str2 = BannerInfoTable.COLUMN_TITLE;
        ea = new b(str3, 161, BannerInfoTable.COLUMN_TITLE);
        str2 = "track_appview";
        eb = new b("TRACK_APPVIEW", 162, "track_appview");
        str2 = "track_event";
        ec = new b("TRACK_EVENT", 163, "track_event");
        str2 = "track_exception";
        ed = new b("TRACK_EXCEPTION", 164, "track_exception");
        str2 = "track_social";
        ee = new b("TRACK_SOCIAL", 165, "track_social");
        str2 = "track_timing";
        ef = new b("TRACK_TIMING", 166, "track_timing");
        str2 = "track_transaction";
        eg = new b("TRACK_TRANSACTION", 167, "track_transaction");
        str2 = "tracker_name";
        eh = new b("TRACKER_NAME", 168, "tracker_name");
        str2 = "transaction_datalayer_map";
        ei = new b("TRANSACTION_DATALAYER_MAP", 169, "transaction_datalayer_map");
        str2 = "transaction_id";
        ej = new b("TRANSACTION_ID", 170, "transaction_id");
        str2 = "transaction_item_datalayer_map";
        ek = new b("TRANSACTION_ITEM_DATALAYER_MAP", 171, "transaction_item_datalayer_map");
        str2 = "transaction_variable";
        el = new b("TRANSACTION_VARIABLE", 172, "transaction_variable");
        str3 = "TYPE";
        str2 = AnalyticsSQLiteHelper.EVENT_LIST_TYPE;
        em = new b(str3, 173, AnalyticsSQLiteHelper.EVENT_LIST_TYPE);
        str2 = "unrepeatable";
        en = new b("UNREPEATABLE", 174, "unrepeatable");
        str3 = MoPubBrowser.DESTINATION_URL_KEY;
        str2 = PlusShare.KEY_CALL_TO_ACTION_URL;
        eo = new b(str3, 175, PlusShare.KEY_CALL_TO_ACTION_URL);
        str2 = "use_data_layer";
        ep = new b("USE_DATA_LAYER", 176, "use_data_layer");
        str2 = "use_hash";
        eq = new b("USE_HASH", 177, "use_hash");
        str2 = "use_iframe";
        er = new b("USE_IFRAME", 178, "use_iframe");
        str2 = "use_image_tag";
        es = new b("USE_IMAGE_TAG", 179, "use_image_tag");
        str2 = "use_postscribe";
        et = new b("USE_POSTSCRIBE", 180, "use_postscribe");
        str2 = "user_id";
        eu = new b("USER_ID", 181, "user_id");
        str2 = "user_variable";
        ev = new b("USER_VARIABLE", 182, "user_variable");
        str2 = "value";
        ew = new b("VALUE", 183, "value");
        str2 = "value_in_dollars";
        ex = new b("VALUE_IN_DOLLARS", 184, "value_in_dollars");
        str2 = "visitor_cookie_timeout";
        ey = new b("VISITOR_COOKIE_TIMEOUT", 185, "visitor_cookie_timeout");
        str2 = "wait_for_tags";
        ez = new b("WAIT_FOR_TAGS", 186, "wait_for_tags");
        str2 = "wait_for_tags_timeout";
        eA = new b("WAIT_FOR_TAGS_TIMEOUT", 187, "wait_for_tags_timeout");
        str2 = "widget_ids";
        eB = new b("WIDGET_IDS", 188, "widget_ids");
        eD = new b[]{aV, aW, aX, aY, aZ, ba, bb, bc, bd, be, bf, bg, bh, bi, bj, bk, bl, bm, bn, bo, bp, bq, br, bs, bt, bu, bv, bw, bx, by, bz, bA, bB, bC, bD, bE, bF, bG, bH, bI, bJ, bK, bL, bM, bN, bO, bP, bQ, bR, bS, bT, bU, bV, bW, bX, bY, bZ, ca, cb, cc, cd, ce, cf, cg, ch, ci, cj, ck, cl, cm, cn, co, cp, cq, cr, cs, ct, cu, cv, cw, cx, cy, cz, cA, cB, cC, cD, cE, cF, cG, cH, cI, cJ, cK, cL, cM, cN, cO, cP, cQ, cR, cS, cT, cU, cV, cW, cX, cY, cZ, da, db, dc, dd, de, df, dg, dh, di, dj, dk, dl, dm, dn, do, dp, dq, dr, ds, dt, du, dv, dw, dx, dy, dz, dA, dB, dC, dD, dE, dF, dG, dH, dI, dJ, dK, dL, dM, dN, dO, dP, dQ, dR, dS, dT, dU, dV, dW, dX, dY, dZ, ea, eb, ec, ed, ee, ef, eg, eh, ei, ej, ek, el, em, en, eo, ep, eq, er, es, et, eu, ev, ew, ex, ey, ez, eA, eB};
    }

    private b(String str) {
        this.eC = str;
    }

    public String toString() {
        return this.eC;
    }
}