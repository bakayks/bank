package com.example.bank.dal.entity;

import com.example.bank.model.enums.Currency;
import com.example.bank.model.enums.TransferStatus;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cash_transfer")
public class CashTransfer extends Audit<String>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @NotEmpty
    private CashBox senderCashBox;

    @ManyToOne
    @NotEmpty
    private CashBox recipientCashBox;

    @Column(name = "transfer_amount")
    @NotEmpty
    private BigDecimal transferAmount;

    @Column(name = "currency")
    @NotEmpty
    private Currency currency;

    @Column(name = "transfer_status")
    @NotEmpty
    private TransferStatus transferStatus;

    @Column(name = "unique_code")
    private String uniqueCode;

    @Column(name = "transfer_comment")
    private String transferComment;

    @ManyToOne
    private Client clientSender;

    @ManyToOne
    private Client clientRecipient;
}
