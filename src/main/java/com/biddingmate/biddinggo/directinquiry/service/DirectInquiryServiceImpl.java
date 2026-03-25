package com.biddingmate.biddinggo.directinquiry.service;

import com.biddingmate.biddinggo.directinquiry.dto.DirectInquiryView;
import com.biddingmate.biddinggo.directinquiry.dto.DirectInquiryViewDetail;
import com.biddingmate.biddinggo.directinquiry.dto.AnswerDirectInquiryRequest;
import com.biddingmate.biddinggo.directinquiry.dto.AnswerDirectInquiryResponse;
import com.biddingmate.biddinggo.directinquiry.dto.CreateDirectInquiryRequest;
import com.biddingmate.biddinggo.directinquiry.dto.CreateDirectInquiryResponse;
import com.biddingmate.biddinggo.directinquiry.mapper.DirectInquiryMapper;
import com.biddingmate.biddinggo.directinquiry.model.DirectInquiry;
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
public class DirectInquiryServiceImpl implements DirectInquiryService {
    private final DirectInquiryMapper directInquiryMapper;

    @Override
    @Transactional
    public CreateDirectInquiryResponse createDirectInquiry(CreateDirectInquiryRequest request) {
        DirectInquiry directInquiry = DirectInquiry.builder()
                .writerId(1L)
                .category(request.getCategory())
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .build();

        int insert = directInquiryMapper.insert(directInquiry);

        if (insert <= 0) {
            throw new CustomException(ErrorType.ADMIN_INQUIRY_CREATED_FAIL);
        }

        return CreateDirectInquiryResponse.builder()
                .id(directInquiry.getId())
                .writerId(directInquiry.getWriterId())
                .category(directInquiry.getCategory())
                .content(directInquiry.getContent())
                .createdAt(directInquiry.getCreatedAt())
                .build();
    }

    @Override
    @Transactional
    public PageResponse<DirectInquiryView> findDirectInquiry(BasePageRequest request, boolean isAdmin, Long memberId) {
        RowBounds rowBounds = new RowBounds(request.getOffset(), request.getSize());
        String order = request.getOrder();

        if (!"ASC".equalsIgnoreCase(order) && !"DESC".equalsIgnoreCase(order)) {
            throw new CustomException(ErrorType.INVALID_SORT_ORDER);
        }
        String sortOrder = order.toUpperCase();

        List<DirectInquiryView> list;
        int count;

        if (isAdmin) {
            list = directInquiryMapper.findDirectInquiry(rowBounds, sortOrder);
            count = directInquiryMapper.getDirectInquiryTotal();
        } else {
          
            list = directInquiryMapper.findDirectInquiryOfMe(rowBounds, memberId, sortOrder);
            count = directInquiryMapper.getDirectInquiryTotalOfMe(memberId);
        }

        return PageResponse.of(list, request.getPage(), request.getSize(), count);
    }

    @Override
    @Transactional(readOnly = true)
    public DirectInquiryViewDetail findDirectInquiryDetail(Long inquiryId, boolean isAdmin, Long memberId) {
        DirectInquiryViewDetail directInquiryViewDetail;

        if (isAdmin) {
            directInquiryViewDetail = directInquiryMapper.findDirectInquiryDetail(inquiryId);
        } else {
            directInquiryViewDetail = directInquiryMapper.findDirectInquiryDetailOfMe(inquiryId, memberId);
        }

        if (directInquiryViewDetail == null) {
            throw new CustomException(ErrorType.ADMIN_INQUIRY_NOT_FOUND);
        }

        return directInquiryViewDetail;
    }

    @Transactional
    public AnswerDirectInquiryResponse answerDirectInquiry(Long inquiryId, AnswerDirectInquiryRequest request, Long adminId) {
        DirectInquiry directInquiry = directInquiryMapper.findById(inquiryId);

        if (directInquiry == null) {
            throw new CustomException(ErrorType.ADMIN_INQUIRY_NOT_FOUND);
        }

        if (directInquiry.getAnsweredAt() != null) {
            throw new CustomException(ErrorType.ADMIN_INQUIRY_ALREADY_ANSWERED);
        }

        LocalDateTime now = LocalDateTime.now();
        DirectInquiry updateDto = DirectInquiry.builder()
                .id(inquiryId)
                .adminId(adminId)
                .answer(request.getAnswer())
                .answeredAt(now)
                .build();

        int updatedRows = directInquiryMapper.update(updateDto);

        if (updatedRows <= 0) {
            throw new CustomException(ErrorType.ADMIN_INQUIRY_UPDATED_FAIL);
        }

        return AnswerDirectInquiryResponse.builder()
                .id(inquiryId)
                .adminId(adminId)
                .answer(request.getAnswer())
                .answeredAt(now)
                .build();
    }
}
