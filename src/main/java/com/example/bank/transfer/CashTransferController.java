package com.example.bank.transfer;

import com.example.bank.dto.CashTransferDto;
import com.example.bank.filter.CashTransferSpecification;
import com.example.bank.model.CashTransferFilterModel;
import com.example.bank.model.WithdrawalModel;
import com.example.bank.services.CashTransferService;
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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cash/transfer")
public class CashTransferController {

    @Autowired
    private CashTransferRepository cashTransferRepository;

    @Autowired
    private CashTransferService cashTransferService;

    @Autowired
    private CashTransferSpecification cashTransferSpecification;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<HttpStatus> createCashTransfer(@RequestBody CashTransferDto cashTransferModel) {
        return cashTransferService.createCashTransfer(cashTransferModel);
    }

    @PostMapping("/withdrawal")
    public ResponseEntity<HttpStatus> withdrawal(@RequestBody WithdrawalModel withdrawalModel) {
        return cashTransferService.withdrawal(withdrawalModel);
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list() {
        return getListCashTransfer(1, 5, new CashTransferFilterModel());
    }

    @PostMapping("/filter")
    private ResponseEntity<Map<String, Object>> getListCashTransfer(@RequestParam(defaultValue = "1") int page,
                                                               @RequestParam(defaultValue = "3") int size, @RequestBody CashTransferFilterModel cashTransferFilterModel){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userRepository.getByUsername(userDetails.getUsername());

        List<CashTransferDto> cashTransferDtoList = new ArrayList<>();

        Pageable paging = PageRequest.of(page - 1, size);

        Page<CashTransfer> pageTuts = cashTransferRepository.findAll(cashTransferSpecification.getCashTransferBySpecification(cashTransferFilterModel), paging);
        for(CashTransfer cashTransfer : pageTuts.getContent()) {
            cashTransferDtoList.add(CashTransferDto.builder()
                    .id(cashTransfer.getId())
                    .uniqueCode(currentUser.getCashBox().getId().equals(cashTransfer.getSenderCashBox().getId()) ? cashTransfer.getUniqueCode() : "КОНФИДЕНЦИАЛЬНО")
                    .currency(cashTransfer.getCurrency())
                            .createdDate(cashTransfer.getCreatedDate())
                    .transferAmount(cashTransfer.getTransferAmount())
                    .transferComment(cashTransfer.getTransferComment())
                    .transferStatus(cashTransfer.getTransferStatus())
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

        Map<String, Object> response = new HashMap<>();
        response.put("cashTransfers", cashTransferDtoList);
        response.put("currentPage", pageTuts.getNumber());
        response.put("totalItems", pageTuts.getTotalElements());
        response.put("totalPages", pageTuts.getTotalPages());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
