package com.ratelimiter.rule;

import java.util.List;

public class RuleConfig {
    private List<AppRuleConfig> config;

    public List<AppRuleConfig> getConfig() {
        return config;
    }

    public void setConfig(List<AppRuleConfig> config) {
        this.config = config;
    }
}
