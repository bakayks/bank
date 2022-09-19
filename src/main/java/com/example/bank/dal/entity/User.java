package com.example.bank.dal.entity;

import com.example.bank.dal.entity.Audit;
import com.example.bank.dal.entity.CashBox;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "usr")
public class User extends Audit<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    @NotEmpty(message = "Логин не должен быть пустым")
    private String username;

    @JsonIgnore
    @NotEmpty(message = "Пароль не должен быть пустым")
    private String password;

    @ManyToOne
    private CashBox cashBox;
}
