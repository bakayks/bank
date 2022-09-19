package com.example.bank.service;

import com.example.bank.dal.entity.User;
import com.example.bank.dal.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class SpringSecurityAuditorAwarServiceImpl implements AuditorAware<String> {
    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.ofNullable("system");
        }else {
            return Optional.ofNullable(((UserDetails) authentication.getPrincipal()).getUsername());
        }
    }
}