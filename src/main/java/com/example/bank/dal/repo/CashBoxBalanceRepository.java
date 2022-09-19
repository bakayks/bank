package com.example.bank.dal.repo;

import com.example.bank.dal.entity.CashBox;
import com.example.bank.dal.entity.CashBoxBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CashBoxBalanceRepository extends JpaRepository<CashBoxBalance, Integer> {
}
