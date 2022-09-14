package com.example.bank.script;

import com.example.bank.cashBox.CashBox;
import com.example.bank.cashBox.CashBoxRepository;
import com.example.bank.user.User;
import com.example.bank.user.UserRepository;
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
        CashBox cashBox4 = CashBox.builder()
                .name("Касса №4")
                .currentBalanceSOM(new BigDecimal("1000"))
                .currentBalanceEURO(new BigDecimal("1000"))
                .currentBalanceUSD(new BigDecimal("1000"))
                .build();
        cashBoxRepo.save(cashBox1);
        cashBoxRepo.save(cashBox2);
        cashBoxRepo.save(cashBox3);
        cashBoxRepo.save(cashBox4);


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
        User user4 = User.builder()
                .username("user4")
                .password(passwordEncoder.encode("123"))
                .cashBox(cashBox4)
                .build();
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
    }
}
