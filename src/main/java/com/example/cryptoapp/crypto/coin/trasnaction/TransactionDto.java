package com.example.cryptoapp.crypto.coin.trasnaction;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class TransactionDto {
    private String symbol;
    private BigDecimal quantity;
    private BigDecimal price;
    private TransactionType type;
    private BigDecimal totalAmount;
    private BigDecimal roi;
    private Date date;
}