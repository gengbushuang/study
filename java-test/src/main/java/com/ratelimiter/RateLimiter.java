package com.ratelimiter;

import com.ratelimiter.rule.ApiLimit;
import com.ratelimiter.rule.RateLimiterRule;
import com.ratelimiter.rule.RuleConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;

public class RateLimiter {
    private final Logger log = LogManager.getLogger(this.getClass());

    private RateLimiterRule rule;

    public RateLimiter() {
        try (InputStream inputStream = this.getClass().getResourceAsStream("/ratelimiter/ratelimiter.yml")) {
            if (inputStream != null) {
                Yaml yaml = new Yaml();
                RuleConfig ruleConfig = yaml.loadAs(inputStream, RuleConfig.class);
                this.rule = new RateLimiterRule(ruleConfig);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean limit(String appid, String url) {
        ApiLimit apiLimit = rule.getLimit(appid, url);
        if (apiLimit == null) {
            return true;
        }



        return true;

    }

    public static void main(String[] args) {
        RateLimiter rateLimiter = new RateLimiter();
    }
}
