package com.fkoc.bankingmanagement.controller;

import com.fkoc.bankingmanagement.dto.TransactionRequest;
import com.fkoc.bankingmanagement.dto.TransactionResponse;
import com.fkoc.bankingmanagement.entity.Transaction;
import com.fkoc.bankingmanagement.service.TransactionFacadeService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/transactions")
public class TransactionController {

    private final TransactionFacadeService transactionFacadeService;
    private final ModelMapper modelMapper;

    @PostMapping(value = "/payment", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionResponse> makePayment(
            @Valid @RequestBody TransactionRequest transactionRequest) {
        final Transaction transaction = modelMapper.map(transactionRequest, Transaction.class);
        final Transaction processedTransaction = transactionFacadeService.makePayment(transaction);
        final TransactionResponse response = modelMapper.map(processedTransaction, TransactionResponse.class);
        return ResponseEntity.ok(response);
    }

}
