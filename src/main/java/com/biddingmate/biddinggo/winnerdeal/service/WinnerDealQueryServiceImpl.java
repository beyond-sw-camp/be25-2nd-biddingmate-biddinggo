package com.biddingmate.biddinggo.winnerdeal.service;

import com.biddingmate.biddinggo.common.response.PageResponse;
import com.biddingmate.biddinggo.winnerdeal.dto.WinnerDealHistoryRequest;
import com.biddingmate.biddinggo.winnerdeal.dto.WinnerDealHistoryResponse;
import com.biddingmate.biddinggo.winnerdeal.mapper.WinnerDealMapper;
import com.biddingmate.biddinggo.winnerdeal.model.WinnerDeal;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WinnerDealQueryServiceImpl implements WinnerDealQueryService {
    private final WinnerDealMapper winnerDealMapper;

    @Override
    @Transactional(readOnly = true)
    public List<WinnerDeal> findByMemberId(Long memberId) {
        return winnerDealMapper.findByMemberId(memberId);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<WinnerDealHistoryResponse> findPurchaseHistory(WinnerDealHistoryRequest request, Long memberId) {
        RowBounds rowBounds = new RowBounds(request.getOffset(), request.getSize());

        List<WinnerDealHistoryResponse> content =
                winnerDealMapper.findPurchaseHistory(rowBounds, request, memberId);

        long totalElements =
                winnerDealMapper.countPurchaseHistory(request, memberId);

        return PageResponse.of(content, request.getPage(), request.getSize(), totalElements);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<WinnerDealHistoryResponse> findSaleHistory(WinnerDealHistoryRequest request, Long memberId) {
        RowBounds rowBounds = new RowBounds(request.getOffset(), request.getSize());

        List<WinnerDealHistoryResponse> content =
                winnerDealMapper.findSaleHistory(rowBounds, request, memberId);

        long totalElements =
                winnerDealMapper.countSaleHistory(request, memberId);

        return PageResponse.of(content, request.getPage(), request.getSize(), totalElements);
    }
}
