package com.example.bank.service;

import com.example.bank.dal.entity.CashBox;
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
        Optional<CashBox> cashBoxOptional = cashBoxRepository.findById(replenishModel.getCashBoxId());
        if(cashBoxOptional.isPresent()){
            CashBox cashBox = cashBoxOptional.get();
            if(operations == Operations.SUBTRACT){
                BigDecimal bigDecimal = null;
                switch (replenishModel.getCurrency()) {
                    case SOM:
                        bigDecimal = subtractOperation(replenishModel.getReplenishAmount(), cashBox.getCurrentBalanceSOM());
                        if(bigDecimal.compareTo(BigDecimal.ZERO) >= 0){
                            cashBox.setCurrentBalanceSOM(bigDecimal);
                        }else {
                            return "Недостаточно валюты (сом) на балансе";
                        }
                        break;
                    case EURO: cashBox.setCurrentBalanceEURO(subtractOperation(replenishModel.getReplenishAmount(), cashBox.getCurrentBalanceEURO()));
                        bigDecimal = subtractOperation(replenishModel.getReplenishAmount(), cashBox.getCurrentBalanceEURO());
                        if(bigDecimal.compareTo(BigDecimal.ZERO) >= 0){
                            cashBox.setCurrentBalanceEURO(bigDecimal);
                        } else {
                            return "Недостаточно валюты (евро) на балансе";
                        }
                        break;
                    case DOLLAR:
                        bigDecimal = subtractOperation(replenishModel.getReplenishAmount(), cashBox.getCurrentBalanceUSD());
                        if(bigDecimal.compareTo(BigDecimal.ZERO) >= 0){
                            cashBox.setCurrentBalanceUSD(bigDecimal);
                        }   else {
                            return "Недостаточно валюты (доллар) на балансе";
                        }
                        break;
                }
            }else if(operations == Operations.ADDITION){
                switch (replenishModel.getCurrency()) {
                    case SOM:
                        cashBox.setCurrentBalanceSOM(additionOperation(replenishModel.getReplenishAmount(), cashBox.getCurrentBalanceSOM()));
                        break;
                    case EURO:
                        cashBox.setCurrentBalanceEURO(additionOperation(replenishModel.getReplenishAmount(), cashBox.getCurrentBalanceEURO()));
                        break;
                    case DOLLAR:
                        cashBox.setCurrentBalanceUSD(additionOperation(replenishModel.getReplenishAmount(), cashBox.getCurrentBalanceUSD()));
                        break;
                }
            }
            cashBoxRepository.save(cashBox);
        }
        return "Снятие прошло успешно!";
    }

    private BigDecimal additionOperation(BigDecimal additionValue, BigDecimal currentBalance) {
        return currentBalance.add(additionValue);
    }

    private BigDecimal subtractOperation(BigDecimal additionValue, BigDecimal currentBalance) {
        return currentBalance.subtract(additionValue);
    }
}
