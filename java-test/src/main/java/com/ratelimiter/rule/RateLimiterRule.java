package com.ratelimiter.rule;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RateLimiterRule {

    private Map<String, ApiLimit> mapLimit = new HashMap<>();

    public RateLimiterRule(RuleConfig ruleConfig) {
        List<AppRuleConfig> appRuleConfigs = ruleConfig.getConfig();
        for (AppRuleConfig appRuleConfig : appRuleConfigs) {
            List<ApiLimit> limits = appRuleConfig.getLimits();
            parse(appRuleConfig.getAppId(), limits);
        }
    }

    private void parse(String appId, List<ApiLimit> apiLimits) {
        for (ApiLimit apiLimit : apiLimits) {
            mapLimit.put(appId + ":" + apiLimit.getApi(), apiLimit);
        }
    }

    public ApiLimit getLimit(String appid, String url) {
        return mapLimit.get(appid + ":" + url);
    }
}
