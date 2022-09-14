package com.example.bank.transfer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CashTransferRepository extends JpaRepository<CashTransfer, Integer> {
    Optional<CashTransfer> getByUniqueCode(String uniqueCode);
}
