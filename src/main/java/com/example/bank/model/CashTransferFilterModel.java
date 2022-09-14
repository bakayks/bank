package com.example.bank.model;

import com.example.bank.Currency;
import com.example.bank.enums.TransferStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CashTransferFilterModel {
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
}
