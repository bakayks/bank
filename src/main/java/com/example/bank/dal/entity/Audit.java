package com.example.bank.dal.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Audit<U> {

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private U createdBy;

    @CreatedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "created_date", updatable = false)
    private Date creationDate;

    @LastModifiedBy
    @Column(name = "last_modified_by")
    private U lastModifiedBy;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Date lastModifiedDate;

    @Column(name = "deleted_date")
    private Date deletedDate;

    @Column(name = "deleted_by")
    private U deletedBy;

    private Boolean deleted = Boolean.FALSE;
}