package com.fkoc.bankingmanagement.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account implements Serializable {

    private static final long serialVersionUID = -5431505559895818549L;

    @Id
    @Column(name = "id")
    private Long id;

    @PositiveOrZero
    @Column(name = "balance", precision = 10, scale = 2)
    private BigDecimal balance;

}
