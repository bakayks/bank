package com.example.bank.dal.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CashBoxDto {

    private Integer id;

    private String name;

    private String currentBalanceUSD;

    private String currentBalanceEURO;

    private String currentBalanceSOM;
}

