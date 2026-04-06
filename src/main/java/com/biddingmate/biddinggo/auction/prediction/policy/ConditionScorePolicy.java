package com.biddingmate.biddinggo.auction.prediction.policy;

public interface ConditionScorePolicy {
    Double resolve(String quality);

    default boolean supports(String quality) {
        return resolve(quality) != null;
    }
}
