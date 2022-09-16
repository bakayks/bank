package com.example.bank.script;

import com.example.bank.dal.entity.CashBox;
import com.example.bank.dal.entity.CashTransfer;
import com.example.bank.dal.repo.CashBoxRepository;
import com.example.bank.dal.repo.CashTransferRepository;
import com.example.bank.model.enums.Currency;
import com.example.bank.model.enums.TransferStatus;
import com.example.bank.user.User;
import com.example.bank.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;


@Component
public class StarterScript implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CashBoxRepository cashBoxRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        CashBox cashBox1 = CashBox.builder()
                .name("Касса №1")
                .currentBalanceSOM(new BigDecimal("1000"))
                .currentBalanceEURO(new BigDecimal("1000"))
                .currentBalanceUSD(new BigDecimal("1000"))
                .build();
        CashBox cashBox2 = CashBox.builder()
                .name("Касса №2")
                .currentBalanceSOM(new BigDecimal("1000"))
                .currentBalanceEURO(new BigDecimal("1000"))
                .currentBalanceUSD(new BigDecimal("1000"))
                .build();
        CashBox cashBox3 = CashBox.builder()
                .name("Касса №3")
                .currentBalanceSOM(new BigDecimal("1000"))
                .currentBalanceEURO(new BigDecimal("1000"))
                .currentBalanceUSD(new BigDecimal("1000"))
                .build();
        cashBoxRepo.save(cashBox1);
        cashBoxRepo.save(cashBox2);
        cashBoxRepo.save(cashBox3);


        User user1 = User.builder()
                .username("user1")
                .password(passwordEncoder.encode("123"))
                .cashBox(cashBox1)
                .build();
        User user2 = User.builder()
                .username("user2")
                .password(passwordEncoder.encode("123"))
                .cashBox(cashBox2)
                .build();
        User user3 = User.builder()
                .username("user3")
                .password(passwordEncoder.encode("123"))
                .cashBox(cashBox3)
                .build();
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
    }
}
