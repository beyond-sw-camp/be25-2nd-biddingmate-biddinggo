package com.biddingmate.biddinggo.auction.prediction.service;

import com.biddingmate.biddinggo.auction.prediction.model.AuctionQueryEmbeddingSyncCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 3단계에서 사용하는 임시 구현체.
 * 실제 Supabase 저장 대신, 어떤 payload가 준비되는지만 로그로 남겨 연결 지점을 검증한다.
 */
@Slf4j
@Service
public class LoggingAuctionQueryEmbeddingSyncService implements AuctionQueryEmbeddingSyncService {
    @Override
    /**
     * 후속 단계에서 외부 저장소 호출로 대체될 자리다.
     */
    public void sync(AuctionQueryEmbeddingSyncCommand command) {
        log.info(
                "Prepared auction query embedding sync request. trigger={}, auctionId={}, itemId={}, categoryId={}, conditionScore={}, textLength={}",
                command.getTrigger(),
                command.getAuctionId(),
                command.getItemId(),
                command.getCategoryId(),
                command.getConditionScore(),
                command.getEmbeddingText() != null ? command.getEmbeddingText().length() : 0
        );
    }
}
