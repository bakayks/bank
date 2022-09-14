package com.example.bank.model;

import com.example.bank.Currency;
import com.example.bank.enums.TransferStatus;
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
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Bishkek")
    private Date startDate;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Bishkek")
    private Date endDate;
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
