package com.biddingmate.biddinggo.directinquiry.mapper;

import com.biddingmate.biddinggo.directinquiry.dto.DirectInquiryView;
import com.biddingmate.biddinggo.directinquiry.dto.DirectInquiryViewDetail;
import com.biddingmate.biddinggo.directinquiry.model.DirectInquiry;
import com.biddingmate.biddinggo.common.inif.IMybatisCRUD;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

@Mapper
public interface DirectInquiryMapper extends IMybatisCRUD<DirectInquiry> {
    List<DirectInquiryView> findAdminInquiry(RowBounds rowBounds,
                                             @Param("order") String sortOrder);
    List<DirectInquiryView> findAdminInquiryOfMe(RowBounds rowBounds,
                                                 @Param("memberId") long memberId,
                                                 @Param("order") String sortOrder);
    int getAdminInquiryTotal();
    int getAdminInquiryTotalOfMe(@Param("memberId") long memberId);

    DirectInquiryViewDetail findAdminInquiryDetail(Long inquiryId);
    DirectInquiryViewDetail findAdminInquiryDetailOfMe(Long inquiryId, Long memberId);
}
