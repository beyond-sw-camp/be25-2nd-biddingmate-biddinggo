package com.biddingmate.biddinggo.bid.service;

import com.biddingmate.biddinggo.auction.mapper.AuctionMapper;
import com.biddingmate.biddinggo.auction.model.Auction;
import com.biddingmate.biddinggo.auction.model.AuctionStatus;
import com.biddingmate.biddinggo.auction.service.AuctionService;
import com.biddingmate.biddinggo.bid.dto.CreateBidRequest;
import com.biddingmate.biddinggo.bid.dto.CreateBidResponse;
import com.biddingmate.biddinggo.bid.mapper.BidMapper;
import com.biddingmate.biddinggo.bid.model.Bid;
import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import com.biddingmate.biddinggo.member.mapper.MemberMapper;
import com.biddingmate.biddinggo.point.mapper.PointHistoryMapper;
import com.biddingmate.biddinggo.point.model.PointHistory;
import com.biddingmate.biddinggo.point.model.PointHistoryType;
import jakarta.validation.Valid;
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
    private final BidMapper bidMapper;
    private final AuctionMapper auctionMapper;
    private final MemberMapper memberMapper;
    private final PointHistoryMapper pointHistoryMapper;

    private final BidService bidService;
    private final AuctionService auctionService;

    @Override
    @Transactional
    public CreateBidResponse createBid(Long memberId, Long auctionId, @Valid CreateBidRequest request){
        Long result = 0L;

        // 0. Request (DTO) 기본 검증
        if(memberId == null || auctionId == null || request.getAmount() == null){
            // 필수 입력값 누락
            // INVALID_BID_CREATE_REQUEST
        }

        // 1. 값 검증
        Auction auction = auctionMapper.findById(auctionId);
        Long bidderPoint = memberMapper.getPointByMemberId(memberId);

        if(auction == null || !(auction.getStatus().equals(AuctionStatus.ON_GOING))){
            // auction_id가 존재하지 않는 경우, 경매가 진행중이 아닌 경우
        }

        if(bidderPoint == null){
            // member_id가 존재하지 않는 경우 -> 있을 수 없다.
        }

        Bid vickreyBid = bidMapper.getVickreyBid(auctionId);
        if(vickreyBid.getAmount() + auction.getBidUnit() > request.getAmount()){
            // 입찰 금액이 현재 차순위 입찰가 + 입찰단위 미만인 경우
        }

        if((request.getAmount() - auction.getStartPrice()) % auction.getBidUnit() != 0){
            // 입찰 단위가 아닌 값이 입찰가로 들어온 경우
        }

        //최종 입찰가 계산 = 입찰 기록에서 입찰가 - 경매에서 사용자가 입찰한 총 금액
        Long lastBidAmount = bidService.getLastBidAmount(auctionId, memberId);
        if(lastBidAmount >= request.getAmount()){
            //이미 입찰한 금액보다 적거나 같은 경우
        }

        Long additionalBidAmount = request.getAmount() - lastBidAmount;
        if(additionalBidAmount > bidderPoint){
            // 포인트가 입찰 금액보다 부족한 경우
        }


        // 2. Bid 등록
        Bid bid = bidService.createBid(memberId, auctionId, request);

        // 3. 입찰자 포인트 차감
        // 추후 MemberService 구현 이후 변경
        memberMapper.usePoint(memberId, additionalBidAmount);

        // 4. Auction 정보 갱신 : 경매 차순위 값 변경 + 입찰 수 증가
        // 추후 AuctionService 구현 이후 변경
        vickreyBid = bidMapper.getVickreyBid(auctionId);
        Long vickreyPrice = (vickreyBid == null) ? auction.getStartPrice() : vickreyBid.getAmount();

        auctionMapper.updateAfterBid(auctionId, vickreyPrice);
        // auction 정보 갱신 실패 예외 처리

        // 5. Point History 저장
        // 추후 PointHistoryService 구현 이후 변경
        PointHistory pointHistory = PointHistory.builder()
                .memberId(memberId)
                .bidId(bid.getId())
                .type(PointHistoryType.BID)
                .amount(additionalBidAmount)
                .createdAt(LocalDateTime.now())
                .build();
        int pointInsert = pointHistoryMapper.insert(pointHistory);

        if (pointInsert != 1) {
            throw new CustomException(ErrorType.POINT_HISTORY_SAVE_FAILED);
        }

        return CreateBidResponse.builder()
                .bidId(bid.getId())
                .build();
    }
}
