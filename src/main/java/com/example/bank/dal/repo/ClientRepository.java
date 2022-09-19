package com.example.bank.dal.repo;

import com.example.bank.dal.entity.CashBoxBalance;
import com.example.bank.dal.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
}
