package com.fkoc.bankingmanagement.controller;

import com.fkoc.bankingmanagement.dto.AccountResponse;
import com.fkoc.bankingmanagement.entity.Account;
import com.fkoc.bankingmanagement.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/accounts")
public class AccountController {

    private final AccountService accountService;
    private final ModelMapper modelMapper;

    @GetMapping(value = "/{accountId}", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountResponse> getBalance(@PathVariable long accountId) {
        final Account account = accountService.getAccountById(accountId);
        final AccountResponse response = modelMapper.map(account, AccountResponse.class);
        return ResponseEntity.ok(response);
    }

}