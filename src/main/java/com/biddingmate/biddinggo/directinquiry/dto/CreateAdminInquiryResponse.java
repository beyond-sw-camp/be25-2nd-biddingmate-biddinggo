package com.biddingmate.biddinggo.directinquiry.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class CreateAdminInquiryResponse {
    private Long id;
    private Long writerId;
    private String category;
    private String content;
    private LocalDateTime createdAt;
}
