package com.example.bank.service;

import com.example.bank.dal.entity.CashBox;
import com.example.bank.dal.entity.CashBoxBalance;
import com.example.bank.dal.repo.CashBoxRepository;
import com.example.bank.model.ReplenishModel;
import com.example.bank.model.enums.Operations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ReplenishService {

    @Autowired
    private CashBoxRepository cashBoxRepository;

    public String operationWithBalance(ReplenishModel replenishModel, Operations operations) {
        String message = null;
        Optional<CashBox> cashBoxOptional = cashBoxRepository.findById(replenishModel.getCashBoxId());
        if(cashBoxOptional.isPresent()){
            CashBox cashBox = cashBoxOptional.get();
            message = checkOperationMethod(cashBox, replenishModel, operations);
            cashBoxRepository.save(cashBox);
        }
        return message;
    }

    public String checkOperationMethod(CashBox cashBox, ReplenishModel replenishModel, Operations operations){
        BigDecimal bigDecimal = null;
        CashBoxBalance cashBoxBalance = cashBox.getCashBoxBalance();
        switch (replenishModel.getCurrency()) {
            case SOM:
                bigDecimal = doOperation(replenishModel.getReplenishAmount(), cashBox.getCashBoxBalance().getCurrentBalanceSOM(), operations);
                cashBoxBalance.setCurrentBalanceSOM(bigDecimal != null ? bigDecimal : cashBoxBalance.getCurrentBalanceSOM());
                break;
            case EURO:
                bigDecimal = doOperation(replenishModel.getReplenishAmount(), cashBox.getCashBoxBalance().getCurrentBalanceEURO(), operations);
                cashBoxBalance.setCurrentBalanceEURO(bigDecimal != null ? bigDecimal : cashBoxBalance.getCurrentBalanceEURO());
                break;
            case DOLLAR:
                bigDecimal = doOperation(replenishModel.getReplenishAmount(), cashBox.getCashBoxBalance().getCurrentBalanceUSD(), operations);
                cashBoxBalance.setCurrentBalanceUSD(bigDecimal != null ? bigDecimal : cashBoxBalance.getCurrentBalanceUSD());
                break;
        }
        if (bigDecimal == null)
            return "Ошибка снятия, недостаточно средств";
        return "Снятие прошло успешно!";
    }

    private BigDecimal doOperation(BigDecimal additionValue, BigDecimal currentBalance, Operations operations) {
        if (operations == Operations.ADDITION)
            return currentBalance.add(additionValue);
        if (operations == Operations.SUBTRACT) {
            if(currentBalance.subtract(additionValue).compareTo(BigDecimal.ZERO) >= 0)
                return currentBalance.subtract(additionValue);
        }
        return null;
    }
}
