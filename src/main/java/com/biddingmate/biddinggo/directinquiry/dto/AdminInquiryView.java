package com.biddingmate.biddinggo.directinquiry.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminInquiryView {
    private Long id;
    private String nickname;
    private String category;
    private String answer;
    private LocalDateTime answeredAt;
    private LocalDateTime createdAt;
}
