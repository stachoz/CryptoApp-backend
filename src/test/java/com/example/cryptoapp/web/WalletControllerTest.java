package com.example.cryptoapp.web;

import com.example.cryptoapp.crypto.wallet.WalletService;
import com.example.cryptoapp.crypto.wallet.dto.TransactionDto;
import com.example.cryptoapp.exception.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {WalletController.class, GlobalExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private WalletService walletService;

    @Test
    public void shouldReturnOkOnGoodTransaction() throws Exception {
        String url = "/wallet/transactions";
        TransactionDto dto = new TransactionDto("BTC", new BigDecimal("1000"), new BigDecimal("0.5"), "BUY");
        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldHandleInvalidBody_EmptySymbol() throws Exception {
        String url = "/wallet/transactions";
        TransactionDto dto = new TransactionDto("", new BigDecimal("1000"), new BigDecimal("0.5"), "BUY");
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"-1", "-11111"})
    public void shouldHandleInvalidBody_InvalidPrice(String price) throws Exception {
        String url = "/wallet/transactions";
        TransactionDto dto = new TransactionDto("BTC", new BigDecimal(price), new BigDecimal("0.5"), "BUY");
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"-1", "-11111"})
    public void shouldHandleInvalidBody_InvalidQuantity(String quantity) throws Exception {
        String url = "/wallet/transactions";
        TransactionDto dto = new TransactionDto("BTC", new BigDecimal("123"), new BigDecimal(quantity), "BUY");
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }





}