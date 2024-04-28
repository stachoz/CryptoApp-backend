package com.example.cryptoapp.web;

import com.example.cryptoapp.crypto.coin.trasnaction.AddTransactionDto;
import com.example.cryptoapp.crypto.coin.trasnaction.TransactionType;
import com.example.cryptoapp.crypto.WalletService;
import com.example.cryptoapp.exception.GlobalExceptionHandler;
import com.example.cryptoapp.exception.OperationConflictException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {WalletController.class, GlobalExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private WalletService walletService;
    @Autowired
    private ObjectMapper objectMapper;
    private String transactionsUrl = "/wallet/transactions";
    private AddTransactionDto addTransactionDto;

    @BeforeEach
    private void init(){
        addTransactionDto = new AddTransactionDto();
        addTransactionDto.setSymbol("BTC");
        addTransactionDto.setType(TransactionType.BUY);
        addTransactionDto.setPrice(new BigDecimal("2"));
        addTransactionDto.setQuantity(new BigDecimal("2"));
    }

    @Test
    public void shouldAddFirstBuyTransaction() throws Exception {
        mockMvc.perform(post(transactionsUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addTransactionDto)))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldHandleException_addFirstTransactionSell() throws Exception {
        addTransactionDto.setType(TransactionType.SELL);
        willThrow(new OperationConflictException("")).given(walletService).addTransaction(any());
        mockMvc.perform(post(transactionsUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addTransactionDto)))
                .andExpect(status().isConflict());
    }

}