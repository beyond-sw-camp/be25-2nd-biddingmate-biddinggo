package com.biddingmate.biddinggo.auctioninquiry.dto;

import com.biddingmate.biddinggo.auctioninquiry.model.AuctionInquiryStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuctionInquiryView {
    private Long id;
    private String title;
    private String content;
    private String writerName;
    private String answer;
    private AuctionInquiryStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    // 닉네임 마스킹 로직
    public void maskWriterName() {
        if (this.writerName != null && this.writerName.length() >= 3) {
            this.writerName = this.writerName.substring(0, 3) + "***";
        } else if (this.writerName != null && !this.writerName.isEmpty()) {
            this.writerName = this.writerName + "***";
        } else {
            this.writerName = "익명***";
        }
    }
}