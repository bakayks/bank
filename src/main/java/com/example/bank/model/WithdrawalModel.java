package com.example.bank.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawalModel {

    private Integer recipientCashBoxId;

    private String uniqueCode;
}
