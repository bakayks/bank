package com.example.bank.user;


import com.example.bank.cashBox.CashBox;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.Nullable;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "usr")
public class User{

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
