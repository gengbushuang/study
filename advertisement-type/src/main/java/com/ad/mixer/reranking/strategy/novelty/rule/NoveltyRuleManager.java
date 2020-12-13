package com.ad.mixer.reranking.strategy.novelty.rule;

import com.ad.model.rule.NoveltyRule;
import com.ad.model.rule.NoveltySignleRule;
import com.ad.model.rule.Rule;
import com.ad.model.rule.TimeType;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

public class NoveltyRuleManager {

    private Map<Long, NoveltySignleRule> noveltyRuleMap = new ConcurrentSkipListMap<>();

    public boolean isLimitByNoveltyRules(Long postId, long timestamp, long now) {
        NoveltySignleRule signleRule = noveltyRuleMap.get(postId);
        if (signleRule != null) {
            if (this.calcLimit(signleRule.getRules(), timestamp, now)) {
                return true;
            }
        }
        return false;
    }

    public boolean calcLimit(List<Rule> rules, long timestamp, long now) {
        for (Rule rule : rules) {
            if (rule.getTimeType() == TimeType.DAY) {
                if (rule.getTimeCount() * 86400000 + timestamp > now) {
                    return true;
                }
            }
        }
        return false;
    }
}
