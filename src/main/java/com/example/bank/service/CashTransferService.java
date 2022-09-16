package com.example.bank.service;

import com.example.bank.dal.dto.CashTransferDto;
import com.example.bank.dal.entity.CashBox;
import com.example.bank.dal.entity.CashTransfer;
import com.example.bank.dal.repo.CashBoxRepository;
import com.example.bank.dal.repo.CashTransferRepository;
import com.example.bank.filter.CashTransferSpecification;
import com.example.bank.model.CashTransferFilterModel;
import com.example.bank.model.CashTransferPaginationModel;
import com.example.bank.model.ReplenishModel;
import com.example.bank.model.WithdrawalModel;
import com.example.bank.model.enums.Operations;
import com.example.bank.model.enums.TransferStatus;
import com.example.bank.user.User;
import com.example.bank.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class CashTransferService {

    @Autowired
    private CashBoxRepository cashBoxRepository;

    @Autowired
    private CashTransferRepository cashTransferRepository;

    @Autowired
    private ReplenishService replenishService;

    @Autowired
    private CashTransferSpecification cashTransferSpecification;

    @Autowired
    private UserRepository userRepository;

    public String createCashTransfer(CashTransferDto cashTransferDto) {
        Optional<CashBox> cashBoxOptional = cashBoxRepository.findById(getCurrentUser().getCashBox().getId());
        if(cashBoxOptional.isPresent()) {
            CashTransfer cashTransfer = CashTransfer.builder()
                    .transferStatus(TransferStatus.CREATED)
                    .transferAmount(cashTransferDto.getTransferAmount())
                    .currency(cashTransferDto.getCurrency())
                    .senderCashBox(cashBoxOptional.get())
                    .senderLastname(cashTransferDto.getSenderLastname())
                    .senderFirstname(cashTransferDto.getSenderFirstname())
                    .senderPatronymic(cashTransferDto.getSenderPatronymic())
                    .senderNumber(cashTransferDto.getSenderNumber())
                    .recipientLastname(cashTransferDto.getRecipientLastname())
                    .recipientFirstname(cashTransferDto.getRecipientFirstname())
                    .recipientPatronymic(cashTransferDto.getRecipientPatronymic())
                    .recipientNumber(cashTransferDto.getRecipientNumber())
                    .uniqueCode(UUID.randomUUID().toString())
                    .createdDate(new Date())
                .build();
            cashTransferRepository.save(cashTransfer);

            ReplenishModel replenishModel = new ReplenishModel();
            replenishModel.setCashBoxId(cashBoxOptional.get().getId());
            replenishModel.setCurrency(cashTransfer.getCurrency());
            replenishModel.setReplenishAmount(cashTransfer.getTransferAmount());

            replenishService.operationWithBalance(replenishModel, Operations.ADDITION);
            return "Перевод принят, уникальный код: " + cashTransfer.getUniqueCode();
        }
        return "Ошибка при переводе";
    }

    public String withdrawal(WithdrawalModel withdrawalModel) {
        User user = getCurrentUser();
        Optional<CashTransfer> cashTransferOptional = cashTransferRepository.getByUniqueCode(withdrawalModel.getUniqueCode());
        Optional<CashBox> recipientCashBoxOptional = cashBoxRepository.findById(user.getCashBox().getId());
        if(recipientCashBoxOptional.isPresent() && cashTransferOptional.isPresent()) {
            CashTransfer cashTransfer = cashTransferOptional.get();
            if(cashTransfer.getTransferStatus() == TransferStatus.CREATED){
                CashBox recipientCashBox = recipientCashBoxOptional.get();

                cashTransfer.setRecipientCashBox(recipientCashBox);
                cashTransfer.setTransferStatus(TransferStatus.ISSUED);

                ReplenishModel replenishModel = new ReplenishModel();
                replenishModel.setReplenishAmount(cashTransfer.getTransferAmount());
                replenishModel.setCurrency(cashTransfer.getCurrency());
                replenishModel.setCashBoxId(cashTransfer.getRecipientCashBox().getId());
                return replenishService.operationWithBalance(replenishModel, Operations.SUBTRACT);
            }
        }
        return "Произошла ошибка при снятии";
    }

    public void getModelCashTransferList(Integer page, Integer size,
                                         CashTransferFilterModel cashTransferFilterModel,
                                         Model model) {

        List<CashTransferDto> cashTransferDtoList = new ArrayList<>();

        Pageable paging = PageRequest.of(page - 1, size);
        Page<CashTransfer> pageTuts = cashTransferRepository.findAll(cashTransferSpecification.getCashTransferBySpecification(cashTransferFilterModel), paging);

        for(CashTransfer cashTransfer : pageTuts.getContent()) {
            cashTransferDtoList.add(CashTransferDto.builder()
                    .id(cashTransfer.getId())
                    .uniqueCode(getCurrentUser().getCashBox().getId().equals(cashTransfer.getSenderCashBox().getId()) ? cashTransfer.getUniqueCode() : "КОНФИДЕНЦИАЛЬНО")
                    .currency(cashTransfer.getCurrency())
                    .createdDate(cashTransfer.getCreatedDate())
                    .transferAmount(cashTransfer.getTransferAmount())
                    .transferComment(cashTransfer.getTransferComment())
                    .transferStatus(cashTransfer.getTransferStatus().name())
                    .senderCashBoxId(cashTransfer.getSenderCashBox().getId())
                    .senderCashBoxName(cashTransfer.getSenderCashBox().getName())
                    .senderLastname(cashTransfer.getSenderLastname())
                    .senderFirstname(cashTransfer.getSenderFirstname())
                    .senderPatronymic(cashTransfer.getSenderPatronymic())
                    .senderNumber(cashTransfer.getSenderNumber())
                    .recipientCashBoxId(cashTransfer.getRecipientCashBox() != null ? cashTransfer.getRecipientCashBox().getId() : null)
                    .recipientCashBoxName(cashTransfer.getRecipientCashBox() != null ? cashTransfer.getRecipientCashBox().getName() : null)
                    .recipientLastname(cashTransfer.getRecipientLastname())
                    .recipientFirstname(cashTransfer.getRecipientFirstname())
                    .recipientPatronymic(cashTransfer.getRecipientPatronymic())
                    .recipientNumber(cashTransfer.getRecipientNumber())
                .build());
        }
        model.addAttribute("transferPage", pageTuts);

        int totalPages = pageTuts.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("cashTransfers", cashTransferDtoList);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userRepository.getByUsername(userDetails.getUsername());
    }
}
