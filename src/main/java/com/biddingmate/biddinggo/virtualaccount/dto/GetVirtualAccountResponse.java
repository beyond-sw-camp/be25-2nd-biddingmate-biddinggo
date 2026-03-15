package com.biddingmate.biddinggo.virtualaccount.dto;

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
public class GetVirtualAccountResponse {
    private String status;
    private String orderId;
    private Long amount;
    private String bankCode;
    private String bankAccount;
    private String accountHolderName;
    private LocalDateTime dueDate;
}
