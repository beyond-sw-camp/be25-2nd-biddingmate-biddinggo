package com.biddingmate.biddinggo.admininquiry.mapper;

import com.biddingmate.biddinggo.admininquiry.dto.AdminInquiryView;
import com.biddingmate.biddinggo.admininquiry.dto.AdminInquiryViewDetail;
import com.biddingmate.biddinggo.admininquiry.model.AdminInquiry;
import com.biddingmate.biddinggo.common.inif.IMybatisCRUD;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

@Mapper
public interface AdminInquiryMapper extends IMybatisCRUD<AdminInquiry> {
    List<AdminInquiryView> findAdminInquiry(RowBounds rowBounds,
                                            @Param("order") String sortOrder);
    List<AdminInquiryView> findAdminInquiryOfMe(RowBounds rowBounds,
                                                @Param("memberId") long memberId,
                                                @Param("order") String sortOrder);
    int getAdminInquiryTotal();
    int getAdminInquiryTotalOfMe(@Param("memberId") long memberId);

    AdminInquiryViewDetail findAdminInquiryDetail(Long inquiryId);
    AdminInquiryViewDetail findAdminInquiryDetailOfMe(Long inquiryId, Long memberId);
}
