package com.example.bank.service;

import com.example.bank.dal.dto.CashBoxDto;
import com.example.bank.dal.entity.CashBox;
import com.example.bank.dal.repo.CashBoxRepository;
import com.example.bank.dal.entity.User;
import com.example.bank.dal.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Optional;

@Service
public class CashBoxService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CashBoxRepository cashBoxRepository;

    public void getCashBoxOfCurrentUser(Model model) {
        Optional<CashBox> cashBoxOptional = cashBoxRepository.findById(getCurrentUser().getCashBox().getId());
        CashBoxDto cashBoxDto = new CashBoxDto();
        if(cashBoxOptional.isPresent()) {
            CashBox cashBox = cashBoxOptional.get();
            cashBoxDto = CashBoxDto.builder()
                    .id(cashBox.getId())
                    .name(cashBox.getName())
                    .currentBalanceUSD(cashBox.getCashBoxBalance().getCurrentBalanceUSD().toString())
                    .currentBalanceEURO(cashBox.getCashBoxBalance().getCurrentBalanceEURO().toString())
                    .currentBalanceSOM(cashBox.getCashBoxBalance().getCurrentBalanceSOM().toString())
                .build();
        }
        model.addAttribute("cashBox", cashBoxDto);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userRepository.getByUsername(userDetails.getUsername());
    }
}
