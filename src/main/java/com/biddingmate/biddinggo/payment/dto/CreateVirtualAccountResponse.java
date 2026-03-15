package com.biddingmate.biddinggo.payment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class CreateVirtualAccountResponse {
    private String status;
    private String orderId;
    private Long amount;
    private String bankCode;
    private String bankAccount;
    private String accountHolderName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dueDate;
}
