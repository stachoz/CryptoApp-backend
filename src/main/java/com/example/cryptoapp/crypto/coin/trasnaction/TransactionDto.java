package com.example.cryptoapp.crypto.coin.trasnaction;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransactionDto {
    private BigDecimal quantity;
    private BigDecimal price;
    private TransactionType type;
    private BigDecimal totalAmount;
    private BigDecimal roi;
    private String symbol;
}