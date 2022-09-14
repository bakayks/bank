package com.example.bank.cashBox;

import com.example.bank.utils.BigDecimalConverter;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cashboxes")
public class CashBox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "current_balance_USD")
    private BigDecimal currentBalanceUSD;

    @Column(name = "current_balance_EURO")
    private BigDecimal currentBalanceEURO;

    @Column(name = "current_balance_SOM")
    private BigDecimal currentBalanceSOM;
}
