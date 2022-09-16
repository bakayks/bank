package com.example.bank.model;

import com.example.bank.model.enums.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
