package com.biddingmate.biddinggo.bid.mapper;

import com.biddingmate.biddinggo.auction.dto.AuctionDetailResponse;
import com.biddingmate.biddinggo.auction.dto.RefundDto;
import com.biddingmate.biddinggo.bid.dto.BidResponse;
import com.biddingmate.biddinggo.bid.model.Bid;
import com.biddingmate.biddinggo.common.inif.IMybatisCRUD;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

@Mapper
public interface BidMapper extends IMybatisCRUD<Bid> {

    Bid getVickreyBid(@Param("auctionId") Long auctionId);

    Long getLastBidAmountByMemberId(@Param("auctionId") Long auctionId,
                                    @Param("bidderId") Long bidderId);

    int getBidCount(Map<String, Object> params);

    List<BidResponse> getBidsByAuctionId(RowBounds rowBounds,
                                         @Param("auctionId") Long auctionId,
                                         @Param("order") String sortOrder);

    List<AuctionDetailResponse> findBidAuctionsByMemberId(RowBounds rowBounds,
                                                          @Param("memberId") Long memberId,
                                                          @Param("order")String sortOrder);

    // 특정 회원이 참여한 진행 중 경매 목록 조회
    List<Long> findOngoingAuctionIdsByMember(@Param("memberId") Long memberId);
    // 현재 최고 입찰자 조회 (전체 기준)
    Long findTopBidderId(@Param("auctionId") Long auctionId);
    // ACTIVE 회원 기준 상위 2개 입찰 조회 (비크리 핵심)
    List<Bid> findTop2ActiveBids(@Param("auctionId") Long auctionId);

    // 경매 id 목록으로 입찰내역 조회
    List<RefundDto> findRefundTargets(@Param("auctionIds") List<Long> auctionIds);
}
