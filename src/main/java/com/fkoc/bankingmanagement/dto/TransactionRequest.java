package com.fkoc.bankingmanagement.dto;

import com.fkoc.bankingmanagement.enums.MessageType;
import com.fkoc.bankingmanagement.enums.Origin;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {

    @NotNull
    private MessageType messageType;
    private String transactionId;
    @NotNull
    private Long accountId;
    @NotNull
    private Origin origin;
    @NotNull
    private BigDecimal amount;

}
