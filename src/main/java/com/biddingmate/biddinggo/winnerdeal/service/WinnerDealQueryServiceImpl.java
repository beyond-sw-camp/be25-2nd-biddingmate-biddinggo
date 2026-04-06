package com.biddingmate.biddinggo.winnerdeal.service;

import com.biddingmate.biddinggo.winnerdeal.mapper.WinnerDealMapper;
import com.biddingmate.biddinggo.winnerdeal.model.WinnerDeal;
import lombok.RequiredArgsConstructor;
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
}
