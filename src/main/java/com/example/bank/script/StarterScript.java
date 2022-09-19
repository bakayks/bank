package com.example.bank.script;

import com.example.bank.dal.entity.CashBox;
import com.example.bank.dal.entity.CashBoxBalance;
import com.example.bank.dal.repo.CashBoxBalanceRepository;
import com.example.bank.dal.repo.CashBoxRepository;
import com.example.bank.dal.entity.User;
import com.example.bank.dal.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component
public class StarterScript implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CashBoxRepository cashBoxRepo;

    @Autowired
    private CashBoxBalanceRepository cashBoxBalanceRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        CashBoxBalance cashBoxBalance1 = CashBoxBalance.builder()
                    .currentBalanceSOM(new BigDecimal("1000"))
                    .currentBalanceEURO(new BigDecimal("1000"))
                    .currentBalanceUSD(new BigDecimal("1000"))
                .build();
        CashBox cashBox1 = CashBox.builder()
                .name("Касса №1")
                .cashBoxBalance(cashBoxBalance1)
                .build();

        CashBoxBalance cashBoxBalance2 = CashBoxBalance.builder()
                .currentBalanceSOM(new BigDecimal("1000"))
                .currentBalanceEURO(new BigDecimal("1000"))
                .currentBalanceUSD(new BigDecimal("1000"))
                .build();
        CashBox cashBox2 = CashBox.builder()
                .name("Касса №2")
                .cashBoxBalance(cashBoxBalance2)
                .build();

        CashBoxBalance cashBoxBalance3 = CashBoxBalance.builder()
                .currentBalanceSOM(new BigDecimal("1000"))
                .currentBalanceEURO(new BigDecimal("1000"))
                .currentBalanceUSD(new BigDecimal("1000"))
                .build();
        CashBox cashBox3 = CashBox.builder()
                .name("Касса №3")
                .cashBoxBalance(cashBoxBalance3)
                .build();
        cashBoxBalanceRepo.save(cashBoxBalance1);
        cashBoxBalanceRepo.save(cashBoxBalance2);
        cashBoxBalanceRepo.save(cashBoxBalance3);
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
