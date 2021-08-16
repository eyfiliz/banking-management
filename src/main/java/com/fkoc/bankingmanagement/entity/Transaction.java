package com.fkoc.bankingmanagement.entity;

import static javax.persistence.EnumType.STRING;

import com.fkoc.bankingmanagement.enums.MessageType;
import com.fkoc.bankingmanagement.enums.Origin;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction implements Serializable {

    private static final long serialVersionUID = 6612772577940954465L;

    @Id
    @Column(name = "transaction_id")
    private String transactionId;

    @Enumerated(STRING)
    @Column(name = "message_type")
    private MessageType messageType;

    @Column(name = "account_id")
    private Long accountId;

    @Enumerated(STRING)
    @Column(name = "origin")
    private Origin origin;

    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "ref_transaction_id")
    private String refTransactionId;

}