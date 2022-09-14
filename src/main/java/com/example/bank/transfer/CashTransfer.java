package com.example.bank.transfer;

import com.example.bank.Currency;
import com.example.bank.cashBox.CashBox;
import com.example.bank.enums.TransferStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cash_transfer")
public class CashTransfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "currency")
    @NotEmpty
    private Currency currency;

    @ManyToOne
    @NotEmpty
    private CashBox senderCashBox;

    @ManyToOne
    @NotEmpty
    private CashBox recipientCashBox;

    @Column(name = "transfer_amount")
    @NotEmpty
    private BigDecimal transferAmount;

    @Column(name = "transfer_status")
    @NotEmpty
    private TransferStatus transferStatus;

    @Column(name = "sender_firstname")
    @NotEmpty
    private String senderFirstname;

    @Column(name = "sender_lastname")
    @NotEmpty
    private String senderLastname;

    @Column(name = "sender_patronymic")
    private String senderPatronymic;

    @Column(name = "recipient_firstname")
    @NotEmpty
    private String recipientFirstname;

    @Column(name = "recipient_lastname")
    @NotEmpty
    private String recipientLastname;

    @Column(name = "recipient_patronymic")
    private String recipientPatronymic;

    @Column(name = "unique_code")
    private String uniqueCode;

    @Column(name = "sender_number")
    private String senderNumber;

    @Column(name = "recipient_number")
    private String recipientNumber;

    @Column(name = "transfer_comment")
    private String transferComment;

    @Column(name = "created_date")
    private Date createdDate;
}
