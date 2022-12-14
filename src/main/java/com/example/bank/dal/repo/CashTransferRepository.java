package com.example.bank.dal.repo;

import com.example.bank.dal.entity.CashTransfer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CashTransferRepository extends JpaRepository<CashTransfer, Integer> {
    Optional<CashTransfer> getByUniqueCode(String uniqueCode);

    Page<CashTransfer> findAll(Specification<CashTransfer> specification, Pageable pageable);
}
