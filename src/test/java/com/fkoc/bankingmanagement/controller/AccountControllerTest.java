package com.fkoc.bankingmanagement.controller;

import static com.fkoc.bankingmanagement.util.TestConstants.ACCOUNT_ID;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fkoc.bankingmanagement.dto.AccountResponse;
import com.fkoc.bankingmanagement.entity.Account;
import com.fkoc.bankingmanagement.service.AccountService;
import com.fkoc.bankingmanagement.util.ObjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AccountService accountService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AccountController controller;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void getBalanceApi() throws Exception {

        final Account account = ObjectBuilder.buildAccount(ACCOUNT_ID);
        final AccountResponse accountResponse = ObjectBuilder.buildAccountResponse();
        when(accountService.getAccountById(ACCOUNT_ID)).thenReturn(account);
        when(modelMapper.map(account, AccountResponse.class)).thenReturn(accountResponse);

        String URL = "/v1/accounts/{accountId}";
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(URL, ACCOUNT_ID)
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

}