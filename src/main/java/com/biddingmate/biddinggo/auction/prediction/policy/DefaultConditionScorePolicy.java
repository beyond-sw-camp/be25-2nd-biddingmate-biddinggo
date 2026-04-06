package com.biddingmate.biddinggo.auction.prediction.policy;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DefaultConditionScorePolicy implements ConditionScorePolicy {
    private static final Map<String, Double> CONDITION_SCORES = Map.ofEntries(
            Map.entry("최상", 1.0d),
            Map.entry("상", 0.7d),
            Map.entry("중", 0.5d),
            Map.entry("하", 0.2d)
    );

    @Override
    public Double resolve(String quality) {
        if (quality == null || quality.isBlank()) {
            return null;
        }

        return CONDITION_SCORES.get(normalize(quality));
    }

    private String normalize(String quality) {
        return quality.trim().replaceAll("\\s+", "");
    }
}
