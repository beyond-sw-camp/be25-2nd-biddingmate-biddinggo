package com.biddingmate.biddinggo.auction.prediction.model;

public enum AuctionPricePredictionReasonCode {
    PREDICTION_AVAILABLE,
    NOT_ENOUGH_REFERENCES,
    EMBEDDING_NOT_FOUND,
    CATEGORY_NOT_FOUND,
    INVALID_CONDITION,
    SUPABASE_TIMEOUT,
    SUPABASE_UNAVAILABLE,
    OUTLIER_FILTERED_ALL,
    FALLBACK_APPLIED
}
