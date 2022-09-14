package com.example.bank.services;

import com.example.bank.cashBox.CashBox;
import com.example.bank.cashBox.CashBoxRepository;
import com.example.bank.enums.Operations;
import com.example.bank.model.ReplenishModel;
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

    public ResponseEntity<HttpStatus> operationWithBalance(ReplenishModel replenishModel, Operations operations) {
        Optional<CashBox> cashBoxOptional = cashBoxRepository.findById(replenishModel.getCashBoxId());
        if(cashBoxOptional.isPresent()){
            CashBox cashBox = cashBoxOptional.get();
            if(operations == Operations.SUBTRACT){
                switch (replenishModel.getCurrency()) {
                    case SOM -> cashBox.setCurrentBalanceSOM(subtractOperation(replenishModel.getReplenishAmount(), cashBox.getCurrentBalanceSOM()));
                    case EURO -> cashBox.setCurrentBalanceEURO(subtractOperation(replenishModel.getReplenishAmount(), cashBox.getCurrentBalanceEURO()));
                    case DOLLAR -> cashBox.setCurrentBalanceUSD(subtractOperation(replenishModel.getReplenishAmount(), cashBox.getCurrentBalanceUSD()));
                }
            }else if(operations == Operations.ADDITION){
                switch (replenishModel.getCurrency()) {
                    case SOM -> cashBox.setCurrentBalanceSOM(additionOperation(replenishModel.getReplenishAmount(), cashBox.getCurrentBalanceSOM()));
                    case EURO -> cashBox.setCurrentBalanceEURO(additionOperation(replenishModel.getReplenishAmount(), cashBox.getCurrentBalanceEURO()));
                    case DOLLAR -> cashBox.setCurrentBalanceUSD(additionOperation(replenishModel.getReplenishAmount(), cashBox.getCurrentBalanceUSD()));
                }
            }
            cashBoxRepository.save(cashBox);
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    private BigDecimal additionOperation(BigDecimal additionValue, BigDecimal currentBalance) {
        return currentBalance.add(additionValue);
    }

    private BigDecimal subtractOperation(BigDecimal additionValue, BigDecimal currentBalance) {
        return currentBalance.subtract(additionValue);
    }
}
