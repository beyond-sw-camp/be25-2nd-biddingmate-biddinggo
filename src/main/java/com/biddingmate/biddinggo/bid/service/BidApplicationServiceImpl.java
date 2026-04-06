package com.biddingmate.biddinggo.bid.service;

import com.biddingmate.biddinggo.auction.mapper.AuctionMapper;
import com.biddingmate.biddinggo.auction.model.Auction;
import com.biddingmate.biddinggo.auction.model.AuctionStatus;
import com.biddingmate.biddinggo.bid.dto.CreateBidRequest;
import com.biddingmate.biddinggo.bid.dto.CreateBidResponse;
import com.biddingmate.biddinggo.bid.model.Bid;
import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import com.biddingmate.biddinggo.member.mapper.MemberMapper;
import com.biddingmate.biddinggo.member.service.MemberService;
import com.biddingmate.biddinggo.point.model.PointHistory;
import com.biddingmate.biddinggo.point.model.PointHistoryType;
import com.biddingmate.biddinggo.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/*
    입찰 전체 흐름을 조율하는 애플리케이션 서비스.
    트랜잭션 경계는 이 클래스에서만 관리한다.
 */
@Service
@RequiredArgsConstructor
public class BidApplicationServiceImpl implements BidApplicationService {
    private final AuctionMapper auctionMapper;
    private final MemberMapper memberMapper;
    private final MemberService memberService;
    private final PointService pointService;
    private final BidService bidService;

    @Override
    @Transactional
    public CreateBidResponse createBidProcess(Long memberId, CreateBidRequest request){
        Long auctionId = request.getAuctionId();

        // 1. 경매 유효성 검증
        Auction auction = auctionMapper.findByIdForUpdate(auctionId);

        if(auction == null) {
            throw new CustomException(ErrorType.AUCTION_NOT_FOUND);
        }

        if(auction.getSellerId().equals(memberId)){
            throw new CustomException(ErrorType.CANNOT_BID_ON_OWN_AUCTION);
        }

        LocalDateTime now = LocalDateTime.now();

        if (auction.getStatus() != AuctionStatus.ON_GOING ||
                now.isBefore(auction.getStartDate()) ||
                now.isAfter(auction.getEndDate())) {
            throw new CustomException(ErrorType.AUCTION_NOT_BIDDABLE);
        }

        Long lastBidAmount = bidService.getLastBidAmount(memberId, auctionId);
        Long additionalBidAmount = request.getAmount() - lastBidAmount;
        Long bidderPoint = memberMapper.getPointById(memberId);
        if(bidderPoint == null || additionalBidAmount > bidderPoint){
            throw new CustomException(ErrorType.NOT_ENOUGH_POINT);
        }


        // 2. Bid 등록
        Bid bid = bidService.createBid(memberId, auction, request);


        // 3. 입찰자 포인트 차감
        // 추후 MemberService 구현 이후 변경
        memberService.deductPoint(memberId, additionalBidAmount);

        // 4. Auction 정보 갱신 : 경매 차순위 값 변경 + 입찰 수 증가
        // 추후 AuctionService 구현 이후 변경
        Bid vickreyBid = bidService.getVickreyBid(auctionId);
        auctionMapper.updateAfterBid(
                auctionId,
                vickreyBid != null ? vickreyBid.getAmount() : null
        );

        // 5. Point History 저장
        // 추후 PointHistoryService 구현 이후 변경
        PointHistory pointHistory = PointHistory.builder()
                .memberId(memberId)
                .bidId(bid.getId())
                .type(PointHistoryType.BID)
                .amount(additionalBidAmount)
                .createdAt(LocalDateTime.now())
                .build();
        int pointInsert = pointService.addPointHistory(pointHistory);

        if (pointInsert != 1) {
            throw new CustomException(ErrorType.POINT_HISTORY_SAVE_FAILED);
        }

        return CreateBidResponse.builder()
                .bidId(bid.getId())
                .build();
    }
}
