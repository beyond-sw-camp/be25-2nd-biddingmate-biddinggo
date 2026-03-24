package com.biddingmate.biddinggo.auctioninquiry.mapper;

import com.biddingmate.biddinggo.auctioninquiry.model.AuctionInquiry;
import com.biddingmate.biddinggo.common.inif.IMybatisCRUD;
import org.apache.ibatis.annotations.Mapper;
import java.util.Optional;

@Mapper
public interface AuctionInquiryMapper extends IMybatisCRUD<AuctionInquiry> {

    Optional<AuctionInquiry> findInquiryById(Long id);

    int updateAnswer(AuctionInquiry inquiry);
}