package com.example.bank.dal.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "client")
public class Client extends Audit<String>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "firstname")
    @NotEmpty
    private String firstname;

    @Column(name = "lastname")
    @NotEmpty
    private String lastname;

    @Column(name = "patronymic")
    private String patronymic;

    @Column(name = "number")
    private String number;
}
