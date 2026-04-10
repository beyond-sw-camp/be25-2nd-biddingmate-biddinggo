package com.biddingmate.biddinggo.report.service;

import com.biddingmate.biddinggo.auction.event.AuctionCancelledEvent;
import com.biddingmate.biddinggo.auction.mapper.AuctionMapper;
import com.biddingmate.biddinggo.auction.model.AuctionStatus;
import com.biddingmate.biddinggo.bid.mapper.BidMapper;
import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import com.biddingmate.biddinggo.member.mapper.MemberMapper;
import com.biddingmate.biddinggo.member.model.MemberStatus;
import com.biddingmate.biddinggo.point.service.PointService;
import com.biddingmate.biddinggo.report.dto.ReportCreateRequest;
import com.biddingmate.biddinggo.report.mapper.ReportMapper;
import com.biddingmate.biddinggo.report.model.Report;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportMapper reportMapper;
    private final MemberMapper memberMapper;
    private final AuctionMapper auctionMapper;
    private final BidMapper bidMapper;
    private final PointService pointService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public void processReport(ReportCreateRequest request, Long reporterId) {

        //자기 자신 신고 검증
        if (request.getTargetMemberId().equals(reporterId)) {
            throw new CustomException(ErrorType.CANNOT_REPORT_SELF);
        }

        // 동시성 제어를 위한 유저 락
        memberMapper.findByIdForUpdate(request.getTargetMemberId());

        // 신고 데이터 저장
        Report report = Report.builder()
                .reporterId(reporterId)
                .targetId(request.getTargetMemberId())
                .targetType("MEMBER")
                .reason(request.getReason())
                .build();

        if (reportMapper.insertReport(report) <= 0) {
            throw new CustomException(ErrorType.REPORT_CREATE_FAIL);
        }

        // 누적 횟수 확인 (3회 이상 시 커스텀 정지 로직 실행)
        int reportCount = reportMapper.countByTargetMemberId(request.getTargetMemberId());

        if (reportCount >= 3) {
            processMemberDeactivation(request.getTargetMemberId());
        }
    }

    private void processMemberDeactivation(Long targetId) {
        log.info("신고 3회 누적 유저({}) 정지 프로세스 시작", targetId);

        // 유저 상태를 INACTIVE로 변경
        memberMapper.updateMemberStatus(targetId, MemberStatus.INACTIVE);

        // 판매자인 경우: 진행 중인 경매 취소 및 해당 경매 입찰자들 환불
        List<Long> sellingAuctionIds = auctionMapper.findActiveAuctionIdsBySeller(targetId);
        if (sellingAuctionIds != null && !sellingAuctionIds.isEmpty()) {
            // 경매 상태를 CANCELLED로 변경
            auctionMapper.updateAuctionStatus(sellingAuctionIds, AuctionStatus.CANCELLED);

            // 경매 취소 이벤트 발행 -> 이 이벤트가 입찰자들을 찾아 자동으로 환불해줍니다.
            eventPublisher.publishEvent(new AuctionCancelledEvent(sellingAuctionIds));
            log.info("판매 중인 경매 {}건 취소 및 입찰자 환불 이벤트 발행", sellingAuctionIds.size());
        }

        // 본인이 입찰 중인 금액 환불
        // 입찰 수 차감
        auctionMapper.decreaseBidCountByDeactiveMember(targetId);

        // 현재 유저의 활성 입찰을 모두 비활성화(INACTIVE) 처리
        bidMapper.invalidateBidsByMember(targetId);

        // 이 유저가 참여했던 진행 중인 경매들을 찾아 최고 입찰금 환불
        List<Long> ongoingBidAuctionIds = bidMapper.findOngoingAuctionIdsByMember(targetId);
        for (Long auctionId : ongoingBidAuctionIds) {
            Long refundAmount = bidMapper.findMaxBidAmountByAuctionAndBidder(auctionId, targetId);
            if (refundAmount != null && refundAmount > 0) {
                pointService.refundBid(targetId, refundAmount);
            }
        }

        log.info("유저({}) 정지 및 경매 취소/본인 환불 처리 완료", targetId);
    }
}