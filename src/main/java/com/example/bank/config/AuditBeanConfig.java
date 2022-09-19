package com.example.bank.config;

import com.example.bank.service.SpringSecurityAuditorAwarServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaAuditing
public class AuditBeanConfig {

    @Bean
    public AuditorAware<String> aware() {
        return new SpringSecurityAuditorAwarServiceImpl();
    }

}