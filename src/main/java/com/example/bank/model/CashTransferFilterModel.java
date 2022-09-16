package com.example.bank.model;

import com.example.bank.model.enums.Currency;
import com.example.bank.model.enums.TransferStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CashTransferFilterModel {
    private String startDate;
    private String endDate;
    private Currency currency;
    private String senderCashBoxName;
    private String senderFirstName;
    private String senderLastName;
    private String senderPatronymic;
    private String recipientCashBoxName;
    private String recipientFirstName;
    private String recipientLastName;
    private String recipientPatronymic;
    private String transferAmount;
    private TransferStatus transferStatus;
    private String keyword;
}
