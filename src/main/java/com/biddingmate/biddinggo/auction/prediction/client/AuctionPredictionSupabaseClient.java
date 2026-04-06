package com.biddingmate.biddinggo.auction.prediction.client;

import com.biddingmate.biddinggo.auction.prediction.model.AuctionPriceReference;
import com.biddingmate.biddinggo.auction.prediction.model.AuctionQueryEmbedding;

/**
 * Supabase 벡터 저장소에 query embedding / price reference를 적재하는 연동 경계.
 */
public interface AuctionPredictionSupabaseClient {
    /**
     * 현재 환경에서 Supabase 연동이 가능한지 확인한다.
     */
    boolean isEnabled();

    /**
     * 경매 조회 대상 임베딩을 Supabase에 upsert 한다.
     */
    void upsertAuctionQueryEmbedding(AuctionQueryEmbedding auctionQueryEmbedding);

    /**
     * 과거 낙찰 reference를 Supabase에 upsert 한다.
     */
    void upsertAuctionPriceReference(AuctionPriceReference auctionPriceReference);
}
