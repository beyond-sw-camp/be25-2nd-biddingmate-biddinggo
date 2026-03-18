package com.biddingmate.biddinggo.auctioninquiry.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AuctionInquiry {

    private Long id;
    private Long auctionId;
    private Long writerId;
    private Long answererId;

    private String content;
    private String answer;

    private LocalDateTime answeredAt;

    private String status;
    private LocalDateTime createdAt;
}