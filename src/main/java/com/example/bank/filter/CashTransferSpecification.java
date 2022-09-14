package com.example.bank.filter;

import com.example.bank.Currency;
import com.example.bank.cashBox.CashBox;
import com.example.bank.enums.TransferStatus;
import com.example.bank.model.CashTransferFilterModel;
import com.example.bank.transfer.CashTransfer;
import com.example.bank.transfer.CashTransferRepository;
import com.example.bank.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Component
public class CashTransferSpecification {
    @Autowired
    private CashTransferRepository cashTransferRepository;

    public Specification<CashTransfer> getCashTransferBySpecification(CashTransferFilterModel request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getStartDate() != null && request.getEndDate() != null) {
                predicates.add(criteriaBuilder.between(
                        root.get("createdDate"), request.getStartDate(), request.getEndDate()
                ));
            }

            if (request.getCurrency() != null) {
                predicates.add(criteriaBuilder.equal(root.get("currency"), request.getCurrency()));
            }

            if (request.getSenderCashBoxName() != null) {
                Join<CashTransfer, CashBox> cashBoxJoin = root.join("senderCashBox");
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(cashBoxJoin.get("name")), "%" + request.getSenderCashBoxName().toLowerCase() + "%"));
            }
            if (request.getRecipientCashBoxName() != null) {
                Join<CashTransfer, CashBox> cashBoxJoin = root.join("recipientCashBox");
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(cashBoxJoin.get("name")), "%" + request.getRecipientCashBoxName().toLowerCase() + "%"));
            }

            if (request.getTransferStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("transferStatus"), request.getTransferStatus()));
            }

            if (request.getSenderLastName() != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("senderLastname")), "%" + request.getSenderLastName().toLowerCase() + "%"));
            }

            if (request.getSenderFirstName() != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("senderFirstname")), "%" + request.getSenderFirstName().toLowerCase() + "%"));
            }

            if (request.getSenderPatronymic() != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("senderPatronymic")), "%" + request.getSenderPatronymic().toLowerCase() + "%"));
            }

            if (request.getRecipientLastName() != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("recipientLastname")), "%" + request.getRecipientLastName().toLowerCase() + "%"));
            }

            if (request.getRecipientFirstName() != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("recipientFirstname")), "%" + request.getRecipientFirstName().toLowerCase() + "%"));
            }

            if (request.getRecipientPatronymic() != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("recipientPatronymic")), "%" + request.getRecipientPatronymic().toLowerCase() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
