package com.biddingmate.biddinggo.point.service;

import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import com.biddingmate.biddinggo.common.request.BasePageRequest;
import com.biddingmate.biddinggo.common.response.PageResponse;
import com.biddingmate.biddinggo.directinquiry.dto.DirectInquiryView;
import com.biddingmate.biddinggo.member.service.MemberService;
import com.biddingmate.biddinggo.point.dto.MyPointResponse;
import com.biddingmate.biddinggo.point.dto.PointHistoryDto;
import com.biddingmate.biddinggo.point.mapper.PointHistoryMapper;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {
    private final MemberService memberService;
    private final PointHistoryMapper pointHistoryMapper;

    @Override
    @Transactional(readOnly = true)
    public MyPointResponse findMyPointList(BasePageRequest request, Long memberId) {
        long currentPoint = memberService.getCurrentPoint(memberId);

        RowBounds rowBounds = new RowBounds(request.getOffset(), request.getSize());
        String order = request.getOrder();

        if (!"ASC".equalsIgnoreCase(order) && !"DESC".equalsIgnoreCase(order)) {
            throw new CustomException(ErrorType.INVALID_SORT_ORDER);
        }
        String sortOrder = order.toUpperCase();

        List<PointHistoryDto> list;
        int count;

        list = pointHistoryMapper.findByMemberId(rowBounds, sortOrder, memberId);
        count = pointHistoryMapper.countByMemberId(memberId);

        PageResponse<PointHistoryDto> pointhistory = PageResponse.of(list, request.getPage(), request.getSize(), count);

        return MyPointResponse.builder()
                .currentPoint(currentPoint)
                .histroies(pointhistory)
                .build();
    }
}
