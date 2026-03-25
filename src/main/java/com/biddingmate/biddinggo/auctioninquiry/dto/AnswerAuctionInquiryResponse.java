package com.biddingmate.biddinggo.auctioninquiry.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerAuctionInquiryResponse {

    private Long id;
    private String answer;      

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime answeredAt;
}