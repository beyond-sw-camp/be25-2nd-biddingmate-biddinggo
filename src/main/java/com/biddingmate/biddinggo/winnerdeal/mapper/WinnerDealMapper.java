package com.biddingmate.biddinggo.winnerdeal.mapper;

import com.biddingmate.biddinggo.winnerdeal.dto.WinnerDealHistoryRequest;
import com.biddingmate.biddinggo.winnerdeal.dto.WinnerDealHistoryResponse;
import com.biddingmate.biddinggo.winnerdeal.model.WinnerDeal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

@Mapper
public interface WinnerDealMapper {
    int insert(WinnerDeal winnerDeal);
    WinnerDeal findByAuctionId(Long auctionId);

    List<WinnerDeal> findByMemberId(@Param("memberId") Long memberId);

    int updateStatus(@Param("id") Long id,
                     @Param("status") String status);

    // 구매내역 조회
    List<WinnerDealHistoryResponse> findPurchaseHistory(RowBounds rowBounds,
                                                        @Param("request") WinnerDealHistoryRequest request,
                                                        @Param("memberId") Long memberId);
    long countPurchaseHistory(@Param("request") WinnerDealHistoryRequest request,
                              @Param("memberId") Long memberId);

    // 판매내역 조회
    List<WinnerDealHistoryResponse> findSaleHistory(RowBounds rowBounds,
                                                    @Param("request") WinnerDealHistoryRequest request,
                                                    @Param("memberId") Long memberId);
    long countSaleHistory(@Param("request") WinnerDealHistoryRequest request,
                          @Param("memberId") Long memberId);
}
