package com.example.bank.services;

import com.example.bank.cashBox.CashBox;
import com.example.bank.cashBox.CashBoxRepository;
import com.example.bank.enums.Operations;
import com.example.bank.enums.TransferStatus;
import com.example.bank.dto.CashTransferDto;
import com.example.bank.model.ReplenishModel;
import com.example.bank.model.WithdrawalModel;
import com.example.bank.transfer.CashTransfer;
import com.example.bank.transfer.CashTransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class CashTransferService {

    @Autowired
    private CashBoxRepository cashBoxRepository;

    @Autowired
    private CashTransferRepository cashTransferRepository;

    @Autowired
    private ReplenishService replenishService;

    public ResponseEntity<HttpStatus> createCashTransfer(CashTransferDto cashTransferModel) {
        Optional<CashBox> cashBoxOptional = cashBoxRepository.findById(cashTransferModel.getSenderCashBoxId());
        if(cashBoxOptional.isPresent()) {
            CashTransfer cashTransfer = new CashTransfer();
            cashTransfer.setTransferStatus(TransferStatus.CREATED);
            cashTransfer.setTransferAmount(cashTransferModel.getTransferAmount());
            cashTransfer.setCurrency(cashTransferModel.getCurrency());
            cashTransfer.setSenderCashBox(cashBoxOptional.get());
            cashTransfer.setSenderLastname(cashTransferModel.getSenderLastname());
            cashTransfer.setSenderFirstname(cashTransferModel.getSenderFirstname());
            cashTransfer.setSenderPatronymic(cashTransferModel.getSenderPatronymic());
            cashTransfer.setSenderNumber(cashTransferModel.getSenderNumber());
            cashTransfer.setRecipientLastname(cashTransferModel.getRecipientLastname());
            cashTransfer.setRecipientFirstname(cashTransferModel.getRecipientFirstname());
            cashTransfer.setRecipientPatronymic(cashTransferModel.getRecipientPatronymic());
            cashTransfer.setRecipientNumber(cashTransferModel.getRecipientNumber());
            cashTransfer.setUniqueCode(UUID.randomUUID().toString());
            cashTransfer.setCreatedDate(new Date());
            cashTransferRepository.save(cashTransfer);

            CashBox senderCashBox = cashBoxOptional.get();

            ReplenishModel replenishModel = new ReplenishModel();
            replenishModel.setCashBoxId(senderCashBox.getId());
            replenishModel.setCurrency(cashTransfer.getCurrency());
            replenishModel.setReplenishAmount(cashTransfer.getTransferAmount());

            replenishService.operationWithBalance(replenishModel, Operations.ADDITION);
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<HttpStatus> withdrawal(WithdrawalModel withdrawalModel) {
        Optional<CashTransfer> cashTransferOptional = cashTransferRepository.getByUniqueCode(withdrawalModel.getUniqueCode());
        Optional<CashBox> recipientCashBoxOptional = cashBoxRepository.findById(withdrawalModel.getRecipientCashBoxId());
        if(recipientCashBoxOptional.isPresent() && cashTransferOptional.isPresent()) {
            CashTransfer cashTransfer = cashTransferOptional.get();
            CashBox recipientCashBox = recipientCashBoxOptional.get();

            cashTransfer.setRecipientCashBox(recipientCashBox);
            cashTransfer.setTransferStatus(TransferStatus.ISSUED);

            ReplenishModel replenishModel = new ReplenishModel();
            replenishModel.setReplenishAmount(cashTransfer.getTransferAmount());
            replenishModel.setCurrency(cashTransfer.getCurrency());
            replenishModel.setCashBoxId(cashTransfer.getRecipientCashBox().getId());
            replenishService.operationWithBalance(replenishModel, Operations.SUBTRACT);
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
