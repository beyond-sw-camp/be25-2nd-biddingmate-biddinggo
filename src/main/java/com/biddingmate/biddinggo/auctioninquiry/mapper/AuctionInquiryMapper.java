package com.biddingmate.biddinggo.auctioninquiry.mapper;

import com.biddingmate.biddinggo.auctioninquiry.dto.AuctionInquiryView;
import com.biddingmate.biddinggo.auctioninquiry.model.AuctionInquiry;
import com.biddingmate.biddinggo.common.inif.IMybatisCRUD;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Optional;

@Mapper
public interface AuctionInquiryMapper extends IMybatisCRUD<AuctionInquiry> {

    Optional<AuctionInquiry> findInquiryById(Long id);

    int updateAnswer(AuctionInquiry inquiry);

    List<AuctionInquiryView> selectInquiryList(RowBounds rowBounds, @Param("auctionId") Long auctionId);

    int selectInquiryCount(@Param("auctionId") Long auctionId);
}