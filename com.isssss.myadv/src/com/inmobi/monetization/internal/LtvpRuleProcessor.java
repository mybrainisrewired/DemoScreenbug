package com.inmobi.monetization.internal;

import com.inmobi.commons.analytics.bootstrapper.AnalyticsInitializer;
import com.inmobi.commons.analytics.iat.impl.AdTrackerConstants;
import com.inmobi.commons.internal.InternalSDKUtil;
import com.inmobi.commons.internal.Log;
import com.inmobi.commons.network.Request;
import com.inmobi.commons.network.Request.Format;
import com.inmobi.commons.network.Request.Method;
import com.inmobi.commons.network.Response;
import com.inmobi.commons.network.ServiceProvider;
import com.inmobi.commons.network.abstraction.INetworkListener;
import com.inmobi.monetization.internal.objects.LtvpRuleCache;
import com.millennialmedia.android.MMAdView;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONObject;

public class LtvpRuleProcessor implements INetworkListener {
    private ServiceProvider a;

    public enum ActionsRule {
        MEDIATION(0),
        NO_ADS(1),
        ACTIONS_TO_MEDIATION(2),
        ACTIONS_ONLY(3);
        int a;

        static {
            MEDIATION = new com.inmobi.monetization.internal.LtvpRuleProcessor.ActionsRule("MEDIATION", 0, 0);
            NO_ADS = new com.inmobi.monetization.internal.LtvpRuleProcessor.ActionsRule("NO_ADS", 1, 1);
            ACTIONS_TO_MEDIATION = new com.inmobi.monetization.internal.LtvpRuleProcessor.ActionsRule("ACTIONS_TO_MEDIATION", 2, 2);
            ACTIONS_ONLY = new com.inmobi.monetization.internal.LtvpRuleProcessor.ActionsRule("ACTIONS_ONLY", 3, 3);
            b = new com.inmobi.monetization.internal.LtvpRuleProcessor.ActionsRule[]{MEDIATION, NO_ADS, ACTIONS_TO_MEDIATION, ACTIONS_ONLY};
        }

        private ActionsRule(int i) {
            this.a = i;
        }

        static com.inmobi.monetization.internal.LtvpRuleProcessor.ActionsRule a(int i) {
            switch (i) {
                case MMAdView.TRANSITION_FADE:
                    return NO_ADS;
                case MMAdView.TRANSITION_UP:
                    return ACTIONS_TO_MEDIATION;
                case MMAdView.TRANSITION_DOWN:
                    return ACTIONS_ONLY;
                default:
                    return MEDIATION;
            }
        }

        public int getValue() {
            return this.a;
        }
    }

    private LtvpRuleProcessor() {
        this.a = ServiceProvider.getInstance();
    }

    public static LtvpRuleProcessor getInstance() {
        return new LtvpRuleProcessor();
    }

    public void dispatchLtvpRule() {
        Log.internal(Constants.LOG_TAG, "Fetching LTVP Rule");
        Request request = new Request(AnalyticsInitializer.getConfigParams().getEndPoints().getRulesUrl(), Format.KEY_VAL, Method.GET);
        request.fillAppInfo();
        request.fillDeviceInfo();
        this.a.executeTask(request, this);
    }

    public ActionsRule getLtvpRuleConfig(long j) {
        ActionsRule.MEDIATION.getValue();
        LtvpRuleCache instance = LtvpRuleCache.getInstance(InternalSDKUtil.getContext());
        if (instance.getHardExpiryForLtvpRule() <= System.currentTimeMillis() || instance.getHardExpiryForLtvpRule() == 0) {
            Log.internal(Constants.LOG_TAG, "Hard Expiry or 1st rule fetch. Default mediation. Fetching Rule");
            dispatchLtvpRule();
            instance.clearLtvpRuleCache();
            return ActionsRule.MEDIATION;
        } else {
            int ltvpRule;
            if (instance.getSoftExpiryForLtvpRule() <= System.currentTimeMillis()) {
                Log.internal(Constants.LOG_TAG, "Soft Expiry. Default mediation. Fetching Rule");
                dispatchLtvpRule();
                ltvpRule = instance.getLtvpRule(j);
            } else {
                Log.internal(Constants.LOG_TAG, "Valid rule");
                ltvpRule = instance.getLtvpRule(j);
            }
            return ActionsRule.a(ltvpRule);
        }
    }

    public void onRequestFailed(Request request, Response response) {
        try {
            Log.internal(Constants.LOG_TAG, "Ltvp Rule error" + response.getError().toString());
        } catch (Exception e) {
            Log.internal(Constants.LOG_TAG, "Ltvp Rule exception", e);
        }
    }

    public void onRequestSucceded(Request request, Response response) {
        try {
            if (response.getStatusCode() == 200) {
                JSONObject jSONObject = new JSONObject(response.getResponseBody());
                if (jSONObject.getBoolean("success")) {
                    jSONObject = jSONObject.getJSONObject("response");
                    Log.debug(Constants.LOG_TAG, "Received LTVP rule fetch success: " + jSONObject.toString());
                    LtvpRulesObject ltvpRulesObject = new LtvpRulesObject();
                    String string = jSONObject.getString("rule_id");
                    long j = jSONObject.getLong(AdTrackerConstants.TIMESTAMP);
                    JSONObject jSONObject2 = jSONObject.getJSONObject("exp");
                    long j2 = jSONObject2.getLong("se");
                    long j3 = jSONObject2.getLong("he");
                    HashMap hashMap = new HashMap();
                    JSONObject jSONObject3 = jSONObject.getJSONObject("rules");
                    Iterator keys = jSONObject3.keys();
                    while (keys.hasNext()) {
                        String str = (String) keys.next();
                        hashMap.put(str, Integer.valueOf(jSONObject3.getInt(String.valueOf(str))));
                    }
                    ltvpRulesObject.setRuleId(string);
                    ltvpRulesObject.setTimeStamp(j);
                    ltvpRulesObject.setSoftExpiry(j2);
                    ltvpRulesObject.setHardExpiry(j3);
                    ltvpRulesObject.setRules(hashMap);
                    Log.internal(Constants.LOG_TAG, "Ltvp Rule received" + ltvpRulesObject.getRules().toString());
                    LtvpRuleCache.getInstance(InternalSDKUtil.getContext()).setLtvpRuleConfig(ltvpRulesObject);
                } else {
                    int i = jSONObject.getInt("error_code");
                    Log.internal(Constants.LOG_TAG, "Received LTVP rule fetch failure: " + i + " : " + jSONObject.getString("error_message"));
                }
            }
        } catch (Exception e) {
            Log.internal(Constants.LOG_TAG, "Exception getting ltvp rule", e);
        }
    }
}