package com.example.bank.model;

import com.example.bank.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReplenishModel {

    private Integer cashBoxId;

    private Currency currency;

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private BigDecimal replenishAmount;

}
