package com.example.bank.model;

import com.example.bank.dal.dto.CashTransferDto;
import lombok.Builder;

import java.util.List;

@Builder
public class CashTransferPaginationModel {
    public List<CashTransferDto> cashTransferList;
    public Integer bookPage;
    public Long totalItems;
    public Integer totalPages;
}
