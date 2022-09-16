package com.example.bank.filter;

import com.example.bank.dal.entity.CashBox;
import com.example.bank.dal.entity.CashTransfer;
import com.example.bank.dal.repo.CashTransferRepository;
import com.example.bank.model.CashTransferFilterModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class CashTransferSpecification {
    @Autowired
    private CashTransferRepository cashTransferRepository;

    public Specification<CashTransfer> getCashTransferBySpecification(CashTransferFilterModel request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getStartDate() != null && request.getEndDate() != null && request.getStartDate().length() > 0 && request.getEndDate().length() > 0) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

                try {
                    Date startDate = simpleDateFormat.parse(request.getStartDate());
                    Date endDate = simpleDateFormat.parse(request.getEndDate());
                    predicates.add(criteriaBuilder.between(
                            root.get("createdDate"), startDate, endDate
                    ));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }

            if (request.getCurrency() != null) {
                predicates.add(criteriaBuilder.equal(root.get("currency"), request.getCurrency()));
            }

            if (request.getSenderCashBoxName() != null && request.getSenderCashBoxName().length() > 0) {
                Join<CashTransfer, CashBox> cashBoxJoin = root.join("senderCashBox");
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(cashBoxJoin.get("name")), "%" + request.getSenderCashBoxName().toLowerCase() + "%"));
            }
            if (request.getRecipientCashBoxName() != null && request.getRecipientCashBoxName().length() > 0) {
                Join<CashTransfer, CashBox> cashBoxJoin = root.join("recipientCashBox");
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(cashBoxJoin.get("name")), "%" + request.getRecipientCashBoxName().toLowerCase() + "%"));
            }

            if (request.getTransferStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("transferStatus"), request.getTransferStatus()));
            }

            if (request.getSenderLastName() != null && request.getSenderLastName().length() > 0) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("senderLastname")), "%" + request.getSenderLastName().toLowerCase() + "%"));
            }

            if (request.getSenderFirstName() != null && request.getSenderFirstName().length() > 0) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("senderFirstname")), "%" + request.getSenderFirstName().toLowerCase() + "%"));
            }

            if (request.getSenderPatronymic() != null && request.getSenderPatronymic().length() > 0) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("senderPatronymic")), "%" + request.getSenderPatronymic().toLowerCase() + "%"));
            }

            if (request.getRecipientLastName() != null && request.getRecipientLastName().length() > 0) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("recipientLastname")), "%" + request.getRecipientLastName().toLowerCase() + "%"));
            }

            if (request.getRecipientFirstName() != null && request.getRecipientFirstName().length() > 0) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("recipientFirstname")), "%" + request.getRecipientFirstName().toLowerCase() + "%"));
            }

            if (request.getRecipientPatronymic() != null && request.getRecipientPatronymic().length() > 0) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("recipientPatronymic")), "%" + request.getRecipientPatronymic().toLowerCase() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
