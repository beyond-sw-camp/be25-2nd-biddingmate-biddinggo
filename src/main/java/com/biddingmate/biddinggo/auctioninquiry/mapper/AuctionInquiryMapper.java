package com.biddingmate.biddinggo.auctioninquiry.mapper;

import com.biddingmate.biddinggo.auctioninquiry.model.AuctionInquiry;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface AuctionInquiryMapper {

    @Insert("""
        INSERT INTO auction_inquiry
        (auction_id, writer_id, content, status, created_at)
        VALUES
        (#{auctionId}, #{writerId}, #{content}, 'ACTIVE', NOW())
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertInquiry(AuctionInquiry inquiry);
}