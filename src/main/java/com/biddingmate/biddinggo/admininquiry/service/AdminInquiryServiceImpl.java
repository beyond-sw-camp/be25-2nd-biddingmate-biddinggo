package com.biddingmate.biddinggo.admininquiry.service;

import com.biddingmate.biddinggo.admininquiry.dto.AdminInquiryView;
import com.biddingmate.biddinggo.admininquiry.dto.AdminInquiryViewDetail;
import com.biddingmate.biddinggo.admininquiry.dto.AnswerAdminInquiryRequest;
import com.biddingmate.biddinggo.admininquiry.dto.AnswerAdminInquiryResponse;
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

    @Override
    @Transactional(readOnly = true)
    public AdminInquiryViewDetail findAdminInquiryDetail(Long inquiryId, boolean isAdmin, Long memberId) {

        AdminInquiryViewDetail adminInquiryViewDetail;

        if (isAdmin) {
            adminInquiryViewDetail = adminInquiryMapper.findAdminInquiryDetail(inquiryId);
        } else {
            adminInquiryViewDetail = adminInquiryMapper.findAdminInquiryDetailOfMe(inquiryId, memberId);
        }

        if (adminInquiryViewDetail == null) {
            throw new CustomException(ErrorType.ADMIN_INQUIRY_NOT_FOUND);
        }

        return adminInquiryViewDetail;
    }
    @Transactional
    public AnswerAdminInquiryResponse answerAdminInquiry(Long inquiryId, AnswerAdminInquiryRequest request, Long adminId) {
        AdminInquiry adminInquiry = adminInquiryMapper.findById(inquiryId);

        if (adminInquiry == null) {
            throw new CustomException(ErrorType.ADMIN_INQUIRY_NOT_FOUND);
        }

        if (adminInquiry.getAnsweredAt() != null) {
            throw new CustomException(ErrorType.ADMIN_INQUIRY_ALREADY_ANSWERED);
        }

        LocalDateTime now = LocalDateTime.now();
        AdminInquiry updateDto = AdminInquiry.builder()
                .id(inquiryId)
                .adminId(adminId)
                .answer(request.getAnswer())
                .answeredAt(now)
                .build();

        int updatedRows = adminInquiryMapper.update(updateDto);

        if (updatedRows <= 0) {
            throw new CustomException(ErrorType.ADMIN_INQUIRY_UPDATED_FAIL);
        }

        return AnswerAdminInquiryResponse.builder()
                .id(inquiryId)
                .adminId(adminId)
                .answer(request.getAnswer())
                .answeredAt(now)
                .build();
    }
}
