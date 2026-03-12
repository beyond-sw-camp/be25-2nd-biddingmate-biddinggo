package com.biddingmate.biddinggo.virtualaccount.dto;

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
    private LocalDateTime dueDate;
}
