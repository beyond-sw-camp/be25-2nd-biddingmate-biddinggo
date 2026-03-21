package com.biddingmate.biddinggo.admininquiry.service;

import com.biddingmate.biddinggo.admininquiry.dto.AdminInquiryView;
import com.biddingmate.biddinggo.admininquiry.dto.CreateAdminInquiryRequest;
import com.biddingmate.biddinggo.admininquiry.dto.CreateAdminInquiryResponse;
import com.biddingmate.biddinggo.admininquiry.mapper.AdminInquiryMapper;
import com.biddingmate.biddinggo.admininquiry.model.AdminInquiry;
import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import com.biddingmate.biddinggo.common.request.BasePageRequest;
import com.biddingmate.biddinggo.common.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminInquiryServiceImpl implements AdminInquiryService {
    private final AdminInquiryMapper adminInquiryMapper;

    @Override
    @Transactional
    public CreateAdminInquiryResponse createAdminInquiry(CreateAdminInquiryRequest request) {
        AdminInquiry adminInquiry = AdminInquiry.builder()
                .writerId(1L)
                .category(request.getCategory())
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .build();

        int insert = adminInquiryMapper.insert(adminInquiry);

        if (insert <= 0) {
            throw new CustomException(ErrorType.ADMIN_INQUIRY_CREATED_FAIL);
        }

        return CreateAdminInquiryResponse.builder()
                .id(adminInquiry.getId())
                .writerId(adminInquiry.getWriterId())
                .category(adminInquiry.getCategory())
                .content(adminInquiry.getContent())
                .createdAt(adminInquiry.getCreatedAt())
                .build();
    }

    @Override
    @Transactional
    public PageResponse<AdminInquiryView> findAdminInquiry(BasePageRequest request, boolean isAdmin, Long memberId) {
        RowBounds rowBounds = new RowBounds(request.getOffset(), request.getSize());
        String order = request.getOrder();

        if (!"ASC".equalsIgnoreCase(order) && !"DESC".equalsIgnoreCase(order)) {
            throw new CustomException(ErrorType.INVALID_SORT_ORDER);
        }
        String sortOrder = order.toUpperCase();

        List<AdminInquiryView> list;
        int count;

        if (isAdmin) {
            list = adminInquiryMapper.findAdminInquiry(rowBounds, sortOrder);
            count = adminInquiryMapper.getAdminInquiryTotal();
        } else {
            list = adminInquiryMapper.findAdminInquiryOfMe(rowBounds, memberId, sortOrder);
            count = adminInquiryMapper.getAdminInquiryTotalOfMe(memberId);
        }

        return PageResponse.of(list, request.getPage(), request.getSize(), count);
    }
}
