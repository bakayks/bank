package com.example.bank.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CashBoxDto {

    private Integer id;

    private String name;

    private BigDecimal currentBalanceUSD = BigDecimal.ZERO;

    private BigDecimal currentBalanceEURO = BigDecimal.ZERO;

    private BigDecimal currentBalanceSOM = BigDecimal.ZERO;
}

