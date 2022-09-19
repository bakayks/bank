package com.example.bank.service;

import com.example.bank.dal.dto.CashTransferDto;
import com.example.bank.dal.entity.CashBox;
import com.example.bank.dal.entity.CashTransfer;
import com.example.bank.dal.entity.Client;
import com.example.bank.dal.repo.CashBoxRepository;
import com.example.bank.dal.repo.CashTransferRepository;
import com.example.bank.dal.repo.ClientRepository;
import com.example.bank.filter.CashTransferSpecification;
import com.example.bank.model.CashTransferFilterModel;
import com.example.bank.model.ReplenishModel;
import com.example.bank.model.WithdrawalModel;
import com.example.bank.model.enums.Operations;
import com.example.bank.model.enums.TransferStatus;
import com.example.bank.dal.entity.User;
import com.example.bank.dal.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    private ClientRepository clientRepository;

    public String createCashTransfer(CashTransferDto cashTransferDto) {
        Optional<CashBox> cashBoxOptional = cashBoxRepository.findById(getCurrentUser().getCashBox().getId());

        if (!cashBoxOptional.isPresent()) return "Ошибка при переводе";

        CashTransfer cashTransfer = CashTransfer.builder()
                .transferStatus(TransferStatus.CREATED)
                .transferAmount(cashTransferDto.getTransferAmount())
                .currency(cashTransferDto.getCurrency())
                .senderCashBox(cashBoxOptional.get())
                .clientSender(clientRepository.save(Client.builder()
                        .lastname(cashTransferDto.getSenderLastname())
                        .firstname(cashTransferDto.getSenderFirstname())
                        .patronymic(cashTransferDto.getSenderPatronymic())
                        .number(cashTransferDto.getSenderNumber())
                        .build()))
                .clientRecipient(clientRepository.save(Client.builder()
                        .lastname(cashTransferDto.getRecipientLastname())
                        .firstname(cashTransferDto.getRecipientFirstname())
                        .patronymic(cashTransferDto.getRecipientPatronymic())
                        .number(cashTransferDto.getRecipientNumber())
                    .build()))
                .uniqueCode(UUID.randomUUID().toString())
                .build();
        cashTransferRepository.save(cashTransfer);

        ReplenishModel replenishModel = ReplenishModel.builder()
                    .cashBoxId(cashBoxOptional.get().getId())
                    .replenishAmount(cashTransfer.getTransferAmount())
                    .currency(cashTransfer.getCurrency())
                .build();
        replenishService.operationWithBalance(replenishModel, Operations.ADDITION);
        return "Перевод принят, уникальный код: " + cashTransfer.getUniqueCode();
    }

    public String withdrawal(WithdrawalModel withdrawalModel) {
        User user = getCurrentUser();
        Optional<CashTransfer> cashTransferOptional = cashTransferRepository.getByUniqueCode(withdrawalModel.getUniqueCode());
        Optional<CashBox> recipientCashBoxOptional = cashBoxRepository.findById(user.getCashBox().getId());
        if (recipientCashBoxOptional.isPresent() && cashTransferOptional.isPresent() && cashTransferOptional.get().getTransferStatus() == TransferStatus.CREATED) {
            CashTransfer cashTransfer = cashTransferOptional.get();
            if (cashTransfer.getTransferStatus() == TransferStatus.CREATED) {
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

        for (CashTransfer cashTransfer : pageTuts.getContent()) {
            cashTransferDtoList.add(CashTransferDto.builder()
                    .id(cashTransfer.getId())
                    .uniqueCode(getCurrentUser().getCashBox().getId().equals(cashTransfer.getSenderCashBox().getId()) ? cashTransfer.getUniqueCode() : "КОНФИДЕНЦИАЛЬНО")
                    .currency(cashTransfer.getCurrency())
                    .createdDate(cashTransfer.getCreationDate())
                    .transferAmount(cashTransfer.getTransferAmount())
                    .transferComment(cashTransfer.getTransferComment())
                    .transferStatus(cashTransfer.getTransferStatus().name())
                    .senderCashBoxId(cashTransfer.getSenderCashBox().getId())
                    .senderCashBoxName(cashTransfer.getSenderCashBox().getName())
                    .senderLastname(cashTransfer.getClientSender().getLastname())
                    .senderFirstname(cashTransfer.getClientSender().getFirstname())
                    .senderPatronymic(cashTransfer.getClientSender().getPatronymic())
                    .senderNumber(cashTransfer.getClientSender().getNumber())
                    .recipientCashBoxId(cashTransfer.getRecipientCashBox() != null ? cashTransfer.getRecipientCashBox().getId() : null)
                    .recipientCashBoxName(cashTransfer.getRecipientCashBox() != null ? cashTransfer.getRecipientCashBox().getName() : null)
                    .recipientLastname(cashTransfer.getClientSender().getLastname())
                    .recipientFirstname(cashTransfer.getClientSender().getFirstname())
                    .recipientPatronymic(cashTransfer.getClientSender().getPatronymic())
                    .recipientNumber(cashTransfer.getClientSender().getNumber())
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
