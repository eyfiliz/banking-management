package com.fkoc.bankingmanagement.controller;

import static com.fkoc.bankingmanagement.util.TestConstants.TRANSACTION_ID;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fkoc.bankingmanagement.dto.TransactionRequest;
import com.fkoc.bankingmanagement.dto.TransactionResponse;
import com.fkoc.bankingmanagement.entity.Transaction;
import com.fkoc.bankingmanagement.service.TransactionFacadeService;
import com.fkoc.bankingmanagement.util.JsonUtil;
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
public class TransactionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TransactionFacadeService transactionFacadeService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TransactionController controller;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void makePaymentApi() throws Exception {

        final TransactionRequest transactionRequest = ObjectBuilder.buildTransactionRequest();
        final Transaction transaction = ObjectBuilder.buildPayment(null);
        final Transaction processedTransaction = ObjectBuilder.buildPayment(TRANSACTION_ID);
        final TransactionResponse transactionResponse = ObjectBuilder.buildTransactionResponse();

        when(modelMapper.map(transactionRequest, Transaction.class)).thenReturn(transaction);
        when(transactionFacadeService.makePayment(transaction)).thenReturn(processedTransaction);
        when(modelMapper.map(processedTransaction, TransactionResponse.class))
                .thenReturn(transactionResponse);

        String URL = "/v1/transactions/payment";
        this.mockMvc
                .perform(MockMvcRequestBuilders.post(URL)
                        .content(JsonUtil.objectToJson(transactionRequest))
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

}
