package com.example.bank.dto;

import com.example.bank.Currency;
import com.example.bank.enums.TransferStatus;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CashTransferDto {
    private Integer id;

    private Currency currency;

    private Integer senderCashBoxId;

    private String senderCashBoxName;

    private Integer recipientCashBoxId;

    private String recipientCashBoxName;

    private BigDecimal transferAmount;

    private TransferStatus transferStatus;

    private String senderFirstname;

    private String senderLastname;

    private String senderPatronymic;

    private String recipientFirstname;

    private String recipientLastname;

    private String recipientPatronymic;

    private String senderNumber;

    private String recipientNumber;

    private String transferComment;
}